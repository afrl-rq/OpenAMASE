// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.setup;

import avtas.xml.Element;
import avtas.xml.XmlNode;
import avtas.xml.XmlReader;
import avtas.xml.XmlWriter;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Supports the copying of XML Elements to/from the system clipboard or
 * drag-and-drop operations
 *
 * @author AFRL/RQQD
 */
public class XmlTransferUtils {

    static final String XML_LIST_TRANSFER = "XML_LIST_TRANSFER";

    public static void copyObjectToClipboard(XmlNode obj) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new XmlTransfer(obj), null);
    }

    public static void copyObjectsToClipboard(List<? extends XmlNode> objs) {
        String contents = createString(objs);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new XmlTransfer((List<XmlNode>) objs), null);
    }

    public static List<XmlNode> getObjectsFromClipboard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        return getObjectsFromTransfer(clipboard.getContents(null));
    }

    public static Transferable createTransfer(List<? extends XmlNode> objs) {
        Transferable xfer = new XmlTransfer((List<XmlNode>) objs);
        return xfer;
    }

    public static Transferable createTransfer(XmlNode... obj) {
        Transferable xfer = new StringSelection(createString(obj));
        return xfer;
    }

    public static List<XmlNode> getObjectsFromTransfer(Transferable t) {
        List<XmlNode> list = new ArrayList<>();
        try {
            if (t.isDataFlavorSupported(XmlTransfer.xmlFlavor)) {
                List<XmlNode> xferlist = (List<XmlNode>) t.getTransferData(XmlTransfer.xmlFlavor);
                if (xferlist.size() == 1 && xferlist.get(0) instanceof Element && 
                        ((Element) xferlist.get(0)).getName().equals(XML_LIST_TRANSFER)) {
                    for (XmlNode child : xferlist) {
                        list.add(child);
                    }
                }
                else {
                    list.addAll(xferlist);
                }
            } else if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String contents = (String) t.getTransferData(DataFlavor.stringFlavor);
                Element el = XmlReader.readDocument(contents);
                if (el.getName().equals(XML_LIST_TRANSFER)) {
                    for (XmlNode child : el.getChildren()) {
                        list.add(child);
                    }
                }
                else {
                    list.add(el);
                }
            }
        } catch (Exception ex) {
            return list;
        }
        return list;
    }

    public static String createString(List<? extends XmlNode> objs) {
        return createString(objs.toArray(new XmlNode[]{}));
    }

    public static String createString(XmlNode... objs) {
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(XML_LIST_TRANSFER).append(">");
        for (XmlNode obj : objs) {
            sb.append(XmlWriter.toCompactString(obj));
        }
        sb.append("</").append(XML_LIST_TRANSFER).append(">");
        return sb.toString();
    }

    public static class XmlTransfer implements Transferable {

        public static DataFlavor xmlFlavor = new DataFlavor(XmlNode.class, "XML");
        private List<XmlNode> xmlList;

        public XmlTransfer(XmlNode el) {
            this.xmlList = new ArrayList<XmlNode>();
            xmlList.add(el);
        }

        public XmlTransfer(List<XmlNode> nodeList) {
            this.xmlList = nodeList;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.stringFlavor, xmlFlavor};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return (flavor == xmlFlavor || flavor == DataFlavor.stringFlavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (flavor == DataFlavor.stringFlavor) {
                return createString(xmlList);
            } else if (flavor == xmlFlavor) {
                return xmlList;
            }
            return null;
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */