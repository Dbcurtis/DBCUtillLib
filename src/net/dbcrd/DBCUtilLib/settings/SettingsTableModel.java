/*
 * Copyright (c) 2015, Daniel B. Curtis <dbcurtis@dbcrd.net>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.dbcrd.DBCUtilLib.settings;

import static java.awt.Color.RED;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Daniel B. Curtis
 */
@ClassPreamble(
        author = "Daniel B. Curtis",
        date = "June 2015",
        currentRevision = 1,
        lastModified = "06/09/15",
        copyright = "(C) 2015 by Daniel B. Curtis, all rights reserved",
        lastModifiedBy = "Daniel B. Curtis"
)
public class SettingsTableModel extends AbstractTableModel {

    /**
     * the Logger
     */
    private static final Logger THE_LOGGER = Logger.getLogger(SettingsTableModel.class.getName());

    /**
     *
     */
    private static final long serialVersionUID = -4827809735569277550L;

    /**
     * an Array of table headers
     */
    String[] headers = {"Property", "Type", "Value"};

    /**
     * TBD
     */
    SettingsTableModel() {
        super();

    }

    /**
     *
     * @return a list of Settings
     */
    List<Setting> getData() {
        return Setting.getSETTINGS();
    }

    @Override
    public int getRowCount() {
        return Setting.getSETTINGS().size();
    }

    @Override
    public int getColumnCount() {
        return headers.length;
    }

    /**
     * for column 0 returns the Setting's name String
     * for column 1 returns the Setting's SETTING_TYPE
     * for column 2 returns the Setting's Value
     *
     * @param rowIndex an int as expected,
     * @param columnIndex an int as expected
     * @return an Object of the above specified types
     */
    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        final Setting s = Setting.getSETTINGS().get(rowIndex);
        switch (columnIndex) {
            case 0:
                return s.getName();
            case 1:
                return s.getStype().toString();
            case 2:
                return s.getValue().toString();
            default:
                break;
        }
        return new Object();
    }

    @Override
    public String getColumnName(final int columnIndex) {
        return headers[columnIndex];
    }

    /**
     * all rows, column 0 and 1 are editable
     * @param rowIndex as expected
     * @param columnIndex as expected
     * @return column 0 and 1 are editable
     */
    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return columnIndex == 0 || columnIndex == 1;
    }

    /**
     *
     * @param value an Object that is the "value" of the Setting
     * @param rowIndex an int
     * @param columnIndex an int
     */
    @Override
    public void setValueAt(final Object value, final int rowIndex, final int columnIndex) {
        final Setting s = Setting.getSETTINGS().get(rowIndex);
        switch (columnIndex) { // get the column
            case 0:
                if (value instanceof String) {
                    final String v = ((String) value).trim();
                    if (Setting.isNotUsed(v)) {
                        s.setName(v);
                    } else {
                        JOptionPane.showMessageDialog(null, "Duplicate Setting ID", "alert", JOptionPane.ERROR_MESSAGE);
                        Setting.mySt.setNote(MessageFormat.format("Duplicate Setting ID: {0}", v),
                                RED);
                    }

                }
                break;
            case 1:
                if (value instanceof Setting.SETTING_TYPE) {
                    s.setStype((Setting.SETTING_TYPE) value);
                }
                break;
            case 2:
                s.setValue(value);
                break;
            default:
                THE_LOGGER.warning(String.format("unexpected enum default %s", columnIndex));
                break;
        }

        fireTableRowsUpdated(rowIndex, rowIndex);
    }
}
