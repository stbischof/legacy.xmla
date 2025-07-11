/*
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// You must accept the terms of that agreement to use this software.
//
// Copyright (C) 2006-2020 Hitachi Vantara and others
// All Rights Reserved.
 */
package mondrian.rolap;

import static org.opencube.junit5.TestUtil.assertQueryReturns;
import static org.opencube.junit5.TestUtil.getDialect;
import static org.opencube.junit5.TestUtil.hierarchyName;
import static org.opencube.junit5.TestUtil.isDefaultNullMemberRepresentation;
import static org.opencube.junit5.TestUtil.withSchema;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.daanse.olap.api.ConfigConstants;
import org.eclipse.daanse.olap.api.Connection;
import org.eclipse.daanse.olap.api.Context;
import org.eclipse.daanse.olap.common.SystemWideProperties;
import org.eclipse.daanse.rolap.mapping.api.model.CatalogMapping;
import org.eclipse.daanse.rolap.mapping.api.model.CubeMapping;
import org.eclipse.daanse.rolap.mapping.instance.rec.complex.foodmart.FoodmartMappingSupplier;
import org.eclipse.daanse.rolap.mapping.modifier.pojo.PojoMappingModifier;
import org.eclipse.daanse.rolap.mapping.pojo.DimensionConnectorMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.DimensionMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.ExplicitHierarchyMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.HierarchyMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.LevelMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.MeasureGroupMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.MeasureMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.PhysicalCubeMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.StandardDimensionMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.SumMeasureMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.TableQueryMappingImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.opencube.junit5.ContextSource;
import org.opencube.junit5.TestUtil;
import org.opencube.junit5.context.TestContextImpl;
import org.opencube.junit5.dataloader.FastFoodmardDataLoader;
import org.opencube.junit5.propupdator.AppandFoodMartCatalog;

import mondrian.enums.DatabaseProduct;
import mondrian.test.SqlPattern;

/**
 * Tests for Filter and native Filters.
 *
 * @author Rushan Chen
 * @since April 28, 2009
 */
class FilterTest extends BatchTestCase {


  @BeforeEach
  public void beforeEach() {

    //propSaver.set(
    //        MondrianProperties.instance().EnableNativeCrossJoin, true );
  }

  @AfterEach
  public void afterEach() {
    SystemWideProperties.instance().populateInitial();
  }


  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  void testInFilterSimple(Context<?> context) throws Exception {
      ((TestContextImpl)context).setExpandNonNative(false);
      ((TestContextImpl)context).setEnableNativeFilter(true);

    // Get a fresh connection; Otherwise the mondrian property setting
    // is not refreshed for this parameter.
    boolean requestFreshConnection = true;

    String query =
      "With "
        + "Set [*NATIVE_CJ_SET] as 'NonEmptyCrossJoin([*BASE_MEMBERS_Customers],[*BASE_MEMBERS_Product])' "
        + "Set [*BASE_MEMBERS_Customers] as 'Filter([Customers].[City].Members,Ancestor([Customers].CurrentMember, "
        + "[Customers].[State Province]) In {[Customers].[All Customers].[USA].[CA]})' "
        + "Set [*BASE_MEMBERS_Product] as 'Filter([Product].[Product Family].Members,[Product].CurrentMember In "
        + "{[Product].[All Products].[Drink]})' "
        + "Set [*CJ_ROW_AXIS] as 'Generate([*NATIVE_CJ_SET], {([Customers].currentMember,[Product].currentMember)})' "
        + "Select "
        + "{[Measures].[Customer Count]} on columns, "
        + "Non Empty [*CJ_ROW_AXIS] on rows "
        + "From [Sales]";

    checkNative(context, 0, 45, query, null, requestFreshConnection );
  }

  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  void testNotInFilterSimple(Context<?> context) throws Exception {
    ((TestContextImpl)context).setExpandNonNative(false);
    ((TestContextImpl)context).setEnableNativeFilter(true);

    // Get a fresh connection; Otherwise the mondrian property setting
    // is not refreshed for this parameter.
    boolean requestFreshConnection = true;

    String query =
      "With "
        + "Set [*NATIVE_CJ_SET] as 'NonEmptyCrossJoin([*BASE_MEMBERS_Customers],[*BASE_MEMBERS_Product])' "
        + "Set [*BASE_MEMBERS_Customers] as 'Filter([Customers].[City].Members,Ancestor([Customers].CurrentMember, "
        + "[Customers].[State Province]) Not In {[Customers].[All Customers].[USA].[CA]})' "
        + "Set [*BASE_MEMBERS_Product] as 'Filter([Product].[Product Family].Members,[Product].CurrentMember Not In "
        + "{[Product].[All Products].[Drink]})' "
        + "Set [*CJ_ROW_AXIS] as 'Generate([*NATIVE_CJ_SET], {([Customers].currentMember,[Product].currentMember)})' "
        + "Select "
        + "{[Measures].[Customer Count]} on columns, "
        + "Non Empty [*CJ_ROW_AXIS] on rows "
        + "From [Sales]";

    checkNative(context, 0, 66, query, null, requestFreshConnection );
  }

  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  void testInFilterAND(Context<?> context) throws Exception {
    ((TestContextImpl)context).setExpandNonNative(false);
    ((TestContextImpl)context).setEnableNativeFilter(true);

    // Get a fresh connection; Otherwise the mondrian property setting
    // is not refreshed for this parameter.
    boolean requestFreshConnection = true;

    String query =
      "With "
        + "Set [*NATIVE_CJ_SET] as 'NonEmptyCrossJoin([*BASE_MEMBERS_Customers],[*BASE_MEMBERS_Product])' "
        + "Set [*BASE_MEMBERS_Customers] as 'Filter([Customers].[City].Members,"
        + "((Ancestor([Customers].CurrentMember, [Customers].[State Province]) In {[Customers].[All Customers].[USA]"
        + ".[CA]}) "
        + "AND ([Customers].CurrentMember Not In {[Customers].[All Customers].[USA].[CA].[Altadena]})))' "
        + "Set [*BASE_MEMBERS_Product] as 'Filter([Product].[Product Family].Members,[Product].CurrentMember Not In "
        + "{[Product].[All Products].[Drink]})' "
        + "Set [*CJ_ROW_AXIS] as 'Generate([*NATIVE_CJ_SET], {([Customers].currentMember,[Product].currentMember)})' "
        + "Select "
        + "{[Measures].[Customer Count]} on columns, "
        + "Non Empty [*CJ_ROW_AXIS] on rows "
        + "From [Sales]";

    checkNative(context, 0, 88, query, null, requestFreshConnection );
  }

  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  void testIsFilterSimple(Context<?> context) throws Exception {
    ((TestContextImpl)context).setExpandNonNative(false);
    ((TestContextImpl)context).setEnableNativeFilter(true);

    // Get a fresh connection; Otherwise the mondrian property setting
    // is not refreshed for this parameter.
    boolean requestFreshConnection = true;

    String query =
      "With "
        + "Set [*NATIVE_CJ_SET] as 'NonEmptyCrossJoin([*BASE_MEMBERS_Customers],[*BASE_MEMBERS_Product])' "
        + "Set [*BASE_MEMBERS_Customers] as 'Filter([Customers].[City].Members,Ancestor([Customers].CurrentMember, "
        + "[Customers].[State Province]) Is [Customers].[All Customers].[USA].[CA])' "
        + "Set [*BASE_MEMBERS_Product] as 'Filter([Product].[Product Family].Members,[Product].CurrentMember Is "
        + "[Product].[All Products].[Drink])' "
        + "Set [*CJ_ROW_AXIS] as 'Generate([*NATIVE_CJ_SET], {([Customers].currentMember,[Product].currentMember)})' "
        + "Select "
        + "{[Measures].[Customer Count]} on columns, "
        + "Non Empty [*CJ_ROW_AXIS] on rows "
        + "From [Sales]";

    checkNative(context, 120, 45, query, null, requestFreshConnection );
  }

  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  void testNotIsFilterSimple(Context<?> context) throws Exception {
      ((TestContextImpl)context).setExpandNonNative(false);
    ((TestContextImpl)context).setEnableNativeFilter(true);

    // Get a fresh connection; Otherwise the mondrian property setting
    // is not refreshed for this parameter.
    boolean requestFreshConnection = true;

    String query =
      "With "
        + "Set [*NATIVE_CJ_SET] as 'NonEmptyCrossJoin([*BASE_MEMBERS_Customers],[*BASE_MEMBERS_Product])' "
        + "Set [*BASE_MEMBERS_Customers] as 'Filter([Customers].[City].Members, not (Ancestor([Customers]"
        + ".CurrentMember, [Customers].[State Province]) Is [Customers].[All Customers].[USA].[CA]))' "
        + "Set [*BASE_MEMBERS_Product] as 'Filter([Product].[Product Family].Members,not ([Product].CurrentMember Is "
        + "[Product].[All Products].[Drink]))' "
        + "Set [*CJ_ROW_AXIS] as 'Generate([*NATIVE_CJ_SET], {([Customers].currentMember,[Product].currentMember)})' "
        + "Select "
        + "{[Measures].[Customer Count]} on columns, "
        + "Non Empty [*CJ_ROW_AXIS] on rows "
        + "From [Sales]";

    checkNative(context, 150, 66, query, null, requestFreshConnection );
  }

  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  public void  testMixedInIsFilters(Context<?> context) throws Exception {
    ((TestContextImpl)context).setExpandNonNative(false);
    ((TestContextImpl)context).setEnableNativeFilter(true);

    // Get a fresh connection; Otherwise the mondrian property setting
    // is not refreshed for this parameter.
    boolean requestFreshConnection = true;

    String query =
      "With "
        + "Set [*NATIVE_CJ_SET] as 'NonEmptyCrossJoin([*BASE_MEMBERS_Customers],[*BASE_MEMBERS_Product])' "
        + "Set [*BASE_MEMBERS_Customers] as 'Filter([Customers].[City].Members,"
        + "((Ancestor([Customers].CurrentMember, [Customers].[State Province]) Is [Customers].[All Customers].[USA]"
        + ".[CA]) "
        + "AND ([Customers].CurrentMember Not In {[Customers].[All Customers].[USA].[CA].[Altadena]})))' "
        + "Set [*BASE_MEMBERS_Product] as 'Filter([Product].[Product Family].Members, not ([Product].CurrentMember Is"
        + " [Product].[All Products].[Drink]))' "
        + "Set [*CJ_ROW_AXIS] as 'Generate([*NATIVE_CJ_SET], {([Customers].currentMember,[Product].currentMember)})' "
        + "Select "
        + "{[Measures].[Customer Count]} on columns, "
        + "Non Empty [*CJ_ROW_AXIS] on rows "
        + "From [Sales]";

    checkNative(context, 0, 88, query, null, requestFreshConnection );
  }

  /**
   * Here the filter is above (rather than as inputs to) the NECJ.  These types of filters are currently not natively
   * evaluated.
   *
   * <p>To expand on this case, RolapNativeFilter needs to be improved so it
   * knows how to represent the dimension filter constraint.  Currently the FilterConstraint is only used for filters on
   * measures.
   *
   * @throws Exception
   */
  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  public void  testInFilterNonNative(Context<?> context) throws Exception {
    ((TestContextImpl)context).setExpandNonNative(false);
    ((TestContextImpl)context).setEnableNativeFilter(true);

    String query =
      "With "
        + "Set [*BASE_CJ_SET] as 'CrossJoin([Customers].[City].Members,[Product].[Product Family].Members)' "
        + "Set [*NATIVE_CJ_SET] as 'Filter([*BASE_CJ_SET], "
        + "(Ancestor([Customers].CurrentMember,[Customers].[State Province]) In {[Customers].[All Customers].[USA]"
        + ".[CA]}) AND ([Product].CurrentMember In {[Product].[All Products].[Drink]}))' "
        + "Set [*CJ_ROW_AXIS] as 'Generate([*NATIVE_CJ_SET], {([Customers].currentMember,[Product].currentMember)})' "
        + "Select "
        + "{[Measures].[Customer Count]} on columns, "
        + "Non Empty [*CJ_ROW_AXIS] on rows "
        + "From [Sales]";

    checkNotNative(context, 45, query );
  }

  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  public void  testTopCountOverInFilter(Context<?> context) throws Exception {
    ((TestContextImpl)context).setExpandNonNative(false);
    ((TestContextImpl)context).setEnableNativeFilter(true);
    ((TestContextImpl)context).setEnableNativeTopCount(true);

    // Get a fresh connection; Otherwise the mondrian property setting
    // is not refreshed for this parameter.
    boolean requestFreshConnection = true;

    String query =
      "With "
        + "Set [*NATIVE_TOP_SET] as 'TopCount([*BASE_MEMBERS_Customers], 3, [Measures].[Customer Count])' "
        + "Set [*BASE_MEMBERS_Customers] as 'Filter([Customers].[City].Members,Ancestor([Customers].CurrentMember, "
        + "[Customers].[State Province]) In {[Customers].[All Customers].[USA].[CA]})' "
        + "Set [*CJ_ROW_AXIS] as 'Generate([*NATIVE_TOP_SET], {([Customers].currentMember,[Product].currentMember)})' "
        + "Select "
        + "{[Measures].[Customer Count]} on columns, "
        + "Non Empty [*CJ_ROW_AXIS] on rows "
        + "From [Sales]";

    checkNative(context, 100, 3, query, null, requestFreshConnection );
  }

  /**
   * Test that if Null member is not explicitly excluded, then the native filter SQL should not filter out null
   * members.
   *
   * @throws Exception
   */
  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  public void  testNotInFilterKeepNullMember(Context<?> context) throws Exception {
    ((TestContextImpl)context).setExpandNonNative(false);
    ((TestContextImpl)context).setEnableNativeFilter(true);

    // Get a fresh connection; Otherwise the mondrian property setting
    // is not refreshed for this parameter.
    boolean requestFreshConnection = true;

    String query =
      "With "
        + "Set [*NATIVE_CJ_SET] as 'NonEmptyCrossJoin([*BASE_MEMBERS_Customers],[*BASE_MEMBERS_SQFT])' "
        + "Set [*BASE_MEMBERS_Customers] as 'Filter([Customers].[Country].Members, [Customers].CurrentMember In "
        + "{[Customers].[All Customers].[USA]})' "
        + "Set [*BASE_MEMBERS_SQFT] as 'Filter([Store Size in SQFT].[Store Sqft].Members, [Store Size in SQFT]"
        + ".currentMember not in {[Store Size in SQFT].[All Store Size in SQFTs].[39696]})' "
        + "Set [*CJ_ROW_AXIS] as 'Generate([*NATIVE_CJ_SET], {([Customers].currentMember,[Store Size in SQFT]"
        + ".currentMember)})' "
        + "Set [*ORDERED_CJ_ROW_AXIS] as 'Order([*CJ_ROW_AXIS], [Store Size in SQFT].currentmember.OrderKey, BASC)' "
        + "Select "
        + "{[Measures].[Customer Count]} on columns, "
        + "Non Empty [*ORDERED_CJ_ROW_AXIS] on rows "
        + "From [Sales]";

    String result =
      "Axis #0:\n"
        + "{}\n"
        + "Axis #1:\n"
        + "{[Measures].[Customer Count]}\n"
        + "Axis #2:\n"
        + "{[Customers].[Customers].[USA], [Store Size in SQFT].[Store Size in SQFT].[#null]}\n"
        + "{[Customers].[Customers].[USA], [Store Size in SQFT].[Store Size in SQFT].[20319]}\n"
        + "{[Customers].[Customers].[USA], [Store Size in SQFT].[Store Size in SQFT].[21215]}\n"
        + "{[Customers].[Customers].[USA], [Store Size in SQFT].[Store Size in SQFT].[22478]}\n"
        + "{[Customers].[Customers].[USA], [Store Size in SQFT].[Store Size in SQFT].[23598]}\n"
        + "{[Customers].[Customers].[USA], [Store Size in SQFT].[Store Size in SQFT].[23688]}\n"
        + "{[Customers].[Customers].[USA], [Store Size in SQFT].[Store Size in SQFT].[27694]}\n"
        + "{[Customers].[Customers].[USA], [Store Size in SQFT].[Store Size in SQFT].[28206]}\n"
        + "{[Customers].[Customers].[USA], [Store Size in SQFT].[Store Size in SQFT].[30268]}\n"
        + "{[Customers].[Customers].[USA], [Store Size in SQFT].[Store Size in SQFT].[33858]}\n"
        + "Row #0: 1,153\n"
        + "Row #1: 563\n"
        + "Row #2: 906\n"
        + "Row #3: 296\n"
        + "Row #4: 1,147\n"
        + "Row #5: 1,059\n"
        + "Row #6: 474\n"
        + "Row #7: 190\n"
        + "Row #8: 84\n"
        + "Row #9: 278\n";

    checkNative(context, 0, 10, query, result, requestFreshConnection );
  }

  /**
   * Test that if Null member is explicitly excluded, then the native filter SQL should filter out null members.
   *
   * @throws Exception
   */
  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  public void  testNotInFilterExcludeNullMember(Context<?> context) throws Exception {
    ((TestContextImpl)context).setExpandNonNative(false);
    ((TestContextImpl)context).setEnableNativeFilter(true);

    // Get a fresh connection; Otherwise the mondrian property setting
    // is not refreshed for this parameter.
    boolean requestFreshConnection = true;

    String query =
      "With "
        + "Set [*NATIVE_CJ_SET] as 'NonEmptyCrossJoin([*BASE_MEMBERS_Customers],[*BASE_MEMBERS_SQFT])' "
        + "Set [*BASE_MEMBERS_Customers] as 'Filter([Customers].[Country].Members, [Customers].CurrentMember In "
        + "{[Customers].[All Customers].[USA]})' "
        + "Set [*BASE_MEMBERS_SQFT] as 'Filter([Store Size in SQFT].[Store Sqft].Members, "
        + "[Store Size in SQFT].currentMember not in {[Store Size in SQFT].[All Store Size in SQFTs].[#null], [Store "
        + "Size in SQFT].[All Store Size in SQFTs].[39696]})' "
        + "Set [*CJ_ROW_AXIS] as 'Generate([*NATIVE_CJ_SET], {([Customers].currentMember,[Store Size in SQFT]"
        + ".currentMember)})' "
        + "Set [*ORDERED_CJ_ROW_AXIS] as 'Order([*CJ_ROW_AXIS], [Store Size in SQFT].currentmember.OrderKey, BASC)' "
        + "Select "
        + "{[Measures].[Customer Count]} on columns, "
        + "Non Empty [*ORDERED_CJ_ROW_AXIS] on rows "
        + "From [Sales]";

    String result =
      "Axis #0:\n"
        + "{}\n"
        + "Axis #1:\n"
        + "{[Measures].[Customer Count]}\n"
        + "Axis #2:\n"
        + "{[Customers].[Customers].[USA], [Store Size in SQFT].[Store Size in SQFT].[20319]}\n"
        + "{[Customers].[Customers].[USA], [Store Size in SQFT].[Store Size in SQFT].[21215]}\n"
        + "{[Customers].[Customers].[USA], [Store Size in SQFT].[Store Size in SQFT].[22478]}\n"
        + "{[Customers].[Customers].[USA], [Store Size in SQFT].[Store Size in SQFT].[23598]}\n"
        + "{[Customers].[Customers].[USA], [Store Size in SQFT].[Store Size in SQFT].[23688]}\n"
        + "{[Customers].[Customers].[USA], [Store Size in SQFT].[Store Size in SQFT].[27694]}\n"
        + "{[Customers].[Customers].[USA], [Store Size in SQFT].[Store Size in SQFT].[28206]}\n"
        + "{[Customers].[Customers].[USA], [Store Size in SQFT].[Store Size in SQFT].[30268]}\n"
        + "{[Customers].[Customers].[USA], [Store Size in SQFT].[Store Size in SQFT].[33858]}\n"
        + "Row #0: 563\n"
        + "Row #1: 906\n"
        + "Row #2: 296\n"
        + "Row #3: 1,147\n"
        + "Row #4: 1,059\n"
        + "Row #5: 474\n"
        + "Row #6: 190\n"
        + "Row #7: 84\n"
        + "Row #8: 278\n";

    checkNative(context, 0, 9, query, result, requestFreshConnection );
  }

  /**
   * Test that null members are included when the filter excludes members that contain multiple levels, but none being
   * null.
   */
  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  public void  testNotInMultiLevelMemberConstraintNonNullParent(Context<?> context) {
    if ( context.getConfigValue(ConfigConstants.READ_AGGREGATES, ConfigConstants.READ_AGGREGATES_DEFAULT_VALUE ,Boolean.class) ) {
      // If aggregate tables are enabled, generates similar SQL involving
      // agg tables.
      return;
    }
    String query =
      "With "
        + "Set [*NATIVE_CJ_SET] as 'NonEmptyCrossJoin([*BASE_MEMBERS_Customers],[*BASE_MEMBERS_Quarters])' "
        + "Set [*BASE_MEMBERS_Customers] as 'Filter([Customers].[Customers].[Country].Members, [Customers].[Customers].CurrentMember In "
        + "{[Customers].[Customers].[All Customers].[USA]})' "
        + "Set [*BASE_MEMBERS_Quarters] as 'Filter([Time].[Time].[Quarter].Members, "
        + "[Time].[Time].currentMember not in {[Time].[Time].[1997].[Q1], [Time].[Time].[1998].[Q3]})' "
        + "Set [*CJ_ROW_AXIS] as 'Generate([*NATIVE_CJ_SET], {([Customers].[Customers].currentMember,[Time].[Time].currentMember)})' "
        + "Set [*ORDERED_CJ_ROW_AXIS] as 'Order([*CJ_ROW_AXIS], [Time].[Time].currentmember.OrderKey, BASC)' "
        + "Select "
        + "{[Measures].[Customer Count]} on columns, "
        + "Non Empty [*ORDERED_CJ_ROW_AXIS] on rows "
        + "From [Sales]";

    String necjSqlDerby =
      "select \"customer\".\"country\", \"time_by_day\".\"the_year\", "
        + "\"time_by_day\".\"quarter\" from \"customer\" as \"customer\", "
        + "\"sales_fact_1997\" as \"sales_fact_1997\", \"time_by_day\" as "
        + "\"time_by_day\" where \"sales_fact_1997\".\"customer_id\" = "
        + "\"customer\".\"customer_id\" and \"sales_fact_1997\".\"time_id\" = "
        + "\"time_by_day\".\"time_id\" and (\"customer\".\"country\" = 'USA') and "
        + "(not ((\"time_by_day\".\"the_year\" = 1997 and \"time_by_day\".\"quarter\" "
        + "= 'Q1') or (\"time_by_day\".\"the_year\" = 1998 and "
        + "\"time_by_day\".\"quarter\" = 'Q3')) or ((\"time_by_day\".\"quarter\" is "
        + "null or \"time_by_day\".\"the_year\" is null) and "
        + "not((\"time_by_day\".\"the_year\" = 1997 and \"time_by_day\".\"quarter\" "
        + "= 'Q1') or (\"time_by_day\".\"the_year\" = 1998 and "
        + "\"time_by_day\".\"quarter\" = 'Q3')))) group by \"customer\".\"country\", "
        + "\"time_by_day\".\"the_year\", \"time_by_day\".\"quarter\" "
        + "order by CASE WHEN \"customer\".\"country\" IS NULL THEN 1 ELSE 0 END, \"customer\".\"country\" ASC, CASE "
        + "WHEN \"time_by_day\".\"the_year\" IS NULL THEN 1 ELSE 0 END, \"time_by_day\".\"the_year\" ASC, CASE WHEN "
        + "\"time_by_day\".\"quarter\" IS NULL THEN 1 ELSE 0 END, \"time_by_day\".\"quarter\" ASC";

    String necjSqlMySql =
      "select `customer`.`country` as `c0`, `time_by_day`.`the_year` as `c1`, "
        + "`time_by_day`.`quarter` as `c2` from `customer` as `customer`, "
        + "`sales_fact_1997` as `sales_fact_1997`, `time_by_day` as `time_by_day` "
        + "where `sales_fact_1997`.`customer_id` = `customer`.`customer_id` "
        + "and `sales_fact_1997`.`time_id` = `time_by_day`.`time_id` and "
        + "(`customer`.`country` = 'USA') and "
        + "(not ((`time_by_day`.`quarter`, `time_by_day`.`the_year`) in "
        + "(('Q1', 1997), ('Q3', 1998))) or (`time_by_day`.`quarter` is null or "
        + "`time_by_day`.`the_year` is null)) "
        + "group by `customer`.`country`, `time_by_day`.`the_year`, `time_by_day`.`quarter` "
        + ( getDialect(context.getConnectionWithDefaultRole()).requiresOrderByAlias()
        ? "order by ISNULL(`c0`) ASC, "
        + "`c0` ASC, ISNULL(`c1`) ASC, "
        + "`c1` ASC, ISNULL(`c2`) ASC, "
        + "`c2` ASC"
        : "order by ISNULL(`customer`.`country`) ASC, "
        + "`customer`.`country` ASC, ISNULL(`time_by_day`.`the_year`) ASC, "
        + "`time_by_day`.`the_year` ASC, ISNULL(`time_by_day`.`quarter`) ASC, "
        + "`time_by_day`.`quarter` ASC" );

    SqlPattern[] patterns = {
      new SqlPattern(
        DatabaseProduct.DERBY, necjSqlDerby, necjSqlDerby ),
      new SqlPattern(
        DatabaseProduct.MYSQL, necjSqlMySql, necjSqlMySql )
    };

    assertQuerySql(context.getConnectionWithDefaultRole(), query, patterns );
  }

  /**
   * Test that null members are included when the filter excludes members that contain multiple levels, but none being
   * null.  The members have the same parent.
   */
  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  public void  testNotInMultiLevelMemberConstraintNonNullSameParent(Context<?> context) {
    if ( context.getConfigValue(ConfigConstants.READ_AGGREGATES, ConfigConstants.READ_AGGREGATES_DEFAULT_VALUE ,Boolean.class) ) {
      // If aggregate tables are enabled, generates similar SQL involving
      // agg tables.
      return;
    }
    String query =
      "With "
        + "Set [*NATIVE_CJ_SET] as 'NonEmptyCrossJoin([*BASE_MEMBERS_Customers],[*BASE_MEMBERS_Quarters])' "
        + "Set [*BASE_MEMBERS_Customers] as 'Filter([Customers].[Customers].[Country].Members, [Customers].[Customers].CurrentMember In "
        + "{[Customers].[Customers].[All Customers].[USA]})' "
        + "Set [*BASE_MEMBERS_Quarters] as 'Filter([Time].[Time].[Quarter].Members, "
        + "[Time].[Time].currentMember not in {[Time].[Time].[1997].[Q1], [Time].[Time].[1997].[Q3]})' "
        + "Set [*CJ_ROW_AXIS] as 'Generate([*NATIVE_CJ_SET], {([Customers].[Customers].currentMember,[Time].[Time].currentMember)})' "
        + "Set [*ORDERED_CJ_ROW_AXIS] as 'Order([*CJ_ROW_AXIS], [Time].[Time].currentmember.OrderKey, BASC)' "
        + "Select "
        + "{[Measures].[Customer Count]} on columns, "
        + "Non Empty [*ORDERED_CJ_ROW_AXIS] on rows "
        + "From [Sales]";

    String necjSqlDerby =
      "select \"customer\".\"country\", \"time_by_day\".\"the_year\", "
        + "\"time_by_day\".\"quarter\" from \"customer\" as \"customer\", "
        + "\"sales_fact_1997\" as \"sales_fact_1997\", \"time_by_day\" as "
        + "\"time_by_day\" where \"sales_fact_1997\".\"customer_id\" = "
        + "\"customer\".\"customer_id\" and \"sales_fact_1997\".\"time_id\" = "
        + "\"time_by_day\".\"time_id\" and (\"customer\".\"country\" = 'USA') and "
        + "((not (\"time_by_day\".\"quarter\" in ('Q1', 'Q3')) or "
        + "(\"time_by_day\".\"quarter\" is null)) or (not "
        + "(\"time_by_day\".\"the_year\" = 1997) or (\"time_by_day\".\"the_year\" is "
        + "null))) group by \"customer\".\"country\", \"time_by_day\".\"the_year\", "
        + "\"time_by_day\".\"quarter\" "
        + "order by CASE WHEN \"customer\".\"country\" IS NULL THEN 1 ELSE 0 END, \"customer\".\"country\" ASC, CASE "
        + "WHEN \"time_by_day\".\"the_year\" IS NULL THEN 1 ELSE 0 END, \"time_by_day\".\"the_year\" ASC, CASE WHEN "
        + "\"time_by_day\".\"quarter\" IS NULL THEN 1 ELSE 0 END, \"time_by_day\".\"quarter\" ASC";

    String necjSqlMySql =
      "select `customer`.`country` as `c0`, `time_by_day`.`the_year` as "
        + "`c1`, `time_by_day`.`quarter` as `c2` from `customer` as "
        + "`customer`, `sales_fact_1997` as `sales_fact_1997`, `time_by_day` "
        + "as `time_by_day` where `sales_fact_1997`.`customer_id` = "
        + "`customer`.`customer_id` and `sales_fact_1997`.`time_id` = "
        + "`time_by_day`.`time_id` and (`customer`.`country` = 'USA') and "
        + "((not (`time_by_day`.`quarter` in ('Q1', 'Q3')) or "
        + "(`time_by_day`.`quarter` is null)) or (not "
        + "(`time_by_day`.`the_year` = 1997) or (`time_by_day`.`the_year` "
        + "is null))) group by `customer`.`country`, `time_by_day`.`the_year`, `time_by_day`.`quarter` "
        + (getDialect(context.getConnectionWithDefaultRole()).requiresOrderByAlias()
        ? "order by ISNULL(`c0`) ASC, "
        + "`c0` ASC, ISNULL(`c1`) ASC, "
        + "`c1` ASC, ISNULL(`c2`) ASC, "
        + "`c2` ASC"
        : "order by ISNULL(`customer`.`country`) ASC, "
        + "`customer`.`country` ASC, ISNULL(`time_by_day`.`the_year`) ASC, "
        + "`time_by_day`.`the_year` ASC, ISNULL(`time_by_day`.`quarter`) ASC, "
        + "`time_by_day`.`quarter` ASC" );

    SqlPattern[] patterns = {
      new SqlPattern(
        DatabaseProduct.DERBY, necjSqlDerby, necjSqlDerby ),
      new SqlPattern(
        DatabaseProduct.MYSQL, necjSqlMySql, necjSqlMySql )
    };

    assertQuerySql(context.getConnectionWithDefaultRole(), query, patterns );
  }

  /**
   * Test that null members are included when the filter explicitly excludes certain members that contain nulls.  The
   * members span multiple levels.
   */
  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  public void  testNotInMultiLevelMemberConstraintMixedNullNonNullParent(Context<?> context) {
    if ( !isDefaultNullMemberRepresentation() ) {
      return;
    }
    if ( SystemWideProperties.instance().FilterChildlessSnowflakeMembers ) {
      return;
    }

    String dimension =
      "<Dimension name=\"Warehouse2\">\n"
        + "  <Hierarchy hasAll=\"true\" primaryKey=\"warehouse_id\">\n"
        + "    <Table name=\"warehouse\"/>\n"
        + "    <Level name=\"fax\" column=\"warehouse_fax\" uniqueMembers=\"true\"/>\n"
        + "    <Level name=\"address1\" column=\"wa_address1\" uniqueMembers=\"false\"/>\n"
        + "    <Level name=\"name\" column=\"warehouse_name\" uniqueMembers=\"false\"/>\n"
        + "  </Hierarchy>\n"
        + "</Dimension>\n";

    String cube =
      "<Cube name=\"Warehouse2\">\n"
        + "  <Table name=\"inventory_fact_1997\"/>\n"
        + "  <DimensionUsage name=\"Product\" source=\"Product\" foreignKey=\"product_id\"/>\n"
        + "  <DimensionUsage name=\"Warehouse2\" source=\"Warehouse2\" foreignKey=\"warehouse_id\"/>\n"
        + "  <Measure name=\"Warehouse Cost\" column=\"warehouse_cost\" aggregator=\"sum\"/>\n"
        + "  <Measure name=\"Warehouse Sales\" column=\"warehouse_sales\" aggregator=\"sum\"/>\n"
        + "</Cube>";

    String query =
      "with\n"
        + "set [Filtered Warehouse Set] as 'Filter([Warehouse2].[name].Members, [Warehouse2].CurrentMember Not In"
        + "{[Warehouse2].[#null].[234 West Covina Pkwy].[Freeman And Co],"
        + " [Warehouse2].[971-555-6213].[3377 Coachman Place].[Jones International]})' "
        + "set [NECJ] as NonEmptyCrossJoin([Filtered Warehouse Set], {[Product].[Product Family].Food}) "
        + "select [NECJ] on 0 from [Warehouse2]";

    String necjSqlDerby =
      "select \"warehouse\".\"warehouse_fax\", \"warehouse\".\"wa_address1\", "
        + "\"warehouse\".\"warehouse_name\", \"product_class\".\"product_family\" "
        + "from \"warehouse\" as \"warehouse\", \"inventory_fact_1997\" as "
        + "\"inventory_fact_1997\", \"product\" as \"product\", \"product_class\" as "
        + "\"product_class\" where \"inventory_fact_1997\".\"warehouse_id\" = "
        + "\"warehouse\".\"warehouse_id\" and \"product\".\"product_class_id\" = "
        + "\"product_class\".\"product_class_id\" and "
        + "\"inventory_fact_1997\".\"product_id\" = \"product\".\"product_id\" and "
        + "(\"product_class\".\"product_family\" = 'Food') and "
        + "(not ((\"warehouse\".\"wa_address1\" = '234 West Covina Pkwy' and "
        + "\"warehouse\".\"warehouse_fax\" is null and "
        + "\"warehouse\".\"warehouse_name\" = 'Freeman And Co') or "
        + "(\"warehouse\".\"wa_address1\" = '3377 Coachman Place' and "
        + "\"warehouse\".\"warehouse_fax\" = '971-555-6213' and "
        + "\"warehouse\".\"warehouse_name\" = 'Jones International')) or "
        + "((\"warehouse\".\"warehouse_name\" is null or "
        + "\"warehouse\".\"wa_address1\" is null or \"warehouse\".\"warehouse_fax\" "
        + "is null) and not((\"warehouse\".\"wa_address1\" = "
        + "'234 West Covina Pkwy' and \"warehouse\".\"warehouse_fax\" is null "
        + "and \"warehouse\".\"warehouse_name\" = 'Freeman And Co') or "
        + "(\"warehouse\".\"wa_address1\" = '3377 Coachman Place' and "
        + "\"warehouse\".\"warehouse_fax\" = '971-555-6213' and "
        + "\"warehouse\".\"warehouse_name\" = 'Jones International')))) "
        + "group by \"warehouse\".\"warehouse_fax\", \"warehouse\".\"wa_address1\", "
        + "\"warehouse\".\"warehouse_name\", \"product_class\".\"product_family\" "
        + "order by \"warehouse\".\"warehouse_fax\" ASC, "
        + "\"warehouse\".\"wa_address1\" ASC, \"warehouse\".\"warehouse_name\" ASC, "
        + "\"product_class\".\"product_family\" ASC";

    String necjSqlMySql =
      "select `warehouse`.`warehouse_fax` as `c0`, `warehouse`.`wa_address1` as `c1`, "
        + "`warehouse`.`warehouse_name` as `c2`, `product_class`.`product_family` as `c3` "
        + "from `warehouse` as `warehouse`, `inventory_fact_1997` as `inventory_fact_1997`, "
        + "`product` as `product`, `product_class` as `product_class` where "
        + "`inventory_fact_1997`.`warehouse_id` = `warehouse`.`warehouse_id` "
        + "and `product`.`product_class_id` = `product_class`.`product_class_id` "
        + "and `inventory_fact_1997`.`product_id` = `product`.`product_id` "
        + "and (`product_class`.`product_family` = 'Food') and "
        + "(not ((`warehouse`.`warehouse_name`, `warehouse`.`wa_address1`, `warehouse`.`warehouse_fax`) "
        + "in (('Jones International', '3377 Coachman Place', '971-555-6213')) "
        + "or (`warehouse`.`warehouse_fax` is null and (`warehouse`.`warehouse_name`, `warehouse`.`wa_address1`) "
        + "in (('Freeman And Co', '234 West Covina Pkwy')))) or "
        + "((`warehouse`.`warehouse_name` is null or `warehouse`.`wa_address1` is null "
        + "or `warehouse`.`warehouse_fax` is null) and not((`warehouse`.`warehouse_fax` is null "
        + "and (`warehouse`.`warehouse_name`, `warehouse`.`wa_address1`) in "
        + "(('Freeman And Co', '234 West Covina Pkwy')))))) "
        + "group by `warehouse`.`warehouse_fax`, `warehouse`.`wa_address1`, "
        + "`warehouse`.`warehouse_name`, `product_class`.`product_family` "
        + "order by ISNULL(`warehouse`.`warehouse_fax`), `warehouse`.`warehouse_fax` ASC, "
        + "ISNULL(`warehouse`.`wa_address1`), `warehouse`.`wa_address1` ASC, "
        + "ISNULL(`warehouse`.`warehouse_name`), `warehouse`.`warehouse_name` ASC, "
        + "ISNULL(`product_class`.`product_family`), `product_class`.`product_family` ASC";

    SqlPattern[] patterns = {
      new SqlPattern(
        DatabaseProduct.DERBY, necjSqlDerby, necjSqlDerby ),
      new SqlPattern(
        DatabaseProduct.MYSQL, necjSqlMySql, necjSqlMySql )
    };
      class TestNotInMultiLevelMemberConstraintMixedNullNonNullParentModifier extends PojoMappingModifier {

          public TestNotInMultiLevelMemberConstraintMixedNullNonNullParentModifier(CatalogMapping catalog) {
              super(catalog);
          }

          @Override
          protected List<CubeMapping> cubes(List<? extends CubeMapping> cubes) {
              List<CubeMapping> result = new ArrayList<>();

              StandardDimensionMappingImpl  warehouse2 = StandardDimensionMappingImpl.builder()
              		.withName("Warehouse2")
              		.withHierarchies(List.of(
              			ExplicitHierarchyMappingImpl.builder()
              			.withHasAll(true)
              			.withPrimaryKey(FoodmartMappingSupplier.WAREHOUSE_ID_COLUMN_IN_WAREHOUSE)
              			.withQuery(TableQueryMappingImpl.builder().withTable(FoodmartMappingSupplier.WAREHOUSE_TABLE).build())
              			.withLevels(List.of(
              				LevelMappingImpl.builder()
              				.withName("fax")
              				.withColumn(FoodmartMappingSupplier.WAREHOUSE_FAX_COLUMN_IN_WAREHOUSE)
              				.withUniqueMembers(true)
              				.build(),
              				LevelMappingImpl.builder()
              				.withName("address1")
              				.withColumn(FoodmartMappingSupplier.WA_ADDRESS1_COLUMN_IN_WAREHOUSE)
              				.withUniqueMembers(false)
              				.build(),
              				LevelMappingImpl.builder()
              				.withName("name")
              				.withColumn(FoodmartMappingSupplier.WAREHOUSE_NAME_COLUMN_IN_WAREHOUSE)
              				.withUniqueMembers(false)
              				.build()
              			))
              			.build()
              		))
              		.build();
              result.addAll(super.cubes(cubes));
              result.add(PhysicalCubeMappingImpl.builder()
                  .withName("Warehouse2")
                  .withQuery(TableQueryMappingImpl.builder().withTable(FoodmartMappingSupplier.INVENTORY_FACKT_1997_TABLE).build())
                  .withDimensionConnectors(List.of(
                      DimensionConnectorMappingImpl.builder()
                      	  .withOverrideDimensionName("Product")
                      	  .withDimension((DimensionMappingImpl) look(FoodmartMappingSupplier.DIMENSION_PRODUCT))
                          .withForeignKey(FoodmartMappingSupplier.PRODUCT_ID_COLUMN_IN_INVENTORY_FACKT_1997)
                          .build(),
                      DimensionConnectorMappingImpl.builder()
                      	  .withOverrideDimensionName("Warehouse2")
                          .withDimension(warehouse2)
                          .withForeignKey(FoodmartMappingSupplier.WAREHOUSE_ID_COLUMN_IN_INVENTORY_FACKT_1997)
                          .build(),
                      DimensionConnectorMappingImpl.builder()
                      	  .withOverrideDimensionName("Warehouse2")
                      	  .withDimension(StandardDimensionMappingImpl.builder()
                      		  .withName("Warehouse2")
                      		  .withHierarchies(List.of(
                             ExplicitHierarchyMappingImpl.builder()
                                  .withHasAll(true)
                                  .withPrimaryKey(FoodmartMappingSupplier.WAREHOUSE_ID_COLUMN_IN_WAREHOUSE)
                                  .withQuery(TableQueryMappingImpl.builder().withTable(FoodmartMappingSupplier.WAREHOUSE_TABLE).build())
                      			  .withLevels(List.of(
                        				LevelMappingImpl.builder()
                        				.withName("fax")
                        				.withColumn(FoodmartMappingSupplier.WAREHOUSE_FAX_COLUMN_IN_WAREHOUSE)
                        				.withUniqueMembers(true)
                        				.build(),
                        				LevelMappingImpl.builder()
                        				.withName("address1")
                        				.withColumn(FoodmartMappingSupplier.WA_ADDRESS1_COLUMN_IN_WAREHOUSE)
                        				.withUniqueMembers(false)
                        				.build(),
                        				LevelMappingImpl.builder()
                        				.withName("name")
                        				.withColumn(FoodmartMappingSupplier.WAREHOUSE_NAME_COLUMN_IN_WAREHOUSE)
                        				.withUniqueMembers(false)
                        				.build()
                        			))
                                  .build()
                          )).build())
                          .build()
                  ))
                  .withMeasureGroups(List.of(MeasureGroupMappingImpl.builder().withMeasures(List.of(
                      SumMeasureMappingImpl.builder()
                          .withName("Warehouse Cost")
                          .withColumn(FoodmartMappingSupplier.WAREHOUSE_COST_COLUMN_IN_INVENTORY_FACKT_1997)
                          .build(),
                      SumMeasureMappingImpl.builder()
                          .withName("Warehouse Sales")
                          .withColumn(FoodmartMappingSupplier.WAREHOUSE_SALES_COLUMN_IN_INVENTORY_FACKT_1997)
                          .build()

                  )).build()))
                  .build());
              return result;
          }
      }
    /*
    String baseSchema = TestUtil.getRawSchema(context);
    String schema = SchemaUtil.getSchema(baseSchema,
        dimension,
        cube,
        null,
        null,
        null,
        null );
    withSchema(context, schema);
    */
      withSchema(context, TestNotInMultiLevelMemberConstraintMixedNullNonNullParentModifier::new);
      assertQuerySql(context.getConnectionWithDefaultRole(), query, patterns );
  }

  /**
   * Test that null members are included when the filter explicitly excludes a single member that has a null.  The
   * members span multiple levels.
   */
  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  public void  testNotInMultiLevelMemberConstraintSingleNullParent(Context<?> context) {
    if ( !isDefaultNullMemberRepresentation() ) {
      return;
    }
    if ( SystemWideProperties.instance().FilterChildlessSnowflakeMembers ) {
      return;
    }

    String dimension =
      "<Dimension name=\"Warehouse2\">\n"
        + "  <Hierarchy hasAll=\"true\" primaryKey=\"warehouse_id\">\n"
        + "    <Table name=\"warehouse\"/>\n"
        + "    <Level name=\"fax\" column=\"warehouse_fax\" uniqueMembers=\"true\"/>\n"
        + "    <Level name=\"address1\" column=\"wa_address1\" uniqueMembers=\"false\"/>\n"
        + "    <Level name=\"name\" column=\"warehouse_name\" uniqueMembers=\"false\"/>\n"
        + "  </Hierarchy>\n"
        + "</Dimension>\n";

    String cube =
      "<Cube name=\"Warehouse2\">\n"
        + "  <Table name=\"inventory_fact_1997\"/>\n"
        + "  <DimensionUsage name=\"Product\" source=\"Product\" foreignKey=\"product_id\"/>\n"
        + "  <DimensionUsage name=\"Warehouse2\" source=\"Warehouse2\" foreignKey=\"warehouse_id\"/>\n"
        + "  <Measure name=\"Warehouse Cost\" column=\"warehouse_cost\" aggregator=\"sum\"/>\n"
        + "  <Measure name=\"Warehouse Sales\" column=\"warehouse_sales\" aggregator=\"sum\"/>\n"
        + "</Cube>";

    String query =
      "with\n"
        + "set [Filtered Warehouse Set] as 'Filter([Warehouse2].[name].Members, [Warehouse2].CurrentMember Not In"
        + "{[Warehouse2].[#null].[234 West Covina Pkwy].[Freeman And Co]})' "
        + "set [NECJ] as NonEmptyCrossJoin([Filtered Warehouse Set], {[Product].[Product Family].Food}) "
        + "select [NECJ] on 0 from [Warehouse2]";

    String necjSqlDerby =
      "select \"warehouse\".\"warehouse_fax\", \"warehouse\".\"wa_address1\", "
        + "\"warehouse\".\"warehouse_name\", \"product_class\".\"product_family\" "
        + "from \"warehouse\" as \"warehouse\", \"inventory_fact_1997\" as "
        + "\"inventory_fact_1997\", \"product\" as \"product\", \"product_class\" "
        + "as \"product_class\" where \"inventory_fact_1997\".\"warehouse_id\" = "
        + "\"warehouse\".\"warehouse_id\" and \"product\".\"product_class_id\" = "
        + "\"product_class\".\"product_class_id\" and "
        + "\"inventory_fact_1997\".\"product_id\" = \"product\".\"product_id\" and "
        + "(\"product_class\".\"product_family\" = 'Food') and ((not "
        + "(\"warehouse\".\"warehouse_name\" = 'Freeman And Co') or "
        + "(\"warehouse\".\"warehouse_name\" is null)) or (not "
        + "(\"warehouse\".\"wa_address1\" = '234 West Covina Pkwy') or "
        + "(\"warehouse\".\"wa_address1\" is null)) or not "
        + "(\"warehouse\".\"warehouse_fax\" is null)) group by "
        + "\"warehouse\".\"warehouse_fax\", \"warehouse\".\"wa_address1\", "
        + "\"warehouse\".\"warehouse_name\", \"product_class\".\"product_family\" "
        + "order by \"warehouse\".\"warehouse_fax\" ASC, "
        + "\"warehouse\".\"wa_address1\" ASC, \"warehouse\".\"warehouse_name\" ASC, "
        + "\"product_class\".\"product_family\" ASC";

    String necjSqlMySql =
      "select `warehouse`.`warehouse_fax` as `c0`, "
        + "`warehouse`.`wa_address1` as `c1`, `warehouse`.`warehouse_name` "
        + "as `c2`, `product_class`.`product_family` as `c3` from "
        + "`warehouse` as `warehouse`, `inventory_fact_1997` as "
        + "`inventory_fact_1997`, `product` as `product`, `product_class` "
        + "as `product_class` where `inventory_fact_1997`.`warehouse_id` = "
        + "`warehouse`.`warehouse_id` and `product`.`product_class_id` = "
        + "`product_class`.`product_class_id` and "
        + "`inventory_fact_1997`.`product_id` = `product`.`product_id` and "
        + "(`product_class`.`product_family` = 'Food') and "
        + "((not (`warehouse`.`warehouse_name` = 'Freeman And Co') or "
        + "(`warehouse`.`warehouse_name` is null)) or (not "
        + "(`warehouse`.`wa_address1` = '234 West Covina Pkwy') or "
        + "(`warehouse`.`wa_address1` is null)) or not "
        + "(`warehouse`.`warehouse_fax` is null)) group by "
        + "`warehouse`.`warehouse_fax`, `warehouse`.`wa_address1`, "
        + "`warehouse`.`warehouse_name`, `product_class`.`product_family` "
        + "order by ISNULL(`warehouse`.`warehouse_fax`), "
        + "`warehouse`.`warehouse_fax` ASC, "
        + "ISNULL(`warehouse`.`wa_address1`), `warehouse`.`wa_address1` ASC, "
        + "ISNULL(`warehouse`.`warehouse_name`), "
        + "`warehouse`.`warehouse_name` ASC, "
        + "ISNULL(`product_class`.`product_family`), "
        + "`product_class`.`product_family` ASC";

    SqlPattern[] patterns = {
      new SqlPattern(
        DatabaseProduct.DERBY, necjSqlDerby, necjSqlDerby ),
      new SqlPattern(
        DatabaseProduct.MYSQL, necjSqlMySql, necjSqlMySql )
    };
      class TestNotInMultiLevelMemberConstraintSingleNullParentModifier extends PojoMappingModifier {

          public TestNotInMultiLevelMemberConstraintSingleNullParentModifier(CatalogMapping catalog) {
              super(catalog);
          }

          @Override
          protected List<CubeMapping> cubes(List<? extends CubeMapping> cubes) {
              StandardDimensionMappingImpl  warehouse2 = StandardDimensionMappingImpl.builder()
                		.withName("Warehouse2")
                		.withHierarchies(List.of(
                			ExplicitHierarchyMappingImpl.builder()
                			.withHasAll(true)
                			.withPrimaryKey(FoodmartMappingSupplier.WAREHOUSE_ID_COLUMN_IN_WAREHOUSE)
                			.withQuery(TableQueryMappingImpl.builder().withTable(FoodmartMappingSupplier.WAREHOUSE_TABLE).build())
                			.withLevels(List.of(
                				LevelMappingImpl.builder()
                				.withName("fax")
                				.withColumn(FoodmartMappingSupplier.WAREHOUSE_FAX_COLUMN_IN_WAREHOUSE)
                				.withUniqueMembers(true)
                				.build(),
                				LevelMappingImpl.builder()
                				.withName("address1")
                				.withColumn(FoodmartMappingSupplier.WA_ADDRESS1_COLUMN_IN_WAREHOUSE)
                				.withUniqueMembers(false)
                				.build(),
                				LevelMappingImpl.builder()
                				.withName("name")
                				.withColumn(FoodmartMappingSupplier.WAREHOUSE_NAME_COLUMN_IN_WAREHOUSE)
                				.withUniqueMembers(false)
                				.build()
                			))
                			.build()
                		))
                		.build();

              List<CubeMapping> result = new ArrayList<>();
              result.addAll(super.cubes(cubes));
              result.add(PhysicalCubeMappingImpl.builder()
                  .withName("Warehouse2")
                  .withQuery(TableQueryMappingImpl.builder().withTable(FoodmartMappingSupplier.INVENTORY_FACKT_1997_TABLE).build())
                  .withDimensionConnectors(List.of(
                      DimensionConnectorMappingImpl.builder()
                      	  .withOverrideDimensionName("Product")
                      	  .withDimension((DimensionMappingImpl) look(FoodmartMappingSupplier.DIMENSION_PRODUCT))
                          .withForeignKey(FoodmartMappingSupplier.PRODUCT_ID_COLUMN_IN_INVENTORY_FACKT_1997)
                          .build(),
                      DimensionConnectorMappingImpl.builder()
                      	  .withOverrideDimensionName("Warehouse2")
                          .withDimension(warehouse2)
                          .withForeignKey(FoodmartMappingSupplier.WAREHOUSE_ID_COLUMN_IN_INVENTORY_FACKT_1997)
                          .build(),
                      DimensionConnectorMappingImpl.builder()
                      	  .withOverrideDimensionName("Warehouse2")
                      	  .withDimension(StandardDimensionMappingImpl.builder()
                      		  .withName("Warehouse2")
                      		  .withHierarchies(List.of(
                             ExplicitHierarchyMappingImpl.builder()
                                  .withHasAll(true)
                                  .withPrimaryKey(FoodmartMappingSupplier.WAREHOUSE_ID_COLUMN_IN_WAREHOUSE)
                                  .withQuery(TableQueryMappingImpl.builder().withTable(FoodmartMappingSupplier.WAREHOUSE_TABLE).build())
                      			  .withLevels(List.of(
                        				LevelMappingImpl.builder()
                        				.withName("fax")
                        				.withColumn(FoodmartMappingSupplier.WAREHOUSE_FAX_COLUMN_IN_WAREHOUSE)
                        				.withUniqueMembers(true)
                        				.build(),
                        				LevelMappingImpl.builder()
                        				.withName("address1")
                        				.withColumn(FoodmartMappingSupplier.WA_ADDRESS1_COLUMN_IN_WAREHOUSE)
                        				.withUniqueMembers(false)
                        				.build(),
                        				LevelMappingImpl.builder()
                        				.withName("name")
                        				.withColumn(FoodmartMappingSupplier.WAREHOUSE_NAME_COLUMN_IN_WAREHOUSE)
                        				.withUniqueMembers(false)
                        				.build()
                        			))
                                  .build()
                          )).build())
                          .build()
                  ))
                  .withMeasureGroups(List.of(MeasureGroupMappingImpl.builder().withMeasures(List.of(
                          SumMeasureMappingImpl.builder()
                              .withName("Warehouse Cost")
                              .withColumn(FoodmartMappingSupplier.WAREHOUSE_COST_COLUMN_IN_INVENTORY_FACKT_1997)
                              .build(),
                          SumMeasureMappingImpl.builder()
                              .withName("Warehouse Sales")
                              .withColumn(FoodmartMappingSupplier.WAREHOUSE_SALES_COLUMN_IN_INVENTORY_FACKT_1997)
                              .build()

                  )).build()))
                  .build());
              return result;
          }
      }
    /*
    String baseSchema = TestUtil.getRawSchema(context);
    String schema = SchemaUtil.getSchema(baseSchema,
        dimension,
        cube,
        null,
        null,
        null,
        null );
    withSchema(context, schema);
    */
      withSchema(context, TestNotInMultiLevelMemberConstraintSingleNullParentModifier::new);
      assertQuerySql(context.getConnectionWithDefaultRole(), query, patterns);
  }

  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  public void  testCachedNativeSetUsingFilters(Context<?> context) throws Exception {
    ((TestContextImpl)context).setExpandNonNative(false);
    ((TestContextImpl)context).setEnableNativeFilter(true);

    // Get a fresh connection; Otherwise the mondrian property setting
    // is not refreshed for this parameter.
    boolean requestFreshConnection = true;

    String query1 =
      "With "
        + "Set [*NATIVE_CJ_SET] as 'NonEmptyCrossJoin([*BASE_MEMBERS_Customers],[*BASE_MEMBERS_Product])' "
        + "Set [*BASE_MEMBERS_Customers] as 'Filter([Customers].[City].Members,Ancestor([Customers].CurrentMember, "
        + "[Customers].[State Province]) In {[Customers].[All Customers].[USA].[CA]})' "
        + "Set [*BASE_MEMBERS_Product] as 'Filter([Product].[Product Family].Members,[Product].CurrentMember In "
        + "{[Product].[All Products].[Drink]})' "
        + "Set [*CJ_ROW_AXIS] as 'Generate([*NATIVE_CJ_SET], {([Customers].currentMember,[Product].currentMember)})' "
        + "Select "
        + "{[Measures].[Customer Count]} on columns, "
        + "Non Empty [*CJ_ROW_AXIS] on rows "
        + "From [Sales]";

    checkNative(context,  100, 45, query1, null, requestFreshConnection );

    // query2 has different filters; it should not reuse the result from
    // query1.
    String query2 =
      "With "
        + "Set [*NATIVE_CJ_SET] as 'NonEmptyCrossJoin([*BASE_MEMBERS_Customers],[*BASE_MEMBERS_Product])' "
        + "Set [*BASE_MEMBERS_Customers] as 'Filter([Customers].[City].Members,Ancestor([Customers].CurrentMember, "
        + "[Customers].[State Province]) In {[Customers].[All Customers].[USA].[OR]})' "
        + "Set [*BASE_MEMBERS_Product] as 'Filter([Product].[Product Family].Members,[Product].CurrentMember In "
        + "{[Product].[All Products].[Drink]})' "
        + "Set [*CJ_ROW_AXIS] as 'Generate([*NATIVE_CJ_SET], {([Customers].currentMember,[Product].currentMember)})' "
        + "Select "
        + "{[Measures].[Customer Count]} on columns, "
        + "Non Empty [*CJ_ROW_AXIS] on rows "
        + "From [Sales]";

    checkNative(context,  100, 11, query2, null, requestFreshConnection );
  }

  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  public void  testNativeFilter(Context<?> context) {
    ((TestContextImpl)context).setExpandNonNative(false);
    ((TestContextImpl)context).setEnableNativeFilter(true);

    // Get a fresh connection; Otherwise the mondrian property setting
    // is not refreshed for this parameter.
    boolean requestFreshConnection = true;
    checkNative(context,
      32,
      18,
      "select {[Measures].[Store Sales]} ON COLUMNS, "
        + "Order(Filter(Descendants([Customers].[All Customers].[USA].[CA], [Customers].[Name]), ([Measures].[Store "
        + "Sales] > 200.0)), [Measures].[Store Sales], DESC) ON ROWS "
        + "from [Sales] "
        + "where ([Time].[1997])",
      null,
      requestFreshConnection );
  }

  /**
   * Executes a Filter() whose condition contains a calculated member.
   */
  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  public void  testCmNativeFilter(Context<?> context) {
    ((TestContextImpl)context).setExpandNonNative(false);
    ((TestContextImpl)context).setEnableNativeFilter(true);

    // Get a fresh connection; Otherwise the mondrian property setting
    // is not refreshed for this parameter.
    boolean requestFreshConnection = true;
    checkNative(context,
      0,
      8,
      "with member [Measures].[Rendite] as '([Measures].[Store Sales] - [Measures].[Store Cost]) / [Measures].[Store "
        + "Cost]' "
        + "select NON EMPTY {[Measures].[Unit Sales], [Measures].[Store Cost], [Measures].[Rendite], [Measures]"
        + ".[Store Sales]} ON COLUMNS, "
        + "NON EMPTY Order(Filter([Product].[Product Name].Members, ([Measures].[Rendite] > 1.8)), [Measures]"
        + ".[Rendite], BDESC) ON ROWS "
        + "from [Sales] "
        + "where ([Store].[All Stores].[USA].[CA], [Time].[1997])",
      "Axis #0:\n"
        + "{[Store].[Store].[USA].[CA], [Time].[Time].[1997]}\n"
        + "Axis #1:\n"
        + "{[Measures].[Unit Sales]}\n"
        + "{[Measures].[Store Cost]}\n"
        + "{[Measures].[Rendite]}\n"
        + "{[Measures].[Store Sales]}\n"
        + "Axis #2:\n"
        + "{[Product].[Product].[Food].[Baking Goods].[Jams and Jellies].[Peanut Butter].[Plato].[Plato Extra Chunky Peanut "
        + "Butter]}\n"
        + "{[Product].[Product].[Food].[Snack Foods].[Snack Foods].[Popcorn].[Horatio].[Horatio Buttered Popcorn]}\n"
        + "{[Product].[Product].[Food].[Canned Foods].[Canned Tuna].[Tuna].[Better].[Better Canned Tuna in Oil]}\n"
        + "{[Product].[Product].[Food].[Produce].[Fruit].[Fresh Fruit].[High Top].[High Top Cantelope]}\n"
        + "{[Product].[Product].[Non-Consumable].[Household].[Electrical].[Lightbulbs].[Denny].[Denny 75 Watt Lightbulb]}\n"
        + "{[Product].[Product].[Food].[Breakfast Foods].[Breakfast Foods].[Cereal].[Johnson].[Johnson Oatmeal]}\n"
        + "{[Product].[Product].[Drink].[Alcoholic Beverages].[Beer and Wine].[Wine].[Portsmouth].[Portsmouth Light Wine]}\n"
        + "{[Product].[Product].[Food].[Produce].[Vegetables].[Fresh Vegetables].[Ebony].[Ebony Squash]}\n"
        + "Row #0: 42\n"
        + "Row #0: 24.06\n"
        + "Row #0: 1.93\n"
        + "Row #0: 70.56\n"
        + "Row #1: 36\n"
        + "Row #1: 29.02\n"
        + "Row #1: 1.91\n"
        + "Row #1: 84.60\n"
        + "Row #2: 39\n"
        + "Row #2: 20.55\n"
        + "Row #2: 1.85\n"
        + "Row #2: 58.50\n"
        + "Row #3: 25\n"
        + "Row #3: 21.76\n"
        + "Row #3: 1.84\n"
        + "Row #3: 61.75\n"
        + "Row #4: 43\n"
        + "Row #4: 59.62\n"
        + "Row #4: 1.83\n"
        + "Row #4: 168.99\n"
        + "Row #5: 34\n"
        + "Row #5: 7.20\n"
        + "Row #5: 1.83\n"
        + "Row #5: 20.40\n"
        + "Row #6: 36\n"
        + "Row #6: 33.10\n"
        + "Row #6: 1.83\n"
        + "Row #6: 93.60\n"
        + "Row #7: 46\n"
        + "Row #7: 28.34\n"
        + "Row #7: 1.81\n"
        + "Row #7: 79.58\n",
      requestFreshConnection );
  }

  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  public void  testNonNativeFilterWithNullMeasure(Context<?> context) {
    ((TestContextImpl)context).setExpandNonNative(false);
    ((TestContextImpl)context).setEnableNativeFilter(false);
    checkNotNative(context,
      9,
      "select Filter([Store].[Store Name].members, "
        + "              Not ([Measures].[Store Sqft] - [Measures].[Grocery Sqft] < 10000)) on rows, "
        + "{[Measures].[Store Sqft], [Measures].[Grocery Sqft]} on columns "
        + "from [Store]",
      "Axis #0:\n"
        + "{}\n"
        + "Axis #1:\n"
        + "{[Measures].[Store Sqft]}\n"
        + "{[Measures].[Grocery Sqft]}\n"
        + "Axis #2:\n"
        + "{[Store].[Store].[Mexico].[DF].[Mexico City].[Store 9]}\n"
        + "{[Store].[Store].[Mexico].[DF].[San Andres].[Store 21]}\n"
        + "{[Store].[Store].[Mexico].[Yucatan].[Merida].[Store 8]}\n"
        + "{[Store].[Store].[USA].[CA].[Alameda].[HQ]}\n"
        + "{[Store].[Store].[USA].[CA].[San Diego].[Store 24]}\n"
        + "{[Store].[Store].[USA].[WA].[Bremerton].[Store 3]}\n"
        + "{[Store].[Store].[USA].[WA].[Tacoma].[Store 17]}\n"
        + "{[Store].[Store].[USA].[WA].[Walla Walla].[Store 22]}\n"
        + "{[Store].[Store].[USA].[WA].[Yakima].[Store 23]}\n"
        + "Row #0: 36,509\n"
        + "Row #0: 22,450\n"
        + "Row #1: \n"
        + "Row #1: \n"
        + "Row #2: 30,797\n"
        + "Row #2: 20,141\n"
        + "Row #3: \n"
        + "Row #3: \n"
        + "Row #4: \n"
        + "Row #4: \n"
        + "Row #5: 39,696\n"
        + "Row #5: 24,390\n"
        + "Row #6: 33,858\n"
        + "Row #6: 22,123\n"
        + "Row #7: \n"
        + "Row #7: \n"
        + "Row #8: \n"
        + "Row #8: \n" );
  }

  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  public void  testNativeFilterWithNullMeasure(Context<?> context) {
    // Currently this behaves differently from the non-native evaluation.
    ((TestContextImpl)context).setEnableNativeFilter(true);
    ((TestContextImpl)context).setExpandNonNative(false);

    // Get a fresh connection; Otherwise the mondrian property setting
    // is not refreshed for this parameter.
    //final TestContext<?> context = getTestContext().withFreshConnection();
    Connection connection = context.getConnectionWithDefaultRole();
    try {
      assertQueryReturns(connection,
        "select Filter([Store].[Store Name].members, "
          + "              Not ([Measures].[Store Sqft] - [Measures].[Grocery Sqft] < 10000)) on rows, "
          + "{[Measures].[Store Sqft], [Measures].[Grocery Sqft]} on columns "
          + "from [Store]", "Axis #0:\n"
          + "{}\n"
          + "Axis #1:\n"
          + "{[Measures].[Store Sqft]}\n"
          + "{[Measures].[Grocery Sqft]}\n"
          + "Axis #2:\n"
          + "{[Store].[Store].[Mexico].[DF].[Mexico City].[Store 9]}\n"
          + "{[Store].[Store].[Mexico].[Yucatan].[Merida].[Store 8]}\n"
          + "{[Store].[Store].[USA].[WA].[Bremerton].[Store 3]}\n"
          + "{[Store].[Store].[USA].[WA].[Tacoma].[Store 17]}\n"
          + "Row #0: 36,509\n"
          + "Row #0: 22,450\n"
          + "Row #1: 30,797\n"
          + "Row #1: 20,141\n"
          + "Row #2: 39,696\n"
          + "Row #2: 24,390\n"
          + "Row #3: 33,858\n"
          + "Row #3: 22,123\n" );
    } finally {
      connection.close();
    }
  }

  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  public void  testNonNativeFilterWithCalcMember(Context<?> context) {
    // Currently this query cannot run natively
    ((TestContextImpl)context).setEnableNativeFilter(false);
    ((TestContextImpl)context).setExpandNonNative(false);
    checkNotNative(context,
      3,
      "with\n"
        + "member [Time].[Time].[Date Range] as 'Aggregate({[Time].[1997].[Q1]:[Time].[1997].[Q4]})'\n"
        + "select\n"
        + "{[Measures].[Unit Sales]} ON columns,\n"
        + "Filter ([Store].[Store State].members, [Measures].[Store Cost] > 100) ON rows\n"
        + "from [Sales]\n"
        + "where [Time].[Date Range]\n",
      "Axis #0:\n"
        + "{[Time].[Time].[Date Range]}\n"
        + "Axis #1:\n"
        + "{[Measures].[Unit Sales]}\n"
        + "Axis #2:\n"
        + "{[Store].[Store].[USA].[CA]}\n"
        + "{[Store].[Store].[USA].[OR]}\n"
        + "{[Store].[Store].[USA].[WA]}\n"
        + "Row #0: 74,748\n"
        + "Row #1: 67,659\n"
        + "Row #2: 124,366\n" );
  }

  /**
   * Verify that filter with Not IsEmpty(storedMeasure) can be natively evaluated.
   */
  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  public void  testNativeFilterNonEmpty(Context<?> context) {
    ((TestContextImpl)context).setExpandNonNative(false);
    ((TestContextImpl)context).setEnableNativeFilter(true);

    // Get a fresh connection; Otherwise the mondrian property setting
    // is not refreshed for this parameter.
    boolean requestFreshConnection = true;
    checkNative(context,
      0,
      20,
      "select Filter(CrossJoin([Store].[Store Name].members, "
        + "                        "
        + hierarchyName( "Store Type", "Store Type" )
        + ".[Store Type].members), "
        + "                        Not IsEmpty([Measures].[Store Sqft])) on rows, "
        + "{[Measures].[Store Sqft]} on columns "
        + "from [Store]",
      null,
      requestFreshConnection );
  }

  /**
   * Testcase for
   * <a href="http://jira.pentaho.com/browse/MONDRIAN-706">bug MONDRIAN-706,
   * "SQL using hierarchy attribute 'Column Name' instead of 'Column' in the filter"</a>.
   */
  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  public void  testBugMondrian706(Context<?> context) {
      ((TestContextImpl)context).setUseAggregates(false);
      ((TestContextImpl)context).setReadAggregates(false);
    ((TestContextImpl)context).setDisableCaching(false);
    SystemWideProperties.instance().EnableNativeNonEmpty = true;

    SystemWideProperties.instance().CompareSiblingsByOrderKey = true;
    ((TestContextImpl)context).setNullDenominatorProducesNull( true );
    ((TestContextImpl)context).setExpandNonNative(true);
    ((TestContextImpl)context).setEnableNativeFilter(true);
    // With bug MONDRIAN-706, would generate
    //
    // ((`store`.`store_name`, `store`.`store_city`, `store`.`store_state`)
    //   in (('11', 'Portland', 'OR'), ('14', 'San Francisco', 'CA'))
    //
    // Notice that the '11' and '14' store ID is applied on the store_name
    // instead of the store_id. So it would return no rows.
    final String badMysqlSQL =
      "select `store`.`store_country` as `c0`, `store`.`store_state` as `c1`, `store`.`store_city` as `c2`, `store`"
        + ".`store_id` as `c3`, `store`.`store_name` as `c4`, `store`.`store_type` as `c5`, `store`.`store_manager` "
        + "as `c6`, `store`.`store_sqft` as `c7`, `store`.`grocery_sqft` as `c8`, `store`.`frozen_sqft` as `c9`, "
        + "`store`.`meat_sqft` as `c10`, `store`.`coffee_bar` as `c11`, `store`.`store_street_address` as `c12` from "
        + "`FOODMART`.`store` as `store` where (`store`.`store_state` in ('CA', 'OR')) and ((`store`.`store_name`,"
        + "`store`.`store_city`,`store`.`store_state`) in (('11','Portland','OR'),('14','San Francisco','CA'))) group"
        + " by `store`.`store_country`, `store`.`store_state`, `store`.`store_city`, `store`.`store_id`, `store`"
        + ".`store_name`, `store`.`store_type`, `store`.`store_manager`, `store`.`store_sqft`, `store`"
        + ".`grocery_sqft`, `store`.`frozen_sqft`, `store`.`meat_sqft`, `store`.`coffee_bar`, `store`"
        + ".`store_street_address` having NOT((sum(`store`.`store_sqft`) is null)) "
        + ( getDialect(context.getConnectionWithDefaultRole()).requiresOrderByAlias()
        ? "order by ISNULL(`c0`) ASC, `c0` ASC, "
        + "ISNULL(`c1`) ASC, `c1` ASC, "
        + "ISNULL(`c2`) ASC, `c2` ASC, "
        + "ISNULL(`c3`) ASC, `c3` ASC"
        : "order by ISNULL(`store`.`store_country`) ASC, `store`.`store_country` ASC, "
        + "ISNULL(`store`.`store_state`) ASC, `store`.`store_state` ASC, "
        + "ISNULL(`store`.`store_city`) ASC, `store`.`store_city` ASC, "
        + "ISNULL(`store`.`store_id`) ASC, `store`.`store_id` ASC" );
    final String goodMysqlSQL =
      "select `store`.`store_country` as `c0`, `store`.`store_state` as `c1`, `store`.`store_city` as `c2`, `store`"
        + ".`store_id` as `c3`, `store`.`store_name` as `c4` from `store` as `store` where (`store`.`store_state` in "
        + "('CA', 'OR')) and ((`store`.`store_id`, `store`.`store_city`, `store`.`store_state`) in ((11, 'Portland', "
        + "'OR'), (14, 'San Francisco', 'CA'))) group by `store`.`store_country`, `store`.`store_state`, `store`"
        + ".`store_city`, `store`.`store_id`, `store`.`store_name` having NOT((sum(`store`.`store_sqft`) is null)) "
        + ( getDialect(context.getConnectionWithDefaultRole()).requiresOrderByAlias()
        ? " order by ISNULL(`c0`) ASC, `c0` ASC, "
        + "ISNULL(`c1`) ASC, `c1` ASC, "
        + "ISNULL(`c2`) ASC, `c2` ASC, "
        + "ISNULL(`c3`) ASC, `c3` ASC"
        : " order by ISNULL(`store`.`store_country`) ASC, `store`.`store_country` ASC, "
        + "ISNULL(`store`.`store_state`) ASC, `store`.`store_state` ASC, "
        + "ISNULL(`store`.`store_city`) ASC, `store`.`store_city` ASC, "
        + "ISNULL(`store`.`store_id`) ASC, `store`.`store_id` ASC" );
    final String mdx =
      "With\n"
        + "Set [*NATIVE_CJ_SET] as 'Filter([*BASE_MEMBERS_Store], Not IsEmpty ([Measures].[Store Sqft]))'\n"
        + "Set [*SORTED_ROW_AXIS] as 'Order([*CJ_ROW_AXIS],Ancestor([Store].CurrentMember, [Store].[Store Country])"
        + ".OrderKey,BASC,Ancestor([Store].CurrentMember, [Store].[Store State]).OrderKey,BASC,Ancestor([Store]"
        + ".CurrentMember,\n"
        + "[Store].[Store City]).OrderKey,BASC,[Store].CurrentMember.OrderKey,BASC)'\n"
        + "Set [*NATIVE_MEMBERS_Store] as 'Generate([*NATIVE_CJ_SET], {[Store].CurrentMember})'\n"
        + "Set [*BASE_MEMBERS_Measures] as '{[Measures].[*FORMATTED_MEASURE_0]}'\n"
        + "Set [*CJ_ROW_AXIS] as 'Generate([*NATIVE_CJ_SET], {([Store].currentMember)})'\n"
        + "Set [*BASE_MEMBERS_Store] as 'Filter([Store].[Store Name].Members,(Ancestor([Store].CurrentMember, [Store]"
        + ".[Store State]) In {[Store].[All Stores].[USA].[CA],[Store].[All Stores].[USA].[OR]}) AND ([Store]"
        + ".CurrentMember In\n"
        + "{[Store].[All Stores].[USA].[OR].[Portland].[Store 11],[Store].[All Stores].[USA].[CA].[San Francisco]"
        + ".[Store 14]}))'\n"
        + "Set [*CJ_COL_AXIS] as '[*NATIVE_CJ_SET]'\n"
        + "Member [Measures].[*FORMATTED_MEASURE_0] as '[Measures].[Store Sqft]', FORMAT_STRING = '#,###', "
        + "SOLVE_ORDER=400\n"
        + "Select\n"
        + "[*BASE_MEMBERS_Measures] on columns,\n"
        + "[*SORTED_ROW_AXIS] on rows\n"
        + "From [Store] \n";
    final SqlPattern[] badPatterns = {
      new SqlPattern(
        DatabaseProduct.MYSQL,
        badMysqlSQL,
        null )
    };
    final SqlPattern[] goodPatterns = {
      new SqlPattern(
        DatabaseProduct.MYSQL,
        goodMysqlSQL,
        null )
    };
    /*
    ((BaseTestContext)context).update(SchemaUpdater.createSubstitutingCube(
        "Store",
        "<Dimension name='Store Type'>\n"
          + "    <Hierarchy name='Store Types Hierarchy' allMemberName='All Store Types Member Name' hasAll='true'>\n"
          + "      <Level name='Store Type' column='store_type' uniqueMembers='true'/>\n"
          + "    </Hierarchy>\n"
          + "  </Dimension>\n"
          + "  <Dimension name='Store'>\n"
          + "    <Hierarchy hasAll='true' primaryKey='store_id'>\n"
          + "      <Table name='store'/>\n"
          + "      <Level name='Store Country' column='store_country' uniqueMembers='true'/>\n"
          + "      <Level name='Store State' column='store_state' uniqueMembers='true'/>\n"
          + "      <Level name='Store City' column='store_city' uniqueMembers='false'/>\n"
          + "      <Level name='Store Name' column='store_id' type='Numeric' nameColumn='store_name' "
          + "uniqueMembers='false'/>\n"
          + "    </Hierarchy>\n"
          + "  </Dimension>\n" ));
     */
    withSchema(context, SchemaModifiers.FilterTestModifier::new);
    Connection connection = context.getConnectionWithDefaultRole();
    assertQuerySqlOrNot(connection, mdx, badPatterns, true, true, true );
    TestUtil.flushSchemaCache(connection);
    assertQuerySqlOrNot(context.getConnectionWithDefaultRole(), mdx, goodPatterns, false, true, true );
    assertQueryReturns(connection,
      mdx,
      "Axis #0:\n"
        + "{}\n"
        + "Axis #1:\n"
        + "{[Measures].[*FORMATTED_MEASURE_0]}\n"
        + "Axis #2:\n"
        + "{[Store].[Store].[USA].[CA].[San Francisco].[Store 14]}\n"
        + "{[Store].[Store].[USA].[OR].[Portland].[Store 11]}\n"
        + "Row #0: 22,478\n"
        + "Row #1: 20,319\n" );
  }

  /**
   * Tests the bug MONDRIAN-779. The {@link import mondrian.rolap.sql.MemberListCrossJoinArg;} was not considering
   * the 'exclude' attribute in its
   * hash code. This resulted in two filters being chained within two different named sets to register a cache element
   * with the same key, even though they were the different because of the added "NOT" keyword.
   */
  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  public void  testBug779(Context<?> context) {
    final String query1 =
      "With Set [*NATIVE_CJ_SET] as 'Filter([*BASE_MEMBERS_Product], Not IsEmpty ([Measures].[Unit Sales]))' Set "
        + "[*BASE_MEMBERS_Product] as 'Filter([Product].[Product Department].Members,(Ancestor([Product]"
        + ".CurrentMember, [Product].[Product Family]) In {[Product].[Drink],[Product].[Food]}) AND ([Product]"
        + ".CurrentMember In {[Product].[Drink].[Dairy]}))' Select [Measures].[Unit Sales] on columns, "
        + "[*NATIVE_CJ_SET] on rows From [Sales]";
    final String query2 =
      "With Set [*NATIVE_CJ_SET] as 'Filter([*BASE_MEMBERS_Product], Not IsEmpty ([Measures].[Unit Sales]))' Set "
        + "[*BASE_MEMBERS_Product] as 'Filter([Product].[Product Department].Members,(Ancestor([Product]"
        + ".CurrentMember, [Product].[Product Family]) In {[Product].[Drink],[Product].[Food]}) AND ([Product]"
        + ".CurrentMember Not In {[Product].[Drink].[Dairy]}))' Select [Measures].[Unit Sales] on columns, "
        + "[*NATIVE_CJ_SET] on rows From [Sales]";

    final String expectedResult1 =
      "Axis #0:\n"
        + "{}\n"
        + "Axis #1:\n"
        + "{[Measures].[Unit Sales]}\n"
        + "Axis #2:\n"
        + "{[Product].[Product].[Drink].[Dairy]}\n"
        + "Row #0: 4,186\n";

    final String expectedResult2 =
      "Axis #0:\n"
        + "{}\n"
        + "Axis #1:\n"
        + "{[Measures].[Unit Sales]}\n"
        + "Axis #2:\n"
        + "{[Product].[Product].[Drink].[Alcoholic Beverages]}\n"
        + "{[Product].[Product].[Drink].[Beverages]}\n"
        + "{[Product].[Product].[Food].[Baked Goods]}\n"
        + "{[Product].[Product].[Food].[Baking Goods]}\n"
        + "{[Product].[Product].[Food].[Breakfast Foods]}\n"
        + "{[Product].[Product].[Food].[Canned Foods]}\n"
        + "{[Product].[Product].[Food].[Canned Products]}\n"
        + "{[Product].[Product].[Food].[Dairy]}\n"
        + "{[Product].[Product].[Food].[Deli]}\n"
        + "{[Product].[Product].[Food].[Eggs]}\n"
        + "{[Product].[Product].[Food].[Frozen Foods]}\n"
        + "{[Product].[Product].[Food].[Meat]}\n"
        + "{[Product].[Product].[Food].[Produce]}\n"
        + "{[Product].[Product].[Food].[Seafood]}\n"
        + "{[Product].[Product].[Food].[Snack Foods]}\n"
        + "{[Product].[Product].[Food].[Snacks]}\n"
        + "{[Product].[Product].[Food].[Starchy Foods]}\n"
        + "Row #0: 6,838\n"
        + "Row #1: 13,573\n"
        + "Row #2: 7,870\n"
        + "Row #3: 20,245\n"
        + "Row #4: 3,317\n"
        + "Row #5: 19,026\n"
        + "Row #6: 1,812\n"
        + "Row #7: 12,885\n"
        + "Row #8: 12,037\n"
        + "Row #9: 4,132\n"
        + "Row #10: 26,655\n"
        + "Row #11: 1,714\n"
        + "Row #12: 37,792\n"
        + "Row #13: 1,764\n"
        + "Row #14: 30,545\n"
        + "Row #15: 6,884\n"
        + "Row #16: 5,262\n";

    assertQueryReturns(context.getConnectionWithDefaultRole(), query1, expectedResult1);
    assertQueryReturns(context.getConnectionWithDefaultRole(), query2, expectedResult2);
  }

  /**
   * http://jira.pentaho.com/browse/MONDRIAN-1458 When using a multi value IN clause which includes null values against
   * a collapsed field on an aggregate table, the dimension table field was referenced as the column expression, causing
   * sql errors.
   */
  @ParameterizedTest
  @ContextSource(propertyUpdater = AppandFoodMartCatalog.class, dataloader = FastFoodmardDataLoader.class)
  public void  testMultiValueInWithNullVals(Context<?> context) {
    // MONDRIAN-1458 - Native exclusion predicate doesn't use agg table
    // when checking for nulls
    //TestContext<?> context = getTestContext();
    if ( !context.getConfigValue(ConfigConstants.ENABLE_NATIVE_CROSS_JOIN, ConfigConstants.ENABLE_NATIVE_CROSS_JOIN_DEFAULT_VALUE, Boolean.class)
      || !context.getConfigValue(ConfigConstants.READ_AGGREGATES, ConfigConstants.READ_AGGREGATES_DEFAULT_VALUE ,Boolean.class)
      || !context.getConfigValue(ConfigConstants.USE_AGGREGATES, ConfigConstants.USE_AGGREGATES_DEFAULT_VALUE ,Boolean.class) ) {
      return;
    }

    String sql;
    if ( !getDialect(context.getConnectionWithDefaultRole()).supportsMultiValueInExpr() ) {
      sql = "select `agg_g_ms_pcat_sales_fact_1997`.`product_family` "
        + "as `c0`,"
        + " `agg_g_ms_pcat_sales_fact_1997`.`product_department` as "
        + "`c1`,"
        + " `agg_g_ms_pcat_sales_fact_1997`.`gender` as `c2` "
        + "from `agg_g_ms_pcat_sales_fact_1997` as "
        + "`agg_g_ms_pcat_sales_fact_1997` "
        + "where (not ((`agg_g_ms_pcat_sales_fact_1997`."
        + "`product_family` = 'Food'"
        + " and `agg_g_ms_pcat_sales_fact_1997`."
        + "`product_department` = 'Baked Goods') "
        + "or (`agg_g_ms_pcat_sales_fact_1997`.`product_family` "
        + "= 'Drink' "
        + "and `agg_g_ms_pcat_sales_fact_1997`."
        + "`product_department` = 'Dairy')) "
        + "or ((`agg_g_ms_pcat_sales_fact_1997`."
        + "`product_department` is null "
        + "or `agg_g_ms_pcat_sales_fact_1997`."
        + "`product_family` is null) "
        + "and not((`agg_g_ms_pcat_sales_fact_1997`.`product_family`"
        + " = 'Food' "
        + "and `agg_g_ms_pcat_sales_fact_1997`.`product_department` "
        + "= 'Baked Goods') "
        + "or (`agg_g_ms_pcat_sales_fact_1997`.`product_family` = "
        + "'Drink' "
        + "and `agg_g_ms_pcat_sales_fact_1997`.`product_department` "
        + "= 'Dairy')))) "
        + "group by `agg_g_ms_pcat_sales_fact_1997`.`product_family`,"
        + " `agg_g_ms_pcat_sales_fact_1997`.`product_department`,"
        + " `agg_g_ms_pcat_sales_fact_1997`.`gender` "
        + "order by ISNULL(`agg_g_ms_pcat_sales_fact_1997`."
        + "`product_family`) ASC,"
        + " `agg_g_ms_pcat_sales_fact_1997`.`product_family` ASC,"
        + " ISNULL(`agg_g_ms_pcat_sales_fact_1997`."
        + "`product_department`) ASC,"
        + " `agg_g_ms_pcat_sales_fact_1997`.`product_department` ASC,"
        + " ISNULL(`agg_g_ms_pcat_sales_fact_1997`.`gender`) ASC,"
        + " `agg_g_ms_pcat_sales_fact_1997`.`gender` ASC";
    } else {
      sql = "select `agg_g_ms_pcat_sales_fact_1997`."
        + "`product_family` as `c0`,"
        + " `agg_g_ms_pcat_sales_fact_1997`.`product_department` as `c1`,"
        + " `agg_g_ms_pcat_sales_fact_1997`.`gender` as `c2` "
        + "from `agg_g_ms_pcat_sales_fact_1997` as "
        + "`agg_g_ms_pcat_sales_fact_1997` "
        + "where (not ((`agg_g_ms_pcat_sales_fact_1997`.`product_department`,"
        + " `agg_g_ms_pcat_sales_fact_1997`.`product_family`) in "
        + "(('Baked Goods',"
        + " 'Food'),"
        + " ('Dairy',"
        + " 'Drink'))) or (`agg_g_ms_pcat_sales_fact_1997`."
        + "`product_department` "
        + "is null or `agg_g_ms_pcat_sales_fact_1997`.`product_family` "
        + "is null)) "
        + "group by `agg_g_ms_pcat_sales_fact_1997`.`product_family`,"
        + " `agg_g_ms_pcat_sales_fact_1997`.`product_department`,"
        + " `agg_g_ms_pcat_sales_fact_1997`.`gender` order by "
        + "ISNULL(`agg_g_ms_pcat_sales_fact_1997`.`product_family`) ASC,"
        + " `agg_g_ms_pcat_sales_fact_1997`.`product_family` ASC,"
        + " ISNULL(`agg_g_ms_pcat_sales_fact_1997`.`product_department`) ASC,"
        + " `agg_g_ms_pcat_sales_fact_1997`.`product_department` ASC,"
        + " ISNULL(`agg_g_ms_pcat_sales_fact_1997`.`gender`) ASC,"
        + " `agg_g_ms_pcat_sales_fact_1997`.`gender` ASC";
    }
    String mdx = "select NonEmptyCrossjoin( \n"
      + "   filter ( product.[product department].members,\n"
      + "      NOT ([Product].CurrentMember IN  \n"
      + "  { [Product].[Food].[Baked Goods], Product.Drink.Dairy})),\n"
      + "   gender.gender.members\n"
      + ")\n"
      + "on 0 from sales\n";
    assertQuerySql(context.getConnectionWithDefaultRole(),
      mdx,
      new SqlPattern[] {
        new SqlPattern( DatabaseProduct.MYSQL, sql, null )
      } );
  }


}
