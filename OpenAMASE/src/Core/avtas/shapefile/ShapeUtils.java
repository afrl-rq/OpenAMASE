// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.shapefile;

import gov.nasa.worldwind.formats.shapefile.Shapefile;
import gov.nasa.worldwind.formats.shapefile.ShapefileRecord;
import gov.nasa.worldwind.formats.shapefile.ShapefileRecordPolyline;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Reads shapefiles and provides access to geometry. Assumes that shapefiles are
 * in the PolygonZ format and are simple extruded polygons.
 *
 * @author AFRL/RQQD
 */
public class ShapeUtils {

    /**
     * returns an array of polygons for a single shp input file. Assumes that an
     * shx and dbf file of the same root name are also present. If the file
     * passed is a directory, then all shp files are read.
     *
     * @param src The shapefile.
     * @return The array of polygons.
     */
    public static List<EsriShape> getPolyGeometry(File src) {
        File[] files = new File[]{};
        List<EsriShape> retList = new ArrayList<>();

        if (src.isDirectory()) {
            files = src.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().toLowerCase().endsWith(".shp");
                }
            });
        } else if (src.exists()) {
            files = new File[]{src};
        }

        for (File file : files) {
            Shapefile shapeFile = new Shapefile(file);
            String type = shapeFile.getShapeType();
            if (!Shapefile.isPolygonType(type) && !Shapefile.isPolylineType(type)) {
                continue;
            }

            for (int i = 0; i < shapeFile.getNumberOfRecords(); i++) {
                ShapefileRecord record = shapeFile.nextRecord();
                List<EsriShape> shapeList = getPolyGeometry(record);

                // fill in the DBF file values
                for (EsriShape shape : shapeList) {
                    for (Map.Entry<String, String> entry : shape.getAttributes().entrySet()) {
                        shape.addAttribute(entry.getKey(), entry.getValue());
                    }

                }

                retList.addAll(shapeList);

            }

        }

        return retList;
    }

    /**
     * Reads Polygons and polylines
     */
    public static List<EsriShape> getPolyGeometry(ShapefileRecord record) {

        List<EsriShape> shapeList = new ArrayList<>();

        EsriShape shape;

        String type = record.getShapeType();

        double[] xs;
        double[] ys;
        double[] zs;
        int offset = 0;

        if (record instanceof ShapefileRecordPolyline) {
            ShapefileRecordPolyline polyRecord = (ShapefileRecordPolyline) record;
            zs = polyRecord.getZValues();

            for (int i = 0; i < polyRecord.getNumberOfParts(); i++) {
                int numPoints = polyRecord.getNumberOfPoints(i);
                Iterable<double[]> pts = polyRecord.getPoints(i);
                xs = new double[numPoints];
                ys = new double[numPoints];
                int j = 0;
                for (double[] pt : pts) {
                    xs[j] = pt[0];
                    ys[j] = pt[1];
                    j++;
                }

                shape = new EsriShape();
                shape.shapeType = ShapeType.fromString(type);

                shape.partIndex = i;
                shape.shapeIndex = record.getRecordNumber();
                shape.x = xs;
                shape.y = ys;

                shape.numPoints = shape.x.length;

                if (Shapefile.isZType(type)) {
                    shape.z = Arrays.copyOfRange(zs, offset, offset + numPoints);
                }

                shapeList.add(shape);
                offset += numPoints;
            }
        }

        return shapeList;
    }

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */