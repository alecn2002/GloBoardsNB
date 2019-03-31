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

import com.alecn.glo.netbeans_bugtracking.repository.GloRepository;
import com.alecn.glo.netbeans_bugtracking.issue.GloIssue;
import com.alecn.glo.sojo.BoardMember;
import com.alecn.glo.sojo.Card;
import com.alecn.glo.sojo.Column;
import com.alecn.glo.util.GloLogger;
import com.alecn.glo.util.LazyValue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.swing.JComponent;
import javax.swing.JTable;
import lombok.AllArgsConstructor;
import org.netbeans.modules.bugtracking.spi.QueryController;
import org.openide.util.HelpCtx;

/**
 *
 * @author <a href="mailto:alecn2002@gmail.com">AlecN</a>
 */
@AllArgsConstructor
public class GloQueryController implements QueryController, ActionListener {

    private static final GloLogger LOGGER = new GloLogger(GloQueryController.class);

    private static final QueryMode[] PROVIDED_MODES_ARRAY = {QueryMode.VIEW}; // {QueryMode.EDIT, QueryMode.VIEW}; // TODO clarify
    private static final Set<QueryMode> PROVIDED_MODES = new HashSet<>(Arrays.asList(PROVIDED_MODES_ARRAY));

    private static final LazyValue<GloQueryPanel> gloQueryPanel = new LazyValue<>(() -> new GloQueryPanel());

    @AllArgsConstructor
    private static class GloIssueVisRenderer implements Function<GloIssue, String> {
        // TODO this should go to UI...
        private final GloRepository gloRepository;

        @Override
        public String apply(GloIssue t) {
            if (t == null) {
                return "";
            }

            Card card = t.getCard();

            if (card == null) {
                return "";
            }

            StringBuilder sb = new StringBuilder("<html><body>");

            sb.append("<h3>").append(card.getName()).append("</h3>");

            if (card.getAssignees() == null || card.getAssignees().isEmpty()) {
                sb.append("<i>Not assigned</i><br>");
            } else {
                sb.append("<i>Assigned to: ")
                        .append(String.join(", ", card.getAssignees().stream()
                                .map(pu -> {
                                    BoardMember assignee = gloRepository.getBoardMemberById(pu.getId());
                                    return assignee.getName() == null
                                            ? assignee.getUsername()
                                            : assignee.getName();
                                        })
                                .collect(Collectors.toList())))
                        .append("</i><br>");
            }
            Integer tasksNo = card.getTotal_task_count();
            sb.append(String.format("Tasks: %d", tasksNo == null ? 0 : tasksNo));

            sb.append("</body></html>");
            return sb.toString();
        }

    }

    private final GloIssueVisRenderer GLO_ISSUE_VIS_RENDERER;

    private final transient GloRepository gloRepository;
    private final transient GloQuery gloQuery;

    public GloQueryController(GloRepository gloRepository, GloQuery gloQuery) {
        this(new GloIssueVisRenderer(gloRepository), gloRepository, gloQuery);
    }

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
        gloQueryPanel.get().qSearchButton.addActionListener(this);
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
        LOGGER.info("GloQueryController: actionPerformed(%s)\n", e.getActionCommand());
        if (e.getSource().equals(gloQueryPanel.get().qSearchButton)) {
            onSearch();
        }
    }
    private void onSearch() {
        LOGGER.info("Retrieving list of columns...\n");
        Collection<Column> columns = gloRepository.getColumns();
        LOGGER.info("Got %d columns\n", columns.size());
        JTable resultTable = gloQueryPanel.get().resultTable;
        GloQueryResultTableModel tableModel = new GloQueryResultTableModel(columns.stream().map(c -> c.getName()).collect(Collectors.toList()),
                GLO_ISSUE_VIS_RENDERER,
                GLO_ISSUE_VIS_RENDERER
        );
        tableModel.setThisModelToTable(resultTable);
        LOGGER.info("Retrieving list of cards...\n");
        gloRepository.refreshCards();
        gloQuery.refresh();
        LOGGER.info("Got %d cards\n", gloQuery.getIssues().size());
        columns.forEach(column -> {
            tableModel.addColumnValues(gloQuery.getIssues().stream()
            .filter(c -> c.getCard().getColumn_id().equals(column.getId()))
            .collect(Collectors.toList()));
        });
    }

}
