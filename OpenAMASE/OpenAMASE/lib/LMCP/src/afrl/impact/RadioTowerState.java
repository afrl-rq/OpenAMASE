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
 Indicates a radio tower state 
*/
public class RadioTowerState extends afrl.cmasi.EntityState {
    
    public static final int LMCP_TYPE = 5;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "RadioTowerState";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.RadioTowerState";

    /**  Whether the radio is enabled (Units: None)*/
    @LmcpType("bool")
    protected boolean Enabled = true;

    
    public RadioTowerState() {
    }

    public RadioTowerState(long ID, float u, float v, float w, float udot, float vdot, float wdot, float Heading, float Pitch, float Roll, float p, float q, float r, float Course, float Groundspeed, afrl.cmasi.Location3D Location, float EnergyAvailable, float ActualEnergyRate, long CurrentWaypoint, long CurrentCommand, afrl.cmasi.NavigationMode Mode, long Time, boolean Enabled){
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
        this.Enabled = Enabled;
    }


    public RadioTowerState clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            RadioTowerState newObj = new RadioTowerState();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  Whether the radio is enabled (Units: None)*/
    public boolean getEnabled() { return Enabled; }

    /**  Whether the radio is enabled (Units: None)*/
    public RadioTowerState setEnabled( boolean val ) {
        Enabled = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 1; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        Enabled = LMCPUtil.getBool(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
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
        buf.append( ws + "<RadioTowerState Series=\"IMPACT\">\n");
        buf.append( ws + "  <Enabled>" + String.valueOf(Enabled) + "</Enabled>\n");
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
        buf.append( ws + "</RadioTowerState>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        RadioTowerState o = (RadioTowerState) anotherObj;
        if (Enabled != o.Enabled) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
