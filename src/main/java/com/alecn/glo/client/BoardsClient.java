/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alecn.glo.client;

import com.alecn.glo.sojo.Board;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author anovitsk
 */
public interface BoardsClient {
    List<Board> get(final Collection<EBoardFields> fields, boolean archived, Integer page, Integer per_page, boolean sort_desc);
    List<Board> get();
}
