// ===============================================================================
// Authors: AFRL/RQQA
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

// This file was auto-created by LmcpGen. Modifications will be overwritten.

package afrl.cmasi;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import avtas.lmcp.*;

/**
 Reports current weather conditions.  A weather report can cover a given geographic area or             the entire scenario.  Multiple reports can be issued during a scenario to indicate             varying weather conditions over time or space. Mission planners can use weather reports             to plan or can read current wind conditions as reported by aircraft as part of             {@link AirVehicleState}.        
*/
public class WeatherReport extends avtas.lmcp.LMCPObject {
    
    public static final int LMCP_TYPE = 55;

    public static final String SERIES_NAME = "CMASI";
    /** Series Name turned into a long for quick comparisons. */
    public static final long SERIES_NAME_ID = 4849604199710720000L;
    public static final int SERIES_VERSION = 3;


    private static final String TYPE_NAME = "WeatherReport";

    private static final String FULL_LMCP_TYPE_NAME = "afrl.cmasi.WeatherReport";

    /**  The area for which this report pertains. If this is null, then the report pertains to the entire mission area. (Units: None)*/
    @LmcpType("AbstractZone")
    protected afrl.cmasi.AbstractZone Area = null;
    /**  Windspeed value for this area (Units: meter/sec)*/
    @LmcpType("real32")
    protected float WindSpeed = (float)0;
    /**  Wind direction. Direction is the true heading from which the wind is blowing. (Units: degree)*/
    @LmcpType("real32")
    protected float WindDirection = (float)0;
    /**  Visibility, according to the <a href="http://en.wikipedia.org/wiki/Visibility">ICAO definition</a>. (Units: meter)*/
    @LmcpType("real32")
    protected float Visibility = (float)0;
    /**  Height of the bottom of a cloud layer, in MSL altitude. If there is more than one cloud layer, create WeatherReports for each zone that contains a cloud layer. A value of "0" denotes free-of-clouds (Units: meter)*/
    @LmcpType("real32")
    protected float CloudCeiling = (float)0;
    /**  Amount of cloud coverage for the given cloud layer. Values should be 0..1, 0 denoting free-of-clouds, and 1 denoting overcast. (Units: None)*/
    @LmcpType("real32")
    protected float CloudCoverage = (float)0;

    
    public WeatherReport() {
    }

    public WeatherReport(afrl.cmasi.AbstractZone Area, float WindSpeed, float WindDirection, float Visibility, float CloudCeiling, float CloudCoverage){
        this.Area = Area;
        this.WindSpeed = WindSpeed;
        this.WindDirection = WindDirection;
        this.Visibility = Visibility;
        this.CloudCeiling = CloudCeiling;
        this.CloudCoverage = CloudCoverage;
    }


    public WeatherReport clone() {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream( calcSize() );
            pack(bos);
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            WeatherReport newObj = new WeatherReport();
            newObj.unpack(bis);
            return newObj;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
     
    /**  The area for which this report pertains. If this is null, then the report pertains to the entire mission area. (Units: None)*/
    public afrl.cmasi.AbstractZone getArea() { return Area; }

    /**  The area for which this report pertains. If this is null, then the report pertains to the entire mission area. (Units: None)*/
    public WeatherReport setArea( afrl.cmasi.AbstractZone val ) {
        Area = val;
        return this;
    }

    /**  Windspeed value for this area (Units: meter/sec)*/
    public float getWindSpeed() { return WindSpeed; }

    /**  Windspeed value for this area (Units: meter/sec)*/
    public WeatherReport setWindSpeed( float val ) {
        WindSpeed = val;
        return this;
    }

    /**  Wind direction. Direction is the true heading from which the wind is blowing. (Units: degree)*/
    public float getWindDirection() { return WindDirection; }

    /**  Wind direction. Direction is the true heading from which the wind is blowing. (Units: degree)*/
    public WeatherReport setWindDirection( float val ) {
        WindDirection = val;
        return this;
    }

    /**  Visibility, according to the <a href="http://en.wikipedia.org/wiki/Visibility">ICAO definition</a>. (Units: meter)*/
    public float getVisibility() { return Visibility; }

    /**  Visibility, according to the <a href="http://en.wikipedia.org/wiki/Visibility">ICAO definition</a>. (Units: meter)*/
    public WeatherReport setVisibility( float val ) {
        Visibility = val;
        return this;
    }

    /**  Height of the bottom of a cloud layer, in MSL altitude. If there is more than one cloud layer, create WeatherReports for each zone that contains a cloud layer. A value of "0" denotes free-of-clouds (Units: meter)*/
    public float getCloudCeiling() { return CloudCeiling; }

    /**  Height of the bottom of a cloud layer, in MSL altitude. If there is more than one cloud layer, create WeatherReports for each zone that contains a cloud layer. A value of "0" denotes free-of-clouds (Units: meter)*/
    public WeatherReport setCloudCeiling( float val ) {
        CloudCeiling = val;
        return this;
    }

    /**  Amount of cloud coverage for the given cloud layer. Values should be 0..1, 0 denoting free-of-clouds, and 1 denoting overcast. (Units: None)*/
    public float getCloudCoverage() { return CloudCoverage; }

    /**  Amount of cloud coverage for the given cloud layer. Values should be 0..1, 0 denoting free-of-clouds, and 1 denoting overcast. (Units: None)*/
    public WeatherReport setCloudCoverage( float val ) {
        CloudCoverage = val;
        return this;
    }



    public int calcSize() {
        int size = super.calcSize();  
        size += 20; // accounts for primitive types
        size += LMCPUtil.sizeOf(Area);

        return size;
    }

    public void unpack(InputStream in) throws IOException {
            Area = (afrl.cmasi.AbstractZone) LMCPUtil.getObject(in);
        WindSpeed = LMCPUtil.getReal32(in);

        WindDirection = LMCPUtil.getReal32(in);

        Visibility = LMCPUtil.getReal32(in);

        CloudCeiling = LMCPUtil.getReal32(in);

        CloudCoverage = LMCPUtil.getReal32(in);


    }

    public void pack(OutputStream out) throws IOException {
        LMCPUtil.putObject(out, Area);
        LMCPUtil.putReal32(out, WindSpeed);
        LMCPUtil.putReal32(out, WindDirection);
        LMCPUtil.putReal32(out, Visibility);
        LMCPUtil.putReal32(out, CloudCeiling);
        LMCPUtil.putReal32(out, CloudCoverage);

    }

    public int getLMCPType() { return LMCP_TYPE; }

    public String getLMCPSeriesName() { return SERIES_NAME; }

    public long getLMCPSeriesNameAsLong() { return SERIES_NAME_ID; }

    public int getLMCPSeriesVersion() { return SERIES_VERSION; };

    public String getLMCPTypeName() { return TYPE_NAME; }

    public String getFullLMCPTypeName() { return FULL_LMCP_TYPE_NAME; }

    public String toString() {
        return toXML("");
    }

    public String toXML(String ws) {
        StringBuffer buf = new StringBuffer();
        buf.append( ws + "<WeatherReport Series=\"CMASI\">\n");
        if (Area!= null){
           buf.append( ws + "  <Area>\n");
           buf.append( ( Area.toXML(ws + "    ")) + "\n");
           buf.append( ws + "  </Area>\n");
        }
        buf.append( ws + "  <WindSpeed>" + String.valueOf(WindSpeed) + "</WindSpeed>\n");
        buf.append( ws + "  <WindDirection>" + String.valueOf(WindDirection) + "</WindDirection>\n");
        buf.append( ws + "  <Visibility>" + String.valueOf(Visibility) + "</Visibility>\n");
        buf.append( ws + "  <CloudCeiling>" + String.valueOf(CloudCeiling) + "</CloudCeiling>\n");
        buf.append( ws + "  <CloudCoverage>" + String.valueOf(CloudCoverage) + "</CloudCoverage>\n");
        buf.append( ws + "</WeatherReport>");

        return buf.toString();
    }

    public boolean equals(Object anotherObj) {
        if ( anotherObj == this ) return true;
        if ( anotherObj == null ) return false;
        if ( anotherObj.getClass() != this.getClass() ) return false;
        WeatherReport o = (WeatherReport) anotherObj;
        if (Area == null && o.Area != null) return false;
        if ( Area!= null && !Area.equals(o.Area)) return false;
        if (WindSpeed != o.WindSpeed) return false;
        if (WindDirection != o.WindDirection) return false;
        if (Visibility != o.Visibility) return false;
        if (CloudCeiling != o.CloudCeiling) return false;
        if (CloudCoverage != o.CloudCoverage) return false;

        return true;
    }

    public int hashCode() {
        int hash = 0;
        hash += 31 * (int)WindSpeed;
        hash += 31 * (int)WindDirection;
        hash += 31 * (int)Visibility;
        hash += 31 * (int)CloudCeiling;

        return hash + super.hashCode();
    }
    
}
