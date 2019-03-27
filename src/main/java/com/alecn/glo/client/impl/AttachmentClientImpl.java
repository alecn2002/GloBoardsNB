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

import com.alecn.glo.client.AttachmentClient;
import com.alecn.glo.client.AttachmentFields;
import com.alecn.glo.sojo.Attachment;
import com.alecn.glo.util.GloLogger;
import java.util.Collection;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

public class AttachmentClientImpl extends GenericCardPartClientImpl<Attachment, byte[], AttachmentFields> implements AttachmentClient {

    private static final GloLogger LOGGER = new GloLogger(AttachmentClientImpl.class);

    public AttachmentClientImpl(String accessKey) {
        super(accessKey, GLO_PATH_ATTACHMENTS, Attachment.class, Attachment[].class, LOGGER);
    }

    @Override
    public Collection<Attachment> list(String boardId, String cardId, Collection<AttachmentFields> fields, Integer page, Integer per_page, boolean sortDesc) {
        return super.list(fields, sortDesc, page, per_page, sortDesc,
                          boardCardIdResolverFactory(boardId, cardId));
    }

    @Override
    public Attachment create(String boardId, String cardId, byte[] attachment) {
        // TODO require uploading from file, not bytestream
        return super.post(Entity.entity(attachment, MediaType.MULTIPART_FORM_DATA_TYPE),
                          boardCardIdResolverFactory(boardId, cardId));
    }

}
