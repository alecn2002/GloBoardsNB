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
package com.alecn.glo.service;

import com.alecn.glo.sojo.Board;
import com.alecn.glo.sojo.Card;
import com.alecn.glo.sojo.Column;
import java.util.List;
import javax.ws.rs.core.Response;

/**
 *
 * @author AlecN <alecn2002@gmail.com>
 */
public interface BoardService {
    List<Board> getBoardsList();

    Board getBoard(String id);

    Column createColumn(String boardId, String columnName);

    Column createColumn(String boardId, String columnName, Integer position);

    Column editColumn(String boardId, String columnId, String columnName);

    Column editColumn(String boardId, String columnId, String columnName, Integer position);

    Response deleteColumn(String boardId, String columnId);

    List<Card> listCards(String boardId);
}
