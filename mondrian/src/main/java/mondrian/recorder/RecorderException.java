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

package mondrian.recorder;

import org.eclipse.daanse.olap.api.exception.OlapRuntimeException;

/**
 * Exception thrown by {@link MessageRecorder} when too many errors
 * have been reported.
 *
 * @author Richard M. Emberson
 */
public final class RecorderException extends OlapRuntimeException {
     protected RecorderException(String msg) {
        super(msg);
    }
}
