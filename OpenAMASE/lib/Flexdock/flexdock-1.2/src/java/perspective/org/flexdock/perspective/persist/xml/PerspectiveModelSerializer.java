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

import java.util.ArrayList;

import org.flexdock.perspective.Perspective;
import org.flexdock.perspective.persist.PerspectiveModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created on 2005-06-03
 *
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 * @version $Id: PerspectiveModelSerializer.java,v 1.8 2005-07-06 18:10:48 winnetou25 Exp $
 */
public class PerspectiveModelSerializer implements ISerializer {

    public Element serialize(Document document, Object object) {
        PerspectiveModel perspectiveModel = (PerspectiveModel) object;

        Element perspectiveModelElement = document.createElement(PersistenceConstants.PERSPECTIVE_MODEL_ELEMENT_NAME);

        perspectiveModelElement.setAttribute(PersistenceConstants.PERSPECTIVE_MODEL_ATTRIBUTE_CURRENT_PERSPECTIVE_ID, perspectiveModel.getCurrentPerspective());
        perspectiveModelElement.setAttribute(PersistenceConstants.PERSPECTIVE_MODEL_ATTRIBUTE_DEFAULT_PERSPECTIVE_ID, perspectiveModel.getDefaultPerspective());

        ISerializer perspectiveSerializer = SerializerRegistry.getSerializer(Perspective.class);

        Perspective[] perspectives = perspectiveModel.getPerspectives();
        for (int i = 0; i < perspectives.length; i++) {
            Perspective perspective = perspectives[i];
            Element perspectiveElement = perspectiveSerializer.serialize(document, perspective);
            perspectiveModelElement.appendChild(perspectiveElement);
        }

        return perspectiveModelElement;
    }

    public Object deserialize(Element element) {
        String currentPerspectiveId = element.getAttribute(PersistenceConstants.PERSPECTIVE_MODEL_ATTRIBUTE_CURRENT_PERSPECTIVE_ID);
        String defaultPerspectiveId = element.getAttribute(PersistenceConstants.PERSPECTIVE_MODEL_ATTRIBUTE_DEFAULT_PERSPECTIVE_ID);

        NodeList perspectivesList = element.getElementsByTagName(PersistenceConstants.PERSPECTIVE_ELEMENT_NAME);
        ISerializer perspectiveSerializer = SerializerRegistry.getSerializer(Perspective.class);
        ArrayList perspectives = new ArrayList();
        for (int i=0; i<perspectivesList.getLength(); i++) {
            Node node = perspectivesList.item(i);
            if (node instanceof Element) {
                Element perspectiveElement = (Element) node;
                Perspective perspective = (Perspective) perspectiveSerializer.deserialize(perspectiveElement);
                perspectives.add(perspective);
            }
        }

        Perspective[] perspectivesArray = (Perspective[]) perspectives.toArray(new Perspective[perspectives.size()]);
        PerspectiveModel perspectiveModel = new PerspectiveModel(defaultPerspectiveId, currentPerspectiveId, perspectivesArray);

        return perspectiveModel;
    }

}
