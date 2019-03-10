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
import lombok.Getter;
import org.netbeans.modules.bugtracking.spi.QueryController;
import org.netbeans.modules.bugtracking.spi.QueryProvider;

/**
 *
 * @author anovitsk
 */
@Getter
public class GloQuery {

    private QueryProvider.IssueContainer<GloIssue> delegateContainer;

    private final transient GloRepository gloRepository;
    private final transient GloQueryController gloQueryController;

    public GloQuery(GloRepository gloRepository) {
        this.gloRepository = gloRepository;
        this.gloQueryController = new GloQueryController(gloRepository, this);
    }

    public void setIssueContainer(QueryProvider.IssueContainer<GloIssue> ic) {
        delegateContainer = ic;
    }

    public String getDisplayName() {
        return "GLo Query"; // FIXME !!! GloQuery.getDisplayName()
    }

    public String getTooltip() {
        return "GLO Query Tooltip"; // FIXME !!! GloQuery.getTooltip()
    }

    public QueryController getController() {
        return gloQueryController;
    }

}
