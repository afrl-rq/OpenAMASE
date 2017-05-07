// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.layers;


import avtas.data.Unit;
import avtas.map.Proj;
import avtas.map.graphics.MapEllipse;
import avtas.map.graphics.MapGraphic;
import avtas.map.graphics.MapText;
import avtas.map.util.WGS84;
import avtas.properties.UserProperty;
import avtas.properties.XmlSerializer;
import avtas.xml.Element;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import javax.swing.SwingConstants;

/**
 *
 * @author AFRL/RQQD
 */
public class RangeLayer extends GraphicsLayer<MapGraphic> {

    double[] ranges;
    
    @UserProperty(Description = "Color for Range Rings")
    private Color color = Color.WHITE;

    /** Creates a new instance of RangeLayer */
    public RangeLayer() {
        super();

        ranges = new double[]{10, 20, 50, 100, 200};

        for (double range : ranges) {
            RangeRing ring = new RangeRing(range);
            ring.setColor(color);
            add(ring);
        }
    }

    public void setConfiguration(Element node) {

        ranges = new double[]{10, 20, 50, 100, 200};

        XmlSerializer.deserialize(node, this);
        //color = Colors.getColor(XMLUtil.getValue(node, "Color", "WHITE"), Color.WHITE);

        for (double range : ranges) {
            RangeRing ring = new RangeRing(range);
            ring.setColor(getColor());
            add(ring);
        }


    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }
    

    class RangeRing extends MapGraphic {

        double range = 0;
        MapText text = new MapText();
        MapEllipse ring = new MapEllipse();
        private Proj proj;

        public RangeRing(double range_NM) {
            //ring.setProjectionType(MapGraphic.PROJECTION_TYPE_XY);
            range = Unit.NM.convertTo(range_NM, Unit.METER);
            text.setText(Integer.toString((int) range_NM) + " NM");
            text.setFont(text.getFont().deriveFont(14f));
            text.setHorizontalAlignment(SwingConstants.CENTER);
            //text.setProjectionType(MapGraphic.PROJECTION_TYPE_XY);
            ring.setRadius(range, range);
            text.setOffset(0, -7);

        }

        public void setColor(Color color) {
            text.setColor(color);
            ring.setPainter(color, 1);
        }

        public void project(Proj proj) {
            this.proj = proj;
            ring.setCenter(proj.getCenterLat(), proj.getCenterLon());
            text.setLatLon(proj.getCenterLat() + Math.toDegrees(range / WGS84.eqRadius),
                    proj.getCenterLon());
            text.setRotation(-proj.getRotation());
            text.project(proj);
            ring.project(proj);
            setProjected(text.isProjected() || ring.isProjected());
        }

        public void paint(Graphics2D g) {

            Graphics2D g2 = (Graphics2D) g.create();

            if (proj != null) {
                g2.rotate(proj.getRotation(), proj.getWidth() / 2f, proj.getHeight() / 2f);
                ring.paint(g2);
                text.paint(g2);
            }
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */