// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Copyright (c) 2005 FlexDock Development Team. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE.
 */
package org.flexdock.perspective.persist.xml;

import java.util.Iterator;
import java.util.List;

import org.flexdock.docking.state.DockingPath;
import org.flexdock.docking.state.LayoutNode;
import org.flexdock.docking.state.tree.SplitNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Created on 2005-06-23
 *
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 * @version $Id: DockingPathSerializer.java,v 1.8 2005-07-06 18:10:48 winnetou25 Exp $
 */
public class DockingPathSerializer implements ISerializer {

    public Element serialize(Document document, Object object) {
        DockingPath dockingPath = (DockingPath) object;

        Element dockingPathElement = document.createElement(PersistenceConstants.DOCKING_PATH_ELEMENT_NAME);
        dockingPathElement.setAttribute(PersistenceConstants.DOCKING_PATH_ATTRIBUTE_ROOT_PORT_ID, dockingPath.getRootPortId());

        if (dockingPath.getSiblingId() != null && !dockingPath.getSiblingId().equals("")) {
            dockingPathElement.setAttribute(PersistenceConstants.DOCKING_PATH_ATTRIBUTE_SIBLING_ID, dockingPath.getSiblingId());
        }

        if (dockingPath.isTabbed()) {
            dockingPathElement.setAttribute(PersistenceConstants.DOCKING_PATH_ATTRIBUTE_IS_TABBED, String.valueOf(dockingPath.isTabbed()));
        }

        List splitNodes = dockingPath.getNodes();
        ISerializer splitNodeSerializer = SerializerRegistry.getSerializer(SplitNode.class);
        for (Iterator it = splitNodes.iterator(); it.hasNext();) {
            SplitNode splitNode = (SplitNode) it.next();
            Element splitNodeElement = splitNodeSerializer.serialize(document, splitNode);
            dockingPathElement.appendChild(splitNodeElement);
        }

        return dockingPathElement;
    }

    public Object deserialize(Element element) {
        //DockingState dockingState = (DockingState) deserializationStack.popObject();
        DockingPath dockingPath = new DockingPath();

        String dockingPathRootPortId = element.getAttribute(PersistenceConstants.DOCKING_PATH_ATTRIBUTE_ROOT_PORT_ID);
        String siblingId = element.getAttribute(PersistenceConstants.DOCKING_PATH_ATTRIBUTE_SIBLING_ID);
        String isTabbed = element.getAttribute(PersistenceConstants.DOCKING_PATH_ATTRIBUTE_IS_TABBED);

        dockingPath.setRootPortId(dockingPathRootPortId);
        if (siblingId != null && !"".equals(siblingId)) {
            dockingPath.setSiblingId(siblingId);
        }
        if (isTabbed != null && !"".equals(isTabbed)) {
            dockingPath.setTabbed(Boolean.valueOf(isTabbed).booleanValue());
        } else {
            dockingPath.setTabbed(false);
        }

        ISerializer layoutNodeSerializer = SerializerRegistry.getSerializer(LayoutNode.class);
        NodeList splitNodeList = element.getElementsByTagName(PersistenceConstants.SPLIT_NODE_ELEMENT_NAME);
        if (splitNodeList.getLength() > 0 && splitNodeList.item(0) instanceof Element) {
            Element splitNodeElement = (Element) splitNodeList.item(0);
            SplitNode splitNode = (SplitNode) layoutNodeSerializer.deserialize(splitNodeElement);
            dockingPath.getNodes().add(splitNode);
        }

        return dockingPath;
    }

}
