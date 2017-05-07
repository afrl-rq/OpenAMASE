// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Copyright (c) 2005 FlexDock Development Team. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE.
 */
package org.flexdock.perspective.persist;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.flexdock.docking.state.PersistenceException;

/**
 * Created on 2005-03-30
 *
 * @author <a href="mailto:marius@eleritec.net">Christopher Butler</a>
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 * @version $Id: Persister.java,v 1.7 2005-07-05 14:53:26 marius Exp $
 */
public interface Persister {

    /**
     * Serializes <code>PerspectiveInfo</code> to the supplied data stream.
     *
     * @param os <code>OutputStream</code> to persist perspectiveInfo to.
     * @param perspectiveInfo data object to be persisted
     * @return <code>true</code> when there was no problem with persisting the perspectiveInfo object.
     * @throws IOException in case of input/output problem.
     */
    boolean store(OutputStream os, PerspectiveModel perspectiveInfo) throws IOException, PersistenceException;

    /**
     * Deserializes <code>PerspectiveInfo</code> from the supplied data stream.
     *
     * @param is <code>InputStream</code> to load perspectiveInfo from.
     * @return <code>true</code> when there was no problem with persisting the perspectiveInfo object.
     * @throws IOException in case of input/output problem.
     */
    PerspectiveModel load(InputStream is) throws IOException, PersistenceException;

}
