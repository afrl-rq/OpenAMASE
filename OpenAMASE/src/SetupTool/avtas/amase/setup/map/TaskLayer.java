// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================


package avtas.amase.setup.map;

import afrl.cmasi.AbstractZone;
import afrl.cmasi.AreaSearchTask;
import afrl.cmasi.Circle;
import afrl.cmasi.KeepInZone;
import afrl.cmasi.KeepOutZone;
import afrl.cmasi.LineSearchTask;
import afrl.cmasi.Location3D;
import afrl.cmasi.LoiterTask;
import afrl.cmasi.PointSearchTask;
import afrl.cmasi.SearchTask;
import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import afrl.cmasi.Task;
import avtas.amase.map.CmasiShapes;
import avtas.amase.map.LoiterGraphic;
import avtas.amase.objtree.ObjectTree;
import avtas.amase.util.CmasiUtils;
import avtas.amase.setup.RemoveObjectsEvent;
import avtas.amase.setup.SelectObjectEvent;
import avtas.amase.setup.ToolbarEvent;
import avtas.app.AppEventListener;
import avtas.app.AppEventManager;
import avtas.lmcp.LMCPObject;
import avtas.map.MapPanel;
import avtas.map.MapPopupListener;
import avtas.map.Proj;
import avtas.map.edit.CircleEditor;
import avtas.map.edit.EditListener;
import avtas.map.edit.EditableLayer;
import avtas.map.edit.EllipseEditor;
import avtas.map.edit.GraphicEditor;
import avtas.map.edit.MarkerEditor;
import avtas.map.edit.PolyEditor;
import avtas.map.edit.RectEditor;
import avtas.map.graphics.MapCircle;
import avtas.map.graphics.MapEllipse;
import avtas.map.graphics.MapGraphic;
import avtas.map.graphics.MapMarker;
import avtas.map.graphics.MapPoly;
import avtas.map.graphics.MapRect;
import avtas.map.graphics.MapText;
import avtas.map.graphics.Painter;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 * Creates a MapLayer that allows for the editing of Search Tasks
 *
 * @author AFRL/RQQD
 */
public class TaskLayer extends EditableLayer<GraphicEditor> implements AppEventListener, EditListener, MapPopupListener {

    private static Painter searchPainter = Painter.createOutlinePainter(Color.GREEN, Color.WHITE, 1f);
    private static Painter keepOutPainter = Painter.createOutlinePainter(Color.RED, Color.WHITE, 1f);
    private static Painter keepInPainter = Painter.createOutlinePainter(Color.YELLOW, Color.WHITE, 1f);
    static float DEFAULT_ZONE_ALT = 5000;
    private AppEventManager eventMgr = null;
    MapText tipText = new MapText();


    public TaskLayer() {

        this.eventMgr = AppEventManager.getDefaultEventManager();

        addEditListener(this);

        tipText.setOffset(10, -10);
        tipText.setColor(Color.YELLOW);
        tipText.setFont(tipText.getFont().deriveFont(Font.BOLD));
    }

    void resetLayer() {
        clear();
        cancelEditing();

        tipText.setColor(Color.YELLOW);
        tipText.setFont(tipText.getFont().deriveFont(Font.BOLD));
        refresh();
    }

    @Override
    public void setMap(MapPanel parent) {
        super.setMap(parent);
        if (parent != null) {
            KeyStroke keystroke = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
            parent.getActionMap().put("DELETE_OBJ", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteCurrentObject();
                }
            });
            parent.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keystroke, "DELETE_OBJ");
        }
    }

    void fireUpdate(LMCPObject changedObj) {
        if (eventMgr != null) {
            eventMgr.fireEvent(changedObj);
            eventMgr.fireEvent(new SelectObjectEvent(changedObj));
        }
        refresh();
    }

    @Override
    public void eventOccurred(Object event) {

        if (event instanceof SessionStatus) {
            if (((SessionStatus) event).getState() == SimulationStatusType.Reset) {
                resetLayer();
            }
        }
        else if (event instanceof SearchTask) {
            SearchTask st = (SearchTask) event;
            cancelEditing();
            for (GraphicEditor g : this) {
                if (g.getRefObject() == st) {
                    remove(g);
                    break;
                }
            }
            GraphicEditor g = makeSearchShape(st);
            add(g);
            //startEdit(g);
            refresh();
        }
        else if (event instanceof LoiterTask) {
            LoiterTask task = (LoiterTask) event;
            cancelEditing();
            for (GraphicEditor g : this) {
                if (g.getRefObject() == task) {
                    remove(g);
                    break;
                }
            }
            GraphicEditor g = makeLoiterTaskGraphic(task);
            add(g);
            refresh();
        }
        else if (event instanceof AbstractZone) {
            AbstractZone zone = (AbstractZone) event;
            cancelEditing();
            for (GraphicEditor g : this) {
                if (g.getRefObject() == zone) {
                    remove(g);
                    break;
                }
            }
            GraphicEditor g = makeZoneShape(zone);
            add(g);
            //startEdit(g);
            refresh();
        }
        else if (event instanceof ToolbarEvent) {
            ToolbarEvent tbEvent = (ToolbarEvent) event;
            if (tbEvent.getObject() == null) {
                cancelEditing();
            }
            else if (tbEvent.getObject() instanceof SearchTask) {
                createNewSearchTask((SearchTask) tbEvent.getObject());
            }
            else if (tbEvent.getObject() instanceof AbstractZone) {
                createNewZone((AbstractZone) tbEvent.getObject());
            }
            else if (tbEvent.getObject() instanceof LoiterTask) {
                createNewLoiterTask((LoiterTask) tbEvent.getObject());
            }
        }
        else if (event instanceof SelectObjectEvent) {
            SelectObjectEvent se = (SelectObjectEvent) event;
            if (!(se.getObject() instanceof LMCPObject)) {
                return;
            }
            if (activeEditor != null && activeEditor.getGraphic().getRefObject() == se.getObject()) {
                return;
            }
            else if (activeEditor != null) {
                cancelEditing();
            }
            for (GraphicEditor g : this) {
                if (g.getRefObject() == se.getObject()) {
                    startEdit(g);
                    break;
                }
            }
        }
        else if (event instanceof RemoveObjectsEvent) {
            for (Object obj : ((RemoveObjectsEvent) event).objects) {
                if (obj != null) {
                    for (GraphicEditor g : this) {
                        if (g.getRefObject() == obj) {
                            remove(g);
                            refresh();
                            break;
                        }
                    }
                }
            }
        }
    }

    public SearchTask createNewSearchTask(SearchTask st) {
        TreeSet<Long> ids = new TreeSet<Long>();
        for (MapGraphic g : this) {
            if (g.getRefObject() instanceof SearchTask) {
                ids.add(((SearchTask) g.getRefObject()).getTaskID());
            }
        }
        st.setTaskID(ids.isEmpty() ? 1 : ids.last() + 1);
        createGraphic(makeSearchShape(st));
        return st;
    }

    public LoiterTask createNewLoiterTask(LoiterTask task) {
        TreeSet<Long> ids = new TreeSet<Long>();
        for (MapGraphic g : this) {
            if (g.getRefObject() instanceof SearchTask) {
                ids.add(((SearchTask) g.getRefObject()).getTaskID());
            }
        }
        task.setTaskID(ids.isEmpty() ? 1 : ids.last() + 1);

        createGraphic(makeLoiterTaskGraphic(task));

        return task;
    }

    public AbstractZone createNewZone(AbstractZone z) {
        TreeSet<Long> ids = new TreeSet<>();
        for (MapGraphic g : this) {
            if (g.getRefObject() instanceof AbstractZone) {
                ids.add(((AbstractZone) g.getRefObject()).getZoneID());
            }
        }
        z.setZoneID(ids.isEmpty() ? 1 : ids.last() + 1);
        createGraphic(makeZoneShape(z));
        return z;
    }

    protected GraphicEditor makeSearchShape(SearchTask st) {

        MapGraphic g = null;

        if (st instanceof AreaSearchTask) {

            AreaSearchTask ast = (AreaSearchTask) st;
            g = CmasiShapes.getMapShape(ast.getSearchArea());
            if (g != null) {
                Color fill = new Color(0, 255, 0, 50);
                g.setFill(fill);
            }

        }
        else if (st instanceof LineSearchTask) {
            MapPoly poly = new MapPoly();
            for (Location3D loc : ((LineSearchTask) st).getPointList()) {
                poly.addPoint(loc.getLatitude(), loc.getLongitude());
            }
            g = poly;
        }
        else if (st instanceof PointSearchTask) {
            MapMarker marker = new MapMarker();
            Location3D loc = ((PointSearchTask) st).getSearchLocation();
            marker.setLat(loc.getLatitude());
            marker.setLon(loc.getLongitude());
            g = marker;
        }
        
        if (g != null) {
            g.setRefObject(st);
            g.setName(st.getClass().getSimpleName() + " " + st.getTaskID());
            g.setPainter(searchPainter);

            return wrapEditor(g);
        }
        return null;
    }

    protected GraphicEditor makeZoneShape(AbstractZone z) {

        MapGraphic g = null;

        if (z instanceof KeepOutZone) {
            g = CmasiShapes.getMapShape(((KeepOutZone) z).getBoundary());
            Color fill = new Color(255, 0, 0, 50);
            g.setFill(fill);
            g.setPainter(keepOutPainter);
        }
        if (z instanceof KeepInZone) {
            g = CmasiShapes.getMapShape(((KeepInZone) z).getBoundary());
            g.setPainter(keepInPainter);
        }
        if (g != null) {
            g.setRefObject(z);
            return wrapEditor(g);
        }

        return null;
    }

    protected GraphicEditor wrapEditor(MapGraphic g) {
        GraphicEditor editor = null;
        if (g instanceof MapPoly) {
            editor = new PolyEditor((MapPoly) g);
        }
        else if (g instanceof MapMarker) {
            editor = new MarkerEditor((MapMarker) g);
        }
        else if (g instanceof MapCircle) {
            editor = new CircleEditor((MapCircle) g);
        }
        else if (g instanceof MapEllipse) {
            editor = new EllipseEditor((MapEllipse) g);
        }
        else if (g instanceof MapRect) {
            editor = new RectEditor((MapRect) g);
        }
        
        if (editor != null) {
            editor.setRefObject(g.getRefObject());
        }
        return editor;
    }

    protected GraphicEditor makeLoiterTaskGraphic(LoiterTask task) {

        LoiterGraphic lg = new LoiterGraphic(task.getDesiredAction());
        lg.setRefObject(task);
        lg.setName(task.getClass().getSimpleName() + " " + task.getTaskID());
        lg.setPainter(searchPainter);
        //lg.getBoundaryGraphics().setPainter(searchPainter);
        GraphicEditor ed = lg.getEditor();
        ed.setRefObject(task);
        return ed;
    }

    @Override
    public void editPerformed(GraphicEditor shape, int mode) {
        if (mode == EditListener.EDIT_START) {
            if (eventMgr != null) {
                eventMgr.fireEvent(new SelectObjectEvent(shape.getRefObject()), this);
            }
        }
        else if (mode == EditListener.EDIT_END) {

            Object obj = shape.getRefObject();

            if (obj instanceof SearchTask) {
                SearchTask st = (SearchTask) obj;
                if (st instanceof AreaSearchTask) {
                    ((AreaSearchTask) st).setSearchArea(CmasiShapes.mapShapeToCMASI(shape.getGraphic()));
                }
                if (st instanceof LineSearchTask) {
                    double[] latlons = ((MapPoly) shape.getGraphic()).getLatLons();
                    LineSearchTask lineTask = (LineSearchTask) st;
                    lineTask.getPointList().clear();
                    for (int i = 0; i < latlons.length; i += 2) {
                        Location3D loc = new Location3D();
                        loc.setLatitude(latlons[i]);
                        loc.setLongitude(latlons[i + 1]);
                        lineTask.getPointList().add(loc);
                    }
                }
                else if (st instanceof PointSearchTask) {
                    Location3D loc = new Location3D();
                    loc.setLatitude(((MapMarker) shape.getGraphic()).getLat());
                    loc.setLongitude(((MapMarker) shape.getGraphic()).getLon());
                    ((PointSearchTask) st).setSearchLocation(loc);
                }
                fireUpdate(st);
            }
            else if (obj instanceof KeepOutZone) {
                KeepOutZone z = (KeepOutZone) obj;
                z.setBoundary(CmasiShapes.mapShapeToCMASI(shape.getGraphic()));
                if (z.getMaxAltitude() == 0 && z.getMinAltitude() == 0) {
                    z.setMaxAltitude(DEFAULT_ZONE_ALT); //DEFAULT VALUE
                }
                fireUpdate(z);

            }
            else if (obj instanceof KeepInZone) {
                KeepInZone z = (KeepInZone) obj;
                z.setBoundary(CmasiShapes.mapShapeToCMASI(shape.getGraphic()));
                if (z.getMaxAltitude() == 0 && z.getMinAltitude() == 0) {
                    z.setMaxAltitude(DEFAULT_ZONE_ALT); //DEFAULT VALUE
                }
                fireUpdate(z);
            }
            else if (obj instanceof LoiterTask) {
                LoiterTask lt = (LoiterTask) obj;
                Location3D loc = new Location3D();
                loc.setLatitude(((LoiterTask) shape.getGraphic().getRefObject()).getDesiredAction().getLocation().getLatitude());
                loc.setLongitude(((LoiterTask) shape.getGraphic().getRefObject()).getDesiredAction().getLocation().getLongitude());
                lt.getDesiredAction().setLocation(loc);
                fireUpdate(lt);
            }
        }
    }

    void deleteCurrentObject() {
        GraphicEditor graphicEditor = getActiveEditor();
        if (graphicEditor != null) {
            Object refObj = graphicEditor.getGraphic().getRefObject();
            if (refObj instanceof LMCPObject) {
                final String objText = refObj.getClass().getSimpleName() + " "
                        + CmasiUtils.getUniqueId((LMCPObject) refObj);
                int ans = JOptionPane.showConfirmDialog(parentMap, "Delete " + objText, "Confirm Delete", JOptionPane.OK_CANCEL_OPTION);
                if (ans == JOptionPane.YES_OPTION) {
                    cancelEditing();
                    if (eventMgr != null) {
                        eventMgr.fireEvent(new RemoveObjectsEvent(refObj));
                    }
                }
            }
        }
    }

    @Override
    public void addPopupMenuItems(javax.swing.JPopupMenu menu, MouseEvent e, double lat, double lon) {

        List<GraphicEditor> hits = getList().getGraphicsWithin(e.getX(), e.getY(), 4);

        // remove hits if there is no fill and they are not on the edge of the graphic
        for (Iterator<GraphicEditor> it = hits.iterator(); it.hasNext();) {
            MapGraphic g = it.next().getGraphic();
            if (!g.onEdge(e.getX(), e.getY(), 2) && g.getFill() == null) {
                it.remove();
            }
        }

        for (final MapGraphic g : hits) {
            if (g.getRefObject() instanceof LMCPObject) {
                final LMCPObject lmcpObj = (LMCPObject) g.getRefObject();
                final String objText = lmcpObj.getClass().getSimpleName() + " "
                        + CmasiUtils.getUniqueId(lmcpObj);
                
                JMenu objMenu = new JMenu(objText);
                menu.add(objMenu);
                
                final JMenuItem editItem = new JMenuItem("Edit " + objText);
                objMenu.add(editItem);
                editItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Object obj = ObjectTree.showEditWindow(lmcpObj, editItem, objText);
                        if (obj instanceof LMCPObject) {
                            fireUpdate((LMCPObject) obj);
                        }
                    }
                });
                JMenuItem deleteItem = new JMenuItem("Delete " + objText);
                deleteItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int ans = JOptionPane.showConfirmDialog(parentMap, "Delete " + objText, "Confirm Delete", JOptionPane.OK_CANCEL_OPTION);
                        if (ans == JOptionPane.YES_OPTION) {
                            if (eventMgr != null) {
                                eventMgr.fireEvent(new RemoveObjectsEvent(lmcpObj));
                            }
                        }
                    }
                });
                objMenu.add(deleteItem);

            }
        }

        JMenu addTaskMenu = new JMenu("Add Task");
        menu.add(addTaskMenu);
        addTaskMenu.add(new AbstractAction("Search Area") {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewSearchTask(new AreaSearchTask());
            }
        });
        addTaskMenu.add(new AbstractAction("Search Line") {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewSearchTask(new LineSearchTask());
            }
        });
        addTaskMenu.add(new AbstractAction("Search Point") {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewSearchTask(new PointSearchTask());
            }
        });



        JMenu addZoneMenu = new JMenu("Add Zone");
        menu.add(addZoneMenu);
        addZoneMenu.add(new AbstractAction("Keep In Zone") {
            public void actionPerformed(ActionEvent e) {
                createNewZone(new KeepInZone());
            }
        });
        addZoneMenu.add(new AbstractAction("Keep Out Zone") {
            public void actionPerformed(ActionEvent e) {
                createNewZone(new KeepOutZone());
            }
        });
        addZoneMenu.add(new AbstractAction("Circular Keep Out Zone") {
            public void actionPerformed(ActionEvent e) {
                KeepOutZone z = new KeepOutZone();
                z.setBoundary(new Circle());
                createNewZone(z);
            }
        });

        super.addPopupMenuItems(menu, e, lat, lon);

    }

    @Override
    public void project(Proj view) {
        tipText.project(view);
        super.project(view);
    }

    @Override
    public void paint(Graphics2D g) {
        
        super.paint(g);
        tipText.paint(g);
    }

    @Override
    public void mouseMoved(MouseEvent e, double degLat, double degLon) {
        super.mouseMoved(e, degLat, degLon);
        for (MapGraphic g : this) {
            if (g.onEdge(e.getX(), e.getY(), 4) && !tipText.isVisible()) {
                tipText.setLatLon(degLat, degLon);
                if (g.getRefObject() instanceof AbstractZone) {
                    tipText.setText("Zone " + ((AbstractZone) g.getRefObject()).getZoneID());
                    tipText.setVisible(true);
                    refresh();
                    return;
                }
                else if (g.getRefObject() instanceof Task) {
                    tipText.setText("Task " + ((Task) g.getRefObject()).getTaskID());
                    tipText.setVisible(true);
                    refresh();
                    return;
                }
            }
        }
        tipText.setVisible(false);
        refresh();
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */
