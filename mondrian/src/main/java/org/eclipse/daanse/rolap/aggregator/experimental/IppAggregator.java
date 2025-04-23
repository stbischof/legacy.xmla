/*
* Copyright (c) 2025 Contributors to the Eclipse Foundation.
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
package org.eclipse.daanse.rolap.aggregator.experimental;

import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.daanse.olap.api.DataTypeJdbc;
import org.eclipse.daanse.olap.api.Evaluator;
import org.eclipse.daanse.olap.api.calc.Calc;
import org.eclipse.daanse.olap.api.calc.todo.TupleList;
import org.eclipse.daanse.rolap.aggregator.AbstractAggregator;

public class IppAggregator extends AbstractAggregator {

    public static IppAggregator INSTANCE = new IppAggregator();

    private static AtomicLong i = new AtomicLong(0);

    public IppAggregator() {
        super("Ipp", false);
    }

    public Object aggregate(Evaluator evaluator, TupleList members, Calc<?> calc) {
        return i.incrementAndGet();
    }

    @Override
    public StringBuilder getExpression(CharSequence operand) {
        StringBuilder buf = new StringBuilder(64);
        buf.append(i.incrementAndGet());
        return buf;
    }

    public boolean supportsFastAggregates(DataTypeJdbc dataType) {
        return true;
    }

}
