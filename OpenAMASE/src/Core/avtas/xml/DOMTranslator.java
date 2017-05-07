// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.xml;

import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 * Translates to and from org.w3c.dom elements
 * @author AFRL/RQQD
 */
public class DOMTranslator {

    public static org.w3c.dom.Element toDOM(Element el) {
        try {
            org.w3c.dom.Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            return toDOM(el, doc);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XmlWriter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private static org.w3c.dom.Element toDOM(Element fromNode, org.w3c.dom.Document doc) {
        org.w3c.dom.Element toNode = doc.createElement(fromNode.getName());
        for (int i=0; i<fromNode.getAttributeCount(); i++) {
            toNode.setAttribute(fromNode.getAttributeName(i), fromNode.getAttributeValue(i));
        }
        if (fromNode.getText() != null && !fromNode.getText().isEmpty()) {
            toNode.setTextContent(fromNode.getText());
        }
        for (XmlNode n : fromNode.getChildren()) {
            if (n instanceof Comment) {
                toNode.appendChild(doc.createComment(((Comment) n).getComment()));
            }
            else if (n instanceof Element) {
                toNode.appendChild(toDOM((Element) n, doc));
            }
        }
        return toNode;
    }

    public static Element fromDOM(org.w3c.dom.Element el) {
        Element newEl = new Element(el.getTagName());
        NamedNodeMap attrMap = el.getAttributes();
        for (int i = 0; i < attrMap.getLength(); i++) {
            Attr attr = (Attr) attrMap.item(i);
            newEl.setAttribute(attr.getName(), attr.getValue());
        }
        NodeList list = el.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i) instanceof org.w3c.dom.Comment) {
                newEl.add(new Comment(((org.w3c.dom.Comment) list.item(i)).getNodeValue()));
            }
            else if (list.item(i) instanceof org.w3c.dom.Element) {
                newEl.add(fromDOM((org.w3c.dom.Element) list.item(i)));
            }
            else if (list.item(i) instanceof org.w3c.dom.Text && !newEl.hasChildren()) {
                newEl.setText(((org.w3c.dom.Text) list.item(i)).getNodeValue());
            }
        }

        return newEl;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */