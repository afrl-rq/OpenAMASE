// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.objtree;

import afrl.cmasi.AirVehicleConfiguration;
import afrl.cmasi.FlightProfile;
import afrl.cmasi.TurnType;
import avtas.util.ObjectUtils;
import avtas.util.WindowUtils;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;


/**
 *
 * @author AFRL/RQQD
 */
public class TreeTest extends JPanel {

    

    public static void main(String[] args) {
        
        final AirVehicleConfiguration object = new AirVehicleConfiguration();
        object.getAvailableTurnTypes().add(TurnType.FlyOver);
        object.getAlternateFlightProfiles().add(new FlightProfile());
        
        ObjectTree table = new ObjectTree();
        
        table.getTreeTableModel().addTreeModelListener(new TreeModelListener() {

            @Override
            public void treeNodesChanged(TreeModelEvent e) {
                System.out.println(ObjectUtils.writeFields(object));
            }

            @Override
            public void treeNodesInserted(TreeModelEvent e) {
                System.out.println(ObjectUtils.writeFields(object));
            }

            @Override
            public void treeNodesRemoved(TreeModelEvent e) {
                System.out.println(ObjectUtils.writeFields(object));
            }

            @Override
            public void treeStructureChanged(TreeModelEvent e) {
                System.out.println(ObjectUtils.writeFields(object));
            }
        });
        
       
        LmcpObjectActions.addActions(table);

        JFrame frame = WindowUtils.showApplicationWindow(new JScrollPane(table));
        frame.setSize(640, 480);
        
        table.setObject(object);
        
        System.out.println(ObjectTree.showEditWindow(object, null, "test"));
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */