// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.amase.scenario;

import avtas.xml.XMLUtil;
import java.io.File;
import java.io.IOException;
import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import avtas.app.UserExceptions;
import avtas.amase.AmasePlugin;
import avtas.util.WindowUtils;
import avtas.swing.UserNotice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;
import avtas.xml.Element;
import java.awt.Frame;
import java.util.List;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author AFRL/RQQD
 */
public class ScenarioOutput extends AmasePlugin {

    private double secSimTime = 0;
    private double secStartTime = 0;
    private Element scenData = null;
    private JFileChooser fileSaver;
    UserNotice userNotice = null;

    public ScenarioOutput() {
        fileSaver = WindowUtils.getFilteredChooser("xml",
                new FileNameExtensionFilter("ZIP File", "zip", "ZIP"), new FileNameExtensionFilter("XML File", "xml", "XML"));
    }

    public void initScenario(ScenarioEvent evt) {
        try {
            secSimTime = 0.0;
            scenData = XMLUtil.getChild(evt.getXML(), "ScenarioData");
            if (scenData == null) {
                scenData = new Element("ScenarioData");
            }
        } catch (Exception ex) {
            ex.printStackTrace(); System.exit(1);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void eventOccurred(Object evt) {
        if (evt instanceof ScenarioEvent) {
            initScenario((ScenarioEvent) evt);
            return;
        }
        if (evt instanceof SessionStatus) {
            SessionStatus ss = (SessionStatus) evt;
            if (ss.getState() == SimulationStatusType.Reset) {
                secStartTime = ss.getScenarioTime() / 1000d;
                secSimTime = secStartTime;
            }
        }
//        else if (evt instanceof LMCPObject) {
//            LMCPObject o = (LMCPObject) evt;
//            objList.add(new ItemStore(o, secSimTime));
//        }

    }

    public void save(File fileout, boolean asZip) {

        try {
            OutputStream out = new FileOutputStream(fileout);
            if (asZip) {
                ZipOutputStream zout = new ZipOutputStream(out);
                zout.putNextEntry(new ZipEntry("scenario.xml"));
                out = zout;
            }
            OutputStreamWriter outWriter = new OutputStreamWriter(out);
            outWriter.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            outWriter.append("<AMASE>\n");

            Element duration = null;
            if (scenData == null) {
                scenData = new Element("ScenarioData");
            }
            if (scenData != null) {
                duration = XMLUtil.getChild(scenData, "ScenarioDuration");
            }
            if (duration == null) {
                duration = new Element("ScenarioDuration");
                scenData.add(duration);
            }
            duration.setText(String.valueOf(secSimTime - secStartTime));
            
            Element startTimeEl = scenData.getChild("ScenarioStartTime");
            if (startTimeEl == null) {
                startTimeEl = new Element("ScenarioStartTime");
            }
            startTimeEl.setText(String.valueOf(secStartTime));

            outWriter.append(scenData.toXML() + "\n");
            outWriter.append("<ScenarioEventList>\n");

            List<ScenarioState.EventWrapper> itemList = ScenarioState.getEventList();

            if (userNotice != null) {
                userNotice.setText("Saving event 0 of " + itemList.size());
            }

            int i = 0;
            for (ScenarioState.EventWrapper item : itemList) {
                i++;
                if (item.event != null) {
                    if (userNotice != null) {
                        userNotice.setText("Saving event " + i + " of " + itemList.size());
                    }
                    String str = item.event.toXML("  ");
                    str = str.replaceFirst(">", " Time=\"" + item.time + "\">");
                    try {
                        outWriter.write(str + "\n");
                    } catch (IOException ex) {
                        ex.printStackTrace(); System.exit(1);
                    }
                }
            }

            outWriter.append("</ScenarioEventList>\n");
            outWriter.append("</AMASE>");
            outWriter.flush();
            outWriter.close();

        } catch (Exception e) {
            if (userNotice != null) {
                userNotice.dispose();
            }
            UserExceptions.showError(this, "Error saving scenario data", e);
        }
    }

    public void step(double timestep_sec, double simTime_sec) {
        this.secSimTime = simTime_sec;
    }

    @Override
    public void getMenus(JMenuBar menubar) {

        final Frame parentFrame = JOptionPane.getFrameForComponent(menubar);
        JMenuItem item = new JMenuItem("Save Data Output");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int ans = fileSaver.showSaveDialog(parentFrame);
                if (ans != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                final File f = fileSaver.getSelectedFile();
                if (f != null) {

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if (userNotice != null) {
                                userNotice.dispose();
                            }
                            userNotice = new UserNotice("Saving Scenario.       ", parentFrame);
                            userNotice.setVisible(true);

                            new Thread() {
                                @Override
                                public void run() {
                                    save(f, f.getName().toLowerCase().endsWith(".zip"));
                                    userNotice.setVisible(false);
                                }
                            }.start();

                            
                        }
                    });
                }


            }
        });
        JMenu menu = WindowUtils.getMenu(menubar, "File");
        menu.add(item);
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */