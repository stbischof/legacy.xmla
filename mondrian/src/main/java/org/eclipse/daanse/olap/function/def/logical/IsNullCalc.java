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
package org.eclipse.daanse.olap.function.def.logical;

import org.eclipse.daanse.olap.api.Evaluator;
import org.eclipse.daanse.olap.api.element.Member;
import org.eclipse.daanse.olap.api.type.Type;
import org.eclipse.daanse.olap.calc.api.MemberCalc;
import org.eclipse.daanse.olap.calc.base.nested.AbstractProfilingNestedBooleanCalc;

import mondrian.rolap.RolapMember;
import mondrian.rolap.RolapUtil;

public class IsNullCalc extends AbstractProfilingNestedBooleanCalc {

    protected IsNullCalc(Type type, final MemberCalc memberCalc) {
        super(type, memberCalc);
    }

    @Override
    public Boolean evaluate(Evaluator evaluator) {
        Member member = getChildCalc(0, MemberCalc.class).evaluate(evaluator);
        return member.isNull() || nonAllWithNullKey((RolapMember) member);
    }

    /**
     * Dimension members with a null value are treated as the null member.
     */
    private boolean nonAllWithNullKey(RolapMember member) {
        return !member.isAll() && member.getKey() == RolapUtil.sqlNullValue;
    }

}