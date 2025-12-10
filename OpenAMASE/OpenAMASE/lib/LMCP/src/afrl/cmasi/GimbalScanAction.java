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
            Parameters for controlling the movement of a sensor in a continuous motion.  This can be used to start a back-and-forth or rotational            scan over time.  The sensor will sweep in a box pattern from the corner defined by:            (StartAzimuth, StartElevation) to (StartAzimuth, EndElevation) to (EndAzimuth, EndElevation)            to (EndAzimuth, StartElevation) and back to the origin.        
*/
public class GimbalScanAction extends afrl.cmasi.PayloadAction {
    
    public static final int LMCP_TYPE = 25;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "GimbalScanAction";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.GimbalScanAction";

    /**  sets the time-to-scan in the azimuthal direction. This is the time to complete one sweep through the the length of EndAzimuth - StartAzimuth. (Units: degree/second)*/
    @LmcpType("real32")
    protected float AzimuthSlewRate = (float)0;
    /**  sets the time-to-scan in the elevation direction. This is the time to complete one sweep through the the length of EndElevation - StartElevation. (Units: degree/second)*/
    @LmcpType("real32")
    protected float ElevationSlewRate = (float)0;
    /**  The starting azimuth for the sensor scan (boresight angle right of aircraft long axis)(Units: degree)*/
    @LmcpType("real32")
    protected float StartAzimuth = (float)0;
    /**  The ending azimuth for the sensor scan (boresight angle right of aircraft long axis) (Units: degree)*/
    @LmcpType("real32")
    protected float EndAzimuth = (float)0;
    /**  The starting elevation for the sensor scan (boresight angle positive from aircraft x-y plane)(Units: degree)*/
    @LmcpType("real32")
    protected float StartElevation = (float)0;
    /**  The ending elevation for the sensor scan (boresight angle positive from aircraft x-y plane)(Units: degree)*/
    @LmcpType("real32")
    protected float EndElevation = (float)0;
    /**  Number of sensor sweeps to perform. A zero value denotes indefinite number of sweeps. (Units: None)*/
    @LmcpType("uint32")
    protected long Cycles = 0L;

    
    public GimbalScanAction() {
    }

    public GimbalScanAction(long PayloadID, float AzimuthSlewRate, float ElevationSlewRate, float StartAzimuth, float EndAzimuth, float StartElevation, float EndElevation, long Cycles){
        this.PayloadID = PayloadID;
        this.AzimuthSlewRate = AzimuthSlewRate;
        this.ElevationSlewRate = ElevationSlewRate;
        this.StartAzimuth = StartAzimuth;
        this.EndAzimuth = EndAzimuth;
        this.StartElevation = StartElevation;
        this.EndElevation = EndElevation;
        this.Cycles = Cycles;
    }


    public GimbalScanAction clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            GimbalScanAction newObj = new GimbalScanAction();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  sets the time-to-scan in the azimuthal direction. This is the time to complete one sweep through the the length of EndAzimuth - StartAzimuth. (Units: degree/second)*/
    public float getAzimuthSlewRate() { return AzimuthSlewRate; }

    /**  sets the time-to-scan in the azimuthal direction. This is the time to complete one sweep through the the length of EndAzimuth - StartAzimuth. (Units: degree/second)*/
    public GimbalScanAction setAzimuthSlewRate( float val ) {
        AzimuthSlewRate = val;
        return this;
    }

    /**  sets the time-to-scan in the elevation direction. This is the time to complete one sweep through the the length of EndElevation - StartElevation. (Units: degree/second)*/
    public float getElevationSlewRate() { return ElevationSlewRate; }

    /**  sets the time-to-scan in the elevation direction. This is the time to complete one sweep through the the length of EndElevation - StartElevation. (Units: degree/second)*/
    public GimbalScanAction setElevationSlewRate( float val ) {
        ElevationSlewRate = val;
        return this;
    }

    /**  The starting azimuth for the sensor scan (boresight angle right of aircraft long axis)(Units: degree)*/
    public float getStartAzimuth() { return StartAzimuth; }

    /**  The starting azimuth for the sensor scan (boresight angle right of aircraft long axis)(Units: degree)*/
    public GimbalScanAction setStartAzimuth( float val ) {
        StartAzimuth = val;
        return this;
    }

    /**  The ending azimuth for the sensor scan (boresight angle right of aircraft long axis) (Units: degree)*/
    public float getEndAzimuth() { return EndAzimuth; }

    /**  The ending azimuth for the sensor scan (boresight angle right of aircraft long axis) (Units: degree)*/
    public GimbalScanAction setEndAzimuth( float val ) {
        EndAzimuth = val;
        return this;
    }

    /**  The starting elevation for the sensor scan (boresight angle positive from aircraft x-y plane)(Units: degree)*/
    public float getStartElevation() { return StartElevation; }

    /**  The starting elevation for the sensor scan (boresight angle positive from aircraft x-y plane)(Units: degree)*/
    public GimbalScanAction setStartElevation( float val ) {
        StartElevation = val;
        return this;
    }

    /**  The ending elevation for the sensor scan (boresight angle positive from aircraft x-y plane)(Units: degree)*/
    public float getEndElevation() { return EndElevation; }

    /**  The ending elevation for the sensor scan (boresight angle positive from aircraft x-y plane)(Units: degree)*/
    public GimbalScanAction setEndElevation( float val ) {
        EndElevation = val;
        return this;
    }

    /**  Number of sensor sweeps to perform. A zero value denotes indefinite number of sweeps. (Units: None)*/
    public long getCycles() { return Cycles; }

    /**  Number of sensor sweeps to perform. A zero value denotes indefinite number of sweeps. (Units: None)*/
    public GimbalScanAction setCycles( long val ) {
        Cycles = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 28; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        AzimuthSlewRate = LMCPUtil.getReal32(in);

        ElevationSlewRate = LMCPUtil.getReal32(in);

        StartAzimuth = LMCPUtil.getReal32(in);

        EndAzimuth = LMCPUtil.getReal32(in);

        StartElevation = LMCPUtil.getReal32(in);

        EndElevation = LMCPUtil.getReal32(in);

        Cycles = LMCPUtil.getUint32(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putReal32(out, AzimuthSlewRate);
        LMCPUtil.putReal32(out, ElevationSlewRate);
        LMCPUtil.putReal32(out, StartAzimuth);
        LMCPUtil.putReal32(out, EndAzimuth);
        LMCPUtil.putReal32(out, StartElevation);
        LMCPUtil.putReal32(out, EndElevation);
        LMCPUtil.putUint32(out, Cycles);

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
        buf.append( ws + "<GimbalScanAction Series=\"CMASI\">\n");
        buf.append( ws + "  <AzimuthSlewRate>" + String.valueOf(AzimuthSlewRate) + "</AzimuthSlewRate>\n");
        buf.append( ws + "  <ElevationSlewRate>" + String.valueOf(ElevationSlewRate) + "</ElevationSlewRate>\n");
        buf.append( ws + "  <StartAzimuth>" + String.valueOf(StartAzimuth) + "</StartAzimuth>\n");
        buf.append( ws + "  <EndAzimuth>" + String.valueOf(EndAzimuth) + "</EndAzimuth>\n");
        buf.append( ws + "  <StartElevation>" + String.valueOf(StartElevation) + "</StartElevation>\n");
        buf.append( ws + "  <EndElevation>" + String.valueOf(EndElevation) + "</EndElevation>\n");
        buf.append( ws + "  <Cycles>" + String.valueOf(Cycles) + "</Cycles>\n");
        buf.append( ws + "  <PayloadID>" + String.valueOf(PayloadID) + "</PayloadID>\n");
        buf.append( ws + "  <AssociatedTaskList>\n");
        for (int i=0; i<AssociatedTaskList.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(AssociatedTaskList.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </AssociatedTaskList>\n");
        buf.append( ws + "</GimbalScanAction>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        GimbalScanAction o = (GimbalScanAction) anotherObj;
        if (AzimuthSlewRate != o.AzimuthSlewRate) return false;
        if (ElevationSlewRate != o.ElevationSlewRate) return false;
        if (StartAzimuth != o.StartAzimuth) return false;
        if (EndAzimuth != o.EndAzimuth) return false;
        if (StartElevation != o.StartElevation) return false;
        if (EndElevation != o.EndElevation) return false;
        if (Cycles != o.Cycles) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)AzimuthSlewRate;
        hash += 31 * (int)ElevationSlewRate;
        hash += 31 * (int)StartAzimuth;
        hash += 31 * (int)EndAzimuth;
        hash += 31 * (int)StartElevation;

        return hash + super.hashCode();
    }
    
}
