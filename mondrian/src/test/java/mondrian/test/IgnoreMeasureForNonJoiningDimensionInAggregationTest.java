/*
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// You must accept the terms of that agreement to use this software.
//
// Copyright (C) 2001-2005 Julian Hyde
// Copyright (C) 2005-2017 Hitachi Vantara and others
// All Rights Reserved.
*/

package mondrian.test;

import static org.opencube.junit5.TestUtil.assertQueryReturns;

import org.eclipse.daanse.olap.api.Connection;
import org.eclipse.daanse.olap.api.Context;
import org.eclipse.daanse.olap.common.SystemWideProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.opencube.junit5.ContextSource;
import org.opencube.junit5.context.TestContextImpl;
import org.opencube.junit5.dataloader.FastFoodmardDataLoader;
import org.opencube.junit5.propupdator.AppandFoodMartCatalog;

/**
 * Test ignoring of measure when unrelated Dimension is in
 * aggregation list when IgnoreMeasureForNonJoiningDimension property is
 * set to true.
 *
 * @author ajoglekar
 * @since Dec 12, 2007
 */
class IgnoreMeasureForNonJoiningDimensionInAggregationTest
{


    @BeforeEach
    public void beforeEach() {


        SystemWideProperties.instance().EnableNonEmptyOnAllAxis =
                true;
    }
    @AfterEach
    public void afterEach() {
        SystemWideProperties.instance().populateInitial();
    }


    @ParameterizedTest
    @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class )
    void testNoTotalsForCompdMeasureWithComponentsHavingNonJoiningDims(Context<?> context)
    {
        ((TestContextImpl)context).setIgnoreMeasureForNonJoiningDimension(true);
        Connection connection = context.getConnectionWithDefaultRole();
        connection.getCacheControl(null).flushSchemaCache();
        assertQueryReturns(connection,
            "with member [Measures].[Total Sales] as "
            + "'[Measures].[Store Sales] + [Measures].[Warehouse Sales]'"
            + "member [Product].x as 'sum({Product.members  * Gender.members})' "
            + "select {[Measures].[Total Sales]} on 0, "
            + "{Product.x} on 1 from [Warehouse and Sales]",
            "Axis #0:\n"
            + "{}\n"
            + "Axis #1:\n"
            + "{[Measures].[Total Sales]}\n"
            + "Axis #2:\n"
            + "{[Product].[Product].[x]}\n"
            + "Row #0: 7,913,333.82\n");
    }

    @ParameterizedTest
    @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class )
    void testNonJoiningDimsWhenAggFunctionIsUsedOrNotUsed(Context<?> context) {
        ((TestContextImpl)context).setIgnoreMeasureForNonJoiningDimension(true);
        Connection connection = context.getConnectionWithDefaultRole();
        connection.getCacheControl(null).flushSchemaCache();
        final String query = "WITH\n"
                             + "MEMBER [Measures].[Total Sales] AS "
                             + "'[Measures].[Store Sales] + [Measures].[Warehouse Sales]'\n"
                             + "MEMBER [Warehouse].[AggSP1] AS\n"
                             + "'IIF([Measures].CURRENTMEMBER IS [Measures].[Total Sales],\n"
                             + "([Warehouse].[All Warehouses], [Measures].[Total Sales]),\n"
                             + "([Product].[All Products], [Warehouse].[All Warehouses]))'\n"
                             + "MEMBER [Warehouse].[AggPreSP] AS\n"
                             + "'IIF([Measures].CURRENTMEMBER IS [Measures].[Total Sales],\n"
                             + "([Warehouse].[All Warehouses], [Measures].[Total Sales]),\n"
                             + "Aggregate({([Product].[All Products], [Warehouse].[All Warehouses])}))'\n"
                             + "\n"
                             + "SELECT\n"
                             + "{[Measures].[Total Sales]} ON AXIS(0),\n"
                             + "{{([Warehouse].[AggPreSP])},\n"
                             + "{([Warehouse].[AggSP1])}} ON AXIS(1)\n"
                             + "FROM\n"
                             + "[Warehouse and Sales]";

        assertQueryReturns(connection,
            query,
            "Axis #0:\n"
            + "{}\n"
            + "Axis #1:\n"
            + "{[Measures].[Total Sales]}\n"
            + "Axis #2:\n"
            + "{[Warehouse].[Warehouse].[AggPreSP]}\n"
            + "{[Warehouse].[Warehouse].[AggSP1]}\n"
            + "Row #0: 196,770.89\n"
            + "Row #1: 196,770.89\n");
        ((TestContextImpl)context).setIgnoreMeasureForNonJoiningDimension(false);
        assertQueryReturns(connection,
            query,
            "Axis #0:\n"
            + "{}\n"
            + "Axis #1:\n"
            + "{[Measures].[Total Sales]}\n"
            + "Axis #2:\n"
            + "{[Warehouse].[Warehouse].[AggPreSP]}\n"
            + "{[Warehouse].[Warehouse].[AggSP1]}\n"
            + "Row #0: 762,009.02\n"
            + "Row #1: 762,009.02\n");
    }

    @ParameterizedTest
    @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class )
    void testNonJoiningDimForAMemberDefinedOnJoiningDim(Context<?> context) {
        ((TestContextImpl)context).setIgnoreMeasureForNonJoiningDimension(true);
        Connection connection = context.getConnectionWithDefaultRole();
        connection.getCacheControl(null).flushSchemaCache();
        assertQueryReturns(connection,
            "WITH\n"
            + "MEMBER [Measures].[Total Sales] AS '[Measures].[Store Sales] + "
            + "[Measures].[Warehouse Sales]'\n"
            + "MEMBER [Product].[AggSP1] AS\n"
            + "'IIF([Measures].CURRENTMEMBER IS [Measures].[Total Sales],\n"
            + "([Warehouse].[All Warehouses], [Measures].[Total Sales]),\n"
            + "([Warehouse].[All Warehouses]))'\n"
            + "\n"
            + "SELECT\n"
            + "{[Measures].[Total Sales]} ON AXIS(0),\n"
            + "{[Product].[AggSP1]} ON AXIS(1)\n"
            + "FROM\n"
            + "[Warehouse and Sales]",
            "Axis #0:\n"
            + "{}\n"
            + "Axis #1:\n"
            + "{[Measures].[Total Sales]}\n"
            + "Axis #2:\n"
            + "{[Product].[Product].[AggSP1]}\n"
            + "Row #0: 196,770.89\n");
    }

    @ParameterizedTest
    @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class )
    void testNonJoiningDimWithNumericIif(Context<?> context) {
        ((TestContextImpl)context).setIgnoreMeasureForNonJoiningDimension(true);
        Connection connection = context.getConnectionWithDefaultRole();
        connection.getCacheControl(null).flushSchemaCache();
        assertQueryReturns(connection,
            "WITH\n"
            + "MEMBER [Measures].[Total Sales] AS "
            + "'[Measures].[Store Sales] + [Measures].[Warehouse Sales]'\n"
            + "MEMBER [Warehouse].[AggSP1_1] AS\n"
            + "'IIF(1=0,\n"
            + "([Warehouse].[All Warehouses], [Measures].[Total Sales]),\n"
            + "([Warehouse].[All Warehouses]))'\n"
            + "MEMBER [Warehouse].[AggSP1_2] AS\n"
            + "'IIF(1=0,\n"
            + "111,\n"
            + "([Warehouse].[All Warehouses], [Store].[All Stores]))'\n"
            + "\n"
            + "SELECT\n"
            + "{[Measures].[Total Sales]} ON AXIS(0),\n"
            + "{([Warehouse].[AggSP1_1]), ([Warehouse].[AggSP1_2])} ON AXIS(1)\n"
            + "FROM\n"
            + "[Warehouse and Sales]",
            "Axis #0:\n"
            + "{}\n"
            + "Axis #1:\n"
            + "{[Measures].[Total Sales]}\n"
            + "Axis #2:\n"
            + "{[Warehouse].[Warehouse].[AggSP1_1]}\n"
            + "{[Warehouse].[Warehouse].[AggSP1_2]}\n"
            + "Row #0: 196,770.89\n"
            + "Row #1: 196,770.89\n");
    }

    @ParameterizedTest
    @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class )
    void testNonJoiningDimAtMemberValueCalcMultipleScenarios(Context<?> context) {
        ((TestContextImpl)context).setIgnoreMeasureForNonJoiningDimension(true);
        Connection connection = context.getConnectionWithDefaultRole();
        connection.getCacheControl(null).flushSchemaCache();
        assertQueryReturns(connection,
            "WITH\n"
            + "MEMBER [Measures].[Total Sales] AS "
            + "'[Measures].[Store Sales] + [Measures].[Warehouse Sales]'\n"
            + "MEMBER [Warehouse].[AggSP1_1] AS\n"
            + "'IIF(1=0,\n"
            + "([Warehouse].[All Warehouses]),\n"
            + "([Warehouse].[All Warehouses]))'\n"
            + "MEMBER [Warehouse].[AggSP1_2] AS\n"
            + "'IIF(1=0,\n"
            + "[Warehouse].[All Warehouses],\n"
            + "([Warehouse].[All Warehouses]))'\n"
            + "MEMBER [Warehouse].[AggSP1_3] AS\n"
            + "'IIF(1=0,\n"
            + "([Warehouse].[All Warehouses]),\n"
            + "[Warehouse].[All Warehouses])'\n"
            + "MEMBER [Warehouse].[AggSP1_4] AS\n"
            + "'IIF(1=0,\n"
            + "StrToMember(\"[Warehouse].[All Warehouses]\"),\n"
            + "[Warehouse].[All Warehouses])'\n"
            + "\n"
            + "SELECT\n"
            + "{[Measures].[Total Sales]} ON AXIS(0),\n"
            + "{([Warehouse].[AggSP1_1]),([Warehouse].[AggSP1_2]),"
            + "([Warehouse].[AggSP1_3]),([Warehouse].[AggSP1_4])} ON AXIS(1)\n"
            + "FROM\n"
            + "[Warehouse and Sales]",
            "Axis #0:\n"
            + "{}\n"
            + "Axis #1:\n"
            + "{[Measures].[Total Sales]}\n"
            + "Axis #2:\n"
            + "{[Warehouse].[Warehouse].[AggSP1_1]}\n"
            + "{[Warehouse].[Warehouse].[AggSP1_2]}\n"
            + "{[Warehouse].[Warehouse].[AggSP1_3]}\n"
            + "{[Warehouse].[Warehouse].[AggSP1_4]}\n"
            + "Row #0: 196,770.89\n"
            + "Row #1: 196,770.89\n"
            + "Row #2: 196,770.89\n"
            + "Row #3: 196,770.89\n");
    }

    @ParameterizedTest
    @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class )
    void testNonJoiningDimAtTupleValueCalcMultipleScenarios(Context<?> context) {
        ((TestContextImpl)context).setIgnoreMeasureForNonJoiningDimension(true);
        Connection connection = context.getConnectionWithDefaultRole();
        connection.getCacheControl(null).flushSchemaCache();
        assertQueryReturns(connection,
            "WITH\n"
            + "MEMBER [Measures].[Total Sales] AS "
            + "'[Measures].[Store Sales] + [Measures].[Warehouse Sales]'\n"
            + "MEMBER [Warehouse].[AggSP1_1] AS\n"
            + "'IIF(1=0,\n"
            + "([Warehouse].[All Warehouses], [Store].[All Stores]),\n"
            + "([Warehouse].[All Warehouses], [Store].[All Stores]))'\n"
            + "MEMBER [Warehouse].[AggSP1_2] AS\n"
            + "'IIF(1=0,\n"
            + "([Warehouse].[All Warehouses]),\n"
            + "([Warehouse].[All Warehouses], [Store].[All Stores]))'\n"
            + "MEMBER [Warehouse].[AggSP1_3] AS\n"
            + "'IIF(1=0,\n"
            + "([Warehouse].[All Warehouses], [Store].[All Stores]),\n"
            + "([Warehouse].[All Warehouses]))'\n"
            + "MEMBER [Warehouse].[AggSP1_4] AS\n"
            + "'IIF(1=0,\n"
            + "StrToTuple(\"([Warehouse].[All Warehouses])\", [Warehouse]),\n"
            + "([Warehouse].[All Warehouses], [Store].[All Stores]))'\n"
            + "MEMBER [Warehouse].[AggSP1_5] AS\n"
            + "'IIF(1=0,\n"
            + "([Warehouse].[All Warehouses], [Store].[All Stores]),\n"
            + "[Warehouse].[All Warehouses])'\n"
            + "MEMBER [Warehouse].[AggSP1_6] AS\n"
            + "'IIF(1=0,\n"
            + "[Warehouse].[All Warehouses],\n"
            + "([Warehouse].[All Warehouses], [Store].[All Stores]))'\n"
            + "\n"
            + "SELECT\n"
            + "{[Measures].[Total Sales]} ON AXIS(0),\n"
            + "{[Warehouse].[AggSP1_1],[Warehouse].[AggSP1_2],[Warehouse].[AggSP1_3],"
            + "[Warehouse].[AggSP1_4],[Warehouse].[AggSP1_5],[Warehouse].[AggSP1_6]} "
            + "ON AXIS(1)\n"
            + "FROM\n"
            + "[Warehouse and Sales]",
            "Axis #0:\n"
            + "{}\n"
            + "Axis #1:\n"
            + "{[Measures].[Total Sales]}\n"
            + "Axis #2:\n"
            + "{[Warehouse].[Warehouse].[AggSP1_1]}\n"
            + "{[Warehouse].[Warehouse].[AggSP1_2]}\n"
            + "{[Warehouse].[Warehouse].[AggSP1_3]}\n"
            + "{[Warehouse].[Warehouse].[AggSP1_4]}\n"
            + "{[Warehouse].[Warehouse].[AggSP1_5]}\n"
            + "{[Warehouse].[Warehouse].[AggSP1_6]}\n"
            + "Row #0: 196,770.89\n"
            + "Row #1: 196,770.89\n"
            + "Row #2: 196,770.89\n"
            + "Row #3: 196,770.89\n"
            + "Row #4: 196,770.89\n"
            + "Row #5: 196,770.89\n");
    }

    @ParameterizedTest
    @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class )
    void testNoTotalsForCompoundMeasureWithNonJoiningDimAtAllLevel(Context<?> context) {
        ((TestContextImpl)context).setIgnoreMeasureForNonJoiningDimension(true);
        Connection connection = context.getConnectionWithDefaultRole();
        connection.getCacheControl(null).flushSchemaCache();
        assertQueryReturns(connection,
            "with member [Measures].[Total Sales] as "
            + "'[Measures].[Store Sales]'"
            + "member [Product].x as 'sum({Product.members  * "
            + "Gender.[All Gender]})' "
            + "select {[Measures].[Total Sales]} on 0, "
            + "{Product.x} on 1 from [Warehouse and Sales]",
            "Axis #0:\n"
            + "{}\n"
            + "Axis #1:\n"
            + "{[Measures].[Total Sales]}\n"
            + "Axis #2:\n"
            + "{[Product].[Product].[x]}\n"
            + "Row #0: 3,956,666.91\n");
    }

    @ParameterizedTest
    @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class )
    void testNoTotalForMeasureWithCrossJoinOfJoiningAndNonJoiningDims(Context<?> context) {
        ((TestContextImpl)context).setIgnoreMeasureForNonJoiningDimension(true);
        Connection connection = context.getConnectionWithDefaultRole();
        connection.getCacheControl(null).flushSchemaCache();
        assertQueryReturns(connection,
            "with member [Product].x as "
            + "'sum({Product.members}  * {Gender.members})' "
            + "select {[Measures].[Warehouse Sales]} on 0, "
            + "{Product.x} on 1 from [Warehouse and Sales]",
            "Axis #0:\n"
            + "{}\n"
            + "Axis #1:\n"
            + "Axis #2:\n");
    }

    @ParameterizedTest
    @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class )
    void testShouldTotalAMeasureWithAllJoiningDimensions(Context<?> context) {
        ((TestContextImpl)context).setIgnoreMeasureForNonJoiningDimension(true);
        Connection connection = context.getConnectionWithDefaultRole();
        connection.getCacheControl(null).flushSchemaCache();
        assertQueryReturns(connection,
            "with member [Product].x as "
            + "'sum({Product.members})' "
            + "select "
            + "{[Measures].[Warehouse Sales]} on 0, "
            + "{Product.x} on 1 "
            + "from [Warehouse and Sales]",
            "Axis #0:\n"
            + "{}\n"
            + "Axis #1:\n"
            + "{[Measures].[Warehouse Sales]}\n"
            + "Axis #2:\n"
            + "{[Product].[Product].[x]}\n"
            + "Row #0: 1,377,396.213\n");
    }

    @ParameterizedTest
    @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class )
    void testShouldNotTotalAMeasureWithANonJoiningDimension(Context<?> context) {
        ((TestContextImpl)context).setIgnoreMeasureForNonJoiningDimension(true);
        Connection connection = context.getConnectionWithDefaultRole();
        connection.getCacheControl(null).flushSchemaCache();
        assertQueryReturns(connection,
            "with member [Gender].x as 'sum({Gender.members})'"
            + "select "
            + "{[Measures].[Warehouse Sales]} on 0, "
            + "{Gender.x} on 1 "
            + "from [Warehouse and Sales]",
            "Axis #0:\n"
            + "{}\n"
            + "Axis #1:\n"
            + "Axis #2:\n");
    }

    // base cube is null for calc measure
    @ParameterizedTest
    @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class )
    void testGetMeasureCubeForCalcMeasureDoesNotThrowCastException(Context<?> context) {
        ((TestContextImpl)context).setIgnoreMeasureForNonJoiningDimension(true);
        Connection connection = context.getConnectionWithDefaultRole();
        connection.getCacheControl(null).flushSchemaCache();
        assertQueryReturns(connection,
            "WITH MEMBER [Measures].[My Profit] AS "
            + "'Measures.[Profit]', SOLVE_ORDER = 3000 "
            + "MEMBER Gender.G AS "
            + "'sum(CROSSJOIN({GENDER.[M]},{[Product].[All Products].[Drink]}))',"
            + "SOLVE_ORDER = 4 "
            + "SELECT {[Measures].[My Profit]} ON 0, {Gender.G} ON 1 FROM [SALES]",
            "Axis #0:\n"
            + "{}\n"
            + "Axis #1:\n"
            + "{[Measures].[My Profit]}\n"
            + "Axis #2:\n"
            + "{[Gender].[Gender].[G]}\n"
            + "Row #0: $14,652.70\n");
    }

}
