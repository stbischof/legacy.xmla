/*
* This software is subject to the terms of the Eclipse Public License v1.0
* Agreement, available at the following URL:
* http://www.eclipse.org/legal/epl-v10.html.
* You must accept the terms of that agreement to use this software.
*
* Copyright (c) 2002-2017 Hitachi Vantara..  All rights reserved.
*/

package mondrian.mdx;

import java.io.PrintWriter;

import org.eclipse.daanse.olap.api.DataType;
import org.eclipse.daanse.olap.api.Syntax;
import org.eclipse.daanse.olap.api.Validator;
import org.eclipse.daanse.olap.api.function.FunctionDefinition;
import org.eclipse.daanse.olap.api.query.component.Expression;
import org.eclipse.daanse.olap.api.query.component.UnresolvedFunCall;
import org.eclipse.daanse.olap.api.query.component.visit.QueryComponentVisitor;
import org.eclipse.daanse.olap.api.type.Type;
import org.eclipse.daanse.olap.calc.api.Calc;
import org.eclipse.daanse.olap.calc.api.compiler.ExpressionCompiler;
import org.eclipse.daanse.olap.query.base.Expressions;
import org.eclipse.daanse.olap.query.component.expression.AbstractExpression;

import mondrian.olap.Util;
import mondrian.olap.fun.FunUtil;

/**
 * An expression consisting of a named function or operator
 * applied to a set of arguments. The syntax determines whether this is
 * called infix, with function call syntax, and so forth.
 *
 * @author jhyde
 * @since Sep 28, 2005
 */
public class UnresolvedFunCallImpl extends AbstractExpression implements UnresolvedFunCall {
    private final String name;
    private final Syntax syntax;
    private final Expression[] args;

    /**
     * Creates a function call with {@link Syntax#Function} syntax.
     */
    public UnresolvedFunCallImpl(String name, Expression[] args) {
        this(name, Syntax.Function, args);
    }

    /**
     * Creates a function call.
     */
    public UnresolvedFunCallImpl(String name, Syntax syntax, Expression[] args) {
        if (name == null || syntax == null || args == null) {
            throw new IllegalArgumentException("UnresolvedFunCall: params should be not null");
        }
        this.name = name;
        this.syntax = syntax;
        this.args = args;
        switch (syntax) {
        case Braces:
            Util.assertTrue(name.equals("{}"));
            break;
        case Parentheses:
            Util.assertTrue(name.equals("()"));
            break;
        case Internal:
            Util.assertTrue(name.startsWith("$"));
            break;
        case Empty:
            Util.assertTrue(name.equals(""));
            break;
        default:
            Util.assertTrue(
                !name.startsWith("$")
                && !name.equals("{}")
                && !name.equals("()"));
            break;
        }
    }

    @Override
	@SuppressWarnings({"CloneDoesntCallSuperClone"})
    public UnresolvedFunCallImpl cloneExp() {
        return new UnresolvedFunCallImpl(name, syntax, Expressions.cloneExpressions(args));
    }

    @Override
	public DataType getCategory() {
        throw new UnsupportedOperationException();
    }

    @Override
	public Type getType() {
        throw new UnsupportedOperationException();
    }

    @Override
	public void unparse(PrintWriter pw) {
        syntax.unparse(name, args, pw);
    }

    @Override
	public Object accept(QueryComponentVisitor visitor) {
        final Object o = visitor.visitUnresolvedFunCall(this);
        if (visitor.visitChildren()) {
            // visit the call's arguments
            for (Expression arg : args) {
                arg.accept(visitor);
            }
        }
        return o;
    }

    @Override
	public Expression accept(Validator validator) {
        Expression[] newArgs = new Expression[args.length];
        FunctionDefinition funDef =
            FunUtil.resolveFunArgs(
                validator, null, args, newArgs, name, syntax);
        return funDef.createCall(validator, newArgs);
    }

    @Override
	public Calc accept(ExpressionCompiler compiler) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the function name.
     *
     * @return function name
     */
    @Override
	public String getFunName() {
        return name;
    }

    /**
     * Returns the syntax of this function call.
     *
     * @return the syntax of the call
     */
    @Override
	public Syntax getSyntax() {
        return syntax;
    }

    /**
     * Returns the Exp argument at the specified index.
     *
     * @param      index   the index of the Exp.
     * @return     the Exp at the specified index of this array of Exp.
     *             The first Exp is at index <code>0</code>.
     * @see #getArgs()
     */
    @Override
	public Expression getArg(int index) {
        return args[index];
    }

    /**
     * Returns the internal array of Exp arguments.
     *
     * <p>Note: this does NOT do a copy.
     *
     * @return the array of expressions
     */
    @Override
	public Expression[] getArgs() {
        return args;
    }

    /**
     * Returns the number of arguments.
     *
     * @return number of arguments.
     * @see #getArgs()
     */
    @Override
	public final int getArgCount() {
        return args.length;
    }

    @SuppressWarnings("java:S4144") //we have getArgs method
    @Override
	public Object[] getChildren() {
        return args;
    }
}