package org.eclipse.daanse.calc.impl;

import java.util.Objects;
import java.util.stream.Stream;

import org.eclipse.daanse.olap.api.model.Hierarchy;

import mondrian.calc.Calc;

public class HirarchyDependsChecker {
	/**
	 * Returns true if a Child-Calc depends on the hierarchy.
	 */
	public static boolean checkAnyDependsOnChilds( Calc<?>[] calcs,Hierarchy hierarchy) {

		return Stream.of(calcs).filter(Objects::nonNull).filter(calc->calc.dependsOn(hierarchy)).findAny().isPresent();
//		for (final Calc<?> calc : calcs) {
//			if (calc != null && calc.dependsOn(hierarchy)) {
//				return true;
//			}
//		}
//		return false;
		
	}

    /**
     * Returns true if first calc depends the hierarchy, else false if calc[0] returns dimension, else true if any of the
     * other calcs depend on dimension.
     *
     * <p>
     * Typical application: <code>Aggregate({Set}, {Value Expression})</code> depends upon everything {Value Expression}
     * depends upon, except the dimensions of {Set}.
     */
    public static boolean checkAnyDependsButFirst( Calc[] calcs, Hierarchy hierarchy ) {
        if ( calcs.length == 0 ) {
            return false;
        }
        if ( calcs[0].dependsOn( hierarchy ) ) {
            return true;
        }
        if ( calcs[0].getType().usesHierarchy( hierarchy, true ) ) {
            return false;
        }
        for ( int i = 1; i < calcs.length; i++ ) {
            final Calc calc = calcs[i];
            if ( calc != null && calc.dependsOn( hierarchy ) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if any of the calcs depend on dimension, else false if any of the calcs return dimension, else true.
     */
    public static boolean butDepends( Calc[] calcs, Hierarchy hierarchy ) {
        boolean result = true;
        for ( final Calc calc : calcs ) {
            if ( calc != null ) {
                if ( calc.dependsOn( hierarchy ) ) {
                    return true;
                }
                if ( calc.getType().usesHierarchy( hierarchy, true ) ) {
                    result = false;
                }
            }
        }
        return result;
    }
}