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
 Describes a polygon defined by geographic locations. Altitude value of points is ignored. 
*/
public class Polygon extends afrl.cmasi.AbstractGeometry {
    
    public static final int LMCP_TYPE = 42;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "Polygon";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.Polygon";

    /**  Boundary points for the polygon (Units: None)*/
    @LmcpType("Location3D")
    protected java.util.ArrayList<afrl.cmasi.Location3D> BoundaryPoints = new java.util.ArrayList<afrl.cmasi.Location3D>();

    
    public Polygon() {
    }



    public Polygon clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            Polygon newObj = new Polygon();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    public java.util.ArrayList<afrl.cmasi.Location3D> getBoundaryPoints() {
        return BoundaryPoints;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 0; // accounts for primitive types
        size += 2;
        size += LMCPUtil.sizeOfList(BoundaryPoints);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        super.unpack(in);
        BoundaryPoints.clear();
        int BoundaryPoints_len = LMCPUtil.getUint16(in);
        for(int i=0; i<BoundaryPoints_len; i++){
        BoundaryPoints.add( (afrl.cmasi.Location3D) LMCPUtil.getObject(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        super.pack(out);
        LMCPUtil.putUint16(out, BoundaryPoints.size());
        for(int i=0; i<BoundaryPoints.size(); i++){
            LMCPUtil.putObject(out, BoundaryPoints.get(i));
        }

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
        buf.append( ws + "<Polygon Series=\"CMASI\">\n");
        buf.append( ws + "  <BoundaryPoints>\n");
        for (int i=0; i<BoundaryPoints.size(); i++) {
            buf.append( BoundaryPoints.get(i) == null ? ( ws + "    <null/>\n") : (BoundaryPoints.get(i).toXML(ws + "    ")) + "\n");
        }
        buf.append( ws + "  </BoundaryPoints>\n");
        buf.append( ws + "</Polygon>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        Polygon o = (Polygon) anotherObj;
         if (!BoundaryPoints.equals( o.BoundaryPoints)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
