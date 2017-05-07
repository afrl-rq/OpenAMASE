// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.setup;

import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import avtas.app.UserExceptions;
import avtas.amase.scenario.ScenarioEvent;
import avtas.amase.scenario.ScenarioState;
import avtas.app.AppEventListener;
import avtas.app.AppEventManager;
import avtas.xml.XMLUtil;
import avtas.lmcp.LMCPObject;
import avtas.lmcp.LMCPXMLReader;
import avtas.xml.DOMTranslator;
import java.util.logging.Level;
import java.util.logging.Logger;
import avtas.xml.Element;
import avtas.xml.XmlReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Listens for ScenarioChangedEvents and publishes events according to data found in
 * the "ScenarioEventList".  This maintains compatibility with tools that may not
 * be aware of the SetupDataManager but may still be of use in the setup tool.
 *
 * @author AFRL/RQQD
 */
public class SetupEventBridge implements AppEventListener {

    AppEventManager eventManager = AppEventManager.getDefaultEventManager();
    //List<Element> xmlEventList = null;
    Element eventElement = null;
    Element scenarioElement = null;
    File sourceFile = null;
    private ObjectMap eventMap = new ObjectMap();
    private double time = 0;
    
    SetupEventBridge instance = null;
    
    public SetupEventBridge() {
        if (instance != null) {
            UserExceptions.showError(this, "Trying to create multiple SetupEventBridge services", null);
        }
        else {
            instance = this;
        }
    }

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof ScenarioEvent) {
            loadEvents((ScenarioEvent) event);
        }
        else if (event instanceof SessionStatus) {
            this.time = ((SessionStatus) event).getScenarioTime() / 1000d;
            ScenarioState.processLMCP( (SessionStatus) event, time);
        }
        else if (event instanceof LMCPObject) {
            LMCPObject lmcpObj = (LMCPObject) event;
            
            

            if (eventElement == null) {
                eventElement = new Element(SetupScenarioManager.EVENTLIST_NAME);
                if (scenarioElement == null) {
                    scenarioElement = new Element(SetupScenarioManager.TOP_LEVEL_NAME);
                }
                scenarioElement.add(eventElement);
            }

            Element oldEvent = eventMap.getElement(lmcpObj);
            if (oldEvent != null) {
                updateXML(lmcpObj, oldEvent);
            }
            else {
                Element xmlEvent = updateXML(lmcpObj, null);
                eventElement.add(xmlEvent);
                eventMap.put(lmcpObj, xmlEvent);
                ScenarioState.processLMCP(lmcpObj, time);
            }
            ScenarioEvent scenEvent = new ScenarioEvent(sourceFile, scenarioElement);
            eventManager.fireEvent(scenEvent, this);
            //loadEvents(scenEvent);
        }

        else if (event instanceof RemoveObjectsEvent) {
            for (Object objToRemove : ((RemoveObjectsEvent) event).getObjects()) {
                if (objToRemove instanceof LMCPObject) {
                    Element oldEvent = eventMap.getElement((LMCPObject) objToRemove);
                    if (oldEvent != null) {
                        eventElement.remove(oldEvent);
                    }
                }
            }
            eventManager.fireEvent(new ScenarioEvent(sourceFile, scenarioElement), this);
        }
        else if (event instanceof SelectObjectEvent) {
            SelectObjectEvent selectEvent = (SelectObjectEvent) event;
            if (selectEvent.getObject() instanceof LMCPObject) {
                Element el = eventMap.getElement((LMCPObject) selectEvent.getObject());
                if (el != null) {
                    eventManager.fireEvent(new SelectObjectEvent(el), this);
                }
            }
            else if (selectEvent.getObject() instanceof Element) {
                Object o = eventMap.getLmcpObject((Element) selectEvent.getObject());
                if (o != null) {
                    eventManager.fireEvent(new SelectObjectEvent(o), this);
                }
            }
        }
    }

    void loadEvents(ScenarioEvent sce) {

        if (eventManager == null) {
            return;
        }
        eventElement = null;
        eventMap.clear();
        
        ScenarioState.clearData();
        ScenarioState.setScenario(sce.getXML(), sce.getSourceFile());

        SessionStatus ss = new SessionStatus();
        ss.setState(SimulationStatusType.Reset);
        eventManager.fireEvent(ss, this);

        scenarioElement = sce.getXML();
        eventElement = scenarioElement.getChild(SetupScenarioManager.EVENTLIST_NAME);
        sourceFile = sce.getSourceFile();

        if (eventElement == null) {
            eventElement = new Element(SetupScenarioManager.EVENTLIST_NAME);
            scenarioElement.add(eventElement);
        }

        double oldTime = 0;

        for (Element eventEl : eventElement.getChildElements()) {
            double time = XMLUtil.getDoubleAttr(eventEl, "Time", oldTime);
            try {
                LMCPObject event = (LMCPObject) LMCPXMLReader.readXML(DOMTranslator.toDOM(eventEl));
                
                if (time != oldTime) {
                    oldTime = time;
                    ss = new SessionStatus();
                    ss.setState(SimulationStatusType.Paused);
                    ss.setScenarioTime( (long) (time*1000) );
                    eventManager.fireEvent(ss, this);
                }

                if (event != null) {
                    ScenarioState.processLMCP(event, time);
                    eventMap.put(event, eventEl);
                    eventManager.fireEvent(event, this);
                }
                else {
                    UserExceptions.showWarning("Cannot read event:\n" + eventEl.toXML());
                }
                

                
            } catch (Exception e) {
                UserExceptions.showWarning("Cannot read event:\n" + eventEl.toXML());
            }
        }

    }

    static class ObjectMap {

        List<LMCPObject> objectList = new ArrayList<LMCPObject>();
        List<Element> elementList = new ArrayList<Element>();

        public ObjectMap() {
        }

        public LMCPObject getLmcpObject(Element el) {
            for (int i = 0; i < elementList.size(); i++) {
                if (elementList.get(i) == el) {
                    return objectList.get(i);
                }
            }
            return null;
        }

        public Element getElement(LMCPObject obj) {
            for (int i = 0; i < objectList.size(); i++) {
                if (objectList.get(i) == obj) {
                    return elementList.get(i);
                }
            }
            return null;
        }

        public void put(LMCPObject obj, Element el) {
            for (int i = 0; i < objectList.size(); i++) {
                if (objectList.get(i) == obj || elementList.get(i) == el) {
                    objectList.remove(i);
                    elementList.remove(i);
                    break;
                }
            }
            objectList.add(obj);
            elementList.add(el);
        }

        public void clear() {
            objectList.clear();
            elementList.clear();
        }
    }

    public static Element updateXML(LMCPObject obj, Element el) {
        try {
            if (el == null) {
                return XmlReader.readDocument(obj.toXML(""));
            }
            else {
                Element lmcpEl = XmlReader.readDocument(obj.toXML(""));
                el.clear();
                el.addAll(lmcpEl.getChildren());
                for (int i=0; i<lmcpEl.getAttributeCount(); i++) {
                    el.setAttribute(lmcpEl.getAttributeName(i), lmcpEl.getAttributeValue(i));
                }
                return el;
            }
        } catch (Exception ex) {
            Logger.getLogger(SetupEventBridge.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */