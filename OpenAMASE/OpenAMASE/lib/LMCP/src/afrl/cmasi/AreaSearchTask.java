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
 Area search task 
*/
public class AreaSearchTask extends afrl.cmasi.SearchTask {
    
    public static final int LMCP_TYPE = 17;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "AreaSearchTask";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.AreaSearchTask";

    /**  Area to search. A valid area search task must define a SearchArea (null not allowed) (Units: None)*/
    @LmcpType("AbstractGeometry")
    protected afrl.cmasi.AbstractGeometry SearchArea = new afrl.cmasi.AbstractGeometry();
    /**  A list of acceptable look-angles for this task. Each wedge is defined relative to true North. To be a valid look angle, a sensor must be looking from a direction within the bounds of the wedge. (Units: None)*/
    @LmcpType("Wedge")
    protected java.util.ArrayList<afrl.cmasi.Wedge> ViewAngleList = new java.util.ArrayList<afrl.cmasi.Wedge>();

    
    public AreaSearchTask() {
    }

    public AreaSearchTask(long TaskID, String Label, float RevisitRate, short Priority, boolean Required, long DwellTime, float GroundSampleDistance, afrl.cmasi.AbstractGeometry SearchArea){
        this.TaskID = TaskID;
        this.Label = Label;
        this.RevisitRate = RevisitRate;
        this.Priority = Priority;
        this.Required = Required;
        this.DwellTime = DwellTime;
        this.GroundSampleDistance = GroundSampleDistance;
        this.SearchArea = SearchArea;
    }


    public AreaSearchTask clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            AreaSearchTask newObj = new AreaSearchTask();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Area to search. A valid area search task must define a SearchArea (null not allowed) (Units: None)*/
    public afrl.cmasi.AbstractGeometry getSearchArea() { return SearchArea; }

    /**  Area to search. A valid area search task must define a SearchArea (null not allowed) (Units: None)*/
    public AreaSearchTask setSearchArea( afrl.cmasi.AbstractGeometry val ) {
        SearchArea = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.Wedge> getViewAngleList() {
        return ViewAngleList;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 0; // accounts for primitive types
        size += LMCPUtil.sizeOf(SearchArea);
        size += 2;
        size += LMCPUtil.sizeOfList(ViewAngleList);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
            SearchArea = (afrl.cmasi.AbstractGeometry) LMCPUtil.getObject(in);
        ViewAngleList.clear();
        int ViewAngleList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<ViewAngleList_len; i++){
        ViewAngleList.add( (afrl.cmasi.Wedge) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putObject(out, SearchArea);
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
        buf.append( ws + "<AreaSearchTask Series=\"CMASI\">\n");
        if (SearchArea!= null){
           buf.append( ws + "  <SearchArea>\n");
           buf.append( ( SearchArea.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </SearchArea>\n");
        }
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
        buf.append( ws + "</AreaSearchTask>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        AreaSearchTask o = (AreaSearchTask) anotherObj;
        if (SearchArea == null && o.SearchArea != null) return false;
        if ( SearchArea!= null && !SearchArea.equals(o.SearchArea)) return false;
         if (!ViewAngleList.equals( o.ViewAngleList)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
