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
*   Copyright (C) 2011-2019 Hitachi Vantara
*   Copyright (C) 2020-2021 Sergei Semenkov
*   SmartCity Jena - initial
*   Stefan Bischof (bipolis.org) - initial
*
*/
package org.eclipse.daanse.olap.api;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.eclipse.daanse.olap.rolap.core.BasicContext;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "%ctx.ocd.name", description = "%ctx.ocd.description", localization = "OSGI-INF/l10n/ctx", factoryPid =  BasicContext.PID)
public interface BasicContextConfig {
    public static final String QUERY_LIMIT = "queryLimit";
    public static final String SEGMENT_CACHE = "segmentCache";
    public static final String ENABLE_TOTAL_COUNT = "enableTotalCount";
    public static final String SEGMENT_CACHE_MANAGER_NUMBER_CACHE_THREADS = "segmentCacheManagerNumberCacheThreads";
    public static final String CELL_BATCH_SIZE = "cellBatchSize";
    public static final String ROLAP_CONNECTION_SHEPHERD_NB_THREADS = "rolapConnectionShepherdNbThreads";
    public static final String ROLAP_CONNECTION_SHEPHERD_THREAD_POLLING_INTERVAL = "rolapConnectionShepherdThreadPollingInterval";
    public static final String ROLAP_CONNECTION_SHEPHERD_THREAD_POLLING_INTERVAL_UNIT = "rolapConnectionShepherdThreadPollingIntervalUnit";
    public static final String SEGMENT_CACHE_MANAGER_NUMBER_SQL_THREADS = "segmentCacheManagerNumberSqlThreads";
    public static final String SOLVE_ORDER_MODE = "solveOrderMode";
    public static final String CHOOSE_AGGREGATE_BY_VOLUME = "chooseAggregateByVolume";
    public static final String DISABLE_CACHING = "disableCaching";
    public static final String DISABLE_LOCAL_SEGMENT_CACHE = "disableLocalSegmentCache";
    public static final String ENABLE_GROUPING_SETS = "enableGroupingSets";
    public static final String ENABLE_SESSION_CACHING = "enableSessionCaching";
    public static final String COMPOUND_SLICER_MEMBER_SOLVE_ORDER = "compoundSlicerMemberSolveOrder";
    public static final String ENABLE_DRILL_THROUGH = "enableDrillThrough";
    public static final String ENABLE_NATIVE_FILTER = "enableNativeFilter";
    public static final String ENABLE_NATIVE_CROSS_JOIN = "enableNativeCrossJoin";
    public static final String ENABLE_NATIVE_NON_EMPTY = "enableNativeNonEmpty";
    public static final String ENABLE_NATIVE_TOP_COUNT = "enableNativeTopCount";
    public static final String ENABLE_IN_MEMORY_ROLLUP = "enableInMemoryRollup";
    public static final String EXPAND_NON_NATIVE = "expandNonNative";
    public static final String GENERATE_AGGREGATE_SQL = "generateAggregateSql";
    public static final String IGNORE_INVALID_MEMBERS_DURING_QUERY = "ignoreInvalidMembersDuringQuery";
    public static final String IGNORE_MEASURE_FOR_NON_JOINING_DIMENSION = "ignoreMeasureForNonJoiningDimension";
    public static final String ITERATION_LIMIT = "iterationLimit";
    public static final String LEVEL_PRE_CACHE_THRESHOLD = "levelPreCacheThreshold";
    public static final String MAX_CONSTRAINTS = "maxConstraints";
    public static final String TEST_EXP_DEPENDENCIES = "testExpDependencies";
    public static final String READ_AGGREGATES = "readAggregates";
    public static final String ALERT_NATIVE_EVALUATION_UNSUPPORTED = "alertNativeEvaluationUnsupported";
    public static final String CROSS_JOIN_OPTIMIZER_SIZE = "crossJoinOptimizerSize";
    public static final String CURRENT_MEMBER_WITH_COMPOUND_SLICER_ALERT = "currentMemberWithCompoundSlicerAlert";
    public static final String IGNORE_INVALID_MEMBERS = "ignoreInvalidMembers";
    public static final String MAX_EVAL_DEPTH = "maxEvalDepth";
    public static final String CHECK_CANCEL_OR_TIMEOUT_INTERVAL = "checkCancelOrTimeoutInterval";
    public static final String MEMORY_MONITOR = "memoryMonitor";
    public static final String WARN_IF_NO_PATTERN_FOR_DIALECT = "warnIfNoPatternForDialect";
    public static final String USE_AGGREGATES = "useAggregates";
    public static final String QUERY_TIMEOUT = "queryTimeout";
    public static final String OPTIMIZE_PREDICATES = "optimizePredicates";
    public static final String NULL_DENOMINATOR_PRODUCES_NULL = "nullDenominatorProducesNull";
    public static final String NEED_DIMENSION_PREFIX = "needDimensionPrefix";
    public static final String NATIVIZE_MIN_THRESHOLD = "nativizeMinThreshold";
    public static final String NATIVIZE_MAX_RESULTS = "nativizeMaxResults";
    public static final String SPARSE_SEGMENT_COUNT_THRESHOLD = "sparseSegmentCountThreshold";
    public static final String SPARSE_SEGMENT_DENSITY_THRESHOLD = "sparseSegmentDensityThreshold";
    public static final String MEMORY_MONITOR_THRESHOLD = "memoryMonitorThreshold";
    public static final String GENERATE_FORMATTED_SQL = "generateFormattedSql";
    public static final String EXECUTE_DURATION = "executeDuration";
    public static final String EXECUTE_DURATION_UNIT = "executeDurationUnit";


    Integer QUERY_LIMIT_DEFAULT_VALUE = 40;
    String SEGMENT_CACHE_DEFAULT_VALUE = null;
    Boolean ENABLE_TOTAL_COUNT_DEFAULT_VALUE = false;
    Integer SEGMENT_CACHE_MANAGER_NUMBER_CACHE_THREADS_DEFAULT_VALUE = 100;
    Integer CELL_BATCH_SIZE_DEFAULT_VALUE = -1;
    Integer ROLAP_CONNECTION_SHEPHERD_NB_THREADS_DEFAULT_VALUE = 20;
    Long ROLAP_CONNECTION_SHEPHERD_THREAD_POLLING_INTERVAL_DEFAULT_VALUE = 1000L;
    TimeUnit ROLAP_CONNECTION_SHEPHERD_THREAD_POLLING_INTERVAL_UNIT_DEFAULT_VALUE = TimeUnit.MILLISECONDS;
    Integer SEGMENT_CACHE_MANAGER_NUMBER_SQL_THREADS_DEFAULT_VALUE = 100;
    String SOLVE_ORDER_MODE_DEFAULT_VALUE = "ABSOLUTE";
    boolean CHOOSE_AGGREGATE_BY_VOLUME_DEFAULT_VALUE = false;
    boolean DISABLE_CACHING_DEFAULT_VALUE = false;
    boolean DISABLE_LOCAL_SEGMENT_CACHE_DEFAULT_VALUE = false;
    boolean ENABLE_GROUPING_SETS_DEFAULT_VALUE = false;
    boolean ENABLE_SESSION_CACHING_DEFAULT_VALUE = false;
    int COMPOUND_SLICER_MEMBER_SOLVE_ORDER_DEFAULT_VALUE = -99999;
    boolean ENABLE_DRILL_THROUGH_DEFAULT_VALUE = true;
    boolean ENABLE_NATIVE_FILTER_DEFAULT_VALUE = true;
    boolean ENABLE_NATIVE_CROSS_JOIN_DEFAULT_VALUE = true;
    boolean ENABLE_NATIVE_TOP_COUNT_DEFAULT_VALUE = true;
    boolean ENABLE_IN_MEMORY_ROLLUP_DEFAULT_VALUE = true;
    boolean EXPAND_NON_NATIVE_DEFAULT_VALUE = false;
    boolean GENERATE_AGGREGATE_SQL_DEFAULT_VALUE = false;
    boolean IGNORE_INVALID_MEMBERS_DURING_QUERY_DEFAULT_VALUE = false;
    boolean IGNORE_MEASURE_FOR_NON_JOINING_DIMENSION_DEFAULT_VALUE = false;
    int ITERATION_LIMIT_DEFAULT_VALUE = 0;
    int LEVEL_PRE_CACHE_THRESHOLD_DEFAULT_VALUE = 300;
    int MAX_CONSTRAINTS_DEFAULT_VALUE = 1000;
    int TEST_EXP_DEPENDENCIES_DEFAULT_VALUE = 0;
    boolean READ_AGGREGATES_DEFAULT_VALUE = false;
    String ALERT_NATIVE_EVALUATION_UNSUPPORTED_DEFAULT_VALUE = "OFF";
    int CROSS_JOIN_OPTIMIZER_SIZE_DEFAULT_VALUE = 0;
    String CURRENT_MEMBER_WITH_COMPOUND_SLICER_ALERT_DEFAULT_VALUE = "ERROR";
    boolean IGNORE_INVALID_MEMBERS_DEFAULT_VALUE = false;
    int MAX_EVAL_DEPTH_DEFAULT_VALUE = 10;
    int CHECK_CANCEL_OR_TIMEOUT_INTERVAL_DEFAULT_VALUE = 1000;
    boolean MEMORY_MONITOR_DEFAULT_VALUE = false;
    String WARN_IF_NO_PATTERN_FOR_DIALECT_DEFAULT_VALUE = "NONE";
    boolean USE_AGGREGATES_DEFAULT_VALUE = false;
    int QUERY_TIMEOUT_DEFAULT_VALUE = 20;
    boolean OPTIMIZE_PREDICATES_DEFAULT_VALUE = true;
    boolean NULL_DENOMINATOR_PRODUCES_NULL_DEFAULT_VALUE = false;
    boolean NEED_DIMENSION_PREFIX_DEFAULT_VALUE = false;
    int NATIVIZE_MIN_THRESHOLD_DEFAULT_VALUE = 100000;
    int NATIVIZE_MAX_RESULTS_DEFAULT_VALUE = 150000;
    int SPARSE_SEGMENT_COUNT_THRESHOLD_DEFAULT_VALUE = 1000;
    double SPARSE_SEGMENT_DENSITY_THRESHOLD_DEFAULT_VALUE = 0.5;
    int MEMORY_MONITOR_THRESHOLD_DEFAULT_VALUE = 90;
    boolean GENERATE_FORMATTED_SQL_DEFAULT_VALUE = false;
    long EXECUTE_DURATION_DEFAULT_VALUE = 0;
    String EXECUTE_DURATION_UNIT_DEFAULT_VALUE = "MILLISECONDS";

    @AttributeDefinition(name = "%name.name", description = "%name.description", required = false)
    default String name() {
        return null;
    }

    @AttributeDefinition(name = "%description.name", description = "%description.description", type = AttributeType.STRING)
    default String description() {
    	 return null;
    }

    //<p>Maximum number of simultaneous queries the system will allow.</p> <p>Oracle fails if you try to run more than the 'processes' parameter in init.ora, typically 150. The throughput of Oracle and other databases will probably reduce long before you get to their limit.</p>
    @AttributeDefinition(name = "%queryLimit.name", description = "%queryLimit.description", type = AttributeType.INTEGER)
    default Integer queryLimit() {
        return QUERY_LIMIT_DEFAULT_VALUE;
    }

    //Integer property that, if set to a value greater than zero, sets a hard limit on the number of cells that are batched together when building segments.
    /**
     * Integer property that, if set to a value greater than zero, sets a hard limit on the
     * number of cells that are batched together when building segments.
     */
    @AttributeDefinition(name = "%cellBatchSize.name", description = "%cellBatchSize.description", type = AttributeType.INTEGER)
    default Integer cellBatchSize() {
        return CELL_BATCH_SIZE_DEFAULT_VALUE;
    }

    //Property which turns on or off the in-memory rollup of segment data. Defaults to true.
    @AttributeDefinition(name = "%enableInMemoryRollup.name", description = "%enableInMemoryRollup.description", type = AttributeType.BOOLEAN)
    default Boolean enableInMemoryRollup() {
        return ENABLE_IN_MEMORY_ROLLUP_DEFAULT_VALUE;
    }

    //Property which defines which SegmentCache implementation to use. Specify the value as a fully qualified class name, such as <code>org.example.SegmentCacheImpl</code> where SegmentCacheImpl is an implementation of {@link mondrian.spi.SegmentCache}.
    @AttributeDefinition(name = "%segmentCache.name", description = "%segmentCache.description", type = AttributeType.STRING)
    default String segmentCache() {
        return SEGMENT_CACHE_DEFAULT_VALUE;
    }

    /**
     * <p>Property that, with {@link #SparseSegmentDensityThreshold}, determines
     * whether to choose a sparse or dense representation when storing
     * collections of cell values in memory.</p>
     *
     * <p>When storing collections of cell values, Mondrian has to choose
     * between a sparse and a dense representation, based upon the
     * <code>possible</code> and <code>actual</code> number of values.
     * The <code>density</code> is <code>actual / possible</code>.</p>
     *
     * <p>We use a sparse representation if
     * <code>(possible -
     * {@link #SparseSegmentCountThreshold countThreshold}) *
     * {@link #SparseSegmentDensityThreshold densityThreshold} &gt;
     * actual</code></p>
     *
     * <p>For example, at the default values
     * ({@link #SparseSegmentCountThreshold countThreshold} = 1000,
     * {@link #SparseSegmentDensityThreshold} = 0.5),
     * we use a dense representation for</p>
     *
     * <ul>
     * <li>(1000 possible, 0 actual), or</li>
     * <li>(2000 possible, 500 actual), or</li>
     * <li>(3000 possible, 1000 actual).</li>
     * </ul>
     *
     * <p>Any fewer actual values, or any more
     * possible values, and Mondrian will use a sparse representation.</p>
     */    @AttributeDefinition(name = "%sparseSegmentCountThreshold.name", description = "%sparseSegmentCountThreshold.description", type = AttributeType.INTEGER)
    default Integer sparseSegmentCountThreshold() {
        return SPARSE_SEGMENT_COUNT_THRESHOLD_DEFAULT_VALUE;
    }

    //Property that, with {@link #SparseSegmentCountThreshold}, determines whether to choose a sparse or dense representation when storing collections of cell values in memory.
    @AttributeDefinition(name = "%sparseSegmentDensityThreshold.name", description = "%sparseSegmentDensityThreshold.description", type = AttributeType.DOUBLE)
    default Double sparseSegmentDensityThreshold() {
        return SPARSE_SEGMENT_DENSITY_THRESHOLD_DEFAULT_VALUE;
    }

    //<p>Property that controls whether warning messages should be printed if a SQL comparison test does not contain expected SQL statements for the specified dialect. The tests are skipped if no expected SQL statements are found for the current dialect.</p> <p>Possible values are the following:</p> NONE: no warning (default); ANY: any dialect; ACCESS; DERBY; LUCIDDB; MYSQL ... and any Dialect enum in SqlPattern.Dialect <p>Specific tests can overwrite the default setting. The priority is: Settings besides "ANY" in mondrian.properties file; Any setting in the test; ANY</p>
    @AttributeDefinition(name = "%warnIfNoPatternForDialect.name", description = "%warnIfNoPatternForDialect.description", type = AttributeType.STRING)
    default String warnIfNoPatternForDialect() {
        return WARN_IF_NO_PATTERN_FOR_DIALECT_DEFAULT_VALUE;
    }

    //<p>Boolean property that controls whether Mondrian uses aggregate tables.</p> <p>If true, then Mondrian uses aggregate tables. This property is queried prior to each aggregate query so that changing the value of this property dynamically (not just at startup) is meaningful.</p> <p>Aggregates can be read from the database using the {@link #ReadAggregates} property but will not be used unless this property is set to true.</p>
    @AttributeDefinition(name = "%useAggregates.name", description = "%useAggregates.description", type = AttributeType.BOOLEAN)
    default Boolean useAggregates() {
        return USE_AGGREGATES_DEFAULT_VALUE;
    }

    //<p>Boolean property that determines whether Mondrian should read aggregate tables.</p> <p>If set to true, then Mondrian scans the database for aggregate tables. Unless mondrian.rolap.aggregates.Use is set to true, the aggregates found will not be used.</p>
    @AttributeDefinition(name = "%readAggregates.name", description = "%readAggregates.description", type = AttributeType.BOOLEAN)
    default Boolean readAggregates() {
        return READ_AGGREGATES_DEFAULT_VALUE;
    }

    //<p>Boolean property that controls whether aggregate tables are ordered by their volume or row count.</p> <p>If true, Mondrian uses the aggregate table with the smallest volume (number of rows multiplied by number of columns); if false, Mondrian uses the aggregate table with the fewest rows.</p>
    @AttributeDefinition(name = "%chooseAggregateByVolume.name", description = "%chooseAggregateByVolume.description", type = AttributeType.BOOLEAN)
    default Boolean chooseAggregateByVolume() {
        return CHOOSE_AGGREGATE_BY_VOLUME_DEFAULT_VALUE;
    }

    @AttributeDefinition(name = "%generateAggregateSql.name", description = "%generateAggregateSql.description", type = AttributeType.BOOLEAN)
    default Boolean generateAggregateSql() { return GENERATE_AGGREGATE_SQL_DEFAULT_VALUE; }

    @AttributeDefinition(name = "%disableCaching.name", description = "%disableCaching.description", type = AttributeType.BOOLEAN)
    default Boolean disableCaching() { return DISABLE_CACHING_DEFAULT_VALUE; }

    @AttributeDefinition(name = "%disableLocalSegmentCache.name", description = "%disableLocalSegmentCache.description", type = AttributeType.BOOLEAN)
    default Boolean disableLocalSegmentCache() { return DISABLE_LOCAL_SEGMENT_CACHE_DEFAULT_VALUE; }

    @AttributeDefinition(name = "%generateFormattedSql.name", description = "%generateFormattedSql.description", type = AttributeType.BOOLEAN)
    default Boolean generateFormattedSql() { return GENERATE_FORMATTED_SQL_DEFAULT_VALUE; }

    @AttributeDefinition(name = "%expandNonNative.name", description = "%expandNonNative.description", type = AttributeType.BOOLEAN)
    default Boolean expandNonNative() { return EXPAND_NON_NATIVE_DEFAULT_VALUE; }

    @AttributeDefinition(name = "%testExpDependencies.name", description = "%testExpDependencies.description", type = AttributeType.INTEGER)
    default Integer testExpDependencies() { return TEST_EXP_DEPENDENCIES_DEFAULT_VALUE; }

    //If enabled some NON EMPTY CrossJoin will be computed in SQL.
    @AttributeDefinition(name = "%enableNativeCrossJoin.name", description = "%enableNativeCrossJoin.description", type = AttributeType.BOOLEAN)
    default Boolean enableNativeCrossJoin() { return ENABLE_NATIVE_CROSS_JOIN_DEFAULT_VALUE; }

    //If enabled some TopCount will be computed in SQL.
    @AttributeDefinition(name = "%enableNativeTopCount.name", description = "%enableNativeTopCount.description", type = AttributeType.BOOLEAN)
    default Boolean enableNativeTopCount() { return ENABLE_NATIVE_TOP_COUNT_DEFAULT_VALUE; }

    //If enabled some Filter() will be computed in SQL.
    @AttributeDefinition(name = "%enableNativeFilter.name", description = "%enableNativeFilter.description", type = AttributeType.BOOLEAN)
    default Boolean enableNativeFilter() { return ENABLE_NATIVE_FILTER_DEFAULT_VALUE; }

    //Alerting action to take in case native evaluation of a function is enabled but not supported for that function's usage in a particular query.  (No alert is ever raised in cases where native evaluation would definitely have been wasted effort.) Recognized actions: OFF: do nothing (default action, also used if unrecognized action is specified) WARN: log a warning to RolapUtil logger ERROR: throw an instance of NativeEvaluationUnsupportedException
    @AttributeDefinition(name = "%alertNativeEvaluationUnsupported.name", description = "%alertNativeEvaluationUnsupported.description", type = AttributeType.STRING)
    default String alertNativeEvaluationUnsupported() { return ALERT_NATIVE_EVALUATION_UNSUPPORTED_DEFAULT_VALUE; }

    //If disabled, Mondrian will throw an exception if someone attempts to perform a drillthrough of any kind.
    @AttributeDefinition(name = "%enableDrillThrough.name", description = "%enableDrillThrough.description", type = AttributeType.BOOLEAN)
    default Boolean enableDrillThrough() { return ENABLE_DRILL_THROUGH_DEFAULT_VALUE; }

    //If enabled, first row in the result of an XML/A drill-through request will be filled with the total count of rows in underlying database.
    @AttributeDefinition(name = "%enableTotalCount.name", description = "%enableTotalCount.description", type = AttributeType.BOOLEAN)
    default Boolean enableTotalCount() { return ENABLE_TOTAL_COUNT_DEFAULT_VALUE; }

    //Max number of constraints in a single 'IN' SQL clause. This value may be variant among database products and their runtime settings. Oracle, for example, gives the error ORA-01795: maximum number of expressions in a list is 1000. Recommended values: Oracle: 1,000, DB2: 2,500, Other: 10,000
    @AttributeDefinition(name = "%maxConstraints.name", description = "%maxConstraints.description", type = AttributeType.INTEGER)
    default Integer maxConstraints() { return MAX_CONSTRAINTS_DEFAULT_VALUE; }

    //Boolean property that determines whether Mondrian optimizes predicates. If true, Mondrian may retrieve a little more data than specified in MDX query and cache it for future use.  For example, if you ask for data on 48 states of the United States for 3 quarters of 2011, Mondrian will round out to all 50 states and all 4 quarters.  If false, Mondrian still optimizes queries that involve all members of a dimension.
    @AttributeDefinition(name = "%optimizePredicates.name", description = "%optimizePredicates.description", type = AttributeType.BOOLEAN)
    default Boolean optimizePredicates() { return OPTIMIZE_PREDICATES_DEFAULT_VALUE; }

    //Integer property that defines the maximum number of passes allowable while evaluating an MDX expression. If evaluation exceeds this depth (for example, while evaluating a very complex calculated member), Mondrian will throw an error.
    @AttributeDefinition(name = "%maxEvalDepth.name", description = "%maxEvalDepth.description", type = AttributeType.INTEGER)
    default Integer maxEvalDepth() { return MAX_EVAL_DEPTH_DEFAULT_VALUE; }

    //Property that defines the timeout value (in seconds) for queries. A value of 0 (the default) indicates no timeout.
    @AttributeDefinition(name = "%queryTimeout.name", description = "%queryTimeout.description", type = AttributeType.INTEGER)
    default Integer queryTimeout() { return QUERY_TIMEOUT_DEFAULT_VALUE; }

    //This controls query timeouts and cancellation, so a small value (a few milliseconds) is best. Setting this to a value higher than mondrian.rolap.queryTimeout will result the timeout not being enforced as expected. Default value is 1000ms. Default time unit is ms.
    @AttributeDefinition(name = "%rolapConnectionShepherdThreadPollingInterval.name", description = "%rolapConnectionShepherdThreadPollingInterval.description", type = AttributeType.LONG)
    default Long rolapConnectionShepherdThreadPollingInterval() { return ROLAP_CONNECTION_SHEPHERD_THREAD_POLLING_INTERVAL_DEFAULT_VALUE; }

    @AttributeDefinition(name = "%rolapConnectionShepherdThreadPollingIntervalUnit.name", description = "%rolapConnectionShepherdThreadPollingIntervalUnit.description")
    default TimeUnit rolapConnectionShepherdThreadPollingIntervalUnit() { return ROLAP_CONNECTION_SHEPHERD_THREAD_POLLING_INTERVAL_UNIT_DEFAULT_VALUE; }

    //Maximum number of MDX query threads per Mondrian server instance. Defaults to 20.
    @AttributeDefinition(name = "%rolapConnectionShepherdNbThreads.name", description = "%rolapConnectionShepherdNbThreads.description", type = AttributeType.INTEGER)
    default Integer rolapConnectionShepherdNbThreads() { return ROLAP_CONNECTION_SHEPHERD_NB_THREADS_DEFAULT_VALUE; }

    //Maximum number of threads per Mondrian server instance that are used to run SQL queries when populating segments. Defaults to 100.
    @AttributeDefinition(name = "%segmentCacheManagerNumberSqlThreads.name", description = "%segmentCacheManagerNumberSqlThreads.description", type = AttributeType.INTEGER)
    default Integer segmentCacheManagerNumberSqlThreads() { return SEGMENT_CACHE_MANAGER_NUMBER_SQL_THREADS_DEFAULT_VALUE; }

    //Maximum number of threads per Mondrian server instance that are used to run perform operations on the external caches. Defaults to 100.
    @AttributeDefinition(name = "%segmentCacheManagerNumberCacheThreads.name", description = "%segmentCacheManagerNumberCacheThreads.description", type = AttributeType.INTEGER)
    default Integer segmentCacheManagerNumberCacheThreads() { return SEGMENT_CACHE_MANAGER_NUMBER_CACHE_THREADS_DEFAULT_VALUE; }

    //Property that defines whether non-existent member errors should be ignored during schema load. If so, the non-existent member is treated as a null member.
    @AttributeDefinition(name = "%ignoreInvalidMembers.name", description = "%ignoreInvalidMembers.description", type = AttributeType.BOOLEAN)
    default Boolean ignoreInvalidMembers() { return IGNORE_INVALID_MEMBERS_DEFAULT_VALUE; }

    //Property that defines whether non-existent member errors should be ignored during query validation. If so, the non-existent member is treated as a null member.
    @AttributeDefinition(name = "%ignoreInvalidMembersDuringQuery.name", description = "%ignoreInvalidMembersDuringQuery.description", type = AttributeType.BOOLEAN)
    default Boolean ignoreInvalidMembersDuringQuery() { return IGNORE_INVALID_MEMBERS_DURING_QUERY_DEFAULT_VALUE; }

    //Integer property indicating the maximum number of iterations allowed when iterating over members to compute aggregates.  A value of 0 (the default) indicates no limit.
    @AttributeDefinition(name = "%iterationLimit.name", description = "%iterationLimit.description", type = AttributeType.STRING)
    default Integer iterationLimit() { return ITERATION_LIMIT_DEFAULT_VALUE; }

    //Positive integer property that determines loop iterations number between checks for whether the current mdx query has been cancelled or timeout was exceeded. Setting the interval too small may result in a performance degradation when reading large result sets; setting it too high can cause a big delay between the query being marked as cancelled or timeout was exceeded and system resources associated to it being released.
    @AttributeDefinition(name = "%checkCancelOrTimeoutInterval.name", description = "%checkCancelOrTimeoutInterval.description", type = AttributeType.INTEGER)
    default Integer checkCancelOrTimeoutInterval() { return CHECK_CANCEL_OR_TIMEOUT_INTERVAL_DEFAULT_VALUE; }

    //Property that defines whether the <code>MemoryMonitor</code> should be enabled. By default it is disabled; memory monitor is not available before Java version 1.5.
    @AttributeDefinition(name = "%memoryMonitor.name", description = "%memoryMonitor.description", type = AttributeType.BOOLEAN)
    default Boolean memoryMonitor() { return MEMORY_MONITOR_DEFAULT_VALUE; }

    //Property that defines the default MemoryMonitor percentage threshold. If enabled, when Java's memory monitor detects that post-garbage collection is above this value, notifications are generated.
    @AttributeDefinition(name = "%memoryMonitorThreshold.name", description = "%memoryMonitorThreshold.description", type = AttributeType.BOOLEAN)
    default Integer memoryMonitorThreshold() { return MEMORY_MONITOR_THRESHOLD_DEFAULT_VALUE; }

    //Property that defines when to apply the crossjoin optimization algorithm. If a crossjoin input list's size is larger than this property's value and the axis has the "NON EMPTY" qualifier, then the crossjoin non-empty optimizer is applied. Setting this value to '0' means that for all crossjoin input lists in non-empty axes will have the optimizer applied. On the other hand, if the value is set larger than any possible list, say <code>Integer.MAX_VALUE</code>, then the optimizer will never be applied.
    @AttributeDefinition(name = "%crossJoinOptimizerSize.name", description = "%crossJoinOptimizerSize.description", type = AttributeType.INTEGER)
    default Integer crossJoinOptimizerSize() { return CROSS_JOIN_OPTIMIZER_SIZE_DEFAULT_VALUE; }

    //Property that defines the behavior of division if the denominator evaluates to zero. If false (the default), if a division has a non-null numerator and a null denominator, it evaluates to "Infinity", which conforms to SSAS behavior. If true, the result is null if the denominator is null. Setting to true enables the old semantics of evaluating this to null; this does not conform to SSAS, but is useful in some applications.
    @AttributeDefinition(name = "%nullDenominatorProducesNull.name", description = "%nullDenominatorProducesNull.description", type = AttributeType.BOOLEAN)
    default Boolean nullDenominatorProducesNull() { return NULL_DENOMINATOR_PRODUCES_NULL_DEFAULT_VALUE; }

    //Alerting action to take when a CurrentMember function is applied to a dimension that is also a compound slicer; Recognized actions: OFF: do nothing; WARN: log a warning; ERROR: throw an CurrentMemberWithCompoundSlicerMondrianException
    @AttributeDefinition(name = "%currentMemberWithCompoundSlicerAlert.name", description = "%currentMemberWithCompoundSlicerAlert.description", type = AttributeType.STRING)
    default String currentMemberWithCompoundSlicerAlert() { return CURRENT_MEMBER_WITH_COMPOUND_SLICER_ALERT_DEFAULT_VALUE; }

    //Property that defines whether to generate SQL queries using the GROUPING SETS construct for rollup. By default it is not enabled. Ignored on databases which do not support the GROUPING SETS construct (see mondrian.spi.Dialect#supportsGroupingSets).
    @AttributeDefinition(name = "%enableGroupingSets.name", description = "%enableGroupingSets.description", type = AttributeType.BOOLEAN)
    default Boolean enableGroupingSets() { return ENABLE_GROUPING_SETS_DEFAULT_VALUE; }

    //Property that defines whether to ignore measure when non joining dimension is in the tuple during aggregation. If there are unrelated dimensions to a measure in context during aggregation, the measure is ignored in the evaluation context. This behaviour kicks in only if the CubeUsage for this measure has IgnoreUnrelatedDimensions attribute set to false. For example, Gender doesn't join with [Warehouse Sales] measure. With mondrian.olap.agg.IgnoreMeasureForNonJoiningDimension=true Warehouse Sales gets eliminated and is ignored in the aggregate value. SUM({Product.members * Gender.members})    7,913,333.82 With mondrian.olap.agg.IgnoreMeasureForNonJoiningDimension=false Warehouse Sales with Gender All level member contributes to the aggregate value. [Store Sales] + [Warehouse Sales] SUM({Product.members * Gender.members})    9,290,730.03 On a report where Gender M, F and All members exist a user will see a large aggregated value compared to the aggregated value that can be arrived at by summing up values against Gender M and F. This can be confusing to the user. This feature can be used to eliminate such a  situation.
    @AttributeDefinition(name = "%ignoreMeasureForNonJoiningDimension.name", description = "%ignoreMeasureForNonJoiningDimension.description", type = AttributeType.BOOLEAN)
    default Boolean ignoreMeasureForNonJoiningDimension() { return IGNORE_MEASURE_FOR_NON_JOINING_DIMENSION_DEFAULT_VALUE; }

    //Property that controls the behavior of {@link Property#SOLVE_ORDER solve order} of calculated members and sets. Valid values are "scoped" and "absolute" (the default). See  {@link mondrian.olap.SolveOrderMode} for details.
    @AttributeDefinition(name = "%solveOrderMode.name", description = "%solveOrderMode.description", type = AttributeType.STRING)
    default String solveOrderMode() { return SOLVE_ORDER_MODE_DEFAULT_VALUE; }

    //Property that sets the compound slicer member solve order.
    @AttributeDefinition(name = "%compoundSlicerMemberSolveOrder.name", description = "%compoundSlicerMemberSolveOrder.description", type = AttributeType.INTEGER)
    default Integer compoundSlicerMemberSolveOrder() { return COMPOUND_SLICER_MEMBER_SOLVE_ORDER_DEFAULT_VALUE; }

    //<p>Property that controls minimum expected cardinality required in order for NativizeSet to natively evaluate a query.</p> <p>If the expected cardinality falls below this level the query is executed non-natively.</p> <p>It is possible for the actual cardinality to fall below this threshold even though the expected cardinality falls above this threshold. In this case the query will be natively evaluated.</p>
    @AttributeDefinition(name = "%nativizeMinThreshold.name", description = "%nativizeMinThreshold.description", type = AttributeType.INTEGER)
    default Integer nativizeMinThreshold() { return NATIVIZE_MIN_THRESHOLD_DEFAULT_VALUE; }

    //<p>Property that controls the maximum number of results contained in a NativizeSet result set.</p> <p>If the number of tuples contained in the result set exceeds this value Mondrian throws a LimitExceededDuringCrossjoin error.</p>
    @AttributeDefinition(name = "%nativizeMaxResults.name", description = "%nativizeMaxResults.description", type = AttributeType.INTEGER)
    default Integer nativizeMaxResults() { return NATIVIZE_MAX_RESULTS_DEFAULT_VALUE; }

    //<p>Property which governs whether child members or members of a level are precached when child or level members are requested within a query expression.  For example, if an expression references two child members in the store dimension, like <code>{ [Store].[USA].[CA], [Store].[USA].[OR] }</code>, precaching will load *all* children under [USA] rather than just the 2 requested. The threshold value is compared against the cardinality of the level to determine whether or not precaching should be performed.  If cardinality is lower than the threshold value Mondrian will precache.  Setting this property to 0 effectively disables precaching. </p>
    @AttributeDefinition(name = "%levelPreCacheThreshold.name", description = "%levelPreCacheThreshold.description", type = AttributeType.INTEGER)
    default Integer levelPreCacheThreshold() { return LEVEL_PRE_CACHE_THRESHOLD_DEFAULT_VALUE; }

    //<p> If enable, then data cache will be created for every session.</p>
    @AttributeDefinition(name = "%enableSessionCaching.name", description = "%enableSessionCaching.description", type = AttributeType.BOOLEAN)
    default Boolean enableSessionCaching() { return ENABLE_SESSION_CACHING_DEFAULT_VALUE; }

    @AttributeDefinition(name = "%executeDuration.name", description = "%executeDuration.description", type = AttributeType.LONG)
    default long executeDuration() {
         return EXECUTE_DURATION_DEFAULT_VALUE;
    }

    @AttributeDefinition(name = "%executeDurationUnit.name", description = "%executeDurationUnit.description")
    default String executeDurationUnit() {
        return EXECUTE_DURATION_UNIT_DEFAULT_VALUE;
    }

    default Optional<Duration> executeDurationValue() {
        if (executeDuration() > 0) {
            String unit = executeDurationUnit().toLowerCase();
            switch (unit) {
                case "milliseconds":
                    return Optional.of(Duration.ofMillis(executeDuration()));
                case "seconds":
                    return Optional.of(Duration.ofSeconds(executeDuration()));
                default:
                    return Optional.of(Duration.ofMillis(executeDuration()));
            }
        }
        return Optional.empty();
    }
}
