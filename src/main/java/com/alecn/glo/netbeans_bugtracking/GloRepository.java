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
package com.alecn.glo.netbeans_bugtracking;

import com.alecn.glo.GloConfig;
import com.alecn.glo.GloConnector;
import com.alecn.glo.client.impl.GloConstants;
import com.alecn.glo.netbeans_bugtracking.query.GloQuery;
import com.alecn.glo.service.BoardService;
import com.alecn.glo.service.CommentService;
import com.alecn.glo.service.impl.BoardServiceImpl;
import com.alecn.glo.service.impl.CommentServiceImpl;
import com.alecn.glo.util.LazyValue;
import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.netbeans.modules.bugtracking.spi.RepositoryInfo;
import org.openide.util.Lookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author anovitsk
 */
@Getter
@AllArgsConstructor
public class GloRepository {

    static final String PROPERTY_ACCESS_KEY = "accessKey";              // NOI18N

    private static final GloConfig gloConfig = Lookup.getDefault().lookup(GloConfig.class);

    // Make sure we know all instances we created - a crude hack, but API does
    // not allow ourselves ....
    private static final List<WeakReference<GloRepository>> repositoryList
            = Collections.synchronizedList(new LinkedList<WeakReference<GloRepository>>());

    {
        repositoryList.add(new WeakReference<>(this));
    }

    public static GloRepository getInstanceById(@NonNull String id) {
        synchronized(repositoryList) {
            Iterator<WeakReference<GloRepository>> it = repositoryList.iterator();
            GloRepository result = null;
            while(it.hasNext() && result == null) {
                WeakReference<GloRepository> weak = it.next();
                GloRepository hard = weak.get();
                if(hard == null) {
                    it.remove();
                } else {
                    if(id.equals(hard.getId())) {
                        result = hard;
                    }
                }
            }
            return result;
        }
    }

    private RepositoryInfo repositoryInfo;
    private final transient InstanceContent ic;

    private transient BoardService boardService;
    private transient CommentService commentService;

    private final transient LazyValue<GloRepositoryController> gloRepositoryController = new LazyValue<>(() -> new GloRepositoryController(this));

    public GloRepository() {
        this.ic = new InstanceContent();
    }

    public GloRepository(RepositoryInfo repositoryInfo) {
        this();
        this.repositoryInfo = repositoryInfo;

        this.boardService = new BoardServiceImpl(repositoryInfo.getUrl());
        this.commentService = new CommentServiceImpl(repositoryInfo.getUrl());
    }

    private String getNonNullRepoInfoValue(Function<RepositoryInfo, String> provider, String defaultValue) {
        return (repositoryInfo == null)
                ? defaultValue
                : provider.apply(repositoryInfo);
    }

    private String getNonNullRepoInfoValue(Function<RepositoryInfo, String> provider) {
        return getNonNullRepoInfoValue(provider, "");
    }

    public String getId() {
        return getNonNullRepoInfoValue((ri) -> ri.getID());
    }

    public String getDisplayName() {
        return getNonNullRepoInfoValue((ri) -> ri.getDisplayName());
    }

    public String getUrl() {
        return getNonNullRepoInfoValue((ri) -> ri.getUrl());
    }

    public String getAccessKey() {
        return getNonNullRepoInfoValue((ri) -> ri.getValue(PROPERTY_ACCESS_KEY));
    }

    public GloRepositoryController getController() {
        return gloRepositoryController.get();
    }

    public void setInfoValues() {
        this.repositoryInfo = new RepositoryInfo(UUID.randomUUID().toString(),
                GloConnector.ID,
                GloConstants.GLO_URL,
                "GLO board",  // TODO replace it with real name
                "GLO board"   // TODO replace it with real name
        );
    }

    public Image getIconImage() {
        return gloConfig.getIconImage();
    }

    //
    // Change Support
    //
    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public GloQuery createQuery() {
        return new GloQuery();
    }
}
