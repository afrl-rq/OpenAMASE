// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.layers;

import avtas.app.UserExceptions;
import avtas.util.Colors;
import avtas.xml.XMLUtil;
import avtas.map.graphics.MapPoly;
import avtas.properties.PropertyEditor;
import avtas.properties.UserProperty;
import avtas.shapefile.EsriShape;
import avtas.shapefile.ShapeUtils;
import avtas.xml.Element;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.List;

/**
 *
 * @author AFRL/RQQD
 */
public class EsriShapeLayer extends GraphicsLayer<MapPoly> {
    
    @UserProperty(Description = "Folder where shapefiles are stored", 
            FileType = UserProperty.FileTypes.Directories,
            DisplayName = "Directory")
    File shapeDirectory;
    
    @UserProperty(DisplayName = "Shape Color", Description = "Default color for shapes")
    Color shapeColor = Color.BLACK;

    public EsriShapeLayer() {
        
    }

    @Override
    public Component getSettingsView() {
        PropertyEditor ed = PropertyEditor.getEditor(this);
        return ed;
    }

    public void setShapeDirectory(File shapeDirectory) {
        this.shapeDirectory = shapeDirectory;
        if (shapeDirectory != null && shapeDirectory.isDirectory()) {
            for (File file : shapeDirectory.listFiles()) {
                if (shapeDirectory.getName().toLowerCase().endsWith(".shp"))
                    loadEsriGeometry(file);
            }
        }
        loadEsriGeometry(shapeDirectory);
    }

    public void setShapeColor(Color shapeColor) {
        this.shapeColor = shapeColor;
        setPaint(shapeColor, 1);
        refresh();
    }
    
    


    @Override
    public void setConfiguration(Element node) {
        File path = new File(XMLUtil.getValue(node, "Directory", ""));
        setShapeDirectory(path);

        Color c = Colors.getColor(XMLUtil.getValue(node, "Color", "Black"), Color.BLACK);
        setShapeColor(c);
    }

    void loadEsriGeometry(File file) {
        if (!file.exists()) {
            return;
        }
        try {
            loadEsriGeometry(ShapeUtils.getPolyGeometry(file));
        } catch (Exception ex) {
            UserExceptions.showWarning(this, "Error loading ESRI Shapefile", ex);
        }
    }

    void loadEsriGeometry(List<EsriShape> polys) {
        for (EsriShape g : polys) {
            MapPoly mp = new MapPoly(g.getY(), g.getX());
            add(mp);
        }
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */