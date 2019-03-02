/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alecn.glo.sojo;

import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author anovitsk
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public abstract class DatedNamedEntity extends NamedEntity {
    Date archived_date;
    Date created_date;
}
