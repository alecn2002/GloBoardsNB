/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alecn.glo.client.impl;

import static com.alecn.glo.client.impl.GloConstants.GLO_URL;
import java.util.List;
import java.util.function.Function;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author anovitsk
 */
public abstract class GenericClientImpl<T> extends GloConstants {
    private final WebTarget webTarget;
    private final String path;
    private final Class<T> klass;

    protected GenericClientImpl(String path, Class<T> klass) {
        webTarget = ClientBuilder.newClient()
                .target(GLO_URL);
        this.path = path;
        this.klass = klass;
    }

    private Invocation.Builder prepareInvocationBuilder(Function<WebTarget, WebTarget> fixer) {
        WebTarget myWebTarget = webTarget.path(path);
        if (fixer != null) {
            myWebTarget = fixer.apply(myWebTarget);
        }
        return myWebTarget.request(MediaType.APPLICATION_JSON);
    }

    public T get(Function<WebTarget, WebTarget> fixer) {
        return prepareInvocationBuilder(fixer).get(klass);
    }

    public List<T> getList(Function<WebTarget, WebTarget> fixer) {
        return prepareInvocationBuilder(fixer).get(new GenericType<List<T>> () {});
    }
}
