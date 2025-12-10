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
 Point search task 
*/
public class PointSearchTask extends afrl.cmasi.SearchTask {
    
    public static final int LMCP_TYPE = 41;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "PointSearchTask";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.PointSearchTask";

    /**  Point to search. A valid PointSearchTask must define SearchLocation (null not allowed). (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D SearchLocation = new afrl.cmasi.Location3D();
    /** Minimum distance that an aircraft must maintain from the point of interest. (Units: meter)*/
    @LmcpType("real32")
    protected float StandoffDistance = (float)0;
    /**  A list of acceptable look-angles for this task. Each wedge is defined relative to true North. To be a valid look angle, a sensor must be looking from a direction within the bounds of the wedge. (Units: None)*/
    @LmcpType("Wedge")
    protected java.util.ArrayList<afrl.cmasi.Wedge> ViewAngleList = new java.util.ArrayList<afrl.cmasi.Wedge>();

    
    public PointSearchTask() {
    }

    public PointSearchTask(long TaskID, String Label, float RevisitRate, short Priority, boolean Required, long DwellTime, float GroundSampleDistance, afrl.cmasi.Location3D SearchLocation, float StandoffDistance){
        this.TaskID = TaskID;
        this.Label = Label;
        this.RevisitRate = RevisitRate;
        this.Priority = Priority;
        this.Required = Required;
        this.DwellTime = DwellTime;
        this.GroundSampleDistance = GroundSampleDistance;
        this.SearchLocation = SearchLocation;
        this.StandoffDistance = StandoffDistance;
    }


    public PointSearchTask clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            PointSearchTask newObj = new PointSearchTask();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Point to search. A valid PointSearchTask must define SearchLocation (null not allowed). (Units: None)*/
    public afrl.cmasi.Location3D getSearchLocation() { return SearchLocation; }

    /**  Point to search. A valid PointSearchTask must define SearchLocation (null not allowed). (Units: None)*/
    public PointSearchTask setSearchLocation( afrl.cmasi.Location3D val ) {
        SearchLocation = val;
        return this;
    }

    /** Minimum distance that an aircraft must maintain from the point of interest. (Units: meter)*/
    public float getStandoffDistance() { return StandoffDistance; }

    /** Minimum distance that an aircraft must maintain from the point of interest. (Units: meter)*/
    public PointSearchTask setStandoffDistance( float val ) {
        StandoffDistance = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.Wedge> getViewAngleList() {
        return ViewAngleList;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 4; // accounts for primitive types
        size += LMCPUtil.sizeOf(SearchLocation);
        size += 2;
        size += LMCPUtil.sizeOfList(ViewAngleList);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
            SearchLocation = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        StandoffDistance = LMCPUtil.getReal32(in);

        ViewAngleList.clear();
        int ViewAngleList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<ViewAngleList_len; i++){
        ViewAngleList.add( (afrl.cmasi.Wedge) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putObject(out, SearchLocation);
        LMCPUtil.putReal32(out, StandoffDistance);
        LMCPUtil.putUint16(out, ViewAngleList.size());
        for(int i=0; i<ViewAngleList.size(); i++){
            LMCPUtil.putObject(out, ViewAngleList.get(i));
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
        buf.append( ws + "<PointSearchTask Series=\"CMASI\">\n");
        if (SearchLocation!= null){
           buf.append( ws + "  <SearchLocation>\n");
           buf.append( ( SearchLocation.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </SearchLocation>\n");
        }
        buf.append( ws + "  <StandoffDistance>" + String.valueOf(StandoffDistance) + "</StandoffDistance>\n");
        buf.append( ws + "  <ViewAngleList>\n");
        for (int i=0; i<ViewAngleList.size(); i++) {
            buf.append( ViewAngleList.get(i) == null ? ( ws + "    <null/>\n") : (ViewAngleList.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </ViewAngleList>\n");
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
        buf.append( ws + "</PointSearchTask>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        PointSearchTask o = (PointSearchTask) anotherObj;
        if (SearchLocation == null && o.SearchLocation != null) return false;
        if ( SearchLocation!= null && !SearchLocation.equals(o.SearchLocation)) return false;
        if (StandoffDistance != o.StandoffDistance) return false;
         if (!ViewAngleList.equals( o.ViewAngleList)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)StandoffDistance;

        return hash + super.hashCode();
    }
    
}
