/*
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// You must accept the terms of that agreement to use this software.
//
// Copyright (C) 2005-2005 Julian Hyde
// Copyright (C) 2005-2017 Hitachi Vantara and others
// All Rights Reserved.
*/

package mondrian.olap;

import org.eclipse.daanse.olap.api.exception.OlapRuntimeException;

/**
 * Exception which indicates that a Cube is invalid
 * because there is a hierarchy with no members.
 */
public class InvalidHierarchyException extends OlapRuntimeException {
    /**
     * Creates a InvalidHierarchyException.
     *
     * @param message Localized error message
     */
    public InvalidHierarchyException(String message) {
        super(message);
    }
}
