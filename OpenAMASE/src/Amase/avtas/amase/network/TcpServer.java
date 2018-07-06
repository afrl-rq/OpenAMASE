// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.network;

import afrl.cmasi.AutomationResponse;
import afrl.cmasi.MissionCommand;
import afrl.cmasi.VehicleActionCommand;
import avtas.amase.AmasePlugin;
import avtas.xml.XMLUtil;
import avtas.lmcp.LMCPObject;
import avtas.app.AppEventManager;
import avtas.app.Context;
import avtas.app.StatusPublisher;
import avtas.app.UserExceptions;
import avtas.lmcp.LMCPFactory;
import avtas.util.WindowUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import avtas.xml.Element;
import java.util.ArrayList;
import java.util.ListIterator;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import org.omg.CORBA.UserException;

/**
 *
 * @author AFRL/RQQD
 */
public class TcpServer extends AmasePlugin {

    protected AppEventManager dataManager = null;
    protected static int default_port = 5555;
    //private ServerPanel serverPanel = new ServerPanel(server);
    protected ServerStatus statusPanel = new ServerStatus();
    protected ArrayList<SocketThread> socketList = new ArrayList<>();
    protected ServerSocket serverSocket;
    protected int port = default_port;

    public TcpServer() {
        this.dataManager = AppEventManager.getDefaultEventManager();
    }

    public TcpServer(int port) {
        this.port = port;
        initializeServer();
    }

    protected void initializeServer() {
        try {
            serverSocket = new ServerSocket(port);
            statusPanel.setServerInfo(serverSocket.getInetAddress().getHostAddress(), serverSocket.getLocalPort());
            new Thread() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            SocketThread t = new SocketThread(serverSocket.accept());
                            t.start();
                            socketList.add(t);
                        }
                    } catch (Exception ex) {
                        //UserExceptions.showWarning("Socket closed for clients on " + port);
                    }
                }
            }.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void addedToApplication(Context context, Element xml, String[] cmdParams) {
        xml = XMLUtil.getChild(xml, "TCPServer");
        port = XMLUtil.getIntAttr(xml, "Port", default_port);

    }

    @Override
    public void initializeComplete() {
        initializeServer();
    }

    @Override
    public void eventOccurred(Object evt) {
        if (evt instanceof LMCPObject) {
            sendMessage((LMCPObject) evt);
        }
    }

    /**
     * sends data to sockets that are connected other than the socket which
     * produced the data.
     */
    protected void sendToOthers(LMCPObject o, SocketThread src) {

        for (ListIterator<SocketThread> it = socketList.listIterator(); it.hasNext();) {
            SocketThread s = it.next();
            if (!s.isRunning()) {
                it.remove();
            }
            if (s != src) {
                s.sendMessage(o);
            }
        }
    }

    /**
     * Called when an LMCP object is read from a socket.
     *
     * @param o object read in
     * @param fromSocket socket from which the object was read
     */
    protected void messageReceived(LMCPObject o, SocketThread fromSocket) {
        if (o instanceof AutomationResponse) {
            AutomationResponse ar = (AutomationResponse) o;
            for (MissionCommand mc : ar.getMissionCommandList()) {
                dataManager.fireEvent(mc, TcpServer.this);
            }
            for (VehicleActionCommand vac : ar.getVehicleCommandList()) {
                dataManager.fireEvent(vac, TcpServer.this);
            }
            dataManager.fireEvent(o, TcpServer.this);
        } else if (o != null) {
            dataManager.fireEvent(o, TcpServer.this);
        }
        
        sendToOthers(o, fromSocket);
        statusPanel.incrementIncomingMsgs();
    }

    /**
     * Sends an LMCP object to all of the connected clients.
     */
    protected void sendMessage(LMCPObject obj) {
        if (!socketList.isEmpty()) {
            for (SocketThread s : socketList) {
                s.sendMessage(obj);
            }
        }
    }

    @Override
    public void shutdown() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
                for (SocketThread socket : socketList) {
                    socket.setRunning(false);
                    socket.socket.close();
                }
            }
        } catch (IOException ex) {
            //Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public class SocketThread extends Thread {

        private Socket socket;
        private boolean running = true;

        public SocketThread(Socket socket) {
            this.socket = socket;
            statusPanel.addSocket(socket);
        }

        public void run() {
            try {
                while (isRunning()) {
                    byte[] bytes = LMCPFactory.getMessageBytes(getSocket().getInputStream());
                    avtas.lmcp.LMCPObject o = LMCPFactory.getObject(bytes);

                    messageReceived(o, this);
                }
            } catch (Exception ex) {
                setRunning(false);
                try {
                    socket.close();
                } catch (IOException ex1) {
                    Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex1);
                }
                statusPanel.removeSocket(socket);
            }
        }

        public boolean sendMessage(LMCPObject o) {

            try {
                byte[] bytes = LMCPFactory.packMessage(o, true);
                try {
                    statusPanel.incrementOutGoingMsgs();
                    statusPanel.updateActivity(bytes.length);
                    socket.getOutputStream().write(bytes);
                    return true;
                } catch (IOException ex) {
                    setRunning(false);
                    try {
                        socket.close();
                    } catch (IOException ex1) {
                    }
                    statusPanel.removeSocket(socket);
                    return false;
                }
            } catch (Exception ex) {
                Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }

        }

        public boolean isRunning() {
            return running;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        public Socket getSocket() {
            return socket;
        }
    }

    @Override
    public void getMenus(final JMenuBar menubar) {
        JMenu serverMenu = WindowUtils.getMenu(menubar, "Server");
        JMenuItem item = new JMenuItem("Show Server Status");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame f = new JFrame("Server Settings");
                f.add(statusPanel);
                f.pack();
                f.setLocationRelativeTo(JOptionPane.getFrameForComponent(menubar));
                f.setVisible(true);
            }
        });
        serverMenu.add(item);
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */