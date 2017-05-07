// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.setup.map;

import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.AirVehicleState;
import afrl.cmasi.EntityConfiguration;
import afrl.cmasi.EntityState;
import afrl.cmasi.Location3D;
import avtas.amase.map.EntityGraphic;
import avtas.amase.objtree.ObjectTree;
import avtas.amase.ui.IconManager;
import avtas.map.MapMouseListener;
import avtas.map.MapPopupListener;
import avtas.map.Proj;
import avtas.map.graphics.Selector;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DecimalFormat;
import javax.swing.AbstractAction;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author AFRL/RQQD
 */
public class EntitySetupGraphic extends EntityGraphic implements MapMouseListener, MapPopupListener {

    boolean dragging = false;
    private Object state;
    private Object config;
    PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    public static final String STATE_CHANGED = "AVS_Changed";
    public static final String CONFIG_CHANGED = "AVC_Changed";
    public static final String DELETE_AIRCRAFT = "Delete_Aircraft";
    public static final String SELECT_AIRCRAFT = "Select_Aircraft";
    private boolean avsChanged = false;
    Selector selector = new Selector();
    String label = "";
    long id = 0;

    public EntitySetupGraphic(AirVehicleConfiguration avc, Color color) {
        super(avc, IconManager.getIcon(avc), color);
        setConfiguration(avc);
        selector.setSelectedGraphic(this);
        selector.setVisible(false);
        //add(selector);
    }

    public EntitySetupGraphic(EntityConfiguration ec, Color color) {
        super(ec, IconManager.getIcon(ec), color);
        setConfiguration(ec);
        selector.setSelectedGraphic(this);
        selector.setVisible(false);
        //add(selector);
    }

    public void setConfiguration(AirVehicleConfiguration avc) {
        this.config = avc;
        setName("Aircraft: " + avc.getID());
        this.label = avc.getLabel();
        id = avc.getID();
        super.vehName.setText(avc.getLabel());
    }

    public void setConfiguration(EntityConfiguration ec) {
        this.config = ec;
        setName("Entity: " + ec.getID());
        this.label = ec.getLabel();
        this.id = ec.getID();
        super.vehName.setText(ec.getLabel());
    }

    public void update(AirVehicleState avs) {
        this.state = avs;
        super.update(avs);
    }

    public void update(EntityState es) {
        this.state = es;
        super.update(es);
    }

    public AirVehicleState getAirVehicleState() {
        if (state instanceof AirVehicleState)
            return (AirVehicleState) state;
        return null;
    }
    
    public EntityState getEntityState() {
        if (state instanceof EntityState)
            return (EntityState) state;
        return null;
    }

    public AirVehicleConfiguration getAirVehicleConfig() {
        if (config instanceof AirVehicleConfiguration)
            return (AirVehicleConfiguration) config;
        return null;
    }

    public EntityConfiguration getEntityConfig() {
        if (config instanceof EntityConfiguration)
            return (EntityConfiguration) config;
        return null;
    }

    public void setSelected(boolean select) {
        selector.setVisible(select);
    }

    public boolean isSelected() {
        return selector.isVisible();
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        changeSupport.addPropertyChangeListener(l);
    }

    public void mouseMoved(MouseEvent e, double lat, double lon) {
    }

    public void mouseClicked(MouseEvent e, double lat, double lon) {
        if (getBounds() != null && getBounds().contains(e.getPoint())) {
            setSelected(true);
            changeSupport.firePropertyChange(SELECT_AIRCRAFT, null, EntitySetupGraphic.this);
        }
        else {
            setSelected(false);
        }
    }

    public void mouseDragged(MouseEvent e, double lat, double lon) {
        if (dragging) {
            if (state instanceof AirVehicleState) {
                ((AirVehicleState) state).getLocation().setLatitude(lat);
                ((AirVehicleState) state).getLocation().setLongitude(lon);
                update((AirVehicleState) state);
                avsChanged = true;
            }
            else if (state instanceof EntityState) {
                ((EntityState) state).getLocation().setLatitude(lat);
                ((EntityState) state).getLocation().setLongitude(lon);
                update((EntityState) state);
                //clearTrail();
                avsChanged = true;
            }
        }
    }

    public void mousePressed(MouseEvent e, double lat, double lon) {
        if (getBounds() != null && getBounds().contains(e.getPoint())) {
            dragging = true;
            e.consume();
        }
    }

    public void mouseReleased(MouseEvent e, double lat, double lon) {
        if (avsChanged) {
            changeSupport.firePropertyChange(STATE_CHANGED, null, EntitySetupGraphic.this);
            e.consume();
        }
        dragging = false;
        avsChanged = false;
    }

    @Override
    public void addPopupMenuItems(JPopupMenu menu, MouseEvent e, double lat, double lon) {

        final JMenu editMenu = new JMenu("Edit...");

        editMenu.add(new AbstractAction("Edit Configuration") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object obj = ObjectTree.showEditWindow(config, editMenu, "Edit Config (" + id + ")");
                if (obj != null) {
                    EntitySetupGraphic.this.changeSupport.firePropertyChange(CONFIG_CHANGED, null, EntitySetupGraphic.this);
                }
            }
        });

        editMenu.add(new AbstractAction("Edit State") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object obj = ObjectTree.showEditWindow(state, editMenu, "Edit State (" + id + ")");
                if (obj != null) {
                    EntitySetupGraphic.this.changeSupport.firePropertyChange(STATE_CHANGED, null, EntitySetupGraphic.this);
                }
            }
        });


        final JMenuItem removeMenu = new JMenuItem(new AbstractAction("Delete " + label) {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ans = JOptionPane.showConfirmDialog(editMenu, "Are you sure you want to delete entity " + id + "?");
                if (ans == JOptionPane.YES_OPTION) {
                    EntitySetupGraphic.this.changeSupport.firePropertyChange(DELETE_AIRCRAFT, null, EntitySetupGraphic.this);
                    EntitySetupGraphic.this.changeSupport.firePropertyChange(DELETE_AIRCRAFT, null, EntitySetupGraphic.this);
                }
            }
        });

        menu.add(createHeadingSpinner());
        menu.add(createAltitudeBox());
        menu.add(editMenu);
        menu.add(removeMenu);

    }

    public Component createHeadingSpinner() {
        double heading = 0;
        if (getAirVehicleState() != null) {
            heading = getAirVehicleState().getHeading();
        }
        else if (getEntityState() != null) {
            heading = getEntityState().getHeading();
        }
        final JSpinner spinner = new JSpinner(new SpinnerNumberModel((int) heading, 0, 360, 1));
        spinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (getAirVehicleState() != null) {
                    getAirVehicleState().setHeading((Integer) spinner.getValue());
                }
                else if (getEntityState() != null) {
                    getEntityState().setHeading((Integer) spinner.getValue());
                }
                changeSupport.firePropertyChange(STATE_CHANGED, null, EntitySetupGraphic.this);
            }
        });
        JPanel p = new JPanel();
        p.add(new JLabel("Heading: "));
        p.add(spinner);
        return p;
    }

    public Component createAltitudeBox() {
        
        Location3D tmp = null;
        if (getAirVehicleState() != null) {
            tmp = getAirVehicleState().getLocation();
        }
        else if (getEntityState() != null) {
            tmp = getEntityState().getLocation();
        }
        final Location3D loc = tmp;
        
        final JFormattedTextField textField = new JFormattedTextField(new DecimalFormat("#"));
        textField.setColumns(10);
        textField.setValue(loc.getAltitude());
        textField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            public void removeUpdate(DocumentEvent e) {
                update();
            }

            public void changedUpdate(DocumentEvent e) {
                update();
            }

            public void update() {
                Number alt = (Number) textField.getValue();
                if (alt != null) {
                    loc.setAltitude(alt.floatValue());
                    changeSupport.firePropertyChange(STATE_CHANGED, null, EntitySetupGraphic.this);
                }
            }
        });

        JPanel p = new JPanel();
        p.add(new JLabel("Altitude: "));
        p.add(textField);
        return p;
    }

    @Override
    public synchronized void paint(Graphics2D g) {
        super.paint(g);
        selector.paint(g);
    }

    @Override
    public synchronized void project(Proj proj) {
        super.project(proj); 
        selector.project(proj);
    }
    
    
    
    
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */