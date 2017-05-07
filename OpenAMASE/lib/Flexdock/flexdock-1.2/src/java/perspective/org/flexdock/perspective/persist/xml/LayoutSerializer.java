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

import org.flexdock.docking.Dockable;
import org.flexdock.docking.state.DockingState;
import org.flexdock.docking.state.FloatingGroup;
import org.flexdock.docking.state.LayoutNode;
import org.flexdock.perspective.Layout;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created on 2005-06-03
 *
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 * @version $Id: LayoutSerializer.java,v 1.11 2005-07-06 18:10:48 winnetou25 Exp $
 */
public class LayoutSerializer implements ISerializer {

    /**
     * @see org.flexdock.perspective.persist.xml.ISerializer#serialize(org.w3c.dom.Document, java.lang.Object)
     */
    public Element serialize(Document document, Object object) {
        Layout layout = (Layout) object;

        Element layoutElement = document.createElement(PersistenceConstants.LAYOUT_ELEMENT_NAME);

        Dockable[] dockables = layout.getDockables();
        ISerializer dockingStateSerializer = SerializerRegistry.getSerializer(DockingState.class);
        for (int i = 0; i < dockables.length; i++) {
            Dockable dockable = dockables[i];
            DockingState dockingState = layout.getDockingState(dockable);
            Element dockingStateElement = dockingStateSerializer.serialize(document, dockingState);
            layoutElement.appendChild(dockingStateElement);
        }

        ISerializer floatingGroupSerializer = SerializerRegistry.getSerializer(FloatingGroup.class);
        String[] floatingGroupIds = layout.getFloatingGroupIds();
        for (int i = 0; i < floatingGroupIds.length; i++) {
            String floatingGroupId = floatingGroupIds[i];
            FloatingGroup floatingGroup = layout.getGroup(floatingGroupId);
            Element floatingGroupElement = floatingGroupSerializer.serialize(document, floatingGroup);
            layoutElement.appendChild(floatingGroupElement);
        }

        LayoutNode layoutNode = layout.getRestorationLayout();
        //TODO should we nest restoration layout in Restoration node?
        if (layoutNode != null) {
            ISerializer layoutNodeSerializer = SerializerRegistry.getSerializer(LayoutNode.class);
            Element layoutNodeElement = layoutNodeSerializer.serialize(document, layoutNode);
            layoutElement.appendChild(layoutNodeElement);
        }

        return layoutElement;
    }

    public Object deserialize(Element element) {
        Layout layout = new Layout();

        ISerializer dockingStateSerializer = SerializerRegistry.getSerializer(DockingState.class);
        NodeList dockingStateNodeList = element.getElementsByTagName(PersistenceConstants.DOCKING_STATE_ELEMENT_NAME);
        for (int i = 0; i < dockingStateNodeList.getLength(); i++) {
            Node node = dockingStateNodeList.item(i);
            if (node instanceof Element) {
                Element dockingStateElement = (Element) node;
                DockingState dockingState = (DockingState) dockingStateSerializer.deserialize(dockingStateElement);
                String dockableId = dockingState.getDockableId();
                layout.setDockingState(dockableId, dockingState);
            }
        }

        ISerializer floatingGroupsSerializer = SerializerRegistry.getSerializer(FloatingGroup.class);
        NodeList floatingGroupsNodeList = element.getElementsByTagName(PersistenceConstants.FLOATING_GROUP_ELEMENT_NAME);
        for (int i=0; i<floatingGroupsNodeList.getLength(); i++) {
            Node floatingGroupNode = floatingGroupsNodeList.item(i);
            if (floatingGroupNode instanceof Element) {
                Element floatingGroupElement = (Element) floatingGroupNode;
                FloatingGroup floatingGroup = (FloatingGroup) floatingGroupsSerializer.deserialize(floatingGroupElement);
                layout.addFloatingGroup(floatingGroup);
            }
        }

        ISerializer layoutNodeSerializer = SerializerRegistry.getSerializer(LayoutNode.class);

        NodeList dockingPortNodeList = element.getElementsByTagName(PersistenceConstants.DOCKING_PORT_NODE_ELEMENT_NAME);
        if (dockingPortNodeList.getLength() > 0 && dockingPortNodeList.item(0) instanceof Element) {
            Element layoutNodeElement = (Element) dockingPortNodeList.item(0);
            LayoutNode restorationLayout = (LayoutNode) layoutNodeSerializer.deserialize(layoutNodeElement);
            layout.setRestorationLayout(restorationLayout);
        }

        return layout;
    }

}
