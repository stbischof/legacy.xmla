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
package org.eclipse.daanse.olap.function.def.crossjoin;

import java.util.List;

import org.eclipse.daanse.olap.api.Evaluator;
import org.eclipse.daanse.olap.api.Execution;
import org.eclipse.daanse.olap.api.NativeEvaluator;
import org.eclipse.daanse.olap.api.SchemaReader;
import org.eclipse.daanse.olap.api.element.Member;
import org.eclipse.daanse.olap.api.query.component.ResolvedFunCall;
import org.eclipse.daanse.olap.calc.api.Calc;
import org.eclipse.daanse.olap.calc.api.ResultStyle;
import org.eclipse.daanse.olap.calc.api.todo.TupleCursor;
import org.eclipse.daanse.olap.calc.api.todo.TupleIterable;
import org.eclipse.daanse.olap.calc.api.todo.TupleIteratorCalc;
import org.eclipse.daanse.olap.calc.api.todo.TupleList;

import mondrian.calc.impl.AbstractIterCalc;
import mondrian.calc.impl.AbstractTupleCursor;
import mondrian.calc.impl.AbstractTupleIterable;
import mondrian.calc.impl.TupleCollections;
import mondrian.olap.Util;
import mondrian.server.LocusImpl;
import mondrian.util.CancellationChecker;

public class CrossJoinIterCalc extends AbstractIterCalc {
    private ResolvedFunCall call;
    private int ctag;
    public CrossJoinIterCalc( ResolvedFunCall call, Calc[] calcs, int ctag ) {
      super( call.getType(), calcs );
      this.call=call;
      this.ctag = ctag;
    }

    @Override
    public TupleIterable evaluateIterable( Evaluator evaluator ) {

      // Use a native evaluator, if more efficient.
      // TODO: Figure this out at compile time.
      SchemaReader schemaReader = evaluator.getSchemaReader();
      NativeEvaluator nativeEvaluator =
          schemaReader.getNativeSetEvaluator( call.getFunDef(), call.getArgs(), evaluator, this );
      if ( nativeEvaluator != null ) {
        return (TupleIterable) nativeEvaluator.execute( ResultStyle.ITERABLE );
      }

      Calc[] calcs = getChildCalcs();
      TupleIteratorCalc calc1 = (TupleIteratorCalc) calcs[0];
      TupleIteratorCalc calc2 = (TupleIteratorCalc) calcs[1];

      TupleIterable o1 = calc1.evaluateIterable( evaluator );
      if ( o1 instanceof TupleList l1 ) {
        l1 = CrossJoinFunDef.nonEmptyOptimizeList( evaluator, l1, call, ctag );
        if ( l1.isEmpty() ) {
          return TupleCollections.emptyList( getType().getArity() );
        }
        o1 = l1;
      }

      TupleIterable o2 = calc2.evaluateIterable( evaluator );
      if ( o2 instanceof TupleList l2 ) {
        l2 = CrossJoinFunDef.nonEmptyOptimizeList( evaluator, l2, call, ctag );
        if ( l2.isEmpty() ) {
          return TupleCollections.emptyList( getType().getArity() );
        }
        o2 = l2;
      }

      return makeIterable( o1, o2 );
    }

    public TupleIterable makeIterable( final TupleIterable it1, final TupleIterable it2 ) {
      // There is no knowledge about how large either it1 ore it2
      // are or how many null members they might have, so all
      // one can do is iterate across them:
      // iterate across it1 and for each member iterate across it2

      return new AbstractTupleIterable( it1.getArity() + it2.getArity() ) {
        @Override
        public TupleCursor tupleCursor() {
          return new AbstractTupleCursor( getArity() ) {
            final TupleCursor i1 = it1.tupleCursor();
            final int arity1 = i1.getArity();
            TupleCursor i2 = TupleCollections.emptyList( 1 ).tupleCursor();
            final Member[] members = new Member[arity];

            long currentIteration = 0;
            Execution execution = LocusImpl.peek().getExecution();

            @Override
            public boolean forward() {
              if ( i2.forward() ) {
                return true;
              }
              while ( i1.forward() ) {
                CancellationChecker.checkCancelOrTimeout( currentIteration++, execution );
                i2 = it2.tupleCursor();
                if ( i2.forward() ) {
                  return true;
                }
              }
              return false;
            }

            @Override
            public List<Member> current() {
              i1.currentToArray( members, 0 );
              i2.currentToArray( members, arity1 );
              return Util.flatList( members );
            }

            @Override
            public Member member( int column ) {
              if ( column < arity1 ) {
                return i1.member( column );
              } else {
                return i2.member( column - arity1 );
              }
            }

            @Override
            public void setContext( Evaluator evaluator ) {
              i1.setContext( evaluator );
              i2.setContext( evaluator );
            }

            @Override
            public void currentToArray( Member[] members, int offset ) {
              i1.currentToArray( members, offset );
              i2.currentToArray( members, offset + arity1 );
            }
          };
        }
      };
    }
  }