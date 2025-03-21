/*
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// You must accept the terms of that agreement to use this software.
//
// Copyright (C) 2004-2005 TONBELLER AG
// Copyright (C) 2005-2017 Hitachi Vantara and others
// All Rights Reserved.
*/

package mondrian.olap;

import org.eclipse.daanse.olap.api.exception.OlapRuntimeException;

/**
 * Abstract base class for exceptions that indicate some limit was exceeded.
 */
public abstract class ResultLimitExceededException extends OlapRuntimeException {

    /**
     * Creates a ResultLimitExceededException.
     *
     * @param message Localized message
     */
    public ResultLimitExceededException(String message) {
        super(message);
    }
}
