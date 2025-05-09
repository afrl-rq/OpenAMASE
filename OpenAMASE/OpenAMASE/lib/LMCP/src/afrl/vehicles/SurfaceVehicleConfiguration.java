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
public class SurfaceVehicleConfiguration extends afrl.cmasi.EntityConfiguration {
    
    public static final int LMCP_TYPE = 3;

    public static final String SERIES_NAME = "VEHICLES";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6216454340153722195L;
    public static final int SERIES_VERSION = 1;


    private static final String TYPE_NAME = "SurfaceVehicleConfiguration";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.vehicles.SurfaceVehicleConfiguration";

    /**  Valid operational water zone (by ID). USV adheres to this area (Units: None)*/
    @LmcpType("int64")
    protected long WaterArea = 0L;
    /**  The minimum speed that should be commanded for this vehicle (Units: meter/sec)*/
    @LmcpType("real32")
    protected float MinimumSpeed = (float)0;
    /**  The maximum speed that should be commanded for this vehicle (Units: meter/sec)*/
    @LmcpType("real32")
    protected float MaximumSpeed = (float)0;
    /**  The consumption rate of available energy at nominal speed, expressed in terms of the percentage of maximum capacity used per second. (Units: %/sec)*/
    @LmcpType("real32")
    protected float EnergyRate = (float)0;
    /**  The maximum angle that this vehicle will bank (Units: degree)*/
    @LmcpType("real32")
    protected float MaxBankAngle = (float)0;
    /**  The maximum angular rate that this vehicle will bank (Units: degree/sec)*/
    @LmcpType("real32")
    protected float MaxBankRate = (float)0;

    
    public SurfaceVehicleConfiguration() {
    }

    public SurfaceVehicleConfiguration(long ID, String Affiliation, String EntityType, String Label, float NominalSpeed, float NominalAltitude, afrl.cmasi.AltitudeType NominalAltitudeType, long WaterArea, float MinimumSpeed, float MaximumSpeed, float EnergyRate, float MaxBankAngle, float MaxBankRate){
        this.ID = ID;
        this.Affiliation = Affiliation;
        this.EntityType = EntityType;
        this.Label = Label;
        this.NominalSpeed = NominalSpeed;
        this.NominalAltitude = NominalAltitude;
        this.NominalAltitudeType = NominalAltitudeType;
        this.WaterArea = WaterArea;
        this.MinimumSpeed = MinimumSpeed;
        this.MaximumSpeed = MaximumSpeed;
        this.EnergyRate = EnergyRate;
        this.MaxBankAngle = MaxBankAngle;
        this.MaxBankRate = MaxBankRate;
    }


    public SurfaceVehicleConfiguration clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            SurfaceVehicleConfiguration newObj = new SurfaceVehicleConfiguration();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Valid operational water zone (by ID). USV adheres to this area (Units: None)*/
    public long getWaterArea() { return WaterArea; }

    /**  Valid operational water zone (by ID). USV adheres to this area (Units: None)*/
    public SurfaceVehicleConfiguration setWaterArea( long val ) {
        WaterArea = val;
        return this;
    }

    /**  The minimum speed that should be commanded for this vehicle (Units: meter/sec)*/
    public float getMinimumSpeed() { return MinimumSpeed; }

    /**  The minimum speed that should be commanded for this vehicle (Units: meter/sec)*/
    public SurfaceVehicleConfiguration setMinimumSpeed( float val ) {
        MinimumSpeed = val;
        return this;
    }

    /**  The maximum speed that should be commanded for this vehicle (Units: meter/sec)*/
    public float getMaximumSpeed() { return MaximumSpeed; }

    /**  The maximum speed that should be commanded for this vehicle (Units: meter/sec)*/
    public SurfaceVehicleConfiguration setMaximumSpeed( float val ) {
        MaximumSpeed = val;
        return this;
    }

    /**  The consumption rate of available energy at nominal speed, expressed in terms of the percentage of maximum capacity used per second. (Units: %/sec)*/
    public float getEnergyRate() { return EnergyRate; }

    /**  The consumption rate of available energy at nominal speed, expressed in terms of the percentage of maximum capacity used per second. (Units: %/sec)*/
    public SurfaceVehicleConfiguration setEnergyRate( float val ) {
        EnergyRate = val;
        return this;
    }

    /**  The maximum angle that this vehicle will bank (Units: degree)*/
    public float getMaxBankAngle() { return MaxBankAngle; }

    /**  The maximum angle that this vehicle will bank (Units: degree)*/
    public SurfaceVehicleConfiguration setMaxBankAngle( float val ) {
        MaxBankAngle = val;
        return this;
    }

    /**  The maximum angular rate that this vehicle will bank (Units: degree/sec)*/
    public float getMaxBankRate() { return MaxBankRate; }

    /**  The maximum angular rate that this vehicle will bank (Units: degree/sec)*/
    public SurfaceVehicleConfiguration setMaxBankRate( float val ) {
        MaxBankRate = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 28; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        WaterArea = LMCPUtil.getInt64(in);

        MinimumSpeed = LMCPUtil.getReal32(in);

        MaximumSpeed = LMCPUtil.getReal32(in);

        EnergyRate = LMCPUtil.getReal32(in);

        MaxBankAngle = LMCPUtil.getReal32(in);

        MaxBankRate = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putInt64(out, WaterArea);
        LMCPUtil.putReal32(out, MinimumSpeed);
        LMCPUtil.putReal32(out, MaximumSpeed);
        LMCPUtil.putReal32(out, EnergyRate);
        LMCPUtil.putReal32(out, MaxBankAngle);
        LMCPUtil.putReal32(out, MaxBankRate);

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
        buf.append( ws + "<SurfaceVehicleConfiguration Series=\"VEHICLES\">\n");
        buf.append( ws + "  <WaterArea>" + String.valueOf(WaterArea) + "</WaterArea>\n");
        buf.append( ws + "  <MinimumSpeed>" + String.valueOf(MinimumSpeed) + "</MinimumSpeed>\n");
        buf.append( ws + "  <MaximumSpeed>" + String.valueOf(MaximumSpeed) + "</MaximumSpeed>\n");
        buf.append( ws + "  <EnergyRate>" + String.valueOf(EnergyRate) + "</EnergyRate>\n");
        buf.append( ws + "  <MaxBankAngle>" + String.valueOf(MaxBankAngle) + "</MaxBankAngle>\n");
        buf.append( ws + "  <MaxBankRate>" + String.valueOf(MaxBankRate) + "</MaxBankRate>\n");
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
        buf.append( ws + "</SurfaceVehicleConfiguration>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        SurfaceVehicleConfiguration o = (SurfaceVehicleConfiguration) anotherObj;
        if (WaterArea != o.WaterArea) return false;
        if (MinimumSpeed != o.MinimumSpeed) return false;
        if (MaximumSpeed != o.MaximumSpeed) return false;
        if (EnergyRate != o.EnergyRate) return false;
        if (MaxBankAngle != o.MaxBankAngle) return false;
        if (MaxBankRate != o.MaxBankRate) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)MinimumSpeed;
        hash += 31 * (int)MaximumSpeed;
        hash += 31 * (int)EnergyRate;
        hash += 31 * (int)MaxBankAngle;

        return hash + super.hashCode();
    }
    
}
