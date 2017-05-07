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
import avtas.amase.scenario.ScenarioState.EventWrapper;
import avtas.lmcp.LMCPObject;
import avtas.lmcp.LMCPXMLReader;
import avtas.xml.Element;
import avtas.xml.XMLUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipInputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author AFRL/RQQD
 */
public class EventListReader {

    private final File scenarioFile;
    static XMLInputFactory factory = null;
    //XMLStreamReader reader = null;
    double currentTime = 0;
    Element currentElement = null;
    List<EventWrapper> eventList = new ArrayList<>();

    public EventListReader(File scenario) {
        this.scenarioFile = scenario;
        if (factory == null) {
            factory = XMLInputFactory.newFactory();
        }
    }

//    static List<EventWrapper> parse(File scenario) {
//        try {
//
//            InputStream in;
//            if (scenario.getName().toUpperCase().endsWith(".ZIP")) {
//                ZipInputStream zin = new ZipInputStream(new FileInputStream(scenario));
//                zin.getNextEntry();
//                in = zin;
//            }
//            else {
//                in = new FileInputStream(scenario);
//            }
//
//            XMLStreamReader reader = factory.createXMLStreamReader(in);
//            readEvents(reader);
//        } catch (XMLStreamException | IOException ex) {
//            AmaseExceptions.showError(EventListReader.class, "Cannot load event list", ex);
//        }
//    }

    /**
     * Returns events that occur between the current time and the requested time
     */
    public List<EventWrapper> getEvents(double time) {
        List<EventWrapper> retList = new ArrayList<>();
        for (EventWrapper ew : eventList) {
            if (ew.time > currentTime && ew.time <= time) {
                retList.add(ew);
            }
        }
        currentTime = time;
        return retList;
    }

    public void setCurrentTime(double time) {
        this.currentTime = time;
    }

    static List<EventWrapper> readEvents(XMLStreamReader reader) throws XMLStreamException {
        int type;
        boolean inList = false;
        List<EventWrapper> retList = new ArrayList<>();


        while (reader.hasNext()) {
            type = reader.next();
            if (type == XMLEvent.START_ELEMENT) {
                if (!inList && reader.getLocalName().equals("ScenarioEventList")) {
                    inList = true;
                    continue;
                }
                if (inList) {

                    if (reader.getLocalName().equals("Link")) {
                        String fileName = reader.getAttributeValue(null, "Source");
                        //retList.addAll(parse());
                        
                    }

                    Element eventEl = getNextElement(reader);
                    double time = XMLUtil.getDoubleAttr(eventEl, "Time", 0);

                    try {
                        LMCPObject obj = LMCPXMLReader.readXML(eventEl.toXML());
                        EventWrapper wrapper = new EventWrapper(time, obj);
                        retList.add(wrapper);

                    } catch (Exception ex) {
                        Logger.getLogger(EventListReader.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }
            else if (type == XMLEvent.END_ELEMENT) {
                if (reader.getLocalName().equals("ScenarioEventList")) {
                    break;
                }
            }
        }
        
        return retList;

    }

    static Element getNextElement(XMLStreamReader reader) throws XMLStreamException {

        int level = 0;
        int type;
        Element currentEl = null;

        Element topEl = new Element(reader.getLocalName());
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            topEl.setAttribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
        }
        level++;
        while (level > 0) {

            type = reader.next();

            if (type == XMLEvent.START_ELEMENT) {
                level++;
                Element tmp = new Element(reader.getLocalName());
                for (int i = 0; i < reader.getAttributeCount(); i++) {
                    tmp.setAttribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
                }
                if (currentEl != null) {
                    currentEl.add(tmp);
                }
                currentEl = tmp;
            }
            else if (type == XMLEvent.END_ELEMENT) {
                level--;
            }
            else if (type == XMLEvent.CHARACTERS && currentEl != null) {
                currentEl.setText(reader.getText());
            }
        }

        return topEl;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */