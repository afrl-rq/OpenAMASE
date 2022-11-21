// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

package avtas.amase.network;

import uxas.messages.task.TaskActive;
import afrl.cmasi.AutomationResponse;
import afrl.cmasi.MissionCommand;
import afrl.cmasi.VehicleActionCommand;
import avtas.amase.entity.EntityModel;
import avtas.amase.entity.EntityModule;
import avtas.lmcp.LMCPFactory;
import avtas.lmcp.LMCPObject;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EntityTcpServer {

    private final int port;
    private final EntityModel entity;

    private final ServerSocket server;
    private final Set<SocketThread> sockets;

    public EntityTcpServer(final EntityModel entity, final int port) throws IOException {
        super();

        this.entity = entity;
        this.port = port;
        this.server = new ServerSocket(port, 10000);
        this.server.setReceiveBufferSize(100000000);
        this.sockets = new HashSet<>();

        new Thread() {
            @Override
            public void run() {
                try {
                    for (;;) {
                        SocketThread socketThread = new SocketThread(server.accept());
                        socketThread.start();
                        sockets.add(socketThread);
                    }
                } catch (Exception ex) {
                }
            }
        }.start();
    }

    public void dispose() {
        for (SocketThread socket : sockets) {
            socket.dispose();
        }

        try {
            server.close();
        } catch (IOException ex) {
            Logger.getLogger(EntityTcpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMessage(LMCPObject lmcp) {
        if (sockets.isEmpty()) {
            return;
        }

        try {
            byte[] bytes = LMCPFactory.packMessage(lmcp, true);
            for (SocketThread socket : sockets) {
                socket.sendMessage(bytes);
                //System.out.println("EntityTcpServer::sendMessage lmcp.getLMCPTypeName()[" + lmcp.getLMCPTypeName() + "]");
            }

        } catch (Exception ex) {
            Logger.getLogger(EntityTcpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void messageReceived(LMCPObject lmcp) {

        if (lmcp instanceof AutomationResponse) {
            processAutomationResponse((AutomationResponse) lmcp);
        } else {
            entity.getEventManger().fireEvent(lmcp);
        }
    }

    private void processAutomationResponse(AutomationResponse automationResponse) {
        for (MissionCommand mc : automationResponse.getMissionCommandList()) {
            entity.getEventManger().fireEvent(mc);
        }

        for (VehicleActionCommand vac : automationResponse.getVehicleCommandList()) {
            entity.getEventManger().fireEvent(vac);
        }

        entity.getEventManger().fireEvent(automationResponse);
    }

    public class SocketThread extends Thread {

        private final Socket socket;
        private boolean isRunning = true;

        public SocketThread(final Socket socket) {
            super();

            this.socket = socket;
        }

        public void dispose() {
            isRunning = false;
        }

        @Override
        public void run() {

            while (isRunning) {
                try {
                    //TODO: The Java Socket types is NOT thread safe and there's no protection here for concurrent read/write
                    // to the socket!
                    byte[] bytes = LMCPFactory.getMessageBytes(socket.getInputStream());
                    if (bytes.length > 0) {
                        avtas.lmcp.LMCPObject lmcpObject = LMCPFactory.getObject(bytes);
                        messageReceived(lmcpObject);
                    }
                } catch (Exception ex) {
                    //setRunning(false);
                    //try {
                    System.out.println("Exception during run. [" + ex.getMessage() + "]");
                    ex.printStackTrace();
                    //socket.close();
                    //} catch (IOException ex1) {
                    //    Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex1);
                    //}
                    //statusPanel.removeSocket(socket);
                }
            }
        }

        public boolean sendMessage(byte[] bytes) {
            try {
                socket.getOutputStream().write(bytes);
                return true;
            } catch (IOException ex) {
//        isRunning = false;
//        try {
                System.out.println("Exception during send. [" + ex.getMessage() + "]");
                ex.printStackTrace();
//          socket.close();
//        } catch (IOException ex1) {
//        }
                return false;
            }
        }
    }
}
