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
package mondrian.rolap;

import mondrian.test.BasicQueryTest;
import mondrian.test.PerformanceTest;
import mondrian.test.UdfTest;
import org.eclipse.daanse.db.dialect.api.Dialect;
import org.eclipse.daanse.olap.api.access.RollupPolicy;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingCalculatedMember;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingCube;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingCubeDimension;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingHierarchy;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingInlineTable;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingLevel;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingMeasure;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingPrivateDimension;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingRole;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingSchema;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingTable;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingUserDefinedFunction;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingView;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingVirtualCube;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.MappingVirtualCubeMeasure;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.enums.AccessEnum;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.enums.DimensionTypeEnum;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.enums.HideMemberIfEnum;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.enums.LevelTypeEnum;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.enums.MeasureDataTypeEnum;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.enums.MemberGrantAccessEnum;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.enums.PropertyTypeEnum;
import org.eclipse.daanse.olap.rolap.dbmapper.model.api.enums.TypeEnum;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.JoinR;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.SQLR;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.TableR;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.AggColumnNameRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.AggExcludeRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.AggForeignKeyRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.AggLevelRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.AggMeasureRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.AggNameRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.AnnotationRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.CalculatedMemberPropertyRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.CalculatedMemberRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.CellFormatterRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.ClosureRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.ColumnDefRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.CubeGrantRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.CubeRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.CubeUsageRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.DimensionGrantRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.DimensionUsageRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.ExpressionViewRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.FormulaRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.HierarchyGrantRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.HierarchyRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.InlineTableRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.LevelRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.MeasureRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.MemberGrantRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.PrivateDimensionRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.PropertyRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.RoleRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.RoleUsageRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.RowRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.SQLRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.SchemaGrantRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.SchemaRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.ScriptRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.UnionRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.UserDefinedFunctionRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.ValueRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.ViewRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.VirtualCubeDimensionRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.VirtualCubeMeasureRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.model.record.builder.VirtualCubeRBuilder;
import org.eclipse.daanse.olap.rolap.dbmapper.provider.modifier.record.RDbMappingSchemaModifier;

import java.util.ArrayList;
import java.util.List;

public class SchemaModifiers {

    /*
            + "<Role name=\"No_WA_State\">\n"
                + "  <SchemaGrant access=\"none\">\n"
                + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
                + "      <HierarchyGrant hierarchy=\"[Customers]\" access=\"custom\" rollupPolicy=\"partial\">\n"
                + "        <MemberGrant member=\"[Customers].[USA].[WA]\" access=\"none\"/>\n"
                + "        <MemberGrant member=\"[Customers].[USA].[OR]\" access=\"all\"/>\n"
                + "        <MemberGrant member=\"[Customers].[USA].[CA]\" access=\"all\"/>\n"
                + "        <MemberGrant member=\"[Customers].[Canada]\" access=\"all\"/>\n"
                + "        <MemberGrant member=\"[Customers].[Mexico]\" access=\"all\"/>\n"
                + "      </HierarchyGrant>\n"
                + "    </CubeGrant>\n"
                + "  </SchemaGrant>\n"
                + "</Role>\n";
    */
    public static class RoleRestrictionWorksWaRoleDef extends RDbMappingSchemaModifier {

        public RoleRestrictionWorksWaRoleDef(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(RoleRBuilder.builder()
                .name("No_WA_State")
                .schemaGrants(List.of(
                    SchemaGrantRBuilder.builder()
                        .access(AccessEnum.NONE)
                        .cubeGrants(List.of(
                            CubeGrantRBuilder.builder()
                                .cube("Sales")
                                .access("all")
                                .hierarchyGrants(List.of(
                                    HierarchyGrantRBuilder.builder()
                                        .hierarchy("[Customers]")
                                        .access(AccessEnum.CUSTOM)
                                        .rollupPolicy("partial")
                                        .memberGrants(List.of(
                                            MemberGrantRBuilder.builder()
                                                .member("[Customers].[USA].[WA]")
                                                .access(MemberGrantAccessEnum.NONE)
                                                .build(),
                                            MemberGrantRBuilder.builder()
                                                .member("[Customers].[USA].[OR]")
                                                .access(MemberGrantAccessEnum.ALL)
                                                .build(),
                                            MemberGrantRBuilder.builder()
                                                .member("[Customers].[USA].[CA]")
                                                .access(MemberGrantAccessEnum.ALL)
                                                .build(),
                                            MemberGrantRBuilder.builder()
                                                .member("[Customers].[Canada]")
                                                .access(MemberGrantAccessEnum.ALL)
                                                .build(),
                                            MemberGrantRBuilder.builder()
                                                .member("[Customers].[Mexico]")
                                                .access(MemberGrantAccessEnum.ALL)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .build());
            return result;
        }
    }

    /*
            + "<Role name=\"Only_DF_State\">\n"
                + "  <SchemaGrant access=\"none\">\n"
                + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
                + "      <HierarchyGrant hierarchy=\"[Customers]\" access=\"custom\" rollupPolicy=\"partial\">\n"
                + "        <MemberGrant member=\"[Customers].[USA].[WA]\" access=\"all\"/>\n"
                + "        <MemberGrant member=\"[Customers].[USA].[OR]\" access=\"all\"/>\n"
                + "        <MemberGrant member=\"[Customers].[USA].[CA]\" access=\"all\"/>\n"
                + "        <MemberGrant member=\"[Customers].[Canada]\" access=\"all\"/>\n"
                + "        <MemberGrant member=\"[Customers].[Mexico].[DF]\" access=\"all\"/>\n"
                + "      </HierarchyGrant>\n"
                + "    </CubeGrant>\n"
                + "  </SchemaGrant>\n"
                + "</Role>\n";
    */
    public static class RoleRestrictionWorksDfRoleDef extends RDbMappingSchemaModifier {

        public RoleRestrictionWorksDfRoleDef(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(RoleRBuilder.builder()
                .name("Only_DF_State")
                .schemaGrants(List.of(
                    SchemaGrantRBuilder.builder()
                        .access(AccessEnum.NONE)
                        .cubeGrants(List.of(
                            CubeGrantRBuilder.builder()
                                .cube("Sales")
                                .access("all")
                                .hierarchyGrants(List.of(
                                    HierarchyGrantRBuilder.builder()
                                        .hierarchy("[Customers]")
                                        .access(AccessEnum.CUSTOM)
                                        .rollupPolicy("partial")
                                        .memberGrants(List.of(
                                            MemberGrantRBuilder.builder()
                                                .member("[Customers].[USA].[WA]")
                                                .access(MemberGrantAccessEnum.ALL)
                                                .build(),
                                            MemberGrantRBuilder.builder()
                                                .member("[Customers].[USA].[OR]")
                                                .access(MemberGrantAccessEnum.ALL)
                                                .build(),
                                            MemberGrantRBuilder.builder()
                                                .member("[Customers].[USA].[CA]")
                                                .access(MemberGrantAccessEnum.ALL)
                                                .build(),
                                            MemberGrantRBuilder.builder()
                                                .member("[Customers].[Canada]")
                                                .access(MemberGrantAccessEnum.ALL)
                                                .build(),
                                            MemberGrantRBuilder.builder()
                                                .member("[Customers].[Mexico].[DF]")
                                                .access(MemberGrantAccessEnum.ALL)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .build());
            return result;
        }
    }

    public static class CustomCountMeasureCubeName extends RDbMappingSchemaModifier {

        public CustomCountMeasureCubeName(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCube> cubes(List<MappingCube> cubes) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.cubes(cubes));
            result.add(CubeRBuilder.builder()
                .name("StoreWithCountM")
                .fact(new TableR("store"))
                .dimensionUsageOrDimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .visible(true)
                        .highCardinality(false)
                        .name("Store Type")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .visible(true)
                                .hasAll(true)
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Store Type")
                                        .visible(true)
                                        .column("store_type")
                                        .type(TypeEnum.STRING)
                                        .uniqueMembers(true)
                                        .levelType(LevelTypeEnum.REGULAR)
                                        .hideMemberIf(HideMemberIfEnum.NEVER)
                                        .build()
                                ))
                                .build()
                        ))
                        .build(),
                    DimensionUsageRBuilder.builder()
                        .source("Store")
                        .name("Store")
                        .visible(true)
                        .highCardinality(false)
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .visible(true)
                        .highCardinality(false)
                        .name("Has coffee bar")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .visible(true)
                                .hasAll(true)
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Has coffee bar")
                                        .visible(true)
                                        .column("coffee_bar")
                                        .type(TypeEnum.BOOLEAN)
                                        .uniqueMembers(true)
                                        .levelType(LevelTypeEnum.REGULAR)
                                        .hideMemberIf(HideMemberIfEnum.NEVER)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Store Sqft")
                        .column("store_sqft")
                        .formatString("#,###")
                        .aggregator("sum")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Grocery Sqft")
                        .column("grocery_sqft")
                        .formatString("#,###")
                        .aggregator("sum")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("CountM")
                        .column("store_id")
                        .formatString("Standard")
                        .aggregator("count")
                        .visible(true)
                        .build()
                ))
                .build());
            return result;
        }
    }

    public static class SharedDimensionTestModifier extends RDbMappingSchemaModifier {
        /*
        "<Cube name=\"Employee Store Analysis A\">\n"
        + "  <Table name=\"inventory_fact_1997\" alias=\"inventory\" />\n"
        + "  <DimensionUsage name=\"Employee\" source=\"Employee\" foreignKey=\"product_id\" />\n"
        + "  <DimensionUsage name=\"Store Type\" source=\"Store Type\" foreignKey=\"warehouse_id\" />\n"
        + "  <Measure name=\"Employee Store Sales\" aggregator=\"sum\" formatString=\"$#,##0\" column=\"warehouse_sales\" />\n"
        + "  <Measure name=\"Employee Store Cost\" aggregator=\"sum\" formatString=\"$#,##0\" column=\"warehouse_cost\" />\n"
        + "</Cube>";


        "<Cube name=\"Employee Store Analysis B\">\n"
        + "  <Table name=\"inventory_fact_1997\" alias=\"inventory\" />\n"
        + "  <DimensionUsage name=\"Employee\" source=\"Employee\" foreignKey=\"time_id\" />\n"
        + "  <DimensionUsage name=\"Store Type\" source=\"Store Type\" foreignKey=\"store_id\" />\n"
        + "  <Measure name=\"Employee Store Sales\" aggregator=\"sum\" formatString=\"$#,##0\" column=\"warehouse_sales\" />\n"
        + "  <Measure name=\"Employee Store Cost\" aggregator=\"sum\" formatString=\"$#,##0\" column=\"warehouse_cost\" />\n"
        + "</Cube>";

        "<VirtualCube name=\"Employee Store Analysis\">\n"
        + "  <VirtualCubeDimension name=\"Employee\"/>\n"
        + "  <VirtualCubeDimension name=\"Store Type\"/>\n"
        + "  <VirtualCubeMeasure cubeName=\"Employee Store Analysis A\" name=\"[Measures].[Employee Store Sales]\"/>\n"
        + "  <VirtualCubeMeasure cubeName=\"Employee Store Analysis B\" name=\"[Measures].[Employee Store Cost]\"/>\n"
        + "</VirtualCube>";

        "<Dimension name=\"Employee\">\n"
        + "  <Hierarchy hasAll=\"true\" primaryKey=\"employee_id\" primaryKeyTable=\"employee\">\n"
        + "    <Join leftKey=\"supervisor_id\" rightKey=\"employee_id\">\n"
        + "      <Table name=\"employee\" alias=\"employee\" />\n"
        + "      <Table name=\"employee\" alias=\"employee_manager\" />\n"
        + "    </Join>\n"
        + "    <Level name=\"Role\" table=\"employee_manager\" column=\"management_role\" uniqueMembers=\"true\"/>\n"
        + "    <Level name=\"Title\" table=\"employee_manager\" column=\"position_title\" uniqueMembers=\"false\"/>\n"
        + "  </Hierarchy>\n"
        + "</Dimension>";

        */
        public SharedDimensionTestModifier(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        protected List<MappingPrivateDimension> schemaDimensions(MappingSchema mappingSchemaOriginal) {
            List<MappingPrivateDimension> result = new ArrayList<>();
            result.addAll(super.schemaDimensions(mappingSchemaOriginal));
            result.add(PrivateDimensionRBuilder.builder()
                .name("Employee")
                .hierarchies(List.of(
                    HierarchyRBuilder.builder()
                        .hasAll(true)
                        .primaryKey("employee_id")
                        .primaryKeyTable("employee")
                        .relation(new JoinR(List.of(
                            new TableR(null, "employee", "employee", List.of()),
                            new TableR(null, "employee", "employee_manager", List.of())
                        ),
                            null, "supervisor_id",
                            null, "employee_id"
                            ))
                        .levels(List.of(
                            LevelRBuilder.builder()
                                .name("Role")
                                .table("employee_manager").column("management_role").uniqueMembers(true)
                                .build(),
                            LevelRBuilder.builder()
                                .name("Title")
                                .table("employee_manager").column("position_title").uniqueMembers(false)
                                .build()
                        ))
                        .build()
                ))
                .build());
            return result;
        }

        @Override
        protected List<MappingCube> schemaCubes(MappingSchema schema) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(schema));
            result.add(CubeRBuilder.builder()
                .name("Employee Store Analysis A")
                .fact(new TableR(null,"inventory_fact_1997", "inventory", List.of()))
                .dimensionUsageOrDimensions(List.of(
                    DimensionUsageRBuilder.builder()
                        .name("Employee")
                        .source("Employee")
                        .foreignKey("product_id")
                        .build(),
                    DimensionUsageRBuilder.builder()
                        .name("Store Type")
                        .source("Store Type")
                        .foreignKey("warehouse_id")
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Employee Store Sales")
                        .aggregator("sum")
                        .formatString("$#,##0")
                        .column("warehouse_sales")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Employee Store Cost")
                        .aggregator("sum")
                        .formatString("$#,##0")
                        .column("warehouse_cost")
                        .build()
                ))
                .build());
            result.add(CubeRBuilder.builder()
                .name("Employee Store Analysis B")
                .fact(new TableR(null,"inventory_fact_1997", "inventory", List.of()))
                .dimensionUsageOrDimensions(List.of(
                    DimensionUsageRBuilder.builder()
                        .name("Employee")
                        .source("Employee")
                        .foreignKey("time_id")
                        .build(),
                    DimensionUsageRBuilder.builder()
                        .name("Store Type")
                        .source("Store Type")
                        .foreignKey("store_id")
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Employee Store Sales")
                        .aggregator("sum")
                        .formatString("$#,##0")
                        .column("warehouse_sales")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Employee Store Cost")
                        .aggregator("sum")
                        .formatString("$#,##0")
                        .column("warehouse_cost")
                        .build()
                ))
                .build());
            return result;
        }


        @Override
        protected List<MappingVirtualCube> schemaVirtualCubes(MappingSchema schema) {
            List<MappingVirtualCube> result = new ArrayList<>();
            result.addAll(super.schemaVirtualCubes(schema));
            result.add(VirtualCubeRBuilder.builder()
                .name("Employee Store Analysis")
                .virtualCubeDimensions(List.of(
                    VirtualCubeDimensionRBuilder.builder()
                        .name("Employee")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .name("Store Type")
                        .build()
                ))
                .virtualCubeMeasures(List.of(
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Employee Store Analysis A")
                        .name("[Measures].[Employee Store Sales]")
                        .build(),
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Employee Store Analysis B")
                        .name("[Measures].[Employee Store Cost]")
                        .build()
                ))
                .build());
            return result;
        }
    }

    public static class SharedDimensionTestModifier1 extends RDbMappingSchemaModifier {

        /*
                "<Cube name=\"Alternate Sales\">\n"
        + "  <Table name=\"sales_fact_1997\"/>\n"
        + "  <DimensionUsage name=\"Store Type\" source=\"Store Type\" foreignKey=\"store_id\" />\n"
        + "  <DimensionUsage name=\"Store\" source=\"Store\" foreignKey=\"store_id\"/>\n"
        + "  <DimensionUsage name=\"Buyer\" source=\"Store\" visible=\"true\" foreignKey=\"product_id\" highCardinality=\"false\"/>\n"
        + "  <DimensionUsage name=\"BuyerTwo\" source=\"Store\" visible=\"true\" foreignKey=\"product_id\" highCardinality=\"false\"/>\n"
        + "  <DimensionUsage name=\"Store Size in SQFT\" source=\"Store Size in SQFT\"\n"
        + "      foreignKey=\"store_id\"/>\n"
        + "  <DimensionUsage name=\"Store Type\" source=\"Store Type\" foreignKey=\"store_id\"/>\n"
        + "  <DimensionUsage name=\"Time\" source=\"Time\" foreignKey=\"time_id\"/>\n"
        + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\" formatString=\"Standard\"/>\n"
        + "</Cube>";

         */
        public SharedDimensionTestModifier1(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCube> schemaCubes(MappingSchema schema) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(schema));
            result.add(CubeRBuilder.builder()
                .name("Alternate Sales")
                .fact(new TableR("sales_fact_1997"))
                .dimensionUsageOrDimensions(List.of(
                    DimensionUsageRBuilder.builder()
                        .name("Store Type")
                        .source("Store Type")
                        .foreignKey("store_id")
                        .build(),
                    DimensionUsageRBuilder.builder()
                        .name("Store")
                        .source("Store")
                        .foreignKey("store_id")
                        .build(),
                    DimensionUsageRBuilder.builder()
                        .name("Buyer")
                        .source("Store")
                        .visible(true)
                        .foreignKey("product_id")
                        .highCardinality(false)
                        .build(),
                    DimensionUsageRBuilder.builder()
                        .name("BuyerTwo")
                        .source("Store")
                        .visible(true)
                        .foreignKey("product_id")
                        .highCardinality(false)
                        .build(),
                    DimensionUsageRBuilder.builder()
                        .name("Store Size in SQFT")
                        .source("Store Size in SQFT")
                        .foreignKey("store_id")
                        .build(),
                    DimensionUsageRBuilder.builder()
                        .name("Store Type")
                        .source("Store Type")
                        .foreignKey("store_id")
                        .build(),
                    DimensionUsageRBuilder.builder()
                        .name("Time")
                        .source("Time")
                        .foreignKey("time_id")
                        .build()

                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Unit Sales")
                        .column("unit_sales")
                        .aggregator("sum")
                        .formatString("Standard")
                        .build()
                ))
                .build());
            return result;
        }


    }

    public static class AggregationOnDistinctCountMeasuresTestModifier extends RDbMappingSchemaModifier {

        public AggregationOnDistinctCountMeasuresTestModifier(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingVirtualCube> schemaVirtualCubes(MappingSchema schema) {
            List<MappingVirtualCube> result = new ArrayList<>();
            result.addAll(super.schemaVirtualCubes(schema));
            result.add(VirtualCubeRBuilder.builder()
                .name("Warehouse and Sales2")
                .defaultMeasure("Store Sales")
                .virtualCubeDimensions(List.of(
                    VirtualCubeDimensionRBuilder.builder()
                        .cubeName("Sales")
                        .name("Gender")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .name("Store")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .name("Product")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .cubeName("Warehouse")
                        .name("Warehouse")
                        .build()
                ))
                .virtualCubeMeasures(List.of(
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Sales")
                        .name("[Measures].[Store Sales]")
                        .build(),
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Sales")
                        .name("[Measures].[Customer Count]")
                        .build()
                ))
                .build());

            result.add(VirtualCubeRBuilder.builder()
                .name("Warehouse and Sales3")
                .defaultMeasure("Store Invoice")
                .cubeUsages(List.of(
                    CubeUsageRBuilder.builder()
                        .cubeName("Sales")
                        .ignoreUnrelatedDimensions(true)
                        .build()
                ))
                .virtualCubeDimensions(List.of(
                    VirtualCubeDimensionRBuilder.builder()
                        .cubeName("Sales")
                        .name("Gender")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .name("Store")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .name("Product")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .cubeName("Warehouse")
                        .name("Warehouse")
                        .build()
                ))
                .virtualCubeMeasures(List.of(
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Sales")
                        .name("[Measures].[Customer Count]")
                        .build()
                ))
                .build());
            return result;
        }
    }

    //storeDimensionLevelDependent,
    //cubeA,
    public static class SelectNotInGroupByTestModifier1 extends RDbMappingSchemaModifier {

        public SelectNotInGroupByTestModifier1(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingPrivateDimension> schemaDimensions(MappingSchema schema) {
            List<MappingPrivateDimension> result = new ArrayList<>();
            result.addAll(super.schemaDimensions(schema));
            result.add(PrivateDimensionRBuilder.builder()
                .name("CustomStore")
                .hierarchies(List.of(
                    HierarchyRBuilder.builder()
                        .hasAll(true)
                        .primaryKey("store_id")
                        .relation(new TableR("store"))
                        .levels(List.of(
                            LevelRBuilder.builder()
                                .name("Store Country")
                                .column("store_country")
                                .uniqueMembers(true)
                                .build(),
                            LevelRBuilder.builder()
                                .name("Store City")
                                .column("store_city")
                                .uniqueMembers(false)
                                .properties(List.of(
                                    PropertyRBuilder.builder()
                                        .name("Store State")
                                        .column("store_state")
                                        .dependsOnLevelValue(true)
                                        .build()
                                ))
                                .build(),
                            LevelRBuilder.builder()
                                .name("Store Name")
                                .column("store_name")
                                .uniqueMembers(true)
                                .build()
                        ))
                        .build()))
                .build());
            return result;
        }

        @Override
        protected List<MappingCube> schemaCubes(MappingSchema schema) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(schema));
            result.add(CubeRBuilder.builder()
                .name("CustomSales")
                .fact(new TableR("sales_fact_1997"))
                .dimensionUsageOrDimensions(List.of(
                    DimensionUsageRBuilder.builder()
                        .name("CustomStore")
                        .source("CustomStore")
                        .foreignKey("store_id")
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Custom Store Sales")
                        .column("store_sales")
                        .aggregator("sum")
                        .formatString("#,###.00")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Custom Store Cost")
                        .column("store_cost")
                        .aggregator("sum")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Sales Count")
                        .column("product_id")
                        .aggregator("count")
                        .build()
                ))
                .build());
            return result;
        }
    }



    //storeDimensionLevelIndependent,
    //cubeA,
    /*
             "<Dimension name=\"CustomStore\">\n"
        + "  <Hierarchy hasAll=\"true\" primaryKey=\"store_id\">\n"
        + "    <Table name=\"store\"/>\n"
        + "    <Level name=\"Store Country\" column=\"store_country\" uniqueMembers=\"true\"/>\n"
        + "    <Level name=\"Store City\" column=\"store_city\" uniqueMembers=\"false\">\n"
        + "      <Property name=\"Store State\" column=\"store_state\"/>\n"
        + "    </Level>\n"
        + "    <Level name=\"Store Name\" column=\"store_name\" uniqueMembers=\"true\"/>\n"
        + "  </Hierarchy>\n"
        + "</Dimension>";

     */
    /*
               "<Cube name=\"CustomSales\">\n"
        + "  <Table name=\"sales_fact_1997\"/>\n"
        + "  <DimensionUsage name=\"CustomStore\" source=\"CustomStore\" foreignKey=\"store_id\"/>\n"
        + "  <Measure name=\"Custom Store Sales\" column=\"store_sales\" aggregator=\"sum\" formatString=\"#,###.00\"/>\n"
        + "  <Measure name=\"Custom Store Cost\" column=\"store_cost\" aggregator=\"sum\"/>\n"
        + "  <Measure name=\"Sales Count\" column=\"product_id\" aggregator=\"count\"/>\n"
        + "</Cube>";

     */

    public static class SelectNotInGroupByTestModifier2 extends RDbMappingSchemaModifier {

        public SelectNotInGroupByTestModifier2(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingPrivateDimension> schemaDimensions(MappingSchema schema) {
            List<MappingPrivateDimension> result = new ArrayList<>();
            result.addAll(super.schemaDimensions(schema));
            result.add(PrivateDimensionRBuilder.builder()
                .name("CustomStore")
                .hierarchies(List.of(
                    HierarchyRBuilder.builder()
                        .hasAll(true)
                        .primaryKey("store_id")
                        .relation(new TableR("store"))
                        .levels(List.of(
                            LevelRBuilder.builder()
                                .name("Store Country")
                                .column("store_country")
                                .uniqueMembers(true)
                                .build(),
                            LevelRBuilder.builder()
                                .name("Store City")
                                .column("store_city")
                                .uniqueMembers(false)
                                .properties(List.of(
                                    PropertyRBuilder.builder()
                                        .name("Store State")
                                        .column("store_state")
                                        .build()
                                ))
                                .build(),
                            LevelRBuilder.builder()
                                .name("Store Name")
                                .column("store_name")
                                .uniqueMembers(true)
                                .build()
                        ))
                        .build()))
                .build());
            return result;
        }

        @Override
        protected List<MappingCube> schemaCubes(MappingSchema schema) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(schema));
            result.add(CubeRBuilder.builder()
                .name("CustomSales")
                .fact(new TableR("sales_fact_1997"))
                .dimensionUsageOrDimensions(List.of(
                    DimensionUsageRBuilder.builder()
                        .name("CustomStore")
                        .source("CustomStore")
                        .foreignKey("store_id")
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Custom Store Sales")
                        .column("store_sales")
                        .aggregator("sum")
                        .formatString("#,###.00")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Custom Store Cost")
                        .column("store_cost")
                        .aggregator("sum")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Sales Count")
                        .column("product_id")
                        .aggregator("count")
                        .build()
                ))
                .build());
            return result;
        }
    }

    //storeDimensionUniqueLevelDependentProp,
    //cubeA
    public static class SelectNotInGroupByTestModifier3 extends RDbMappingSchemaModifier {

        public SelectNotInGroupByTestModifier3(MappingSchema mappingSchema) {
            super(mappingSchema);
        }
        @Override
        protected List<MappingPrivateDimension> schemaDimensions(MappingSchema schema) {
            List<MappingPrivateDimension> result = new ArrayList<>();
            result.addAll(super.schemaDimensions(schema));
            result.add(PrivateDimensionRBuilder.builder()
                .name("CustomStore")
                .hierarchies(List.of(
                    HierarchyRBuilder.builder()
                        .hasAll(true)
                        .primaryKey("store_id")
                        .uniqueKeyLevelName("Store Name")
                        .relation(new TableR("store"))
                        .levels(List.of(
                            LevelRBuilder.builder()
                                .name("Store Country")
                                .column("store_country")
                                .uniqueMembers(true)
                                .build(),
                            LevelRBuilder.builder()
                                .name("Store City")
                                .column("store_city")
                                .uniqueMembers(false)
                                .properties(List.of(
                                    PropertyRBuilder.builder()
                                        .name("Store State")
                                        .column("store_state")
                                        .dependsOnLevelValue(true)
                                        .build()
                                ))
                                .build(),
                            LevelRBuilder.builder()
                                .name("Store Name")
                                .column("store_name")
                                .uniqueMembers(true)
                                .build()
                        ))
                        .build()))
                .build());
            return result;
        }

        @Override
        protected List<MappingCube> schemaCubes(MappingSchema schema) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(schema));
            result.add(CubeRBuilder.builder()
                .name("CustomSales")
                .fact(new TableR("sales_fact_1997"))
                .dimensionUsageOrDimensions(List.of(
                    DimensionUsageRBuilder.builder()
                        .name("CustomStore")
                        .source("CustomStore")
                        .foreignKey("store_id")
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Custom Store Sales")
                        .column("store_sales")
                        .aggregator("sum")
                        .formatString("#,###.00")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Custom Store Cost")
                        .column("store_cost")
                        .aggregator("sum")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Sales Count")
                        .column("product_id")
                        .aggregator("count")
                        .build()
                ))
                .build());
            return result;
        }
    }

    //storeDimensionUniqueLevelIndependentProp,
    //cubeA
    public static class SelectNotInGroupByTestModifier4 extends RDbMappingSchemaModifier {

        public SelectNotInGroupByTestModifier4(MappingSchema mappingSchema) {
            super(mappingSchema);
        }
        @Override
        protected List<MappingPrivateDimension> schemaDimensions(MappingSchema schema) {
            List<MappingPrivateDimension> result = new ArrayList<>();
            result.addAll(super.schemaDimensions(schema));
            result.add(PrivateDimensionRBuilder.builder()
                .name("CustomStore")
                .hierarchies(List.of(
                    HierarchyRBuilder.builder()
                        .hasAll(true)
                        .primaryKey("store_id")
                        .uniqueKeyLevelName("Store Name")
                        .uniqueKeyLevelName("Store Name")
                        .relation(new TableR("store"))
                        .levels(List.of(
                            LevelRBuilder.builder()
                                .name("Store Country")
                                .column("store_country")
                                .uniqueMembers(true)
                                .build(),
                            LevelRBuilder.builder()
                                .name("Store City")
                                .column("store_city")
                                .uniqueMembers(false)
                                .properties(List.of(
                                    PropertyRBuilder.builder()
                                        .name("Store State")
                                        .column("store_state")
                                        .dependsOnLevelValue(false)
                                        .build()
                                ))
                                .build(),
                            LevelRBuilder.builder()
                                .name("Store Name")
                                .column("store_name")
                                .uniqueMembers(true)
                                .build()
                        ))
                        .build()))
                .build());
            return result;
        }

        @Override
        protected List<MappingCube> schemaCubes(MappingSchema schema) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(schema));
            result.add(CubeRBuilder.builder()
                .name("CustomSales")
                .fact(new TableR("sales_fact_1997"))
                .dimensionUsageOrDimensions(List.of(
                    DimensionUsageRBuilder.builder()
                        .name("CustomStore")
                        .source("CustomStore")
                        .foreignKey("store_id")
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Custom Store Sales")
                        .column("store_sales")
                        .aggregator("sum")
                        .formatString("#,###.00")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Custom Store Cost")
                        .column("store_cost")
                        .aggregator("sum")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Sales Count")
                        .column("product_id")
                        .aggregator("count")
                        .build()
                ))
                .build());
            return result;
        }
    }


    public static class IgnoreUnrelatedDimensionsTestModifier extends RDbMappingSchemaModifier {

        public IgnoreUnrelatedDimensionsTestModifier(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingVirtualCube> schemaVirtualCubes(MappingSchema schema) {
            List<MappingVirtualCube> result = new ArrayList<>();
            result.addAll(super.schemaVirtualCubes(schema));
            result.add(VirtualCubeRBuilder.builder()
                .name("Warehouse and Sales2")
                .defaultMeasure("Store Sales")
                .cubeUsages(List.of(
                    CubeUsageRBuilder.builder()
                        .cubeName("Sales")
                        .ignoreUnrelatedDimensions(true)
                        .build(),
                    CubeUsageRBuilder.builder()
                        .cubeName("Warehouse")
                        .ignoreUnrelatedDimensions(true)
                        .build()
                ))

                .virtualCubeDimensions(List.of(
                    VirtualCubeDimensionRBuilder.builder()
                        .cubeName("Sales")
                        .name("Customers")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .cubeName("Sales")
                        .name("Education Level")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .cubeName("Sales")
                        .name("Gender")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .cubeName("Sales")
                        .name("Marital Status")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .name("Product")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .cubeName("Sales")
                        .name("Product")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .cubeName("Sales")
                        .name("Promotions")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .name("Store")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .name("Time")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .cubeName("Sales")
                        .name("Yearly Income")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .cubeName("Warehouse")
                        .name("Warehouse")
                        .build()
                ))
                .virtualCubeMeasures(List.of(
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Sales")
                        .name("[Measures].[Sales Count]")
                        .build(),
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Sales")
                        .name("[Measures].[Store Cost]")
                        .build(),
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Sales")
                        .name("[Measures].[Store Sales]")
                        .build(),
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Sales")
                        .name("Unit Sales")
                        .build(),
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Sales")
                        .name("[Measures].[Profit]")
                        .build(),
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Sales")
                        .name("[Measures].[Profit Growth]")
                        .build(),
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Warehouse")
                        .name("[Measures].[Store Invoice]")
                        .build(),
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Warehouse")
                        .name("[Measures].[Supply Time]")
                        .build(),
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Warehouse")
                        .name("[Measures].[Units Ordered]")
                        .build(),
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Warehouse")
                        .name("[Measures].[Units Shipped]")
                        .build(),
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Warehouse")
                        .name("[Measures].[Warehouse Cost]")
                        .build(),
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Warehouse")
                        .name("[Measures].[Warehouse Profit]")
                        .build(),
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Warehouse")
                        .name("[Measures].[Warehouse Sales]")
                        .build(),
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Warehouse")
                        .name("[Measures].[Average Warehouse Sale]")
                        .build()
                ))
                .calculatedMembers(List.of(
                    CalculatedMemberRBuilder.builder()
                        .name("Profit Per Unit Shipped")
                        .dimension("Measures")
                        .formulaElement(FormulaRBuilder.builder()
                            .cdata("[Measures].[Profit] / [Measures].[Units Shipped]").build())
                        .build()
                ))
                .build());

            result.add(VirtualCubeRBuilder.builder()
                .name("Warehouse and Sales3")
                .defaultMeasure("Store Invoice")
                .cubeUsages(List.of(
                    CubeUsageRBuilder.builder()
                        .cubeName("Sales")
                        .ignoreUnrelatedDimensions(true)
                        .build()
                ))
                .virtualCubeDimensions(List.of(
                    VirtualCubeDimensionRBuilder.builder()
                        .cubeName("Sales")
                        .name("Gender")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .name("Store")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .name("Product")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .cubeName("Warehouse")
                        .name("Warehouse")
                        .build()
                ))
                .virtualCubeMeasures(List.of(
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Sales")
                        .name("[Measures].[Customer Count]")
                        .build()
                ))
                .build());
            return result;
        }
    }

    //cubeSales3
    public static class IgnoreUnrelatedDimensionsTestModifier1 extends RDbMappingSchemaModifier {

        public IgnoreUnrelatedDimensionsTestModifier1(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

            /*
            "<Cube name=\"Sales 3\">\n"
                + "   <Table name=\"sales_fact_1997\"/>\n"
                + "   <DimensionUsage name=\"Time\" source=\"Time\" foreignKey=\"time_id\"/>\n"
                + "   <Dimension name=\"Education Level\" foreignKey=\"customer_id\">\n"
                + "    <Hierarchy hasAll=\"true\" primaryKey=\"customer_id\">\n"
                + "      <Table name=\"customer\"/>\n"
                + "      <Level name=\"Education Level\" column=\"education\" uniqueMembers=\"true\"/>\n"
                + "    </Hierarchy>\n"
                + "  </Dimension>\n"
                + "   <DimensionUsage name=\"Product\" source=\"Product\" foreignKey=\"product_id\"/>\n"
                + "   <Dimension name=\"Gender\" foreignKey=\"customer_id\">\n"
                + "     <Hierarchy hasAll=\"true\" defaultMember=\"[Gender].[F]\" "
                + "allMemberName=\"All Gender\" primaryKey=\"customer_id\">\n"
                + "       <Table name=\"customer\"/>\n"
                + "       <Level name=\"Gender\" column=\"gender\" uniqueMembers=\"true\"/>\n"
                + "     </Hierarchy>\n"
                + "   </Dimension>\n"
                + "   <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"    formatString=\"Standard\">\n"
                + "     <CalculatedMemberProperty name=\"MEMBER_ORDINAL\" value=\"2\"/>\n"
                + "   </Measure>\n"
                + "</Cube>";
            */
        @Override
        protected List<MappingCube> schemaCubes(MappingSchema schema) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(schema));
            result.add(CubeRBuilder.builder()
                .name("Sales 3")
                .fact(new TableR("sales_fact_1997"))
                .dimensionUsageOrDimensions(List.of(
                    DimensionUsageRBuilder.builder()
                        .name("Time")
                        .source("Time")
                        .foreignKey("time_id")
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .name("Education Level")
                        .foreignKey("customer_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("customer_id")
                                .relation(new TableR("customer"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Education Level")
                                        .column("education")
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        ))
                        .build(),
                    DimensionUsageRBuilder.builder()
                        .name("Product")
                        .source("Product")
                        .foreignKey("product_id")
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .name("Gender")
                        .foreignKey("customer_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .defaultMember("[Gender].[F]")
                                .allMemberName("All Gender")
                                .primaryKey("customer_id")
                                .relation(new TableR("customer"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Gender")
                                        .column("gender")
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Unit Sales")
                        .column("unit_sales")
                        .aggregator("sum")
                        .formatString("Standard")
                        .calculatedMemberProperties(List.of(
                            CalculatedMemberPropertyRBuilder.builder()
                                .name("MEMBER_ORDINAL")
                                .value("2")
                                .build()
                        ))
                        .build()
                ))
                .build());
            return result;
        }

        @Override
        protected List<MappingVirtualCube> schemaVirtualCubes(MappingSchema schema) {
            List<MappingVirtualCube> result = new ArrayList<>();
            result.addAll(super.schemaVirtualCubes(schema));
            result.add(VirtualCubeRBuilder.builder()
                .name("Warehouse and Sales 3")
                .defaultMeasure("Store Invoice")
                .cubeUsages(List.of(
                    CubeUsageRBuilder.builder()
                        .cubeName("Sales 3")
                        .ignoreUnrelatedDimensions(false)
                        .build(),
                    CubeUsageRBuilder.builder()
                        .cubeName("Warehouse")
                        .ignoreUnrelatedDimensions(true)
                        .build()
                ))
                .virtualCubeDimensions(List.of(
                    VirtualCubeDimensionRBuilder.builder()
                        .cubeName("Sales 3")
                        .name("Gender")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .cubeName("Sales 3")
                        .name("Education Level")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .name("Product")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .name("Time")
                        .build(),
                    VirtualCubeDimensionRBuilder.builder()
                        .cubeName("Warehouse")
                        .name("Warehouse")
                        .build()
                ))
                .virtualCubeMeasures(List.of(
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Sales 3")
                        .name("[Measures].[Unit Sales]")
                        .build(),
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Warehouse")
                        .name("[Measures].[Store Invoice]")
                        .build(),
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("Warehouse")
                        .name("[Measures].[Warehouse Sales]")
                        .build()
                ))
                .build());

            return result;
        }
    }

    public static class ParentChildHierarchyTestModifier1 extends RDbMappingSchemaModifier {

        public ParentChildHierarchyTestModifier1(MappingSchema mappingSchema) {
            super(mappingSchema);
        }
        /*
            "  <Dimension name=\"EmployeesClosure\" foreignKey=\"employee_id\">\n"
            + "      <Hierarchy hasAll=\"true\" allMemberName=\"All Employees\"\n"
            + "          primaryKey=\"employee_id\" primaryKeyTable=\"employee_closure\">\n"
            + "        <Join leftKey=\"supervisor_id\" rightKey=\"employee_id\">\n"
            + "          <Table name=\"employee_closure\"/>\n"
            + "          <Table name=\"employee\" alias=\"employee2\" />\n"
            + "        </Join>\n"
            + "        <Level name=\"Closure\"  type=\"Numeric\" uniqueMembers=\"false\"\n"
            + "            table=\"employee_closure\" column=\"supervisor_id\"/>\n"
            + "        <Level name=\"Employee\" type=\"Numeric\" uniqueMembers=\"true\"\n"
            + "            table=\"employee_closure\" column=\"employee_id\"/>\n"
            + "      </Hierarchy>\n"
            + "  </Dimension>"));
         */
        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("HR".equals(cube.name())) {
                MappingLevel level1 = LevelRBuilder
                    .builder()
                    .name("Closure")
                    .type(TypeEnum.NUMERIC)
                    .uniqueMembers(false)
                    .build();
                MappingLevel level2 = LevelRBuilder
                    .builder()
                    .name("Employee")
                    .type(TypeEnum.NUMERIC)
                    .uniqueMembers(true)
                    .table("employee_closure")
                    .column("employee_id")
                    .build();
                MappingHierarchy hierarchy = HierarchyRBuilder
                    .builder()
                    .hasAll(true)
                    .allMemberName("All Employees")
                    .primaryKey("employee_id")
                    .primaryKeyTable("employee_closure")
                    .relation(new JoinR(List.of(
                        new TableR("employee_closure"),
                        new TableR(null, "employee", "employee2", List.of())),
                        null, "supervisor_id", null, "employee_id"))
                    .levels(List.of(level1, level2))
                    .build();
                MappingCubeDimension dimension = PrivateDimensionRBuilder
                    .builder()
                    .name("EmployeesClosure")
                    .foreignKey("employee_id")
                    .hierarchies(List.of(hierarchy))
                    .build();
                result.add(dimension);
            }
            return result;
        }
    }

    public static class ParentChildHierarchyTestModifier2 extends RDbMappingSchemaModifier {

        public ParentChildHierarchyTestModifier2(MappingSchema mappingSchema) {
            super(mappingSchema);
        }
        /*
            "<Dimension name=\"EmployeeSnowFlake\" foreignKey=\"employee_id\">"
            + "<Hierarchy hasAll=\"true\" allMemberName=\"All Employees\""
            + "    primaryKey=\"employee_id\" primaryKeyTable=\"employee\">"
            + "  <Join leftKey=\"store_id\""
            + "    rightAlias=\"store\" rightKey=\"store_id\">"
            + "    <Table name=\"employee\"/>"
            + "    <Table name=\"store\"/>"
            + "  </Join>"
            + "  <Level name=\"Employee Stores\" table=\"store\""
            + "      column=\"store_id\" uniqueMembers=\"true\"/>"
            + "  <Level name=\"Employee Id\" type=\"Numeric\" table=\"employee\" uniqueMembers=\"true\""
            + "      column=\"employee_id\" parentColumn=\"supervisor_id\""
            + "      nameColumn=\"full_name\" nullParentValue=\"0\">"
            + "    <Closure parentColumn=\"supervisor_id\" childColumn=\"employee_id\">"
            + "      <Table name=\"employee_closure\"/>"
            + "    </Closure>"
            + "    <Property name=\"Marital Status\" column=\"marital_status\"/>"
            + "    <Property name=\"Position Title\" column=\"position_title\"/>"
            + "    <Property name=\"Gender\" column=\"gender\"/>"
            + "    <Property name=\"Salary\" column=\"salary\"/>"
            + "    <Property name=\"Education Level\" column=\"education_level\"/>"
            + "    <Property name=\"Management Role\" column=\"management_role\"/>"
            + "  </Level>"
            + "</Hierarchy>"
            + "</Dimension>"));
         */
        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("HR".equals(cube.name())) {
                MappingLevel level1 = LevelRBuilder
                    .builder()
                    .name("Employee Stores")
                    .table("store")
                    .column("store_id")
                    .uniqueMembers(true)
                    .build();
                MappingLevel level2 = LevelRBuilder
                    .builder()
                    .name("Employee Id")
                    .type(TypeEnum.NUMERIC)
                    .table("employee")
                    .uniqueMembers(true)
                    .table("employee_closure")
                    .column("employee_id")
                    .parentColumn("supervisor_id")
                    .nameColumn("full_name")
                    .nullParentValue("0")
                    .closure(ClosureRBuilder.builder()
                        .parentColumn("supervisor_id")
                        .childColumn("employee_id")
                        .table(new TableR("employee_closure"))
                        .build())
                    .properties(List.of(
                        PropertyRBuilder.builder()
                            .name("Marital Status")
                            .column("marital_status")
                            .build(),
                        PropertyRBuilder.builder()
                            .name("Position Title")
                            .column("position_title")
                            .build(),
                        PropertyRBuilder.builder()
                            .name("Gender")
                            .column("gender")
                            .build(),
                        PropertyRBuilder.builder()
                            .name("Salary")
                            .column("salary")
                            .build(),
                        PropertyRBuilder.builder()
                            .name("Education Level")
                            .column("education_level")
                            .build(),
                        PropertyRBuilder.builder()
                            .name("Management Role")
                            .column("management_role")
                            .build()
                    ))
                    .build();

                MappingHierarchy hierarchy = HierarchyRBuilder
                    .builder()
                    .hasAll(true)
                    .allMemberName("All Employees")
                    .primaryKey("employee_id")
                    .primaryKeyTable("employee")
                    .relation(new JoinR(List.of(
                        new TableR("employee"),
                        new TableR("store")),
                        null, "store_id", "store", "store_id"))
                    .levels(List.of(level1, level2))
                    .build();

                MappingCubeDimension dimension = PrivateDimensionRBuilder
                    .builder()
                    .name("EmployeeSnowFlake")
                    .foreignKey("employee_id")
                    .hierarchies(List.of(hierarchy))
                    .build();
                result.add(dimension);
            }
            return result;
        }
    }

    public static class ParentChildHierarchyTestModifier3 extends RDbMappingSchemaModifier {

        public ParentChildHierarchyTestModifier3(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        /*
              "<Cube name=\"EmployeeSharedClosureCube\">\n"
            + "  <Table name=\"salary\" alias=\"salary_closure\" />\n"
            + "  <DimensionUsage name=\"SharedEmployee\" source=\"SharedEmployee\" foreignKey=\"employee_id\" />\n"
            + "  <Dimension name=\"Department\" foreignKey=\"department_id\">"
            + "    <Hierarchy hasAll=\"true\" primaryKey=\"department_id\">"
            + "      <Table name=\"department\"/>"
            + "        <Level name=\"Department Description\" uniqueMembers=\"true\""
            + "          column=\"department_id\"/>"
            + "    </Hierarchy>"
            + "  </Dimension>"
            + "  <DimensionUsage name=\"Store Type\" source=\"Store Type\" foreignKey=\"warehouse_id\" />\n"
            + "  <Measure name=\"Org Salary\" column=\"salary_paid\" aggregator=\"sum\""
            + "      formatString=\"Currency\"/>"
            + "   <Measure name=\"Count\" column=\"employee_id\" aggregator=\"count\""
            + "    formatString=\"#,#\"/>"
            + "</Cube>";
         */
        @Override
        protected List<MappingCube> schemaCubes(MappingSchema schema) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(schema));
            result.add(CubeRBuilder.builder()
                .name("EmployeeSharedClosureCube")
                .fact(new TableR(null, "salary", "sales_fact_1997", List.of()))
                .dimensionUsageOrDimensions(List.of(
                    DimensionUsageRBuilder.builder()
                        .name("SharedEmployee")
                        .source("SharedEmployee")
                        .foreignKey("employee_id")
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .name("Department")
                        .foreignKey("department_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("department_id")
                                .relation(new TableR("department"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Department Description")
                                        .column("department_id")
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        ))
                        .build(),
                    DimensionUsageRBuilder.builder()
                        .name("Store Type")
                        .source("Store Type")
                        .foreignKey("warehouse_id")
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Org Salary")
                        .column("salary_paid")
                        .aggregator("sum")
                        .formatString("Currency")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Count")
                        .column("employee_id")
                        .aggregator("count")
                        .formatString("#,#")
                        .build()
                ))
                .build()
            );
            return result;
        }

        /*
              "<Dimension name=\"SharedEmployee\">"
            + "<Hierarchy hasAll=\"true\""
            + "    primaryKey=\"employee_id\" primaryKeyTable=\"employee\">"
            + "  <Join leftKey=\"store_id\""
            + "    rightAlias=\"store\" rightKey=\"store_id\">"
            + "    <Table name=\"employee\"/>"
            + "    <Table name=\"store\"/>"
            + "  </Join>"
            + "  <Level name=\"Employee Id\" type=\"Numeric\" table=\"employee\" uniqueMembers=\"true\""
            + "      column=\"employee_id\" parentColumn=\"supervisor_id\""
            + "      nameColumn=\"full_name\" nullParentValue=\"0\">"
            + "    <Closure parentColumn=\"supervisor_id\" childColumn=\"employee_id\">"
            + "      <Table name=\"employee_closure\"/>"
            + "    </Closure>"
            + "    <Property name=\"Marital Status\" column=\"marital_status\"/>"
            + "    <Property name=\"Position Title\" column=\"position_title\"/>"
            + "    <Property name=\"Gender\" column=\"gender\"/>"
            + "    <Property name=\"Salary\" column=\"salary\"/>"
            + "    <Property name=\"Education Level\" column=\"education_level\"/>"
            + "    <Property name=\"Management Role\" column=\"management_role\"/>"
            + "  </Level>"
            + "</Hierarchy>"
            + "</Dimension>";
         */
        @Override
        protected List<MappingPrivateDimension> schemaDimensions(MappingSchema schema) {
            List<MappingPrivateDimension> result = new ArrayList<>();
            result.addAll(super.schemaDimensions(schema));
            result.add(PrivateDimensionRBuilder.builder()
                .name("SharedEmployee")
                .hierarchies(List.of(
                    HierarchyRBuilder.builder()
                        .hasAll(true)
                        .primaryKey("employee_id")
                        .primaryKeyTable("employee")
                        .relation(new JoinR(
                            List.of(new TableR("employee"), new TableR("store")),
                            null, "store_id", "store", "store_id"))
                        .levels(List.of(
                            LevelRBuilder.builder()
                                .name("Employee Id")
                                .type(TypeEnum.NUMERIC)
                                .table("employee")
                                .uniqueMembers(true)
                                .column("employee_id")
                                .parentColumn("supervisor_id")
                                .nameColumn("full_name")
                                .nullParentValue("0")
                                .closure(ClosureRBuilder.builder()
                                    .parentColumn("supervisor_id")
                                    .childColumn("employee_id")
                                    .table(new TableR("employee_closure"))
                                    .build())
                                .properties(List.of(
                                    PropertyRBuilder.builder()
                                        .name("Marital Status")
                                        .column("marital_status")
                                        .build(),
                                    PropertyRBuilder.builder()
                                        .name("Position Title")
                                        .column("position_title")
                                        .build(),
                                    PropertyRBuilder.builder()
                                        .name("Gender")
                                        .column("gender")
                                        .build(),
                                    PropertyRBuilder.builder()
                                        .name("Salary")
                                        .column("salary")
                                        .build(),
                                    PropertyRBuilder.builder()
                                        .name("Education Level")
                                        .column("education_level")
                                        .build(),
                                    PropertyRBuilder.builder()
                                        .name("Management Role")
                                        .column("management_role")
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .build());
            return result;
        }
    }

    public static class ParentChildHierarchyTestModifier4 extends RDbMappingSchemaModifier {

        public ParentChildHierarchyTestModifier4(MappingSchema mappingSchema) {
            super(mappingSchema);
        }
        /*
            "<Dimension name=\"EmployeesNonClosure\" foreignKey=\"employee_id\">"
            + "<Hierarchy hasAll=\"true\" allMemberName=\"All Employees\""
            + "    primaryKey=\"employee_id\">"
            + "  <Table name=\"employee\"/>"
            + "  <Level name=\"Employee Id\" type=\"Numeric\" uniqueMembers=\"true\""
            + "      column=\"employee_id\" parentColumn=\"supervisor_id\""
            + "      nameColumn=\"full_name\" nullParentValue=\"0\">"
            + "    <Property name=\"Marital Status\" column=\"marital_status\"/>"
            + "    <Property name=\"Position Title\" column=\"position_title\"/>"
            + "    <Property name=\"Gender\" column=\"gender\"/>"
            + "    <Property name=\"Salary\" column=\"salary\"/>"
            + "    <Property name=\"Education Level\" column=\"education_level\"/>"
            + "    <Property name=\"Management Role\" column=\"management_role\"/>"
            + "  </Level>"
            + "</Hierarchy>"
            + "</Dimension>",
         */
        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("HR".equals(cube.name())) {


                MappingLevel level = LevelRBuilder
                    .builder()
                    .name("Employee Id")
                    .type(TypeEnum.NUMERIC)
                    .uniqueMembers(true)
                    .column("employee_id")
                    .parentColumn("supervisor_id")
                    .nameColumn("full_name")
                    .nullParentValue("0")
                    .closure(ClosureRBuilder.builder()
                        .parentColumn("supervisor_id")
                        .childColumn("employee_id")
                        .table(new TableR("employee_closure"))
                        .build())
                    .properties(List.of(
                        PropertyRBuilder.builder()
                            .name("Marital Status")
                            .column("marital_status")
                            .build(),
                        PropertyRBuilder.builder()
                            .name("Position Title")
                            .column("position_title")
                            .build(),
                        PropertyRBuilder.builder()
                            .name("Gender")
                            .column("gender")
                            .build(),
                        PropertyRBuilder.builder()
                            .name("Salary")
                            .column("salary")
                            .build(),
                        PropertyRBuilder.builder()
                            .name("Education Level")
                            .column("education_level")
                            .build(),
                        PropertyRBuilder.builder()
                            .name("Management Role")
                            .column("management_role")
                            .build()
                    ))
                    .build();

                MappingHierarchy hierarchy = HierarchyRBuilder
                    .builder()
                    .hasAll(true)
                    .allMemberName("All Employees")
                    .primaryKey("employee_id")
                    .relation(new TableR("employee"))
                    .levels(List.of(level))
                    .build();

                MappingCubeDimension dimension = PrivateDimensionRBuilder
                    .builder()
                    .name("EmployeesNonClosure")
                    .foreignKey("employee_id")
                    .hierarchies(List.of(hierarchy))
                    .build();
                result.add(dimension);
            }
            return result;
        }
    }

    public static class ParentChildHierarchyTestModifier5 extends RDbMappingSchemaModifier {

        public ParentChildHierarchyTestModifier5(MappingSchema mappingSchema) {
            super(mappingSchema);
        }
        /*
                "<Dimension name=\"EmployeesNoClosure\" foreignKey=\"employee_id\">\n"
                + "<Hierarchy hasAll=\"true\" allMemberName=\"All Employees\" primaryKey=\"employee_id\">\n"
                + "<Table name=\"employee\"/>\n"
                + "<Level name=\"Employee Id\" uniqueMembers=\"true\" type=\"Numeric\" column=\"employee_id\" nameColumn=\"full_name\" parentColumn=\"supervisor_id\" nullParentValue=\"0\">\n"
                + "<Property name=\"Marital Status\" column=\"marital_status\"/>\n"
                + "<Property name=\"Position Title\" column=\"position_title\"/>\n"
                + "<Property name=\"Gender\" column=\"gender\"/>\n"
                + "<Property name=\"Salary\" column=\"salary\"/>\n"
                + "<Property name=\"Education Level\" column=\"education_level\"/>\n"
                + "<Property name=\"Management Role\" column=\"management_role\"/>\n"
                + "</Level>\n"
                + "</Hierarchy>\n"
                + "</Dimension>\n"));
         */
        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("HR".equals(cube.name())) {
                MappingLevel level = LevelRBuilder
                    .builder()
                    .name("Employee Id")
                    .uniqueMembers(true)
                    .type(TypeEnum.NUMERIC)
                    .column("employee_id")
                    .parentColumn("supervisor_id")
                    .nameColumn("full_name")
                    .nullParentValue("0")
                    .closure(ClosureRBuilder.builder()
                        .parentColumn("supervisor_id")
                        .childColumn("employee_id")
                        .table(new TableR("employee_closure"))
                        .build())
                    .properties(List.of(
                        PropertyRBuilder.builder()
                            .name("Marital Status")
                            .column("marital_status")
                            .build(),
                        PropertyRBuilder.builder()
                            .name("Position Title")
                            .column("position_title")
                            .build(),
                        PropertyRBuilder.builder()
                            .name("Gender")
                            .column("gender")
                            .build(),
                        PropertyRBuilder.builder()
                            .name("Salary")
                            .column("salary")
                            .build(),
                        PropertyRBuilder.builder()
                            .name("Education Level")
                            .column("education_level")
                            .build(),
                        PropertyRBuilder.builder()
                            .name("Management Role")
                            .column("management_role")
                            .build()
                    ))
                    .build();

                MappingHierarchy hierarchy = HierarchyRBuilder
                    .builder()
                    .hasAll(true)
                    .allMemberName("All Employees")
                    .primaryKey("employee_id")
                    .relation(new TableR("employee"))
                    .levels(List.of(level))
                    .build();

                MappingCubeDimension dimension = PrivateDimensionRBuilder
                    .builder()
                    .name("EmployeesNonClosure")
                    .foreignKey("employee_id")
                    .hierarchies(List.of(hierarchy))
                    .build();
                result.add(dimension);
            }
            return result;
        }
    }

    public static class ParentChildHierarchyTestModifier6 extends RDbMappingSchemaModifier {

        public ParentChildHierarchyTestModifier6(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        /*
            "<Cube name='HR-fewer-dims'>\n"
            + "    <Table name='salary'/>\n"
            + "    <Dimension name='Department' foreignKey='department_id'>\n"
            + "        <Hierarchy hasAll='true' primaryKey='department_id'>\n"
            + "            <Table name='department'/>\n"
            + "            <Level name='Department Description' uniqueMembers='true' column='department_id'/>\n"
            + "        </Hierarchy>\n"
            + "    </Dimension>\n"
            + "    <Dimension name='Employees' foreignKey='employee_id'>\n"
            + "        <Hierarchy hasAll='true' allMemberName='All Employees' primaryKey='employee_id'>\n"
            + "            <Table name='employee'/>\n"
            + "            <Level name='Employee Id' type='Numeric' uniqueMembers='true' column='employee_id' parentColumn='supervisor_id' nameColumn='full_name' nullParentValue='0'>\n"
            + "                <Property name='Marital Status' column='marital_status'/>\n"
            + "                <Property name='Position Title' column='position_title'/>\n"
            + "                <Property name='Gender' column='gender'/>\n"
            + "                <Property name='Salary' column='salary'/>\n"
            + "                <Property name='Education Level' column='education_level'/>\n"
            + "                <Property name='Management Role' column='management_role'/>\n"
            + "            </Level>\n"
            + "        </Hierarchy>\n"
            + "    </Dimension>\n"
            + "    <Measure name='Org Salary' column='salary_paid' aggregator='sum' formatString='Currency' />\n"
            + "    <Measure name='Count' column='employee_id' aggregator='count' formatString='#,#'/>\n"
            + "</Cube>",
         */
        @Override
        protected List<MappingCube> schemaCubes(MappingSchema schema) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(schema));
            result.add(CubeRBuilder.builder()
                .name("HR-fewer-dims")
                .fact(new TableR("salary"))
                .dimensionUsageOrDimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Department")
                        .foreignKey("department_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("department_id")
                                .relation(new TableR("department"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Department Description")
                                        .column("department_id")
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        ))
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .name("Employees")
                        .foreignKey("employee_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .allMemberName("All Employees")
                                .primaryKey("employee_id")
                                .relation(new TableR("employee"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Employee Id")
                                        .type(TypeEnum.NUMERIC)
                                        .uniqueMembers(true)
                                        .column("employee_id")
                                        .parentColumn("supervisor_id")
                                        .nameColumn("full_name")
                                        .nullParentValue("0")
                                        .properties(List.of(
                                            PropertyRBuilder.builder()
                                                .name("Marital Status").column("marital_status").build(),
                                            PropertyRBuilder.builder()
                                                .name("Position Title").column("position_title").build(),
                                            PropertyRBuilder.builder()
                                                .name("Gender").column("gender").build(),
                                            PropertyRBuilder.builder()
                                                .name("Salary").column("salary").build(),
                                            PropertyRBuilder.builder()
                                                .name("Education Level").column("education_level").build(),
                                            PropertyRBuilder.builder()
                                                .name("Management Role").column("management_role").build()
                                        ))
                                        .build()
                                ))

                                .build()
                        ))
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Org Salary")
                        .column("salary_paid")
                        .aggregator("sum")
                        .formatString("Currency")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Count")
                        .column("employee_id")
                        .aggregator("count")
                        .formatString("#,#")
                        .build()
                ))
                .build()
            );
            return result;
        }
    }

    public static class ParentChildHierarchyTestModifier7 extends RDbMappingSchemaModifier {

        public ParentChildHierarchyTestModifier7(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        /*
            "<Cube name=\"HR-ordered\">\n"
            + "  <Table name=\"salary\"/>\n"
            + "  <Dimension name=\"Employees\" foreignKey=\"employee_id\">\n"
            + "    <Hierarchy hasAll=\"true\" allMemberName=\"All Employees\"\n"
            + "        primaryKey=\"employee_id\">\n"
            + "      <Table name=\"employee\"/>\n"
            + "      <Level name=\"Employee Id\" type=\"Numeric\" uniqueMembers=\"true\"\n"
            + "          column=\"employee_id\" parentColumn=\"supervisor_id\"\n"
            + "          nameColumn=\"full_name\" nullParentValue=\"0\""
            // Original "HR" cube has no ordinalColumn.
            + "          ordinalColumn=\"last_name\" >\n"
            + "        <Closure parentColumn=\"supervisor_id\" childColumn=\"employee_id\">\n"
            + "          <Table name=\"employee_closure\"/>\n"
            + "        </Closure>\n"
            + "        <Property name=\"First Name\" column=\"first_name\"/>\n"
            + "      </Level>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "\n"
            + "  <Measure name=\"Org Salary\" column=\"salary_paid\" aggregator=\"sum\"\n"
            + "      formatString=\"Currency\"/>\n"
            + "  <Measure name=\"Count\" column=\"employee_id\" aggregator=\"count\"\n"
            + "      formatString=\"#,#\"/>\n"
            + "</Cube>",
         */
        @Override
        protected List<MappingCube> schemaCubes(MappingSchema schema) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(schema));
            result.add(CubeRBuilder.builder()
                .name("HR-ordered")
                .fact(new TableR("salary"))
                .dimensionUsageOrDimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Employees")
                        .foreignKey("employee_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .allMemberName("All Employees")
                                .primaryKey("employee_id")
                                .relation(new TableR("employee"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Employee Id")
                                        .type(TypeEnum.NUMERIC)
                                        .uniqueMembers(true)
                                        .column("employee_id")
                                        .parentColumn("supervisor_id")
                                        .nameColumn("full_name")
                                        .nullParentValue("0")
                                        .ordinalColumn("last_name")
                                        .closure(ClosureRBuilder.builder()
                                            .table(new TableR("employee_closure"))
                                            .build())
                                        .properties(List.of(
                                            PropertyRBuilder.builder()
                                                .name("First Name")
                                                .column("first_name")
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Org Salary")
                        .column("salary_paid")
                        .aggregator("sum")
                        .formatString("Currency")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Count")
                        .column("employee_id")
                        .aggregator("count")
                        .formatString("#,#")
                        .build()
                ))
                .build()
            );
            return result;
        }
    }

    public static class ParentChildHierarchyTestModifier8 extends RDbMappingSchemaModifier {

        public ParentChildHierarchyTestModifier8(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        /*
            "<Cube name=\"CustomSales\">"
            + "   <Table name=\"sales_fact_1997\"/>"
            + "   <DimensionUsage name=\"Employees\" source=\"Employees\" foreignKey=\"time_id\"/>"
            + "   <Measure name=\"Store Sales\" column=\"store_sales\" aggregator=\"sum\"/>"
            + "</Cube>"
            + "<Cube name=\"CustomHR\">"
            + "   <Table name=\"salary\"/>"
            + "   <DimensionUsage name=\"Employees\" source=\"Employees\" foreignKey=\"employee_id\"/>"
            + "   <Measure name=\"Org Salary\" column=\"salary_paid\" aggregator=\"sum\"/>"
            + "</Cube>"
         */
        @Override
        protected List<MappingCube> schemaCubes(MappingSchema schema) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(schema));
            result.add(CubeRBuilder.builder()
                .name("CustomSales")
                .fact(new TableR("sales_fact_1997"))
                .dimensionUsageOrDimensions(List.of(
                    DimensionUsageRBuilder.builder()
                        .name("Employees")
                        .source("Employees")
                        .foreignKey("time_id")
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Store Sales")
                        .column("store_sales")
                        .aggregator("sum")
                        .build()
                ))
                .build()
            );
            result.add(CubeRBuilder.builder()
                .name("CustomHR")
                .fact(new TableR("salary"))
                .dimensionUsageOrDimensions(List.of(
                    DimensionUsageRBuilder.builder()
                        .name("Employees")
                        .source("Employees")
                        .foreignKey("employee_id")
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Org Salary")
                        .column("salary_paid")
                        .aggregator("sum")
                        .build()
                ))
                .build()
            );

            return result;
        }

        /*
            "<Dimension name=\"Employees\" >"
            + "   <Hierarchy hasAll=\"true\" allMemberName=\"All Employees\""
            + "      primaryKey=\"employee_id\" primaryKeyTable=\"employee\">"
            + "      <Table name=\"employee\"/>"
            + "      <Level name=\"Employee Name\" type=\"Numeric\" uniqueMembers=\"true\""
            + "         column=\"employee_id\" parentColumn=\"supervisor_id\""
            + "         nameColumn=\"full_name\" nullParentValue=\"0\">"
            + "         <Closure parentColumn=\"supervisor_id\" childColumn=\"employee_id\">"
            + "            <Table name=\"employee_closure\"/>"
            + "         </Closure>"
            + "      </Level>"
            + "   </Hierarchy>"
            + "</Dimension>",
         */
        @Override
        protected List<MappingPrivateDimension> schemaDimensions(MappingSchema schema) {
            List<MappingPrivateDimension> result = new ArrayList<>();
            result.addAll(super.schemaDimensions(schema));
            result.add(PrivateDimensionRBuilder.builder()
                .name("Employees")
                .hierarchies(List.of(
                    HierarchyRBuilder.builder()
                        .hasAll(true)
                        .allMemberName("All Employees")
                        .primaryKey("employee_id")
                        .primaryKeyTable("employee")
                        .relation(new TableR("employee"))
                        .levels(List.of(
                            LevelRBuilder.builder()
                                .name("Employee Name")
                                .type(TypeEnum.NUMERIC)
                                .uniqueMembers(true)
                                .column("employee_id")
                                .parentColumn("supervisor_id")
                                .nameColumn("full_name")
                                .nullParentValue("0")
                                .closure(ClosureRBuilder.builder()
                                    .parentColumn("supervisor_id")
                                    .childColumn("employee_id")
                                    .table(new TableR("employee_closure"))
                                    .build())
                                .build()
                        ))
                        .build()
                ))
                .build());
            return result;
        }

        /*
            + "<VirtualCube name=\"CustomSalesAndHR\" >"
            + "<VirtualCubeDimension name=\"Employees\"/>"
            + "<VirtualCubeMeasure cubeName=\"CustomSales\" name=\"[Measures].[Store Sales]\"/>"
            + "<VirtualCubeMeasure cubeName=\"CustomHR\" name=\"[Measures].[Org Salary]\"/>"
            + "<CalculatedMember name=\"HR Cost per Sale\" dimension=\"Measures\">"
            + "<Formula>[Measures].[Store Sales] / [Measures].[Org Salary]</Formula>"
            + "</CalculatedMember>"
            + "</VirtualCube>",
         */
        @Override
        protected List<MappingVirtualCube> schemaVirtualCubes(MappingSchema schema) {
            List<MappingVirtualCube> result = new ArrayList<>();
            result.addAll(super.schemaVirtualCubes(schema));
            result.add(VirtualCubeRBuilder.builder()
                .name("CustomSalesAndHR")
                .virtualCubeDimensions(List.of(
                    VirtualCubeDimensionRBuilder.builder()
                        .name("Employees")
                        .build()
                ))
                .virtualCubeMeasures(List.of(
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("CustomSales")
                        .name("[Measures].[Store Sales]")
                        .build(),
                    VirtualCubeMeasureRBuilder.builder()
                        .cubeName("CustomHR")
                        .name("[Measures].[Org Salary]")
                        .build()
                ))
                .calculatedMembers(List.of(
                    CalculatedMemberRBuilder.builder()
                        .name("HR Cost per Sale")
                        .dimension("Measures")
                        .formulaElement(FormulaRBuilder.builder()
                            .cdata("[Measures].[Store Sales] / [Measures].[Org Salary]")
                            .build())
                        .build()
                ))
                .build());
            return result;
        }
    }

    public static class ParentChildHierarchyTestModifier9 extends RDbMappingSchemaModifier {

        public ParentChildHierarchyTestModifier9(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        /*
                "<Cube name=\"HR4C\">\n"
                        + "  <Table name=\"salary\"/>\n"
                        + "  <Dimension name=\"Employees\" foreignKey=\"employee_id\">\n"
                        + "    <Hierarchy hasAll=\"true\" allMemberName=\"All\"\n"
                        + "        primaryKey=\"employee_id\">\n"
                        + "      <Table name=\"employee\"/>\n"
                        + "      <Level name=\"Employee Id\" type=\"Numeric\" uniqueMembers=\"true\"\n"
                        + "          column=\"employee_id\" parentColumn=\"supervisor_id\"\n"
                        + "          nameColumn=\"full_name\" nullParentValue=\"0\">\n";

                "        <Closure parentColumn=\"supervisor_id\" childColumn=\"employee_id\">\n"
                        + "          <Table name=\"employee_closure\"/>\n"
                        + "        </Closure>\n";

                "      </Level>\n"
                        + "    </Hierarchy>\n"
                        + "  </Dimension>\n"
                        + "\n"
                        + "  <Measure name=\"Count\" column=\"employee_id\" aggregator=\"count\" />\n"
                        + "</Cube>\n";
         */
        @Override
        protected List<MappingCube> schemaCubes(MappingSchema schema) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(schema));
            result.add(CubeRBuilder.builder()
                .name("HR4C")
                .fact(new TableR("salary"))
                .dimensionUsageOrDimensions(List.of(


                    PrivateDimensionRBuilder.builder()
                        .name("Employees")
                        .foreignKey("employee_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .allMemberName("All")
                                .primaryKey("employee_id")
                                .relation(new TableR("employee"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Employee Id")
                                        .type(TypeEnum.NUMERIC)
                                        .uniqueMembers(true)
                                        .column("employee_id")
                                        .parentColumn("supervisor_id")
                                        .nameColumn("full_name")
                                        .nullParentValue("0")
                                        .closure(ClosureRBuilder.builder()
                                            .parentColumn("supervisor_id")
                                            .childColumn("employee_id")
                                            .table(new TableR("employee_closure"))
                                            .build())
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Count")
                        .column("employee_id")
                        .aggregator("count")
                        .build()
                ))
                .build()
            );

            return result;
        }

    }

    public static class ParentChildHierarchyTestModifier10 extends RDbMappingSchemaModifier {

        public ParentChildHierarchyTestModifier10(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        /*
                "<Cube name=\"HR4C\">\n"
                        + "  <Table name=\"salary\"/>\n"
                        + "  <Dimension name=\"Employees\" foreignKey=\"employee_id\">\n"
                        + "    <Hierarchy hasAll=\"true\" allMemberName=\"All\"\n"
                        + "        primaryKey=\"employee_id\">\n"
                        + "      <Table name=\"employee\"/>\n"
                        + "      <Level name=\"Employee Id\" type=\"Numeric\" uniqueMembers=\"true\"\n"
                        + "          column=\"employee_id\" parentColumn=\"supervisor_id\"\n"
                        + "          nameColumn=\"full_name\" nullParentValue=\"0\">\n";


                "      </Level>\n"
                        + "    </Hierarchy>\n"
                        + "  </Dimension>\n"
                        + "\n"
                        + "  <Measure name=\"Count\" column=\"employee_id\" aggregator=\"count\" />\n"
                        + "</Cube>\n";
         */
        @Override
        protected List<MappingCube> schemaCubes(MappingSchema schema) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(schema));
            result.add(CubeRBuilder.builder()
                .name("HR4C")
                .fact(new TableR("salary"))
                .dimensionUsageOrDimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Employees")
                        .foreignKey("employee_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .allMemberName("All")
                                .primaryKey("employee_id")
                                .relation(new TableR("employee"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Employee Id")
                                        .type(TypeEnum.NUMERIC)
                                        .uniqueMembers(true)
                                        .column("employee_id")
                                        .parentColumn("supervisor_id")
                                        .nameColumn("full_name")
                                        .nullParentValue("0")
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Count")
                        .column("employee_id")
                        .aggregator("count")
                        .build()
                ))
                .build()
            );

            return result;
        }

    }

    public static class ParentChildHierarchyTestModifier11 extends RDbMappingSchemaModifier {

        public ParentChildHierarchyTestModifier11(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        /*
                    "<Schema name='FoodMart'>\n"
            + "  <Dimension type='StandardDimension' highCardinality='false' name='Employee'>\n"
            + "    <Hierarchy name='Employee' hasAll='false' primaryKey='store_id' primaryKeyTable='bri_store_employee'>\n"
            + "      <Join leftKey='employee_id' rightKey='employee_id'>\n"
            + "        <InlineTable alias='bri_store_employee'>\n"
            + "          <ColumnDefs>\n"
            + "            <ColumnDef name='store_id' type='Integer'/>\n"
            + "            <ColumnDef name='employee_id' type='Integer'/>\n"
            + "          </ColumnDefs>\n"
            + "          <Rows>\n"
            + "            <Row>\n"
            + "              <Value column='store_id'>2</Value>\n"
            + "              <Value column='employee_id'>o</Value>\n"
            + "            </Row>\n"
            + "            <Row>\n"
            + "              <Value column='store_id'>2</Value>\n"
            + "              <Value column='employee_id'>1</Value>\n"
            + "            </Row>\n"
            + "            <Row>\n"
            + "              <Value column='store_id'>2</Value>\n"
            + "              <Value column='employee_id'>2</Value>\n"
            + "            </Row>\n"
            + "            <Row>\n"
            + "              <Value column='store_id'>2</Value>\n"
            + "              <Value column='employee_id'>22</Value>\n"
            + "            </Row>\n"
            + "            <Row>\n"
            + "              <Value column='store_id'>2</Value>\n"
            + "              <Value column='employee_id'>22</Value>\n"
            + "            </Row>\n"
            + "            <Row>\n"
            + "              <Value column='store_id'>2</Value>\n"
            + "              <Value column='employee_id'>32</Value>\n"
            + "            </Row>\n"
            + "            <Row>\n"
            + "              <Value column='store_id'>2</Value>\n"
            + "              <Value column='employee_id'>484</Value>\n"
            + "            </Row>\n"
            + "          </Rows>\n"
            + "        </InlineTable>\n"
            + "        <Table name='employee' alias='employee'/>\n"
            + "      </Join>\n"
            + "      <Level name='Employee' table='employee' column='employee_id' nameColumn='full_name' parentColumn='supervisor_id' nullParentValue='0' type='Integer' uniqueMembers='true' levelType='Regular' hideMemberIf='Never'>\n"
            + "        <Closure parentColumn='supervisor_id' childColumn='employee_id'>\n"
            + "          <Table name='employee_closure'/>\n"
            + "        </Closure>\n"
            + "      </Level>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "  <Cube name='Sales_Bug_441' cache='true' enabled='true'>\n"
            + "    <Table name='sales_fact_1997'/>\n"
            + "    <DimensionUsage source='Employee' name='Employee' foreignKey='store_id' highCardinality='false'/>\n"
            + "    <Measure name='Store Sales' column='store_sales' datatype='Numeric' formatString='#,###.00' aggregator='sum' visible='true'/>\n"
            + "  </Cube>\n"
            + "</Schema>");

         */
        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            MappingInlineTable it = InlineTableRBuilder.builder()
                .alias("bri_store_employee")
                .columnDefs(List.of(
                    ColumnDefRBuilder.builder()
                        .name("store_id")
                        .type(TypeEnum.INTEGER)
                        .build(),
                    ColumnDefRBuilder.builder()
                        .name("employee_id")
                        .type(TypeEnum.INTEGER)
                        .build()
                ))
                .rows(List.of(
                    RowRBuilder.builder()
                        .values(List.of(
                            ValueRBuilder.builder().column("store_id").content("2").build(),
                            ValueRBuilder.builder().column("employee_id").content("o").build()
                        ))
                        .build(),
                    RowRBuilder.builder()
                        .values(List.of(
                            ValueRBuilder.builder().column("store_id").content("2").build(),
                            ValueRBuilder.builder().column("employee_id").content("1").build()
                        ))
                        .build(),
                    RowRBuilder.builder()
                        .values(List.of(
                            ValueRBuilder.builder().column("store_id").content("2").build(),
                            ValueRBuilder.builder().column("employee_id").content("2").build()
                        ))
                        .build(),
                    RowRBuilder.builder()
                        .values(List.of(
                            ValueRBuilder.builder().column("store_id").content("2").build(),
                            ValueRBuilder.builder().column("employee_id").content("22").build()
                        ))
                        .build(),
                    RowRBuilder.builder()
                        .values(List.of(
                            ValueRBuilder.builder().column("store_id").content("2").build(),
                            ValueRBuilder.builder().column("employee_id").content("22").build()
                        ))
                        .build(),
                    RowRBuilder.builder()
                        .values(List.of(
                            ValueRBuilder.builder().column("store_id").content("2").build(),
                            ValueRBuilder.builder().column("employee_id").content("32").build()
                        ))
                        .build(),
                    RowRBuilder.builder()
                        .values(List.of(
                            ValueRBuilder.builder().column("store_id").content("2").build(),
                            ValueRBuilder.builder().column("employee_id").content("484").build()
                        ))
                        .build()

                ))
                .build();
            MappingTable t = new TableR(null, "employee", "employee", List.of());

            return SchemaRBuilder.builder()
                .name("FoodMart")
                .dimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .type(DimensionTypeEnum.STANDARD_DIMENSION)
                        .highCardinality(false)
                        .name("Employee")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .name("Employee")
                                .hasAll(false)
                                .primaryKey("store_id")
                                .primaryKeyTable("bri_store_employee")
                                .relation( new JoinR(
                                    List.of(it, t),
                                    null, "employee_id", null, "employee_id"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Employee")
                                        .table("employee")
                                        .column("employee_id")
                                        .nameColumn("full_name")
                                        .parentColumn("supervisor_id")
                                        .nullParentValue("nullParentValue")
                                        .type(TypeEnum.INTEGER)
                                        .uniqueMembers(true)
                                        .levelType(LevelTypeEnum.REGULAR)
                                        .hideMemberIf(HideMemberIfEnum.NEVER)
                                        .closure(ClosureRBuilder.builder()
                                            .parentColumn("supervisor_id")
                                            .childColumn("employee_id")
                                            .table(new TableR("employee_closure"))
                                            .build())
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Sales_Bug_441")
                        .cache(true)
                        .enabled(true)
                        .fact(new TableR("sales_fact_1997"))
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .source("Employee")
                                .name("Employee")
                                .foreignKey("store_id")
                                .highCardinality(false)
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Store Sales")
                                .column("store_sales")
                                .datatype(MeasureDataTypeEnum.NUMERIC)
                                .formatString("#,###.00")
                                .aggregator("sum")
                                .visible(true)
                                .build()
                        ))
                        .build()
                ))
                .build();
        }
    }

    public static class ValidMeasureFunDefTestModifier extends RDbMappingSchemaModifier {

        public ValidMeasureFunDefTestModifier(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        /*
    "<?xml version=\"1.0\"?>\n"
    + "<Schema name=\"FoodMart\">\n"
    + "  <Dimension name=\"Product\">\n"
    + "    <Hierarchy hasAll=\"true\" primaryKey=\"product_id\" primaryKeyTable=\"product\">\n"
    + "      <Join leftKey=\"product_class_id\" rightKey=\"product_class_id\">\n"
    + "        <Table name=\"product\"/>\n"
    + "        <Table name=\"product_class\"/>\n"
    + "      </Join>\n"
    + "      <Level name=\"Product Name\" table=\"product\" column=\"product_name\" uniqueMembers=\"true\"/>\n"
    + "    </Hierarchy>\t\n"
    + "\t<Hierarchy name=\"BrandOnly\" hasAll=\"true\" primaryKey=\"product_id\" primaryKeyTable=\"product\">\n"
    + "      <Join leftKey=\"product_class_id\" rightKey=\"product_class_id\">\n"
    + "        <Table name=\"product\"/>\n"
    + "        <Table name=\"product_class\"/>\n"
    + "      </Join>\n"
    + "      <Level name=\"Product\" table=\"product\" column=\"brand_name\" uniqueMembers=\"false\"/>\n"
    + "    </Hierarchy>\n"
    + "  </Dimension>\n"
    + "  <Cube name=\"Sales\" defaultMeasure=\"Unit Sales\">\n"
    + "    <Table name=\"sales_fact_1997\"/>\n"
    + "    <DimensionUsage name=\"Product\" source=\"Product\" foreignKey=\"product_id\"/>\n"
    + "  </Cube>\n"
    + "  <Cube name=\"Sales 1\" cache=\"true\" enabled=\"true\">\n"
    + "    <Table name=\"sales_fact_1997\"/>\n"
    + "\t<Measure name=\"Unit Sales1\" column=\"unit_sales\" aggregator=\"sum\"\n"
    + "      formatString=\"Standard\"/>\n" + "  </Cube>  \n"
    + " \n"
    + "  <VirtualCube enabled=\"true\" name=\"Virtual Cube\">\n"
    + "\t<VirtualCubeDimension cubeName=\"Sales\" highCardinality=\"false\" name=\"Product\">\n"
    + "    </VirtualCubeDimension>\n"
    + "    <VirtualCubeMeasure cubeName=\"Sales 1\" name=\"[Measures].[Unit Sales1]\" visible=\"true\">\n"
    + "    </VirtualCubeMeasure>\n"
    + "  </VirtualCube>\n" + "</Schema>";
         */
        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("FoodMart")
                .dimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Product")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("product_id")
                                .primaryKeyTable("product")
                                .relation( new JoinR(
                                    List.of(new TableR("product"), new TableR("product_class")),
                                    null, "product_class_id", null, "product_class_id"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Product Name")
                                        .table("product")
                                        .column("product_name")
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build(),
                            HierarchyRBuilder.builder()
                                .name("BrandOnly")
                                .hasAll(true)
                                .primaryKey("product_id")
                                .primaryKeyTable("product")
                                .relation(new JoinR(
                                    List.of(new TableR("product"), new TableR("product_class")),
                                    null, "product_class_id",
                                    null, "product_class_id"
                                ))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Product")
                                        .table("product")
                                        .column("brand_name")
                                        .uniqueMembers(false)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Sales")
                        .defaultMeasure("Unit Sales")
                        .fact(new TableR("sales_fact_1997"))
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .source("Product")
                                .name("Product")
                                .foreignKey("product_id")
                                .build()
                        ))
                        .build(),
                    CubeRBuilder.builder()
                        .name("Sales 1")
                        .cache(true)
                        .enabled(true)
                        .fact(new TableR("sales_fact_1997"))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales1")
                                .column("unit_sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build()
                        ))
                        .build()
                ))
                .virtualCubes(List.of(
                    VirtualCubeRBuilder.builder()
                        .enabled(true)
                        .name("Virtual Cube")
                        .virtualCubeDimensions(List.of(
                            VirtualCubeDimensionRBuilder.builder()
                                .cubeName("Sales")
                                .highCardinality(false)
                                .name("Product")
                                .build()
                        ))
                        .virtualCubeMeasures(List.of(
                            VirtualCubeMeasureRBuilder.builder()
                                .cubeName("Sales 1")
                                .name("[Measures].[Unit Sales1]")
                                .visible(true)
                                .build()
                        ))
                        .build()
                ))
                .build();
        }
    }

    public static class FunctionTestModifier extends RDbMappingSchemaModifier {

        /*
         "<CalculatedMember "
        + "name='H1 1997' "
        + "formula='Aggregate([Time].[1997].[Q1]:[Time].[1997].[Q2])' "
        + "dimension='Time' />" ));
         */
        public FunctionTestModifier(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCalculatedMember> cubeCalculatedMembers(MappingCube cube) {
            List<MappingCalculatedMember> result = new ArrayList<>();
            result.addAll(super.cubeCalculatedMembers(cube));
            if ("Sales".equals(cube.name())) {
                result.add(CalculatedMemberRBuilder.builder()
                    .name("H1 1997")
                    .formula("Aggregate([Time].[1997].[Q1]:[Time].[1997].[Q2])")
                    .dimension("Time")
                    .build());
            }
            return result;
        }
    }

    public static class FunctionTestModifier2 extends RDbMappingSchemaModifier {

        /*
      "<CalculatedMember "
        + "name='H1 1997' "
        + "formula='Aggregate([Time].[1997].[Q1]:[Time].[1997].[Q2])' "
        + "dimension='Time' />"
        + "<CalculatedMember "
        + "name='Partial' "
        + "formula='Aggregate([Education Level].[Partial College]:[Education Level].[Partial High School])' "
        + "dimension='Education Level' />"));
         */
        public FunctionTestModifier2(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCalculatedMember> cubeCalculatedMembers(MappingCube cube) {
            List<MappingCalculatedMember> result = new ArrayList<>();
            result.addAll(super.cubeCalculatedMembers(cube));
            if ("Sales".equals(cube.name())) {
                result.add(CalculatedMemberRBuilder.builder()
                    .name("H1 1997")
                    .formula("Aggregate([Time].[1997].[Q1]:[Time].[1997].[Q2])")
                    .dimension("Time")
                    .build());
                result.add(CalculatedMemberRBuilder.builder()
                    .name("Partial")
                    .formula("Aggregate([Education Level].[Partial College]:[Education Level].[Partial High School])")
                    .dimension("Education Level")
                    .build());

            }
            return result;
        }
    }

    public static class FunctionTestModifier3 extends RDbMappingSchemaModifier {

        /*
      "<Cube name=\"Sales_Hierarchize\">\n"
        + "  <Table name=\"sales_fact_1997\"/>\n"
        + "  <Dimension name=\"Time_Alphabetical\" type=\"TimeDimension\" foreignKey=\"time_id\">\n"
        + "    <Hierarchy hasAll=\"false\" primaryKey=\"time_id\">\n"
        + "      <Table name=\"time_by_day\"/>\n"
        + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
        + "          levelType=\"TimeYears\"/>\n"
        + "      <Level name=\"Quarter\" column=\"quarter\" uniqueMembers=\"false\"\n"
        + "          levelType=\"TimeQuarters\"/>\n"
        + "      <Level name=\"Month\" column=\"month_of_year\" uniqueMembers=\"false\" type=\"Numeric\"\n"
        + "          ordinalColumn=\"the_month\"\n"
        + "          levelType=\"TimeMonths\"/>\n"
        + "    </Hierarchy>\n"
        + "  </Dimension>\n"
        + "\n"
        + "  <Dimension name=\"Month_Alphabetical\" type=\"TimeDimension\" foreignKey=\"time_id\">\n"
        + "    <Hierarchy hasAll=\"false\" primaryKey=\"time_id\">\n"
        + "      <Table name=\"time_by_day\"/>\n"
        + "      <Level name=\"Month\" column=\"month_of_year\" uniqueMembers=\"false\" type=\"Numeric\"\n"
        + "          ordinalColumn=\"the_month\"\n"
        + "          levelType=\"TimeMonths\"/>\n"
        + "    </Hierarchy>\n"
        + "  </Dimension>\n"
        + "\n"
        + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"\n"
        + "      formatString=\"Standard\"/>\n"
        + "</Cube>"
         */
        public FunctionTestModifier3(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCube> schemaCubes(MappingSchema mappingSchemaOriginal) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(mappingSchemaOriginal));
            result.add(CubeRBuilder.builder()
                .name("Sales_Hierarchize")
                .fact(new TableR("sales_fact_1997"))
                .dimensionUsageOrDimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Time_Alphabetical")
                        .type(DimensionTypeEnum.TIME_DIMENSION)
                        .foreignKey("time_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(false)
                                .primaryKey("time_id")
                                .relation(new TableR("time_by_day"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Year")
                                        .column("the_year")
                                        .type(TypeEnum.NUMERIC)
                                        .uniqueMembers(true)
                                        .levelType(LevelTypeEnum.TIME_YEARS)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Quarter")
                                        .column("quarter")
                                        .uniqueMembers(false)
                                        .levelType(LevelTypeEnum.TIME_QUARTERS)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Month")
                                        .column("month_of_year")
                                        .uniqueMembers(false)
                                        .type(TypeEnum.NUMERIC)
                                        .ordinalColumn("the_month")
                                        .levelType(LevelTypeEnum.TIME_MONTHS)
                                        .build()
                                ))
                                .build()
                        ))
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .name("Month_Alphabetical")
                        .type(DimensionTypeEnum.TIME_DIMENSION)
                        .foreignKey("time_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(false)
                                .primaryKey("time_id")
                                .relation(new TableR("time_by_day"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Month")
                                        .column("month_of_year")
                                        .uniqueMembers(false)
                                        .type(TypeEnum.NUMERIC)
                                        .ordinalColumn("the_month")
                                        .levelType(LevelTypeEnum.TIME_MONTHS)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Unit Sales")
                        .column("unit_sales")
                        .aggregator("sum")
                        .formatString("Standard")
                        .build()
                ))
                .build());


            return result;
        }
    }

    public static class FilterTestModifier extends RDbMappingSchemaModifier {

        /*
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
        public FilterTestModifier(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Store".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Store Type")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .name("Store Types Hierarchy")
                            .allMemberName("All Store Types Member Name")
                            .hasAll(true)
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Store Type")
                                    .column("store_type")
                                    .uniqueMembers(true)
                                    .build()
                            ))
                            .build()
                    ))
                    .build());
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Store")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .primaryKey("store_id")
                            .relation(new TableR("store"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Store Country")
                                    .column("store_country")
                                    .uniqueMembers(true)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Store State")
                                    .column("store_state")
                                    .uniqueMembers(true)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Store City")
                                    .column("store_city")
                                    .uniqueMembers(false)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Store Name")
                                    .column("store_id")
                                    .type(TypeEnum.NUMERIC)
                                    .nameColumn("store_name")
                                    .uniqueMembers(false)
                                    .build()
                            ))
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class MemberCacheControlTestModifier extends RDbMappingSchemaModifier {

        /*
            "  <Dimension name=\"Retail\" foreignKey=\"store_id\">\n"
            + "    <Hierarchy hasAll=\"true\" primaryKey=\"store_id\">\n"
            + "      <Table name=\"store\"/>\n"
            + "      <Level name=\"State\" column=\"store_state\" uniqueMembers=\"true\">\n"
            + "        <Property name=\"Country\" column=\"store_country\"/>\n"
            + "      </Level>\n"
            + "      <Level name=\"City\" column=\"store_city\" uniqueMembers=\"true\">\n"
            + "        <Property name=\"Population\" column=\"store_postal_code\"/>\n"
            + "      </Level>\n"
            + "      <Level name=\"Name\" column=\"store_name\" uniqueMembers=\"true\">\n"
            + "        <Property name=\"Store Type\" column=\"store_type\"/>\n"
            + "        <Property name=\"Store Manager\" column=\"store_manager\"/>\n"
            + "        <Property name=\"Store Sqft\" column=\"store_sqft\" type=\"Numeric\"/>\n"
            + "        <Property name=\"Has coffee bar\" column=\"coffee_bar\" type=\"Boolean\"/>\n"
            + "        <Property name=\"Street address\" column=\"store_street_address\" type=\"String\"/>\n"
            + "      </Level>\n"
            + "    </Hierarchy>\n"
            + "   </Dimension>"));
         */
        public MemberCacheControlTestModifier(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Retail")
                    .foreignKey("store_id")
                        .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .primaryKey("store_id")
                            .relation(new TableR("store"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("State")
                                    .column("store_state")
                                    .uniqueMembers(true)
                                    .properties(List.of(
                                        PropertyRBuilder.builder()
                                            .name("Country")
                                            .column("store_country")
                                            .build()
                                    ))
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("City")
                                    .column("store_city")
                                    .uniqueMembers(true)
                                    .properties(List.of(
                                        PropertyRBuilder.builder()
                                            .name("Population")
                                            .column("store_postal_code")
                                            .build()
                                    ))
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Name")
                                    .column("store_name")
                                    .uniqueMembers(true)
                                    .properties(List.of(
                                        PropertyRBuilder.builder()
                                            .name("Store Type")
                                            .column("store_type")
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("Store Manager")
                                            .column("store_manager")
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("Store Sqft")
                                            .column("store_sqft")
                                            .type(PropertyTypeEnum.NUMERIC)
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("Has coffee bar")
                                            .column("coffee_bar")
                                            .type(PropertyTypeEnum.BOOLEAN)
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("Street address")
                                            .column("store_street_address")
                                            .type(PropertyTypeEnum.STRING)
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class NonEmptyTestModifier extends RDbMappingSchemaModifier {

        /*
      "<Dimension name=\"Product Ragged\" foreignKey=\"product_id\">\n"
        + "  <Hierarchy hasAll=\"false\" primaryKey=\"product_id\">\n"
        + "    <Table name=\"product\"/>\n"
        + "    <Level name=\"Brand Name\" table=\"product\" column=\"brand_name\" uniqueMembers=\"false\"/>\n"
        + "    <Level name=\"Product Name\" table=\"product\" column=\"product_name\" uniqueMembers=\"true\"\n"
        + "        hideMemberIf=\"IfBlankName\""
        + "        />\n"
        + "  </Hierarchy>\n"
        + "</Dimension>" ) );
         */
        public NonEmptyTestModifier(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Product Ragged")
                    .foreignKey("product_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(false)
                            .primaryKey("product_id")
                            .relation(new TableR("product"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Brand Name")
                                    .table("product")
                                    .column("brand_name")
                                    .uniqueMembers(false)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Product Name")
                                    .table("product")
                                    .column("product_name")
                                    .uniqueMembers(true)
                                    .hideMemberIf(HideMemberIfEnum.IF_BLANK_NAME)
                                    .build()
                            ))
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class NonEmptyTestModifier2 extends RDbMappingSchemaModifier {

        /*
              "<Dimension name=\"Product Ragged\" foreignKey=\"product_id\">\n"
        + "  <Hierarchy hasAll=\"true\" primaryKey=\"product_id\">\n"
        + "    <Table name=\"product\"/>\n"
        + "    <Level name=\"Brand Name\" table=\"product\" column=\"brand_name\" uniqueMembers=\"false\"/>\n"
        + "    <Level name=\"Product Name\" table=\"product\" column=\"product_name\" uniqueMembers=\"true\"\n"
        + "        hideMemberIf=\"IfBlankName\""
        + "        />\n"
        + "  </Hierarchy>\n"
        + "</Dimension>" ) );

         */
        public NonEmptyTestModifier2(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Product Ragged")
                    .foreignKey("product_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .primaryKey("product_id")
                            .relation(new TableR("product"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Brand Name")
                                    .table("product")
                                    .column("brand_name")
                                    .uniqueMembers(false)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Product Name")
                                    .table("product")
                                    .column("product_name")
                                    .uniqueMembers(true)
                                    .hideMemberIf(HideMemberIfEnum.IF_BLANK_NAME)
                                    .build()
                            ))
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class NonEmptyTestModifier3 extends RDbMappingSchemaModifier {

        /*
      "<Dimension name=\"Product Ragged\" foreignKey=\"product_id\">\n"
        + "  <Hierarchy hasAll=\"true\" primaryKey=\"product_id\">\n"
        + "    <Table name=\"product\"/>\n"
        + "    <Level name=\"Brand Name\" table=\"product\" column=\"brand_name\" uniqueMembers=\"false\""
        + "        hideMemberIf=\"IfBlankName\""
        + "        />\n"
        + "    <Level name=\"Product Name\" table=\"product\" column=\"product_name\"\n uniqueMembers=\"true\"/>\n"
        + "  </Hierarchy>\n"
        + "</Dimension>" ) );
         */
        public NonEmptyTestModifier3(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Product Ragged")
                    .foreignKey("product_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .primaryKey("product_id")
                            .relation(new TableR("product"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Brand Name")
                                    .table("product")
                                    .column("brand_name")
                                    .uniqueMembers(false)
                                    .hideMemberIf(HideMemberIfEnum.IF_BLANK_NAME)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Product Name")
                                    .table("product")
                                    .column("product_name")
                                    .uniqueMembers(true)
                                    .build()
                            ))
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class NonEmptyTestModifier4 extends RDbMappingSchemaModifier {

        /*
      "  <Dimension name=\"Time\" type=\"TimeDimension\" foreignKey=\"time_id\">\n"
        + "    <Hierarchy hasAll=\"false\" primaryKey=\"time_id\" defaultMember=\"[Time].[1997].[Q1].[1]\" >\n"
        + "      <Table name=\"time_by_day\"/>\n"
        + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
        + "          levelType=\"TimeYears\"/>\n"
        + "      <Level name=\"Quarter\" column=\"quarter\" uniqueMembers=\"false\"\n"
        + "          levelType=\"TimeQuarters\"/>\n"
        + "      <Level name=\"Month\" column=\"month_of_year\" uniqueMembers=\"false\" type=\"Numeric\"\n"
        + "          levelType=\"TimeMonths\"/>\n"
        + "    </Hierarchy>\n"
        + "  </Dimension>" ));
         */
        public NonEmptyTestModifier4(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Time")
                    .type(DimensionTypeEnum.TIME_DIMENSION)
                    .foreignKey("time_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(false)
                            .primaryKey("time_id")
                            .defaultMember("[Time].[1997].[Q1].[1]")
                            .relation(new TableR("time_by_day"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Year")
                                    .column("the_year")
                                    .type(TypeEnum.NUMERIC)
                                    .uniqueMembers(true)
                                    .levelType(LevelTypeEnum.TIME_YEARS)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Quarter")
                                    .column("quarter")
                                    .uniqueMembers(true)
                                    .levelType(LevelTypeEnum.TIME_QUARTERS)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Month")
                                    .column("month_of_year")
                                    .uniqueMembers(false)
                                    .type(TypeEnum.NUMERIC)
                                    .levelType(LevelTypeEnum.TIME_MONTHS)
                                    .build()
                            ))
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }
    public static class NonEmptyTestModifier5 extends RDbMappingSchemaModifier {

        /*
      "  <Dimension name=\"Store2\"  foreignKey=\"store_id\" >\n"
        + "    <Hierarchy hasAll=\"false\" primaryKey=\"store_id\"  defaultMember='[Store2].[USA].[OR]'>\n"
        + "      <Table name=\"store\"/>\n"
        + "      <Level name=\"Store Country\" column=\"store_country\"  uniqueMembers=\"true\"\n"
        + "          />\n"
        + "      <Level name=\"Store State\" column=\"store_state\" uniqueMembers=\"true\"\n"
        + "         />\n"
        + "      <Level name=\"Store City\" column=\"store_city\" uniqueMembers=\"false\" />\n"
        + "    </Hierarchy>\n"
        + "  </Dimension>" ));
         */
        public NonEmptyTestModifier5(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Store2")
                    .foreignKey("store_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(false)
                            .primaryKey("store_id")
                            .defaultMember("[Store2].[USA].[OR]")
                            .relation(new TableR("store"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Store Country")
                                    .column("store_country")
                                    .uniqueMembers(true)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Store State")
                                    .column("store_state")
                                    .uniqueMembers(true)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Store City")
                                    .column("store_city")
                                    .uniqueMembers(false)
                                    .build()
                            ))
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class NonEmptyTestModifier6 extends RDbMappingSchemaModifier {

        /*
              "<?xml version=\"1.0\"?>\n"
        + "<Schema name=\"custom\">\n"
        + "  <Dimension name=\"Store\">\n"
        + "    <Hierarchy hasAll=\"true\" primaryKey=\"store_id\">\n"
        + "      <Table name=\"store\"/>\n"
        + "      <Level name=\"Store Country\" column=\"store_country\" uniqueMembers=\"true\"/>\n"
        + "      <Level name=\"Store State\" column=\"store_state\" uniqueMembers=\"true\"/>\n"
        + "      <Level name=\"Store City\" column=\"store_city\" uniqueMembers=\"false\"/>\n"
        + "      <Level name=\"Store Name\" column=\"store_name\" uniqueMembers=\"true\">\n"
        + "      </Level>\n"
        + "    </Hierarchy>\n"
        + "  </Dimension>\n"
        + "  <Dimension name=\"Time\" type=\"TimeDimension\">\n"
        + "    <Hierarchy hasAll=\"true\" primaryKey=\"time_id\">\n"
        + "      <Table name=\"time_by_day\"/>\n"
        + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
        + "          levelType=\"TimeYears\"/>\n"
        + "      <Level name=\"Quarter\" column=\"quarter\" uniqueMembers=\"false\"\n"
        + "          levelType=\"TimeQuarters\"/>\n"
        + "      <Level name=\"Month\" column=\"month_of_year\" uniqueMembers=\"false\" type=\"Numeric\"\n"
        + "          levelType=\"TimeMonths\"/>\n"
        + "    </Hierarchy>\n"
        + "  </Dimension>\n"
        + "  <Cube name=\"Sales1\" defaultMeasure=\"Unit Sales\">\n"
        + "    <Table name=\"sales_fact_1997\">\n"
        + "        <AggExclude name=\"agg_c_special_sales_fact_1997\" />"
        + "    </Table>\n"
        + "    <DimensionUsage name=\"Store\" source=\"Store\" foreignKey=\"store_id\"/>\n"
        + "    <DimensionUsage name=\"Time\" source=\"Time\" foreignKey=\"time_id\"/>\n"
        + "    <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"\n"
        + "      formatString=\"Standard\"/>\n"
        + "    <Measure name=\"Store Cost\" column=\"store_cost\" aggregator=\"sum\"\n"
        + "      formatString=\"#,###.00\"/>\n"
        + "    <Measure name=\"Store Sales\" column=\"store_sales\" aggregator=\"sum\"\n"
        + "      formatString=\"#,###.00\"/>\n"
        + "  </Cube>\n"
        + "<Role name=\"Role1\">\n"
        + "  <SchemaGrant access=\"none\">\n"
        + "    <CubeGrant cube=\"Sales1\" access=\"all\">\n"
        + "      <HierarchyGrant hierarchy=\"[Time]\" access=\"custom\" rollupPolicy=\"partial\">\n"
        + "        <MemberGrant member=\"[Time].[Year].[1997]\" access=\"all\"/>\n"
        + "      </HierarchyGrant>\n"
        + "    </CubeGrant>\n"
        + "  </SchemaGrant>\n"
        + "</Role> \n"
        + "</Schema>\n";
         */
        public NonEmptyTestModifier6(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("custom")
                .dimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Store")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("store_id")
                                .relation(new TableR("store"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Store Country").column("store_country").uniqueMembers(true)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Store State").column("store_state").uniqueMembers(true)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Store City").column("store_city").uniqueMembers(false)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Store Name").column("store_name").uniqueMembers(true)
                                        .build()

                                ))
                                .build()
                        ))
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .name("Time")
                        .type(DimensionTypeEnum.TIME_DIMENSION)
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("time_id")
                                .relation(new TableR("time_by_day"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Year")
                                        .column("the_year")
                                        .type(TypeEnum.NUMERIC)
                                        .uniqueMembers(true)
                                        .levelType(LevelTypeEnum.TIME_YEARS)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Quarter")
                                        .column("quarter")
                                        .uniqueMembers(false)
                                        .levelType(LevelTypeEnum.TIME_QUARTERS)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Month")
                                        .column("month_of_year")
                                        .uniqueMembers(false)
                                        .type(TypeEnum.NUMERIC)
                                        .levelType(LevelTypeEnum.TIME_MONTHS)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Sales1")
                        .defaultMeasure("Unit Sales")
                        .fact(new TableR("sales_fact_1997",
                            List.of(AggExcludeRBuilder.builder().name("agg_c_special_sales_fact_1997").build()),
                            List.of()))
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .name("Store")
                                .source("Store")
                                .foreignKey("store_id")
                                .build(),
                            DimensionUsageRBuilder.builder()
                                .name("Time")
                                .source("Time")
                                .foreignKey("time_id")
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales")
                                .column("unit_sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Store Cost")
                                .column("store_cost")
                                .aggregator("sum")
                                .formatString("#,###.00")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Store Sales")
                                .column("store_sales")
                                .aggregator("sum")
                                .formatString("#,###.00")
                                .build()
                        ))
                        .build()
                ))
                .roles(List.of(
                    RoleRBuilder.builder()
                        .name("Role1")
                        .schemaGrants(List.of(
                            SchemaGrantRBuilder.builder()
                                .access(AccessEnum.NONE)
                                .cubeGrants(List.of(
                                    CubeGrantRBuilder.builder()
                                        .cube("Sales1")
                                        .access("all")
                                        .hierarchyGrants(List.of(
                                            HierarchyGrantRBuilder.builder()
                                                .hierarchy("[Time]")
                                                .access(AccessEnum.CUSTOM)
                                                .rollupPolicy("partial")
                                                .memberGrants(List.of(
                                                    MemberGrantRBuilder.builder()
                                                        .member("[Time].[Year].[1997]")
                                                        .access(MemberGrantAccessEnum.ALL)
                                                        .build()
                                                ))
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .build();

        }
    }

    public static class NonEmptyTestModifier7 extends RDbMappingSchemaModifier {

        /*
        "<Schema name=\"FoodMart\">"
          + "  <Dimension name=\"Store\">"
          + "    <Hierarchy hasAll=\"true\" primaryKey=\"store_id\">"
          + "      <Table name=\"store\" />"
          + "      <Level name=\"Store Country\" column=\"store_country\" uniqueMembers=\"true\" />"
          + "      <Level name=\"Store State\" column=\"store_state\" uniqueMembers=\"true\" />"
          + "    </Hierarchy>"
          + "  </Dimension>"
          + "  <Dimension name=\"Time\" type=\"TimeDimension\">\n"
          + "    <Hierarchy hasAll=\"false\" primaryKey=\"time_id\">\n"
          + "      <Table name=\"time_by_day\"/>\n"
          + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
          + "          levelType=\"TimeYears\"/>\n"
          + "      <Level name=\"Quarter\" column=\"quarter\" uniqueMembers=\"false\"\n"
          + "          levelType=\"TimeQuarters\"/>\n"
          + "    </Hierarchy>\n"
          + "    </Dimension>"
          + "  <Cube name=\"Sales\" defaultMeasure=\"Unit Sales\">"
          + "    <Table name=\"sales_fact_1997\" />"
          + "    <DimensionUsage name=\"Store\" source=\"Store\" foreignKey=\"store_id\" />"
          + "    <DimensionUsage name=\"Time\" source=\"Time\" foreignKey=\"time_id\" />"
          + "    <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\" formatString=\"Standard\" />"
          + "    <CalculatedMember name=\"dummyMeasure\" dimension=\"Measures\">"
          + "      <Formula>[Measures].[Unit Sales]</Formula>"
          + "    </CalculatedMember>"
          + "    <CalculatedMember name=\"dummyMeasure2\" dimension=\"Measures\">"
          + "      <Formula>[Measures].[dummyMeasure]</Formula>"
          + "    </CalculatedMember>"
          + "  </Cube>"
          + "  <VirtualCube defaultMeasure=\"dummyMeasure\" name=\"virtual\">"
          + "    <VirtualCubeDimension name=\"Store\" />"
          + "    <VirtualCubeDimension name=\"Time\" />"
          + "    <VirtualCubeMeasure name=\"[Measures].[dummyMeasure2]\" cubeName=\"Sales\" />"
          + "  </VirtualCube>"
          + "</Schema>" );
         */
        public NonEmptyTestModifier7(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("FoodMart")
                .dimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Store")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("store_id")
                                .relation(new TableR("store"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Store Country").column("store_country").uniqueMembers(true)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Store State").column("store_state").uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        ))
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .name("Time")
                        .type(DimensionTypeEnum.TIME_DIMENSION)
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(false)
                                .primaryKey("time_id")
                                .relation(new TableR("time_by_day"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Year")
                                        .column("the_year")
                                        .type(TypeEnum.NUMERIC)
                                        .uniqueMembers(true)
                                        .levelType(LevelTypeEnum.TIME_YEARS)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Quarter")
                                        .column("quarter")
                                        .uniqueMembers(false)
                                        .levelType(LevelTypeEnum.TIME_QUARTERS)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Sales")
                        .defaultMeasure("Unit Sales")
                        .fact(new TableR("sales_fact_1997"))
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .name("Store")
                                .source("Store")
                                .foreignKey("store_id")
                                .build(),
                            DimensionUsageRBuilder.builder()
                                .name("Time")
                                .source("Time")
                                .foreignKey("time_id")
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales")
                                .column("unit_sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build()
                        ))
                        .calculatedMembers(List.of(
                            CalculatedMemberRBuilder.builder()
                                .name("dummyMeasure")
                                .dimension("Measures")
                                .formulaElement(FormulaRBuilder.builder()
                                    .cdata("[Measures].[Unit Sales]")
                                    .build())
                                .build(),
                            CalculatedMemberRBuilder.builder()
                                .name("dummyMeasure2")
                                .dimension("Measures")
                                .formulaElement(FormulaRBuilder.builder()
                                    .cdata("[Measures].[dummyMeasure]")
                                    .build())
                                .build()
                        ))
                        .build()
                ))
                .virtualCubes(List.of(
                    VirtualCubeRBuilder.builder()
                        .defaultMeasure("dummyMeasure")
                        .name("virtual")
                        .virtualCubeDimensions(List.of(
                            VirtualCubeDimensionRBuilder.builder()
                                .name("Store")
                                .build(),
                            VirtualCubeDimensionRBuilder.builder()
                                .name("Time")
                                .build()
                        ))
                        .virtualCubeMeasures(List.of(
                            VirtualCubeMeasureRBuilder.builder()
                                .name("[Measures].[dummyMeasure2]")
                                .cubeName("Sales")
                                .build()
                        ))
                        .build()
                ))
                .build();

        }
    }

    public static class BasicQueryTestModifier1 extends RDbMappingSchemaModifier {

        /*
            "<Dimension name=\"Gender2\" foreignKey=\"customer_id\">\n"
                + "  <Hierarchy hasAll=\"true\" allMemberName=\"All Gender\" primaryKey=\"customer_id\">\n"
                + "    <View alias=\"gender2\">\n" + "      <SQL dialect=\"generic\">\n"
                + "        <![CDATA[SELECT * FROM customer]]>\n" + "      </SQL>\n"
                + "      <SQL dialect=\"oracle\">\n" + "        <![CDATA[SELECT * FROM \"customer\"]]>\n"
                + "      </SQL>\n" + "      <SQL dialect=\"hsqldb\">\n"
                + "        <![CDATA[SELECT * FROM \"customer\"]]>\n" + "      </SQL>\n"
                + "      <SQL dialect=\"derby\">\n" + "        <![CDATA[SELECT * FROM \"customer\"]]>\n"
                + "      </SQL>\n" + "      <SQL dialect=\"luciddb\">\n"
                + "        <![CDATA[SELECT * FROM \"customer\"]]>\n" + "      </SQL>\n"
                + "      <SQL dialect=\"db2\">\n" + "        <![CDATA[SELECT * FROM \"customer\"]]>\n"
                + "      </SQL>\n" + "      <SQL dialect=\"neoview\">\n"
                + "        <![CDATA[SELECT * FROM \"customer\"]]>\n" + "      </SQL>\n"
                + "      <SQL dialect=\"netezza\">\n" + "        <![CDATA[SELECT * FROM \"customer\"]]>\n"
                + "      </SQL>\n" + "      <SQL dialect=\"snowflake\">\n"
                + "        <![CDATA[SELECT * FROM \"customer\"]]>\n" + "      </SQL>\n" + "    </View>\n"
                + "    <Level name=\"Gender\" column=\"gender\" uniqueMembers=\"true\"/>\n" + "  </Hierarchy>\n"
                + "</Dimension>", null ));
         */
        public BasicQueryTestModifier1(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                MappingView v = ViewRBuilder.builder()
                    .alias("gender2")
                    .sqls(List.of(
                        SQLRBuilder.builder().dialect("generic").content("SELECT * FROM customer").build(),
                        SQLRBuilder.builder().dialect("oracle").content("SELECT * FROM \"customer\"").build(),
                        SQLRBuilder.builder().dialect("hsqldb").content("SELECT * FROM \"customer\"").build(),
                        SQLRBuilder.builder().dialect("derby").content("SELECT * FROM \"customer\"").build(),
                        SQLRBuilder.builder().dialect("luciddb").content("SELECT * FROM \"customer\"").build(),
                        SQLRBuilder.builder().dialect("db2").content("SELECT * FROM \"customer\"").build(),
                        SQLRBuilder.builder().dialect("neoview").content("SELECT * FROM \"customer\"").build(),
                        SQLRBuilder.builder().dialect("netezza").content("SELECT * FROM \"customer\"").build(),
                        SQLRBuilder.builder().dialect("snowflake").content("SELECT * FROM \"customer\"").build()
                    ))
                    .build();
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Gender2")
                    .foreignKey("customer_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .allMemberName("All Gender")
                            .primaryKey("customer_id")
                            .relation(v)
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Gender")
                                    .column("gender")
                                    .uniqueMembers(true)
                                    .build()
                            ))
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class BasicQueryTestModifier2 extends RDbMappingSchemaModifier {

        /*
            "   <Dimension name=\"ProdAmbiguousLevelName\" foreignKey=\"product_id\">\n"
                + "    <Hierarchy hasAll=\"true\" primaryKey=\"product_id\" primaryKeyTable=\"product\">\n"
                + "      <Join leftKey=\"product_class_id\" rightKey=\"product_class_id\">\n"
                + "        <Table name=\"product\"/>\n" + "        <Table name=\"product_class\"/>\n"
                + "      </Join>\n" + "\n"
                + "      <Level name=\"Drink\" table=\"product_class\" column=\"product_family\"\n"
                + "          uniqueMembers=\"true\"/>\n"
                + "      <Level name=\"Beverages\" table=\"product_class\" column=\"product_department\"\n"
                + "          uniqueMembers=\"false\"/>\n"
                + "      <Level name=\"Product Category\" table=\"product_class\" column=\"product_category\"\n"
                + "          uniqueMembers=\"false\"/>\n" + "    </Hierarchy>\n" + "  </Dimension>\n", null ));
         */
        public BasicQueryTestModifier2(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("ProdAmbiguousLevelName")
                    .foreignKey("product_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .primaryKey("product_id")
                            .primaryKeyTable("product")
                            .relation(new JoinR(List.of(new TableR("product"), new TableR("product_class")),
                                null, "product_class_id", null, "product_class_id"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Drink")
                                    .table("product_class")
                                    .column("product_family")
                                    .uniqueMembers(true)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Beverages")
                                    .table("product_class")
                                    .column("product_department")
                                    .uniqueMembers(false)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Product Category")
                                    .table("product_class")
                                    .column("product_category")
                                    .uniqueMembers(false)
                                    .build()
                            ))
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class BasicQueryTestModifier3 extends RDbMappingSchemaModifier {

        /*
            "<Dimension name=\"ProductView\" foreignKey=\"product_id\">\n"
                + "   <Hierarchy hasAll=\"true\" primaryKey=\"product_id\" primaryKeyTable=\"productView\">\n"
                + "       <View alias=\"productView\">\n" + "           <SQL dialect=\"db2\"><![CDATA[\n"
                + "SELECT *\n" + "FROM \"product\", \"product_class\"\n"
                + "WHERE \"product\".\"product_class_id\" = \"product_class\".\"product_class_id\"\n" + "]]>\n"
                + "           </SQL>\n" + "           <SQL dialect=\"mssql\"><![CDATA[\n"
                + "SELECT \"product\".\"product_id\",\n" + "\"product\".\"brand_name\",\n"
                + "\"product\".\"product_name\",\n" + "\"product\".\"SKU\",\n" + "\"product\".\"SRP\",\n"
                + "\"product\".\"gross_weight\",\n" + "\"product\".\"net_weight\",\n"
                + "\"product\".\"recyclable_package\",\n" + "\"product\".\"low_fat\",\n"
                + "\"product\".\"units_per_case\",\n" + "\"product\".\"cases_per_pallet\",\n"
                + "\"product\".\"shelf_width\",\n" + "\"product\".\"shelf_height\",\n"
                + "\"product\".\"shelf_depth\",\n" + "\"product_class\".\"product_class_id\",\n"
                + "\"product_class\".\"product_subcategory\",\n" + "\"product_class\".\"product_category\",\n"
                + "\"product_class\".\"product_department\",\n" + "\"product_class\".\"product_family\"\n"
                + "FROM \"product\" inner join \"product_class\"\n"
                + "ON \"product\".\"product_class_id\" = \"product_class\".\"product_class_id\"\n" + "]]>\n"
                + "           </SQL>\n" + "           <SQL dialect=\"mysql\"><![CDATA[\n"
                + "SELECT `product`.`product_id`,\n" + "`product`.`brand_name`,\n" + "`product`.`product_name`,\n"
                + "`product`.`SKU`,\n" + "`product`.`SRP`,\n" + "`product`.`gross_weight`,\n"
                + "`product`.`net_weight`,\n" + "`product`.`recyclable_package`,\n" + "`product`.`low_fat`,\n"
                + "`product`.`units_per_case`,\n" + "`product`.`cases_per_pallet`,\n" + "`product`.`shelf_width`,\n"
                + "`product`.`shelf_height`,\n" + "`product`.`shelf_depth`,\n"
                + "`product_class`.`product_class_id`,\n" + "`product_class`.`product_family`,\n"
                + "`product_class`.`product_department`,\n" + "`product_class`.`product_category`,\n"
                + "`product_class`.`product_subcategory` \n" + "FROM `product`, `product_class`\n"
                + "WHERE `product`.`product_class_id` = `product_class`.`product_class_id`\n" + "]]>\n"
                + "           </SQL>\n" + "           <SQL dialect=\"generic\"><![CDATA[\n" + "SELECT *\n"
                + "FROM \"product\", \"product_class\"\n"
                + "WHERE \"product\".\"product_class_id\" = \"product_class\".\"product_class_id\"\n" + "]]>\n"
                + "           </SQL>\n" + "       </View>\n"
                + "       <Level name=\"Product Family\" column=\"product_family\" uniqueMembers=\"true\"/>\n"
                + "       <Level name=\"Product Department\" column=\"product_department\" uniqueMembers=\"false\"/>\n"
                + "       <Level name=\"Product Category\" column=\"product_category\" uniqueMembers=\"false\"/>\n"
                + "       <Level name=\"Product Subcategory\" column=\"product_subcategory\" uniqueMembers=\"false\"/>\n"
                + "       <Level name=\"Brand Name\" column=\"brand_name\" uniqueMembers=\"false\"/>\n"
                + "       <Level name=\"Product Name\" column=\"product_name\" uniqueMembers=\"true\"/>\n"
                + "   </Hierarchy>\n" + "</Dimension>" ));
         */
        public BasicQueryTestModifier3(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                MappingView v = ViewRBuilder.builder()
                    .alias("productView")
                    .sqls(List.of(
                        SQLRBuilder.builder().dialect("db2")
                            .content(
                                "SELECT * FROM \"product\", \"product_class\" WHERE \"product\".\"product_class_id\" = \"product_class\".\"product_class_id\""
                            )
                            .build(),
                        SQLRBuilder.builder().dialect("mssql")
                            .content(
                                "SELECT \"product\".\"product_id\",\n" + "\"product\".\"brand_name\",\n"
                                    + "\"product\".\"product_name\",\n" + "\"product\".\"SKU\",\n" + "\"product\".\"SRP\",\n"
                                    + "\"product\".\"gross_weight\",\n" + "\"product\".\"net_weight\",\n"
                                    + "\"product\".\"recyclable_package\",\n" + "\"product\".\"low_fat\",\n"
                                    + "\"product\".\"units_per_case\",\n" + "\"product\".\"cases_per_pallet\",\n"
                                    + "\"product\".\"shelf_width\",\n" + "\"product\".\"shelf_height\",\n"
                                    + "\"product\".\"shelf_depth\",\n" + "\"product_class\".\"product_class_id\",\n"
                                    + "\"product_class\".\"product_subcategory\",\n" + "\"product_class\".\"product_category\",\n"
                                    + "\"product_class\".\"product_department\",\n" + "\"product_class\".\"product_family\"\n"
                                    + "FROM \"product\" inner join \"product_class\"\n"
                                    + "ON \"product\".\"product_class_id\" = \"product_class\".\"product_class_id\"\n"
                            )
                            .build(),
                        SQLRBuilder.builder().dialect("mysql")
                            .content(
                                "SELECT `product`.`product_id`,\n" + "`product`.`brand_name`,\n" + "`product`.`product_name`,\n"
                                    + "`product`.`SKU`,\n" + "`product`.`SRP`,\n" + "`product`.`gross_weight`,\n"
                                    + "`product`.`net_weight`,\n" + "`product`.`recyclable_package`,\n" + "`product`.`low_fat`,\n"
                                    + "`product`.`units_per_case`,\n" + "`product`.`cases_per_pallet`,\n" + "`product`.`shelf_width`,\n"
                                    + "`product`.`shelf_height`,\n" + "`product`.`shelf_depth`,\n"
                                    + "`product_class`.`product_class_id`,\n" + "`product_class`.`product_family`,\n"
                                    + "`product_class`.`product_department`,\n" + "`product_class`.`product_category`,\n"
                                    + "`product_class`.`product_subcategory` \n" + "FROM `product`, `product_class`\n"
                                    + "WHERE `product`.`product_class_id` = `product_class`.`product_class_id`\n"
                            )
                            .build(),
                        SQLRBuilder.builder().dialect("db2")
                            .content(
                                "SELECT *\n"
                                    + "FROM \"product\", \"product_class\"\n"
                                    + "WHERE \"product\".\"product_class_id\" = \"product_class\".\"product_class_id\"\n"
                            )
                            .build()
                    ))
                    .build();

                result.add(PrivateDimensionRBuilder.builder()
                    .name("ProductView")
                    .foreignKey("product_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .primaryKey("product_id")
                            .primaryKeyTable("View")
                            .relation(v)
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Product Family")
                                    .column("product_family")
                                    .uniqueMembers(true)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Product Department")
                                    .column("product_department")
                                    .uniqueMembers(false)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Product Category")
                                    .column("product_category")
                                    .uniqueMembers(false)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Product Subcategory")
                                    .column("product_subcategory")
                                    .uniqueMembers(false)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Brand Name")
                                    .column("brand_name")
                                    .uniqueMembers(false)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Product Name")
                                    .column("product_name")
                                    .uniqueMembers(false)
                                    .build()

                            ))
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class BasicQueryTestModifier4 extends RDbMappingSchemaModifier {

        /*
            <DimensionUsage name="Other Store" source="Store" foreignKey="unit_sales" />
         */
        public BasicQueryTestModifier4(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(DimensionUsageRBuilder.builder()
                    .name("Other Store")
                    .source("Other Store")
                    .foreignKey("unit_sales")
                    .build());
            }
            return result;
        }
    }

    public static class BasicQueryTestModifier5 extends RDbMappingSchemaModifier {

        /*
                        "<Dimension name=\"Gender3\" foreignKey=\"customer_id\">\n"
                + "  <Hierarchy hasAll=\"true\" allMemberName=\"All Gender\"\n"
                + " allMemberCaption=\"Frauen und Maenner\" primaryKey=\"customer_id\">\n"
                + "  <Table name=\"customer\"/>\n"
                + "    <Level name=\"Gender\" column=\"gender\" uniqueMembers=\"true\"/>\n" + "  </Hierarchy>\n"
                + "</Dimension>" ));

         */
        public BasicQueryTestModifier5(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Gender3")
                    .foreignKey("customer_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .allMemberName("All Gender")
                            .allMemberCaption("Frauen und Maenner")
                            .primaryKey("customer_id")
                            .relation(new TableR("customer"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Gender")
                                    .column("gender")
                                    .uniqueMembers(true)
                                    .build()
                            ))
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class BasicQueryTestModifier6 extends RDbMappingSchemaModifier {

        /*
            "<Dimension name=\"Position2608\" foreignKey=\"employee_id\">\n"
                + " <Hierarchy hasAll=\"true\" allMemberName=\"All Position\"\n"
                + "        primaryKey=\"employee_id\">\n" + "   <Table name=\"employee\"/>\n"
                + "   <Level name=\"Management Role\" uniqueMembers=\"true\"\n"
                + "          column=\"management_role\"/>\n"
                + "   <Level name=\"Position Title\" uniqueMembers=\"false\"\n"
                + "          column=\"position_title\" ordinalColumn=\"position_id\"/>\n" + " </Hierarchy>\n"
                + "</Dimension>" ));
         */
        public BasicQueryTestModifier6(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("HR".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Position2608")
                    .foreignKey("employee_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .allMemberName("All Position")
                            .allMemberCaption("Frauen und Maenner")
                            .primaryKey("employee_id")
                            .relation(new TableR("employee"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Management Role")
                                    .column("management_role")
                                    .uniqueMembers(true)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Position Title")
                                    .uniqueMembers(false)
                                    .column("position_title")
                                    .ordinalColumn("position_id")
                                    .build()
                            ))
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class BasicQueryTestModifier7 extends RDbMappingSchemaModifier {

        /*
            "<Measure name='zero' aggregator='sum'>\n"
            + " <MeasureExpression>\n" + " <SQL dialect='generic'>\n" + " NULL" + " </SQL>"
            + " <SQL dialect='vertica'>\n" + " NULL::FLOAT" + " </SQL>" + "</MeasureExpression></Measure>"
        */
        public BasicQueryTestModifier7(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingMeasure> cubeMeasures(MappingCube cube) {
            List<MappingMeasure> result = new ArrayList<>();
            result.addAll(super.cubeMeasures(cube));
            if ("Sales".equals(cube.name())) {
                result.add(MeasureRBuilder.builder()
                	.name("zero")
                	.aggregator("sum")
                    .measureExpression(ExpressionViewRBuilder.builder()
                        .sqls(List.of(
                        	SQLRBuilder.builder()
                                .dialect("generic")
                                .content(" NULL ")
                                .build(),
                            SQLRBuilder.builder()
                                .dialect("vertica")
                                .content(" NULL::FLOAT ")
                                .build()
                        ))
                        .build())
                    .build());
            }
            return result;
        }
    }

    public static class BasicQueryTestModifier8 extends RDbMappingSchemaModifier {

        /*
            "<Dimension foreignKey=\"product_id\" type=\"StandardDimension\" visible=\"true\" highCardinality=\"false\" "
                + "name=\"Example\">\n"
                + "  <Hierarchy name=\"Example Hierarchy\" visible=\"true\" hasAll=\"true\" allMemberName=\"All\" "
                + "allMemberCaption=\"All\" primaryKey=\"product_id\" primaryKeyTable=\"product\">\n"
                + "    <Join leftKey=\"product_class_id\" rightKey=\"product_class_id\">\n"
                + "      <Table name=\"product\">\n" + "      </Table>\n" + "         <Table name=\"product_class\">\n"
                + "      </Table>\n" + "    </Join>\n"
                + "    <Level name=\"IsZero\" visible=\"true\" table=\"product\" column=\"product_id\" type=\"Integer\" "
                + "uniqueMembers=\"false\" levelType=\"Regular\" hideMemberIf=\"Never\">\n"
                + "      <NameExpression>\n" + "        <SQL dialect=\"generic\">\n" + "          <![CDATA[case when "
                + dialect.quoteIdentifier( "product", "product_id" ) + "=0 then 'Zero' else 'Non-Zero' end]]>\n"
                + "        </SQL>\n" + "      </NameExpression>\n" + "    </Level>\n"
                + "    <Level name=\"SubCat\" visible=\"true\" table=\"product_class\" column=\"product_class_id\" "
                + "type=\"String\" uniqueMembers=\"false\" levelType=\"Regular\" hideMemberIf=\"Never\">\n"
                + "      <NameExpression>\n" + "        <SQL dialect=\"generic\">\n" + "          <![CDATA[" + dialect
                    .quoteIdentifier( "product_class", "product_subcategory" ) + "]]>\n" + "        </SQL>\n"
                + "      </NameExpression>\n" + "    </Level>\n"
                + "    <Level name=\"ProductName\" visible=\"true\" table=\"product\" column=\"product_id\" "
                + "type=\"Integer\" uniqueMembers=\"false\" levelType=\"Regular\" hideMemberIf=\"Never\">\n"
                + "      <NameExpression>\n" + "        <SQL dialect=\"generic\">\n" + "          <![CDATA[" + dialect
                    .quoteIdentifier( "product", "product_name" ) + "]]>\n" + "        </SQL>\n"
                + "      </NameExpression>\n" + "    </Level>\n" + "  </Hierarchy>\n" + "</Dimension>\n", null, null,
         */
        private Dialect dialect;
        public BasicQueryTestModifier8(MappingSchema mappingSchema, Dialect dialect) {
            super(mappingSchema);
            this.dialect = dialect;
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .foreignKey("product_id")
                    .type(DimensionTypeEnum.STANDARD_DIMENSION)
                    .visible(true)
                    .highCardinality(false)
                    .name("Example")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .allMemberName("Example Hierarchy")
                            .visible(true)
                            .hasAll(true)
                            .allMemberName("All")
                            .allMemberCaption("All")
                            .primaryKey("product_id")
                            .primaryKeyTable("product")
                            .relation(new JoinR(List.of(new TableR("product"), new TableR("product_class")),
                                null, "product_class_id", null, "product_class_id"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("IsZero")
                                    .visible(true)
                                    .table("product")
                                    .column("product_id")
                                    .type(TypeEnum.INTEGER)
                                    .uniqueMembers(false)
                                    .levelType(LevelTypeEnum.REGULAR)
                                    .hideMemberIf(HideMemberIfEnum.NEVER)
                                    .nameExpression(ExpressionViewRBuilder.builder()
                                        .sqls(List.of(
                                            SQLRBuilder.builder()
                                                .dialect("generic")
                                                .content("case when " + dialect.quoteIdentifier( "product", "product_id" ) + "=0 then 'Zero' else 'Non-Zero' end")
                                                .build()
                                        ))
                                        .build())
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("SubCat")
                                    .visible(true)
                                    .table("product_class")
                                    .column("product_class_id")
                                    .type(TypeEnum.STRING)
                                    .uniqueMembers(false)
                                    .levelType(LevelTypeEnum.REGULAR)
                                    .hideMemberIf(HideMemberIfEnum.NEVER)
                                    .nameExpression(ExpressionViewRBuilder.builder()
                                        .sqls(List.of(
                                            SQLRBuilder.builder()
                                                .dialect("generic")
                                                .content(dialect.quoteIdentifier( "product_class", "product_subcategory" ))
                                                .build()
                                        ))
                                        .build())
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("ProductName")
                                    .visible(true)
                                    .table("product")
                                    .column("product_id")
                                    .type(TypeEnum.INTEGER)
                                    .uniqueMembers(false)
                                    .levelType(LevelTypeEnum.REGULAR)
                                    .hideMemberIf(HideMemberIfEnum.NEVER)
                                    .nameExpression(ExpressionViewRBuilder.builder()
                                        .sqls(List.of(
                                            SQLRBuilder.builder()
                                                .dialect("generic")
                                                .content(dialect.quoteIdentifier( "product", "product_name" ))
                                                .build()
                                        ))
                                        .build())
                                    .build()
                                ))
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class BasicQueryTestModifier9 extends RDbMappingSchemaModifier {

        /*
            "<CalculatedMember dimension=\"Gender\" visible=\"true\" name=\"last\">"
                + "<Formula>([Gender].LastChild)</Formula>" + "</CalculatedMember>" ));
        */
        public BasicQueryTestModifier9(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCalculatedMember> cubeCalculatedMembers(MappingCube cube) {
            List<MappingCalculatedMember> result = new ArrayList<>();
            result.addAll(super.cubeCalculatedMembers(cube));
            if ("Sales".equals(cube.name())) {
                result.add(CalculatedMemberRBuilder.builder()
                    .dimension("Gender")
                    .visible(true)
                    .name("last")
                    .formulaElement(FormulaRBuilder.builder()
                        .cdata("([Gender].LastChild)")
                        .build())
                    .build());
            }
            return result;
        }
    }

    public static class BasicQueryTestModifier10 extends RDbMappingSchemaModifier {

        /*
            "<Dimension name=\"Gender4\" foreignKey=\"customer_id\">\n"
                + "  <Hierarchy hasAll=\"true\" allMemberName=\"All Gender\"\n"
                + " allLevelName=\"GenderLevel\" primaryKey=\"customer_id\">\n" + "  <Table name=\"customer\"/>\n"
                + "    <Level name=\"Gender\" column=\"gender\" uniqueMembers=\"true\"/>\n" + "  </Hierarchy>\n"
                + "</Dimension>" ));
         */
        public BasicQueryTestModifier10(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Gender4")
                    .foreignKey("customer_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .allMemberName("All Gender")
                            .allLevelName("GenderLevel")
                            .primaryKey("customer_id")
                            .relation(new TableR("customer"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Gender")
                                    .column("gender")
                                    .uniqueMembers(true)
                                    .build()
                            ))
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class BasicQueryTestModifier11 extends RDbMappingSchemaModifier {

        /*
            "  <Dimension name=\"Customer_2\" foreignKey=\"customer_id\">\n" + "    <Hierarchy hasAll=\"true\" "
                + "allMemberName=\"All Customers\" " + "primaryKey=\"customer_id\" " + " >\n"
                + "      <Table name=\"customer\"/>\n"
                + "      <Level name=\"Name1\" column=\"customer_id\" uniqueMembers=\"true\"/>"
                + "      <Level name=\"Name2\" column=\"customer_id\" uniqueMembers=\"true\"/>\n"
                + "    </Hierarchy>\n" + "  </Dimension>" ));
         */
        public BasicQueryTestModifier11(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Customer_2")
                    .foreignKey("customer_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .allMemberName("All Customers")
                            .primaryKey("customer_id")
                            .relation(new TableR("customer"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Name1")
                                    .column("customer_id")
                                    .uniqueMembers(true)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Name2")
                                    .column("customer_id")
                                    .uniqueMembers(true)
                                    .build()
                            ))
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class BasicQueryTestModifier12 extends RDbMappingSchemaModifier {

        /*
            "<Measure name='zero' aggregator='sum'>\n"
            + "  <MeasureExpression>\n" + "  <SQL dialect='generic'>\n" + "    0"
            + "  </SQL></MeasureExpression></Measure>", null, null ));        */
        public BasicQueryTestModifier12(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingMeasure> cubeMeasures(MappingCube cube) {
            List<MappingMeasure> result = new ArrayList<>();
            result.addAll(super.cubeMeasures(cube));
            if ("Sales".equals(cube.name())) {
                result.add(MeasureRBuilder.builder()
                    .name("zero")
                    .aggregator("sum")
                    .measureExpression(ExpressionViewRBuilder.builder()
                        .sqls(List.of(
                            SQLRBuilder.builder()
                                .dialect("generic")
                                .content("0")
                                .build()
                        ))
                        .build())
                    .build());
            }
            return result;
        }
    }

    public static class BasicQueryTestModifier14 extends RDbMappingSchemaModifier {

        /*
        "" + "<?xml version=\"1.0\"?>\n" + "<Schema name=\"FoodMart 2442\">\n"
            + "<Cube name=\"Sales\" defaultMeasure=\"Unit Sales\">\n" + "  <Table name=\"sales_fact_1997\">\n"
            + "    <AggName name=\"agg_c_special_sales_fact_1997\">\n"
            + "        <AggFactCount column=\"FACT_COUNT\"/>\n" + "        <AggIgnoreColumn column=\"foo\"/>\n"
            + "        <AggIgnoreColumn column=\"bar\"/>\n"
            + "        <AggMeasure name=\"[Measures].[Unit Sales]\" column=\"UNIT_SALES_SUM\" />\n"
            + "        <AggLevel name=\"[Time].[Year]\" column=\"TIME_YEAR\" />\n"
            + "        <AggLevel name=\"[Time].[Quarter]\" column=\"TIME_QUARTER\" />\n" + "    </AggName>\n"
            + "  </Table>\n"

            + " <Dimension name=\"Time\"" + " type=\"TimeDimension\" foreignKey=\"time_id\">\n"
            + "   <Hierarchy hasAll=\"false\" primaryKey=\"time_id\">\n" + "      <Table name=\"time_by_day\"/>\n"
            + "      <Level name=\"Year\" \n"

            // column and nameColumn are the same
            + "         column=\"the_year\" nameColumn=\"the_year\" ordinalColumn=\"the_year\"\n"
            + "         type=\"Numeric\" uniqueMembers=\"true\" levelType=\"TimeYears\"/>\n"
            + "      <Level name=\"Quarter\" \n" + "         column=\"quarter\" ordinalColumn=\"quarter\"\n"
            + "         uniqueMembers=\"false\" levelType=\"TimeQuarters\"/>\n" + "    </Hierarchy>\n"
            + "  </Dimension>\n"

            + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"Standard\"/>\n" + "</Cube>\n" + "</Schema>";
            */
        public BasicQueryTestModifier14(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("FoodMart 2442")
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Sales")
                        .defaultMeasure("Unit Sales")
                        .fact(new TableR("sales_fact_1997",
                            List.of(),
                            List.of(
                                AggNameRBuilder.builder()
                                    .name("agg_c_special_sales_fact_1997")
                                    .aggFactCount(AggColumnNameRBuilder.builder()
                                        .column("FACT_COUNT")
                                        .build())
                                    .aggIgnoreColumns(List.of(
                                        AggColumnNameRBuilder.builder()
                                            .column("foo")
                                            .build(),
                                        AggColumnNameRBuilder.builder()
                                            .column("bar")
                                            .build()
                                    ))
                                    .aggMeasures(List.of(
                                        AggMeasureRBuilder.builder()
                                            .name("[Measures].[Unit Sales]")
                                            .column("UNIT_SALES_SUM")
                                            .build()
                                    ))
                                    .aggLevels(List.of(
                                        AggLevelRBuilder.builder()
                                            .name("[Time].[Year]")
                                            .column("TIME_YEAR")
                                            .build(),
                                        AggLevelRBuilder.builder()
                                            .name("[Time].[Quarter]")
                                            .column("TIME_QUARTER")
                                            .build()
                                    ))
                                    .build()
                            )))
                        .dimensionUsageOrDimensions(List.of(
                            PrivateDimensionRBuilder.builder()
                                .name("Time")
                                .type(DimensionTypeEnum.TIME_DIMENSION)
                                .foreignKey("time_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(false)
                                        .primaryKey("time_id")
                                        .relation(new TableR("time_by_day"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Year")
                                                .column("the_year")
                                                .nameColumn("the_year")
                                                .ordinalColumn("the_year")
                                                .type(TypeEnum.NUMERIC)
                                                .uniqueMembers(true)
                                                .levelType(LevelTypeEnum.TIME_YEARS)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Quarter")
                                                .column("quarter")
                                                .ordinalColumn("quarter")
                                                .uniqueMembers(false)
                                                .levelType(LevelTypeEnum.TIME_QUARTERS)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales")
                                .column("unit_sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build()
                        ))
                        .build()
                ))
                .build();
        }
    }

    public static class BasicQueryTestModifier15 extends RDbMappingSchemaModifier {

        /*
        "" + "<?xml version=\"1.0\"?>\n" + "<Schema name=\"FoodMart 2285\">\n"
            + "<Cube name=\"Sales\" defaultMeasure=\"Unit Sales\">\n" + "  <Table name=\"sales_fact_1997\">\n"
            + "     <AggExclude name=\"agg_c_special_sales_fact_1997\" />" + "  </Table>\n"
            + "  <Dimension name=\"Product\" foreignKey=\"product_id\">\n"
            + "     <Hierarchy hasAll=\"true\" primaryKey=\"product_id\" primaryKeyTable=\"product\">\n"
            + "         <Join leftKey=\"product_class_id\" rightKey=\"product_class_id\">\n"
            + "             <Table name=\"product\"/>\n" + "             <Table name=\"product_class\"/>\n"
            + "         </Join>\t  \n"
            + "         <Level name=\"Product Subcategory\" table=\"product_class\" column=\"product_class_id\"\n"
            + "             uniqueMembers=\"false\"/>\n" + "     </Hierarchy>\n" + "  </Dimension>\n"
            + "  <Dimension name=\"Time\" type=\"TimeDimension\" foreignKey=\"time_id\">\n"
            + "     <Hierarchy hasAll=\"false\" primaryKey=\"time_id\">\n" + "         <Table name=\"time_by_day\"/>\n"
            + "         <Level name=\"Month Upper\" column=\"month_of_year\" nameColumn=\"the_month\" "
            + "             uniqueMembers=\"false\" type=\"Numeric\" levelType=\"TimeMonths\"/>"
            + "         <Level name=\"Month\" column=\"month_of_year\" nameColumn=\"the_month\" "
            + "             uniqueMembers=\"false\" type=\"Numeric\" levelType=\"TimeMonths\"/>\n"
            + "    </Hierarchy>\n" + "  </Dimension>\n"
            + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\" "
            + "     formatString=\"Standard\"/>\n" + "</Cube>\n" + "</Schema>";
            */
        public BasicQueryTestModifier15(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("FoodMart 2285")
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Sales")
                        .defaultMeasure("Unit Sales")
                        .fact(new TableR("sales_fact_1997",
                            List.of(AggExcludeRBuilder.builder()
                                .name("agg_c_special_sales_fact_1997")
                                .build()),
                            List.of()))
                        .dimensionUsageOrDimensions(List.of(
                            PrivateDimensionRBuilder.builder()
                                .name("Product")
                                .foreignKey("product_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(true)
                                        .primaryKey("product_id")
                                        .primaryKeyTable("product")
                                        .relation(new JoinR(List.of(new TableR("product"), new TableR("product_class")),
                                            null, "product_class_id", null, "product_class_id"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Product Subcategory")
                                                .table("product_class")
                                                .column("product_class_id")
                                                .uniqueMembers(false)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build(),
                            PrivateDimensionRBuilder.builder()
                                .name("Time")
                                .type(DimensionTypeEnum.TIME_DIMENSION)
                                .foreignKey("time_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(false)
                                        .primaryKey("time_id")
                                        .relation(new TableR("time_by_day"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Month Upper")
                                                .column("month_of_year")
                                                .nameColumn("the_month")
                                                .uniqueMembers(false)
                                                .type(TypeEnum.NUMERIC)
                                                .levelType(LevelTypeEnum.TIME_MONTHS)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Month")
                                                .column("month_of_year")
                                                .nameColumn("the_month")
                                                .uniqueMembers(false)
                                                .type(TypeEnum.NUMERIC)
                                                .levelType(LevelTypeEnum.TIME_MONTHS)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales")
                                .column("unit_sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build()
                        ))
                        .build()
                ))
                .build();
        }
    }

    public static class BasicQueryTestModifier16 extends RDbMappingSchemaModifier {

        /*
        "" + "<?xml version=\"1.0\"?>\n" + "<Schema name=\"tiny\">\n"
            + "  <Dimension name=\"Time\" type=\"TimeDimension\">\n"
            + "    <Hierarchy hasAll=\"false\" primaryKey=\"time_id\">\n" + "      <Table name=\"time_by_day\" />\n"
            + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\" "
            + "levelType=\"TimeYears\" />\n"
            + "      <Level name=\"Quarter\" uniqueMembers=\"false\" levelType=\"TimeQuarters\" >\n"
            + "        <KeyExpression><SQL>RTRIM(quarter)</SQL></KeyExpression>\n" + "      </Level>\n"
            + "    </Hierarchy>\n" + "  </Dimension>\n" + "

            <Dimension name=\"Product\">\n"
            + "    <Hierarchy hasAll=\"true\" primaryKey=\"product_id\" primaryKeyTable=\"product\">\n"
            + "      <Join leftKey=\"product_class_id\" rightKey=\"product_class_id\">\n"
            + "        <Table name=\"product\"/>\n" + "        <Table name=\"product_class\"/>\n" + "      </Join>\n"
            + "      <Level name=\"Product Family\" table=\"product_class\" column=\"product_family\" "
            + "uniqueMembers=\"true\" />\n" + "    </Hierarchy>\n" + "  </Dimension>\n"

            + "  <Dimension name=\"Warehouse\">\n" + "    <Hierarchy hasAll=\"true\" primaryKey=\"warehouse_id\">\n"
            + "      <Table name=\"warehouse\"/>\n"
            + "      <Level name=\"Country\" column=\"warehouse_country\" uniqueMembers=\"true\"/>\n"
            + "      <Level name=\"State Province\" column=\"warehouse_state_province\"\n"
            + "          uniqueMembers=\"true\"/>\n"
            + "      <Level name=\"City\" column=\"warehouse_city\" uniqueMembers=\"false\"/>\n"
            + "      <Level name=\"Warehouse Name\" column=\"warehouse_name\" uniqueMembers=\"true\"/>\n"
            + "    </Hierarchy>\n" + "  </Dimension>\n" + "


            <Cube name=\"Sales\">\n"
            + "    <Table name=\"sales_fact_1997\" />\n"
            + "    <DimensionUsage name=\"Time\" source=\"Time\" foreignKey=\"time_id\" />\n"
            + "    <DimensionUsage name=\"Product\" source=\"Product\" foreignKey=\"product_id\" />\n"
            + "    <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\" formatString=\"Standard\" />\n"
            + "  </Cube>\n" + "
            <Cube name=\"Warehouse\">\n" + "    <Table name=\"inventory_fact_1997\" />\n"
            + "    <DimensionUsage name=\"Time\" source=\"Time\" foreignKey=\"time_id\" />\n"
            + "    <DimensionUsage name=\"Product\" source=\"Product\" foreignKey=\"product_id\" />\n"
            + "    <DimensionUsage name=\"Warehouse\" source=\"Warehouse\" foreignKey=\"warehouse_id\"/>\n"
            + "    <Measure name=\"Warehouse Sales\" column=\"warehouse_sales\" aggregator=\"sum\" "
            + "formatString=\"Standard\" />\n"
            + "    <CalculatedMember name=\"Warehouse Sales Calc\" dimension=\"Measures\">\n"
            + "      <Formula>[Measures].[Warehouse Sales]</Formula>\n" + "    </CalculatedMember>\n" + "  </Cube>\n"

            + "  <VirtualCube name=\"Warehouse and Sales\">\n" + "    <VirtualCubeDimension name=\"Time\" />\n"
            + "    <VirtualCubeDimension name=\"Product\" />\n"
            + "    <VirtualCubeDimension cubeName=\"Warehouse\" name=\"Warehouse\"/>\n"
            + "    <VirtualCubeMeasure cubeName=\"Sales\" name=\"[Measures].[Unit Sales]\" />\n"
            + "    <VirtualCubeMeasure cubeName=\"Warehouse\" name=\"[Measures].[Warehouse Sales Calc]\" />\n"
            + "  </VirtualCube>\n" + "</Schema>\n";

            */
        public BasicQueryTestModifier16(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("tiny")
                .dimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Time")
                        .type(DimensionTypeEnum.TIME_DIMENSION)
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(false)
                                .primaryKey("time_id")
                                .relation(new TableR("time_by_day"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Year")
                                        .column("the_year")
                                        .type(TypeEnum.NUMERIC)
                                        .uniqueMembers(true)
                                        .levelType(LevelTypeEnum.TIME_YEARS)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Quarter")
                                        .uniqueMembers(false)
                                        .levelType(LevelTypeEnum.TIME_QUARTERS)
                                        .keyExpression(ExpressionViewRBuilder.builder()
                                            .sqls(List.of(
                                                SQLRBuilder.builder()
                                                    .content("RTRIM(quarter)")
                                                    .build()
                                            ))
                                            .build())
                                        .build()
                                ))
                                .build()
                        ))
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .name("Product")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("product_id")
                                .primaryKeyTable("product")
                                .relation(new JoinR(List.of(
                                    new TableR("product"),
                                    new TableR("product_class")),
                                    null, "product_class_id",
                                    null, "product_class_id"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Product Family")
                                        .table("product_class")
                                        .column("product_family")
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        ))
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .name("Warehouse")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("warehouse_id")
                                .relation(new TableR("warehouse"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Country")
                                        .column("warehouse_country")
                                        .uniqueMembers(true)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("State Province")
                                        .column("warehouse_state_province")
                                        .uniqueMembers(true)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("City")
                                        .column("warehouse_city")
                                        .uniqueMembers(false)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Warehouse Name")
                                        .column("warehouse_name")
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()

                ))
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Sales")
                        .fact(new TableR("sales_fact_1997"))
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .name("Time")
                                .source("Time")
                                .foreignKey("time_id")
                                .build(),
                            DimensionUsageRBuilder.builder()
                                .name("Product")
                                .source("Product")
                                .foreignKey("product_id")
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales")
                                .column("unit_sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build()
                        ))
                        .build(),
                    CubeRBuilder.builder()
                        .name("Warehouse")
                        .fact(new TableR("inventory_fact_1997"))
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .name("Time").source("Time").foreignKey("time_id")
                                .build(),
                            DimensionUsageRBuilder.builder()
                                .name("Product").source("Product").foreignKey("product_id")
                                .build(),
                            DimensionUsageRBuilder.builder()
                                .name("Warehouse").source("Warehouse").foreignKey("warehouse_id")
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Warehouse Sales")
                                .column("warehouse_sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build()
                        ))
                        .calculatedMembers(List.of(
                            CalculatedMemberRBuilder.builder()
                                .name("Warehouse Sales Calc")
                                .dimension("Measures")
                                .formulaElement(
                                    FormulaRBuilder.builder()
                                        .cdata("[Measures].[Warehouse Sales]")
                                    .build())
                                .build()
                        ))
                        .build()
                ))
                .virtualCubes(List.of(
                    VirtualCubeRBuilder.builder()
                        .name("Warehouse and Sales")
                        .virtualCubeDimensions(List.of(
                            VirtualCubeDimensionRBuilder.builder()
                                .name("Time")
                                .build(),
                            VirtualCubeDimensionRBuilder.builder()
                                .name("Product")
                                .build(),
                            VirtualCubeDimensionRBuilder.builder()
                                .name("Warehouse")
                                .build()
                        ))
                        .virtualCubeMeasures(List.of(
                            VirtualCubeMeasureRBuilder.builder()
                                .cubeName("Sales")
                                .name("[Measures].[Unit Sales]")
                                .build(),
                            VirtualCubeMeasureRBuilder.builder()
                                .cubeName("Warehouse")
                                .name("[Measures].[Warehouse Sales Calc]")
                                .build()
                        ))
                        .build()
                ))
                .build();
        }
    }

    public static class BasicQueryTestModifier17 extends RDbMappingSchemaModifier {

        /*
        "" + "<?xml version=\"1.0\"?>\n" + "<Schema name=\"FoodMart 2399 Rollup Type\">\n"
            + "<Cube name=\"Sales\" defaultMeasure=\"Unit Sales\">\n" + "  <Table name=\"sales_fact_1997\">\n"
            + "<AggExclude name=\"agg_c_14_sales_fact_1997\" />\n"
            + "<AggExclude name=\"agg_g_ms_pcat_sales_fact_1997\" />\n"
            + "    <AggExclude name=\"agg_l_03_sales_fact_1997\" />\n"
            + "<AggExclude name=\"agg_l_04_sales_fact_1997\" />\n"
            + "<AggExclude name=\"agg_l_05_sales_fact_1997\" />\n"
            + "<AggExclude name=\"agg_lc_06_sales_fact_1997\" />\n"
            + "<AggExclude name=\"agg_lc_100_sales_fact_1997\" />\n"
            + "    <AggExclude name=\"agg_ll_01_sales_fact_1997\" />\n"
            + "    <AggExclude name=\"agg_pl_01_sales_fact_1997\" />\n"
            + "<AggExclude name=\"agg_c_special_sales_fact_1997\" />\n"
            + "    <AggName name=\"agg_c_10_sales_fact_1997\">\n" + "        <AggFactCount column=\"FACT_COUNT\"/>\n"
            + "        <AggMeasure name=\"[Measures].[Unit Sales]\" column=\"UNIT_SALES\" rollupType=\"AvgFromSum\" />\n"
            + "        <AggLevel name=\"[Time].[Year]\" column=\"THE_YEAR\" />\n"
            + "        <AggLevel name=\"[Time].[Quarter]\" column=\"QUARTER\" />\n" + "    </AggName>\n"
            + "  </Table>\n" + "  <Dimension name=\"Time\" type=\"TimeDimension\" foreignKey=\"time_id\">\n"
            + "    <Hierarchy hasAll=\"false\" primaryKey=\"time_id\">\n" + "      <Table name=\"time_by_day\"/>\n"
            + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
            + "          levelType=\"TimeYears\"/>\n"
            + "      <Level name=\"Quarter\" column=\"quarter\" uniqueMembers=\"false\"\n"
            + "          levelType=\"TimeQuarters\"/>\n" + "    </Hierarchy>\n" + "  </Dimension>\n"
            + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"avg\" />\n" + "</Cube>\n"
            + "</Schema>";
            */
        public BasicQueryTestModifier17(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("FoodMart 2399 Rollup Type")
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Sales")
                        .defaultMeasure("Unit Sales")
                        .fact(new TableR("sales_fact_1997",
                            List.of(
                                AggExcludeRBuilder.builder().name("agg_c_14_sales_fact_1997").build(),
                                AggExcludeRBuilder.builder().name("agg_g_ms_pcat_sales_fact_1997").build(),
                                AggExcludeRBuilder.builder().name("agg_l_03_sales_fact_1997").build(),
                                AggExcludeRBuilder.builder().name("agg_l_04_sales_fact_1997").build(),
                                AggExcludeRBuilder.builder().name("agg_l_05_sales_fact_1997").build(),
                                AggExcludeRBuilder.builder().name("agg_lc_06_sales_fact_1997").build(),
                                AggExcludeRBuilder.builder().name("agg_lc_100_sales_fact_1997").build(),
                                AggExcludeRBuilder.builder().name("agg_ll_01_sales_fact_1997").build(),
                                AggExcludeRBuilder.builder().name("agg_pl_01_sales_fact_1997").build(),
                                AggExcludeRBuilder.builder().name("agg_c_special_sales_fact_1997").build()
                            ),
                            List.of(
                                AggNameRBuilder.builder()
                                    .name("agg_c_10_sales_fact_1997")
                                    .aggFactCount(AggColumnNameRBuilder.builder()
                                        .column("FACT_COUNT")
                                        .build())
                                    .aggMeasures(List.of(
                                        AggMeasureRBuilder.builder()
                                            .name("[Measures].[Unit Sales]")
                                            .column("UNIT_SALES")
                                            .rollupType("AvgFromSum")
                                            .build()
                                    ))
                                    .aggLevels(List.of(
                                        AggLevelRBuilder.builder()
                                            .name("[Time].[Year]")
                                            .column("THE_YEAR")
                                            .build(),
                                        AggLevelRBuilder.builder()
                                            .name("[Time].[Quarter]")
                                            .column("QUARTER")
                                            .build()
                                    ))
                                    .build())
                            )
                        )
                        .dimensionUsageOrDimensions(List.of(
                            PrivateDimensionRBuilder.builder()
                                .name("Time")
                                .type(DimensionTypeEnum.TIME_DIMENSION)
                                .foreignKey("time_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(false)
                                        .primaryKey("time_id")
                                        .relation(new TableR("time_by_day"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Year")
                                                .column("the_year")
                                                .type(TypeEnum.NUMERIC)
                                                .uniqueMembers(true)
                                                .levelType(LevelTypeEnum.TIME_YEARS)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Quarter")
                                                .column("quarter")
                                                .uniqueMembers(false)
                                                .levelType(LevelTypeEnum.TIME_QUARTERS)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales")
                                .column("unit_sales")
                                .aggregator("sum")
                                .build()
                        )).build()
                ))
                .build();
        }

    }

    public static class BasicQueryTestModifier18 extends RDbMappingSchemaModifier {

        /*
        "" + "<?xml version=\"1.0\"?>\n" + "<Schema name=\"FoodMart 2399 Rollup Type\">\n"
            + "<Cube name=\"Sales\" defaultMeasure=\"Unit Sales\">\n" + "  <Table name=\"sales_fact_1997\">\n"
            + "<AggExclude name=\"agg_c_14_sales_fact_1997\" />\n"
            + "<AggExclude name=\"agg_g_ms_pcat_sales_fact_1997\" />\n"
            + "    <AggExclude name=\"agg_l_03_sales_fact_1997\" />\n"
            + "<AggExclude name=\"agg_l_04_sales_fact_1997\" />\n"
            + "<AggExclude name=\"agg_l_05_sales_fact_1997\" />\n"
            + "<AggExclude name=\"agg_lc_06_sales_fact_1997\" />\n"
            + "<AggExclude name=\"agg_lc_100_sales_fact_1997\" />\n"
            + "    <AggExclude name=\"agg_ll_01_sales_fact_1997\" />\n"
            + "    <AggExclude name=\"agg_pl_01_sales_fact_1997\" />\n"
            + "<AggExclude name=\"agg_c_special_sales_fact_1997\" />\n"
            + "    <AggName name=\"agg_c_10_sales_fact_1997\">\n" + "        <AggFactCount column=\"FACT_COUNT\"/>\n"
            + "        <AggMeasure name=\"[Measures].[Unit Sales]\" column=\"UNIT_SALES\" rollupType=\"SumFromAvg\" />\n"
            + "        <AggLevel name=\"[Time].[Year]\" column=\"THE_YEAR\" />\n"
            + "        <AggLevel name=\"[Time].[Quarter]\" column=\"QUARTER\" />\n" + "    </AggName>\n"
            + "  </Table>\n" + "  <Dimension name=\"Time\" type=\"TimeDimension\" foreignKey=\"time_id\">\n"
            + "    <Hierarchy hasAll=\"false\" primaryKey=\"time_id\">\n" + "      <Table name=\"time_by_day\"/>\n"
            + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
            + "          levelType=\"TimeYears\"/>\n"
            + "      <Level name=\"Quarter\" column=\"quarter\" uniqueMembers=\"false\"\n"
            + "          levelType=\"TimeQuarters\"/>\n" + "    </Hierarchy>\n" + "  </Dimension>\n"
            + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"avg\" />\n" + "</Cube>\n"
            + "</Schema>";
            */
        public BasicQueryTestModifier18(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("FoodMart 2399 Rollup Type")
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Sales")
                        .defaultMeasure("Unit Sales")
                        .fact(new TableR("sales_fact_1997",
                                List.of(
                                    AggExcludeRBuilder.builder().name("agg_c_14_sales_fact_1997").build(),
                                    AggExcludeRBuilder.builder().name("agg_g_ms_pcat_sales_fact_1997").build(),
                                    AggExcludeRBuilder.builder().name("agg_l_03_sales_fact_1997").build(),
                                    AggExcludeRBuilder.builder().name("agg_l_04_sales_fact_1997").build(),
                                    AggExcludeRBuilder.builder().name("agg_l_05_sales_fact_1997").build(),
                                    AggExcludeRBuilder.builder().name("agg_lc_06_sales_fact_1997").build(),
                                    AggExcludeRBuilder.builder().name("agg_lc_100_sales_fact_1997").build(),
                                    AggExcludeRBuilder.builder().name("agg_ll_01_sales_fact_1997").build(),
                                    AggExcludeRBuilder.builder().name("agg_pl_01_sales_fact_1997").build(),
                                    AggExcludeRBuilder.builder().name("agg_c_special_sales_fact_1997").build()
                                ),
                                List.of(
                                    AggNameRBuilder.builder()
                                        .name("agg_c_10_sales_fact_1997")
                                        .aggFactCount(AggColumnNameRBuilder.builder()
                                            .column("FACT_COUNT")
                                            .build())
                                        .aggMeasures(List.of(
                                            AggMeasureRBuilder.builder()
                                                .name("[Measures].[Unit Sales]")
                                                .column("UNIT_SALES")
                                                .rollupType("AvgFromAvg")
                                                .build()
                                        ))
                                        .aggLevels(List.of(
                                            AggLevelRBuilder.builder()
                                                .name("[Time].[Year]")
                                                .column("THE_YEAR")
                                                .build(),
                                            AggLevelRBuilder.builder()
                                                .name("[Time].[Quarter]")
                                                .column("QUARTER")
                                                .build()
                                        ))
                                        .build())
                            )
                        )
                        .dimensionUsageOrDimensions(List.of(
                            PrivateDimensionRBuilder.builder()
                                .name("Time")
                                .type(DimensionTypeEnum.TIME_DIMENSION)
                                .foreignKey("time_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(false)
                                        .primaryKey("time_id")
                                        .relation(new TableR("time_by_day"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Year")
                                                .column("the_year")
                                                .type(TypeEnum.NUMERIC)
                                                .uniqueMembers(true)
                                                .levelType(LevelTypeEnum.TIME_YEARS)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Quarter")
                                                .column("quarter")
                                                .uniqueMembers(false)
                                                .levelType(LevelTypeEnum.TIME_QUARTERS)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales")
                                .column("unit_sales")
                                .aggregator("avg")
                                .build()
                        )).build()
                ))
                .build();
        }

    }

    public static class BasicQueryTestModifier19 extends RDbMappingSchemaModifier {

        /*
        "" + "<?xml version=\"1.0\"?>\n" + "<Schema name=\"FoodMart 2399 Rollup Type\">\n"
            + "<Cube name=\"Sales\" defaultMeasure=\"Unit Sales\">\n" + "  <Table name=\"sales_fact_1997\">\n"
            + "<AggExclude name=\"agg_c_14_sales_fact_1997\" />\n"
            + "<AggExclude name=\"agg_g_ms_pcat_sales_fact_1997\" />\n"
            + "    <AggExclude name=\"agg_l_03_sales_fact_1997\" />\n"
            + "<AggExclude name=\"agg_l_04_sales_fact_1997\" />\n"
            + "<AggExclude name=\"agg_l_05_sales_fact_1997\" />\n"
            + "<AggExclude name=\"agg_lc_06_sales_fact_1997\" />\n"
            + "<AggExclude name=\"agg_lc_100_sales_fact_1997\" />\n"
            + "    <AggExclude name=\"agg_ll_01_sales_fact_1997\" />\n"
            + "    <AggExclude name=\"agg_pl_01_sales_fact_1997\" />\n"
            + "<AggExclude name=\"agg_c_special_sales_fact_1997\" />\n"
            + "    <AggName name=\"agg_c_10_sales_fact_1997\">\n" + "        <AggFactCount column=\"FACT_COUNT\"/>\n"
            + "        <AggMeasure name=\"[Measures].[Unit Sales]\" column=\"UNIT_SALES\" />\n"
            + "        <AggLevel name=\"[Time].[Year]\" column=\"THE_YEAR\" />\n"
            + "        <AggLevel name=\"[Time].[Quarter]\" column=\"QUARTER\" />\n" + "    </AggName>\n"
            + "  </Table>\n" + "  <Dimension name=\"Time\" type=\"TimeDimension\" foreignKey=\"time_id\">\n"
            + "    <Hierarchy hasAll=\"false\" primaryKey=\"time_id\">\n" + "      <Table name=\"time_by_day\"/>\n"
            + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
            + "          levelType=\"TimeYears\"/>\n"
            + "      <Level name=\"Quarter\" column=\"quarter\" uniqueMembers=\"false\"\n"
            + "          levelType=\"TimeQuarters\"/>\n" + "    </Hierarchy>\n" + "  </Dimension>\n"
            + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"avg\" />\n" + "</Cube>\n"
            + "</Schema>";
            */
        public BasicQueryTestModifier19(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("FoodMart 2399 Rollup Type")
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Sales")
                        .defaultMeasure("Unit Sales")
                        .fact(new TableR("sales_fact_1997",
                                List.of(
                                    AggExcludeRBuilder.builder().name("agg_c_14_sales_fact_1997").build(),
                                    AggExcludeRBuilder.builder().name("agg_g_ms_pcat_sales_fact_1997").build(),
                                    AggExcludeRBuilder.builder().name("agg_l_03_sales_fact_1997").build(),
                                    AggExcludeRBuilder.builder().name("agg_l_04_sales_fact_1997").build(),
                                    AggExcludeRBuilder.builder().name("agg_l_05_sales_fact_1997").build(),
                                    AggExcludeRBuilder.builder().name("agg_lc_06_sales_fact_1997").build(),
                                    AggExcludeRBuilder.builder().name("agg_lc_100_sales_fact_1997").build(),
                                    AggExcludeRBuilder.builder().name("agg_ll_01_sales_fact_1997").build(),
                                    AggExcludeRBuilder.builder().name("agg_pl_01_sales_fact_1997").build(),
                                    AggExcludeRBuilder.builder().name("agg_c_special_sales_fact_1997").build()
                                ),
                            List.of(
                                    AggNameRBuilder.builder()
                                        .name("agg_c_10_sales_fact_1997")
                                        .aggFactCount(AggColumnNameRBuilder.builder()
                                            .column("FACT_COUNT")
                                            .build())
                                        .aggMeasures(List.of(
                                            AggMeasureRBuilder.builder()
                                                .name("[Measures].[Unit Sales]")
                                                .column("UNIT_SALES")
                                                .build()
                                        ))
                                        .aggLevels(List.of(
                                            AggLevelRBuilder.builder()
                                                .name("[Time].[Year]")
                                                .column("THE_YEAR")
                                                .build(),
                                            AggLevelRBuilder.builder()
                                                .name("[Time].[Quarter]")
                                                .column("QUARTER")
                                                .build()
                                        ))
                                        .build())
                            )
                        )
                        .dimensionUsageOrDimensions(List.of(
                            PrivateDimensionRBuilder.builder()
                                .name("Time")
                                .type(DimensionTypeEnum.TIME_DIMENSION)
                                .foreignKey("time_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(false)
                                        .primaryKey("time_id")
                                        .relation(new TableR("time_by_day"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Year")
                                                .column("the_year")
                                                .type(TypeEnum.NUMERIC)
                                                .uniqueMembers(true)
                                                .levelType(LevelTypeEnum.TIME_YEARS)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Quarter")
                                                .column("quarter")
                                                .uniqueMembers(false)
                                                .levelType(LevelTypeEnum.TIME_QUARTERS)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales")
                                .column("unit_sales")
                                .aggregator("avg")
                                .build()
                        )).build()
                ))
                .build();
        }
    }

    public static class BasicQueryTestModifier20 extends RDbMappingSchemaModifier {

        /*
            String cubeName = "Sales_MemberVis";
            String schema = SchemaUtil.getSchema(baseSchema, null, "<Cube name=\"" + cubeName + "\">\n"
            + "  <Table name=\"sales_fact_1997\"/>\n"
            + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"Standard\" visible=\"false\"/>\n"
            + "  <Measure name=\"Store Cost\" column=\"store_cost\" aggregator=\"sum\"\n"
            + "      formatString=\"#,###.00\"/>\n"
            + "  <Measure name=\"Store Sales\" column=\"store_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"#,###.00\"/>\n"
            + "  <Measure name=\"Sales Count\" column=\"product_id\" aggregator=\"count\"\n"
            + "      formatString=\"#,###\"/>\n" + "  <Measure name=\"Customer Count\" column=\"customer_id\"\n"
            + "      aggregator=\"distinct-count\" formatString=\"#,###\"/>\n" + "  <CalculatedMember\n"
            + "      name=\"Profit\"\n" + "      dimension=\"Measures\"\n" + "      visible=\"false\"\n"
            + "      formula=\"[Measures].[Store Sales]-[Measures].[Store Cost]\">\n"
            + "    <CalculatedMemberProperty name=\"FORMAT_STRING\" value=\"$#,##0.00\"/>\n"
            + "  </CalculatedMember>\n" + "</Cube>", null, null, null, null );
            */
        public BasicQueryTestModifier20(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCube> schemaCubes(MappingSchema mappingSchemaOriginal) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(mappingSchemaOriginal));
            result.add(CubeRBuilder.builder()
                .name("Sales_MemberVis")
                .fact(new TableR("sales_fact_1997"))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Unit Sales")
                        .column("unit_sales")
                        .aggregator("sum")
                        .formatString("Standard")
                        .visible(false)
                        .build(),
                   MeasureRBuilder.builder()
                        .name("Store Cost")
                        .column("store_cost")
                        .aggregator("sum")
                        .formatString("#,###.00")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Store Sales")
                        .column("store_sales")
                        .aggregator("sum")
                        .formatString("#,###.00")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Sales Count")
                        .column("product_id")
                        .aggregator("count")
                        .formatString("#,###")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Customer Count")
                        .column("customer_id")
                        .aggregator("distinct-count")
                        .formatString("#,###")
                        .build()
                ))
                .calculatedMembers(List.of(
                    CalculatedMemberRBuilder.builder()
                        .name("Profit")
                        .dimension("Measures")
                        .visible(false)
                        .formula("[Measures].[Store Sales]-[Measures].[Store Cost]")
                        .calculatedMemberProperties(List.of(
                            CalculatedMemberPropertyRBuilder.builder()
                                .name("FORMAT_STRING")
                                .value("$#,##0.00")
                                .build()
                        ))
                        .build()
                ))
                .build());
            return result;
        }
    }

    public static class BasicQueryTestModifier21 extends RDbMappingSchemaModifier {

        /*
            "<Cube name=\"Sales_DimWithoutAll\">\n"
            + "  <Table name=\"sales_fact_1997\"/>\n" + "  <Dimension name=\"Product\" foreignKey=\"product_id\">\n"
            + "    <Hierarchy hasAll=\"false\" primaryKey=\"product_id\" " + "primaryKeyTable=\"product\">\n"
            + "      <Join leftKey=\"product_class_id\" " + "rightKey=\"product_class_id\">\n"
            + "        <Table name=\"product\"/>\n" + "        <Table name=\"product_class\"/>\n" + "      </Join>\n"
            + "      <Level name=\"Product Family\" table=\"product_class\" " + "column=\"product_family\"\n"
            + "          uniqueMembers=\"true\"/>\n" + "      <Level name=\"Product Department\" "
            + "table=\"product_class\" column=\"product_department\"\n" + "          uniqueMembers=\"false\"/>\n"
            + "      <Level name=\"Product Category\" table=\"product_class\"" + " column=\"product_category\"\n"
            + "          uniqueMembers=\"false\"/>\n" + "      <Level name=\"Product Subcategory\" "
            + "table=\"product_class\" column=\"product_subcategory\"\n" + "          uniqueMembers=\"false\"/>\n"
            + "      <Level name=\"Brand Name\" table=\"product\" "
            + "column=\"brand_name\" uniqueMembers=\"false\"/>\n"
            + "      <Level name=\"Product Name\" table=\"product\" " + "column=\"product_name\"\n"
            + "          uniqueMembers=\"true\"/>\n" + "    </Hierarchy>\n" + "  </Dimension>\n"
            + "  <Dimension name=\"Gender\" foreignKey=\"customer_id\">\n"
            + "    <Hierarchy hasAll=\"false\" primaryKey=\"customer_id\">\n" + "    <Table name=\"customer\"/>\n"
            + "      <Level name=\"Gender\" column=\"gender\" " + "uniqueMembers=\"true\"/>\n" + "    </Hierarchy>\n"
            + "  </Dimension>" + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" " + "aggregator=\"sum\"\n"
            + "      formatString=\"Standard\" visible=\"false\"/>\n"
            + "  <Measure name=\"Store Cost\" column=\"store_cost\" aggregator=\"sum\"\n"
            + "      formatString=\"#,###.00\"/>\n" + "</Cube>"
            */
        public BasicQueryTestModifier21(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCube> schemaCubes(MappingSchema mappingSchemaOriginal) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(mappingSchemaOriginal));
            result.add(CubeRBuilder.builder()
                .name("Sales_DimWithoutAll")
                .fact(new TableR("sales_fact_1997"))
                .dimensionUsageOrDimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Product")
                        .foreignKey("product_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(false)
                                .primaryKey("product_id")
                                .primaryKeyTable("product")
                                .relation(new JoinR(List.of(
                                    new TableR("product"),
                                    new TableR("product_class")
                                ),
                                    null, "product_class_id", null, "product_class_id"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Product Family")
                                        .table("product_class")
                                        .column("product_family")
                                        .uniqueMembers(true)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Product Department")
                                        .table("product_class")
                                        .column("product_department")
                                        .uniqueMembers(false)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Product Category")
                                        .table("product_class")
                                        .column("product_category")
                                        .uniqueMembers(false)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Product Subcategory")
                                        .table("product_class")
                                        .column("product_subcategory")
                                        .uniqueMembers(false)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Brand Name")
                                        .table("product")
                                        .column("brand_name")
                                        .uniqueMembers(false)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Product Name")
                                        .table("product")
                                        .column("product_name")
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        ))
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .name("Gender")
                        .foreignKey("customer_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(false)
                                .primaryKey("customer_id")
                                .relation(new TableR("customer"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Gender")
                                        .column("gender")
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Unit Sales")
                        .column("unit_sales")
                        .aggregator("sum")
                        .formatString("Standard")
                        .visible(false)
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Store Cost")
                        .column("store_cost")
                        .aggregator("sum")
                        .formatString("#,###.00")
                        .build()
                ))
                .build());
            return result;
        }
    }

    public static class BasicQueryTestModifier22 extends RDbMappingSchemaModifier {

        /*
            final String cubeName = "Sales_withCities";
      String baseSchema = TestUtil.getRawSchema(context);
      String schema = SchemaUtil.getSchema(baseSchema, null, "<Cube name=\"" + cubeName + "\">\n"
            + "  <Table name=\"sales_fact_1997\"/>\n"
            + "  <DimensionUsage name=\"Time\" source=\"Time\" foreignKey=\"time_id\"/>\n"
            + "  <Dimension name=\"Cities\" foreignKey=\"customer_id\">\n"
            + "    <Hierarchy hasAll=\"true\" allMemberName=\"All Cities\" primaryKey=\"customer_id\">\n"
            + "      <Table name=\"customer\"/>\n"
            + "      <Level name=\"City\" column=\"city\" uniqueMembers=\"false\"/> \n" + "    </Hierarchy>\n"
            + "  </Dimension>\n" + "  <Dimension name=\"Customers\" foreignKey=\"customer_id\">\n"
            + "    <Hierarchy hasAll=\"true\" allMemberName=\"All Customers\" primaryKey=\"customer_id\">\n"
            + "      <Table name=\"customer\"/>\n"
            + "      <Level name=\"Country\" column=\"country\" uniqueMembers=\"true\"/>\n"
            + "      <Level name=\"State Province\" column=\"state_province\" uniqueMembers=\"true\"/>\n"
            + "      <Level name=\"City\" column=\"city\" uniqueMembers=\"false\"/>\n"
            + "      <Level name=\"Name\" column=\"fullname\" uniqueMembers=\"true\">\n"
            + "        <Property name=\"Gender\" column=\"gender\"/>\n"
            + "        <Property name=\"Marital Status\" column=\"marital_status\"/>\n"
            + "        <Property name=\"Education\" column=\"education\"/>\n"
            + "        <Property name=\"Yearly Income\" column=\"yearly_income\"/>\n" + "      </Level>\n"
            + "    </Hierarchy>\n" + "  </Dimension>\n" + "  <Dimension name=\"Gender\" foreignKey=\"customer_id\">\n"
            + "    <Hierarchy hasAll=\"true\" primaryKey=\"customer_id\">\n" + "    <Table name=\"customer\"/>\n"
            + "      <Level name=\"Gender\" column=\"gender\" uniqueMembers=\"true\"/>\n" + "    </Hierarchy>\n"
            + "  </Dimension>" + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"Standard\" visible=\"false\"/>\n"
            + "  <Measure name=\"Store Sales\" column=\"store_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"#,###.00\"/>\n" + "</Cube>", null, null, null, null );
            */
        public BasicQueryTestModifier22(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCube> schemaCubes(MappingSchema mappingSchemaOriginal) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(mappingSchemaOriginal));
            result.add(CubeRBuilder.builder()
                .name("Sales_withCities")
                .fact(new TableR("sales_fact_1997"))
                .dimensionUsageOrDimensions(List.of(
                    DimensionUsageRBuilder.builder()
                        .name("Time")
                        .source("Time")
                        .foreignKey("time_id")
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .name("Cities")
                        .foreignKey("customer_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .allMemberName("All Cities")
                                .primaryKey("customer_id")
                                .relation(new TableR("customer"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("City")
                                        .column("city")
                                        .uniqueMembers(false)
                                        .build()
                                ))
                                .build()
                        ))
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .name("Customers")
                        .foreignKey("customer_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .allMemberName("All Customers")
                                .primaryKey("customer_id")
                                .relation(new TableR("customer"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Country")
                                        .column("country")
                                        .uniqueMembers(true)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("State Province")
                                        .column("state_province")
                                        .uniqueMembers(true)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("City")
                                        .column("city")
                                        .uniqueMembers(false)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Name")
                                        .column("fullname")
                                        .uniqueMembers(true)
                                        .properties(List.of(
                                            PropertyRBuilder.builder()
                                                .name("Gender")
                                                .column("gender")
                                                .build(),
                                            PropertyRBuilder.builder()
                                                .name("Marital Status")
                                                .column("marital_status")
                                                .build(),
                                            PropertyRBuilder.builder()
                                                .name("Education")
                                                .column("education")
                                                .build(),
                                            PropertyRBuilder.builder()
                                                .name("Yearly Income")
                                                .column("yearly_income")
                                                .build()


                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .name("Gender")
                        .foreignKey("customer_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("customer_id")
                                .relation(new TableR("customer"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Gender")
                                        .column("gender")
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Unit Sales")
                        .column("unit_sales")
                        .aggregator("sum")
                        .formatString("Standard")
                        .visible(false)
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Store Sales")
                        .column("store_sales")
                        .aggregator("sum")
                        .formatString("#,###.00")
                        .build()
                ))
                .build());
            return result;
        }
    }

    public static class BasicQueryTestModifier23 extends RDbMappingSchemaModifier {

        /*
            <Cube name=\"SalesWithBadMeasure\">\n"
            + "  <Table name=\"sales_fact_1997\"/>\n"
            + "  <DimensionUsage name=\"Time\" source=\"Time\" foreignKey=\"time_id\"/>\n"
            + "  <Measure name=\"Bad Measure\" aggregator=\"sum\"\n" + "      formatString=\"Standard\"/>\n"
            + "</Cube>
            */
        public BasicQueryTestModifier23(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCube> schemaCubes(MappingSchema mappingSchemaOriginal) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(mappingSchemaOriginal));
            result.add(CubeRBuilder.builder()
                .name("SalesWithBadMeasure")
                .fact(new TableR("sales_fact_1997"))
                .dimensionUsageOrDimensions(List.of(
                    DimensionUsageRBuilder.builder()
                        .name("Time")
                        .source("Time")
                        .foreignKey("time_id")
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Bad Measure")
                        .aggregator("sum").formatString("Standard")
                        .build()
                ))
                .build());
            return result;
        }
    }

    public static class BasicQueryTestModifier24 extends RDbMappingSchemaModifier {

        /*
            "<Cube name=\"SalesWithBadMeasure2\">\n"
            + "  <Table name=\"sales_fact_1997\"/>\n"
            + "  <DimensionUsage name=\"Time\" source=\"Time\" foreignKey=\"time_id\"/>\n"
            + "  <Measure name=\"Bad Measure\" column=\"unit_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"Standard\">\n" + "    <MeasureExpression>\n" + "       <SQL dialect=\"generic\">\n"
            + "         unit_sales\n" + "       </SQL>\n" + "    </MeasureExpression>\n" + "  </Measure>\n"
            + "</Cube>"
            */
        public BasicQueryTestModifier24(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCube> schemaCubes(MappingSchema mappingSchemaOriginal) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(mappingSchemaOriginal));
            result.add(CubeRBuilder.builder()
                .name("SalesWithBadMeasure2")
                .fact(new TableR("sales_fact_1997"))
                .dimensionUsageOrDimensions(List.of(
                    DimensionUsageRBuilder.builder()
                        .name("Time")
                        .source("Time")
                        .foreignKey("time_id")
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Bad Measure")
                        .column("unit_sales")
                        .aggregator("sum")
                        .formatString("Standard")
                        .measureExpression(ExpressionViewRBuilder.builder()
                            .sqls(List.of(
                                SQLRBuilder.builder()
                                    .dialect("generic")
                                    .content("unit_sales")
                                    .build()
                            ))
                            .build())
                        .build()
                ))
                .build());
            return result;
        }
    }

    public static class BasicQueryTestModifier25 extends RDbMappingSchemaModifier {

        /*
            <UserDefinedFunction name=\"SleepUdf\" className=\""
            + SleepUdf.class.getName() + "\"/>"
            */
        public BasicQueryTestModifier25(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingUserDefinedFunction> schemaUserDefinedFunctions(MappingSchema schema) {
            List<MappingUserDefinedFunction> result = new ArrayList<>();
            result.addAll(super.schemaUserDefinedFunctions(schema));
            result.add(UserDefinedFunctionRBuilder.builder()
                .name("SleepUdf")
                .className(BasicQueryTest.SleepUdf.class.getName())
                .build());
            return result;
        }
    }

    public static class BasicQueryTestModifier26 extends RDbMappingSchemaModifier {

        /*
            <Cube name=\"DefaultMeasureTesting\" defaultMeasure=\"Supply Time\">\n"
            + "  <Table name=\"inventory_fact_1997\"/>\n" + "  <DimensionUsage name=\"Store\" source=\"Store\" "
            + "foreignKey=\"store_id\"/>\n" + "  <DimensionUsage name=\"Store Type\" source=\"Store Type\" "
            + "foreignKey=\"store_id\"/>\n" + "  <Measure name=\"Store Invoice\" column=\"store_invoice\" "
            + "aggregator=\"sum\"/>\n" + "  <Measure name=\"Supply Time\" column=\"supply_time\" "
            + "aggregator=\"sum\"/>\n" + "  <Measure name=\"Warehouse Cost\" column=\"warehouse_cost\" "
            + "aggregator=\"sum\"/>\n" + "</Cube>
            */
        public BasicQueryTestModifier26(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCube> schemaCubes(MappingSchema mappingSchemaOriginal) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(mappingSchemaOriginal));
            result.add(CubeRBuilder.builder()
                .name("DefaultMeasureTesting")
                .defaultMeasure("Supply Time")
                .fact(new TableR("inventory_fact_1997"))
                .dimensionUsageOrDimensions(List.of(
                    DimensionUsageRBuilder.builder()
                        .name("Store")
                        .source("Store")
                        .foreignKey("store_id")
                        .build(),
                    DimensionUsageRBuilder.builder()
                        .name("Store Type")
                        .source("Store Type")
                        .foreignKey("store_id")
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Store Invoice")
                        .column("store_invoice")
                        .aggregator("sum")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Supply Time")
                        .column("supply_time")
                        .aggregator("sum")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Warehouse Cost")
                        .column("warehouse_cost")
                        .aggregator("sum")
                        .build()
                ))
                .build());
            return result;
        }
    }

    public static class BasicQueryTestModifier27 extends RDbMappingSchemaModifier {

        /*
                        "<Cube name=\"DefaultMeasureTesting\" defaultMeasure=\"Supply Time Error\">\n"
                + "  <Table name=\"inventory_fact_1997\"/>\n" + "  <DimensionUsage name=\"Store\" source=\"Store\" "
                + "foreignKey=\"store_id\"/>\n" + "  <DimensionUsage name=\"Store Type\" source=\"Store Type\" "
                + "foreignKey=\"store_id\"/>\n" + "  <Measure name=\"Store Invoice\" column=\"store_invoice\" "
                + "aggregator=\"sum\"/>\n" + "  <Measure name=\"Supply Time\" column=\"supply_time\" "
                + "aggregator=\"sum\"/>\n" + "  <Measure name=\"Warehouse Cost\" column=\"warehouse_cost\" "
                + "aggregator=\"sum\"/>\n" + "</Cube>"
            */
        public BasicQueryTestModifier27(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCube> schemaCubes(MappingSchema mappingSchemaOriginal) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(mappingSchemaOriginal));
            result.add(CubeRBuilder.builder()
                .name("DefaultMeasureTesting")
                .defaultMeasure("Supply Time Error")
                .fact(new TableR("inventory_fact_1997"))
                .dimensionUsageOrDimensions(List.of(
                    DimensionUsageRBuilder.builder()
                        .name("Store")
                        .source("Store")
                        .foreignKey("store_id")
                        .build(),
                    DimensionUsageRBuilder.builder()
                        .name("Store Type")
                        .source("Store Type")
                        .foreignKey("store_id")
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Store Invoice")
                        .column("store_invoice")
                        .aggregator("sum")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Supply Time")
                        .column("supply_time")
                        .aggregator("sum")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Warehouse Cost")
                        .column("warehouse_cost")
                        .aggregator("sum")
                        .build()
                ))
                .build());
            return result;
        }
    }

    public static class BasicQueryTestModifier28 extends RDbMappingSchemaModifier {

        /*
                        <Cube name='FooBarZerOneAnything'>\n" + "  <Table name='sales_fact_1997'/>\n"
            + "  <Dimension name='Gender' foreignKey='customer_id'>\n"
            + "    <Hierarchy hasAll='true' allMemberName='All Gender' primaryKey='customer_id'>\n"
            + "      <Table name='customer'/>\n"
            + "      <Level name='Gender' column='gender' uniqueMembers='true'/>\n" + "    </Hierarchy>\n"
            + "  </Dimension>" + "<Measure name='zero' aggregator='sum'>\n" + "  <MeasureExpression>\n"
            + "  <SQL dialect='generic'>\n" + "    0" + "  </SQL></MeasureExpression></Measure>" + "</Cube>
            */
        public BasicQueryTestModifier28(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCube> schemaCubes(MappingSchema mappingSchemaOriginal) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(mappingSchemaOriginal));
            result.add(CubeRBuilder.builder()
                .name("FooBarZerOneAnything")
                .fact(new TableR("sales_fact_1997"))
                .dimensionUsageOrDimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Gender")
                        .foreignKey("customer_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .allMemberName("All Gender")
                                .primaryKey("customer_id")
                                .relation(new TableR("customer"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Gender").column("gender").uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("zero")
                        .aggregator("sum")
                        .measureExpression(ExpressionViewRBuilder.builder()
                            .sqls(List.of(
                                SQLRBuilder.builder()
                                    .dialect("generic")
                                    .content("0")
                                    .build()
                            ))
                            .build())
                        .build()
                ))
                .build());
            return result;
        }
    }

    public static class BasicQueryTestModifier29 extends RDbMappingSchemaModifier {

        /*
            <UserDefinedFunction name='CountConcurrentUdf' className='" + CountConcurrentUdf.class.getName() + "'/>
            */
        public BasicQueryTestModifier29(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingUserDefinedFunction> schemaUserDefinedFunctions(MappingSchema schema) {
            List<MappingUserDefinedFunction> result = new ArrayList<>();
            result.addAll(super.schemaUserDefinedFunctions(schema));
            result.add(UserDefinedFunctionRBuilder.builder()
                .name("CountConcurrentUdf")
                .className(BasicQueryTest.CountConcurrentUdf.class.getName())
                .build());
            return result;
        }
    }

    public static class BasicQueryTestModifier30 extends RDbMappingSchemaModifier {

        /*
        "<Schema name=\"Foo\">\n" + "  <Cube name=\"Bar\">\n"
            + "    <Table name=\"warehouse\">\n" + "      <SQL>sleep(0.1) = 0</SQL>\n" + "    </Table>   \n"
            + " <Dimension name=\"Dim\">\n" + "   <Hierarchy hasAll=\"true\">\n"
            + "     <Level name=\"Level\" column=\"warehouse_id\"/>\n" + "      </Hierarchy>\n" + " </Dimension>\n"
            + " <Measure name=\"Measure\" aggregator=\"sum\">\n" + "   <MeasureExpression>\n" + "     <SQL>1</SQL>\n"
            + "   </MeasureExpression>\n" + " </Measure>\n" + "  </Cube>\n" + "</Schema>\n"
            */
        public BasicQueryTestModifier30(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("Foo")
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Bar")
                        .fact(new TableR("warehouse", SQLRBuilder.builder().content("sleep(0.1) = 0").build()))
                        .dimensionUsageOrDimensions(List.of(
                            PrivateDimensionRBuilder.builder()
                                .name("Dim")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(true)
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Level").column("warehouse_id")
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Measure").aggregator("sum")
                                .measureExpression(ExpressionViewRBuilder.builder()
                                    .sqls(List.of(
                                        SQLRBuilder.builder().content("1").build()
                                    ))
                                    .build())
                                .build()
                        ))
                        .build()))
                .build();
        }
    }

    public static class BasicQueryTestModifier31 extends RDbMappingSchemaModifier {

        /*
        "" + "<?xml version=\"1.0\"?>\n" + "<Schema name=\"snowflake bug\">\n" + "  <Cube name=\"Bug\">\n"
            + "    <Table name=\"sales_fact_1997\"/>\n"
            + "    <Dimension name=\"Product - Bug\" foreignKey=\"product_id\" highCardinality=\"false\">\n"
            + "      <Hierarchy hasAll=\"true\" primaryKeyTable=\"product\" primaryKey=\"product_id\">\n"
            + "        <Join leftAlias=\"product_class\" leftKey=\"product_class_id\" rightAlias=\"product\" "
            + "rightKey=\"product_class_id\">\n"
            + "          <Table name=\"product_class\" alias=\"product_class\"/>\n"
            + "          <Table name=\"product\" alias=\"product\"/>\n" + "        </Join>\n"
            + "        <Level name=\"Product Family\" table=\"product_class\" column=\"product_family\" "
            + "uniqueMembers=\"false\"/>\n" + "      </Hierarchy>\n" + "    </Dimension>\n"
            + "    <Measure name=\"Sales\" aggregator=\"sum\" column=\"store_sales\"/>\n" + "  </Cube>\n"
            + "  <Cube name=\"No Bug\">\n" + "    <Table name=\"sales_fact_1997\"/>\n"
            + "    <Dimension name=\"Product - no Bug\" highCardinality=\"false\" foreignKey=\"product_id\">\n"
            + "      <Hierarchy hasAll=\"true\" primaryKeyTable=\"product\" primaryKey=\"product_id\">\n"
            + "        <Join leftAlias=\"product\" leftKey=\"product_class_id\" rightAlias=\"product_class\" "
            + "rightKey=\"product_class_id\">\n" + "          <Table name=\"product\" alias=\"product\"/>\n"
            + "          <Table name=\"product_class\" alias=\"product_class\"/>\n" + "        </Join>\n"
            + "        <Level name=\"Product Family\" table=\"product_class\" column=\"product_family\"/>\n"
            + "      </Hierarchy>\n" + "    </Dimension>\n"
            + "    <Measure name=\"Sales\" aggregator=\"sum\" column=\"store_sales\"/>\n" + "  </Cube>\n"
            + "</Schema>";

            */
        public BasicQueryTestModifier31(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("snowflake bug")
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Bug")
                        .fact(new TableR("sales_fact_1997"))
                        .dimensionUsageOrDimensions(List.of(
                            PrivateDimensionRBuilder.builder()
                                .name("Product - Bug")
                                .foreignKey("product_id")
                                .highCardinality(false)
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(true)
                                        .primaryKeyTable("product")
                                        .primaryKey("product_id")
                                        .relation(new JoinR(List.of(
                                            new TableR(null, "product_class", "product_class", List.of()),
                                            new TableR(null, "product", "product", List.of())
                                        ),
                                            "product_class", "product_class_id",
                                            "product", "product_class_id"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Product Family")
                                                .table("product_class")
                                                .column("product_family")
                                                .uniqueMembers(false)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Sales").aggregator("sum")
                                .column("store_sales")
                                .build()
                        ))
                        .build(),
                    CubeRBuilder.builder()
                    	.name("No Bug")
                    	.fact(new TableR("sales_fact_1997"))
                    	.dimensionUsageOrDimensions(List.of(
                    	    PrivateDimensionRBuilder.builder()
                                .name("Product - no Bug")
                                .highCardinality(false)
                                .foreignKey("product_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(true)
                                        .primaryKeyTable("product")
                                        .primaryKey("product_id")
                                        .relation(new JoinR(List.of(
                                            new TableR(null, "product", "product", List.of()),
                                            new TableR(null, "product_class", "product_class", List.of())
                                        ),
                                            "product", "product_class_id",
                                            "product_class", "product_class_id"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Product Family")
                                                .table("product_class")
                                                .column("product_family")
                                                .build()
                                        ))
                                        .build()
                                ))
                    			.build()
                    			))
                    	.measures(List.of(
                    	    MeasureRBuilder.builder()
                                .name("Sales")
                                .aggregator("sum")
                                .column("store_sales")
                                .build()
                        ))
                        .build()
                        ))

                .build();
        }
    }

    public static class BasicQueryTestModifier32 extends RDbMappingSchemaModifier {

        /*
            "<Schema name=\"FoodMart\">\n"
            + "  <Dimension name=\"Store Size in SQFT\">\n"
            + "    <Hierarchy hasAll=\"true\" primaryKey=\"store_id\">\n" + "      <Table name=\"store\"/>\n"
            + "      <Level name=\"Store Sqft\" column=\"store_sqft\" type=\"Numeric\" uniqueMembers=\"true\"/>\n"
            + "    </Hierarchy>\n" + "  </Dimension>\n" + "  <Cube name=\"Sales\" defaultMeasure=\"Unit Sales\">\n"
            + "    <Table name=\"sales_fact_1997\"/>\n"
            + "    <DimensionUsage name=\"Store Size in SQFT\" source=\"Store Size in SQFT\" foreignKey=\"store_id\"/>\n"
            + "    <DimensionUsage name=\"SQFT 2\" source=\"Store Size in SQFT\" foreignKey=\"store_id\"/>\n"
            + "    <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\" formatString=\"Standard\"/>\n"
            + "  </Cube>\n" + "</Schema>"
            */
        public BasicQueryTestModifier32(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("FoodMart")
                .dimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Store Size in SQFT")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("store_id")
                                .relation(new TableR("store"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Store Sqft")
                                        .column("store_sqft")
                                        .type(TypeEnum.NUMERIC)
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Sales")
                        .defaultMeasure("Unit Sales")
                        .fact(new TableR("sales_fact_1997"))
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .name("Store Size in SQFT")
                                .source("Store Size in SQFT")
                                .foreignKey("store_id")
                                .build(),
                            DimensionUsageRBuilder.builder()
                                .name("SQFT 2")
                                .source("Store Size in SQFT")
                                .foreignKey("store_id")
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales").aggregator("sum")
                                .column("unit_sales")
                                .formatString("Standard")
                                .build()
                        ))
                        .build()))
                .build();
        }
    }

    public static class RolapCubeTestModifier1 extends RDbMappingSchemaModifier {

        /*
        String nonAccessibleMember =
            "  <CalculatedMember name=\"~Missing\" dimension=\"Gender\">\n"
            + "    <Formula>100</Formula>\n"
            + "  </CalculatedMember>\n";
        String accessibleMember =
            "  <CalculatedMember name=\"~Missing\" dimension=\"Product\">\n"
            + "    <Formula>100</Formula>\n"
            + "  </CalculatedMember>\n";
        */
        public RolapCubeTestModifier1(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCalculatedMember> cubeCalculatedMembers(MappingCube cube) {
            List<MappingCalculatedMember> result = new ArrayList<>();
            result.addAll(super.cubeCalculatedMembers(cube));
            if ("Sales".equals(cube.name())) {
                result.add(CalculatedMemberRBuilder.builder()
                    .name("~Missing")
                    .dimension("Gender")
                    .formulaElement(FormulaRBuilder.builder().cdata("100").build())
                    .build());
                result.add(CalculatedMemberRBuilder.builder()
                    .name("~Missing")
                    .dimension("Product")
                    .formulaElement(FormulaRBuilder.builder().cdata("100").build())
                    .build());
            }
            return result;
        }
    }

    public static class OrderByAliasTestModifier1 extends RDbMappingSchemaModifier {

        /*
        "<Dimension name=\"Promotions\" foreignKey=\"promotion_id\">\n"
        + "  <Hierarchy hasAll=\"true\" allMemberName=\"All Promotions\" primaryKey=\"promotion_id\" defaultMember=\"[All Promotions]\">\n"
        + "    <Table name=\"promotion\"/>\n"
        + "    <Level name=\"Promotion Name\" column=\"promotion_name\" uniqueMembers=\"true\">\n"
        + "      <KeyExpression><SQL>RTRIM("
        + colName + ")</SQL></KeyExpression>\n"
        + "    </Level>\n"
        + "  </Hierarchy>\n"
        + "</Dimension>"));
         */

        private StringBuilder colName;
        public OrderByAliasTestModifier1(MappingSchema mappingSchema, final StringBuilder colName) {
            super(mappingSchema);
            this.colName = colName;
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Promotions")
                    .foreignKey("promotion_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .allMemberName("All Promotions")
                            .primaryKey("promotion_id")
                            .defaultMember("[All Promotions]")
                            .relation(new TableR("promotion"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Promotion Name")
                                    .column("promotion_name")
                                    .uniqueMembers(true)
                                    .keyExpression(ExpressionViewRBuilder.builder()
                                        .sqls(List.of(
                                            SQLRBuilder.builder()
                                                .content("RTRIM("+ colName + ")")
                                                .build()
                                        ))
                                        .build())
                                    .build()
                            ))
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class OrderByAliasTestModifier2 extends RDbMappingSchemaModifier {

        /*
        "<Dimension name=\"Employees\" foreignKey=\"employee_id\">\n"
        + "  <Hierarchy hasAll=\"true\" allMemberName=\"All Employees\"\n"
        + "      primaryKey=\"employee_id\">\n"
        + "    <Table name=\"employee\"/>\n"
        + "    <Level name=\"Employee Id\" type=\"Numeric\" uniqueMembers=\"true\"\n"
        + "        column=\"employee_id\" parentColumn=\"supervisor_id\"\n"
        + "        nameColumn=\"full_name\" nullParentValue=\"0\">\n"
        + "      <ParentExpression><SQL>RTRIM("
        + colName + ")</SQL></ParentExpression>\n"
        + "      <Closure parentColumn=\"supervisor_id\" childColumn=\"employee_id\">\n"
        + "        <Table name=\"employee_closure\"/>\n"
        + "      </Closure>\n"
        + "      <Property name=\"Marital Status\" column=\"marital_status\"/>\n"
        + "      <Property name=\"Position Title\" column=\"position_title\"/>\n"
        + "      <Property name=\"Gender\" column=\"gender\"/>\n"
        + "      <Property name=\"Salary\" column=\"salary\"/>\n"
        + "      <Property name=\"Education Level\" column=\"education_level\"/>\n"
        + "      <Property name=\"Management Role\" column=\"management_role\"/>\n"
        + "    </Level>\n"
        + "  </Hierarchy>\n"
        + "</Dimension>"));
         */
        private StringBuilder colName;
        public OrderByAliasTestModifier2(MappingSchema mappingSchema, final StringBuilder colName) {
            super(mappingSchema);
            this.colName = colName;
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("HR".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Employees")
                    .foreignKey("employee_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .allMemberName("All Employees")
                            .primaryKey("employee_id")
                            .relation(new TableR("employee"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Employee Id")
                                    .type(TypeEnum.NUMERIC)
                                    .uniqueMembers(true)
                                    .column("employee_id")
                                    .parentColumn("supervisor_id")
                                    .nameColumn("full_name")
                                    .nullParentValue("0")
                                    .parentExpression(ExpressionViewRBuilder.builder()
                                        .sqls(List.of(
                                            SQLRBuilder.builder()
                                                .content("RTRIM("+ colName + ")")
                                                .build()
                                        ))
                                        .build())
                                    .closure(ClosureRBuilder.builder()
                                        .parentColumn("supervisor_id")
                                        .childColumn("employee_id")
                                        .table(new TableR("employee_closure"))
                                        .build())
                                    .properties(List.of(
                                        PropertyRBuilder.builder()
                                            .name("Marital Status").column("marital_status")
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("Position Title").column("position_titles")
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("Gender").column("gender")
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("Salary").column("salary")
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("Education Level").column("education_level")
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("Management Role").column("management_role")
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class OrderByAliasTestModifier3 extends RDbMappingSchemaModifier {

        /*
        "<Dimension name=\"Promotions\" foreignKey=\"promotion_id\">\n"
        + "  <Hierarchy hasAll=\"true\" allMemberName=\"All Promotions\" primaryKey=\"promotion_id\" defaultMember=\"[All Promotions]\">\n"
        + "    <Table name=\"promotion\"/>\n"
        + "    <Level name=\"Promotion Name\" column=\"promotion_name\" uniqueMembers=\"true\">\n"
        + "      <PropertyExpression name=\"Rtrim Name\"><SQL>RTRIM("
        + colName + ")</SQL></PropertyExpression>\n"
        + "    </Level>\n"
        + "  </Hierarchy>\n"
        + "</Dimension>"));
         */

        private StringBuilder colName;
        public OrderByAliasTestModifier3(MappingSchema mappingSchema, final StringBuilder colName) {
            super(mappingSchema);
            this.colName = colName;
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Promotions")
                    .foreignKey("promotion_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .allMemberName("All Promotions")
                            .primaryKey("promotion_id")
                            .defaultMember("[All Promotions]")
                            .relation(new TableR("promotion"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Promotion Name")
                                    .column("promotion_name")
                                    .uniqueMembers(true)
                                    .keyExpression(ExpressionViewRBuilder.builder()
                                        .name("Rtrim Name")
                                        .sqls(List.of(
                                            SQLRBuilder.builder()
                                                .content("RTRIM("+ colName + ")")
                                                .build()
                                        ))
                                        .build())
                                    .build()
                            ))
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class TestCalculatedMembersModifier1 extends RDbMappingSchemaModifier {

        /*
            "<CalculatedMember name='Profit With Spaces'"
            + "  dimension='Measures'"
            + "  formula='[Measures].[Store Sales]-[Measures].[Store Cost]'/>"));
        */
        public TestCalculatedMembersModifier1(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCalculatedMember> virtualCubeCalculatedMember(MappingVirtualCube virtualCube) {
            List<MappingCalculatedMember> result = new ArrayList<>();
            result.addAll(super.virtualCubeCalculatedMember(virtualCube));
            if ("Warehouse and Sales".equals(virtualCube.name())) {
                result.add(CalculatedMemberRBuilder.builder()
                    .name("Profit With Spaces")
                    .dimension("Measures")
                    .formula("[Measures].[Store Sales]-[Measures].[Store Cost]")
                    .build());
            }
            return result;
        }
    }

    public static class TestCalculatedMembersModifier2 extends RDbMappingSchemaModifier {

        /*
            "<CalculatedMember\n"
            + "    name=\"Profit Formatted\"\n"
            + "    dimension=\"Measures\"\n"
            + "    visible=\"false\"\n"
            + "    formula=\"[Measures].[Store Sales]-[Measures].[Store Cost]\">\n"
            + "  <CalculatedMemberProperty name=\"FORMAT_STRING\" value=\"$#,##0.00\"/>\n"
            + "  <CalculatedMemberProperty name=\"CELL_FORMATTER\" value=\""
            + UdfTest.FooBarCellFormatter.class.getName()
            + "\"/>\n"
            + "</CalculatedMember>\n"));
        */
        public TestCalculatedMembersModifier2(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCalculatedMember> cubeCalculatedMembers(MappingCube cube) {
            List<MappingCalculatedMember> result = new ArrayList<>();
            result.addAll(super.cubeCalculatedMembers(cube));
            if ("Sales".equals(cube.name())) {
                result.add(CalculatedMemberRBuilder.builder()
                    .name("Profit Formatted")
                    .dimension("Measures")
                    .visible(false)
                    .formula("[Measures].[Store Sales]-[Measures].[Store Cost]")
                    .calculatedMemberProperties(List.of(
                        CalculatedMemberPropertyRBuilder.builder()
                            .name("FORMAT_STRING")
                            .value("$#,##0.00")
                            .build(),
                        CalculatedMemberPropertyRBuilder.builder()
                            .name("CELL_FORMATTER")
                            .value(UdfTest.FooBarCellFormatter.class.getName())
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class TestCalculatedMembersModifier3 extends RDbMappingSchemaModifier {

        /*
                "<CalculatedMember\n"
                + "    name=\"My Tuple\"\n"
                + "    dimension=\"Measures\"\n"
                + "    visible=\"false\"\n"
                + "    formula=\"StrToTuple('([Gender].[M], [Marital Status].[S])', [Gender], [Marital Status])\"/>\n"));
        */
        public TestCalculatedMembersModifier3(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCalculatedMember> cubeCalculatedMembers(MappingCube cube) {
            List<MappingCalculatedMember> result = new ArrayList<>();
            result.addAll(super.cubeCalculatedMembers(cube));
            if ("Sales".equals(cube.name())) {
                result.add(CalculatedMemberRBuilder.builder()
                    .name("My Tuple")
                    .dimension("Measures")
                    .visible(false)
                    .formula("StrToTuple('([Gender].[M], [Marital Status].[S])', [Gender], [Marital Status])")
                    .build());
            }
            return result;
        }
    }

    public static class TestCalculatedMembersModifier4 extends RDbMappingSchemaModifier {

        /*
                "  <CalculatedMember\n"
                + "    name=\"Profit Formatted\"\n"
                + "    dimension=\"Measures\"\n"
                + "    visible=\"false\"\n"
                + "    formula=\"[Measures].[Store Sales]-[Measures].[Store Cost]\">\n"
                + "  <CalculatedMemberProperty name=\"FORMAT_STRING\" value=\"$#,##0.00\"/>\n"
                + "  <CalculatedMemberProperty name=\"CELL_FORMATTER\" value=\"mondrian.test.NonExistentCellFormatter\"/>\n"
                + "</CalculatedMember>\n"));
        */
        public TestCalculatedMembersModifier4(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCalculatedMember> cubeCalculatedMembers(MappingCube cube) {
            List<MappingCalculatedMember> result = new ArrayList<>();
            result.addAll(super.cubeCalculatedMembers(cube));
            if ("Sales".equals(cube.name())) {
                result.add(CalculatedMemberRBuilder.builder()
                    .name("Profit Formatted")
                    .dimension("Measures")
                    .visible(false)
                    .formula("[Measures].[Store Sales]-[Measures].[Store Cost]")
                    .calculatedMemberProperties(List.of(
                        CalculatedMemberPropertyRBuilder.builder()
                            .name("FORMAT_STRING")
                            .value("$#,##0.00")
                            .build(),
                        CalculatedMemberPropertyRBuilder.builder()
                            .name("CELL_FORMATTER")
                            .value("mondrian.test.NonExistentCellFormatter")
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class TestAggregationManagerModifier1 extends RDbMappingSchemaModifier {

        /*
            "<Dimension name=\"Store2\" foreignKey=\"store_id\">\n"
            + "  <Hierarchy hasAll=\"true\" primaryKey=\"store_id\" allMemberName=\"All Stores\">"
            + "    <Table name=\"store\"/>\n"
            + "    <Level name=\"Store Country\" column=\"store_country\" uniqueMembers=\"true\"/>\n"
            + "    <Level name=\"Store State\"   column=\"store_state\"   uniqueMembers=\"true\"/>\n"
            + "    <Level name=\"Store City\"    column=\"store_city\"    uniqueMembers=\"false\"/>\n"
            + "    <Level name=\"Store Type\"    column=\"store_type\"    uniqueMembers=\"false\"/>\n"
            + "    <Level name=\"Store Name\"    column=\"store_name\"    uniqueMembers=\"true\"/>\n"
            + "  </Hierarchy>\n"
            + "</Dimension>"));
         */

        public TestAggregationManagerModifier1(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Store2")
                    .foreignKey("store_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .primaryKey("store_id")
                            .allMemberName("All Stores")
                            .relation(new TableR("store"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Store Country")
                                    .column("store_country")
                                    .uniqueMembers(true)
                                .build(),
                                LevelRBuilder.builder()
                                    .name("Store State")
                                    .column("store_state")
                                    .uniqueMembers(true)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Store City")
                                    .column("store_city")
                                    .uniqueMembers(false)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Store Type")
                                    .column("store_type")
                                    .uniqueMembers(false)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Store Name")
                                    .column("store_name")
                                    .uniqueMembers(true)
                                    .build()
                            ))
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class TestAggregationManagerModifier2 extends RDbMappingSchemaModifier {

        /*
            "<Dimension name=\"Promotions\" foreignKey=\"promotion_id\">\n"
            + "  <Hierarchy hasAll=\"true\" allMemberName=\"All Promotions\" primaryKey=\"promotion_id\" defaultMember=\"[All Promotions]\">\n"
            + "    <Table name=\"promotion\"/>\n"
            + "    <Level name=\"Promotion Name\" column=\"promotion_name\" uniqueMembers=\"true\">\n"
            + "      <KeyExpression><SQL>ERROR_TEST_FUNCTION_NAME("
            + colName + ")</SQL></KeyExpression>\n"
            + "    </Level>\n"
            + "  </Hierarchy>\n"
            + "</Dimension>", false));
         */
        private final StringBuilder colName;

        public TestAggregationManagerModifier2(MappingSchema mappingSchema, final StringBuilder colName) {
            super(mappingSchema);
            this.colName = colName;
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Promotions")
                    .foreignKey("promotion_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .allMemberName("All Promotions")
                            .primaryKey("promotion_id")
                            .defaultMember("[All Promotions]")
                            .relation(new TableR("promotion"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Promotion Name")
                                    .column("promotion_name")
                                    .uniqueMembers(true)
                                    .keyExpression(ExpressionViewRBuilder.builder()
                                        .sqls(List.of(
                                            SQLRBuilder.builder()
                                                .content("ERROR_TEST_FUNCTION_NAME(" + colName + ")")
                                                .build()
                                        ))
                                        .build())
                                    .build()

                            ))
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class DrillThroughTestModifier1 extends RDbMappingSchemaModifier {

        /*
            "  <Dimension name=\"Store2\" foreignKey=\"store_id\">\n"
            + "    <Hierarchy hasAll=\"true\" primaryKey=\"store_id\">\n"
            + "      <Table name=\"store_ragged\"/>\n"
            + "      <Level name=\"Store Country\" column=\"store_country\" uniqueMembers=\"true\"/>\n"
            + "      <Level name=\"Store Id\" column=\"store_id\" captionColumn=\"store_name\" uniqueMembers=\"true\" type=\"Integer\"/>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "  <Dimension name=\"Store3\" foreignKey=\"store_id\">\n"
            + "    <Hierarchy hasAll=\"true\" primaryKey=\"store_id\">\n"
            + "      <Table name=\"store\"/>\n"
            + "      <Level name=\"Store Country\" column=\"store_country\" uniqueMembers=\"true\"/>\n"
            + "      <Level name=\"Store Id\" column=\"store_id\" captionColumn=\"store_name\" uniqueMembers=\"true\" type=\"Numeric\"/>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"));
         */


        public DrillThroughTestModifier1(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Store2")
                    .foreignKey("store_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .primaryKey("store_id")
                            .relation(new TableR("store_ragged"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Store Country")
                                    .column("store_country")
                                    .uniqueMembers(true)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Store Id")
                                    .column("store_id")
                                    .captionColumn("store_name")
                                    .uniqueMembers(true)
                                    .type(TypeEnum.INTEGER)
                                    .build()
                            ))
                            .build()
                    ))
                    .build());
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Store3")
                    .foreignKey("store_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .primaryKey("store_id")
                            .relation(new TableR("store"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Store Country")
                                    .column("store_country")
                                    .uniqueMembers(true)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Store Id")
                                    .column("store_id")
                                    .captionColumn("store_name")
                                    .uniqueMembers(true)
                                    .type(TypeEnum.NUMERIC)
                                    .build()
                            ))
                            .build()
                    ))
                    .build());


            }
            return result;
        }
    }

    public static class DrillThroughTestModifier2 extends RDbMappingSchemaModifier {

        /*
                "  <Dimension name=\"Store2\" foreignKey=\"store_id\">\n"
                + "    <Hierarchy hasAll=\"true\" allMemberName=\"All Stores\" >\n"
                + "      <Table name=\"store_ragged\"/>\n"
                + "      <Level name=\"Store Id\" column=\"store_id\" nameColumn=\"store_id\" ordinalColumn=\"region_id\" uniqueMembers=\"true\">\n"
                + "     </Level>"
                + "    </Hierarchy>\n"
                + "  </Dimension>\n"));
         */


        public DrillThroughTestModifier2(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Store2")
                    .foreignKey("store_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .allMemberName("All Stores")
                            .relation(new TableR("store_ragged"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Store Id")
                                    .column("store_id")
                                    .nameColumn("store_id")
                                    .ordinalColumn("region_id")
                                    .uniqueMembers(true)
                                    .build()
                            ))
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class DrillThroughTestModifier3 extends RDbMappingSchemaModifier {

        /*
            "  <Dimension name=\"Education Level2\" foreignKey=\"customer_id\">\n"
            + "    <Hierarchy hasAll=\"true\" primaryKey=\"customer_id\">\n"
            + "      <Table name=\"customer\"/>\n"
            + "      <Level name=\"Education Level but with a very long name that will be too long if converted directly into a column\" column=\"education\" uniqueMembers=\"true\"/>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>",
         */


        public DrillThroughTestModifier3(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Education Level2")
                    .foreignKey("customer_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .primaryKey("customer_id")
                            .relation(new TableR("customer"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Education Level but with a very long name that will be too long if converted directly into a column")
                                    .column("education")
                                    .uniqueMembers(true)
                                    .build()
                            ))
                            .build()
                    ))
                    .build());


            }
            return result;
        }
    }

    public static class RaggedHierarchyTestModifier1 extends RDbMappingSchemaModifier {

        /*
                "<Dimension name=\"Gender4\" foreignKey=\"customer_id\">\n"
                + "    <Hierarchy hasAll=\"true\" allMemberName=\"All Gender\" primaryKey=\"customer_id\">\n"
                + "      <Table name=\"customer\"/>\n"
                + "      <Level name=\"Gender\" column=\"gender\" uniqueMembers=\"true\" hideMemberIf=\"IfBlankName\">\n"
                + "         <NameExpression> "
                + " <SQL dialect='generic'> "
                    +           "case \"gender\" "
                    +           "when 'F' then ' ' "
                    +           "when 'M' then 'M' "
                    + " end "
                    + "</SQL> "
                    + "</NameExpression>  "
                    + "      </Level>"
                    + "    </Hierarchy>\n"
                    + "  </Dimension>"));
         */


        public RaggedHierarchyTestModifier1(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Gender4")
                    .foreignKey("customer_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .allMemberName("All Gender")
                            .primaryKey("customer_id")
                            .relation(new TableR("customer"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Gender")
                                    .column("gender")
                                    .uniqueMembers(true)
                                    .hideMemberIf(HideMemberIfEnum.IF_BLANK_NAME)
                                    .nameExpression(ExpressionViewRBuilder.builder()
                                        .sqls(List.of(
                                            SQLRBuilder.builder()
                                                .dialect("generic")
                                                .content("case \"gender\" when 'F' then ' ' when 'M' then 'M' ")
                                                .build()
                                        ))
                                        .build())
                                    .build()
                            ))
                            .build()
                    ))
                    .build());


            }
            return result;
        }
    }

    public static class RaggedHierarchyTestModifier2 extends RDbMappingSchemaModifier {

        /*
            "<Dimension name=\"Store\" foreignKey=\"store_id\">\n"
            + "    <Hierarchy hasAll=\"true\" primaryKey=\"store_id\">\n"
            + "      <Table name=\"store_ragged\"/>\n"
            + "      <Level name=\"Store Country\" column=\"store_country\" uniqueMembers=\"true\"\n"
            + "          hideMemberIf=\"Never\"/>\n"
            + "      <Level name=\"Store State\" column=\"store_state\" uniqueMembers=\"true\"\n"
            + "          hideMemberIf=\"IfParentsName\"/>\n"
            + "      <Level name=\"Store City\" column=\"store_city\" uniqueMembers=\"false\"\n"
            + "          hideMemberIf=\"IfBlankName\"/>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>"));
         */


        public RaggedHierarchyTestModifier2(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales Ragged".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Store")
                    .foreignKey("store_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .primaryKey("store_id")
                            .relation(new TableR("store_ragged"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Store Country")
                                    .column("store_country")
                                    .uniqueMembers(true)
                                    .hideMemberIf(HideMemberIfEnum.NEVER)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Store State")
                                    .column("store_state")
                                    .uniqueMembers(true)
                                    .hideMemberIf(HideMemberIfEnum.IF_PARENTS_NAME)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("Store City")
                                    .column("store_city")
                                    .uniqueMembers(false)
                                    .hideMemberIf(HideMemberIfEnum.IF_BLANK_NAME)
                                    .build()

                            ))
                            .build()
                    ))
                    .build());


            }
            return result;
        }
    }

    public static class RolapResultTestModifier extends RDbMappingSchemaModifier {

        /*
            "<Dimension name=\"Promotions2\" foreignKey=\"promotion_id\">\n"
            + "  <Hierarchy hasAll=\"false\" primaryKey=\"promotion_id\">\n"
            + "    <Table name=\"promotion\"/>\n"
            + "    <Level name=\"Promotion2 Name\" column=\"promotion_name\" uniqueMembers=\"true\"/>\n"
            + "  </Hierarchy>\n"
            + "</Dimension>"));
         */


        public RolapResultTestModifier(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Promotions2")
                    .foreignKey("promotion_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(false)
                            .primaryKey("promotion_id")
                            .relation(new TableR("promotion"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Promotion2 Name")
                                    .column("promotion_name")
                                    .uniqueMembers(true)
                                    .build()
                            ))
                            .build()
                    ))
                    .build());


            }
            return result;
        }
    }

    public static class VirtualCubeTestModifier1 extends RDbMappingSchemaModifier {

        /*
            "  <CalculatedMember name=\"Shipped per Ordered\" dimension=\"Measures\">\n"
            + "    <Formula>[Measures].[Units Shipped] / [Measures].[Unit Sales]</Formula>\n"
            + "    <CalculatedMemberProperty name=\"FORMAT_STRING\" value=\"#.0%\"/>\n"
            + "  </CalculatedMember>\n"));
        */
        public VirtualCubeTestModifier1(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCalculatedMember> virtualCubeCalculatedMember(MappingVirtualCube virtualCube) {
            List<MappingCalculatedMember> result = new ArrayList<>();
            result.addAll(super.virtualCubeCalculatedMember(virtualCube));
            if ("Warehouse and Sales".equals(virtualCube.name())) {
                result.add(CalculatedMemberRBuilder.builder()
                    .name("Shipped per Ordered")
                    .dimension("Measures")
                    .formulaElement(FormulaRBuilder.builder()
                        .cdata("[Measures].[Units Shipped] / [Measures].[Unit Sales]")
                        .build())
                    .calculatedMemberProperties(List.of(
                        CalculatedMemberPropertyRBuilder.builder()
                            .name("FORMAT_STRING")
                            .value("#.0%")
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class VirtualCubeTestModifier2 extends RDbMappingSchemaModifier {

        /*
            <VirtualCubeMeasure cubeName=\"Sales\" name=\"[Measures].[Customer Count]\"/>"
        */
        public VirtualCubeTestModifier2(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingVirtualCubeMeasure> virtualCubeVirtualCubeMeasure(MappingVirtualCube virtualCube) {
            List<MappingVirtualCubeMeasure> result = new ArrayList<>();
            result.addAll(super.virtualCubeVirtualCubeMeasure(virtualCube));
            if ("Warehouse and Sales".equals(virtualCube.name())) {
                result.add(VirtualCubeMeasureRBuilder.builder()
                    .cubeName("Sales")
                    .name("[Measures].[Customer Count]")
                    .build());
            }
            return result;
        }
    }

    public static class VirtualCubeTestModifier3 extends RDbMappingSchemaModifier {

        /*
            "<Schema name=\"FoodMart\">"
          + "<Dimension type=\"TimeDimension\" highCardinality=\"false\" name=\"Time\">"
          + "<Hierarchy visible=\"true\" hasAll=\"false\" primaryKey=\"time_id\">"
          + "<Table name=\"time_by_day\">"
          + "</Table>"
          + "<Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\" levelType=\"TimeYears\">"
          + "</Level>"
          + "<Level name=\"Quarter\" column=\"quarter\" type=\"String\" uniqueMembers=\"false\" levelType=\"TimeQuarters\">"
          + "</Level>"
          + "</Hierarchy>"
          + "</Dimension>"
          + "<Cube name=\"Sales\" visible=\"true\" defaultMeasure=\"Unit Sales\" >"
          + "<Table name=\"sales_fact_1997\">"
          + "<AggName name=\"agg_c_special_sales_fact_1997\">"
          + "<AggFactCount column=\"FACT_COUNT\">"
          + "</AggFactCount>"
          + "<AggMeasure column=\"UNIT_SALES_SUM\" name=\"[Measures].[Unit Sales]\">"
          + "</AggMeasure>"
          + "<AggLevel column=\"TIME_YEAR\" name=\"[Time].[Year]\">"
          + "</AggLevel>"
          + "</AggName>"
          + "</Table>"
          + "<DimensionUsage source=\"Time\" name=\"Time\" foreignKey=\"time_id\" highCardinality=\"false\">"
          + "</DimensionUsage>"
          + "<Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\">"
          + "</Measure>"
          + "<CalculatedMember name=\"recurse\" dimension=\"Measures\" visible=\"true\">"
          + "<Formula>"
          + "<![CDATA[(CoalesceEmpty((Measures.[Unit Sales], [Time].CurrentMember ) ,"
          + "(Measures.[recurse],[Time].CurrentMember.PrevMember)))]]>"
          + "</Formula>"
          + "</CalculatedMember>"
          + "</Cube>"
          + "<VirtualCube enabled=\"true\" name=\"Warehouse and Sales\" defaultMeasure=\"Store Sales\" visible=\"true\">"
          + "<VirtualCubeDimension visible=\"true\" highCardinality=\"false\" name=\"Time\">"
          + "</VirtualCubeDimension>"
          + "<VirtualCubeMeasure cubeName=\"Sales\" name=\"[Measures].[recurse]\" visible=\"true\">"
          + "</VirtualCubeMeasure>"
          + "</VirtualCube>"
          + "</Schema>";
         */
        public VirtualCubeTestModifier3(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {

            return SchemaRBuilder.builder()
                .name("FoodMart")
                .dimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .type(DimensionTypeEnum.TIME_DIMENSION)
                        .highCardinality(false)
                        .name("Time")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .visible(true)
                                .hasAll(false)
                                .primaryKey("time_id")
                                .relation(new TableR("time_by_day"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Year")
                                        .column("the_year")
                                        .type(TypeEnum.NUMERIC)
                                        .uniqueMembers(true)
                                        .levelType(LevelTypeEnum.TIME_YEARS)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Quarter")
                                        .column("quarter")
                                        .type(TypeEnum.STRING)
                                        .uniqueMembers(false)
                                        .levelType(LevelTypeEnum.TIME_QUARTERS)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Sales")
                        .visible(true)
                        .defaultMeasure("Unit Sales")
                        .fact(new TableR("sales_fact_1997",
                            List.of(),
                            List.of(
                                AggNameRBuilder.builder()
                                    .name("agg_c_special_sales_fact_1997")
                                    .aggFactCount(
                                        AggColumnNameRBuilder.builder()
                                            .column("FACT_COUNT")
                                            .build())
                                    .aggMeasures(List.of(
                                        AggMeasureRBuilder.builder()
                                            .column("UNIT_SALES_SUM")
                                            .name("[Measures].[Unit Sales]")
                                            .build()
                                    ))
                                    .aggLevels(List.of(
                                        AggLevelRBuilder.builder()
                                            .column("TIME_YEAR")
                                            .name("[Time].[Year]")
                                            .build()
                                    ))
                                    .build()
                            )))
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .source("Time")
                                .name("Time")
                                .foreignKey("time_id")
                                .highCardinality(false)
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales")
                                .column("unit_sales")
                                .aggregator("sum")
                                .build()
                        ))
                        .calculatedMembers(List.of(
                            CalculatedMemberRBuilder.builder()
                                .name("recurse")
                                .dimension("Measures")
                                .visible(true)
                                .formulaElement(FormulaRBuilder.builder()
                                    .cdata("(CoalesceEmpty((Measures.[Unit Sales], [Time].CurrentMember ) ,(Measures.[recurse],[Time].CurrentMember.PrevMember)))")
                                    .build())
                                .build()
                        ))
                        .build()
                ))
                .virtualCubes(List.of(
                    VirtualCubeRBuilder.builder()
                        .enabled(true)
                        .name("Warehouse and Sales")
                        .defaultMeasure("Store Sales")
                        .visible(true)
                        .virtualCubeDimensions(List.of(
                            VirtualCubeDimensionRBuilder.builder()
                                .visible(true)
                                .highCardinality(false)
                                .name("Time")
                                .build()
                        ))
                        .virtualCubeMeasures(List.of(
                            VirtualCubeMeasureRBuilder.builder()
                                .cubeName("Sales")
                                .name("[Measures].[recurse]")
                                .visible(true)
                                .build()
                        ))
                        .build()
                ))
                .build();
        }
    }

    public static class SqlQueryTestModifier extends RDbMappingSchemaModifier {

        /*
                        " <Measure name=\"Avg Sales\" column=\"unit_sales\" aggregator=\"avg\"\n"
            + " formatString=\"#.###\"/>",

        */
        public SqlQueryTestModifier(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingMeasure> cubeMeasures(MappingCube cube) {
            List<MappingMeasure> result = new ArrayList<>();
            result.addAll(super.cubeMeasures(cube));
            if ("Sales".equals(cube.name())) {
                result.add(MeasureRBuilder.builder()
                    .name("Avg Sales")
                    .column("unit_sales")
                    .aggregator("avg")
                    .formatString("#.###")
                    .build());
            }
            return result;
        }
    }


    public static class CompoundSlicerTestModifier1 extends RDbMappingSchemaModifier {

        /*
                "<Measure name='Unit Sales Foo Bar' column='unit_sales'\n"
                        + "    aggregator='sum' formatString='Standard' formatter='"
                        + UdfTest.FooBarCellFormatter.class.getName()
                        + "'/>";
        */
        public CompoundSlicerTestModifier1(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingMeasure> cubeMeasures(MappingCube cube) {
            List<MappingMeasure> result = new ArrayList<>();
            result.addAll(super.cubeMeasures(cube));
            if ("Sales".equals(cube.name())) {
                result.add(MeasureRBuilder.builder()
                    .name("Unit Sales Foo Bar")
                    .column("unit_sales")
                    .aggregator("sum")
                    .formatter(UdfTest.FooBarCellFormatter.class.getName())
                    .build());
            }
            return result;
        }
    }

    public static class CompoundSlicerTestModifier2 extends RDbMappingSchemaModifier {

        /*
                "<Measure name='Avg Unit Sales' aggregator='avg' column='unit_sales'/>\n"
                        + "<Measure name='Count Unit Sales' aggregator='count' column='unit_sales'/>\n"
                        + "<Measure name='Sum Unit Sales' aggregator='sum' column='unit_sales'/>\n",
        */
        public CompoundSlicerTestModifier2(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingMeasure> cubeMeasures(MappingCube cube) {
            List<MappingMeasure> result = new ArrayList<>();
            result.addAll(super.cubeMeasures(cube));
            if ("Sales".equals(cube.name())) {
                result.add(MeasureRBuilder.builder()
                    .name("Avg Unit Sales")
                    .aggregator("avg")
                    .column("unit_sales")
                    .build());
                result.add(MeasureRBuilder.builder()
                    .name("Count Unit Sales")
                    .aggregator("count")
                    .column("unit_sales")
                    .build());
                result.add(MeasureRBuilder.builder()
                    .name("Sum Unit Sales")
                    .aggregator("sum")
                    .column("unit_sales")
                    .build());
            }
            return result;
        }
    }

    public static class CompoundSlicerTestModifier3 extends RDbMappingSchemaModifier {

        /*
                            "<VirtualCubeMeasure cubeName=\"Sales\" name=\"[Measures].[Customer Count]\"/>\n",
                " <CalculatedMember name=\"Unit Sales by Customer\" dimension=\"Measures\">"
                        + "<Formula>Measures.[Unit Sales]/Measures.[Customer Count]</Formula>"
                        + "</CalculatedMember>",

        */
        public CompoundSlicerTestModifier3(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingVirtualCubeMeasure> virtualCubeVirtualCubeMeasure(MappingVirtualCube virtualCube) {
            List<MappingVirtualCubeMeasure> result = new ArrayList<>();
            result.addAll(super.virtualCubeVirtualCubeMeasure(virtualCube));
            if ("Warehouse and Sales".equals(virtualCube.name())) {
                result.add(VirtualCubeMeasureRBuilder.builder()
                    .cubeName("Sales")
                    .name("[Measures].[Customer Count]")
                    .build());
            }
            return result;
        }

        @Override
        protected List<MappingCalculatedMember> virtualCubeCalculatedMember(MappingVirtualCube virtualCube) {
            List<MappingCalculatedMember> result = new ArrayList<>();
            result.addAll(super.virtualCubeCalculatedMember(virtualCube));
            if ("Warehouse and Sales".equals(virtualCube.name())) {
                result.add(CalculatedMemberRBuilder.builder()
                    .name("Unit Sales by Customer")
                    .dimension("Measures")
                    .formulaElement(FormulaRBuilder.builder()
                        .cdata("Measures.[Unit Sales]/Measures.[Customer Count]")
                        .build())
                    .build());
            }
            return result;
        }

    }

    public static class XmlaHandlerTypeTestModifier extends RDbMappingSchemaModifier {

        /*
            "<Measure name='typeMeasure' " + aggregator + datatype + ">\n"
            + "  <MeasureExpression>\n"
            + "  <SQL dialect='generic'>\n"
            + expression
            + "  </SQL></MeasureExpression></Measure>",
        */
        private final String expression;
        private final String type;
        public XmlaHandlerTypeTestModifier(MappingSchema mappingSchema, String expression, String type) {
            super(mappingSchema);
            this.expression = expression;
            this.type = type;
        }

        @Override
        protected List<MappingMeasure> cubeMeasures(MappingCube cube) {
            List<MappingMeasure> result = new ArrayList<>();
            result.addAll(super.cubeMeasures(cube));
            if ("Sales".equals(cube.name())) {
                //String datatype = "";
                String aggregator = "sum";
                if (type != null) {
                    if (type.equals("String")) {
                        aggregator = "max";
                    }
                }

                result.add(MeasureRBuilder.builder()
                    .name("typeMeasure")
                    .aggregator(aggregator)
                    .datatype(type != null ? MeasureDataTypeEnum.fromValue(type) : null)
                    .column("unit_sales")
                    .measureExpression(ExpressionViewRBuilder.builder()
                        .sqls(List.of(SQLRBuilder.builder()
                            .content(expression)
                            .build()))
                        .build())
                    .build());
            }
            return result;
        }
    }

    public static class MultipleHierarchyTestModifier1 extends RDbMappingSchemaModifier {

        /*
            "<Dimension name=\"NuStore\" foreignKey=\"store_id\">\n"
            + "<Hierarchy hasAll=\"true\" primaryKey=\"store_id\">\n"
            + "  <Table name=\"store\"/>\n"
            + "  <Level name=\"NuStore Country\" column=\"store_country\" uniqueMembers=\"true\"/>\n"
            + "  <Level name=\"NuStore State\" column=\"store_state\" uniqueMembers=\"true\"/>\n"
            + "  <Level name=\"NuStore City\" column=\"store_city\" uniqueMembers=\"false\"/>\n"
            + "  <Level name=\"NuStore Name\" column=\"store_name\" uniqueMembers=\"true\">\n"
            + "    <Property name=\"NuStore Type\" column=\"store_type\"/>\n"
            + "    <Property name=\"NuStore Manager\" column=\"store_manager\"/>\n"
            + "    <Property name=\"NuStore Sqft\" column=\"store_sqft\" type=\"Numeric\"/>\n"
            + "    <Property name=\"Grocery Sqft\" column=\"grocery_sqft\" type=\"Numeric\"/>\n"
            + "    <Property name=\"Frozen Sqft\" column=\"frozen_sqft\" type=\"Numeric\"/>\n"
            + "    <Property name=\"Meat Sqft\" column=\"meat_sqft\" type=\"Numeric\"/>\n"
            + "    <Property name=\"Has coffee bar\" column=\"coffee_bar\" type=\"Boolean\"/>\n"
            + "    <Property name=\"Street address\" column=\"store_street_address\" type=\"String\"/>\n"
            + "  </Level>\n"
            + "</Hierarchy>\n"
            + "<Hierarchy caption=\"NuStore2\" name=\"NuStore2\" allMemberName=\"All NuStore2s\" hasAll=\"true\" primaryKey=\"NuStore_id\">\n"
            + "  <Table name=\"store\"/>\n"
            + "  <Level name=\"NuStore City\" column=\"store_city\" uniqueMembers=\"false\"/>\n"
            + "  <Level name=\"NuStore Name\" column=\"store_name\"  uniqueMembers=\"true\">\n"
            + "    <Property name=\"NuStore Type\" column=\"store_type\"/>\n"
            + "    <Property name=\"NuStore Manager\" column=\"store_manager\"/>\n"
            + "    <Property name=\"NuStore Sqft\" column=\"store_sqft\" type=\"Numeric\"/>\n"
            + "    <Property name=\"Grocery Sqft\" column=\"grocery_sqft\" type=\"Numeric\"/>\n"
            + "    <Property name=\"Frozen Sqft\" column=\"frozen_sqft\" type=\"Numeric\"/>\n"
            + "    <Property name=\"Meat Sqft\" column=\"meat_sqft\" type=\"Numeric\"/>\n"
            + "    <Property name=\"Has coffee bar\" column=\"coffee_bar\" type=\"Boolean\"/>\n"
            + "    <Property name=\"Street address\" column=\"store_street_address\" type=\"String\"/>\n"
            + "  </Level>\n"
            + "</Hierarchy>\n"
            + "</Dimension>"));
         */


        public MultipleHierarchyTestModifier1(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("NuStore")
                    .foreignKey("store_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .primaryKey("store_id")
                            .relation(new TableR("store"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("NuStore Country")
                                    .column("store_country")
                                    .uniqueMembers(true)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("NuStore State")
                                    .column("store_state")
                                    .uniqueMembers(true)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("NuStore City")
                                    .column("store_city")
                                    .uniqueMembers(false)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("NuStore Name")
                                    .column("store_name")
                                    .uniqueMembers(true)
                                    .properties(List.of(
                                        PropertyRBuilder.builder()
                                            .name("NuStore Type")
                                            .column("store_type")
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("NuStore Manager")
                                            .column("store_manager")
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("NuStore Sqft")
                                            .column("store_sqft")
                                            .type(PropertyTypeEnum.NUMERIC)
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("Grocery Sqft")
                                            .column("grocery_sqft")
                                            .type(PropertyTypeEnum.NUMERIC)
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("Frozen Sqft")
                                            .column("frozen_sqft")
                                            .type(PropertyTypeEnum.NUMERIC)
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("Meat Sqft")
                                            .column("meat_sqft")
                                            .type(PropertyTypeEnum.NUMERIC)
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("Has coffee bar")
                                            .column("coffee_bar")
                                            .type(PropertyTypeEnum.BOOLEAN)
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("Street address")
                                            .column("store_street_address")
                                            .type(PropertyTypeEnum.STRING)
                                            .build()

                                        ))
                                    .build()

                            ))
                            .build(),
                        HierarchyRBuilder.builder()
                            .caption("NuStore2")
                            .name("NuStore2")
                            .allMemberName("All NuStore2s")
                            .hasAll(true)
                            .primaryKey("NuStore_id")
                            .relation(new TableR("store"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("NuStore City")
                                    .column("store_city")
                                    .uniqueMembers(false)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("NuStore Name")
                                    .column("store_name")
                                    .uniqueMembers(true)
                                    .properties(List.of(
                                        PropertyRBuilder.builder()
                                            .name("NuStore Type")
                                            .column("store_type")
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("NuStore Manager")
                                            .column("store_manager")
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("NuStore Sqft")
                                            .column("store_sqft")
                                            .type(PropertyTypeEnum.NUMERIC)
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("Grocery Sqft")
                                            .column("grocery_sqft")
                                            .type(PropertyTypeEnum.NUMERIC)
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("Frozen Sqft")
                                            .column("frozen_sqft")
                                            .type(PropertyTypeEnum.NUMERIC)
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("Meat Sqft")
                                            .column("meat_sqft")
                                            .type(PropertyTypeEnum.NUMERIC)
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("Has coffee bar")
                                            .column("coffee_bar")
                                            .type(PropertyTypeEnum.BOOLEAN)
                                            .build(),
                                        PropertyRBuilder.builder()
                                            .name("Street address")
                                            .column("store_street_address")
                                            .type(PropertyTypeEnum.STRING)
                                            .build()
                                    ))
                            .build()
                            )).build()
                    )).build()
                );
            }
            return result;
        }
    }

    public static class MultipleHierarchyTestModifier2 extends RDbMappingSchemaModifier {

        /*
            "<Dimension name=\"NuStore\" foreignKey=\"store_id\">\n"
            + "<Hierarchy name=\"NuStore\" hasAll=\"true\" primaryKey=\"store_id\">\n"
            + "  <Table name=\"store\"/>\n"
            + "  <Level name=\"NuStore Country\" column=\"store_country\" uniqueMembers=\"true\"/>\n"
            + "  <Level name=\"NuStore State\" column=\"store_state\" uniqueMembers=\"true\"/>\n"
            + "  <Level name=\"NuStore City\" column=\"store_city\" uniqueMembers=\"false\"/>\n"
            + "  <Level name=\"NuStore Name\" column=\"store_name\" uniqueMembers=\"true\"/>\n"
            + "</Hierarchy>\n"
            + "<Hierarchy caption=\"NuStore2\" name=\"NuStore2\" allMemberName=\"All NuStore2s\" hasAll=\"true\" primaryKey=\"NuStore_id\">\n"
            + "  <Table name=\"store\"/>\n"
            + "  <Level name=\"NuStore City\" column=\"store_city\" uniqueMembers=\"false\"/>\n"
            + "  <Level name=\"NuStore Name\" column=\"store_name\"  uniqueMembers=\"true\"/>\n"
            + "</Hierarchy>\n"
            + "</Dimension>"));
         */
        public MultipleHierarchyTestModifier2(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("NuStore")
                    .foreignKey("store_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .primaryKey("store_id")
                            .relation(new TableR("store"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("NuStore Country")
                                    .column("store_country")
                                    .uniqueMembers(true)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("NuStore State")
                                    .column("store_state")
                                    .uniqueMembers(true)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("NuStore City")
                                    .column("store_city")
                                    .uniqueMembers(false)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("NuStore Name")
                                    .column("store_name")
                                    .uniqueMembers(true)
                                    .build()
                            ))
                            .build(),
                        HierarchyRBuilder.builder()
                            .caption("NuStore2")
                            .name("NuStore2")
                            .allMemberName("All NuStore2s")
                            .hasAll(true)
                            .primaryKey("NuStore_id")
                            .relation(new TableR("store"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("NuStore City")
                                    .column("store_city")
                                    .uniqueMembers(false)
                                    .build(),
                                LevelRBuilder.builder()
                                    .name("NuStore Name")
                                    .column("store_name")
                                    .uniqueMembers(true)
                                    .build()
                            )).build()
                    )).build()
                );
            }
            return result;
        }
    }

    public static class PerformanceTestModifier1 extends RDbMappingSchemaModifier {

        /*
      "      <Dimension name=\"ACC\" caption=\"Account\" type=\"StandardDimension\" foreignKey=\"customer_id\">\n"
        + "         <Hierarchy hasAll=\"true\" allMemberName=\"All\" primaryKey=\"customer_id\">\n"
        + "            <Table name=\"customer\"/>\n"
        + "            <Level name=\"CODE\" caption=\"Account\" uniqueMembers=\"true\" column=\"account_num\" "
        + "type=\"String\"/>\n"
        + "         </Hierarchy>\n"
        + "      </Dimension>\n"
        + "      <Dimension name=\"Store Name sans All\" type=\"StandardDimension\" foreignKey=\"store_id\">\n"
        + "         <Hierarchy hasAll=\"false\" primaryKey=\"store_id\">\n"
        + "            <Table name=\"store\" />\n"
        + "            <Level name=\"Store Name\" uniqueMembers=\"true\" column=\"store_number\" type=\"Numeric\" "
        + "ordinalColumn=\"store_name\"/>\n"
        + "         </Hierarchy>\n"
        + "      </Dimension>\n",
      "      <CalculatedMember dimension=\"Measures\" name=\"EXP2_4\" formula=\"IIf([ACC].CurrentMember.Level.Ordinal"
        + " = [ACC].[All].Ordinal, Sum([ACC].[All].Children, [Measures].[Unit Sales]),     [Measures].[Unit Sales])"
        + "\"/>\n"
        + "      <CalculatedMember dimension=\"Measures\" name=\"EXP2\" formula=\"IIf(0 &#60; [Measures].[EXP2_4], "
        + "[Measures].[EXP2_4], NULL)\"/>\n" ));
         */

        public PerformanceTestModifier1(MappingSchema mappingSchema) {
            super(mappingSchema);
        }
        @Override
        protected List<MappingCalculatedMember> cubeCalculatedMembers(MappingCube cube) {
            List<MappingCalculatedMember> result = new ArrayList<>();
            result.addAll(super.cubeCalculatedMembers(cube));
            if ("Sales".equals(cube.name())) {
                result.add(CalculatedMemberRBuilder.builder()
                    .dimension("Measures")
                    .name("EXP2_4")
                    .formula("IIf([ACC].CurrentMember.Level.Ordinal = [ACC].[All].Ordinal, Sum([ACC].[All].Children, [Measures].[Unit Sales]), [Measures].[Unit Sales])")
                    .build());
                result.add(CalculatedMemberRBuilder.builder()
                    .dimension("Measures")
                    .name("EXP2")
                    .formula("IIf(0 &#60; [Measures].[EXP2_4], [Measures].[EXP2_4], NULL)")
                    .build());
            }
            return result;
        }
        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                    result.add(PrivateDimensionRBuilder.builder()
                        .name("ACC")
                        .caption("Account")
                        .type(DimensionTypeEnum.STANDARD_DIMENSION)
                        .foreignKey("customer_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .allMemberName("All")
                                .primaryKey("customer_id")
                                .relation(new TableR("customer"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("CODE")
                                        .caption("Account")
                                        .uniqueMembers(true)
                                        .column("account_num")
                                        .type(TypeEnum.STRING)
                                        .build()
                                ))
                                .build()
                        )).build()
                    );
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Store Name sans All")
                    .type(DimensionTypeEnum.STANDARD_DIMENSION)
                    .foreignKey("store_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(false)
                            .primaryKey("store_id")
                            .relation(new TableR("store"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Store Name")
                                    .uniqueMembers(true)
                                    .column("store_number")
                                    .type(TypeEnum.NUMERIC)
                                    .ordinalColumn("store_name")
                                    .build()
                            ))
                            .build()
                    )).build()
                );

            }
            return result;
        }
    }

    public static class Ssas2005CompatibilityTestModifier1 extends RDbMappingSchemaModifier {

        /*
            "<Dimension name=\"Store Type 2\" foreignKey=\"store_id\">"
            + " <Hierarchy name=\"Store Type 2\" hasAll=\"true\" primaryKey=\"store_id\">"
            + " <Table name=\"store\"/>"
            + " <Level name=\"Store Type\" column=\"store_type\" uniqueMembers=\"true\"/>"
            + " </Hierarchy>"
            + "</Dimension>",
            */
        public Ssas2005CompatibilityTestModifier1(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                    result.add(PrivateDimensionRBuilder.builder()
                        .name("Store Type 2")
                        .foreignKey("store_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .name("Store Type 2")
                                .hasAll(true)
                                .primaryKey("store")
                                .relation(new TableR("store"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Store Type")
                                        .column("store_type")
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        )).build()
                    );
            }
            return result;
        }
    }

    public static class Ssas2005CompatibilityTestModifier2 extends RDbMappingSchemaModifier {

        /*
             "<Dimension name=\"SameName\" foreignKey=\"customer_id\">\n"
             + " <Hierarchy hasAll=\"true\" primaryKey=\"id\">\n"
             + " <InlineTable alias=\"sn\">\n"
             + " <ColumnDefs>\n"
             + " <ColumnDef name=\"id\" type=\"Numeric\" />\n"
             + " <ColumnDef name=\"desc\" type=\"String\" />\n"
             + " </ColumnDefs>\n"
             + " <Rows>\n"
             + " <Row>\n"
             + " <Value column=\"id\">1</Value>\n"
             + " <Value column=\"desc\">SameName</Value>\n"
             + " </Row>\n"
             + " </Rows>\n"
             + " </InlineTable>\n"
             + " <Level name=\"SameName\" column=\"desc\" uniqueMembers=\"true\" />\n"
             + " </Hierarchy>\n"
             + "</Dimension>"));
            */
        public Ssas2005CompatibilityTestModifier2(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                MappingInlineTable i = InlineTableRBuilder.builder()
                    .alias("sn")
                    .columnDefs(List.of(
                        ColumnDefRBuilder.builder()
                            .name("id")
                            .type(TypeEnum.NUMERIC)
                            .build(),
                        ColumnDefRBuilder.builder()
                            .name("desc")
                            .type(TypeEnum.STRING)
                            .build()
                    ))
                    .rows(List.of(
                        RowRBuilder.builder()
                            .values(List.of(
                                ValueRBuilder.builder()
                                    .column("id")
                                    .content("1")
                                    .build(),
                                ValueRBuilder.builder()
                                    .column("desc")
                                    .content("SameName")
                                    .build()
                            ))
                            .build()
                    ))
                    .build();
                result.add(PrivateDimensionRBuilder.builder()
                    .name("SameName")
                    .foreignKey("customer_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .primaryKey("id")
                            .relation(i)
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("SameName")
                                    .column("desc")
                                    .uniqueMembers(true)
                                    .build()
                            ))
                            .build()
                    )).build()
                );
            }
            return result;
        }
    }

    public static class Ssas2005CompatibilityTestModifier3 extends RDbMappingSchemaModifier {

        /*
                "  <Dimension name=\"Customer Last Name\" "
                + "foreignKey=\"customer_id\">\n"
                + "    <Hierarchy hasAll=\"true\" allMemberName=\"All Customers\""
                + " primaryKey=\"customer_id\">\n"
                + "      <Table name=\"customer\"/>\n"
                + "      <Level name=\"Last Name\" column=\"lname\" keyColumn=\"customer_id\" uniqueMembers=\"true\"/>\n"
                + "    </Hierarchy>\n"
                + "  </Dimension>\n"));
            */
        public Ssas2005CompatibilityTestModifier3(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Customer Last Name")
                    .foreignKey("customer_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .allMemberName("All Customers")
                            .primaryKey("customer_id")
                            .relation(new TableR("customer"))

                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Last Name")
                                    .column("lname")
                                    //.keyColumn("customer_id")
                                    .uniqueMembers(true)
                                    .build()
                            ))
                            .build()
                    )).build()
                );
            }
            return result;
        }
    }

    public static class Ssas2005CompatibilityTestModifier4 extends RDbMappingSchemaModifier {

        /*
            final String cubeName = "Sales_inline";
            "<Cube name=\"" + cubeName + "\">\n"
            + "  <Table name=\"sales_fact_1997\"/>\n"
            + "  <DimensionUsage name=\"Time\" source=\"Time\" foreignKey=\"time_id\"/>\n"
            + "  <Dimension name=\"Alternative Promotion\" foreignKey=\"promotion_id\">\n"
            + "    <Hierarchy hasAll=\"true\" primaryKey=\"promo_id\">\n"
            + "      <InlineTable alias=\"alt_promotion\">\n"
            + "        <ColumnDefs>\n"
            + "          <ColumnDef name=\"promo_id\" type=\"Numeric\"/>\n"
            + "          <ColumnDef name=\"promo_name\" type=\"String\"/>\n"
            + "        </ColumnDefs>\n"
            + "        <Rows>\n"
            + "          <Row>\n"
            + "            <Value column=\"promo_id\">0</Value>\n"
            + "            <Value column=\"promo_name\">Promo0</Value>\n"
            + "          </Row>\n"
            + "          <Row>\n"
            + "            <Value column=\"promo_id\">1</Value>\n"
            + "          </Row>\n"
            + "        </Rows>\n"
            + "      </InlineTable>\n"
            + "      <Level name=\"Alternative Promotion\" column=\"promo_name\" uniqueMembers=\"true\"/> \n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"Standard\" visible=\"false\"/>\n"
            + "  <Measure name=\"Store Sales\" column=\"store_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"#,###.00\"/>\n"
            + "</Cube>",
            */
        public Ssas2005CompatibilityTestModifier4(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCube> schemaCubes(MappingSchema mappingSchemaOriginal) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(mappingSchemaOriginal));
            result.add(CubeRBuilder.builder()
                .name("Sales_inline")
                .fact(new TableR("sales_fact_1997"))
                .dimensionUsageOrDimensions(List.of(
                    DimensionUsageRBuilder.builder()
                        .name("Time")
                        .source("Time")
                        .foreignKey("time_id")
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .name("Alternative Promotion")
                        .foreignKey("promotion_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("promo_id")
                                .relation(InlineTableRBuilder.builder()
                                    .alias("alt_promotion")
                                    .columnDefs(List.of(
                                        ColumnDefRBuilder.builder()
                                            .name("promo_id")
                                            .type(TypeEnum.NUMERIC)
                                            .build(),
                                        ColumnDefRBuilder.builder()
                                            .name("promo_name")
                                            .type(TypeEnum.STRING)
                                            .build()
                                    ))
                                    .rows(List.of(
                                        RowRBuilder.builder()
                                            .values(List.of(
                                                ValueRBuilder.builder()
                                                    .column("promo_id")
                                                    .content("0")
                                                    .build(),
                                                ValueRBuilder.builder()
                                                    .column("promo_name")
                                                    .content("Promo0")
                                                    .build()
                                            ))
                                            .build(),
                                        RowRBuilder.builder()
                                            .values(List.of(
                                                ValueRBuilder.builder()
                                                    .column("promo_id")
                                                    .content("1")
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build())
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Alternative Promotion")
                                        .column("promo_name")
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Unit Sales")
                        .column("unit_sales")
                        .aggregator("sum")
                        .formatString("Standard")
                        .visible(false)
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Store Sales")
                        .column("store_sales")
                        .aggregator("sum")
                        .formatString("#,###.00")
                        .build()
                ))
                .build());
            return result;
        }
    }

    public static class PerformanceTestModifier2 extends RDbMappingSchemaModifier {

        /*
          "<Dimension name=\"Gender%d \" foreignKey=\"customer_id\">"
            + "  <Hierarchy hasAll=\"true\" allMemberName=\"All Gender\" primaryKey=\"customer_id\">"
            + "    <Table name=\"customer\"/>"
            + "    <Level name=\"Gender\" column=\"gender\" uniqueMembers=\"true\"/>"
            + "  </Hierarchy>"
            + "</Dimension>"         */
        public PerformanceTestModifier2(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                for (int i = 0; i < 1000; i++) {
                    result.add(PrivateDimensionRBuilder.builder()
                        .name(new StringBuilder("Gender").append(i).toString())
                        .foreignKey("customer_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .allMemberName("All Gender")
                                .primaryKey("customer_id")
                                .relation(new TableR("customer"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Gender")
                                        .column("gender")
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        )).build()
                    );
                }
            }
            return result;
        }
    }

    public static class PerformanceTestModifier3 extends RDbMappingSchemaModifier {

        public PerformanceTestModifier3(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        /*
        "<Role name='Role1'>\n"
          + "  <SchemaGrant access='none'>\n"
          + "    <CubeGrant cube='Sales' access='all'>\n"
          + "      <HierarchyGrant hierarchy='[Store Type]' access='custom' rollupPolicy='partial'>\n"
          + "        <MemberGrant member='[Store Type].[All Store Types]' access='all'/>\n"
          + "        <MemberGrant member='[Store Type].[Supermarket]' access='none'/>\n"
          + "      </HierarchyGrant>\n"
          + "      <HierarchyGrant hierarchy='[Customers]' access='custom' rollupPolicy='partial'>\n"
          + "        <MemberGrant member='[Customers].[All Customers]' access='all'/>\n"
          + "        <MemberGrant member='[Customers].[USA].[CA].[Los Angeles]' access='none'/>\n"
          + "      </HierarchyGrant>\n"
          + "      <HierarchyGrant hierarchy='[Product]' access='custom' rollupPolicy='partial'>\n"
          + "        <MemberGrant member='[Product].[All Products]' access='all'/>\n"
          + "        <MemberGrant member='[Product].[Drink]' access='none'/>\n"
          + "      </HierarchyGrant>\n"
          + "      <HierarchyGrant hierarchy='[Promotion Media]' access='custom' rollupPolicy='partial'>\n"
          + "        <MemberGrant member='[Promotion Media].[All Media]' access='all'/>\n"
          + "        <MemberGrant member='[Promotion Media].[TV]' access='none'/>\n"
          + "      </HierarchyGrant>\n"
          + "      <HierarchyGrant hierarchy='[Education Level]' access='custom' rollupPolicy='partial'>\n"
          + "        <MemberGrant member='[Education Level].[All Education Levels]' access='all'/>\n"
          + "        <MemberGrant member='[Education Level].[Graduate Degree]' access='none'/>\n"
          + "      </HierarchyGrant>\n"
          + "    </CubeGrant>\n"
          + "  </SchemaGrant>\n"
          + "</Role>\n" );
         */
        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(RoleRBuilder.builder()
                .name("Role1")
                .schemaGrants(List.of(
                    SchemaGrantRBuilder.builder()
                        .access(AccessEnum.NONE)
                        .cubeGrants(List.of(
                            CubeGrantRBuilder.builder()
                                .cube("Sales")
                                .access("all")
                                .hierarchyGrants(List.of(
                                    HierarchyGrantRBuilder.builder()
                                        .hierarchy("[Store Type]")
                                        .access(AccessEnum.CUSTOM)
                                        .rollupPolicy("partial")
                                        .memberGrants(List.of(
                                            MemberGrantRBuilder.builder()
                                                .member("[Store Type].[All Store Types]")
                                                .access(MemberGrantAccessEnum.ALL)
                                                .build(),
                                            MemberGrantRBuilder.builder()
                                                .member("[Store Type].[Supermarket]")
                                                .access(MemberGrantAccessEnum.NONE)
                                                .build()
                                        ))
                                        .build(),
                                    HierarchyGrantRBuilder.builder()
                                        .hierarchy("[Customers]")
                                        .access(AccessEnum.CUSTOM)
                                        .rollupPolicy("partial")
                                        .memberGrants(List.of(
                                            MemberGrantRBuilder.builder()
                                                .member("[Customers].[All Customers]")
                                                .access(MemberGrantAccessEnum.ALL)
                                                .build(),
                                            MemberGrantRBuilder.builder()
                                                .member("[Customers].[USA].[CA].[Los Angeles]")
                                                .access(MemberGrantAccessEnum.NONE)
                                                .build()
                                        ))
                                        .build(),
                                    HierarchyGrantRBuilder.builder()
                                        .hierarchy("[Product]")
                                        .access(AccessEnum.CUSTOM)
                                        .rollupPolicy("partial")
                                        .memberGrants(List.of(
                                            MemberGrantRBuilder.builder()
                                                .member("[Product].[All Products]")
                                                .access(MemberGrantAccessEnum.ALL)
                                                .build(),
                                            MemberGrantRBuilder.builder()
                                                .member("[Product].[Drink]")
                                                .access(MemberGrantAccessEnum.NONE)
                                                .build()
                                        ))
                                        .build(),
                                    HierarchyGrantRBuilder.builder()
                                        .hierarchy("[Promotion Media]")
                                        .access(AccessEnum.CUSTOM)
                                        .rollupPolicy("partial")
                                        .memberGrants(List.of(
                                            MemberGrantRBuilder.builder()
                                                .member("[Promotion Media].[All Media]")
                                                .access(MemberGrantAccessEnum.ALL)
                                                .build(),
                                            MemberGrantRBuilder.builder()
                                                .member("[Promotion Media].[TV]")
                                                .access(MemberGrantAccessEnum.NONE)
                                                .build()
                                        ))
                                        .build(),
                                    HierarchyGrantRBuilder.builder()
                                        .hierarchy("[Education Level]")
                                        .access(AccessEnum.CUSTOM)
                                        .rollupPolicy("partial")
                                        .memberGrants(List.of(
                                            MemberGrantRBuilder.builder()
                                                .member("[Education Level].[All Education Levels]")
                                                .access(MemberGrantAccessEnum.ALL)
                                                .build(),
                                            MemberGrantRBuilder.builder()
                                                .member("[Education Level].[Graduate Degree]")
                                                .access(MemberGrantAccessEnum.NONE)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .build());
            return result;
        }

    }
    public static class PerformanceTestModifier4 extends RDbMappingSchemaModifier {

        /*
      "<UserDefinedFunction name=\"StringMult\" className=\""
        + CounterUdf.class.getName()
        + "\"/>\n",

            */


        public PerformanceTestModifier4(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingUserDefinedFunction> schemaUserDefinedFunctions(MappingSchema schema) {
            List<MappingUserDefinedFunction> result = new ArrayList<>();
            result.addAll(super.schemaUserDefinedFunctions(schema));
            result.add(UserDefinedFunctionRBuilder.builder()
                .name("StringMult")
                .className(PerformanceTest.CounterUdf.class.getName())
                .build());
            return result;
        }
    }

    public static class NativeSetEvaluationTestModifier extends RDbMappingSchemaModifier {

        /*
                <DimensionUsage name="PurchaseDate" source="Time" foreignKey="time_id"/>
            */
        public NativeSetEvaluationTestModifier(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(DimensionUsageRBuilder.builder()
                    .name("PurchaseDate")
                    .source("Time")
                    .foreignKey("time_id")
                    .build());
            }
            return result;
        }
    }

    public static class Olap4jTestModifier extends RDbMappingSchemaModifier {

        /*
            <CalculatedMember name='H1 1997' formula='Aggregate([Time].[1997].[Q1]:[Time].[1997].[Q2])' dimension='Time' />
         */
        public Olap4jTestModifier(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCalculatedMember> cubeCalculatedMembers(MappingCube cube) {
            List<MappingCalculatedMember> result = new ArrayList<>();
            result.addAll(super.cubeCalculatedMembers(cube));
            if ("Sales".equals(cube.name())) {
                result.add(CalculatedMemberRBuilder.builder()
                    .name("H1 1997")
                    .formula("Aggregate([Time].[1997].[Q1]:[Time].[1997].[Q2])")
                    .dimension("Time")
                    .build());
            }
            return result;
        }
    }

    public static class ScenarioTestModifier1 extends RDbMappingSchemaModifier {

        /*
                "<Dimension name='Scenario' foreignKey='time_id'>\n"
                + "  <Hierarchy primaryKey='time_id' hasAll='true'>\n"
                + "    <InlineTable alias='foo'>\n"
                + "      <ColumnDefs>\n"
                + "        <ColumnDef name='foo' type='Numeric'/>\n"
                + "      </ColumnDefs>\n"
                + "      <Rows/>\n"
                + "    </InlineTable>\n"
                + "    <Level name='Scenario' column='foo'/>\n"
                + "  </Hierarchy>\n"
                + "</Dimension>",
                "<Measure name='Atomic Cell Count' aggregator='count'/>"));

            */

        public ScenarioTestModifier1(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingMeasure> cubeMeasures(MappingCube cube) {
            List<MappingMeasure> result = new ArrayList<>();
            result.addAll(super.cubeMeasures(cube));
            if ("Sales".equals(cube.name())) {
                result.add(MeasureRBuilder.builder()
                    .name("Atomic Cell Count")
                    .aggregator("count")
                    .build());
            }
            return result;
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Scenario")
                    .foreignKey("time_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .primaryKey("time_id")
                            .hasAll(true)
                            .relation(InlineTableRBuilder.builder()
                                .alias("foo")
                                .columnDefs(List.of(
                                    ColumnDefRBuilder.builder()
                                        .name("foo")
                                        .type(TypeEnum.NUMERIC)
                                        .build()
                                ))
                                .rows(List.of())
                                .build())
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Scenario")
                                    .column("foo")
                                    .build()
                            ))
                            .build()
                    )).build()
                );
            }
            return result;
        }
    }

    public static class SolveOrderScopeIsolationTestModifier extends RDbMappingSchemaModifier {

        /*
        "<CalculatedMember\n"
        + "    name=\"maleMinusFemale\"\n"
        + "    dimension=\"gender\"\n"
        + "    visible=\"false\"\n"
        + "    formula=\"gender.m - gender.f\">\n"
        + "  <CalculatedMemberProperty name=\"SOLVE_ORDER\" value=\"3000\"/>\n"
        + "</CalculatedMember>"
        + "<CalculatedMember\n"
        + "    name=\"ProfitSolveOrder3000\"\n"
        + "    dimension=\"Measures\">\n"
        + "  <Formula>[Measures].[Store Sales] - [Measures].[Store Cost]</Formula>\n"
        + "  <CalculatedMemberProperty name=\"SOLVE_ORDER\" value=\"3000\"/>\n"
        + "  <CalculatedMemberProperty name=\"FORMAT_STRING\" value=\"$#,##0.000000\"/>\n"
        + "</CalculatedMember>"
        + "<CalculatedMember\n"
        + "    name=\"ratio\"\n"
        + "    dimension=\"measures\"\n"
        + "    visible=\"false\"\n"
        + "    formula=\"measures.[unit sales] / measures.[sales count]\">\n"
        + "  <CalculatedMemberProperty name=\"FORMAT_STRING\" value=\"0.0#\"/>\n"
        + "  <CalculatedMemberProperty name=\"SOLVE_ORDER\" value=\"10\"/>\n"
        + "</CalculatedMember>"
        + "<CalculatedMember\n"
        + "    name=\"Total\"\n"
        + "    hierarchy=\"[Time].[Time]\"\n"
        + "    visible=\"false\"\n"
        + "    formula=\"AGGREGATE({[Time].[1997].[Q1],[Time].[1997].[Q2]})\">\n"
        + "  <CalculatedMemberProperty name=\"SOLVE_ORDER\" value=\"20\"/>\n"
        + "</CalculatedMember>";
         */
        public SolveOrderScopeIsolationTestModifier(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCalculatedMember> cubeCalculatedMembers(MappingCube cube) {
            List<MappingCalculatedMember> result = new ArrayList<>();
            result.addAll(super.cubeCalculatedMembers(cube));
            if ("Sales".equals(cube.name())) {
                result.add(CalculatedMemberRBuilder.builder()
                    .name("maleMinusFemale")
                    .dimension("gender")
                    .visible(false)
                    .formula("gender.m - gender.f")
                    .calculatedMemberProperties(List.of(
                        CalculatedMemberPropertyRBuilder.builder()
                            .name("SOLVE_ORDER")
                            .value("3000")
                            .build()
                    ))
                    .build());
                result.add(CalculatedMemberRBuilder.builder()
                    .name("ProfitSolveOrder3000")
                    .dimension("Measures")
                    .formulaElement(FormulaRBuilder.builder()
                        .cdata("[Measures].[Store Sales] - [Measures].[Store Cost]")
                        .build())
                    .calculatedMemberProperties(List.of(
                        CalculatedMemberPropertyRBuilder.builder()
                            .name("SOLVE_ORDER")
                            .value("3000")
                            .build(),
                        CalculatedMemberPropertyRBuilder.builder()
                            .name("FORMAT_STRING")
                            .value("$#,##0.000000")
                            .build()
                    ))
                    .build());
                result.add(CalculatedMemberRBuilder.builder()
                    .name("ratio")
                    .dimension("measures")
                    .formula("measures.[unit sales] / measures.[sales count]")
                    .calculatedMemberProperties(List.of(
                        CalculatedMemberPropertyRBuilder.builder()
                            .name("FORMAT_STRING")
                            .value("0.0#")
                            .build(),
                        CalculatedMemberPropertyRBuilder.builder()
                            .name("SOLVE_ORDER")
                            .value("10")
                            .build()
                        ))
                    .build());
                result.add(CalculatedMemberRBuilder.builder()
                    .name("Total")
                    .hierarchy("[Time].[Time]")
                    .visible(false)
                    .formula("AGGREGATE({[Time].[1997].[Q1],[Time].[1997].[Q2]})")
                    .calculatedMemberProperties(List.of(
                        CalculatedMemberPropertyRBuilder.builder()
                            .name("SOLVE_ORDER")
                            .value("20")
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class UdfTestModifier1 extends RDbMappingSchemaModifier {

        /*
            "<Measure name='Unit Sales Foo Bar' column='unit_sales'\n"
            + "    aggregator='sum' formatString='Standard' formatter='"
            + FooBarCellFormatter.class.getName()
            + "'/>");
        */
        public UdfTestModifier1(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingMeasure> cubeMeasures(MappingCube cube) {
            List<MappingMeasure> result = new ArrayList<>();
            result.addAll(super.cubeMeasures(cube));
            if ("Sales".equals(cube.name())) {
                result.add(MeasureRBuilder.builder()
                    .name("Unit Sales Foo Bar")
                    .column("unit_sales")
                    .aggregator("sum")
                    .formatString("Standard")
                    .formatter(UdfTest.FooBarCellFormatter.class.getName())
                    .build());
            }
            return result;
        }
    }

    public static class UdfTestModifier2 extends RDbMappingSchemaModifier {

         /*
           "<Measure name='Unit Sales Foo Bar' column='unit_sales'\n"
            + "    aggregator='sum' formatString='Standard'>\n"
            + "  <CellFormatter>\n"
            + "    <Script>\n"
            + "      return \"foo\" + value + \"bar\";\n"
            + "    </Script>\n"
            + "  </CellFormatter>\n"
            + "</Measure>");

        */
        public UdfTestModifier2(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingMeasure> cubeMeasures(MappingCube cube) {
            List<MappingMeasure> result = new ArrayList<>();
            result.addAll(super.cubeMeasures(cube));
            if ("Sales".equals(cube.name())) {
                result.add(MeasureRBuilder.builder()
                    .name("Unit Sales Foo Bar")
                    .column("unit_sales")
                    .aggregator("sum")
                    .formatString("Standard")
                    .cellFormatter(CellFormatterRBuilder.builder()
                        .script(ScriptRBuilder.builder()
                            .cdata("return \"foo\" + value + \"bar\";")
                            .build())
                        .build())
                    .build());
            }
            return result;
        }
    }

    public static class UdfTestModifier3 extends RDbMappingSchemaModifier {

        /*
                        "<CalculatedMember\n"
            + "  name='Unit Sales Foo Bar'\n"
            + "      dimension='Measures'>\n"
            + "  <Formula>[Measures].[Unit Sales]</Formula>\n"
            + "  <CalculatedMemberProperty name='CELL_FORMATTER' value='"
            + FooBarCellFormatter.class.getName()
            + "'/>\n"
            + "</CalculatedMember>");

         */
        public UdfTestModifier3(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCalculatedMember> cubeCalculatedMembers(MappingCube cube) {
            List<MappingCalculatedMember> result = new ArrayList<>();
            result.addAll(super.cubeCalculatedMembers(cube));
            if ("Sales".equals(cube.name())) {
                result.add(CalculatedMemberRBuilder.builder()
                    .name("Unit Sales Foo Bar")
                    .formulaElement(FormulaRBuilder.builder()
                        .cdata("[Measures].[Unit Sales]")
                        .build())
                    .dimension("Measures")
                    .calculatedMemberProperties(List.of(
                        CalculatedMemberPropertyRBuilder.builder()
                            .name("CELL_FORMATTER")
                            .value(UdfTest.FooBarCellFormatter.class.getName())
                            .build()
                    ))
                    .build());
            }
            return result;
        }
    }

    public static class UdfTestModifier4 extends RDbMappingSchemaModifier {

        /*
            "<CalculatedMember\n"
            + "  name='Unit Sales Foo Bar'\n"
            + "      dimension='Measures'>\n"
            + "  <Formula>[Measures].[Unit Sales]</Formula>\n"
            + "  <CellFormatter className='"
            + FooBarCellFormatter.class.getName()
            + "'/>\n"
            + "</CalculatedMember>");
         */
        public UdfTestModifier4(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCalculatedMember> cubeCalculatedMembers(MappingCube cube) {
            List<MappingCalculatedMember> result = new ArrayList<>();
            result.addAll(super.cubeCalculatedMembers(cube));
            if ("Sales".equals(cube.name())) {
                result.add(CalculatedMemberRBuilder.builder()
                    .name("Unit Sales Foo Bar")
                    .formulaElement(FormulaRBuilder.builder()
                        .cdata("[Measures].[Unit Sales]")
                        .build())
                    .dimension("Measures")
                    .cellFormatter(CellFormatterRBuilder.builder()
                        .className(UdfTest.FooBarCellFormatter.class.getName())
                        .build())
                    .build());
            }
            return result;
        }
    }

    public static class UdfTestModifier5 extends RDbMappingSchemaModifier {

        /*
            "<CalculatedMember\n"
            + "  name='Unit Sales Foo Bar'\n"
            + "      dimension='Measures'>\n"
            + "  <Formula>[Measures].[Unit Sales]</Formula>\n"
            + "  <CellFormatter>\n"
            + "    <Script>\n"
            + "      return \"foo\" + value + \"bar\";\n"
            + "    </Script>\n"
            + "  </CellFormatter>\n"
            + "</CalculatedMember>");
         */
        public UdfTestModifier5(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCalculatedMember> cubeCalculatedMembers(MappingCube cube) {
            List<MappingCalculatedMember> result = new ArrayList<>();
            result.addAll(super.cubeCalculatedMembers(cube));
            if ("Sales".equals(cube.name())) {
                result.add(CalculatedMemberRBuilder.builder()
                    .name("Unit Sales Foo Bar")
                    .formulaElement(FormulaRBuilder.builder()
                        .cdata("[Measures].[Unit Sales]")
                        .build())
                    .dimension("Measures")
                    .cellFormatter(CellFormatterRBuilder.builder()
                        .script(ScriptRBuilder.builder()
                            .cdata("return \"foo\" + value + \"bar\";\n")
                            .build())
                        .build())
                    .build());
            }
            return result;
        }
    }

    public static class UdfTestModifier6 extends RDbMappingSchemaModifier {

        /*
            "  <Dimension name='Promotion Media2' foreignKey='promotion_id'>\n"
            + "    <Hierarchy hasAll='true' allMemberName='All Media' primaryKey='promotion_id'>\n"
            + "      <Table name='promotion'/>\n"
            + "      <Level name='Media Type' column='media_type'\n"
            + "          uniqueMembers='true' formatter='"
            + FooBarMemberFormatter.class.getName()
            + "'/>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>"));
            */


        public UdfTestModifier6(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Promotion Media2")
                    .foreignKey("promotion_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .allMemberName("All Media")
                            .primaryKey("promotion_id")
                            .relation(new TableR("promotion"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Media Type")
                                    .column("media_type")
                                    .uniqueMembers(true)
                                    .formatter(UdfTest.FooBarMemberFormatter.class.getName())
                                    .build()
                            ))
                            .build()
                    )).build()
                );
            }
            return result;
        }
    }

    public static class UdfTestModifier7 extends RDbMappingSchemaModifier {

        /*
            "  <Dimension name='Promotion Media2' foreignKey='promotion_id'>\n"
            + "    <Hierarchy hasAll='true' allMemberName='All Media' primaryKey='promotion_id'>\n"
            + "      <Table name='promotion'/>\n"
            + "      <Level name='Media Type' column='media_type'\n"
            + "          uniqueMembers='true'>\n"
            + "        <MemberFormatter>\n"
            + "          <Script language='JavaScript'>\n"
            + "             return \"foo\" + member.getName() + \"bar\"\n"
            + "          </Script>\n"
            + "        </MemberFormatter>\n"
            + "      </Level>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>"));

            */

        public UdfTestModifier7(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Promotion Media2")
                    .foreignKey("promotion_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .allMemberName("All Media")
                            .primaryKey("promotion_id")
                            .relation(new TableR("promotion"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Media Type")
                                    .column("media_type")
                                    .uniqueMembers(true)
                                    .memberFormatter(CellFormatterRBuilder.builder()
                                        .script(ScriptRBuilder.builder()
                                            .language("JavaScript")
                                            .cdata("return \"foo\" + member.getName() + \"bar\"\n")
                                            .build())
                                        .build())
                                    .build()
                            ))
                            .build()
                    )).build()
                );
            }
            return result;
        }
    }

    public static class UdfTestModifier8 extends RDbMappingSchemaModifier {

        /*
            "<Dimension name='Promotions2' foreignKey='promotion_id'>\n"
            + "  <Hierarchy hasAll='true' allMemberName='All Promotions' primaryKey='promotion_id' defaultMember='[All Promotions]'>\n"
            + "    <Table name='promotion'/>\n"
            + "    <Level name='Promotion Name' column='promotion_id' uniqueMembers='true'>\n"
            + "      <Property name='Medium' column='media_type' formatter='"
            + FooBarPropertyFormatter.class.getName()
            + "'/>\n"
            + "    </Level>\n"
            + "  </Hierarchy>\n"
            + "</Dimension>"));

            */


        public UdfTestModifier8(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Promotions2")
                    .foreignKey("promotion_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .allMemberName("All Promotions")
                            .primaryKey("promotion_id")
                            .defaultMember("[All Promotions]")
                            .relation(new TableR("promotion"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Promotion Name")
                                    .column("promotion_id")
                                    .uniqueMembers(true)
                                    .properties(List.of(
                                        PropertyRBuilder.builder()
                                            .name("Medium")
                                            .column("media_type")
                                            .formatter(UdfTest.FooBarPropertyFormatter.class.getName())
                                            .build()
                                    ))
                                    .formatter(UdfTest.FooBarMemberFormatter.class.getName())
                                    .build()
                            ))
                            .build()
                    )).build()
                );
            }
            return result;
        }
    }

    public static class UdfTestModifier9 extends RDbMappingSchemaModifier {

        /*
            "<Dimension name='Promotions2' foreignKey='promotion_id'>\n"
            + "  <Hierarchy hasAll='true' allMemberName='All Promotions' primaryKey='promotion_id' defaultMember='[All Promotions]'>\n"
            + "    <Table name='promotion'/>\n"
            + "    <Level name='Promotion Name' column='promotion_id' uniqueMembers='true'>\n"
            + "      <Property name='Medium' column='media_type'>\n"
            + "        <PropertyFormatter className='"
            + FooBarPropertyFormatter.class.getName()
            + "'/>\n"
            + "      </Property>\n"
            + "    </Level>\n"
            + "  </Hierarchy>\n"
            + "</Dimension>"));

            */


        public UdfTestModifier9(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Promotions2")
                    .foreignKey("promotion_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .allMemberName("All Promotions")
                            .primaryKey("promotion_id")
                            .defaultMember("[All Promotions]")
                            .relation(new TableR("promotion"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Promotion Name")
                                    .column("promotion_id")
                                    .uniqueMembers(true)
                                    .properties(List.of(
                                        PropertyRBuilder.builder()
                                            .name("Medium")
                                            .column("media_type")
                                            .propertyFormatter(CellFormatterRBuilder.builder()
                                                .className(UdfTest.FooBarPropertyFormatter.class.getName())
                                                .build())
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    )).build()
                );
            }
            return result;
        }
    }

    public static class UdfTestModifier10 extends RDbMappingSchemaModifier {

        /*
            "<Dimension name='Promotions2' foreignKey='promotion_id'>\n"
            + "  <Hierarchy hasAll='true' allMemberName='All Promotions' primaryKey='promotion_id' defaultMember='[All Promotions]'>\n"
            + "    <Table name='promotion'/>\n"
            + "    <Level name='Promotion Name' column='promotion_id' uniqueMembers='true'>\n"
            + "      <Property name='Medium' column='media_type'>\n"
            + "        <PropertyFormatter>\n"
            + "          <Script language='JavaScript'>\n"
            + "            return \"foo\" + member.getName() + \"/\"\n"
            + "                   + propertyName + \"/\"\n"
            + "                   + propertyValue + \"bar\";\n"
            + "          </Script>\n"
            + "        </PropertyFormatter>\n"
            + "      </Property>\n"
            + "    </Level>\n"
            + "  </Hierarchy>\n"
            + "</Dimension>"));

            */


        public UdfTestModifier10(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCubeDimension> cubeDimensionUsageOrDimensions(MappingCube cube) {
            List<MappingCubeDimension> result = new ArrayList<>();
            result.addAll(super.cubeDimensionUsageOrDimensions(cube));
            if ("Sales".equals(cube.name())) {
                result.add(PrivateDimensionRBuilder.builder()
                    .name("Promotions2")
                    .foreignKey("promotion_id")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .allMemberName("All Promotions")
                            .primaryKey("promotion_id")
                            .defaultMember("[All Promotions]")
                            .relation(new TableR("promotion"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Promotion Name")
                                    .column("promotion_id")
                                    .uniqueMembers(true)
                                    .properties(List.of(
                                        PropertyRBuilder.builder()
                                            .name("Medium")
                                            .column("media_type")
                                            .propertyFormatter(CellFormatterRBuilder.builder()
                                                .script(ScriptRBuilder.builder()
                                                    .language("JavaScript")
                                                    .cdata("return \"foo\" + member.getName() + \"/\"\n + propertyName + \"/\"\n + propertyValue + \"bar\";\n")
                                                    .build())
                                                .build())
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    )).build()
                );
            }
            return result;
        }
    }

    public static class UdfTestModifier11 extends RDbMappingSchemaModifier {

        /*
            "<UserDefinedFunction name=\"PlusOne\" className=\""
            + PlusOneUdf.class.getName()
            + "\"/>\n");

            */


        public UdfTestModifier11(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingUserDefinedFunction> schemaUserDefinedFunctions(MappingSchema schema) {
            List<MappingUserDefinedFunction> result = new ArrayList<>();
            result.addAll(super.schemaUserDefinedFunctions(schema));
            result.add(UserDefinedFunctionRBuilder.builder()
                .name("PlusOne")
                .className(UdfTest.PlusOneUdf.class.getName())
                .build());
            return result;
        }
    }

    public static class UdfTestModifier12 extends RDbMappingSchemaModifier {

        /*
            "<UserDefinedFunction name=\"BadPlusOne\" className=\""
            + BadPlusOneUdf.class.getName()
            + "\"/>\n");

            */


        public UdfTestModifier12(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingUserDefinedFunction> schemaUserDefinedFunctions(MappingSchema schema) {
            List<MappingUserDefinedFunction> result = new ArrayList<>();
            result.addAll(super.schemaUserDefinedFunctions(schema));
            result.add(UserDefinedFunctionRBuilder.builder()
                .name("BadPlusOne")
                .className(UdfTest.BadPlusOneUdf.class.getName())
                .build());
            return result;
        }
    }

    public static class UdfTestModifier14 extends RDbMappingSchemaModifier {

        /*
            "<UserDefinedFunction name=\"GenericPlusOne\" className=\""
            + PlusOrMinusOneUdf.class.getName()
            + "\"/>\n"
            + "<UserDefinedFunction name=\"GenericMinusOne\" className=\""
            + PlusOrMinusOneUdf.class.getName()
            + "\"/>\n");

            */


        public UdfTestModifier14(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingUserDefinedFunction> schemaUserDefinedFunctions(MappingSchema schema) {
            List<MappingUserDefinedFunction> result = new ArrayList<>();
            result.addAll(super.schemaUserDefinedFunctions(schema));
            result.add(UserDefinedFunctionRBuilder.builder()
                .name("GenericPlusOne")
                .className(UdfTest.PlusOrMinusOneUdf.class.getName())
                .build());
            result.add(UserDefinedFunctionRBuilder.builder()
                .name("GenericMinusOne")
                .className(UdfTest.PlusOrMinusOneUdf.class.getName())
                .build());
            return result;
        }
    }

    public static class UdfTestModifier15 extends RDbMappingSchemaModifier {

        /*
            "<UserDefinedFunction name=\"StringMult\" className=\""
            + StringMultUdf.class.getName()
            + "\"/>\n");
            */


        public UdfTestModifier15(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingUserDefinedFunction> schemaUserDefinedFunctions(MappingSchema schema) {
            List<MappingUserDefinedFunction> result = new ArrayList<>();
            result.addAll(super.schemaUserDefinedFunctions(schema));
            result.add(UserDefinedFunctionRBuilder.builder()
                .name("StringMult")
                .className(UdfTest.StringMultUdf.class.getName())
                .build());
            return result;
        }
    }

    public static class UdfTestModifier16 extends RDbMappingSchemaModifier {

        /*
            "<UserDefinedFunction name=\"PlusOne\" className=\""
            + PlusOneUdf.class.getName() + "\"/>\n"
            + "<UserDefinedFunction name=\"AnotherMemberError\" className=\""
            + AnotherMemberErrorUdf.class.getName() + "\"/>");

            */


        public UdfTestModifier16(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingUserDefinedFunction> schemaUserDefinedFunctions(MappingSchema schema) {
            List<MappingUserDefinedFunction> result = new ArrayList<>();
            result.addAll(super.schemaUserDefinedFunctions(schema));
            result.add(UserDefinedFunctionRBuilder.builder()
                .name("PlusOne")
                .className(UdfTest.PlusOneUdf.class.getName())
                .build());
            result.add(UserDefinedFunctionRBuilder.builder()
                .name("AnotherMemberError")
                .className(UdfTest.AnotherMemberErrorUdf.class.getName())
                .build());
            return result;
        }
    }

    public static class UdfTestModifier17 extends RDbMappingSchemaModifier {

        /*
            "<UserDefinedFunction name=\"Reverse\" className=\""
            + functionClass.getName()
            + "\"/>\n");
            */

        private final Class<? extends UdfTest.ReverseFunction> functionClass;

        public UdfTestModifier17(MappingSchema mappingSchema, final Class<? extends UdfTest.ReverseFunction> functionClass) {
            super(mappingSchema);
            this.functionClass = functionClass;
        }

        @Override
        protected List<MappingUserDefinedFunction> schemaUserDefinedFunctions(MappingSchema schema) {
            List<MappingUserDefinedFunction> result = new ArrayList<>();
            result.addAll(super.schemaUserDefinedFunctions(schema).stream().filter(f -> !"Reverse".equals(f.name())).toList());
            result.add(UserDefinedFunctionRBuilder.builder()
                .name("Reverse")
                .className(functionClass.getName())
                .build());
            return result;
        }
    }

    public static class UdfTestModifier18 extends RDbMappingSchemaModifier {

        /*
            "<UserDefinedFunction name=\"Reverse2\" className=\""
            + ReverseFunctionNotStatic.class.getName()
            + "\"/>\n");
            + "\"/>\n");            */


        public UdfTestModifier18(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingUserDefinedFunction> schemaUserDefinedFunctions(MappingSchema schema) {
            List<MappingUserDefinedFunction> result = new ArrayList<>();
            result.addAll(super.schemaUserDefinedFunctions(schema));
            result.add(UserDefinedFunctionRBuilder.builder()
                .name("Reverse2")
                .className(UdfTest.ReverseFunctionNotStatic.class.getName())
                .build());
            return result;
        }
    }

    public static class UdfTestModifier19 extends RDbMappingSchemaModifier {

        /*
            "<UserDefinedFunction name=\"MemberName\" className=\""
            + MemberNameFunction.class.getName()
            + "\"/>\n");
        */


        public UdfTestModifier19(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingUserDefinedFunction> schemaUserDefinedFunctions(MappingSchema schema) {
            List<MappingUserDefinedFunction> result = new ArrayList<>();
            result.addAll(super.schemaUserDefinedFunctions(schema));
            result.add(UserDefinedFunctionRBuilder.builder()
                .name("MemberName")
                .className(UdfTest.MemberNameFunction.class.getName())
                .build());
            return result;
        }
    }

    public static class UdfTestModifier20 extends RDbMappingSchemaModifier {

        /*
            "<UserDefinedFunction name='StringMult'/>\n");
        */


        public UdfTestModifier20(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingUserDefinedFunction> schemaUserDefinedFunctions(MappingSchema schema) {
            List<MappingUserDefinedFunction> result = new ArrayList<>();
            result.addAll(super.schemaUserDefinedFunctions(schema));
            result.add(UserDefinedFunctionRBuilder.builder()
                .name("StringMult")
                .build());
            return result;
        }
    }

    public static class UdfTestModifier21 extends RDbMappingSchemaModifier {

        /*
                        "<UserDefinedFunction name='StringMult' className='foo'>\n"
            + " <Script>bar</Script>\n"
            + "</UserDefinedFunction>");

        */


        public UdfTestModifier21(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingUserDefinedFunction> schemaUserDefinedFunctions(MappingSchema schema) {
            List<MappingUserDefinedFunction> result = new ArrayList<>();
            result.addAll(super.schemaUserDefinedFunctions(schema));
            result.add(UserDefinedFunctionRBuilder.builder()
                .name("StringMult")
                .script(ScriptRBuilder.builder()
                    .cdata("bar")
                    .build())
                .build());
            return result;
        }
    }

    public static class UdfTestModifier22 extends RDbMappingSchemaModifier {

        /*
            "<UserDefinedFunction name='StringMult'>\n"
            + " <Script language='bad'>bar</Script>\n"
            + "</UserDefinedFunction>");

        */


        public UdfTestModifier22(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingUserDefinedFunction> schemaUserDefinedFunctions(MappingSchema schema) {
            List<MappingUserDefinedFunction> result = new ArrayList<>();
            result.addAll(super.schemaUserDefinedFunctions(schema));
            result.add(UserDefinedFunctionRBuilder.builder()
                .name("StringMult")
                .script(ScriptRBuilder.builder()
                    .language("bad")
                    .cdata("bar")
                    .build())
                .build());
            return result;
        }
    }

    public static class UdfTestModifier23 extends RDbMappingSchemaModifier {

        /*
            "<UserDefinedFunction name='StringMult'>\n"
            + "  <Script language='JavaScript'>\n"
            + "    function getParameterTypes() {\n"
            + "      return new Array(\n"
            + "        new mondrian.olap.type.NumericType(),\n"
            + "        new mondrian.olap.type.StringType());\n"
            + "    }\n"
            + "    function getReturnType(parameterTypes) {\n"
            + "      return new mondrian.olap.type.StringType();\n"
            + "    }\n"
            + "    function execute(evaluator, arguments) {\n"
            + "      var n = arguments[0].evaluateScalar(evaluator);\n"
            + "      var s = arguments[1].evaluateScalar(evaluator);\n"
            + "      var r = \"\";\n"
            + "      while (n-- > 0) {\n"
            + "        r = r + s;\n"
            + "      }\n"
            + "      return r;\n"
            + "    }\n"
            + "  </Script>\n"
            + "</UserDefinedFunction>\n");

        */


        public UdfTestModifier23(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingUserDefinedFunction> schemaUserDefinedFunctions(MappingSchema schema) {
            List<MappingUserDefinedFunction> result = new ArrayList<>();
            result.addAll(super.schemaUserDefinedFunctions(schema));
            String f = """
                            function getParameterTypes() {
                                return new Array(
                                    new mondrian.olap.type.NumericType(),
                                    new mondrian.olap.type.StringType());
                            }
                            function getReturnType(parameterTypes) {
                                return new mondrian.olap.type.StringType();
                            }
                            function execute(evaluator, arguments) {
                                var n = arguments[0].evaluateScalar(evaluator);
                                var s = arguments[1].evaluateScalar(evaluator);
                                var r = \\"\\";
                                while (n-- > 0) {
                                    r = r + s;
                                }
                                  return r;
                            }
                """;
            result.add(UserDefinedFunctionRBuilder.builder()
                .name("StringMult")
                .script(ScriptRBuilder.builder()
                    .language("JavaScript")
                    .cdata(f)
                    .build())
                .build());
            return result;
        }
    }

    public static class UdfTestModifier24 extends RDbMappingSchemaModifier {

        /*
            "<UserDefinedFunction name='Factorial'>\n"
            + "  <Script language='JavaScript'><![CDATA[\n"
            + "    function getParameterTypes() {\n"
            + "      return new Array(\n"
            + "        new mondrian.olap.type.NumericType());\n"
            + "    }\n"
            + "    function getReturnType(parameterTypes) {\n"
            + "      return new mondrian.olap.type.NumericType();\n"
            + "    }\n"
            + "    function execute(evaluator, arguments) {\n"
            + "      var n = arguments[0].evaluateScalar(evaluator);\n"
            + "      return factorial(n);\n"
            + "    }\n"
            + "    function factorial(n) {\n"
            + "      return n <= 1 ? 1 : n * factorial(n - 1);\n"
            + "    }\n"
            + "  ]]>\n"
            + "  </Script>\n"
            + "</UserDefinedFunction>\n");
        */


        public UdfTestModifier24(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingUserDefinedFunction> schemaUserDefinedFunctions(MappingSchema schema) {
            List<MappingUserDefinedFunction> result = new ArrayList<>();
            result.addAll(super.schemaUserDefinedFunctions(schema));
            String f = """
                    function getParameterTypes() {
                      return new Array(
                        new mondrian.olap.type.NumericType());
                    }
                    function getReturnType(parameterTypes) {
                      return new mondrian.olap.type.NumericType();
                    }
                    function execute(evaluator, arguments) {
                      var n = arguments[0].evaluateScalar(evaluator);
                      return factorial(n);
                    }
                    function factorial(n) {
                      return n <= 1 ? 1 : n * factorial(n - 1);
                    }
                    """;
            result.add(UserDefinedFunctionRBuilder.builder()
                .name("Factorial")
                .script(ScriptRBuilder.builder()
                    .language("JavaScript")
                    .cdata(f)
                    .build())
                .build());
            return result;
        }
    }

    public static class UdfTestModifier25 extends RDbMappingSchemaModifier {

        /*
            "<UserDefinedFunction name='Factorial'>\n"
            + "  <Script language='JavaScript'><![CDATA[\n"
            + "    function getParameterTypes() {\n"
            + "      return new Array(\n"
            + "        new mondrian.olap.type.NumericType());\n"
            + "    }\n"
            + "    function getReturnType(parameterTypes) {\n"
            + "      return new mondrian.olap.type.NumericType();\n"
            + "    }\n"
            + "    function execute(evaluator, arguments) {\n"
            + "      var n = arguments[0].evaluateScalar(evaluator);\n"
            + "      return factorial(n);\n"
            + "    }\n"
            + "    function factorial(n) {\n"
            + "      return n <= 1 ? 1 : n * factorial_xx(n - 1);\n"
            + "    }\n"
            + "  ]]>\n"
            + "  </Script>\n"
            + "</UserDefinedFunction>\n");
        */


        public UdfTestModifier25(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingUserDefinedFunction> schemaUserDefinedFunctions(MappingSchema schema) {
            List<MappingUserDefinedFunction> result = new ArrayList<>();
            result.addAll(super.schemaUserDefinedFunctions(schema));
            String f = """
                    function getParameterTypes() {
                      return new Array(
                        new mondrian.olap.type.NumericType());
                    }
                    function getReturnType(parameterTypes) {
                      return new mondrian.olap.type.NumericType();
                    }
                    function execute(evaluator, arguments) {
                      var n = arguments[0].evaluateScalar(evaluator);
                      return factorial(n);
                    }
                    function factorial(n) {
                      return n <= 1 ? 1 : n * factorial_xx(n - 1);
                    }
                        """;
            result.add(UserDefinedFunctionRBuilder.builder()
                .name("Factorial")
                .script(ScriptRBuilder.builder()
                    .language("JavaScript")
                    .cdata(f)
                    .build())
                .build());
            return result;
        }
    }

    public static class TestAggregationManagerModifier extends RDbMappingSchemaModifier {

        /*
                    "<Schema name=\"AMC\"><Cube name=\"Foo\" defaultMeasure=\"Unit Sales\">\n"
            + "  <Table name=\"sales_fact_1997\">\n"
            + "    <AggExclude name=\"agg_g_ms_pcat_sales_fact_1997\"/>"
            + "    <AggExclude name=\"agg_c_14_sales_fact_1997\"/>"
            + "    <AggExclude name=\"agg_pl_01_sales_fact_1997\"/>"
            + "    <AggExclude name=\"agg_ll_01_sales_fact_1997\"/>"
            + "    <AggExclude name=\"agg_l_03_sales_fact_1997\"/>"
            + "    <AggExclude name=\"agg_lc_06_sales_fact_1997\"/>"
            + "    <AggExclude name=\"agg_l_04_sales_fact_1997\"/>"
            + "    <AggExclude name=\"agg_c_10_sales_fact_1997\"/>"
            + "    <AggName name=\"agg_l_05_sales_fact_1997\">"
            + "        <AggFactCount column=\"fact_count\"/>\n"
            + "        <AggIgnoreColumn column=\"customer_id\"/>\n"
            + "        <AggIgnoreColumn column=\"store_id\"/>\n"
            + "        <AggIgnoreColumn column=\"promotion_id\"/>\n"
            + " <AggForeignKey factColumn=\"product_id\" aggColumn=\"product_id\"/>"
            + "        <AggMeasure name=\"[Measures].[Store Cost]\" column=\"store_cost\" />\n"
            + "        <AggMeasure name=\"[Measures].[Store Sales]\" column=\"store_sales\" />\n"
            + "        <AggMeasure name=\"[Measures].[Unit Sales]\" column=\"unit_sales\" />\n"
            + "    </AggName>\n"
            + "</Table>\n"
            + "  <Dimension name=\"Product\" foreignKey=\"product_id\">\n"
            + "<Hierarchy hasAll=\"true\" primaryKey=\"product_id\" primaryKeyTable=\"product\">\n"
            + "      <Join leftKey=\"product_class_id\" rightKey=\"product_class_id\">\n"
            + "        <Table name=\"product\"/>\n"
            + "        <Table name=\"product_class\"/>\n"
            + "     </Join>\n"
            + "     <Level name=\"Product Family\" table=\"product_class\" column=\"product_family\"\n"
            + "        uniqueMembers=\"true\"/>"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "<Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"Standard\"/>\n"
            + "<Measure name=\"Customer Count\" column=\"customer_id\" aggregator=\"distinct-count\"\n"
            + "      formatString=\"Standard\"/>\n"
            + "<Measure name=\"Store Sales\" column=\"store_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"Standard\"/>\n"
            + "<Measure name=\"Store Cost\" column=\"store_cost\" aggregator=\"sum\"\n"
            + "      formatString=\"Standard\"/>\n"
            + "</Cube></Schema>\n";
         */
        public TestAggregationManagerModifier(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            MappingTable t = new TableR("sales_fact_1997",
                List.of(
                    AggExcludeRBuilder.builder().name("agg_g_ms_pcat_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_c_14_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_pl_01_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_ll_01_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_l_03_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_lc_06_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_l_04_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_c_10_sales_fact_1997").build()
                ),
                List.of(
                    AggNameRBuilder.builder()
                        .name("agg_l_05_sales_fact_1997")
                        .aggFactCount(
                            AggColumnNameRBuilder.builder()
                                .column("fact_count")
                                .build())
                        .aggIgnoreColumns(List.of(
                            AggColumnNameRBuilder.builder()
                                .column("customer_id")
                                .build(),
                            AggColumnNameRBuilder.builder()
                                .column("store_id")
                                .build(),
                            AggColumnNameRBuilder.builder()
                                .column("promotion_id")
                                .build())
                        )
                        .aggForeignKeys(List.of(
                            AggForeignKeyRBuilder.builder()
                                .factColumn("product_id")
                                .aggColumn("product_id")
                                .build()
                        ))
                        .aggMeasures(List.of(
                            AggMeasureRBuilder.builder()
                                .name("[Measures].[Store Cost]")
                                .column("store_cost")
                                .build(),
                            AggMeasureRBuilder.builder()
                                .name("[Measures].[Store Sales]")
                                .column("store_sales")
                                .build(),
                            AggMeasureRBuilder.builder()
                                .name("[Measures].[Unit Sales]")
                                .column("unit_sales")
                                .build()
                        ))
                        .build()
                ));
            return SchemaRBuilder.builder()
                .name("AMC")
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Foo")
                        .defaultMeasure("Unit Sales")
                        .fact(t)
                        .dimensionUsageOrDimensions(List.of(
                            PrivateDimensionRBuilder.builder()
                                .name("Product")
                                .foreignKey("product_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(true)
                                        .primaryKey("product_id")
                                        .primaryKeyTable("product")
                                        .relation(new JoinR(List.of(new TableR("product"), new TableR("product_class")),
                                            null, "product_class_id", null, "product_class_id"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Product Family")
                                                .table("product_class")
                                                .column("product_family")
                                                .uniqueMembers(true)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales")
                                .column("unit_sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Customer Count")
                                .column("customer_id")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Store Sales")
                                .column("store_sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Store Cost")
                                .column("store_cost")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build()

                        ))
                        .build()
                ))
                .build();
        }
    }

    public static class TestAggregationManagerModifier3 extends RDbMappingSchemaModifier {

        /*
                "<Schema name=\"FoodMart\">"
                + "<Cube name=\"Sales\" defaultMeasure=\"Unit Sales\">\n"
                + "  <Table name=\"sales_fact_1997\" />\n"
                + "  <Dimension name=\"Gender\" foreignKey=\"customer_id\">\n"
                + "    <Hierarchy hasAll=\"true\" allMemberName=\"All Gender\" primaryKey=\"customer_id\">\n"
                + "      <Table name=\"customer\"/>\n"
                + "      <Level name=\"Gender\" column=\"gender\" uniqueMembers=\"true\"/>\n"
                + "    </Hierarchy>\n"
                + "  </Dimension>\n"
                + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"\n"
                + "      formatString=\"Standard\"/>\n"
                + "</Cube>\n"
                + "</Schema>");
         */
        public TestAggregationManagerModifier3(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("FoodMart")
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Sales")
                        .defaultMeasure("Unit Sales")
                        .fact(new TableR("sales_fact_1997"))
                        .dimensionUsageOrDimensions(List.of(
                            PrivateDimensionRBuilder.builder()
                                .name("Gender")
                                .foreignKey("customer_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(true)
                                        .allMemberName("All Gender")
                                        .primaryKey("customer_id")
                                        .relation(new TableR("customer"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Gender")
                                                .column("gender")
                                                .uniqueMembers(true)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales")
                                .column("unit_sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build()
                        ))
                        .build()
                ))
                .build();
        }
    }

    public static class TestAggregationManagerModifier4 extends RDbMappingSchemaModifier {

        /*
                "<Schema name=\"FoodMart\">"
                + "  <Dimension name=\"Time\" type=\"TimeDimension\">\n"
                + "    <Hierarchy hasAll=\"false\" primaryKey=\"time_id\">\n"
                + "      <Table name=\"time_by_day\"/>\n"
                + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
                + "          levelType=\"TimeYears\"/>\n"
                + "      <Level name=\"Quarter\" column=\"quarter\" uniqueMembers=\"false\"\n"
                + "          levelType=\"TimeQuarters\"/>\n"
                + "    </Hierarchy>\n"
                + "  </Dimension>\n"
                + "<Cube name=\"Sales\" defaultMeasure=\"Unit Sales\">\n"
                + "  <Table name=\"sales_fact_1997\">\n"
                + "    <AggExclude name=\"agg_c_special_sales_fact_1997\" />\n"
                + "    <AggExclude name=\"agg_lc_100_sales_fact_1997\" />\n"
                + "    <AggExclude name=\"agg_lc_10_sales_fact_1997\" />\n"
                + "    <AggExclude name=\"agg_pc_10_sales_fact_1997\" />\n"
                + "    <AggName name=\"agg_g_ms_pcat_sales_fact_1997\">\n"
                + "        <AggFactCount column=\"FACT_COUNT\"/>\n"
                + "        <AggIgnoreColumn column=\"Quarter\"/>\n"
                + "        <AggIgnoreColumn column=\"MONTH_OF_YEAR\"/>\n"
                + "        <AggMeasure name=\"[Measures].[Customer Count]\" column=\"customer_count\" />\n"
                + "        <AggLevel name=\"[Time].[Year]\" column=\"the_year\" />\n"
                + "    </AggName>\n"
                + "  </Table>\n"
                + "  <DimensionUsage name=\"Time\" source=\"Time\" foreignKey=\"time_id\"/>\n"
                + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"\n"
                + "      formatString=\"Standard\"/>\n"
                + "  <Measure name=\"Customer Count\" column=\"customer_id\" aggregator=\"distinct-count\"\n"
                + "      formatString=\"Standard\"/>\n"
                + "</Cube>\n"
                + "</Schema>");
         */
        public TestAggregationManagerModifier4(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            MappingTable t = new TableR("sales_fact_1997",
                List.of(
                    AggExcludeRBuilder.builder().name("agg_c_special_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_lc_100_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_lc_10_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_pc_10_sales_fact_1997").build()
                ),
                List.of(
                    AggNameRBuilder.builder()
                        .name("agg_g_ms_pcat_sales_fact_1997")
                        .aggFactCount(
                            AggColumnNameRBuilder.builder()
                                .column("FACT_COUNT")
                                .build())
                        .aggIgnoreColumns(List.of(
                            AggColumnNameRBuilder.builder()
                                .column("Quarter")
                                .build(),
                            AggColumnNameRBuilder.builder()
                                .column("MONTH_OF_YEAR")
                                .build()
                        ))
                        .aggMeasures(List.of(
                            AggMeasureRBuilder.builder()
                                .name("[Measures].[Customer Count]")
                                .column("customer_count")
                                .build()
                        ))
                        .aggLevels(List.of(
                            AggLevelRBuilder.builder()
                                .name("[Time].[Year]")
                                .column("the_year")
                                .build()
                        ))
                        .build()
                ));

            return SchemaRBuilder.builder()
                .name("FoodMart")
                .dimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Time")
                        .type(DimensionTypeEnum.TIME_DIMENSION)
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("time_id")
                                .relation(new TableR("time_by_day"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Year").column("the_year")
                                        .type(TypeEnum.NUMERIC)
                                        .uniqueMembers(true)
                                        .levelType(LevelTypeEnum.TIME_YEARS)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Quarter").column("quarter")
                                        .uniqueMembers(false)
                                        .levelType(LevelTypeEnum.TIME_QUARTERS)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Sales")
                        .defaultMeasure("Unit Sales")
                        .fact(t)
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .name("Time")
                                .source("Time")
                                .foreignKey("time_id")
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales")
                                .column("unit_sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Customer Count")
                                .column("customer_id")
                                .aggregator("distinct-count")
                                .formatString("Standard")
                                .build()
                        ))
                        .build()
                ))
                .build();
        }
    }

    public static class TestAggregationManagerModifier5 extends RDbMappingSchemaModifier {

        /*
                "<Schema name=\"FooSchema\"><Cube name=\"Sales_Foo\" defaultMeasure=\"Unit Sales\">\n"
                + "  <Table name=\"sales_fact_1997\">\n"
                + " <AggName name=\"agg_pl_01_sales_fact_1997\" approxRowCount=\"86000\">\n"
                + "     <AggFactCount column=\"FACT_COUNT\"/>\n"
                + "     <AggForeignKey factColumn=\"product_id\" aggColumn=\"PRODUCT_ID\" />\n"
                + "     <AggForeignKey factColumn=\"customer_id\" aggColumn=\"CUSTOMER_ID\" />\n"
                + "     <AggForeignKey factColumn=\"time_id\" aggColumn=\"TIME_ID\" />\n"
                + "     <AggMeasure name=\"[Measures].[Unit Sales]\" column=\"UNIT_SALES_SUM\" />\n"
                + "     <AggMeasure name=\"[Measures].[Store Cost]\" column=\"STORE_COST_SUM\" />\n"
                + "     <AggMeasure name=\"[Measures].[Store Sales]\" column=\"STORE_SALES_SUM\" />\n"
                + " </AggName>\n"
                + "    <AggExclude name=\"agg_c_special_sales_fact_1997\" />\n"
                + "    <AggExclude name=\"agg_lc_100_sales_fact_1997\" />\n"
                + "    <AggExclude name=\"agg_lc_10_sales_fact_1997\" />\n"
                + "    <AggExclude name=\"agg_pc_10_sales_fact_1997\" />\n"
                + "  </Table>\n"
                + "<Dimension name=\"Time\" type=\"TimeDimension\" foreignKey=\"time_id\">\n"
                + "    <Hierarchy hasAll=\"true\" name=\"Weekly\" primaryKey=\"time_id\">\n"
                + "      <Table name=\"time_by_day\"/>\n"
                + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
                + "          levelType=\"TimeYears\"/>\n"
                + "      <Level name=\"Week\" column=\"week_of_year\" type=\"Numeric\" uniqueMembers=\"false\"\n"
                + "          levelType=\"TimeWeeks\"/>\n"
                + "      <Level name=\"Day\" column=\"day_of_month\" uniqueMembers=\"false\" type=\"Numeric\"\n"
                + "          levelType=\"TimeDays\"/>\n"
                + "    </Hierarchy>\n"
                + "</Dimension>\n"
                + "<Dimension name=\"Product\" foreignKey=\"product_id\">\n"
                + "    <Hierarchy hasAll=\"true\" primaryKey=\"product_id\" primaryKeyTable=\"product\">\n"
                + "      <Join leftKey=\"product_class_id\" rightKey=\"product_class_id\">\n"
                + "        <Table name=\"product\"/>\n"
                + "        <Table name=\"product_class\"/>\n"
                + "      </Join>\n"
                + "      <Level name=\"Product Family\" table=\"product_class\" column=\"product_family\"\n"
                + "          uniqueMembers=\"true\"/>\n"
                + "      <Level name=\"Product Department\" table=\"product_class\" column=\"product_department\"\n"
                + "          uniqueMembers=\"false\"/>\n"
                + "      <Level name=\"Product Category\" table=\"product_class\" column=\"product_category\"\n"
                + "          uniqueMembers=\"false\"/>\n"
                + "      <Level name=\"Product Subcategory\" table=\"product_class\" column=\"product_subcategory\"\n"
                + "          uniqueMembers=\"false\"/>\n"
                + "      <Level name=\"Brand Name\" table=\"product\" column=\"brand_name\" uniqueMembers=\"false\"/>\n"
                + "      <Level name=\"Product Name\" table=\"product\" column=\"product_name\"\n"
                + "          uniqueMembers=\"true\"/>\n"
                + "    </Hierarchy>\n"
                + "</Dimension>\n"
                + "  <Dimension name=\"Customers\" foreignKey=\"customer_id\">\n"
                + "    <Hierarchy hasAll=\"true\" allMemberName=\"All Customers\" primaryKey=\"customer_id\">\n"
                + "      <Table name=\"customer\"/>\n"
                + "      <Level name=\"Country\" column=\"country\" uniqueMembers=\"true\"/>\n"
                + "      <Level name=\"State Province\" column=\"state_province\" uniqueMembers=\"true\"/>\n"
                + "      <Level name=\"City\" column=\"city\" uniqueMembers=\"false\"/>\n"
                + "      <Level name=\"Name\" column=\"customer_id\" type=\"Numeric\" uniqueMembers=\"true\">\n"
                + "        <NameExpression>\n"
                + "          <SQL dialect=\"oracle\">\n"
                + "\"fname\" || ' ' || \"lname\"\n"
                + "          </SQL>\n"
                + "          <SQL dialect=\"hive\">\n"
                + "`customer`.`fullname`\n"
                + "          </SQL>\n"
                + "          <SQL dialect=\"hsqldb\">\n"
                + "\"fname\" || ' ' || \"lname\"\n"
                + "          </SQL>\n"
                + "          <SQL dialect=\"access\">\n"
                + "fname + ' ' + lname\n"
                + "          </SQL>\n"
                + "          <SQL dialect=\"postgres\">\n"
                + "\"fname\" || ' ' || \"lname\"\n"
                + "          </SQL>\n"
                + "          <SQL dialect=\"mysql\">\n"
                + "CONCAT(`customer`.`fname`, ' ', `customer`.`lname`)\n"
                + "          </SQL>\n"
                + "          <SQL dialect=\"mssql\">\n"
                + "fname + ' ' + lname\n"
                + "          </SQL>\n"
                + "          <SQL dialect=\"derby\">\n"
                + "\"customer\".\"fullname\"\n"
                + "          </SQL>\n"
                + "          <SQL dialect=\"db2\">\n"
                + "CONCAT(CONCAT(\"customer\".\"fname\", ' '), \"customer\".\"lname\")\n"
                + "          </SQL>\n"
                + "          <SQL dialect=\"luciddb\">\n"
                + "\"fname\" || ' ' || \"lname\"\n"
                + "          </SQL>\n"
                + "          <SQL dialect=\"neoview\">\n"
                + "\"customer\".\"fullname\"\n"
                + "          </SQL>\n"
                + "          <SQL dialect=\"teradata\">\n"
                + "\"fname\" || ' ' || \"lname\"\n"
                + "          </SQL>\n"
                + "          <SQL dialect=\"generic\">\n"
                + "fullname\n"
                + "          </SQL>\n"
                + "        </NameExpression>\n"
                + "        <OrdinalExpression>\n"
                + "          <SQL dialect=\"oracle\">\n"
                + "\"fname\" || ' ' || \"lname\"\n"
                + "          </SQL>\n"
                + "          <SQL dialect=\"hsqldb\">\n"
                + "\"fname\" || ' ' || \"lname\"\n"
                + "          </SQL>\n"
                + "          <SQL dialect=\"access\">\n"
                + "fname + ' ' + lname\n"
                + "          </SQL>\n"
                + "          <SQL dialect=\"postgres\">\n"
                + "\"fname\" || ' ' || \"lname\"\n"
                + "          </SQL>\n"
                + "          <SQL dialect=\"mysql\">\n"
                + "CONCAT(`customer`.`fname`, ' ', `customer`.`lname`)\n"
                + "          </SQL>\n"
                + "          <SQL dialect=\"mssql\">\n"
                + "fname + ' ' + lname\n"
                + "          </SQL>\n"
                + "          <SQL dialect=\"neoview\">\n"
                + "\"customer\".\"fullname\"\n"
                + "          </SQL>\n"
                + "          <SQL dialect=\"derby\">\n"
                + "\"customer\".\"fullname\"\n"
                + "          </SQL>\n"
                + "          <SQL dialect=\"db2\">\n"
                + "CONCAT(CONCAT(\"customer\".\"fname\", ' '), \"customer\".\"lname\")\n"
                + "          </SQL>\n"
                + "          <SQL dialect=\"luciddb\">\n"
                + "\"fname\" || ' ' || \"lname\"\n"
                + "          </SQL>\n"
                + "          <SQL dialect=\"generic\">\n"
                + "fullname\n"
                + "          </SQL>\n"
                + "        </OrdinalExpression>\n"
                + "        <Property name=\"Gender\" column=\"gender\"/>\n"
                + "        <Property name=\"Marital Status\" column=\"marital_status\"/>\n"
                + "        <Property name=\"Education\" column=\"education\"/>\n"
                + "        <Property name=\"Yearly Income\" column=\"yearly_income\"/>\n"
                + "      </Level>\n"
                + "    </Hierarchy>\n"
                + "  </Dimension>\n"
                + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"\n"
                + "      formatString=\"Standard\"/>\n"
                + "  <Measure name=\"Store Cost\" column=\"store_cost\" aggregator=\"sum\"\n"
                + "      formatString=\"#,###.00\"/>\n"
                + "  <Measure name=\"Store Sales\" column=\"store_sales\" aggregator=\"sum\"\n"
                + "      formatString=\"#,###.00\"/>\n"
                + "  <Measure name=\"Sales Count\" column=\"product_id\" aggregator=\"count\"\n"
                + "      formatString=\"#,###\"/>\n"
                + "  <Measure name=\"Customer Count\" column=\"customer_id\"\n"
                + "      aggregator=\"distinct-count\" formatString=\"#,###\"/>\n"
                + "</Cube></Schema>\n");

         */
        public TestAggregationManagerModifier5(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            MappingTable t = new TableR("sales_fact_1997",
                List.of(
                    AggExcludeRBuilder.builder().name("agg_c_special_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_lc_100_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_lc_10_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_pc_10_sales_fact_1997").build()
                ),
                List.of(
                    AggNameRBuilder.builder()
                        .name("agg_pl_01_sales_fact_1997")
                        .approxRowCount("86000")
                        .aggFactCount(
                            AggColumnNameRBuilder.builder()
                                .column("FACT_COUNT")
                                .build())
                        .aggForeignKeys(List.of(
                            AggForeignKeyRBuilder.builder()
                                .factColumn("product_id")
                                .aggColumn("PRODUCT_ID")
                                .build(),
                            AggForeignKeyRBuilder.builder()
                                .factColumn("customer_id")
                                .aggColumn("CUSTOMER_ID")
                                .build(),
                            AggForeignKeyRBuilder.builder()
                                .factColumn("time_id")
                                .aggColumn("TIME_ID")
                                .build()
                        ))
                        .aggMeasures(List.of(
                            AggMeasureRBuilder.builder()
                                .name("[Measures].[Unit Sales]")
                                .column("UNIT_SALES_SUM")
                                .build(),
                            AggMeasureRBuilder.builder()
                                .name("[Measures].[Store Cost]")
                                .column("STORE_COST_SUM")
                                .build(),
                            AggMeasureRBuilder.builder()
                                .name("[Measures].[Store Sales]")
                                .column("STORE_SALES_SUM")
                                .build()
                        ))
                        .build()
                ));

            return SchemaRBuilder.builder()
                .name("FooSchema")
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Sales_Foo")
                        .defaultMeasure("Unit Sales")
                        .fact(t)
                        .dimensionUsageOrDimensions(List.of(
                            PrivateDimensionRBuilder.builder()
                                .name("Time")
                                .type(DimensionTypeEnum.TIME_DIMENSION)
                                .foreignKey("time_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(true)
                                        .name("Weekly")
                                        .primaryKey("time_id")
                                        .relation(new TableR("time_by_day"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Year")
                                                .column("the_year")
                                                .type(TypeEnum.NUMERIC)
                                                .uniqueMembers(true)
                                                .levelType(LevelTypeEnum.TIME_YEARS)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Week")
                                                .column("week_of_year")
                                                .type(TypeEnum.NUMERIC)
                                                .uniqueMembers(false)
                                                .levelType(LevelTypeEnum.TIME_WEEKS)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Day")
                                                .column("day_of_month")
                                                .uniqueMembers(false)
                                                .type(TypeEnum.NUMERIC)
                                                .levelType(LevelTypeEnum.TIME_DAYS)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build(),
                            PrivateDimensionRBuilder.builder()
                                .name("Product")
                                .foreignKey("product_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(true)
                                        .primaryKey("product_id")
                                        .primaryKeyTable("product")
                                        .relation(new JoinR(List.of(new TableR("product"), new TableR("product_class"))
                                            , null, "product_class_id", null, "product_class_id"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Product Family")
                                                .table("product_class")
                                                .column("product_family")
                                                .uniqueMembers(true)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Product Department")
                                                .table("product_class")
                                                .column("product_department")
                                                .uniqueMembers(false)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Product Category")
                                                .table("product_class")
                                                .column("product_category")
                                                .uniqueMembers(false)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Product Subcategory")
                                                .table("product_class")
                                                .column("product_subcategory")
                                                .uniqueMembers(false)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Brand Name")
                                                .table("product")
                                                .column("brand_name")
                                                .uniqueMembers(false)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Product Name")
                                                .table("product")
                                                .column("product_name")
                                                .uniqueMembers(true)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build(),
                            PrivateDimensionRBuilder.builder()
                                .name("Customers")
                                .foreignKey("customer_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(true)
                                        .allMemberName("All Customers")
                                        .primaryKey("customer_id")
                                        .relation(new TableR("customer"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Country")
                                                .column("country").uniqueMembers(true)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("State Province")
                                                .column("state_province").uniqueMembers(true)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("City")
                                                .column("city").uniqueMembers(false)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Name")
                                                .column("customer_id").type(TypeEnum.NUMERIC).uniqueMembers(true)
                                                .nameExpression(ExpressionViewRBuilder.builder()
                                                    .sqls(List.of(
                                                        SQLRBuilder.builder()
                                                            .dialect("oracle")
                                                            .content("\"fname\" || ' ' || \"lname\"\n")
                                                            .build(),
                                                        SQLRBuilder.builder()
                                                            .dialect("hive")
                                                            .content("`customer`.`fullname`\n")
                                                            .build(),
                                                        SQLRBuilder.builder()
                                                            .dialect("hsqldb")
                                                            .content("\"fname\" || ' ' || \"lname\"\n")
                                                            .build(),
                                                        SQLRBuilder.builder()
                                                            .dialect("access")
                                                            .content("fname + ' ' + lname\n")
                                                            .build(),
                                                        SQLRBuilder.builder()
                                                            .dialect("postgres")
                                                            .content("\"fname\" || ' ' || \"lname\"\n")
                                                            .build(),
                                                        SQLRBuilder.builder()
                                                            .dialect("mysql")
                                                            .content("CONCAT(`customer`.`fname`, ' ', `customer`.`lname`)\n")
                                                            .build(),
                                                        SQLRBuilder.builder()
                                                            .dialect("mssql")
                                                            .content("fname + ' ' + lname\n")
                                                            .build(),
                                                        SQLRBuilder.builder()
                                                            .dialect("derby")
                                                            .content("\"customer\".\"fullname\"\n")
                                                            .build(),
                                                        SQLRBuilder.builder()
                                                            .dialect("db2")
                                                            .content("CONCAT(CONCAT(\"customer\".\"fname\", ' '), \"customer\".\"lname\")\n")
                                                            .build(),
                                                        SQLRBuilder.builder()
                                                            .dialect("luciddb")
                                                            .content("\"fname\" || ' ' || \"lname\"\n")
                                                            .build(),
                                                        SQLRBuilder.builder()
                                                            .dialect("neoview")
                                                            .content("\"customer\".\"fullname\"\n")
                                                            .build(),
                                                        SQLRBuilder.builder()
                                                            .dialect("teradata")
                                                            .content("\"fname\" || ' ' || \"lname\"\n")
                                                            .build(),
                                                        SQLRBuilder.builder()
                                                            .dialect("generic")
                                                            .content("fullname")
                                                            .build()
                                                    ))
                                                    .build())
                                                .properties(List.of(
                                                    PropertyRBuilder.builder().name("Gender").column("gender").build(),
                                                    PropertyRBuilder.builder().name("Marital Status").column("marital_status").build(),
                                                    PropertyRBuilder.builder().name("Education").column("education").build(),
                                                    PropertyRBuilder.builder().name("Yearly Income").column("yearly_income").build()
                                                ))
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales")
                                .column("unit_sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Store Cost")
                                .column("store_cost")
                                .aggregator("sum")
                                .formatString("#,###.00")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Store Sales")
                                .column("store_sales")
                                .aggregator("sum")
                                .formatString("#,###.00")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Sales Count")
                                .column("product_id")
                                .aggregator("count")
                                .formatString("#,###")
                                .build(),

                            MeasureRBuilder.builder()
                                .name("Customer Count")
                                .column("customer_id")
                                .aggregator("distinct-count")
                                .formatString("#,###")
                                .build()

                        ))
                        .build()
                ))
                .build();
        }
    }

    public static class TestAggregationManagerModifier6 extends RDbMappingSchemaModifier {

        /*
            "<?xml version=\"1.0\"?>\n"
            + "<Schema name=\"custom\">\n"
            + "  <Dimension name=\"Store\">\n"
            + "    <Hierarchy hasAll=\"true\" primaryKey=\"store_id\">\n"
            + "      <Table name=\"store\"/>\n"
            + "      <Level name=\"Store Country\" column=\"store_country\" uniqueMembers=\"true\"/>\n"
            + "      <Level name=\"Store State\" column=\"store_state\" uniqueMembers=\"true\"/>\n"
            + "      <Level name=\"Store City\" column=\"store_city\" uniqueMembers=\"false\"/>\n"
            + "      <Level name=\"Store Name\" column=\"store_name\" uniqueMembers=\"true\">\n"
            + "      </Level>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "  <Dimension name=\"Time\" type=\"TimeDimension\">\n"
            + "    <Hierarchy hasAll=\"false\" primaryKey=\"time_id\">\n"
            + "      <Table name=\"time_by_day\"/>\n"
            + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
            + "          levelType=\"TimeYears\"/>\n"
            + "      <Level name=\"Quarter\" column=\"quarter\" uniqueMembers=\"false\"\n"
            + "          levelType=\"TimeQuarters\"/>\n"
            + "      <Level name=\"Month\" column=\"month_of_year\" uniqueMembers=\"true\" type=\"Numeric\"\n"
            + "          levelType=\"TimeMonths\"/>\n"
            + "      <Level name=\"Day\" column=\"day_of_month\" uniqueMembers=\"false\" type=\"Numeric\"\n"
            + "          levelType=\"TimeDays\"/>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "  <Cube name=\"Sales1\" defaultMeasure=\"Unit Sales\">\n"
            + "    <Table name=\"sales_fact_1997\">\n"
            + "      <AggExclude name=\"agg_c_special_sales_fact_1997\"/>"
            + "      <AggExclude name=\"agg_c_10_sales_fact_1997\"/>"
            + "      <AggExclude name=\"agg_l_04_sales_fact_1997\"/>"
            + "      <AggExclude name=\"agg_g_ms_pcat_sales_fact_1997\"/>"
            + "      <AggExclude name=\"agg_lc_06_sales_fact_1997\"/>"
            + "      <AggExclude name=\"agg_l_03_sales_fact_1997\"/>"
            + "      <AggExclude name=\"agg_lc_100_sales_fact_1997\"/>"
            + "      <AggExclude name=\"agg_pl_01_sales_fact_1997\"/>"
            + "      <AggExclude name=\"agg_ll_01_sales_fact_1997\"/>"
            + "      <AggExclude name=\"agg_l_05_sales_fact_1997\"/>"
            + "      <AggName name=\"agg_c_14_sales_fact_1997\">\n"
            + "        <AggFactCount column=\"fact_count\"/>\n"
            + "        <AggIgnoreColumn column=\"product_id\" />\n"
            + "        <AggIgnoreColumn column=\"customer_id\" />\n"
            + "        <AggIgnoreColumn column=\"promotion_id\" />\n"
            + "        <AggIgnoreColumn column=\"the_year\" />\n"
            + "        <AggIgnoreColumn column=\"quarter\" />\n"
            + "        <AggForeignKey factColumn=\"store_id\" aggColumn=\"store_id\" />\n"
            + "        <AggMeasure name=\"[Measures].[Unit Sales]\" column=\"unit_sales\" />\n"
            + "        <AggMeasure name=\"[Measures].[Store Cost]\" column=\"store_cost\" />\n"
            + "        <AggMeasure name=\"[Measures].[Store Sales]\" column=\"store_sales\" />\n"
            + "        <AggLevel name=\"[Time].[Month]\" column=\"month_of_year\" collapsed=\"false\" />\n"
            + "      </AggName>\n"
            + "    </Table>\n"
            + "    <DimensionUsage name=\"Store\" source=\"Store\" foreignKey=\"store_id\"/>\n"
            + "    <DimensionUsage name=\"Time\" source=\"Time\" foreignKey=\"time_id\"/>\n"
            + "    <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"Standard\"/>\n"
            + "    <Measure name=\"Store Cost\" column=\"store_cost\" aggregator=\"sum\"\n"
            + "      formatString=\"#,###.00\"/>\n"
            + "    <Measure name=\"Store Sales\" column=\"store_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"#,###.00\"/>\n"
            + "  </Cube>\n"
            + "</Schema>\n";
         */
        public TestAggregationManagerModifier6(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            MappingTable t = new TableR("sales_fact_1997",
                List.of(
                    AggExcludeRBuilder.builder().name("agg_c_special_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_c_10_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_l_04_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_g_ms_pcat_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_lc_06_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_l_03_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_lc_100_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_pl_01_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_ll_01_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_l_05_sales_fact_1997").build()
                ),
                List.of(
                    AggNameRBuilder.builder()
                        .name("agg_c_14_sales_fact_1997")
                        .aggFactCount(
                            AggColumnNameRBuilder.builder()
                                .column("fact_count")
                                .build())
                        .aggIgnoreColumns(List.of(
                            AggColumnNameRBuilder.builder()
                                .column("product_id")
                                .build(),
                            AggColumnNameRBuilder.builder()
                                .column("customer_id")
                                .build(),
                            AggColumnNameRBuilder.builder()
                                .column("promotion_id")
                                .build(),
                            AggColumnNameRBuilder.builder()
                                .column("the_year")
                                .build(),
                            AggColumnNameRBuilder.builder()
                                .column("quarter")
                                .build())
                )
                        .aggForeignKeys(List.of(
                            AggForeignKeyRBuilder.builder()
                                .factColumn("store_id")
                                .aggColumn("store_id")
                                .build()
                        ))
                        .aggMeasures(List.of(
                            AggMeasureRBuilder.builder()
                                .name("[Measures].[Unit Sales]")
                                .column("unit_sales")
                                .build(),
                            AggMeasureRBuilder.builder()
                                .name("[Measures].[Store Cost]")
                                .column("store_cost")
                                .build(),
                            AggMeasureRBuilder.builder()
                                .name("[Measures].[Store Sales]")
                                .column("store_sales")
                                .build()
                        ))
                        .aggLevels(List.of(
                            AggLevelRBuilder.builder()
                                .name("[Time].[Month]")
                                .column("month_of_year")
                                .collapsed(false)
                                .build()
                        ))
                        .build()
                ));
            return SchemaRBuilder.builder()
                .name("custom")
                .dimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Store")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("store_id")
                                .relation(new TableR("store"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Store Country")
                                        .column("store_country")
                                        .uniqueMembers(true)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Store State")
                                        .column("store_state")
                                        .uniqueMembers(true)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Store City")
                                        .column("store_city")
                                        .uniqueMembers(false)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Store Name")
                                        .column("store_name")
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        ))
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .name("Time")
                        .type(DimensionTypeEnum.TIME_DIMENSION)
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(false)
                                .primaryKey("time_id")
                                .relation(new TableR("time_by_day"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Year")
                                        .column("the_year")
                                        .type(TypeEnum.NUMERIC)
                                        .uniqueMembers(true)
                                        .levelType(LevelTypeEnum.TIME_YEARS)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Quarter")
                                        .column("quarter")
                                        .uniqueMembers(false)
                                        .levelType(LevelTypeEnum.TIME_QUARTERS)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Month")
                                        .column("month_of_year")
                                        .uniqueMembers(true)
                                        .type(TypeEnum.NUMERIC)
                                        .levelType(LevelTypeEnum.TIME_MONTHS)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Day")
                                        .column("day_of_month")
                                        .uniqueMembers(false)
                                        .type(TypeEnum.NUMERIC)
                                        .levelType(LevelTypeEnum.TIME_DAYS)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Sales1")
                        .defaultMeasure("Unit Sales")
                        .fact(t)
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .name("Store")
                                .source("Store")
                                .foreignKey("store_id")
                                .build(),
                            DimensionUsageRBuilder.builder()
                                .name("Time")
                                .source("Time")
                                .foreignKey("time_id")
                                .build()
                                ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales")
                                .column("unit_sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Store Cost")
                                .column("store_cost")
                                .aggregator("sum")
                                .formatString("#,###.00")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Store Sales")
                                .column("store_sales")
                                .aggregator("sum")
                                .formatString("#,###.00")
                                .build()
                        ))
                        .build()
                ))
                .build();
        }
    }

    public static class TestAggregationManagerModifier7 extends RDbMappingSchemaModifier {

        /*
                "<Schema name=\"FoodMart\">"
                + "  <Dimension name=\"Time\" type=\"TimeDimension\">\n"
                + "    <Hierarchy hasAll=\"false\" primaryKey=\"time_id\">\n"
                + "      <Table name=\"time_by_day\"/>\n"
                + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
                + "          levelType=\"TimeYears\"/>\n"
                + "      <Level name=\"Quarter\" column=\"quarter\" uniqueMembers=\"false\"\n"
                + "          levelType=\"TimeQuarters\"/>\n"
                + "    </Hierarchy>\n"
                + "  </Dimension>\n"
                + "<Cube name=\"Sales\" defaultMeasure=\"Unit Sales\">\n"
                + "  <Table name=\"sales_fact_1997\">\n"
                + "    <AggExclude name=\"agg_c_special_sales_fact_1997\" />\n"
                + "    <AggExclude name=\"agg_lc_100_sales_fact_1997\" />\n"
                + "    <AggExclude name=\"agg_lc_10_sales_fact_1997\" />\n"
                + "    <AggExclude name=\"agg_pc_10_sales_fact_1997\" />\n"
                + "    <AggName name=\"agg_c_10_sales_fact_1997\">\n"
                + "        <AggFactCount column=\"FACT_COUNT\"/>\n"
                + "        <AggIgnoreColumn column=\"Quarter\"/>\n"
                + "        <AggIgnoreColumn column=\"MONTH_OF_YEAR\"/>\n"
                + "        <AggMeasure name=\"[Measures].[Unit Sales]\" column=\"unit_sales\" />\n"
                + "        <AggLevel name=\"[Time].[Year]\" column=\"the_year\" />\n"
                + "    </AggName>\n"
                + "  </Table>\n"
                + "  <DimensionUsage name=\"Time\" source=\"Time\" foreignKey=\"time_id\"/>\n"
                + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"\n"
                + "      formatString=\"Standard\"/>\n"
                + "</Cube>\n"
                + "</Schema>");

         */
        public TestAggregationManagerModifier7(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            MappingTable t = new TableR("sales_fact_1997",
                List.of(
                    AggExcludeRBuilder.builder().name("agg_c_special_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_lc_100_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_lc_10_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_pc_10_sales_fact_1997").build()
                ),
                List.of(
                    AggNameRBuilder.builder()
                        .name("agg_c_10_sales_fact_1997")
                        .aggFactCount(
                            AggColumnNameRBuilder.builder()
                                .column("FACT_COUNT")
                                .build())
                        .aggIgnoreColumns(List.of(
                            AggColumnNameRBuilder.builder()
                                .column("Quarter")
                                .build(),
                            AggColumnNameRBuilder.builder()
                                .column("MONTH_OF_YEAR")
                                .build()
                        ))
                        .aggMeasures(List.of(
                            AggMeasureRBuilder.builder()
                                .name("[Measures].[Unit Sales]")
                                .column("unit_sales")
                                .build()
                        ))
                        .aggLevels(List.of(
                            AggLevelRBuilder.builder()
                                .name("[Time].[Year]")
                                .column("the_year")
                                .build()
                        ))
                        .build()
                ));

            return SchemaRBuilder.builder()
                .name("FoodMart")
                .dimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Time")
                        .type(DimensionTypeEnum.TIME_DIMENSION)
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(false)
                                .primaryKey("time_id")
                                .relation(new TableR("time_by_day"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Year").column("the_year")
                                        .type(TypeEnum.NUMERIC)
                                        .uniqueMembers(true)
                                        .levelType(LevelTypeEnum.TIME_YEARS)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Quarter").column("quarter")
                                        .uniqueMembers(false)
                                        .levelType(LevelTypeEnum.TIME_QUARTERS)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Sales")
                        .defaultMeasure("Unit Sales")
                        .fact(t)
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .name("Time")
                                .source("Time")
                                .foreignKey("time_id")
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales")
                                .column("unit_sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build()
                        ))
                        .build()
                ))
                .build();
        }
    }

    public static class TestAggregationManagerModifier8 extends RDbMappingSchemaModifier {

        /*
            "<Schema name=\"AMC\"><Cube name=\"Foo\" defaultMeasure=\"Unit Sales\">\n"
            + "  <Table name=\"sales_fact_1997\">\n"
            + "    <AggExclude name=\"agg_g_ms_pcat_sales_fact_1997\"/>"
            + "    <AggExclude name=\"agg_c_14_sales_fact_1997\"/>"
            + "    <AggExclude name=\"agg_pl_01_sales_fact_1997\"/>"
            + "    <AggExclude name=\"agg_ll_01_sales_fact_1997\"/>"
            + "    <AggExclude name=\"agg_l_03_sales_fact_1997\"/>"
            + "    <AggExclude name=\"agg_lc_06_sales_fact_1997\"/>"
            + "    <AggExclude name=\"agg_l_04_sales_fact_1997\"/>"
            + "    <AggExclude name=\"agg_c_10_sales_fact_1997\"/>"
            + "    <AggName name=\"agg_l_05_sales_fact_1997\">"
            + "        <AggFactCount column=\"fact_count\"/>\n"
            + "        <AggIgnoreColumn column=\"customer_id\"/>\n"
            + "        <AggIgnoreColumn column=\"store_id\"/>\n"
            + "        <AggIgnoreColumn column=\"promotion_id\"/>\n"
            + " <AggForeignKey factColumn=\"promotion_id\" aggColumn=\"promotion_id\"/>"
            + "        <AggMeasure name=\"[Measures].[Store Cost]\" column=\"store_cost\" />\n"
            + "        <AggMeasure name=\"[Measures].[Store Sales]\" column=\"store_sales\" />\n"
            + "        <AggMeasure name=\"[Measures].[Unit Sales]\" column=\"unit_sales\" />\n"
            + "    </AggName>\n"
            + "</Table>\n"
            + "  <Dimension name=\"Promotions\" foreignKey=\"promotion_id\">\n"
            + "    <Hierarchy hasAll=\"true\" allMemberName=\"All Promotions\" primaryKey=\"promotion_id\" defaultMember=\"[All Promotions]\">\n"
            + "      <Table name=\"promotion\"/>\n"
            + "      <Level name=\"Media Type\" column=\"media_type\" uniqueMembers=\"true\"/>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>"
            + "<Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"Standard\"/>\n"
            + "<Measure name=\"Customer Count\" column=\"customer_id\" aggregator=\"distinct-count\"\n"
            + "      formatString=\"Standard\"/>\n"
            + "<Measure name=\"Store Sales\" column=\"store_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"Standard\"/>\n"
            + "<Measure name=\"Store Cost\" column=\"store_cost\" aggregator=\"sum\"\n"
            + "      formatString=\"Standard\"/>\n"
            + "</Cube></Schema>\n";
         */
        public TestAggregationManagerModifier8(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            MappingTable t = new TableR("sales_fact_1997",
                List.of(
                    AggExcludeRBuilder.builder().name("agg_g_ms_pcat_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_c_14_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_pl_01_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_ll_01_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_l_03_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_lc_06_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_l_04_sales_fact_1997").build(),
                    AggExcludeRBuilder.builder().name("agg_c_10_sales_fact_1997").build()
                ),
                List.of(
                    AggNameRBuilder.builder()
                        .name("agg_l_05_sales_fact_1997")
                        .aggFactCount(
                            AggColumnNameRBuilder.builder()
                                .column("fact_count")
                                .build())
                        .aggIgnoreColumns(List.of(
                            AggColumnNameRBuilder.builder()
                                .column("customer_id")
                                .build(),
                            AggColumnNameRBuilder.builder()
                                .column("store_id")
                                .build(),
                            AggColumnNameRBuilder.builder()
                                .column("promotion_id")
                                .build()
                        ))
                        .aggForeignKeys(List.of(
                            AggForeignKeyRBuilder.builder()
                                .factColumn("promotion_id")
                                .aggColumn("promotion_id")
                                .build()
                        ))
                        .aggMeasures(List.of(
                            AggMeasureRBuilder.builder()
                                .name("[Measures].[Store Cost]")
                                .column("store_cost")
                                .build(),
                            AggMeasureRBuilder.builder()
                                .name("[Measures].[Store Sales]")
                                .column("store_sales")
                                .build(),
                            AggMeasureRBuilder.builder()
                                .name("[Measures].[Unit Sales]")
                                .column("unit_sales")
                                .build()
                        ))
                        .build()
                ));

            return SchemaRBuilder.builder()
                .name("AMC")
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Foo")
                        .defaultMeasure("Unit Sales")
                        .fact(t)
                        .dimensionUsageOrDimensions(List.of(
                            PrivateDimensionRBuilder.builder()
                                .name("Promotions")
                                .foreignKey("promotion_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(true)
                                        .allMemberName("All Promotions")
                                        .primaryKey("promotion_id")
                                        .defaultMember("[All Promotions]")
                                        .relation(new TableR("promotion"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Media Type")
                                                .column("media_type")
                                                .uniqueMembers(true)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales")
                                .column("unit_sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Customer Count")
                                .column("customer_id")
                                .aggregator("distinct-count")
                                .formatString("Standard")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Store Sales")
                                .column("store_sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Store Cost")
                                .column("store_cost")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build()
                        ))
                        .build()
                ))
                .build();
        }
    }

    public static class TestAggregationManagerModifier9 extends RDbMappingSchemaModifier {

        /*
            "<?xml version=\"1.0\"?>\n"
            + "<Schema name=\"custom\">\n"
            + "  <Dimension name=\"Store\">\n"
            + "    <Hierarchy hasAll=\"true\" primaryKey=\"store_id\">\n"
            + "      <Table name=\"store\"/>\n"
            + "      <Level name=\"Store Country\" column=\"store_country\" uniqueMembers=\"true\"/>\n"
            + "      <Level name=\"Store State\" column=\"store_state\" uniqueMembers=\"true\"/>\n"
            + "      <Level name=\"Store City\" column=\"store_city\" uniqueMembers=\"false\"/>\n"
            + "      <Level name=\"Store Name\" column=\"store_name\" uniqueMembers=\"true\">\n"
            + "      </Level>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "  <Dimension name=\"Time\" type=\"TimeDimension\">\n"
            + "    <Hierarchy hasAll=\"false\" primaryKey=\"time_id\">\n"
            + "      <Table name=\"time_by_day\"/>\n"
            + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
            + "          levelType=\"TimeYears\"/>\n"
            + "      <Level name=\"Quarter\" column=\"quarter\" uniqueMembers=\"false\"\n"
            + "          levelType=\"TimeQuarters\"/>\n"
            + "      <Level name=\"Month\" column=\"month_of_year\" uniqueMembers=\"false\" type=\"Numeric\"\n"
            + "          levelType=\"TimeMonths\"/>\n"
            + "    </Hierarchy>\n"
            + "    <Hierarchy hasAll=\"true\" name=\"Weekly\" primaryKey=\"time_id\">\n"
            + "      <Table name=\"time_by_day\"/>\n"
            + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
            + "          levelType=\"TimeYears\"/>\n"
            + "      <Level name=\"Week\" column=\"week_of_year\" type=\"Numeric\" uniqueMembers=\"false\"\n"
            + "          levelType=\"TimeWeeks\"/>\n"
            + "      <Level name=\"Day\" column=\"day_of_month\" uniqueMembers=\"false\" type=\"Numeric\"\n"
            + "          levelType=\"TimeDays\"/>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "  <Cube name=\"Sales1\" defaultMeasure=\"Unit Sales\">\n"
            + "    <Table name=\"sales_fact_1997\">\n"
            + "      <AggName name=\"agg_c_special_sales_fact_1997\">\n"
            + "        <AggFactCount column=\"FACT_COUNT\"/>\n"
            + "        <AggIgnoreColumn column=\"foo\"/>\n"
            + "        <AggIgnoreColumn column=\"bar\"/>\n"
            + "        <AggIgnoreColumn column=\"PRODUCT_ID\" />\n"
            + "        <AggIgnoreColumn column=\"CUSTOMER_ID\" />\n"
            + "        <AggIgnoreColumn column=\"PROMOTION_ID\" />\n"
            + "        <AggForeignKey factColumn=\"store_id\" aggColumn=\"STORE_ID\" />\n"
            + "        <AggMeasure name=\"[Measures].[Unit Sales]\" column=\"UNIT_SALES_SUM\" />\n"
            + "        <AggMeasure name=\"[Measures].[Store Cost]\" column=\"STORE_COST_SUM\" />\n"
            + "        <AggMeasure name=\"[Measures].[Store Sales]\" column=\"STORE_SALES_SUM\" />\n"
            + "        <AggLevel name=\"[Time].[Year]\" column=\"TIME_YEAR\" />\n"
            + "        <AggLevel name=\"[Time].[Quarter]\" column=\"TIME_QUARTER\" />\n"
            + "        <AggLevel name=\"[Time].[Month]\" column=\"TIME_MONTH\" />\n"
            + "      </AggName>\n"
            + "    </Table>\n"
            + "    <DimensionUsage name=\"Store\" source=\"Store\" foreignKey=\"store_id\"/>\n"
            + "    <DimensionUsage name=\"Time\" source=\"Time\" foreignKey=\"time_id\"/>\n"
            + "    <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"Standard\"/>\n"
            + "    <Measure name=\"Store Cost\" column=\"store_cost\" aggregator=\"sum\"\n"
            + "      formatString=\"#,###.00\"/>\n"
            + "    <Measure name=\"Store Sales\" column=\"store_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"#,###.00\"/>\n"
            + "  </Cube>\n"
            + "  <Cube name=\"Sales2\" defaultMeasure=\"Unit Sales\">\n"
            + "    <Table name=\"sales_fact_1997\">\n"
            + "      <AggName name=\"agg_c_special_sales_fact_1997\">\n"
            + "        <AggFactCount column=\"FACT_COUNT\"/>\n"
            + "        <AggIgnoreColumn column=\"foo\"/>\n"
            + "        <AggIgnoreColumn column=\"bar\"/>\n"
            + "        <AggIgnoreColumn column=\"PRODUCT_ID\" />\n"
            + "        <AggIgnoreColumn column=\"CUSTOMER_ID\" />\n"
            + "        <AggIgnoreColumn column=\"PROMOTION_ID\" />\n"
            + "        <AggForeignKey factColumn=\"store_id\" aggColumn=\"STORE_ID\" />\n"
            + "        <AggMeasure name=\"[Measures].[Unit Sales]\" column=\"UNIT_SALES_SUM\" />\n"
            + "        <AggMeasure name=\"[Measures].[Store Cost]\" column=\"STORE_COST_SUM\" />\n"
            + "        <AggMeasure name=\"[Measures].[Store Sales]\" column=\"STORE_SALES_SUM\" />\n"
            + "        <AggLevel name=\"[Time].[Year]\" column=\"TIME_YEAR\" />\n"
            + "        <AggLevel name=\"[Time].[Quarter]\" column=\"TIME_QUARTER\" />\n"
            + "        <AggLevel name=\"[Time].[Month]\" column=\"TIME_MONTH\" />\n"
            + "      </AggName>\n"
            + "    </Table>\n"
            + "    <DimensionUsage name=\"Store\" source=\"Store\" foreignKey=\"store_id\"/>\n"
            + "    <DimensionUsage name=\"Time\" source=\"Time\" foreignKey=\"time_id\"/>\n"
            + "    <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"Standard\"/>\n"
            + "    <Measure name=\"Store Cost\" column=\"store_cost\" aggregator=\"sum\"\n"
            + "      formatString=\"#,###.00\"/>\n"
            + "    <Measure name=\"Store Sales\" column=\"store_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"#,###.00\"/>\n"
            + "  </Cube>\n"
            + "  <VirtualCube name=\"SuperSales\" defaultMeasure=\"Unit Sales\">\n"
            + "    <VirtualCubeDimension cubeName=\"Sales1\" name=\"Store\"/>\n"
            + " <VirtualCubeDimension cubeName=\"Sales1\" name=\"Time\"/>\n"
            + "    <VirtualCubeMeasure cubeName=\"Sales2\" name=\"[Measures].[Unit Sales]\"/>\n"
            + " <VirtualCubeMeasure cubeName=\"Sales2\" name=\"[Measures].[Store Cost]\"/>\n"
            + " <VirtualCubeMeasure cubeName=\"Sales2\" name=\"[Measures].[Store Sales]\"/>\n"
            + "  </VirtualCube>\n"
            + "</Schema>\n";

         */
        public TestAggregationManagerModifier9(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            MappingTable t1 = new TableR("sales_fact_1997",
                List.of(),
                List.of(
                    AggNameRBuilder.builder()
                        .name("agg_c_special_sales_fact_1997")
                        .aggFactCount(
                            AggColumnNameRBuilder.builder()
                                .column("FACT_COUNT")
                                .build())
                        .aggIgnoreColumns(List.of(
                            AggColumnNameRBuilder.builder()
                                .column("foo")
                                .build(),
                            AggColumnNameRBuilder.builder()
                                .column("bar")
                                .build(),
                            AggColumnNameRBuilder.builder()
                                .column("PRODUCT_ID")
                                .build(),
                            AggColumnNameRBuilder.builder()
                                .column("CUSTOMER_ID")
                                .build(),
                            AggColumnNameRBuilder.builder()
                                .column("PROMOTION_ID")
                                .build()
                        ))
                        .aggForeignKeys(List.of(
                            AggForeignKeyRBuilder.builder()
                                .factColumn("store_id")
                                .aggColumn("STORE_ID")
                                .build()
                        ))
                        .aggMeasures(List.of(
                            AggMeasureRBuilder.builder()
                                .name("[Measures].[Unit Sales]")
                                .column("UNIT_SALES_SUM")
                                .build(),

                            AggMeasureRBuilder.builder()
                                .name("[Measures].[Store Cost]")
                                .column("STORE_COST_SUM")
                                .build(),
                            AggMeasureRBuilder.builder()
                                .name("[Measures].[Store Sales]")
                                .column("STORE_SALES_SUM")
                                .build()
                        ))
                        .aggLevels(List.of(
                            AggLevelRBuilder.builder()
                                .name("[Time].[Year]")
                                .column("TIME_YEAR")
                                .build(),
                            AggLevelRBuilder.builder()
                                .name("[Time].[Quarter]")
                                .column("TIME_QUARTER")
                                .build(),
                            AggLevelRBuilder.builder()
                                .name("[Time].[Month]")
                                .column("TIME_MONTH")
                                .build()
                        ))
                        .build()
                ));

            MappingTable t2 = new TableR("sales_fact_1997",
                List.of(),
                List.of(
                    AggNameRBuilder.builder()
                        .name("agg_c_special_sales_fact_1997")
                        .aggFactCount(
                            AggColumnNameRBuilder.builder()
                                .column("FACT_COUNT")
                                .build())
                        .aggIgnoreColumns(List.of(
                            AggColumnNameRBuilder.builder()
                                .column("foo")
                                .build(),
                            AggColumnNameRBuilder.builder()
                                .column("bar")
                                .build(),
                            AggColumnNameRBuilder.builder()
                                .column("PRODUCT_ID")
                                .build(),
                            AggColumnNameRBuilder.builder()
                                .column("CUSTOMER_ID")
                                .build(),
                            AggColumnNameRBuilder.builder()
                                .column("PROMOTION_ID")
                                .build()
                        ))
                        .aggForeignKeys(List.of(
                            AggForeignKeyRBuilder.builder()
                                .factColumn("store_id")
                                .aggColumn("STORE_ID")
                                .build()
                        ))
                        .aggMeasures(List.of(
                            AggMeasureRBuilder.builder()
                                .name("[Measures].[Unit Sales]")
                                .column("UNIT_SALES_SUM")
                                .build(),
                            AggMeasureRBuilder.builder()
                                .name("[Measures].[Store Cost]")
                                .column("STORE_COST_SUM")
                                .build(),
                            AggMeasureRBuilder.builder()
                                .name("[Measures].[Store Sales]")
                                .column("STORE_SALES_SUM")
                                .build()
                        ))
                        .aggLevels(List.of(
                            AggLevelRBuilder.builder()
                                .name("[Time].[Year]")
                                .column("TIME_YEAR")
                                .build(),
                            AggLevelRBuilder.builder()
                                .name("[Time].[Quarter]")
                                .column("TIME_QUARTER")
                                .build(),
                            AggLevelRBuilder.builder()
                                .name("[Time].[Month]")
                                .column("TIME_MONTH")
                                .build()
                        ))
                        .build()
                ));

            return SchemaRBuilder.builder()
                .name("custom")
                .dimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Store")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("store_id")
                                .relation(new TableR("store"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Store Country")
                                        .column("store_country")
                                        .uniqueMembers(true)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Store State")
                                        .column("store_state")
                                        .uniqueMembers(true)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Store City")
                                        .column("store_city")
                                        .uniqueMembers(false)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Store Name")
                                        .column("store_name")
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        ))
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .name("Time")
                        .type(DimensionTypeEnum.TIME_DIMENSION)
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(false)
                                .primaryKey("time_id")
                                .relation(new TableR("time_by_day"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Year").column("the_year")
                                        .type(TypeEnum.NUMERIC)
                                        .uniqueMembers(true)
                                        .levelType(LevelTypeEnum.TIME_YEARS)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Quarter").column("quarter")
                                        .uniqueMembers(false)
                                        .levelType(LevelTypeEnum.TIME_QUARTERS)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Month").column("month_of_year")
                                        .uniqueMembers(false)
                                        .type(TypeEnum.NUMERIC)
                                        .levelType(LevelTypeEnum.TIME_MONTHS)
                                        .build()
                                ))
                                .build(),
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .name("Weekly")
                                .primaryKey("time_id")
                                .relation(new TableR("time_by_day"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Year").column("the_year")
                                        .type(TypeEnum.NUMERIC)
                                        .uniqueMembers(true)
                                        .levelType(LevelTypeEnum.TIME_YEARS)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Week").column("week_of_year")
                                        .uniqueMembers(false)
                                        .type(TypeEnum.NUMERIC)
                                        .levelType(LevelTypeEnum.TIME_WEEKS)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Day").column("day_of_month")
                                        .uniqueMembers(false)
                                        .type(TypeEnum.NUMERIC)
                                        .levelType(LevelTypeEnum.TIME_DAYS)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Sales1")
                        .defaultMeasure("Unit Sales")
                        .fact(t1)
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .name("Store")
                                .source("Store")
                                .foreignKey("store_id")
                                .build(),
                            DimensionUsageRBuilder.builder()
                                .name("Time")
                                .source("Time")
                                .foreignKey("time_id")
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales")
                                .column("unit_sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Store Cost")
                                .column("store_cost")
                                .aggregator("sum")
                                .formatString("#,###.00")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Store Sales")
                                .column("store_sales")
                                .aggregator("sum")
                                .formatString("#,###.00")
                                .build()
                        ))
                        .build(),
                    CubeRBuilder.builder()
                        .name("Sales2")
                        .defaultMeasure("Unit Sales")
                        .fact(t2)
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .name("Store")
                                .source("Store")
                                .foreignKey("store_id")
                                .build(),
                            DimensionUsageRBuilder.builder()
                                .name("Time")
                                .source("Time")
                                .foreignKey("time_id")
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales")
                                .column("unit_sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Store Cost")
                                .column("store_cost")
                                .aggregator("sum")
                                .formatString("#,###.00")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Store Sales")
                                .column("store_sales")
                                .aggregator("sum")
                                .formatString("#,###.00")
                                .build()

                        ))
                        .build()
                ))
                .virtualCubes(List.of(
                    VirtualCubeRBuilder.builder()
                        .name("SuperSales")
                        .defaultMeasure("Unit Sales")
                        .virtualCubeDimensions(List.of(
                            VirtualCubeDimensionRBuilder.builder()
                                .cubeName("Sales1")
                                .name("Store")
                                .build(),
                            VirtualCubeDimensionRBuilder.builder()
                                .cubeName("Sales1")
                                .name("Time")
                                .build()
                        ))
                        .virtualCubeMeasures(List.of(
                            VirtualCubeMeasureRBuilder.builder()
                                .cubeName("Sales2")
                                .name("[Measures].[Unit Sales]")
                                .build(),
                            VirtualCubeMeasureRBuilder.builder()
                                .cubeName("Sales2")
                                .name("[Measures].[Store Cost]")
                                .build(),
                            VirtualCubeMeasureRBuilder.builder()
                                .cubeName("Sales2")
                                .name("[Measures].[Store Sales]")
                                .build()

                        ))
                        .build()
                ))
                .build();
        }
    }

    public static class TestCalculatedMembers1 extends RDbMappingSchemaModifier {

        /*
        final String cubeName = "Sales_Bug1410383";
                "<Cube name=\"" + cubeName + "\">\n"
                + "  <Table name=\"sales_fact_1997\"/>\n"
                + "  <Dimension name=\"Gender\" foreignKey=\"customer_id\">\n"
                + "    <Hierarchy hasAll=\"false\" primaryKey=\"customer_id\">\n"
                + "    <Table name=\"customer\"/>\n"
                + "      <Level name=\"Gender\" column=\"gender\" uniqueMembers=\"true\"/>\n"
                + "    </Hierarchy>\n"
                + "  </Dimension>\n"
                + "  <Measure name=\"Store Sales\" column=\"store_sales\" aggregator=\"sum\"\n"
                + "      formatString=\"Standard\" visible=\"false\"/>\n"
                + "  <Measure name=\"Store Cost\" column=\"store_cost\" aggregator=\"sum\"\n"
                + "      formatString=\"Standard\" visible=\"false\"/>\n"
                + "  <CalculatedMember\n"
                + "      name=\"Apos in dq\"\n"
                + "      dimension=\"Measures\"\n"
                + "      visible=\"false\"\n"
                + "      formula=\" &quot;an 'apos' in dq&quot; \" />\n"
                + "  <CalculatedMember\n"
                + "      name=\"Dq in dq\"\n"
                + "      dimension=\"Measures\"\n"
                + "      visible=\"false\"\n"
                + "      formula=\" &quot;a &quot;&quot;dq&quot;&quot; in dq&quot; \" />\n"
                + "  <CalculatedMember\n"
                + "      name=\"Apos in apos\"\n"
                + "      dimension=\"Measures\"\n"
                + "      visible=\"false\"\n"
                + "      formula=\" &apos;an &apos;&apos;apos&apos;&apos; in apos&apos; \" />\n"
                + "  <CalculatedMember\n"
                + "      name=\"Dq in apos\"\n"
                + "      dimension=\"Measures\"\n"
                + "      visible=\"false\"\n"
                + "      formula=\" &apos;a &quot;dq&quot; in apos&apos; \" />\n"
                + "  <CalculatedMember\n"
                + "      name=\"Colored Profit\"\n"
                + "      dimension=\"Measures\"\n"
                + "      visible=\"false\"\n"
                + "      formula=\" [Measures].[Store Sales] - [Measures].[Store Cost] \">\n"
                + "    <CalculatedMemberProperty name=\"FORMAT_STRING\" expression=\"Iif([Measures].[Colored Profit] &lt; 0, '|($#,##0.00)|style=red', '|$#,##0.00|style=green')\"/>\n"
                + "  </CalculatedMember>\n"
                + "</Cube>";

         */
        public TestCalculatedMembers1(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCube> schemaCubes(MappingSchema mappingSchemaOriginal) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(mappingSchemaOriginal));
            result.add(CubeRBuilder.builder()
                .name("Sales_Bug1410383")
                .fact(new TableR("sales_fact_1997"))
                .dimensionUsageOrDimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Gender")
                        .foreignKey("customer_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("customer_id")
                                .relation(new TableR("customer"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Gender")
                                        .column("gender")
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Store Sales")
                        .column("store_sales")
                        .aggregator("sum")
                        .formatString("Standard")
                        .visible(false)
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Store Cost")
                        .column("store_cost")
                        .aggregator("sum")
                        .formatString("Standard")
                        .visible(false)
                        .build()
                ))
                .calculatedMembers(List.of(
                    CalculatedMemberRBuilder.builder()
                        .name("Apos in dq")
                        .dimension("Measures")
                        .visible(false)
                        .formula(" &quot;an 'apos' in dq&quot; ")
                        .build(),
                    CalculatedMemberRBuilder.builder()
                        .name("Dq in dq")
                        .dimension("Measures")
                        .visible(false)
                        .formula(" &quot;a &quot;&quot;dq&quot;&quot; in dq&quot; ")
                        .build(),
                    CalculatedMemberRBuilder.builder()
                        .name("Apos in apos")
                        .dimension("Measures")
                        .visible(false)
                        .formula(" &apos;an &apos;&apos;apos&apos;&apos; in apos&apos; ")
                        .build(),
                    CalculatedMemberRBuilder.builder()
                        .name("Dq in apos")
                        .dimension("Measures")
                        .visible(false)
                        .formula(" &apos;a &quot;dq&quot; in apos&apos; ")
                        .build(),
                    CalculatedMemberRBuilder.builder()
                        .name("Colored Profit")
                        .dimension("Measures")
                        .visible(false)
                        .formula(" [Measures].[Store Sales] - [Measures].[Store Cost] ")
                        .calculatedMemberProperties(List.of(
                            CalculatedMemberPropertyRBuilder.builder()
                                .name("FORMAT_STRING")
                                .expression("Iif([Measures].[Colored Profit] < 0, '|($#,##0.00)|style=red', '|$#,##0.00|style=green')")
                                .build()
                        ))
                        .build()
                ))

                .build());
            return result;

        }
    }

    public static class TestCalculatedMembers2 extends RDbMappingSchemaModifier {

        /*
            "<Cube name=\"Store5\"> \n"
            + "  <Table name=\"store\"/> \n"
            + "  <!-- We could have used the shared dimension \"Store Type\", but we \n"
            + "     want to test private dimensions without primary key. --> \n"
            + "  <Dimension name=\"Store Type\"> \n"
            + "    <Hierarchy name=\"Store Types Hierarchy\" allMemberName=\"All Store Types Member Name\" hasAll=\"true\"> \n"
            + "      <Level name=\"Store Type\" column=\"store_type\" uniqueMembers=\"true\"/> \n"
            + "    </Hierarchy> \n"
            + "  </Dimension> \n"
            + "\n"
            + "  <Dimension name=\"Country\">\n"
            + "    <Hierarchy hasAll=\"true\" primaryKey=\"customer_id\">\n"
            + "      <Level name=\"Country\" column=\"store_country\" uniqueMembers=\"true\"/>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "\n"
            + "  <Measure name=\"Store Sqft\" column=\"store_sqft\" aggregator=\"sum\" \n"
            + "      formatString=\"#,###\"/> \n"
            + "  <Measure name=\"Grocery Sqft\" column=\"grocery_sqft\" aggregator=\"sum\" \n"
            + "      formatString=\"#,###\" description=\"Grocery Sqft Description...\"> \n"
            + "    <Annotations> \n"
            + "        <Annotation name=\"AnalyzerBusinessGroup\">Numbers</Annotation> \n"
            + "    </Annotations> \n"
            + "  </Measure> \n"
            + "  <CalculatedMember \n"
            + "      name=\"Constant 1\" description=\"Constant 1 Description...\" \n"
            + "      dimension=\"Measures\"> \n"
            + "    <Annotations> \n"
            + "        <Annotation name=\"AnalyzerBusinessGroup\">Numbers</Annotation> \n"
            + "    </Annotations> \n"
            + "    <Formula>1</Formula> \n"
            + "  </CalculatedMember> \n"
            + "</Cube> ",


         */
        public TestCalculatedMembers2(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCube> schemaCubes(MappingSchema mappingSchemaOriginal) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(mappingSchemaOriginal));
            result.add(CubeRBuilder.builder()
                .name("Store5")
                .fact(new TableR("store"))
                .dimensionUsageOrDimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Store Type")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .name("Store Types Hierarchy")
                                .allMemberName("All Store Types Member Name")
                                .hasAll(true)
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Store Type")
                                        .column("store_type")
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        ))
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .name("Country")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("customer_id")
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Country")
                                        .column("store_country")
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Store Sqft")
                        .column("store_sqft")
                        .aggregator("sum")
                        .formatString("#,###")
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Grocery Sqft")
                        .column("grocery_sqft")
                        .aggregator("sum")
                        .formatString("#,###")
                        .description("Grocery Sqft Description...")
                        .annotations(List.of(
                            AnnotationRBuilder.builder()
                                .name("AnalyzerBusinessGroup")
                                .content("Numbers")
                                .build()
                        ))
                        .build()
                ))
                .calculatedMembers(List.of(
                    CalculatedMemberRBuilder.builder()
                        .name("Constant 1")
                        .dimension("Constant 1 Description...")
                        .dimension("Measures")
                        .annotations(List.of(
                            AnnotationRBuilder.builder()
                                .name("AnalyzerBusinessGroup")
                                .content("Numbers")
                                .build()
                        ))
                        .formulaElement(FormulaRBuilder.builder().cdata("1").build())
                        .build()
                ))

                .build());
            return result;

        }
    }

    public static class TestCalculatedMembers3 extends RDbMappingSchemaModifier {

        /*
        final String cubeName = "Sales_BracketInCubeCalcMemberName";
        String s =
            "<Cube name=\"" + cubeName + "\">\n"
            + "  <Table name=\"sales_fact_1997\"/>\n"
            + "  <Dimension name=\"Gender\" foreignKey=\"customer_id\">\n"
            + "    <Hierarchy hasAll=\"false\" primaryKey=\"customer_id\">\n"
            + "    <Table name=\"customer\"/>\n"
            + "      <Level name=\"Gender\" column=\"gender\" uniqueMembers=\"true\"/>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"Standard\" visible=\"false\"/>\n"
            + "  <CalculatedMember\n"
            + "      name=\"With a [bracket] inside it\"\n"
            + "      dimension=\"Measures\"\n"
            + "      visible=\"false\"\n"
            + "      formula=\"[Measures].[Unit Sales] * 10\">\n"
            + "    <CalculatedMemberProperty name=\"FORMAT_STRING\" value=\"$#,##0.00\"/>\n"
            + "  </CalculatedMember>\n"
            + "</Cube>";

         */
        public TestCalculatedMembers3(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCube> schemaCubes(MappingSchema mappingSchemaOriginal) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(mappingSchemaOriginal));
            result.add(CubeRBuilder.builder()
                .name("Sales_BracketInCubeCalcMemberName")
                .fact(new TableR("sales_fact_1997"))
                .dimensionUsageOrDimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Gender")
                        .foreignKey("customer_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(false)
                                .primaryKey("customer_id")
                                .relation(new TableR("customer"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Gender")
                                        .column("gender")
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Unit Sales")
                        .column("unit_sales")
                        .aggregator("sum")
                        .formatString("Standard")
                        .visible(false)
                        .build()
                ))
                .calculatedMembers(List.of(
                    CalculatedMemberRBuilder.builder()
                        .name("With a [bracket] inside it")
                        .dimension("Measures")
                        .visible(false)
                        .formula("[Measures].[Unit Sales] * 10")
                        .calculatedMemberProperties(List.of(
                            CalculatedMemberPropertyRBuilder.builder()
                                .name("FORMAT_STRING")
                                .value("$#,##0.00")
                                .build()
                        ))
                        .build()
                ))

                .build());
            return result;

        }
    }


    public static class CurrentDateMemberUdfTestModifier1 extends RDbMappingSchemaModifier {

        /*
            "<UserDefinedFunction name=\"MockCurrentDateMember\" "
            + "className=\"mondrian.udf.MockCurrentDateMember\" /> ",

            */


        public CurrentDateMemberUdfTestModifier1(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingUserDefinedFunction> schemaUserDefinedFunctions(MappingSchema schema) {
            List<MappingUserDefinedFunction> result = new ArrayList<>();
            result.addAll(super.schemaUserDefinedFunctions(schema));
            result.add(UserDefinedFunctionRBuilder.builder()
                .name("MockCurrentDateMember")
                .className("mondrian.udf.MockCurrentDateMember")
                .build());
            return result;
        }
    }

    public static class UsagePrefixTestModifier1 extends RDbMappingSchemaModifier {

        /*
            "<Schema name=\"usagePrefixTest\">"
            + "<Dimension name='StoreX' >\n"
            + " <Hierarchy hasAll='true' primaryKey='store_id'>\n"
            + " <Table name='store_x'/>\n"
            + " <Level name='Store Value' column='value' uniqueMembers='true'/>\n"
            + " </Hierarchy>\n"
            + "</Dimension>\n"
            + "<Dimension name='StoreY' >\n"
            + " <Hierarchy hasAll='true' primaryKey='store_id'>\n"
            + " <Table name='store_y'/>\n"
            + " <Level name='Store Value' column='value' uniqueMembers='true'/>\n"
            + " </Hierarchy>\n"
            + "</Dimension>\n"
            + "<Cube name='Cheques'>\n"
            + "<Table name='cheques'>\n"
            + "<AggName name='agg_lp_xxx_cheques'>\n"
            + "<AggFactCount column='FACT_COUNT'/>\n"

            + "<AggMeasure name='[Measures].[Amount]'\n"
            + "   column='amount' />\n"
            + "        <AggLevel name=\"[StoreX].[Store Value]\" column=\"value\" />"
            + "</AggName>\n"
            + "</Table>\n"

            + "<DimensionUsage name=\"StoreX\" source=\"StoreX\" foreignKey=\"store_id\" "
            + " usagePrefix=\"firstprefix_\" />"

            + "<DimensionUsage name=\"StoreY\" source=\"StoreY\" foreignKey=\"store_id\" "
            + " usagePrefix=\"secondprefix_\" />"

            + "<Measure name='Amount' \n"
            + "    column='amount' aggregator='sum'\n"
            + "   formatString='00.0'/>\n"
            + "</Cube>"
            + "</Schema>";

         */
        public UsagePrefixTestModifier1(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("usagePrefixTest")
                .dimensions(List.of(PrivateDimensionRBuilder.builder()
                    .name("StoreX")
                    .hierarchies(List.of(
                        HierarchyRBuilder.builder()
                            .hasAll(true)
                            .primaryKey("store_id")
                            .relation(new TableR("store_x"))
                            .levels(List.of(
                                LevelRBuilder.builder()
                                    .name("Store Value")
                                    .column("value")
                                    .uniqueMembers(true)
                                    .build()
                            ))
                            .build()
                    ))
                    .build(),
                    PrivateDimensionRBuilder.builder()
                        .name("StoreY")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("store_id")
                                .relation(new TableR("store_y"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Store Value")
                                        .column("value")
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                    ))
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Cheques")
                        .fact(new TableR("cheques",
                            List.of(),
                            List.of(AggNameRBuilder.builder()
                                .name("agg_lp_xxx_cheques")
                                .aggFactCount(AggColumnNameRBuilder.builder()
                                    .column("FACT_COUNT")
                                    .build())
                                .aggMeasures(List.of(
                                    AggMeasureRBuilder.builder()
                                        .name("[Measures].[Amount]")
                                        .column("amount")
                                        .build()
                                ))
                                .aggLevels(List.of(
                                    AggLevelRBuilder.builder()
                                        .name("[StoreX].[Store Value]")
                                        .column("value")
                                        .build()
                                ))
                                .build())))
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .name("StoreX").source("StoreX").foreignKey("store_id")
                                .usagePrefix("firstprefix_")
                                .build(),
                            DimensionUsageRBuilder.builder()
                                .name("StoreY").source("StoreY").foreignKey("store_id")
                                .usagePrefix("secondprefix_")
                                .build()

                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Amount")
                                .column("amount")
                                .aggregator("sum")
                                .formatString("00.0")
                                .build()
                        ))
                        .build()
                ))
                .build();

        }
    }

    public static class DrillThroughExcludeFilterTestModifier extends RDbMappingSchemaModifier {

        /*
    String schema = "<Schema name=\"MYFoodmart\">\n"
            + "  <Dimension visible=\"true\" highCardinality=\"false\" name=\"Store\">\n"
            + "    <Hierarchy visible=\"true\" hasAll=\"true\" primaryKey=\"store_id\">\n"
            + "      <Table name=\"store\"/>\n"
            + "      <Level name=\"Store ID\" visible=\"true\" column=\"store_id\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\"/>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "  <Dimension type=\"TimeDimension\" visible=\"true\" highCardinality=\"false\" name=\"Time\">\n"
            + "    <Hierarchy name=\"Time Hierarchy\" visible=\"true\" hasAll=\"true\" primaryKey=\"time_id\">\n"
            + "      <Table name=\"time_by_day\"/>\n"
            + "      <Level name=\"Year\" visible=\"true\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"false\" levelType=\"TimeYears\" hideMemberIf=\"Never\"/>\n"
            + "      <Level name=\"Quarter\" visible=\"true\" column=\"quarter\" type=\"String\" uniqueMembers=\"false\" levelType=\"TimeQuarters\" hideMemberIf=\"Never\"/>\n"
            + "      <Level name=\"Month\" visible=\"true\" column=\"month_of_year\" nameColumn=\"the_month\" type=\"Integer\" uniqueMembers=\"false\" levelType=\"TimeMonths\" hideMemberIf=\"Never\"/>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "  <Dimension visible=\"true\" highCardinality=\"false\" name=\"Warehouse\">\n"
            + "    <Hierarchy name=\"Warehouse\" visible=\"true\" hasAll=\"true\" primaryKey=\"warehouse_id\">\n"
            + "      <Table name=\"warehouse\"/>\n"
            + "      <Level name=\"Warehouse Name\" visible=\"true\" column=\"warehouse_name\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\"/>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "  <Cube name=\"Sales\" visible=\"true\" defaultMeasure=\"Unit Sales\" cache=\"true\" enabled=\"true\">\n"
            + "    <Table name=\"sales_fact_1997\"/>\n"
            + "    <DimensionUsage source=\"Store\" name=\"Store\" visible=\"true\" foreignKey=\"store_id\" highCardinality=\"false\"/>\n"
            + "    <DimensionUsage source=\"Time\" name=\"Time\" visible=\"true\" foreignKey=\"time_id\" highCardinality=\"false\"/>\n"
            + "    <Measure name=\"Store Sales\" column=\"store_sales\" formatString=\"#,###.00\" aggregator=\"sum\"/>\n"
            + "  </Cube>\n"
            + "  <Cube name=\"Warehouse\" visible=\"true\" cache=\"true\" enabled=\"true\">\n"
            + "    <Table name=\"inventory_fact_1997\"/>\n"
            + "    <DimensionUsage source=\"Store\" name=\"Store\" visible=\"true\" foreignKey=\"store_id\" highCardinality=\"false\"/>\n"
            + "    <DimensionUsage source=\"Time\" name=\"Time\" visible=\"true\" foreignKey=\"time_id\" highCardinality=\"false\"/>\n"
            + "    <DimensionUsage source=\"Warehouse\" name=\"Warehouse\" visible=\"true\" foreignKey=\"warehouse_id\" highCardinality=\"false\"/>\n"
            + "    <Measure name=\"Warehouse Sales\" column=\"warehouse_sales\" aggregator=\"sum\"/>\n"
            + "  </Cube>\n"
            + "  <VirtualCube enabled=\"true\" name=\"Warehouse and Sales\" defaultMeasure=\"Store Sales\" visible=\"true\">\n"
            + "    <VirtualCubeDimension visible=\"true\" highCardinality=\"false\" name=\"Time\"/>\n"
            + "    <VirtualCubeMeasure cubeName=\"Sales\" name=\"[Measures].[Store Sales]\"/>\n"
            + "    <VirtualCubeMeasure cubeName=\"Warehouse\" name=\"[Measures].[Warehouse Sales]\"/>\n"
            + "  </VirtualCube>  \n"
            + "</Schema>\n";

         */
        public DrillThroughExcludeFilterTestModifier(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("MYFoodmart")
                .dimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .visible(true)
                        .highCardinality(false)
                        .name("Store")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .visible(true)
                                .hasAll(true)
                                .primaryKey("store_id")
                                .relation(new TableR("store"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Store ID")
                                        .visible(true)
                                        .column("store_id")
                                        .type(TypeEnum.STRING)
                                        .uniqueMembers(true)
                                        .levelType(LevelTypeEnum.REGULAR)
                                        .hideMemberIf(HideMemberIfEnum.NEVER)
                                        .build()
                                ))
                                .build()
                        ))
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .type(DimensionTypeEnum.TIME_DIMENSION)
                        .visible(true)
                        .highCardinality(false)
                        .name("Time")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .name("Time Hierarchy")
                                .visible(true)
                                .hasAll(true)
                                .primaryKey("time_id")
                                .relation(new TableR("time_by_day"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Year")
                                        .visible(true)
                                        .column("the_year")
                                        .type(TypeEnum.NUMERIC)
                                        .uniqueMembers(false)
                                        .levelType(LevelTypeEnum.TIME_YEARS)
                                        .hideMemberIf(HideMemberIfEnum.NEVER)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Quarter")
                                        .visible(true)
                                        .column("quarter")
                                        .type(TypeEnum.STRING)
                                        .uniqueMembers(false)
                                        .levelType(LevelTypeEnum.TIME_QUARTERS)
                                        .hideMemberIf(HideMemberIfEnum.NEVER)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Month")
                                        .visible(true)
                                        .column("month_of_year")
                                        .nameColumn("the_month")
                                        .type(TypeEnum.INTEGER)
                                        .uniqueMembers(false)
                                        .levelType(LevelTypeEnum.TIME_MONTHS)
                                        .hideMemberIf(HideMemberIfEnum.NEVER)
                                        .build()
                                ))
                                .build()
                        ))
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .visible(true)
                        .highCardinality(false)
                        .name("Warehouse")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .name("Warehouse")
                                .visible(true)
                                .hasAll(true)
                                .primaryKey("warehouse_id")
                                .relation(new TableR("warehouse"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Warehouse Name")
                                        .visible(true)
                                        .column("warehouse_name")
                                        .type(TypeEnum.STRING)
                                        .uniqueMembers(true)
                                        .levelType(LevelTypeEnum.REGULAR)
                                        .hideMemberIf(HideMemberIfEnum.NEVER)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Sales")
                        .visible(true)
                        .defaultMeasure("Unit Sales")
                        .cache(true)
                        .enabled(true)
                        .fact(new TableR("sales_fact_1997"))
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .name("Store").source("Store")
                                .visible(true)
                                .foreignKey("store_id")
                                .highCardinality(false)
                                .build(),
                            DimensionUsageRBuilder.builder()
                                .name("Time").source("Time")
                                .visible(true)
                                .foreignKey("time_id")
                                .highCardinality(false)
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Store Sales")
                                .column("store_sales")
                                .formatString("#,###.00")
                                .aggregator("sum")
                                .build()
                        ))
                        .build(),
                    CubeRBuilder.builder()
                        .name("Warehouse")
                        .visible(true)
                        .cache(true)
                        .enabled(true)
                        .fact(new TableR("inventory_fact_1997"))
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .source("Store")
                                .name("Store")
                                .visible(true)
                                .foreignKey("store_id")
                                .highCardinality(false)
                                .build(),
                            DimensionUsageRBuilder.builder()
                                .source("Time")
                                .name("Time")
                                .visible(true)
                                .foreignKey("time_id")
                                .highCardinality(false)
                                .build(),
                            DimensionUsageRBuilder.builder()
                                .source("Warehouse")
                                .name("Warehouse")
                                .visible(true)
                                .foreignKey("warehouse_id")
                                .highCardinality(false)
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Warehouse Sales")
                                .column("warehouse_sales")
                                .aggregator("sum")
                                .build()
                        ))
                        .build()
                ))
                .virtualCubes(List.of(
                    VirtualCubeRBuilder.builder()
                        .enabled(true)
                        .name("Warehouse and Sales")
                        .defaultMeasure("Store Sales")
                        .visible(true)
                        .virtualCubeDimensions(List.of(
                            VirtualCubeDimensionRBuilder.builder()
                                .visible(true)
                                .highCardinality(false)
                                .name("Time")
                                .build()
                        ))
                        .virtualCubeMeasures(List.of(
                            VirtualCubeMeasureRBuilder.builder()
                                .cubeName("Sales")
                                .name("[Measures].[Store Sales]")
                                .build(),
                            VirtualCubeMeasureRBuilder.builder()
                                .cubeName("Warehouse")
                                .name("[Measures].[Warehouse Sales]")
                                .build()
                        ))
                        .build()
                ))
                .build();
        }
    }

    public static class CompatibilityTestModifier extends RDbMappingSchemaModifier {

        /*
        final String cubeName = "Sales_inline";
        String schema = SchemaUtil.getSchema(
    		baseSchema,
            null,
            "<Cube name=\"" + cubeName + "\">\n"
            + "  <Table name=\"sales_fact_1997\"/>\n"
            + "  <DimensionUsage name=\"Time\" source=\"Time\" foreignKey=\"time_id\"/>\n"
            + "  <Dimension name=\"Alternative Promotion\" foreignKey=\"promotion_id\">\n"
            + "    <Hierarchy hasAll=\"true\" primaryKey=\"promo_id\">\n"
            + "      <InlineTable alias=\"alt_promotion\">\n"
            + "        <ColumnDefs>\n"
            + "          <ColumnDef name=\"promo_id\" type=\"Numeric\"/>\n"
            + "          <ColumnDef name=\"promo_name\" type=\"String\"/>\n"
            + "        </ColumnDefs>\n"
            + "        <Rows>\n"
            + "          <Row>\n"
            + "            <Value column=\"promo_id\">0</Value>\n"
            + "          </Row>\n"
            + "          <Row>\n"
            + "            <Value column=\"promo_id\">1</Value>\n"
            + "            <Value column=\"promo_name\">Promo1</Value>\n"
            + "          </Row>\n"
            + "        </Rows>\n"
            + "      </InlineTable>\n"
            + "      <Level name=\"Alternative Promotion\" column=\"promo_id\" nameColumn=\"promo_name\" uniqueMembers=\"true\"/> \n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"Standard\" visible=\"false\"/>\n"
            + "  <Measure name=\"Store Sales\" column=\"store_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"#,###.00\"/>\n"
            + "</Cube>", null, null, null, null);
         */
        public CompatibilityTestModifier(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCube> schemaCubes(MappingSchema mappingSchemaOriginal) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(mappingSchemaOriginal));
            result.add(CubeRBuilder.builder()
                .name("Sales_inline")
                .fact(new TableR("sales_fact_1997"))
                .dimensionUsageOrDimensions(List.of(
                    DimensionUsageRBuilder.builder()
                        .name("Time")
                        .source("Time")
                        .foreignKey("time_id")
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .name("Alternative Promotion")
                        .foreignKey("promotion_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("promo_id")
                                .relation(InlineTableRBuilder.builder()
                                    .alias("alt_promotion")
                                    .columnDefs(List.of(
                                        ColumnDefRBuilder.builder()
                                            .name("promo_id")
                                            .type(TypeEnum.NUMERIC)
                                            .build(),
                                        ColumnDefRBuilder.builder()
                                            .name("promo_name")
                                            .type(TypeEnum.STRING)
                                            .build()
                                    ))
                                    .rows(List.of(
                                        RowRBuilder.builder()
                                            .values(List.of(
                                                ValueRBuilder.builder()
                                                    .column("promo_id")
                                                    .content("0")
                                                    .build()
                                            ))
                                            .build(),
                                        RowRBuilder.builder()
                                            .values(List.of(
                                                ValueRBuilder.builder()
                                                    .column("promo_id")
                                                    .content("1")
                                                    .build(),
                                                ValueRBuilder.builder()
                                                    .column("promo_name")
                                                    .content("Promo1")
                                                    .build()

                                            ))
                                            .build()
                                    ))
                                    .build())
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Alternative Promotion")
                                        .column("promo_id")
                                        .nameColumn("promo_name")
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Unit Sales")
                        .column("unit_sales")
                        .aggregator("sum")
                        .formatString("Standard")
                        .visible(false)
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Store Sales")
                        .column("store_sales")
                        .aggregator("sum")
                        .formatString("#,###.00")
                        .build()
                ))
                .build());
            return result;

        }

    }

    public static class CompatibilityTestModifier2 extends RDbMappingSchemaModifier {

        /*
        final String cubeName = "Store_NullsCollation";
            "<Cube name=\"" + cubeName + "\">\n"
            + "  <Table name=\"store\"/>\n"
            + "  <Dimension name=\"Store\" foreignKey=\"store_id\">\n"
            + "    <Hierarchy hasAll=\"true\" primaryKey=\"store_id\">\n"
            + "      <Level name=\"Store Name\" column=\"store_name\"  uniqueMembers=\"true\">\n"
            + "       <OrdinalExpression>\n"
            + "        <SQL dialect=\"access\">\n"
            + "           Iif(store_name = 'HQ', null, store_name)\n"
            + "       </SQL>\n"
            + "        <SQL dialect=\"oracle\">\n"
            + "           case \"store_name\" when 'HQ' then null else \"store_name\" end\n"
            + "       </SQL>\n"
            + "        <SQL dialect=\"hsqldb\">\n"
            + "           case \"store_name\" when 'HQ' then null else \"store_name\" end\n"
            + "       </SQL>\n"
            + "        <SQL dialect=\"db2\">\n"
            + "           case \"store\".\"store_name\" when 'HQ' then null else \"store\".\"store_name\" end\n"
            + "       </SQL>\n"
            + "        <SQL dialect=\"luciddb\">\n"
            + "           case \"store_name\" when 'HQ' then null else \"store_name\" end\n"
            + "       </SQL>\n"
            + "        <SQL dialect=\"netezza\">\n"
            + "           case \"store_name\" when 'HQ' then null else \"store_name\" end\n"
            + "       </SQL>\n"
            + "        <SQL dialect=\"generic\">\n"
            + "           case store_name when 'HQ' then null else store_name end\n"
            + "       </SQL>\n"
            + "       </OrdinalExpression>\n"
            + "        <Property name=\"Store Sqft\" column=\"store_sqft\" type=\"Numeric\"/>\n"
            + "      </Level>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "  <Measure name=\"Store Sqft\" column=\"store_sqft\" aggregator=\"sum\"\n"
            + "      formatString=\"#,###\"/>\n"
            + "</Cube>",
         */
        public CompatibilityTestModifier2(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingCube> schemaCubes(MappingSchema mappingSchemaOriginal) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(mappingSchemaOriginal));
            result.add(CubeRBuilder.builder()
                .name("Store_NullsCollation")
                .fact(new TableR("store"))
                .dimensionUsageOrDimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Store")
                        .foreignKey("store_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("store_id")
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Store Name")
                                        .column("store_name")
                                        .uniqueMembers(true)
                                        .ordinalExpression(ExpressionViewRBuilder.builder()
                                            .sqls(List.of(
                                                new SQLR("Iif(store_name = 'HQ', null, store_name)", "access"),
                                                new SQLR("case \"store_name\" when 'HQ' then null else \"store_name\" end",
                                                    "oracle"),
                                                new SQLR("case \"store_name\" when 'HQ' then null else \"store_name\" end",
                                                    "hsqldb"),
                                                new SQLR("case \"store\".\"store_name\" when 'HQ' then null else \"store\".\"store_name\" end",
                                                    "db2"),
                                                new SQLR("case \"store_name\" when 'HQ' then null else \"store_name\" end",
                                                    "luciddb"),
                                                new SQLR("case \"store_name\" when 'HQ' then null else \"store_name\" end",
                                                    "netezza"),
                                                new SQLR("case store_name when 'HQ' then null else store_name end",
                                                    "generic")
                                            ))
                                            .build())
                                        .properties(List.of(
                                            PropertyRBuilder.builder()
                                                .name("Store Sqft")
                                                .column("store_sqft")
                                                .type(PropertyTypeEnum.NUMERIC)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Unit Sales")
                        .column("unit_sales")
                        .aggregator("sum")
                        .formatString("Standard")
                        .visible(false)
                        .build(),
                    MeasureRBuilder.builder()
                        .name("Store Sales")
                        .column("store_sales")
                        .aggregator("sum")
                        .formatString("#,###.00")
                        .build()
                ))
                .build());
            return result;

        }

    }

    public static class AccessControlTestModifier1 extends RDbMappingSchemaModifier {

        /*
                "<Role name=\"Role1\">\n"
                + "  <SchemaGrant access=\"none\">\n"
                + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
                + "      <HierarchyGrant hierarchy=\"[Customers]\" access=\"custom\" rollupPolicy=\"bad\" bottomLevel=\"[Customers].[City]\">\n"
                + "        <MemberGrant member=\"[Customers].[USA]\" access=\"all\"/>\n"
                + "        <MemberGrant member=\"[Customers].[USA].[CA].[Los Angeles]\" access=\"none\"/>\n"
                + "      </HierarchyGrant>\n"
                + "    </CubeGrant>\n"
                + "  </SchemaGrant>\n"
                + "</Role>");
        */

        public AccessControlTestModifier1(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(RoleRBuilder.builder()
                .name("Role1")
                .schemaGrants(List.of(
                    SchemaGrantRBuilder.builder()
                        .access(AccessEnum.NONE)
                        .cubeGrants(List.of(
                            CubeGrantRBuilder.builder()
                                .cube("Sales")
                                .access("all")
                                .hierarchyGrants(List.of(
                                    HierarchyGrantRBuilder.builder()
                                        .hierarchy("[Customers]")
                                        .access(AccessEnum.CUSTOM)
                                        .rollupPolicy("bad")
                                        .bottomLevel("[Customers].[City]")
                                        .memberGrants(List.of(
                                            MemberGrantRBuilder.builder()
                                                .member("[Customers].[USA]")
                                                .access(MemberGrantAccessEnum.ALL)
                                                .build(),
                                            MemberGrantRBuilder.builder()
                                                .member("[Customers].[USA].[CA].[Los Angeles]")
                                                .access(MemberGrantAccessEnum.NONE)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .build());
            return result;
        }
    }

    public static class AccessControlTestModifier2 extends RDbMappingSchemaModifier {

        /*
                "<Role name=\"Role1\">\n"
                + "  <SchemaGrant access=\"none\">\n"
                + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
                + "      <HierarchyGrant hierarchy=\"[Customers]\" access=\"custom\" rollupPolicy=\"Partial\">\n"
                + "        <MemberGrant member=\"[Customers].[USA].[CA]\" access=\"all\"/>\n"
                + "        <MemberGrant member=\"[Customers].[USA].[CA].[San Francisco].[Gladys Evans]\" access=\"none\"/>\n"
                + "      </HierarchyGrant>\n"
                + "      <HierarchyGrant hierarchy=\"[Promotion Media]\" access=\"all\"/>\n"
                + "      <HierarchyGrant hierarchy=\"[Marital Status]\" access=\"none\"/>\n"
                + "      <HierarchyGrant hierarchy=\"[Gender]\" access=\"none\"/>\n"
                + "      <HierarchyGrant hierarchy=\"[Store]\" access=\"custom\" rollupPolicy=\"Partial\" topLevel=\"[Store].[Store State]\"/>\n"
                + "    </CubeGrant>\n"
                + "    <CubeGrant cube=\"Warehouse\" access=\"all\"/>\n"
                + "  </SchemaGrant>\n"
                + "</Role>\n"
                + "<Role name=\"Role2\">\n"
                + "  <SchemaGrant access=\"none\">\n"
                + "    <CubeGrant cube=\"Sales\" access=\"none\">\n"
                + "      <HierarchyGrant hierarchy=\"[Customers]\" access=\"custom\" rollupPolicy=\"Hidden\">\n"
                + "        <MemberGrant member=\"[Customers].[USA]\" access=\"all\"/>\n"
                + "        <MemberGrant member=\"[Customers].[USA].[CA]\" access=\"none\"/>\n"
                + "        <MemberGrant member=\"[Customers].[USA].[OR]\" access=\"none\"/>\n"
                + "        <MemberGrant member=\"[Customers].[USA].[OR].[Portland]\" access=\"all\"/>\n"
                + "      </HierarchyGrant>\n"
                + "      <HierarchyGrant hierarchy=\"[Store]\" access=\"all\" rollupPolicy=\"Hidden\"/>\n"
                + "    </CubeGrant>\n"
                + "  </SchemaGrant>\n"
                + "</Role>\n");
        */

        public AccessControlTestModifier2(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                .name("Role1")
                .schemaGrants(List.of(
                    SchemaGrantRBuilder.builder()
                        .access(AccessEnum.NONE)
                        .cubeGrants(List.of(
                            CubeGrantRBuilder.builder()
                                .cube("Sales")
                                .access("all")
                                .hierarchyGrants(List.of(
                                    HierarchyGrantRBuilder.builder()
                                        .hierarchy("[Customers]")
                                        .access(AccessEnum.CUSTOM)
                                        .rollupPolicy("Partial")
                                        .bottomLevel("[Customers].[City]")
                                        .memberGrants(List.of(
                                            MemberGrantRBuilder.builder()
                                                .member("[Customers].[USA].[CA]")
                                                .access(MemberGrantAccessEnum.ALL)
                                                .build(),
                                            MemberGrantRBuilder.builder()
                                                .member("[Customers].[USA].[CA].[San Francisco].[Gladys Evans]")
                                                .access(MemberGrantAccessEnum.NONE)
                                                .build()
                                        ))
                                        .build(),
                                    HierarchyGrantRBuilder.builder()
                                        .hierarchy("[Promotion Media]")
                                        .access(AccessEnum.ALL)
                                        .build(),
                                    HierarchyGrantRBuilder.builder()
                                        .hierarchy("[Marital Status]")
                                        .access(AccessEnum.NONE)
                                        .build(),
                                    HierarchyGrantRBuilder.builder()
                                        .hierarchy("[Gender]")
                                        .access(AccessEnum.NONE)
                                        .build(),
                                    HierarchyGrantRBuilder.builder()
                                        .hierarchy("[Store]")
                                        .access(AccessEnum.CUSTOM)
                                        .rollupPolicy("Partial")
                                        .topLevel("[Store].[Store State]")
                                        .build()
                                ))
                                .build(),
                            CubeGrantRBuilder.builder()
                                .cube("Warehouse")
                                .access("all")
                                .build()
                        ))
                        .build()
                ))
                .build()
            );
            result.add(
                RoleRBuilder.builder()
                .name("Role2")
                .schemaGrants(List.of(
                    SchemaGrantRBuilder.builder()
                        .access(AccessEnum.NONE)
                        .cubeGrants(List.of(
                            CubeGrantRBuilder.builder()
                                .cube("Sales")
                                .access("none")
                                .hierarchyGrants(List.of(
                                    HierarchyGrantRBuilder.builder()
                                        .hierarchy("[Customers]")
                                        .access(AccessEnum.CUSTOM)
                                        .rollupPolicy("Hidden")
                                        .memberGrants(List.of(
                                            MemberGrantRBuilder.builder()
                                                .member("[Customers].[USA]")
                                                .access(MemberGrantAccessEnum.ALL)
                                                .build(),
                                            MemberGrantRBuilder.builder()
                                                .member("[Customers].[USA].[CA]")
                                                .access(MemberGrantAccessEnum.NONE)
                                                .build(),
                                            MemberGrantRBuilder.builder()
                                                .member("[Customers].[USA].[OR]")
                                                .access(MemberGrantAccessEnum.NONE)
                                                .build(),
                                            MemberGrantRBuilder.builder()
                                                .member("[Customers].[USA].[OR].[Portland]")
                                                .access(MemberGrantAccessEnum.ALL)
                                                .build()
                                        ))
                                        .build(),
                                    HierarchyGrantRBuilder.builder()
                                        .hierarchy("[Store]")
                                        .access(AccessEnum.ALL)
                                        .rollupPolicy("Hidden")
                                        .build()

                                ))
                                .build()
                        ))
                        .build()
                ))
                .build()
            );
            return result;
        }
    }

    public static class AccessControlTestModifier3 extends RDbMappingSchemaModifier {

        /*
        String roleDefs =
            "<Role name=\"USA manager\">\n"
            + "  <SchemaGrant access=\"none\">\n"
            + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
            + "      <DimensionGrant access=\"all\" dimension=\"[Measures]\"/>\n"
            + "      <HierarchyGrant access=\"custom\" hierarchy=\"[Customers]\">\n"
            + "        <MemberGrant access=\"all\" member=\"[Customers].[USA]\"/>\n"
            + "      </HierarchyGrant>\n"
            + "    </CubeGrant>\n"
            + "  </SchemaGrant>\n"
            + "</Role>\n"
            + "<Role name=\"parent of USA manager\">\n"
            + "  <Union>\n"
            + "    <RoleUsage roleName=\"USA manager\"/>\n"
            + "  </Union>\n"
            + "</Role>"
            + "<Role name=\"grandparent of USA manager\">\n"
            + "  <Union>\n"
            + "    <RoleUsage roleName=\"parent of USA manager\"/>\n"
            + "  </Union>\n"
            + "</Role>";
        */

        public AccessControlTestModifier3(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("USA manager")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .dimensionGrants(List.of(
                                        DimensionGrantRBuilder.builder()
                                            .access(AccessEnum.ALL)
                                            .dimension("[Measures]")
                                            .build()
                                    ))
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Customers]")
                                            .access(AccessEnum.CUSTOM)
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );
            result.add(
                RoleRBuilder.builder()
                    .name("parent of USA manager")
                    .union(UnionRBuilder.builder()
                        .roleUsages(List.of(
                            RoleUsageRBuilder.builder()
                                .roleName("USA manager")
                                .build()
                        ))
                        .build())
                    .build()
            );
            result.add(
                RoleRBuilder.builder()
                    .name("grandparent of USA manager")
                    .union(UnionRBuilder.builder()
                        .roleUsages(List.of(
                            RoleUsageRBuilder.builder()
                                .roleName("parent of USA manager")
                                .build()
                        ))
                        .build())
                    .build()
            );
            return result;
        }
    }

    public static class AccessControlTestModifier4 extends RDbMappingSchemaModifier {

        /*
                "<Role name=\"Role1\">\n"
                + "  <SchemaGrant access=\"none\">\n"
                + "  </SchemaGrant>\n"
                + "</Role>\n"
                + "<Role name=\"Role2\">\n"
                + "  <SchemaGrant access=\"all\">\n"
                + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
                + "      <HierarchyGrant hierarchy=\"[Customers]\" access=\"custom\" rollupPolicy=\"partial\">\n"
                + "        <MemberGrant member=\"[Customers].[USA].[OR]\" access=\"all\"/>\n"
                + "      </HierarchyGrant>\n"
                + "    </CubeGrant>\n"
                + "  </SchemaGrant>\n"
                + "</Role>\n");

        */

        public AccessControlTestModifier4(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Role1")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .build()
                    ))
                    .build()
            );
            result.add(
                RoleRBuilder.builder()
                    .name("Role2")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.ALL)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Customers]")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("partial")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA].[OR]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );
            return result;
        }
    }

    public static class AccessControlTestModifier5 extends RDbMappingSchemaModifier {

        /*
                "<Role name=\"Role1\">\n"
                + "  <SchemaGrant access=\"all\">\n"
                + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
                + "      <HierarchyGrant hierarchy=\"[Customers]\" access=\"custom\" rollupPolicy=\"Partial\" topLevel=\"[Customers].[State Province]\" bottomLevel=\"[Customers].[State Province]\">\n"
                + "        <MemberGrant member=\"[Customers].[USA].[CA]\" access=\"all\"/>\n"
                + "      </HierarchyGrant>\n"
                + "    </CubeGrant>\n"
                + "  </SchemaGrant>\n"
                + "</Role>\n"
                + "<Role name=\"Role2\">\n"
                + "  <SchemaGrant access=\"none\">\n"
                + "  </SchemaGrant>\n"
                + "</Role>\n");

        */

        public AccessControlTestModifier5(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Role1")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.ALL)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Customers]")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("partial")
                                            .topLevel("[Customers].[State Province]")
                                            .bottomLevel("[Customers].[State Province]")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA].[CA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );
            result.add(
                RoleRBuilder.builder()
                    .name("Role2")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .build()
                    ))
                    .build()
            );
            return result;
        }
    }

    public static class AccessControlTestModifier6 extends RDbMappingSchemaModifier {

        /*
                "<Role name=\"Role1\">\n"
                + "  <SchemaGrant access=\"none\">\n"
                + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
                + "      <HierarchyGrant hierarchy=\"[Product]\" access=\"custom\">\n"
                + "        <MemberGrant member=\"[Product].[Drink]\" access=\"all\"/>\n"
                + "      </HierarchyGrant>\n"
                + "    </CubeGrant>\n"
                + "  </SchemaGrant>\n"
                + "</Role>");
        */

        public AccessControlTestModifier6(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Role1")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Product]")
                                            .access(AccessEnum.CUSTOM)
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Product].[Drink]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );
            return result;
        }
    }

    public static class AccessControlTestModifier7 extends RDbMappingSchemaModifier {

        /*
                "<Role name=\"California manager\">\n"
                + "  <SchemaGrant access=\"none\">\n"
                + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
                + "      <HierarchyGrant hierarchy=\"[Store]\" access=\"none\" />\n"
                + "    </CubeGrant>\n"
                + "    <CubeGrant cube=\"Sales Ragged\" access=\"all\">\n"
                + "      <HierarchyGrant hierarchy=\"[Store]\" access=\"custom\" />\n"
                + "    </CubeGrant>\n"
                + "  </SchemaGrant>\n"
                + "</Role>");
        */

        public AccessControlTestModifier7(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("California manager")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store]")
                                            .access(AccessEnum.NONE)
                                            .build()
                                    ))
                                    .build(),
                                CubeGrantRBuilder.builder()
                                    .cube("Sales Ragged")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store]")
                                            .access(AccessEnum.CUSTOM)
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );
            return result;
        }
    }

    public static class AccessControlTestModifier8 extends RDbMappingSchemaModifier {

        /*
            "<Role name=\"Buggy Role\">\n"
            + "  <SchemaGrant access=\"none\">\n"
            + "    <CubeGrant cube=\"HR\" access=\"all\">\n"
            + "      <HierarchyGrant hierarchy=\"[Employees]\" access=\"custom\"\n"
            + "                      rollupPolicy=\"partial\">\n"
            + "        <MemberGrant\n"
            + "            member=\"[Employees].[All Employees].[Sheri Nowmer].[Darren Stanz]\"\n"
            + "            access=\"all\"/>\n"
            + "      </HierarchyGrant>\n"
            + "      <HierarchyGrant hierarchy=\"[Store]\" access=\"custom\"\n"
            + "                      rollupPolicy=\"partial\">\n"
            + "        <MemberGrant member=\"[Store].[All Stores].[USA].[CA]\" access=\"all\"/>\n"
            + "      </HierarchyGrant>\n"
            + "    </CubeGrant>\n"
            + "  </SchemaGrant>\n"
            + "</Role>");
        */

        public AccessControlTestModifier8(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Buggy Role")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("HR")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Employees]")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("partial")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Employees].[All Employees].[Sheri Nowmer].[Darren Stanz]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build(),
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store]")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("partial")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[All Stores].[USA].[CA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()

                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );
            return result;
        }
    }

    public static class AccessControlTestModifier9 extends RDbMappingSchemaModifier {

        /*
        "<Role name=\"role1\">\n"
        + " <SchemaGrant access=\"none\">\n"
        + "  <CubeGrant cube=\"Warehouse\" access=\"all\">\n"
        + "   <HierarchyGrant hierarchy=\"[Store Size in SQFT]\" access=\"custom\" rollupPolicy=\"partial\">\n"
        + "    <MemberGrant member=\"[Store Size in SQFT].[20319]\" access=\"all\"/>\n"
        + "    <MemberGrant member=\"[Store Size in SQFT].[21215]\" access=\"none\"/>\n"
        + "   </HierarchyGrant>\n"
        + "   <HierarchyGrant hierarchy=\"[Store Type]\" access=\"custom\" rollupPolicy=\"partial\">\n"
        + "    <MemberGrant member=\"[Store Type].[Supermarket]\" access=\"all\"/>\n"
        + "   </HierarchyGrant>\n"
        + "  </CubeGrant>\n"
        + " </SchemaGrant>\n"
        + "</Role>";
        */

        public AccessControlTestModifier9(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("role1")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Warehouse")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store Size in SQFT]")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("partial")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store Size in SQFT].[20319]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store Size in SQFT].[21215]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build()
                                            ))
                                            .build(),
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store Type]")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("partial")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store Type].[Supermarket]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );
            return result;
        }
    }

    public static class AccessControlTestModifier10 extends RDbMappingSchemaModifier {

        /*
            "<Role name=\"VCRole\">\n"
            + "  <SchemaGrant access=\"none\">\n"
            + "    <CubeGrant cube=\"Warehouse and Sales\" access=\"all\">\n"
            + "      <HierarchyGrant hierarchy=\"[Store]\" access=\"custom\">\n"
            + "        <MemberGrant member=\"[Store].[USA].[CA]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Store].[USA].[CA].[Los Angeles]\" access=\"none\"/>\n"
            + "      </HierarchyGrant>\n"
            + "      <HierarchyGrant hierarchy=\"[Customers]\" access=\"custom\"\n"
            + "          topLevel=\"[Customers].[State Province]\" bottomLevel=\"[Customers].[City]\">\n"
            + "        <MemberGrant member=\"[Customers].[USA].[CA]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Customers].[USA].[CA].[Los Angeles]\" access=\"none\"/>\n"
            + "      </HierarchyGrant>\n"
            + "      <HierarchyGrant hierarchy=\"[Gender]\" access=\"none\"/>\n"
            + "    </CubeGrant>\n"
            + "  </SchemaGrant>\n"
            + "</Role>");
        */

        public AccessControlTestModifier10(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("VCRole")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Warehouse and Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store]")
                                            .access(AccessEnum.CUSTOM)
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[CA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[CA].[Los Angeles]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build()
                                            ))
                                            .build(),
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Customers]")
                                            .access(AccessEnum.CUSTOM)
                                            .topLevel("[Customers].[State Province]")
                                            .bottomLevel("[Customers].[City]")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA].[CA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA].[CA].[Los Angeles]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build()
                                            ))
                                            .build(),
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Gender]")
                                            .access(AccessEnum.NONE)
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );
            return result;
        }
    }

    public static class AccessControlTestModifier11 extends RDbMappingSchemaModifier {

        /*
            "<Role name=\"role2\">"
            + " <SchemaGrant access=\"none\">"
            + "  <CubeGrant cube=\"Sales\" access=\"all\">"
            + "   <HierarchyGrant hierarchy=\"[Store]\" access=\"custom\" rollupPolicy=\"partial\">"
            + "    <MemberGrant member=\"[Store].[USA].[CA]\" access=\"all\"/>"
            + "    <MemberGrant member=\"[Store].[USA].[CA].[Los Angeles]\" access=\"none\"/>"
            + "   </HierarchyGrant>"
            + "  </CubeGrant>"
            + " </SchemaGrant>"
            + "</Role>";
        */

        public AccessControlTestModifier11(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("role2")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store]")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("partial")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[CA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[CA].[Los Angeles]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );
            return result;
        }
    }

    public static class AccessControlTestModifier12 extends RDbMappingSchemaModifier {

        private List<MappingRole> roles;


        public AccessControlTestModifier12(MappingSchema mappingSchema, List<MappingRole> roles) {
            super(mappingSchema);
            this.roles = roles;
        }

                /*
                    " <Dimension name=\"Customers\"> \n"
            + "    <Hierarchy hasAll=\"true\" primaryKey=\"customer_id\"> \n"
            + "      <Table name=\"customer\"/> \n"
            + "      <Level name=\"Country\" column=\"country\" uniqueMembers=\"true\"/> \n"
            + "      <Level name=\"State Province\" column=\"state_province\" uniqueMembers=\"true\"/> \n"
            + "      <Level name=\"City\" column=\"city\" uniqueMembers=\"false\"/> \n"
            + "      <Level name=\"Name\" column=\"customer_id\" type=\"Numeric\" uniqueMembers=\"true\"/> \n"
            + "    </Hierarchy> \n"
            + "  </Dimension> ",

        */
        @Override
        protected List<MappingPrivateDimension> schemaDimensions(MappingSchema mappingSchemaOriginal) {
            List<MappingPrivateDimension> result = new ArrayList<>();
            result.addAll(super.schemaDimensions(mappingSchemaOriginal));
            result.add(PrivateDimensionRBuilder.builder()
                .name("Customers")
                .hierarchies(List.of(
                    HierarchyRBuilder.builder()
                        .hasAll(true)
                        .primaryKeyTable("customer_id")
                        .relation(new TableR("customer"))
                        .levels(List.of(
                            LevelRBuilder.builder()
                                .name("Country")
                                .column("country")
                                .uniqueMembers(true)
                                .build(),
                            LevelRBuilder.builder()
                                .name("State Province")
                                .column("state_province")
                                .uniqueMembers(true)
                                .build(),
                            LevelRBuilder.builder()
                                .name("City")
                                .column("city")
                                .uniqueMembers(false)
                                .build(),
                            LevelRBuilder.builder()
                                .name("Name")
                                .column("customer_id")
                                .type(TypeEnum.NUMERIC)
                                .uniqueMembers(true)
                                .build()
                        ))
                        .build()
                ))
                .build());
            return result;
        }
        /*
                    "  <Cube name=\"" + cubeName + "\"> \n"
            + "    <Table name=\"sales_fact_1997\"/> \n"
            + "    <DimensionUsage name=\"Time\" source=\"Time\" foreignKey=\"time_id\"/> \n"
            + "    <DimensionUsage name=\"Product\" source=\"Product\" foreignKey=\"product_id\"/> \n"
            + "    <DimensionUsage name=\"Customers\" source=\"Customers\" foreignKey=\"customer_id\"/> \n"
            + "    <DimensionUsage name=\"Customers2\" source=\"Customers\" foreignKey=\"customer_id\"/> \n"
            + "    <DimensionUsage name=\"Customers3\" source=\"Customers\" foreignKey=\"customer_id\"/> \n"
            + "    <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\" formatString=\"Standard\"/> \n"
            + "  </Cube> \n",

         */
        @Override
        protected List<MappingCube> schemaCubes(MappingSchema mappingSchemaOriginal) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(mappingSchemaOriginal));
            result.add(CubeRBuilder.builder()
                .name("Sales with multiple customers")
                .fact(new TableR("sales_fact_1997"))
                .dimensionUsageOrDimensions(List.of(
                    DimensionUsageRBuilder.builder()
                        .name("Time")
                        .source("Time")
                        .foreignKey("time_id")
                        .build(),
                    DimensionUsageRBuilder.builder()
                        .name("Product")
                        .source("Product")
                        .foreignKey("product_id")
                        .build(),
                    DimensionUsageRBuilder.builder()
                        .name("Customers")
                        .source("Customers")
                        .foreignKey("customer_id")
                        .build(),
                    DimensionUsageRBuilder.builder()
                        .name("Customers2")
                        .source("Customers")
                        .foreignKey("customer_id")
                        .build(),
                    DimensionUsageRBuilder.builder()
                        .name("Customers3")
                        .source("Customers")
                        .foreignKey("customer_id")
                        .build()
                ))
                .build());
            return result;
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.addAll(roles);
            return result;
        }
    }

    public static class AccessControlTestModifier14 extends RDbMappingSchemaModifier {

        /*
                        "<Role name=\"REG1\"> \n"
                + "  <SchemaGrant access=\"none\"> \n"
                + "    <CubeGrant cube=\"HR\" access=\"all\"> \n"
                + "      <HierarchyGrant hierarchy=\"Employees\" access=\"custom\" rollupPolicy=\"partial\"> \n"
                + "        <MemberGrant member=\"[Employees].[All Employees]\" access=\"none\"/>\n"
                + "        <MemberGrant member=\"[Employees].[Sheri Nowmer].[Derrick Whelply].[Laurie Borges].[Cody Goldey].[Shanay Steelman].[Steven Betsekas]\" access=\"all\"/> \n"
                + "        <MemberGrant member=\"[Employees].[Sheri Nowmer].[Derrick Whelply].[Laurie Borges].[Cody Goldey].[Shanay Steelman].[Arvid Ziegler]\" access=\"all\"/> \n"
                + "        <MemberGrant member=\"[Employees].[Sheri Nowmer].[Derrick Whelply].[Laurie Borges].[Cody Goldey].[Shanay Steelman].[Ann Weyerhaeuser]\" access=\"all\"/> \n"
                + "      </HierarchyGrant> \n"
                + "    </CubeGrant> \n"
                + "  </SchemaGrant> \n"
                + "</Role>");
        */

        public AccessControlTestModifier14(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("REG1")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("HR")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("Employees")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("partial")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Employees].[All Employees]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Employees].[Sheri Nowmer].[Derrick Whelply].[Laurie Borges].[Cody Goldey].[Shanay Steelman].[Steven Betsekas]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Employees].[Sheri Nowmer].[Derrick Whelply].[Laurie Borges].[Cody Goldey].[Shanay Steelman].[Arvid Ziegler]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Employees].[Sheri Nowmer].[Derrick Whelply].[Laurie Borges].[Cody Goldey].[Shanay Steelman].[Ann Weyerhaeuser]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                                ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );
            return result;
        }
    }

    public static class AccessControlTestModifier15 extends RDbMappingSchemaModifier {

        /*
            "<Role name=\"CTO\">\n"
            + "  <SchemaGrant access=\"none\">\n"
            + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
            + "      <HierarchyGrant hierarchy=\"[Customers]\" access=\"custom\">\n"
            + "        <MemberGrant member=\"[Customers].[USA].[XX]\" access=\"none\"/>\n"
            + "        <MemberGrant member=\"[Customers].[USA].[XX].[Yyy Yyyyyyy]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Customers].[USA]\" access=\"none\"/>\n"
            + "        <MemberGrant member=\"[Customers].[USA].[CA]\" access=\"none\"/>\n"
            + "        <MemberGrant member=\"[Customers].[USA].[CA].[Los Angeles]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Customers].[USA].[CA].[Zzz Zzzz]\" access=\"none\"/>\n"
            + "        <MemberGrant member=\"[Customers].[USA].[CA].[San Francisco]\" access=\"all\"/>\n"
            + "      </HierarchyGrant>\n"
            + "      <HierarchyGrant hierarchy=\"[Gender]\" access=\"none\"/>\n"
            + "    </CubeGrant>\n"
            + "  </SchemaGrant>\n"
            + "</Role>");

        */

        public AccessControlTestModifier15(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("CTO")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Customers]")
                                            .access(AccessEnum.CUSTOM)
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA].[XX]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA].[XX].[Yyy Yyyyyyy]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA].[CA]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA].[CA].[Los Angeles]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA].[CA].[Zzz Zzzz]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA].[CA].[San Francisco]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build(),
                                        HierarchyGrantRBuilder.builder().hierarchy("[Gender]").access(AccessEnum.NONE)
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );
            return result;
        }
    }

    public static class AccessControlTestModifier16 extends RDbMappingSchemaModifier {

        /*
                "<Role name=\"Role1\">\n"
                + "  <SchemaGrant access=\"none\">\n"
                + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
                + "      <HierarchyGrant hierarchy=\"[Store]\" access=\"custom\" rollupPolicy=\"Partial\" topLevel=\"[Store].[Store State]\">\n"
                + "      </HierarchyGrant>\n"
                + "    </CubeGrant>\n"
                + "  </SchemaGrant>\n"
                + "</Role>\n");
        */

        public AccessControlTestModifier16(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Role1")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store]")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("Partial")
                                            .topLevel("[Store].[Store State]")
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );
            return result;
        }
    }

    public static class AccessControlTestModifier17 extends RDbMappingSchemaModifier {

        /*
                "<Role name=\"Role1\">\n"
                + "  <SchemaGrant access=\"none\">\n"
                + "    <CubeGrant cube=\"Sales\" access=\"none\">\n"
                + "      <HierarchyGrant hierarchy=\"[Measures]\" access=\"custom\">\n"
                + "        <MemberGrant member=\"[Measures].[Unit Sales]\" access=\"all\"/>\n"
                + "      </HierarchyGrant>"
                + "    </CubeGrant>\n"
                + "  </SchemaGrant>\n"
                + "</Role>\n"
                + "<Role name=\"Role2\">\n"
                + "  <SchemaGrant access=\"none\">\n"
                + "    <CubeGrant cube=\"Sales Ragged\" access=\"all\"/>\n"
                + "  </SchemaGrant>\n"
                + "</Role>");
        */

        public AccessControlTestModifier17(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Role1")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("none")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Measures]")
                                            .access(AccessEnum.CUSTOM)
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Measures].[Unit Sales]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            result.add(
                RoleRBuilder.builder()
                    .name("Role2")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales Ragged")
                                    .access("all")
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            return result;
        }
    }

    public static class AccessControlTestModifier18 extends RDbMappingSchemaModifier {

        /*
                "<Role name='Role1'>\n"
                + "  <SchemaGrant access='none'>\n"
                + "    <CubeGrant cube='Sales' access='all'>\n"
                + "      <HierarchyGrant hierarchy='[Store Type]' access='custom' rollupPolicy='partial'>\n"
                + "        <MemberGrant member='[Store Type].[All Store Types]' access='none'/>\n"
                + "        <MemberGrant member='[Store Type].[Supermarket]' access='all'/>\n"
                + "      </HierarchyGrant>\n"
                + "      <HierarchyGrant hierarchy='[Customers]' access='custom' rollupPolicy='partial' >\n"
                + "        <MemberGrant member='[Customers].[All Customers]' access='none'/>\n"
                + "        <MemberGrant member='[Customers].[USA].[WA]' access='all'/>\n"
                + "        <MemberGrant member='[Customers].[USA].[CA]' access='none'/>\n"
                + "        <MemberGrant member='[Customers].[USA].[CA].[Los Angeles]' access='all'/>\n"
                + "      </HierarchyGrant>\n"
                + "    </CubeGrant>\n"
                + "  </SchemaGrant>\n"
                + "</Role>\n");

        */

        public AccessControlTestModifier18(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Role1")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store Type]")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("partial")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store Type].[All Store Types]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store Type].[Supermarket]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build(),
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Customers]")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("partial")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[All Customers]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA].[WA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA].[CA]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA].[CA].[Los Angeles]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );
            return result;
        }
    }

    public static class AccessControlTestModifier19 extends RDbMappingSchemaModifier {

        /*
            "<Role name=\"Role1\">\n"
            + "  <SchemaGrant access=\"none\">\n"
            + "    <CubeGrant cube=\"Sales\" access=\"custom\">\n"
            + "      <DimensionGrant dimension=\"[Measures]\" access=\"all\" />\n"
            + "      <DimensionGrant dimension=\"[Education Level]\" access=\"all\" />\n"
            + "      <DimensionGrant dimension=\"[Gender]\" access=\"all\" />\n"
            + "    </CubeGrant>\n"
            + "  </SchemaGrant>\n"
            + "</Role>\n"
            + "<Role name=\"Role2\">\n"
            + "  <SchemaGrant access=\"none\">\n"
            + "    <CubeGrant cube=\"Sales\" access=\"custom\">\n"
            + "      <DimensionGrant dimension=\"[Measures]\" access=\"all\" />\n"
            + "      <DimensionGrant dimension=\"[Education Level]\" access=\"all\" />\n"
            + "      <DimensionGrant dimension=\"[Customers]\" access=\"none\" />\n"
            + "    </CubeGrant>\n"
            + "  </SchemaGrant>\n"
            + "</Role>\n"
            + "<Role name=\"Role3\">\n"
            + "  <SchemaGrant access=\"none\">\n"
            + "    <CubeGrant cube=\"Sales\" access=\"custom\">\n"
            + "      <DimensionGrant dimension=\"[Education Level]\" access=\"all\" />\n"
            + "      <DimensionGrant dimension=\"[Measures]\" access=\"custom\" />\n"
            + "    </CubeGrant>\n"
            + "  </SchemaGrant>\n"
            + "</Role>\n");

        */

        public AccessControlTestModifier19(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Role1")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("custom")
                                    .dimensionGrants(List.of(
                                        DimensionGrantRBuilder.builder()
                                            .dimension("[Measures]")
                                            .access(AccessEnum.ALL)
                                            .build(),
                                        DimensionGrantRBuilder.builder()
                                            .dimension("[Education Level]")
                                            .access(AccessEnum.ALL)
                                            .build(),
                                        DimensionGrantRBuilder.builder()
                                            .dimension("[Gender]")
                                            .access(AccessEnum.ALL)
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            result.add(
                RoleRBuilder.builder()
                    .name("Role2")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("custom")
                                    .dimensionGrants(List.of(
                                        DimensionGrantRBuilder.builder()
                                            .dimension("[Measures]")
                                            .access(AccessEnum.ALL)
                                            .build(),
                                        DimensionGrantRBuilder.builder()
                                            .dimension("[Education Level]")
                                            .access(AccessEnum.ALL)
                                            .build(),
                                        DimensionGrantRBuilder.builder()
                                            .dimension("[Gender]")
                                            .access(AccessEnum.NONE)
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            result.add(
                RoleRBuilder.builder()
                    .name("Role3")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("custom")
                                    .dimensionGrants(List.of(
                                        DimensionGrantRBuilder.builder()
                                            .dimension("[Education Level]")
                                            .access(AccessEnum.ALL)
                                            .build(),
                                        DimensionGrantRBuilder.builder()
                                            .dimension("[Measures]")
                                            .access(AccessEnum.CUSTOM)
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            return result;
        }
    }

    public static class AccessControlTestModifier20 extends RDbMappingSchemaModifier {

        /*
                    "  <Role name=\"Role1\">\n"
                    + "    <SchemaGrant access=\"all\">\n"
                    + "      <CubeGrant cube=\"Sales\" access=\"all\">\n"
                    + "        <HierarchyGrant hierarchy=\"[Customers]\" access=\"custom\" topLevel=\"[Customers].[City]\" bottomLevel=\"[Customers].[City]\" rollupPolicy=\"partial\">\n"
                    + "          <MemberGrant member=\"[City].[Coronado]\" access=\"all\">\n"
                    + "          </MemberGrant>\n"
                    + "        </HierarchyGrant>\n"
                    + "      </CubeGrant>\n"
                    + "    </SchemaGrant>\n"
                    + "  </Role>\n"
                    + "  <Role name=\"Role2\">\n"
                    + "    <SchemaGrant access=\"all\">\n"
                    + "      <CubeGrant cube=\"Sales\" access=\"all\">\n"
                    + "        <HierarchyGrant hierarchy=\"[Customers]\" access=\"custom\" topLevel=\"[Customers].[City]\" bottomLevel=\"[Customers].[City]\" rollupPolicy=\"partial\">\n"
                    + "          <MemberGrant member=\"[City].[Burbank]\" access=\"all\">\n"
                    + "          </MemberGrant>\n"
                    + "        </HierarchyGrant>\n"
                    + "      </CubeGrant>\n"
                    + "    </SchemaGrant>\n"
                    + "  </Role>\n");

        */

        public AccessControlTestModifier20(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Role1")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.ALL)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Customers]")
                                            .access(AccessEnum.CUSTOM)
                                            .topLevel("[Customers].[City]")
                                            .bottomLevel("[Customers].[City]")
                                            .rollupPolicy("partial")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[City].[Coronado]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            result.add(
                RoleRBuilder.builder()
                    .name("Role2")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.ALL)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Customers]")
                                            .access(AccessEnum.CUSTOM)
                                            .topLevel("[Customers].[City]")
                                            .bottomLevel("[Customers].[City]")
                                            .rollupPolicy("partial")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[City].[Burbank]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            return result;
        }
    }

    public static class AccessControlTestModifier21 extends RDbMappingSchemaModifier {

        /*
            "<Role name=\"Bacon\">\n"
            + "  <SchemaGrant access=\"none\">\n"
            + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
            + "      <HierarchyGrant hierarchy=\"[Customers]\" access=\"custom\" rollupPolicy=\"partial\">\n"
            + "        <MemberGrant member=\"[Customers].[USA].[CA].[Los Angeles]\" access=\"all\"/>\n"
            + "      </HierarchyGrant>\n"
            + "    </CubeGrant>\n"
            + "  </SchemaGrant>\n"
            + "</Role>");

        */

        public AccessControlTestModifier21(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Bacon")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Customers]")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("partial")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA].[CA].[Los Angeles]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );
            return result;
        }
    }

    public static class AccessControlTestModifier22 extends RDbMappingSchemaModifier {

        /*
            "<Role name=\"Role1\">\n"
            + "  <SchemaGrant access=\"none\">\n"
            + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
            + "      <HierarchyGrant hierarchy=\"[Store]\" access=\"custom\" rollupPolicy=\"partial\">\n"
            + "        <MemberGrant member=\"[Store].[USA].[CA]\" access=\"all\"/>\n"
            + "      </HierarchyGrant>\n"
            + "    </CubeGrant>\n"
            + "  </SchemaGrant>\n"
            + "</Role>");
        */

        public AccessControlTestModifier22(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Role1")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store]")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("partial")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[CA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );
            return result;
        }
    }

    public static class AccessControlTestModifier23 extends RDbMappingSchemaModifier {

        /*
            "<Role name=\"Role1\">\n"
            + "  <SchemaGrant access=\"none\">\n"
            + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
            + "      <HierarchyGrant hierarchy=\"[Store]\" access=\"custom\" rollupPolicy=\"partial\">\n"
            + "        <MemberGrant member=\"[Store].[USA].[CA]\" access=\"all\"/>\n"
            + "      </HierarchyGrant>\n"
            + "    </CubeGrant>\n"
            + "  </SchemaGrant>\n"
            + "</Role>"
            + "<Role name=\"Role2\">\n"
            + "  <SchemaGrant access=\"none\">\n"
            + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
            + "      <HierarchyGrant hierarchy=\"[Store]\" access=\"custom\" rollupPolicy=\"partial\">\n"
            + "        <MemberGrant member=\"[Store].[USA].[OR]\" access=\"all\"/>\n"
            + "      </HierarchyGrant>\n"
            + "    </CubeGrant>\n"
            + "  </SchemaGrant>\n"
            + "</Role>");
        */

        public AccessControlTestModifier23(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Role1")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store]")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("partial")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[CA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            result.add(
                RoleRBuilder.builder()
                    .name("Role2")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store]")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("partial")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[OR]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            return result;
        }
    }


    public static class AccessControlTestModifier24 extends RDbMappingSchemaModifier {

        /*
                "<Role name=\"Admin\">\n"
                + "  <SchemaGrant access=\"none\">\n"
                + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
                + "      <HierarchyGrant hierarchy=\"[Store]\" rollupPolicy=\"partial\" access=\"custom\">\n"
                + "        <MemberGrant member=\"[Store].[USA].[CA]\" access=\"all\">\n"
                + "        </MemberGrant>\n"
                + "      </HierarchyGrant>\n"
                + "      <HierarchyGrant hierarchy=\"[Customers]\" rollupPolicy=\"partial\" access=\"custom\">\n"
                + "        <MemberGrant member=\"[Customers].[USA].[CA]\" access=\"all\">\n"
                + "        </MemberGrant>\n"
                + "      </HierarchyGrant>\n"
                + "    </CubeGrant>\n"
                + "  </SchemaGrant>\n"
                + "</Role> \n");
        */

        public AccessControlTestModifier24(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Admin")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store]")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("partial")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[CA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build(),
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Customers]")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("partial")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[CA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            return result;
        }
    }

    public static class AccessControlTestModifier25 extends RDbMappingSchemaModifier {

        /*
            "<Role name=\"test\">\n"
            + " <SchemaGrant access=\"none\">\n"
            + "   <CubeGrant cube=\"Sales\" access=\"all\">\n"
            + "     <HierarchyGrant hierarchy=\"[Store]\" access=\"custom\"\n"
            + "         topLevel=\"[Store].[Store Country]\" rollupPolicy=\"partial\">\n"
            + "       <MemberGrant member=\"[Store].[All Stores]\" access=\"none\"/>\n"
            + "       <MemberGrant member=\"[Store].[USA].[CA].[Los Angeles]\" access=\"all\"/>\n"
            + "       <MemberGrant member=\"[Store].[USA].[CA].[Alameda]\" access=\"all\"/>\n"
            + "       <MemberGrant member=\"[Store].[USA].[CA].[Beverly Hills]\"\n"
            + "access=\"all\"/>\n"
            + "       <MemberGrant member=\"[Store].[USA].[CA].[San Francisco]\"\n"
            + "access=\"all\"/>\n"
            + "       <MemberGrant member=\"[Store].[USA].[CA].[San Diego]\" access=\"all\"/>\n"
            + "\n"
            + "       <MemberGrant member=\"[Store].[USA].[OR].[Portland]\" access=\"all\"/>\n"
            + "       <MemberGrant member=\"[Store].[USA].[OR].[Salem]\" access=\"all\"/>\n"
            + "     </HierarchyGrant>\n"
            + "   </CubeGrant>\n"
            + " </SchemaGrant>\n"
            + "</Role>");
        */

        public AccessControlTestModifier25(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("test")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store]")
                                            .access(AccessEnum.CUSTOM)
                                            .topLevel("[Store].[Store Country]")
                                            .rollupPolicy("partial")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[All Stores]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[CA].[Los Angeles]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[CA].[Alameda]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[CA].[Beverly Hills]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[CA].[San Francisco]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[CA].[San Diego]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[OR].[Portland]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[OR].[Salem]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            return result;
        }
    }

    public static class AccessControlTestModifier26 extends RDbMappingSchemaModifier {

        /*
            "<Role name=\"dev\">"
            + "    <SchemaGrant access=\"all\">"
            + "      <CubeGrant cube=\"Sales\" access=\"all\">"
            + "      </CubeGrant>"
            + "      <CubeGrant cube=\"HR\" access=\"all\">"
            + "      </CubeGrant>"
            + "      <CubeGrant cube=\"Warehouse and Sales\" access=\"all\">"
            + "         <HierarchyGrant hierarchy=\"Measures\" access=\"custom\">"
            + "            <MemberGrant member=\"[Measures].[Warehouse Sales]\" access=\"all\">"
            + "            </MemberGrant>"
            + "         </HierarchyGrant>"
            + "     </CubeGrant>"
            + "  </SchemaGrant>"
            + "</Role>";
        */

        public AccessControlTestModifier26(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("dev")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.ALL)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .build(),
                                CubeGrantRBuilder.builder()
                                    .cube("HR")
                                    .access("all")
                                    .build(),
                                CubeGrantRBuilder.builder()
                                    .cube("Warehouse and Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("Measures")
                                            .access(AccessEnum.CUSTOM)
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Measures].[Warehouse Sales]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            return result;
        }
    }

    public static class AccessControlTestModifier27 extends RDbMappingSchemaModifier {

        /*
            "<Role name=\"dev\">"
            + "    <SchemaGrant access=\"all\">"
            + "      <CubeGrant cube=\"Sales\" access=\"all\">"
            + "         <HierarchyGrant hierarchy=\"Measures\" access=\"custom\">"
            + "            <MemberGrant member=\"[Measures].[Unit Sales]\" access=\"all\">"
            + "            </MemberGrant>"
            + "         </HierarchyGrant>"
            + "      </CubeGrant>"
            + "      <CubeGrant cube=\"HR\" access=\"all\">"
            + "      </CubeGrant>"
            + "      <CubeGrant cube=\"Warehouse and Sales\" access=\"all\">"
            + "     </CubeGrant>"
            + "  </SchemaGrant>"
            + "</Role>";
        */

        public AccessControlTestModifier27(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("dev")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.ALL)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("Measures")
                                            .access(AccessEnum.CUSTOM)
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Measures].[Unit Sales]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build(),
                                CubeGrantRBuilder.builder()
                                    .cube("HR")
                                    .access("all")
                                    .build(),
                                CubeGrantRBuilder.builder()
                                    .cube("Warehouse and Sales")
                                    .access("all")
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            return result;
        }
    }

    public static class AccessControlTestModifier28 extends RDbMappingSchemaModifier {

        /*
                "<Role name=\"Admin\">\n"
                + "    <SchemaGrant access=\"none\">\n"
                + "      <CubeGrant cube=\"Sales\" access=\"all\">\n"
                + "        <HierarchyGrant hierarchy=\"[Gender]\" rollupPolicy=\"partial\" access=\"custom\">\n"
                + "          <MemberGrant member=\"[Gender].[F]\" access=\"all\">\n"
                + "          </MemberGrant>\n"
                + "        </HierarchyGrant>\n"
                + "      </CubeGrant>\n"
                + "    </SchemaGrant>\n"
                + "  </Role>\n");
        */

        public AccessControlTestModifier28(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Admin")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Gender]")
                                            .rollupPolicy("partial")
                                            .access(AccessEnum.CUSTOM)
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Gender].[F]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            return result;
        }
    }

    public static class AccessControlTestModifier39 extends RDbMappingSchemaModifier {

        /*
                "<Role name=\"Role1\">\n"
                + "  <SchemaGrant access=\"none\">\n"
                + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
                + "      <HierarchyGrant hierarchy=\"[Customers]\" access=\"custom\" rollupPolicy=\""
                    + rollupPolicy
                    + "\" bottomLevel=\"[Customers].[City]\">\n"
                    + "        <MemberGrant member=\"[Customers].[USA]\" access=\"all\"/>\n"
                    + "        <MemberGrant member=\"[Customers].[USA].[CA]\" access=\"all\"/>\n"
                    + "        <MemberGrant member=\"[Customers].[USA].[CA].[Los Angeles]\" access=\"none\"/>\n"
                    + "      </HierarchyGrant>\n"
                    + "    </CubeGrant>\n"
                    + "  </SchemaGrant>\n"
                    + "</Role>");

        */

        private final RollupPolicy policy;

        public AccessControlTestModifier39(MappingSchema mappingSchema, RollupPolicy policy) {
            super(mappingSchema);
            this.policy = policy;
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Role1")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Customers]")
                                            .rollupPolicy(policy.name())
                                            .access(AccessEnum.CUSTOM)
                                            .bottomLevel("[Customers].[City]")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA].[CA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA].[CA].[Los Angeles]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            return result;
        }
    }

    public static class AccessControlTestModifier40 extends RDbMappingSchemaModifier {

        /*
                "<Role name=\"Role1\">\n"
                + "  <SchemaGrant access=\"none\">\n"
                + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
                + "      <HierarchyGrant hierarchy=\"[Customers]\" access=\"custom\" rollupPolicy=\""
                + policy
                + "\">\n"
                + "        <MemberGrant member=\"[Customers].[USA]\" access=\"all\"/>\n"
                + "        <MemberGrant member=\"[Customers].[USA].[CA].[San Francisco].[Gladys Evans]\" access=\"none\"/>\n"
                + "      </HierarchyGrant>\n"
                + "    </CubeGrant>\n"
                + "  </SchemaGrant>\n"
                + "</Role>");
        */

        private final RollupPolicy policy;

        public AccessControlTestModifier40(MappingSchema mappingSchema, RollupPolicy policy) {
            super(mappingSchema);
            this.policy = policy;
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Role1")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Customers]")
                                            .rollupPolicy(policy.name())
                                            .access(AccessEnum.CUSTOM)
                                            .bottomLevel("[Customers].[City]")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA].[CA].[San Francisco].[Gladys Evans]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            return result;
        }
    }

    public static class AccessControlTestModifier41 extends RDbMappingSchemaModifier {

        /*
                "<Role name=\"Role1\">\n"
                + "  <SchemaGrant access=\"none\">\n"
                + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
                + "      <HierarchyGrant hierarchy=\"[Customers]\" access=\"custom\" rollupPolicy=\""
                + policy
                + "\">\n"
                + "        <MemberGrant member=\"[Customers].[USA]\" access=\"all\"/>\n"
                + "        <MemberGrant member=\"[Customers].[USA].[CA].[San Francisco].[Gladys Evans]\" access=\"none\"/>\n"
                + "      </HierarchyGrant>\n"
                + "      <HierarchyGrant hierarchy=\"[Store]\" access=\"custom\" rollupPolicy=\""
                + policy
                + "\">\n"
                + "        <MemberGrant member=\"[Store].[USA].[CA]\" access=\"all\"/>\n"
                + "        <MemberGrant member=\"[Store].[USA].[CA].[San Francisco].[Store 14]\" access=\"none\"/>\n"
                + "      </HierarchyGrant>\n"
                + "    </CubeGrant>\n"
                + "  </SchemaGrant>\n"
                + "</Role>");
        */

        private final RollupPolicy policy;

        public AccessControlTestModifier41(MappingSchema mappingSchema, RollupPolicy policy) {
            super(mappingSchema);
            this.policy = policy;
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Role1")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Customers]")
                                            .rollupPolicy(policy.name())
                                            .access(AccessEnum.CUSTOM)
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA].[CA].[San Francisco].[Gladys Evans]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build()
                                            ))
                                            .build(),
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store]")
                                            .rollupPolicy(policy.name())
                                            .access(AccessEnum.CUSTOM)
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Customers].[USA].[CA].[San Francisco].[Gladys Evans]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            return result;
        }
    }

    public static class AccessControlTestModifier42 extends RDbMappingSchemaModifier {

        /*
                "<Role name=\"California manager\">\n"
                + "  <SchemaGrant access=\"none\">\n"
                + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
                + "      <HierarchyGrant hierarchy=\"[Store]\" rollupPolicy=\""
                + policy.name().toLowerCase()
                + "\" access=\"custom\">\n"
                + "        <MemberGrant member=\"[Store].[USA].[CA]\" access=\"all\"/>\n"
                + "        <MemberGrant member=\"[Store].[USA].[OR].[Portland]\" access=\"all\"/>\n"
                + "      </HierarchyGrant>"
                + "    </CubeGrant>\n"
                + "  </SchemaGrant>\n"
                + "</Role>");
        */

        private final RollupPolicy policy;

        public AccessControlTestModifier42(MappingSchema mappingSchema, RollupPolicy policy) {
            super(mappingSchema);
            this.policy = policy;
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("California manager")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store]")
                                            .rollupPolicy(policy.name().toLowerCase())
                                            .access(AccessEnum.CUSTOM)
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[CA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[OR].[Portland]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            return result;
        }
    }

    public static class AccessControlTestModifier29 extends RDbMappingSchemaModifier {

        /*
                "<Role name=\"Admin\">\n"
                + "    <SchemaGrant access=\"none\">\n"
                + "      <CubeGrant cube=\"Sales\" access=\"all\">\n"
                + "        <HierarchyGrant hierarchy=\"[Gender]\" rollupPolicy=\"partial\" access=\"custom\">\n"
                + "          <MemberGrant member=\"[Gender].[F]\" access=\"all\">\n"
                + "          </MemberGrant>\n"
                + "        </HierarchyGrant>\n"
                + "      </CubeGrant>\n"
                + "    </SchemaGrant>\n"
                + "  </Role>\n");
        */
        private final Boolean hasAll;
        private final String defaultMem;
        private final RollupPolicy policy;

        public AccessControlTestModifier29(MappingSchema mappingSchema,
                                           Boolean hasAll, String defaultMem, RollupPolicy policy) {
            super(mappingSchema);
            this.hasAll = hasAll;
            this.defaultMem = defaultMem;
            this.policy = policy;
        }

        /*
            "<Cube name=\"TinySales\">\n"
            + "  <Table name=\"sales_fact_1997\"/>\n"
            + "  <DimensionUsage name=\"Product\" source=\"Product\" foreignKey=\"product_id\"/>\n"
            + "  <DimensionUsage name=\"Store2\" source=\"Store2\" foreignKey=\"store_id\"/>\n"
            + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"/>\n"
            + "</Cube>";
         */
        protected List<MappingCube> schemaCubes(MappingSchema mappingSchemaOriginal) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(mappingSchemaOriginal));
            result.add(CubeRBuilder.builder()
                .name("TinySales")
                .fact(new TableR("sales_fact_1997"))
                .dimensionUsageOrDimensions(List.of(
                    DimensionUsageRBuilder.builder()
                        .name("Product")
                        .source("Product")
                        .foreignKey("product_id")
                        .build(),
                    DimensionUsageRBuilder.builder()
                        .name("Store2")
                        .source("Store2")
                        .foreignKey("store_id")
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Unit Sales")
                        .column("unit_sales")
                        .aggregator("sum")
                        .build()
                ))
                .build());
            return result;
        }

        protected List<MappingPrivateDimension> schemaDimensions(MappingSchema mappingSchemaOriginal) {
            List<MappingPrivateDimension> result = new ArrayList<>();
            result.addAll(super.schemaDimensions(mappingSchemaOriginal));
            result.add(PrivateDimensionRBuilder.builder()
                .name("Store2")
                .hierarchies(List.of(
                    HierarchyRBuilder.builder()
                        .hasAll(hasAll)
                        .primaryKeyTable("store_id")
                        .defaultMember(defaultMem)
                        .relation(new TableR("store"))
                        .levels(List.of(
                            LevelRBuilder.builder()
                                .name("Store Country")
                                .column("store_country")
                                .uniqueMembers(true)
                                .build(),
                            LevelRBuilder.builder()
                                .name("Store State")
                                .column("store_state")
                                .uniqueMembers(true)
                                .build()
                        ))
                        .build()
                ))
                .build());
            return result;
        }

        /*
                    "<Role name=\"test\">\n"
            + "        <SchemaGrant access=\"none\">\n"
            + "            <CubeGrant cube=\"TinySales\" access=\"all\">\n"
            + "                <HierarchyGrant hierarchy=\"[Store2]\" access=\"custom\"\n"
            + "                                 rollupPolicy=\"%s\">\n"
            + "                    <MemberGrant member=\"[Store2].[USA].[CA]\" access=\"all\"/>\n"
            + "                    <MemberGrant member=\"[Store2].[USA].[OR]\" access=\"all\"/>\n"
            + "                    <MemberGrant member=\"[Store2].[Canada]\" access=\"all\"/>\n"
            + "                </HierarchyGrant>\n"
            + "            </CubeGrant>\n"
            + "        </SchemaGrant>\n"
            + "    </Role> ";

         */
        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("test")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("TinySales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store2]")
                                            .rollupPolicy(policy.name())
                                            .access(AccessEnum.CUSTOM)
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store2].[USA].[CA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store2].[USA].[OR]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store2].[Canada]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            return result;
        }
    }

    public static class AccessControlTestModifier30 extends RDbMappingSchemaModifier {

        /*
            "<Role name=\"noBaseCubes\">\n"
            + " <SchemaGrant access=\"all\">\n"
            + "  <CubeGrant cube=\"Sales\" access=\"none\" />\n"
            + "  <CubeGrant cube=\"Sales Ragged\" access=\"none\" />\n"
            + "  <CubeGrant cube=\"Sales 2\" access=\"none\" />\n"
            + "  <CubeGrant cube=\"Warehouse\" access=\"none\" />\n"
            + " </SchemaGrant>\n"
            + "</Role> ";
        */

        public AccessControlTestModifier30(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("noBaseCubes")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.ALL)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("none")
                                    .build(),
                                CubeGrantRBuilder.builder()
                                    .cube("Sales Ragged")
                                    .access("none")
                                    .build(),
                                CubeGrantRBuilder.builder()
                                    .cube("Sales 2")
                                    .access("none")
                                    .build(),
                                CubeGrantRBuilder.builder()
                                    .cube("Warehouse")
                                    .access("none")
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            return result;
        }
    }

    public static class AccessControlTestModifier31 extends RDbMappingSchemaModifier {

        /*
            "<Role name=\"Role1\">\n"
            + "  <SchemaGrant access=\"all\">\n"
            + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
            + "      <HierarchyGrant hierarchy=\"[Measures]\" access=\"all\">\n"
            + "      </HierarchyGrant>\n"
            + "    </CubeGrant>\n"
            + "  </SchemaGrant>\n"
            + "</Role>"
            + "<Role name=\"Role2\">\n"
            + "  <SchemaGrant access=\"all\">\n"
            + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
            + "      <HierarchyGrant hierarchy=\"[Measures]\" access=\"custom\">\n"
            + "        <MemberGrant member=\"[Measures].[Unit Sales]\" access=\"all\"/>\n"
            + "      </HierarchyGrant>\n"
            + "    </CubeGrant>\n"
            + "  </SchemaGrant>\n"
            + "</Role>");
        */

        public AccessControlTestModifier31(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Role1")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.ALL)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Measures]")
                                            .access(AccessEnum.ALL)
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            result.add(
                RoleRBuilder.builder()
                    .name("Role2")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.ALL)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Measures]")
                                            .access(AccessEnum.CUSTOM)
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Measures].[Unit Sales]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            return result;
        }
    }

    public static class AccessControlTestModifier33 extends RDbMappingSchemaModifier {


        public AccessControlTestModifier33(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        /*
                String cubeDef = "<Cube name=\"Sales1\">"
          + "  <Table name=\"sales_fact_1997\"/>\n"
            "    <Dimension visible=\"true\" foreignKey=\"customer_id\" highCardinality=\"false\" name=\"Customers\">\n"
            + "      <Hierarchy visible=\"true\" hasAll=\"true\" allMemberName=\"All Customers\" primaryKey=\"customer_id\">\n"
            + "        <Table name=\"customer\">\n"
            + "        </Table>\n"
            + "        <Level name=\"Country\" visible=\"true\" column=\"country\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\">\n"
            + "        </Level>\n"
            + "        <Level name=\"State Province\" visible=\"true\" column=\"state_province\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\">\n"
            + "        </Level>\n"
            + "        <Level name=\"City\" visible=\"true\" column=\"city\" type=\"String\" uniqueMembers=\"false\" levelType=\"Regular\" hideMemberIf=\"Never\">\n"
            + "        </Level>\n"
            + "        <Level name=\"Name1\" visible=\"true\" column=\"fname\" type=\"String\" uniqueMembers=\"false\" levelType=\"Regular\" hideMemberIf=\"Never\">\n"
            + "          <Property name=\"Gender\" column=\"gender\" type=\"String\">\n"
            + "          </Property>\n"
            + "          <Property name=\"Marital Status\" column=\"marital_status\" type=\"String\">\n"
            + "          </Property>\n"
            + "          <Property name=\"Education\" column=\"education\" type=\"String\">\n"
            + "          </Property>\n"
            + "          <Property name=\"Yearly Income\" column=\"yearly_income\" type=\"String\">\n"
            + "          </Property>\n"
            + "        </Level>\n"
            + "        <Level name=\"First Name\" visible=\"true\" column=\"fname\" type=\"String\" uniqueMembers=\"false\" levelType=\"Regular\" hideMemberIf=\"Never\">\n"
            + "        </Level>\n"
            + "      </Hierarchy>\n"
            + "      <Hierarchy name=\"Gender\" visible=\"true\" hasAll=\"true\" primaryKey=\"customer_id\">\n"
            + "        <Table name=\"customer\">\n"
            + "        </Table>\n"
            + "        <Level name=\"Gender\" visible=\"true\" column=\"gender\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\">\n"
            + "          <Annotations>\n"
            + "            <Annotation name=\"AnalyzerBusinessGroup\">\n"
            + "              <![CDATA[Customers]]>\n"
            + "            </Annotation>\n"
            + "          </Annotations>\n"
            + "        </Level>\n"
            + "      </Hierarchy>\n"
            + "      <Hierarchy name=\"Marital Status\" visible=\"true\" hasAll=\"true\" primaryKey=\"customer_id\">\n"
            + "        <Table name=\"customer\">\n"
            + "        </Table>\n"
            + "        <Level name=\"Marital Status\" visible=\"true\" column=\"marital_status\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\">\n"
            + "          <Annotations>\n"
            + "            <Annotation name=\"AnalyzerBusinessGroup\">\n"
            + "              <![CDATA[Customers]]>\n"
            + "            </Annotation>\n"
            + "          </Annotations>\n"
            + "        </Level>\n"
            + "      </Hierarchy>\n"
            + "    </Dimension>\n"
            + "  <Dimension visible=\"true\" highCardinality=\"false\" name=\"Store\" foreignKey=\"store_id\">\n"
            + "    <Hierarchy visible=\"true\" hasAll=\"true\" primaryKey=\"store_id\">\n"
            + "      <Table name=\"store\">\n"
            + "      </Table>\n"
            + "      <Level name=\"Store ID\" visible=\"true\" column=\"store_id\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\">\n"
            + "      </Level>\n"
            + "      <Level name=\"Store Country\" visible=\"true\" column=\"store_country\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\">\n"
            + "      </Level>\n"
            + "      <Level name=\"Store State\" visible=\"true\" column=\"store_state\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\">\n"
            + "      </Level>\n"
            + "      <Level name=\"Store City\" visible=\"true\" column=\"store_city\" type=\"String\" uniqueMembers=\"false\" levelType=\"Regular\" hideMemberIf=\"Never\">\n"
            + "      </Level>\n"
            + "      <Level name=\"Store Name\" visible=\"true\" column=\"store_name\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\">\n"
            + "        <Property name=\"Store Type\" column=\"store_type\" type=\"String\">\n"
            + "        </Property>\n"
            + "        <Property name=\"Store Manager\" column=\"store_manager\" type=\"String\">\n"
            + "        </Property>\n"
            + "        <Property name=\"Store Sqft\" column=\"store_sqft\" type=\"Numeric\">\n"
            + "        </Property>\n"
            + "        <Property name=\"Grocery Sqft\" column=\"grocery_sqft\" type=\"Numeric\">\n"
            + "        </Property>\n"
            + "        <Property name=\"Frozen Sqft\" column=\"frozen_sqft\" type=\"Numeric\">\n"
            + "        </Property>\n"
            + "        <Property name=\"Meat Sqft\" column=\"meat_sqft\" type=\"Numeric\">\n"
            + "        </Property>\n"
            + "        <Property name=\"Has coffee bar\" column=\"coffee_bar\" type=\"Boolean\">\n"
            + "        </Property>\n"
            + "        <Property name=\"Street address\" column=\"store_street_address\" type=\"String\">\n"
            + "        </Property>\n"
            + "      </Level>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n";

          + "</Cube>";

         */
        protected List<MappingCube> schemaCubes(MappingSchema mappingSchemaOriginal) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(mappingSchemaOriginal));
            result.add(CubeRBuilder.builder()
                .name("Sales1")
                .dimensionUsageOrDimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .visible(true)
                        .foreignKey("customer_id")
                        .highCardinality(false)
                        .name("Customers")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .visible(true)
                                .hasAll(true)
                                .allMemberName("All Customers")
                                .primaryKey("customer_id")
                                .relation(new TableR("customer"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Country")
                                        .visible(true)
                                        .column("country")
                                        .type(TypeEnum.STRING)
                                        .uniqueMembers(true)
                                        .levelType(LevelTypeEnum.REGULAR)
                                        .hideMemberIf(HideMemberIfEnum.NEVER)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("State Province")
                                        .visible(true)
                                        .column("state_province")
                                        .type(TypeEnum.STRING)
                                        .uniqueMembers(true)
                                        .levelType(LevelTypeEnum.REGULAR)
                                        .hideMemberIf(HideMemberIfEnum.NEVER)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("City")
                                        .visible(true)
                                        .column("city")
                                        .type(TypeEnum.STRING)
                                        .uniqueMembers(false)
                                        .levelType(LevelTypeEnum.REGULAR)
                                        .hideMemberIf(HideMemberIfEnum.NEVER)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Name1")
                                        .visible(true)
                                        .column("fname")
                                        .type(TypeEnum.STRING)
                                        .uniqueMembers(false)
                                        .levelType(LevelTypeEnum.REGULAR)
                                        .hideMemberIf(HideMemberIfEnum.NEVER)
                                        .properties(List.of(
                                            PropertyRBuilder.builder()
                                                .name("Gender")
                                                .column("gender")
                                                .type(PropertyTypeEnum.STRING)
                                                .build(),
                                            PropertyRBuilder.builder()
                                                .name("Marital Status")
                                                .column("marital_status")
                                                .type(PropertyTypeEnum.STRING)
                                                .build(),
                                            PropertyRBuilder.builder()
                                                .name("Education")
                                                .column("education")
                                                .type(PropertyTypeEnum.STRING)
                                                .build(),
                                            PropertyRBuilder.builder()
                                                .name("Yearly Income")
                                                .column("yearly_income")
                                                .type(PropertyTypeEnum.STRING)
                                                .build()
                                        ))
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("First Name")
                                        .visible(true)
                                        .column("fname")
                                        .type(TypeEnum.STRING)
                                        .uniqueMembers(false)
                                        .levelType(LevelTypeEnum.REGULAR)
                                        .hideMemberIf(HideMemberIfEnum.NEVER)
                                        .build()
                                ))
                                .build(),
                            HierarchyRBuilder.builder()
                                .name("Gender")
                                .visible(true)
                                .hasAll(true)
                                .primaryKey("customer_id")
                                .relation(new TableR("customer"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Gender")
                                        .visible(true)
                                        .column("gender")
                                        .type(TypeEnum.STRING)
                                        .uniqueMembers(true)
                                        .levelType(LevelTypeEnum.REGULAR)
                                        .hideMemberIf(HideMemberIfEnum.NEVER)
                                        .annotations(List.of(
                                            AnnotationRBuilder.builder()
                                                .name("AnalyzerBusinessGroup")
                                                .content("Customers")
                                                .build()
                                        ))
                                        .build()

                                ))
                                .build(),
                            HierarchyRBuilder.builder()
                                .name("Marital Status")
                                .visible(true)
                                .hasAll(true)
                                .primaryKey("customer_id")
                                .relation(new TableR("customer"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Marital Status")
                                        .visible(true)
                                        .column("marital_status")
                                        .type(TypeEnum.STRING)
                                        .uniqueMembers(true)
                                        .levelType(LevelTypeEnum.REGULAR)
                                        .hideMemberIf(HideMemberIfEnum.NEVER)
                                        .annotations(List.of(
                                            AnnotationRBuilder.builder()
                                                .name("AnalyzerBusinessGroup")
                                                .content("Customers")
                                                .build()
                                        ))
                                        .build()

                                ))
                                .build()
                        ))
                        .build()
                )).build());
            return result;
        }


        /*
            "<Role name=\"MR\">\n"
            + "  <SchemaGrant access=\"none\">\n"
            + "    <CubeGrant cube=\"Sales1\" access=\"all\">\n"
            + "      <HierarchyGrant hierarchy=\"[Store]\" access=\"all\">\n"
            + "      </HierarchyGrant>\n"
            + "      <HierarchyGrant hierarchy=\"[Customers]\" access=\"custom\" topLevel=\"[Customers].[State Province]\" bottomLevel=\"[Customers].[City]\">\n"
            + "\t  </HierarchyGrant>\n"
            + "    </CubeGrant>\n"
            + "  </SchemaGrant>\n"
            + "</Role>\n"
            + "<Role name=\"DBPentUsers\">\n"
            + "   <SchemaGrant access=\"none\">\n"
            + "   </SchemaGrant>\n"
            + "</Role>");
        */
        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("MR")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales1")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store]")
                                            .access(AccessEnum.ALL)
                                            .build(),
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Customers]")
                                            .access(AccessEnum.CUSTOM)
                                            .topLevel("[Customers].[State Province]")
                                            .bottomLevel("[Customers].[City]")
                                            .build()

                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            result.add(
                RoleRBuilder.builder()
                    .name("DBPentUsers")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .build()
                    ))
                    .build()
            );

            return result;
        }
    }

    public static class AccessControlTestModifier34 extends RDbMappingSchemaModifier {

        /*
            "<Role name=\"Role1\">\n"
            + "  <SchemaGrant access=\"none\">\n"
            + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
            + "      <HierarchyGrant hierarchy=\"[Store]\" access=\"custom\" rollupPolicy=\"partial\">\n"
            + "        <MemberGrant member=\"[Store].[USA].[Non Existent]\" access=\"all\"/>\n"
            + "      </HierarchyGrant>\n"
            + "    </CubeGrant>\n"
            + "  </SchemaGrant>\n"
            + "</Role>");
        */

        public AccessControlTestModifier34(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Role1")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store]")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("partial")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[Non Existent]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            return result;
        }
    }

    public static class AccessControlTestModifier35 extends RDbMappingSchemaModifier {

        /*
            "<Role name=\"Role1\">\n"
            + "  <SchemaGrant access=\"none\">\n"
            + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
            + "      <HierarchyGrant hierarchy=\"[Store]\" access=\"custom\" rollupPolicy=\"partial\">\n"
            + "        <MemberGrant member=\"[Store].[USA].[WA]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Store].[USA].[OR]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Store].[USA].[CA].[San Francisco]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Store].[USA].[CA].[Los Angeles]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Store].[Mexico]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Store].[Mexico].[DF]\" access=\"none\"/>\n"
            + "        <MemberGrant member=\"[Store].[Canada]\" access=\"none\"/>\n"
            + "      </HierarchyGrant>\n"
            + "    </CubeGrant>\n"
            + "  </SchemaGrant>\n"
            + "</Role>\n"
            + "<Role name=\"Role2\">\n"
            + "  <SchemaGrant access=\"none\">\n"
            + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
            + "      <HierarchyGrant hierarchy=\"[Store]\" access=\"custom\" rollupPolicy=\"full\">\n"
            + "        <MemberGrant member=\"[Store].[USA].[WA]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Store].[USA].[OR]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Store].[USA].[CA].[San Francisco]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Store].[USA].[CA].[Los Angeles]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Store].[Mexico]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Store].[Mexico].[DF]\" access=\"none\"/>\n"
            + "        <MemberGrant member=\"[Store].[Canada]\" access=\"none\"/>\n"
            + "      </HierarchyGrant>\n"
            + "    </CubeGrant>\n"
            + "  </SchemaGrant>\n"
            + "</Role>";

        */

        public AccessControlTestModifier35(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Role1")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store]")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("full")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[WA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[OR]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[CA].[San Francisco]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[CA].[Los Angeles]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[Mexico]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[Mexico].[DF]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[Canada]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            result.add(
                RoleRBuilder.builder()
                    .name("Role2")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store]")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("full")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[WA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[OR]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[CA].[San Francisco]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[CA].[Los Angeles]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[Mexico]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[Mexico].[DF]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[Canada]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            return result;
        }
    }

    public static class AccessControlTestModifier36 extends RDbMappingSchemaModifier {

        /*
            "<Role name=\"Role1\">\n"
            + "  <SchemaGrant access=\"none\">\n"
            + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
            + "      <HierarchyGrant hierarchy=\"[Store]\" access=\"custom\" rollupPolicy=\"partial\">\n"
            + "        <MemberGrant member=\"[Store].[USA].[WA]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Store].[USA].[OR]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Store].[USA].[CA].[San Francisco]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Store].[USA].[CA].[Los Angeles]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Store].[Mexico]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Store].[Mexico].[DF]\" access=\"none\"/>\n"
            + "        <MemberGrant member=\"[Store].[Canada]\" access=\"none\"/>\n"
            + "      </HierarchyGrant>\n"
            + "    </CubeGrant>\n"
            + "  </SchemaGrant>\n"
            + "</Role>\n"
            + "<Role name=\"Role2\">\n"
            + "  <SchemaGrant access=\"none\">\n"
            + "    <CubeGrant cube=\"Sales\" access=\"all\">\n"
            + "      <HierarchyGrant hierarchy=\"[Store]\" access=\"custom\" rollupPolicy=\"partial\">\n"
            + "        <MemberGrant member=\"[Store].[USA].[WA]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Store].[USA].[OR]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Store].[USA].[CA].[San Francisco]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Store].[Mexico]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Store].[Mexico].[DF]\" access=\"none\"/>\n"
            + "        <MemberGrant member=\"[Store].[Canada]\" access=\"none\"/>\n"
            + "      </HierarchyGrant>\n"
            + "    </CubeGrant>\n"
            + "  </SchemaGrant>\n"
            + "</Role>";

        */

        public AccessControlTestModifier36(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Role1")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store]")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("partial")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[WA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[OR]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[CA].[San Francisco]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[CA].[Los Angeles]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[Mexico]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[Mexico].[DF]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[Canada]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );

            result.add(
                RoleRBuilder.builder()
                    .name("Role2")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("all")
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store]")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("partial")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[WA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[OR]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[CA].[San Francisco]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[Mexico]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[Mexico].[DF]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[Canada]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );
            return result;
        }
    }

    public static class AccessControlTestModifier37 extends RDbMappingSchemaModifier {

        /*
            "<Role name=\"Role1\">\n"
            + "  <SchemaGrant access=\"none\">\n"
            + "    <CubeGrant cube=\"Sales\" access=\"custom\">\n"
            + "      <DimensionGrant dimension=\"[Measures]\" access=\"all\"/>\n"
            + "      <DimensionGrant dimension=\"[Gender]\" access=\"all\"/>\n"
            + "      <HierarchyGrant hierarchy=\"[Store]\" access=\"custom\" rollupPolicy=\"partial\">\n"
            + "        <MemberGrant member=\"[Store].[USA]\" access=\"all\"/>\n"
            + "        <MemberGrant member=\"[Store].[USA].[CA]\" access=\"none\"/>\n"
            + "      </HierarchyGrant>\n"
            + "    </CubeGrant>\n"
            + "  </SchemaGrant>\n"
            + "</Role>");
        */

        public AccessControlTestModifier37(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Role1")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales")
                                    .access("custom")
                                    .dimensionGrants(List.of(
                                        DimensionGrantRBuilder.builder()
                                            .dimension("[Measures]")
                                            .access(AccessEnum.ALL)
                                            .build(),
                                        DimensionGrantRBuilder.builder()
                                            .dimension("[Gender]")
                                            .access(AccessEnum.ALL)
                                            .build()
                                    ))
                                    .hierarchyGrants(List.of(
                                        HierarchyGrantRBuilder.builder()
                                            .hierarchy("[Store]")
                                            .access(AccessEnum.CUSTOM)
                                            .rollupPolicy("partial")
                                            .memberGrants(List.of(
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA]")
                                                    .access(MemberGrantAccessEnum.ALL)
                                                    .build(),
                                                MemberGrantRBuilder.builder()
                                                    .member("[Store].[USA].[CA]")
                                                    .access(MemberGrantAccessEnum.NONE)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );
            return result;
        }
    }

    public static class AccessControlTestModifier32 extends RDbMappingSchemaModifier {

        /*
      String schema =
          "<Schema name=\"FoodMart.DimAndMeasure.Role\">\n"
          + " <Dimension name=\"WarehouseShared\">\n"
          + "   <Hierarchy hasAll=\"true\" primaryKey=\"warehouse_id\">\n"
          + "     <Table name=\"warehouse\"/>\n"
          + "     <Level name=\"Country\" column=\"warehouse_country\" uniqueMembers=\"true\"/>\n"
          + "     <Level name=\"State Province\" column=\"warehouse_state_province\"\n"
          + "          uniqueMembers=\"true\"/>\n"
          + "     <Level name=\"City\" column=\"warehouse_city\" uniqueMembers=\"false\"/>\n"
          + "     <Level name=\"Warehouse Name\" column=\"warehouse_name\" uniqueMembers=\"true\"/>\n"
          + "   </Hierarchy>\n"
          + " </Dimension>\n"
          + " <Cube name=\"Warehouse1\">\n"
          + "   <Table name=\"inventory_fact_1997\"/>\n"
          + "   <DimensionUsage name=\"WarehouseShared\" source=\"WarehouseShared\" foreignKey=\"warehouse_id\"/>\n"
          + "   <Measure name=\"Measure1_0\" column=\"warehouse_cost\" aggregator=\"sum\"/>\n"
          + "   <Measure name=\"Measure1_1\" column=\"warehouse_sales\" aggregator=\"sum\"/>\n"
          + "   <CalculatedMember name=\"Calculated Measure1\" dimension=\"Measures\">\n"
          + "     <Formula>[Measures].[Measure1_1] / [Measures].[Measure1_0]</Formula>\n"
          + "     <CalculatedMemberProperty name=\"FORMAT_STRING\" value=\"$#,##0.00\"/>\n"
          + "   </CalculatedMember>\n"
          + " </Cube>\n"
          + " <Cube name=\"Warehouse2\">\n"
          + "   <Table name=\"inventory_fact_1997\"/>\n"
          + "   <DimensionUsage name=\"WarehouseShared\" source=\"WarehouseShared\" foreignKey=\"warehouse_id\"/>\n"
          + "   <Measure name=\"Measure2_0\" column=\"warehouse_cost\" aggregator=\"sum\"/>\n"
          + "   <Measure name=\"Measure2_1\" column=\"warehouse_sales\" aggregator=\"sum\"/>\n"
          + "   <CalculatedMember name=\"Calculated Measure2\" dimension=\"Measures\">\n"
          + "     <Formula>[Measures].[Measure2_1] / [Measures].[Measure2_0]</Formula>\n"
          + "     <CalculatedMemberProperty name=\"FORMAT_STRING\" value=\"$#,##0.00\"/>\n"
          + "   </CalculatedMember>\n"
          + " </Cube>\n"
          + " <Role name=\"Administrator\">\n"
          + "   <SchemaGrant access=\"none\">\n"
          + "     <CubeGrant cube=\"Warehouse1\" access=\"custom\">\n"
          + "       <HierarchyGrant hierarchy=\"[WarehouseShared]\" access=\"all\">\n"
          + "       </HierarchyGrant>\n"
          + "       <HierarchyGrant hierarchy=\"[Measures]\" access=\"all\">\n"
          + "       </HierarchyGrant>\n"
          + "     </CubeGrant>\n"
          + "     <CubeGrant cube=\"Warehouse2\" access=\"custom\">\n"
          + "       <HierarchyGrant hierarchy=\"[WarehouseShared]\" access=\"all\">\n"
          + "       </HierarchyGrant>\n"
          + "       <HierarchyGrant hierarchy=\"[Measures]\" access=\"all\">\n"
          + "       </HierarchyGrant>\n"
          + "     </CubeGrant>\n"
          + "   </SchemaGrant>\n"
          + " </Role>\n"
          + "</Schema>";

        */

        public AccessControlTestModifier32(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("FoodMart.DimAndMeasure.Role")
                .dimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("WarehouseShared")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("warehouse_id")
                                .relation(new TableR("warehouse"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("State Province")
                                        .column("warehouse_state_province")
                                        .uniqueMembers(true)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("City")
                                        .column("warehouse_city")
                                        .uniqueMembers(false)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Warehouse Name")
                                        .column("warehouse_name")
                                        .uniqueMembers(true)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Warehouse1")
                        .fact(new TableR("inventory_fact_1997"))
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .name("WarehouseShared")
                                .source("WarehouseShared")
                                .foreignKey("warehouse_id")
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Measure1_0")
                                .column("warehouse_cost")
                                .aggregator("sum")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Measure1_1")
                                .column("warehouse_sales")
                                .aggregator("sum")
                                .build()
                        ))
                        .calculatedMembers(List.of(
                            CalculatedMemberRBuilder.builder()
                                .name("Calculated Measure1")
                                .dimension("Measures")
                                .formulaElement(FormulaRBuilder.builder()
                                    .cdata("[Measures].[Measure1_1] / [Measures].[Measure1_0]")
                                    .build())
                                .calculatedMemberProperties(List.of(
                                    CalculatedMemberPropertyRBuilder.builder()
                                        .name("FORMAT_STRING")
                                        .value("$#,##0.00")
                                        .build()
                                ))
                                .build()
                        ))
                        .build(),
                    CubeRBuilder.builder()
                        .name("Warehouse2")
                        .fact(new TableR("inventory_fact_1997"))
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .name("WarehouseShared")
                                .source("WarehouseShared")
                                .foreignKey("warehouse_id")
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Measure2_0")
                                .column("warehouse_cost")
                                .aggregator("sum")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Measure2_1")
                                .column("warehouse_sales")
                                .aggregator("sum")
                                .build()
                        ))
                        .calculatedMembers(List.of(
                            CalculatedMemberRBuilder.builder()
                                .name("Calculated Measure2")
                                .dimension("Measures")
                                .formulaElement(FormulaRBuilder.builder()
                                    .cdata("[Measures].[Measure2_1] / [Measures].[Measure2_0]")
                                    .build())
                                .calculatedMemberProperties(List.of(
                                    CalculatedMemberPropertyRBuilder.builder()
                                        .name("FORMAT_STRING")
                                        .value("$#,##0.00")
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                    ))
                .roles(List.of(
                    RoleRBuilder.builder()
                        .name("Administrator")
                        .schemaGrants(List.of(
                            SchemaGrantRBuilder.builder()
                                .access(AccessEnum.NONE)
                                .cubeGrants(List.of(
                                    CubeGrantRBuilder.builder()
                                        .cube("Warehouse1")
                                        .access("custom")
                                        .hierarchyGrants(List.of(
                                            HierarchyGrantRBuilder.builder()
                                                .hierarchy("[WarehouseShared]")
                                                .access(AccessEnum.ALL)
                                                .build(),
                                            HierarchyGrantRBuilder.builder()
                                                .hierarchy("[Measures]")
                                                .access(AccessEnum.ALL)
                                                .build()
                                        ))
                                        .build(),
                                    CubeGrantRBuilder.builder()
                                        .cube("Warehouse2")
                                        .access("custom")
                                        .hierarchyGrants(List.of(
                                            HierarchyGrantRBuilder.builder()
                                                .hierarchy("[WarehouseShared]")
                                                .access(AccessEnum.ALL)
                                                .build(),
                                            HierarchyGrantRBuilder.builder()
                                                .hierarchy("[Measures]")
                                                .access(AccessEnum.ALL)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .build();
        }
    }

    public static class AccessControlTestModifier38 extends RDbMappingSchemaModifier {

        /*
          "<Role name=\"Sales Ragged\">\n"
          + "  <SchemaGrant access=\"none\">\n"
          + "    <CubeGrant cube=\"Sales Ragged\" access=\"all\" />\n"
          + "  </SchemaGrant>\n"
          + "</Role>";
        */

        public AccessControlTestModifier38(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        @Override
        protected List<MappingRole> schemaRoles(MappingSchema mappingSchemaOriginal) {
            List<MappingRole> result = new ArrayList<>();
            result.addAll(super.schemaRoles(mappingSchemaOriginal));
            result.add(
                RoleRBuilder.builder()
                    .name("Sales Ragged")
                    .schemaGrants(List.of(
                        SchemaGrantRBuilder.builder()
                            .access(AccessEnum.NONE)
                            .cubeGrants(List.of(
                                CubeGrantRBuilder.builder()
                                    .cube("Sales Ragged")
                                    .access("all")
                                    .build()
                            ))
                            .build()
                    ))
                    .build()
            );
            return result;
        }
    }

    public static class OrderKeyOneToOneCheckTestModifier extends RDbMappingSchemaModifier {

        /*
                    + "<Schema name=\"FoodMart 2358\">\n"
                    + "  <Dimension name=\"Time\" type=\"TimeDimension\">\n"
                    + "    <Hierarchy hasAll=\"false\" primaryKey=\"time_id\">\n"
                    + "      <Table name=\"time_by_day\"/>\n"
                    + "      <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
                    + "          levelType=\"TimeYears\"/> \n"
                    + "      <Level name=\"Quarter\" column=\"quarter\" ordinalColumn=\"month_of_year\" uniqueMembers=\"false\" levelType=\"TimeQuarters\"/>  \n"
                    + "      <Level name=\"Month\" column=\"month_of_year\" uniqueMembers=\"false\" type=\"Numeric\"\n"
                    + "          levelType=\"TimeMonths\"/>\n"
                    + "    </Hierarchy>\n"
                    + "  </Dimension>\n"
                    + "<Cube name=\"Sales\" defaultMeasure=\"Unit Sales\">\n"
                    + "  <Table name=\"sales_fact_1997\"/>\n"
                    + "  <DimensionUsage name=\"Time\" source=\"Time\" foreignKey=\"time_id\"/>\n"
                    + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"\n"
                    + "      formatString=\"Standard\"/>\n"
                    + "</Cube>\n"
                    + "</Schema>");
        */

        public OrderKeyOneToOneCheckTestModifier(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("FoodMart 2358")
                .dimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Time")
                        .type(DimensionTypeEnum.TIME_DIMENSION)
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("time_id")
                                .relation(new TableR("time_by_day"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Year")
                                        .column("the_year")
                                        .type(TypeEnum.NUMERIC)
                                        .uniqueMembers(true)
                                        .levelType(LevelTypeEnum.TIME_YEARS)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Quarter")
                                        .column("quarter")
                                        .ordinalColumn("month_of_year")
                                        .uniqueMembers(false)
                                        .levelType(LevelTypeEnum.TIME_QUARTERS)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Month")
                                        .column("month_of_year")
                                        .uniqueMembers(false)
                                        .type(TypeEnum.NUMERIC)
                                        .levelType(LevelTypeEnum.TIME_MONTHS)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Sales")
                        .fact(new TableR("sales_fact_1997"))
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .name("Time")
                                .source("Time")
                                .foreignKey("time_id")
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales")
                                .column("unit_sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build()
                        ))
                        .build()
                ))
                .build();
        }
    }

    public static class DefaultRecognizerTestModifier extends RDbMappingSchemaModifier {

        /*
                    + "<Schema name=\"FoodMart\">\n"
        "<Dimension name=\"Time\" type=\"TimeDimension\"> "
        + "  <Hierarchy hasAll=\"false\" primaryKey=\"time_id\"> "
        + "    <Table name=\"time_by_day\"/> "
        + "    <Level name=\"Year\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\" levelType=\"TimeYears\"/> "
        + "    <Level name=\"Quarter\" column=\"quarter\" uniqueMembers=\"false\" levelType=\"TimeQuarters\"/> "
        + "    <Level name=\"Month\" column=\"month_of_year\" uniqueMembers=\"false\" type=\"Numeric\" levelType=\"TimeMonths\"/> "
        + "  </Hierarchy> "
        + "</Dimension>";
                    "<Cube name=\"Sales\" defaultMeasure=\"Unit Sales\"> "
            // For this test, we use an alias on the fact table.
            + "  <Table name=\"sales_fact_1997\" alias=\"foobar\"> "
            + "      <AggExclude name=\"agg_c_special_sales_fact_1997\"/>"
            + "      <AggExclude name=\"agg_c_14_sales_fact_1997\"/>"
            + "  </Table>"
            + "  <DimensionUsage name=\"Time\" source=\"Time\" foreignKey=\"time_id\"/> "
            + "<Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"Standard\"/>\n"
            + "  <Measure name=\"Store Cost\" column=\"store_cost\" aggregator=\"sum\"\n"
            + "      formatString=\"#,###.00\"/>\n"
            + "  <Measure name=\"Store Sales\" column=\"store_sales\" aggregator=\"sum\"\n"
            + "      formatString=\"#,###.00\"/>"
            + "  <Measure name=\"Customer Count\" column=\"customer_id\" aggregator=\"distinct-count\" formatString=\"#,###\" />"
            + "</Cube>";

                    + "</Schema>");
        */

        public DefaultRecognizerTestModifier(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("FoodMart")
                .dimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Time")
                        .type(DimensionTypeEnum.TIME_DIMENSION)
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("time_id")
                                .relation(new TableR("time_by_day"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Year")
                                        .column("the_year")
                                        .type(TypeEnum.NUMERIC)
                                        .uniqueMembers(true)
                                        .levelType(LevelTypeEnum.TIME_YEARS)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Quarter")
                                        .column("quarter")
                                        .uniqueMembers(false)
                                        .levelType(LevelTypeEnum.TIME_QUARTERS)
                                        .build(),
                                    LevelRBuilder.builder()
                                        .name("Month")
                                        .column("month_of_year")
                                        .uniqueMembers(false)
                                        .type(TypeEnum.NUMERIC)
                                        .levelType(LevelTypeEnum.TIME_MONTHS)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Sales")
                        .defaultMeasure("Unit Sales")
                        .fact(new TableR("sales_fact_1997",
                            List.of(
                                AggExcludeRBuilder.builder().name("agg_c_special_sales_fact_1997").build(),
                                AggExcludeRBuilder.builder().name("agg_c_14_sales_fact_1997").build()
                            ),
                            List.of())
                        )
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .name("Time")
                                .source("Time")
                                .foreignKey("time_id")
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales")
                                .column("unit_sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Store Cost")
                                .column("store_cost")
                                .aggregator("sum")
                                .formatString("#,###.00")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Store Sales")
                                .column("store_sales")
                                .aggregator("sum")
                                .formatString("#,###.00")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Customer Count")
                                .column("customer_id")
                                .aggregator("distinct-count")
                                .formatString("#,###")
                                .build()
                        ))
                        .build()
                ))
                .build();
        }
    }

    public static class DialectTestModifier1 extends RDbMappingSchemaModifier {

        /*
            "<?xml version=\"1.0\"?>\n"
                    + "<Schema name=\"FoodMart\">\n"
                    + "  <Dimension  name=\"Time\" type=\"TimeDimension\">\n"
                    + "    <Hierarchy hasAll='true' primaryKey=\"time_id\">\n"
                    + "      <Table name=\"time_by_day\"/>\n"
                    + "      <Level name=\"Day\"  type=\"Date\" uniqueMembers=\"true\"\n"
                    + "          levelType=\"TimeYears\">\n"
                    + "        <KeyExpression>\n"
                    + "          <SQL>\n"
                    + "            cast(\"the_date\" as DATE)\n"
                    + "          </SQL>\n"
                    + "        </KeyExpression>\n"
                    + "      </Level>\n"
                    + "    </Hierarchy>\n"
                    + "  </Dimension>\n"
                    + "  <Cube name=\"DateLiteralTest\" defaultMeasure=\"expression\">\n"
                    + "    <Table name=\"sales_fact_1997\" />\n"
                    + "    <DimensionUsage name=\"Time\" source=\"Time\" foreignKey=\"time_id\"/>\n"
                    + "    <Measure name=\"Unit Sales\" column=\"unit_sales\"  aggregator=\"sum\"\n"
                    + "    formatString=\"Standard\" />\n"
                    + "  </Cube>\n"
                    + "</Schema>\n" );
        */

        public DialectTestModifier1(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("FoodMart")
                .dimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Time")
                        .type(DimensionTypeEnum.TIME_DIMENSION)
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("time_id")
                                .relation(new TableR("time_by_day"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Day")
                                        .type(TypeEnum.DATE)
                                        .column("the_year")
                                        .uniqueMembers(true)
                                        .levelType(LevelTypeEnum.TIME_YEARS)
                                        .keyExpression(ExpressionViewRBuilder.builder()
                                            .sqls(List.of(
                                                SQLRBuilder.builder()
                                                    .content("cast(\"the_date\" as DATE)\n")
                                                    .build()
                                            ))
                                            .build())
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("DateLiteralTest")
                        .defaultMeasure("expression")
                        .fact(new TableR("sales_fact_1997"))
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .name("Time")
                                .source("Time")
                                .foreignKey("time_id")
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales")
                                .column("unit_sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build()
                        ))
                        .build()
                ))
                .build();
        }
    }

    public static class DialectTestModifier2 extends RDbMappingSchemaModifier {

        /*
            "<?xml version=\"1.0\"?>\n"
                    + "<Schema name=\"FoodMart\">\n"
                    + "  <Dimension name=\"StoreSqft\">\n"
                    + "    <Hierarchy hasAll=\"true\" primaryKey=\"store_id\">\n"
                    + "      <Table name=\"store\"  />\n"
                    + "      <Level name=\"StoreSqft\"  type=\"Numeric\" uniqueMembers=\"true\">\n"
                    + "         <KeyExpression>"
                    + "          <SQL dialect='mysql'>\n"
                    + "            cast(`store_sqft` as UNSIGNED INTEGER) + "
                    + Integer.MAX_VALUE
                    + "          </SQL>\n"
                    + "          <SQL dialect='vertica'>\n"
                    + "            cast(\"store_sqft\" as BIGINT) + "
                    + Integer.MAX_VALUE
                    + "          </SQL>\n"
                    + "          <SQL dialect='oracle'>\n"
                    + "            CAST(\"store_sqft\" + 2147483647 AS NUMBER(22))  "
                    + "          </SQL>\n"
                    + "         </KeyExpression>"
                    + "      </Level>"
                    + "    </Hierarchy>\n"
                    + "  </Dimension>"
                    + "  <Cube name=\"BigIntTest\" defaultMeasure=\"expression\">\n"
                    + "    <Table name=\"sales_fact_1997\" />\n"
                    + "    <DimensionUsage name=\"StoreSqft\" source=\"StoreSqft\" foreignKey=\"store_id\"/>\n"
                    + "    <Measure name=\"Big Unit Sales\"   aggregator=\"sum\"\n"
                    + "    formatString=\"Standard\" >\n"
                    + "           <MeasureExpression>\n"
                    + "      <SQL dialect=\"vertica\">\n"
                    + "   CAST(\"unit_sales\" + 2147483647 AS NUMBER(22)) \n"
                    + "      </SQL>\n"
                    + "      </MeasureExpression>\n"
                    + "      </Measure>\n"
                    + "  <Measure name=\"Pass Agg enabled\" column=\"store_cost\" aggregator=\"sum\"/>\n"
                    + "  </Cube>\n"
                    + "</Schema>\n" );
        */

        public DialectTestModifier2(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("FoodMart")
                .dimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("StoreSqft")
                        .type(DimensionTypeEnum.TIME_DIMENSION)
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .primaryKey("store_id")
                                .relation(new TableR("store"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("StoreSqft")
                                        .type(TypeEnum.NUMERIC)
                                        .uniqueMembers(true)
                                        .keyExpression(ExpressionViewRBuilder.builder()
                                            .sqls(List.of(
                                                SQLRBuilder.builder()
                                                    .dialect("mysql")
                                                    .content("cast(`store_sqft` as UNSIGNED INTEGER) + " + Integer.MAX_VALUE)
                                                    .build(),
                                                SQLRBuilder.builder()
                                                    .dialect("vertica")
                                                    .content("cast(\"store_sqft\" as BIGINT) + " + Integer.MAX_VALUE)
                                                    .build(),
                                                SQLRBuilder.builder()
                                                    .dialect("oracle")
                                                    .content("CAST(\"store_sqft\" + 2147483647 AS NUMBER(22))")
                                                    .build()
                                            ))
                                            .build())
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("BigIntTest")
                        .defaultMeasure("expression")
                        .fact(new TableR("sales_fact_1997"))
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .name("StoreSqft")
                                .source("StoreSqft")
                                .foreignKey("store_id")
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Big Unit Sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .measureExpression(ExpressionViewRBuilder.builder()
                                    .sqls(List.of(
                                        SQLRBuilder.builder()
                                            .dialect("vertica")
                                            .content("CAST(\"unit_sales\" + 2147483647 AS NUMBER(22))")
                                            .build()
                                    ))
                                    .build())
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Pass Agg enabled")
                                .column("store_cost")
                                .aggregator("sum")
                                .build()
                        ))
                        .build()
                ))
                .build();
        }
    }

    public static class DrillThroughTestModifier4 extends RDbMappingSchemaModifier {

        /*
            "<Schema name=\"dsad\">\n"
            + "  <Dimension name=\"Frozen sqft\">\n"
            + "    <Hierarchy hasAll=\"true\">\n"
            + "      <Table name=\"store\"/>\n"
            + "      <Level name=\"Frozen sqft\" uniqueMembers=\"false\" column=\"frozen_sqft\" type=\"Numeric\">\n"
            + "      </Level>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "  <Dimension name=\"Grocery sqft\">\n"
            + "    <Hierarchy hasAll=\"true\">\n"
            + "      <Table name=\"store\"/>\n"
            + "      <Level name=\"Grocery sqft\" uniqueMembers=\"false\" column=\"grocery_sqft\" type=\"Numeric\">\n"
            + "      </Level>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "  <Dimension name=\"Meat sqft\">\n"
            + "    <Hierarchy hasAll=\"true\">\n"
            + "      <Table name=\"store\"/>\n"
            + "      <Level name=\"Meat sqft\" uniqueMembers=\"false\" column=\"meat_sqft\" type=\"Numeric\">\n"
            + "      </Level>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "  <Dimension name=\"Store sqft\">\n"
            + "    <Hierarchy hasAll=\"true\">\n"
            + "      <Table name=\"store\"/>\n"
            + "      <Level name=\"Store sqft\" uniqueMembers=\"false\" column=\"store_sqft\" type=\"Numeric\">\n"
            + "      </Level>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "  <Cube name=\"dsad\">\n"
            + "    <Table name=\"store\"/>\n"
            + "    <DimensionUsage name=\"Frozen sqft\" source=\"Frozen sqft\"/>\n"
            + "    <DimensionUsage name=\"Grocery sqft\" source=\"Grocery sqft\"/>\n"
            + "    <DimensionUsage name=\"Meat sqft\" source=\"Meat sqft\"/>\n"
            + "    <DimensionUsage name=\"Store sqft\" source=\"Store sqft\"/>\n"
            + "    <Measure name=\"Frozen sqft\" column=\"frozen_sqft\" aggregator=\"sum\"/>\n"
            + "    <Measure name=\"Grocery sqft\" column=\"grocery_sqft\" aggregator=\"sum\"/>\n"
            + "    <Measure name=\"Meat sqft\" column=\"meat_sqft\" aggregator=\"sum\"/>\n"
            + "    <Measure name=\"Store sqft\" column=\"store_sqft\" aggregator=\"sum\"/>\n"
            + "  </Cube>\n"
            + "</Schema>\n");
        */

        public DrillThroughTestModifier4(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("dsad")
                .dimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Frozen sqft")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .relation(new TableR("store"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Frozen sqft")
                                        .uniqueMembers(false)
                                        .column("frozen_sqft")
                                        .type(TypeEnum.NUMERIC)
                                        .build()
                                ))
                                .build()
                        ))
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .name("Grocery sqft")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .relation(new TableR("store"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Grocery sqft")
                                        .uniqueMembers(false)
                                        .column("grocery_sqft")
                                        .type(TypeEnum.NUMERIC)
                                        .build()
                                ))
                                .build()
                        ))
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .name("Meat sqft")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .relation(new TableR("store"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Meat sqft")
                                        .uniqueMembers(false)
                                        .column("meat_sqft")
                                        .type(TypeEnum.NUMERIC)
                                        .build()
                                ))
                                .build()
                        ))
                        .build(),
                    PrivateDimensionRBuilder.builder()
                        .name("Store sqft")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .relation(new TableR("store"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Store sqft")
                                        .uniqueMembers(false)
                                        .column("store_sqft")
                                        .type(TypeEnum.NUMERIC)
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("dsad")
                        .fact(new TableR("store"))
                        .dimensionUsageOrDimensions(List.of(
                            DimensionUsageRBuilder.builder()
                                .name("Frozen sqft")
                                .source("Frozen sqft")
                                .build(),
                            DimensionUsageRBuilder.builder()
                                .name("Grocery sqft")
                                .source("Grocery sqft")
                                .build(),
                            DimensionUsageRBuilder.builder()
                                .name("Meat sqft")
                                .source("Meat sqft")
                                .build(),
                            DimensionUsageRBuilder.builder()
                                .name("Store sqft")
                                .source("Store sqft")
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Frozen sqft")
                                .column("frozen_sqft")
                                .aggregator("sum")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Grocery sqft")
                                .column("grocery_sqft")
                                .aggregator("sum")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Meat sqft")
                                .column("meat_sqft")
                                .aggregator("sum")
                                .build(),
                            MeasureRBuilder.builder()
                                .name("Store sqft")
                                .column("store_sqft")
                                .aggregator("sum")
                                .build()
                        ))
                        .build()
                ))
                .build();
        }
    }

    public static class DrillThroughTestModifier5 extends RDbMappingSchemaModifier {

        /*
        "<Schema name=\"FoodMartSalesOnly\">\n"
        + " <Cube name=\"SalesShort\">\n"
        + "   <Table name=\"sales_fact_1997\"/>\n"
        + "   <Dimension name=\"Customers Dimension\" foreignKey=\"customer_id\">\n"
        + "     <Hierarchy hasAll=\"true\" allMemberName=\"All Customers hierarchy name\" primaryKey=\"customer_id\">\n"
        + "       <Table name=\"customer\"/>\n"
        + "       <Level name=\"Customer Level Name\" caption=\"Customer Level Caption\" description=\"Customer Level Description\" column=\"customer_id\" nameColumn=\"fullname\" type=\"String\" uniqueMembers=\"true\" />\n"
        + "     </Hierarchy>\n"
        + "   </Dimension>\n"
        + "   <Dimension name=\"Product Dimension\" foreignKey=\"product_id\">\n"
        + "     <Hierarchy hasAll=\"true\" allMemberName=\"All products hierarchy name\" primaryKey=\"product_id\">\n"
        + "       <Table name=\"product\"/>\n"
        + "       <Level name=\"Product Level Name\" caption=\"Product Level Caption\" description=\"Product Level Description\" column=\"product_id\" nameColumn=\"product_name\" type=\"String\" uniqueMembers=\"true\" />\n"
        + "     </Hierarchy>\n"
        + "   </Dimension>\n"
        + "   <Measure name=\"Store Sales\" column=\"store_sales\" aggregator=\"sum\" formatString=\"#,###.00\"/>\n"
        + " </Cube>\n"
        + "</Schema>\n";
        */

        public DrillThroughTestModifier5(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("FoodMartSalesOnly")
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("SalesShort")
                        .fact(new TableR("sales_fact_1997"))
                        .dimensionUsageOrDimensions(List.of(
                            PrivateDimensionRBuilder.builder()
                                .name("Customers Dimension")
                                .foreignKey("customer_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(true).allMemberName("All Customers hierarchy name").primaryKey("customer_id")
                                        .relation(new TableR("customer"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Customer Level Name")
                                                .caption("Customer Level Caption")
                                                .description("Customer Level Description")
                                                .column("customer_id")
                                                .nameColumn("fullname")
                                                .type(TypeEnum.STRING)
                                                .uniqueMembers(true)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build(),
                            PrivateDimensionRBuilder.builder()
                                .name("Product Dimension")
                                .foreignKey("product_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(true).allMemberName("All products hierarchy name").primaryKey("product_id")
                                        .relation(new TableR("product"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Product Level Name")
                                                .caption("Product Level Caption")
                                                .description("Product Level Description")
                                                .column("product_id")
                                                .nameColumn("product_name")
                                                .type(TypeEnum.STRING)
                                                .uniqueMembers(true)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Store Sales")
                                .column("store_sales")
                                .aggregator("sum")
                                .formatString("#,###.00")
                                .build()
                        ))
                        .build()
                ))
                .build();
        }
    }

    public static class DrillThroughTestModifier6 extends RDbMappingSchemaModifier {

        /*
        "<Schema name=\"FoodMartSalesOnly\">\n"
        + " <Cube name=\"SalesShort\">\n"
        + "   <Table name=\"sales_fact_1997\"/>\n"
        + "   <Dimension name=\"Customers Dimension\" foreignKey=\"customer_id\">\n"
        + "     <Hierarchy hasAll=\"true\" allMemberName=\"All Customers hierarchy name\" primaryKey=\"customer_id\">\n"
        + "       <Table name=\"customer\"/>\n"
        + "       <Level name=\"Customer Level Name\" caption=\"Customer Level Caption\" description=\"Customer Level Description\" column=\"customer_id\" type=\"String\" uniqueMembers=\"true\" />\n"
        + "     </Hierarchy>\n"
        + "   </Dimension>\n"
        + "   <Dimension name=\"Product Dimension\" foreignKey=\"product_id\">\n"
        + "     <Hierarchy hasAll=\"true\" allMemberName=\"All products hierarchy name\" primaryKey=\"product_id\">\n"
        + "       <Table name=\"product\"/>\n"
        + "       <Level name=\"Product Level Name\" caption=\"Product Level Caption\" description=\"Product Level Description\" column=\"product_id\" type=\"String\" uniqueMembers=\"true\" />\n"
        + "     </Hierarchy>\n"
        + "   </Dimension>\n"
        + "   <Measure name=\"Store Sales\" column=\"store_sales\" aggregator=\"sum\" formatString=\"#,###.00\"/>\n"
        + " </Cube>\n"
        + "</Schema>\n";
        */

        public DrillThroughTestModifier6(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("FoodMartSalesOnly")
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("SalesShort")
                        .fact(new TableR("sales_fact_1997"))
                        .dimensionUsageOrDimensions(List.of(
                            PrivateDimensionRBuilder.builder()
                                .name("Customers Dimension")
                                .foreignKey("customer_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(true).allMemberName("All Customers hierarchy name").primaryKey("customer_id")
                                        .relation(new TableR("customer"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Customer Level Name")
                                                .caption("Customer Level Caption")
                                                .description("Customer Level Description")
                                                .column("customer_id")
                                                .type(TypeEnum.STRING)
                                                .uniqueMembers(true)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build(),
                            PrivateDimensionRBuilder.builder()
                                .name("Product Dimension")
                                .foreignKey("product_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(true).allMemberName("All products hierarchy name").primaryKey("product_id")
                                        .relation(new TableR("product"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Product Level Name")
                                                .caption("Product Level Caption")
                                                .description("Product Level Description")
                                                .column("product_id")
                                                .type(TypeEnum.STRING)
                                                .uniqueMembers(true)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Store Sales")
                                .column("store_sales")
                                .aggregator("sum")
                                .formatString("#,###.00")
                                .build()
                        ))
                        .build()
                ))
                .build();
        }
    }


    public static class PropertiesTestModifier extends RDbMappingSchemaModifier {

        /*
            "<Cube name=\"Foo\" defaultMeasure=\"Unit Sales\">\n"
            + "  <Table name=\"sales_fact_1997\"/>\n"
            + "  <Dimension name=\"Promotions\" foreignKey=\"promotion_id\">\n"
            + "    <Hierarchy hasAll=\"true\" allMemberName=\"All Promotions\" primaryKey=\"promotion_id\" defaultMember=\"[All Promotions]\">\n"
            + "      <Table name=\"promotion\"/>\n"
            + "      <Level name=\"Promotion Name\" column=\"promotion_name\" uniqueMembers=\"true\">\n"
            + "   <Property name=\"BarProp\" column=\"promotion_name\" description=\"BaconDesc\"/>\n"
            + "   </Level>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\" formatString=\"Standard\"/>\n"
            + "</Cube>\n",
        */

        public PropertiesTestModifier(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        protected List<MappingCube> schemaCubes(MappingSchema mappingSchemaOriginal) {
            List<MappingCube> result = new ArrayList<>();
            result.addAll(super.schemaCubes(mappingSchemaOriginal));
            result.add(CubeRBuilder.builder()
                .name("Foo")
                .defaultMeasure("Unit Sales")
                .fact(new TableR("sales_fact_1997"))
                .dimensionUsageOrDimensions(List.of(
                    PrivateDimensionRBuilder.builder()
                        .name("Promotions")
                        .foreignKey("promotion_id")
                        .hierarchies(List.of(
                            HierarchyRBuilder.builder()
                                .hasAll(true)
                                .allMemberName("All Promotions")
                                .primaryKey("promotion_id")
                                .defaultMember("[All Promotions]")
                                .relation(new TableR("promotion"))
                                .levels(List.of(
                                    LevelRBuilder.builder()
                                        .name("Promotion Name")
                                        .column("promotion_name")
                                        .uniqueMembers(true)
                                        .properties(List.of(
                                            PropertyRBuilder.builder()
                                                .name("BarProp")
                                                .column("promotion_name")
                                                .description("BaconDesc")
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .build()
                ))
                .measures(List.of(
                    MeasureRBuilder.builder()
                        .name("Unit Sales")
                        .column("unit_sales")
                        .aggregator("sum")
                        .formatString("Standard")
                        .build()
                ))
                .build());
            return result;
        }

    }

    public static class Ssas2005CompatibilityTestModifier5 extends RDbMappingSchemaModifier {


        public Ssas2005CompatibilityTestModifier5(MappingSchema mappingSchema) {
            super(mappingSchema);
        }

        /*
            "<Schema name=\"FoodMart\">\n"
            + "<Cube name=\"Warehouse and Sales\" defaultMeasure=\"Unit Sales\">\n"
            + "  <Table name=\"sales_fact_1997\" />\n"
            + "  <Dimension name=\"Store\" foreignKey=\"store_id\">\n"
            + "    <Hierarchy name=\"Stores\" hasAll=\"true\" primaryKey=\"store_id\">\n"
            + "      <Table name=\"store\"/>\n"
            + "      <Level name=\"Store Country\" column=\"store_country\" uniqueMembers=\"true\"/>\n"
            + "      <Level name=\"Store State\" column=\"store_state\" uniqueMembers=\"true\"/>\n"
            + "      <Level name=\"Store City\" column=\"store_city\" uniqueMembers=\"false\"/>\n"
            + "      <Level name=\"Store Name\" column=\"store_name\" uniqueMembers=\"true\">\n"
            + "        <Property name=\"Store Type\" column=\"store_type\"/>\n"
            + "        <Property name=\"Store Sqft\" column=\"store_sqft\" type=\"Numeric\"/>\n"
            + "      </Level>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "  <Dimension name=\"Time\" type=\"TimeDimension\" foreignKey=\"time_id\">\n"
            + "    <Hierarchy hasAll=\"true\" name=\"Time By Week\" primaryKey=\"time_id\" >\n"
            + "      <Table name=\"time_by_day\"/>\n"
            + "      <Level name=\"Year2\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
            + "          levelType=\"TimeYears\"/>\n"
            + "      <Level name=\"Week\" column=\"week_of_year\" type=\"Numeric\" uniqueMembers=\"false\"\n"
            + "          levelType=\"TimeWeeks\"/>\n"
            + "      <Level name=\"Date2\" column=\"day_of_month\" uniqueMembers=\"false\" type=\"Numeric\"\n"
            + "          levelType=\"TimeDays\"/>\n"
            + "    </Hierarchy>\n"
            + "    <Hierarchy name=\"Time2\" hasAll=\"false\" primaryKey=\"time_id\">\n"
            + "      <Table name=\"time_by_day\"/>\n"
            + "      <Level name=\"Year2\" column=\"the_year\" type=\"Numeric\" uniqueMembers=\"true\"\n"
            + "          levelType=\"TimeYears\"/>\n"
            + "      <Level name=\"Quarter\" column=\"quarter\" uniqueMembers=\"false\"\n"
            + "          levelType=\"TimeQuarters\"/>\n"
            + "      <Level name=\"Month\" column=\"month_of_year\" nameColumn=\"the_month\" uniqueMembers=\"false\" type=\"Numeric\"\n"
            + "          levelType=\"TimeMonths\"/>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "  <Dimension name=\"Product\" foreignKey=\"product_id\">\n"
            + "    <Hierarchy name=\"Products\" hasAll=\"true\" primaryKey=\"product_id\" primaryKeyTable=\"product\">\n"
            + "      <Join leftKey=\"product_class_id\" rightKey=\"product_class_id\">\n"
            + "        <Table name=\"product\"/>\n"
            + "        <Table name=\"product_class\"/>\n"
            + "      </Join>\n"
            + "      <Level name=\"Product Family\" table=\"product_class\" column=\"product_family\"\n"
            + "          uniqueMembers=\"true\"/>\n"
            + "      <Level name=\"Product Department\" table=\"product_class\" column=\"product_department\"\n"
            + "          uniqueMembers=\"false\"/>\n"
            + "      <Level name=\"Product Category\" table=\"product_class\" column=\"product_category\"\n"
            + "          uniqueMembers=\"false\"/>\n"
            + "      <Level name=\"Product Subcategory\" table=\"product_class\" column=\"product_subcategory\"\n"
            + "          uniqueMembers=\"false\"/>\n"
            + "      <Level name=\"Brand Name\" table=\"product\" column=\"brand_name\" uniqueMembers=\"false\"/>\n"
            + "      <Level name=\"Product Name\" table=\"product\" nameColumn=\"product_name\" column=\"product_id\" \n"
            + "          uniqueMembers=\"true\"/>\n"
            + "    </Hierarchy>\n"
            + "    <Hierarchy name=\"Product Name\" hasAll=\"true\" primaryKey=\"product_id\" primaryKeyTable=\"product\">\n"
            + "      <Join leftKey=\"product_class_id\" rightKey=\"product_class_id\">\n"
            + "        <Table name=\"product\"/>\n"
            + "        <Table name=\"product_class\"/>\n"
            + "      </Join>\n"
            + "      <Level name=\"Product Name\" table=\"product\" column=\"product_name\"\n"
            + "          uniqueMembers=\"true\"/>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "  <Dimension name=\"Promotion\" foreignKey=\"promotion_id\">\n"
            + "    <Hierarchy hasAll=\"true\" allMemberName=\"All Promotions\" primaryKey=\"promotion_id\" defaultMember=\"[All Promotions]\">\n"
            + "      <Table name=\"promotion\"/>\n"
            + "      <Level name=\"Promotion Name\" column=\"promotion_name\" uniqueMembers=\"true\"/>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>\n"
            + "  <Dimension name=\"Currency\" foreignKey=\"promotion_id\">\n"
            + "    <Hierarchy hasAll=\"true\" primaryKey=\"promotion_id\">\n"
            + "      <Table name=\"promotion\"/>\n"
            + "      <Level name=\"Currency\" column=\"media_type\" uniqueMembers=\"true\"/>\n"
            + "    </Hierarchy>\n"
            + "  </Dimension>"
            + "  <Dimension name=\"Customer\" foreignKey=\"customer_id\">\n"
                + "    <Hierarchy hasAll=\"true\" allMemberName=\"All Customers\" primaryKey=\"customer_id\">\n"
                + "      <Table name=\"customer\"/>\n"
                + "      <Level name=\"Country\" column=\"country\" uniqueMembers=\"true\"/>\n"
                + "      <Level name=\"State Province\" column=\"state_province\" uniqueMembers=\"true\"/>\n"
                + "      <Level name=\"City\" column=\"city\" uniqueMembers=\"false\"/>\n"
                + "      <Level name=\"Name\" column=\"customer_id\" type=\"Numeric\" uniqueMembers=\"true\"/>\n"
                + "    </Hierarchy>\n"
                + "  </Dimension>\n"
                + "  <Dimension name='Store Size in SQFT' foreignKey='store_id'>\n"
                + "    <Hierarchy hasAll='true' primaryKey='store_id'>\n"
                + "      <Table name='store'/>\n"
                + "      <Level name='Store Sqft' column='store_sqft' type='Numeric' uniqueMembers='true'/>\n"
                + "    </Hierarchy>\n"
                + "  </Dimension>\n"
                + "  <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\"\n"
                + "      formatString=\"Standard\"/>\n"
                + "</Cube>\n"
                + "</Schema>");
         */
        @Override
        protected MappingSchema modifyMappingSchema(MappingSchema mappingSchemaOriginal) {
            return SchemaRBuilder.builder()
                .name("FoodMart")
                .cubes(List.of(
                    CubeRBuilder.builder()
                        .name("Warehouse and Sales")
                        .defaultMeasure("Unit Sales")
                        .fact(new TableR("sales_fact_1997"))
                        .dimensionUsageOrDimensions(List.of(
                            PrivateDimensionRBuilder.builder()
                                .name("Store")
                                .foreignKey("store_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .name("Stores")
                                        .hasAll(true).primaryKey("store_id")
                                        .relation(new TableR("store"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Store Country")
                                                .column("store_country")
                                                .uniqueMembers(true)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Store State")
                                                .column("store_state")
                                                .uniqueMembers(true)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Store City")
                                                .column("store_city")
                                                .uniqueMembers(false)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Store Name")
                                                .column("store_name")
                                                .uniqueMembers(true)
                                                .properties(List.of(
                                                    PropertyRBuilder.builder()
                                                        .name("Store Type")
                                                        .column("store_type")
                                                        .build(),
                                                    PropertyRBuilder.builder()
                                                        .name("Store Sqft")
                                                        .column("store_sqft")
                                                        .type(PropertyTypeEnum.NUMERIC)
                                                        .build()
                                                ))
                                                .build()

                                        ))
                                        .build()
                                ))
                                .build(),
                            PrivateDimensionRBuilder.builder()
                                .name("Time")
                                .type(DimensionTypeEnum.TIME_DIMENSION)
                                .foreignKey("time_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(true).name("Time By Week").primaryKey("time_id")
                                        .relation(new TableR("time_by_day"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Year2")
                                                .column("the_year")
                                                .type(TypeEnum.NUMERIC)
                                                .uniqueMembers(true)
                                                .levelType(LevelTypeEnum.TIME_YEARS)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Week")
                                                .column("week_of_year")
                                                .type(TypeEnum.NUMERIC)
                                                .uniqueMembers(false)
                                                .levelType(LevelTypeEnum.TIME_WEEKS)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Date2")
                                                .column("day_of_month")
                                                .type(TypeEnum.NUMERIC)
                                                .uniqueMembers(false)
                                                .levelType(LevelTypeEnum.TIME_DAYS)
                                                .build()
                                        ))
                                        .build(),
                                    HierarchyRBuilder.builder()
                                        .hasAll(false).name("Time2").primaryKey("time_id")
                                        .relation(new TableR("time_by_day"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Year2")
                                                .column("the_year")
                                                .type(TypeEnum.NUMERIC)
                                                .uniqueMembers(true)
                                                .levelType(LevelTypeEnum.TIME_YEARS)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Quarter")
                                                .column("quarter")
                                                .uniqueMembers(false)
                                                .levelType(LevelTypeEnum.TIME_QUARTERS)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Month")
                                                .column("month_of_year")
                                                .nameColumn("the_month")
                                                .type(TypeEnum.NUMERIC)
                                                .uniqueMembers(false)
                                                .levelType(LevelTypeEnum.TIME_MONTHS)
                                                .build()

                                        ))
                                        .build()

                                ))
                                .build(),
                            PrivateDimensionRBuilder.builder()
                                .name("Product")
                                .foreignKey("product_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(true).name("Products").primaryKey("product_id").primaryKeyTable("product")
                                        .relation(new JoinR(List.of(
                                            new TableR("product"), new TableR("product_class")),
                                            null, "product_class_id",
                                            null, "product_class_id"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Product Family")
                                                .table("product_class")
                                                .column("product_family")
                                                .uniqueMembers(true)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Product Department")
                                                .table("product_class")
                                                .column("product_department")
                                                .uniqueMembers(false)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Product Category")
                                                .table("product_class")
                                                .column("product_category")
                                                .uniqueMembers(false)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Product Subcategory")
                                                .table("product_class")
                                                .column("product_subcategory")
                                                .uniqueMembers(false)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Brand Name")
                                                .table("product")
                                                .column("brand_name")
                                                .uniqueMembers(false)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Product Name")
                                                .table("product")
                                                .nameColumn("product_name")
                                                .column("product_id")
                                                .uniqueMembers(true)
                                                .build()
                                        ))
                                        .build(),
                                    HierarchyRBuilder.builder()
                                        .name("Product Name")
                                        .hasAll(true)
                                        .primaryKey("product_id")
                                        .primaryKeyTable("product")
                                        .relation(new JoinR(List.of(
                                            new TableR("product"),
                                            new TableR("product_class")
                                        )
                                            , null, "product_class_id", null, "product_class_id"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Product Name")
                                                .table("product")
                                                .column("product_name")
                                                .uniqueMembers(true)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build(),
                                PrivateDimensionRBuilder.builder()
                                    .name("Promotion")
                                    .foreignKey("promotion_id")
                                    .hierarchies(List.of(
                                        HierarchyRBuilder.builder()
                                            .hasAll(true)
                                            .allMemberName("All Promotions")
                                            .primaryKey("promotion_id")
                                            .defaultMember("[All Promotions]")
                                            .relation(new TableR("promotion"))
                                            .levels(List.of(
                                                LevelRBuilder.builder()
                                                    .name("Promotion Name")
                                                    .column("promotion_name")
                                                    .uniqueMembers(true)
                                                    .build()
                                            ))
                                            .build()
                                    ))
                                    .build(),
                            PrivateDimensionRBuilder.builder()
                                .name("Currency")
                                .foreignKey("promotion_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(true)
                                        .primaryKey("promotion_id")
                                        .relation(new TableR("promotion"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Currency")
                                                .column("media_type")
                                                .uniqueMembers(true)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build(),
                            PrivateDimensionRBuilder.builder()
                                .name("Customer")
                                .foreignKey("Customer_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(true)
                                        .allMemberName("All Customers")
                                        .primaryKey("customer_id")
                                        .relation(new TableR("customer"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Country")
                                                .column("country")
                                                .uniqueMembers(true)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("State Province")
                                                .column("state_province")
                                                .uniqueMembers(true)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("City")
                                                .column("city")
                                                .uniqueMembers(false)
                                                .build(),
                                            LevelRBuilder.builder()
                                                .name("Name")
                                                .column("customer_id")
                                                .type(TypeEnum.NUMERIC)
                                                .uniqueMembers(true)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build(),
                            PrivateDimensionRBuilder.builder()
                                .name("Store Size in SQFT")
                                .foreignKey("store_id")
                                .hierarchies(List.of(
                                    HierarchyRBuilder.builder()
                                        .hasAll(true)
                                        .primaryKey("store_id")
                                        .relation(new TableR("store"))
                                        .levels(List.of(
                                            LevelRBuilder.builder()
                                                .name("Store Sqft")
                                                .column("store_sqft")
                                                .type(TypeEnum.NUMERIC)
                                                .uniqueMembers(true)
                                                .build()
                                        ))
                                        .build()
                                ))
                                .build()
                        ))
                        .measures(List.of(
                            MeasureRBuilder.builder()
                                .name("Unit Sales")
                                .column("unit_sales")
                                .aggregator("sum")
                                .formatString("Standard")
                                .build()
                        ))
                        .build()
                ))
                .build();
        }

    }

}