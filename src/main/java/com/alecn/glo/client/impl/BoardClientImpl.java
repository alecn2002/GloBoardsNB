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

import com.alecn.glo.client.BoardClient;
import com.alecn.glo.client.EBoardFields;
import com.alecn.glo.sojo.Board;
import java.util.Arrays;
import java.util.Collection;
import javax.ws.rs.client.WebTarget;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author AlecN <alecn2002@gmail.com>
 */
@ServiceProvider(service = BoardClient.class)
public class BoardClientImpl extends GenericClientImpl<Board> implements BoardClient {

    public BoardClientImpl() {
        super(GLO_PATH_BOARDS, Board.class);
    }

    @Override
    public Board get(String board_id, Collection<EBoardFields> fields) {
        return super.get((WebTarget t) -> {
            t = t.path(board_id);
            Collection<EBoardFields> myFields = fields == null
                    ? BoardsClientImpl.DEFAULT_FIELDS
                    : fields;
            for (EBoardFields field : myFields) {
                t = t.queryParam(BoardsClientImpl.EBoardsParams.FIELDS.getQuery_str(), field.getRest_name());
            }
            return t;
        });
    }

    @Override
    public Board get(Board board, Collection<EBoardFields> fields) {
        return get(board.getId(), fields);
    }

    @Override
    public Board get(String board_id) {
        return get(board_id, Arrays.asList(EBoardFields.values()));
    }

    @Override
    public Board get(Board board) {
        return get(board, Arrays.asList(EBoardFields.values()));
    }

}
