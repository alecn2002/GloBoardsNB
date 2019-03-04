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

import com.alecn.glo.client.BoardClient;
import com.alecn.glo.client.BoardsClient;
import com.alecn.glo.client.ColumnClient;
import com.alecn.glo.service.BoardService;
import com.alecn.glo.sojo.Board;
import com.alecn.glo.sojo.Column;
import com.alecn.glo.sojo.ColumnRequest;
import java.util.List;
import javax.ws.rs.core.Response;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author AlecN <alecn2002@gmail.com>
 */
@ServiceProvider(service = BoardService.class)
public class BoardServiceImpl implements BoardService {

    private static final BoardClient boardClient = Lookup.getDefault().lookup(BoardClient.class);
    private static final BoardsClient boardsClient = Lookup.getDefault().lookup(BoardsClient.class);
    private static final ColumnClient columnClient = Lookup.getDefault().lookup(ColumnClient.class);

    @Override
    public List<Board> getBoardsList() {
        return boardsClient.get();
    }

    @Override
    public Board getBoard(String id) {
        return boardClient.get(id);
    }

    @Override
    public Column createColumn(String boardId, String columnName) {
        return columnClient.create(boardId, new ColumnRequest(columnName));
    }

    @Override
    public Column createColumn(String boardId, String columnName, Integer position) {
        return columnClient.create(boardId, new ColumnRequest(columnName, position));
    }

    @Override
    public Column editColumn(String boardId, String columnId, String columnName) {
        return columnClient.edit(boardId, columnId, new ColumnRequest(columnName));
    }

    @Override
    public Column editColumn(String boardId, String columnId, String columnName, Integer position) {
        return columnClient.edit(boardId, columnId, new ColumnRequest(columnName, position));
    }

    @Override
    public Response deleteColumn(String boardId, String columnId) {
        return columnClient.delete(boardId, columnId);
    }

}
