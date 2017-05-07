// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on 23.03.2005
 */
package org.flexdock.plaf.resources;

import java.awt.Insets;




/**
 * @author Claudio Romano
 */
public class InsetsResourceHandler extends ResourceHandler {

    public Object getResource(String data) {
//      pattern should be "top, left, bottom, right"
        String[] args = getArgs(data);
        int top = getInt(args, 0);
        int left = getInt(args, 1);
        int bottom = getInt(args, 2);
        int right = getInt(args, 3);


        return new Insets(top, left, bottom, right);
    }

    private int getInt(String args[], int index) {
        return args.length>index? getInt(args[index]): 0;
    }

    private int getInt(String data) {
        try {
            return Integer.parseInt(data);
        } catch(Exception e) {
            System.err.println("Exception: " +e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}
