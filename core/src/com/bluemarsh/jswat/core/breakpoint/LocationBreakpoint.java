/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is JSwat. The Initial Developer of the Original
 * Software is Nathan L. Fiedler. Portions created by Nathan L. Fiedler
 * are Copyright (C) 2006. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id: LocationBreakpoint.java 6 2007-05-16 07:14:24Z nfiedler $
 */

package com.bluemarsh.jswat.core.breakpoint;

import com.sun.jdi.Location;

/**
 * A LocationBreakpoint stops at a specific JDI Location (i.e. a code index
 * within a particular class). It does not need to resolve against any class
 * since it is given an exact Location at which to stop. Because of this,
 * it must self-destruct when the session disconnects from the debuggee.
 *
 * @author Nathan Fiedler
 */
public interface LocationBreakpoint extends Breakpoint {
    /** Name of the 'location' property. */
    public static final String PROP_LOCATION = "location";

    /**
     * Retrieve the location associated with this breakpoint.
     *
     * @return  location of breakpoint.
     */
    public Location getLocation();

    /**
     * Set the location at which this breakpoint should stop.
     *
     * @param  location  location at this this breakpoint should stop.
     */
    public void setLocation(Location location);
}