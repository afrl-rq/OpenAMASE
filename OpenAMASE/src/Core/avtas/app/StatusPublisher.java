// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controls status messages throughout the application. {@link StatusListener}s
 * register with this class to receive updates. Status updates are anything that
 * might b of interest to other components in the application, such as "loading
 * plugin xxx", or reporting errors. Use the {@link #getDefault() }
 * method to get a reference to the application-wide publisher.
 *
 * @author AFRL/RQQD
 */
public class StatusPublisher {

    protected final List<StatusListener> listenerList = Collections.synchronizedList(new ArrayList<StatusListener>());
    boolean reportToStandardOut = false;
    protected static StatusPublisher defaultPublisher = new StatusPublisher();

    public void setStatus(String status) {
        synchronized (listenerList) {
            for (int i = 0; i < listenerList.size(); i++) {
                listenerList.get(i).statusUpdate(status);
            }
        }
        if (reportToStandardOut) {
            System.out.println(status);
        }
    }

    public void addListener(StatusListener l) {
        synchronized (listenerList) {
            listenerList.add(l);
        }
    }

    public boolean removeListener(StatusListener l) {
        synchronized (listenerList) {
            return listenerList.remove(l);
        }
    }

    /**
     * Returns the default publisher. This is usually used to publish
     * application-wide updates
     */
    public static StatusPublisher getDefault() {
        return defaultPublisher;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */