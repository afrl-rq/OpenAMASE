// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package afrl.cmasi;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Defines a circular shape based on a center point and radius 
*/
public class Circle extends afrl.cmasi.AbstractGeometry {
    
    public static final int LMCP_TYPE = 22;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "Circle";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.Circle";

    /**  Center point of the circle. A valid Circle must define CenterPoint (null not allowed) (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D CenterPoint = new afrl.cmasi.Location3D();
    /**  Radius of the circle (Units: meter)*/
    @LmcpType("real32")
    protected float Radius = (float)0;

    
    public Circle() {
    }

    public Circle(afrl.cmasi.Location3D CenterPoint, float Radius){
        this.CenterPoint = CenterPoint;
        this.Radius = Radius;
    }


    public Circle clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            Circle newObj = new Circle();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Center point of the circle. A valid Circle must define CenterPoint (null not allowed) (Units: None)*/
    public afrl.cmasi.Location3D getCenterPoint() { return CenterPoint; }

    /**  Center point of the circle. A valid Circle must define CenterPoint (null not allowed) (Units: None)*/
    public Circle setCenterPoint( afrl.cmasi.Location3D val ) {
        CenterPoint = val;
        return this;
    }

    /**  Radius of the circle (Units: meter)*/
    public float getRadius() { return Radius; }

    /**  Radius of the circle (Units: meter)*/
    public Circle setRadius( float val ) {
        Radius = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 4; // accounts for primitive types
        size += LMCPUtil.sizeOf(CenterPoint);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
            CenterPoint = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        Radius = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putObject(out, CenterPoint);
        LMCPUtil.putReal32(out, Radius);

    }

    public int getLMCPType() { return LMCP_TYPE; }

    public String getLMCPSeriesName() { return SERIES_NAME; }

    public long getLMCPSeriesNameAsLong() { return SERIES_NAME_ID; }

    public int getLMCPSeriesVersion() { return SERIES_VERSION; };

    public String getLMCPTypeName() { return TYPE_NAME; }

    public String getFullLMCPTypeName() { return FULL_LMCP_TYPE_NAME; }

    public String toString() {
        return toXML("");
    }

    public String toXML(String ws) {
        StringBuffer buf = new StringBuffer();
        buf.append( ws + "<Circle Series=\"CMASI\">\n");
        if (CenterPoint!= null){
           buf.append( ws + "  <CenterPoint>\n");
           buf.append( ( CenterPoint.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </CenterPoint>\n");
        }
        buf.append( ws + "  <Radius>" + String.valueOf(Radius) + "</Radius>\n");
        buf.append( ws + "</Circle>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        Circle o = (Circle) anotherObj;
        if (CenterPoint == null && o.CenterPoint != null) return false;
        if ( CenterPoint!= null && !CenterPoint.equals(o.CenterPoint)) return false;
        if (Radius != o.Radius) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)Radius;

        return hash + super.hashCode();
    }
    
}
