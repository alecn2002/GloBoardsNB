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

import com.alecn.glo.client.BoardFieldsEnum;
import com.alecn.glo.client.FieldsEnumI;
import static com.alecn.glo.client.impl.GloConstants.GLO_URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.Getter;

/**
 *
 * @author AlecN <alecn2002@gmail.com>
 */
public abstract class GenericClientImpl<T, R, F extends FieldsEnumI> extends GloConstants {

    @Getter
    enum EBoardsParams {
        FIELDS("fields"),
        ARCHIVED("archived"),
        PAGE("page"),
        PER_PAGE("per_page"),
        SORT("sort");

        private final String queryStr;

        EBoardsParams(String queryStr) {
            this.queryStr = queryStr;
        }
    }


    protected static WebTarget apply_board_id(WebTarget target, String board_id) {
        return target.resolveTemplate(GLO_PATH_BOARD_ID, board_id);
    }

    private final WebTarget webTarget;
    private final String path;
    private final Class<T> klass;
    private final Class<T[]> arrayKlass;

    protected GenericClientImpl(String path, Class<T> klass, Class<T[]> arrayKlass) {
        webTarget = ClientBuilder.newClient()
                .target(GLO_URL);
        this.path = path;
        this.klass = klass;
        this.arrayKlass = arrayKlass;
    }

    protected GenericClientImpl(String path, Class<T> klass) {
        this(path, klass, null);
    }

    private WebTarget prepareWebTarget(Function<WebTarget, WebTarget> fixer) {
        WebTarget myWebTarget = webTarget.path(path);
        // TODO replace with real authorizetion!
        myWebTarget = myWebTarget.queryParam("access_token", "pcad84c0e279a1a233e1eb31a7a4b20b4ad3ea947");
        if (fixer != null) {
            myWebTarget = fixer.apply(myWebTarget);
        }
        return myWebTarget;
    }

    private WebTarget prepareWebTarget(String id, Collection<F> fields, Function<WebTarget, WebTarget> fixer) {
        WebTarget myWebTarget = prepareWebTarget(fixer);
        if (id != null) {
            myWebTarget = myWebTarget.path(id);
        }
        for (F field : fields) {
            myWebTarget = myWebTarget.queryParam(EBoardsParams.FIELDS.getQueryStr(), field.getRestName());
        }

        return myWebTarget;
    }

    private Invocation.Builder prepareInvocationBuilder(Function<WebTarget, WebTarget> fixer) {
        WebTarget myWebTarget = prepareWebTarget(fixer);
        return myWebTarget.request(MediaType.APPLICATION_JSON);
    }

    protected T get(Function<WebTarget, WebTarget> fixer) {
        return prepareInvocationBuilder(fixer).get(klass);
    }

    protected T get(String id, Collection<F> fields, Function<WebTarget, WebTarget> fixer) {
        return prepareWebTarget(id, fields, fixer)
                .request(MediaType.APPLICATION_JSON)
                .get(klass);
    }


    protected T get(String id, Collection<F> fields) {
        return get(id, fields, null);
    }

    protected List<T> list(Function<WebTarget, WebTarget> fixer) {
        return Arrays.asList(prepareInvocationBuilder(fixer).get(arrayKlass));
    }

    protected List<T> list(Collection<F> fields, boolean archived, Integer page, Integer per_page, boolean sort_desc, Function<WebTarget, WebTarget> fixer) {
        WebTarget myWebTarget = prepareWebTarget(null, fields, fixer);
        if (archived) {
            myWebTarget = myWebTarget.queryParam(EBoardsParams.ARCHIVED.getQueryStr(), "true");
        }
        if (page != null && per_page != null) {
            myWebTarget = myWebTarget.queryParam(EBoardsParams.PAGE.getQueryStr(), page)
                    .queryParam(EBoardsParams.PER_PAGE.getQueryStr(), per_page);
        }
        if (sort_desc) {
            myWebTarget = myWebTarget.queryParam(EBoardsParams.SORT.getQueryStr(), "desc");
        }
        return Arrays.asList(myWebTarget.request(MediaType.APPLICATION_JSON)
                .get(arrayKlass));
    }

    protected List<T> list(Collection<F> fields, boolean archived, Integer page, Integer per_page, boolean sort_desc) {
        return list(fields, archived, page, per_page, sort_desc, null);
    }

    protected T post(Entity<R> entity, Function<WebTarget, WebTarget> fixer) {
        return prepareInvocationBuilder(fixer).post(entity, klass);
    }

    protected Response delete(Function<WebTarget, WebTarget> fixer) {
        return prepareInvocationBuilder(fixer).delete(Response.class);
    }
}
