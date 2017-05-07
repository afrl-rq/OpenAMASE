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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created on 2005-06-03
 *
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 * @version $Id: PointSerializer.java,v 1.8 2005-07-06 18:10:47 winnetou25 Exp $
 */
public class PointSerializer implements ISerializer {

//    private String m_tagName = null;
//
//    public PointSerializer(String tagName) {
//        if (tagName == null) throw new IllegalArgumentException("tagName cannot be null");
//        m_tagName = tagName;
//    }

    /**
     * @see org.flexdock.perspective.persist.xml.ISerializer#serialize(org.w3c.dom.Document, java.lang.Object)
     */
    public Element serialize(Document document, Object object) {
        Point point = (Point) object;

        Element pointElement = document.createElement(PersistenceConstants.POINT_ELEMENT_NAME);
        pointElement.setAttribute(PersistenceConstants.POINT_ATTRIBUTE_X, String.valueOf(point.x));
        pointElement.setAttribute(PersistenceConstants.POINT_ATTRIBUTE_Y, String.valueOf(point.y));

        return pointElement;
    }

    public Object deserialize(Element element) {
        String xString = element.getAttribute(PersistenceConstants.POINT_ATTRIBUTE_X);
        String yString = element.getAttribute(PersistenceConstants.POINT_ATTRIBUTE_Y);

        return new Point(Integer.parseInt(xString), Integer.parseInt(yString));
    }

}
