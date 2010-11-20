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
 * are Copyright (C) 2006-2010. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id$
 */
package com.bluemarsh.jswat.core.output;

/**
 * Default implementation of the OutputWriter interface that does nothing.
 * This is a placeholder for the unit tests which do not presently provide
 * access to the NetBeans interface.
 *
 * @author Nathan Fiedler
 */
public class DefaultOutputWriter implements OutputWriter {

    @Override
    public void ensureVisible() {
        // ignored
    }

    @Override
    public void printError(final String msg) {
        // ignored
    }

    @Override
    public void printOutput(final String msg) {
        // ignored
    }
}
