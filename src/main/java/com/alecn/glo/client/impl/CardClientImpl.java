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
package com.alecn.glo.client.impl;

import com.alecn.glo.client.CardClient;
import com.alecn.glo.client.CardFieldsEnum;
import com.alecn.glo.client.dto.CardRequest;
import com.alecn.glo.sojo.Card;
import com.alecn.glo.sojo.Description;
import com.alecn.glo.sojo.PartialLabel;
import com.alecn.glo.sojo.PartialUser;
import com.alecn.glo.util.GloLogger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = CardClient.class)
public class CardClientImpl extends GenericClientImpl<Card, CardRequest, CardFieldsEnum> implements CardClient {

    private static final GloLogger LOGGER = new GloLogger(CardClientImpl.class);

    private static final CardFieldsEnum[] DEFAULT_FIELDS_LIST = {CardFieldsEnum.BOARD_ID, CardFieldsEnum.COLUMN_ID, CardFieldsEnum.NAME};
    static final Collection<CardFieldsEnum> DEFAULT_FIELDS = Arrays.asList(DEFAULT_FIELDS_LIST);

    public CardClientImpl(String access_key) {
        super(access_key, GLO_PATH_CARDS, Card.class, Card[].class, LOGGER);
    }

    public CardClientImpl() {
        this("pcad84c0e279a1a233e1eb31a7a4b20b4ad3ea947"); // TODO remove default access key!
    }

    @AllArgsConstructor
    private static class BoardIdResolver implements Function<WebTarget, WebTarget> {
        private final String board_id;

        public static WebTarget apply(WebTarget t, String board_id) {
            return t.resolveTemplate(GloConstants.GLO_PATH_BOARD_ID, board_id);
        }

        @Override
        public WebTarget apply(WebTarget t) {
            return apply(t, this.board_id);
        }
    }

    private static Collection<CardFieldsEnum> fieldsOrDefaults(Collection<CardFieldsEnum> fields) {
        return fields == null
                ? DEFAULT_FIELDS
                : fields;
    }

    private static Entity<CardRequest> buildCardRequestEntity(String column_id, String name, Integer position, String description, List<PartialUser> assignees, List<PartialLabel> labels, Date due_date) {
        return Entity.json(CardRequest.builder()
                    .column_id(column_id)
                    .name(name)
                    .position(position)
                    .description(new Description(description))
                    .assignees(assignees)
//                    .labels(labels)
                    .labels(new ArrayList<>()) // TODO proper labels processing
                    .due_date(due_date)
                    .build());
    }

    @Override
    public List<Card> list(String board_id, Collection<CardFieldsEnum> fields, boolean archived, Integer page, Integer per_page, boolean sort_desc) {
        return super.list(fieldsOrDefaults(fields), archived, page, per_page, sort_desc, new BoardIdResolver(board_id), LOGGER, "cards");
    }

    @Override
    public Card create(String board_id, String column_id, String name, Integer position, String description, List<PartialUser> assignees, List<PartialLabel> labels, Date due_date) {
        return super.post(buildCardRequestEntity(column_id, name, position, description, assignees, labels, due_date),
                new BoardIdResolver(board_id));
    }

    @Override
    public Card get(String board_id, String id, Collection<CardFieldsEnum> fields) {
        return super.get(id, fieldsOrDefaults(fields), new BoardIdResolver(board_id), LOGGER, "card");
    }

    @Override
    public Card edit(String board_id, String column_id, String id, String name, Integer position, String description, List<PartialUser> assignees, List<PartialLabel> labels, Date due_date) {
        return super.post(buildCardRequestEntity(column_id, name, position, description, assignees, labels, due_date),
                (wt) -> {
                    return BoardIdResolver.apply(wt, board_id)
                                          .path(id);
                });
    }

    @Override
    public Response delete(String board_id, String id) {
        return super.delete((wt) -> {
            return BoardIdResolver.apply(wt, board_id)
                           .path(id);
        });
    }

}
