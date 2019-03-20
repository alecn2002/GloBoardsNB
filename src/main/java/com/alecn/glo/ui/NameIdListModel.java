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
package com.alecn.glo.ui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 *
 * @author alecn
 */
public class NameIdListModel<T> extends AbstractListModel<T> implements ComboBoxModel<T> {

    public class NameRenderer extends BasicComboBoxRenderer {
        @Override
        public Component getListCellRendererComponent(
            JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus)
        {
            super.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);

            if (value == null) {
                setText("");
            } else {
                setText(getDisplayName((T)value));
            }
            return this;
        }
    }

    private final List<T> list;

    private final Function<T, String> nameVisitor;

    private final Function<T, String> idVisitor;

    private Integer selectionId;


    public NameIdListModel(Collection<T> list, Function<T, String> nameVisitor, Function<T, String> idVisitor, Integer selectionId) {
        this.list = list instanceof List
                ? (List<T>)list
                : new ArrayList<>(list);
        this.nameVisitor = nameVisitor;
        this.idVisitor = idVisitor;
        this.selectionId = selectionId;
    }

    public NameIdListModel(Collection<T> list, Function<T, String> nameVisitor, Function<T, String> idVisitor) {
        this(list, nameVisitor, idVisitor, null);
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public T getElementAt(int index) {
        return list.get(index);
    }

    public String getId(T v) {
        return idVisitor.apply(v);
    }

    public String getName(T v) {
        return nameVisitor.apply(v);
    }

    public String getDisplayName(T v) {
        StringBuilder sb = new StringBuilder();
        return sb.append(nameVisitor.apply(v))
                .append(" (")
                .append(idVisitor.apply(v))
                .append(")")
                .toString();
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectionId = null;
        if (anItem == null) {
            return;
        }
        for (int i = 0 ; i < list.size() ; ++i) {
            if (anItem.equals(list.get(i))) {
                selectionId = i;
                break;
            }
        }
    }

    @Override
    public Object getSelectedItem() {
        return selectionId == null
                ? null
                : list.get(selectionId);
    }

    public BasicComboBoxRenderer rendererFactory() {
        return new NameRenderer();
    }

    public void setThisModelToControl(JComboBox<T> control) {
            control.setModel(this);
            control.setRenderer(rendererFactory());
    }
}
