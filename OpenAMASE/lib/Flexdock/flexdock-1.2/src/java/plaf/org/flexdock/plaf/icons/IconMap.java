// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Mar 1, 2005
 */
package org.flexdock.plaf.icons;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Christopher Butler
 *
 */
public class IconMap extends HashMap {
    public Object put(Object key, Object value) {
        // do nothing
        return null;
    }

    public void putAll(Map m) {
        // do nothing
    }

    public void addAll(IconMap map) {
        if(map!=null)
            super.putAll(map);
    }

    public IconResource getIcons(String key) {
        return key==null? null: (IconResource)get(key);
    }

    public void addIcons(String key, IconResource icons) {
        if(key!=null && icons!=null)
            super.put(key, icons);
    }
}
