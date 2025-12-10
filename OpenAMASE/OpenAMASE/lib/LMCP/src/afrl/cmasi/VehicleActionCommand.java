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
            A message that specifies actions that should be performed by the specified aircraft.  Actions can be navigational modes            or payload actions.        
*/
public class VehicleActionCommand extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 47;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "VehicleActionCommand";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.VehicleActionCommand";

    /**  A unique id for this command. automation services should issue new ids with every new command (Units: None)*/
    @LmcpType("int64")
    protected long CommandID = 0L;
    /**  The id of the vehicle for this command. (Units: None)*/
    @LmcpType("int64")
    protected long VehicleID = 0L;
    /**  a set of actions to be performed immediately by the vehicle. (Units: None)*/
    @LmcpType("VehicleAction")
    protected java.util.ArrayList<afrl.cmasi.VehicleAction> VehicleActionList = new java.util.ArrayList<afrl.cmasi.VehicleAction>();
    /**  Denotes the current execution status of this command. (Units: None)*/
    @LmcpType("CommandStatusType")
    protected afrl.cmasi.CommandStatusType Status = afrl.cmasi.CommandStatusType.Pending;

    
    public VehicleActionCommand() {
    }

    public VehicleActionCommand(long CommandID, long VehicleID, afrl.cmasi.CommandStatusType Status){
        this.CommandID = CommandID;
        this.VehicleID = VehicleID;
        this.Status = Status;
    }


    public VehicleActionCommand clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            VehicleActionCommand newObj = new VehicleActionCommand();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  A unique id for this command. automation services should issue new ids with every new command (Units: None)*/
    public long getCommandID() { return CommandID; }

    /**  A unique id for this command. automation services should issue new ids with every new command (Units: None)*/
    public VehicleActionCommand setCommandID( long val ) {
        CommandID = val;
        return this;
    }

    /**  The id of the vehicle for this command. (Units: None)*/
    public long getVehicleID() { return VehicleID; }

    /**  The id of the vehicle for this command. (Units: None)*/
    public VehicleActionCommand setVehicleID( long val ) {
        VehicleID = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.VehicleAction> getVehicleActionList() {
        return VehicleActionList;
    }

    /**  Denotes the current execution status of this command. (Units: None)*/
    public afrl.cmasi.CommandStatusType getStatus() { return Status; }

    /**  Denotes the current execution status of this command. (Units: None)*/
    public VehicleActionCommand setStatus( afrl.cmasi.CommandStatusType val ) {
        Status = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 20; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(VehicleActionList);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        CommandID = LMCPUtil.getInt64(in);

        VehicleID = LMCPUtil.getInt64(in);

        VehicleActionList.clear();
        int VehicleActionList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<VehicleActionList_len; i++){
        VehicleActionList.add( (afrl.cmasi.VehicleAction) LMCPUtil.getObject(in));
        }
        Status = afrl.cmasi.CommandStatusType.unpack( in );


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, CommandID);
        LMCPUtil.putInt64(out, VehicleID);
        LMCPUtil.putUint16(out, VehicleActionList.size());
        for(int i=0; i<VehicleActionList.size(); i++){
            LMCPUtil.putObject(out, VehicleActionList.get(i));
        }
        Status.pack(out);

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
        buf.append( ws + "<VehicleActionCommand Series=\"CMASI\">\n");
        buf.append( ws + "  <CommandID>" + String.valueOf(CommandID) + "</CommandID>\n");
        buf.append( ws + "  <VehicleID>" + String.valueOf(VehicleID) + "</VehicleID>\n");
        buf.append( ws + "  <VehicleActionList>\n");
        for (int i=0; i<VehicleActionList.size(); i++) {
            buf.append( VehicleActionList.get(i) == null ? ( ws + "    <null/>\n") : (VehicleActionList.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </VehicleActionList>\n");
        buf.append( ws + "  <Status>" + String.valueOf(Status) + "</Status>\n");
        buf.append( ws + "</VehicleActionCommand>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        VehicleActionCommand o = (VehicleActionCommand) anotherObj;
        if (CommandID != o.CommandID) return false;
        if (VehicleID != o.VehicleID) return false;
         if (!VehicleActionList.equals( o.VehicleActionList)) return false;
        if (Status != o.Status) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
