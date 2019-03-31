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
package com.alecn.glo.action;

import com.alecn.glo.GloConfig;
import com.alecn.glo.service.BoardService;
import com.alecn.glo.service.CommentService;
import com.alecn.glo.sojo.Board;
import com.alecn.glo.sojo.Card;
import com.alecn.glo.sojo.Column;
import com.alecn.glo.sojo.Comment;
import com.alecn.glo.util.OeWriter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.ws.rs.core.Response;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author <a href="mailto:alecn2002@gmail.com">AlecN</a>
 */
@ActionID(
        category = "Team",
        id = "com.alecn.glo.action.GetBoardListAction"
)
@ActionRegistration(
        displayName = "#CTL_GetBoardListAction"
)
@ActionReference(path = "Menu/Versioning", position = 0, separatorAfter = 50)
@Messages("CTL_GetBoardListAction=get Glo Boards list")
public final class GetBoardListAction implements ActionListener {

    private final BoardService boardService = Lookup.getDefault().lookup(BoardService.class);
    private final CommentService commentService = Lookup.getDefault().lookup(CommentService.class);

    private final GloConfig gloConfig = Lookup.getDefault().lookup(GloConfig.class);

    private final OeWriter oeWriter = gloConfig.getOeWriter();

    @Override
    public void actionPerformed(ActionEvent e) {
        oeWriter.errWrite((err) -> {err.println (">>>> Retrieving board list...");});
        if (boardService == null) {
            oeWriter.errWrite((err) -> {err.println ("boardService not initialized");});
            return;
        }
        final List<Board> boards = boardService.listBoards();
        if (boards == null || boards.isEmpty()) {
            oeWriter.errWrite((err) -> {err.println ("getBoardList() returned null or empty list");});
            return;
        }
        oeWriter.outWrite((out) -> {
            boards.forEach((board) -> {
                out.println (board.toString());
            });
        });

        final String board0Id = boards.get(0).getId();

        retrieveBoardNListColumns(board0Id, true);

        oeWriter.errWrite((err) -> {err.println (">>>> Creating new column Abcde...");});
        Column abcdeColumn = boardService.createColumn(board0Id, "Abcde");
        oeWriter.outWrite((out) -> {
            out.println ("Created column:");
            out.println (abcdeColumn.toString());
        });

        retrieveBoardNListColumns(board0Id, false);

        oeWriter.errWrite((err) -> {err.println (">>>> Editing column Abcde to Abcde0 and moving to 0 position...");});
        boardService.editColumn(board0Id, abcdeColumn.getId(), "Abcde0", 0);

        retrieveBoardNListColumns(board0Id, false);

        oeWriter.errWrite((err) -> {err.println (">>>> Deleting column Abcde0...");});
        Response response = boardService.deleteColumn(board0Id, abcdeColumn.getId());
        oeWriter.outWrite((out) -> {
            out.println ("Column delete result:");
            out.println (response.toString());
        });

        retrieveBoardNListColumns(board0Id, false);

        oeWriter.errWrite((err) -> {err.println (">>>> Retrieving all cards...");});
        List<Card> cards = boardService.listCards(board0Id);
        oeWriter.outWrite((out) -> {
            cards.forEach((card) -> {
                out.println (card.toString());
            });
        });

        oeWriter.errWrite((err) -> {err.println (">>>> Retrieving comments for the first card...");});
        List<Comment> comments = commentService.list(board0Id, cards.get(0).getId());
        oeWriter.outWrite((out) -> {
            comments.forEach((comment) -> {
                out.println (comment.toString());
            });
        });

    }

    private Board retrieveBoardNListColumns(final String board0Id, boolean printBoard) {
        oeWriter.errWrite((err) -> {err.println (">>>> Retrieving details for Board #0...");});
        final Board b = boardService.getBoard(board0Id);
        oeWriter.outWrite((out) -> {
            if (printBoard) {
                out.println (b.toString());
            }
            out.println ("Current columns list:");
            b.getColumns().forEach((column) -> {
                out.println ("    - " + column.toString());
            });
        });
        return b;
    }
}
