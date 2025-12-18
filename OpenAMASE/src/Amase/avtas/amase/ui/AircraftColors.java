// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.ui;

import avtas.amase.AmasePlugin;
import avtas.app.SettingsManager;
import avtas.amase.scenario.ScenarioState;
import avtas.util.Colors;
import avtas.properties.XmlSerializer;
import avtas.xml.Element;
import avtas.xml.XMLUtil;
import java.awt.Color;
import java.util.ArrayList;

import java.util.List;

/**
 * A static reference for colors used to represent aircraft in a simulation.
 * This is configurable through the "AircraftColors.xml" file in the
 * application's config directory.
 *
 * @author AFRL/RQQD
 */
public class AircraftColors {

    static final List<Color> colorList = new ArrayList<>();
    static Color defaultColor = Color.WHITE;

    static {

        Element top = SettingsManager.getAsXml("AircraftColors.xml");
        if (top != null) {
            Element el = top.getChild("ColorList");
            if (el != null) {
                colorList.clear();
                List<Element> colorElList = el.getChildren("Color");
                for (Element colorEl : colorElList) {
                    String colorStr = colorEl.getText();
                    if (colorStr != null) {
                        Color color = Colors.getColor(colorStr, defaultColor);
                        colorList.add(color);
                    }

                }
            }
            defaultColor = Colors.getColor(XMLUtil.getValue(top, "DefaultColor", ""), defaultColor);
        }
        else {
            colorList.add((Color.CYAN));
            colorList.add((Color.MAGENTA));
            colorList.add((new Color(Integer.decode("#99FF66"))));  // Yellow-Green
            colorList.add((Color.ORANGE));
            colorList.add((Color.PINK));
            colorList.add((Color.BLUE));
            colorList.add((new Color(128, 0, 128)));  //purple
            colorList.add((new Color(0, 255, 255)));  //aqua
        }
    }

    /**
     * Returns the color that is associated with a given aircraft, based on
     * order of creation in the simulation. Colors are repeated once the end of
     * the list is reached.
     *
     * @param id
     * @return color for given id.
     */
    public static Color getColor(long id) {
        int index = ScenarioState.getAirVehicleOrder().indexOf(id);
        if (index == -1) {
            return defaultColor;
        }
        index = index % colorList.size();
        Color color = colorList.get(index);
        return (color == null ? defaultColor : color);
    }

    public static Color getDefaultColor() {
        return defaultColor;
    }

    public static void saveColorInfo() {
        Element el = new Element("AircraftColors");
        el.addElement("DefaultColor").setText(XmlSerializer.toString(defaultColor));
        Element listEl = el.addElement("ColorList");
        for (Color c : colorList) {
            listEl.addElement("Color").setText(Colors.getLabel(c));
        }
        SettingsManager.setFileData(el.toXML().getBytes(), "AircraftColors.xml");
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */