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

import com.alecn.glo.netbeans_bugtracking.GloRepository;
import com.alecn.glo.sojo.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author anovitsk
 */
@Getter
@AllArgsConstructor
public class GloIssue {

    private static boolean _isNew(Card card) {
        return card.getUpdated_date() == null
                && card.getArchived_date() == null;
    }

    private static boolean _isFinished(Card card) {
        return card.getCompleted_task_count() >= card.getTotal_task_count()
                || card.getArchived_date() != null;
    }

    private final Card card;
    private final GloRepository repository;

    public boolean isNew() {
        return _isNew(card);
    }

    public boolean isFinished() {
        return _isFinished(card);
    }
}
