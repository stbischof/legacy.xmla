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

package org.eclipse.daanse.calc.impl;

import org.eclipse.daanse.calc.api.StringCalc;

import mondrian.calc.Calc;
import mondrian.olap.Evaluator;
import mondrian.olap.type.Type;

/**
 * Abstract implementation of the {@link org.eclipse.daanse.calc.api.StringCalc}
 * interface.
 * 
 * Handles nested child and profiling
 *
 */
public abstract class AbstractProfilingNestedStringCalc extends AbstractProfilingNestedCalc<String>
		implements StringCalc {
	/**
	 * {@inheritDoc}
	 *
	 */
	protected AbstractProfilingNestedStringCalc(String name, Type type, Calc<?>[] calcs) {
		super(name, type, calcs);
	}

}