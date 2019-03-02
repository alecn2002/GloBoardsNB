package com.alecn.glo.sojo;

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
public abstract class NamedEntity extends Entity {
    protected String name;
}
