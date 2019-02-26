package com.alecn.glo.sojo;

import lombok.EqualsAndHashCode;

/**
 *
 * @author anovitsk
 */
@EqualsAndHashCode(callSuper = true)
public abstract class NamedEntity extends Entity {
    protected String name;
}
