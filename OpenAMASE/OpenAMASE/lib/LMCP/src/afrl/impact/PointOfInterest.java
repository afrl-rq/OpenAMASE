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
 Point of interest 
*/
public class PointOfInterest extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 19;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "PointOfInterest";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.PointOfInterest";

    /**  ID for points of interest (Units: None)*/
    @LmcpType("int64")
    protected long PointID = 0L;
    /**  Point of interest location. A valid PointOfInterest must define Location (null not allowed). (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D Location = new afrl.cmasi.Location3D();
    /**  Action that updated this point of interest (e.g. created, destroyed, modified) (Units: None)*/
    @LmcpType("AreaActionOptions")
    protected afrl.impact.AreaActionOptions PointAction = afrl.impact.AreaActionOptions.Created;
    /**  Human readable label for point of interest (Units: None)*/
    @LmcpType("string")
    protected String PointLabel = "";
    /**  Background Behavior point is true if point is for background behavior (Units: None)*/
    @LmcpType("bool")
    protected boolean BackgroundBehaviorPoint = false;

    
    public PointOfInterest() {
    }

    public PointOfInterest(long PointID, afrl.cmasi.Location3D Location, afrl.impact.AreaActionOptions PointAction, String PointLabel, boolean BackgroundBehaviorPoint){
        this.PointID = PointID;
        this.Location = Location;
        this.PointAction = PointAction;
        this.PointLabel = PointLabel;
        this.BackgroundBehaviorPoint = BackgroundBehaviorPoint;
    }


    public PointOfInterest clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            PointOfInterest newObj = new PointOfInterest();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  ID for points of interest (Units: None)*/
    public long getPointID() { return PointID; }

    /**  ID for points of interest (Units: None)*/
    public PointOfInterest setPointID( long val ) {
        PointID = val;
        return this;
    }

    /**  Point of interest location. A valid PointOfInterest must define Location (null not allowed). (Units: None)*/
    public afrl.cmasi.Location3D getLocation() { return Location; }

    /**  Point of interest location. A valid PointOfInterest must define Location (null not allowed). (Units: None)*/
    public PointOfInterest setLocation( afrl.cmasi.Location3D val ) {
        Location = val;
        return this;
    }

    /**  Action that updated this point of interest (e.g. created, destroyed, modified) (Units: None)*/
    public afrl.impact.AreaActionOptions getPointAction() { return PointAction; }

    /**  Action that updated this point of interest (e.g. created, destroyed, modified) (Units: None)*/
    public PointOfInterest setPointAction( afrl.impact.AreaActionOptions val ) {
        PointAction = val;
        return this;
    }

    /**  Human readable label for point of interest (Units: None)*/
    public String getPointLabel() { return PointLabel; }

    /**  Human readable label for point of interest (Units: None)*/
    public PointOfInterest setPointLabel( String val ) {
        PointLabel = val;
        return this;
    }

    /**  Background Behavior point is true if point is for background behavior (Units: None)*/
    public boolean getBackgroundBehaviorPoint() { return BackgroundBehaviorPoint; }

    /**  Background Behavior point is true if point is for background behavior (Units: None)*/
    public PointOfInterest setBackgroundBehaviorPoint( boolean val ) {
        BackgroundBehaviorPoint = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 13; // accounts for primitive types
        size += LMCPUtil.sizeOf(Location);
        size += LMCPUtil.sizeOfString(PointLabel);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        PointID = LMCPUtil.getInt64(in);

            Location = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
        PointAction = afrl.impact.AreaActionOptions.unpack( in );

        PointLabel = LMCPUtil.getString(in);

        BackgroundBehaviorPoint = LMCPUtil.getBool(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putInt64(out, PointID);
        LMCPUtil.putObject(out, Location);
        PointAction.pack(out);
        LMCPUtil.putString(out, PointLabel);
        LMCPUtil.putBool(out, BackgroundBehaviorPoint);

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
        buf.append( ws + "<PointOfInterest Series=\"IMPACT\">\n");
        buf.append( ws + "  <PointID>" + String.valueOf(PointID) + "</PointID>\n");
        if (Location!= null){
           buf.append( ws + "  <Location>\n");
           buf.append( ( Location.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </Location>\n");
        }
        buf.append( ws + "  <PointAction>" + String.valueOf(PointAction) + "</PointAction>\n");
        buf.append( ws + "  <PointLabel>" + String.valueOf(PointLabel) + "</PointLabel>\n");
        buf.append( ws + "  <BackgroundBehaviorPoint>" + String.valueOf(BackgroundBehaviorPoint) + "</BackgroundBehaviorPoint>\n");
        buf.append( ws + "</PointOfInterest>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        PointOfInterest o = (PointOfInterest) anotherObj;
        if (PointID != o.PointID) return false;
        if (Location == null && o.Location != null) return false;
        if ( Location!= null && !Location.equals(o.Location)) return false;
        if (PointAction != o.PointAction) return false;
        if (PointLabel == null && o.PointLabel != null) return false;
        if ( PointLabel!= null && !PointLabel.equals(o.PointLabel)) return false;
        if (BackgroundBehaviorPoint != o.BackgroundBehaviorPoint) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
