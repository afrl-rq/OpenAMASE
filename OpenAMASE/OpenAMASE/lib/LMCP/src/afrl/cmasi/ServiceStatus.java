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
            Provides information regarding automation service compuation percent complete.  The automation service should send this when the computation of a            mission plan may take more than a few seconds, or when an error occurs (such as the inability to create one or more            mission commands given the task set)        
*/
public class ServiceStatus extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 45;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "ServiceStatus";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.ServiceStatus";

    /**  The estimated percentage completed for an automation process. (Units: None)*/
    @LmcpType("real32")
    protected float PercentComplete = (float)0;
    /**  Status from the automation service to the simulation regarding errors or conditions. (Units: None)*/
    @LmcpType("KeyValuePair")
    protected java.util.ArrayList<afrl.cmasi.KeyValuePair> Info = new java.util.ArrayList<afrl.cmasi.KeyValuePair>();
    /**  Describes the type of message conveyed. 
    *<br/> Information. Used to note normal progress updates 
    *<br/> Warning. Notes non-fatal problem(s) in data or processing 
    *<br/> Error. Notes fatal problem(s) 
    *<br/> (Units: None)*/
    @LmcpType("ServiceStatusType")
    protected afrl.cmasi.ServiceStatusType StatusType = afrl.cmasi.ServiceStatusType.Information;

    
    public ServiceStatus() {
    }

    public ServiceStatus(float PercentComplete, afrl.cmasi.ServiceStatusType StatusType){
        this.PercentComplete = PercentComplete;
        this.StatusType = StatusType;
    }


    public ServiceStatus clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            ServiceStatus newObj = new ServiceStatus();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  The estimated percentage completed for an automation process. (Units: None)*/
    public float getPercentComplete() { return PercentComplete; }

    /**  The estimated percentage completed for an automation process. (Units: None)*/
    public ServiceStatus setPercentComplete( float val ) {
        PercentComplete = val;
        return this;
    }

    public java.util.ArrayList<afrl.cmasi.KeyValuePair> getInfo() {
        return Info;
    }

    /**  Describes the type of message conveyed. 
    *<br/> Information. Used to note normal progress updates 
    *<br/> Warning. Notes non-fatal problem(s) in data or processing 
    *<br/> Error. Notes fatal problem(s) 
    *<br/> (Units: None)*/
    public afrl.cmasi.ServiceStatusType getStatusType() { return StatusType; }

    /**  Describes the type of message conveyed. 
    *<br/> Information. Used to note normal progress updates 
    *<br/> Warning. Notes non-fatal problem(s) in data or processing 
    *<br/> Error. Notes fatal problem(s) 
    *<br/> (Units: None)*/
    public ServiceStatus setStatusType( afrl.cmasi.ServiceStatusType val ) {
        StatusType = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 8; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(Info);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        PercentComplete = LMCPUtil.getReal32(in);

        Info.clear();
        int Info_len = LMCPUtil.getUint16(in);
        for(int i=0; i<Info_len; i++){
        Info.add( (afrl.cmasi.KeyValuePair) LMCPUtil.getObject(in));
        }
        StatusType = afrl.cmasi.ServiceStatusType.unpack( in );


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putReal32(out, PercentComplete);
        LMCPUtil.putUint16(out, Info.size());
        for(int i=0; i<Info.size(); i++){
            LMCPUtil.putObject(out, Info.get(i));
        }
        StatusType.pack(out);

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
        buf.append( ws + "<ServiceStatus Series=\"CMASI\">\n");
        buf.append( ws + "  <PercentComplete>" + String.valueOf(PercentComplete) + "</PercentComplete>\n");
        buf.append( ws + "  <Info>\n");
        for (int i=0; i<Info.size(); i++) {
            buf.append( Info.get(i) == null ? ( ws + "    <null/>\n") : (Info.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Info>\n");
        buf.append( ws + "  <StatusType>" + String.valueOf(StatusType) + "</StatusType>\n");
        buf.append( ws + "</ServiceStatus>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        ServiceStatus o = (ServiceStatus) anotherObj;
        if (PercentComplete != o.PercentComplete) return false;
         if (!Info.equals( o.Info)) return false;
        if (StatusType != o.StatusType) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)PercentComplete;

        return hash + super.hashCode();
    }
    
}
