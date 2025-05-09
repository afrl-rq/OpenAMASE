// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package afrl.cmasi.perceive;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 A report of a potential target/tracked entity.  Perceptions are received from a             given sensor or set of sensors.  Entities can be perceived by scenario aircraft or other entities.        
*/
public class EntityPerception extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 1;

    public static final String SERIES_NAME = "PERCEIVE";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5784119745305990725L;
    public static final int SERIES_VERSION = 1;


    private static final String TYPE_NAME = "EntityPerception";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.perceive.EntityPerception";

    /**  A scenario-unique ID for this target entity. IDs should be greater than zero. Since this is a perception, the entity ID may not be known. In that case, this field should be set to zero. (Units: None)*/
    @LmcpType("uint32")
    protected long PerceivedEntityID = 0L;
    /**  ID of the entity or aircraft that reported the detection/perception (Units: None)*/
    @LmcpType("uint32")
    protected long PerceiverID = 0L;
    /**  The IDs of the payloads that reported this perception. (Units: None)*/
    @LmcpType("uint32")
    protected java.util.ArrayList<Long> PerceiverPayloads = new java.util.ArrayList<Long>();
    /**  The perceived target velocity in three-dimensions. The velocity should be stated in tangential-plane coordinates using the North-East-Down coordinate system. (Units: meter/sec)*/
    @LmcpType("real32")
    protected float[] Velocity = new float[3];
    /**  Velocity error for this perception. The error terms should be in the same coordinate system as the velocity. (Units: meter/sec)*/
    @LmcpType("real32")
    protected float[] VelocityError = new float[3];
    /**  If true, denotes that the velocity and velocity error have meaning. (Units: None)*/
    @LmcpType("bool")
    protected boolean VelocityValid = false;
    /**  Euler angle tangential-plane attitude for this entity. Should be stated as Psi-Theta-Phi. For information on Euler coordinates, see <a href="http://en.wikipedia.org/wiki/Euler_angle">Euler Angles</a> (Units: degree)*/
    @LmcpType("real32")
    protected float[] Attitude = new float[3];
    /**  Error in the perception of the entity attitude. This should be in the same coordinate system as the attitude. (Units: degree)*/
    @LmcpType("real32")
    protected float[] AttitudeError = new float[3];
    /**  If true, denotes that the attitude and attitude error have meaning. (Units: None)*/
    @LmcpType("bool")
    protected boolean AttitudeValid = false;
    /**  The entity location. A valid EntityPerception must define Location (null not allowed) (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D Location = new afrl.cmasi.Location3D();
    /**  Error in the perception of the location of this entity. The first index is North-South error, the second index is the East-West error, and the third index is the vertical error. The result is an error cylinder. (Units: meter)*/
    @LmcpType("real32")
    protected float[] LocationError = new float[3];
    /**  Time that this entity was perceived since scenario start. (Units: millisecond)*/
    @LmcpType("int64")
    protected long TimeLastSeen = 0L;

    
    public EntityPerception() {
    }

    public EntityPerception(long PerceivedEntityID, long PerceiverID, boolean VelocityValid, boolean AttitudeValid, afrl.cmasi.Location3D Location, long TimeLastSeen){
        this.PerceivedEntityID = PerceivedEntityID;
        this.PerceiverID = PerceiverID;
        this.VelocityValid = VelocityValid;
        this.AttitudeValid = AttitudeValid;
        this.Location = Location;
        this.TimeLastSeen = TimeLastSeen;
    }


    public EntityPerception clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            EntityPerception newObj = new EntityPerception();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  A scenario-unique ID for this target entity. IDs should be greater than zero. Since this is a perception, the entity ID may not be known. In that case, this field should be set to zero. (Units: None)*/
    public long getPerceivedEntityID() { return PerceivedEntityID; }

    /**  A scenario-unique ID for this target entity. IDs should be greater than zero. Since this is a perception, the entity ID may not be known. In that case, this field should be set to zero. (Units: None)*/
    public EntityPerception setPerceivedEntityID( long val ) {
        PerceivedEntityID = val;
        return this;
    }

    /**  ID of the entity or aircraft that reported the detection/perception (Units: None)*/
    public long getPerceiverID() { return PerceiverID; }

    /**  ID of the entity or aircraft that reported the detection/perception (Units: None)*/
    public EntityPerception setPerceiverID( long val ) {
        PerceiverID = val;
        return this;
    }

    public java.util.ArrayList<Long> getPerceiverPayloads() {
        return PerceiverPayloads;
    }

    public float[] getVelocity() {
        return Velocity;
    }

    public float[] getVelocityError() {
        return VelocityError;
    }

    /**  If true, denotes that the velocity and velocity error have meaning. (Units: None)*/
    public boolean getVelocityValid() { return VelocityValid; }

    /**  If true, denotes that the velocity and velocity error have meaning. (Units: None)*/
    public EntityPerception setVelocityValid( boolean val ) {
        VelocityValid = val;
        return this;
    }

    public float[] getAttitude() {
        return Attitude;
    }

    public float[] getAttitudeError() {
        return AttitudeError;
    }

    /**  If true, denotes that the attitude and attitude error have meaning. (Units: None)*/
    public boolean getAttitudeValid() { return AttitudeValid; }

    /**  If true, denotes that the attitude and attitude error have meaning. (Units: None)*/
    public EntityPerception setAttitudeValid( boolean val ) {
        AttitudeValid = val;
        return this;
    }

    /**  The entity location. A valid EntityPerception must define Location (null not allowed) (Units: None)*/
    public afrl.cmasi.Location3D getLocation() { return Location; }

    /**  The entity location. A valid EntityPerception must define Location (null not allowed) (Units: None)*/
    public EntityPerception setLocation( afrl.cmasi.Location3D val ) {
        Location = val;
        return this;
    }

    public float[] getLocationError() {
        return LocationError;
    }

    /**  Time that this entity was perceived since scenario start. (Units: millisecond)*/
    public long getTimeLastSeen() { return TimeLastSeen; }

    /**  Time that this entity was perceived since scenario start. (Units: millisecond)*/
    public EntityPerception setTimeLastSeen( long val ) {
        TimeLastSeen = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 78; // accounts for primitive types
        
        size += 2 + 4 * PerceiverPayloads.size();
        size += LMCPUtil.sizeOf(Location);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        PerceivedEntityID = LMCPUtil.getUint32(in);

        PerceiverID = LMCPUtil.getUint32(in);

        PerceiverPayloads.clear();
        int PerceiverPayloads_len = LMCPUtil.getUint16(in);
        for(int i=0; i<PerceiverPayloads_len; i++){
            PerceiverPayloads.add(LMCPUtil.getUint32(in));
        }
        for(int i=0; i<Velocity.length; i++){
            Velocity[i] = LMCPUtil.getReal32(in);
        }
        for(int i=0; i<VelocityError.length; i++){
            VelocityError[i] = LMCPUtil.getReal32(in);
        }
        VelocityValid = LMCPUtil.getBool(in);

        for(int i=0; i<Attitude.length; i++){
            Attitude[i] = LMCPUtil.getReal32(in);
        }
        for(int i=0; i<AttitudeError.length; i++){
            AttitudeError[i] = LMCPUtil.getReal32(in);
        }
        AttitudeValid = LMCPUtil.getBool(in);

            Location = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        for(int i=0; i<LocationError.length; i++){
            LocationError[i] = LMCPUtil.getReal32(in);
        }
        TimeLastSeen = LMCPUtil.getInt64(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putUint32(out, PerceivedEntityID);
        LMCPUtil.putUint32(out, PerceiverID);
        LMCPUtil.putUint16(out, PerceiverPayloads.size());
        for(int i=0; i<PerceiverPayloads.size(); i++){
            LMCPUtil.putUint32(out, PerceiverPayloads.get(i));
        }
        for(int i=0; i<Velocity.length; i++){
            LMCPUtil.putReal32(out, Velocity[i]);
        }
        for(int i=0; i<VelocityError.length; i++){
            LMCPUtil.putReal32(out, VelocityError[i]);
        }
        LMCPUtil.putBool(out, VelocityValid);
        for(int i=0; i<Attitude.length; i++){
            LMCPUtil.putReal32(out, Attitude[i]);
        }
        for(int i=0; i<AttitudeError.length; i++){
            LMCPUtil.putReal32(out, AttitudeError[i]);
        }
        LMCPUtil.putBool(out, AttitudeValid);
        LMCPUtil.putObject(out, Location);
        for(int i=0; i<LocationError.length; i++){
            LMCPUtil.putReal32(out, LocationError[i]);
        }
        LMCPUtil.putInt64(out, TimeLastSeen);

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
        buf.append( ws + "<EntityPerception Series=\"PERCEIVE\">\n");
        buf.append( ws + "  <PerceivedEntityID>" + String.valueOf(PerceivedEntityID) + "</PerceivedEntityID>\n");
        buf.append( ws + "  <PerceiverID>" + String.valueOf(PerceiverID) + "</PerceiverID>\n");
        buf.append( ws + "  <PerceiverPayloads>\n");
        for (int i=0; i<PerceiverPayloads.size(); i++) {
        buf.append( ws + "  <uint32>" + String.valueOf(PerceiverPayloads.get(i)) + "</uint32>\n");
        }
        buf.append( ws + "  </PerceiverPayloads>\n");
        buf.append( ws + "  <Velocity>\n");
        for (int i=0; i<Velocity.length; i++) {
        buf.append( ws + "  <real32>" + String.valueOf(Velocity[i]) + "</real32>\n");
        }
        buf.append( ws + "  </Velocity>\n");
        buf.append( ws + "  <VelocityError>\n");
        for (int i=0; i<VelocityError.length; i++) {
        buf.append( ws + "  <real32>" + String.valueOf(VelocityError[i]) + "</real32>\n");
        }
        buf.append( ws + "  </VelocityError>\n");
        buf.append( ws + "  <VelocityValid>" + String.valueOf(VelocityValid) + "</VelocityValid>\n");
        buf.append( ws + "  <Attitude>\n");
        for (int i=0; i<Attitude.length; i++) {
        buf.append( ws + "  <real32>" + String.valueOf(Attitude[i]) + "</real32>\n");
        }
        buf.append( ws + "  </Attitude>\n");
        buf.append( ws + "  <AttitudeError>\n");
        for (int i=0; i<AttitudeError.length; i++) {
        buf.append( ws + "  <real32>" + String.valueOf(AttitudeError[i]) + "</real32>\n");
        }
        buf.append( ws + "  </AttitudeError>\n");
        buf.append( ws + "  <AttitudeValid>" + String.valueOf(AttitudeValid) + "</AttitudeValid>\n");
        if (Location!= null){
           buf.append( ws + "  <Location>\n");
           buf.append( ( Location.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </Location>\n");
        }
        buf.append( ws + "  <LocationError>\n");
        for (int i=0; i<LocationError.length; i++) {
        buf.append( ws + "  <real32>" + String.valueOf(LocationError[i]) + "</real32>\n");
        }
        buf.append( ws + "  </LocationError>\n");
        buf.append( ws + "  <TimeLastSeen>" + String.valueOf(TimeLastSeen) + "</TimeLastSeen>\n");
        buf.append( ws + "</EntityPerception>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        EntityPerception o = (EntityPerception) anotherObj;
        if (PerceivedEntityID != o.PerceivedEntityID) return false;
        if (PerceiverID != o.PerceiverID) return false;
         if (!PerceiverPayloads.equals( o.PerceiverPayloads)) return false;
         if (!java.util.Arrays.equals(Velocity, o.Velocity)) return false;
         if (!java.util.Arrays.equals(VelocityError, o.VelocityError)) return false;
        if (VelocityValid != o.VelocityValid) return false;
         if (!java.util.Arrays.equals(Attitude, o.Attitude)) return false;
         if (!java.util.Arrays.equals(AttitudeError, o.AttitudeError)) return false;
        if (AttitudeValid != o.AttitudeValid) return false;
        if (Location == null && o.Location != null) return false;
        if ( Location!= null && !Location.equals(o.Location)) return false;
         if (!java.util.Arrays.equals(LocationError, o.LocationError)) return false;
        if (TimeLastSeen != o.TimeLastSeen) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)PerceivedEntityID;
        hash += 31 * (int)PerceiverID;

        return hash + super.hashCode();
    }
    
}
