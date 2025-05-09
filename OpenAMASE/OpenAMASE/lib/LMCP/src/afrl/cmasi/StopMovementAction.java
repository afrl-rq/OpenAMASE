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
 Tells the Entity to stop at the given location.  If the location is null, then the         entity will stop immediately. 
*/
public class StopMovementAction extends afrl.cmasi.VehicleAction {
    
    public static final int LMCP_TYPE = 58;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "StopMovementAction";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.StopMovementAction";

    /**  A location at which the entity should stop. If this is null, the entity will stop immediately. (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D Location = null;

    
    public StopMovementAction() {
    }

    public StopMovementAction(afrl.cmasi.Location3D Location){
        this.Location = Location;
    }


    public StopMovementAction clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            StopMovementAction newObj = new StopMovementAction();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  A location at which the entity should stop. If this is null, the entity will stop immediately. (Units: None)*/
    public afrl.cmasi.Location3D getLocation() { return Location; }

    /**  A location at which the entity should stop. If this is null, the entity will stop immediately. (Units: None)*/
    public StopMovementAction setLocation( afrl.cmasi.Location3D val ) {
        Location = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 0; // accounts for primitive types
        size += LMCPUtil.sizeOf(Location);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
            Location = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);

    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
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
        buf.append( ws + "<StopMovementAction Series=\"CMASI\">\n");
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
        buf.append( ws + "</StopMovementAction>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        StopMovementAction o = (StopMovementAction) anotherObj;
        if (Location == null && o.Location != null) return false;
        if ( Location!= null && !Location.equals(o.Location)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
