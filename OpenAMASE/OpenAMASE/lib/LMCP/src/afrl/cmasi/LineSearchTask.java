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
 Defines a line search task.            A line search is a list of points that forms a polyline.  The ViewAngleList determines            from which direction the line may be viewed.  View angles are specified using the {@link Wedge} type. If the            UseInertialViewAngles option is true, then wedges are defined in terms of North-East coordinates, otherwise            wedges are defined relative to the line segment currently being viewed (a vector from point i through point i+1).            To be a valid look angle, the line segment must be viewed from an angle within the bounds of the wedge.        
*/
public class LineSearchTask extends afrl.cmasi.SearchTask {
    
    public static final int LMCP_TYPE = 31;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "LineSearchTask";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.LineSearchTask";

    /**  Line to search (Units: None)*/
    @LmcpType("Location3D")
    protected java.util.ArrayList<afrl.cmasi.Location3D> PointList = new java.util.ArrayList<afrl.cmasi.Location3D>();
    /**  Defines a list of acceptable look-angles for this task. See the documentation above for details. (Units: None)*/
    @LmcpType("Wedge")
    protected java.util.ArrayList<afrl.cmasi.Wedge> ViewAngleList = new java.util.ArrayList<afrl.cmasi.Wedge>();
    /**  If true, the ViewAngleList specifies inertial (North-East) angles. See documentation above. (Units: None)*/
    @LmcpType("bool")
    protected boolean UseInertialViewAngles = false;

    
    public LineSearchTask() {
    }

    public LineSearchTask(long TaskID, String Label, float RevisitRate, short Priority, boolean Required, long DwellTime, float GroundSampleDistance, boolean UseInertialViewAngles){
        this.TaskID = TaskID;
        this.Label = Label;
        this.RevisitRate = RevisitRate;
        this.Priority = Priority;
        this.Required = Required;
        this.DwellTime = DwellTime;
        this.GroundSampleDistance = GroundSampleDistance;
        this.UseInertialViewAngles = UseInertialViewAngles;
    }


    public LineSearchTask clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            LineSearchTask newObj = new LineSearchTask();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    public java.util.ArrayList<afrl.cmasi.Location3D> getPointList() {
        return PointList;
    }

    public java.util.ArrayList<afrl.cmasi.Wedge> getViewAngleList() {
        return ViewAngleList;
    }

    /**  If true, the ViewAngleList specifies inertial (North-East) angles. See documentation above. (Units: None)*/
    public boolean getUseInertialViewAngles() { return UseInertialViewAngles; }

    /**  If true, the ViewAngleList specifies inertial (North-East) angles. See documentation above. (Units: None)*/
    public LineSearchTask setUseInertialViewAngles( boolean val ) {
        UseInertialViewAngles = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 1; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(PointList);
        size += 2;
        size += LMCPUtil.sizeOfList(ViewAngleList);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        PointList.clear();
        int PointList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<PointList_len; i++){
        PointList.add( (afrl.cmasi.Location3D) LMCPUtil.getObject(in));
        }
        ViewAngleList.clear();
        int ViewAngleList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<ViewAngleList_len; i++){
        ViewAngleList.add( (afrl.cmasi.Wedge) LMCPUtil.getObject(in));
        }
        UseInertialViewAngles = LMCPUtil.getBool(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putUint16(out, PointList.size());
        for(int i=0; i<PointList.size(); i++){
            LMCPUtil.putObject(out, PointList.get(i));
        }
        LMCPUtil.putUint16(out, ViewAngleList.size());
        for(int i=0; i<ViewAngleList.size(); i++){
            LMCPUtil.putObject(out, ViewAngleList.get(i));
        }
        LMCPUtil.putBool(out, UseInertialViewAngles);

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
        buf.append( ws + "<LineSearchTask Series=\"CMASI\">\n");
        buf.append( ws + "  <PointList>\n");
        for (int i=0; i<PointList.size(); i++) {
            buf.append( PointList.get(i) == null ? ( ws + "    <null/>\n") : (PointList.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </PointList>\n");
        buf.append( ws + "  <ViewAngleList>\n");
        for (int i=0; i<ViewAngleList.size(); i++) {
            buf.append( ViewAngleList.get(i) == null ? ( ws + "    <null/>\n") : (ViewAngleList.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </ViewAngleList>\n");
        buf.append( ws + "  <UseInertialViewAngles>" + String.valueOf(UseInertialViewAngles) + "</UseInertialViewAngles>\n");
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
        buf.append( ws + "</LineSearchTask>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        LineSearchTask o = (LineSearchTask) anotherObj;
         if (!PointList.equals( o.PointList)) return false;
         if (!ViewAngleList.equals( o.ViewAngleList)) return false;
        if (UseInertialViewAngles != o.UseInertialViewAngles) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
