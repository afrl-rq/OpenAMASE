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
import avtas.map.MapPanel;
import avtas.map.Proj;
import avtas.properties.UserProperty;
import avtas.properties.XmlSerializer;
import avtas.xml.Element;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * A layer that provides a brightness control for the map.  All layers that sit below this layer will
 * appear darker, based on the alpha value that is set for the mask.
 *
 * @author AFRL/RQQD
 */
public class MaskLayer extends MapLayer {

    @UserProperty(Description = "Opacity level for the mask.  Use values [0..1]")
    float Opacity = 0.5f;
    
    Color maskColor = new Color(0,0,0,Opacity);

    public MaskLayer() {}

    @Override
    public void paint(Graphics2D g) {
        MapPanel parent = getMap();
        if (parent != null) {
            g.setColor(maskColor);
            g.fillRect(0, 0, parent.getWidth(), parent.getHeight());
        }
    }

    @Override
    public void setConfiguration(Element node) {
        //float opacity = XMLUtil.getFloat(node, "Opacity", shadeColor.getAlpha()/255f);
        XmlSerializer.deserialize(node, this);
        maskColor = new Color(0f, 0f, 0f, Opacity);
    }



    /** Sets the opacity of the mask.  Values should be between 0 and 1.0
     *
     * @param alpha the opacity of the mask (0=clear, 1=opaque)
     */
    public void setOpacity(float alpha) {
        Opacity = alpha;
        this.maskColor = new Color(0, 0, 0, (float) alpha);
        refresh();
    }
    
    public float getOpacity() {
        return Opacity;
    }

    @Override
    public void project(Proj proj) {
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */