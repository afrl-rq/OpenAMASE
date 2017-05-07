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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created on 2005-06-03
 *
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 * @version $Id: DimensionSerializer.java,v 1.7 2005-07-07 22:24:01 winnetou25 Exp $
 */
public class DimensionSerializer implements ISerializer {

    /**
     * @see org.flexdock.perspective.persist.xml.ISerializer#serialize(org.w3c.dom.Document, java.lang.Object)
     */
    public Element serialize(Document document, Object object) {
        Dimension dimension = (Dimension) object;

        Element dimensionElement = document.createElement(PersistenceConstants.DIMENSION_ELEMENT_NAME);

        dimensionElement.setAttribute(PersistenceConstants.DIMENSION_ATTRIBUTE_HEIGHT, String.valueOf(dimension.height));
        dimensionElement.setAttribute(PersistenceConstants.DIMENSION_ATTRIBUTE_WIDTH, String.valueOf(dimension.width));

        return dimensionElement;
    }

    public Object deserialize(Element element) {
        String width = element.getAttribute(PersistenceConstants.DIMENSION_ATTRIBUTE_WIDTH);
        String height = element.getAttribute(PersistenceConstants.DIMENSION_ATTRIBUTE_HEIGHT);

        return new Dimension(Integer.parseInt(width), Integer.parseInt(height));
    }

}
