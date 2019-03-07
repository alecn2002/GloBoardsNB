/*
 * The MIT License
 *
 * Copyright 2019 alecn.
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

import com.alecn.glo.client.CommentClient;
import com.alecn.glo.client.CommentFieldsEnum;
import com.alecn.glo.client.impl.CommentClientImpl;
import com.alecn.glo.service.CommentService;
import com.alecn.glo.sojo.Comment;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.core.Response;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = CommentService.class)
public class CommentServiceImpl implements CommentService {

    private static final CommentClient COMMENT_CLIENT = Lookup.getDefault().lookup(CommentClient.class);

    private final CommentClient commentClient;

    public CommentServiceImpl() {
        commentClient = COMMENT_CLIENT;
    }

    public CommentServiceImpl(String glo_api_url) {
        commentClient = new CommentClientImpl(glo_api_url);
    }

    @Override
    public List<Comment> list(String board_id, String card_id, Collection<CommentFieldsEnum> fields, Integer page, Integer per_page, boolean sort_desc) {
        return commentClient.list(board_id, card_id, fields, page, per_page, sort_desc);
    }

    @Override
    public List<Comment> list(String board_id, String card_id) {
        return list(board_id, card_id, null, null, null, false);
    }

    @Override
    public Comment create(String board_id, String card_id, String text) {
        return commentClient.create(board_id, card_id, text);
    }

    @Override
    public Comment edit(Comment comment) {
        return commentClient.edit(comment.getBoardId(), comment.getCardId(), comment.getId(), comment.getText());
    }

    @Override
    public Comment edit(String board_id, String card_id, String id, String text) {
        return commentClient.edit(board_id, card_id, id, text);
    }

    @Override
    public Response delete(Comment comment) {
        return commentClient.delete(comment.getBoardId(), comment.getCardId(), comment.getId());
    }

    @Override
    public Response delete(String board_id, String card_id, String id) {
        return commentClient.delete(board_id, card_id, id);
    }

}
