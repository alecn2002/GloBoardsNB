/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alecn.glo.client;

import lombok.Getter;

/**
 *
 * @author anovitsk
 */
@Getter
public enum EBoardFields {
    ARCHIVED_COLUMNS("archived_columns"),
    ARCHIVED_DATE("archived_date"),
    COLUMNS("columns"),
    CREATED_BY("created_by"),
    CREATED_DATE("created_date"),
    INVITED_MEMBERS("invited_members"),
    LABELS("labels"),
    MEMBERS("members"),
    NAME("name");

    private final String rest_name;

    EBoardFields(String _name) {
        this.rest_name = _name;
    }
}
