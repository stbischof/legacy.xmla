/*
* This software is subject to the terms of the Eclipse Public License v1.0
* Agreement, available at the following URL:
* http://www.eclipse.org/legal/epl-v10.html.
* You must accept the terms of that agreement to use this software.
*
* Copyright (c) 2002-2017 Hitachi Vantara..  All rights reserved.
*/

package org.eclipse.daanse.olap.api;

import org.eclipse.daanse.olap.api.calc.ResultStyle;

/**
 * Allows expressions to be evaluated native, e.g. in SQL.
 *
 * @author av
 * @since Nov 11, 2005
 */

public interface NativeEvaluator {
    Object execute(ResultStyle resultStyle);
}
