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
package com.alecn.glo.util;

import com.alecn.glo.sojo.Entity;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 *
 * @author alecn
 */
public class Cache<T extends Entity> {

    private final LazyValue<Map<String, T>> cache;

    public Cache(Supplier<List<T>> refresher) {
        this.cache = new LazyValue<>(
                () -> ((List<T>)refresher.get()).stream()
                    .sequential()
                    .collect(Collectors.toMap(e -> e.getId(), e -> e, (v1, v2) -> v1, LinkedHashMap::new)));
    }

    public Map<String, T> getCache() {
        return cache.get();
    }

    public T getElementById(String id) {
        return VisitorHelper.nvlVisitor(cache, null,
                c -> ((LazyValue<Map<String, T>>)c).get(),
                m -> ((Map<String, T>) m).get(id));
    }

    public void refresh() {
        cache.refresh();
    }
}
