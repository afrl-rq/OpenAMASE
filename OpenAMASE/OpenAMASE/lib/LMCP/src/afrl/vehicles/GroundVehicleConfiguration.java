// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package afrl.vehicles;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Provides information regarding a vehicle's configuration items. 
*/
public class GroundVehicleConfiguration extends afrl.cmasi.EntityConfiguration {
    
    public static final int LMCP_TYPE = 1;

    public static final String SERIES_NAME = "VEHICLES";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6216454340153722195L;
    public static final int SERIES_VERSION = 1;


    private static final String TYPE_NAME = "GroundVehicleConfiguration";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.vehicles.GroundVehicleConfiguration";

    /**  Valid operational road network. UGV adheres to positions defined in this graph. Indicated by previously sent GraphRegion's ID (Units: None)*/
    @LmcpType("int64")
    protected long RoadGraphID = 0L;
    /**  The minimum speed that should be commanded for this vehicle (Units: meter/sec)*/
    @LmcpType("real32")
    protected float MinimumSpeed = (float)0;
    /**  The maximum speed that should be commanded for this vehicle (Units: meter/sec)*/
    @LmcpType("real32")
    protected float MaximumSpeed = (float)0;
    /**  The consumption rate of available energy at nominal speed, expressed in terms of the percentage of maximum capacity used per second. (Units: %/sec)*/
    @LmcpType("real32")
    protected float EnergyRate = (float)0;

    
    public GroundVehicleConfiguration() {
    }

    public GroundVehicleConfiguration(long ID, String Affiliation, String EntityType, String Label, float NominalSpeed, float NominalAltitude, afrl.cmasi.AltitudeType NominalAltitudeType, long RoadGraphID, float MinimumSpeed, float MaximumSpeed, float EnergyRate){
        this.ID = ID;
        this.Affiliation = Affiliation;
        this.EntityType = EntityType;
        this.Label = Label;
        this.NominalSpeed = NominalSpeed;
        this.NominalAltitude = NominalAltitude;
        this.NominalAltitudeType = NominalAltitudeType;
        this.RoadGraphID = RoadGraphID;
        this.MinimumSpeed = MinimumSpeed;
        this.MaximumSpeed = MaximumSpeed;
        this.EnergyRate = EnergyRate;
    }


    public GroundVehicleConfiguration clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            GroundVehicleConfiguration newObj = new GroundVehicleConfiguration();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Valid operational road network. UGV adheres to positions defined in this graph. Indicated by previously sent GraphRegion's ID (Units: None)*/
    public long getRoadGraphID() { return RoadGraphID; }

    /**  Valid operational road network. UGV adheres to positions defined in this graph. Indicated by previously sent GraphRegion's ID (Units: None)*/
    public GroundVehicleConfiguration setRoadGraphID( long val ) {
        RoadGraphID = val;
        return this;
    }

    /**  The minimum speed that should be commanded for this vehicle (Units: meter/sec)*/
    public float getMinimumSpeed() { return MinimumSpeed; }

    /**  The minimum speed that should be commanded for this vehicle (Units: meter/sec)*/
    public GroundVehicleConfiguration setMinimumSpeed( float val ) {
        MinimumSpeed = val;
        return this;
    }

    /**  The maximum speed that should be commanded for this vehicle (Units: meter/sec)*/
    public float getMaximumSpeed() { return MaximumSpeed; }

    /**  The maximum speed that should be commanded for this vehicle (Units: meter/sec)*/
    public GroundVehicleConfiguration setMaximumSpeed( float val ) {
        MaximumSpeed = val;
        return this;
    }

    /**  The consumption rate of available energy at nominal speed, expressed in terms of the percentage of maximum capacity used per second. (Units: %/sec)*/
    public float getEnergyRate() { return EnergyRate; }

    /**  The consumption rate of available energy at nominal speed, expressed in terms of the percentage of maximum capacity used per second. (Units: %/sec)*/
    public GroundVehicleConfiguration setEnergyRate( float val ) {
        EnergyRate = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 20; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        RoadGraphID = LMCPUtil.getInt64(in);

        MinimumSpeed = LMCPUtil.getReal32(in);

        MaximumSpeed = LMCPUtil.getReal32(in);

        EnergyRate = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putInt64(out, RoadGraphID);
        LMCPUtil.putReal32(out, MinimumSpeed);
        LMCPUtil.putReal32(out, MaximumSpeed);
        LMCPUtil.putReal32(out, EnergyRate);

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
        buf.append( ws + "<GroundVehicleConfiguration Series=\"VEHICLES\">\n");
        buf.append( ws + "  <RoadGraphID>" + String.valueOf(RoadGraphID) + "</RoadGraphID>\n");
        buf.append( ws + "  <MinimumSpeed>" + String.valueOf(MinimumSpeed) + "</MinimumSpeed>\n");
        buf.append( ws + "  <MaximumSpeed>" + String.valueOf(MaximumSpeed) + "</MaximumSpeed>\n");
        buf.append( ws + "  <EnergyRate>" + String.valueOf(EnergyRate) + "</EnergyRate>\n");
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
        buf.append( ws + "</GroundVehicleConfiguration>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        GroundVehicleConfiguration o = (GroundVehicleConfiguration) anotherObj;
        if (RoadGraphID != o.RoadGraphID) return false;
        if (MinimumSpeed != o.MinimumSpeed) return false;
        if (MaximumSpeed != o.MaximumSpeed) return false;
        if (EnergyRate != o.EnergyRate) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)MinimumSpeed;
        hash += 31 * (int)MaximumSpeed;
        hash += 31 * (int)EnergyRate;

        return hash + super.hashCode();
    }
    
}
