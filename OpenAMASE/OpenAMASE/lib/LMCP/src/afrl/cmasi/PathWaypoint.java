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
 A waypoint that can be used as part of commanding entities' movement 
*/
public class PathWaypoint extends afrl.cmasi.Waypoint {
    
    public static final int LMCP_TYPE = 57;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "PathWaypoint";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.PathWaypoint";

    /**  The amount of time that an entity should pause before moving to the next waypoint. (Units: milliseconds)*/
    @LmcpType("int64")
    protected long PauseTime = 0L;

    
    public PathWaypoint() {
    }

    public PathWaypoint(double Latitude, double Longitude, float Altitude, afrl.cmasi.AltitudeType AltitudeType, long Number, long NextWaypoint, float Speed, afrl.cmasi.SpeedType SpeedType, float ClimbRate, afrl.cmasi.TurnType TurnType, long ContingencyWaypointA, long ContingencyWaypointB, long PauseTime){
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Altitude = Altitude;
        this.AltitudeType = AltitudeType;
        this.Number = Number;
        this.NextWaypoint = NextWaypoint;
        this.Speed = Speed;
        this.SpeedType = SpeedType;
        this.ClimbRate = ClimbRate;
        this.TurnType = TurnType;
        this.ContingencyWaypointA = ContingencyWaypointA;
        this.ContingencyWaypointB = ContingencyWaypointB;
        this.PauseTime = PauseTime;
    }


    public PathWaypoint clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            PathWaypoint newObj = new PathWaypoint();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  The amount of time that an entity should pause before moving to the next waypoint. (Units: milliseconds)*/
    public long getPauseTime() { return PauseTime; }

    /**  The amount of time that an entity should pause before moving to the next waypoint. (Units: milliseconds)*/
    public PathWaypoint setPauseTime( long val ) {
        PauseTime = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        PauseTime = LMCPUtil.getInt64(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putInt64(out, PauseTime);

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
        buf.append( ws + "<PathWaypoint Series=\"CMASI\">\n");
        buf.append( ws + "  <PauseTime>" + String.valueOf(PauseTime) + "</PauseTime>\n");
        buf.append( ws + "  <Number>" + String.valueOf(Number) + "</Number>\n");
        buf.append( ws + "  <NextWaypoint>" + String.valueOf(NextWaypoint) + "</NextWaypoint>\n");
        buf.append( ws + "  <Speed>" + String.valueOf(Speed) + "</Speed>\n");
        buf.append( ws + "  <SpeedType>" + String.valueOf(SpeedType) + "</SpeedType>\n");
        buf.append( ws + "  <ClimbRate>" + String.valueOf(ClimbRate) + "</ClimbRate>\n");
        buf.append( ws + "  <TurnType>" + String.valueOf(TurnType) + "</TurnType>\n");
        buf.append( ws + "  <VehicleActionList>\n");
        for (int i=0; i<VehicleActionList.size(); i++) {
            buf.append( VehicleActionList.get(i) == null ? ( ws + "    <null/>\n") : (VehicleActionList.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </VehicleActionList>\n");
        buf.append( ws + "  <ContingencyWaypointA>" + String.valueOf(ContingencyWaypointA) + "</ContingencyWaypointA>\n");
        buf.append( ws + "  <ContingencyWaypointB>" + String.valueOf(ContingencyWaypointB) + "</ContingencyWaypointB>\n");
        buf.append( ws + "  <AssociatedTasks>\n");
        for (int i=0; i<AssociatedTasks.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(AssociatedTasks.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </AssociatedTasks>\n");
        buf.append( ws + "  <Latitude>" + String.valueOf(Latitude) + "</Latitude>\n");
        buf.append( ws + "  <Longitude>" + String.valueOf(Longitude) + "</Longitude>\n");
        buf.append( ws + "  <Altitude>" + String.valueOf(Altitude) + "</Altitude>\n");
        buf.append( ws + "  <AltitudeType>" + String.valueOf(AltitudeType) + "</AltitudeType>\n");
        buf.append( ws + "</PathWaypoint>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        PathWaypoint o = (PathWaypoint) anotherObj;
        if (PauseTime != o.PauseTime) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
