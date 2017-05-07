// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Created on 18.03.2005
 *
 */
package org.flexdock.plaf.resources;



import org.flexdock.plaf.Configurator;
import org.flexdock.plaf.PropertySet;
import org.flexdock.plaf.resources.paint.Painter;
import org.flexdock.plaf.resources.paint.PainterResource;

/**
 * @author Claudio Romano
 *
 */
public class PainterResourceHandler extends ResourceHandler {

    public static final String PAINTER_RESOURCE_KEY = "painter-resource";

    public Object getResource(String stringValue) {
        PropertySet propertySet = Configurator.getProperties(stringValue, PAINTER_RESOURCE_KEY);
        PainterResource pr = createResource( propertySet);

        Painter p = createPainter( pr.getImplClass());
        p.setPainterResource( pr);
        return p;
    }

    private static Painter createPainter(Class clazz) {

        if(clazz ==null)
            return null;

        try {
            return (Painter)clazz.newInstance();
        } catch(Exception e) {
            System.err.println("Exception: " +e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static PainterResource createResource(PropertySet properties) {
        PainterResource painterResource = new PainterResource();
        painterResource.setAll( properties);
        return painterResource;
    }
}
