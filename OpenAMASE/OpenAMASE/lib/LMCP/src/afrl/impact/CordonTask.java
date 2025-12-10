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
 Task for using multiple ground vehicles to block access to an area. Given a        point to secure and a standoff distance, task identifies number (K) routes that must        be blocked to successfully deny access to the area. If there are not enough eligible        vehicles, then this task will use the maximum number of eligible vehicles in a best        effort strategy which attempts to maximize radial coverage. 
*/
public class CordonTask extends afrl.cmasi.Task {
    
    public static final int LMCP_TYPE = 29;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "CordonTask";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.CordonTask";

    /**  Block road access to this location. A valid CordonTask must define CordonLocation (null not allowed) (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D CordonLocation = new afrl.cmasi.Location3D();
    /**  Distance away from 'CordonLocation' that access should be blocked (Units: meters)*/
    @LmcpType("real32")
    protected float StandoffDistance = (float)100;

    
    public CordonTask() {
    }

    public CordonTask(long TaskID, String Label, float RevisitRate, short Priority, boolean Required, afrl.cmasi.Location3D CordonLocation, float StandoffDistance){
        this.TaskID = TaskID;
        this.Label = Label;
        this.RevisitRate = RevisitRate;
        this.Priority = Priority;
        this.Required = Required;
        this.CordonLocation = CordonLocation;
        this.StandoffDistance = StandoffDistance;
    }


    public CordonTask clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            CordonTask newObj = new CordonTask();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Block road access to this location. A valid CordonTask must define CordonLocation (null not allowed) (Units: None)*/
    public afrl.cmasi.Location3D getCordonLocation() { return CordonLocation; }

    /**  Block road access to this location. A valid CordonTask must define CordonLocation (null not allowed) (Units: None)*/
    public CordonTask setCordonLocation( afrl.cmasi.Location3D val ) {
        CordonLocation = val;
        return this;
    }

    /**  Distance away from 'CordonLocation' that access should be blocked (Units: meters)*/
    public float getStandoffDistance() { return StandoffDistance; }

    /**  Distance away from 'CordonLocation' that access should be blocked (Units: meters)*/
    public CordonTask setStandoffDistance( float val ) {
        StandoffDistance = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 4; // accounts for primitive types
        size += LMCPUtil.sizeOf(CordonLocation);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
            CordonLocation = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        StandoffDistance = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putObject(out, CordonLocation);
        LMCPUtil.putReal32(out, StandoffDistance);

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
        buf.append( ws + "<CordonTask Series=\"IMPACT\">\n");
        if (CordonLocation!= null){
           buf.append( ws + "  <CordonLocation>\n");
           buf.append( ( CordonLocation.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </CordonLocation>\n");
        }
        buf.append( ws + "  <StandoffDistance>" + String.valueOf(StandoffDistance) + "</StandoffDistance>\n");
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
        buf.append( ws + "</CordonTask>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        CordonTask o = (CordonTask) anotherObj;
        if (CordonLocation == null && o.CordonLocation != null) return false;
        if ( CordonLocation!= null && !CordonLocation.equals(o.CordonLocation)) return false;
        if (StandoffDistance != o.StandoffDistance) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)StandoffDistance;

        return hash + super.hashCode();
    }
    
}
