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
import com.alecn.glo.util.GloLogger;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;
import javax.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.netbeans.modules.bugtracking.spi.IssueScheduleInfo;

/**
 *
 * @author anovitsk
 */
@Getter
@AllArgsConstructor
public class GloIssue {

    private static final GloLogger LOGGER = new GloLogger(GloIssue.class);

    private static boolean _isNew(Card card) {
        return card.getUpdated_date() == null
                && card.getArchived_date() == null;
    }

    private static boolean _isFinished(Card card) {
        return card.getCompletedTaskCount() >= card.getTotalTaskCount()
                || card.getArchived_date() != null;
    }

    private Card card;
    private final GloRepository repository;

    public boolean isNew() {
        return _isNew(card);
    }

    public boolean isFinished() {
        return _isFinished(card);
    }

    void setSchedule(IssueScheduleInfo scheduleInfo) {
        // TODO is anything to do here?
    }

    Date getDueDate() {
        return card.getDue_date(); // Calendar.getInstance().getTime(); // FIXME hmmmmmm...
    }

    IssueScheduleInfo getSchedule() {
        return null;
    }

    public void refresh() {
        if (card == null || card.getId() == null || card.getBoardId() == null || repository == null) {
            LOGGER.severe("Refresh requested, but either card, or card or board id, or repository is null");
            return;
        }
        // TODO do it in proper thread-safe way
        this.card = repository.getCardService().get(card.getBoardId(), card.getId());
    }

    public void save() {
        // TODO check if changed
        if (card == null || card.getId() == null || card.getBoardId() == null || repository == null) {
            LOGGER.severe("Save requested, but either card, or card or board id, or repository is null");
            return;
        }
        // TODO do it in proper thread-safe way
        this.card = repository.getCardService().edit(card);
    }

    public Response delete() {
        if (card == null || card.getId() == null || card.getBoardId() == null || repository == null) {
            LOGGER.severe("Save requested, but either card, or card or board id, or repository is null");
            return Response.noContent().build();
        }
        // TODO do it in proper thread-safe way
        return repository.getCardService().delete(card);
    }
    //
    // Change Support
    //
    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}
