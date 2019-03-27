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
package com.alecn.glo.client.impl;

import com.alecn.glo.client.FieldsEnumI;
import static com.alecn.glo.client.impl.GloConstants.GLO_PATH_BOARD_ID;
import com.alecn.glo.util.GloLogger;
import java.util.function.Function;
import javax.ws.rs.client.WebTarget;

/**
 *
 * @author alecn
 */
public abstract class GenericCardPartClientImpl<T, R, F extends FieldsEnumI> extends GenericBoardPartClient<T, R, F> {

    protected static Function<WebTarget, WebTarget> boardCardIdResolverFactory(String boardId, String cardId) {
        return (WebTarget wt) -> wt.resolveTemplate(GLO_PATH_BOARD_ID, boardId)
                                   .resolveTemplate(GLO_PATH_CARD_ID,  cardId);
    }

    protected static Function<WebTarget, WebTarget> boardCardIdResolverPathApplierFactory(String boardId, String cardId, String path) {
        return pathApplierFactory(boardCardIdResolverFactory(boardId, cardId), path);
    }

    public GenericCardPartClientImpl(String accessKey, String path, Class<T> klass, Class<T[]> arrayKlass, GloLogger _logger) {
        super(accessKey, path, klass, arrayKlass, _logger);
    }

}