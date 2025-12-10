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
            An action for controlling a camera that is mounted on a gimbal.        
*/
public class CameraAction extends afrl.cmasi.PayloadAction {
    
    public static final int LMCP_TYPE = 18;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "CameraAction";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.CameraAction";

    /**  The commanded horizontal (azimuth) field of view. (Units: degree)*/
    @LmcpType("real32")
    protected float HorizontalFieldOfView = (float)0;

    
    public CameraAction() {
    }

    public CameraAction(long PayloadID, float HorizontalFieldOfView){
        this.PayloadID = PayloadID;
        this.HorizontalFieldOfView = HorizontalFieldOfView;
    }


    public CameraAction clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            CameraAction newObj = new CameraAction();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  The commanded horizontal (azimuth) field of view. (Units: degree)*/
    public float getHorizontalFieldOfView() { return HorizontalFieldOfView; }

    /**  The commanded horizontal (azimuth) field of view. (Units: degree)*/
    public CameraAction setHorizontalFieldOfView( float val ) {
        HorizontalFieldOfView = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 4; // accounts for primitive types

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        HorizontalFieldOfView = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putReal32(out, HorizontalFieldOfView);

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
        buf.append( ws + "<CameraAction Series=\"CMASI\">\n");
        buf.append( ws + "  <HorizontalFieldOfView>" + String.valueOf(HorizontalFieldOfView) + "</HorizontalFieldOfView>\n");
        buf.append( ws + "  <PayloadID>" + String.valueOf(PayloadID) + "</PayloadID>\n");
        buf.append( ws + "  <AssociatedTaskList>\n");
        for (int i=0; i<AssociatedTaskList.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(AssociatedTaskList.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </AssociatedTaskList>\n");
        buf.append( ws + "</CameraAction>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        CameraAction o = (CameraAction) anotherObj;
        if (HorizontalFieldOfView != o.HorizontalFieldOfView) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)HorizontalFieldOfView;

        return hash + super.hashCode();
    }
    
}
