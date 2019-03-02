/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alecn.glo.client.impl;

import com.alecn.glo.client.BoardsClient;
import com.alecn.glo.client.EBoardFields;
import com.alecn.glo.sojo.Board;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.client.WebTarget;
import lombok.Getter;

/**
 *
 * @author anovitsk
 */
public class BoardsClientImpl extends GenericClientImpl<Board> implements BoardsClient {

    @Getter
    enum EBoardsParams {
        FIELDS("fields"),
        ARCHIVED("archived"),
        PAGE("page"),
        PER_PAGE("per_page"),
        SORT("sort");

        private final String query_str;

        EBoardsParams(String query_str) {
            this.query_str = query_str;
        }
    }

    private static final EBoardFields[] DEFAULT_FIELDS_LIST = {EBoardFields.NAME};
    static final Collection<EBoardFields> DEFAULT_FIELDS = Arrays.asList(DEFAULT_FIELDS_LIST);

    public BoardsClientImpl() {
        super("boards", Board.class);
    }

    @Override
    public List<Board> get(final Collection<EBoardFields> fields, boolean archived, Integer page, Integer per_page, boolean sort_desc) {
        return super.getList((WebTarget t) -> {
            Collection<EBoardFields> myFields = fields == null
                    ? DEFAULT_FIELDS
                    : fields;
            for (EBoardFields field : myFields) {
                t = t.queryParam(EBoardsParams.FIELDS.getQuery_str(), field.getRest_name());
            }
            if (archived) {
                t = t.queryParam(EBoardsParams.ARCHIVED.getQuery_str(), "true");
            }
            if (page != null && per_page != null) {
                t = t.queryParam(EBoardsParams.PAGE.getQuery_str(), page)
                        .queryParam(EBoardsParams.PER_PAGE.getQuery_str(), per_page);
            }
            if (sort_desc) {
                t = t.queryParam(EBoardsParams.SORT.getQuery_str(), "desc");
            }
            return t;
        });
    }

    @Override
    public List<Board> get() {
        return get(null, false, null, null, false);
    }
}
