/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alecn.glo.client;

import com.alecn.glo.sojo.Board;
import java.util.Collection;

/**
 *
 * @author anovitsk
 */
public interface BoardClient {
    Board get(String board_id, final Collection<EBoardFields> fields);

    Board get(Board board, final Collection<EBoardFields> fields);
}
