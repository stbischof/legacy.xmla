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
package org.eclipse.daanse.mdx.model;

import java.math.BigDecimal;

public record NumericLiteral(BigDecimal value) implements Literal {

    static final NumericLiteral ONE = new NumericLiteral(BigDecimal.ONE);
    static final NumericLiteral ZERO = new NumericLiteral(BigDecimal.ZERO);
    static final NumericLiteral NEGATIVE_ONE = new NumericLiteral(BigDecimal.ONE.negate());

}