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
import com.alecn.glo.sojo.Comment;
import com.alecn.glo.sojo.Description;
import com.alecn.glo.sojo.Label;
import com.alecn.glo.util.GloLogger;
import com.alecn.glo.util.LazyValue;
import com.alecn.glo.util.VisitorHelper;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
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
        return (card.getCompleted_task_count() != null && card.getTotal_task_count() != null && card.getCompleted_task_count() >= card.getTotal_task_count())
                || card.getArchived_date() != null;
    }

    private Card card;
    private final GloRepository repository;

    private final transient LazyValue<GloIssueController> gloRepositoryController;

    public GloIssue(Card card, GloRepository repository) {
        this.card = card;
        this.repository = repository;
        this.gloRepositoryController = new LazyValue<>(() -> new GloIssueController(repository, this));
    }

    public boolean isNew() {
        return _isNew(card);
    }

    public boolean isFinished() {
        return _isFinished(card);
    }

    void setSchedule(IssueScheduleInfo scheduleInfo) {
        // TODO is anything to do here?
    }

    public Date getDueDate() {
        return card.getDue_date();
    }

    public IssueScheduleInfo getSchedule() {
        return null;
    }

    public String getDescriptionText() {
        return VisitorHelper.nvlVisitor(getCard(), (String)null,
                c -> ((Card)c).getDescription(),
                d -> ((Description)d).getText());
     }

    public void refresh() {
        if (card == null || card.getId() == null || card.getBoard_id() == null || repository == null) {
            StringBuilder sb = new StringBuilder("Save requested, but");
            if (card == null) {
                sb.append(" card is NULL ");
            } else {
                if (card.getId() == null) {
                    sb.append(" card ID is NULL");
                }
                if (card.getBoard_id() == null) {
                    sb.append(" board ID is NULL");
                }
            }
            if (repository == null) {
                sb.append(" repository is NULL");
            }
            LOGGER.severe(sb.toString());
            return;
        }
        // TODO do it in proper thread-safe way
        this.card = repository.getCardService().get(card.getBoard_id(), card.getId());
    }

    public void save() {
        // TODO check if changed
        if (card.getBoard_id() == null) {
            card.setBoard_id(repository.getBoardId());
        }
        if (card == null || card.getBoard_id() == null || card.getColumn_id()== null || repository == null) {
            StringBuilder sb = new StringBuilder("Save requested, but");
            if (card == null) {
                sb.append(" Card is NULL ");
            } else {
                if (card.getBoard_id() == null) {
                    sb.append(" Board ID is NULL");
                }
                if (card.getColumn_id() == null) {
                    sb.append(" Column ID is NULL");
                }
            }
            if (repository == null) {
                sb.append(" Repository is NULL");
            }
            LOGGER.severe(sb.toString());
            return;
        }
        // TODO do it in proper thread-safe way
        if (card.getId() == null) {
            // Creating new card
            this.card = repository.getCardService().create(card);
        } else {
            // Saving existing card
            this.card = repository.getCardService().edit(card);
        }

    }

    public Response delete() {
        if (card == null || card.getId() == null || card.getBoard_id() == null || repository == null) {
            LOGGER.severe("Save requested, but either card, or card or board id, or repository is null");
            return Response.noContent().build();
        }
        // TODO do it in proper thread-safe way
        return repository.getCardService().delete(card);
    }

    public Collection<Comment> getComments() {
        return repository.getCommentService().list(card.getBoard_id(), card.getId());
    }


    public Collection<Label> getLabels() {
        return repository.chooseLabels(card.getLabels());
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
