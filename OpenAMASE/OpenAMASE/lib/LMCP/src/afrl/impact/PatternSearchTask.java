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
 Search task with specified search pattern 
*/
public class PatternSearchTask extends afrl.cmasi.SearchTask {
    
    public static final int LMCP_TYPE = 23;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "PatternSearchTask";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.PatternSearchTask";

    /**  Point at which to search is centered (from available points of interest) (Units: None)*/
    @LmcpType("int64")
    protected long SearchLocationID = 0L;
    /**  Defines coordinates directly, only used when SearchLocationID is non-zero (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D SearchLocation = null;
    /**  Search pattern to use (Units: None)*/
    @LmcpType("AreaSearchPattern")
    protected afrl.impact.AreaSearchPattern Pattern = afrl.impact.AreaSearchPattern.Spiral;
    /**  Pattern extent (Units: meters)*/
    @LmcpType("real32")
    protected float Extent = (float)0.0;

    
    public PatternSearchTask() {
    }

    public PatternSearchTask(long TaskID, String Label, float RevisitRate, short Priority, boolean Required, long DwellTime, float GroundSampleDistance, long SearchLocationID, afrl.cmasi.Location3D SearchLocation, afrl.impact.AreaSearchPattern Pattern, float Extent){
        this.TaskID = TaskID;
        this.Label = Label;
        this.RevisitRate = RevisitRate;
        this.Priority = Priority;
        this.Required = Required;
        this.DwellTime = DwellTime;
        this.GroundSampleDistance = GroundSampleDistance;
        this.SearchLocationID = SearchLocationID;
        this.SearchLocation = SearchLocation;
        this.Pattern = Pattern;
        this.Extent = Extent;
    }


    public PatternSearchTask clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            PatternSearchTask newObj = new PatternSearchTask();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Point at which to search is centered (from available points of interest) (Units: None)*/
    public long getSearchLocationID() { return SearchLocationID; }

    /**  Point at which to search is centered (from available points of interest) (Units: None)*/
    public PatternSearchTask setSearchLocationID( long val ) {
        SearchLocationID = val;
        return this;
    }

    /**  Defines coordinates directly, only used when SearchLocationID is non-zero (Units: None)*/
    public afrl.cmasi.Location3D getSearchLocation() { return SearchLocation; }

    /**  Defines coordinates directly, only used when SearchLocationID is non-zero (Units: None)*/
    public PatternSearchTask setSearchLocation( afrl.cmasi.Location3D val ) {
        SearchLocation = val;
        return this;
    }

    /**  Search pattern to use (Units: None)*/
    public afrl.impact.AreaSearchPattern getPattern() { return Pattern; }

    /**  Search pattern to use (Units: None)*/
    public PatternSearchTask setPattern( afrl.impact.AreaSearchPattern val ) {
        Pattern = val;
        return this;
    }

    /**  Pattern extent (Units: meters)*/
    public float getExtent() { return Extent; }

    /**  Pattern extent (Units: meters)*/
    public PatternSearchTask setExtent( float val ) {
        Extent = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 16; // accounts for primitive types
        size += LMCPUtil.sizeOf(SearchLocation);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        SearchLocationID = LMCPUtil.getInt64(in);

            SearchLocation = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        Pattern = afrl.impact.AreaSearchPattern.unpack( in );

        Extent = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putInt64(out, SearchLocationID);
        LMCPUtil.putObject(out, SearchLocation);
        Pattern.pack(out);
        LMCPUtil.putReal32(out, Extent);

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
        buf.append( ws + "<PatternSearchTask Series=\"IMPACT\">\n");
        buf.append( ws + "  <SearchLocationID>" + String.valueOf(SearchLocationID) + "</SearchLocationID>\n");
        if (SearchLocation!= null){
           buf.append( ws + "  <SearchLocation>\n");
           buf.append( ( SearchLocation.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </SearchLocation>\n");
        }
        buf.append( ws + "  <Pattern>" + String.valueOf(Pattern) + "</Pattern>\n");
        buf.append( ws + "  <Extent>" + String.valueOf(Extent) + "</Extent>\n");
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
        buf.append( ws + "</PatternSearchTask>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        PatternSearchTask o = (PatternSearchTask) anotherObj;
        if (SearchLocationID != o.SearchLocationID) return false;
        if (SearchLocation == null && o.SearchLocation != null) return false;
        if ( SearchLocation!= null && !SearchLocation.equals(o.SearchLocation)) return false;
        if (Pattern != o.Pattern) return false;
        if (Extent != o.Extent) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)Extent;

        return hash + super.hashCode();
    }
    
}
