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

import static com.alecn.glo.client.impl.GloConstants.GLO_URL;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author AlecN <alecn2002@gmail.com>
 */
public abstract class GenericClientImpl<T, R> extends GloConstants {
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

    private Invocation.Builder prepareInvocationBuilder(Function<WebTarget, WebTarget> fixer) {
        WebTarget myWebTarget = webTarget.path(path);
        // TODO replace with real authorizetion!
        myWebTarget = myWebTarget.queryParam("access_token", "pcad84c0e279a1a233e1eb31a7a4b20b4ad3ea947");
        if (fixer != null) {
            myWebTarget = fixer.apply(myWebTarget);
        }
        return myWebTarget.request(MediaType.APPLICATION_JSON);
    }

    protected T get(Function<WebTarget, WebTarget> fixer) {
        return prepareInvocationBuilder(fixer).get(klass);
    }

    protected List<T> getList(Function<WebTarget, WebTarget> fixer) {
        return Arrays.asList(prepareInvocationBuilder(fixer).get(arrayKlass));
    }

    protected T post(Entity<R> entity, Function<WebTarget, WebTarget> fixer) {
        return prepareInvocationBuilder(fixer).post(entity, klass);
    }

    protected Response delete(Function<WebTarget, WebTarget> fixer) {
        return prepareInvocationBuilder(fixer).delete(Response.class);
    }
}
