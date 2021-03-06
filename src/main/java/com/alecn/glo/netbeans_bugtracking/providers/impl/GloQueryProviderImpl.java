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

import com.alecn.glo.netbeans_bugtracking.issue.GloIssue;
import com.alecn.glo.netbeans_bugtracking.providers.GloQueryProvider;
import com.alecn.glo.netbeans_bugtracking.query.GloQuery;
import org.netbeans.modules.bugtracking.spi.QueryController;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = GloQueryProvider.class)
public class GloQueryProviderImpl implements GloQueryProvider {

    @Override
    public String getDisplayName(GloQuery q) {
        return q.getDisplayName();
    }

    @Override
    public String getTooltip(GloQuery q) {
        return q.getTooltip();
    }

    @Override
    public QueryController getController(GloQuery q) {
        return q.getController();
    }

    @Override
    public boolean canRemove(GloQuery q) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(GloQuery q) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean canRename(GloQuery q) {
        return false; // TODO Update when real query editor will be implemented
    }

    @Override
    public void rename(GloQuery q, String newName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setIssueContainer(GloQuery q, IssueContainer<GloIssue> c) {
        q.setIssueContainer(c);
    }

    @Override
    public void refresh(GloQuery q) {
        q.refresh();
    }

}
