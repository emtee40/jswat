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
 * are Copyright (C) 2005. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id: ClassBreakpoint.java 6 2007-05-16 07:14:24Z nfiedler $
 */

package com.bluemarsh.jswat.core.breakpoint;

/**
 * A ClassBreakpoint stops each time a particular class is loaded or
 * unloaded during the execution of the debuggee.
 *
 * @author  Nathan Fiedler
 */
public interface ClassBreakpoint extends Breakpoint {
    /** Name of 'stopOnPrepare' property. */
    public static final String PROP_STOPONPREPARE = "stopOnPrepare";
    /** Name of 'stopOnUnload' property. */
    public static final String PROP_STOPONUNLOAD = "stopOnUnload";

    /**
     * Returns the stop-on-prepare status.
     *
     * @return  true if stopping when class prepares.
     */
    public boolean getStopOnPrepare();

    /**
     * Returns the stop-on-unload status.
     *
     * @return  true if stopping when class unloads.
     */
    public boolean getStopOnUnload();

    /**
     * Sets the stop-on-prepare status.
     *
     * @param  stop  true to stop when class prepares.
     */
    public void setStopOnPrepare(boolean stop);

    /**
     * Sets the stop-on-unload status.
     *
     * @param  stop  true to stop when class unloads.
     */
    public void setStopOnUnload(boolean stop);
}
