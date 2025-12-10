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


import avtas.lmcp.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** */
public enum CommandStatusType {

    /**  Pending authorization prior to execution  */
    Pending(0),
    /**  Approved for execution by the aircraft  */
    Approved(1),
    /**  Currently executing by an aircraft  */
    InProcess(2),
    /**  Completed execution by an aircraft  */
    Executed(3),
    /**  Cancelled by an operator, aircraft, or other entity  */
    Cancelled(4);


    private final int val;

    /** creates a new enum of the specified value */
    CommandStatusType(int val) {
        this.val = val;
    }

    /** returns the set value for this enum */
    public int getValue() {
        return val;
    }

    /** packs this enum into the LMCP buffer */
    public void pack(OutputStream out) throws IOException { LMCPUtil.putInt32(out, getValue()); }

    /** creates an enum for the value in the LMCP buffer */
    public static CommandStatusType unpack(InputStream in) throws IOException{
        return getEnum( LMCPUtil.getInt32(in) );
    }

    /** returns a new instance of this enum that matches the passed value (null if value is not known) */
    public static CommandStatusType getEnum(int val) {
        switch(val) {
            case 0 : return Pending;
            case 1 : return Approved;
            case 2 : return InProcess;
            case 3 : return Executed;
            case 4 : return Cancelled;
            default: return Pending;

        }
    }
}
