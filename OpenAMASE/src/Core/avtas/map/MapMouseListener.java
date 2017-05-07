// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map;

import java.awt.event.MouseEvent;

/**
 *
 * @author AFRL/RQQD
 */
public interface MapMouseListener {
    
    public void mouseMoved( MouseEvent e, double lat, double lon);
    
    public void mouseClicked( MouseEvent e, double lat, double lon);
    
    public void mouseDragged( MouseEvent e, double lat, double lon);
    
    public void mousePressed( MouseEvent e, double lat, double lon);
    
    public void mouseReleased( MouseEvent e, double lat, double lon);
    
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */