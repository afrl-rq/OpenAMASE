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
 Encapsulates data from an automation service to a client.  The automation service may respond with        MissionCommands, VehicleActionCommands, or no commands at all.  This type is meant to serve as a single        container for commands that are a response to {@link AutomationRequest}        
*/
public class AutomationResponse extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 51;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "AutomationResponse";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.AutomationResponse";

    /**  A list of MissionCommands issued by the automation service (Units: None)*/
    @LmcpType("MissionCommand")
    protected java.util.ArrayList<afrl.cmasi.MissionCommand> MissionCommandList = new java.util.ArrayList<afrl.cmasi.MissionCommand>();
    /**  A list of VehicleActionCommands issued by the automation service (Units: None)*/
    @LmcpType("VehicleActionCommand")
    protected java.util.ArrayList<afrl.cmasi.VehicleActionCommand> VehicleCommandList = new java.util.ArrayList<afrl.cmasi.VehicleActionCommand>();
    /**  Status from the automation service to the simulation regarding errors or conditions. (Units: None)*/
    @LmcpType("KeyValuePair")
    protected java.util.ArrayList<afrl.cmasi.KeyValuePair> Info = new java.util.ArrayList<afrl.cmasi.KeyValuePair>();

    
    public AutomationResponse() {
    }



    public AutomationResponse clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            AutomationResponse newObj = new AutomationResponse();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    public java.util.ArrayList<afrl.cmasi.MissionCommand> getMissionCommandList() {
        return MissionCommandList;
    }

    public java.util.ArrayList<afrl.cmasi.VehicleActionCommand> getVehicleCommandList() {
        return VehicleCommandList;
    }

    public java.util.ArrayList<afrl.cmasi.KeyValuePair> getInfo() {
        return Info;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 0; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(MissionCommandList);
        size += 2;
        size += LMCPUtil.sizeOfList(VehicleCommandList);
        size += 2;
        size += LMCPUtil.sizeOfList(Info);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        MissionCommandList.clear();
        int MissionCommandList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<MissionCommandList_len; i++){
        MissionCommandList.add( (afrl.cmasi.MissionCommand) LMCPUtil.getObject(in));
        }
        VehicleCommandList.clear();
        int VehicleCommandList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<VehicleCommandList_len; i++){
        VehicleCommandList.add( (afrl.cmasi.VehicleActionCommand) LMCPUtil.getObject(in));
        }
        Info.clear();
        int Info_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Info_len; i++){
        Info.add( (afrl.cmasi.KeyValuePair) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putUint16(out, MissionCommandList.size());
        for(int i=0; i<MissionCommandList.size(); i++){
            LMCPUtil.putObject(out, MissionCommandList.get(i));
        }
        LMCPUtil.putUint16(out, VehicleCommandList.size());
        for(int i=0; i<VehicleCommandList.size(); i++){
            LMCPUtil.putObject(out, VehicleCommandList.get(i));
        }
        LMCPUtil.putUint16(out, Info.size());
        for(int i=0; i<Info.size(); i++){
            LMCPUtil.putObject(out, Info.get(i));
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
        buf.append( ws + "<AutomationResponse Series=\"CMASI\">\n");
        buf.append( ws + "  <MissionCommandList>\n");
        for (int i=0; i<MissionCommandList.size(); i++) {
            buf.append( MissionCommandList.get(i) == null ? ( ws + "    <null/>\n") : (MissionCommandList.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </MissionCommandList>\n");
        buf.append( ws + "  <VehicleCommandList>\n");
        for (int i=0; i<VehicleCommandList.size(); i++) {
            buf.append( VehicleCommandList.get(i) == null ? ( ws + "    <null/>\n") : (VehicleCommandList.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </VehicleCommandList>\n");
        buf.append( ws + "  <Info>\n");
        for (int i=0; i<Info.size(); i++) {
            buf.append( Info.get(i) == null ? ( ws + "    <null/>\n") : (Info.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Info>\n");
        buf.append( ws + "</AutomationResponse>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        AutomationResponse o = (AutomationResponse) anotherObj;
         if (!MissionCommandList.equals( o.MissionCommandList)) return false;
         if (!VehicleCommandList.equals( o.VehicleCommandList)) return false;
         if (!Info.equals( o.Info)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
