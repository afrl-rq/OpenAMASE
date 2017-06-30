package amase.examples;

import afrl.cmasi.SessionStatus;
import afrl.cmasi.WeatherReport;
import avtas.amase.AmasePlugin;
import java.util.Random;
/**
 * Created by Sriram Sankaranarayanan on 6/21/17.
 */
public class StochasticWindGenerator extends AmasePlugin {
    float lastWindSpeed;
    float lastWindDirection;
    double lastScenarioTime;
    double windStatusDelta;
    Random r;
    boolean debug;


    public StochasticWindGenerator() {
        setPluginName("Stochastic Wind Disturbance Generator");
        this.lastWindDirection = 0.0f;
        this.lastWindSpeed = 0.0f;
        this.lastScenarioTime = 0.0;
        this.windStatusDelta = 1.0; // Update wind status roughly every 0.2 seconds
        /* -- TODO: Seed this RNG from outside. --*/
        r = new Random();
        debug = true;
        System.out.println("Loaded Stochastic Wind Generator Plugin!!");
    }

    @Override
    public void eventOccurred(Object event) {
        if (event instanceof WeatherReport){
            /* As a hack, we will use externally generated weather reports to
               simply have a nominal windSpeed and Direction
             */
            WeatherReport w = (WeatherReport) event;
            this.lastWindSpeed = w.getWindSpeed();
            this.lastWindDirection = w.getWindDirection();
        }

    }

    public void step(double timestep, double sim_time) {
        if (sim_time >= this.lastScenarioTime + this.windStatusDelta){
            this.lastScenarioTime = sim_time;
            generateWindGust();

        }
    }


    public void generateWindGust(){
        /* TODO: Adjust the wind gust model later. */
        /* Current simple wind gust model:
            w(t) = nominalWindSpeed * ( 1 + Gaussian(0, 0.05) )
            theta(t) = thetaNominal + uniform (-5, 5)
         */
        double w1 = 0.05 * r.nextGaussian();
        double w2 = 10.0 * r.nextFloat() - 5.0;
        double gustSpeed = this.lastWindSpeed * (1.0 + w1);
        double gustDirection = this.lastWindDirection + w2;
        /*
            Create a new weather report.
         */
        WeatherReport w = new WeatherReport();
        w.setWindDirection((float) gustDirection);
        w.setWindSpeed((float) gustSpeed);
        if (this.debug)
            System.out.println("StochasticWindGenerator: (direction = " + gustDirection+ " speed = " + gustSpeed + ")");
	/* 
	   Send the event to the simulator 
	*/
        fireEvent(w);
    }
}
