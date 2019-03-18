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
import com.alecn.glo.sojo.Card;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.netbeans.modules.bugtracking.spi.QueryProvider;

/**
 *
 * @author anovitsk
 */
@Getter
public class GloQuery {

    @Setter
    private QueryProvider.IssueContainer<GloIssue> issueContainer;

    private final transient GloRepository gloRepository;
    private final transient GloQueryController controller;

    private final Set<GloIssue> issues;

    public GloQuery(GloRepository gloRepository) {
        this.gloRepository = gloRepository;
        this.controller = new GloQueryController(gloRepository, this);

        this.issues = new LinkedHashSet<>();
    }

    public String getDisplayName() {
        return "GLo Query"; // FIXME !!! GloQuery.getDisplayName()
    }

    public String getTooltip() {
        return "GLO Query Tooltip"; // FIXME !!! GloQuery.getTooltip()
    }

    public void refresh() {
        issues.clear();
        List<Card> cards = gloRepository.listCards();
        issues.addAll(cards.stream().map(c -> new GloIssue(c, gloRepository)).collect(Collectors.toList()));
        if (issueContainer != null) {
            issueContainer.clear();
            issueContainer.add(issues.toArray(new GloIssue[0]));
        }
    }

}
