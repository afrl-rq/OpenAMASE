// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.layers;

import avtas.map.MapLayer;
import avtas.map.MapPanel;
import avtas.map.Proj;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import javax.swing.JPanel;

/**
 * A MapLayer that allows users to add Swing/AWT components to the map. This layer 
 * maintains an internal panel that is added to the map as a child.  To manipulate
 * content of the panel, use the {@link #getPanel() } method and use typical Swing 
 * methods to add, manipulate, and layout child components.<br/>
 * <br/>
 * The control panel is invisible, but automatically covers the entire map bounds. 
 * The panel stretches and shrinks automatically as the map is resized.  Because the
 * component covers the entire map, do not add mouse event listeners directly to the
 * panel.  This will disable map mouse functions.  However, listeners can be added
 * to controls that are added to this layer.  Those events will not pass through to 
 * the map.
 * 
 * @author AFRL/RQQD
 */
public class ControlLayer extends MapLayer {
    
    JPanel internalComp = new JPanel();
    Paint background = null;

    public ControlLayer() {
        internalComp.setOpaque(false);
    }

    @Override
    public void setMap(MapPanel parent) {
        if (getMap() != null) {
            getMap().remove(internalComp);
        }
        if (parent != null) {
            parent.add(internalComp, 0);
        }
        super.setMap(parent);
    }

    @Override
    public void paint(Graphics2D g) {
        if (background != null) {
            g.setPaint(background);
            g.fillRect(0, 0, internalComp.getWidth(), internalComp.getHeight());
        }
    }

    @Override
    public void setVisible(boolean visible) {
        internalComp.setVisible(visible);
        super.setVisible(visible); 
    }
    
    
    
    /** Returns the color that fills the background of the layer.  Returns null
     *  if no background has been set.
     */
    public Paint getBackground() {
        return background;
    }
    
    /** Sets the background for this layer.  This method can be used to create a 
     *  semi-transparent background for the control panel.  A null Color is interpreted
     *  as clear.
     * @param background 
     */
    public void setBackground(Paint background) {
        this.background = background;
        refresh();
    }

    @Override
    public void project(Proj proj) {
        internalComp.setBounds(0, 0, proj.getWidth(), proj.getHeight());
    }
    
    /** Returns a reference to the internal panel that holds the controls.  To add 
     *  controls, get the panel and use standard Swing/AWT methods to add children to it.
     * @return A panel holding the user controls.
     */
    public JPanel getPanel() {
        return internalComp;
    } 
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */