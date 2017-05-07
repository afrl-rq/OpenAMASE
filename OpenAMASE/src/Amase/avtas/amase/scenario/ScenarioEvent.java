// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.scenario;

import java.io.File;
import avtas.xml.Element;

/**
 * Indicates a change in the AMASE scenario.
 * @author AFRL/RQQD
 */
public class ScenarioEvent {

    File sourceFile;
    Element xmlNode;

    public ScenarioEvent(File sourceFile, Element xmlNode) {
        this.xmlNode = xmlNode;
        this.sourceFile = sourceFile;
    }

    public Element getXML() {
        return xmlNode;
    }

    public File getSourceFile() {
        return sourceFile;
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */