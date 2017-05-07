// ===============================================================================
// Authors: AFRL/RQQD
// Organization: Air Force Research Laboratory, Aerospace Systems Directorate, Power and Control Division
// 
// Copyright (c) 2017 Government of the United State of America, as represented by
// the Secretary of the Air Force.  No copyright is claimed in the United States under
// Title 17, U.S. Code.  All Other Rights Reserved.
// ===============================================================================

/*
 * Copyright (c) 2004 Christopher M Butler
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.flexdock.docking.defaults;

import org.flexdock.docking.DockingPort;

/**
 * This interface provides a set of methods used by the
 * {@code DefaultDockingPort} class to manage border state after docking and
 * undocking operations. This class is necessary because
 * {@code DefaultDockingPort} is a {@code DockingPort} implementation that uses
 * nested {@code Components} to achieve a docking layout. Particularly,
 * {@code DefaultDockingPort} uses {@code JSplitPane} and {@code JTabbedPane},
 * each of which have their own {@code Borders} provided by the currently
 * installed PLAF. Nesting these {@code Components} within one another without
 * proper border management can result in a compound border effect that is
 * displeasing to the eye. This class provides a means by which custom border
 * management behavior may be plugged into the {@code DefaultDockingPort} in
 * response to various different layout conditions.
 *
 * @author Chris Butler
 */
public interface BorderManager {
    /**
     * Callback method allowing for customized behavior when the
     * {@code DefaultDockingPort's} docked component state has changed and there
     * is no longer a component docked within the port.
     *
     * @param port
     *            the {@code DockingPort} whose layout borders are to be managed
     */
    public void managePortNullChild(DockingPort port);

    /**
     * Callback method allowing for customized behavior when the
     * {@code DefaultDockingPort's} docked component state has changed and there
     * is a single generic component docked within the port. The
     * {@code Component} may be retrieved by calling
     * {@code port.getDockedComponent()}.
     *
     * @param port
     *            the {@code DockingPort} whose layout borders are to be managed
     */
    public void managePortSimpleChild(DockingPort port);

    /**
     * Callback method allowing for customized behavior when the
     * {@code DefaultDockingPort's} docked component state has changed and the
     * port has been split between two components. The {@code JSPlitPane} may be
     * retrieved by calling {@code port.getDockedComponent()}.
     *
     * @param port
     *            the {@code DockingPort} whose layout borders are to be managed
     */
    public void managePortSplitChild(DockingPort port);

    /**
     * Callback method allowing for customized behavior when the
     * {@code DefaultDockingPort's} docked component state has changed and
     * docked components within the {@code CENTER} region are layed-out within a
     * {@code JTabbedPane}. The {@code JTabbedPane} may be retrieved by calling
     * {@code port.getDockedComponent()}.
     *
     * @param port
     *            the {@code DockingPort} whose layout borders are to be managed
     */
    public void managePortTabbedChild(DockingPort port);
}
