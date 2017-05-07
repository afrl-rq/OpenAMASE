// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.swing;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Simplifies mouse drag movements by tracking drag distances internally.  
 * Implement the {@link #mouseDragged(java.awt.event.MouseEvent, int, int) } method 
 * to receive drag events.
 * @author AFRL/RQQD
 */
public abstract class MouseDragAdapter extends MouseAdapter {

    MouseEvent drag = null;

    public abstract void mouseDragged(MouseEvent e, int dx, int dy);

    @Override
    public final void mouseDragged(MouseEvent e) {
        if (drag != null) {
            int dx = e.getPoint().x - drag.getPoint().x;
            int dy = e.getPoint().y - drag.getPoint().y;
            mouseDragged(e, dx, dy);
            
        }
        drag = e;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        drag = e;
    }

    

    
    
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */