/*
 * The MIT License
 *
 * Copyright 2019 anovitsk.
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
 * @author <a href="mailto:alecn2002@gmail.com">AlecN</a>
 */
@Getter
public enum CardFieldsEnum implements FieldsEnumI {

    ARCHIVED_DATE("archived_date"),
    ASSIGNEES("assignees"),
    ATTACHMENT_COUNT("attachment_count"),
    BOARD_ID("board_id"),
    COLUMN_ID("column_id"),
    COMMENT_COUNT("comment_count"),
    COMPLETED_TASK_COUNT("completed_task_count"),
    CREATED_BY("created_by"),
    CREATED_DATE("created_date"),
    DUE_DATE("due_date"),
    DESCRIPTION("description"),
    LABELS("labels"),
    NAME("name"),
    TOTAL_TASK_COUNT("total_task_count"),
    UPDATED_DATE("updated_date");

    private final String restName;

    private CardFieldsEnum(String restName) {
        this.restName = restName;
    }
}
