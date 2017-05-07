// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.map.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

/**
 * Implements a basic quadtree.
 *
 * @author AFRL/RQQD
 */
public class Quadtree<T> {

    HashMap<T, WorldBounds> items = new HashMap<T, WorldBounds>();
    Quadtree ne;
    Quadtree nw, se, sw;
    WorldBounds bounds;
    int maxCap;
    boolean hasChildren = false;
    static int DEFAULT_CAP = 10;

    public Quadtree() {
        this(WorldBounds.fromCorners(90, -180, -90, 180), DEFAULT_CAP);
    }

    public Quadtree(int maxCapacity) {
        this(WorldBounds.fromCorners(90, -180, -90, 180), maxCapacity);
    }

    public Quadtree(WorldBounds b, int maxCapacity) {
        this.maxCap = maxCapacity;
        this.bounds = b;
        split();
    }

    protected Quadtree(double westLon, double southLat, double lonwidth, double latheight, int maxCapacity) {
        bounds = new WorldBounds(westLon, southLat, lonwidth, latheight);
        this.maxCap = maxCapacity;
    }

    public Quadtree getNW() {
        return nw;
    }

    public Quadtree getNE() {
        return ne;
    }

    public Quadtree getSW() {
        return sw;
    }

    public Quadtree getSE() {
        return se;
    }

    public void split() {
        if (!hasChildren) {
            System.out.println("splitting " + bounds);
            WorldBounds b = bounds;
            double h2 = b.getLatHeight() / 2.;
            double w2 = b.getLonWidth() / 2.;
            nw = new Quadtree(b.getWestLon(), b.getSouthLat() + h2, w2, h2, maxCap);
            ne = new Quadtree(b.getWestLon() + w2, b.getSouthLat() + h2, w2, h2, maxCap);
            sw = new Quadtree(b.getWestLon(), b.getSouthLat(), w2, h2, maxCap);
            se = new Quadtree(b.getWestLon() + w2, b.getSouthLat(), w2, h2, maxCap);

            for (Entry<T, WorldBounds> e : items.entrySet()) {
                T obj = e.getKey();
                b = e.getValue();
                nw.add(obj, b);
                ne.add(obj, b);
                sw.add(obj, b);
                se.add(obj, b);
            }
            items.clear();
            hasChildren = true;
        }
    }

    public boolean add(T obj, WorldBounds b) {
        if (bounds.intersects(b)) {
            if (items.size() >= maxCap) {
                split();
            }
            if (hasChildren) {
                return ne.add(obj, b) || nw.add(obj, b) || se.add(obj, b) || sw.add(obj, b);
            }
            else {
                items.put(obj, b);
                return true;
            }
        }
        return false;
    }

    public Vector<T> getAllItems() {
        Vector<T> ret = new Vector<T>();
        if (hasChildren) {
            ret.addAll(ne.getAllItems());
            ret.addAll(nw.getAllItems());
            ret.addAll(se.getAllItems());
            ret.addAll(sw.getAllItems());
        }
        else {
            ret.addAll(items.keySet());
        }
        return ret;
    }

    public Vector<T> getItems(WorldBounds b) {
        Vector<T> ret = new Vector<T>();
        if (bounds.intersects(b)) {
            if (hasChildren) {
                ret.addAll(ne.getItems(b));
                ret.addAll(nw.getItems(b));
                ret.addAll(se.getItems(b));
                ret.addAll(sw.getItems(b));
            }
            else {
                ret.addAll(items.keySet());
            }
        }
        return ret;
    }

    public Vector<WorldBounds> getBoundaries() {
        Vector<WorldBounds> ret = new Vector<WorldBounds>();
        if (hasChildren) {
            ret.addAll(ne.getBoundaries());
            ret.addAll(nw.getBoundaries());
            ret.addAll(se.getBoundaries());
            ret.addAll(sw.getBoundaries());
        }
        ret.add(bounds);
        return ret;
    }

    public void clear() {
        ne = null;
        nw = null;
        se = null;
        sw = null;
        items.clear();
        hasChildren = false;
    }
}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */