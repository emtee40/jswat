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
 * The Original Software is JSwat Installer. The Initial Developer of the
 * Software is Nathan L. Fiedler. Portions created by Nathan L. Fiedler
 * are Copyright (C) 2005. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id: ReviewPanel.java 6 2007-05-16 07:14:24Z nfiedler $
 */

package com.bluemarsh.jswat.installer;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 * Displays the review screen of the installer.
 *
 * @author  Nathan Fiedler
 */
public class ReviewPanel extends InstallerPanel {
    /** silence compiler warnings */
    private static final long serialVersionUID = 1L;

    /**
     * Creates new form ReviewPanel.
     */
    public ReviewPanel() {
        initComponents();

        // Set up the text pane styles.
        StyledDocument doc = reviewTextPane.getStyledDocument();
        addStylesToDocument(doc);
    }

    /**
     * Set up the styles for the styled document.
     *
     * @param  doc  styled document to prepare.
     */
    private void addStylesToDocument(StyledDocument doc) {
        Style def = StyleContext.getDefaultStyleContext().getStyle(
                StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");

        Style s = doc.addStyle("emphasized", regular);
        StyleConstants.setBold(s, true);
    }

    /**
     * Calculates the total installation size in bytes, by summing up the
     * sizes of the entries in the jswat.zip file.
     *
     * @return  installation size in bytes.
     */
    private long calculateSize() {
        long size = 0;
        int count = 0;
        InputStream is = ClassLoader.getSystemResourceAsStream("jswat.zip");
        ZipInputStream zis = new ZipInputStream(is);
        try {
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                if (!entry.isDirectory()) {
                    // Only count files, not directories.
                    count++;
                }
                // Don't worry about overflow, that will never happen.
                size += entry.getSize();
                zis.closeEntry();
                entry = zis.getNextEntry();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                zis.close();
            } catch (IOException ioe) { }
        }
        // As an optimization, set the file count so the progress panel doesn't
        // have to perform the same count again later.
        Controller.getDefault().setProperty("fileCount", String.valueOf(count));
        return size;
    }

    public void doHide() {
    }

    public void doShow() {
        populateText();
    }

    public String getNext() {
        return "progress";
    }

    public String getPrevious() {
        return "home";
    }

    /**
     * Populate the text pane with the latest information.
     */
    private void populateText() {
        StyledDocument doc = reviewTextPane.getStyledDocument();
        try {
            Style rstyle = doc.getStyle("regular");
            Style estyle = doc.getStyle("emphasized");
            String msg = Bundle.getString("MSG_Review_Location");
            doc.insertString(doc.getLength(), msg, rstyle);
            String home = Controller.getDefault().getProperty("home");
            doc.insertString(doc.getLength(), home, estyle);
            msg = Bundle.getString("MSG_Review_InstallSize");
            doc.insertString(doc.getLength(), msg, rstyle);
            long size = calculateSize() / 1048576;
            doc.insertString(doc.getLength(), size + " MB", estyle);
        } catch (BadLocationException ble) {
            // This would be entirely unexpected.
            ble.printStackTrace();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        reviewTextPane = new javax.swing.JTextPane();

        setLayout(new java.awt.BorderLayout());

        reviewTextPane.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        reviewTextPane.setEditable(false);
        reviewTextPane.setFocusable(false);
        reviewTextPane.setMargin(new java.awt.Insets(12, 12, 12, 12));
        add(reviewTextPane, java.awt.BorderLayout.CENTER);

    }
    // </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane reviewTextPane;
    // End of variables declaration//GEN-END:variables
}
