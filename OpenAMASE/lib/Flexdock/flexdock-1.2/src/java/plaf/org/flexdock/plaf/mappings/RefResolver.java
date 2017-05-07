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
package org.flexdock.plaf.mappings;

/**
 * @author Christopher Butler
 */
public class RefResolver {
    private String defaultRef;

    public void setDefaultRef(String ref) {
        defaultRef = ref;
    }

    public String getDefaultRef() {
        return defaultRef;
    }

    public String getRef(String plaf) {
        return getDefaultRef();
    }
}
