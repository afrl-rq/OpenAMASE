// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.map;

import afrl.cmasi.LoiterAction;
import afrl.cmasi.LoiterType;
import avtas.map.Proj;
import avtas.map.edit.DragPoint;
import avtas.map.edit.GraphicEditor;
import avtas.map.graphics.MapArc;
import avtas.map.graphics.MapCircle;
import avtas.map.graphics.MapGraphicsList;
import avtas.map.graphics.MapLine;
import avtas.map.graphics.MapMarker;

import avtas.util.NavUtils;
import java.awt.BasicStroke;
import java.awt.Stroke;

import static java.lang.Math.*;

/**
 *
 * @author AFRL/RQQD
 */
public class LoiterGraphic extends MapGraphicsList {

    public static final int TYPE_ORBIT = 0;
    public static final int TYPE_FIGURE_EIGHT = 1;
    public static final int TYPE_RACETRACK = 2;
    public static final int DIR_CLOCKWISE = 0;
    public static final int DIR_COUNTERCLOCKWISE = 1;
    Stroke dashStroke = new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1f,
            new float[]{5, 5}, 0f);
    LoiterAction a;

    public LoiterGraphic(LoiterAction a) {
        this.a = a;
        makeGraphics();
        setRefObject(a);
    }
    
    
    public GraphicEditor getEditor() {
        GraphicEditor editor = new GraphicEditor(this) {
            
            DragPoint dp = new DragPoint(a.getLocation().getLatitude(), a.getLocation().getLongitude(), this);
            
            @Override
            public void pointDragged(DragPoint dp) {
                a.getLocation().setLatitude(dp.getLat());
                a.getLocation().setLongitude(dp.getLon());
                makeGraphics();
            }
            
            @Override
            public void setBounds(double degLat1, double degLon1, double degLat2, double degLon2) {
                a.getLocation().setLatitude(degLat1);
                a.getLocation().setLongitude(degLon1);
                makeGraphics();
            }
            
            @Override
            protected void resetEditor() {
                dp.setLat(a.getLocation().getLatitude());
                dp.setLon(a.getLocation().getLongitude());
            }
            
            @Override
            public void translateGraphic(double deltaLat, double deltaLon, Proj proj) {
                a.getLocation().setLatitude(a.getLocation().getLatitude() + deltaLat);
                a.getLocation().setLongitude(a.getLocation().getLongitude() + deltaLon);
                makeGraphics();
            }
        };
        return editor;
    }
    
    

    public void makeGraphics() {
        
        clear();

        double clat = Math.toRadians(a.getLocation().getLatitude());
        double clon = Math.toRadians(a.getLocation().getLongitude());
        MapMarker cp = new MapMarker(a.getLocation().getLatitude(), a.getLocation().getLongitude());
        add(cp);
        cp.setRefObject(a.getLocation());
        MapCircle rangeCirc;


        switch (a.getLoiterType()) {
            case VehicleDefault:
            case Circular:
                rangeCirc = new MapCircle(toDegrees(clat), toDegrees(clon), a.getRadius());
                add(rangeCirc);
                break;
            case Racetrack:
            case FigureEight:

                double leglen2 = a.getLength() * 0.5;
                double radius = a.getRadius();
                if (a.getLoiterType() == LoiterType.FigureEight) {
                    leglen2 += radius;
                }
                double axis = Math.toRadians(a.getAxis());

                //upper turn circle
                double[] arcCenter1 = NavUtils.getLatLon(clat, clon, leglen2, axis);
                add( new MapArc(toDegrees(arcCenter1[0]), 
                        toDegrees(arcCenter1[1]), toDegrees(axis + PI / 2f), -180, radius) );

                //lower turn circle
                double[] arcCenter2 = NavUtils.getLatLon(clat, clon, leglen2, axis + PI);
                add( new MapArc(toDegrees(arcCenter2[0]), 
                        toDegrees(arcCenter2[1]), toDegrees(axis + PI / 2f), 180, radius) );


                //straight-leg
                double[] pt1 = NavUtils.getLatLon(arcCenter1[0], arcCenter1[1], radius, axis + PI / 2f);
                double[] pt2 = NavUtils.getLatLon(arcCenter2[0], arcCenter2[1], radius, axis + PI / 2f);

                //opposite straight-leg
                double[] pt3 = NavUtils.getLatLon(arcCenter1[0], arcCenter1[1], radius, axis - PI / 2f);
                double[] pt4 = NavUtils.getLatLon(arcCenter2[0], arcCenter2[1], radius, axis - PI / 2f);

                if (a.getLoiterType() == LoiterType.Racetrack) {
                    add(new MapLine(toDegrees(pt1[0]), toDegrees(pt1[1]), 
                            toDegrees(pt2[0]), toDegrees(pt2[1])));
                    add(new MapLine(toDegrees(pt3[0]), toDegrees(pt3[1]), 
                            toDegrees(pt4[0]), toDegrees(pt4[1])));
                }
                else if(a.getLoiterType() == LoiterType.FigureEight)  {
                    add(new MapLine(toDegrees(pt1[0]), toDegrees(pt1[1]), 
                            toDegrees(pt4[0]), toDegrees(pt4[1])));
                    add(new MapLine(toDegrees(pt2[0]), toDegrees(pt2[1]), 
                            toDegrees(pt3[0]), toDegrees(pt3[1])));
                }

        }


    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */