// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.edit;

import avtas.map.MapLayer;
import avtas.map.MapMouseListener;
import avtas.map.MapPanel;
import avtas.map.MapPopupListener;
import avtas.map.Proj;
import avtas.map.graphics.MapGraphic;
import avtas.map.graphics.MapGraphicsList;
import avtas.map.layers.GraphicsLayer;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * A layer used to manipulate editable graphics.
 *
 * @author AFRL/RQQD
 */
public class EditableLayer<T extends GraphicEditor> extends GraphicsLayer<T> implements MapMouseListener, MapPopupListener {

    protected GraphicEditor activeEditor = null;
    MapGraphic dragGraphic = null;
    static Shape dragPointShape = new Rectangle(5, 5);
    DragPoint currentDragPt = null;
    private MouseEvent startEvent = null;
    public static int CREATE_MODE = 0;
    public static int EDIT_MODE = 1;
    public static int NO_EDIT_MODE = 2;
    private int layerMode = 1;
    private boolean dragging = false;
    private ArrayList<EditListener> listenerList = new ArrayList<>();
    private Proj proj;

    public EditableLayer() {
    }

    @Override
    public void setMap(MapPanel parent) {
        super.setMap(parent);
        if (parent != null) {
            KeyStroke keystroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
            parent.getActionMap().put("END_EDIT", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    endEdit();
                }
            });
            parent.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keystroke, "END_EDIT");
        }
    }

    public void addEditListener(EditListener listener) {
        if (!listenerList.contains(listener)) {
            listenerList.add(listener);
        }
    }

    public void removeEditListener(EditListener listener) {
        listenerList.remove(listener);
    }

    public void fireListenEvent(GraphicEditor shape, int mode) {
        for (EditListener l : listenerList) {
            l.editPerformed(shape, mode);
        }
    }

    /**
     * Returns the currently active graphic editor, or null if no editor is
     * active
     */
    public GraphicEditor getActiveEditor() {
        return activeEditor;
    }

    /**
     * Sets the editing mode for this layer. EDIT_MODE = edit graphics on the
     * layer, CREATE_MODE = create new graphics NO_EDIT_MODE = do nothing
     *
     * @param mode the mode to set ( CREATE_MODE, EDIT_MODE, NO_EDIT_MODE)
     */
    public void setLayerMode(int mode) {
        layerMode = mode;

        if (layerMode == CREATE_MODE) {
            parentMap.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }
        else {
            parentMap.setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * Returns the editing mode for this layer. EDIT_MODE = edit graphics on the
     * layer, CREATE_MODE = create new graphics NO_EDIT_MODE = do nothing
     */
    public int getLayerMode() {
        return this.layerMode;
    }

    public void cancelEditing() {
        if (activeEditor != null) {
            fireListenEvent(activeEditor, EditListener.EDIT_CANCELED);
            activeEditor.setActive(false);
            activeEditor = null;
            refresh();
        }

    }

    public void startEdit(T g) {
        cancelEditing();
        if (getList().contains(g)) {
            activeEditor = g;
            activeEditor.setActive(true);
            fireListenEvent(activeEditor, EditListener.EDIT_START);
            refresh();
        }
    }

    public void endEdit() {
        if (activeEditor != null) {
            
            fireListenEvent(activeEditor, EditListener.EDIT_END);
            activeEditor.setActive(false);
            activeEditor = null;
            setLayerMode(EDIT_MODE);
            refresh();
        }


    }

    /**
     * puts a new graphic on the list and prepares it for editing.
     */
    public void createGraphic(T editor) {
        cancelEditing();
        setLayerMode(CREATE_MODE);
        editor.getGraphic().setVisible(true);
        editor.setVisible(true);
        add(editor);
        activeEditor = editor;
        editor.setActive(true);
        fireListenEvent(activeEditor, EditListener.EDIT_START);
    }

    public void mouseClicked(MouseEvent e, double degLat, double degLon) {
        startEvent = null;
        if (layerMode == EDIT_MODE) {
//            if (activeEditor != null) {
//                activeEditor.setActive(false);
//                activeEditor = null;
//            }
            for (T g : this) {
                boolean clickOn = g.getGraphic().contains(e.getPoint()) && g.getFill() != null;
                clickOn = clickOn || g.getGraphic().onEdge(e.getX(), e.getY(), 10);
                if (clickOn) {
                    if (g == activeEditor) {
                        return;
                    }
                    else if (activeEditor != null) {
                        endEdit();
                    }
                    startEdit(g);
                    refresh();
                    return;
                }
            }
            
            cancelEditing();
            refresh();
            
        }

    }

    public void mouseDragged(MouseEvent e, double degLat, double degLon) {

        if (layerMode == CREATE_MODE && activeEditor != null) {
            dragging = true;
            int x1 = startEvent.getPoint().x;
            int y1 = startEvent.getPoint().y;
            int x2 = e.getPoint().x;
            int y2 = e.getPoint().y;

            activeEditor.setBounds(proj.getLat(x1, y1), proj.getLon(x1, y1), proj.getLat(x2, y2), proj.getLon(x2, y2));

            fireListenEvent(activeEditor, EditListener.EDIT_ONGOING);
            refresh();

        }
        else if (layerMode == EDIT_MODE) {

            if (currentDragPt != null) {
                currentDragPt.moveTo(e.getX(), e.getY());
                dragging = true;
                fireListenEvent(activeEditor, EditListener.EDIT_ONGOING);
                refresh();
            }
            else if (startEvent != null && dragGraphic != null) {

                if (activeEditor != null && activeEditor == dragGraphic) {
                    dragging = true;
                    double dlat = -(e.getY() - startEvent.getY()) / proj.getPixPerLat();
                    double dlon = (e.getX() - startEvent.getX()) / proj.getPixPerLon();
                    activeEditor.translateGraphic(dlat, dlon, proj);
                    fireListenEvent(activeEditor, EditListener.EDIT_ONGOING);
                    startEvent = e;
                    refresh();
                }
            }
        }
    }

    public void mouseEntered(MouseEvent e, double degLat, double degLon) {
    }

    public void mouseExited(MouseEvent e, double degLat, double degLon) {
    }

    public void mouseMoved(MouseEvent e, double degLat, double degLon) {
    }

    public void mousePressed(MouseEvent e, double degLat, double degLon) {
        startEvent = e;
        if (layerMode == EDIT_MODE) {
            if (activeEditor != null) {
                currentDragPt = activeEditor.getDragPoint(e.getX(), e.getY());
            }
            if (currentDragPt == null) {
                for (T g : this) {
                    boolean dragHit = false;
                    if (g.getGraphic().contains(e.getPoint())) {
                        dragHit = true;
                    }
                    else if (g.getGraphic().onEdge(e.getX(), e.getY(), 5)) {
                        dragHit = true;
                    }
                    if (dragHit) {
                        dragGraphic = g;
                        startEvent = e;
                    }
                }
            }
        }
        else if (layerMode == CREATE_MODE) {
            if (e.getClickCount() > 1) {
                setLayerMode(EDIT_MODE);
                endEdit();
            }
            else {
                if (activeEditor != null) {
                    activeEditor.createPoint(e, degLat, degLon, this);
                }
                refresh();
            }
        }
    }

    public void mouseReleased(MouseEvent e, double degLat, double degLon) {
        if (activeEditor != null && dragging) {
            if (layerMode == EDIT_MODE) {
                //graphicEditor.setActive(false);
                fireListenEvent(activeEditor, EditListener.EDIT_END);
            }
            else if (layerMode == CREATE_MODE) {
                setLayerMode(EDIT_MODE);
                fireListenEvent(activeEditor, EditListener.EDIT_END);
            }
        }
        currentDragPt = null;
        startEvent = null;
        dragGraphic = null;
        dragging = false;
    }

    @Override
    public void project(Proj view) {
        this.proj = view;
        super.project(proj);
    }

    @Override
    public void addPopupMenuItems(javax.swing.JPopupMenu menu, MouseEvent e, final double lat, final double lon) {

        if ((layerMode == EDIT_MODE || layerMode == CREATE_MODE) && activeEditor != null) {
            activeEditor.addPopupItems(e, lat, lon, menu, this);
        }
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */