// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.properties;

import avtas.xml.Element;
import java.awt.Component;

/**
 * A facility for storing and receiving configuration properties.  
 * @author AFRL/RQQD
 */
public class UserProperties {
    
    public Element toXml() {
        return XmlSerializer.serialize(this);
    }
    
    public Element toXml(String topElementName) {
        Element el = XmlSerializer.serialize(this);
        el.setName(topElementName);
        return el;
    }
    
    public void fromXml(Element el) {
        XmlSerializer.deserialize(el, this);
    }
    
    public Component getEditor() {
        return PropertyEditor.getEditor(this);
    }
    
    public void commitEdit() {}
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */