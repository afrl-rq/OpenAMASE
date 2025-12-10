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
            Used to command a vehicle to go into a vehicle-specific hold pattern.        
*/
public class LoiterAction extends afrl.cmasi.NavigationAction {
    
    public static final int LMCP_TYPE = 33;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "LoiterAction";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.LoiterAction";

    /**  The loiter shape that the vehicle should fly (Units: none)*/
    @LmcpType("LoiterType")
    protected afrl.cmasi.LoiterType LoiterType = afrl.cmasi.LoiterType.VehicleDefault;
    /**  The radius for the loiter. May be used for any curvature parameter of the vehicle specific hold pattern. (Units: meter)*/
    @LmcpType("real32")
    protected float Radius = (float)0;
    /**  Direction of the major axis of the vehicle specific hold pattern. For Racetracks and Figure-Eights, this is the direction of the long axis. Hovering loiters, this is direction the aircraft should face in the loiter. (Units: degree)*/
    @LmcpType("real32")
    protected float Axis = (float)0;
    /**  Used in racetrack and figure-eight loiters. For figure-eight loiters, this is the length between the focii of the turn circles. For racetracks, this is the length of the straight-away. Both types have a total length of Length + 2 * Radius. For figure-eight loiters, if Length is less than 2 * radius, then this field is ignored. (Units: meter)*/
    @LmcpType("real32")
    protected float Length = (float)0;
    /**  Direction of travel. (Units: None)*/
    @LmcpType("LoiterDirection")
    protected afrl.cmasi.LoiterDirection Direction = afrl.cmasi.LoiterDirection.VehicleDefault;
    /**  The time to loiter at this point before continuing. A negative time value denotes perpetual orbit. (Units: milliseconds)*/
    @LmcpType("int64")
    protected long Duration = 0L;
    /**  Commanded True Airspeed (Units: meter/sec)*/
    @LmcpType("real32")
    protected float Airspeed = (float)0;
    /**  The geometric center point of the loiter. A valid LoiterAction must define Location (null not allowed). (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D Location = new afrl.cmasi.Location3D();

    
    public LoiterAction() {
    }

    public LoiterAction(afrl.cmasi.LoiterType LoiterType, float Radius, float Axis, float Length, afrl.cmasi.LoiterDirection Direction, long Duration, float Airspeed, afrl.cmasi.Location3D Location){
        this.LoiterType = LoiterType;
        this.Radius = Radius;
        this.Axis = Axis;
        this.Length = Length;
        this.Direction = Direction;
        this.Duration = Duration;
        this.Airspeed = Airspeed;
        this.Location = Location;
    }


    public LoiterAction clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            LoiterAction newObj = new LoiterAction();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  The loiter shape that the vehicle should fly (Units: none)*/
    public afrl.cmasi.LoiterType getLoiterType() { return LoiterType; }

    /**  The loiter shape that the vehicle should fly (Units: none)*/
    public LoiterAction setLoiterType( afrl.cmasi.LoiterType val ) {
        LoiterType = val;
        return this;
    }

    /**  The radius for the loiter. May be used for any curvature parameter of the vehicle specific hold pattern. (Units: meter)*/
    public float getRadius() { return Radius; }

    /**  The radius for the loiter. May be used for any curvature parameter of the vehicle specific hold pattern. (Units: meter)*/
    public LoiterAction setRadius( float val ) {
        Radius = val;
        return this;
    }

    /**  Direction of the major axis of the vehicle specific hold pattern. For Racetracks and Figure-Eights, this is the direction of the long axis. Hovering loiters, this is direction the aircraft should face in the loiter. (Units: degree)*/
    public float getAxis() { return Axis; }

    /**  Direction of the major axis of the vehicle specific hold pattern. For Racetracks and Figure-Eights, this is the direction of the long axis. Hovering loiters, this is direction the aircraft should face in the loiter. (Units: degree)*/
    public LoiterAction setAxis( float val ) {
        Axis = val;
        return this;
    }

    /**  Used in racetrack and figure-eight loiters. For figure-eight loiters, this is the length between the focii of the turn circles. For racetracks, this is the length of the straight-away. Both types have a total length of Length + 2 * Radius. For figure-eight loiters, if Length is less than 2 * radius, then this field is ignored. (Units: meter)*/
    public float getLength() { return Length; }

    /**  Used in racetrack and figure-eight loiters. For figure-eight loiters, this is the length between the focii of the turn circles. For racetracks, this is the length of the straight-away. Both types have a total length of Length + 2 * Radius. For figure-eight loiters, if Length is less than 2 * radius, then this field is ignored. (Units: meter)*/
    public LoiterAction setLength( float val ) {
        Length = val;
        return this;
    }

    /**  Direction of travel. (Units: None)*/
    public afrl.cmasi.LoiterDirection getDirection() { return Direction; }

    /**  Direction of travel. (Units: None)*/
    public LoiterAction setDirection( afrl.cmasi.LoiterDirection val ) {
        Direction = val;
        return this;
    }

    /**  The time to loiter at this point before continuing. A negative time value denotes perpetual orbit. (Units: milliseconds)*/
    public long getDuration() { return Duration; }

    /**  The time to loiter at this point before continuing. A negative time value denotes perpetual orbit. (Units: milliseconds)*/
    public LoiterAction setDuration( long val ) {
        Duration = val;
        return this;
    }

    /**  Commanded True Airspeed (Units: meter/sec)*/
    public float getAirspeed() { return Airspeed; }

    /**  Commanded True Airspeed (Units: meter/sec)*/
    public LoiterAction setAirspeed( float val ) {
        Airspeed = val;
        return this;
    }

    /**  The geometric center point of the loiter. A valid LoiterAction must define Location (null not allowed). (Units: None)*/
    public afrl.cmasi.Location3D getLocation() { return Location; }

    /**  The geometric center point of the loiter. A valid LoiterAction must define Location (null not allowed). (Units: None)*/
    public LoiterAction setLocation( afrl.cmasi.Location3D val ) {
        Location = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 32; // accounts for primitive types
        size += LMCPUtil.sizeOf(Location);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        LoiterType = afrl.cmasi.LoiterType.unpack( in );

        Radius = LMCPUtil.getReal32(in);

        Axis = LMCPUtil.getReal32(in);

        Length = LMCPUtil.getReal32(in);

        Direction = afrl.cmasi.LoiterDirection.unpack( in );

        Duration = LMCPUtil.getInt64(in);

        Airspeed = LMCPUtil.getReal32(in);

            Location = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);

    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LoiterType.pack(out);
        LMCPUtil.putReal32(out, Radius);
        LMCPUtil.putReal32(out, Axis);
        LMCPUtil.putReal32(out, Length);
        Direction.pack(out);
        LMCPUtil.putInt64(out, Duration);
        LMCPUtil.putReal32(out, Airspeed);
        LMCPUtil.putObject(out, Location);

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
        buf.append( ws + "<LoiterAction Series=\"CMASI\">\n");
        buf.append( ws + "  <LoiterType>" + String.valueOf(LoiterType) + "</LoiterType>\n");
        buf.append( ws + "  <Radius>" + String.valueOf(Radius) + "</Radius>\n");
        buf.append( ws + "  <Axis>" + String.valueOf(Axis) + "</Axis>\n");
        buf.append( ws + "  <Length>" + String.valueOf(Length) + "</Length>\n");
        buf.append( ws + "  <Direction>" + String.valueOf(Direction) + "</Direction>\n");
        buf.append( ws + "  <Duration>" + String.valueOf(Duration) + "</Duration>\n");
        buf.append( ws + "  <Airspeed>" + String.valueOf(Airspeed) + "</Airspeed>\n");
        if (Location!= null){
           buf.append( ws + "  <Location>\n");
           buf.append( ( Location.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </Location>\n");
        }
        buf.append( ws + "  <AssociatedTaskList>\n");
        for (int i=0; i<AssociatedTaskList.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(AssociatedTaskList.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </AssociatedTaskList>\n");
        buf.append( ws + "</LoiterAction>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        LoiterAction o = (LoiterAction) anotherObj;
        if (LoiterType != o.LoiterType) return false;
        if (Radius != o.Radius) return false;
        if (Axis != o.Axis) return false;
        if (Length != o.Length) return false;
        if (Direction != o.Direction) return false;
        if (Duration != o.Duration) return false;
        if (Airspeed != o.Airspeed) return false;
        if (Location == null && o.Location != null) return false;
        if ( Location!= null && !Location.equals(o.Location)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)Radius;
        hash += 31 * (int)Axis;
        hash += 31 * (int)Length;

        return hash + super.hashCode();
    }
    
}
