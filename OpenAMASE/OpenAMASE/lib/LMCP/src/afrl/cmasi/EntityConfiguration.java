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
            Provides information regarding a base entitie's configuration items.        
*/
public class EntityConfiguration extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 11;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "EntityConfiguration";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.EntityConfiguration";

    /**  A unique id for this entity (Units: None)*/
    @LmcpType("int64")
    protected long ID = 0L;
    /**  the team or "side" that an entity belongs to. This can be a name of a country, a "friend" or "foe" designation, or a team name (e.g. "red team"). (Units: None)*/
    @LmcpType("string")
    protected String Affiliation = "Unknown";
    /**  The type of this entity. The content of this field is dependent on the session. The value of this field is not specifically defined, so it is up to the simulation/session components to define a type system. For a common entity definition, see MIL STD 2525 or use DIS enumerations. (Units: None)*/
    @LmcpType("string")
    protected String EntityType = "";
    /**  An optional text string for the vehicle. This is not necessarily unique, and is included for information only. ID should be used to uniquely identify entities. (Units: None)*/
    @LmcpType("string")
    protected String Label = "";
    /**  The speed that is typically commanded for this entity (Units: meter/sec)*/
    @LmcpType("real32")
    protected float NominalSpeed = (float)0;
    /**  The altitude that is typically commanded for this entity (Units: meter)*/
    @LmcpType("real32")
    protected float NominalAltitude = (float)0.0;
    /**  Altitude type for nominal altitude (Units: None)*/
    @LmcpType("AltitudeType")
    protected afrl.cmasi.AltitudeType NominalAltitudeType = afrl.cmasi.AltitudeType.AGL;
    /**  A list of all payload configurations for this vehicle. Examples of payloads include: gimbaled sensors, SAR radars (not yet supported), air-launched UAVs (not yet supported), and weapons (not yet supported). (Units: None)*/
    @LmcpType("PayloadConfiguration")
    protected java.util.ArrayList<afrl.cmasi.PayloadConfiguration> PayloadConfigurationList = new java.util.ArrayList<afrl.cmasi.PayloadConfiguration>();
    /**  A list that maps keys to values for the inclusion of extra, custom information about this entity (Units: None)*/
    @LmcpType("KeyValuePair")
    protected java.util.ArrayList<afrl.cmasi.KeyValuePair> Info = new java.util.ArrayList<afrl.cmasi.KeyValuePair>();

    
    public EntityConfiguration() {
    }

    public EntityConfiguration(long ID, String Affiliation, String EntityType, String Label, float NominalSpeed, float NominalAltitude, afrl.cmasi.AltitudeType NominalAltitudeType){
        this.ID = ID;
        this.Affiliation = Affiliation;
        this.EntityType = EntityType;
        this.Label = Label;
        this.NominalSpeed = NominalSpeed;
        this.NominalAltitude = NominalAltitude;
        this.NominalAltitudeType = NominalAltitudeType;
    }


    public EntityConfiguration clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            EntityConfiguration newObj = new EntityConfiguration();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  A unique id for this entity (Units: None)*/
    public long getID() { return ID; }

    /**  A unique id for this entity (Units: None)*/
    public EntityConfiguration setID( long val ) {
        ID = val;
        return this;
    }

    /**  the team or "side" that an entity belongs to. This can be a name of a country, a "friend" or "foe" designation, or a team name (e.g. "red team"). (Units: None)*/
    public String getAffiliation() { return Affiliation; }

    /**  the team or "side" that an entity belongs to. This can be a name of a country, a "friend" or "foe" designation, or a team name (e.g. "red team"). (Units: None)*/
    public EntityConfiguration setAffiliation( String val ) {
        Affiliation = val;
        return this;
    }

    /**  The type of this entity. The content of this field is dependent on the session. The value of this field is not specifically defined, so it is up to the simulation/session components to define a type system. For a common entity definition, see MIL STD 2525 or use DIS enumerations. (Units: None)*/
    public String getEntityType() { return EntityType; }

    /**  The type of this entity. The content of this field is dependent on the session. The value of this field is not specifically defined, so it is up to the simulation/session components to define a type system. For a common entity definition, see MIL STD 2525 or use DIS enumerations. (Units: None)*/
    public EntityConfiguration setEntityType( String val ) {
        EntityType = val;
        return this;
    }

    /**  An optional text string for the vehicle. This is not necessarily unique, and is included for information only. ID should be used to uniquely identify entities. (Units: None)*/
    public String getLabel() { return Label; }

    /**  An optional text string for the vehicle. This is not necessarily unique, and is included for information only. ID should be used to uniquely identify entities. (Units: None)*/
    public EntityConfiguration setLabel( String val ) {
        Label = val;
        return this;
    }

    /**  The speed that is typically commanded for this entity (Units: meter/sec)*/
    public float getNominalSpeed() { return NominalSpeed; }

    /**  The speed that is typically commanded for this entity (Units: meter/sec)*/
    public EntityConfiguration setNominalSpeed( float val ) {
        NominalSpeed = val;
        return this;
    }

    /**  The altitude that is typically commanded for this entity (Units: meter)*/
    public float getNominalAltitude() { return NominalAltitude; }

    /**  The altitude that is typically commanded for this entity (Units: meter)*/
    public EntityConfiguration setNominalAltitude( float val ) {
        NominalAltitude = val;
        return this;
    }

    /**  Altitude type for nominal altitude (Units: None)*/
    public afrl.cmasi.AltitudeType getNominalAltitudeType() { return NominalAltitudeType; }

    /**  Altitude type for nominal altitude (Units: None)*/
    public EntityConfiguration setNominalAltitudeType( afrl.cmasi.AltitudeType val ) {
        NominalAltitudeType = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.PayloadConfiguration> getPayloadConfigurationList() {
        return PayloadConfigurationList;
    }

    public java.util.ArrayList<afrl.cmasi.KeyValuePair> getInfo() {
        return Info;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 20; // accounts for primitive types
        size += LMCPUtil.sizeOfString(Affiliation);
        size += LMCPUtil.sizeOfString(EntityType);
        size += LMCPUtil.sizeOfString(Label);
        size += 2;
        size += LMCPUtil.sizeOfList(PayloadConfigurationList);
        size += 2;
        size += LMCPUtil.sizeOfList(Info);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        ID = LMCPUtil.getInt64(in);

        Affiliation = LMCPUtil.getString(in);

        EntityType = LMCPUtil.getString(in);

        Label = LMCPUtil.getString(in);

        NominalSpeed = LMCPUtil.getReal32(in);

        NominalAltitude = LMCPUtil.getReal32(in);

        NominalAltitudeType = afrl.cmasi.AltitudeType.unpack( in );

        PayloadConfigurationList.clear();
        int PayloadConfigurationList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<PayloadConfigurationList_len; i++){
        PayloadConfigurationList.add( (afrl.cmasi.PayloadConfiguration) LMCPUtil.getObject(in));
        }
        Info.clear();
        int Info_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Info_len; i++){
        Info.add( (afrl.cmasi.KeyValuePair) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, ID);
        LMCPUtil.putString(out, Affiliation);
        LMCPUtil.putString(out, EntityType);
        LMCPUtil.putString(out, Label);
        LMCPUtil.putReal32(out, NominalSpeed);
        LMCPUtil.putReal32(out, NominalAltitude);
        NominalAltitudeType.pack(out);
        LMCPUtil.putUint16(out, PayloadConfigurationList.size());
        for(int i=0; i<PayloadConfigurationList.size(); i++){
            LMCPUtil.putObject(out, PayloadConfigurationList.get(i));
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
        buf.append( ws + "<EntityConfiguration Series=\"CMASI\">\n");
        buf.append( ws + "  <ID>" + String.valueOf(ID) + "</ID>\n");
        buf.append( ws + "  <Affiliation>" + String.valueOf(Affiliation) + "</Affiliation>\n");
        buf.append( ws + "  <EntityType>" + String.valueOf(EntityType) + "</EntityType>\n");
        buf.append( ws + "  <Label>" + String.valueOf(Label) + "</Label>\n");
        buf.append( ws + "  <NominalSpeed>" + String.valueOf(NominalSpeed) + "</NominalSpeed>\n");
        buf.append( ws + "  <NominalAltitude>" + String.valueOf(NominalAltitude) + "</NominalAltitude>\n");
        buf.append( ws + "  <NominalAltitudeType>" + String.valueOf(NominalAltitudeType) + "</NominalAltitudeType>\n");
        buf.append( ws + "  <PayloadConfigurationList>\n");
        for (int i=0; i<PayloadConfigurationList.size(); i++) {
            buf.append( PayloadConfigurationList.get(i) == null ? ( ws + "    <null/>\n") : (PayloadConfigurationList.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </PayloadConfigurationList>\n");
        buf.append( ws + "  <Info>\n");
        for (int i=0; i<Info.size(); i++) {
            buf.append( Info.get(i) == null ? ( ws + "    <null/>\n") : (Info.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Info>\n");
        buf.append( ws + "</EntityConfiguration>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        EntityConfiguration o = (EntityConfiguration) anotherObj;
        if (ID != o.ID) return false;
        if (Affiliation == null && o.Affiliation != null) return false;
        if ( Affiliation!= null && !Affiliation.equals(o.Affiliation)) return false;
        if (EntityType == null && o.EntityType != null) return false;
        if ( EntityType!= null && !EntityType.equals(o.EntityType)) return false;
        if (Label == null && o.Label != null) return false;
        if ( Label!= null && !Label.equals(o.Label)) return false;
        if (NominalSpeed != o.NominalSpeed) return false;
        if (NominalAltitude != o.NominalAltitude) return false;
        if (NominalAltitudeType != o.NominalAltitudeType) return false;
         if (!PayloadConfigurationList.equals( o.PayloadConfigurationList)) return false;
         if (!Info.equals( o.Info)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)NominalSpeed;

        return hash + super.hashCode();
    }
    
}
