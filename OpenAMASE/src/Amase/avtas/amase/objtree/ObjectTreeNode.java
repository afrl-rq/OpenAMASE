// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.objtree;


import avtas.util.ObjectUtils;
import avtas.util.ReflectionUtils;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

/**
 * A collection of Node types for traversing objects. Changes to the nodes are
 * reflected in the objects they encapsulate.
 *
 * @author AFRL/RQQD
 */
public abstract class ObjectTreeNode extends DefaultMutableTreeNode {

    public Object value;
    public Class type = null;
    public String name = null;

    public Object getValue() {
        return value;
    }

    public Class getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static class ObjectNode extends ObjectTreeNode {


        public ObjectNode(String name, Object o, Class type) {
            this.type = type;
            this.name = name;
            setUserObject(o);
        }

        public ObjectNode(Object o) {
            if (o != null) {
                this.type = o.getClass();
                this.name = o.getClass().getSimpleName();
                setUserObject(o);
            }
        }

        @Override
        public void setUserObject(Object o) {
            if (name == null && o != null) {
                super.setUserObject(o.getClass().getSimpleName());
            }
            else if (o != null) {
                super.setUserObject(name + " (" + o.getClass().getSimpleName() + ")");
            }
            else {
                super.setUserObject(name + " = null");
            }
            this.value = o;

            if (getParent() instanceof ObjectNode && name != null) {
                ObjectNode pNode = (ObjectNode) getParent();
                if (pNode.value != null) {
                    ReflectionUtils.setFieldValue(pNode.value, name, o);
                }
            }

            removeAllChildren();
            if (this.value == null) {
                return;
            }
            try {
                for (Field f : ReflectionUtils.getAllFields(o.getClass())) {

                    f.setAccessible(true);
                    if ((f.getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
                        continue;
                    }
                    if (List.class.isAssignableFrom(f.getType())) {
                        add(new ListNode(f, (List) f.get(o)));
                    }
                    else if (f.getType().isArray()) {
                        add(new ArrayNode(f, f.get(o)));
                    }
                    else if (f.getType().isEnum()) {
                        add(new EnumNode(f.getName(), f.get(o), f.getType()));
                    }
                    else if (ObjectUtils.isPrimitive(f.getType())) {
                        add(new PrimitiveNode(f.getName(), f.get(o), f.getType()));
                    }
                    else if (Object.class.isAssignableFrom(f.getType())) {
                        add(new ObjectNode(f.getName(), f.get(o), f.getType()));
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace(); System.exit(1);
            }
        }
    }

    public static class ListNode extends ObjectTreeNode {

        public List<Object> list = null;

        public ListNode(Field f, List<Object> list) {
            setList(f, list);
        }

        public void setList(Field f, List<Object> list) {
            super.setUserObject(f.getName());
            this.name = f.getName();
            if (f.getGenericType() instanceof ParameterizedType) {
                Type listType = ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0];
                this.type = (Class) listType;
            }
            else {
                this.type = Object.class;
            }
            this.list = list;
            this.value = list;

            for (Object o : list) {
                if (ObjectUtils.isPrimitive(type)) {
                    add(new PrimitiveNode(type.getSimpleName(), o, type));
                }
                else if (type.isEnum()) {
                    add(new EnumNode(((Object) o).getClass().getSimpleName(), o, type));
                }
                else if (o == null) {
                    add(new NullNode(null, type));
                }
                else {
                    add(new ObjectNode(o.getClass().getSimpleName(), o, type));
                }
            }
        }

        @Override
        public void setUserObject(Object userObject) {
            // don't let editors modify this
        }

        public void addObject(Object o) {
            insert(o, getChildCount());
        }

        public List getList() {
            return list;
        }

        public void insert(Object o, int childIndex) {
            if (ObjectUtils.isPrimitive(type)) {
                insert(new PrimitiveNode(type.getSimpleName(), o, type), childIndex);
            }
            else if (type.isEnum()) {
                insert(new EnumNode(((Object) o).getClass().getSimpleName(), o, type), childIndex);
            }
            else if (o == null) {
                insert(new NullNode(null, type), childIndex);
            }
            else {
                insert(new ObjectNode(type.getSimpleName(), o, type), childIndex);
            }
            list.add(childIndex, o);
        }

        @Override
        public void remove(MutableTreeNode aChild) {
            int index = super.getIndex(aChild);
            list.remove(index);
            super.remove(aChild);
        }
    }

    public static class ArrayNode extends ObjectTreeNode {

        public Object array = null;

        public ArrayNode(Field f, Object array) {
            this.array = array;
            this.type = f.getType().getComponentType();
            this.name = f.getName();
            this.value = array;
            super.setUserObject(this.name);

            if (array != null) {
                for (int i = 0; i < Array.getLength(array); i++) {
                    Object o = Array.get(array, i);
                    if (ObjectUtils.isPrimitive(type)) {
                        add(new PrimitiveNode(type.getSimpleName(), o, type));
                    }
                    else if (type.isEnum()) {
                        add(new EnumNode(((Object) o).getClass().getSimpleName(), o, type));
                    }
                    else if (o == null) {
                        add(new NullNode(null, type));
                    }
                    else {
                        add(new ObjectNode(o.getClass().getSimpleName(), o, o.getClass()));
                    }
                }
            }

        }

        @Override
        public void setUserObject(Object userObject) {
        }
    }

    public static class PrimitiveNode extends ObjectTreeNode {

        public PrimitiveNode(String name, Object value, Class type) {
            this.name = name;
            this.value = value;
            this.type = type;
            setAllowsChildren(false);
        }

        @Override
        public void setParent(MutableTreeNode newParent) {
            super.setParent(newParent);
            setUserObject(value);
        }

        @Override
        public void setUserObject(Object val) {
            if (val == null) {
                return;
            }
            if (val instanceof String) {
                this.value = ObjectUtils.getValueOf((String) val, type);
            }
            else if (type.isAssignableFrom(val.getClass())) {
                this.value = val;
            }
            if (parent instanceof ObjectNode) {
                super.setUserObject(name + " = " + this.value);
                try {
                    Object parentObj = ((ObjectNode) parent).getValue();
                    Field f = ReflectionUtils.getField(parentObj.getClass(), name);
                    f.set(parentObj, this.value);
                } catch (Exception ex) {
                }
            }
            else if (parent instanceof ListNode) {
                super.setUserObject(this.value);
                List parentList = ((ListNode) parent).getList();
                int index = parent.getIndex(this);
                if (index != -1) {
                    parentList.set(index, this.value);
                }
            }
            else if (parent instanceof ArrayNode) {
                super.setUserObject(this.value);
                Object array = ((ArrayNode) parent).array;
                int index = parent.getIndex(this);
                if (index != -1) {
                    Array.set(array, index, this.value);
                }
            }
        }
    }

    public static class EnumNode extends PrimitiveNode {

        public EnumNode(String name, Object value, Class type) {
            super(name, value, type);
        }
    }

    public static class NullNode extends ObjectNode {

        public NullNode(String name, Class type) {
            super(name, null, type);
        }

        @Override
        public void setUserObject(Object o) {
        }

        @Override
        public Object getUserObject() {
            return "null";
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */