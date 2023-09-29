// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.graphics;

import avtas.util.ObjectUtils;
import avtas.map.Proj;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MapGraphicsList<T extends MapGraphic> extends MapGraphic implements Iterable<T> {

    protected final List<T> internalList = new ArrayList<T>();

    /** Creates a new instance of GraphicList */
    public MapGraphicsList() {
    }

    public MapGraphicsList(Collection<T> anotherList) {
        addAll(anotherList);
    }

    public synchronized void paint(Graphics2D g) {

        if (!isProjected() || !isVisible()) {
            return;
        }

        for (int i = 0; i < internalList.size(); i++) {
            T gg = internalList.get(i);
            if (gg != null && gg.isProjected()) {
                Graphics2D tmp = (Graphics2D) g.create();
                gg.paint((Graphics2D) tmp);
                tmp.dispose();
            }
        }
    }

    public synchronized void project(Proj proj) {

        setProjected(false);
        setScreenShape(null);
        if (!isVisible()) {
            return;
        }

        for (T g : internalList) {
            if (g == null) {
                continue;
            }
            g.project(proj);
            //setProjected(g.isProjected() || isProjected());
        }
        setProjected(true);
    }

    @Override
    public Rectangle getBounds() {
        Rectangle r = null;
        for (T o : this) {
            if (!o.isVisible() || !o.isProjected()) {
                continue;
            }
            Rectangle bounds = o.getBounds();
            if (bounds != null && !bounds.isEmpty()) {
                if (r == null) {
                    r = bounds;
                }
                else {
                    Rectangle.union(r, bounds, r);
                }
            }
        }
        return r;
    }

    @Override
    public void setFill(Paint fill) {
        for (int i = 0; i < internalList.size(); i++) {
            internalList.get(i).setFill(fill);
        }
        super.setFill(fill);
    }

    @Override
    public void setPainter(Painter painter) {
        for (int i = 0; i < internalList.size(); i++) {
            internalList.get(i).setPainter(painter);
        }
        super.setPainter(painter);
    }

    /**
     * Returns all of the graphics that intersect the given screen location in the
     * current projection.
     * @param x screen x-coordinate
     * @param y screen y-coordinate
     * @param numPix tolerance (number of pixels to form a square bounds around x,y)
     * @return all of the graphics that intersect the given point
     */
    public List<T> getGraphicsWithin(double x, double y, int numPix) {
        if (numPix <= 0) {
            numPix = 1;
        }
        List<T> retList = new ArrayList<T>();
        Rectangle rect = new Rectangle((int) (x - numPix / 2f), (int) (y - numPix / 2f), numPix, numPix);
        for (T g : this) {
            //Rectangle s = g.getBounds();
            if (g.intersects(rect)) {
                retList.add(g);
            }
        }
        return retList;
    }
    
    /**
     * Performs the same operation as getGraphicsWithin(), but expands any lists 
     * encountered.  The final list includes graphics that are within graphics lists
     * that pass the boundary test.
     */
    public List<MapGraphic> getAllGraphicsWithin(double x, double y, int numPix) {
        List<T> retList = getGraphicsWithin(x, y, numPix);
        int size = retList.size();
        for (int i=0; i<size; i++) {
            MapGraphic g = retList.get(i);
            if (g instanceof MapGraphicsList) {
                retList.addAll( ((MapGraphicsList) g).getAllGraphicsWithin(x, y, numPix) );
            }
        }
        return (List<MapGraphic>) retList;
    }

    /** returns the graphic that has the same reference object as the one requested.
     * 
     * @param obj the reference object to search by
     * @return the first graphic that has the reference object, or null if none is found.
     */
    public T getByRefObject(Object obj) {
        synchronized (internalList) {
            for (T g : this) {
                if (ObjectUtils.equals(obj, g.getRefObject())) {
                    return g;
                }
            }
        }
        return null;
    }

    @Override
    public void setSelected(boolean selected) {
        synchronized (internalList) {
            for (T g : this) {
                g.setSelected(selected);
            }
        }
        super.setSelected(selected);
    }

    @Override
    public boolean isSelected() {
        return super.isSelected(); 
    }
    
    
    
    

    @Override
    public boolean intersects(Rectangle2D otherShape) {
        for (T g : this) {
            if (g.intersects(otherShape)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onEdge(int screenX, int screenY, int maxDist) {
        for (T g : this) {
            if (g.onEdge(screenX, screenY, maxDist)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Point2D point) {
        for (T g : this) {
            if (g.contains(point)) {
                return true;
            }
        }
        return false;
    }
    
    
    

    // Methods from List Interface below
    public boolean add(T e) {
        synchronized (internalList) {
            if (e == null) {
                return false;
            }
            e.setParent(this);
            return internalList.add(e);
        }
    }

    public boolean remove(Object o) {
        return internalList.remove(o);
    }

    public void clear() {
        internalList.clear();
    }

    public T get(int index) {
        return internalList.get(index);
    }

    public T set(int index, T graphic) {
        if (graphic == null) {
            return null;
        }
        graphic.setParent(this);
        return internalList.set(index, graphic);
    }

    public void add(int index, T graphic) {
        if (graphic != null) {
            graphic.setParent(this);
            internalList.add(index, graphic);
        }
    }

    public Object[] toArray() {
        return internalList.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return internalList.toArray(a);
    }

    public boolean containsAll(Collection<?> c) {
        return internalList.containsAll(c);
    }

    public boolean addAll(Collection<? extends T> c) {
        for (T g : c) {
            g.setParent(this);
        }
        return internalList.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        for (T g : c) {
            g.setParent(this);
        }
        return internalList.addAll(index, c);
    }

    public boolean isEmpty() {
        return internalList.isEmpty();
    }

    public boolean contains(Object o) {
        return internalList.contains(o);
    }

    public int indexOf(Object o) {
        return internalList.indexOf(o);
    }

    public Iterator<T> iterator() {
        return internalList.iterator();
    }

    public boolean removeAll(Collection<?> c) {
        for (Object g : c) {
            if (g instanceof MapGraphic) {
                if (internalList.contains(g)) {
                    ((MapGraphic) g).setParent(null);
                }
            }
        }
        return internalList.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        for (T g : internalList) {
            if (!c.contains(g)) {
                g.setParent(null);
            }
        }
        return internalList.retainAll(c);
    }

    public T remove(int index) {
        T g = internalList.remove(index);
        if (g != null) {
            g.setParent(null);
        }
        return g;
    }

    public int lastIndexOf(Object o) {
        return internalList.lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return new GraphicIterator(0);
    }

    public ListIterator<T> listIterator(int index) {
        return new GraphicIterator(index);
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return internalList.subList(fromIndex, toIndex);
    }

    public int size() {
        return internalList.size();
    }

    /** An iterator that allows interrogation of items before add/remove operations on the internalList. */
    class GraphicIterator implements ListIterator<T> {

        int index = 0;
        ListIterator<T> itr;

        public GraphicIterator(int index) {
            this.index = index;
            itr = internalList.listIterator(0);
        }

        @Override
        public boolean hasNext() {
            return itr.hasNext();
        }

        @Override
        public T next() {
            return itr.next();
        }

        @Override
        public boolean hasPrevious() {
            return itr.hasPrevious();
        }

        @Override
        public T previous() {
            return itr.previous();
        }

        @Override
        public int nextIndex() {
            return itr.nextIndex();
        }

        @Override
        public int previousIndex() {
            return itr.previousIndex();
        }

        @Override
        public void remove() {
            int prevIndex = previousIndex();
            if (prevIndex != -1) {
                get(prevIndex).setParent(null);
            }
            itr.remove();
        }

        @Override
        public void set(T e) {
            e.setParent(MapGraphicsList.this);
            itr.set(e);
        }

        @Override
        public void add(T e) {
            e.setParent(MapGraphicsList.this);
            itr.add(e);
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */