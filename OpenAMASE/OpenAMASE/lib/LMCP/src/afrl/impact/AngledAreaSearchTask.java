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
 Area search task with specified direction 
*/
public class AngledAreaSearchTask extends afrl.cmasi.SearchTask {
    
    public static final int LMCP_TYPE = 24;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "AngledAreaSearchTask";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.AngledAreaSearchTask";

    /**  Area to search (from available areas of interest) (Units: None)*/
    @LmcpType("int64")
    protected long SearchAreaID = 0L;
    /**  Sweep angle defined from true North in clockwise manner (Units: degrees)*/
    @LmcpType("real32")
    protected float SweepAngle = (float)0;
    /**  Optional start point that must be reached before beginning search (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D StartPoint = null;

    
    public AngledAreaSearchTask() {
    }

    public AngledAreaSearchTask(long TaskID, String Label, float RevisitRate, short Priority, boolean Required, long DwellTime, float GroundSampleDistance, long SearchAreaID, float SweepAngle, afrl.cmasi.Location3D StartPoint){
        this.TaskID = TaskID;
        this.Label = Label;
        this.RevisitRate = RevisitRate;
        this.Priority = Priority;
        this.Required = Required;
        this.DwellTime = DwellTime;
        this.GroundSampleDistance = GroundSampleDistance;
        this.SearchAreaID = SearchAreaID;
        this.SweepAngle = SweepAngle;
        this.StartPoint = StartPoint;
    }


    public AngledAreaSearchTask clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            AngledAreaSearchTask newObj = new AngledAreaSearchTask();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Area to search (from available areas of interest) (Units: None)*/
    public long getSearchAreaID() { return SearchAreaID; }

    /**  Area to search (from available areas of interest) (Units: None)*/
    public AngledAreaSearchTask setSearchAreaID( long val ) {
        SearchAreaID = val;
        return this;
    }

    /**  Sweep angle defined from true North in clockwise manner (Units: degrees)*/
    public float getSweepAngle() { return SweepAngle; }

    /**  Sweep angle defined from true North in clockwise manner (Units: degrees)*/
    public AngledAreaSearchTask setSweepAngle( float val ) {
        SweepAngle = val;
        return this;
    }

    /**  Optional start point that must be reached before beginning search (Units: None)*/
    public afrl.cmasi.Location3D getStartPoint() { return StartPoint; }

    /**  Optional start point that must be reached before beginning search (Units: None)*/
    public AngledAreaSearchTask setStartPoint( afrl.cmasi.Location3D val ) {
        StartPoint = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 12; // accounts for primitive types
        size += LMCPUtil.sizeOf(StartPoint);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        SearchAreaID = LMCPUtil.getInt64(in);

        SweepAngle = LMCPUtil.getReal32(in);

            StartPoint = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);

    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putInt64(out, SearchAreaID);
        LMCPUtil.putReal32(out, SweepAngle);
        LMCPUtil.putObject(out, StartPoint);

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
        buf.append( ws + "<AngledAreaSearchTask Series=\"IMPACT\">\n");
        buf.append( ws + "  <SearchAreaID>" + String.valueOf(SearchAreaID) + "</SearchAreaID>\n");
        buf.append( ws + "  <SweepAngle>" + String.valueOf(SweepAngle) + "</SweepAngle>\n");
        if (StartPoint!= null){
           buf.append( ws + "  <StartPoint>\n");
           buf.append( ( StartPoint.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </StartPoint>\n");
        }
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
        buf.append( ws + "</AngledAreaSearchTask>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        AngledAreaSearchTask o = (AngledAreaSearchTask) anotherObj;
        if (SearchAreaID != o.SearchAreaID) return false;
        if (SweepAngle != o.SweepAngle) return false;
        if (StartPoint == null && o.StartPoint != null) return false;
        if ( StartPoint!= null && !StartPoint.equals(o.StartPoint)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)SweepAngle;

        return hash + super.hashCode();
    }
    
}
