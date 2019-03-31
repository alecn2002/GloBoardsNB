/*
 * The MIT License
 *
 * Copyright 2019 alecn.
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.alecn.glo.netbeans_bugtracking.query;

import com.alecn.glo.netbeans_bugtracking.issue.GloIssue;
import com.alecn.glo.util.LazyValue;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author <a href="mailto:alecn2002@gmail.com">AlecN</a>
 */
@RequiredArgsConstructor
public class GloQueryResultTableModel implements TableModel {

    @NonNull
    private final List<String> columnNames;
    private List<List<GloIssue>> issues = new ArrayList<>();
    private int rowCount = 0;
    private List<TableModelListener> tableModelListeners = new ArrayList<>();

    @NonNull
    private final Function<GloIssue, String> displayVisitor;
    @NonNull
    private final Function<GloIssue, String> tooltipVisitor;

    private class GloIssueTableRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            setText(displayVisitor.apply((GloIssue)value));

            setToolTipText(tooltipVisitor.apply((GloIssue)value));

            return this;
        }

    }

    private final LazyValue<GloIssueTableRenderer> gloIssueTableRenderer = new LazyValue<>(() -> new GloIssueTableRenderer());

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames.get(columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return GloIssue.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // TODO make editable
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (issues == null) {
            return null;
        }
        List<GloIssue> column = issues.get(columnIndex);
        return (rowIndex >= column.size())
                ? null
                : column.get(rowIndex);
    }

    public void addColumnValues(List<GloIssue> column_issues) {
        this.issues.add(column_issues);
        TableModelEvent e = new TableModelEvent(this, 0, column_issues.size() - 1, issues.size() - 1);
        rowCount = Math.max(rowCount, column_issues.size());
        notifyTableModelListeners(e);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        tableModelListeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        tableModelListeners.remove(l);
    }

    public void notifyTableModelListeners(TableModelEvent e) {
        tableModelListeners.forEach(c -> c.tableChanged(e));
    }

    public void setThisModelToTable(JTable table) {
        table.setModel(this);
        table.setDefaultRenderer(GloIssue.class, gloIssueTableRenderer.get());
    }

}
