// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.network;

import avtas.util.WindowUtils;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author AFRL/RQQD
 */
public class ServerStatus extends JPanel {

    JLabel connectLabel = new JLabel("<host>:<port>");
    JLabel sentLabel = new JLabel("0");
    JLabel recvdLabel = new JLabel("0");
    JLabel bytesLabel = new JLabel("0");
    JList connectionList = new JList();
    List<String> socketList = new ArrayList<>();
    private int incomingMsgs;
    private int outgoingMsgs;
    int bytes = 0;

    public ServerStatus() {
        
        setLayout(new BorderLayout(5, 5));

        JPanel topPanel = new JPanel(new GridLayout(0, 2));
        topPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        topPanel.add(new JLabel("Connect to"));
        topPanel.add(connectLabel);
        topPanel.add(new JLabel("Msgs Received"));
        topPanel.add(recvdLabel);
        topPanel.add(new JLabel("Msgs Sent"));
        topPanel.add(sentLabel);
        topPanel.add(new JLabel("Bytes Sent"));
        topPanel.add(bytesLabel);

        JPanel connectionsPanel = new JPanel();
        connectionsPanel.setBorder(new TitledBorder("Connections"));
        connectionsPanel.add(new JScrollPane(connectionList));
        add(connectionsPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(connectionsPanel, BorderLayout.CENTER);

    }

    public void setServerInfo(String host, int port) {
        connectLabel.setText(host + ":" + port);
    }

    public void incrementIncomingMsgs() {
        incomingMsgs++;
        recvdLabel.setText(String.valueOf(incomingMsgs));
    }

    public void incrementOutGoingMsgs() {
        outgoingMsgs++;
        sentLabel.setText(String.valueOf(outgoingMsgs));
    }

    public void updateActivity(int bytes) {
        this.bytes += bytes;
        bytesLabel.setText(String.valueOf(bytes));
    }

    public void addSocket(Socket socket) {
        socketList.add(socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
        connectionList.setListData(socketList.toArray());
    }

    public void removeSocket(Socket socket) {
        socketList.remove(socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
        connectionList.setListData(socketList.toArray());
    }

    public static void main(String[] args) {
        WindowUtils.showApplicationWindow(new ServerStatus());
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */