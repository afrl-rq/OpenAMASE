// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================


package avtas.map.edit;

import avtas.map.graphics.MapArc;
import avtas.map.graphics.MapCircle;
import avtas.map.graphics.MapEllipse;
import avtas.map.graphics.MapGraphic;
import avtas.map.graphics.MapLine;
import avtas.map.graphics.MapMarker;
import avtas.map.graphics.MapPoly;
import avtas.map.graphics.MapRect;

/**
 * Utility class for obtaining an editor for a given map shape
 * @author AFRL/RQQD
 */
public class MapEditors {
    
    public static GraphicEditor wrapEditor(MapGraphic g) {
        GraphicEditor editor = null;
        
        if (g instanceof MapPoly) {
            editor = new PolyEditor((MapPoly) g);
        }
        else if (g instanceof MapMarker) {
            editor = new MarkerEditor((MapMarker) g);
        }
        else if (g instanceof MapCircle) {
            editor = new CircleEditor((MapCircle) g);
        }
        else if (g instanceof MapEllipse) {
            editor = new EllipseEditor((MapEllipse) g);
        }
        else if (g instanceof MapRect) {
            editor = new RectEditor((MapRect) g);
        }
        else if (g instanceof MapLine) {
            editor = new LineEditor((MapLine) g);
        }
        else if (g instanceof MapArc) {
            editor = new ArcEditor((MapArc) g);
        }
        
        return editor;
    }
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */