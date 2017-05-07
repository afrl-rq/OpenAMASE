// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.layers;


import avtas.map.graphics.MapGraphic;
import java.awt.Color;
import avtas.map.graphics.MapGraphicsList;
import avtas.map.graphics.MapLine;
import avtas.map.Proj;
import avtas.properties.UserProperty;
import avtas.properties.XmlSerializer;
import avtas.xml.Element;

/**
 * Implements a simple Graticule (lines of latitude/longitude)
 * @author AFRL/RQQD
 */
public class Graticule extends GraphicsLayer<MapGraphic>{
    
    MapGraphicsList<MapGraphic> oneDegList = new MapGraphicsList<MapGraphic>();
    MapGraphicsList<MapGraphic> fiveDegList = new MapGraphicsList<MapGraphic>();
    MapGraphicsList<MapGraphic> tenDegList = new MapGraphicsList<MapGraphic>();
    
    private int factor = 1;
    
    private int y1, y2, x1, x2, firstX, firstY, latStep, lonStep;
    
    @UserProperty(Description = "Color for lines in the Graticule.")
    private Color LineColor = Color.WHITE;
    
    /** Creates a new instance of Graticule */
    public Graticule() {
        
        for(double lon = -180; lon < 180; lon ++ ) {
            MapLine line = new MapLine( -90., lon, 90., lon);
            if ( lon % 10 == 0)
                tenDegList.add(line);
            else if ( lon % 5 == 0)
                fiveDegList.add(line);
            else
                oneDegList.add(line);
        }
        
        for(double lat = -90.; lat <= 90.; lat++) {
            MapLine line1 = new MapLine(lat, 0, lat, 180);
            MapLine line2 = new MapLine(lat, 0, lat, -180);
            
            if ( lat % 10 == 0) {
                tenDegList.add(line1);
                tenDegList.add(line2);
            } else if ( lat % 5 == 0) {
                fiveDegList.add(line1);
                fiveDegList.add(line2);
            } else {
                oneDegList.add(line1);
                oneDegList.add(line2);
            }
        }
        
        add(oneDegList);
        add(fiveDegList);
        add(tenDegList);
        
    }

    public void setConfiguration(Element node) {
        XmlSerializer.deserialize(node, this);
        //setPaint( Colors.getColor( XMLUtil.getValue(node, "Color", "WHITE" ), Color.WHITE));
        setPaint(getLineColor(), 1);
    }
    
    
    public void project(Proj proj) {
        
        oneDegList.setVisible(false);
        fiveDegList.setVisible(false);
        tenDegList.setVisible(true);
        
        
        if (proj.getPixPerLon() > 50) {
            oneDegList.setVisible(true);
        }
        if (proj.getPixPerLon() > 20) {
            fiveDegList.setVisible(true);
        }
        
        super.project(proj);
        
    }

    /**
     * @return the LineColor
     */
    public Color getLineColor() {
        return LineColor;
    }

    /**
     * @param LineColor the LineColor to set
     */
    public void setLineColor(Color LineColor) {
        this.LineColor = LineColor;
        refresh();
    }
    

    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */