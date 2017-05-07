// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.terrain;

import avtas.amase.AmasePlugin;
import avtas.app.SettingsManager;
import avtas.app.Context;
import avtas.xml.Element;
import avtas.xml.XMLUtil;
import java.io.File;
import java.util.List;

/**
 * Configures the static TerrainService using the simulation configuration file.
 * Configurations set by this class affect all users of the TerrainService in
 * the application.
 *
 * This class expects a "DTED" entry in the configuration file with one or more
 * "Directory" nodes as children. The text under each must be a relative or
 * absolute path to the directory containing DTED data. Data must be structured
 * according to DTED conventions( i.e. folders for lines of longitude containing
 * files named for latitude. See MIL-PRF-89020B for details.
 *
 * @author AFRL/RQQD
 */
public class TerrainConfigurator extends AmasePlugin {

    public TerrainConfigurator() {
        super("Terrain Service Configurator");
    }

    @Override
    public void addedToApplication(Context context, Element xml, String[] cmdParams) {
        List<Element> els = XMLUtil.getChildren(xml, "DTED/Directory");
        if (els.isEmpty()) {
            Element el = SettingsManager.getAsXml("TerrainService.xml");
            els = XMLUtil.getChildren(el, "DTED/Directory");
        }

        for (Element dirEl : els) {
            File dir = new File(dirEl.getText());
            if (dir.isDirectory()) {
                TerrainService.addDirectory(dir);
            }
        }

    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */