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
import avtas.swing.FindTool;
import avtas.swing.PopupMenuAdapter;
import avtas.swing.PopupSupport;
import avtas.xml.Comment;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.undo.UndoManager;
import avtas.xml.Element;
import avtas.xml.XmlNode;
import avtas.xml.XmlReader;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.stream.XMLStreamException;
import org.jdesktop.swingx.JXHyperlink;

/**
 *
 * @author AFRL/RQQD
 */
public class XMLEditPane extends JEditorPane {

    ArrayList<ContentBounds> contentList = new ArrayList<ContentBounds>();
    private static final String LEVEL_WHITESPACE = "    ";
    public static final String XML_SECTION_CHANGED = "XML_SECTION_CHANGED";
    Object xmlSelectionHighlight = null;
    Element xmlDoc = null;
    //SAXBuilder builder = new SAXBuilder();
    boolean edited = false;
    UndoManager undoManager = new UndoManager();
    
    public static DefaultHighlighter.DefaultHighlightPainter HIGHLIGHT_PAINTER =
                            new DefaultHighlighter.DefaultHighlightPainter(new Color(255, 255, 0, 100));

    public XMLEditPane() {
        this.setEditorKitForContentType("text/xml", new XmlEditorKit());
        this.setContentType("text/xml");
        setFont(Font.decode(Font.MONOSPACED));
        setBackground(Color.WHITE);
        //builder.setIgnoringBoundaryWhitespace(true);
        //builder.setIgnoringElementContentWhitespace(true);

        setupPopupMenu();
        getDocument().addUndoableEditListener(undoManager);

        registerKeyboardAction(undoAction, KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_FOCUSED);
        registerKeyboardAction(redoAction, KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_FOCUSED);
        registerKeyboardAction(findAction, KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_FOCUSED);

        // add logic to the document listener to allow for behaviors due to editing
        getDocument().getDefaultRootElement().getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                highlight(null);
                edited = true;
            }

            public void removeUpdate(DocumentEvent e) {
                highlight(null);
                edited = true;
            }

            public void changedUpdate(DocumentEvent e) {
                highlight(null);
                edited = true;
            }
        });
    }

    public void setXML(Element el) {
        contentList.clear();
        StringBuffer buf = new StringBuffer();
        appendContent((Element) el, buf, 0);
        setText(buf.toString());
        this.xmlDoc = el;
        edited = false;
    }

//    public void setXML(Document doc) {
//        contentList.clear();
//        StringBuffer buf = new StringBuffer();
//        for (Object o : doc.getContent()) {
//            if (o instanceof Element) {
//                appendContent((Element) o, buf, 0);
//            } else {
//                buf.append(getContentString((Content) o, ""));
//            }
//        }
//        setText(buf.toString());
//        this.xmlDoc = doc;
//        edited = false;
//    }

    public void highlight(XmlNode xml) {
        if (xmlSelectionHighlight != null) {
            getHighlighter().removeHighlight(xmlSelectionHighlight);
        }
        if (xml == null) {
            return;
        }
        for (ContentBounds b : contentList) {
            if (b.content.equals(xml)) {
                try {
                    xmlSelectionHighlight = getHighlighter().addHighlight(b.start, b.end, HIGHLIGHT_PAINTER);
                    return;
                } catch (BadLocationException ex) {
                }
            }
        }
    }

    public void select(XmlNode xml) {
        if (xml == null) {
            return;
        }
        for (ContentBounds b : contentList) {
            if (b.content.equals(xml)) {
                try {
                    setSelectionStart(b.start);
                    setSelectionEnd(b.end);
                    scrollRectToVisible(modelToView(b.start));
                    return;
                } catch (BadLocationException ex) {
                    Logger.getLogger(XMLEditPane.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void formatXML() {
        int loc = getCaretPosition();
        setXML(getUpdatedXML());
        setCaretPosition(loc);
    }

    public void goTo(XmlNode xml) {
        for (ContentBounds b : contentList) {
            if (b.content.equals(xml)) {
                try {
                    setCaretPosition(getText().length());
                    setCaretPosition(b.start);
                    Rectangle r = modelToView(b.start);
                    scrollRectToVisible(r);
                    return;
                } catch (BadLocationException ex) {
                    Logger.getLogger(XMLEditPane.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public XmlNode getXML(int loc) {
        for (int i = 1; i < contentList.size(); i++) {
            if (contentList.get(i).start > loc) {
                for (int j = i - 1; j > 0; j--) {
                    if (contentList.get(j).end >= loc) {
                        return contentList.get(j).content;
                    }
                }
            }
        }
        return null;
    }

    public Element getUpdatedXML() {
        if (checkXML()) {
            edited = false;
            int pos = getCaretPosition();
            setXML(xmlDoc);
            setCaretPosition(pos);
            return xmlDoc;
        }
        return null;
    }

    /** returns true if the content of the text editor has changed since the last call to setXML() or
     *  getUpdatedXML().
     */
    public boolean edited() {
        return edited;
    }

    public boolean checkXML() {
        try {
            xmlDoc = XmlReader.readDocument(new StringReader(getText()));
        } catch (XMLStreamException ex) {
            final int charLoc = ex.getLocation().getCharacterOffset();
            
            JXHyperlink link = new JXHyperlink(new AbstractAction("Go to Error") {

                @Override
                public void actionPerformed(ActionEvent e) {
                    setSelectionStart(charLoc);
                    setSelectionEnd(charLoc+1);
                    try {
                        scrollRectToVisible(modelToView(charLoc));
                        getHighlighter().addHighlight(charLoc, charLoc+1, HIGHLIGHT_PAINTER);
                    } catch (BadLocationException ex1) {
                        Logger.getLogger(XMLEditPane.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            });
            
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel(ex.getLocalizedMessage()));
            panel.add(link);

            JOptionPane.showMessageDialog(getParent(), panel, "XML Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    

    private void appendContent(Element el, StringBuffer buf, int level) {
        String ws = "";
        for (int i = 0; i < level; i++) {
            ws += LEVEL_WHITESPACE;
        }

        ContentBounds bounds = new ContentBounds(el);
        contentList.add(bounds);
        bounds.start = buf.length();

        buf.append(ws).append("<").append(el.getName());
        for (int i=0; i<el.getAttributeCount(); i++) {
            buf.append(" ").append(el.getAttributeName(i)).append("=\"").append(el.getAttributeValue(i)).append("\"");
        }
        if (!el.hasChildren() && el.getText().isEmpty()) {
            buf.append("/>");
            bounds.end = buf.length();
        } else {
            buf.append(">");

            boolean textonly = true;
            buf.append(el.getText());
            
            for (Object o : el.getChildren()) {
                if (o instanceof Comment) {
                    ContentBounds b = new ContentBounds((XmlNode) o);
                    b.start = buf.length();
                    contentList.add(b);
                    textonly = false;
                    buf.append("\n").append(ws + LEVEL_WHITESPACE);
                    buf.append("<!--").append(((Comment) o).getComment()).append("-->");
                    b.end = buf.length();
                } else if (o instanceof Element) {
                    textonly = false;
                    buf.append("\n");
                    appendContent((Element) o, buf, level + 1);
                } else {
                    ContentBounds b = new ContentBounds((XmlNode) o);
                    b.start = buf.length();
                    contentList.add(b);
                    buf.append( ((XmlNode) o).toString() + ws + LEVEL_WHITESPACE);
                    b.end = buf.length();
                }
            }
            if (!textonly) {
                buf.append("\n").append(ws);
            }
            buf.append("</").append(el.getName()).append(">");
            bounds.end = buf.length();
        }
    }

    public void setupPopupMenu() {
        
        PopupSupport popupSupport = new PopupSupport(this);
        popupSupport.addPopupMenuAdapter(new PopupMenuAdapter() {

            @Override
            public void setMenuContents(JPopupMenu menu, final java.awt.Point p) {
                menu.add(new JMenuItem(formatAction));
                menu.add(new JMenuItem(checkAction));
                menu.addSeparator();
                menu.add(new JMenuItem(copyAction));
                menu.add(new JMenuItem(cutAction));
                menu.add(new JMenuItem(pasteAction));

                final JMenu gotoXMLMenu = new JMenu("Go To Element");
                gotoXMLMenu.addMenuListener(new MenuListener() {

                    public void menuSelected(MenuEvent e) {
                        if (xmlDoc != null) {
                            buildXMLTree(gotoXMLMenu, xmlDoc);
                        }
                    }

                    public void menuDeselected(MenuEvent e) {
                        gotoXMLMenu.removeAll();
                    }

                    public void menuCanceled(MenuEvent e) {
                        gotoXMLMenu.removeAll();
                    }
                });
                menu.add(gotoXMLMenu);

                // selects the entire XML content that is under the cursor.
                XmlNode content = getXML(viewToModel(p));
                if (content != null) {
                    JMenu contentSelMenu = new JMenu("Select XML Content");
                    menu.add(contentSelMenu);
                    final XmlNode tmp = content;
                    contentSelMenu.add(new JMenuItem(new AbstractAction("This") {

                        public void actionPerformed(ActionEvent e) {
                            select(tmp);
                        }
                    }));
                    while (content.getParent() != null) {
                        final Element el = content.getParent();
                        content = el;
                        contentSelMenu.add(new JMenuItem(new AbstractAction(el.getName()) {

                            public void actionPerformed(ActionEvent e) {
                                select(el);
                            }
                        }));
                    }
                }

                // set up the unit converter
                String text = getSelectedText();
                try {
                    double val = Double.parseDouble(text);
                    final UnitMenu unitMenu = new UnitMenu(val);
                    unitMenu.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            replaceSelection(String.valueOf(unitMenu.getValue()));
                        }
                    });
                    menu.add(unitMenu);
                } catch (Exception ex) {
                }
            }
        });

    }

    public static void main(String[] args) {
        try {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            final XMLEditPane editpane = new XMLEditPane();
            editpane.setXML(XmlReader.readDocument(new File("c:/users/matt/desktop/test.xml")));
            editpane.setPreferredSize(new Dimension(480, 640));
            f.add(new JScrollPane(editpane));
            f.pack();
            f.setVisible(true);

            editpane.addPropertyChangeListener(XML_SECTION_CHANGED, new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent e) {
                    System.out.println(e.getNewValue());
                }
            });

        } catch (Exception ex) {
            Logger.getLogger(XMLEditPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // defines actions for this component
    Action formatAction = new AbstractAction("Format XML") {

        public void actionPerformed(ActionEvent e) {
            formatXML();
        }
    };
    Action copyAction = new AbstractAction("Copy") {

        public void actionPerformed(ActionEvent e) {
            copy();
        }
    };
    Action pasteAction = new AbstractAction("Paste") {

        @Override
        public boolean isEnabled() {
            return Toolkit.getDefaultToolkit().getSystemClipboard().isDataFlavorAvailable(DataFlavor.stringFlavor);
        }

        public void actionPerformed(ActionEvent e) {
            paste();
        }
    };
    Action cutAction = new AbstractAction("Cut") {

        public void actionPerformed(ActionEvent e) {
            cut();
        }
    };
    Action checkAction = new AbstractAction("Check XML") {

        public void actionPerformed(ActionEvent e) {
            boolean ok = checkXML();
            JOptionPane.showMessageDialog(null, "XML Check " + (ok ? "OK" : "Fail"),
                    "XML Check", ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
        }
    };
    Action undoAction = new AbstractAction("Undo") {

        public void actionPerformed(ActionEvent e) {
            undoManager.undo();
        }
    };
    Action redoAction = new AbstractAction("Redo") {

        public void actionPerformed(ActionEvent e) {
            undoManager.redo();
        }
    };
    Action findAction = new AbstractAction("Find") {

        public void actionPerformed(ActionEvent e) {
            FindTool findTool = new FindTool(XMLEditPane.this);
            findTool.getReplaceWindow();
        }
    };

    protected void buildXMLTree(JMenu top, Element xml) {
        if (xml == null) {
            return;
        }
        for (final Element child : xml.getChildElements() ) {
            final JMenu childMenu = new JMenu(child.getName());
            top.add(childMenu);
            childMenu.addMenuListener(new MenuListener() {

                public void menuSelected(MenuEvent e) {
                    JMenuItem goItem = new JMenuItem(new AbstractAction("Select") {

                        public void actionPerformed(ActionEvent e) {
                            select(child);
                        }
                    });
                    childMenu.add(goItem);
                    childMenu.addSeparator();
                    buildXMLTree(childMenu, child);
                }

                public void menuDeselected(MenuEvent e) {
                    childMenu.removeAll();
                }

                public void menuCanceled(MenuEvent e) {
                    childMenu.removeAll();
                }
            });
        }
    }

    static class ContentBounds {

        int start = 0;
        int end = 0;
        XmlNode content;

        public ContentBounds(XmlNode c) {
            this.content = c;
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */