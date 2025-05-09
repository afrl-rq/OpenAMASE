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

*/
public class PayloadDropTask extends afrl.cmasi.Task {
    
    public static final int LMCP_TYPE = 35;

    public static final String SERIES_NAME = "IMPACT";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 5281966179208134656L;
    public static final int SERIES_VERSION = 14;


    private static final String TYPE_NAME = "PayloadDropTask";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.impact.PayloadDropTask";

    /** (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D DropLocation = new afrl.cmasi.Location3D();
    /** (Units: None)*/
    @LmcpType("Location3D")
    protected afrl.cmasi.Location3D BDALocation = new afrl.cmasi.Location3D();

    
    public PayloadDropTask() {
    }

    public PayloadDropTask(long TaskID, String Label, float RevisitRate, short Priority, boolean Required, afrl.cmasi.Location3D DropLocation, afrl.cmasi.Location3D BDALocation){
        this.TaskID = TaskID;
        this.Label = Label;
        this.RevisitRate = RevisitRate;
        this.Priority = Priority;
        this.Required = Required;
        this.DropLocation = DropLocation;
        this.BDALocation = BDALocation;
    }


    public PayloadDropTask clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            PayloadDropTask newObj = new PayloadDropTask();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /** (Units: None)*/
    public afrl.cmasi.Location3D getDropLocation() { return DropLocation; }

    /** (Units: None)*/
    public PayloadDropTask setDropLocation( afrl.cmasi.Location3D val ) {
        DropLocation = val;
        return this;
    }

    /** (Units: None)*/
    public afrl.cmasi.Location3D getBDALocation() { return BDALocation; }

    /** (Units: None)*/
    public PayloadDropTask setBDALocation( afrl.cmasi.Location3D val ) {
        BDALocation = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 0; // accounts for primitive types
        size += LMCPUtil.sizeOf(DropLocation);
        size += LMCPUtil.sizeOf(BDALocation);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
            DropLocation = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);
            BDALocation = (afrl.cmasi.Location3D) LMCPUtil.getObject(in);

    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putObject(out, DropLocation);
        LMCPUtil.putObject(out, BDALocation);

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
        buf.append( ws + "<PayloadDropTask Series=\"IMPACT\">\n");
        if (DropLocation!= null){
           buf.append( ws + "  <DropLocation>\n");
           buf.append( ( DropLocation.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </DropLocation>\n");
        }
        if (BDALocation!= null){
           buf.append( ws + "  <BDALocation>\n");
           buf.append( ( BDALocation.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </BDALocation>\n");
        }
        buf.append( ws + "  <TaskID>" + String.valueOf(TaskID) + "</TaskID>\n");
        buf.append( ws + "  <Label>" + String.valueOf(Label) + "</Label>\n");
        buf.append( ws + "  <EligibleEntities>\n");
        for (int i=0; i<EligibleEntities.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(EligibleEntities.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </EligibleEntities>\n");
        buf.append( ws + "  <RevisitRate>" + String.valueOf(RevisitRate) + "</RevisitRate>\n");
        buf.append( ws + "  <Parameters>\n");
        for (int i=0; i<Parameters.size(); i++) {
            buf.append( Parameters.get(i) == null ? ( ws + "    <null/>\n") : (Parameters.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </Parameters>\n");
        buf.append( ws + "  <Priority>" + String.valueOf(Priority) + "</Priority>\n");
        buf.append( ws + "  <Required>" + String.valueOf(Required) + "</Required>\n");
        buf.append( ws + "</PayloadDropTask>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        PayloadDropTask o = (PayloadDropTask) anotherObj;
        if (DropLocation == null && o.DropLocation != null) return false;
        if ( DropLocation!= null && !DropLocation.equals(o.DropLocation)) return false;
        if (BDALocation == null && o.BDALocation != null) return false;
        if ( BDALocation!= null && !BDALocation.equals(o.BDALocation)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
