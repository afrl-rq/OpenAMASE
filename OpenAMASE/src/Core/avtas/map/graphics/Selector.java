// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package avtas.map.graphics;

import avtas.map.Proj;
import java.awt.Color;
import java.awt.Rectangle;

/**
 * A graphic that draws a selection box around another graphic. To select a
 * graphic, add it to the selector using {@link #addGraphic }. To remove a
 * graphic, call {@link #removeGraphic }. The default selector draws a plain
 * 1-pixel yellow box around the graphic.
 *
 * @author AFRL/RQQD
 */
public class Selector extends MapGraphic {

    private MapGraphic selectedGraphic = null;
    static int buffer = 2;

    public Selector() {
        setPainter(Color.YELLOW, 1);
    }

    /**
     * Adds a graphic to be "selected"
     */
    public synchronized void setSelectedGraphic(MapGraphic g) {
        this.selectedGraphic = g;
    }


    @Override
    public synchronized void project(Proj proj) {
        Rectangle bounds = selectedGraphic == null ? null : selectedGraphic.getBounds();
        if (bounds != null) {
            bounds = new Rectangle(bounds);
            bounds.grow(2, 2);
        }
        
        setScreenShape(bounds);
        setProjected(bounds != null);
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */