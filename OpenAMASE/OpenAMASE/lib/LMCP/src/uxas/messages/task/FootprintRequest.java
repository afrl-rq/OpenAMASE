// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package uxas.messages.task;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Request to compute potential eligible sensor footprint for a single vehicle at        various GSDs and altitudes from available sensors on the vehicle 
*/
public class FootprintRequest extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 11;

    public static final String SERIES_NAME = "UXTASK";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149757930721443840L;
    public static final int SERIES_VERSION = 8;


    private static final String TYPE_NAME = "FootprintRequest";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.task.FootprintRequest";

    /**  ID corresponding to this specific footprint request (Units: None)*/
    @LmcpType("int64")
    protected long FootprintRequestID = 0L;
    /**  Single vehicle to be considered for sensor request. (Units: None)*/
    @LmcpType("int64")
    protected long VehicleID = 0L;
    /**  Camera wavelength to be considered. If list is empty, planner should plan for all available sensors on each entity (Units: None)*/
    @LmcpType("WavelengthBand")
    protected java.util.ArrayList<afrl.cmasi.WavelengthBand> EligibleWavelengths = new java.util.ArrayList<afrl.cmasi.WavelengthBand>();
    /**  Desired ground sample distance for an eligible sensor. If list is empty, then footprint calculation uses the max ground sample distance for the specified altitude. (Units: None)*/
    @LmcpType("real32")
    protected java.util.ArrayList<Float> GroundSampleDistances = new java.util.ArrayList<Float>();
    /**  AGL Altitude to consider during sensor information calculation. If 'AglAltitudes' list is empty, sensor planner should use nominal altitude from entity configurations (Units: meters)*/
    @LmcpType("real32")
    protected java.util.ArrayList<Float> AglAltitudes = new java.util.ArrayList<Float>();
    /**  Desired camera elevation angles. If list is empty, then uses an optimal elevation angle for achieving max GSD (Units: deg)*/
    @LmcpType("real32")
    protected java.util.ArrayList<Float> ElevationAngles = new java.util.ArrayList<Float>();

    
    public FootprintRequest() {
    }

    public FootprintRequest(long FootprintRequestID, long VehicleID){
        this.FootprintRequestID = FootprintRequestID;
        this.VehicleID = VehicleID;
    }


    public FootprintRequest clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            FootprintRequest newObj = new FootprintRequest();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  ID corresponding to this specific footprint request (Units: None)*/
    public long getFootprintRequestID() { return FootprintRequestID; }

    /**  ID corresponding to this specific footprint request (Units: None)*/
    public FootprintRequest setFootprintRequestID( long val ) {
        FootprintRequestID = val;
        return this;
    }

    /**  Single vehicle to be considered for sensor request. (Units: None)*/
    public long getVehicleID() { return VehicleID; }

    /**  Single vehicle to be considered for sensor request. (Units: None)*/
    public FootprintRequest setVehicleID( long val ) {
        VehicleID = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.WavelengthBand> getEligibleWavelengths() {
        return EligibleWavelengths;
    }

    public java.util.ArrayList<Float> getGroundSampleDistances() {
        return GroundSampleDistances;
    }

    public java.util.ArrayList<Float> getAglAltitudes() {
        return AglAltitudes;
    }

    public java.util.ArrayList<Float> getElevationAngles() {
        return ElevationAngles;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 16; // accounts for primitive types
        
        size += 2 + 4 * EligibleWavelengths.size();
        
        size += 2 + 4 * GroundSampleDistances.size();
        
        size += 2 + 4 * AglAltitudes.size();
        
        size += 2 + 4 * ElevationAngles.size();

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        FootprintRequestID = LMCPUtil.getInt64(in);

        VehicleID = LMCPUtil.getInt64(in);

        EligibleWavelengths.clear();
        int EligibleWavelengths_len = LMCPUtil.getUint16(in);
        for(int i=0; i<EligibleWavelengths_len; i++){
        EligibleWavelengths.add(afrl.cmasi.WavelengthBand.unpack( in ));

        }
        GroundSampleDistances.clear();
        int GroundSampleDistances_len = LMCPUtil.getUint16(in);
        for(int i=0; i<GroundSampleDistances_len; i++){
            GroundSampleDistances.add(LMCPUtil.getReal32(in));
        }
        AglAltitudes.clear();
        int AglAltitudes_len = LMCPUtil.getUint16(in);
        for(int i=0; i<AglAltitudes_len; i++){
            AglAltitudes.add(LMCPUtil.getReal32(in));
        }
        ElevationAngles.clear();
        int ElevationAngles_len = LMCPUtil.getUint16(in);
        for(int i=0; i<ElevationAngles_len; i++){
            ElevationAngles.add(LMCPUtil.getReal32(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, FootprintRequestID);
        LMCPUtil.putInt64(out, VehicleID);
        LMCPUtil.putUint16(out, EligibleWavelengths.size());
        for(int i=0; i<EligibleWavelengths.size(); i++){
            EligibleWavelengths.get(i).pack(out);
        }
        LMCPUtil.putUint16(out, GroundSampleDistances.size());
        for(int i=0; i<GroundSampleDistances.size(); i++){
            LMCPUtil.putReal32(out, GroundSampleDistances.get(i));
        }
        LMCPUtil.putUint16(out, AglAltitudes.size());
        for(int i=0; i<AglAltitudes.size(); i++){
            LMCPUtil.putReal32(out, AglAltitudes.get(i));
        }
        LMCPUtil.putUint16(out, ElevationAngles.size());
        for(int i=0; i<ElevationAngles.size(); i++){
            LMCPUtil.putReal32(out, ElevationAngles.get(i));
        }

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
        buf.append( ws + "<FootprintRequest Series=\"UXTASK\">\n");
        buf.append( ws + "  <FootprintRequestID>" + String.valueOf(FootprintRequestID) + "</FootprintRequestID>\n");
        buf.append( ws + "  <VehicleID>" + String.valueOf(VehicleID) + "</VehicleID>\n");
        buf.append( ws + "  <EligibleWavelengths>\n");
        for (int i=0; i<EligibleWavelengths.size(); i++) {
        buf.append( ws + "  <WavelengthBand>" + String.valueOf(EligibleWavelengths.get(i)) + "</WavelengthBand>\n");
        }
        buf.append( ws + "  </EligibleWavelengths>\n");
        buf.append( ws + "  <GroundSampleDistances>\n");
        for (int i=0; i<GroundSampleDistances.size(); i++) {
        buf.append( ws + "  <real32>" + String.valueOf(GroundSampleDistances.get(i)) + "</real32>\n");
        }
        buf.append( ws + "  </GroundSampleDistances>\n");
        buf.append( ws + "  <AglAltitudes>\n");
        for (int i=0; i<AglAltitudes.size(); i++) {
        buf.append( ws + "  <real32>" + String.valueOf(AglAltitudes.get(i)) + "</real32>\n");
        }
        buf.append( ws + "  </AglAltitudes>\n");
        buf.append( ws + "  <ElevationAngles>\n");
        for (int i=0; i<ElevationAngles.size(); i++) {
        buf.append( ws + "  <real32>" + String.valueOf(ElevationAngles.get(i)) + "</real32>\n");
        }
        buf.append( ws + "  </ElevationAngles>\n");
        buf.append( ws + "</FootprintRequest>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        FootprintRequest o = (FootprintRequest) anotherObj;
        if (FootprintRequestID != o.FootprintRequestID) return false;
        if (VehicleID != o.VehicleID) return false;
         if (!EligibleWavelengths.equals( o.EligibleWavelengths)) return false;
         if (!GroundSampleDistances.equals( o.GroundSampleDistances)) return false;
         if (!AglAltitudes.equals( o.AglAltitudes)) return false;
         if (!ElevationAngles.equals( o.ElevationAngles)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
