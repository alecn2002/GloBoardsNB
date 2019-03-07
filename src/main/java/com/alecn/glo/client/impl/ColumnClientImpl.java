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
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ColumnClient.class)
public class ColumnClientImpl extends GenericClientImpl<Column, ColumnRequest, FieldsEnumI> implements ColumnClient {

    public ColumnClientImpl() {
        super(GLO_PATH_COLUMNS, Column.class);
    }

    public ColumnClientImpl(String glo_api_url) {
        super(glo_api_url, GLO_PATH_COLUMNS, Column.class, Column[].class);
    }

    @Override
    public Column create(final String board_id, ColumnRequest column_request) {
        return super.post(Entity.json(column_request), (WebTarget t) -> {
            return apply_board_id(t, board_id);
        });
    }

    @Override
    public Column edit(String board_id, String column_id, ColumnRequest column_request) {
        return super.post(Entity.json(column_request), (WebTarget t) -> {
            return apply_board_id(t, board_id)
                    .path(column_id);
        });
    }

    @Override
    public Response delete(String board_id, String column_id) {
        return super.delete((WebTarget t) -> {
            return apply_board_id(t, board_id)
                    .path(column_id);
        });
    }

}
