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

import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.state.LayoutNode;
import org.flexdock.docking.state.tree.SplitNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created on 2005-06-23
 *
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 * @version $Id: SplitNodeSerializer.java,v 1.9 2005-07-06 18:10:48 winnetou25 Exp $
 */
public class SplitNodeSerializer extends AbstractLayoutNodeSerializer implements ISerializer {

    public Element serialize(Document document, Object object) {
        SplitNode splitNode = (SplitNode) object;

        Element splitNodeElement = super.serialize(document, object);

        if (splitNode.getSiblingId() != null && !"".equals(splitNode.getSiblingId())) {
            splitNodeElement.setAttribute(PersistenceConstants.SPLIT_NODE_ATTRIBUTE_SIBLING_ID, splitNode.getSiblingId());
        }
        splitNodeElement.setAttribute(PersistenceConstants.SPLIT_NODE_ATTRIBUTE_ORIENTATION, splitNode.getOrientationDesc());
        splitNodeElement.setAttribute(PersistenceConstants.SPLIT_NODE_ATTRIBUTE_REGION, splitNode.getRegionDesc());
        splitNodeElement.setAttribute(PersistenceConstants.SPLIT_NODE_ATTRIBUTE_PERCENTAGE, String.valueOf(splitNode.getPercentage()));

        if (splitNode.getDockingRegion() != null) {
            splitNodeElement.setAttribute(PersistenceConstants.SPLIT_NODE_ATTRIBUTE_DOCKING_REGION, splitNode.getDockingRegion().toLowerCase());
        }

        return splitNodeElement;
    }

    protected Element getElement(Document document, Object o) {
        return document.createElement(PersistenceConstants.SPLIT_NODE_ELEMENT_NAME);
    }

    public Object deserialize(Element element) {

        SplitNode splitNode = (SplitNode) super.deserialize(element);

        String siblingId = element.getAttribute(PersistenceConstants.SPLIT_NODE_ATTRIBUTE_SIBLING_ID);
        String orientationString = element.getAttribute(PersistenceConstants.SPLIT_NODE_ATTRIBUTE_ORIENTATION);
        String regionString = element.getAttribute(PersistenceConstants.SPLIT_NODE_ATTRIBUTE_REGION);
        String percentage = element.getAttribute(PersistenceConstants.SPLIT_NODE_ATTRIBUTE_PERCENTAGE);
        String dockingRegion = element.getAttribute(PersistenceConstants.SPLIT_NODE_ATTRIBUTE_DOCKING_REGION);

        int orientation = DockingConstants.UNINITIALIZED;
        if (orientationString.equals("vertical")) {
            orientation = DockingConstants.VERTICAL;
        } else if (orientationString.equals("horizontal")) {
            orientation = DockingConstants.HORIZONTAL;
        }

        int region = DockingConstants.UNINITIALIZED;
        if (regionString.equals("top")) {
            region = DockingConstants.TOP;
        } else if (regionString.equals("bottom")) {
            region = DockingConstants.BOTTOM;
        } else if (regionString.equals("left")) {
            region = DockingConstants.LEFT;
        } else if (regionString.equals("right")) {
            region = DockingConstants.RIGHT;
        }

        splitNode.setOrientation(orientation);
        splitNode.setRegion(region);
        splitNode.setPercentage(Float.parseFloat(percentage));
        if (siblingId != null && !"".equals(siblingId)) {
            splitNode.setSiblingId(siblingId);
        }
        if (dockingRegion != null && !dockingRegion.equals("")) {
            splitNode.setDockingRegion(dockingRegion.toUpperCase());
        }

        return splitNode;
    }

    protected LayoutNode createLayoutNode() {
        return new SplitNode(-1, -1, -1.0f, null);
    }

}
