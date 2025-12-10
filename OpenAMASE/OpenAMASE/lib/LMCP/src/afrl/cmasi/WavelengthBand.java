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

/**  Describes wavelength bands */
public enum WavelengthBand {

    /**  used in cases when wavelength is not applicable  */
    AllAny(0),
    /**  Electro-optical.  Visible spectrum  */
    EO(1),
    /**  Long-wave Infrared  */
    LWIR(2),
    /**  Short-wave infrared  */
    SWIR(3),
    /**  mid-wave infrared  */
    MWIR(4),
    /**  Other or undefined wavelength band  */
    Other(5);


    private final int val;

    /** creates a new enum of the specified value */
    WavelengthBand(int val) {
        this.val = val;
    }

    /** returns the set value for this enum */
    public int getValue() {
        return val;
    }

    /** packs this enum into the LMCP buffer */
    public void pack(OutputStream out) throws IOException { LMCPUtil.putInt32(out, getValue()); }

    /** creates an enum for the value in the LMCP buffer */
    public static WavelengthBand unpack(InputStream in) throws IOException{
        return getEnum( LMCPUtil.getInt32(in) );
    }

    /** returns a new instance of this enum that matches the passed value (null if value is not known) */
    public static WavelengthBand getEnum(int val) {
        switch(val) {
            case 0 : return AllAny;
            case 1 : return EO;
            case 2 : return LWIR;
            case 3 : return SWIR;
            case 4 : return MWIR;
            case 5 : return Other;
            default: return AllAny;

        }
    }
}
