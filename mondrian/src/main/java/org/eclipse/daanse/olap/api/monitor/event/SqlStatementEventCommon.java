/*
* Copyright (c) 2024 Contributors to the Eclipse Foundation.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*   SmartCity Jena - initial
*/

package org.eclipse.daanse.olap.api.monitor.event;

import org.eclipse.daanse.olap.api.Statement;
import org.eclipse.daanse.olap.api.monitor.event.SqlStatementEvent.Purpose;

import mondrian.server.Locus;

public record SqlStatementEventCommon(EventCommon eventCommon, long mdxStatementId, long sqlStatementId, String sql,
		Purpose purpose) {

	public static long mdxStatementIdOf(Locus locus) {
		if (locus.execution != null) {
			final Statement statement = locus.execution.getMondrianStatement();
			if (statement != null) {
				return statement.getId();
			}
		}
		return -1;
	}

}