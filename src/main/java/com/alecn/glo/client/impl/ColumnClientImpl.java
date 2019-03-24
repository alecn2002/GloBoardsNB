/*
 * The MIT License
 *
 * Copyright 2019 anovitsk.
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

import com.alecn.glo.client.ColumnClient;
import com.alecn.glo.client.FieldsEnumI;
import com.alecn.glo.sojo.Column;
import com.alecn.glo.client.dto.ColumnRequest;
import com.alecn.glo.util.GloLogger;
import java.util.function.Function;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ColumnClient.class)
public class ColumnClientImpl extends GenericBoardPartClient<Column, ColumnRequest, FieldsEnumI> implements ColumnClient {

    private static final GloLogger LOGGER = new GloLogger(ColumnClientImpl.class);

    private static class BoardColumnIdResolver extends BoardIdResolver implements Function<WebTarget, WebTarget> {

        private final String column_id;

        public static WebTarget apply(WebTarget t, String column_id) {
            return t.path(column_id);
        }

        public BoardColumnIdResolver(String board_id, String column_id) {
            super(board_id);
            this.column_id = column_id;
        }

        @Override
        public WebTarget apply(WebTarget t) {
            return BoardColumnIdResolver.apply(super.apply(t), column_id);
        }

    }

    public ColumnClientImpl(String access_key) {
        super(access_key, GLO_PATH_COLUMNS, Column.class, Column[].class, LOGGER);
    }

    public ColumnClientImpl() {
        this("pcad84c0e279a1a233e1eb31a7a4b20b4ad3ea947"); // TODO remove default access key!
    }

    @Override
    public Column create(final String board_id, ColumnRequest column_request) {
        return super.post(Entity.json(column_request),
                new BoardIdResolver(board_id)
        );
    }

    @Override
    public Column edit(String board_id, String column_id, ColumnRequest column_request) {
        return super.post(Entity.json(column_request),
                new BoardColumnIdResolver(board_id, column_id));
    }

    @Override
    public Response delete(String board_id, String column_id) {
        return super.delete(new BoardColumnIdResolver(board_id, column_id));
    }

}
