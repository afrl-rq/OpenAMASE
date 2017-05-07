// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on Feb 27, 2005
 */
package org.flexdock.plaf.resources;

import javax.swing.plaf.ColorUIResource;



import org.flexdock.plaf.Configurator;

/**
 * @author Christopher Butler
 */
public class ColorResourceHandler extends ResourceHandler {

    public Object getResource(String stringValue) {
        return parseHexColor(stringValue);
    }

    public static ColorUIResource parseHexColor(String hexColor) {
        if(Configurator.isNull(hexColor))
            return null;

        StringBuffer sb = new StringBuffer(6);
        int len = hexColor.length();

        // strip out non-hex characters
        for(int i=0; i<len; i++) {
            char c = hexColor.charAt(i);
            if(isHex(c))
                sb.append(c);
        }

        try {
            int color = Integer.parseInt(sb.toString(), 16);
            return new ColorUIResource(color);
        } catch(NumberFormatException e) {
            System.err.println("Exception: " +e.getMessage());
            return null;
        }
    }

    private static boolean isHex(char c) {
        return c=='1' || c=='2' || c=='3' || c=='4' || c=='5' || c=='6' || c=='7' || c=='8' ||
               c=='9' || c=='0' || c=='A' || c=='B' || c=='C' || c=='D' || c=='E' || c=='F' ||
               c=='a' || c=='b' || c=='c' || c=='d' || c=='e' || c=='f';
    }
}
