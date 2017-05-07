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

import java.awt.Rectangle;
import java.util.Iterator;

import org.flexdock.docking.state.FloatingGroup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created on 2005-06-03
 *
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 * @version $Id: FloatingGroupSerializer.java,v 1.8 2005-07-06 18:10:48 winnetou25 Exp $
 */
public class FloatingGroupSerializer implements ISerializer {

    /**
     * @see org.flexdock.perspective.persist.xml.ISerializer#serialize(org.w3c.dom.Document, java.lang.Object)
     */
    public Element serialize(Document document, Object object) {
        FloatingGroup floatingGroup = (FloatingGroup) object;

        Element floatingGroupElement = document.createElement(PersistenceConstants.FLOATING_GROUP_ELEMENT_NAME);
        floatingGroupElement.setAttribute(PersistenceConstants.FLOATING_GROUP_ATTRIBUTE_NAME, floatingGroup.getName());

        ISerializer rectangleSerializer = SerializerRegistry.getSerializer(Rectangle.class);
        Element rectangleElement = rectangleSerializer.serialize(document, floatingGroup.getBounds());
        floatingGroupElement.appendChild(rectangleElement);

        for (Iterator it = floatingGroup.getDockableIterator(); it.hasNext();) {
            String dockableId = (String) it.next();
            Element dockableElement = document.createElement(PersistenceConstants.DOCKABLE_ELEMENT_NAME);
            dockableElement.setAttribute(PersistenceConstants.DOCKABLE_ATTRIBUTE_ID, dockableId);
            floatingGroupElement.appendChild(dockableElement);
        }

        return floatingGroupElement;
    }

    public Object deserialize(Element element) {
        String floatingGroupName = element.getAttribute(PersistenceConstants.FLOATING_GROUP_ATTRIBUTE_NAME);

        ISerializer rectangleSerializer = SerializerRegistry.getSerializer(Rectangle.class);
        FloatingGroup floatingGroup = new FloatingGroup(floatingGroupName);

        NodeList rectangleNodeList = element.getElementsByTagName(PersistenceConstants.RECTANGLE_ELEMENT_NAME);
        if (rectangleNodeList.getLength() > 0 && rectangleNodeList.item(0) instanceof Element) {
            Node rectangleNode = rectangleNodeList.item(0);
            if (rectangleNode instanceof Element) {
                Element rectangleElement = (Element) rectangleNode;
                Rectangle rectangle = (Rectangle) rectangleSerializer.deserialize(rectangleElement);
                floatingGroup.setBounds(rectangle);
            }
        }

        NodeList dockableNodeList = element.getElementsByTagName(PersistenceConstants.DOCKABLE_ELEMENT_NAME);
        for (int i=0; i<dockableNodeList.getLength(); i++) {
            Node dockableNode = dockableNodeList.item(i);
            if (dockableNode instanceof Element) {
                Element dockableElement = (Element) dockableNode;
                String dockableId = dockableElement.getAttribute(PersistenceConstants.DOCKABLE_ATTRIBUTE_ID);
                floatingGroup.addDockable(dockableId);
            }
        }

        return floatingGroup;
    }

}
