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
import org.eclipse.daanse.rolap.mapping.api.model.enums.ColumnDataType;
import org.eclipse.daanse.rolap.mapping.api.model.enums.InternalDataType;
import org.eclipse.daanse.rolap.mapping.modifier.pojo.PojoMappingModifier;
import org.eclipse.daanse.rolap.mapping.pojo.PhysicalColumnMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.DimensionConnectorMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.ExplicitHierarchyMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.HierarchyMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.LevelMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.MeasureGroupMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.PhysicalCubeMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.PhysicalTableMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.StandardDimensionMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.SumMeasureMappingImpl;
import org.eclipse.daanse.rolap.mapping.pojo.TableQueryMappingImpl;

public class Checkin_7634Modifier extends PojoMappingModifier {

    public Checkin_7634Modifier(CatalogMapping c) {
        super(c);
    }

    /*
                "<Cube name='Checkin_7634'>\n"
            + "<Table name='table7634'/>\n"
            + "<Dimension name='Geography' foreignKey='cust_loc_id'>\n"
            + "    <Hierarchy hasAll='true' allMemberName='All Regions' primaryKey='cust_loc_id'>\n"
            + "    <Table name='geography7631'/>\n"
            + "    <Level column='state_cd' name='State' type='String' uniqueMembers='true'/>\n"
            + "    <Level column='city_nm' name='City' type='String' uniqueMembers='true'/>\n"
            + "    <Level column='zip_cd' name='Zip Code' type='String' uniqueMembers='true'/>\n"
            + "    </Hierarchy>\n"
            + "</Dimension>\n"
            + "<Dimension name='Product' foreignKey='prod_id'>\n"
            + "    <Hierarchy hasAll='true' allMemberName='All Products' primaryKey='prod_id'>\n"
            + "    <Table name='prod7631'/>\n"
            + "    <Level column='class' name='Class' type='String' uniqueMembers='true'/>\n"
            + "    <Level column='brand' name='Brand' type='String' uniqueMembers='true'/>\n"
            + "    <Level column='item' name='Item' type='String' uniqueMembers='true'/>\n"
            + "    </Hierarchy>\n"
            + "</Dimension>\n"
            + "<Measure name='First Measure' \n"
            + "    column='first' aggregator='sum'\n"
            + "   formatString='#,###'/>\n"
            + "<Measure name='Requested Value' \n"
            + "    column='request_value' aggregator='sum'\n"
            + "   formatString='#,###'/>\n"
            + "<Measure name='Shipped Value' \n"
            + "    column='shipped_value' aggregator='sum'\n"
            + "   formatString='#,###'/>\n"
            + "</Cube>";

     */

    @Override
    protected List<? extends CubeMapping> catalogCubes(CatalogMapping schemaMappingOriginal) {
        List<CubeMapping> result = new ArrayList<>();
        PhysicalColumnMappingImpl cust_loc_id_geography7631 = PhysicalColumnMappingImpl.builder().withName("cust_loc_id").withDataType(ColumnDataType.INTEGER).build();
        PhysicalColumnMappingImpl state_cd = PhysicalColumnMappingImpl.builder().withName("state_cd").withDataType(ColumnDataType.VARCHAR).withCharOctetLength(20).build();
        PhysicalColumnMappingImpl city_nm = PhysicalColumnMappingImpl.builder().withName("city_nm").withDataType(ColumnDataType.VARCHAR).withCharOctetLength(20).build();
        PhysicalColumnMappingImpl zip_cd = PhysicalColumnMappingImpl.builder().withName("zip_cd").withDataType(ColumnDataType.VARCHAR).withCharOctetLength(20).build();
        PhysicalTableMappingImpl geography7631 = ((PhysicalTableMappingImpl.Builder) PhysicalTableMappingImpl.builder().withName("geography7631")
                .withColumns(List.of(
                        cust_loc_id_geography7631, state_cd, city_nm, zip_cd
                        ))).build();
        PhysicalColumnMappingImpl cust_loc_id_table7634 = PhysicalColumnMappingImpl.builder().withName("cust_loc_id").withDataType(ColumnDataType.INTEGER).build();
        PhysicalColumnMappingImpl prod_id_table7634 = PhysicalColumnMappingImpl.builder().withName("prod_id").withDataType(ColumnDataType.INTEGER).build();
        PhysicalColumnMappingImpl first_table7634 = PhysicalColumnMappingImpl.builder().withName("first").withDataType(ColumnDataType.DECIMAL).withColumnSize(10).withDecimalDigits(2).build();
        PhysicalColumnMappingImpl request_value_table7634 = PhysicalColumnMappingImpl.builder().withName("request_value").withDataType(ColumnDataType.DECIMAL).withColumnSize(10).withDecimalDigits(2).build();
        PhysicalColumnMappingImpl shipped_value_table7634 = PhysicalColumnMappingImpl.builder().withName("shipped_value").withDataType(ColumnDataType.DECIMAL).withColumnSize(10).withDecimalDigits(2).build();
        PhysicalTableMappingImpl table7634 = ((PhysicalTableMappingImpl.Builder) PhysicalTableMappingImpl.builder().withName("table7634")
                .withColumns(List.of(
                        cust_loc_id_table7634, prod_id_table7634, first_table7634, request_value_table7634, shipped_value_table7634
                        ))).build();
        PhysicalColumnMappingImpl prod_id_prod7631 = PhysicalColumnMappingImpl.builder().withName("prod_id").withDataType(ColumnDataType.INTEGER).build();
        PhysicalColumnMappingImpl class_prod7631 = PhysicalColumnMappingImpl.builder().withName("class").withDataType(ColumnDataType.INTEGER).build();
        PhysicalColumnMappingImpl brand_prod7631 = PhysicalColumnMappingImpl.builder().withName("brand").withDataType(ColumnDataType.INTEGER).build();
        PhysicalColumnMappingImpl item_prod7631 = PhysicalColumnMappingImpl.builder().withName("item").withDataType(ColumnDataType.INTEGER).build();
        PhysicalTableMappingImpl prod7631 = ((PhysicalTableMappingImpl.Builder) PhysicalTableMappingImpl.builder().withName("prod7631")
                .withColumns(List.of(
                		prod_id_prod7631, class_prod7631, brand_prod7631, item_prod7631
                        ))).build();

        result.addAll(super.catalogCubes(schemaMappingOriginal));
        result.add(PhysicalCubeMappingImpl.builder()
            .withName("Checkin_7634")
            .withQuery(TableQueryMappingImpl.builder().withTable(table7634).build())
            .withDimensionConnectors(List.of(
            	DimensionConnectorMappingImpl.builder()
            		.withOverrideDimensionName("Geography")
                    .withForeignKey(cust_loc_id_table7634)
                    .withDimension(StandardDimensionMappingImpl.builder()
                        .withName("Geography")
                        .withHierarchies(List.of(
                        ExplicitHierarchyMappingImpl.builder()
                            .withHasAll(true)
                            .withAllMemberName("All Regions")
                            .withPrimaryKey(cust_loc_id_geography7631)
                            .withQuery(TableQueryMappingImpl.builder().withTable(geography7631).build())
                            .withLevels(List.of(
                                LevelMappingImpl.builder()
                                    .withColumn(state_cd)
                                    .withName("State")
                                    .withType(InternalDataType.STRING)
                                    .withUniqueMembers(true)
                                    .build(),
                                LevelMappingImpl.builder()
                                    .withColumn(city_nm)
                                    .withName("City")
                                    .withType(InternalDataType.STRING)
                                    .withUniqueMembers(true)
                                    .build(),
                                LevelMappingImpl.builder()
                                    .withColumn(zip_cd)
                                    .withName("Zip Code")
                                    .withType(InternalDataType.STRING)
                                    .withUniqueMembers(true)
                                    .build()
                            ))
                            .build()

                    )).build())
                    .build(),
                DimensionConnectorMappingImpl.builder()
                	.withOverrideDimensionName("Product")
                    .withForeignKey(prod_id_table7634)
                    .withDimension(StandardDimensionMappingImpl.builder()
                        .withName("Product")
                        .withHierarchies(List.of(
                        ExplicitHierarchyMappingImpl.builder()
                            .withHasAll(true)
                            .withAllMemberName("All Products")
                            .withPrimaryKey(prod_id_prod7631)
                            .withQuery(TableQueryMappingImpl.builder().withTable(prod7631).build())
                            .withLevels(List.of(
                                LevelMappingImpl.builder()
                                    .withColumn(class_prod7631)
                                    .withName("Class")
                                    .withType(InternalDataType.STRING)
                                    .withUniqueMembers(true)
                                    .build(),
                                LevelMappingImpl.builder()
                                    .withColumn(brand_prod7631)
                                    .withName("Brand")
                                    .withType(InternalDataType.STRING)
                                    .withUniqueMembers(true)
                                    .build(),
                                LevelMappingImpl.builder()
                                    .withColumn(item_prod7631)
                                    .withName("Item")
                                    .withType(InternalDataType.STRING)
                                    .withUniqueMembers(true)
                                    .build()
                            ))
                            .build()

                    )).build())
                    .build()
            ))
            .withMeasureGroups(List.of(MeasureGroupMappingImpl.builder().withMeasures(List.of(
                SumMeasureMappingImpl.builder()
                    .withName("First Measure")
                    .withColumn(first_table7634)
                    .withFormatString("#,###")
                    .build(),
                SumMeasureMappingImpl.builder()
                    .withName("Requested Value")
                    .withColumn(request_value_table7634)
                    .withFormatString("#,###")
                    .build(),
                SumMeasureMappingImpl.builder()
                    .withName("Shipped Value")
                    .withColumn(shipped_value_table7634)
                    .withFormatString("#,###")
                    .build()
            )).build()))
            .build());


        return result;
    }
}
