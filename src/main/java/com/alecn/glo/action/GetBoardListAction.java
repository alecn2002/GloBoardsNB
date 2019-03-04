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

import com.alecn.glo.service.BoardService;
import com.alecn.glo.sojo.Board;
import com.alecn.glo.sojo.Column;
import com.alecn.glo.wrappers.InputOutputCloseableWrapper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import org.netbeans.api.io.IOProvider;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author AlecN <alecn2002@gmail.com>
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

    @Override
    public void actionPerformed(ActionEvent e) {
        try (InputOutputCloseableWrapper io = new InputOutputCloseableWrapper(IOProvider.getDefault().getIO ("Hello", true))) {
            if (boardService == null) {
                io.getErr().println ("boardService not initialized");
                return;
            }
            io.getErr().println (">>>> Retrieving board list...");
            List<Board> boards = boardService.getBoardsList();
            if (boards == null || boards.isEmpty()) {
                io.getErr().println ("getBoardList() returned null or empty list");
                return;
            }
            boards.forEach((board) -> {
                io.getOut().println (board.toString());
            });
            io.getErr().println (">>>> Retrieving details for Board #0...");
            String board0Id = boards.get(0).getId();
            Board b = boardService.getBoard(board0Id);
            io.getOut().println (b.toString());
            io.getOut().println ("Current columns list:");
            for (Column column : b.getColumns()) {
                io.getOut().println ("    - " + column.toString());
            }

            io.getErr().println (">>>> Creating new column Abcde...");
            Column abcdeColumn = boardService.createColumn(board0Id, "Abcde");
            io.getOut().println ("Created column:");
            io.getOut().println (abcdeColumn.toString());

            io.getErr().println (">>>> Retrieving details for Board #0...");
            b = boardService.getBoard(board0Id);
            io.getOut().println ("Current columns list:");
            for (Column column : b.getColumns()) {
                io.getOut().println ("    - " + column.toString());
            }


        } catch (IOException ex) {

        }
    }
}
