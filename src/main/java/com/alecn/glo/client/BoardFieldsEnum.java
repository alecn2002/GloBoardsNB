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
package com.alecn.glo.client;

import lombok.Getter;

/**
 *
 * @author AlecN <alecn2002@gmail.com>
 */
@Getter
public enum BoardFieldsEnum implements FieldsEnumI {
    ARCHIVED_COLUMNS("archived_columns"),
    ARCHIVED_DATE("archived_date"),
    COLUMNS("columns"),
    CREATED_BY("created_by"),
    CREATED_DATE("created_date"),
    INVITED_MEMBERS("invited_members"),
    LABELS("labels"),
    MEMBERS("members"),
    NAME("name");

    private final String restName;

    BoardFieldsEnum(String _name) {
        this.restName = _name;
    }
}
