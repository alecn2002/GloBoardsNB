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

import java.util.List;
import java.util.function.Function;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author alecn
 */
public class NameIdListModel<T> extends AbstractListModel<T> implements ComboBoxModel<T> {

    private final List<T> list;

    private final Function<T, String> nameVisitor;

    private final Function<T, String> idVisitor;

    private Integer selectionId;


    public NameIdListModel(List<T> list, Function<T, String> nameVisitor, Function<T, String> idVisitor) {
        this.list = list;
        this.nameVisitor = nameVisitor;
        this.idVisitor = idVisitor;
        this.selectionId = null;
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
}
