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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Created on 2005-06-03
 *
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 * @version $Id: RectangleSerializer.java,v 1.7 2005-07-06 18:10:48 winnetou25 Exp $
 */
public class RectangleSerializer implements ISerializer {

    /**
     * @see org.flexdock.perspective.persist.xml.ISerializer#serialize(org.w3c.dom.Document, java.lang.Object)
     */
    public Element serialize(Document document, Object object) {
        Rectangle rectangle = (Rectangle) object;

        Element rectangleElement = document.createElement(PersistenceConstants.RECTANGLE_ELEMENT_NAME);

        //TODO consider writing PersistenceDelegateAdapter
        ISerializer pointSerializer = SerializerRegistry.getSerializer(Point.class);
        //TODO consider writing PersistenceDelegateAdapter
        ISerializer dimensionSerializer = SerializerRegistry.getSerializer(Dimension.class);

        Element pointElement = pointSerializer.serialize(document, rectangle.getLocation());
        Element dimensionElement = dimensionSerializer.serialize(document, rectangle.getSize());

        rectangleElement.appendChild(pointElement);
        rectangleElement.appendChild(dimensionElement);

        return rectangleElement;
    }

    public Object deserialize(Element element) {
        ISerializer pointSerializer = SerializerRegistry.getSerializer(Point.class);
        ISerializer dimensionSerializer = SerializerRegistry.getSerializer(Dimension.class);

        Rectangle rectangle = new Rectangle();

        NodeList dimenstionNodeList = element.getElementsByTagName(PersistenceConstants.DIMENSION_ELEMENT_NAME);
        if (dimenstionNodeList.getLength() > 0 && dimenstionNodeList.item(0) instanceof Element) {
            Element dimensionElement = (Element) dimenstionNodeList.item(0);
            Dimension dimension = (Dimension) dimensionSerializer.deserialize(dimensionElement);
            rectangle.setSize(dimension);
        }

        NodeList pointNodeList = element.getElementsByTagName(PersistenceConstants.POINT_ELEMENT_NAME);
        if (pointNodeList.getLength() > 0 && pointNodeList.item(0) instanceof Element) {
            Element pointElement = (Element) pointNodeList.item(0);
            Point point = (Point) pointSerializer.deserialize(pointElement);
            rectangle.setLocation(point);
        }


        return rectangle;
    }

}
