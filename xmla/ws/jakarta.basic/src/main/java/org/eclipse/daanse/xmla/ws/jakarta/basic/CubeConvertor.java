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
package org.eclipse.daanse.xmla.ws.jakarta.basic;

import static org.eclipse.daanse.xmla.ws.jakarta.basic.AnnotationConvertor.convertAnnotationList;
import static org.eclipse.daanse.xmla.ws.jakarta.basic.BindingConvertor.convertBindingList;
import static org.eclipse.daanse.xmla.ws.jakarta.basic.CommandConvertor.convertCommandList;
import static org.eclipse.daanse.xmla.ws.jakarta.basic.ConvertorUtil.convertDuration;
import static org.eclipse.daanse.xmla.ws.jakarta.basic.ConvertorUtil.convertToInstant;
import static org.eclipse.daanse.xmla.ws.jakarta.basic.DataItemConvertor.convertDataItem;
import static org.eclipse.daanse.xmla.ws.jakarta.basic.DataItemConvertor.convertDataItemList;
import static org.eclipse.daanse.xmla.ws.jakarta.basic.TabularBindingConvertor.convertTabularBinding;

import java.util.List;
import java.util.Optional;

import org.eclipse.daanse.xmla.api.engine300.CalculationPropertiesVisualizationProperties;
import org.eclipse.daanse.xmla.api.xmla.AccessEnum;
import org.eclipse.daanse.xmla.api.xmla.Action;
import org.eclipse.daanse.xmla.api.xmla.Aggregation;
import org.eclipse.daanse.xmla.api.xmla.AggregationAttribute;
import org.eclipse.daanse.xmla.api.xmla.AggregationDesign;
import org.eclipse.daanse.xmla.api.xmla.AggregationDesignAttribute;
import org.eclipse.daanse.xmla.api.xmla.AggregationDesignDimension;
import org.eclipse.daanse.xmla.api.xmla.AggregationDimension;
import org.eclipse.daanse.xmla.api.xmla.AggregationInstance;
import org.eclipse.daanse.xmla.api.xmla.AggregationInstanceAttribute;
import org.eclipse.daanse.xmla.api.xmla.AggregationInstanceDimension;
import org.eclipse.daanse.xmla.api.xmla.AggregationInstanceMeasure;
import org.eclipse.daanse.xmla.api.xmla.AttributePermission;
import org.eclipse.daanse.xmla.api.xmla.CalculationProperty;
import org.eclipse.daanse.xmla.api.xmla.CellPermission;
import org.eclipse.daanse.xmla.api.xmla.ColumnBinding;
import org.eclipse.daanse.xmla.api.xmla.Command;
import org.eclipse.daanse.xmla.api.xmla.Cube;
import org.eclipse.daanse.xmla.api.xmla.CubeAttribute;
import org.eclipse.daanse.xmla.api.xmla.CubeDimension;
import org.eclipse.daanse.xmla.api.xmla.CubeDimensionPermission;
import org.eclipse.daanse.xmla.api.xmla.CubeHierarchy;
import org.eclipse.daanse.xmla.api.xmla.CubePermission;
import org.eclipse.daanse.xmla.api.xmla.CubeStorageModeEnumType;
import org.eclipse.daanse.xmla.api.xmla.DataItem;
import org.eclipse.daanse.xmla.api.xmla.DataSourceViewBinding;
import org.eclipse.daanse.xmla.api.xmla.ErrorConfiguration;
import org.eclipse.daanse.xmla.api.xmla.IncrementalProcessingNotification;
import org.eclipse.daanse.xmla.api.xmla.Kpi;
import org.eclipse.daanse.xmla.api.xmla.MdxScript;
import org.eclipse.daanse.xmla.api.xmla.Measure;
import org.eclipse.daanse.xmla.api.xmla.MeasureGroup;
import org.eclipse.daanse.xmla.api.xmla.MeasureGroupBinding;
import org.eclipse.daanse.xmla.api.xmla.MeasureGroupDimension;
import org.eclipse.daanse.xmla.api.xmla.MeasureGroupStorageModeEnumType;
import org.eclipse.daanse.xmla.api.xmla.Partition;
import org.eclipse.daanse.xmla.api.xmla.PartitionCurrentStorageModeEnumType;
import org.eclipse.daanse.xmla.api.xmla.PartitionStorageModeEnumType;
import org.eclipse.daanse.xmla.api.xmla.PersistenceEnum;
import org.eclipse.daanse.xmla.api.xmla.Perspective;
import org.eclipse.daanse.xmla.api.xmla.PerspectiveAction;
import org.eclipse.daanse.xmla.api.xmla.PerspectiveAttribute;
import org.eclipse.daanse.xmla.api.xmla.PerspectiveCalculation;
import org.eclipse.daanse.xmla.api.xmla.PerspectiveDimension;
import org.eclipse.daanse.xmla.api.xmla.PerspectiveHierarchy;
import org.eclipse.daanse.xmla.api.xmla.PerspectiveKpi;
import org.eclipse.daanse.xmla.api.xmla.PerspectiveMeasure;
import org.eclipse.daanse.xmla.api.xmla.PerspectiveMeasureGroup;
import org.eclipse.daanse.xmla.api.xmla.ProactiveCaching;
import org.eclipse.daanse.xmla.api.xmla.ProactiveCachingBinding;
import org.eclipse.daanse.xmla.api.xmla.QueryNotification;
import org.eclipse.daanse.xmla.api.xmla.ReadDefinitionEnum;
import org.eclipse.daanse.xmla.api.xmla.ReadWritePermissionEnum;
import org.eclipse.daanse.xmla.api.xmla.RefreshPolicyEnum;
import org.eclipse.daanse.xmla.api.xmla.ReportFormatParameter;
import org.eclipse.daanse.xmla.api.xmla.ReportParameter;
import org.eclipse.daanse.xmla.api.xmla.TargetTypeEnum;
import org.eclipse.daanse.xmla.api.xmla.Translation;
import org.eclipse.daanse.xmla.api.xmla.TypeEnum;
import org.eclipse.daanse.xmla.model.record.engine300.CalculationPropertiesVisualizationPropertiesR;
import org.eclipse.daanse.xmla.model.record.xmla.AggregationAttributeR;
import org.eclipse.daanse.xmla.model.record.xmla.AggregationDesignAttributeR;
import org.eclipse.daanse.xmla.model.record.xmla.AggregationDesignDimensionR;
import org.eclipse.daanse.xmla.model.record.xmla.AggregationDesignR;
import org.eclipse.daanse.xmla.model.record.xmla.AggregationDimensionR;
import org.eclipse.daanse.xmla.model.record.xmla.AggregationInstanceAttributeR;
import org.eclipse.daanse.xmla.model.record.xmla.AggregationInstanceDimensionR;
import org.eclipse.daanse.xmla.model.record.xmla.AggregationInstanceMeasureR;
import org.eclipse.daanse.xmla.model.record.xmla.AggregationInstanceR;
import org.eclipse.daanse.xmla.model.record.xmla.AggregationR;
import org.eclipse.daanse.xmla.model.record.xmla.AttributePermissionR;
import org.eclipse.daanse.xmla.model.record.xmla.CalculationPropertyR;
import org.eclipse.daanse.xmla.model.record.xmla.CellPermissionR;
import org.eclipse.daanse.xmla.model.record.xmla.ColumnBindingR;
import org.eclipse.daanse.xmla.model.record.xmla.CubeAttributeR;
import org.eclipse.daanse.xmla.model.record.xmla.CubeDimensionPermissionR;
import org.eclipse.daanse.xmla.model.record.xmla.CubeDimensionR;
import org.eclipse.daanse.xmla.model.record.xmla.CubeHierarchyR;
import org.eclipse.daanse.xmla.model.record.xmla.CubePermissionR;
import org.eclipse.daanse.xmla.model.record.xmla.CubeR;
import org.eclipse.daanse.xmla.model.record.xmla.DataSourceViewBindingR;
import org.eclipse.daanse.xmla.model.record.xmla.DrillThroughActionR;
import org.eclipse.daanse.xmla.model.record.xmla.ErrorConfigurationR;
import org.eclipse.daanse.xmla.model.record.xmla.IncrementalProcessingNotificationR;
import org.eclipse.daanse.xmla.model.record.xmla.KpiR;
import org.eclipse.daanse.xmla.model.record.xmla.MdxScriptR;
import org.eclipse.daanse.xmla.model.record.xmla.MeasureGroupBindingR;
import org.eclipse.daanse.xmla.model.record.xmla.MeasureGroupR;
import org.eclipse.daanse.xmla.model.record.xmla.MeasureR;
import org.eclipse.daanse.xmla.model.record.xmla.PartitionR;
import org.eclipse.daanse.xmla.model.record.xmla.PerspectiveActionR;
import org.eclipse.daanse.xmla.model.record.xmla.PerspectiveAttributeR;
import org.eclipse.daanse.xmla.model.record.xmla.PerspectiveCalculationR;
import org.eclipse.daanse.xmla.model.record.xmla.PerspectiveDimensionR;
import org.eclipse.daanse.xmla.model.record.xmla.PerspectiveHierarchyR;
import org.eclipse.daanse.xmla.model.record.xmla.PerspectiveKpiR;
import org.eclipse.daanse.xmla.model.record.xmla.PerspectiveMeasureGroupR;
import org.eclipse.daanse.xmla.model.record.xmla.PerspectiveMeasureR;
import org.eclipse.daanse.xmla.model.record.xmla.PerspectiveR;
import org.eclipse.daanse.xmla.model.record.xmla.ProactiveCachingIncrementalProcessingBindingR;
import org.eclipse.daanse.xmla.model.record.xmla.ProactiveCachingQueryBindingR;
import org.eclipse.daanse.xmla.model.record.xmla.ProactiveCachingR;
import org.eclipse.daanse.xmla.model.record.xmla.QueryNotificationR;
import org.eclipse.daanse.xmla.model.record.xmla.ReportActionR;
import org.eclipse.daanse.xmla.model.record.xmla.ReportFormatParameterR;
import org.eclipse.daanse.xmla.model.record.xmla.ReportParameterR;
import org.eclipse.daanse.xmla.model.record.xmla.StandardActionR;
import org.eclipse.daanse.xmla.model.record.xmla.TranslationR;

public class CubeConvertor {

	private CubeConvertor() {
	}

	public static Cube convertCube(org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Cube cube) {
		if (cube != null) {
			return new CubeR(cube.getName(), cube.getID(), convertToInstant(cube.getCreatedTimestamp()),
					convertToInstant(cube.getLastSchemaUpdate()), cube.getDescription(),
					convertAnnotationList(cube.getAnnotations() == null ? null : cube.getAnnotations().getAnnotation()),
					cube.getLanguage(), cube.getCollation(), convertCubeTranslations(cube.getTranslations()),
					convertCubeDimensions(cube.getDimensions()), convertCubeCubePermissions(cube.getCubePermissions()),
					convertCubeMdxScripts(cube.getMdxScripts()), convertCubePerspectives(cube.getPerspectives()),
					cube.getState(), cube.getDefaultMeasure(), cube.isVisible(),
					convertCubeMeasureGroups(cube.getMeasureGroups()), convertDataSourceViewBinding(cube.getSource()),
					cube.getAggregationPrefix(), cube.getProcessingPriority(),
					convertCubeStorageMode(cube.getStorageMode()), cube.getProcessingMode(),
					cube.getScriptCacheProcessingMode(), cube.getScriptErrorHandlingMode(),
					cube.getDaxOptimizationMode(), convertProactiveCaching(cube.getProactiveCaching()),
					convertCubeKpis(cube.getKpis()), convertErrorConfiguration(cube.getErrorConfiguration()),
					convertCubeActions(cube.getActions()), cube.getStorageLocation(), cube.getEstimatedRows(),
					convertToInstant(cube.getLastProcessed()));
		}
		return null;
	}

	private static DataSourceViewBinding convertDataSourceViewBinding(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.DataSourceViewBinding source) {
		if (source != null) {
			return new DataSourceViewBindingR(source.getDataSourceViewID());
		}
		return null;
	}

	private static List<MeasureGroup> convertCubeMeasureGroups(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Cube.MeasureGroups measureGroups) {
		if (measureGroups != null) {
			return convertMeasureGroupList(measureGroups.getMeasureGroup());
		}
		return List.of();
	}

	private static List<MeasureGroup> convertMeasureGroupList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.MeasureGroup> measureGroup) {
		if (measureGroup != null) {
			return measureGroup.stream().map(CubeConvertor::convertMeasureGroup).toList();
		}
		return List.of();
	}

	public static MeasureGroup convertMeasureGroup(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.MeasureGroup measureGroup) {
		if (measureGroup != null) {
			return new MeasureGroupR(measureGroup.getName(), measureGroup.getID(),
					convertToInstant(measureGroup.getCreatedTimestamp()),
					convertToInstant(measureGroup.getLastSchemaUpdate()), measureGroup.getDescription(),
					convertAnnotationList(measureGroup.getAnnotations() == null ? null
							: measureGroup.getAnnotations().getAnnotation()),
					convertToInstant(measureGroup.getLastProcessed()),
					convertMeasureGroupTranslations(measureGroup.getTranslations()), measureGroup.getType(),
					measureGroup.getState(), convertMeasureGroupMeasures(measureGroup.getMeasures()),
					measureGroup.getDataAggregation(), convertMeasureGroupBinding(measureGroup.getSource()),
					convertMeasureGroupStorageMode(measureGroup.getStorageMode()), measureGroup.getStorageLocation(),
					measureGroup.isIgnoreUnrelatedDimensions(),
					convertProactiveCaching(measureGroup.getProactiveCaching()), measureGroup.getEstimatedRows(),
					convertErrorConfiguration(measureGroup.getErrorConfiguration()), measureGroup.getEstimatedSize(),
					measureGroup.getProcessingMode(), convertMeasureGroupDimensions(measureGroup.getDimensions()),
					convertMeasureGroupPartitions(measureGroup.getPartitions()), measureGroup.getAggregationPrefix(),
					measureGroup.getProcessingPriority(),
					convertMeasureGroupAggregationDesigns(measureGroup.getAggregationDesigns()));
		}
		return null;
	}

	public static MeasureGroupBinding convertMeasureGroupBinding(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.MeasureGroupBinding source) {
		if (source != null) {
			return new MeasureGroupBindingR(source.getDataSourceID(), source.getCubeID(), source.getMeasureGroupID(),
					Optional.ofNullable(PersistenceEnum.fromValue(source.getPersistence())),
					Optional.ofNullable(RefreshPolicyEnum.fromValue(source.getRefreshPolicy())),
					Optional.ofNullable(convertDuration(source.getRefreshInterval())),
					Optional.ofNullable(source.getFilter()));
		}
		return null;
	}

	private static List<Partition> convertMeasureGroupPartitions(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.MeasureGroup.Partitions partitions) {
		if (partitions != null) {
			return convertPartitionList(partitions.getPartition());
		}
		return List.of();
	}

	private static List<Partition> convertPartitionList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Partition> partitionList) {
		if (partitionList != null) {
			return partitionList.stream().map(CubeConvertor::convertPartition).toList();
		}
		return List.of();
	}

	public static Partition convertPartition(org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Partition partition) {
		if (partition != null) {
			return new PartitionR(partition.getName(), partition.getID(),
					convertToInstant(partition.getCreatedTimestamp()),
					convertToInstant(partition.getLastSchemaUpdate()), partition.getDescription(),
					convertAnnotationList(
							partition.getAnnotations() == null ? null : partition.getAnnotations().getAnnotation()),
					convertTabularBinding(partition.getSource()), partition.getProcessingPriority(),
					partition.getAggregationPrefix(), convertPartitionStorageMode(partition.getStorageMode()),
					partition.getProcessingMode(), convertErrorConfiguration(partition.getErrorConfiguration()),
					partition.getStorageLocation(), partition.getRemoteDatasourceID(), partition.getSlice(),
					convertProactiveCaching(partition.getProactiveCaching()), partition.getType(),
					partition.getEstimatedSize(), partition.getEstimatedRows(),
					convertPartitionCurrentStorageMode(partition.getCurrentStorageMode()),
					partition.getAggregationDesignID(),
					convertPartitionAggregationInstances(partition.getAggregationInstances()),
					convertDataSourceViewBinding(partition.getAggregationInstanceSource()),
					convertToInstant(partition.getLastProcessed()), partition.getState(),
					partition.getStringStoresCompatibilityLevel(), partition.getCurrentStringStoresCompatibilityLevel(),
					partition.getDirectQueryUsage());
		}
		return null;
	}

	private static List<AggregationInstance> convertPartitionAggregationInstances(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Partition.AggregationInstances aggregationInstances) {
		if (aggregationInstances != null) {
			return convertAggregationInstanceList(aggregationInstances.getAggregationInstance());
		}
		return List.of();
	}

	private static List<AggregationInstance> convertAggregationInstanceList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationInstance> aggregationInstanceList) {
		if (aggregationInstanceList != null) {
			return aggregationInstanceList.stream().map(CubeConvertor::convertAggregationInstance).toList();
		}
		return List.of();
	}

	private static AggregationInstance convertAggregationInstance(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationInstance aggregationInstance) {
		if (aggregationInstance != null) {
			return new AggregationInstanceR(aggregationInstance.getID(), aggregationInstance.getName(),
					aggregationInstance.getAggregationType(), convertTabularBinding(aggregationInstance.getSource()),
					convertAggregationInstanceDimensions(aggregationInstance.getDimensions()),
					convertAggregationInstanceMeasures(aggregationInstance.getMeasures()),
					convertAnnotationList(aggregationInstance.getAnnotations() == null ? null
							: aggregationInstance.getAnnotations().getAnnotation()),
					aggregationInstance.getDescription());
		}
		return null;
	}

	private static List<AggregationInstanceMeasure> convertAggregationInstanceMeasures(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationInstance.Measures measures) {
		if (measures != null) {
			return convertAggregationInstanceMeasureList(measures.getMeasure());
		}
		return List.of();
	}

	private static List<AggregationInstanceMeasure> convertAggregationInstanceMeasureList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationInstanceMeasure> measureList) {
		if (measureList != null) {
			return measureList.stream().map(CubeConvertor::convertAggregationInstanceMeasure).toList();
		}
		return List.of();
	}

	private static AggregationInstanceMeasure convertAggregationInstanceMeasure(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationInstanceMeasure aggregationInstanceMeasure) {
		if (aggregationInstanceMeasure != null) {
			return new AggregationInstanceMeasureR(aggregationInstanceMeasure.getMeasureID(),
					convertColumnBinding(aggregationInstanceMeasure.getSource()));
		}
		return null;
	}

	private static ColumnBinding convertColumnBinding(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.ColumnBinding source) {
		if (source != null) {
			return new ColumnBindingR(source.getTableID(), source.getColumnID());
		}
		return null;
	}

	private static List<AggregationInstanceDimension> convertAggregationInstanceDimensions(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationInstance.Dimensions dimensions) {
		if (dimensions != null) {
			return convertAggregationInstanceDimensionList(dimensions.getDimension());
		}
		return List.of();
	}

	private static List<AggregationInstanceDimension> convertAggregationInstanceDimensionList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationInstanceDimension> dimensionList) {
		if (dimensionList != null) {
			return dimensionList.stream().map(CubeConvertor::convertAggregationInstanceDimension).toList();
		}
		return List.of();
	}

	private static AggregationInstanceDimension convertAggregationInstanceDimension(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationInstanceDimension aggregationInstanceDimension) {
		if (aggregationInstanceDimension != null) {
			return new AggregationInstanceDimensionR(aggregationInstanceDimension.getCubeDimensionID(),
					Optional.ofNullable(convertAggregationInstanceDimensionAttributes(
							aggregationInstanceDimension.getAttributes())));
		}
		return null;
	}

	private static List<AggregationInstanceAttribute> convertAggregationInstanceDimensionAttributes(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationInstanceDimension.Attributes attributes) {
		if (attributes != null) {
			return convertAggregationInstanceAttributeList(attributes.getAttribute());
		}
		return List.of();
	}

	private static List<AggregationInstanceAttribute> convertAggregationInstanceAttributeList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationInstanceAttribute> attributeList) {
		if (attributeList != null) {
			return attributeList.stream().map(CubeConvertor::convertAggregationInstanceAttribute).toList();
		}
		return List.of();
	}

	private static AggregationInstanceAttribute convertAggregationInstanceAttribute(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationInstanceAttribute aggregationInstanceAttribute) {
		if (aggregationInstanceAttribute != null) {
			return new AggregationInstanceAttributeR(aggregationInstanceAttribute.getAttributeID(), Optional.ofNullable(
					convertAggregationInstanceAttributeKeyColumns(aggregationInstanceAttribute.getKeyColumns())));
		}
		return null;
	}

	private static List<DataItem> convertAggregationInstanceAttributeKeyColumns(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationInstanceAttribute.KeyColumns keyColumns) {
		if (keyColumns != null) {
			return convertDataItemList(keyColumns.getKeyColumn());
		}
		return List.of();
	}

	private static Partition.CurrentStorageMode convertPartitionCurrentStorageMode(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Partition.CurrentStorageMode currentStorageMode) {
		if (currentStorageMode != null) {
			return new PartitionR.CurrentStorageMode(
					PartitionCurrentStorageModeEnumType.fromValue(currentStorageMode.getValue().value()),
					currentStorageMode.getValuens());
		}
		return null;
	}

	private static Partition.StorageMode convertPartitionStorageMode(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Partition.StorageMode storageMode) {
		if (storageMode != null) {
			return new PartitionR.StorageMode(PartitionStorageModeEnumType.fromValue(storageMode.getValue().value()),
					storageMode.getValuens());
		}
		return null;
	}

	private static List<MeasureGroupDimension> convertMeasureGroupDimensions(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.MeasureGroup.Dimensions dimensions) {
		if (dimensions != null) {
			return convertMeasureGroupDimensionList(dimensions.getDimension());
		}
		return List.of();
	}

	private static List<MeasureGroupDimension> convertMeasureGroupDimensionList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.MeasureGroupDimension> dimensionList) {
		if (dimensionList != null) {
			return dimensionList.stream().map(MeasureGroupDimensionConvertor::convertMeasureGroupDimension).toList();
		}
		return List.of();
	}

	private static MeasureGroup.StorageMode convertMeasureGroupStorageMode(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.MeasureGroup.StorageMode storageMode) {
		if (storageMode != null) {
			return new MeasureGroupR.StorageMode(
					MeasureGroupStorageModeEnumType.fromValue(storageMode.getValue().value()),
					storageMode.getValuens());
		}
		return null;

	}

	private static List<Measure> convertMeasureGroupMeasures(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.MeasureGroup.Measures measures) {
		if (measures != null) {
			return convertMeasureList(measures.getMeasure());
		}
		return List.of();
	}

	private static List<Measure> convertMeasureList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Measure> measureList) {
		if (measureList != null) {
			return measureList.stream().map(CubeConvertor::convertMeasure).toList();
		}
		return List.of();
	}

	private static Measure convertMeasure(org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Measure measure) {
		if (measure != null) {
			return new MeasureR(measure.getName(), measure.getID(), measure.getDescription(),
					measure.getAggregateFunction(), measure.getDataType(), convertDataItem(measure.getSource()),
					measure.isVisible(), measure.getMeasureExpression(), measure.getDisplayFolder(),
					measure.getFormatString(), measure.getBackColor(), measure.getForeColor(), measure.getFontName(),
					measure.getFontSize(), measure.getFontFlags(),
					convertMeasureTranslations(measure.getTranslations()), convertAnnotationList(
							measure.getAnnotations() == null ? null : measure.getAnnotations().getAnnotation()));
		}
		return null;
	}

	private static List<Translation> convertMeasureTranslations(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Measure.Translations translations) {
		if (translations != null) {
			return convertTranslationList(translations.getTranslation());
		}
		return List.of();
	}

	private static List<AggregationDesign> convertMeasureGroupAggregationDesigns(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.MeasureGroup.AggregationDesigns aggregationDesigns) {
		if (aggregationDesigns != null) {
			return convertaggregationDesignList(aggregationDesigns.getAggregationDesign());
		}
		return List.of();
	}

	private static List<AggregationDesign> convertaggregationDesignList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationDesign> aggregationDesignList) {
		if (aggregationDesignList != null) {
			return aggregationDesignList.stream().map(CubeConvertor::convertAggregationDesign).toList();
		}
		return List.of();
	}

	private static AggregationDesign convertAggregationDesign(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationDesign aggregationDesign) {
		if (aggregationDesign != null) {
			return new AggregationDesignR(aggregationDesign.getName(), Optional.ofNullable(aggregationDesign.getID()),
					Optional.ofNullable(convertToInstant(aggregationDesign.getCreatedTimestamp())),
					Optional.ofNullable(convertToInstant(aggregationDesign.getLastSchemaUpdate())),
					Optional.ofNullable(aggregationDesign.getDescription()),
					Optional.ofNullable(convertAnnotationList(aggregationDesign.getAnnotations() == null ? null
							: aggregationDesign.getAnnotations().getAnnotation())),
					Optional.ofNullable(aggregationDesign.getEstimatedRows()),
					Optional.ofNullable(convertAggregationDesignDimensions(aggregationDesign.getDimensions())),
					Optional.ofNullable(convertAggregationDesignAggregations(aggregationDesign.getAggregations())),
					Optional.ofNullable(aggregationDesign.getEstimatedPerformanceGain()));
		}
		return null;
	}

	private static List<Aggregation> convertAggregationDesignAggregations(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationDesign.Aggregations aggregations) {
		if (aggregations != null) {
			return convertAggregationList(aggregations.getAggregation());
		}
		return List.of();
	}

	private static List<Aggregation> convertAggregationList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Aggregation> aggregationList) {
		if (aggregationList != null) {
			return aggregationList.stream().map(CubeConvertor::convertAggregation).toList();
		}
		return List.of();
	}

	private static Aggregation convertAggregation(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Aggregation aggregation) {
		if (aggregation != null) {
			return new AggregationR(Optional.ofNullable(aggregation.getID()), aggregation.getName(),
					Optional.ofNullable(convertAggregationDimensions(aggregation.getDimensions())),
					Optional.ofNullable(convertAnnotationList(aggregation.getAnnotations() == null ? null
							: aggregation.getAnnotations().getAnnotation())),
					Optional.ofNullable(aggregation.getDescription()));
		}
		return null;
	}

	private static List<AggregationDimension> convertAggregationDimensions(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Aggregation.Dimensions dimensions) {
		if (dimensions != null) {
			return convertAggregationDimensionList(dimensions.getDimension());
		}
		return List.of();
	}

	private static List<AggregationDimension> convertAggregationDimensionList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationDimension> dimensionList) {
		if (dimensionList != null) {
			return dimensionList.stream().map(CubeConvertor::convertAggregationDimension).toList();
		}
		return List.of();
	}

	private static AggregationDimension convertAggregationDimension(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationDimension aggregationDimension) {
		if (aggregationDimension != null) {
			return new AggregationDimensionR(aggregationDimension.getCubeDimensionID(),
					Optional.ofNullable(convertAggregationDimensionAttributes(aggregationDimension.getAttributes())),
					Optional.ofNullable(convertAnnotationList(aggregationDimension.getAnnotations() == null ? null
							: aggregationDimension.getAnnotations().getAnnotation())));
		}
		return null;
	}

	private static List<AggregationAttribute> convertAggregationDimensionAttributes(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationDimension.Attributes attributes) {
		if (attributes != null) {
			return convertAggregationAttributList(attributes.getAttribute());
		}
		return List.of();
	}

	private static List<AggregationAttribute> convertAggregationAttributList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationAttribute> attributeList) {
		if (attributeList != null) {
			return attributeList.stream().map(CubeConvertor::convertAggregationAttribute).toList();
		}
		return List.of();
	}

	private static AggregationAttribute convertAggregationAttribute(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationAttribute aggregationAttribute) {
		if (aggregationAttribute != null) {
			return new AggregationAttributeR(aggregationAttribute.getAttributeID(),
					Optional.ofNullable(convertAnnotationList(aggregationAttribute.getAnnotations() == null ? null
							: aggregationAttribute.getAnnotations().getAnnotation())));
		}
		return null;
	}

	private static List<AggregationDesignDimension> convertAggregationDesignDimensions(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationDesign.Dimensions dimensions) {
		if (dimensions != null) {
			return convertAggregationDesignDimensionList(dimensions.getDimension());
		}
		return List.of();
	}

	private static List<AggregationDesignDimension> convertAggregationDesignDimensionList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationDesignDimension> dimension) {
		if (dimension != null) {
			return dimension.stream().map(CubeConvertor::convertAggregationDesignDimension).toList();
		}
		return List.of();
	}

	private static AggregationDesignDimension convertAggregationDesignDimension(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationDesignDimension aggregationDesignDimension) {
		if (aggregationDesignDimension != null) {
			return new AggregationDesignDimensionR(aggregationDesignDimension.getCubeDimensionID(),
					Optional.ofNullable(
							convertAggregationDesignDimensionAttributes(aggregationDesignDimension.getAttributes())),
					Optional.ofNullable(convertAnnotationList(aggregationDesignDimension.getAnnotations() == null ? null
							: aggregationDesignDimension.getAnnotations().getAnnotation())));
		}
		return null;
	}

	private static List<AggregationDesignAttribute> convertAggregationDesignDimensionAttributes(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationDesignDimension.Attributes attributes) {
		if (attributes != null) {
			return convertAggregationDesignAttributeList(attributes.getAttribute());
		}
		return List.of();
	}

	private static List<AggregationDesignAttribute> convertAggregationDesignAttributeList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationDesignAttribute> attributeList) {
		if (attributeList != null) {
			return attributeList.stream().map(CubeConvertor::convertAggregationDesignAttribute).toList();
		}
		return List.of();
	}

	private static AggregationDesignAttribute convertAggregationDesignAttribute(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AggregationDesignAttribute aggregationDesignAttribute) {
		if (aggregationDesignAttribute != null) {
			return new AggregationDesignAttributeR(aggregationDesignAttribute.getAttributeID(),
					Optional.ofNullable(aggregationDesignAttribute.getEstimatedCount()));
		}
		return null;
	}

	private static List<Translation> convertMeasureGroupTranslations(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.MeasureGroup.Translations translations) {
		if (translations != null) {
			return convertTranslationList(translations.getTranslation());
		}
		return List.of();
	}

	private static Cube.StorageMode convertCubeStorageMode(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Cube.StorageMode storageMode) {
		if (storageMode != null) {
			return new CubeR.StorageMode(CubeStorageModeEnumType.fromValue(storageMode.getValue().value()),
					storageMode.getValuens());
		}
		return null;
	}

	public static ProactiveCaching convertProactiveCaching(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.ProactiveCaching proactiveCaching) {
		if (proactiveCaching != null) {
			return new ProactiveCachingR(Optional.ofNullable(proactiveCaching.getOnlineMode()),
					Optional.ofNullable(proactiveCaching.getAggregationStorage()),
					Optional.ofNullable(convertProactiveCachingBinding(proactiveCaching.getSource())),
					Optional.ofNullable(proactiveCaching.getSilenceInterval()),
					Optional.ofNullable(proactiveCaching.getLatency()),
					Optional.ofNullable(proactiveCaching.getSilenceOverrideInterval()),
					Optional.ofNullable(proactiveCaching.getForceRebuildInterval()),
					Optional.ofNullable(proactiveCaching.getEnabled()));
		}
		return null;
	}

	private static ProactiveCachingBinding convertProactiveCachingBinding(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.ProactiveCachingBinding source) {
		if (source != null) {
			if (source instanceof org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.ProactiveCachingQueryBinding proactiveCachingQueryBinding) {

				return new ProactiveCachingQueryBindingR(
						Optional.ofNullable(convertDuration(proactiveCachingQueryBinding.getRefreshInterval())),
						convertProactiveCachingQueryBindingQueryNotifications(
								proactiveCachingQueryBinding.getQueryNotifications()));
			}
			if (source instanceof org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.ProactiveCachingIncrementalProcessingBinding proactiveCachingIncrementalProcessingBinding) {

				return new ProactiveCachingIncrementalProcessingBindingR(
						Optional.ofNullable(
								convertDuration(proactiveCachingIncrementalProcessingBinding.getRefreshInterval())),
						convertProactiveCachingIncrementalProcessingBindingIncrementalProcessingNotifications(
								proactiveCachingIncrementalProcessingBinding.getIncrementalProcessingNotifications()));
			}
		}
		return null;

	}

	private static List<IncrementalProcessingNotification> convertProactiveCachingIncrementalProcessingBindingIncrementalProcessingNotifications(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.ProactiveCachingIncrementalProcessingBinding.IncrementalProcessingNotifications incrementalProcessingNotifications) {
		if (incrementalProcessingNotifications != null) {
			return convertIncrementalProcessingNotificationList(
					incrementalProcessingNotifications.getIncrementalProcessingNotification());
		}
		return List.of();
	}

	private static List<IncrementalProcessingNotification> convertIncrementalProcessingNotificationList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.IncrementalProcessingNotification> incrementalProcessingNotificationList) {
		if (incrementalProcessingNotificationList != null) {
			return incrementalProcessingNotificationList.stream()
					.map(CubeConvertor::convertIncrementalProcessingNotification).toList();
		}
		return List.of();
	}

	private static IncrementalProcessingNotification convertIncrementalProcessingNotification(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.IncrementalProcessingNotification incrementalProcessingNotification) {
		if (incrementalProcessingNotification != null) {
			return new IncrementalProcessingNotificationR(incrementalProcessingNotification.getTableID(),
					incrementalProcessingNotification.getProcessingQuery());
		}
		return null;
	}

	private static List<QueryNotification> convertProactiveCachingQueryBindingQueryNotifications(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.ProactiveCachingQueryBinding.QueryNotifications queryNotifications) {
		if (queryNotifications != null) {
			return convertQueryNotificationList(queryNotifications.getQueryNotification());
		}
		return List.of();
	}

	private static List<QueryNotification> convertQueryNotificationList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.QueryNotification> queryNotificationList) {
		if (queryNotificationList != null) {
			return queryNotificationList.stream().map(CubeConvertor::convertQueryNotification).toList();
		}
		return List.of();
	}

	private static QueryNotification convertQueryNotification(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.QueryNotification queryNotification) {
		if (queryNotification != null) {
			return new QueryNotificationR(Optional.ofNullable(queryNotification.getQuery()));
		}
		return null;
	}

	private static List<Kpi> convertCubeKpis(org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Cube.Kpis kpis) {
		if (kpis != null) {
			return convertKpiList(kpis.getKpi());
		}
		return List.of();
	}

	private static List<Kpi> convertKpiList(List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Kpi> kpiList) {
		if (kpiList != null) {
			return kpiList.stream().map(CubeConvertor::convertKpi).toList();
		}
		return List.of();
	}

	private static Kpi convertKpi(org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Kpi kpi) {
		if (kpi != null) {
			return new KpiR(kpi.getName(), kpi.getID(), kpi.getDescription(),
					convertKpiTranslations(kpi.getTranslations()), kpi.getDisplayFolder(),
					kpi.getAssociatedMeasureGroupID(), kpi.getValue(), kpi.getGoal(), kpi.getStatus(), kpi.getTrend(),
					kpi.getWeight(), kpi.getTrendGraphic(), kpi.getStatusGraphic(), kpi.getCurrentTimeMember(),
					kpi.getParentKpiID(),
					convertAnnotationList(kpi.getAnnotations() == null ? null : kpi.getAnnotations().getAnnotation()));
		}
		return null;
	}

	private static List<Translation> convertKpiTranslations(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Kpi.Translations translations) {
		if (translations != null) {
			return convertTranslationList(translations.getTranslation());
		}
		return List.of();
	}

	private static ErrorConfiguration convertErrorConfiguration(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.ErrorConfiguration errorConfiguration) {
		if (errorConfiguration != null) {
			return new ErrorConfigurationR(Optional.ofNullable(errorConfiguration.getKeyErrorLimit()),
					Optional.ofNullable(errorConfiguration.getKeyErrorLogFile()),
					Optional.ofNullable(errorConfiguration.getKeyErrorAction()),
					Optional.ofNullable(errorConfiguration.getKeyErrorLimitAction()),
					Optional.ofNullable(errorConfiguration.getKeyNotFound()),
					Optional.ofNullable(errorConfiguration.getKeyDuplicate()),
					Optional.ofNullable(errorConfiguration.getNullKeyConvertedToUnknown()),
					Optional.ofNullable(errorConfiguration.getNullKeyNotAllowed()),
					Optional.ofNullable(errorConfiguration.getCalculationError()));
		}
		return null;

	}

	private static List<Action> convertCubeActions(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Cube.Actions actions) {
		if (actions != null) {
			return convertActionList(actions.getAction());
		}
		return List.of();
	}

	private static List<Action> convertActionList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Action> actionList) {
		if (actionList != null) {
			return actionList.stream().map(CubeConvertor::convertAction).toList();
		}
		return List.of();
	}

	private static Action convertAction(org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Action action) {
		if (action != null) {
			if (action instanceof org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.StandardAction standardAction) {

				return new StandardActionR(standardAction.getName(), Optional.ofNullable(standardAction.getID()),
						Optional.ofNullable(standardAction.getCaption()),
						Optional.ofNullable(standardAction.isCaptionIsMdx()),
						Optional.ofNullable(convertTranslationList(standardAction.getTranslations() == null ? null
								: standardAction.getTranslations().getTranslation())),
						TargetTypeEnum.fromValue(standardAction.getTargetType()),
						Optional.ofNullable(standardAction.getTarget()),
						Optional.ofNullable(standardAction.getCondition()),
						TypeEnum.fromValue(standardAction.getType()),
						Optional.ofNullable(standardAction.getInvocation()),
						Optional.ofNullable(standardAction.getApplication()),
						Optional.ofNullable(standardAction.getDescription()),
						Optional.ofNullable(convertAnnotationList(standardAction.getAnnotations() == null ? null
								: standardAction.getAnnotations().getAnnotation())),
						standardAction.getExpression());
			}
			if (action instanceof org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.ReportAction reportAction) {

				return new ReportActionR(reportAction.getName(), Optional.ofNullable(reportAction.getID()),
						Optional.ofNullable(reportAction.getCaption()),
						Optional.ofNullable(reportAction.isCaptionIsMdx()),
						Optional.ofNullable(convertTranslationList(reportAction.getTranslations() == null ? null
								: reportAction.getTranslations().getTranslation())),
						TargetTypeEnum.fromValue(reportAction.getTargetType()),
						Optional.ofNullable(reportAction.getTarget()), Optional.ofNullable(reportAction.getCondition()),
						TypeEnum.fromValue(reportAction.getType()), Optional.ofNullable(reportAction.getInvocation()),
						Optional.ofNullable(reportAction.getApplication()),
						Optional.ofNullable(reportAction.getDescription()),
						Optional.ofNullable(convertAnnotationList(reportAction.getAnnotations() == null ? null
								: reportAction.getAnnotations().getAnnotation())),
						reportAction.getReportServer(), Optional.ofNullable(reportAction.getPath()),
						Optional.ofNullable(
								convertReportParametersList(reportAction.getReportParameters() == null ? null
										: reportAction.getReportParameters().getReportParameter())),
						Optional.ofNullable(
								convertReportFormatParameterList(reportAction.getReportFormatParameters() == null ? null
										: reportAction.getReportFormatParameters().getReportFormatParameter())));
			}
			if (action instanceof org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.DrillThroughAction drillThroughAction) {

				return new DrillThroughActionR(drillThroughAction.getName(),
						Optional.ofNullable(drillThroughAction.getID()),
						Optional.ofNullable(drillThroughAction.getCaption()),
						Optional.ofNullable(drillThroughAction.isCaptionIsMdx()),
						Optional.ofNullable(convertTranslationList(drillThroughAction.getTranslations() == null ? null
								: drillThroughAction.getTranslations().getTranslation())),
						TargetTypeEnum.fromValue(drillThroughAction.getTargetType()),
						Optional.ofNullable(drillThroughAction.getTarget()),
						Optional.ofNullable(drillThroughAction.getCondition()),
						TypeEnum.fromValue(drillThroughAction.getType()),
						Optional.ofNullable(drillThroughAction.getInvocation()),
						Optional.ofNullable(drillThroughAction.getApplication()),
						Optional.ofNullable(drillThroughAction.getDescription()),
						Optional.ofNullable(convertAnnotationList(drillThroughAction.getAnnotations() == null ? null
								: drillThroughAction.getAnnotations().getAnnotation())),
						Optional.ofNullable(drillThroughAction.isDefault()),
						Optional.ofNullable(convertBindingList(drillThroughAction.getColumns().getColumn())),
						Optional.ofNullable(drillThroughAction.getMaximumRows()));
			}
		}
		return null;
	}

	private static List<ReportFormatParameter> convertReportFormatParameterList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.ReportFormatParameter> list) {
		if (list != null) {
			return list.stream().map(CubeConvertor::convertReportFormatParameter).toList();
		}
		return List.of();
	}

	private static ReportFormatParameter convertReportFormatParameter(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.ReportFormatParameter reportFormatParameter) {
		if (reportFormatParameter != null) {
			return new ReportFormatParameterR(reportFormatParameter.getName(), reportFormatParameter.getValue());
		}
		return null;
	}

	private static List<ReportParameter> convertReportParametersList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.ReportParameter> list) {
		if (list != null) {
			return list.stream().map(CubeConvertor::convertReportParameter).toList();
		}
		return List.of();
	}

	private static ReportParameter convertReportParameter(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.ReportParameter reportParameter) {
		if (reportParameter != null) {
			return new ReportParameterR(reportParameter.getName(), reportParameter.getValue());
		}
		return null;
	}

	private static List<Perspective> convertCubePerspectives(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Cube.Perspectives perspectives) {
		if (perspectives != null) {
			return convertPerspectiveList(perspectives.getPerspective());
		}
		return List.of();
	}

	private static List<Perspective> convertPerspectiveList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Perspective> perspectiveList) {
		if (perspectiveList != null) {
			return perspectiveList.stream().map(CubeConvertor::convertPerspective).toList();
		}
		return List.of();

	}

	public static Perspective convertPerspective(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Perspective perspective) {
		if (perspective != null) {
			return new PerspectiveR(perspective.getName(), perspective.getID(),
					convertToInstant(perspective.getCreatedTimestamp()),
					convertToInstant(perspective.getLastSchemaUpdate()), perspective.getDescription(),
					convertAnnotationList(
							perspective.getAnnotations() == null ? null : perspective.getAnnotations().getAnnotation()),
					convertPerspectiveTranslations(perspective.getTranslations()), perspective.getDefaultMeasure(),
					convertPerspectiveDimensions(perspective.getDimensions()),
					convertPerspectiveMeasureGroups(perspective.getMeasureGroups()),
					convertPerspectiveCalculations(perspective.getCalculations()),
					convertPerspectiveKpis(perspective.getKpis()), convertPerspectiveActions(perspective.getActions()));
		}
		return null;
	}

	private static List<PerspectiveKpi> convertPerspectiveKpis(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Perspective.Kpis kpis) {
		if (kpis != null) {
			return convertPerspectiveKpiList(kpis.getKpi());
		}
		return List.of();
	}

	private static List<PerspectiveKpi> convertPerspectiveKpiList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.PerspectiveKpi> kpiList) {
		if (kpiList != null) {
			return kpiList.stream().map(CubeConvertor::convertPerspectiveKpi).toList();
		}
		return List.of();
	}

	private static PerspectiveKpi convertPerspectiveKpi(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.PerspectiveKpi perspectiveKpi) {
		if (perspectiveKpi != null) {
			return new PerspectiveKpiR(perspectiveKpi.getKpiID(), Optional.ofNullable(convertAnnotationList(
					perspectiveKpi.getAnnotations() == null ? null : perspectiveKpi.getAnnotations().getAnnotation())));
		}
		return null;
	}

	private static List<PerspectiveCalculation> convertPerspectiveCalculations(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Perspective.Calculations calculations) {
		if (calculations != null) {
			return convertPerspectiveCalculationList(calculations.getCalculation());
		}
		return List.of();
	}

	private static List<PerspectiveCalculation> convertPerspectiveCalculationList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.PerspectiveCalculation> calculationList) {
		if (calculationList != null) {
			return calculationList.stream().map(CubeConvertor::convertPerspectiveCalculation).toList();
		}
		return List.of();
	}

	private static PerspectiveCalculation convertPerspectiveCalculation(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.PerspectiveCalculation perspectiveCalculation) {
		if (perspectiveCalculation != null) {
			return new PerspectiveCalculationR(perspectiveCalculation.getName(), perspectiveCalculation.getType(),
					Optional.ofNullable(convertAnnotationList(perspectiveCalculation.getAnnotations() == null ? null
							: perspectiveCalculation.getAnnotations().getAnnotation())));
		}
		return null;
	}

	private static List<PerspectiveAction> convertPerspectiveActions(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Perspective.Actions actions) {
		if (actions != null) {
			return convertPerspectiveActionList(actions.getAction());
		}
		return List.of();
	}

	private static List<PerspectiveAction> convertPerspectiveActionList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.PerspectiveAction> actionList) {
		if (actionList != null) {
			return actionList.stream().map(CubeConvertor::convertPerspectiveAction).toList();
		}
		return List.of();
	}

	private static PerspectiveAction convertPerspectiveAction(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.PerspectiveAction perspectiveAction) {
		if (perspectiveAction != null) {
			return new PerspectiveActionR(perspectiveAction.getActionID(),
					Optional.ofNullable(convertAnnotationList(perspectiveAction.getAnnotations() == null ? null
							: perspectiveAction.getAnnotations().getAnnotation())));
		}
		return null;
	}

	private static List<PerspectiveMeasureGroup> convertPerspectiveMeasureGroups(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Perspective.MeasureGroups measureGroups) {
		if (measureGroups != null) {
			return convertPerspectiveMeasureGroupList(measureGroups.getMeasureGroup());
		}
		return List.of();
	}

	private static List<PerspectiveMeasureGroup> convertPerspectiveMeasureGroupList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.PerspectiveMeasureGroup> measureGroupList) {
		if (measureGroupList != null) {
			return measureGroupList.stream().map(CubeConvertor::convertPerspectiveMeasureGroup).toList();
		}
		return List.of();
	}

	private static PerspectiveMeasureGroup convertPerspectiveMeasureGroup(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.PerspectiveMeasureGroup perspectiveMeasureGroup) {
		if (perspectiveMeasureGroup != null) {
			return new PerspectiveMeasureGroupR(perspectiveMeasureGroup.getMeasureGroupID(),
					Optional.ofNullable(convertPerspectiveMeasureGroupMeasures(perspectiveMeasureGroup.getMeasures())),
					Optional.ofNullable(convertAnnotationList(perspectiveMeasureGroup.getAnnotations() == null ? null
							: perspectiveMeasureGroup.getAnnotations().getAnnotation())));
		}
		return null;
	}

	private static List<PerspectiveMeasure> convertPerspectiveMeasureGroupMeasures(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.PerspectiveMeasureGroup.Measures measures) {
		if (measures != null) {
			return convertPerspectiveMeasureList(measures.getMeasure());
		}
		return List.of();
	}

	private static List<PerspectiveMeasure> convertPerspectiveMeasureList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.PerspectiveMeasure> measureList) {
		if (measureList != null) {
			return measureList.stream().map(CubeConvertor::convertPerspectiveMeasure).toList();
		}
		return List.of();
	}

	private static PerspectiveMeasure convertPerspectiveMeasure(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.PerspectiveMeasure perspectiveMeasure) {
		if (perspectiveMeasure != null) {
			return new PerspectiveMeasureR(perspectiveMeasure.getMeasureID(),
					convertAnnotationList(perspectiveMeasure.getAnnotations() == null ? null
							: perspectiveMeasure.getAnnotations().getAnnotation()));
		}
		return null;
	}

	private static List<PerspectiveDimension> convertPerspectiveDimensions(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Perspective.Dimensions dimensions) {
		if (dimensions != null) {
			return convertPerspectiveDimensionList(dimensions.getDimension());
		}
		return List.of();
	}

	private static List<PerspectiveDimension> convertPerspectiveDimensionList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.PerspectiveDimension> dimensionList) {
		if (dimensionList != null) {
			return dimensionList.stream().map(CubeConvertor::convertPerspectiveDimension).toList();
		}
		return List.of();

	}

	private static PerspectiveDimension convertPerspectiveDimension(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.PerspectiveDimension perspectiveDimension) {
		if (perspectiveDimension != null) {
			return new PerspectiveDimensionR(perspectiveDimension.getCubeDimensionID(),
					Optional.ofNullable(convertPerspectiveDimensionAttributes(perspectiveDimension.getAttributes())),
					Optional.ofNullable(convertPerspectiveDimensionHierarchies(perspectiveDimension.getHierarchies())),
					Optional.ofNullable(convertAnnotationList(perspectiveDimension.getAnnotations() == null ? null
							: perspectiveDimension.getAnnotations().getAnnotation())));
		}
		return null;
	}

	private static List<PerspectiveHierarchy> convertPerspectiveDimensionHierarchies(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.PerspectiveDimension.Hierarchies hierarchies) {
		if (hierarchies != null) {
			return convertPerspectiveHierarchyList(hierarchies.getHierarchy());
		}
		return List.of();

	}

	private static List<PerspectiveHierarchy> convertPerspectiveHierarchyList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.PerspectiveHierarchy> hierarchyList) {
		if (hierarchyList != null) {
			return hierarchyList.stream().map(CubeConvertor::convertPerspectiveHierarchy).toList();
		}
		return List.of();
	}

	private static PerspectiveHierarchy convertPerspectiveHierarchy(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.PerspectiveHierarchy perspectiveHierarchy) {
		if (perspectiveHierarchy != null) {
			return new PerspectiveHierarchyR(perspectiveHierarchy.getHierarchyID(),
					Optional.ofNullable(convertAnnotationList(perspectiveHierarchy.getAnnotations() == null ? null
							: perspectiveHierarchy.getAnnotations().getAnnotation())));
		}
		return null;
	}

	private static List<PerspectiveAttribute> convertPerspectiveDimensionAttributes(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.PerspectiveDimension.Attributes attributes) {
		if (attributes != null) {
			return convertPerspectiveAttributeList(attributes.getAttribute());
		}
		return List.of();
	}

	private static List<PerspectiveAttribute> convertPerspectiveAttributeList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.PerspectiveAttribute> attributeList) {
		if (attributeList != null) {
			return attributeList.stream().map(CubeConvertor::convertPerspectiveAttribute).toList();
		}
		return List.of();
	}

	private static PerspectiveAttribute convertPerspectiveAttribute(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.PerspectiveAttribute perspectiveAttribute) {
		if (perspectiveAttribute != null) {
			return new PerspectiveAttributeR(perspectiveAttribute.getAttributeID(),
					Optional.ofNullable(perspectiveAttribute.isAttributeHierarchyVisible()),
					Optional.ofNullable(perspectiveAttribute.getDefaultMember()),
					Optional.ofNullable(convertAnnotationList(perspectiveAttribute.getAnnotations() == null ? null
							: perspectiveAttribute.getAnnotations().getAnnotation())));
		}
		return null;
	}

	private static List<Translation> convertPerspectiveTranslations(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Perspective.Translations translations) {
		if (translations != null) {
			return convertTranslationList(translations.getTranslation());
		}
		return List.of();
	}

	private static List<MdxScript> convertCubeMdxScripts(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Cube.MdxScripts mdxScripts) {
		if (mdxScripts != null) {
			return convertMdxScriptList(mdxScripts.getMdxScript());
		}
		return List.of();
	}

	private static List<MdxScript> convertMdxScriptList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.MdxScript> mdxScriptList) {
		if (mdxScriptList != null) {
			return mdxScriptList.stream().map(CubeConvertor::convertMdxScript).toList();
		}
		return List.of();
	}

	public static MdxScript convertMdxScript(org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.MdxScript mdxScript) {
		if (mdxScript != null) {
			return new MdxScriptR(mdxScript.getName(), mdxScript.getID(),
					convertToInstant(mdxScript.getCreatedTimestamp()),
					convertToInstant(mdxScript.getLastSchemaUpdate()), mdxScript.getDescription(),
					convertAnnotationList(
							mdxScript.getAnnotations() == null ? null : mdxScript.getAnnotations().getAnnotation()),
					convertMdxScriptCommands(mdxScript.getCommands()), mdxScript.isDefaultScript(),
					convertMdxScriptCalculationProperties(mdxScript.getCalculationProperties()));
		}
		return null;
	}

	private static List<CalculationProperty> convertMdxScriptCalculationProperties(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.MdxScript.CalculationProperties calculationProperties) {
		if (calculationProperties != null) {
			return convertCalculationPropertyList(calculationProperties.getCalculationProperty());
		}
		return List.of();
	}

	private static List<CalculationProperty> convertCalculationPropertyList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.CalculationProperty> calculationPropertyList) {
		if (calculationPropertyList != null) {
			return calculationPropertyList.stream().map(CubeConvertor::convertCalculationProperty).toList();
		}
		return List.of();
	}

	private static CalculationProperty convertCalculationProperty(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.CalculationProperty calculationProperty) {
		if (calculationProperty != null) {
			return new CalculationPropertyR(calculationProperty.getCalculationReference(),
					calculationProperty.getCalculationType(),
					convertCalculationPropertyTranslations(calculationProperty.getTranslations()),
					calculationProperty.getDescription(), calculationProperty.isVisible(),
					calculationProperty.getSolveOrder(), calculationProperty.getFormatString(),
					calculationProperty.getForeColor(), calculationProperty.getBackColor(),
					calculationProperty.getFontName(), calculationProperty.getFontSize(),
					calculationProperty.getFontFlags(), calculationProperty.getNonEmptyBehavior(),
					calculationProperty.getAssociatedMeasureGroupID(), calculationProperty.getDisplayFolder(),
					calculationProperty.getLanguage(), convertCalculationPropertiesVisualizationProperties(
							calculationProperty.getVisualizationProperties()));
		}
		return null;
	}

	private static CalculationPropertiesVisualizationProperties convertCalculationPropertiesVisualizationProperties(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.engine300.CalculationPropertiesVisualizationProperties visualizationProperties) {
		if (visualizationProperties != null) {
			return new CalculationPropertiesVisualizationPropertiesR(visualizationProperties.getFolderPosition(),
					visualizationProperties.getContextualNameRule(), visualizationProperties.getAlignment(),
					visualizationProperties.getFolderDefault(), visualizationProperties.getRightToLeft(),
					visualizationProperties.getSortDirection(), visualizationProperties.getUnits(),
					visualizationProperties.getWidth(), visualizationProperties.getDefaultMeasure(),
					visualizationProperties.getDefaultDetailsPosition(),
					visualizationProperties.getSortPropertiesPosition(), visualizationProperties.getSimpleMeasure());
		}
		return null;
	}

	private static List<Translation> convertCalculationPropertyTranslations(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.CalculationProperty.Translations translations) {
		if (translations != null) {
			return convertTranslationList(translations.getTranslation());
		}
		return List.of();
	}

	private static List<Command> convertMdxScriptCommands(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.MdxScript.Commands commands) {
		if (commands != null) {
			return convertCommandList(commands.getCommand());
		}
		return List.of();
	}

	private static List<CubePermission> convertCubeCubePermissions(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Cube.CubePermissions cubePermissions) {
		if (cubePermissions != null) {
			return convertCubePermissionList(cubePermissions.getCubePermission());
		}
		return List.of();
	}

	private static List<CubePermission> convertCubePermissionList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.CubePermission> cubePermissionList) {
		if (cubePermissionList != null) {
			return cubePermissionList.stream().map(CubeConvertor::convertCubePermission).toList();
		}
		return List.of();
	}

	private static CubePermission convertCubePermission(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.CubePermission cubePermission) {
		if (cubePermission != null) {
			return new CubePermissionR(Optional.ofNullable(cubePermission.getReadSourceData()),
					Optional.ofNullable(
							convertCubePermissionDimensionPermissions(cubePermission.getDimensionPermissions())),
					Optional.ofNullable(convertCubePermissionCellPermissions(cubePermission.getCellPermissions())),
					cubePermission.getName(), Optional.ofNullable(cubePermission.getID()),
					Optional.ofNullable(convertToInstant(cubePermission.getCreatedTimestamp())),
					Optional.ofNullable(convertToInstant(cubePermission.getLastSchemaUpdate())),
					Optional.ofNullable(cubePermission.getDescription()),
					Optional.ofNullable(convertAnnotationList(cubePermission.getAnnotations() == null ? null
							: cubePermission.getAnnotations().getAnnotation())),
					cubePermission.getRoleID(), Optional.ofNullable(cubePermission.isProcess()),
					Optional.ofNullable(ReadDefinitionEnum.fromValue(cubePermission.getReadDefinition())),
					Optional.ofNullable(ReadWritePermissionEnum.fromValue(cubePermission.getRead())),
					Optional.ofNullable(ReadWritePermissionEnum.fromValue(cubePermission.getWrite())));
		}
		return null;
	}

	private static List<CubeDimensionPermission> convertCubePermissionDimensionPermissions(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.CubePermission.DimensionPermissions dimensionPermissions) {
		if (dimensionPermissions != null) {
			return convertCubeDimensionPermissionList(dimensionPermissions.getDimensionPermission());
		}
		return List.of();
	}

	private static List<CubeDimensionPermission> convertCubeDimensionPermissionList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.CubeDimensionPermission> dimensionPermissionList) {
		if (dimensionPermissionList != null) {
			return dimensionPermissionList.stream().map(CubeConvertor::convertCubeDimensionPermission).toList();
		}
		return List.of();
	}

	public static CubeDimensionPermission convertCubeDimensionPermission(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.CubeDimensionPermission cubeDimensionPermission) {
		if (cubeDimensionPermission != null) {
			return new CubeDimensionPermissionR(cubeDimensionPermission.getCubeDimensionID(),
					Optional.ofNullable(cubeDimensionPermission.getDescription()),
					Optional.ofNullable(ReadWritePermissionEnum.fromValue(cubeDimensionPermission.getRead())),
					Optional.ofNullable(ReadWritePermissionEnum.fromValue(cubeDimensionPermission.getWrite())),
					Optional.ofNullable(convertCubeDimensionPermissionAttributePermissions(
							cubeDimensionPermission.getAttributePermissions())),
					Optional.ofNullable(convertAnnotationList(cubeDimensionPermission.getAnnotations() == null ? null
							: cubeDimensionPermission.getAnnotations().getAnnotation())));
		}
		return null;
	}

	private static List<AttributePermission> convertCubeDimensionPermissionAttributePermissions(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.CubeDimensionPermission.AttributePermissions attributePermissions) {
		if (attributePermissions != null) {
			return convertAttributePermissionList(attributePermissions.getAttributePermission());
		}
		return List.of();
	}

	public static List<AttributePermission> convertAttributePermissionList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AttributePermission> attributePermissionList) {
		if (attributePermissionList != null) {
			return attributePermissionList.stream().map(CubeConvertor::convertAttributePermission).toList();
		}
		return List.of();
	}

	private static AttributePermission convertAttributePermission(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.AttributePermission attributePermission) {
		if (attributePermission != null) {
			return new AttributePermissionR(attributePermission.getAttributeID(),
					Optional.ofNullable(attributePermission.getDescription()),
					Optional.ofNullable(attributePermission.getDefaultMember()),
					Optional.ofNullable(attributePermission.getVisualTotals()),
					Optional.ofNullable(attributePermission.getAllowedSet()),
					Optional.ofNullable(attributePermission.getDeniedSet()),
					Optional.ofNullable(convertAnnotationList(attributePermission.getAnnotations() == null ? null
							: attributePermission.getAnnotations().getAnnotation())));
		}
		return null;
	}

	private static List<CellPermission> convertCubePermissionCellPermissions(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.CubePermission.CellPermissions cellPermissions) {
		if (cellPermissions != null) {
			return convertCellPermissionList(cellPermissions.getCellPermission());
		}
		return List.of();
	}

	private static List<CellPermission> convertCellPermissionList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.CellPermission> cellPermissionList) {
		if (cellPermissionList != null) {
			return cellPermissionList.stream().map(CubeConvertor::convertCellPermission).toList();
		}
		return List.of();
	}

	private static CellPermission convertCellPermission(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.CellPermission cellPermission) {
		if (cellPermission != null) {
			return new CellPermissionR(Optional.ofNullable(AccessEnum.fromValue(cellPermission.getAccess())),
					Optional.ofNullable(cellPermission.getDescription()),
					Optional.ofNullable(cellPermission.getExpression()),
					Optional.ofNullable(convertAnnotationList(cellPermission.getAnnotations() == null ? null
							: cellPermission.getAnnotations().getAnnotation())));
		}
		return null;
	}

	private static List<CubeDimension> convertCubeDimensions(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Cube.Dimensions dimensions) {
		if (dimensions != null) {
			return convertDimensionList(dimensions.getDimension());
		}
		return List.of();

	}

	private static List<CubeDimension> convertDimensionList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.CubeDimension> dimensionList) {
		if (dimensionList != null) {
			return dimensionList.stream().map(CubeConvertor::convertCubeDimension).toList();
		}
		return List.of();
	}

	public static CubeDimension convertCubeDimension(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.CubeDimension cubeDimension) {
		if (cubeDimension != null) {
			return new CubeDimensionR(cubeDimension.getDimensionID(), cubeDimension.getName(),
					cubeDimension.getDescription(), convertCubeDimensionTranslations(cubeDimension.getTranslations()),
					cubeDimension.getDimensionID(), cubeDimension.isVisible(),
					cubeDimension.getAllMemberAggregationUsage(), cubeDimension.getHierarchyUniqueNameStyle(),
					cubeDimension.getMemberUniqueNameStyle(),
					convertCubeDimensionAttributes(cubeDimension.getAttributes()),
					convertCubeDimensionHierarchies(cubeDimension.getHierarchies()),
					convertAnnotationList(cubeDimension.getAnnotations() == null ? null
							: cubeDimension.getAnnotations().getAnnotation()));
		}
		return null;
	}

	private static List<CubeHierarchy> convertCubeDimensionHierarchies(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.CubeDimension.Hierarchies hierarchies) {
		if (hierarchies != null) {
			return convertCubeHierarchyList(hierarchies.getHierarchy());
		}
		return List.of();
	}

	private static List<CubeHierarchy> convertCubeHierarchyList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.CubeHierarchy> hierarchy) {
		if (hierarchy != null) {
			return hierarchy.stream().map(CubeConvertor::convertCubeHierarchy).toList();
		}
		return List.of();
	}

	private static CubeHierarchy convertCubeHierarchy(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.CubeHierarchy cubeHierarchy) {
		if (cubeHierarchy != null) {
			return new CubeHierarchyR(cubeHierarchy.getHierarchyID(), cubeHierarchy.getOptimizedState(),
					cubeHierarchy.isVisible(), cubeHierarchy.isEnabled(),
					convertAnnotationList(cubeHierarchy.getAnnotations() == null ? null
							: cubeHierarchy.getAnnotations().getAnnotation()));
		}
		return null;
	}

	private static List<CubeAttribute> convertCubeDimensionAttributes(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.CubeDimension.Attributes attributes) {
		if (attributes != null) {
			return convertCubeAttributeList(attributes.getAttribute());
		}
		return List.of();
	}

	private static List<CubeAttribute> convertCubeAttributeList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.CubeAttribute> attributeList) {
		if (attributeList != null) {
			return attributeList.stream().map(CubeConvertor::convertCubeAttribute).toList();
		}
		return List.of();

	}

	private static CubeAttribute convertCubeAttribute(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.CubeAttribute cubeAttribute) {
		if (cubeAttribute != null) {
			return new CubeAttributeR(cubeAttribute.getAttributeID(), cubeAttribute.getAggregationUsage(),
					cubeAttribute.getAttributeHierarchyOptimizedState(), cubeAttribute.isAttributeHierarchyEnabled(),
					cubeAttribute.isAttributeHierarchyVisible(),
					convertAnnotationList(cubeAttribute.getAnnotations() == null ? null
							: cubeAttribute.getAnnotations().getAnnotation()));
		}
		return null;
	}

	private static List<Translation> convertCubeDimensionTranslations(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.CubeDimension.Translations translations) {
		if (translations != null) {
			return convertTranslationList(translations.getTranslation());
		}
		return List.of();
	}

	private static List<Translation> convertCubeTranslations(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Cube.Translations translations) {
		if (translations != null) {
			return convertTranslationList(translations.getTranslation());
		}
		return List.of();
	}

	public static List<Translation> convertTranslationList(
			List<org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Translation> translationList) {
		if (translationList != null) {
			return translationList.stream().map(CubeConvertor::convertTranslation).toList();
		}
		return List.of();
	}

	private static Translation convertTranslation(
			org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla.Translation translation) {
		if (translation != null) {
			return new TranslationR(translation.getLanguage(), translation.getCaption(), translation.getDescription(),
					translation.getDisplayFolder(), convertAnnotationList(translation.getAnnotations() == null ? null
							: translation.getAnnotations().getAnnotation()));
		}
		return null;
	}

}