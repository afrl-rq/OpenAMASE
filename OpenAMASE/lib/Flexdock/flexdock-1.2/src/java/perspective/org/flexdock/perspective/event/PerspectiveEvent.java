// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package org.flexdock.perspective.event;

import org.flexdock.event.Event;
import org.flexdock.perspective.Perspective;

/**
 *
 * @author Mateusz Szczap
 */
public class PerspectiveEvent extends Event {
    public final static int CHANGED = 1;
    public final static int RESET = 2;

    private Perspective oldPerspective;

    public PerspectiveEvent(Perspective perspective, Perspective oldPerspective, int eventType) {
        super(perspective, eventType);
        this.oldPerspective = oldPerspective;
    }

    public Perspective getPerspective() {
        return (Perspective)getSource();
    }

    public Perspective getOldPerspective() {
        return oldPerspective;
    }
}
