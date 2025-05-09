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
Provides basic state information regarding entity position, orientation, and velocity. 
*/
public class SurfaceVehicleState extends afrl.cmasi.EntityState {
    
    public static final int LMCP_TYPE = 4;

    public static final String SERIES_NAME = "VEHICLES";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6216454340153722195L;
    public static final int SERIES_VERSION = 1;


    private static final String TYPE_NAME = "SurfaceVehicleState";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.vehicles.SurfaceVehicleState";

    /**  Current bank angle of the surface vehicle (Units: degree)*/
    @LmcpType("real32")
    protected float BankAngle = (float)0;
    /**  Forward speed of the vehicle relative to the mass of water (Units: meter/sec)*/
    @LmcpType("real32")
    protected float Speed = (float)0;

    
    public SurfaceVehicleState() {
    }

    public SurfaceVehicleState(long ID, float u, float v, float w, float udot, float vdot, float wdot, float Heading, float Pitch, float Roll, float p, float q, float r, float Course, float Groundspeed, afrl.cmasi.Location3D Location, float EnergyAvailable, float ActualEnergyRate, long CurrentWaypoint, long CurrentCommand, afrl.cmasi.NavigationMode Mode, long Time, float BankAngle, float Speed){
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
        this.BankAngle = BankAngle;
        this.Speed = Speed;
    }


    public SurfaceVehicleState clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            SurfaceVehicleState newObj = new SurfaceVehicleState();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Current bank angle of the surface vehicle (Units: degree)*/
    public float getBankAngle() { return BankAngle; }

    /**  Current bank angle of the surface vehicle (Units: degree)*/
    public SurfaceVehicleState setBankAngle( float val ) {
        BankAngle = val;
        return this;
    }

    /**  Forward speed of the vehicle relative to the mass of water (Units: meter/sec)*/
    public float getSpeed() { return Speed; }

    /**  Forward speed of the vehicle relative to the mass of water (Units: meter/sec)*/
    public SurfaceVehicleState setSpeed( float val ) {
        Speed = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        BankAngle = LMCPUtil.getReal32(in);

        Speed = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putReal32(out, BankAngle);
        LMCPUtil.putReal32(out, Speed);

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
        buf.append( ws + "<SurfaceVehicleState Series=\"VEHICLES\">\n");
        buf.append( ws + "  <BankAngle>" + String.valueOf(BankAngle) + "</BankAngle>\n");
        buf.append( ws + "  <Speed>" + String.valueOf(Speed) + "</Speed>\n");
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
        buf.append( ws + "</SurfaceVehicleState>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        SurfaceVehicleState o = (SurfaceVehicleState) anotherObj;
        if (BankAngle != o.BankAngle) return false;
        if (Speed != o.Speed) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)BankAngle;
        hash += 31 * (int)Speed;

        return hash + super.hashCode();
    }
    
}
