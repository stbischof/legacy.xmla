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
package org.eclipse.daanse.olap.function.def.set.siblings;

import static org.opencube.junit5.TestUtil.assertAxisReturns;
import static org.opencube.junit5.TestUtil.assertExprReturns;

import org.eclipse.daanse.olap.api.Context;
import org.junit.jupiter.params.ParameterizedTest;
import org.opencube.junit5.ContextSource;
import org.opencube.junit5.dataloader.FastFoodmardDataLoader;
import org.opencube.junit5.propupdator.AppandFoodMartCatalog;


class SiblingsFunDefTest {

    @ParameterizedTest
    @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
    void testSiblingsA(Context<?> context) {
        assertAxisReturns(context.getConnectionWithDefaultRole(), "Sales",
            "{[Time].[1997].Siblings}",
            "[Time].[Time].[1997]\n"
                + "[Time].[Time].[1998]" );
    }

    @ParameterizedTest
    @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
    void testSiblingsB(Context<?> context) {
        assertAxisReturns(context.getConnectionWithDefaultRole(), "Sales",
            "{[Store].Siblings}",
            "[Store].[Store].[All Stores]" );
    }

    @ParameterizedTest
    @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
    void testSiblingsC(Context<?> context) {
        assertAxisReturns(context.getConnectionWithDefaultRole(), "Sales",
            "{[Store].[USA].[CA].Siblings}",
            "[Store].[Store].[USA].[CA]\n"
                + "[Store].[Store].[USA].[OR]\n"
                + "[Store].[Store].[USA].[WA]" );
    }

    @ParameterizedTest
    @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
    void testSiblingsD(Context<?> context) {
        // The null member has no siblings -- not even itself
        assertAxisReturns(context.getConnectionWithDefaultRole(), "Sales", "{[Gender].Parent.Siblings}", "" );

        assertExprReturns(context.getConnectionWithDefaultRole(), "Sales",
            "count ([Gender].parent.siblings, includeempty)", "0" );
    }

}
