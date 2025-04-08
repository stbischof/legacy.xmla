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

import org.eclipse.daanse.olap.api.DataTypeJdbc;
import org.eclipse.daanse.olap.api.Evaluator;
import org.eclipse.daanse.olap.api.calc.Calc;
import org.eclipse.daanse.olap.api.calc.todo.TupleList;
import org.eclipse.daanse.rolap.aggregator.AbstractAggregator;

public class NoneAggregator extends AbstractAggregator {

    public static NoneAggregator INSTANCE = new NoneAggregator();

    public NoneAggregator() {
        super("None", false);
    }

    public Object aggregate(Evaluator evaluator, TupleList members, Calc<?> calc) {

        final Evaluator eval = evaluator.pushAggregation(members);
        eval.setNonEmpty(false);
        return eval.evaluateCurrent(); // never calc and cache always value from db.
    }

    @Override
    public StringBuilder getExpression(CharSequence operand) {
        StringBuilder buf = new StringBuilder(64);
        buf.append("NTH_VALUE");
        buf.append('(');
        buf.append(operand);
        buf.append(") OVER (ORDER BY ");
        buf.append(operand);
        buf.append(')');
        return buf;
    }

    public boolean supportsFastAggregates(DataTypeJdbc dataType) {
        return false;
    }
}
