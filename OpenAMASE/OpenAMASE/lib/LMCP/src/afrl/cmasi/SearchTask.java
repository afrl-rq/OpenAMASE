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
 Used to request a search task allocation from the automation service. 
*/
public class SearchTask extends afrl.cmasi.Task {
    
    public static final int LMCP_TYPE = 9;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "SearchTask";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.SearchTask";

    /**  The wavelength bands that should be used to complete the task. (Units: None)*/
    @LmcpType("WavelengthBand")
    protected java.util.ArrayList<afrl.cmasi.WavelengthBand> DesiredWavelengthBands = new java.util.ArrayList<afrl.cmasi.WavelengthBand>();
    /**  Minimum time that a sensor must look at any given point in this search task. (Units: milliseconds)*/
    @LmcpType("int64")
    protected long DwellTime = 0L;
    /**  Average ground sample distance for locations in search area (Units: meters/pixel)*/
    @LmcpType("real32")
    protected float GroundSampleDistance = (float)0;

    
    public SearchTask() {
    }

    public SearchTask(long TaskID, String Label, float RevisitRate, short Priority, boolean Required, long DwellTime, float GroundSampleDistance){
        this.TaskID = TaskID;
        this.Label = Label;
        this.RevisitRate = RevisitRate;
        this.Priority = Priority;
        this.Required = Required;
        this.DwellTime = DwellTime;
        this.GroundSampleDistance = GroundSampleDistance;
    }


    public SearchTask clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            SearchTask newObj = new SearchTask();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    public java.util.ArrayList<afrl.cmasi.WavelengthBand> getDesiredWavelengthBands() {
        return DesiredWavelengthBands;
    }

    /**  Minimum time that a sensor must look at any given point in this search task. (Units: milliseconds)*/
    public long getDwellTime() { return DwellTime; }

    /**  Minimum time that a sensor must look at any given point in this search task. (Units: milliseconds)*/
    public SearchTask setDwellTime( long val ) {
        DwellTime = val;
        return this;
    }

    /**  Average ground sample distance for locations in search area (Units: meters/pixel)*/
    public float getGroundSampleDistance() { return GroundSampleDistance; }

    /**  Average ground sample distance for locations in search area (Units: meters/pixel)*/
    public SearchTask setGroundSampleDistance( float val ) {
        GroundSampleDistance = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 12; // accounts for primitive types
        
        size += 2 + 4 * DesiredWavelengthBands.size();

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        DesiredWavelengthBands.clear();
        int DesiredWavelengthBands_len = LMCPUtil.getUint16(in);
        for(int i=0; i<DesiredWavelengthBands_len; i++){
        DesiredWavelengthBands.add(afrl.cmasi.WavelengthBand.unpack( in ));

        }
        DwellTime = LMCPUtil.getInt64(in);

        GroundSampleDistance = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putUint16(out, DesiredWavelengthBands.size());
        for(int i=0; i<DesiredWavelengthBands.size(); i++){
            DesiredWavelengthBands.get(i).pack(out);
        }
        LMCPUtil.putInt64(out, DwellTime);
        LMCPUtil.putReal32(out, GroundSampleDistance);

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
        buf.append( ws + "<SearchTask Series=\"CMASI\">\n");
        buf.append( ws + "  <DesiredWavelengthBands>\n");
        for (int i=0; i<DesiredWavelengthBands.size(); i++) {
        buf.append( ws + "  <WavelengthBand>" + String.valueOf(DesiredWavelengthBands.get(i)) + "</WavelengthBand>\n");
        }
        buf.append( ws + "  </DesiredWavelengthBands>\n");
        buf.append( ws + "  <DwellTime>" + String.valueOf(DwellTime) + "</DwellTime>\n");
        buf.append( ws + "  <GroundSampleDistance>" + String.valueOf(GroundSampleDistance) + "</GroundSampleDistance>\n");
        buf.append( ws + "  <TaskID>" + String.valueOf(TaskID) + "</TaskID>\n");
        buf.append( ws + "  <Label>" + String.valueOf(Label) + "</Label>\n");
        buf.append( ws + "  <EligibleEntities>\n");
        for (int i=0; i<EligibleEntities.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(EligibleEntities.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </EligibleEntities>\n");
        buf.append( ws + "  <RevisitRate>" + String.valueOf(RevisitRate) + "</RevisitRate>\n");
        buf.append( ws + "  <Parameters>\n");
        for (int i=0; i<Parameters.size(); i++) {
            buf.append( Parameters.get(i) == null ? ( ws + "    <null/>\n") : (Parameters.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Parameters>\n");
        buf.append( ws + "  <Priority>" + String.valueOf(Priority) + "</Priority>\n");
        buf.append( ws + "  <Required>" + String.valueOf(Required) + "</Required>\n");
        buf.append( ws + "</SearchTask>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        SearchTask o = (SearchTask) anotherObj;
         if (!DesiredWavelengthBands.equals( o.DesiredWavelengthBands)) return false;
        if (DwellTime != o.DwellTime) return false;
        if (GroundSampleDistance != o.GroundSampleDistance) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)GroundSampleDistance;

        return hash + super.hashCode();
    }
    
}
