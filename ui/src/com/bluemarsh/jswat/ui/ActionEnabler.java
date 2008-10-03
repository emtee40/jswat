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
 * $Id: ActionEnabler.java 6 2007-05-16 07:14:24Z nfiedler $
 */

package com.bluemarsh.jswat.ui;

import com.bluemarsh.jswat.core.session.Session;
import com.bluemarsh.jswat.core.session.SessionEvent;
import com.bluemarsh.jswat.core.session.SessionListener;
import com.bluemarsh.jswat.core.session.SessionManager;
import com.bluemarsh.jswat.core.session.SessionManagerEvent;
import com.bluemarsh.jswat.core.session.SessionManagerListener;
import com.bluemarsh.jswat.core.session.SessionProvider;
import java.awt.EventQueue;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Action;


/**
 * Class ActionEnabler listens to the state of the current session and
 * enables and disables certain actions depending on the session state.
 *
 * @author Nathan Fiedler
 */
public class ActionEnabler implements SessionListener, SessionManagerListener {
    /** The single instance of this class. */
    private static ActionEnabler theInstance;
    /** The current session. */
    private Session currentSession;
    /** Actions to be enabled when debuggee is connected. */
    private Set<Action> activeActions;
    /** Actions to be enabled when debuggee is disconnected. */
    private Set<Action> inactiveActions;
    /** Actions to be enabled when debuggee is running. */
    private Set<Action> runningActions;
    /** Actions to be enabled when debuggee is suspended. */
    private Set<Action> suspendedActions;
    
    /**
     * Creates a new instance of ActionEnabler.
     */
    private ActionEnabler() {
        SessionManager sm = SessionProvider.getSessionManager();
        sm.addSessionManagerListener(this);
        currentSession = sm.getCurrent();
        activeActions = new HashSet<Action>();
        inactiveActions = new HashSet<Action>();
        runningActions = new HashSet<Action>();
        suspendedActions = new HashSet<Action>();
    }

    /**
     * Called when the Session has connected to the debuggee.
     *
     * @param  sevt  session event.
     */
    public void connected(SessionEvent sevt) {
        if (sevt.getSession().equals(currentSession)) {
            setEnabled(activeActions, true);
            setEnabled(inactiveActions, false);
            boolean suspended = currentSession.isSuspended();
            setEnabled(suspendedActions, suspended);
            setEnabled(runningActions, !suspended);
        }
    }

    /**
     * Called when the Session is about to be closed.
     *
     * @param  sevt  session event.
     */
    public void closing(SessionEvent sevt) {
    }

    /**
     * Disable the given Action on the AWT event thread.
     *
     * @param  action  Action to disable.
     */
    private void disableAction(final Action action) {
        Runnable runner = new Runnable() {
            public void run() {
                action.setEnabled(false);
            }
        };
        EventQueue.invokeLater(runner);
    }

    /**
     * Called when the Session has disconnected from the debuggee.
     *
     * @param  sevt  session event.
     */
    public void disconnected(SessionEvent sevt) {
        if (sevt.getSession().equals(currentSession)) {
            setEnabled(activeActions, false);
            setEnabled(inactiveActions, true);
            setEnabled(runningActions, false);
            setEnabled(suspendedActions, false);
        }
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return  instance of this class.
     */
    public static synchronized ActionEnabler getDefault() {
        if (theInstance == null) {
            theInstance = new ActionEnabler();
        }
        return theInstance;
    }

    /**
     * Called after the Session has added this listener to the Session
     * listener list.
     *
     * @param  session  the Session.
     */
    public void opened(Session session) {
    }

    /**
     * Register the given action as requiring a Session connected to the
     * debuggee. While disconnected, the action will be disabled.
     *
     * @param  action  Action to be registered.
     */
    public void registerActiveAction(Action action) {
        activeActions.add(action);
        if (!currentSession.isConnected()) {
            disableAction(action);
        }
    }

    /**
     * Register the given action as requiring a Session that is disconnected
     * from the debuggee. While connected, the action will be disabled.
     *
     * @param  action  Action to be registered.
     */
    public void registerInactiveAction(Action action) {
        inactiveActions.add(action);
        if (currentSession.isConnected()) {
            disableAction(action);
        }
    }

    /**
     * Register the given action as requiring a Session that is connected
     * to the debuggee, and currently in a resumed state (i.e. no threads
     * have been suspended). While disconnected or suspended, the action
     * will be disabled.
     *
     * @param  action  Action to be registered.
     */
    public void registerRunningAction(Action action) {
        runningActions.add(action);
        if (!currentSession.isConnected() || currentSession.isSuspended()) {
            disableAction(action);
        }
    }

    /**
     * Register the given action as requiring a Session that is connected
     * to the debuggee, and currently in a suspended state (i.e. all threads
     * must be suspended). While disconnected or running, the action will be
     * disabled.
     *
     * @param  action  Action to be registered.
     */
    public void registerSuspendedAction(Action action) {
        suspendedActions.add(action);
        if (!currentSession.isConnected() || !currentSession.isSuspended()) {
            disableAction(action);
        }
    }

    /**
     * Called when the debuggee is about to be resumed.
     *
     * @param  sevt  session event.
     */
    public void resuming(SessionEvent sevt) {
        if (sevt.getSession().equals(currentSession)) {
            setEnabled(runningActions, true);
            setEnabled(suspendedActions, false);
        }
    }

    /**
     * Called when a Session has been added to the SessionManager.
     *
     * @param  e  session manager event.
     */
    public void sessionAdded(SessionManagerEvent e) {
        Session session = e.getSession();
        session.addSessionListener(this);
    }

    /**
     * Called when a Session has been removed from the SessionManager.
     *
     * @param  e  session manager event.
     */
    public void sessionRemoved(SessionManagerEvent e) {
        // the session will discard its listeners
    }

    /**
     * Called when a Session has been made the current session.
     *
     * @param  e  session manager event.
     */
    public void sessionSetCurrent(SessionManagerEvent e) {
        // check session state and enable/disable actions
        Session session = e.getSession();
        currentSession = session;
        if (session.isConnected()) {
            setEnabled(activeActions, true);
            setEnabled(inactiveActions, false);
            boolean suspended = session.isSuspended();
            setEnabled(suspendedActions, suspended);
            setEnabled(runningActions, !suspended);
        } else {
            setEnabled(activeActions, false);
            setEnabled(inactiveActions, true);
            setEnabled(runningActions, false);
            setEnabled(suspendedActions, false);
        }
    }

    /**
     * Enable or disable the given set of actions on the AWT event thread.
     *
     * @param  actions  set of actions.
     * @param  enable   true to enable, false to disable.
     */
    private void setEnabled(final Set<Action> actions, final boolean enable) {
        Runnable runner = new Runnable() {
            public void run() {
                for (Action action : actions) {
                    action.setEnabled(enable);
                }
            }
        };
        EventQueue.invokeLater(runner);
    }

    /**
     * Called when the debuggee has been suspended.
     *
     * @param  sevt  session event.
     */
    public void suspended(SessionEvent sevt) {
        if (sevt.getSession().equals(currentSession)) {
            setEnabled(suspendedActions, true);
            setEnabled(runningActions, false);
        }
    }    
}
