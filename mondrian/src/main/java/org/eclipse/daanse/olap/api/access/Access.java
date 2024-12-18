/*
 * This software is subject to the terms of the Eclipse Public License v1.0
 * Agreement, available at the following URL:
 * http://www.eclipse.org/legal/epl-v10.html.
 * You must accept the terms of that agreement to use this software.
 *
 * Copyright (C) 2003-2005 Julian Hyde
 * Copyright (C) 2005-2017 Hitachi Vantara and others
 * All Rights Reserved.
 *
 * Contributors:
 *   SmartCity Jena - refactor, clean API
 */

package org.eclipse.daanse.olap.api.access;

/**
 * <code>Access</code> enumerates the allowable access rights.
 *
 * @author jhyde
 * @since Feb 21, 2003
 */
public enum Access {
    /** No access to an object and its children. */
    NONE,
    /**
     * A grant that covers none of the children
     * unless explicitly granted.
     */
    CUSTOM,
    /**
     * Grant that covers all children except those denied.
     * (internal use only)
     */
    RESTRICTED,
    /** Access to all shared dimensions (applies to schema grant). */
    ALL_DIMENSIONS,
    /** All access to an object and its children. */
    ALL;
    @Override
	public String toString() {
        return this.name();
    }
}
