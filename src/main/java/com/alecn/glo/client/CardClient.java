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
package com.alecn.glo.client;

import com.alecn.glo.sojo.Card;
import com.alecn.glo.sojo.PartialLabel;
import com.alecn.glo.sojo.PartialUser;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.Response;

/**
 *
 * @author anovitsk
 */
public interface CardClient {
    List<Card> list(String board_id, Collection<CardFieldsEnum> fields, boolean archived, Integer page, Integer per_page, boolean sort_desc);

    Card create(String board_id, String column_id, String name, Integer position, String description, List<PartialUser> assignees, List<PartialLabel> labels, Date due_date);

    Card get(String board_id, String id, Collection<CardFieldsEnum> fields);

    Card edit(String board_id, String column_id, String id, String name, Integer position, String description, List<PartialUser> assignees, List<PartialLabel> labels, Date due_date);

    Response delete(String board_id, String id);
}
