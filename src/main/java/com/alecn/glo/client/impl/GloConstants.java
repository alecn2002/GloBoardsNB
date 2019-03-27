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

/**
 *
 * @author AlecN <alecn2002@gmail.com>
 */
public abstract class GloConstants {
    public static final String GLO_URL = "https://gloapi.gitkraken.com/v1/glo/";

    protected static final String GLO_PATH_BOARD_ID = "board_id";
    protected static final String GLO_PATH_COLUMN_ID = "column_id";
    protected static final String GLO_PATH_CARD_ID = "card_id";
    protected static final String GLO_PATH_COMMENT_ID = "comment_id";

    protected static final String GLO_PATH_BOARDS = "boards";
    protected static final String GLO_PATH_BOARD = GLO_PATH_BOARDS + "/{" + GLO_PATH_BOARD_ID + "}";

    protected static final String GLO_PATH_COLUMNS = GLO_PATH_BOARD + "/columns";
    protected static final String GLO_PATH_COLUMN = GLO_PATH_COLUMNS + "/{" + GLO_PATH_COLUMN_ID + "}";

    protected static final String GLO_PATH_CARDS = GLO_PATH_BOARD + "/cards";
    protected static final String GLO_PATH_CARD = GLO_PATH_BOARD + "/cards/{" + GLO_PATH_CARD_ID + "}";

    protected static final String GLO_PATH_COLUMN_CARDS = GLO_PATH_COLUMN + "/cards";

    protected static final String GLO_PATH_ATTACHMENTS = GLO_PATH_CARD + "/attachments";

    protected static final String GLO_PATH_COMMENTS = GLO_PATH_CARD + "/comments";
    protected static final String GLO_PATH_COMMENT = GLO_PATH_COMMENTS + "/{" + GLO_PATH_COMMENT_ID + "}";

    protected static final String GLO_PATH_LABELS = GLO_PATH_BOARD + "/labels";

    protected static final String GLO_PATH_USER = "user";
}
