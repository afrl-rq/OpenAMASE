// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================
package avtas.amase.entity.modules;

import afrl.cmasi.GimbalAngleAction;
import afrl.cmasi.MissionCommand;
import avtas.amase.network.EntityTcpServer;
import avtas.amase.entity.EntityModule;
import avtas.amase.scenario.ScenarioEvent;
import avtas.app.AppEventListener;
import avtas.app.AppEventManager;
import avtas.lmcp.LMCPObject;
import avtas.xml.Element;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import afrl.cmasi.AirVehicleState;
import afrl.cmasi.GimbalAngleAction;

public class EntityNetworkModule extends EntityModule implements AppEventListener {

    private int tcpPort;
    private EntityTcpServer entityServer = null;

    public EntityNetworkModule() {
        super();

        AppEventManager.getDefaultEventManager().addListener(this);
    }

    @Override
    public void initialize(Element xmlElement) {
        for (Element child : xmlElement.getChildElements()) {
            if (Long.parseLong(child.getAttribute("Id")) == getModel().getID()) {
                this.tcpPort = Integer.parseInt(child.getAttribute("Port"));
                try {
                    entityServer = new EntityTcpServer(getModel(), this.tcpPort);
                } catch (IOException ex) {
                    Logger.getLogger(EntityNetworkModule.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }
        }
    }

    @Override
    public void modelEventOccurred(Object event) {
        if (event instanceof GimbalAngleAction) {  //// getting swamped by GimbalAngleAction
        } else if (event instanceof MissionCommand) {  //dont send mission commands back
        } else if (event instanceof LMCPObject) {
            if (entityServer != null) {
                entityServer.sendMessage((LMCPObject) event);
            }
        }
    }

    private void processScenarioEvent(ScenarioEvent scenarioEvent) {
        if (entityServer != null) {
            entityServer.dispose();
        }
    }

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof ScenarioEvent) {
            processScenarioEvent((ScenarioEvent) event);
        }
    }
}
