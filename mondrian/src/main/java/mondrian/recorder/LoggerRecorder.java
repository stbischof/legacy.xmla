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

import org.slf4j.Logger;

/**
 * Implementation of {@link MessageRecorder} that writes to a
 * {@link Logger logger}.
 *
 * @author Richard M. Emberson
 */
public class LoggerRecorder extends AbstractRecorder {
    private final Logger logger;

    public LoggerRecorder(final Logger logger) {
        this.logger = logger;
    }

    @Override
	protected void recordMessage(
        final String msg,
        final Object info,
        final MsgType msgType)
    {
        String context = getContext();
        logMessage(context, msg, msgType, logger);
    }
}
