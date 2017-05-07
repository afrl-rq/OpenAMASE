// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Feb 27, 2005
 */
package org.flexdock.plaf.resources;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author Christopher Butler
 */
public class ResourceHandler {
    public Object getResource(String stringValue) {
        return stringValue;
    }

    protected String[] getArgs(String data) {
        if(data==null)
            return new String[0];

        if(!data.endsWith(","))
            data += ",";

        ArrayList args = new ArrayList(3);
        for(StringTokenizer st = new StringTokenizer(data, ","); st.hasMoreTokens();) {
            args.add(st.nextToken().trim());
        }
        return (String[])args.toArray(new String[0]);
    }
}
