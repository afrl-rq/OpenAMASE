// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.layers;

import avtas.map.MapLayer;
import avtas.data.Unit;
import avtas.util.NavUtils;
import avtas.map.Proj;
import avtas.properties.UserProperty;
import avtas.properties.XmlSerializer;
import avtas.xml.Element;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.GlyphVector;

/**
 * Creates a small widget on the map that shows the current scale.
 *
 * @author AFRL/RQQD
 */
public class MapScale extends MapLayer {

    private Proj proj = null;
    private int unitLen = 0;
    private static int NOM_LINE_LEN = 100;
    private double distM = 0;
    private int yoffset = 50;
    private int xoffset = 50;
    private UnitScale scale = new StandardScale();
    
    @UserProperty(Description = "Type of Map Scale.")
    public ScaleTypeEnum ScaleType = ScaleTypeEnum.Standard;

    public static enum ScaleTypeEnum {

        Metric, Nautical, Standard;
    }

    public MapScale() {
    }

    @Override
    public void setConfiguration(Element node) {
        super.setConfiguration(node);
        XmlSerializer.deserialize(node, this);

        switch (ScaleType) {
            case Metric:
                scale = new MetricScale();
                break;
            case Standard:
                scale = new StandardScale();
                break;
            case Nautical:
                scale = new NauticalScale();
                break;
        }
        //String scaleType = XMLUtil.getValue(node, "ScaleType", "Standard");

    }

    @Override
    public void paint(Graphics2D g) {
        if (proj != null && scale != null) {
            int startX = proj.getWidth() - xoffset;
            int startY = proj.getHeight() - yoffset;

            g.setColor(Color.WHITE);

            g.drawLine(startX, startY, startX - unitLen, startY);
            g.drawLine(startX, startY - 5, startX, startY + 5);
            g.drawLine(startX - unitLen, startY - 5, startX - unitLen, startY + 5);

            String str = scale.getOutput(distM);

            GlyphVector gv = g.getFont().createGlyphVector(g.getFontRenderContext(), str);
            g.drawGlyphVector(gv, startX - 0.5f * unitLen - (float) (0.5 * gv.getLogicalBounds().getWidth()),
                    startY + (float) (0.5 * yoffset));
        }
    }

    @Override
    public void project(Proj proj) {
        this.proj = proj;
        if (proj != null && scale != null) {
            double pixPerLon = proj.getPixPerLon();
            double dlon = Math.toRadians(NOM_LINE_LEN / pixPerLon);
            double radLat = Math.toRadians(proj.getCenterLat());
            double rlat = NavUtils.getRadius(radLat);
            double coslat = Math.cos(radLat);
            distM = rlat * coslat * dlon;
            distM = scale.getNearestUnit(distM);
            dlon = distM / rlat / coslat;
            unitLen = (int) (Math.toDegrees(dlon) * pixPerLon);
            //System.out.println("Len: " + unitLen + " for dlon: " + dlon + " ( " + distM + " )");
        }
    }

    private static abstract class UnitScale {

        public abstract double getNearestUnit(double dist_m);

        public abstract String getOutput(double dist_m);

        public double getClosestVal(double val, double[] vals) {
            if (vals[0] >= val) {
                return vals[0];
            }
            for (int i = 1; i < vals.length; i++) {
                if (vals[i] >= val) {
                    if (val - vals[i - 1] < vals[i] - val)
                        return vals[i - 1];
                    else return vals[i];
                }
            }
            return vals[vals.length - 1];
        }
    }

    private static class MetricScale extends UnitScale {

        public static double[] units = new double[]{1, 10, 100, 500, 1000, 5000, 20000, 50000, 100000, 500000, 1000000};

        public double getNearestUnit(double dist_m) {
            return getClosestVal(dist_m, units);
        }

        public String getOutput(double dist_m) {
            int e = (int) Math.log10(dist_m);
            if (e >= 3) {
                return (int) Math.round(dist_m / 1000) + " KM";
            }
            else {
                return (int) Math.round(dist_m) + " M";
            }
        }
    }

    private static class StandardScale extends UnitScale {

        private static final double mi = 5280;
        public static double[] units = new double[]{1, 10, 100, 500, 1000, mi, 5 * mi, 10 * mi, 50 * mi, 100 * mi, 500 * mi, 1000 * mi};

        public double getNearestUnit(double dist_m) {
            double dist_ft = Unit.METER.convertTo(dist_m, Unit.FEET);
            return Unit.FEET.convertTo(getClosestVal(dist_ft, units), Unit.METER);
        }

        public String getOutput(double dist_m) {
            double dist_ft = Unit.METER.convertTo(dist_m, Unit.FEET);
            if (dist_ft >= mi) {
                return (int) Math.round(dist_ft / mi) + " MI";
            }
            else {
                return (int) Math.round(dist_ft) + " FT";
            }
        }
    }

    private static class NauticalScale extends UnitScale {

        private static final double mi = 6076;
        public static double[] units = new double[]{1, 10, 100, 500, 1000, mi, 5 * mi, 10 * mi, 50 * mi, 100 * mi, 500 * mi, 1000 * mi};

        public double getNearestUnit(double dist_m) {
            double dist_ft = Unit.METER.convertTo(dist_m, Unit.FEET);
            return Unit.FEET.convertTo(getClosestVal(dist_ft, units), Unit.METER);
        }

        public String getOutput(double dist_m) {
            double dist_ft = Unit.METER.convertTo(dist_m, Unit.FEET);
            if (dist_ft >= mi) {
                return (int) Math.round(dist_ft / mi) + " NM";
            }
            else {
                return (int) Math.round(dist_ft) + " FT";
            }
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */