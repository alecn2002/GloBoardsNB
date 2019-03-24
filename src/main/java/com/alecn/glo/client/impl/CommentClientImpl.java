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

import com.alecn.glo.client.CommentClient;
import com.alecn.glo.client.CommentFieldsEnum;
import com.alecn.glo.client.dto.CommentRequest;
import com.alecn.glo.sojo.Comment;
import com.alecn.glo.util.GloLogger;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = CommentClient.class)
public class CommentClientImpl extends GenericCardPartClientImpl<Comment, CommentRequest, CommentFieldsEnum> implements CommentClient {

    private static final GloLogger LOGGER = new GloLogger(CommentClientImpl.class);

    private static final CommentFieldsEnum[] DEFAULT_FIELDS_LIST = {CommentFieldsEnum.TEXT};
    static final Collection<CommentFieldsEnum> DEFAULT_FIELDS = Arrays.asList(DEFAULT_FIELDS_LIST);

    private static class BoardCardCommentIdResolver extends BoardCardIdResolver implements Function<WebTarget, WebTarget> {

        public static WebTarget apply(WebTarget t, String comment_id) {
            return t.path(comment_id);
        }

        private final String comment_id;

        public BoardCardCommentIdResolver(String board_id, String card_id, String comment_id) {
            super(board_id, card_id);
            this.comment_id = comment_id;
        }

        @Override
        public WebTarget apply(WebTarget t) {
            return apply(super.apply(t), this.comment_id);
        }
    }

    private static Collection<CommentFieldsEnum> fieldsOrDefaults(Collection<CommentFieldsEnum> fields) {
        return fields == null
                ? DEFAULT_FIELDS
                : fields;
    }

    public CommentClientImpl(String access_key) {
        super(access_key, GLO_PATH_COMMENTS, Comment.class, Comment[].class, LOGGER);
    }

    public CommentClientImpl() {
        this("pcad84c0e279a1a233e1eb31a7a4b20b4ad3ea947"); // TODO remove default access key!
    }

    @Override
    public List<Comment> list(String board_id, String card_id, Collection<CommentFieldsEnum> fields, Integer page, Integer per_page, boolean sort_desc) {
        return super.list(fieldsOrDefaults(fields), false, page, per_page, sort_desc, new BoardCardIdResolver(board_id, card_id), LOGGER, "comments");
    }

    @Override
    public Comment create(String board_id, String card_id, String text) {
        return super.post(Entity.json(new CommentRequest(text)),
                new BoardCardIdResolver(board_id, card_id));
    }

    @Override
    public Comment edit(String board_id, String card_id, final String id, String text) {
        return super.post(Entity.json(new CommentRequest(text)),
                new BoardCardCommentIdResolver(board_id, card_id, id)
                );
    }

    @Override
    public Response delete(String board_id, String card_id, String id) {
        return super.delete(new BoardCardCommentIdResolver(board_id, card_id, id));
    }

}
