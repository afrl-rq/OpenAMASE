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

import javax.swing.tree.MutableTreeNode;

import org.flexdock.docking.state.LayoutNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created on 2005-06-27
 *
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 * @version $Id: AbstractLayoutNodeSerializer.java,v 1.7 2005-07-06 18:10:49 winnetou25 Exp $
 */
public abstract class AbstractLayoutNodeSerializer implements ISerializer {

    public Element serialize(Document document, Object object) {
        LayoutNode layoutNode = (LayoutNode) object;

        Element layoutNodeElement = getElement(document, object);

        ISerializer layoutNodeSerializer = SerializerRegistry.getSerializer(LayoutNode.class);
        int childCount = layoutNode.getChildCount();
        for (int i=0; i<childCount; i++) {
            MutableTreeNode childTreeNode = (MutableTreeNode) layoutNode.getChildAt(i);
            Element element = layoutNodeSerializer.serialize(document, childTreeNode);
            layoutNodeElement.appendChild(element);
        }

        return layoutNodeElement;
    }

    protected abstract Element getElement(Document document, Object o);

    public Object deserialize(Element element) {
        LayoutNode layoutNode = createLayoutNode();

        ISerializer layoutNodeSerializer = SerializerRegistry.getSerializer(LayoutNode.class);
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i<nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                Element childElement = (Element) node;
                LayoutNode childLayoutNode = (LayoutNode) layoutNodeSerializer.deserialize(childElement);
                layoutNode.add(childLayoutNode);
            }
        }

        return layoutNode;
    }

    protected abstract LayoutNode createLayoutNode();

}
