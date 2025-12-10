// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package test;

import avtas.lmcp.LMCPFactory;
import avtas.lmcp.LMCPObject;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Sets up a server socket and reads incoming LMCP messages
 */
public class TestServer {
    
    private static int port = 11041;

    public static void main(String[] args) {
        try {
            ServerSocket socket = new ServerSocket(port);

            while(true) {
                Socket s = socket.accept();
                System.out.println("Connected to: " + s.getInetAddress().getHostName());
                while (true) {
                    try {
                        LMCPObject o = LMCPFactory.getObject(s.getInputStream());
                        System.out.println(o.toString());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        s.close();
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(TestServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
