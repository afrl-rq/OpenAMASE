// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * A class for writing out XML documents
 *
 * @author AFRL/RQQD
 */
public class XmlWriter {

    private static final String DEFAULT_INDENT = "  ";
    private static XMLOutputFactory factory = null;

    /**
     * Writes An XML Element to a file using indenting, line returns, and a
     * top-level XML declaration.
     */
    public static void writeToFile(File file, Element el) {

        try (FileWriter fw = new FileWriter(file)) {
            write(fw, el, DEFAULT_INDENT, true, true);
        } catch (IOException ex) {
            Logger.getLogger(XmlWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Writes XML formatted with indents and line returns. Does not include an
     * XML declaration.
     */
    public static String toFormattedString(XmlNode el) {
        return write(el, DEFAULT_INDENT, true, false);
    }

    /**
     * Writes XML with no indents, line returns, or XML declaration tag.
     */
    public static String toCompactString(XmlNode el) {
        return write(el, "", false, false);
    }
    
    /**
     * Writes an XML node to a String.
     *
     * @param node the XML node to write
     * @param indent the string representing the indent at each level. Can be null or empty
     * @param lineReturn set to true to insert line returns 
     * @param showXMLDec set to true to print a leading XML declaration tag
     * @throws IOException If an error occurs writing the XML.
     */
    public static String write(XmlNode node, String indent, boolean lineReturn, boolean showXMLDec) {
        try {
            StringWriter sw = new StringWriter();
            write(sw, node, indent, lineReturn, showXMLDec);
            sw.close();
            return sw.toString();
        } catch (IOException ex) {
            Logger.getLogger(XmlWriter.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    /**
     * Writes an XML node to a Writer.
     *
     * @param out writer to which XML is written
     * @param node the XML node to write
     * @param indent the string representing the indent at each level. Can be null or empty
     * @param lineReturn set to true to insert line returns 
     * @param showXMLDec set to true to print a leading XML declaration tag
     * @throws IOException If an error occurs writing the XML.
     */
    public static void write(Writer out, XmlNode node, String indent, boolean lineReturn, boolean showXMLDec)
            throws IOException {


        if (factory == null) {
            factory = XMLOutputFactory.newFactory();
        }

        try {

            BufferedWriter writer = new BufferedWriter(out);
            XMLStreamWriter xmlWriter = factory.createXMLStreamWriter(writer);

            if (showXMLDec) {
                xmlWriter.writeStartDocument();
                if (lineReturn) {
                    writer.newLine();
                }
            }

            write(xmlWriter, writer, node, 0, indent == null ? "" : indent, lineReturn);

            xmlWriter.writeEndDocument();
            writer.newLine();
            xmlWriter.flush();
            writer.flush();

        } catch (XMLStreamException | IOException ex) {
            Logger.getLogger(XmlWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected static void write(XMLStreamWriter xmlWriter, BufferedWriter writer, XmlNode node, int level,
            String indentStr, boolean lineRet) {
        try {

            if (lineRet) {
                writer.newLine();
            }

            if (!indentStr.isEmpty()) {
                writeIndent(level, writer, indentStr);
            }

            if (node instanceof Comment) {
                xmlWriter.writeComment(((Comment) node).getComment());
            } else if (node instanceof Element) {

                Element el = (Element) node;
                String text = el.getText() == null ? "" : el.getText().trim();

                if (!el.hasChildren() && text.isEmpty()) {
                    xmlWriter.writeEmptyElement(el.getName());
                } else {
                    xmlWriter.writeStartElement(el.getName());
                }

                for (int i = 0; i < el.getAttributeCount(); i++) {
                    xmlWriter.writeAttribute(el.getAttributeName(i), el.getAttributeValue(i));
                }

                // need to write this even if there is empty text. It tells the 
                // writer to close the node before newlines or indents are written
                xmlWriter.writeCharacters(text);

                if (el.hasChildren()) {
                    for (XmlNode child : el.getChildren()) {
                        write(xmlWriter, writer, child, level + 1, indentStr, lineRet);
                    }
                    if (lineRet) {
                        writer.newLine();
                        if (!indentStr.isEmpty()) {
                            writeIndent(level, writer, indentStr);
                        }
                    }
                }

                if (!text.isEmpty() || el.hasChildren()) {
                    xmlWriter.writeEndElement();
                }


            }
        } catch (IOException | XMLStreamException ex) {
            Logger.getLogger(XmlWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected static void writeIndent(int level, Writer writer, String indentStr) throws IOException {
        for (int i = 0; i < level; i++) {
            writer.write(indentStr);
        }
    }

    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */