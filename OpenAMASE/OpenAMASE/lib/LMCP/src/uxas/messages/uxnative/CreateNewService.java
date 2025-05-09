// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package uxas.messages.uxnative;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Create a new service in UxAS 
*/
public class CreateNewService extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 3;

    public static final String SERIES_NAME = "UXNATIVE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6149751333668345413L;
    public static final int SERIES_VERSION = 9;


    private static final String TYPE_NAME = "CreateNewService";

    private static final String FULL_LMCP_TYPE_NAME = "uxas.messages.uxnative.CreateNewService";

    /**  Globally unique ID for this service. Negative ServiceIDs are invalid. If ServiceID == 0, then a unique service ID will be generated. (Units: None)*/
    @LmcpType("int64")
    protected long ServiceID = 0L;
    /**  XML configuration for this service (i.e. options in XML format) (Units: None)*/
    @LmcpType("string")
    protected String XmlConfiguration = "";
    /**  Existing entity configurations in the system when this new service is created (Units: None)*/
    @LmcpType("EntityConfiguration")
    protected java.util.ArrayList<afrl.cmasi.EntityConfiguration> EntityConfigurations = new java.util.ArrayList<afrl.cmasi.EntityConfiguration>();
    /**  Existing entity states in the system when this new service is created (Units: None)*/
    @LmcpType("EntityState")
    protected java.util.ArrayList<afrl.cmasi.EntityState> EntityStates = new java.util.ArrayList<afrl.cmasi.EntityState>();
    /**  Existing mission commands for vehicles in the system when this new service is created (Units: None)*/
    @LmcpType("MissionCommand")
    protected java.util.ArrayList<afrl.cmasi.MissionCommand> MissionCommands = new java.util.ArrayList<afrl.cmasi.MissionCommand>();
    /**  Defined areas of interest at time of service creation (Units: None)*/
    @LmcpType("AreaOfInterest")
    protected java.util.ArrayList<afrl.impact.AreaOfInterest> Areas = new java.util.ArrayList<afrl.impact.AreaOfInterest>();
    /**  Defined lines of interest at time of service creation (Units: None)*/
    @LmcpType("LineOfInterest")
    protected java.util.ArrayList<afrl.impact.LineOfInterest> Lines = new java.util.ArrayList<afrl.impact.LineOfInterest>();
    /**  Defined points of interest at time of service creation (Units: None)*/
    @LmcpType("PointOfInterest")
    protected java.util.ArrayList<afrl.impact.PointOfInterest> Points = new java.util.ArrayList<afrl.impact.PointOfInterest>();
    /**  Defined keep in zones at time of service creation (Units: None)*/
    @LmcpType("KeepInZone")
    protected java.util.ArrayList<afrl.cmasi.KeepInZone> KeepInZones = new java.util.ArrayList<afrl.cmasi.KeepInZone>();
    /**  Defined keep out zones at time of service creation (Units: None)*/
    @LmcpType("KeepOutZone")
    protected java.util.ArrayList<afrl.cmasi.KeepOutZone> KeepOutZones = new java.util.ArrayList<afrl.cmasi.KeepOutZone>();
    /**  Defined opearting regions at time of service creation (Units: None)*/
    @LmcpType("OperatingRegion")
    protected java.util.ArrayList<afrl.cmasi.OperatingRegion> OperatingRegions = new java.util.ArrayList<afrl.cmasi.OperatingRegion>();

    
    public CreateNewService() {
    }

    public CreateNewService(long ServiceID, String XmlConfiguration){
        this.ServiceID = ServiceID;
        this.XmlConfiguration = XmlConfiguration;
    }


    public CreateNewService clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            CreateNewService newObj = new CreateNewService();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Globally unique ID for this service. Negative ServiceIDs are invalid. If ServiceID == 0, then a unique service ID will be generated. (Units: None)*/
    public long getServiceID() { return ServiceID; }

    /**  Globally unique ID for this service. Negative ServiceIDs are invalid. If ServiceID == 0, then a unique service ID will be generated. (Units: None)*/
    public CreateNewService setServiceID( long val ) {
        ServiceID = val;
        return this;
    }

    /**  XML configuration for this service (i.e. options in XML format) (Units: None)*/
    public String getXmlConfiguration() { return XmlConfiguration; }

    /**  XML configuration for this service (i.e. options in XML format) (Units: None)*/
    public CreateNewService setXmlConfiguration( String val ) {
        XmlConfiguration = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.EntityConfiguration> getEntityConfigurations() {
        return EntityConfigurations;
    }

    public java.util.ArrayList<afrl.cmasi.EntityState> getEntityStates() {
        return EntityStates;
    }

    public java.util.ArrayList<afrl.cmasi.MissionCommand> getMissionCommands() {
        return MissionCommands;
    }

    public java.util.ArrayList<afrl.impact.AreaOfInterest> getAreas() {
        return Areas;
    }

    public java.util.ArrayList<afrl.impact.LineOfInterest> getLines() {
        return Lines;
    }

    public java.util.ArrayList<afrl.impact.PointOfInterest> getPoints() {
        return Points;
    }

    public java.util.ArrayList<afrl.cmasi.KeepInZone> getKeepInZones() {
        return KeepInZones;
    }

    public java.util.ArrayList<afrl.cmasi.KeepOutZone> getKeepOutZones() {
        return KeepOutZones;
    }

    public java.util.ArrayList<afrl.cmasi.OperatingRegion> getOperatingRegions() {
        return OperatingRegions;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types
        size += LMCPUtil.sizeOfString(XmlConfiguration);
        size += 2;
        size += LMCPUtil.sizeOfList(EntityConfigurations);
        size += 2;
        size += LMCPUtil.sizeOfList(EntityStates);
        size += 2;
        size += LMCPUtil.sizeOfList(MissionCommands);
        size += 2;
        size += LMCPUtil.sizeOfList(Areas);
        size += 2;
        size += LMCPUtil.sizeOfList(Lines);
        size += 2;
        size += LMCPUtil.sizeOfList(Points);
        size += 2;
        size += LMCPUtil.sizeOfList(KeepInZones);
        size += 2;
        size += LMCPUtil.sizeOfList(KeepOutZones);
        size += 2;
        size += LMCPUtil.sizeOfList(OperatingRegions);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        ServiceID = LMCPUtil.getInt64(in);

        XmlConfiguration = LMCPUtil.getString(in);

        EntityConfigurations.clear();
        int EntityConfigurations_len = LMCPUtil.getUint16(in);
        for(int i=0; i<EntityConfigurations_len; i++){
        EntityConfigurations.add( (afrl.cmasi.EntityConfiguration) LMCPUtil.getObject(in));
        }
        EntityStates.clear();
        int EntityStates_len = LMCPUtil.getUint16(in);
        for(int i=0; i<EntityStates_len; i++){
        EntityStates.add( (afrl.cmasi.EntityState) LMCPUtil.getObject(in));
        }
        MissionCommands.clear();
        int MissionCommands_len = LMCPUtil.getUint16(in);
        for(int i=0; i<MissionCommands_len; i++){
        MissionCommands.add( (afrl.cmasi.MissionCommand) LMCPUtil.getObject(in));
        }
        Areas.clear();
        int Areas_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Areas_len; i++){
        Areas.add( (afrl.impact.AreaOfInterest) LMCPUtil.getObject(in));
        }
        Lines.clear();
        int Lines_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Lines_len; i++){
        Lines.add( (afrl.impact.LineOfInterest) LMCPUtil.getObject(in));
        }
        Points.clear();
        int Points_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Points_len; i++){
        Points.add( (afrl.impact.PointOfInterest) LMCPUtil.getObject(in));
        }
        KeepInZones.clear();
        int KeepInZones_len = LMCPUtil.getUint16(in);
        for(int i=0; i<KeepInZones_len; i++){
        KeepInZones.add( (afrl.cmasi.KeepInZone) LMCPUtil.getObject(in));
        }
        KeepOutZones.clear();
        int KeepOutZones_len = LMCPUtil.getUint16(in);
        for(int i=0; i<KeepOutZones_len; i++){
        KeepOutZones.add( (afrl.cmasi.KeepOutZone) LMCPUtil.getObject(in));
        }
        OperatingRegions.clear();
        int OperatingRegions_len = LMCPUtil.getUint16(in);
        for(int i=0; i<OperatingRegions_len; i++){
        OperatingRegions.add( (afrl.cmasi.OperatingRegion) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, ServiceID);
        LMCPUtil.putString(out, XmlConfiguration);
        LMCPUtil.putUint16(out, EntityConfigurations.size());
        for(int i=0; i<EntityConfigurations.size(); i++){
            LMCPUtil.putObject(out, EntityConfigurations.get(i));
        }
        LMCPUtil.putUint16(out, EntityStates.size());
        for(int i=0; i<EntityStates.size(); i++){
            LMCPUtil.putObject(out, EntityStates.get(i));
        }
        LMCPUtil.putUint16(out, MissionCommands.size());
        for(int i=0; i<MissionCommands.size(); i++){
            LMCPUtil.putObject(out, MissionCommands.get(i));
        }
        LMCPUtil.putUint16(out, Areas.size());
        for(int i=0; i<Areas.size(); i++){
            LMCPUtil.putObject(out, Areas.get(i));
        }
        LMCPUtil.putUint16(out, Lines.size());
        for(int i=0; i<Lines.size(); i++){
            LMCPUtil.putObject(out, Lines.get(i));
        }
        LMCPUtil.putUint16(out, Points.size());
        for(int i=0; i<Points.size(); i++){
            LMCPUtil.putObject(out, Points.get(i));
        }
        LMCPUtil.putUint16(out, KeepInZones.size());
        for(int i=0; i<KeepInZones.size(); i++){
            LMCPUtil.putObject(out, KeepInZones.get(i));
        }
        LMCPUtil.putUint16(out, KeepOutZones.size());
        for(int i=0; i<KeepOutZones.size(); i++){
            LMCPUtil.putObject(out, KeepOutZones.get(i));
        }
        LMCPUtil.putUint16(out, OperatingRegions.size());
        for(int i=0; i<OperatingRegions.size(); i++){
            LMCPUtil.putObject(out, OperatingRegions.get(i));
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
        buf.append( ws + "<CreateNewService Series=\"UXNATIVE\">\n");
        buf.append( ws + "  <ServiceID>" + String.valueOf(ServiceID) + "</ServiceID>\n");
        buf.append( ws + "  <XmlConfiguration>" + String.valueOf(XmlConfiguration) + "</XmlConfiguration>\n");
        buf.append( ws + "  <EntityConfigurations>\n");
        for (int i=0; i<EntityConfigurations.size(); i++) {
            buf.append( EntityConfigurations.get(i) == null ? ( ws + "    <null/>\n") : (EntityConfigurations.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </EntityConfigurations>\n");
        buf.append( ws + "  <EntityStates>\n");
        for (int i=0; i<EntityStates.size(); i++) {
            buf.append( EntityStates.get(i) == null ? ( ws + "    <null/>\n") : (EntityStates.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </EntityStates>\n");
        buf.append( ws + "  <MissionCommands>\n");
        for (int i=0; i<MissionCommands.size(); i++) {
            buf.append( MissionCommands.get(i) == null ? ( ws + "    <null/>\n") : (MissionCommands.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </MissionCommands>\n");
        buf.append( ws + "  <Areas>\n");
        for (int i=0; i<Areas.size(); i++) {
            buf.append( Areas.get(i) == null ? ( ws + "    <null/>\n") : (Areas.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Areas>\n");
        buf.append( ws + "  <Lines>\n");
        for (int i=0; i<Lines.size(); i++) {
            buf.append( Lines.get(i) == null ? ( ws + "    <null/>\n") : (Lines.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Lines>\n");
        buf.append( ws + "  <Points>\n");
        for (int i=0; i<Points.size(); i++) {
            buf.append( Points.get(i) == null ? ( ws + "    <null/>\n") : (Points.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Points>\n");
        buf.append( ws + "  <KeepInZones>\n");
        for (int i=0; i<KeepInZones.size(); i++) {
            buf.append( KeepInZones.get(i) == null ? ( ws + "    <null/>\n") : (KeepInZones.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </KeepInZones>\n");
        buf.append( ws + "  <KeepOutZones>\n");
        for (int i=0; i<KeepOutZones.size(); i++) {
            buf.append( KeepOutZones.get(i) == null ? ( ws + "    <null/>\n") : (KeepOutZones.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </KeepOutZones>\n");
        buf.append( ws + "  <OperatingRegions>\n");
        for (int i=0; i<OperatingRegions.size(); i++) {
            buf.append( OperatingRegions.get(i) == null ? ( ws + "    <null/>\n") : (OperatingRegions.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </OperatingRegions>\n");
        buf.append( ws + "</CreateNewService>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        CreateNewService o = (CreateNewService) anotherObj;
        if (ServiceID != o.ServiceID) return false;
        if (XmlConfiguration == null && o.XmlConfiguration != null) return false;
        if ( XmlConfiguration!= null && !XmlConfiguration.equals(o.XmlConfiguration)) return false;
         if (!EntityConfigurations.equals( o.EntityConfigurations)) return false;
         if (!EntityStates.equals( o.EntityStates)) return false;
         if (!MissionCommands.equals( o.MissionCommands)) return false;
         if (!Areas.equals( o.Areas)) return false;
         if (!Lines.equals( o.Lines)) return false;
         if (!Points.equals( o.Points)) return false;
         if (!KeepInZones.equals( o.KeepInZones)) return false;
         if (!KeepOutZones.equals( o.KeepOutZones)) return false;
         if (!OperatingRegions.equals( o.OperatingRegions)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
