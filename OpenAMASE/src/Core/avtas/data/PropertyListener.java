// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.data;

/**
 * Simple interface that allows a <code>Property</code> to listen for and react
 * to changes in other <code>Properties</code>.
 * 
 * @author AFRL/RQQD
 */
public interface PropertyListener {

    /**
     * Implemented by a <code>PropertyListener</code> so that it can react to
     * changes in other <code>Properties</code>.
     *
     * @param   prop    The <code>Property</code> that has changed.
     */
    public void propertyChanged(Property prop);

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */