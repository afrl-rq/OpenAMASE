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
import java.awt.Shape;

/**
 * A MapGraphic that is in screen coordinates.  This graphic does not change based
 * on the projection of the map.
 * @author AFRL/RQQD
 */
public class ScreenGraphic extends MapGraphic{

    /** This graphic wraps a java 2D shape to display it in screen coordinates.  */
    public ScreenGraphic(Shape s) {
        setScreenShape(s);
    }
    
    public ScreenGraphic() {
    }
    
    

    @Override
    public void project(Proj proj) {
        // this is always projected since it is in screen coordinates
        setProjected(true);
    }

    /** Returns the underlying shape in this graphic */
    public Shape getShape() {
        return super.screenShape;
    }

    /** Sets the underlying screen-coordinate shape for this graphic */
    public void setShape(Shape s) {
        screenShape = s;
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */