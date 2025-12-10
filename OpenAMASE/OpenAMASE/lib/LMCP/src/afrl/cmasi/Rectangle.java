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
 Defines a rectangular shape based on a corner point, width, height, and rotation 
*/
public class Rectangle extends afrl.cmasi.AbstractGeometry {
    
    public static final int LMCP_TYPE = 43;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "Rectangle";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.Rectangle";

    /**  Center point of the rectangle (note altitude value is ignored). A valid Rectangle must define CenterPoint (null not allowed). (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D CenterPoint = new afrl.cmasi.Location3D();
    /**  Width of the rectangle (Units: meter)*/
    @LmcpType("real32")
    protected float Width = (float)0;
    /**  Height of the rectangle (Units: meter)*/
    @LmcpType("real32")
    protected float Height = (float)0;
    /**  Rotation of the rectangle around the center point (positive from north axis towards east) (Units: degree)*/
    @LmcpType("real32")
    protected float Rotation = (float)0;

    
    public Rectangle() {
    }

    public Rectangle(afrl.cmasi.Location3D CenterPoint, float Width, float Height, float Rotation){
        this.CenterPoint = CenterPoint;
        this.Width = Width;
        this.Height = Height;
        this.Rotation = Rotation;
    }


    public Rectangle clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            Rectangle newObj = new Rectangle();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Center point of the rectangle (note altitude value is ignored). A valid Rectangle must define CenterPoint (null not allowed). (Units: None)*/
    public afrl.cmasi.Location3D getCenterPoint() { return CenterPoint; }

    /**  Center point of the rectangle (note altitude value is ignored). A valid Rectangle must define CenterPoint (null not allowed). (Units: None)*/
    public Rectangle setCenterPoint( afrl.cmasi.Location3D val ) {
        CenterPoint = val;
        return this;
    }

    /**  Width of the rectangle (Units: meter)*/
    public float getWidth() { return Width; }

    /**  Width of the rectangle (Units: meter)*/
    public Rectangle setWidth( float val ) {
        Width = val;
        return this;
    }

    /**  Height of the rectangle (Units: meter)*/
    public float getHeight() { return Height; }

    /**  Height of the rectangle (Units: meter)*/
    public Rectangle setHeight( float val ) {
        Height = val;
        return this;
    }

    /**  Rotation of the rectangle around the center point (positive from north axis towards east) (Units: degree)*/
    public float getRotation() { return Rotation; }

    /**  Rotation of the rectangle around the center point (positive from north axis towards east) (Units: degree)*/
    public Rectangle setRotation( float val ) {
        Rotation = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 12; // accounts for primitive types
        size += LMCPUtil.sizeOf(CenterPoint);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
            CenterPoint = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        Width = LMCPUtil.getReal32(in);

        Height = LMCPUtil.getReal32(in);

        Rotation = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putObject(out, CenterPoint);
        LMCPUtil.putReal32(out, Width);
        LMCPUtil.putReal32(out, Height);
        LMCPUtil.putReal32(out, Rotation);

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
        buf.append( ws + "<Rectangle Series=\"CMASI\">\n");
        if (CenterPoint!= null){
           buf.append( ws + "  <CenterPoint>\n");
           buf.append( ( CenterPoint.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </CenterPoint>\n");
        }
        buf.append( ws + "  <Width>" + String.valueOf(Width) + "</Width>\n");
        buf.append( ws + "  <Height>" + String.valueOf(Height) + "</Height>\n");
        buf.append( ws + "  <Rotation>" + String.valueOf(Rotation) + "</Rotation>\n");
        buf.append( ws + "</Rectangle>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        Rectangle o = (Rectangle) anotherObj;
        if (CenterPoint == null && o.CenterPoint != null) return false;
        if ( CenterPoint!= null && !CenterPoint.equals(o.CenterPoint)) return false;
        if (Width != o.Width) return false;
        if (Height != o.Height) return false;
        if (Rotation != o.Rotation) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)Width;
        hash += 31 * (int)Height;
        hash += 31 * (int)Rotation;

        return hash + super.hashCode();
    }
    
}
