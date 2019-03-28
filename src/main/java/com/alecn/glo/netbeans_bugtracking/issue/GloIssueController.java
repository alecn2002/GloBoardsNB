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
package com.alecn.glo.netbeans_bugtracking.issue;

import com.alecn.glo.netbeans_bugtracking.repository.GloRepository;
import com.alecn.glo.sojo.Card;
import com.alecn.glo.sojo.Column;
import com.alecn.glo.sojo.Description;
import com.alecn.glo.ui.NameIdListModel;
import com.alecn.glo.util.GloLogger;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import org.netbeans.modules.bugtracking.spi.IssueController;
import org.openide.util.HelpCtx;

/**
 *
 * @author anovitsk
 */
public class GloIssueController implements IssueController, ActionListener, PropertyChangeListener  {

    private static final GloLogger LOGGER = new GloLogger(GloIssueController.class);

    private final GloIssue gloIssue;
    private final GloRepository gloRepository;
    private final GloIssuePanel component;

    private boolean changed;

    @SuppressWarnings("LeakingThisInConstructor")
    public GloIssueController(GloRepository gloRepository, GloIssue gloIssue) {
        this.gloIssue = gloIssue;
        this.gloRepository = gloRepository;
        this.component = new GloIssuePanel();
        this.changed = false;

        component.deleteButton.addActionListener(this);
        component.refreshButton.addActionListener(this);
        component.saveButton.addActionListener(this);
    }

    public GloIssueController(GloRepository gloRepository) {
        this(gloRepository, gloRepository.createNewIssue());
    }

    @Override
    public JComponent getComponent() {
        return component;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(getClass().getName());
    }

    @Override
    public void opened() {
        populateComponent();
    }

    @Override
    public void closed() {
        // Usually nothing to do here
    }

    @Override
    public boolean saveChanges() {
        try {
            gloIssue.save();
            changed = false;
            return true;
        } catch (Error | Exception ex) {
            LOGGER.severe("Error saving card: %s", ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean discardUnsavedChanges() {
        try {
            gloIssue.refresh();
            changed = false;
            return true;
        } catch (Error | Exception ex) {
            LOGGER.severe("Error refreshing card: %s", ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean isChanged() {
        return changed;
    }

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        support.removePropertyChangeListener(l);
    }

    private static <T> T nvl(T v, T deflt) {
        return v == null
                ? deflt
                : v;
    }

    private static String nvl(String v) {
        return nvl(v, "");
    }

    private static final Description EMPTY_DESCR = new Description("");

    private void populateComponent() {
        Card card = gloIssue.getCard();

        component.titleField.setText(nvl(card.getName()));
        component.nameField.setText(nvl(card.getName()));
        component.descriptionField.setText(nvl(nvl(card.getDescription(), EMPTY_DESCR).getText()));
        component.idField.setText(nvl(card.getId()));

        String selectedColumnId = card.getColumn_id();
        Collection<Column> columns = gloRepository.getColumns();
        Column selectedColumn = (selectedColumnId == null || selectedColumnId.isEmpty())
                ? null
                : columns.stream().filter(c -> c.getId().equals(selectedColumnId)).findFirst().get();
        NameIdListModel model = new NameIdListModel<>(
                Arrays.asList(columns.toArray(new Column[0])),
                (Column c) -> c.getName(),
                (Column c) -> c.getId()
        );
        model.setSelectedItem(selectedColumn);
        model.setThisModelToControl(component.columnList);

        String labelsText = String.join(" ",
                gloRepository.chooseLabels(card.getLabels())
                        .stream()
                        .map(l -> l.getName())
                        .collect(Collectors.toList()));
//        String labelsText = String.join(" ", card.getLabels().stream().map(l -> l.getId()).collect(Collectors.toList()));
        LOGGER.info("Going to add %d label(s)", card.getLabels().size());
        component.getLabelsField().setText(labelsText);

        // Populate comments
        Arrays.asList(component.commentsPanel.getComponents()).forEach(c -> component.commentsPanel.remove(c));
        Insets margin = new Insets(4, 0, 0, 0);
        gloRepository.getCommentService().list(card.getBoard_id(), card.getId()).forEach(cmt -> {
            LOGGER.info("Adding comment: '%s'", cmt.getText());
            JTextArea ta = new JTextArea(cmt.getText());
            ta.setSize(component.commentsPanel.getWidth(), ta.getHeight());
            ta.setMargin(margin);
            component.commentsPanel.add(ta);
        });
    }

    private void populateCard() {
        Card card = gloIssue.getCard();

        card.setColumn_id(component.getSelectedColumnId());
        card.setName(component.getIssueName());
        card.setDescription(new Description(component.getDescription()));

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(component.nameField)) {
            on_name_changed();
        } else if (e.getSource().equals(component.deleteButton)) {
            on_delete();
        } else if (e.getSource().equals(component.refreshButton)) {
            on_refresh();
        } else if (e.getSource().equals(component.saveButton)) {
            on_save();
        }
    }

    private void on_name_changed() {
        component.titleField.setText(component.nameField.getText());
    }

    private void on_delete() {
        // TODO ask before deleting
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void on_refresh() {
        // TODO ask before refreshing
        gloIssue.refresh();
        populateComponent();
        this.changed = false;
    }

    private void on_save() {
        // TODO ask before saving
        populateCard();
        gloIssue.save();
        populateComponent();
        this.changed = false;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.changed = true;
    }
}
