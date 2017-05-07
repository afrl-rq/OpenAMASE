// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.setup;

import avtas.app.UserExceptions;
import avtas.lmcp.LMCPObject;
import avtas.lmcp.LMCPXMLReader;
import avtas.xml.DOMTranslator;
import avtas.xml.Element;
import java.io.File;

/**
 *
 * @author AFRL/RQQD
 */
public class EventElement {
    Element el;
    File sourceFile;
    LMCPObject eventObject;
    

    public EventElement(Element el, File sourceFile) {
        try {
            this.el = el;
            this.sourceFile = sourceFile;
            this.eventObject = (LMCPObject) LMCPXMLReader.readXML(DOMTranslator.toDOM(el));
        } catch (Exception ex) {
            UserExceptions.showError(this, "Cannot read LMCP object from XML", ex);
        }
    }

    public Element getXml() {
        return el;
    }

    public LMCPObject getEventObject() {
        return eventObject;
    }

    public double getTime() {
        return el.getDoubleAttr("Time", 0);
    }
    
    public void setTime(double time) {
        el.setAttribute("Time", time);
    }

    public File getSourceFile() {
        return sourceFile;
    }

    /** Replaces the old LMCP object in this event with a new one. */
    public void setEventObject(LMCPObject eventObject) {
       if (eventObject != null) {
           Element newEl = Element.read(eventObject.toXML(""));
           el.clear();
           el.addAll(newEl.getChildren());
           el.setName(newEl.getName());
           for (int i=0; i<newEl.getAttributeCount(); i++) {
               el.setAttribute(newEl.getAttributeName(i), newEl.getAttributeValue(i));
           }
       }
    }
    
    

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */