// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on May 27, 2005
 */
package org.flexdock.docking.state;

import java.io.IOException;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingPort;

/**
 * @author Christopher Butler
 */
public interface LayoutManager {

    DockingState getDockingState(String dockableId);

    DockingState getDockingState(Dockable dockable);

    FloatManager getFloatManager();

    LayoutNode createLayout(DockingPort port);

    boolean display(Dockable dockable);

    boolean store() throws IOException, PersistenceException;

    boolean store(String persistenceKey) throws IOException, PersistenceException;

    boolean load() throws IOException, PersistenceException;

    boolean load(String persistenceKey) throws IOException, PersistenceException;

    boolean restore(boolean loadFromStorage) throws IOException, PersistenceException;

    String getDefaultPersistenceKey();

    void setDefaultPersistenceKey(String key);

}
