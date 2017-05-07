// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Copyright (c) 2005 FlexDock Development Team. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE.
 */
package org.flexdock.perspective;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Window;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.SwingUtilities;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.DockingPort;
import org.flexdock.docking.event.hierarchy.DockingPortTracker;
import org.flexdock.docking.state.DockingState;
import org.flexdock.docking.state.FloatManager;
import org.flexdock.docking.state.LayoutManager;
import org.flexdock.docking.state.LayoutNode;
import org.flexdock.docking.state.PersistenceException;
import org.flexdock.event.EventManager;
import org.flexdock.event.RegistrationEvent;
import org.flexdock.perspective.event.LayoutEventHandler;
import org.flexdock.perspective.event.PerspectiveEvent;
import org.flexdock.perspective.event.PerspectiveEventHandler;
import org.flexdock.perspective.event.PerspectiveListener;
import org.flexdock.perspective.event.RegistrationHandler;
import org.flexdock.perspective.persist.FilePersistenceHandler;
import org.flexdock.perspective.persist.PersistenceHandler;
import org.flexdock.perspective.persist.PerspectiveModel;
import org.flexdock.util.RootWindow;
import org.flexdock.util.Utilities;

/**
 * @author Mateusz Szczap
 */
public class PerspectiveManager implements LayoutManager {

    public static final String EMPTY_PERSPECTIVE = "PerspectiveManager.EMPTY_PERSPECTIVE";
    public static final String DEFAULT_PERSISTENCE_KEY_VALUE = "perspectiveFile.data";
    private static PerspectiveManager SINGLETON = new PerspectiveManager();
    private static DockingStateListener UPDATE_LISTENER = new DockingStateListener();

    private HashMap m_perspectives = new HashMap();
    private PerspectiveFactory perspectiveFactory;
    private String m_defaultPerspective;
    private String m_currentPerspective;
    private PersistenceHandler m_persistHandler;
    private boolean restoreFloatingOnLoad;
    private String m_defaultPersistenceKey;

    static {
        initialize();
    }

    private static void initialize() {
        // TODO: Add logic to add and remove event handlers based on whether
        // the perspective manager is currently installed.  Right now, we're
        // just referencing DockingManager.class to ensure the class is properly
        // initialized before we add our event handlers.  This should be
        // called indirectly form within DockingManager, and we should have
        // uninstall capability as well.
        Class c = DockingManager.class;

        EventManager.addHandler(new RegistrationHandler());
        EventManager.addHandler(PerspectiveEventHandler.getInstance());
        EventManager.addHandler(new LayoutEventHandler());

        EventManager.addListener(UPDATE_LISTENER);

        String pKey = System.getProperty(DockingConstants.DEFAULT_PERSISTENCE_KEY);
        setPersistenceHandler(FilePersistenceHandler.createDefault(DEFAULT_PERSISTENCE_KEY_VALUE));
        getInstance().setDefaultPersistenceKey(pKey);
    }

    public static PerspectiveManager getInstance() {
        return SINGLETON;
    }

    public static void setFactory(PerspectiveFactory factory) {
        getInstance().perspectiveFactory = factory;
    }

    public static void setPersistenceHandler(PersistenceHandler handler) {
        getInstance().m_persistHandler = handler;
    }

    public static PersistenceHandler getPersistenceHandler() {
        return getInstance().m_persistHandler;
    }


    private PerspectiveManager() {
        setDefaultPerspective(EMPTY_PERSPECTIVE);
        loadPerspective(m_defaultPerspective, (DockingPort)null);
    }

    public void add(Perspective perspective) {
        add(perspective, false);
    }

    public void add(Perspective perspective, boolean isDefault) {
        if (perspective == null) throw new NullPointerException("perspective cannot be null");

        m_perspectives.put(perspective.getPersistentId(), perspective);
        if(isDefault)
            setDefaultPerspective(perspective.getPersistentId());

        EventManager.dispatch(new RegistrationEvent(perspective, this, true));
    }

    public void remove(String perspectiveId) {
        if (perspectiveId == null) throw new NullPointerException("perspectiveId cannot be null");

        Perspective perspective = getPerspective(perspectiveId);
        if (perspective == null)
            return;

        m_perspectives.remove(perspectiveId);

        //set defaultPerspective
        if(m_defaultPerspective.equals(perspectiveId))
            setDefaultPerspective(EMPTY_PERSPECTIVE);

        EventManager.dispatch(new RegistrationEvent(perspective, this, false));
    }

    public Perspective getPerspective(String perspectiveId) {
        if (perspectiveId == null)
            return null;

        Perspective perspective = (Perspective) m_perspectives.get(perspectiveId);
        if(perspective==null) {
            perspective = createPerspective(perspectiveId);
            if(perspective!=null) {
                add(perspective);
            }
        }
        return perspective;
    }

    public Perspective createPerspective(String perspectiveId) {
        if(EMPTY_PERSPECTIVE.equals(perspectiveId))
            return new Perspective(EMPTY_PERSPECTIVE, EMPTY_PERSPECTIVE) {
            public void load(DockingPort port, boolean defaultSetting) {
                // noop
            }
        };

        Perspective p = null;

        if (perspectiveFactory != null) {
            p = perspectiveFactory.getPerspective(perspectiveId);

            //this code ensures that perspective factory create perspectives that return the correct id
            //otherwise a NPE appears extremely far away in the code during the first docking operation
            if (!p.getPersistentId().equals(perspectiveId)) {
                //TODO create a good exception for this
                throw new IllegalStateException("Factory created perspective does not match intended ID: " + perspectiveId);
            }
        }

        return p;
    }

    public Perspective[] getPerspectives() {
        synchronized(m_perspectives) {
            ArrayList list = new ArrayList(m_perspectives.values());
            return (Perspective[])list.toArray(new Perspective[0]);
        }

    }

    public void addListener(PerspectiveListener perspectiveListener) {
        EventManager.addListener(perspectiveListener);
    }

    public void removeListener(PerspectiveListener perspectiveListener) {
        EventManager.removeListener(perspectiveListener);
    }

    public PerspectiveListener[] getPerspectiveListeners() {
        return PerspectiveEventHandler.getInstance().getListeners();
    }

    public void setDefaultPerspective(String perspectiveId) {
        m_defaultPerspective = perspectiveId;
    }

    public void setCurrentPerspective(String perspectiveId) {
        setCurrentPerspective(perspectiveId, false);
    }

    public String getCurrentPerspectiveName() {
        return m_currentPerspective;
    }

    private void setCurrentPerspectiveName(String name) {
        m_currentPerspective = "".equals(name)? null: name;
    }

    public void setCurrentPerspective(String perspectiveId, boolean asDefault) {
        perspectiveId = perspectiveId==null? m_defaultPerspective: perspectiveId;
        setCurrentPerspectiveName(perspectiveId);
        if(asDefault)
            setDefaultPerspective(perspectiveId);
    }

    public Perspective getDefaultPerspective() {
        return getPerspective(m_defaultPerspective);
    }

    public Perspective getCurrentPerspective() {
        return getPerspective(getCurrentPerspectiveName());
    }


    public DockingState getDockingState(Dockable dockable) {
        return getCurrentPerspective().getDockingState(dockable);
    }

    public DockingState getDockingState(String dockable) {
        return getCurrentPerspective().getDockingState(dockable);
    }

    public DockingState getDockingState(Dockable dockable, boolean load) {
        return getCurrentPerspective().getDockingState(dockable, load);
    }

    public DockingState getDockingState(String dockable, boolean load) {
        return getCurrentPerspective().getDockingState(dockable, load);
    }


    public FloatManager getFloatManager() {
        return getCurrentPerspective().getLayout();
    }

    public void reset() {
        RootWindow[] windows = DockingManager.getDockingWindows();
        if(windows.length!=0)
            reset(windows[0].getRootContainer());
    }

    public void reset(Component window) {
        if(window==null) {
            reset();
        } else {
            DockingPort port = DockingManager.getRootDockingPort(window);
            reset(port);
        }
    }

    public void reset(DockingPort rootPort) {
        loadPerspectiveImpl(getCurrentPerspectiveName(), rootPort, true);
    }

    /**
     * PerspectiveManager#getMainApplicationWindow returns the first
     * window where #getOwner == null. This is especially a problem for apps with
     * multiple frames. To display a perspective for a specified window
     * it is highly recommended to use #reload(Window w) instead of #reload()
     * which is the same as DockingManager#restoreLayout().
     * You can use #restoreLayout when the application does not need multiple
     * independent docking windows.
     */
    public void reload(Window w) {
        reload(w, true);
    }

    // use to load parentless frames
    public void reload(Window w, boolean reset) {
        String current = getCurrentPerspectiveName();
        // if the current perspective is null, use the default value
        String key = current == null ? m_defaultPerspective : current;

        // null-out the current perspective name to force a reload
        // otherwise, the loadPerspective() call will short-circuit since
        // it'll detect that the requested perspective is already loaded.
        setCurrentPerspectiveName(null);

        DockingPort port = DockingManager.getRootDockingPort(w);
        Perspective[] perspectives = getPerspectives();
        for (int i = 0; i < perspectives.length; i++) {
            String id = perspectives[i].getPersistentId();
            if (!id.equals(EMPTY_PERSPECTIVE)) {
                //TODO reset layout, maybe there is a better way
                if (reset)
                    perspectives[i].getLayout().setRestorationLayout(null);
                //p.unload();
                //p.reset(port);
            }
        }
        loadPerspectiveImpl(key, port, reset);

        // if perspective load fails, then rollback the perspective name
        // to its previous value (instead of null)
        if(!Utilities.isEqual(getCurrentPerspectiveName(), key))
            setCurrentPerspectiveName(current);
    }

    public void restore(Window w) throws IOException, PersistenceException {
        reload(w, true);
        load();
        reload(w, false);
        /*DockingPort port = DockingManager.getRootDockingPort(w);
        String current = getCurrentPerspectiveName();
        String key = current == null ? m_defaultPerspective : current;
        setCurrentPerspectiveName(null);
        loadPerspectiveImpl(key, port, false);
        if(!Utilities.isEqual(getCurrentPerspectiveName(), key))
          setCurrentPerspectiveName(current);*/
    }

    public void reload() {
        String current = getCurrentPerspectiveName();
        // if the current perspective is null, the use the default value
        String key = current==null? m_defaultPerspective: current;
        // null-out the current perspective name to force a reload.
        // otherwise, the loadPerspective() call will short-circuit since
        // it'll detect that the requested perspective is already loaded.
        setCurrentPerspectiveName(null);
        // load the perspective
        loadPerspective(key);
        // if the perspective load failed, then rollback the perspective name
        // to its previous value (instead of null)
        if(!Utilities.isEqual(getCurrentPerspectiveName(), key))
            setCurrentPerspectiveName(current);
    }

    public void loadPerspective() {
        loadPerspective(m_defaultPerspective);
    }

    public void loadPerspectiveAsDefault(String perspectiveId) {
        loadPerspectiveAsDefault(perspectiveId, false);
    }

    public void loadPerspectiveAsDefault(String perspectiveId, boolean reset) {
        if(perspectiveId!=null)
            setDefaultPerspective(perspectiveId);
        loadPerspective(perspectiveId, reset);
    }

    public void loadPerspective(String perspectiveId) {
        loadPerspective(perspectiveId, false);
    }

    public void loadPerspective(String perspectiveId, boolean reset) {
        RootWindow window = getMainApplicationWindow();
        if(window!=null) {
            loadPerspective(perspectiveId, window.getRootContainer(), reset);
            return;
        }

        DockingPort rootPort = findMainDockingPort();
        if(rootPort!=null)
            loadPerspective(perspectiveId, rootPort, reset);
    }

    public void loadPerspective(String perspectiveId, Component window) {
        loadPerspective(perspectiveId, window, false);
    }

    public void loadPerspective(String perspectiveId, Component window, boolean reset) {
        if(window==null) {
            loadPerspective(perspectiveId, reset);
            return;
        }

        DockingPort port = DockingManager.getRootDockingPort(window);
        loadPerspective(perspectiveId, port, reset);
    }

    public void loadPerspective(String perspectiveId, DockingPort rootPort) {
        loadPerspective(perspectiveId, rootPort, false);
    }

    public void loadPerspective(String perspectiveId, DockingPort rootPort, boolean reset) {
        if(perspectiveId==null || perspectiveId.equals(getCurrentPerspectiveName()))
            return;
        loadPerspectiveImpl(perspectiveId, rootPort, reset);
    }

    private void loadPerspectiveImpl(String perspectiveId, final DockingPort rootPort, boolean reset) {
        if(perspectiveId==null)
            return;

        Perspective current = getCurrentPerspective();
        final Perspective perspective = getPerspective(perspectiveId);

        // remember the current layout state so we'll be able to
        // restore when we switch back
        if(current!=null) {
            cacheLayoutState(current, rootPort);
            current.unload();
        }

        // if the new perspective isn't available, then we're done
        if(perspective==null)
            return;

        synchronized(this) {
            setCurrentPerspectiveName(perspectiveId);
            if(reset) {
                perspective.reset(rootPort);
                EventManager.dispatch(new PerspectiveEvent(perspective, current,
                                      PerspectiveEvent.RESET));
            } else {
                perspective.load(rootPort);
                EventManager.dispatch(new PerspectiveEvent(perspective, current,
                                      PerspectiveEvent.CHANGED));
            }
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                cacheLayoutState(perspective, rootPort);
            }
        });
    }

    private void cacheLayoutState(Perspective p, DockingPort port) {
        if(p!=null)
            p.cacheLayoutState(port);
    }



    public LayoutNode createLayout(DockingPort port) {
        return LayoutBuilder.getInstance().createLayout(port);
    }

    public boolean display(Dockable dockable) {
        return RestorationManager.getInstance().restore(dockable);
    }

    static void setDockingStateListening(boolean enabled) {
        UPDATE_LISTENER.setEnabled(enabled);
    }

    static boolean isDockingStateListening() {
        return UPDATE_LISTENER.isEnabled();
    }

    static void clear(DockingPort port) {
        if(port!=null) {
            boolean currState = isDockingStateListening();
            setDockingStateListening(false);
            port.clear();
            setDockingStateListening(currState);
        }
    }

    static void updateDockingStates(final Dockable[] dockables) {
        if(dockables==null)
            return;

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                for(int i=0; i<dockables.length; i++) {
                    UPDATE_LISTENER.updateState(dockables[i]);
                }
            }
        });
    }

    public synchronized boolean store() throws IOException, PersistenceException {
        return store(null);
    }

    public synchronized boolean store(String persistenceKey) throws IOException, PersistenceException {
        if(m_persistHandler==null)
            return false;

        DockingPort rootPort = findMainDockingPort();
        cacheLayoutState(getCurrentPerspective(), rootPort);

        Perspective[] items = getPerspectives();
        for(int i=0; i<items.length; i++) {
            items[i] = (Perspective)items[i].clone();
        }

        PerspectiveModel info = new PerspectiveModel(m_defaultPerspective, getCurrentPerspectiveName(), items);
        String pKey = persistenceKey==null? m_defaultPersistenceKey: persistenceKey;
        return m_persistHandler.store(pKey, info);
    }

    public synchronized boolean load() throws IOException, PersistenceException {
        return load(null);
    }

    public synchronized boolean load(String persistenceKey) throws IOException, PersistenceException {
        if(m_persistHandler==null)
            return false;

        String pKey = persistenceKey==null? m_defaultPersistenceKey: persistenceKey;
        PerspectiveModel info = m_persistHandler.load(pKey);
        if(info==null)
            return false;

        Perspective[] perspectives = info.getPerspectives();

        m_perspectives.clear();
        for(int i=0; i<perspectives.length; i++) {
            add(perspectives[i]);
        }
        setDefaultPerspective(info.getDefaultPerspective());
        setCurrentPerspectiveName(info.getCurrentPerspective());
        return true;
    }

    public static boolean isRestoreFloatingOnLoad() {
        return getInstance().restoreFloatingOnLoad;
    }

    public static void setRestoreFloatingOnLoad(boolean restoreFloatingOnLoad) {
        getInstance().restoreFloatingOnLoad = restoreFloatingOnLoad;
    }

    //FIXME returns wrong window (first found) for multiple frames
    public static RootWindow getMainApplicationWindow() {
        RootWindow[] windows = DockingManager.getDockingWindows();
        // if the DockingManager couldn't resolve any windows using the
        // standard mechanism, we can try our own custom search
        if(windows.length==0)
            windows = resolveDockingWindows();

        // TODO: fix this code to keep track of the proper dialog owner
        RootWindow window = null;
        for(int i=0; i<windows.length; i++) {
            window = windows[i];
            if(window.getOwner()==null)
                break;
        }
        return window;
    }

    private static RootWindow[] resolveDockingWindows() {
        // locate all the root dockingports
        Set rootPorts = DockingPortTracker.getRootDockingPorts();
        ArrayList windows = new ArrayList(rootPorts.size());
        // for each dockingPort, resolve its root window
        for(Iterator it=rootPorts.iterator(); it.hasNext();) {
            DockingPort port = (DockingPort)it.next();
            RootWindow window = RootWindow.getRootContainer((Component)port);
            if(window!=null)
                windows.add(window);
        }
        return (RootWindow[])windows.toArray(new RootWindow[0]);
    }

    public static DockingPort getMainDockingPort() {
        RootWindow window = getMainApplicationWindow();
        return window==null? null: DockingManager.getRootDockingPort(window.getRootContainer());
    }

    public boolean restore(boolean loadFromStorage) throws IOException, PersistenceException {
        boolean loaded = loadFromStorage? load(): true;
        reload();
        return loaded;
    }

    public String getDefaultPersistenceKey() {
        return m_defaultPersistenceKey;
    }

    public void setDefaultPersistenceKey(String key) {
        m_defaultPersistenceKey = key;
    }

    private DockingPort findMainDockingPort() {
        Set rootPorts = DockingPortTracker.getRootDockingPorts();
        DockingPort rootPort = null;
        for(Iterator it=rootPorts.iterator(); it.hasNext();) {
            DockingPort port = (DockingPort)it.next();
            Window win = SwingUtilities.getWindowAncestor((Component)port);
            if(win instanceof Dialog)
                continue;

            rootPort = port;
            break;
        }
        return rootPort;
    }
}
