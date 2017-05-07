// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.xml.ui;

import avtas.data.UnitMenu;
import avtas.swing.PopupMenuAdapter;
import avtas.swing.PopupSupport;
import avtas.xml.ui.XMLTree.AbstractXMLNode;
import avtas.xml.ui.XMLTree.XMLElementNode;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import avtas.xml.Element;
import avtas.xml.XmlNode;
import avtas.xml.XmlReader;
import avtas.xml.XmlWriter;
import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

;

/**
 *
 * @author AFRL/RQQD
 */
public class XMLComposer extends JPanel {

    XMLTree xmlTree;
    XMLEditPane textEditor;
    Element rootNode = new Element("root");
    boolean updating = false;
    JSplitPane updownPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    XMLValueEditor valEditor = new XMLValueEditor(null);
    public static String XML_UPDATED_PROP = "XML_UPDATED";
    boolean updateFromText = false;
    boolean updateFromTree = false;
    boolean edited = false;
    // variables used in self-contained editor
    File currentFile;

    /** Creates a new instance of XMLComposer */
    public XMLComposer() {
        initialize();
        setupListeners();
        //new TextMonitorThread().start();
    }

//    public void setXML(Document xml) {
//        this.rootNode = xml;
//        xmlTree.setRootNode(xml.getRootElement());
//        textEditor.setXML(xml);
//        edited = false;
//    }
    public void setXML(Element xml) {
        this.rootNode = xml;
        xmlTree.setRootNode(xml);
        textEditor.setXML(xml);
        edited = false;
    }

    public Element getXML() {
        return rootNode;
    }

    private void fireXMLChanged() {
        edited = true;
        firePropertyChange(XML_UPDATED_PROP, null, rootNode);
    }

    /** returns true if the XML has changed since the last call to setXML() */
    public boolean isEdited() {
        return edited || textEditor.edited();
    }

    public XMLEditPane getEditPane() {
        return textEditor;
    }

    public XMLTree getTree() {
        return xmlTree;
    }

    public void initialize() {

        setLayout(new BorderLayout());

        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        xmlTree = new XMLTree(rootNode);
        xmlTree.setEditable(true);

        sp.setLeftComponent(updownPane);
        updownPane.setTopComponent(new JScrollPane(xmlTree));
        updownPane.setBottomComponent(valEditor);


        textEditor = new XMLEditPane();
        textEditor.setPreferredSize(new Dimension(800, 600));
        sp.setRightComponent(new JScrollPane(textEditor));



        add(sp, BorderLayout.CENTER);
        sp.setDividerLocation(0.25);
        sp.setResizeWeight(0.2);
        updownPane.setResizeWeight(0.6);

    }

    void refreshText() {
        textEditor.setXML(rootNode);
        textEditor.setCaretPosition(0);
        TreePath p = xmlTree.getSelectionPath();
        if (p != null) {
            if (p.getLastPathComponent() instanceof XMLElementNode) {
                textEditor.goTo(((XMLElementNode) p.getLastPathComponent()).getXML());
            }
        }
        fireXMLChanged();
    }

    void setupListeners() {

        xmlTree.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                if (textEditor.edited()) {
                    updateFromText = true;
                    rootNode = textEditor.getUpdatedXML();
                    xmlTree.setRootNode(rootNode);
                    valEditor.setContent(null);
                    fireXMLChanged();
                    updateFromText = false;
                }
            }
        });


        xmlTree.model.addTreeModelListener(new TreeModelListener() {

            void update() {
                if (!updateFromText) {
                    updateFromTree = true;
                    refreshText();
                    updateFromTree = false;
                }
            }

            public void treeStructureChanged(TreeModelEvent e) {
                update();
            }

            public void treeNodesRemoved(TreeModelEvent e) {
                update();
            }

            public void treeNodesInserted(TreeModelEvent e) {
                update();
            }

            public void treeNodesChanged(TreeModelEvent e) {
                update();
            }
        });

        xmlTree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                if (e.getPath() != null) {
                    if (!updateFromText) {
                        if (e.getPath().getLastPathComponent() instanceof AbstractXMLNode) {
                            //textEditor.setXML(rootNode);
                            final AbstractXMLNode node = (AbstractXMLNode) e.getPath().getLastPathComponent();
                            XmlNode c = node.getXML();
                            valEditor.setContent(c);
                            textEditor.highlight(node.getXML());
                            textEditor.goTo(node.getXML());
                        }
                    }
                }
                else {
                    valEditor.setContent(null);
                }
            }
        });

        valEditor.addPropertyChangeListener(XMLValueEditor.NODE_UPDATED, new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (xmlTree.getSelectionPath().getLastPathComponent() instanceof AbstractXMLNode) {
                    refreshText();
                    textEditor.goTo(valEditor.getContent());
                    textEditor.highlight(valEditor.getContent());
                }
            }
        });
        
        PopupSupport popsupport = new PopupSupport(textEditor);
        popsupport.addPopupMenuAdapter(new PopupMenuAdapter() {

            @Override
            public void setMenuContents(JPopupMenu menu, final java.awt.Point p) {

                menu.add(new AbstractAction("Edit Element") {

                    public void actionPerformed(ActionEvent ae) {
                        XmlNode c = textEditor.getXML(textEditor.viewToModel(p));
                        if (c != null) {
                            TreePath p = xmlTree.getPath(c);
                            if (p != null) {
                                xmlTree.expandPath(p);
                                xmlTree.scrollPathToVisible(p);
                                xmlTree.setSelectionPath(p);
                            }
                        }
                    }
                });

//                menu.add(new JMenuItem(textEditor.formatAction));
//                menu.add(new JMenuItem(textEditor.checkAction));
//                menu.addSeparator();
//                menu.add(new JMenuItem(textEditor.copyAction));
//                menu.add(new JMenuItem(textEditor.cutAction));
//                menu.add(new JMenuItem(textEditor.pasteAction));

                // set up the unit converter
                String text = textEditor.getSelectedText();
                try {
                    double val = Double.parseDouble(text);
                    final UnitMenu unitMenu = new UnitMenu(val);
                    unitMenu.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            textEditor.replaceSelection(String.valueOf(unitMenu.getValue()));
                        }
                    });
                    menu.add(unitMenu);
                } catch (Exception ex) {
                }

            }
        });
    }

    /** Creates a self-contained editor frame with file operations */
    public static JFrame createComposerWindow(File file) {
        final JFrame f = new JFrame("XML Composer");
        final XMLComposer comp = new XMLComposer();
        try {
            f.setIconImage(ImageIO.read(comp.getClass().getResource("elementIcon.png")));
        } catch (IOException ex) {
            Logger.getLogger(XMLComposer.class.getName()).log(Level.SEVERE, null, ex);
        }
        f.add(comp);
        f.pack();
        
        if (file != null) {
            try {
                Element xmlEl = XmlReader.readDocument(file);
                comp.setXML(xmlEl);
                comp.currentFile = file;
            } catch (Exception ex) {
                Logger.getLogger(XMLComposer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        final JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);

        final Action saveAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (comp.getXML() != null) {
                    if (comp.currentFile != null) {
                        int ans = chooser.showSaveDialog(f);
                        if (ans == JFileChooser.APPROVE_OPTION) {
                            comp.currentFile = chooser.getSelectedFile();
                            f.setTitle("XML Composer " + comp.currentFile.getAbsolutePath());
                        }
                        else {
                            return;
                        }
                    }
                    XmlWriter.writeToFile(comp.currentFile, comp.getXML());
                }
            }
        };


        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new AbstractAction("Open") {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (comp.edited) {
                    int ans = JOptionPane.showConfirmDialog(f, "Save Current Work?");
                    if (ans == JOptionPane.YES_OPTION) {
                        saveAction.actionPerformed(e);
                    }
                }
                int ans = chooser.showOpenDialog(f);
                if (ans == JFileChooser.APPROVE_OPTION) {
                    try {
                        comp.setXML(XmlReader.readDocument(chooser.getSelectedFile()));
                        comp.currentFile = chooser.getSelectedFile();
                        f.setTitle("XML Composer " + comp.currentFile.getAbsolutePath());
                    } catch (Exception ex) {
                        Logger.getLogger(XMLComposer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
        fileMenu.add(new AbstractAction("Save") {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveAction.actionPerformed(e);
            }
        });
        
        fileMenu.add(new AbstractAction("Save As") {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (comp.getXML() != null) {
                    int ans = chooser.showSaveDialog(f);
                    if (ans == JFileChooser.APPROVE_OPTION) {
                        XmlWriter.writeToFile(chooser.getSelectedFile(), comp.getXML());
                        comp.currentFile = chooser.getSelectedFile();
                        f.setTitle("XML Composer " + comp.currentFile.getAbsolutePath());
                    }
                }
            }
        });

        JMenuBar menuBar = new JMenuBar();
        f.setJMenuBar(menuBar);
        f.getJMenuBar().add(fileMenu);
        f.getJMenuBar().setVisible(true);
        return f;
    }
    
    public static JFrame createComposerWindow() {
        return createComposerWindow(null);
    }

    public static void main(String[] args) {
        try {

            JFrame f = createComposerWindow();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.pack();
            f.setVisible(true);

        } catch (Exception ex) {
            Logger.getLogger(XMLComposer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    class TextMonitorThread extends Thread {
//
//        long timestep = 1000;
//
//        public TextMonitorThread() {
//        }
//
//        public void run() {
//            while (true) {
//                if (rootNode != null) {
//                    if (textEditor.edited()) {
//                        try {
//                            updateFromText = true;
//                            rootNode = textEditor.getUpdatedXML();
//                            xmlTree.setRootNode(rootNode);
//                            valEditor.setContent(null);
//                            fireXMLChanged();
//                            updateFromText = false;
//                        } catch (Exception ex) {
//                        }
//                    }
//                }
//                try {
//                    sleep(timestep);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(XMLComposer.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
//    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */