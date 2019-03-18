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

import com.alecn.glo.netbeans_bugtracking.repository.GloRepository;
import com.alecn.glo.netbeans_bugtracking.issue.GloIssue;
import com.alecn.glo.netbeans_bugtracking.providers.GloRepositoryProvider;
import com.alecn.glo.netbeans_bugtracking.query.GloQuery;
import com.alecn.glo.sojo.Card;
import com.alecn.glo.sojo.Description;
import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.netbeans.modules.bugtracking.spi.RepositoryController;
import org.netbeans.modules.bugtracking.spi.RepositoryInfo;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = GloRepositoryProvider.class)
public class GloRepositoryProviderImpl implements GloRepositoryProvider {

//    private static final GloConfig gloConfig = Lookup.getDefault().lookup(GloConfig.class);

    private final Set<GloIssue> newIssues = Collections.synchronizedSet(new HashSet<>());

    @Override
    public RepositoryInfo getInfo(GloRepository r) {
        return r.getRepositoryInfo();
    }

    @Override
    public Image getIcon(GloRepository r) {
        return r.getIconImage();
    }

    @Override
    public Collection<GloIssue> getIssues(GloRepository r, String... ids) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removed(GloRepository r) {
        r.removed();
    }

    @Override
    public RepositoryController getController(GloRepository r) {
        return r.getController();
    }

    @Override
    public GloQuery createQuery(GloRepository r) {
        return r.createQuery();
    }

    private GloIssue createIssue(GloIssue gloIssue) {
        newIssues.add(gloIssue);
        return gloIssue;
    }

    @Override
    public GloIssue createIssue(GloRepository r) {
        return createIssue(new GloIssue(new Card(), r));
    }

    @Override
    public GloIssue createIssue(GloRepository r, String summary, String description) {
        return createIssue(new GloIssue(Card.builder().name(summary).description(new Description(description)).build(), r));
    }

    @Override
    public Collection<GloQuery> getQueries(GloRepository r) {
        return r.getQueries();
    }

    @Override
    public Collection<GloIssue> simpleSearch(GloRepository r, String criteria) {
        return r.getBoardService().listCards(r.getBoardId())
                .stream()
                .map(c -> new GloIssue(c, r))
                .collect(Collectors.toList());
    }

    @Override
    public boolean canAttachFiles(GloRepository r) {
        // TODO implement files attachment
        return false;
    }

    @Override
    public void removePropertyChangeListener(GloRepository r, PropertyChangeListener listener) {
        r.removePropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(GloRepository r, PropertyChangeListener listener) {
        r.addPropertyChangeListener(listener);
    }

}
