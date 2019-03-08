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
import com.alecn.glo.client.BoardFieldsEnum;
import static com.alecn.glo.client.impl.GloConstants.GLO_PATH_BOARDS;
import com.alecn.glo.sojo.Board;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author AlecN <alecn2002@gmail.com>
 */
@ServiceProvider(service = BoardClient.class)
public class BoardClientImpl extends GenericClientImpl<Board, Board, BoardFieldsEnum> implements BoardClient {

    private static final BoardFieldsEnum[] DEFAULT_FIELDS_LIST = {BoardFieldsEnum.NAME};
    static final Collection<BoardFieldsEnum> DEFAULT_FIELDS = Arrays.asList(DEFAULT_FIELDS_LIST);
    static final Collection<BoardFieldsEnum> ALL_FIELDS = Arrays.asList(BoardFieldsEnum.values());

    public BoardClientImpl(String access_key) {
        super(access_key, GLO_PATH_BOARDS, Board.class, Board[].class);
    }

    public BoardClientImpl() {
        this("pcad84c0e279a1a233e1eb31a7a4b20b4ad3ea947"); // TODO remove default access key!
    }

    @Override
    public Board get(String board_id, Collection<BoardFieldsEnum> fields) {
            Collection<BoardFieldsEnum> myFields = fields == null
                    ? DEFAULT_FIELDS
                    : fields;
        return super.get(board_id, myFields);
    }

    @Override
    public Board get(Board board, Collection<BoardFieldsEnum> fields) {
        return get(board.getId(), fields);
    }

    @Override
    public Board get(String board_id) {
        return get(board_id, ALL_FIELDS);
    }

    @Override
    public Board get(Board board) {
        return get(board, ALL_FIELDS);
    }

    @Override
    public List<Board> list(final Collection<BoardFieldsEnum> fields, boolean archived, Integer page, Integer per_page, boolean sort_desc) {
            Collection<BoardFieldsEnum> myFields = fields == null
                    ? DEFAULT_FIELDS
                    : fields;
        return super.list(myFields, archived, page, per_page, sort_desc);
    }

    @Override
    public List<Board> list() {
        return list(null, false, null, null, false);
    }

}
