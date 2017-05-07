// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.edit;

import avtas.map.graphics.MapGraphic;


/**
 * Listens for changes to graphics through edit actions
 *
 * @author AFRL/RQQD
 */
public interface EditListener {
    
    public static final int EDIT_START = 0;
    public static final int EDIT_ONGOING = 1;
    public static final int EDIT_END = 2;
    public static final int EDIT_CANCELED = 3;
    public static final int EDIT_SELECTED = 4;
    public static final int EDIT_RECT_SELECTED = 5;    
    

    /**
     * Informs the listener of an edit on a graphic
     * @param shape the shape being edited
     * @param mode the editing mode
     */
    public void editPerformed(GraphicEditor shape, int mode);

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */