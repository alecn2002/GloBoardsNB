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
import static com.alecn.glo.client.impl.GloConstants.GLO_URL;
import com.alecn.glo.util.GloLogger;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import lombok.Getter;

/**
 *
 * @author AlecN <alecn2002@gmail.com>
 */
public abstract class GenericClientImpl<T, R, F extends FieldsEnumI> extends GloConstants {

    private static final String ACCESS_KEY_HTTP_PARAM = "Authorization";
    private static final String ACCESS_KEY_PREFIX = "Bearer ";

    private static void dumpHeaders(GloLogger logger, MultivaluedMap<String, ?> headers) {
        logger.info("Headers:");
        headers.forEach((String name, List<?> values) ->
                logger.info("    %s: %s",
                            name,
                            String.join(", ", values.stream().map(v -> v.toString()).collect(Collectors.toList())
                            )));
    }

    @Getter
    enum EBoardsParams {
        FIELDS("fields"),
        ARCHIVED("archived"),
        PAGE("page"),
        PER_PAGE("per_page"),
        SORT("sort"),
        ACCESS_TOKEN("access_token");

        private final String queryStr;

        EBoardsParams(String queryStr) {
            this.queryStr = queryStr;
        }
    }


    protected static WebTarget apply_board_id(WebTarget target, String board_id) {
        return target.resolveTemplate(GLO_PATH_BOARD_ID, board_id);
    }

    protected static Function<WebTarget, WebTarget> pathApplierFactory(String path) {
        return (WebTarget t) -> t.path(path);
    }

    protected static Function<WebTarget, WebTarget> pathApplierFactory(Function<WebTarget, WebTarget> chain, String path) {
        return (WebTarget t) -> chain.apply(t).path(path);
    }

    private final WebTarget webTarget;
    private final String path;
    private final Class<T> klass;
    private final Class<T[]> arrayKlass;
    private final String accessKey;
    private final GloLogger logger;

    private static void logResponseEntity(GloLogger logger, ClientResponseContext responseContext) throws IOException {
        if (responseContext.hasEntity()) {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            try (InputStream stream = responseContext.getEntityStream()) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = stream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
            }
            logger.info("    ==== Response body: '%s'", result.toString("UTF-8"));

            responseContext.setEntityStream(new ByteArrayInputStream(result.toByteArray()));
        }
    }

    private static class MyOutputStream extends OutputStream {

        private final OutputStream originalStream;
        private final OutputStream myStream;

        public MyOutputStream(OutputStream originalStream, OutputStream myStream) {
            this.originalStream = originalStream;
            this.myStream = myStream;
        }

        @Override
        public void write(int b) throws IOException {
            originalStream.write(b);
            myStream.write(b);
        }

        @Override
        public void close() throws IOException {
            originalStream.close();
            myStream.close();
        }
    }

    private final ByteArrayOutputStream myOutputStream;

    protected GenericClientImpl(String url, String accessKey, String path, Class<T> klass, Class<T[]> arrayKlass, GloLogger _logger) {
        this.logger = _logger;
        Client client = ClientBuilder.newClient();

        this.myOutputStream = new ByteArrayOutputStream();

        client.register((ClientRequestFilter) (ClientRequestContext requestContext) -> {
            if (requestContext == null) {
                logger.warning("requestContext == null");
            } else {
                logger.info("Requesting %s %s", requestContext.getMethod(), requestContext.getUri().toString());
                dumpHeaders(logger, requestContext.getHeaders());
                if (requestContext.hasEntity()) {
                    logger.info("With entity: %s", requestContext.getEntity().toString());
                }
                requestContext.setEntityStream(new MyOutputStream(requestContext.getEntityStream(), myOutputStream));
            }
        });

        client.register((ClientResponseFilter) (ClientRequestContext requestContext, ClientResponseContext responseContext) -> {
            if (responseContext == null) {
                logger.warning("requestContext == null");
            } else {
                logger.info("Response %d %s", responseContext.getStatusInfo().getStatusCode(), responseContext.getStatusInfo().getReasonPhrase());
                dumpHeaders(logger, responseContext.getHeaders());
                InputStream objStream = responseContext.getEntityStream();
                logResponseEntity(logger, responseContext);
            }
        });

        webTarget = client.target(url);
        this.path = path;
        this.klass = klass;
        this.arrayKlass = arrayKlass;
        this.accessKey = accessKey;
    }

    protected GenericClientImpl(String accessKey, String path, Class<T> klass, Class<T[]> arrayKlass, GloLogger logger) {
        this(GLO_URL, accessKey, path, klass, arrayKlass, logger);
    }

    private WebTarget prepareWebTarget(Function<WebTarget, WebTarget> fixer) {
        WebTarget myWebTarget = webTarget.path(path);
        return fixer == null
                ? myWebTarget
                : fixer.apply(myWebTarget);
    }

    private WebTarget prepareWebTarget(String id, Collection<F> fields, boolean archived, Integer page, Integer per_page, boolean sort_desc, Function<WebTarget, WebTarget> fixer) {
        WebTarget myWebTarget = prepareWebTarget(fixer);
        if (id != null) {
            myWebTarget = myWebTarget.path(id);
        }
        if (fields != null) {
            for (F field : fields) {
                myWebTarget = myWebTarget.queryParam(EBoardsParams.FIELDS.getQueryStr(), field.getRestName());
            }
        }
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

        return myWebTarget;
    }

    private WebTarget prepareWebTarget(String id, Collection<F> fields, Function<WebTarget, WebTarget> fixer) {
         return prepareWebTarget(id, fields, false, null, null, false, fixer);
    }

    private Invocation.Builder prepareInvocationBuilder(String id, Collection<F> fields, boolean archived, Integer page, Integer per_page, boolean sort_desc, Function<WebTarget, WebTarget> fixer, MediaType mediaType) {
        WebTarget myWebTarget = prepareWebTarget(id, fields, archived, page, per_page, sort_desc, fixer);
        return myWebTarget.request(mediaType)
                .header(ACCESS_KEY_HTTP_PARAM, ACCESS_KEY_PREFIX + accessKey);
    }

    private Invocation.Builder prepareInvocationBuilder(String id, Collection<F> fields, Function<WebTarget, WebTarget> fixer, MediaType mediaType) {
        return prepareInvocationBuilder(id, fields, false, null, null, false, fixer, mediaType);
    }

    private Invocation.Builder prepareInvocationBuilder(Function<WebTarget, WebTarget> fixer, MediaType mediaType) {
        return prepareInvocationBuilder(null, null, fixer, mediaType);

    }

    private Invocation.Builder prepareInvocationBuilder(Function<WebTarget, WebTarget> fixer) {
        return prepareInvocationBuilder(fixer, MediaType.APPLICATION_JSON_TYPE);
    }

    private Invocation.Builder prepareInvocationBuilder(String id, Collection<F> fields, Function<WebTarget, WebTarget> fixer) {
        return prepareInvocationBuilder(id, fields, fixer, MediaType.APPLICATION_JSON_TYPE);
    }

    protected T get(Function<WebTarget, WebTarget> fixer) {
        return prepareInvocationBuilder(fixer).get(klass);
    }

    protected T get(String id, Collection<F> fields, Function<WebTarget, WebTarget> fixer) {
        return prepareInvocationBuilder(id, fields, fixer)
                .get(klass);
    }

    protected T get(String id, Collection<F> fields, Function<WebTarget, WebTarget> fixer, GloLogger logger, String name) {
        logger.info("Fetching %s id=%s with %d fields (%s)",
                    name,
                    id,
                    fields.size(),
                    GloLogger.join(fields, ff -> ff.getRestName()));
        return prepareInvocationBuilder(id, fields, fixer)
                .get(klass);
    }

    protected T get(String id, Collection<F> fields) {
        return get(id, fields, null);
    }

    protected T get(String id, Collection<F> fields, GloLogger logger, String name) {
        return get(id, fields, null, logger, name);
    }

    protected List<T> list(Function<WebTarget, WebTarget> fixer) {
        return Arrays.asList(prepareInvocationBuilder(fixer).get(arrayKlass));
    }

    protected List<T> list(Collection<F> fields, boolean archived, Integer page, Integer per_page, boolean sort_desc, Function<WebTarget, WebTarget> fixer) {
        Invocation.Builder invocationBuilder = prepareInvocationBuilder(null, fields, archived, page, per_page, sort_desc, fixer, MediaType.APPLICATION_JSON_TYPE);
        return Arrays.asList(invocationBuilder.get(arrayKlass));
    }

    protected List<T> list(Collection<F> fields, boolean archived, Integer page, Integer per_page, boolean sort_desc, Function<WebTarget, WebTarget> fixer, GloLogger logger, String name) {
        logger.info("Fetching %s list with %d fields (%s), %s archived, page=%d, per_page=%d, sort=%s",
                    name,
                    fields.size(),
                    GloLogger.join(fields, ff -> ff.getRestName()),
                    archived ? "" : "NOT",
                    page,
                    per_page,
                    sort_desc ? "DESC" : "ASC");
        return list(fields, archived, page, per_page, sort_desc, fixer);
    }

    protected List<T> list(Collection<F> fields, boolean archived, Integer page, Integer per_page, boolean sort_desc) {
        return list(fields, archived, page, per_page, sort_desc, null);
    }

    protected List<T> list(Collection<F> fields, boolean archived, Integer page, Integer per_page, boolean sort_desc, GloLogger logger, String name) {
        return list(fields, archived, page, per_page, sort_desc, null, logger, name);
    }

    protected T post(Entity<R> entity, Function<WebTarget, WebTarget> fixer) {
        try {
            return prepareInvocationBuilder(fixer).post(entity, klass);
        } finally {
            if (myOutputStream.size() > 0) {
                try {
                    logger.info("Request body: '%s'", myOutputStream.toString("UTF-8"));
                } catch (UnsupportedEncodingException ex) {
                    logger.severe("Unsupported encoding exception on attempt to print request body: %s", ex.getMessage());
                }
                myOutputStream.reset();
            }
        }
    }

    protected Response delete(Function<WebTarget, WebTarget> fixer) {
        // TODO logger
        return prepareInvocationBuilder(fixer).delete(Response.class);
    }

    protected <RR, P> RR execWithDefault(P param, P default_param, Function<P, RR> toExec) {
        P toUse = param == null
                ? default_param
                : param;
        return toExec.apply(toUse);
    }
}
