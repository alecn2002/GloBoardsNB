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

import com.alecn.glo.netbeans_bugtracking.GloRepository;
import com.alecn.glo.netbeans_bugtracking.issue.GloIssue;
import com.alecn.glo.netbeans_bugtracking.issue.GloIssueScheduleProvider;
import com.alecn.glo.netbeans_bugtracking.providers.GloQueryProvider;
import com.alecn.glo.netbeans_bugtracking.providers.GloIssueProvider;
import com.alecn.glo.netbeans_bugtracking.providers.GloRepositoryProvider;
import com.alecn.glo.netbeans_bugtracking.query.GloQuery;
import java.awt.Image;
import org.netbeans.modules.bugtracking.spi.BugtrackingSupport;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author anovitsk
 */
@ServiceProvider(service = GloConfig.class)
public class GloConfig {

    public static final String GLO_ICON_PATH = "com/alecn/glo/icons/";

    public static final String GLO_ICON_16x16 = "globoard16x16.png";

    public static final String GLO_ICON_16x16_FULL = GLO_ICON_PATH + GLO_ICON_16x16;

    private static final GloIssueProvider gloIssueProvider = Lookup.getDefault().lookup(GloIssueProvider.class);

    private static final GloIssueScheduleProvider gloIssueScheduleProvider = Lookup.getDefault().lookup(GloIssueScheduleProvider.class);

    private static final GloQueryProvider gloQueryProvider = Lookup.getDefault().lookup(GloQueryProvider.class);

    private static final GloRepositoryProvider gloRepositoryProvider = Lookup.getDefault().lookup(GloRepositoryProvider.class);

    private static final BugtrackingSupport<GloRepository, GloQuery, GloIssue> support = new BugtrackingSupport<>(gloRepositoryProvider, gloQueryProvider, gloIssueProvider);

    public GloIssueProvider getIssueProvider() {
        return gloIssueProvider;
    }

    public GloIssueScheduleProvider getIssueScheduleProvider() {
        return gloIssueScheduleProvider;
    }

    public GloQueryProvider getQueryProvider() {
        return gloQueryProvider;
    }

    public GloRepositoryProvider getRepositoryProvider() {
        return gloRepositoryProvider;
    }

    public BugtrackingSupport<GloRepository, GloQuery, GloIssue> getSupport() {
        return support;
    }

    public Image getImage(String imageName) {
        return ImageUtilities.loadImage(GLO_ICON_PATH + imageName);
    }

    public Image getIconImage() {
        return getImage(GLO_ICON_16x16);
    }
}
