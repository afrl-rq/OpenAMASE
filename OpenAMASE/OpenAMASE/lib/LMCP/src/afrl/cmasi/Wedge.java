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
            Defines an angular range for use in setting constraints or desired approaches to tasks.  In most cases, wedges are            defined in the inertial sense, where azimuth is the angle between North and the center of the wedge, elevation is the            angle between the horizon and the center of the wedge (positive up).  In some instances, other coordinate systems are defined, such as            angles relative to a line.        
*/
public class Wedge extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 16;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "Wedge";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.Wedge";

    /**  Azimuthal centerline of the wedge. (Units: degree)*/
    @LmcpType("real32")
    protected float AzimuthCenterline = (float)0;
    /**  Vertical centerline of the wedge. (Units: degree)*/
    @LmcpType("real32")
    protected float VerticalCenterline = (float)0;
    /**  Azimuthal angular extent of the wedge. The extent is centered around the centerline. A value of zero denotes that this wedge is defined as a single angle. (Units: degree)*/
    @LmcpType("real32")
    protected float AzimuthExtent = (float)0;
    /**  Vertical angular extent of the wedge. The extent is centered around the centerline. A value of zero denotes that this wedge is defined as a single angle. (Units: degree)*/
    @LmcpType("real32")
    protected float VerticalExtent = (float)0;

    
    public Wedge() {
    }

    public Wedge(float AzimuthCenterline, float VerticalCenterline, float AzimuthExtent, float VerticalExtent){
        this.AzimuthCenterline = AzimuthCenterline;
        this.VerticalCenterline = VerticalCenterline;
        this.AzimuthExtent = AzimuthExtent;
        this.VerticalExtent = VerticalExtent;
    }


    public Wedge clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            Wedge newObj = new Wedge();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Azimuthal centerline of the wedge. (Units: degree)*/
    public float getAzimuthCenterline() { return AzimuthCenterline; }

    /**  Azimuthal centerline of the wedge. (Units: degree)*/
    public Wedge setAzimuthCenterline( float val ) {
        AzimuthCenterline = val;
        return this;
    }

    /**  Vertical centerline of the wedge. (Units: degree)*/
    public float getVerticalCenterline() { return VerticalCenterline; }

    /**  Vertical centerline of the wedge. (Units: degree)*/
    public Wedge setVerticalCenterline( float val ) {
        VerticalCenterline = val;
        return this;
    }

    /**  Azimuthal angular extent of the wedge. The extent is centered around the centerline. A value of zero denotes that this wedge is defined as a single angle. (Units: degree)*/
    public float getAzimuthExtent() { return AzimuthExtent; }

    /**  Azimuthal angular extent of the wedge. The extent is centered around the centerline. A value of zero denotes that this wedge is defined as a single angle. (Units: degree)*/
    public Wedge setAzimuthExtent( float val ) {
        AzimuthExtent = val;
        return this;
    }

    /**  Vertical angular extent of the wedge. The extent is centered around the centerline. A value of zero denotes that this wedge is defined as a single angle. (Units: degree)*/
    public float getVerticalExtent() { return VerticalExtent; }

    /**  Vertical angular extent of the wedge. The extent is centered around the centerline. A value of zero denotes that this wedge is defined as a single angle. (Units: degree)*/
    public Wedge setVerticalExtent( float val ) {
        VerticalExtent = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 16; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        AzimuthCenterline = LMCPUtil.getReal32(in);

        VerticalCenterline = LMCPUtil.getReal32(in);

        AzimuthExtent = LMCPUtil.getReal32(in);

        VerticalExtent = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putReal32(out, AzimuthCenterline);
        LMCPUtil.putReal32(out, VerticalCenterline);
        LMCPUtil.putReal32(out, AzimuthExtent);
        LMCPUtil.putReal32(out, VerticalExtent);

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
        buf.append( ws + "<Wedge Series=\"CMASI\">\n");
        buf.append( ws + "  <AzimuthCenterline>" + String.valueOf(AzimuthCenterline) + "</AzimuthCenterline>\n");
        buf.append( ws + "  <VerticalCenterline>" + String.valueOf(VerticalCenterline) + "</VerticalCenterline>\n");
        buf.append( ws + "  <AzimuthExtent>" + String.valueOf(AzimuthExtent) + "</AzimuthExtent>\n");
        buf.append( ws + "  <VerticalExtent>" + String.valueOf(VerticalExtent) + "</VerticalExtent>\n");
        buf.append( ws + "</Wedge>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        Wedge o = (Wedge) anotherObj;
        if (AzimuthCenterline != o.AzimuthCenterline) return false;
        if (VerticalCenterline != o.VerticalCenterline) return false;
        if (AzimuthExtent != o.AzimuthExtent) return false;
        if (VerticalExtent != o.VerticalExtent) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)AzimuthCenterline;
        hash += 31 * (int)VerticalCenterline;
        hash += 31 * (int)AzimuthExtent;
        hash += 31 * (int)VerticalExtent;

        return hash + super.hashCode();
    }
    
}
