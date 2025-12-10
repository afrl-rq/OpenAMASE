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
 Denotes a list of entities that should be removed permanently from the scenario. 
*/
public class RemoveEntities extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 53;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "RemoveEntities";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.RemoveEntities";

    /**  List of entities to remove (Units: None)*/
    @LmcpType("int64")
    protected java.util.ArrayList<Long> EntityList = new java.util.ArrayList<Long>();

    
    public RemoveEntities() {
    }



    public RemoveEntities clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            RemoveEntities newObj = new RemoveEntities();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    public java.util.ArrayList<Long> getEntityList() {
        return EntityList;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 0; // accounts for primitive types
        
        size += 2 + 8 * EntityList.size();

        return size;
    }

    public void unpack(InputStream in) throws IOException {
        EntityList.clear();
        int EntityList_len = LMCPUtil.getUint16(in);
        for(int i=0; i<EntityList_len; i++){
            EntityList.add(LMCPUtil.getInt64(in));
        }

    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putUint16(out, EntityList.size());
        for(int i=0; i<EntityList.size(); i++){
            LMCPUtil.putInt64(out, EntityList.get(i));
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
        buf.append( ws + "<RemoveEntities Series=\"CMASI\">\n");
        buf.append( ws + "  <EntityList>\n");
        for (int i=0; i<EntityList.size(); i++) {
        buf.append( ws + "  <int64>" + String.valueOf(EntityList.get(i)) + "</int64>\n");
        }
        buf.append( ws + "  </EntityList>\n");
        buf.append( ws + "</RemoveEntities>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        RemoveEntities o = (RemoveEntities) anotherObj;
         if (!EntityList.equals( o.EntityList)) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;

        return hash + super.hashCode();
    }
    
}
