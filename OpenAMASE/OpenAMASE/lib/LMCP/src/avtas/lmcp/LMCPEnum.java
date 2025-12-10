// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package avtas.lmcp;

/** Base type for each series enumeration (used for object identification) */
public interface LMCPEnum {

        public long getSeriesNameAsLong();

        public String getSeriesName();

        public int getSeriesVersion();

        public String getName(long type);

        public long getType(String name);

        public LMCPObject getInstance(long type);

        public java.util.Collection<String> getAllTypes();
}
