// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Aug 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.flexdock.docking.props;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author Christopher Butler
 */
public abstract class PropertyChangeListenerFactory {
    private static final Vector FACTORIES = new Vector();

    public static void addFactory(PropertyChangeListenerFactory factory) {
        if(factory!=null)
            FACTORIES.add(factory);
    }

    public static void removeFactory(PropertyChangeListenerFactory factory) {
        if(factory!=null)
            FACTORIES.remove(factory);
    }

    public static PropertyChangeListener[] getListeners() {
        ArrayList list = new ArrayList(FACTORIES.size());
        for(Iterator it=FACTORIES.iterator(); it.hasNext();) {
            PropertyChangeListenerFactory factory = (PropertyChangeListenerFactory)it.next();
            PropertyChangeListener listener = factory.getListener();
            if(listener!=null)
                list.add(listener);
        }
        return (PropertyChangeListener[])list.toArray(new PropertyChangeListener[list.size()]);
    }

    public abstract PropertyChangeListener getListener();
}
