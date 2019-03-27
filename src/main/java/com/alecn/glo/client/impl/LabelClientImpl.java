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

import com.alecn.glo.client.FieldsEnumI;
import com.alecn.glo.client.LabelClient;
import com.alecn.glo.client.dto.LabelRequest;
import com.alecn.glo.sojo.Label;
import com.alecn.glo.util.GloLogger;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;


public class LabelClientImpl extends GenericBoardPartClient<Label, LabelRequest, FieldsEnumI> implements LabelClient {

    private static final GloLogger LOGGER = new GloLogger(LabelClientImpl.class);

    public LabelClientImpl(String accessKey) {
        super(accessKey, GLO_PATH_LABELS, Label.class, Label[].class, LOGGER);
    }

    @Override
    public Label create(String boardId, LabelRequest labelRequest) {
        return super.post(Entity.json(labelRequest), boardIdResolverFactory(boardId));
    }

    @Override
    public Label edit(String boardId, String labelId, LabelRequest labelRequest) {
        return super.post(Entity.json(labelRequest), boardIdResolverPathApplierFactory(boardId, labelId));
    }

    @Override
    public Response delete(String boardId, String labelId) {
        return super.delete(boardIdResolverPathApplierFactory(boardId, labelId));
    }

}
