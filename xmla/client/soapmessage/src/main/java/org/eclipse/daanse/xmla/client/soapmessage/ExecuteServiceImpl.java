/*
 * Copyright (c) 2023 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   SmartCity Jena - initial
 *   Stefan Bischof (bipolis.org) - initial
 */
package org.eclipse.daanse.xmla.client.soapmessage;

import org.eclipse.daanse.xmla.api.execute.ExecuteService;
import org.eclipse.daanse.xmla.api.execute.alter.AlterRequest;
import org.eclipse.daanse.xmla.api.execute.alter.AlterResponse;
import org.eclipse.daanse.xmla.api.execute.statement.StatementRequest;
import org.eclipse.daanse.xmla.api.execute.statement.StatementResponse;

public class ExecuteServiceImpl implements ExecuteService {

    public ExecuteServiceImpl(SoapClient client) {
        // TODO Auto-generated constructor stub
    }

    @Override
    public StatementResponse statement(StatementRequest statementRequest) {
        // TODO Auto-generated constructor stub
        return null;
    }

    @Override
    public AlterResponse alter(AlterRequest statementRequest) {
        // TODO Auto-generated constructor stub
        return null;
    }
}