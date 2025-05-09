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
 Indicates a radio tower 
*/
public class RadioTowerConfiguration extends afrl.cmasi.EntityConfiguration {
    
    public static final int LMCP_TYPE = 3;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "RadioTowerConfiguration";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.RadioTowerConfiguration";

    /**  The position of the tower. A valid RadioTowerConfiguration must define Position (null not allowed) (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D Position = new afrl.cmasi.Location3D();
    /**  The range of the radio (Units: meters)*/
    @LmcpType("real32")
    protected float Range = (float)1500.0;
    /**  Whether the radio is enabled (Units: None)*/
    @LmcpType("bool")
    protected boolean Enabled = true;

    
    public RadioTowerConfiguration() {
    }

    public RadioTowerConfiguration(long ID, String Affiliation, String EntityType, String Label, float NominalSpeed, float NominalAltitude, afrl.cmasi.AltitudeType NominalAltitudeType, afrl.cmasi.Location3D Position, float Range, boolean Enabled){
        this.ID = ID;
        this.Affiliation = Affiliation;
        this.EntityType = EntityType;
        this.Label = Label;
        this.NominalSpeed = NominalSpeed;
        this.NominalAltitude = NominalAltitude;
        this.NominalAltitudeType = NominalAltitudeType;
        this.Position = Position;
        this.Range = Range;
        this.Enabled = Enabled;
    }


    public RadioTowerConfiguration clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            RadioTowerConfiguration newObj = new RadioTowerConfiguration();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  The position of the tower. A valid RadioTowerConfiguration must define Position (null not allowed) (Units: None)*/
    public afrl.cmasi.Location3D getPosition() { return Position; }

    /**  The position of the tower. A valid RadioTowerConfiguration must define Position (null not allowed) (Units: None)*/
    public RadioTowerConfiguration setPosition( afrl.cmasi.Location3D val ) {
        Position = val;
        return this;
    }

    /**  The range of the radio (Units: meters)*/
    public float getRange() { return Range; }

    /**  The range of the radio (Units: meters)*/
    public RadioTowerConfiguration setRange( float val ) {
        Range = val;
        return this;
    }

    /**  Whether the radio is enabled (Units: None)*/
    public boolean getEnabled() { return Enabled; }

    /**  Whether the radio is enabled (Units: None)*/
    public RadioTowerConfiguration setEnabled( boolean val ) {
        Enabled = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 5; // accounts for primitive types
        size += LMCPUtil.sizeOf(Position);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
            Position = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        Range = LMCPUtil.getReal32(in);

        Enabled = LMCPUtil.getBool(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putObject(out, Position);
        LMCPUtil.putReal32(out, Range);
        LMCPUtil.putBool(out, Enabled);

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
        buf.append( ws + "<RadioTowerConfiguration Series=\"IMPACT\">\n");
        if (Position!= null){
           buf.append( ws + "  <Position>\n");
           buf.append( ( Position.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </Position>\n");
        }
        buf.append( ws + "  <Range>" + String.valueOf(Range) + "</Range>\n");
        buf.append( ws + "  <Enabled>" + String.valueOf(Enabled) + "</Enabled>\n");
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
        buf.append( ws + "</RadioTowerConfiguration>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        RadioTowerConfiguration o = (RadioTowerConfiguration) anotherObj;
        if (Position == null && o.Position != null) return false;
        if ( Position!= null && !Position.equals(o.Position)) return false;
        if (Range != o.Range) return false;
        if (Enabled != o.Enabled) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)Range;

        return hash + super.hashCode();
    }
    
}
