// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package afrl.impact;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Reports on navigability of the water in the "Area".  Based on the WeatherReport CMASI message, designed to allow changes during execution. 
*/
public class WaterReport extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 33;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "WaterReport";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.WaterReport";

    /**  Area for which this report is valid. A valid WaterReport must define Area (null not allowed) (Units: None)*/
    @LmcpType("AbstractGeometry")
    protected afrl.cmasi.AbstractGeometry Area = new afrl.cmasi.AbstractGeometry();
    /**  Speed of the current, e.g. due to tides or river flow (Units: meters/sec)*/
    @LmcpType("real32")
    protected float CurrentSpeed = (float)0;
    /**  Direction of the current (Units: degree)*/
    @LmcpType("real32")
    protected float CurrentDirection = (float)0;
    /**  Wave direction of travel. Generally in the direction of the wind, except near sea coasts(Units: degree)*/
    @LmcpType("real32")
    protected float WaveDirection = (float)0;
    /**  Average wave height (Units: meters)*/
    @LmcpType("real32")
    protected float WaveHeight = (float)0;

    
    public WaterReport() {
    }

    public WaterReport(afrl.cmasi.AbstractGeometry Area, float CurrentSpeed, float CurrentDirection, float WaveDirection, float WaveHeight){
        this.Area = Area;
        this.CurrentSpeed = CurrentSpeed;
        this.CurrentDirection = CurrentDirection;
        this.WaveDirection = WaveDirection;
        this.WaveHeight = WaveHeight;
    }


    public WaterReport clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            WaterReport newObj = new WaterReport();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Area for which this report is valid. A valid WaterReport must define Area (null not allowed) (Units: None)*/
    public afrl.cmasi.AbstractGeometry getArea() { return Area; }

    /**  Area for which this report is valid. A valid WaterReport must define Area (null not allowed) (Units: None)*/
    public WaterReport setArea( afrl.cmasi.AbstractGeometry val ) {
        Area = val;
        return this;
    }

    /**  Speed of the current, e.g. due to tides or river flow (Units: meters/sec)*/
    public float getCurrentSpeed() { return CurrentSpeed; }

    /**  Speed of the current, e.g. due to tides or river flow (Units: meters/sec)*/
    public WaterReport setCurrentSpeed( float val ) {
        CurrentSpeed = val;
        return this;
    }

    /**  Direction of the current (Units: degree)*/
    public float getCurrentDirection() { return CurrentDirection; }

    /**  Direction of the current (Units: degree)*/
    public WaterReport setCurrentDirection( float val ) {
        CurrentDirection = val;
        return this;
    }

    /**  Wave direction of travel. Generally in the direction of the wind, except near sea coasts(Units: degree)*/
    public float getWaveDirection() { return WaveDirection; }

    /**  Wave direction of travel. Generally in the direction of the wind, except near sea coasts(Units: degree)*/
    public WaterReport setWaveDirection( float val ) {
        WaveDirection = val;
        return this;
    }

    /**  Average wave height (Units: meters)*/
    public float getWaveHeight() { return WaveHeight; }

    /**  Average wave height (Units: meters)*/
    public WaterReport setWaveHeight( float val ) {
        WaveHeight = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 16; // accounts for primitive types
        size += LMCPUtil.sizeOf(Area);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
            Area = (afrl.cmasi.AbstractGeometry) LMCPUtil.getObject(in);
        CurrentSpeed = LMCPUtil.getReal32(in);

        CurrentDirection = LMCPUtil.getReal32(in);

        WaveDirection = LMCPUtil.getReal32(in);

        WaveHeight = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putObject(out, Area);
        LMCPUtil.putReal32(out, CurrentSpeed);
        LMCPUtil.putReal32(out, CurrentDirection);
        LMCPUtil.putReal32(out, WaveDirection);
        LMCPUtil.putReal32(out, WaveHeight);

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
        buf.append( ws + "<WaterReport Series=\"IMPACT\">\n");
        if (Area!= null){
           buf.append( ws + "  <Area>\n");
           buf.append( ( Area.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </Area>\n");
        }
        buf.append( ws + "  <CurrentSpeed>" + String.valueOf(CurrentSpeed) + "</CurrentSpeed>\n");
        buf.append( ws + "  <CurrentDirection>" + String.valueOf(CurrentDirection) + "</CurrentDirection>\n");
        buf.append( ws + "  <WaveDirection>" + String.valueOf(WaveDirection) + "</WaveDirection>\n");
        buf.append( ws + "  <WaveHeight>" + String.valueOf(WaveHeight) + "</WaveHeight>\n");
        buf.append( ws + "</WaterReport>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        WaterReport o = (WaterReport) anotherObj;
        if (Area == null && o.Area != null) return false;
        if ( Area!= null && !Area.equals(o.Area)) return false;
        if (CurrentSpeed != o.CurrentSpeed) return false;
        if (CurrentDirection != o.CurrentDirection) return false;
        if (WaveDirection != o.WaveDirection) return false;
        if (WaveHeight != o.WaveHeight) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)CurrentSpeed;
        hash += 31 * (int)CurrentDirection;
        hash += 31 * (int)WaveDirection;
        hash += 31 * (int)WaveHeight;

        return hash + super.hashCode();
    }
    
}
