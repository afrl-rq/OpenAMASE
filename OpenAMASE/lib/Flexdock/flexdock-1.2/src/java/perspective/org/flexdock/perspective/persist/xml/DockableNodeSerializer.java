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

import org.flexdock.docking.state.tree.DockableNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created on 2005-06-27
 *
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 * @version $Id: DockableNodeSerializer.java,v 1.7 2005-07-06 18:10:49 winnetou25 Exp $
 */
public class DockableNodeSerializer implements ISerializer {

    public Element serialize(Document document, Object object) {
        DockableNode dockableNode = (DockableNode) object;

        Element dockableNodeElement = document.createElement(PersistenceConstants.DOCKABLE_NODE_ELEMENT_NAME);
        dockableNodeElement.setAttribute(PersistenceConstants.DOCKABLE_NODE_ATTRIBUTE_DOCKABLE_ID, dockableNode.getDockableId());

        return dockableNodeElement;
    }

    public Object deserialize(Element element) {
        String dockableId = element.getAttribute(PersistenceConstants.DOCKABLE_NODE_ATTRIBUTE_DOCKABLE_ID);

        DockableNode dockableNode = new DockableNode();
        dockableNode.setDockableId(dockableId);

        return dockableNode;
    }

}
