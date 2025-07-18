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
package org.eclipse.daanse.olap.function.def.level.numeric;

import org.eclipse.daanse.olap.api.Connection;
import org.eclipse.daanse.olap.api.Context;
import org.junit.jupiter.params.ParameterizedTest;
import org.opencube.junit5.ContextSource;
import org.opencube.junit5.TestUtil;
import org.opencube.junit5.dataloader.FastFoodmardDataLoader;
import org.opencube.junit5.propupdator.AppandFoodMartCatalog;

class LevelsNumericPropertyDefTest {

    @ParameterizedTest
    @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
    void testLevelsNumeric(Context<?> context) {
        Connection connection = context.getConnectionWithDefaultRole();
        TestUtil.assertExprReturns(connection, "Sales", "[Time].[Time].Levels(2).Name", "Month" );
        TestUtil.assertExprReturns(connection, "Sales", "[Time].[Time].Levels(0).Name", "Year" );
        TestUtil.assertExprReturns(connection, "Sales", "[Product].Levels(0).Name", "(All)" );
    }

    @ParameterizedTest
    @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
    void testLevelsTooSmall(Context<?> context) {
        TestUtil.assertExprThrows(context.getConnectionWithDefaultRole(), "Sales",
            "[Time].[Time].Levels(-1).Name", "Index '-1' out of bounds" );
    }

    @ParameterizedTest
    @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
    void testLevelsTooLarge(Context<?> context) {
        TestUtil.assertExprThrows(context.getConnectionWithDefaultRole(), "Sales",
            "[Time].[Time].Levels(8).Name", "Index '8' out of bounds" );
    }

}
