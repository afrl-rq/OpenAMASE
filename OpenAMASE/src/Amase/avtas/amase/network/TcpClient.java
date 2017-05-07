// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.network;

import avtas.amase.AmasePlugin;
import avtas.app.AppEventManager;
import avtas.app.Context;
import avtas.app.StatusPublisher;
import avtas.app.UserExceptions;
import avtas.xml.XMLUtil;
import avtas.lmcp.LMCPFactory;
import avtas.lmcp.LMCPObject;
import avtas.util.WindowUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.Deque;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import avtas.xml.Element;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Connects to an AMASE-style TCP service. This allows an application to act as
 * a client in a network.
 *
 * @author AFRL/RQQD
 */
public class TcpClient extends AmasePlugin {

    SocketThread sockThread = null;
    String host = "localhost";
    int port = 5555;
    private AppEventManager eventMgr = null;
    ServerPanel serverPanel = new ServerPanel();

    public TcpClient() {
        this.eventMgr = AppEventManager.getDefaultEventManager();
        eventMgr.addListener(this);
    }

    public void eventOccurred(Object event) {
        if (event instanceof LMCPObject) {
            if (sockThread != null) {
                sockThread.send((LMCPObject) event);
            }
        }

    }

    @Override
    public void addedToApplication(Context context, Element xml, String[] cmdParams) {
        Element connEl = xml.getChild("Connection");
        if (connEl != null) {
            host = XMLUtil.getAttr(connEl, "Host", null);
            port = XMLUtil.getIntAttr(connEl, "Port", 0);
        }
    }

    @Override
    public void initializeComplete() {
        if (host != null && port != 0) {
            sockThread = new SocketThread(host, port);
            sockThread.start();
        }
    }

    public void incomingMsg(LMCPObject evt) {
        if (eventMgr == null) {
            return;
        }
        if (evt != null) {
            eventMgr.fireEvent(evt, this);
        }

    }

    @Override
    public void getMenus(final JMenuBar menubar) {
        JMenu menu = WindowUtils.getMenu(menubar, "Network");
        JMenuItem serverSelect = new JMenuItem("Select Server");
        menu.add(serverSelect);
        serverSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int ans = JOptionPane.showConfirmDialog(JOptionPane.getFrameForComponent(menubar), serverPanel,
                        "Connect to an LMCP Server", JOptionPane.OK_CANCEL_OPTION);
                if (ans == JOptionPane.OK_OPTION) {
                    if (sockThread != null) {
                        sockThread.stopExec = true;
                        sockThread = new SocketThread(serverPanel.getHostname(), serverPanel.getPort());
                    }
                }
            }
        });
    }

    class SocketThread extends Thread {

        private final String host;
        private final int port;
        private Socket sock = null;
        private boolean stopExec = false;
        Deque<LMCPObject> sendQueue = new ArrayDeque<LMCPObject>();

        public SocketThread(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public void connect() {
            Socket sock = null;
            try {
                sock = new Socket(host, port);
                StatusPublisher.getDefault().setStatus("Connected to " + host + ":" + port);
                this.sock = sock;
            } catch (UnknownHostException ex) {
                UserExceptions.showError(this, "Host Unknown.  Quitting.", ex);
                stopExec = true;
            } catch (IOException ex) {
                StatusPublisher.getDefault().setStatus("Could not connect to " + host + ":" + port + ".  Trying again...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex1) {
                    ex1.printStackTrace();
                }
                if (!stopExec) {
                    connect();
                }
            }
        }

        public void send(LMCPObject o) {
            if (o != null && sock != null && sock.isConnected()) {
                try {
                    byte[] bytes = LMCPFactory.packMessage(o, true);
                    sock.getOutputStream().write(bytes);
                } catch (Exception ex) {
                    StatusPublisher.getDefault().setStatus("Error writing to socket.");
                }
            }
        }

        @Override
        public void run() {
            while (!stopExec) {
                if (sock == null) {
                    connect();
                }
                LMCPObject o = null;
                try {
                    o = LMCPFactory.getObject(sock.getInputStream());
                    if (o != null) {
                        incomingMsg(o);
                    }
                } catch (Exception ex) {
                    StatusPublisher.getDefault().setStatus("Error reading from socket. Trying to reconnect.");
                    connect();
                }

            }
        }
        
        public void shutdown() {
            stopExec = true;
        }
    }

    static class ServerPanel extends JPanel {

        JTextField serverField = new JTextField("localhost", 30);
        JFormattedTextField portField = new JFormattedTextField(new DecimalFormat("#"));

        public ServerPanel() {
            setLayout(new BorderLayout(5, 5));
            setBorder(new EmptyBorder(5, 5, 5, 5));
            JPanel leftpanel = new JPanel(new GridLayout(0, 1, 5, 5));
            leftpanel.add(new JLabel("Server"));
            leftpanel.add(serverField);
            add(leftpanel, BorderLayout.WEST);

            JPanel rightpanel = new JPanel(new GridLayout(0, 1, 5, 5));
            rightpanel.add(new JLabel("Port"));
            rightpanel.add(portField);
            add(rightpanel, BorderLayout.EAST);

            portField.setValue(5555);
            portField.setColumns(6);
        }

        public String getHostname() {
            return serverField.getText();
        }

        public int getPort() {
            return ((Number) portField.getValue()).intValue();
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */