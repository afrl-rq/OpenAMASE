// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Aug 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.flexdock.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * @author Christopher Butler
 */
public class OsInfo {
    private static final OsInfo SINGLETON = new OsInfo();
    private static final String XML_RESOURCE = "org/flexdock/util/os-info.xml";

    private String osArch;
    private List osNameList;
    private List osLibraryList;
    private List prefixOsLibraryList;

    public static OsInfo getInstance() {
        return SINGLETON;
    }

    public OsInfo() {
        this(null);
    }

    public OsInfo(Properties systemProps) {
        if(systemProps==null)
            systemProps = System.getProperties();

        Document doc = ResourceManager.getDocument(XML_RESOURCE);
        osArch = getSystemArch(doc, systemProps);
        osNameList = Collections.unmodifiableList(getOSChain(doc, systemProps));

        ArrayList libList = new ArrayList(osNameList.size());
        ArrayList prefixLibList = new ArrayList(osNameList.size());
        for(Iterator it=osNameList.iterator(); it.hasNext();) {
            String osName = (String)it.next();
            libList.add(osName + "-" + osArch);
        }
        libList.addAll(osNameList);

        for(Iterator it=libList.iterator(); it.hasNext();) {
            String prefixed = "-" + it.next();
            prefixLibList.add(prefixed);
        }

        osLibraryList = Collections.unmodifiableList(libList);
        prefixOsLibraryList = Collections.unmodifiableList(prefixLibList);
    }

    private String getSystemArch(Document doc, Properties systemProps) {
        String archTag = "arch";
        String sysArch = format(systemProps.getProperty("os.arch"));

        Element archElem = findElementByName(doc, archTag, sysArch);
        while(isNested(archElem, archTag)) {
            archElem = (Element)archElem.getParentNode();
        }
        return archElem==null? sysArch: archElem.getAttribute("name");
    }


    private List getOSChain(Document doc, Properties systemProps) {
        String osTag = "os";
        String osName = format(systemProps.getProperty("os.name"));
        Element osElem = findElementByName(doc, osTag, osName);
        ArrayList list = new ArrayList();

        while(osElem!=null && osTag.equals(osElem.getTagName())) {
            list.add(osElem.getAttribute("name"));
            osElem = (Element)osElem.getParentNode();
        }

        if(list.isEmpty())
            list.add(osName);
        return list;
    }







    private Element findElementByName(Document doc, String tagName, String nameAttrib) {
        NodeList list = doc.getElementsByTagName(tagName);
        for(int i=0; i<list.getLength(); i++) {
            Element elem = (Element)list.item(i);
            if(nameAttrib.equals(elem.getAttribute("name")))
                return elem;
        }
        return null;
    }

    private String format(String data) {
        return data.replace(' ', '_').toLowerCase();
    }

    private boolean isNested(Element elem, String tagName) {
        if(elem==null)
            return false;

        Element parent = (Element)elem.getParentNode();
        return parent==null? false: tagName.equals(parent.getTagName());
    }

    /**
     * @return Returns the osArch.
     */
    public String getArch() {
        return osArch;
    }
    /**
     * @return Returns the osLibraryList.
     */
    public List getLibraryKeys() {
        return osLibraryList;
    }

    /**
     * @return Returns the prefixOsLibraryList.
     */
    public List getPrefixLibraryKeys() {
        return prefixOsLibraryList;
    }
    /**
     * @return Returns the osNameList.
     */
    public List getOsNames() {
        return osNameList;
    }

    public String getLibraryKey() {
        return (String)getLibraryKeys().get(0);
    }

    public String getPrefixLibraryKey() {
        return (String)getPrefixLibraryKeys().get(0);
    }

    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty("os.name", System.getProperty("os.name"));
        props.setProperty("os.arch", System.getProperty("os.arch"));

        OsInfo info = new OsInfo(props);
        List libList = info.getLibraryKeys();

        System.out.println("Library Keychain");
        System.out.println("----------------");
        for(Iterator it=libList.iterator(); it.hasNext();) {
            System.out.println(it.next());
        }
        System.out.println();
        System.out.println("Default Library Key: " + info.getLibraryKey());
    }
}
