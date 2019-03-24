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
import com.alecn.glo.util.GloLogger;
import java.util.function.Function;
import javax.ws.rs.client.WebTarget;
import lombok.AllArgsConstructor;

/**
 *
 * @author alecn
 */
public abstract class GenericBoardPartClient<T, R, F extends FieldsEnumI> extends GenericClientImpl<T, R, F> {

    @AllArgsConstructor
    protected static class BoardIdResolver implements Function<WebTarget, WebTarget> {
        private final String board_id;

        public static WebTarget apply(WebTarget t, String board_id) {
            return t.resolveTemplate(GloConstants.GLO_PATH_BOARD_ID, board_id);
        }

        @Override
        public WebTarget apply(WebTarget t) {
            return apply(t, this.board_id);
        }
    }

    public GenericBoardPartClient(String accessKey, String path, Class<T> klass, Class<T[]> arrayKlass, GloLogger _logger) {
        super(accessKey, path, klass, arrayKlass, _logger);
    }

}
