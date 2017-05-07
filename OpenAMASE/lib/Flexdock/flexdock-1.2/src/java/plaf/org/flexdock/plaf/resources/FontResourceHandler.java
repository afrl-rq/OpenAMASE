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

import java.awt.Font;
import java.util.StringTokenizer;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/**
 * @author Christopher Butler
 */
public class FontResourceHandler extends ResourceHandler {
    public static final String BASE_FONT_KEY = "Panel.font";

    public Object getResource(String fontData) {
        if(fontData==null)
            return null;

        String name = null;
        int style = -1;
        int size = -1;

        if(!fontData.endsWith(","))
            fontData += ",";

        StringTokenizer st = new StringTokenizer(fontData, ",");
        for(int i=0; st.hasMoreTokens(); i++) {
            switch(i) {
            case 0:
                name = getFontName(st.nextToken());
                break;
            case 1:
                style = getInt(st.nextToken(), Font.PLAIN);
                break;
            case 2:
                size = getInt(st.nextToken(), 10);
                break;
            }
        }

        FontUIResource defaultFont = (FontUIResource)UIManager.getDefaults().getFont(BASE_FONT_KEY);
        if(name==null)
            name = defaultFont.getName();
        if(style==-1)
            style = defaultFont.getStyle();
        if(size==-1)
            size = defaultFont.getSize();

        return new FontUIResource(name, style, size);

    }

    private String getFontName(String data) {
        data = data==null? null: data.trim();
        return data==null || data.length()==0? null: data;
    }

    private int getInt(String data, int defaultValue) {
        data = data==null? "": data.trim();
        try {
            return Integer.parseInt(data);
        } catch(NumberFormatException e) {
            return defaultValue;
        }
    }
}
