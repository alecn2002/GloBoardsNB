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
package com.alecn.glo.netbeans_bugtracking;

import com.alecn.glo.GloConfig;
import com.alecn.glo.sojo.Board;
import com.alecn.glo.ui.NameIdListModel;
import com.alecn.glo.util.GloLogger;
import com.alecn.glo.util.OeWriter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import lombok.Getter;
import org.netbeans.modules.bugtracking.spi.RepositoryController;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

/**
 *
 * @author alecn
 */
@Getter
public class GloRepositoryController implements RepositoryController, DocumentListener, ActionListener {

    private static final GloLogger LOGGER = new GloLogger(GloRepositoryController.class);


    private static final GloConfig gloConfig = Lookup.getDefault().lookup(GloConfig.class);

    private static final OeWriter oeWriter = gloConfig.getOeWriter();

    private final transient GloRepository gloRepository;
    private final transient GloRepositoryPanel panel;

    private enum V_STATE {
        NOT_VERIFIED,
        VERIFYING,
        VERIFIED;
    }

    private V_STATE verified = V_STATE.NOT_VERIFIED;

    private String errorMessage = "";

    @SuppressWarnings("LeakingThisInConstructor")
    GloRepositoryController(GloRepository gloRepository) {
        this.gloRepository = gloRepository;
        this.panel = new GloRepositoryPanel(this);

        panel.gloRepoAccessKey.getDocument().addDocumentListener(this);
        panel.gloRepositoryName.getDocument().addDocumentListener(this);

        panel.gloRepoBoard.addActionListener(this);

        panel.verifyButton.addActionListener(this);
    }

    @Override
    public JComponent getComponent() {
        return panel;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(getClass().getName());
    }

    @Override
    public boolean isValid() {
        StringBuilder sb = new StringBuilder();

        if (panel.getNameText().isEmpty()) {
            sb.append("Name can not be empty\n");
        }
        if (panel.getAccessKey().isEmpty()) {
            sb.append("Access key can not be empty\n");
        }
        if (sb.toString().isEmpty() && verified == V_STATE.NOT_VERIFIED) {
            sb.append("Access string must be verified\n");
        }
        if (verified == V_STATE.VERIFIED && panel.getSelectedBoard() == null) {
            sb.append("Board must be chosen\n");
        }
        errorMessage = sb.toString();
        return errorMessage.isEmpty();
    }

    @Override
    public void populate() {
        assert SwingUtilities.isEventDispatchThread();

        panel.setNameText(gloRepository.getDisplayName()); // gloRepositoryName.setText(gloRepository.getDisplayName());
        panel.setAccessKey(gloRepository.getAccessKey());

//        panel.gloRepoBoard.setSelectedItem(gloRepository.);
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void applyChanges() {
        String boardId = null;
        String boardName = null;
        Board selectedBoard = panel.getSelectedBoard();
        if (selectedBoard != null) {
            boardId = selectedBoard.getId();
            boardName = selectedBoard.getName();
        }
        gloRepository.setInfoValues(panel.getNameText(),
                panel.getAccessKey(),
                boardId,
                boardName);
    }

    @Override
    public void cancelChanges() {
        // FIXME is anything to do here?
    }
    //
    // Change Support
    //
    private transient final ChangeSupport changeSupport = new ChangeSupport(this);
    @Override
    public void addChangeListener(ChangeListener cl) {
        changeSupport.addChangeListener(cl);
    }

    @Override
    public void removeChangeListener(ChangeListener cl) {
        changeSupport.removeChangeListener(cl);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        changeSupport.fireChange();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        changeSupport.fireChange();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        changeSupport.fireChange();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        oeWriter.outWrite(o -> o.printf("GloRepositoryController: actionPerformed(%s)\n", e.getActionCommand()));
        LOGGER.info("GloRepositoryController: actionPerformed(%s)\n", e.getActionCommand());
        if (e.getSource().equals(panel.verifyButton)) {
            onVerify();
            changeSupport.fireChange();
        } else if (e.getSource().equals(panel.gloRepoBoard)) {
            changeSupport.fireChange();
        }
    }

    private void onVerify() {
        LOGGER.info("onVerify()...\n");
        verified = V_STATE.VERIFYING;
        if (!isValid()) {
            LOGGER.warning("... and is not valid\n");
            return;
        }
        applyChanges();
        // TODO make it async
        errorMessage = "";
        try {
            LOGGER.info("Starting to retrieve boards list...\n");
            List<Board> boards = gloRepository.getBoardsList();
            LOGGER.info("%d boards retrieved\n", boards.size());
            NameIdListModel<Board> model = new NameIdListModel<>(boards, b -> b.getName(), b -> b.getId());
            panel.gloRepoBoard.setModel(model);
            panel.gloRepoBoard.setRenderer(model.rendererFactory());
            LOGGER.info("and appended to boards list in combobox\n");
            verified = V_STATE.VERIFIED;
            isValid();
        } catch (Error e) {
            errorMessage = "Couldn't fetch boards list: " + e.getMessage();
        }
    }

}
