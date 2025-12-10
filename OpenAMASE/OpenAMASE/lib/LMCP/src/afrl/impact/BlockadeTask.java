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
 Task for using multiple vehicles to surround an entity, for example,        multiple surface vehicles surrounding incoming enemy ship. 
*/
public class BlockadeTask extends afrl.cmasi.Task {
    
    public static final int LMCP_TYPE = 30;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "BlockadeTask";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.BlockadeTask";

    /**  Entity to surround and deny access (Units: None)*/
    @LmcpType("int64")
    protected long BlockedEntityID = 0L;
    /**  When in blocking formation, the distance that vehicles should stand off. (Units: meters)*/
    @LmcpType("real32")
    protected float StandoffDistance = (float)0;
    /**  Number of vehicles to simultaneously block the target (Units: None)*/
    @LmcpType("byte")
    protected short NumberVehicles = (byte)1;
    /**  Biases the blockade so that more vehicles are between enemy and protected location. If null location is given, then blockade attempts to block in direction of enemy travel (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D ProtectedLocation = null;

    
    public BlockadeTask() {
    }

    public BlockadeTask(long TaskID, String Label, float RevisitRate, short Priority, boolean Required, long BlockedEntityID, float StandoffDistance, short NumberVehicles, afrl.cmasi.Location3D ProtectedLocation){
        this.TaskID = TaskID;
        this.Label = Label;
        this.RevisitRate = RevisitRate;
        this.Priority = Priority;
        this.Required = Required;
        this.BlockedEntityID = BlockedEntityID;
        this.StandoffDistance = StandoffDistance;
        this.NumberVehicles = NumberVehicles;
        this.ProtectedLocation = ProtectedLocation;
    }


    public BlockadeTask clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            BlockadeTask newObj = new BlockadeTask();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Entity to surround and deny access (Units: None)*/
    public long getBlockedEntityID() { return BlockedEntityID; }

    /**  Entity to surround and deny access (Units: None)*/
    public BlockadeTask setBlockedEntityID( long val ) {
        BlockedEntityID = val;
        return this;
    }

    /**  When in blocking formation, the distance that vehicles should stand off. (Units: meters)*/
    public float getStandoffDistance() { return StandoffDistance; }

    /**  When in blocking formation, the distance that vehicles should stand off. (Units: meters)*/
    public BlockadeTask setStandoffDistance( float val ) {
        StandoffDistance = val;
        return this;
    }

    /**  Number of vehicles to simultaneously block the target (Units: None)*/
    public short getNumberVehicles() { return NumberVehicles; }

    /**  Number of vehicles to simultaneously block the target (Units: None)*/
    public BlockadeTask setNumberVehicles( short val ) {
        NumberVehicles = val;
        return this;
    }

    /**  Biases the blockade so that more vehicles are between enemy and protected location. If null location is given, then blockade attempts to block in direction of enemy travel (Units: None)*/
    public afrl.cmasi.Location3D getProtectedLocation() { return ProtectedLocation; }

    /**  Biases the blockade so that more vehicles are between enemy and protected location. If null location is given, then blockade attempts to block in direction of enemy travel (Units: None)*/
    public BlockadeTask setProtectedLocation( afrl.cmasi.Location3D val ) {
        ProtectedLocation = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 13; // accounts for primitive types
        size += LMCPUtil.sizeOf(ProtectedLocation);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        BlockedEntityID = LMCPUtil.getInt64(in);

        StandoffDistance = LMCPUtil.getReal32(in);

        NumberVehicles = LMCPUtil.getByte(in);

            ProtectedLocation = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);

    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putInt64(out, BlockedEntityID);
        LMCPUtil.putReal32(out, StandoffDistance);
        LMCPUtil.putByte(out, NumberVehicles);
        LMCPUtil.putObject(out, ProtectedLocation);

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
        buf.append( ws + "<BlockadeTask Series=\"IMPACT\">\n");
        buf.append( ws + "  <BlockedEntityID>" + String.valueOf(BlockedEntityID) + "</BlockedEntityID>\n");
        buf.append( ws + "  <StandoffDistance>" + String.valueOf(StandoffDistance) + "</StandoffDistance>\n");
        buf.append( ws + "  <NumberVehicles>" + String.valueOf(NumberVehicles) + "</NumberVehicles>\n");
        if (ProtectedLocation!= null){
           buf.append( ws + "  <ProtectedLocation>\n");
           buf.append( ( ProtectedLocation.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </ProtectedLocation>\n");
        }
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
        buf.append( ws + "</BlockadeTask>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        BlockadeTask o = (BlockadeTask) anotherObj;
        if (BlockedEntityID != o.BlockedEntityID) return false;
        if (StandoffDistance != o.StandoffDistance) return false;
        if (NumberVehicles != o.NumberVehicles) return false;
        if (ProtectedLocation == null && o.ProtectedLocation != null) return false;
        if ( ProtectedLocation!= null && !ProtectedLocation.equals(o.ProtectedLocation)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)StandoffDistance;
        hash += 31 * (int)NumberVehicles;

        return hash + super.hashCode();
    }
    
}
