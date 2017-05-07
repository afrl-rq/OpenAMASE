// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.map;

import afrl.cmasi.AreaSearchTask;
import afrl.cmasi.LineSearchTask;
import afrl.cmasi.Location3D;
import afrl.cmasi.PointSearchTask;
import afrl.cmasi.RemoveTasks;
import afrl.cmasi.Task;
import avtas.amase.objtree.ObjectTree;
import avtas.amase.scenario.ScenarioEvent;
import avtas.app.AppEventListener;
import avtas.app.AppEventManager;
import avtas.map.graphics.MapGraphic;
import avtas.map.graphics.MapMarker;
import avtas.map.graphics.MapPoly;
import avtas.map.graphics.Painter;
import avtas.map.layers.GraphicsLayer;
import avtas.util.Colors;
import avtas.util.WindowUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ListIterator;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;

/**
 *
 * @author AFRL/RQQD
 */
public class TaskLayer extends GraphicsLayer<MapGraphic> implements AppEventListener {

    //Color taskLineColor = Color.GREEN;
    Color taskFill = Colors.setAlpha(Color.GREEN, 0.3f);
    //Stroke taskStroke = new BasicStroke(1f);
    protected static Painter taskPainter = Painter.createOutlinePainter(Color.GREEN, Color.WHITE, 1f);
    
    

    public TaskLayer() {
        AppEventManager.getDefaultEventManager().addListener(this);
    }

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof Task) {
            Task task = (Task) event;
            MapGraphic g = createGraphic(task);
            
            for (ListIterator<MapGraphic> it=getList().listIterator(); it.hasNext();) {
                if ( ((Task) it.next().getRefObject()).getTaskID() == task.getTaskID() ) {
                    it.remove();
                }
            }

            if (g != null) {
                add(g);
                refresh();
            }
        }
        else if (event instanceof ScenarioEvent) {
            clear();
            refresh();
        }
        else if (event instanceof RemoveTasks) {
            RemoveTasks rt = (RemoveTasks) event;
            for (long id : rt.getTaskList()) {
                int index = indexOf(getByRefObject(id));
                if (index != -1) {
                    remove(index);
                }
            }
        }
    }

    MapGraphic createGraphic(Task task) {

        MapGraphic g = null;
        if (task instanceof LineSearchTask) {
            LineSearchTask lineTask = (LineSearchTask) task;
            MapPoly line = new MapPoly();
            for (Location3D loc : lineTask.getPointList()) {
                line.addPoint(loc.getLatitude(), loc.getLongitude());
            }
            g = line;
        }
        else if (task instanceof AreaSearchTask) {
            AreaSearchTask areaTask = (AreaSearchTask) task;
            g = CmasiShapes.getMapShape(areaTask.getSearchArea());
        }
        else if (task instanceof PointSearchTask) {
            PointSearchTask ptTask = (PointSearchTask) task;
            Location3D pt = ptTask.getSearchLocation();
            if (pt != null) {
                g = new MapMarker(pt.getLatitude(), pt.getLongitude());
            }
        }

        if (g != null) {
            g.setPainter(taskPainter);
            if ( !(g instanceof MapPoly) || ((MapPoly)g).isPolygon() ) {
                g.setFill(taskFill);
            }
            //g.setStroke(taskStroke);
            g.setRefObject(task);
            g.setName(task.getClass().getSimpleName() + " " + task.getTaskID());
        }

        return g;
    }

    @Override
    public void addPopupMenuItems(javax.swing.JPopupMenu menu, MouseEvent e, double lat, double lon) {
        for (final MapGraphic g : this) {
            if (g.contains(e.getPoint()) && !(g instanceof MapPoly) ) {
                menu.add( getViewItem(g));
            }
            else if (g.onEdge(e.getX(), e.getY(), 4)) {
                menu.add(getViewItem(g));
            }
        }
    }
    
    JMenuItem getViewItem(final MapGraphic g) {

        return new JMenuItem(new AbstractAction(g.getName()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                ObjectTree tree = new ObjectTree(g.getRefObject());
                tree.setEditable(false);
                WindowUtils.showPlainDialog(getMap(), new JScrollPane(tree), g.getName());
            }
        });
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */