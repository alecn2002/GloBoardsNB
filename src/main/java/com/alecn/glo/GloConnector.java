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
package com.alecn.glo;

import org.netbeans.modules.bugtracking.api.Repository;
import org.netbeans.modules.bugtracking.spi.BugtrackingConnector;
import org.netbeans.modules.bugtracking.spi.RepositoryInfo;
import org.openide.util.NbBundle;

/**
 *
 * @author anovitsk
 */
@NbBundle.Messages({
    "LBL_ConnectorTooltip=NetBeans plugin for integration with GLO Boards"
})
@BugtrackingConnector.Registration(id = GloConnector.ID,
        displayName = GloConnector.NAME,
        tooltip = "#LBL_ConnectorTooltip",
        iconPath = "com/alecn/glo/icons/globoard16x16.png")
public class GloConnector implements BugtrackingConnector {

    public static final String ID = "com.alecn.glo";
    public static final String NAME = "GLO Boards";

    @Override
    public Repository createRepository() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Repository createRepository(RepositoryInfo ri) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
