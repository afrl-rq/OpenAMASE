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
            An action that commands a gimbal to make necessary rotations to allow mounted sensors to stare at the            given location.Individual sensors on the gimbal are controlled via the {@link CameraAction} message.<br/>        
*/
public class GimbalStareAction extends afrl.cmasi.PayloadAction {
    
    public static final int LMCP_TYPE = 26;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "GimbalStareAction";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.GimbalStareAction";

    /**  The commanded stare point. A valid GimbalStareAction must define the Starepoint (null not allowed). (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D Starepoint = new afrl.cmasi.Location3D();
    /**  Duration for the stare operation. Zero denotes an indefinate stare time (Units: milliseconds)*/
    @LmcpType("int64")
    protected long Duration = 0L;

    
    public GimbalStareAction() {
    }

    public GimbalStareAction(long PayloadID, afrl.cmasi.Location3D Starepoint, long Duration){
        this.PayloadID = PayloadID;
        this.Starepoint = Starepoint;
        this.Duration = Duration;
    }


    public GimbalStareAction clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            GimbalStareAction newObj = new GimbalStareAction();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  The commanded stare point. A valid GimbalStareAction must define the Starepoint (null not allowed). (Units: None)*/
    public afrl.cmasi.Location3D getStarepoint() { return Starepoint; }

    /**  The commanded stare point. A valid GimbalStareAction must define the Starepoint (null not allowed). (Units: None)*/
    public GimbalStareAction setStarepoint( afrl.cmasi.Location3D val ) {
        Starepoint = val;
        return this;
    }

    /**  Duration for the stare operation. Zero denotes an indefinate stare time (Units: milliseconds)*/
    public long getDuration() { return Duration; }

    /**  Duration for the stare operation. Zero denotes an indefinate stare time (Units: milliseconds)*/
    public GimbalStareAction setDuration( long val ) {
        Duration = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types
        size += LMCPUtil.sizeOf(Starepoint);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
            Starepoint = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        Duration = LMCPUtil.getInt64(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putObject(out, Starepoint);
        LMCPUtil.putInt64(out, Duration);

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
        buf.append( ws + "<GimbalStareAction Series=\"CMASI\">\n");
        if (Starepoint!= null){
           buf.append( ws + "  <Starepoint>\n");
           buf.append( ( Starepoint.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </Starepoint>\n");
        }
        buf.append( ws + "  <Duration>" + String.valueOf(Duration) + "</Duration>\n");
        buf.append( ws + "  <PayloadID>" + String.valueOf(PayloadID) + "</PayloadID>\n");
        buf.append( ws + "  <AssociatedTaskList>\n");
        for (int i=0; i<AssociatedTaskList.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(AssociatedTaskList.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </AssociatedTaskList>\n");
        buf.append( ws + "</GimbalStareAction>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        GimbalStareAction o = (GimbalStareAction) anotherObj;
        if (Starepoint == null && o.Starepoint != null) return false;
        if ( Starepoint!= null && !Starepoint.equals(o.Starepoint)) return false;
        if (Duration != o.Duration) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
