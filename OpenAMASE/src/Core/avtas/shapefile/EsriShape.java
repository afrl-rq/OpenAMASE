// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.shapefile;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A shape read from a ESRI shapefile. The following shapes are supported:<br/>
 * <ul> <li>Polyline (3)</li> <li>Polygon (5)</li> <li>PolylineZ (13)</li>
 * <li>PolygonZ (15)</li> </ul>
 *
 * @author AFRL/RQQD
 */
public class EsriShape {

    
    /**
     * Type of shape
     */
    protected ShapeType shapeType = ShapeType.POLYGON_Z;
    /**
     * Array of x points for this shape
     */
    protected double[] x = null;
    /**
     * Array of y points for this shape
     */
    protected double[] y = null;
    /**
     * Array of y points for this shape (if applicable)
     */
    protected double[] z = null;
//    /**
//     * minimum height of this shape, if applicable
//     */
//    protected double minZ = 0;
//    /**
//     * maximum height of this shape, if applicable
//     */
//    protected double maxZ = 0;
//    
//    /** minimum y-extent of shape */
//    protected double minY = 0;
//    
//    /** maximum y-extent of shape */
//    protected double maxY = 0;
//    
//    /** minimum x-extent of shape */
//    protected double minX = 0;
//    
//    /** maximum x-extent of shape */
//    protected double maxX = 0;
    
    /**
     * number of points in this shape
     */
    protected int numPoints = 0;
    /**
     * index of this shape in the Shapefile
     */
    protected int shapeIndex = 0;
    /**
     * index of this part in the shape (0 for single part shapes)
     */
    protected int partIndex = 0;
    
    /** Source file for this shape (name only, not path).  May be blank */
    //protected String fileName = "";
    
    /**
     * contains attributes for this shape from an ESRI DBF file
     */
    private HashMap<String, String> attrMap = new HashMap<String, String>();

    public EsriShape() {
    }

    public EsriShape(ShapeType shapeType, double[] x, double[] y, double[] z) {
        this.shapeType = shapeType;
        this.x = x;
        this.y = y;
        this.z = z;

        if (x.length != y.length || (z != null && z.length != x.length)) {
            throw new IllegalArgumentException("shape point arrays do not match in length");
        }
        this.numPoints = x.length;
    }

    public static EsriShape createPolyline(int shapeIndex, int partIndex, double[] x, double[] y) {
        EsriShape shape = new EsriShape(ShapeType.POLYLINE, x, y, null);
        shape.shapeIndex = shapeIndex;
        shape.partIndex = partIndex;

        return shape;
    }

    public static EsriShape createPolygon(int shapeIndex, int partIndex, double[] x, double[] y) {
        EsriShape shape = new EsriShape(ShapeType.POLYGON, x, y, null);
        shape.shapeIndex = shapeIndex;
        shape.partIndex = partIndex;

        return shape;
    }

    public static EsriShape createPolylineZ(int shapeIndex, int partIndex, double[] x, double[] y, double[] z, 
            double minZ, double maxZ) {
        EsriShape shape = new EsriShape(ShapeType.POLYLINE_Z, x, y, z);
        shape.shapeIndex = shapeIndex;
        shape.partIndex = partIndex;
        //shape.minZ = minZ;
        //shape.maxZ = maxZ;

        return shape;
    }

    public static EsriShape createPolygonZ(int shapeIndex, int partIndex, double[] x, double[] y, double[] z, 
            double minZ, double maxZ) {
        EsriShape shape = new EsriShape(ShapeType.POLYGON_Z, x, y, z);
        shape.shapeIndex = shapeIndex;
        shape.partIndex = partIndex;
        //shape.minZ = minZ;
        //shape.maxZ = maxZ;

        return shape;
    }

    public void addAttribute(String key, String value) {
        attrMap.put(key, value);
    }

    public String getAttribute(String key) {
        return attrMap.get(key);
    }

    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attrMap);
    }

    public double[] getX() {
        return x;
    }

    /**
     * @return the y
     */
    public double[] getY() {
        return y;
    }

    /**
     * @return the z coordinates. This will return null if the shape is not a
     * PolygonZ or a PolylineZ
     */
    public double[] getZ() {
        return z;
    }

//    /**
//     * @return the minY
//     */
//    public double getMinY() {
//        return minY;
//    }
//
//    /**
//     * @return the maxY
//     */
//    public double getMaxY() {
//        return maxY;
//    }
//
//    /**
//     * @return the minX
//     */
//    public double getMinX() {
//        return minX;
//    }
//
//    /**
//     * @return the maxX
//     */
//    public double getMaxX() {
//        return maxX;
//    }
//
//    /**
//     * @return the minZ
//     */
//    public double getMinZ() {
//        return minZ;
//    }
//
//    /**
//     * @return the maxZ
//     */
//    public double getMaxZ() {
//        return maxZ;
//    }
//
//    /**
//     * @return the numPoints
//     */
//    public int getNumPoints() {
//        return numPoints;
//    }
//
//    /**
//     * @return the shapeIndex
//     */
//    public int getShapeIndex() {
//        return shapeIndex;
//    }
//
//    /**
//     * @return the partIndex
//     */
//    public int getPartIndex() {
//        return partIndex;
//    }
//
//    /**
//     * @return the shapeType
//     */
//    public ShapeType getShapeType() {
//        return shapeType;
//    }
//
//    /**
//     * @return the fileName
//     */
//    public String getFileName() {
//        return fileName;
//    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */