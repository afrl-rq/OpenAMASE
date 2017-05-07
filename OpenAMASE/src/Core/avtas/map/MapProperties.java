// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map;

import avtas.properties.UserProperties;
import avtas.properties.UserProperty;
import java.awt.Color;
import java.awt.Dimension;

/**
 *
 * @author AFRL/RQQD
 */
public class MapProperties extends UserProperties {

    @UserProperty(Description = "<html>The type of rendering.  Use NearestHemisphere for quick drawing, <br/>"
            + "or WrapWorld for full globe painting.</html>")
    private MapPanel.RenderType RenderType = MapPanel.RenderType.NearestHemisphere;
    @UserProperty(Description = "Degrees", DisplayName = "Center Latitude",
            Category = "Initial View")
    private double CenterLat = 40;
    @UserProperty(Description = "Degrees", DisplayName = "Center Longitude",
            Category = "Initial View")
    private double CenterLon = -95;
    @UserProperty(Description = "The number of degrees longitude in the view",
            DisplayName = "Longitude Width", Category = "Initial View")
    private double LonWidth = 40;
    @UserProperty(Description = "Preferred Width for the map.", Category = "Size")
    private int PreferredWidth = 640;
    @UserProperty(Description = "Preferred Height for the map.", Category = "Size")
    private int PreferredHeight = 480;
    @UserProperty(Description = "Background Color for the map")
    private Color BackgroundColor = Color.BLACK;
    @UserProperty(Description = "Maximum rate to refresh the map (1/sec)")
    private int RefreshRate = 5;
    
    
    
    public final MapPanel map;

    public MapProperties(MapPanel map) {
        this.map = map;
    }

    /**
     * @return the renderType
     */
    public MapPanel.RenderType getRenderType() {
        return RenderType;
    }

    /**
     * @param renderType the renderType to set
     */
    public void setRenderType(MapPanel.RenderType renderType) {
        this.RenderType = renderType;
    }

    /**
     * @return the centerLat
     */
    public double getCenterLat() {
        return CenterLat;
    }

    /**
     * @param centerLat the centerLat to set
     */
    public void setCenterLat(double centerLat) {
        map.setCenter(centerLat, CenterLon);
        this.CenterLat = centerLat;
    }

    /**
     * @return the centerLon
     */
    public double getCenterLon() {
        return CenterLon;
    }

    /**
     * @param centerLon the centerLon to set
     */
    public void setCenterLon(double centerLon) {
        map.setCenter(CenterLat, centerLon);
        this.CenterLon = centerLon;
    }

    /**
     * @return the lonWidth
     */
    public double getLonWidth() {
        return LonWidth;
    }

    /**
     * @param lonWidth the lonWidth to set
     */
    public void setLonWidth(double lonWidth) {
        this.LonWidth = lonWidth;
        map.getProj().setLonWidth(lonWidth);
    }

    /**
     * @return the PreferredWidth
     */
    public int getPreferredWidth() {
        return PreferredWidth;
    }

    /**
     * @param PreferredWidth the PreferredWidth to set
     */
    public void setPreferredWidth(int PreferredWidth) {
        this.PreferredWidth = PreferredWidth;
        Dimension prefSize = map.getPreferredSize();
        if (prefSize != null) {
            map.setPreferredSize(new Dimension(PreferredWidth, prefSize.height));
        }
    }

    /**
     * @return the PreferredHeight
     */
    public int getPreferredHeight() {
        return PreferredHeight;
    }

    /**
     * @param PreferredHeight the PreferredHeight to set
     */
    public void setPreferredHeight(int PreferredHeight) {
        this.PreferredHeight = PreferredHeight;
        Dimension prefSize = map.getPreferredSize();
        if (prefSize != null) {
            map.setPreferredSize(new Dimension(prefSize.width, PreferredHeight));
        }
    }

    /**
     * @return the BackgroundColor
     */
    public Color getBackgroundColor() {
        return BackgroundColor;
    }

    /**
     * @param BackgroundColor the BackgroundColor to set
     */
    public void setBackgroundColor(Color BackgroundColor) {
        this.BackgroundColor = BackgroundColor;
        map.setBackground(BackgroundColor);
    }

    public int getRefreshRate() {
        return RefreshRate;
    }

    public void setRefreshRate(int RefreshRate) {
        this.RefreshRate = RefreshRate;
        if (map instanceof AnimatedMap) {
            ((AnimatedMap) map).setRefreshRate(RefreshRate);
        }
    }
    
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */