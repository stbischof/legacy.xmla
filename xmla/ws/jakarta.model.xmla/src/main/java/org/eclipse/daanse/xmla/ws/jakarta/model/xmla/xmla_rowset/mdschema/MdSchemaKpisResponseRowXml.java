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
package org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla_rowset.mdschema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import org.eclipse.daanse.xmla.ws.jakarta.model.xmla.enums.ScopeEnum;
import org.eclipse.daanse.xmla.ws.jakarta.model.xmla.xmla_rowset.Row;

import java.io.Serializable;

/**
 * This schema rowset describes the KPIs within a database.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MdSchemaKpisResponseRowXml")
public class MdSchemaKpisResponseRowXml extends Row implements Serializable {

    @XmlTransient
    private final static long serialVersionUID = 8526237824295308208L;

    /**
     * The name of the database.
     */
    @XmlElement(name = "CATALOG_NAME", required = false)
    private String catalogName;

    /**
     * The name of the schema.
     */
    @XmlElement(name = "SCHEMA_NAME", required = false)
    private String schemaName;

    /**
     * The name of the cube.
     */
    @XmlElement(name = "CUBE_NAME", required = false)
    private String cubeName;

    /**
     * The associated measure group for the KPI.
     */
    @XmlElement(name = "MEASUREGROUP_NAME", required = false)
    private String measureGroupName;

    /**
     * The name of the KPI.
     */
    @XmlElement(name = "KPI_NAME", required = false)
    private String kpiName;

    /**
     * A label or caption associated with the KPI.
     */
    @XmlElement(name = "KPI_CAPTION", required = false)
    private String kpiCaption;

    /**
     * A description of the KPI.
     */
    @XmlElement(name = "KPI_DESCRIPTION", required = false)
    private String kpiDescription;

    /**
     * The display folder.
     */
    @XmlElement(name = "KPI_DISPLAY_FOLDER", required = false)
    private String kpiDisplayFolder;

    /**
     * The unique name of the member in the measures
     * dimension for the KPI value.
     */
    @XmlElement(name = "KPI_VALUE", required = false)
    private String kpiValue;

    /**
     * The unique name of the member in the measures
     * dimension for the KPI goal.
     */
    @XmlElement(name = "KPI_GOAL", required = false)
    private String kpiGoal;

    /**
     * The unique name of the member in the measures
     * dimension for the KPI status.
     */
    @XmlElement(name = "KPI_STATUS", required = false)
    private String kpiStatus;

    /**
     * The unique name of the member in the measures
     * dimension for the KPI trend.
     */
    @XmlElement(name = "KPI_TREND", required = false)
    private String kpiTrend;

    /**
     * The default graphical representation of the KPI
     * status.
     */
    @XmlElement(name = "KPI_STATUS_GRAPHIC", required = false)
    private String kpiStatusGraphic;

    /**
     * The default graphical representation of the KPI
     * trend.
     */
    @XmlElement(name = "KPI_TREND_GRAPHIC", required = false)
    private String kpiTrendGraphic;

    /**
     * The unique name of the member in the measures
     * dimension for the KPI weight.
     */
    @XmlElement(name = "KPI_WEIGHT", required = false)
    private String kpiWight;

    /**
     * The unique name of the member in the time
     * dimension that defines the temporal context of the
     * KPI.
     */
    @XmlElement(name = "KPI_CURRENT_TIME_MEMBER", required = false)
    private String kpiCurrentTimeMember;

    /**
     * The name of the parent KPI.
     * ANNOTATIONSxsd:stringThe annotations on the KPI.
     */
    @XmlElement(name = "KPI_PARENT_KPI_NAME", required = false)
    private String kpiParentKpiName;

    /**
     * The annotations on the KPI.
     */
    @XmlElement(name = "ANNOTATIONS", required = false)
    private String annotation;
    /**
     * The scope of the KPI. The KPI can be a session KPI
     * or global KPI.
     * This column can have one of the following values:
     * 1 - Global
     * 2 – Session
     */
    @XmlElement(name = "SCOPE", required = false)
    private ScopeEnum scope;

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getCubeName() {
        return cubeName;
    }

    public void setCubeName(String cubeName) {
        this.cubeName = cubeName;
    }

    public String getMeasureGroupName() {
        return measureGroupName;
    }

    public void setMeasureGroupName(String measureGroupName) {
        this.measureGroupName = measureGroupName;
    }

    public String getKpiName() {
        return kpiName;
    }

    public void setKpiName(String kpiName) {
        this.kpiName = kpiName;
    }

    public String getKpiCaption() {
        return kpiCaption;
    }

    public void setKpiCaption(String kpiCaption) {
        this.kpiCaption = kpiCaption;
    }

    public String getKpiDescription() {
        return kpiDescription;
    }

    public void setKpiDescription(String kpiDescription) {
        this.kpiDescription = kpiDescription;
    }

    public String getKpiDisplayFolder() {
        return kpiDisplayFolder;
    }

    public void setKpiDisplayFolder(String kpiDisplayFolder) {
        this.kpiDisplayFolder = kpiDisplayFolder;
    }

    public String getKpiValue() {
        return kpiValue;
    }

    public void setKpiValue(String kpiValue) {
        this.kpiValue = kpiValue;
    }

    public String getKpiGoal() {
        return kpiGoal;
    }

    public void setKpiGoal(String kpiGoal) {
        this.kpiGoal = kpiGoal;
    }

    public String getKpiStatus() {
        return kpiStatus;
    }

    public void setKpiStatus(String kpiStatus) {
        this.kpiStatus = kpiStatus;
    }

    public String getKpiTrend() {
        return kpiTrend;
    }

    public void setKpiTrend(String kpiTrend) {
        this.kpiTrend = kpiTrend;
    }

    public String getKpiStatusGraphic() {
        return kpiStatusGraphic;
    }

    public void setKpiStatusGraphic(String kpiStatusGraphic) {
        this.kpiStatusGraphic = kpiStatusGraphic;
    }

    public String getKpiTrendGraphic() {
        return kpiTrendGraphic;
    }

    public void setKpiTrendGraphic(String kpiTrendGraphic) {
        this.kpiTrendGraphic = kpiTrendGraphic;
    }

    public String getKpiWight() {
        return kpiWight;
    }

    public void setKpiWight(String kpiWight) {
        this.kpiWight = kpiWight;
    }

    public String getKpiCurrentTimeMember() {
        return kpiCurrentTimeMember;
    }

    public void setKpiCurrentTimeMember(String kpiCurrentTimeMember) {
        this.kpiCurrentTimeMember = kpiCurrentTimeMember;
    }

    public String getKpiParentKpiName() {
        return kpiParentKpiName;
    }

    public void setKpiParentKpiName(String kpiParentKpiName) {
        this.kpiParentKpiName = kpiParentKpiName;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public ScopeEnum getScope() {
        return scope;
    }

    public void setScope(ScopeEnum scope) {
        this.scope = scope;
    }
}