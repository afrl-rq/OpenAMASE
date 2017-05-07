// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

/**
 * A simple and lightweight way to produce a DOM-like tree using a stream reader. This
 * allows the user to specify elements that should not be parsed in the process.
 * For Very large XML documents, there may be only a subset of the document that
 * is of interest. Ignore lists should be specified as full paths in the XML
 * tree using "/" as the path separator. Regular expressions are supported.
 * WARNING: Not thoroughly tested. There may be advanced XML features that are
 * not captured by this parser.
 *
 * Optionally, the user can specify a set of Element names to ignore in the
 * processing. Element names are specified as paths, separated by "/". This is
 * handy for large XML documents.
 *
 * @author AFRL/RQQD
 */
public class XmlReader {

    static XMLInputFactory factory = null;

    /**
     * Reads an XML document and returns the root element.
     *
     * @param reader A source containing the document
     * @param ignoreElements A set of element names to ignore. Nested elements
     * can be specified using the "/" delimiter
     * @return the root element of the document, minus any ignored elements
     */
    public static Element readDocument(Reader reader, String... ignoreElements) throws XMLStreamException {

        if (factory == null) {
            factory = XMLInputFactory.newFactory();
        }

        XMLStreamReader xmlReader = factory.createXMLStreamReader(reader);

        Element el = readXml(xmlReader, Arrays.asList(ignoreElements));
        xmlReader.close();

        return el;
    }

    /**
     * Reads an XML document and returns the root element.
     *
     * @param file A file containing the document
     * @param ignoreElements A set of element names to ignore. Nested elements
     * can be specified using the "/" delimiter
     * @return the root element of the document, minus any ignored elements
     */
    public static Element readDocument(File file, String... ignoreElements) throws XMLStreamException {
        try {
            return readDocument(new FileReader(file), ignoreElements);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XmlReader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Reads an XML document and returns the root element.
     *
     * @param xmlString An input string containing the document
     * @param ignoreElements A set of element names to ignore. Nested elements
     * can be specified using the "/" delimiter
     * @return the root element of the document, minus any ignored elements
     */
    public static Element readDocument(String xmlString, String... ignoreElements) throws XMLStreamException {
        return readDocument(new StringReader(xmlString), ignoreElements);
    }

    public static Element readDocument(InputStream is, String... ignoreElements) throws XMLStreamException {
        return readDocument(new InputStreamReader(is), ignoreElements);
    }

    protected static Element readXml(XMLStreamReader reader, List<String> ignoreElements) throws XMLStreamException {
        Element topEl = null;
        Element currentEl = null;
        int skipCounter = -1;

        int eventType;
        while (reader.hasNext()) {

            eventType = reader.next();

            if (eventType == XMLEvent.COMMENT) {
                if (skipCounter == -1) {
                    if (currentEl != null) {
                        currentEl.add(new Comment(reader.getText()));
                    }
                }
            }
            else if (eventType == XMLEvent.START_ELEMENT) {

                String name = reader.getLocalName();
                if (skipCounter != -1) {
                    skipCounter++;
                    continue;
                }
                else if (ignoreElements.contains(getPath(name, currentEl))) {
                    skipCounter = 0;
                    continue;
                }
                

                Element tmp = new Element(name);

                for (int i = 0; i < reader.getAttributeCount(); i++) {
                    tmp.setAttribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
                }

                if (currentEl != null) {
                    currentEl.add(tmp);
                }
                currentEl = tmp;
                if (topEl == null) {
                    topEl = currentEl;
                }

            }
            else if (eventType == XMLEvent.END_ELEMENT) {
                if (skipCounter != -1) {
                    skipCounter--;
                }
                else if (currentEl != null) {
                    currentEl.setText(currentEl.getText().trim());
                    currentEl = currentEl.getParent();
                }
            }
            else if (eventType == XMLEvent.CHARACTERS) {
                if (skipCounter == -1) {
                    if (currentEl != null) {
                        currentEl.setText(currentEl.getText() + reader.getText());
                    }
                }
            }
            else if (eventType == XMLEvent.CDATA) {
                if (skipCounter == -1) {
                    if (currentEl != null) {
                        currentEl.setText(reader.getText());
                    }
                }
            }

        }


        return topEl;
    }

    /**
     * returns a "/" delimited absolute path for a new Element with the given
     * name.
     *
     * @param qName The name of a new Element
     * @return the full path from the root node to the new Element
     */
    protected static String getPath(String qName, Element currentNode) {
        StringBuilder sb = new StringBuilder();
        //String path = qName;
        sb.append(qName);
        Element n = currentNode;
        while (n != null) {
            sb.insert(0,"/").insert(0, n.getName());
            //path = n.getName() + "/" + path;
            n = (Element) n.getParent();
        }
        return sb.toString();
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */