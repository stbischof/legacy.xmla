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
 *   Stefan Bischof (bipolis.org) - initial
 */
package org.eclipse.daanse.olap.function.def.tupletostr;

import org.eclipse.daanse.olap.api.Evaluator;
import org.eclipse.daanse.olap.api.element.Member;
import org.eclipse.daanse.olap.api.type.Type;
import org.eclipse.daanse.olap.calc.api.TupleCalc;
import org.eclipse.daanse.olap.calc.base.nested.AbstractProfilingNestedStringCalc;

import mondrian.olap.fun.FunUtil;

public class TupleToStrCalc extends AbstractProfilingNestedStringCalc {

    protected TupleToStrCalc(Type type, TupleCalc tupleCalc) {
        super(type, tupleCalc);
    }

    @Override
    public String evaluate(Evaluator evaluator) {
        final Member[] members = getChildCalc(0, TupleCalc.class).evaluate(evaluator);
        if (members == null) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        FunUtil.appendTuple(buf, members);
        return buf.toString();
    }

}