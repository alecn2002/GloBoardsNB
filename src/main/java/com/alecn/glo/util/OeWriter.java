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

import com.alecn.glo.wrappers.InputOutputCloseableWrapper;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;
import org.netbeans.api.io.IOProvider;
import org.netbeans.api.io.OutputWriter;

/**
 *
 * @author alecn
 */
public class OeWriter {

    private boolean first_time;

    private final String name;

    public OeWriter(String name) {
        this.first_time = false;
        this.name = name;
    }

    private static void oeWrite(Consumer<OutputWriter> writer, Function<InputOutputCloseableWrapper, OutputWriter> writerGetter, boolean newOut) {
        try (InputOutputCloseableWrapper io = new InputOutputCloseableWrapper(IOProvider.getDefault().getIO ("Hello", newOut))) {
            writer.accept(writerGetter.apply(io));
        } catch (IOException ex) {

        }
    }

    private void oeWrite(Consumer<OutputWriter> writer, Function<InputOutputCloseableWrapper, OutputWriter> writerGetter) {
        oeWrite(writer, writerGetter, first_time);
        first_time = false;
    }

    public void outWrite(Consumer<OutputWriter> writer) {
        oeWrite(writer, (io) -> { return io.getOut();});
    }

    public void errWrite(Consumer<OutputWriter> writer) {
        oeWrite(writer, (io) -> { return io.getErr();});
    }
}
