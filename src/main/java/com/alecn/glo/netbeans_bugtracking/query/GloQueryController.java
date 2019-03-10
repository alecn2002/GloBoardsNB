/*
 * The MIT License
 *
 * Copyright 2019 anovitsk.
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

import com.alecn.glo.netbeans_bugtracking.GloRepository;
import com.alecn.glo.netbeans_bugtracking.issue.GloIssue;
import com.alecn.glo.sojo.Card;
import com.alecn.glo.sojo.Column;
import com.alecn.glo.util.LazyValue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import lombok.AllArgsConstructor;
import org.netbeans.modules.bugtracking.spi.QueryController;
import org.openide.util.HelpCtx;

/**
 *
 * @author anovitsk
 */
@AllArgsConstructor
public class GloQueryController implements QueryController, ActionListener {

    private static final QueryMode[] PROVIDED_MODES_ARRAY = {QueryMode.VIEW}; // {QueryMode.EDIT, QueryMode.VIEW}; // TODO clarify
    private static final Set<QueryMode> PROVIDED_MODES = new HashSet<>(Arrays.asList(PROVIDED_MODES_ARRAY));

    private static final LazyValue<GloQueryPanel> gloQueryPanel = new LazyValue<>(() -> new GloQueryPanel());

    private final transient GloRepository gloRepository;
    private final transient GloQuery gloQuery;

    @Override
    public boolean providesMode(QueryMode mode) {
        return PROVIDED_MODES.contains(mode);
    }

    @Override
    public JComponent getComponent(QueryMode mode) {
        return gloQueryPanel.get();
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void opened() {
        List<Column> columns = gloRepository.listColumns();
        JTable resultTable = gloQueryPanel.get().resultTable;
        GloQueryResultTableModel tableModel = new GloQueryResultTableModel(columns.stream().map(c -> c.getName()).collect(Collectors.toList()));
        resultTable.setModel(tableModel);
        List<Card> cards = gloRepository.listCards();
        columns.forEach(column -> {
            tableModel.addColumnValues(cards.stream()
            .filter(c -> c.getColumn_id().equals(column.getId()))
            .map(c -> new GloIssue(c, gloRepository))
            .collect(Collectors.toList()));
        });
    }

    @Override
    public void closed() {
        // In most cases nothing to do here
    }

    @Override
    public boolean saveChanges(String name) {
        // TODO GloQueryController.saveChanges()
        return true;
    }

    @Override
    public boolean discardUnsavedChanges() {
        // TODO GloQueryController.discardUnsavedChanges()
        return true;
     }

    @Override
    public boolean isChanged() {
        // TODO GloQueryController.isChanged()
        return true;
    }

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    @Override
    public void addPropertyChangeListener(PropertyChangeListener pl) {
        support.addPropertyChangeListener(pl);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener pl) {
        support.removePropertyChangeListener(pl);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
