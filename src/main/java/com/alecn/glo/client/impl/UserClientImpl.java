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
package com.alecn.glo.client.impl;

import com.alecn.glo.client.UserClient;
import com.alecn.glo.client.UserFieldsEnum;
import com.alecn.glo.sojo.User;
import com.alecn.glo.util.GloLogger;
import java.util.Arrays;
import java.util.Collection;


public class UserClientImpl extends GenericClientImpl<User, User, UserFieldsEnum> implements UserClient {

    private static final GloLogger LOGGER = new GloLogger(UserClientImpl.class);

    private static final UserFieldsEnum[] DEFAULT_FIELDS_LIST = {UserFieldsEnum.USERNAME};
    static final Collection<UserFieldsEnum> DEFAULT_FIELDS = Arrays.asList(DEFAULT_FIELDS_LIST);
    static final Collection<UserFieldsEnum> ALL_FIELDS = Arrays.asList(UserFieldsEnum.values());

    public UserClientImpl(String access_key) {
        super(access_key, GloConstants.GLO_PATH_USER, User.class, User[].class, LOGGER);
    }

    @Override
    public User get() {
        return super.get((String)null, null);
    }

    @Override
    public User get(Collection<UserFieldsEnum> fields) {
        return super.get(null, fields);
    }

}
