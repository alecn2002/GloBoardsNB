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
package com.alecn.glo.service.impl;

import com.alecn.glo.client.CardClient;
import com.alecn.glo.client.CardFieldsEnum;
import com.alecn.glo.client.impl.CardClientImpl;
import com.alecn.glo.service.CardService;
import com.alecn.glo.sojo.Card;
import com.alecn.glo.sojo.Description;
import com.alecn.glo.sojo.Label;
import com.alecn.glo.sojo.PartialLabel;
import com.alecn.glo.util.VisitorHelper;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;
import org.openide.util.Lookup;


public class CardServiceImpl implements CardService {

    private static final CardClient CARD_CLIENT = Lookup.getDefault().lookup(CardClient.class);

    private static final List<CardFieldsEnum> ALL_FIELDS = Arrays.asList(CardFieldsEnum.values());

    private final CardClient cardClient;

    CardServiceImpl() {
        this.cardClient = CARD_CLIENT;
    }

    public CardServiceImpl(String accessKey) {
        this.cardClient = new CardClientImpl(accessKey);
    }

    @Override
    public Card get(String boardId, String id, Collection<CardFieldsEnum> fields) {
        return cardClient.get(boardId, id, fields);
    }

    @Override
    public Card get(String boardId, String id) {
        return get(boardId, id, ALL_FIELDS);
    }

    @Override
    public Card edit(Card card) {
        if (card == null || card.getBoard_id() == null || card.getId() == null) {
            return null; // TODO better error processing
        }
        return cardClient.edit(card.getBoard_id(), card.getColumn_id(), card.getId(), card.getName(), card.getPosition(), card.getDescription().getText(),
                card.getAssignees(),
                card.getLabels().stream().map(l -> PartialLabel.builder().id(l.getId()).build()).collect(Collectors.toList()),
                card.getDue_date());
    }

    @Override
    public Response delete(String boardId, String id) {
        if (boardId == null || id == null) {
            return Response.status(422, "Unprocessable Entity: board or card ID are null").build();
        }
        return cardClient.delete(boardId, id);
    }

    @Override
    public Response delete(Card card) {
        if (card == null || card.getBoard_id() == null || card.getId() == null) {
            return Response.status(422, "Unprocessable Entity: card is null").build();
        }
        return delete(card.getBoard_id(), card.getId());
    }

    @Override
    public Card create(Card card) {
        return cardClient.create(card.getBoard_id(),
                card.getColumn_id(),
                card.getName(),
                card.getPosition(),
                VisitorHelper.nvlVisitor(card, "",
                        c -> ((Card)c).getDescription(),
                        d -> ((Description)d).getText()),
                card.getAssignees(),
                VisitorHelper.nvlVisitor(card, null,
                        c -> ((Card)c).getLabels(),
                        ll -> ((List<Label>)ll).stream().map(l -> PartialLabel.builder().id(l.getId()).build()).collect(Collectors.toList())),
                card.getDue_date());
    }

}
