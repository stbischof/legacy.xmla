/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation.
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
package mondrian.olap;

import java.io.PrintWriter;

import org.eclipse.daanse.olap.api.DataType;
import org.eclipse.daanse.olap.api.query.component.SymbolLiteral;
import org.eclipse.daanse.olap.api.query.component.visit.QueryComponentVisitor;
import org.eclipse.daanse.olap.api.type.Type;
import org.eclipse.daanse.olap.calc.api.Calc;
import org.eclipse.daanse.olap.calc.api.compiler.ExpressionCompiler;
import org.eclipse.daanse.olap.calc.base.constant.ConstantStringCalc;

import mondrian.olap.type.StringType;
import mondrian.olap.type.SymbolType;

public class SymbolLiteralImpl extends AbstractLiteralImpl<String> implements SymbolLiteral {

	private SymbolLiteralImpl(String o) {
		super(o);
	}

	public static AbstractLiteralImpl<String> create(String s) {
		return new SymbolLiteralImpl(s);
	}

	@Override
	public Object accept(QueryComponentVisitor visitor) {
		return visitor.visitLiteral(this);
	}

	@Override
	public DataType getCategory() {
		return DataType.SYMBOL;
	}

	@Override
	public Type getType() {
		return SymbolType.INSTANCE;
	}

	@Override
	public void unparse(PrintWriter pw) {
		pw.print(getValue());
	}

	@Override
	public Calc<String> accept(ExpressionCompiler compiler) {

		// why is this not a symbolType?
		return new ConstantStringCalc(StringType.INSTANCE, getValue());
	}
}