// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.edit;

import avtas.map.Proj;
import avtas.map.graphics.MapMarker;
import avtas.map.graphics.Painter;
import java.awt.Color;
import java.awt.Rectangle;

/**
 *
 * @author AFRL/RQQD
 */
public class DragPoint extends MapMarker {

    GraphicEditor editor = null;
    Proj view;

    public DragPoint(double degLat, double degLon, GraphicEditor graphic) {
        this(graphic);
        setLat(degLat);
        setLon(degLon);
    }

    public DragPoint(GraphicEditor editor) {
        super();
        setEditor(editor);
        setMarkerShape(new Rectangle(0, 0, 7, 7));
        setFill(Color.LIGHT_GRAY);
        setPainter(Painter.createPainter(Color.WHITE) );
    }
    
    public void setEditor(GraphicEditor editor) {
        this.editor = editor;
    }

    public void moveTo(int x, int y) {
        if (view != null) {
            moveTo( view.getLat(x, y),  view.getLon(x, y) );
        }
    }
    
    public void moveTo(double lat, double lon)
    {        
        setLat( lat );
        setLon( lon );
        //must reproject to get new x/y points
        super.project(view);
        editor.pointDragged(this);
    }

    @Override
    public void project(Proj view) {
        this.view = view;
        super.project(view);
    }

    /** Returns the positional index of this point within its point list; or -1 if we don't have a valid point list */
    public int getLocationIndex()
    {
        if(editor!=null){
            return editor.getDragPoints().indexOf(this);        
        }else{
            return -1;
        }        
    }

    /** Returns true if this point is the first in its point list */
    public boolean isFirst()
    {
        if( getLocationIndex()==0 ){
            return true;
        }

        return false;
}
    
    /** Returns true if this point is the last in its point list */
    public boolean isLast()
    {
        if( getLocationIndex()==editor.getDragPoints().size()-1 ){
            return true;
        }
        
        return false;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */