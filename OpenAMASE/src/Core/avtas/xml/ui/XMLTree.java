// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.xml.ui;

import avtas.swing.PopupMenuAdapter;
import avtas.xml.Comment;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import avtas.xml.Element;
import avtas.xml.XmlNode;
import avtas.xml.XmlReader;
import avtas.xml.XmlWriter;
import javax.swing.AbstractAction;

/**
 *
 * @author AFRL/RQQD
 */
public class XMLTree extends JTree {

    DefaultTreeModel model = null;
    AbstractXMLNode copyNode = null;

    /**
     * Creates a new instance of XMLTree
     */
    public XMLTree() {
        this(new Element("root"), true);
        setEditable(true);
    }

    public XMLTree(Element element, boolean editable) {
        this(element);
        setEditable(editable);
        if (editable) {
            setPopupMenus();
            setupDragAndDrop();
        }
    }

    public XMLTree(Element rootNode) {


        model = new DefaultTreeModel(new XMLElementNode(rootNode)) {
            @Override
            public void reload(TreeNode node) {
                Enumeration<TreePath> expandedPaths = getExpandedDescendants(new TreePath(getPathToRoot(node)));
                super.reload(node);
                expandOldNodes(expandedPaths);
            }

            @Override
            public void setRoot(TreeNode node) {
                Enumeration<TreePath> expandedPaths = getExpandedDescendants(new TreePath(getRoot()));
                super.setRoot(node);
                expandOldNodes(expandedPaths);
            }
        };

        setModel(model);

        setCellRenderer(new XmlTreeRenderer());


    }

    public void setRootNode(Element node) {
        //Enumeration<TreePath> expandedPaths = getExpandedDescendants(new TreePath(model.getRoot()));
        model.setRoot(new XMLElementNode(node));
        //expandOldNodes(expandedPaths);

    }

    public Element getRootNode() {
        return ((XMLElementNode) model.getRoot()).getXML();
    }

    public Element getNode(TreePath path) {
        if (path == null) {
            return null;
        }
        if (!(path.getLastPathComponent() instanceof XMLElementNode)) {
            return null;
        }
        return ((XMLElementNode) path.getLastPathComponent()).getXML();
    }

    /**
     * Returns a tree path to the given element, or null if no path exists
     */
    public TreePath getPath(XmlNode c) {
        return getPath(c, (XMLElementNode) model.getRoot());
    }

    private TreePath getPath(XmlNode c, XMLElementNode currentNode) {
        for (int i = 0; i < currentNode.getChildCount(); i++) {
            if (currentNode.getChildAt(i) instanceof XMLElementNode) {
                XMLElementNode n = (XMLElementNode) currentNode.getChildAt(i);
                if (n.getXML().equals(c)) {
                    return new TreePath(n.getPath());
                }
                TreePath p = getPath(c, n);
                if (p != null) {
                    return p;
                }
            }
        }
        return null;
    }

    public void setNode(TreePath path, Element node) {
        if (path == null || !(path.getLastPathComponent() instanceof XMLElementNode)) {
            return;
        }
        XMLElementNode treeNode = (XMLElementNode) path.getLastPathComponent();
        treeNode.setXML(node);
        model.reload(treeNode);
    }

    public XmlNode copy(TreePath path) {
        AbstractXMLNode treeNode = (AbstractXMLNode) path.getLastPathComponent();
        if (path == null || treeNode == null) {
            return null;
        }
        return (XmlNode) treeNode.getXML().clone();
    }

    public void paste(TreePath path, Element node) {
        AbstractXMLNode treeNode = (AbstractXMLNode) path.getLastPathComponent();
        if (path == null || treeNode == null || node == null) {
            return;
        }
        if (treeNode instanceof XMLElementNode) {
            XMLElementNode elNode = (XMLElementNode) treeNode;
            if (!elNode.getXML().getText().isEmpty()) {
                return;
            }

            elNode.getXML().add((XmlNode) node.clone());
            elNode.refreshNode();
            model.reload(treeNode);
        }
    }

    public XmlNode remove(TreePath path) {
        AbstractXMLNode node = ((AbstractXMLNode) path.getLastPathComponent());
        if (path == null || node == null || node.getParent() == null) {
            return null;
        }
        XMLElementNode parent = (XMLElementNode) node.getParent();
        parent.getXML().remove(node.getXML());
        parent.refreshNode();
        model.reload(parent);
        return node.getXML();
    }

    protected void setPopupMenus() {

        addMouseListener(new PopupMenuAdapter() {
            public void setMenuContents(JPopupMenu menu, java.awt.Point p) {
                final TreePath path = getPathForLocation(p.x, p.y);
                if (path == null) {
                    return;
                }
                setSelectionPath(path);

                menu.add(new AbstractAction("Refresh") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        model.reload((TreeNode) path.getLastPathComponent());
                    }
                });

                menu.add(new AbstractAction("Expand") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        expandPath(path);
                    }
                });

                menu.add(new AbstractAction("Collapse") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        collapsePath(path);
                    }
                });

                menu.add(new AbstractAction("Copy") {
                    public void actionPerformed(ActionEvent e) {
                        AbstractXMLNode node = (AbstractXMLNode) path.getLastPathComponent();
                        copyNode = node;
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        if (node instanceof XMLElementNode) {
                            clipboard.setContents(new StringSelection(((Element) node.getXML()).toXML()), null);
                        } else if (node instanceof XMLCommentNode) {
                            clipboard.setContents(new StringSelection(((Comment) node.getXML()).getComment()), null);
                        }

                    }
                });

                menu.add(new AbstractAction("Cut") {
                    public void actionPerformed(ActionEvent e) {
                        AbstractXMLNode node = (AbstractXMLNode) path.getLastPathComponent();
                        copyNode = node;
                        AbstractXMLNode parent = (AbstractXMLNode) node.getParent();
                        if (parent instanceof XMLElementNode) {
                            ((XMLElementNode) parent).getXML().remove(node.getXML());
                            ((XMLElementNode) parent).refreshNode();
                            model.reload(parent);
                        }
                    }
                });

                if (copyNode != null) {
                    menu.add(new AbstractAction("Paste") {
                        public void actionPerformed(ActionEvent e) {
                            AbstractXMLNode node = (AbstractXMLNode) path.getLastPathComponent();
                            if (node instanceof XMLElementNode) {
                                ((XMLElementNode) node).getXML().add((XmlNode) copyNode.getXML().clone());
                                ((XMLElementNode) node).refreshNode();
                                model.reload(node);
                            }
                        }
                    });
                }

                menu.add(new AbstractAction("Add Element") {
                    public void actionPerformed(ActionEvent e) {
                        if (path.getLastPathComponent() instanceof XMLElementNode) {
                            XMLElementNode node = (XMLElementNode) path.getLastPathComponent();
                            node.getXML().add(new Element("Blank"));
                            node.refreshNode();
                            model.reload(node);
                        }
                    }
                });

                menu.add(new AbstractAction("Add Comment") {
                    public void actionPerformed(ActionEvent e) {
                        if (path.getLastPathComponent() instanceof XMLElementNode) {
                            XMLElementNode node = (XMLElementNode) path.getLastPathComponent();
                            node.getXML().add(new Comment("New Comment"));
                            node.refreshNode();
                            model.reload(node);
                        }
                    }
                });

                menu.add(new AbstractAction("Remove") {
                    public void actionPerformed(ActionEvent e) {
                        AbstractXMLNode node = (AbstractXMLNode) path.getLastPathComponent();
                        AbstractXMLNode parent = (AbstractXMLNode) node.getParent();
                        if (parent instanceof XMLElementNode) {
                            ((XMLElementNode) parent).getXML().remove(node.getXML());
                            ((XMLElementNode) parent).refreshNode();
                            model.reload(parent);
                        }
                    }
                });


                menu.add(new AbstractAction("Move Up") {
                    public void actionPerformed(ActionEvent e) {
                        AbstractXMLNode node = (AbstractXMLNode) path.getLastPathComponent();
                        XMLElementNode parent = (XMLElementNode) node.getParent();
                        Element el = (Element) parent.getXML();
                        int index = el.indexOf(node.getXML());
                        if (index > 0) {
                            el.remove(index);
                            el.add(index - 1, node.getXML());

                            parent.refreshNode();
                            model.reload(parent);
                        }
                    }
                });

                menu.add(new AbstractAction("Move Down") {
                    public void actionPerformed(ActionEvent e) {
                        AbstractXMLNode node = (AbstractXMLNode) path.getLastPathComponent();
                        XMLElementNode parent = (XMLElementNode) node.getParent();
                        if (parent != null) {
                            Element el = (Element) parent.getXML();
                            int index = el.indexOf(node.getXML());
                            if (index < parent.getChildCount() - 1) {
                                el.remove(index);
                                el.add(index + 1, node.getXML());
                            }

                            parent.refreshNode();
                            model.reload(parent);
                        }
                    }
                });

            }
        });

    }

    private void setupDragAndDrop() {
        setDragEnabled(true);
        setDropMode(DropMode.ON_OR_INSERT);
        setTransferHandler(new TransferHandler() {
            TreePath[] dragPaths = null;

            @Override
            public int getSourceActions(JComponent c) {
                return COPY_OR_MOVE;
            }

            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.stringFlavor) || dragPaths != null;
            }

            @Override
            public boolean importData(TransferSupport support) {
                if (!canImport(support)) {
                    return false;
                }
                JTree.DropLocation loc = (JTree.DropLocation) support.getDropLocation();
                TreePath dropPath = loc.getPath();

                if (dropPath == null) {
                    return false;
                }

                if (!(dropPath.getLastPathComponent() instanceof XMLElementNode)) {
                    return false;
                }

                XMLElementNode newParent = (XMLElementNode) dropPath.getLastPathComponent();

                if (dragPaths != null) {
                    ArrayList<XmlNode> XmlNodeList = new ArrayList<XmlNode>();
                    for (TreePath p : dragPaths) {
                        if (p.getLastPathComponent() instanceof AbstractXMLNode) {
                            AbstractXMLNode n = (AbstractXMLNode) p.getLastPathComponent();
                            XmlNodeList.add(0, n.getXML());
                            if (support.getDropAction() == MOVE && n.getXML().getParent() != null) {
                                n.getXML().getParent().remove(n.getXML());
                                if (n.getParent() instanceof XMLElementNode) {
                                    ((XMLElementNode) n.getParent()).refreshNode();
                                }
                            }
                        }
                    }
                    for (XmlNode XmlNode : XmlNodeList) {
                        if (loc.getChildIndex() != -1 && loc.getChildIndex() < newParent.getXML().getChildCount()) {
                            newParent.getXML().add(loc.getChildIndex(), (XmlNode) XmlNode.clone());
                        } else {
                            newParent.getXML().add((XmlNode) XmlNode.clone());
                        }
                    }
                    dragPaths = null;
                } else if (support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    try {
                        Element el = XmlReader.readDocument((String) support.getTransferable().getTransferData(DataFlavor.stringFlavor));
                        if (loc.getChildIndex() != -1) {
                            newParent.getXML().add(loc.getChildIndex(), el);
                        } else {
                            newParent.getXML().add(el);
                        }
                    } catch (Exception ex) {
                        return true;
                    }
                }
                newParent.refreshNode();
                model.reload();
                return true;
            }

            @Override
            public void exportAsDrag(JComponent comp, InputEvent e, int action) {
                dragPaths = getSelectionPaths();
                if (dragPaths != null) {
                    super.exportAsDrag(comp, e, action);
                }
            }

            @Override
            protected Transferable createTransferable(JComponent c) {
                TreePath p = getSelectionPath();
                if (p == null) {
                    return null;
                }
                if (p.getLastPathComponent() instanceof AbstractXMLNode) {
                    String s = XmlWriter.toCompactString(((AbstractXMLNode) p.getLastPathComponent()).getXML());
                    System.out.println("export " + s);
                    return new StringSelection(s);
                }
                return null;
            }
        });

    }

    void expandOldNodes(Enumeration<TreePath> en) {
        if (en == null) {
            return;
        }

        XMLElementNode root = (XMLElementNode) model.getRoot();

        while (en.hasMoreElements()) {
            TreePath oldPath = en.nextElement();
            AbstractXMLNode newNode = root;
            for (int i = 1; i < oldPath.getPathCount(); i++) {
                AbstractXMLNode oldNode = (AbstractXMLNode) oldPath.getPathComponent(i);
                for (int j = 0; j < newNode.getChildCount(); j++) {
                    AbstractXMLNode n = (AbstractXMLNode) newNode.getChildAt(j);
                    if (n.getUserObject().equals(oldNode.getUserObject())) {
                        if (n.getParent() != null && oldNode.getParent() != null) {
                            if (n.getParent().getIndex(n) == oldNode.getParent().getIndex(oldNode)) {
                                newNode = n;
                            }
                        }
                    }
                }
            }
            if (newNode != root) {
                TreePath exPath = new TreePath(model.getPathToRoot(newNode));
                expandPath(exPath);
            }
        }
    }

    public static abstract class AbstractXMLNode extends DefaultMutableTreeNode {

        public abstract XmlNode getXML();

        public abstract void setXML(XmlNode c);
    }

    public static class XMLElementNode extends AbstractXMLNode {

        Element node;

        public XMLElementNode(Element node) {
            setXML(node);
            setUserObject(node.getName());
        }

        @Override
        public void setUserObject(Object userObject) {
            super.setUserObject(userObject);
            node.setName(String.valueOf(userObject));
        }

        @Override
        public Object getUserObject() {
            return node.getName();
        }

        public Element getXML() {
            return node;
        }

        public void setXML(XmlNode c) {
            removeAllChildren();
            if (!(c instanceof Element)) {
                return;
            }
            this.node = (Element) c;
            setUserObject(node.getName());
            for (Object child : node.getChildren()) {
                if (child instanceof Element) {
                    XMLElementNode n = new XMLElementNode((Element) child);
                    add(n);
                }
                if (child instanceof Comment) {
                    XMLCommentNode n = new XMLCommentNode((Comment) child);
                    add(n);
                }
            }
        }

        public void refreshNode() {
            setXML(node);
        }
    }

    public static class XMLCommentNode extends AbstractXMLNode {

        Comment comment = null;

        public XMLCommentNode(Comment comment) {
            this.comment = comment;
            setUserObject(comment.getComment());
        }

        @Override
        public void setUserObject(Object userObject) {
            this.comment.setComment(String.valueOf(userObject));
            super.setUserObject(comment.getComment());
        }

        @Override
        public Object getUserObject() {
            return comment.getComment();
        }

        @Override
        public XmlNode getXML() {
            return comment;
        }

        @Override
        public void setXML(XmlNode c) {
            if (c instanceof Comment) {
                this.comment = (Comment) c;
            }
        }
    }

    static class XmlTreeRenderer extends DefaultTreeCellRenderer {

        final ImageIcon elIcon = new ImageIcon(getClass().getResource("elementIcon.png"));
        final ImageIcon commentIcon = new ImageIcon(getClass().getResource("commentIcon.png"));

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (value instanceof XMLElementNode) {
                Element el = ((XMLElementNode) value).getXML();
                label.setIcon(elIcon);
                if (el.getText() != null && !el.getText().isEmpty()) {
                    String txt = el.getText().trim();
                    if (txt.length() > 8) {
                        txt = txt.substring(0, 6) + "...";
                    }
                    label.setText("<html>" + el.getName() + "<font color=\"gray\"> = " + txt + "</font></html>");
                } else {
                    label.setText(el.getName());
                }
            }
            if (value instanceof XMLCommentNode) {
                label.setIcon(commentIcon);
                Comment comment = (Comment) ((XMLCommentNode) value).getXML();
                String txt = comment.getComment();
                if (txt.length() > 8) {
                    txt = txt.substring(0, 6) + "...";
                }
                label.setText(txt);
            }
            return label;
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */