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

/**
 * Created on 2005-06-03
 *
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 * @version $Id: PersistenceConstants.java,v 1.19 2005-07-06 17:27:45 winnetou25 Exp $
 */
public interface PersistenceConstants {

    //perspective model constants
    String PERSPECTIVE_MODEL_ELEMENT_NAME = "PerspectiveModel";
    String PERSPECTIVE_MODEL_ATTRIBUTE_CURRENT_PERSPECTIVE_ID = "currentPerspectiveId";
    String PERSPECTIVE_MODEL_ATTRIBUTE_DEFAULT_PERSPECTIVE_ID = "defaultPerspectiveId";

    //perspective constants
    String PERSPECTIVE_ELEMENT_NAME = "Perspective";
    String PERSPECTIVE_ATTRIBUTE_ID = "id";
    String PERSPECTIVE_ATTRIBUTE_NAME = "name";

    //layout constants
    String LAYOUT_ELEMENT_NAME = "Layout";

    //floating group constants
    String FLOATING_GROUP_ELEMENT_NAME = "FloatingGroup";
    String FLOATING_GROUP_ATTRIBUTE_NAME = "name";

    //rectangle constants
    String RECTANGLE_ELEMENT_NAME = "Rectangle";

    //point constants
    String POINT_ELEMENT_NAME = "Point";
    String POINT_ATTRIBUTE_X = "x";
    String POINT_ATTRIBUTE_Y = "y";

    //dimension constants
    String DIMENSION_ELEMENT_NAME = "Dimension";
    String DIMENSION_ATTRIBUTE_WIDTH = "width";
    String DIMENSION_ATTRIBUTE_HEIGHT = "height";

    //docking state constants
    String DOCKING_STATE_ELEMENT_NAME = "DockingState";

    String DOCKING_STATE_ATTRIBUTE_STATE = "state";
    String DOCKING_STATE_ATTRIBUTE_SPLIT_RATIO = "splitRatio";
    String DOCKING_STATE_ATTRIBUTE_MINIMIZE_CONSTRAINT = "minimizeConstraint";
    String DOCKING_STATE_ATTRIBUTE_DOCKABLE_ID = "dockableId";
    String DOCKING_STATE_ATTRIBUTE_RELATIVE_PARENT_ID = "relativeParentId";
    String DOCKING_STATE_ATTRIBUTE_REGION = "dockingRegion";
    String DOCKING_STATE_ATTRIBUTE_FLOATING_GROUP_NAME = "floatingGroupName";

    String DOCKING_PATH_ELEMENT_NAME = "DockingPath";
    String DOCKING_PATH_ATTRIBUTE_ROOT_PORT_ID = "rootPortId";
    String DOCKING_PATH_ATTRIBUTE_IS_TABBED = "isTabbed";
    String DOCKING_PATH_ATTRIBUTE_SIBLING_ID = "siblingId";

    String SPLIT_NODE_ELEMENT_NAME = "SplitNode";
    String SPLIT_NODE_ATTRIBUTE_ORIENTATION = "orientation";
    String SPLIT_NODE_ATTRIBUTE_REGION = "region";
    String SPLIT_NODE_ATTRIBUTE_PERCENTAGE = "percentage";
    String SPLIT_NODE_ATTRIBUTE_SIBLING_ID = "siblingId";
    String SPLIT_NODE_ATTRIBUTE_DOCKING_REGION = "dockingRegion";

    String LAYOUT_SEQUENCE_ELEMENT_NAME = "LayoutSequence";

    String DOCKABLE_NODE_ELEMENT_NAME = "DockableNode";
    String DOCKABLE_NODE_ATTRIBUTE_DOCKABLE_ID = "dockableId";

    String DOCKING_PORT_NODE_ELEMENT_NAME = "DockingPortNode";

    String DOCKABLE_ELEMENT_NAME = "Dockable";
    String DOCKABLE_ATTRIBUTE_ID = "id";

}
