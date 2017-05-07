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

import java.awt.Point;

import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.state.DockingPath;
import org.flexdock.docking.state.DockingState;
import org.flexdock.docking.state.MinimizationManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Created on 2005-06-03
 *
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 * @version $Id: DockingStateSerializer.java,v 1.20 2005-07-07 17:21:28 winnetou25 Exp $
 */
public class DockingStateSerializer implements ISerializer {

    private final static String OPENED_STATE = "opened";
    private final static String MINIMIZED_STATE = "minimized";
    private final static String FLOATING_STATE = "floating";

    /**
     * @see org.flexdock.perspective.persist.xml.ISerializer#serialize(org.w3c.dom.Document, java.lang.Object)
     */
    public Element serialize(Document document, Object object) {
        DockingState dockingState = (DockingState) object;

        Element dockingStateElement = document.createElement(PersistenceConstants.DOCKING_STATE_ELEMENT_NAME);
        dockingStateElement.setAttribute(PersistenceConstants.DOCKING_STATE_ATTRIBUTE_DOCKABLE_ID, dockingState.getDockableId());
        if (dockingState.getRelativeParentId() != null && !dockingState.getRelativeParentId().equals("")) {
            dockingStateElement.setAttribute(PersistenceConstants.DOCKING_STATE_ATTRIBUTE_RELATIVE_PARENT_ID, dockingState.getRelativeParentId());
        }
        dockingStateElement.setAttribute(PersistenceConstants.DOCKING_STATE_ATTRIBUTE_REGION, dockingState.getRegion().toLowerCase());

        if (dockingState.getSplitRatio() != DockingConstants.UNINITIALIZED_RATIO) {
            dockingStateElement.setAttribute(PersistenceConstants.DOCKING_STATE_ATTRIBUTE_SPLIT_RATIO, String.valueOf(dockingState.getSplitRatio()));
        }

        handleDockingState(dockingStateElement, dockingState);

        if (dockingState.isFloating()) {
            dockingStateElement.setAttribute(PersistenceConstants.DOCKING_STATE_ATTRIBUTE_FLOATING_GROUP_NAME, dockingState.getFloatingGroup());
        } else if (dockingState.isMinimized()) {
            int constraint = dockingState.getMinimizedConstraint();
            String presConstraint = getPresentationMinimizeConstraint(constraint);
            dockingStateElement.setAttribute(PersistenceConstants.DOCKING_STATE_ATTRIBUTE_MINIMIZE_CONSTRAINT, presConstraint);
        }

        if (dockingState.hasCenterPoint()) {
            ISerializer pointSerializer = SerializerRegistry.getSerializer(Point.class);
            Element pointElement = pointSerializer.serialize(document, dockingState.getCenterPoint());
            dockingStateElement.appendChild(pointElement);
        }

        if (dockingState.hasDockingPath()) {
            ISerializer dockingPathSerializer = SerializerRegistry.getSerializer(DockingPath.class);
            Element dockingPathElement = dockingPathSerializer.serialize(document, dockingState.getPath());
            dockingStateElement.appendChild(dockingPathElement);
        }

        return dockingStateElement;
    }

    private void handleDockingState(Element dockingStateElement, DockingState dockingState) {
        if (dockingState.isMinimized()) {
            dockingStateElement.setAttribute(PersistenceConstants.DOCKING_STATE_ATTRIBUTE_STATE, MINIMIZED_STATE);
        } else if (dockingState.isFloating()) {
            dockingStateElement.setAttribute(PersistenceConstants.DOCKING_STATE_ATTRIBUTE_STATE, FLOATING_STATE);
        } else {
            dockingStateElement.setAttribute(PersistenceConstants.DOCKING_STATE_ATTRIBUTE_STATE, OPENED_STATE);
        }
    }

    private String getPresentationMinimizeConstraint(int constraint) {
        switch (constraint) {

        case MinimizationManager.LEFT:
            return "left";
        case MinimizationManager.BOTTOM:
            return "bottom";
        case MinimizationManager.CENTER:
            return "center";
        case MinimizationManager.RIGHT:
            return "right";
        case MinimizationManager.TOP:
            return "top";
        case MinimizationManager.UNSPECIFIED_LAYOUT_CONSTRAINT:
            return "unspecified";

        default:
            throw new RuntimeException("Unknown dockbarEdge");
        }
    }

    private int getRealMinimizeConstraint(String presConstraint) {
        if (presConstraint.equals("left")) {
            return MinimizationManager.LEFT;
        } else if (presConstraint.equals("bottom")) {
            return MinimizationManager.BOTTOM;
        } else if (presConstraint.equals("center")) {
            return MinimizationManager.CENTER;
        } else if (presConstraint.equals("right")) {
            return MinimizationManager.RIGHT;
        } else if (presConstraint.equals("top")) {
            return MinimizationManager.TOP;
        }

        throw new RuntimeException("Minimization conversion error!");
    }

    public Object deserialize(Element element) {
        String dockableId = element.getAttribute(PersistenceConstants.DOCKING_STATE_ATTRIBUTE_DOCKABLE_ID);
        String relativeParentId = element.getAttribute(PersistenceConstants.DOCKING_STATE_ATTRIBUTE_RELATIVE_PARENT_ID);
        String region = element.getAttribute(PersistenceConstants.DOCKING_STATE_ATTRIBUTE_REGION);

        DockingState dockingState = new DockingState(dockableId);
        if (relativeParentId != null && !relativeParentId.equals("")) {
            dockingState.setRelativeParentId(relativeParentId);
        }

        dockingState.setRegion(region.toUpperCase());

        String splitRatioString = element.getAttribute(PersistenceConstants.DOCKING_STATE_ATTRIBUTE_SPLIT_RATIO);
        if (splitRatioString != null && !splitRatioString.equals("")) {
            float splitRatio = Float.parseFloat(splitRatioString);
            dockingState.setSplitRatio(splitRatio);
        }

        String dockingStateState = element.getAttribute(PersistenceConstants.DOCKING_STATE_ATTRIBUTE_STATE);
        if (dockingStateState.equals(FLOATING_STATE)) {
            String floatingGroupName = element.getAttribute(PersistenceConstants.DOCKING_STATE_ATTRIBUTE_FLOATING_GROUP_NAME);
            if (floatingGroupName != null && !floatingGroupName.equals("")) {
                dockingState.setFloatingGroup(floatingGroupName);
            }
        } else if (dockingStateState.equals(MINIMIZED_STATE)) {
            String minimizeConstraint = element.getAttribute(PersistenceConstants.DOCKING_STATE_ATTRIBUTE_MINIMIZE_CONSTRAINT);
            int minimizeConstraintInt = getRealMinimizeConstraint(minimizeConstraint);
            dockingState.setMinimizedConstraint(minimizeConstraintInt);
        }
        ISerializer pointDeserializer = SerializerRegistry.getSerializer(Point.class);
        NodeList pointNodeList = element.getElementsByTagName(PersistenceConstants.POINT_ELEMENT_NAME);
        if (pointNodeList.getLength() > 0 && pointNodeList.item(0) instanceof Element) {
            Element centerPointElement = (Element) pointNodeList.item(0);
            Point centerPoint = (Point) pointDeserializer.deserialize(centerPointElement);
            dockingState.setCenter(centerPoint);
        }

        ISerializer dockingPathDeserializer = SerializerRegistry.getSerializer(DockingPath.class);
        NodeList dockingPathNodeList = element.getElementsByTagName(PersistenceConstants.DOCKING_PATH_ELEMENT_NAME);
        if (dockingPathNodeList.getLength() > 0 && dockingPathNodeList.item(0) instanceof Element) {
            Element dockingPathElement = (Element) dockingPathNodeList.item(0);
            DockingPath dockingPath = (DockingPath) dockingPathDeserializer.deserialize(dockingPathElement);
            dockingState.setPath(dockingPath);
        }

        return dockingState;
    }

}
