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
package com.alecn.glo.client.impl;

import com.alecn.glo.client.BoardsClient;
import com.alecn.glo.client.EBoardFields;
import com.alecn.glo.sojo.Board;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.client.WebTarget;
import lombok.Getter;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author AlecN <alecn2002@gmail.com>
 */
@ServiceProvider(service = BoardsClient.class)
public class BoardsClientImpl extends GenericClientImpl<Board> implements BoardsClient {

    @Getter
    enum EBoardsParams {
        FIELDS("fields"),
        ARCHIVED("archived"),
        PAGE("page"),
        PER_PAGE("per_page"),
        SORT("sort");

        private final String query_str;

        EBoardsParams(String query_str) {
            this.query_str = query_str;
        }
    }

    private static final EBoardFields[] DEFAULT_FIELDS_LIST = {EBoardFields.NAME};
    static final Collection<EBoardFields> DEFAULT_FIELDS = Arrays.asList(DEFAULT_FIELDS_LIST);

    public BoardsClientImpl() {
        super("boards", Board.class, Board[].class);
    }

    @Override
    public List<Board> get(final Collection<EBoardFields> fields, boolean archived, Integer page, Integer per_page, boolean sort_desc) {
        return super.getList((WebTarget t) -> {
            Collection<EBoardFields> myFields = fields == null
                    ? DEFAULT_FIELDS
                    : fields;
            for (EBoardFields field : myFields) {
                t = t.queryParam(EBoardsParams.FIELDS.getQuery_str(), field.getRest_name());
            }
            if (archived) {
                t = t.queryParam(EBoardsParams.ARCHIVED.getQuery_str(), "true");
            }
            if (page != null && per_page != null) {
                t = t.queryParam(EBoardsParams.PAGE.getQuery_str(), page)
                        .queryParam(EBoardsParams.PER_PAGE.getQuery_str(), per_page);
            }
            if (sort_desc) {
                t = t.queryParam(EBoardsParams.SORT.getQuery_str(), "desc");
            }
            return t;
        });
    }

    @Override
    public List<Board> get() {
        return get(null, false, null, null, false);
    }
}
