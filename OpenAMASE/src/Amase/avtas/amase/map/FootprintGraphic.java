// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.map;

import afrl.cmasi.AirVehicleState;
import afrl.cmasi.CameraConfiguration;
import afrl.cmasi.CameraState;
import afrl.cmasi.Location3D;
import afrl.cmasi.PayloadConfiguration;
import afrl.cmasi.PayloadState;
import avtas.amase.entity.modules.CameraControl;
import avtas.amase.scenario.ScenarioState;
import avtas.map.graphics.MapGraphic;
import avtas.map.graphics.MapGraphicsList;
import avtas.map.graphics.MapMarker;
import avtas.map.graphics.MapPoly;
import java.awt.Color;
import java.awt.geom.Path2D;
import java.util.HashMap;
import java.util.List;

/**
 * Creates a <code>FootprintGraphic</code> that represents the field-of-view of the sensor.  This is a generalized method
 * that can be applied to any sensor look angle or vehicle euler angle.  Sensor angles are defined in sensor axis 
 * coordinates: fovX is side-to-side from the boresight, and fovY is up and down from the boresight.  Sensor
 * boresight angle is defined in vehicle corrdinates: epsilonX is in the body yaw plane (positive right from nose) 
 * epsilonZ is defined as the elevation angle between the sensor boresight and the x-y plane of the vehicle (positive
 * upwards).
 * 
 * @author AFRL/RQQD
 */
public class FootprintGraphic extends MapGraphicsList<MapGraphic> {

    public static final Path2D crosshair;
    Color vehicleColor = Color.BLACK;
    Color fillColor = Color.BLACK;

    
    HashMap<Long, MapPoly> footprintMap = new HashMap<>();
    HashMap<Long, MapMarker> centerMap = new HashMap<>();

    static {
        crosshair = new Path2D.Float();
        crosshair.moveTo(0, 4);
        crosshair.lineTo(8, 4);
        crosshair.moveTo(4, 0);
        crosshair.lineTo(4, 8);
    }
    

    public FootprintGraphic(List<PayloadConfiguration> pcList, Color color) {
        this.vehicleColor = color;

        fillColor = new Color(150, 150, 150, 50);
        
        
        for(PayloadConfiguration pc : pcList) {
            if (pc instanceof CameraConfiguration) {
                MapPoly poly = new MapPoly();
                poly.setVisible(false);
                footprintMap.put(pc.getPayloadID(), poly);
                
                poly.setPolygon(true);
                poly.setFill(fillColor);
                poly.setPainter(vehicleColor, 1);
                
                MapMarker centerMark = new MapMarker();
                centerMark.setMarkerShape(crosshair);
                centerMap.put(pc.getPayloadID(), centerMark);
                centerMark.setPainter(vehicleColor, 1);
                centerMark.setVisible(false);
                
                add(poly);
                add(centerMark);
            }
        }
    }

    /**
     * Updates the footprint of each camera in the current air vehicle state
     *
     * @param psList list of payloads for the entity/aircraft
     */
    public void update(List<PayloadState> psList) {

        for (PayloadState ps : psList) {
            if (ps instanceof CameraState) {
                CameraState cs = (CameraState) ps;
                MapPoly footprint = footprintMap.get(ps.getPayloadID());
                if (footprint == null) {
                    continue;
                }
                footprint.clear();
                footprint.setVisible(true);
                
                for (Location3D loc : cs.getFootprint()) {
                    footprint.addPoint(loc.getLatitude(), loc.getLongitude());
                }

                if (cs.getCenterpoint() != null && centerMap.containsKey(ps.getPayloadID())) {
                    MapMarker centerMark = centerMap.get(ps.getPayloadID());
                    centerMark.setVisible(true);
                    centerMark.setLat(cs.getCenterpoint().getLatitude());
                    centerMark.setLon(cs.getCenterpoint().getLongitude());
                }
                
            }
        }

    }
    

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */