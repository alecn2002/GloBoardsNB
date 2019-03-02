/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alecn.glo.client.impl;

import com.alecn.glo.client.BoardClient;
import com.alecn.glo.client.EBoardFields;
import com.alecn.glo.sojo.Board;
import java.util.Collection;
import javax.ws.rs.client.WebTarget;

/**
 *
 * @author anovitsk
 */
public class BoardClientImpl extends GenericClientImpl<Board> implements BoardClient {

    public BoardClientImpl() {
        super("boards/", Board.class);
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

}
