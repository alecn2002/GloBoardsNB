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
package com.alecn.glo.netbeans_bugtracking.repository;

import com.alecn.glo.GloConfig;
import com.alecn.glo.GloConnector;
import com.alecn.glo.client.BoardFieldsEnum;
import com.alecn.glo.client.CardFieldsEnum;
import com.alecn.glo.client.impl.GloConstants;
import com.alecn.glo.netbeans_bugtracking.issue.GloIssue;
import com.alecn.glo.netbeans_bugtracking.query.GloQuery;
import com.alecn.glo.service.BoardService;
import com.alecn.glo.service.CardService;
import com.alecn.glo.service.CommentService;
import com.alecn.glo.service.impl.BoardServiceImpl;
import com.alecn.glo.service.impl.CardServiceImpl;
import com.alecn.glo.service.impl.CommentServiceImpl;
import com.alecn.glo.sojo.Board;
import com.alecn.glo.sojo.BoardMember;
import com.alecn.glo.sojo.Card;
import com.alecn.glo.sojo.Column;
import com.alecn.glo.util.Cache;
import com.alecn.glo.util.GloLogger;
import com.alecn.glo.util.LazyValue;
import com.alecn.glo.util.OeWriter;
import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
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

    static final GloLogger LOGGER = new GloLogger(GloRepository.class);

    static final String PROPERTY_ACCESS_KEY = "accessKey";              // NOI18N

    static final String PROPERTY_BOARD_ID = "boardId";              // NOI18N

    static final String PROPERTY_BOARD_NAME = "boardName";              // NOI18N

    private static final GloConfig gloConfig = Lookup.getDefault().lookup(GloConfig.class);

    private static final OeWriter oeWriter = gloConfig.getOeWriter();

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
    private transient CardService cardService;
    private transient CommentService commentService;

    private final transient LazyValue<GloRepositoryController> gloRepositoryController = new LazyValue<>(() -> new GloRepositoryController(this));

    private final transient Cache<Column> columns;
    private final transient Cache<Card> cards;
    private final transient Cache<Board> boards;
    private final transient Cache<Board> currentBoard;
    private final transient Cache<BoardMember> members;

    public GloRepository() {
        this.ic = new InstanceContent();
        this.columns = new Cache(() -> listColumns());
        this.cards = new Cache(() -> listCards());
        this.boards = new Cache(() -> listBoards());
        this.currentBoard = new Cache(() -> fetchCurrentBoard());
        this.members = new Cache(() -> {
            List<BoardMember> boardMembers = new ArrayList<>(listBoardMembers());
            boardMembers.addAll(listInvitedMembers());
            return boardMembers;
        }
        );
    }

    public GloRepository(RepositoryInfo repositoryInfo) {
        this();
        this.repositoryInfo = repositoryInfo;
        createServices();
    }

    private void createServices() {
        oeWriter.outWrite(o -> o.println(">>>> createServices()"));
        String access_key = getNonNullRepoInfoValue((r) -> r.getValue(PROPERTY_ACCESS_KEY), "");
        if (access_key.isEmpty()) {
            oeWriter.errWrite(e -> e.println("access_key.isEmpty"));
            return;
        }
        this.boardService = new BoardServiceImpl(access_key);
        this.cardService = new CardServiceImpl(access_key);
        this.commentService = new CommentServiceImpl(access_key);
        oeWriter.outWrite(o -> o.println("<<<< createServices() - services created"));
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

    public void setInfoValues(String name, String access_key, String boardId, String boardName) {
        String id = (this.repositoryInfo == null)
                ? UUID.randomUUID().toString()
                : this.repositoryInfo.getID();
        this.repositoryInfo = new RepositoryInfo(id,
                    GloConnector.ID,
                    GloConstants.GLO_URL,
                    name,
                    name + " (" + boardName + ")"
            );
        repositoryInfo.putValue(PROPERTY_ACCESS_KEY, access_key);
        repositoryInfo.putValue(PROPERTY_BOARD_ID, boardId);
        repositoryInfo.putValue(PROPERTY_BOARD_NAME, boardName);

        createServices();
    }

    public Image getIconImage() {
        return gloConfig.getIconImage();
    }

    public GloQuery createQuery() {
        return new GloQuery(this);
    }

    public GloIssue createNewIssue() {
        return new GloIssue(Card.builder().board_id(getBoardId()).build(), this);
    }

    public Collection<Board> getBoardsList() {
        return boards.getCache().values();
    }

    public Board getCurrentBoard() {
        return currentBoard.getElementById(getBoardId());
    }

    public List<BoardMember> listBoardMembers() {
        List<BoardMember> ans = getCurrentBoard().getMembers();
        LOGGER.info("List of BoardUsers contains %d : [%s]", ans.size(),
                String.join(", ", ans.stream()
                .map(bu -> String.format("%s <%s> as %s", bu.getName(), bu.getUsername(), bu.getRole()))
                .collect(Collectors.toList())));
        return ans;
    }

    public List<BoardMember> listInvitedMembers() {
        return getCurrentBoard().getInvited_members();
    }

    public BoardMember getBoardMemberById(String id) {
        return members.getElementById(id);
    }

    private List<Board> listBoards() {
        oeWriter.outWrite(o -> o.println(">>>> getBoardsList()"));
        if (boardService == null) {
            oeWriter.errWrite(o -> o.println("boardService == null"));
            return new ArrayList<>();
        }
        List<Board> list = boardService.listBoards(Arrays.asList(BoardFieldsEnum.values())); // TODO caching
        oeWriter.outWrite(o -> o.printf("list of boards contains %d items\n", list.size()));
        return list;
    }

    public String getBoardId() {
        return repositoryInfo == null
                ? null
                : repositoryInfo.getValue(PROPERTY_BOARD_ID);
    }

    public void removed() {

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

    private List<Column> listColumns() {
        oeWriter.outWrite(o -> o.println(">>>> getBoardsList()"));
        if (boardService == null) {
            oeWriter.errWrite(o -> o.println("boardService == null"));
            return new ArrayList<>();
        }
        List<Column> list = boardService.listBoardColumns(getBoardId()); // TODO caching
        oeWriter.outWrite(o -> o.printf("list of columns contains %d items\n", list.size()));
        return list;
    }

    public Collection<Column> getColumns() {
        return columns.getCache().values();
    }

    public void refreshColumns() {
        columns.refresh();
    }

    private List<Card> listCards() {
        oeWriter.outWrite(o -> o.println(">>>> getBoardsList()"));
        if (boardService == null) {
            oeWriter.errWrite(o -> o.println("boardService == null"));
            return new ArrayList<>();
        }
        List<Card> list = boardService.listCards(getBoardId(), Arrays.asList(CardFieldsEnum.values())); // TODO caching
        oeWriter.outWrite(o -> o.printf("list of cards contains %d items\n", list.size()));
        return list;
    }

    public Collection<Card> getCards() {
        return cards.getCache().values();
    }

    public void refreshCards() {
        cards.refresh();
    }

    private List<Board> fetchCurrentBoard() {
        String boardId = getBoardId();
        if (boardId == null) {
            return null;
        }
        Board[] local_boards = new Board[] {boardService.getBoardWithAllFields(boardId)};
        return Arrays.asList(local_boards);
    }

    private static class CountingMap<K> {
        private final Map<K, Integer> counter;

        public CountingMap() {
            this.counter  = new HashMap<>();
        }

        public Integer getNext(K key) {
            Integer ans = counter.get(key);
            counter.put(key, 1 + ((ans == null) ? 0 : ans));
            return ans;
        }
    }

    public List<GloQuery> getQueries() {
        refreshCards();
        CountingMap<String> usedNames = new CountingMap<>();
        return getColumns().stream()
                .map(col -> {
                    Integer id = usedNames.getNext(col.getName());
                    return new HashMap.SimpleEntry<>(
                            col.getId(),
                            id == null
                                ? col.getName()
                                : String.format("%s (%d)", col.getName(), id)
                    );
                })
                .map(in -> new GloQuery(this, in.getKey(), in.getValue()))
                .collect(Collectors.toList());
    }
}
