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
import com.alecn.glo.client.BoardFieldsEnum;
import com.alecn.glo.client.CardClient;
import com.alecn.glo.client.CardFieldsEnum;
import com.alecn.glo.client.ColumnClient;
import com.alecn.glo.service.BoardService;
import com.alecn.glo.sojo.Board;
import com.alecn.glo.sojo.Column;
import com.alecn.glo.client.dto.ColumnRequest;
import com.alecn.glo.client.impl.BoardClientImpl;
import com.alecn.glo.client.impl.CardClientImpl;
import com.alecn.glo.client.impl.ColumnClientImpl;
import com.alecn.glo.sojo.Card;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.core.Response;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author <a href="mailto:alecn2002@gmail.com">AlecN</a>
 */
@ServiceProvider(service = BoardService.class)
public class BoardServiceImpl implements BoardService {

    private static final BoardFieldsEnum[] DEFAULT_FIELDS_LIST = {BoardFieldsEnum.NAME};
    static final Collection<BoardFieldsEnum> DEFAULT_FIELDS = Arrays.asList(DEFAULT_FIELDS_LIST);

    static final Collection<BoardFieldsEnum> ALL_FIELDS = Arrays.asList(BoardFieldsEnum.values());

    private static final BoardFieldsEnum[] FIELDS_FOR_BOARD_COLUMNS_LIST = {BoardFieldsEnum.COLUMNS};
    private static final List<BoardFieldsEnum> FIELDS_FOR_BOARD_COLUMNS = Arrays.asList(FIELDS_FOR_BOARD_COLUMNS_LIST);

    private static final List<BoardFieldsEnum> ALL_BOARD_COLUMNS = Arrays.asList(BoardFieldsEnum.values());

    private static final BoardClient BOARD_CLIENT = Lookup.getDefault().lookup(BoardClient.class);
    private static final ColumnClient COLUMN_CLIENT = Lookup.getDefault().lookup(ColumnClient.class);
    private static final CardClient CARD_CLIENT = Lookup.getDefault().lookup(CardClient.class);

    private final BoardClient boardClient;
    private final ColumnClient columnClient;
    private final CardClient cardClient;

    public BoardServiceImpl() {
        this.boardClient = BOARD_CLIENT;
        this.columnClient = COLUMN_CLIENT;
        this.cardClient = CARD_CLIENT;
    }

    public BoardServiceImpl(String access_key) {
        this.boardClient = new BoardClientImpl(access_key);
        this.columnClient = new ColumnClientImpl(access_key);
        this.cardClient = new CardClientImpl(access_key);
    }

    @Override
    public List<Board> listBoards() {
        return listBoards(DEFAULT_FIELDS);
    }

    @Override
    public List<Board> listBoards(Collection<BoardFieldsEnum> fields) {
        return boardClient.list(fields, false, null, null, false);
    }

    @Override
    public Board getBoard(String id) {
        return boardClient.get(id, DEFAULT_FIELDS);
    }

    @Override
    public Board getBoardWithAllFields(String id) {
        return boardClient.get(id, ALL_BOARD_COLUMNS);
    }

    @Override
    public List<Column> listBoardColumns(String board_id) {
        return boardClient.get(board_id, FIELDS_FOR_BOARD_COLUMNS).getColumns();
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

    @Override
    public List<Card> listCards(String boardId) {
        return cardClient.list(boardId, null, false, null, null, false);
    }

    @Override
    public List<Card> listCards(String boardId, Collection<CardFieldsEnum> fields) {
        return cardClient.list(boardId, fields, false, null, null, false);
    }

}
