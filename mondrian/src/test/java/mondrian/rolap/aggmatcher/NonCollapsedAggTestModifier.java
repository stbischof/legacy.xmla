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
package mondrian.rolap.aggmatcher;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.daanse.rolap.mapping.api.model.CatalogMapping;
import org.eclipse.daanse.rolap.mapping.api.model.CubeMapping;
import org.eclipse.daanse.rolap.mapping.api.model.DatabaseSchemaMapping;
import org.eclipse.daanse.rolap.mapping.api.model.TableMapping;
import org.eclipse.daanse.rolap.mapping.api.model.enums.ColumnDataType;
import org.eclipse.daanse.rolap.mapping.modifier.pojo.PojoMappingModifier;
import org.eclipse.daanse.rolap.mapping.pojo.AggregationColumnNameMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.AggregationLevelMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.AggregationMeasureMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.AggregationNameMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.PhysicalColumnMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.DimensionConnectorMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.ExplicitHierarchyMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.HierarchyMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.JoinQueryMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.JoinedQueryElementMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.LevelMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.MeasureGroupMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.MeasureMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.PhysicalCubeMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.PhysicalTableMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.StandardDimensionMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.SumMeasureMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.TableQueryMappingImpl;

public class NonCollapsedAggTestModifier extends PojoMappingModifier {

    //## ColumnNames: line_id,unit_sales
    //## ColumnTypes: INTEGER,INTEGER
	PhysicalColumnMappingImpl lineIdFooFact = PhysicalColumnMappingImpl.builder().withName("line_id").withDataType(ColumnDataType.INTEGER).build();
	PhysicalColumnMappingImpl unitSalesFooFact = PhysicalColumnMappingImpl.builder().withName("unit_sales").withDataType(ColumnDataType.INTEGER).build();
    PhysicalTableMappingImpl fooFact = ((PhysicalTableMappingImpl.Builder) PhysicalTableMappingImpl.builder().withName("foo_fact")
            .withColumns(List.of(lineIdFooFact, unitSalesFooFact))).build();
    //## TableName: line
    //## ColumnNames: line_id,line_name
    //## ColumnTypes: INTEGER,VARCHAR(30)
    PhysicalColumnMappingImpl lineIdLine = PhysicalColumnMappingImpl.builder().withName("line_id").withDataType(ColumnDataType.INTEGER).build();
    PhysicalColumnMappingImpl lineNameLine = PhysicalColumnMappingImpl.builder().withName("line_name").withDataType(ColumnDataType.VARCHAR).withCharOctetLength(30).build();
    PhysicalTableMappingImpl line = ((PhysicalTableMappingImpl.Builder) PhysicalTableMappingImpl.builder().withName("line")
            .withColumns(List.of(lineIdLine, lineNameLine))).build();
    //## TableName: line_tenant
    //## ColumnNames: line_id,tenant_id
    //## ColumnTypes: INTEGER,INTEGER
    PhysicalColumnMappingImpl lineIdLineTenant = PhysicalColumnMappingImpl.builder().withName("line_id").withDataType(ColumnDataType.INTEGER).build();
    PhysicalColumnMappingImpl tenantIdLineTenant = PhysicalColumnMappingImpl.builder().withName("tenant_id").withDataType(ColumnDataType.INTEGER).build();
    PhysicalTableMappingImpl lineTenant = ((PhysicalTableMappingImpl.Builder) PhysicalTableMappingImpl.builder().withName("line_tenant")
            .withColumns(List.of(lineIdLineTenant, tenantIdLineTenant))).build();
    //## TableName: tenant
    //## ColumnNames: tenant_id,tenant_name
    //## ColumnTypes: INTEGER,VARCHAR(30)
    PhysicalColumnMappingImpl tenantIdTenant = PhysicalColumnMappingImpl.builder().withName("tenant_id").withDataType(ColumnDataType.INTEGER).build();
    PhysicalColumnMappingImpl tenantNameTenant = PhysicalColumnMappingImpl.builder().withName("tenant_name").withDataType(ColumnDataType.VARCHAR).withCharOctetLength(30).build();
    PhysicalTableMappingImpl tenant = ((PhysicalTableMappingImpl.Builder) PhysicalTableMappingImpl.builder().withName("tenant")
            .withColumns(List.of(tenantIdTenant, tenantNameTenant))).build();
    //## TableName: line_line_class
    //## ColumnNames: line_id,line_class_id
    //## ColumnTypes: INTEGER,INTEGER
    PhysicalColumnMappingImpl lineIdLineLineClass = PhysicalColumnMappingImpl.builder().withName("line_id").withDataType(ColumnDataType.INTEGER).build();
    PhysicalColumnMappingImpl lineClassIdLineLineClass = PhysicalColumnMappingImpl.builder().withName("line_class_id").withDataType(ColumnDataType.INTEGER).build();
    PhysicalTableMappingImpl lineLineClass = ((PhysicalTableMappingImpl.Builder) PhysicalTableMappingImpl.builder().withName("line_line_class")
            .withColumns(List.of(lineIdLineLineClass, lineClassIdLineLineClass))).build();
    //## TableName: distributor
    //## ColumnNames: distributor_id,distributor_name
    //## ColumnTypes: INTEGER,VARCHAR(30)
    PhysicalColumnMappingImpl distributorIdDistributor = PhysicalColumnMappingImpl.builder().withName("distributor_id").withDataType(ColumnDataType.INTEGER).build();
    PhysicalColumnMappingImpl distributorNameDistributor = PhysicalColumnMappingImpl.builder().withName("distributor_name").withDataType(ColumnDataType.VARCHAR).withCharOctetLength(30).build();
    PhysicalTableMappingImpl distributor = ((PhysicalTableMappingImpl.Builder) PhysicalTableMappingImpl.builder().withName("distributor")
            .withColumns(List.of(distributorIdDistributor, distributorNameDistributor))).build();
    //## TableName: line_class_distributor
    //## ColumnNames: line_class_id,distributor_id
    //## ColumnTypes: INTEGER,INTEGER
    PhysicalColumnMappingImpl lineClassIdLineClassDistributor = PhysicalColumnMappingImpl.builder().withName("line_class_id").withDataType(ColumnDataType.INTEGER).build();
    PhysicalColumnMappingImpl distributorIdLineClassDistributor = PhysicalColumnMappingImpl.builder().withName("distributor_id").withDataType(ColumnDataType.INTEGER).build();
    PhysicalTableMappingImpl lineClassDistributor = ((PhysicalTableMappingImpl.Builder) PhysicalTableMappingImpl.builder().withName("line_class_distributor")
            .withColumns(List.of(lineClassIdLineClassDistributor, distributorIdLineClassDistributor))).build();
    //## TableName: line_class
    //## ColumnNames: line_class_id,line_class_name
    //## ColumnTypes: INTEGER,VARCHAR(30)
    PhysicalColumnMappingImpl lineClassIdLineClass = PhysicalColumnMappingImpl.builder().withName("line_class_id").withDataType(ColumnDataType.INTEGER).build();
    PhysicalColumnMappingImpl lineClassNameLineClass = PhysicalColumnMappingImpl.builder().withName("line_class_name").withDataType(ColumnDataType.VARCHAR).withCharOctetLength(30).build();
    PhysicalTableMappingImpl lineClass = ((PhysicalTableMappingImpl.Builder) PhysicalTableMappingImpl.builder().withName("line_class")
            .withColumns(List.of(lineClassIdLineClass, lineClassNameLineClass))).build();
    //## TableName: network
    //## ColumnNames: network_id,network_name
    //## ColumnTypes: INTEGER,VARCHAR(30)
    PhysicalColumnMappingImpl networkIdNetwork = PhysicalColumnMappingImpl.builder().withName("network_id").withDataType(ColumnDataType.INTEGER).build();
    PhysicalColumnMappingImpl networkNameNetwork = PhysicalColumnMappingImpl.builder().withName("network_name").withDataType(ColumnDataType.VARCHAR).withCharOctetLength(30).build();
    PhysicalTableMappingImpl network = ((PhysicalTableMappingImpl.Builder) PhysicalTableMappingImpl.builder().withName("network")
            .withColumns(List.of(networkIdNetwork, networkNameNetwork))).build();
    //## TableName: line_class_network
    //## ColumnNames: line_class_id,network_id
    //## ColumnTypes: INTEGER,INTEGER
    PhysicalColumnMappingImpl lineClassIdLineClassNetwork = PhysicalColumnMappingImpl.builder().withName("line_class_id").withDataType(ColumnDataType.INTEGER).build();
    PhysicalColumnMappingImpl networkIdLineClassNetwork = PhysicalColumnMappingImpl.builder().withName("network_id").withDataType(ColumnDataType.INTEGER).build();
    PhysicalTableMappingImpl lineClassNetwork = ((PhysicalTableMappingImpl.Builder) PhysicalTableMappingImpl.builder().withName("line_class_network")
            .withColumns(List.of(lineClassIdLineClassNetwork, networkIdLineClassNetwork))).build();

    //## TableName: agg_tenant
    //## ColumnNames: tenant_id,unit_sales,fact_count
    //## ColumnTypes: INTEGER,INTEGER,INTEGER
    PhysicalColumnMappingImpl tenantIdAggTenant = PhysicalColumnMappingImpl.builder().withName("tenant_id").withDataType(ColumnDataType.INTEGER).build();
    PhysicalColumnMappingImpl unitSalesAggTenant = PhysicalColumnMappingImpl.builder().withName("unit_sales").withDataType(ColumnDataType.INTEGER).build();
    PhysicalColumnMappingImpl factCountAggTenant = PhysicalColumnMappingImpl.builder().withName("fact_count").withDataType(ColumnDataType.INTEGER).build();
    PhysicalTableMappingImpl aggTenant = ((PhysicalTableMappingImpl.Builder) PhysicalTableMappingImpl.builder().withName("agg_tenant")
            .withColumns(List.of(tenantIdAggTenant, unitSalesAggTenant, factCountAggTenant))).build();

    //## TableName: agg_line_class
    //## ColumnNames: line_class_id,unit_sales,fact_count
    //## ColumnTypes: INTEGER,INTEGER,INTEGER
    PhysicalColumnMappingImpl lineClassIdAggLineClass = PhysicalColumnMappingImpl.builder().withName("line_class_id").withDataType(ColumnDataType.INTEGER).build();
    PhysicalColumnMappingImpl unitSalesAggLineClass = PhysicalColumnMappingImpl.builder().withName("unit_sales").withDataType(ColumnDataType.INTEGER).build();
    PhysicalColumnMappingImpl factCountAggLineClass = PhysicalColumnMappingImpl.builder().withName("fact_count").withDataType(ColumnDataType.INTEGER).build();
    PhysicalTableMappingImpl aggLineClass = ((PhysicalTableMappingImpl.Builder) PhysicalTableMappingImpl.builder().withName("agg_line_class")
            .withColumns(List.of(lineClassIdAggLineClass, unitSalesAggLineClass, factCountAggLineClass))).build();

    //## TableName: agg_10_foo_fact
    //## ColumnNames: line_class_id,unit_sales,fact_count
    //## ColumnTypes: INTEGER,INTEGER,INTEGER
    PhysicalColumnMappingImpl lineClassIdAgg10FooFact = PhysicalColumnMappingImpl.builder().withName("line_class_id").withDataType(ColumnDataType.INTEGER).build();
    PhysicalColumnMappingImpl unitSalesAgg10FooFact = PhysicalColumnMappingImpl.builder().withName("unit_sales").withDataType(ColumnDataType.INTEGER).build();
    PhysicalColumnMappingImpl factCountAgg10FooFact = PhysicalColumnMappingImpl.builder().withName("fact_count").withDataType(ColumnDataType.INTEGER).build();
    PhysicalTableMappingImpl agg10FooFact = ((PhysicalTableMappingImpl.Builder) PhysicalTableMappingImpl.builder().withName("agg_10_foo_fact")
            .withColumns(List.of(lineClassIdAgg10FooFact, unitSalesAgg10FooFact, factCountAgg10FooFact))).build();


    public NonCollapsedAggTestModifier(CatalogMapping catalog) {
        super(catalog);
    }

    /*
            "<Cube name=\"foo\">\n"
        + "    <Table name=\"foo_fact\">\n"
        + "        <AggName name=\"agg_tenant\">\n"
        + "            <AggFactCount column=\"fact_count\"/>\n"
        + "            <AggMeasure name=\"[Measures].[Unit Sales]\" column=\"unit_sales\"/>\n"
        + "            <AggLevel name=\"[dimension.tenant].[tenant]\"\n"
        + "                column=\"tenant_id\" collapsed=\"false\"/>\n"
        + "        </AggName>\n"
        + "        <AggName name=\"agg_line_class\">\n"
        + "            <AggFactCount column=\"fact_count\"/>\n"
        + "            <AggMeasure name=\"[Measures].[Unit Sales]\" column=\"unit_sales\"/>\n"
        + "            <AggLevel name=\"[dimension.distributor].[line class]\"\n"
        + "                column=\"line_class_id\" collapsed=\"false\"/>\n"
        + "        </AggName>\n"
        + "        <AggName name=\"agg_line_class\">\n"
        + "            <AggFactCount column=\"fact_count\"/>\n"
        + "            <AggMeasure name=\"[Measures].[Unit Sales]\" column=\"unit_sales\"/>\n"
        + "            <AggLevel name=\"[dimension.network].[line class]\"\n"
        + "                column=\"line_class_id\" collapsed=\"false\"/>\n"
        + "        </AggName>\n"
        + "    </Table>\n"
        + "    <Dimension name=\"dimension\" foreignKey=\"line_id\">\n"
        + "        <Hierarchy name=\"tenant\" hasAll=\"true\" allMemberName=\"All tenants\"\n"
        + "            primaryKey=\"line_id\" primaryKeyTable=\"line\">\n"
        + "            <Join leftKey=\"line_id\" rightKey=\"line_id\"\n"
        + "                rightAlias=\"line_tenant\">\n"
        + "                <Table name=\"line\"/>\n"
        + "                <Join leftKey=\"tenant_id\" rightKey=\"tenant_id\">\n"
        + "                    <Table name=\"line_tenant\"/>\n"
        + "                    <Table name=\"tenant\"/>\n"
        + "                </Join>\n"
        + "            </Join>\n"
        + "            <Level name=\"tenant\" table=\"tenant\" column=\"tenant_id\" nameColumn=\"tenant_name\" uniqueMembers=\"true\"/>\n"
        + "            <Level name=\"line\" table=\"line\" column=\"line_id\" nameColumn=\"line_name\"/>\n"
        + "        </Hierarchy>\n"
        + "        <Hierarchy name=\"distributor\" hasAll=\"true\" allMemberName=\"All distributors\"\n"
        + "            primaryKey=\"line_id\" primaryKeyTable=\"line\">\n"
        + "            <Join leftKey=\"line_id\" rightKey=\"line_id\" rightAlias=\"line_line_class\">\n"
        + "                <Table name=\"line\"/>\n"
        + "                <Join leftKey=\"line_class_id\" rightKey=\"line_class_id\" rightAlias=\"line_class\">\n"
        + "                    <Table name=\"line_line_class\"/>\n"
        + "                    <Join leftKey=\"line_class_id\" rightKey=\"line_class_id\" rightAlias=\"line_class_distributor\">\n"
        + "                        <Table name=\"line_class\"/>\n"
        + "                        <Join leftKey=\"distributor_id\" rightKey=\"distributor_id\">\n"
        + "                            <Table name=\"line_class_distributor\"/>\n"
        + "                            <Table name=\"distributor\"/>\n"
        + "                        </Join>\n"
        + "                    </Join>\n"
        + "                </Join>\n"
        + "            </Join>\n"
        + "            <Level name=\"distributor\" table=\"distributor\" column=\"distributor_id\" nameColumn=\"distributor_name\"/>\n"
        + "            <Level name=\"line class\" table=\"line_class\" column=\"line_class_id\" nameColumn=\"line_class_name\" uniqueMembers=\"true\"/>\n"
        + "            <Level name=\"line\" table=\"line\" column=\"line_id\" nameColumn=\"line_name\"/>\n"
        + "        </Hierarchy>\n"
        + "        <Hierarchy name=\"network\" hasAll=\"true\" allMemberName=\"All networks\"\n"
        + "            primaryKey=\"line_id\" primaryKeyTable=\"line\">\n"
        + "            <Join leftKey=\"line_id\" rightKey=\"line_id\" rightAlias=\"line_line_class\">\n"
        + "                <Table name=\"line\"/>\n"
        + "                <Join leftKey=\"line_class_id\" rightKey=\"line_class_id\" rightAlias=\"line_class\">\n"
        + "                    <Table name=\"line_line_class\"/>\n"
        + "                    <Join leftKey=\"line_class_id\" rightKey=\"line_class_id\" rightAlias=\"line_class_network\">\n"
        + "                        <Table name=\"line_class\"/>\n"
        + "                        <Join leftKey=\"network_id\" rightKey=\"network_id\">\n"
        + "                            <Table name=\"line_class_network\"/>\n"
        + "                            <Table name=\"network\"/>\n"
        + "                        </Join>\n"
        + "                    </Join>\n"
        + "                </Join>\n"
        + "            </Join>\n"
        + "            <Level name=\"network\" table=\"network\" column=\"network_id\" nameColumn=\"network_name\"/>\n"
        + "            <Level name=\"line class\" table=\"line_class\" column=\"line_class_id\" nameColumn=\"line_class_name\" uniqueMembers=\"true\"/>\n"
        + "            <Level name=\"line\" table=\"line\" column=\"line_id\" nameColumn=\"line_name\"/>\n"
        + "        </Hierarchy>\n"
        + " </Dimension>\n"
        + "   <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\" formatString=\"Standard\" />\n"
        + "</Cube>\n"
        + "<Cube name=\"foo2\">\n"
        + "    <Table name=\"foo_fact\">\n"
        + "    </Table>\n"
        + "    <Dimension name=\"dimension\" foreignKey=\"line_id\">\n"
        + "        <Hierarchy name=\"tenant\" hasAll=\"true\" allMemberName=\"All tenants\"\n"
        + "            primaryKey=\"line_id\" primaryKeyTable=\"line\">\n"
        + "            <Join leftKey=\"line_id\" rightKey=\"line_id\"\n"
        + "                rightAlias=\"line_tenant\">\n"
        + "                <Table name=\"line\"/>\n"
        + "                <Join leftKey=\"tenant_id\" rightKey=\"tenant_id\">\n"
        + "                    <Table name=\"line_tenant\"/>\n"
        + "                    <Table name=\"tenant\"/>\n"
        + "                </Join>\n"
        + "            </Join>\n"
        + "            <Level name=\"tenant\" table=\"tenant\" column=\"tenant_id\" nameColumn=\"tenant_name\" uniqueMembers=\"true\"/>\n"
        + "            <Level name=\"line\" table=\"line\" column=\"line_id\" nameColumn=\"line_name\"/>\n"
        + "        </Hierarchy>\n"
        + "        <Hierarchy name=\"distributor\" hasAll=\"true\" allMemberName=\"All distributors\"\n"
        + "            primaryKey=\"line_id\" primaryKeyTable=\"line\">\n"
        + "            <Join leftKey=\"line_id\" rightKey=\"line_id\" rightAlias=\"line_line_class\">\n"
        + "                <Table name=\"line\"/>\n"
        + "                <Join leftKey=\"line_class_id\" rightKey=\"line_class_id\" rightAlias=\"line_class\">\n"
        + "                    <Table name=\"line_line_class\"/>\n"
        + "                    <Join leftKey=\"line_class_id\" rightKey=\"line_class_id\" rightAlias=\"line_class_distributor\">\n"
        + "                        <Table name=\"line_class\"/>\n"
        + "                        <Join leftKey=\"distributor_id\" rightKey=\"distributor_id\">\n"
        + "                            <Table name=\"line_class_distributor\"/>\n"
        + "                            <Table name=\"distributor\"/>\n"
        + "                        </Join>\n"
        + "                    </Join>\n"
        + "                </Join>\n"
        + "            </Join>\n"
        + "            <Level name=\"distributor\" table=\"distributor\" column=\"distributor_id\" nameColumn=\"distributor_name\"/>\n"
        + "            <Level name=\"line class\" table=\"line_class\" column=\"line_class_id\" nameColumn=\"line_class_name\" uniqueMembers=\"true\"/>\n"
        + "            <Level name=\"line\" table=\"line\" column=\"line_id\" nameColumn=\"line_name\"/>\n"
        + "        </Hierarchy>\n"
        + "        <Hierarchy name=\"network\" hasAll=\"true\" allMemberName=\"All networks\"\n"
        + "            primaryKey=\"line_id\" primaryKeyTable=\"line\">\n"
        + "            <Join leftKey=\"line_id\" rightKey=\"line_id\" rightAlias=\"line_line_class\">\n"
        + "                <Table name=\"line\"/>\n"
        + "                <Join leftKey=\"line_class_id\" rightKey=\"line_class_id\" rightAlias=\"line_class\">\n"
        + "                    <Table name=\"line_line_class\"/>\n"
        + "                    <Join leftKey=\"line_class_id\" rightKey=\"line_class_id\" rightAlias=\"line_class_network\">\n"
        + "                        <Table name=\"line_class\"/>\n"
        + "                        <Join leftKey=\"network_id\" rightKey=\"network_id\">\n"
        + "                            <Table name=\"line_class_network\"/>\n"
        + "                            <Table name=\"network\"/>\n"
        + "                        </Join>\n"
        + "                    </Join>\n"
        + "                </Join>\n"
        + "            </Join>\n"
        + "            <Level name=\"network\" table=\"network\" column=\"network_id\" nameColumn=\"network_name\"/>\n"
        + "            <Level name=\"line class\" table=\"line_class\" column=\"line_class_id\" nameColumn=\"line_class_name\" uniqueMembers=\"true\"/>\n"
        + "            <Level name=\"line\" table=\"line\" column=\"line_id\" nameColumn=\"line_name\"/>\n"
        + "        </Hierarchy>\n"
        + " </Dimension>\n"
        + "   <Measure name=\"Unit Sales\" column=\"unit_sales\" aggregator=\"sum\" formatString=\"Standard\" />\n"
        + "</Cube>\n";

     */

    @Override
    protected List<? extends TableMapping> databaseSchemaTables(DatabaseSchemaMapping databaseSchema) {
        List<TableMapping> result = new ArrayList();
        result.addAll(super.databaseSchemaTables(databaseSchema));
        result.addAll(List.of(fooFact, line, lineTenant, tenant, lineLineClass, distributor, lineClassDistributor,
        		lineClass, network, lineClassNetwork, aggTenant, aggLineClass, agg10FooFact));
        return result;
    }

    @Override
    protected List<? extends CubeMapping> catalogCubes(CatalogMapping schemaMappingOriginal) {

        List<CubeMapping> result = new ArrayList<>();
        result.add(PhysicalCubeMappingImpl.builder()
        	.withName("foo")
            .withQuery(TableQueryMappingImpl.builder().withTable(fooFact)
            	.withAggregationTables(List.of(
                	AggregationNameMappingImpl.builder()
                        .withName(aggTenant)
                        .withAggregationFactCount(AggregationColumnNameMappingImpl.builder()
                            .withColumn(factCountAggTenant)
                            .build())
                        .withAggregationMeasures(List.of(
                        		AggregationMeasureMappingImpl.builder()
                                .withName("[Measures].[Unit Sales]").withColumn(unitSalesAggTenant)
                                .build()
                        ))
                        .withAggregationLevels(List.of(
                        		AggregationLevelMappingImpl.builder()
                                .withName("[dimension].[tenant].[tenant]")
                                .withColumn(tenantIdAggTenant).withCollapsed(false)
                                .build()
                        ))
                        .build(),
                    AggregationNameMappingImpl.builder()
                        .withName(aggLineClass)
                        .withAggregationFactCount(AggregationColumnNameMappingImpl.builder()
                            .withColumn(factCountAggLineClass)
                            .build())
                        .withAggregationMeasures(List.of(
                            AggregationMeasureMappingImpl.builder()
                                .withName("[Measures].[Unit Sales]").withColumn(unitSalesAggLineClass)
                                .build()
                        ))
                        .withAggregationLevels(List.of(
                            AggregationLevelMappingImpl.builder()
                                .withName("[dimension].[distributor].[line class]")
                                .withColumn(lineClassIdAggLineClass).withCollapsed(false)
                                .build()
                        ))
                        .build(),
                    AggregationNameMappingImpl.builder()
                        .withName(aggLineClass)
                        .withAggregationFactCount(AggregationColumnNameMappingImpl.builder()
                            .withColumn(factCountAggLineClass)
                            .build())
                        .withAggregationMeasures(List.of(
                            AggregationMeasureMappingImpl.builder()
                                .withName("[Measures].[Unit Sales]").withColumn(unitSalesAggLineClass)
                                .build()
                        ))
                        .withAggregationLevels(List.of(
                            AggregationLevelMappingImpl.builder()
                                .withName("[dimension].[network].[line class]")
                                .withColumn(lineClassIdAggLineClass).withCollapsed(false)
                                .build()
                        ))
                        .build()
             )).build())
            .withDimensionConnectors(List.of(
            	DimensionConnectorMappingImpl.builder()
            		.withOverrideDimensionName("dimension")
                    .withForeignKey(lineIdFooFact)
                    .withDimension(StandardDimensionMappingImpl.builder()
                    	.withName("dimension")
                    	.withHierarchies(List.of(
                        ExplicitHierarchyMappingImpl.builder()
                            .withName("tenant")
                            .withHasAll(true)
                            .withAllMemberName("All tenants")
                            .withPrimaryKey(lineIdLine)
                            .withQuery(JoinQueryMappingImpl.builder()
                            		.withLeft(JoinedQueryElementMappingImpl.builder()
                            				.withKey(lineIdLine)
                            				.withQuery(TableQueryMappingImpl.builder().withTable(line).build())
                            				.build())
                            		.withRight(JoinedQueryElementMappingImpl.builder()
                            				.withAlias("line_tenant")
                            				.withKey(lineIdLineTenant)
                                            .withQuery(JoinQueryMappingImpl.builder()
                                            		.withLeft(JoinedQueryElementMappingImpl.builder()
                                            				.withKey(tenantIdLineTenant)
                                            				.withQuery(TableQueryMappingImpl.builder().withTable(lineTenant).build())
                                            				.build())
                                            		.withRight(JoinedQueryElementMappingImpl.builder()
                                            				.withKey(tenantIdTenant)
                                            				.withQuery(TableQueryMappingImpl.builder().withTable(tenant).build())
                                            				.build())
                                            		.build())
                            				.build())
                            		.build())
                            .withLevels(List.of(
                                LevelMappingImpl.builder()
                                    .withName("tenant")
                                    .withColumn(tenantIdTenant)
                                    .withNameColumn(tenantNameTenant)
                                    .withUniqueMembers(true)
                                    .build(),
                                LevelMappingImpl.builder()
                                    .withName("line")
                                    .withColumn(lineIdLine)
                                    .withNameColumn(lineNameLine)
                                    .build()

                            ))
                            .build(),
                        ExplicitHierarchyMappingImpl.builder()
                            .withName("distributor")
                            .withHasAll(true)
                            .withAllMemberName("All distributors")
                            .withPrimaryKey(lineIdLine)
                            .withQuery(JoinQueryMappingImpl.builder()
                            		.withLeft(JoinedQueryElementMappingImpl.builder()
                            				.withKey(lineIdLine)
                            				.withQuery(TableQueryMappingImpl.builder().withTable(line).build())
                            				.build())
                            		.withRight(JoinedQueryElementMappingImpl.builder()
                            				.withAlias("line_line_class")
                            				.withKey(lineIdLineLineClass)
                                            .withQuery(JoinQueryMappingImpl.builder()
                                            		.withLeft(JoinedQueryElementMappingImpl.builder()
                                            				.withKey(lineClassIdLineLineClass)
                                            				.withQuery(TableQueryMappingImpl.builder().withTable(lineLineClass).build())
                                            				.build())
                                            		.withRight(JoinedQueryElementMappingImpl.builder()
                                            				.withAlias("line_class")
                                            				.withKey(lineClassIdLineClass)
                                                            .withQuery(JoinQueryMappingImpl.builder()
                                                            		.withLeft(JoinedQueryElementMappingImpl.builder()
                                                            				.withKey(lineClassIdLineClass)
                                                            				.withQuery(TableQueryMappingImpl.builder().withTable(lineClass).build())
                                                            				.build())
                                                            		.withRight(JoinedQueryElementMappingImpl.builder()
                                                            				.withAlias("line_class_distributor")
                                                            				.withKey(lineClassIdLineClassDistributor)
                                                                            .withQuery(JoinQueryMappingImpl.builder()
                                                                            		.withLeft(JoinedQueryElementMappingImpl.builder()
                                                                            				.withKey(distributorIdLineClassDistributor)
                                                                            				.withQuery(TableQueryMappingImpl.builder().withTable(lineClassDistributor).build())
                                                                            				.build())
                                                                            		.withRight(JoinedQueryElementMappingImpl.builder()
                                                                            				.withKey(distributorIdDistributor)
                                                                            				.withQuery(TableQueryMappingImpl.builder().withTable(distributor).build())
                                                                            				.build())
                                                                            		.build())
                                                            				.build())
                                                            		.build())

                                            				.build())
                                            		.build())
                            				.build())
                            		.build())
                            .withLevels(List.of(
                                LevelMappingImpl.builder()
                                    .withName("distributor")
                                    .withColumn(distributorIdDistributor)
                                    .withNameColumn(distributorNameDistributor)
                                    .build(),
                                LevelMappingImpl.builder()
                                    .withName("line class")
                                    .withColumn(lineClassIdLineClass)
                                    .withNameColumn(lineClassNameLineClass)
                                    .withUniqueMembers(true)
                                    .build(),
                                LevelMappingImpl.builder()
                                    .withName("line")
                                    .withColumn(lineIdLine)
                                    .withNameColumn(lineNameLine)
                                    .build()
                            ))
                            .build(),
                        ExplicitHierarchyMappingImpl.builder()
                            .withName("network")
                            .withHasAll(true)
                            .withAllMemberName("All networks")
                            .withPrimaryKey(lineIdLine)
                            .withQuery(JoinQueryMappingImpl.builder()
                            		.withLeft(JoinedQueryElementMappingImpl.builder()
                            				.withKey(lineIdLine)
                            				.withQuery(TableQueryMappingImpl.builder().withTable(line).build())
                            				.build())
                            		.withRight(JoinedQueryElementMappingImpl.builder()
                            				.withAlias("line_line_class")
                            				.withKey(lineIdLineLineClass)
                                            .withQuery(JoinQueryMappingImpl.builder()
                                            		.withLeft(JoinedQueryElementMappingImpl.builder()
                                            				.withKey(lineClassIdLineLineClass)
                                            				.withQuery(TableQueryMappingImpl.builder().withTable(lineLineClass).build())
                                            				.build())
                                            		.withRight(JoinedQueryElementMappingImpl.builder()
                                            				.withAlias("line_class")
                                            				.withKey(lineClassIdLineClass)
                                                            .withQuery(JoinQueryMappingImpl.builder()
                                                            		.withLeft(JoinedQueryElementMappingImpl.builder()
                                                            				.withKey(lineClassIdLineClass)
                                                            				.withQuery(TableQueryMappingImpl.builder().withTable(lineClass).build())
                                                            				.build())
                                                            		.withRight(JoinedQueryElementMappingImpl.builder()
                                                            				.withAlias("line_class_network")
                                                            				.withKey(lineClassIdLineClassNetwork)
                                                                            .withQuery(JoinQueryMappingImpl.builder()
                                                                            		.withLeft(JoinedQueryElementMappingImpl.builder()
                                                                            				.withKey(networkIdLineClassNetwork)
                                                                            				.withQuery(TableQueryMappingImpl.builder().withTable(lineClassNetwork).build())
                                                                            				.build())
                                                                            		.withRight(JoinedQueryElementMappingImpl.builder()
                                                                            				.withKey(networkIdNetwork)
                                                                            				.withQuery(TableQueryMappingImpl.builder().withTable(network).build())
                                                                            				.build())
                                                                            		.build())
                                                            				.build())
                                                            		.build())

                                            				.build())
                                            		.build())
                            				.build())
                            		.build())
                            .withLevels(List.of(
                                LevelMappingImpl.builder()
                                    .withName("network")
                                    .withColumn(networkIdNetwork)
                                    .withNameColumn(networkNameNetwork)
                                    .build(),
                                LevelMappingImpl.builder()
                                    .withName("line class")
                                    .withColumn(lineClassIdLineClass)
                                    .withNameColumn(lineClassNameLineClass)
                                    .withUniqueMembers(true)
                                    .build(),
                                LevelMappingImpl.builder()
                                    .withName("line")
                                    .withColumn(lineIdLine)
                                    .withNameColumn(lineNameLine)
                                    .build()
                            ))
                            .build()
                    )).build())
                    .build()
            ))
            .withMeasureGroups(List.of(MeasureGroupMappingImpl.builder().withMeasures(List.of(
                SumMeasureMappingImpl.builder()
                    .withName("Unit Sales")
                    .withColumn(unitSalesFooFact)
                    .withFormatString("Standard")
                    .build()

            )).build()))
            .build());

        result.add(PhysicalCubeMappingImpl.builder()
            .withName("foo2")
            .withQuery(TableQueryMappingImpl.builder().withTable(fooFact).build())
            .withDimensionConnectors(List.of(
                DimensionConnectorMappingImpl.builder()
                	.withOverrideDimensionName("dimension")
                    .withForeignKey(lineIdFooFact)
                    .withDimension(StandardDimensionMappingImpl.builder()
                    	.withName("dimension")
                    	.withHierarchies(List.of(
                        ExplicitHierarchyMappingImpl.builder()
                            .withName("tenant")
                            .withHasAll(true)
                            .withAllMemberName("All tenants")
                            .withPrimaryKey(lineIdLine)
                            .withQuery(JoinQueryMappingImpl.builder()
                            		.withLeft(JoinedQueryElementMappingImpl.builder()
                            				.withKey(lineIdLine)
                            				.withQuery(TableQueryMappingImpl.builder().withTable(line).build())
                            				.build())
                            		.withRight(JoinedQueryElementMappingImpl.builder()
                            				.withAlias("line_tenant")
                            				.withKey(lineIdLineTenant)
                                            .withQuery(JoinQueryMappingImpl.builder()
                                            		.withLeft(JoinedQueryElementMappingImpl.builder()
                                            				.withKey(tenantIdLineTenant)
                                            				.withQuery(TableQueryMappingImpl.builder().withTable(lineTenant).build())
                                            				.build())
                                            		.withRight(JoinedQueryElementMappingImpl.builder()
                                            				.withKey(tenantIdTenant)
                                            				.withQuery(TableQueryMappingImpl.builder().withTable(tenant).build())
                                            				.build())
                                            		.build())
                            				.build())
                            		.build())
                            .withLevels(List.of(
                                LevelMappingImpl.builder()
                                    .withName("tenant")
                                    .withColumn(tenantIdTenant)
                                    .withNameColumn(tenantNameTenant)
                                    .withUniqueMembers(true)
                                    .build(),
                                LevelMappingImpl.builder()
                                    .withName("line")
                                    .withColumn(lineIdLine)
                                    .withNameColumn(lineNameLine)
                                    .build()
                            ))
                            .build(),
                        ExplicitHierarchyMappingImpl.builder()
                            .withName("distributor")
                            .withHasAll(true)
                            .withAllMemberName("All distributors")
                            .withPrimaryKey(lineIdLine)
                            .withQuery(JoinQueryMappingImpl.builder()
                            		.withLeft(JoinedQueryElementMappingImpl.builder()
                            				.withKey(lineIdLine)
                            				.withQuery(TableQueryMappingImpl.builder().withTable(line).build())
                            				.build())
                            		.withRight(JoinedQueryElementMappingImpl.builder()
                            				.withAlias("line_line_class")
                            				.withKey(lineIdLineLineClass)
                                            .withQuery(JoinQueryMappingImpl.builder()
                                            		.withLeft(JoinedQueryElementMappingImpl.builder()
                                            				.withKey(lineClassIdLineLineClass)
                                            				.withQuery(TableQueryMappingImpl.builder().withTable(lineLineClass).build())
                                            				.build())
                                            		.withRight(JoinedQueryElementMappingImpl.builder()
                                            				.withAlias("line_class")
                                            				.withKey(lineClassIdLineClass)
                                                            .withQuery(JoinQueryMappingImpl.builder()
                                                            		.withLeft(JoinedQueryElementMappingImpl.builder()
                                                            				.withKey(lineClassIdLineClass)
                                                            				.withQuery(TableQueryMappingImpl.builder().withTable(lineClass).build())
                                                            				.build())
                                                            		.withRight(JoinedQueryElementMappingImpl.builder()
                                                            				.withAlias("line_class")
                                                            				.withKey(lineClassIdLineClass)
                                                                            .withQuery(JoinQueryMappingImpl.builder()
                                                                            		.withLeft(JoinedQueryElementMappingImpl.builder()
                                                                            				.withKey(lineClassIdLineClass)
                                                                            				.withQuery(TableQueryMappingImpl.builder().withTable(lineClass).build())
                                                                            				.build())
                                                                            		.withRight(JoinedQueryElementMappingImpl.builder()
                                                                            				.withAlias("line_class_distributor")
                                                                            				.withKey(lineClassIdLineClassDistributor)
                                                                            				.withQuery(JoinQueryMappingImpl.builder()
                                                                                            		.withLeft(JoinedQueryElementMappingImpl.builder()
                                                                                            				.withKey(distributorIdLineClassDistributor)
                                                                                            				.withQuery(TableQueryMappingImpl.builder().withTable(lineClassDistributor).build())
                                                                                            				.build())
                                                                                            		.withRight(JoinedQueryElementMappingImpl.builder()
                                                                                            				.withKey(distributorIdDistributor)
                                                                                            				.withQuery(TableQueryMappingImpl.builder().withTable(distributor).build())
                                                                                            				.build())
                                                                                            		.build())
                                                                            				.build())
                                                                            		.build())
                                                            				.build())
                                                            		.build())

                                            				.build())
                                            		.build())
                            				.build())
                            		.build())
                            .withLevels(List.of(
                                LevelMappingImpl.builder()
                                    .withName("distributor")
                                    .withColumn(distributorIdDistributor)
                                    .withNameColumn(distributorNameDistributor)
                                    .build(),
                                LevelMappingImpl.builder()
                                    .withName("line class")
                                    .withColumn(lineClassIdLineClass)
                                    .withNameColumn(lineClassNameLineClass)
                                    .withUniqueMembers(true)
                                    .build(),
                                LevelMappingImpl.builder()
                                    .withName("line")
                                    .withColumn(lineIdLine)
                                    .withNameColumn(lineNameLine)
                                    .build()
                            ))
                            .build(),
                        ExplicitHierarchyMappingImpl.builder()
                            .withName("network")
                            .withHasAll(true)
                            .withAllMemberName("All networks")
                            .withPrimaryKey(lineIdLine)

                            .withQuery(JoinQueryMappingImpl.builder()
                            		.withLeft(JoinedQueryElementMappingImpl.builder()
                            				.withKey(lineIdLine)
                            				.withQuery(TableQueryMappingImpl.builder().withTable(line).build())
                            				.build())
                            		.withRight(JoinedQueryElementMappingImpl.builder()
                            				.withAlias("line_line_class").withKey(lineIdLineLineClass)
                                            .withQuery(JoinQueryMappingImpl.builder()
                                            		.withLeft(JoinedQueryElementMappingImpl.builder()
                                            				.withKey(lineClassIdLineLineClass)
                                            				.withQuery(TableQueryMappingImpl.builder().withTable(lineLineClass).build())
                                            				.build())
                                            		.withRight(JoinedQueryElementMappingImpl.builder()
                                            				.withAlias("line_class").withKey(lineClassIdLineClass)//-
                                                            .withQuery(JoinQueryMappingImpl.builder()
                                                            		.withLeft(JoinedQueryElementMappingImpl.builder()
                                                            				.withKey(lineClassIdLineClass)
                                                            				.withQuery(TableQueryMappingImpl.builder().withTable(lineClass).build()) //----
                                                            				.build())
                                                            		.withRight(JoinedQueryElementMappingImpl.builder()
                                                            				.withAlias("line_class_network").withKey(lineClassIdLineClassNetwork) //++
                                                                            .withQuery(JoinQueryMappingImpl.builder()
                                                                            		.withLeft(JoinedQueryElementMappingImpl.builder()
                                                                            				.withKey(networkIdLineClassNetwork)
                                                                            				.withQuery(TableQueryMappingImpl.builder().withTable(lineClassNetwork).build())
                                                                            				.build())
                                                                            		.withRight(JoinedQueryElementMappingImpl.builder()
                                                                            				.withKey(networkIdNetwork)
                                                                            				.withQuery(TableQueryMappingImpl.builder().withTable(network).build())
                                                                            				.build())
                                                                            		.build())
                                                            				.build())
                                                            		.build())

                                            				.build())
                                            		.build())
                            				.build())
                            .build())
                            .withLevels(List.of(
                                LevelMappingImpl.builder()
                                    .withName("network")
                                    .withColumn(networkIdNetwork)
                                    .withNameColumn(networkNameNetwork)
                                    .build(),
                                LevelMappingImpl.builder()
                                    .withName("line class")
                                    .withColumn(lineClassIdLineClass)
                                    .withNameColumn(lineClassNameLineClass)
                                    .withUniqueMembers(true)
                                    .build(),
                                LevelMappingImpl.builder()
                                    .withName("line")
                                    .withColumn(lineIdLine)
                                    .withNameColumn(lineNameLine)
                                    .build()
                            ))
                            .build()
                    )).build())
                    .build()
            ))
            .withMeasureGroups(List.of(MeasureGroupMappingImpl.builder().withMeasures(List.of(
                SumMeasureMappingImpl.builder()
                    .withName("Unit Sales")
                    .withColumn(unitSalesFooFact)
                    .withFormatString("Standard")
                    .build()
            )).build()))
            .build());
        result.addAll(super.catalogCubes(schemaMappingOriginal));
        return result;
    }
}
