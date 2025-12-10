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
public class GroundVehicleState extends afrl.cmasi.EntityState {
    
    public static final int LMCP_TYPE = 2;

    public static final String SERIES_NAME = "VEHICLES";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 6216454340153722195L;
    public static final int SERIES_VERSION = 1;


    private static final String TYPE_NAME = "GroundVehicleState";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.vehicles.GroundVehicleState";


    
    public GroundVehicleState() {
    }



    public GroundVehicleState clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            GroundVehicleState newObj = new GroundVehicleState();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     


    public int calcSize() {
        int size = super.calcSize();  
        size += 0; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);

    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);

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
        buf.append( ws + "<GroundVehicleState Series=\"VEHICLES\">\n");
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
        buf.append( ws + "</GroundVehicleState>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        GroundVehicleState o = (GroundVehicleState) anotherObj;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
