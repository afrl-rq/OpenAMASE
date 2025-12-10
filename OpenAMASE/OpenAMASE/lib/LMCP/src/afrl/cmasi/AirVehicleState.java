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
            Provides basic information regarding the vehicle state with regard to its location and orientation in the            world, and current velocity in body axis.  <br/>            Provides basic state information regarding entity position, orientation, velocity, and acceleration.            The entity body right-hand coordinate system is used, where X is out the nose, Y is out the right side, and Z            is downward.  The order of rotation from inertial to body coordinates is yaw-pitch-roll. <br/>            The direction cosine matrix (DCM) for conversion from body to inertial coordinates is:<br/><br/>            [cTheta*cPsi, cTheta*sPsi, -sTheta]<br/>            [sPhi*sTheta*cPsi-cPhi*sPsi, sPhi*sTheta*sPsi+cPhi*cPsi, sPhi*cTheta]<br/>            [cPhi*sTheta*cPsi+sPhi*sPsi, cPhi*sTheta*sPsi-sPhi*cPsi, cPhi*cTheta]<br/><br/>            where, c denotes cosine and s denotes sine.            <br/><br/>            To convert from body to inertial velocities, use,<br/>            [North Vel, East Vel, Down Vel]' = DCM' * [Vx, Vy, Vz]'  where [Vx, Vy, Vz] is the body vel or accel.        
*/
public class AirVehicleState extends afrl.cmasi.EntityState {
    
    public static final int LMCP_TYPE = 15;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "AirVehicleState";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.AirVehicleState";

    /**  vehicle true airspeed (Units: meter/sec)*/
    @LmcpType("real32")
    protected float Airspeed = (float)0;
    /**  Vertical speed (positive upwards) of the vehicle in the inertial frame (rate of change of altitude) (Units: meter/sec)*/
    @LmcpType("real32")
    protected float VerticalSpeed = (float)0;
    /**  Wind speed as detected or computed by the vehicle (Units: meter/sec)*/
    @LmcpType("real32")
    protected float WindSpeed = (float)0;
    /**  Wind source direction (true) as detected or computed by the vehicle(Units: degree)*/
    @LmcpType("real32")
    protected float WindDirection = (float)0;

    
    public AirVehicleState() {
    }

    public AirVehicleState(long ID, float u, float v, float w, float udot, float vdot, float wdot, float Heading, float Pitch, float Roll, float p, float q, float r, float Course, float Groundspeed, afrl.cmasi.Location3D Location, float EnergyAvailable, float ActualEnergyRate, long CurrentWaypoint, long CurrentCommand, afrl.cmasi.NavigationMode Mode, long Time, float Airspeed, float VerticalSpeed, float WindSpeed, float WindDirection){
        this.ID = ID;
        this.u = u;
        this.v = v;
        this.w = w;
        this.udot = udot;
        this.vdot = vdot;
        this.wdot = wdot;
        this.Heading = Heading;
        this.Pitch = Pitch;
        this.Roll = Roll;
        this.p = p;
        this.q = q;
        this.r = r;
        this.Course = Course;
        this.Groundspeed = Groundspeed;
        this.Location = Location;
        this.EnergyAvailable = EnergyAvailable;
        this.ActualEnergyRate = ActualEnergyRate;
        this.CurrentWaypoint = CurrentWaypoint;
        this.CurrentCommand = CurrentCommand;
        this.Mode = Mode;
        this.Time = Time;
        this.Airspeed = Airspeed;
        this.VerticalSpeed = VerticalSpeed;
        this.WindSpeed = WindSpeed;
        this.WindDirection = WindDirection;
    }


    public AirVehicleState clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            AirVehicleState newObj = new AirVehicleState();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  vehicle true airspeed (Units: meter/sec)*/
    public float getAirspeed() { return Airspeed; }

    /**  vehicle true airspeed (Units: meter/sec)*/
    public AirVehicleState setAirspeed( float val ) {
        Airspeed = val;
        return this;
    }

    /**  Vertical speed (positive upwards) of the vehicle in the inertial frame (rate of change of altitude) (Units: meter/sec)*/
    public float getVerticalSpeed() { return VerticalSpeed; }

    /**  Vertical speed (positive upwards) of the vehicle in the inertial frame (rate of change of altitude) (Units: meter/sec)*/
    public AirVehicleState setVerticalSpeed( float val ) {
        VerticalSpeed = val;
        return this;
    }

    /**  Wind speed as detected or computed by the vehicle (Units: meter/sec)*/
    public float getWindSpeed() { return WindSpeed; }

    /**  Wind speed as detected or computed by the vehicle (Units: meter/sec)*/
    public AirVehicleState setWindSpeed( float val ) {
        WindSpeed = val;
        return this;
    }

    /**  Wind source direction (true) as detected or computed by the vehicle(Units: degree)*/
    public float getWindDirection() { return WindDirection; }

    /**  Wind source direction (true) as detected or computed by the vehicle(Units: degree)*/
    public AirVehicleState setWindDirection( float val ) {
        WindDirection = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 16; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        Airspeed = LMCPUtil.getReal32(in);

        VerticalSpeed = LMCPUtil.getReal32(in);

        WindSpeed = LMCPUtil.getReal32(in);

        WindDirection = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putReal32(out, Airspeed);
        LMCPUtil.putReal32(out, VerticalSpeed);
        LMCPUtil.putReal32(out, WindSpeed);
        LMCPUtil.putReal32(out, WindDirection);

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
        buf.append( ws + "<AirVehicleState Series=\"CMASI\">\n");
        buf.append( ws + "  <Airspeed>" + String.valueOf(Airspeed) + "</Airspeed>\n");
        buf.append( ws + "  <VerticalSpeed>" + String.valueOf(VerticalSpeed) + "</VerticalSpeed>\n");
        buf.append( ws + "  <WindSpeed>" + String.valueOf(WindSpeed) + "</WindSpeed>\n");
        buf.append( ws + "  <WindDirection>" + String.valueOf(WindDirection) + "</WindDirection>\n");
        buf.append( ws + "  <ID>" + String.valueOf(ID) + "</ID>\n");
        buf.append( ws + "  <u>" + String.valueOf(u) + "</u>\n");
        buf.append( ws + "  <v>" + String.valueOf(v) + "</v>\n");
        buf.append( ws + "  <w>" + String.valueOf(w) + "</w>\n");
        buf.append( ws + "  <udot>" + String.valueOf(udot) + "</udot>\n");
        buf.append( ws + "  <vdot>" + String.valueOf(vdot) + "</vdot>\n");
        buf.append( ws + "  <wdot>" + String.valueOf(wdot) + "</wdot>\n");
        buf.append( ws + "  <Heading>" + String.valueOf(Heading) + "</Heading>\n");
        buf.append( ws + "  <Pitch>" + String.valueOf(Pitch) + "</Pitch>\n");
        buf.append( ws + "  <Roll>" + String.valueOf(Roll) + "</Roll>\n");
        buf.append( ws + "  <p>" + String.valueOf(p) + "</p>\n");
        buf.append( ws + "  <q>" + String.valueOf(q) + "</q>\n");
        buf.append( ws + "  <r>" + String.valueOf(r) + "</r>\n");
        buf.append( ws + "  <Course>" + String.valueOf(Course) + "</Course>\n");
        buf.append( ws + "  <Groundspeed>" + String.valueOf(Groundspeed) + "</Groundspeed>\n");
        if (Location!= null){
           buf.append( ws + "  <Location>\n");
           buf.append( ( Location.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </Location>\n");
        }
        buf.append( ws + "  <EnergyAvailable>" + String.valueOf(EnergyAvailable) + "</EnergyAvailable>\n");
        buf.append( ws + "  <ActualEnergyRate>" + String.valueOf(ActualEnergyRate) + "</ActualEnergyRate>\n");
        buf.append( ws + "  <PayloadStateList>\n");
        for (int i=0; i<PayloadStateList.size(); i++) {
            buf.append( PayloadStateList.get(i) == null ? ( ws + "    <null/>\n") : (PayloadStateList.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </PayloadStateList>\n");
        buf.append( ws + "  <CurrentWaypoint>" + String.valueOf(CurrentWaypoint) + "</CurrentWaypoint>\n");
        buf.append( ws + "  <CurrentCommand>" + String.valueOf(CurrentCommand) + "</CurrentCommand>\n");
        buf.append( ws + "  <Mode>" + String.valueOf(Mode) + "</Mode>\n");
        buf.append( ws + "  <AssociatedTasks>\n");
        for (int i=0; i<AssociatedTasks.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(AssociatedTasks.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </AssociatedTasks>\n");
        buf.append( ws + "  <Time>" + String.valueOf(Time) + "</Time>\n");
        buf.append( ws + "  <Info>\n");
        for (int i=0; i<Info.size(); i++) {
            buf.append( Info.get(i) == null ? ( ws + "    <null/>\n") : (Info.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Info>\n");
        buf.append( ws + "</AirVehicleState>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        AirVehicleState o = (AirVehicleState) anotherObj;
        if (Airspeed != o.Airspeed) return false;
        if (VerticalSpeed != o.VerticalSpeed) return false;
        if (WindSpeed != o.WindSpeed) return false;
        if (WindDirection != o.WindDirection) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)Airspeed;
        hash += 31 * (int)VerticalSpeed;
        hash += 31 * (int)WindSpeed;
        hash += 31 * (int)WindDirection;

        return hash + super.hashCode();
    }
    
}
