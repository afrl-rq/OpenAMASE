// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.map;

import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.AirVehicleState;
import afrl.cmasi.EntityConfiguration;
import afrl.cmasi.EntityState;
import avtas.amase.ui.IconTools;
import avtas.util.ObjectUtils;
import avtas.map.graphics.MapGraphic;
import avtas.map.graphics.MapGraphicsList;
import avtas.map.graphics.MapIcon;
import avtas.map.graphics.MapText;
import java.awt.Color;
import java.awt.Image;

/** 
 * <code>VehicleGraphic</code> class creates and updates an icon to represent 
 * an entity and displays in the proper location on the map layer.  
 * Also creates the the sensor footprint {@link FootprintGraphic }
 *
 * @author AFRL/RQQD
 */
public class EntityGraphic extends MapGraphicsList<MapGraphic> {

    protected MapIcon icon;
    protected FootprintGraphic footprintGraphic;
    //TrailGraphic trail = new TrailGraphic();
    protected MapText vehName;
    protected String name = "";
    protected long id = 0;

    /**
     * Creates a <Code>VehicleGraphic</code> to represent the vehicle.  Creates and updates the
     * vehicle <code>FootprintGraphic</code> and <code>TrailGraphic</code>
     *
     * @param ec entity state at start
     * @param color color of icon
     */
    public EntityGraphic(EntityConfiguration ec, Image icon, Color color) {

        this.name = ec.getLabel();
        this.id = ec.getID();
        setRefObject(this.id);
        //trail.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, new float[]{1f, 2f}, 0.5f));
        //trail.setPaint(Color.RED);
        //add(trail);
        footprintGraphic = new FootprintGraphic(ec.getPayloadConfigurationList(), color);
        add(footprintGraphic);
        
        //Image shadowImage = IconTools.getFilledImage(icon, 12, 12, 4, Color.WHITE, color);
        Image shadowImage = IconTools.getOutlinedImage(icon, 24, 24, 1, Color.WHITE);
        this.icon = new MapIcon(shadowImage);
        add(this.icon);
        
        vehName = new MapText(0, 0, String.valueOf(id), 0, 15);
        if (!ObjectUtils.isNullString(ec.getLabel())) {
            vehName.setText(ec.getLabel());
        }
        
        vehName.setColor(Color.WHITE);
        add(vehName);

        // don't show until and AirVehicleState is received
        setVisible(false);
    }
    
     /**
     * Creates a <Code>VehicleGraphic</code> to represent the vehicle.  Creates and updates the
     * vehicle <code>FootprintGraphic</code> and <code>TrailGraphic</code>
     *
     * @param avc air vehicle state at start
     * @param color color of icon
     */
    public EntityGraphic(AirVehicleConfiguration avc, Image icon, Color color) {

        this.name = avc.getLabel();
        this.id = avc.getID();
        setRefObject(this.id);
        //trail.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, new float[]{1f, 2f}, 0.5f));
        //trail.setPaint(Color.RED);
        //add(trail);
        footprintGraphic = new FootprintGraphic(avc.getPayloadConfigurationList(), color);
        add(footprintGraphic);
        
        Image shadowImage = IconTools.getFilledImage(icon, 20, 20, 4, Color.WHITE, color);
        this.icon = new MapIcon(shadowImage);
        add(this.icon);
        
        vehName = new MapText(0, 0, String.valueOf(id), 0, 15);
        if (!ObjectUtils.isNullString(avc.getLabel())) {
            vehName.setText(avc.getLabel());
        }
        
        vehName.setColor(Color.WHITE);
        add(vehName);

        // don't show until and AirVehicleState is received
        setVisible(false);
    }
    
    /**
     *
     * @return id vehicle ID
     */
    public long getId() {
        return id;
    }

    /**
     *
     * @return name air vehicle call sign
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Updates the state of the vehicle icon, then updates the footprint and trail graphics.
     *
     * @param state current air vehicle state
     */
    public void update(EntityState state) {
        if (state.getID() != id) {
            return;
        }
        setVisible(true);
        icon.setLatLon(state.getLocation().getLatitude(), state.getLocation().getLongitude());
        icon.setRotation(Math.toRadians(state.getHeading()));
        footprintGraphic.update(state.getPayloadStateList());
        //trail.update(state.getLocation().getLatitude(), state.getLocation().getLongitude(), state.getTime());
        vehName.setLatLon(state.getLocation().getLatitude(), state.getLocation().getLongitude());
    }
    
    /**
     * Updates the state of the vehicle icon, then updates the footprint and trail graphics.
     *
     * @param state current air vehicle state
     */
    public void update(AirVehicleState state) {
        if (state.getID() != id) {
            return;
        }
        setVisible(true);
        icon.setLatLon(state.getLocation().getLatitude(), state.getLocation().getLongitude());
        icon.setRotation(Math.toRadians(state.getHeading()));
        footprintGraphic.update(state.getPayloadStateList());
        //trail.update(state.getLocation().getLatitude(), state.getLocation().getLongitude(), state.getTime());
        vehName.setLatLon(state.getLocation().getLatitude(), state.getLocation().getLongitude());
    }

    //public void clearTrail() {
        //trail.clear();
    //}

//    /**
//     * Internal class that creates and updates the vehicle icon.
//     *
//     */
//    public static class VehicleIcon extends MapCanvas {
//
//        static int height = 10;
//        static int width = 10;
//        static Path2D path = new Path2D.Float();
//        Color color;
//
//        public VehicleIcon(Color color) {
//            this.color = color;
//        }
//
//        static {
//            path.moveTo(0, -height);
//            path.lineTo(width, height);
//            path.lineTo(0, 0.4 * height);
//            path.lineTo(-width, height);
//            path.closePath();
//        }
//
//        @Override
//        public void paintGraphics(Graphics2D g) {
//            g.setPaint(color);
//            g.fill(path);
//            g.setColor(Color.WHITE);
//            g.draw(path);
//        }
//
//        @Override
//        public Dimension getSize() {
//            return new Dimension(2*width, 2*height);
//        }
//    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */