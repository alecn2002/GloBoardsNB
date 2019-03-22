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
package com.alecn.glo.netbeans_bugtracking.providers.impl;

import com.alecn.glo.netbeans_bugtracking.providers.GloIssueProvider;
import com.alecn.glo.netbeans_bugtracking.issue.GloIssue;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import org.netbeans.modules.bugtracking.spi.IssueController;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author anovitsk
 */
@ServiceProvider(service = GloIssueProvider.class)
public class GloIssueProviderImpl  implements GloIssueProvider {

    @Override
    public String getDisplayName(GloIssue i) {
        return i.getCard().getName();
    }

    @Override
    public String getTooltip(GloIssue i) {
        return i.getCard().getName();
    }

    @Override
    public String getID(GloIssue i) {
        return i.getCard().getId();
    }

    @Override
    public Collection<String> getSubtasks(GloIssue i) {
        return new ArrayList<>(); // TODO maybe GLO will provide interface to subtasks in the future
    }

    @Override
    public String getSummary(GloIssue i) {
        return i.getDescriptionText();
    }

    @Override
    public boolean isNew(GloIssue i) {
        return i.isNew();
    }

    @Override
    public boolean isFinished(GloIssue i) {
        return i.isFinished();
    }

    @Override
    public boolean refresh(GloIssue i) {
        return true;
    }

    @Override
    public void addComment(GloIssue i, String comment, boolean close) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void attachFile(GloIssue i, File file, String description, boolean isPatch) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IssueController getController(GloIssue i) {
        return i.getGloRepositoryController().get();
    }

    @Override
    public void removePropertyChangeListener(GloIssue i, PropertyChangeListener listener) {
        i.removePropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(GloIssue i, PropertyChangeListener listener) {
        i.addPropertyChangeListener(listener);
    }

}
