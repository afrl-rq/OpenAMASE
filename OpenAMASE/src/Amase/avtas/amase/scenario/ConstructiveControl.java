/*
 * This software was developed by AFRL/RBCD, a U. S. Government Agency<br/>
 * For controlled distribution contact:<br/>
 * 
 * Air Vehicles Technology Assessment and Simulation Branch (AFRL/RBCD)
 * 2180 8th Street<br/>
 * WPAFB, Ohio 45433<br/>
 * <br/>
 * THIS IS GOVERNMENT OWNED SOFTWARE. The User shall not use or permit use of this 
 * software for profit or in any manner offer it for sale. Software shall not be 
 * disclosed or transferred to any activity or firm without the prior written 
 * approval from the government assigned agency, currently AFRL/RBCD.<br/>
 * <br/>
 * This software is distributed WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  In no event shall the contributors or the
 * Government be liable for any loss or for any indirect, special,
 * punitive, exemplary, incidental, or consequential damages arising from the 
 * use, possession or performance of this software.<br/>
 */
package avtas.amase.scenario;

import avtas.app.Context;
import avtas.amase.analysis.AnalysisManager;
import afrl.cmasi.SessionStatus;
import afrl.cmasi.SimulationStatusType;
import avtas.amase.AmasePlugin;
import avtas.amase.util.SimTimer;
import java.io.File;
import java.io.PrintStream;
import avtas.xml.Element;
import avtas.xml.XMLUtil;
import avtas.xml.XmlWriter;

/**
 *
 * @author Matt Duquette (AFRL/RBCD)
 */
public class ConstructiveControl extends AmasePlugin {

    protected PrintStream outStream = System.out;
    protected long startTime;
    protected ScenarioOutput dataOutput;

    protected AnalysisManager analysisMgr;
    public double scenarioTime;
    protected File scenarioFile = null;
    protected File analysisFile = null;
    protected File outputFile = null;
    public static String optionStr;
    
    protected double sim_rate = 100000;

    static {
        StringBuffer buf = new StringBuffer();
        buf.append("\nOptions:\n\n");
        buf.append("--scenario <Path to Scenario File> Scenario file.\n");
        buf.append("--output <Path to Output File> File to save message output.\n");
        buf.append("--analysis <Path to Analysis File> File to save analysis results.\n");
        buf.append("--help  Displays this help.\n");
        buf.append("--sim_rate <rate to run sim as multiple of wallclock time>\n");
        optionStr = buf.toString();
    }
    

    public ConstructiveControl() {
    }

    public void initScenario(ScenarioEvent evt) {
        outStream.println("initializing sim.");
        scenarioTime = XMLUtil.getDouble(evt.getXML(), "ScenarioData/ScenarioDuration",
                Double.MAX_VALUE);
        startTime = System.currentTimeMillis();
    }

    public void runSim(File scenarioFile, File outputFile, File analysisFile) {
        this.scenarioFile = scenarioFile;
        this.analysisFile = analysisFile;
        this.outputFile = outputFile;

        if (scenarioFile == null) {
            outStream.print("must specify a scenario file\n");
            exit();
        }
        // set realtime multiplier to something big
        SimTimer.setRealtimeMultiple(sim_rate);

        SimTimer.go();

    }

    @Override
    public void addedToApplication(Context context, Element xml, String[] cmdParams) {
        initialize(xml, cmdParams);
    }

    @Override
    public void initializeComplete() {

        runSim(scenarioFile, outputFile, analysisFile);

    }

    @Override
    public void applicationPeerAdded(Object peer) {
        if (peer instanceof ScenarioOutput) {
            this.dataOutput = (ScenarioOutput) peer;
        } else if (peer instanceof AnalysisManager) {
            this.analysisMgr = (AnalysisManager) peer;
        }
    }

    @Override
    public void eventOccurred(Object evt) {
        if (evt instanceof ScenarioEvent) {
            initScenario((ScenarioEvent) evt);
            return;
        } else if (evt instanceof SessionStatus) {
            SessionStatus ss = (SessionStatus) evt;
            if (ss.getState() == SimulationStatusType.Stopped) {
                outStream.println("Duration: " + (System.currentTimeMillis() - startTime) / 1E3 + " seconds ");
                if (outputFile != null) {
                    outStream.print("saving data to: " + outputFile.getPath());
                    dataOutput.save(outputFile, false);
                    outStream.println(" Done");
                }
                if (analysisFile != null) {
                    outStream.print("saving analysis to: " + analysisFile.getPath());
                    XmlWriter.writeToFile(analysisFile, analysisMgr.getAnalysisReportXML());
                    outStream.println(" Done");

                }
                exit();

            } else if (ss.getState() == SimulationStatusType.Paused) {
                outStream.println("awaiting run state...");
            } else if (ss.getState() == SimulationStatusType.Reset) {
                outStream.println("resetting.");
            } else if (ss.getState() == SimulationStatusType.Running) {
                outStream.println(ss.getScenarioTime());
            }

        }

    }

    public void exit() {
        outStream.println("execution complete.");
        System.exit(0);
    }

    public void initialize(Element node, String[] args) {

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--output") && args.length > i) {
                outputFile = new File(args[++i]);
            } else if (args[i].equals("--analysis") && args.length > i) {
                analysisFile = new File(args[++i]);
            } else if (args[i].equals("--scenario") && args.length > i) {
                scenarioFile = new File(args[++i]);
            } else if (args[i].equals("--help")) {
                outStream.print(optionStr);
                return;
            } else if (args[i].equals("--sim_rate") && args.length > i) {
                this.sim_rate = Double.parseDouble(args[++i]);
            }
        }
    }

}
