// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================




package avtas.terrain;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Used to receive updates regarding a viewshed generation process
 *
 * @author AFRL/RQQD
 */
public interface ViewShedObserver {

    /**
     * Updates the viewshed generation process
     *
     * @param percentComplete percent of viewshed generation that has completed. in the range (0..100)
     */
    public void viewshedUpdate(double percentComplete);

}

/* Distribution A. Approved for public release. 
 *  Case: #88ABW-2015-4601. Date: 24 Sep 2015. */