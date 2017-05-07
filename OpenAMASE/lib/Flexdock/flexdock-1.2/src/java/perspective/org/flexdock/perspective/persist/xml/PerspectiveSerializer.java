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

import org.flexdock.perspective.Layout;
import org.flexdock.perspective.LayoutSequence;
import org.flexdock.perspective.Perspective;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Created on 2005-06-03
 *
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 * @version $Id: PerspectiveSerializer.java,v 1.9 2006-12-20 20:55:21 kschaefe Exp $
 */
public class PerspectiveSerializer implements ISerializer {

    /**
     * {@inheritDoc}
     */
    public Element serialize(Document document, Object object) {
        Perspective perspective = (Perspective) object;

        Element perspectiveElement = document.createElement(PersistenceConstants.PERSPECTIVE_ELEMENT_NAME);
        perspectiveElement.setAttribute(PersistenceConstants.PERSPECTIVE_ATTRIBUTE_ID, perspective.getPersistentId());
        perspectiveElement.setAttribute(PersistenceConstants.PERSPECTIVE_ATTRIBUTE_NAME, perspective.getName());

        ISerializer layoutSerializer = SerializerRegistry.getSerializer(Layout.class);
        Element layoutElement = layoutSerializer.serialize(document, perspective.getLayout());

        perspectiveElement.appendChild(layoutElement);

        ISerializer layoutSequenceSerializer = SerializerRegistry.getSerializer(LayoutSequence.class);
        if (perspective.getInitialSequence() != null) {
            Element layoutSequenceElement = layoutSequenceSerializer.serialize(document, perspective.getInitialSequence());
            perspectiveElement.appendChild(layoutSequenceElement);
        }

        return perspectiveElement;
    }

    /**
     * {@inheritDoc}
     */
    public Object deserialize(Element element) {
        String perspectiveId = element.getAttribute(PersistenceConstants.PERSPECTIVE_ATTRIBUTE_ID);
        String perspectiveName = element.getAttribute(PersistenceConstants.PERSPECTIVE_ATTRIBUTE_NAME);

        Perspective perspective = new Perspective(perspectiveId, perspectiveName);

        NodeList layoutNodeList = element.getElementsByTagName(PersistenceConstants.LAYOUT_ELEMENT_NAME);
        ISerializer layoutSerializer = SerializerRegistry.getSerializer(Layout.class);
        if (layoutNodeList.getLength() > 0 && layoutNodeList.item(0) instanceof Element) {
            Element layoutElement = (Element) layoutNodeList.item(0);
            Layout layout = (Layout) layoutSerializer.deserialize(layoutElement);
            perspective.setLayout(layout);
        }

        NodeList layoutSequenceNodeList = element.getElementsByTagName(PersistenceConstants.LAYOUT_SEQUENCE_ELEMENT_NAME);
        ISerializer layoutSequenceSerializer = SerializerRegistry.getSerializer(LayoutSequence.class);
        if (layoutSequenceNodeList.getLength() > 0 && layoutSequenceNodeList.item(0) instanceof Element) {
            Element layoutSequenceElement = (Element) layoutSequenceNodeList.item(0);
            LayoutSequence layoutSequence = (LayoutSequence) layoutSequenceSerializer.deserialize(layoutSequenceElement);
            perspective.setInitialSequence(layoutSequence);
        }

        return perspective;
    }

}
