// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.network;

import avtas.app.UserExceptions;
import avtas.amase.AmasePlugin;
import avtas.app.Context;
import avtas.lmcp.LMCPFactory;
import avtas.lmcp.LMCPObject;
import avtas.xml.Element;
import avtas.xml.XMLUtil;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements a UDP plugin by opening a UDP port for each connection and sending
 * LMCP data at the rate specified by the SimTimer.
 *
 * @author AFRL/RQQD
 */
public class UdpSender extends AmasePlugin {

    private List<UDPConnection> connectionList = new ArrayList<UDPConnection>();
    private DatagramSocket socket;

    public UdpSender() {
        setPluginName("UDP Data Sender");
        try {
            socket = new DatagramSocket();
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }

    public void sendMessage(LMCPObject state) {
        if (connectionList.isEmpty()) {
            return;
        }
        try {
            byte[] buf = LMCPFactory.packMessage(state, false);
            for (UDPConnection conn : connectionList) {
                DatagramPacket p = new DatagramPacket(buf, buf.length);
                p.setPort(conn.portNumber);
                p.setAddress(conn.address);
                socket.send(p);
            }

        } catch (Exception e) {
            UserExceptions.showError(this, "UDP Send Error", e);
        }
    }

    public void eventOccurred(Object evt) {
        if (evt instanceof LMCPObject) {
            sendMessage((LMCPObject) evt);
        } 
    }

    @Override
    public void addedToApplication(Context context, Element xml, String[] cmdParams) {
        xml = XMLUtil.getChild(xml, "UDPAddressList");
        if (xml != null) {
            List<Element> children = XMLUtil.getChildren(xml, "Connection");
            for (Element n : children) {
                try {
                    String addrStr = XMLUtil.getAttr(n, "Address", "");
                    InetAddress addr = InetAddress.getByName(addrStr);
                    int port = XMLUtil.getIntAttr(n, "Port", 0);
                    UDPConnection c = new UDPConnection(addr, port);
                    connectionList.add(c);
                } catch (UnknownHostException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    class UDPConnection {

        InetAddress address;
        int portNumber;

        public UDPConnection(InetAddress address, int portNumber) {
            this.address = address;
            this.portNumber = portNumber;
        }
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */