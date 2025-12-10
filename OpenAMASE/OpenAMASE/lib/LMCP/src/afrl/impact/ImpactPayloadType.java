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


import avtas.lmcp.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**  Impact specific payload types */
public enum ImpactPayloadType {

    /**  Unknown  */
    Unknown(0),
    /**  EO  */
    EO(1),
    /**  Forward Looking Infra Red  */
    FLIR(2),
    /**  Mid Wave Infra Red  */
    MWIR(3),
    /**  LFIR  */
    LFIR(4),
    /**  Auto-tracking payload  */
    Track(5),
    /**  Placement of physical tag for tracking  */
    Tag(6),
    /**  Megaphone  */
    Megaphone(7),
    /**  Siren  */
    Siren(8),
    /**  Search Light  */
    SearchLight(9),
    /**  Browning .50 caliber weapon  */
    FiftyCal(10),
    /**  M240B  */
    M240B(11),
    /**  Flashbang  */
    Flashbang(12),
    /**  Tear Gas  */
    TearGas(13),
    /**  Taser  */
    Taser(14),
    /**  Heat Beam  */
    HeatBeam(15),
    /**  Scan Eagle Guided Munition  */
    SEGM(16),
    /**  Comm Relay  */
    CommRelay(17),
    /**  Ground Moving Target Indicator  */
    GMTI(18),
    /**  Laser Designator  */
    LaserDesignator(19),
    /**  LWIR  */
    LWIR(20);


    private final int val;

    /** creates a new enum of the specified value */
    ImpactPayloadType(int val) {
        this.val = val;
    }

    /** returns the set value for this enum */
    public int getValue() {
        return val;
    }

    /** packs this enum into the LMCP buffer */
    public void pack(OutputStream out) throws IOException { LMCPUtil.putInt32(out, getValue()); }

    /** creates an enum for the value in the LMCP buffer */
    public static ImpactPayloadType unpack(InputStream in) throws IOException{
        return getEnum( LMCPUtil.getInt32(in) );
    }

    /** returns a new instance of this enum that matches the passed value (null if value is not known) */
    public static ImpactPayloadType getEnum(int val) {
        switch(val) {
            case 0 : return Unknown;
            case 1 : return EO;
            case 2 : return FLIR;
            case 3 : return MWIR;
            case 4 : return LFIR;
            case 5 : return Track;
            case 6 : return Tag;
            case 7 : return Megaphone;
            case 8 : return Siren;
            case 9 : return SearchLight;
            case 10 : return FiftyCal;
            case 11 : return M240B;
            case 12 : return Flashbang;
            case 13 : return TearGas;
            case 14 : return Taser;
            case 15 : return HeatBeam;
            case 16 : return SEGM;
            case 17 : return CommRelay;
            case 18 : return GMTI;
            case 19 : return LaserDesignator;
            case 20 : return LWIR;
            default: return Unknown;

        }
    }
}
