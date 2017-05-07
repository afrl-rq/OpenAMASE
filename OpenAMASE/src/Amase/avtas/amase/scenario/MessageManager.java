// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package avtas.amase.scenario;

import avtas.app.UserExceptions;
import avtas.lmcp.LMCPObject;
import avtas.lmcp.LMCPXMLReader;
import avtas.xml.DOMTranslator;
import avtas.xml.Element;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author AFRL/RQQD
 */
public class MessageManager {

    static XMLInputFactory factory = null;
    static final String EVENTLIST_NAME = "ScenarioEventList";
    Element pendingElement = null;
    XMLStreamReader xmlReader = null;
    InputStream stream = null;
    boolean inList = false;
    double time = 0;

    public MessageManager() {

        if (factory == null) {
            factory = XMLInputFactory.newFactory();
        }
    }

    public void resetReader(File file) {

        try {
            if (xmlReader != null) {
                xmlReader.close();
            }
            if (this.stream != null) {
                this.stream.close();
            }

            this.stream = new FileInputStream(file);
            if (stream != null) {
                xmlReader = factory.createXMLStreamReader(stream);
                inList = enterEventList();
            }
        } catch (Exception ex) {
            UserExceptions.showError(this, "Cannot parse file: " + file.getPath(), ex);
        }

    }

    public boolean hasMessages() {
        return inList;
    }

    /**
     * Returns the next event in the list, without exceeding the given scenario
     * dispatch time.
     *
     * @param doNotExceedTime maximum scenario time in seconds.
     * @return an LMCPObject, or null if the list end has been reached, or the
     * event is not readable by the LMCP XML factory.
     */
    public LMCPObject getNextEvent(double doNotExceedTime) {
        try {
            if (!inList) {
                return null;
            }
            if (pendingElement == null) {

                pendingElement = getNextElement();

            }
            if (pendingElement != null) {
                double elTime = pendingElement.getDoubleAttr("Time", 0);
                if (elTime > time) {
                    time = elTime;
                }
                if (time <= doNotExceedTime) {
                    LMCPObject obj = (LMCPObject) LMCPXMLReader.readXML(DOMTranslator.toDOM(pendingElement));
                    pendingElement = null;
                    return obj;
                }
            }
        } catch (Exception ex) {
            UserExceptions.showError(this, "Error parsing events.", ex);
            return null;
        }
        return null;
    }

    public List<Element> getEvents(double stopTime) {
        List<Element> retList = new ArrayList<>();

        while (time <= stopTime && inList) {
            if (pendingElement != null) {
                double elTime = pendingElement.getDoubleAttr("Time", 0);
                if (elTime > time) {
                    time = elTime;
                }
                if (time <= stopTime) {
                    retList.add(pendingElement);
                }
            }
            try {
                pendingElement = getNextElement();
            } catch (Exception ex) {
                UserExceptions.showError(this, "Error parsing events.", ex);
            }
        }

        return retList;
    }

    /**
     * Cues the stream reader to the first child under the ScenarioEventList
     * element.
     *
     * @return true if there is a ScenarioEventList in this file.
     * @throws Exception if any type of reader error occurs.
     */
    private boolean enterEventList() throws Exception {
        pendingElement = null;
        time = 0;
        while (xmlReader.hasNext()) {
            int type = xmlReader.next();

            if (type == XMLEvent.START_ELEMENT) {
                if (xmlReader.getLocalName().equals(EVENTLIST_NAME)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the next event element in the ScenarioEventList. Will return null if
     * the end of the list is reached, this file has no event list, or the
     * reader has not been cued to the start of the event list.
     */
    private Element getNextElement() throws Exception {
        if (!inList) {
            return null;
        }
        int level = 0;
        Element topElement = null;
        Element currentElement = null;
        while (xmlReader.hasNext()) {
            int type = xmlReader.next();

            if (type == XMLEvent.END_ELEMENT) {
                if (xmlReader.getLocalName().equals(EVENTLIST_NAME)) {
                    inList = false;
                    return null;
                }
                level--;
                if (level == 0) {
                    return topElement;
                } else {
                    if (currentElement != null) {
                        currentElement = currentElement.getParent();
                    }
                }
            } else if (type == XMLEvent.START_ELEMENT) {
                Element tmp = new Element(xmlReader.getLocalName());

                for (int i = 0; i < xmlReader.getAttributeCount(); i++) {
                    tmp.setAttribute(xmlReader.getAttributeLocalName(i), xmlReader.getAttributeValue(i));
                }
                if (level == 0) {
                    topElement = tmp;
                    currentElement = topElement;
                } else {
                    currentElement.add(tmp);
                    currentElement = tmp;
                }
                level++;
            } else if (type == XMLEvent.CHARACTERS) {
                if (currentElement != null) {
                    currentElement.setText(xmlReader.getText());
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        File input = new File("C:/users/matt/Desktop/test.xml");

        MessageManager man = new MessageManager();
        man.resetReader(input);

        for (double time = 0; time < 10; time += 0.1) {
            LMCPObject obj = man.getNextEvent(time);
            while (obj != null) {
                System.out.println(time + ": " + obj.getClass());
                obj = man.getNextEvent(time);
            }
        }

    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */