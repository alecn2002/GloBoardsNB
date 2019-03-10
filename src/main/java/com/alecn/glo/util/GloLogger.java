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

import java.util.Collection;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author alecn
 */
public class GloLogger {

    private final Logger logger;

    public GloLogger(Class klass) {
        logger = Logger.getLogger(klass.getCanonicalName());
    }

    public static <T> String join(Collection<T> list, String delimeter, Function<T, String> string_mapper) {
        return String.join(delimeter,
                list.stream()
                    .map(v -> string_mapper.apply(v))
                    .collect(Collectors.toList()));
    }

    public static <T> String join(Collection<T> list, Function<T, String> string_mapper) {
        return join(list, ", ", string_mapper);
    }

    public void info(String str) {
        logger.info(str);
    }

    public void info(String format, Object... params) {
        info(String.format(format, params));
    }

    public void warning(String str) {
        logger.warning(str);
    }

    public void warning(String format, Object... params) {
        warning(String.format(format, params));
    }

    public void severe(String str) {
        logger.severe(str);
    }

    public void severe(String format, Object... params) {
        severe(String.format(format, params));
    }

    public void trace(String str) {
        logger.fine(str);
    }

    public void trace(String format, Object... params) {
        trace(String.format(format, params));
    }
}
