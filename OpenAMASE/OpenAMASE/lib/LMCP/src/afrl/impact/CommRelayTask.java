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
 Task for providing comm relay support 
*/
public class CommRelayTask extends afrl.cmasi.Task {
    
    public static final int LMCP_TYPE = 28;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "CommRelayTask";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.CommRelayTask";

    /**  ID of entity which requires comm relay support (Units: None)*/
    @LmcpType("int64")
    protected long SupportedEntityID = 0L;
    /**  Destination location for supported entity, if known. (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D DestinationLocation = null;
    /**  ID of entity to which communication will be delivered (i.e. Tower ID) (Units: None)*/
    @LmcpType("int64")
    protected long TowerID = 0L;

    
    public CommRelayTask() {
    }

    public CommRelayTask(long TaskID, String Label, float RevisitRate, short Priority, boolean Required, long SupportedEntityID, afrl.cmasi.Location3D DestinationLocation, long TowerID){
        this.TaskID = TaskID;
        this.Label = Label;
        this.RevisitRate = RevisitRate;
        this.Priority = Priority;
        this.Required = Required;
        this.SupportedEntityID = SupportedEntityID;
        this.DestinationLocation = DestinationLocation;
        this.TowerID = TowerID;
    }


    public CommRelayTask clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            CommRelayTask newObj = new CommRelayTask();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  ID of entity which requires comm relay support (Units: None)*/
    public long getSupportedEntityID() { return SupportedEntityID; }

    /**  ID of entity which requires comm relay support (Units: None)*/
    public CommRelayTask setSupportedEntityID( long val ) {
        SupportedEntityID = val;
        return this;
    }

    /**  Destination location for supported entity, if known. (Units: None)*/
    public afrl.cmasi.Location3D getDestinationLocation() { return DestinationLocation; }

    /**  Destination location for supported entity, if known. (Units: None)*/
    public CommRelayTask setDestinationLocation( afrl.cmasi.Location3D val ) {
        DestinationLocation = val;
        return this;
    }

    /**  ID of entity to which communication will be delivered (i.e. Tower ID) (Units: None)*/
    public long getTowerID() { return TowerID; }

    /**  ID of entity to which communication will be delivered (i.e. Tower ID) (Units: None)*/
    public CommRelayTask setTowerID( long val ) {
        TowerID = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 16; // accounts for primitive types
        size += LMCPUtil.sizeOf(DestinationLocation);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        SupportedEntityID = LMCPUtil.getInt64(in);

            DestinationLocation = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        TowerID = LMCPUtil.getInt64(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putInt64(out, SupportedEntityID);
        LMCPUtil.putObject(out, DestinationLocation);
        LMCPUtil.putInt64(out, TowerID);

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
        buf.append( ws + "<CommRelayTask Series=\"IMPACT\">\n");
        buf.append( ws + "  <SupportedEntityID>" + String.valueOf(SupportedEntityID) + "</SupportedEntityID>\n");
        if (DestinationLocation!= null){
           buf.append( ws + "  <DestinationLocation>\n");
           buf.append( ( DestinationLocation.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </DestinationLocation>\n");
        }
        buf.append( ws + "  <TowerID>" + String.valueOf(TowerID) + "</TowerID>\n");
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
        buf.append( ws + "</CommRelayTask>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        CommRelayTask o = (CommRelayTask) anotherObj;
        if (SupportedEntityID != o.SupportedEntityID) return false;
        if (DestinationLocation == null && o.DestinationLocation != null) return false;
        if ( DestinationLocation!= null && !DestinationLocation.equals(o.DestinationLocation)) return false;
        if (TowerID != o.TowerID) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
