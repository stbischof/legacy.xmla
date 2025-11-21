# Übergabe: XMLA-natives Check-System

## 📦 Für Repository

```
https://github.com/eclipse-daanse/org.eclipse.daanse.xmla
```

---

## 🎯 Executive Summary

Dieses Dokument übergibt ein **XMLA-natives deklaratives Check-System** für das xmla Repository. Das System basiert auf Analyse von **2200+ Tests** im legacy.xmla Repository und extrahiert wiederverwendbare Test-Patterns in ein **XMLA DISCOVER** und **XMLA EXECUTE** basiertes Modell.

---

## 📋 Übergebene Artefakte

### 1. **Ecore-Modell**
- **Datei:** `olapchecks-xmla.ecore`
- **Namespace:** `http://www.eclipse.org/daanse/xmla/checks/1.0`
- **Beschreibung:** EMF-Modell für XMLA-basierte Checks

### 2. **Dokumentation**
- `ERKENNTNISSE_ZUSAMMENFASSUNG.md` - Komplette Test-Analyse
- `XMLA_CHECKS_SPECIFICATION.md` - Vollständige Spezifikation (siehe unten)
- `XMLA_REQUEST_MAPPING.md` - XMLA Request Mapping
- `IMPLEMENTATION_GUIDE.md` - Implementierungs-Anleitung

### 3. **Beispiele**
- `example-discover-checks.xml` - DISCOVER Beispiele
- `example-execute-checks.xml` - EXECUTE Beispiele
- `example-role-checks.xml` - Rollen-Tests
- `example-integration.xml` - Vollständiges Beispiel

---

## 🏗️ Vorgeschlagene Modul-Struktur im xmla Repository

```
org.eclipse.daanse.xmla/
├── client/                           (existiert)
├── api/                              (existiert)
│
├── checks/                           (NEU)
│   │
│   ├── model/                        Maven-Modul: checks-model
│   │   ├── src/main/resources/
│   │   │   └── model/
│   │   │       └── olapchecks-xmla.ecore
│   │   ├── src/main/java-gen/      (EMF generiert)
│   │   │   └── org/eclipse/daanse/xmla/checks/model/
│   │   │       ├── CheckSuite.java
│   │   │       ├── DiscoverCheck.java
│   │   │       ├── ExecuteCheck.java
│   │   │       └── ...
│   │   └── pom.xml
│   │
│   ├── api/                          Maven-Modul: checks-api
│   │   ├── src/main/java/
│   │   │   └── org/eclipse/daanse/xmla/checks/api/
│   │   │       ├── CheckExecutor.java
│   │   │       ├── CheckResultHandler.java
│   │   │       └── ...
│   │   └── pom.xml
│   │
│   ├── executor/                     Maven-Modul: checks-executor
│   │   ├── src/main/java/
│   │   │   └── org/eclipse/daanse/xmla/checks/executor/
│   │   │       ├── XmlaCheckExecutor.java
│   │   │       ├── DiscoverCheckExecutor.java
│   │   │       ├── ExecuteCheckExecutor.java
│   │   │       └── ...
│   │   └── pom.xml
│   │
│   ├── assertj/                      Maven-Modul: checks-assertj
│   │   ├── src/main/java/
│   │   │   └── org/eclipse/daanse/xmla/checks/assertj/
│   │   │       ├── CheckResultAssert.java
│   │   │       ├── CheckSuiteResultAssert.java
│   │   │       └── Assertions.java
│   │   └── pom.xml
│   │
│   ├── junit5/                       Maven-Modul: checks-junit5
│   │   ├── src/main/java/
│   │   │   └── org/eclipse/daanse/xmla/checks/junit5/
│   │   │       ├── @XmlaCheck.java
│   │   │       ├── XmlaCheckExtension.java
│   │   │       └── ...
│   │   └── pom.xml
│   │
│   └── examples/                     Maven-Modul: checks-examples
│       ├── src/main/resources/
│       │   ├── checks/
│       │   │   ├── foodmart-schema-checks.xml
│       │   │   ├── role-based-checks.xml
│       │   │   └── integration-checks.xml
│       │   └── ...
│       ├── src/test/java/
│       │   └── examples/
│       │       ├── FoodMartChecksTest.java
│       │       └── ...
│       └── pom.xml
```

---

## 🔍 Kern-Konzepte

### 1. **XMLA DISCOVER Checks**

Basieren auf XMLA DISCOVER Requests mit Restrictions.

```xml
<discoverCheck id="sales-cube-exists"
               requestType="MDSCHEMA_CUBES"
               severity="ERROR">
  <restrictions>
    <restriction name="CATALOG_NAME" value="FoodMart"/>
    <restriction name="CUBE_NAME" value="Sales"/>
  </restrictions>
  <expectations>
    <rowCountCheck expectedCount="1" operator="GREATER_OR_EQUAL"/>
    <columnValueCheck columnName="CUBE_NAME" expectedValue="Sales"/>
  </expectations>
</discoverCheck>
```

**Entspricht XMLA:**
```xml
<Discover>
  <RequestType>MDSCHEMA_CUBES</RequestType>
  <Restrictions>
    <RestrictionList>
      <CATALOG_NAME>FoodMart</CATALOG_NAME>
      <CUBE_NAME>Sales</CUBE_NAME>
    </RestrictionList>
  </Restrictions>
</Discover>
```

### 2. **XMLA EXECUTE Checks**

Basieren auf XMLA EXECUTE Requests (MDX).

```xml
<executeCheck id="total-sales"
              statement="SELECT {[Measures].[Unit Sales]} ON COLUMNS FROM [Sales]"
              severity="ERROR">
  <properties>
    <property name="Catalog" value="FoodMart"/>
  </properties>
  <expectations>
    <expectNonEmptyResult>true</expectNonEmptyResult>
    <cellValueChecks>
      <cellValueCheck columnIndex="0" rowIndex="0"
                      expectedValue="266773"
                      comparisonMode="EXACT"/>
    </cellValueChecks>
    <performanceCheck maxExecutionTimeMs="5000"/>
  </expectations>
</executeCheck>
```

**Entspricht XMLA:**
```xml
<Execute>
  <Command>
    <Statement>SELECT {[Measures].[Unit Sales]} ON COLUMNS FROM [Sales]</Statement>
  </Command>
  <Properties>
    <PropertyList>
      <Catalog>FoodMart</Catalog>
    </PropertyList>
  </Properties>
</Execute>
```

---

## 📊 XMLA Request Type Mapping

### MDSCHEMA_* Requests (OLAP Schema)

| Request Type | Liefert | Wichtige Restrictions | Check Use Cases |
|--------------|---------|----------------------|-----------------|
| **MDSCHEMA_CUBES** | Cubes im Catalog | CATALOG_NAME, CUBE_NAME, CUBE_TYPE | Cube Existenz, Anzahl Cubes, Cube-Eigenschaften |
| **MDSCHEMA_DIMENSIONS** | Dimensions in Cube | CATALOG_NAME, CUBE_NAME, DIMENSION_UNIQUE_NAME, DIMENSION_TYPE | Dimension Existenz, Anzahl, Typ (TIME, STANDARD, MEASURE) |
| **MDSCHEMA_HIERARCHIES** | Hierarchies in Dimension | CATALOG_NAME, CUBE_NAME, DIMENSION_UNIQUE_NAME, HIERARCHY_UNIQUE_NAME | Hierarchy Existenz, HasAll, DefaultMember |
| **MDSCHEMA_LEVELS** | Levels in Hierarchy | CATALOG_NAME, CUBE_NAME, HIERARCHY_UNIQUE_NAME, LEVEL_UNIQUE_NAME, LEVEL_NUMBER | Level Existenz, Anzahl, Depth, LevelType |
| **MDSCHEMA_MEMBERS** | Members auf Level | CATALOG_NAME, CUBE_NAME, HIERARCHY_UNIQUE_NAME, LEVEL_UNIQUE_NAME, MEMBER_UNIQUE_NAME | Member Existenz, Anzahl, Caption, Type |
| **MDSCHEMA_MEASURES** | Measures in Cube | CATALOG_NAME, CUBE_NAME, MEASURE_UNIQUE_NAME | Measure Existenz, FormatString, Aggregator |
| **MDSCHEMA_PROPERTIES** | Member Properties | PROPERTY_NAME, PROPERTY_TYPE | Property Existenz, Type |
| **MDSCHEMA_SETS** | Named Sets | CATALOG_NAME, CUBE_NAME, SET_NAME | Set Existenz |

### DBSCHEMA_* Requests (Database Schema)

| Request Type | Liefert | Wichtige Restrictions | Check Use Cases |
|--------------|---------|----------------------|-----------------|
| **DBSCHEMA_CATALOGS** | Verfügbare Catalogs | CATALOG_NAME | Catalog Existenz |
| **DBSCHEMA_TABLES** | Tabellen | CATALOG_NAME, SCHEMA_NAME, TABLE_NAME, TABLE_TYPE | Tabelle Existenz (rollenbasiert) |
| **DBSCHEMA_COLUMNS** | Spalten | CATALOG_NAME, TABLE_NAME, COLUMN_NAME | Spalte Existenz |

### DISCOVER_* Requests (Server Info)

| Request Type | Liefert | Check Use Cases |
|--------------|---------|-----------------|
| **DISCOVER_DATASOURCES** | Data Sources | Server-Verbindung |
| **DISCOVER_PROPERTIES** | Server Properties | Feature-Support |
| **DISCOVER_SCHEMA_ROWSETS** | Verfügbare Rowsets | Server-Capabilities |

---

## 🎨 Check-Typen

### DISCOVER Check-Expectations

#### 1. **RowCountCheck**
Prüft Anzahl zurückgegebener Rows.

```xml
<rowCountCheck expectedCount="3" operator="GREATER_OR_EQUAL"/>
```

**Operators:**
- `EQUALS`: Genau N Rows
- `GREATER_THAN`: Mehr als N
- `LESS_THAN`: Weniger als N
- `GREATER_OR_EQUAL`: Mindestens N
- `LESS_OR_EQUAL`: Höchstens N
- `NOT_EQUALS`: Nicht N

#### 2. **ColumnValueCheck**
Prüft Wert(e) in einer Spalte.

```xml
<!-- Prüfe Spalte in erster Row -->
<columnValueCheck columnName="CUBE_NAME"
                  expectedValue="Sales"
                  rowIndex="0"/>

<!-- Prüfe Spalte in allen Rows -->
<columnValueCheck columnName="DIMENSION_TYPE"
                  expectedValue="2"
                  checkAllRows="true"/>
```

**Comparison Modes:**
- `EXACT`: Exakter String-Vergleich
- `CONTAINS`: String enthält Wert
- `REGEX`: Regex-Match
- `NUMERIC_EQUALS`: Numerischer Vergleich
- `NUMERIC_TOLERANCE`: Mit Toleranz

#### 3. **ColumnNotNullCheck**
Prüft, dass Spalte nicht NULL ist.

```xml
<columnNotNullCheck columnName="DEFAULT_FORMAT_STRING" checkAllRows="true"/>
```

#### 4. **ColumnPatternCheck**
Prüft Spalte gegen Regex-Pattern.

```xml
<columnPatternCheck columnName="CUBE_NAME"
                    pattern="^Sales.*"
                    checkAllRows="true"/>
```

### EXECUTE Check-Expectations

#### 1. **AxisCheck**
Prüft Achsen im MDX-Ergebnis.

```xml
<axisCheck axisOrdinal="0"
           expectedPositionCount="1"
           operator="EQUALS"/>
```

#### 2. **CellValueCheck**
Prüft Zellwerte.

```xml
<!-- Per Ordinal -->
<cellValueCheck cellOrdinal="0"
                expectedValue="266773"
                comparisonMode="EXACT"/>

<!-- Per Row/Column -->
<cellValueCheck rowIndex="0"
                columnIndex="0"
                expectedValue="266773"
                comparisonMode="NUMERIC_EQUALS"
                tolerance="0.01"/>

<!-- Formatted Value -->
<cellValueCheck rowIndex="0"
                columnIndex="0"
                expectedFormattedValue="$266,773.00"/>
```

#### 3. **PerformanceCheck**
Prüft Performance.

```xml
<performanceCheck maxExecutionTimeMs="5000"
                  maxNetworkTimeMs="1000"/>
```

---

## 🔐 Rollenbasierte Tests

### Drei Ansätze für Rollen-Tests

#### 1. **Global: Connection-Level**
```xml
<checkSuite name="Role Tests">
  <connection url="http://localhost:8080/xmla">
    <defaultProperties>
      <property name="Catalog" value="FoodMart"/>
      <property name="Roles" value="California manager"/>
    </defaultProperties>
  </connection>

  <!-- Alle Checks verwenden diese Rolle -->
  <checks xsi:type="discoverCheck" requestType="MDSCHEMA_CUBES">
    <expectations>
      <rowCountCheck expectedCount="1"/>
    </expectations>
  </checks>
</checkSuite>
```

#### 2. **Per Check: Properties Override**
```xml
<checkSuite name="Multi-Role Tests">
  <connection url="http://localhost:8080/xmla"/>

  <!-- Check mit Manager-Rolle -->
  <checks xsi:type="discoverCheck" requestType="MDSCHEMA_CUBES">
    <properties>
      <property name="Catalog" value="FoodMart"/>
      <property name="Roles" value="Manager"/>
    </properties>
    <expectations>
      <rowCountCheck expectedCount="3"/>
    </expectations>
  </checks>

  <!-- Check mit User-Rolle -->
  <checks xsi:type="discoverCheck" requestType="MDSCHEMA_CUBES">
    <properties>
      <property name="Catalog" value="FoodMart"/>
      <property name="Roles" value="User"/>
    </properties>
    <expectations>
      <rowCountCheck expectedCount="1"/>
    </expectations>
  </checks>
</checkSuite>
```

#### 3. **Session-basiert** (mit XMLA Session Support)
```java
// Für fortgeschrittene Implementierung
Session session1 = client.createSession();
session1.setProperty("Roles", "Manager");

Session session2 = client.createSession();
session2.setProperty("Roles", "User");

executor.execute(suite, session1); // Verwendet Manager-Rolle
executor.execute(suite, session2); // Verwendet User-Rolle
```

---

## 📝 Vollständige Beispiele

### Beispiel 1: Schema Validation nach Build

```xml
<?xml version="1.0" encoding="UTF-8"?>
<xmlachecks:CheckSuite xmi:version="2.0"
    xmlns:xmi="http://www.omg.org/XMI"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:xmlachecks="http://www.eclipse.org/daanse/xmla/checks/1.0"
    name="Post-Build Schema Validation"
    description="Validates schema structure after build"
    defaultSeverity="ERROR">

  <connection url="http://localhost:8080/xmla">
    <defaultProperties>
      <property name="Catalog" value="FoodMart"/>
    </defaultProperties>
  </connection>

  <!-- Check 1: Alle erwarteten Cubes existieren -->
  <checks xsi:type="xmlachecks:DiscoverCheck"
          id="all-cubes-exist"
          requestType="MDSCHEMA_CUBES"
          description="Should have 7 cubes">
    <expectations>
      <rowCountCheck expectedCount="7" operator="EQUALS"/>
    </expectations>
  </checks>

  <!-- Check 2: Sales Cube existiert -->
  <checks xsi:type="xmlachecks:DiscoverCheck"
          id="sales-cube-exists"
          requestType="MDSCHEMA_CUBES">
    <restrictions>
      <restriction name="CUBE_NAME" value="Sales"/>
    </restrictions>
    <expectations>
      <rowCountCheck expectedCount="1" operator="GREATER_OR_EQUAL"/>
      <columnValueCheck columnName="CUBE_NAME" expectedValue="Sales"/>
      <columnValueCheck columnName="CUBE_TYPE" expectedValue="CUBE"/>
    </expectations>
  </checks>

  <!-- Check 3: Sales hat TIME Dimension -->
  <checks xsi:type="xmlachecks:DiscoverCheck"
          id="sales-time-dimension"
          requestType="MDSCHEMA_DIMENSIONS">
    <restrictions>
      <restriction name="CUBE_NAME" value="Sales"/>
      <restriction name="DIMENSION_TYPE" value="2"/> <!-- TIME -->
    </restrictions>
    <expectations>
      <rowCountCheck expectedCount="1" operator="GREATER_OR_EQUAL"/>
      <columnValueCheck columnName="DIMENSION_TYPE" expectedValue="2" checkAllRows="true"/>
    </expectations>
  </checks>

  <!-- Check 4: TIME Dimension hat mindestens 3 Levels -->
  <checks xsi:type="xmlachecks:DiscoverCheck"
          id="time-hierarchy-levels"
          requestType="MDSCHEMA_LEVELS">
    <restrictions>
      <restriction name="CUBE_NAME" value="Sales"/>
      <restriction name="HIERARCHY_UNIQUE_NAME" value="[Time].[Time]"/>
    </restrictions>
    <expectations>
      <rowCountCheck expectedCount="3" operator="GREATER_OR_EQUAL"/>
      <columnNotNullCheck columnName="LEVEL_UNIQUE_NAME" checkAllRows="true"/>
      <columnNotNullCheck columnName="LEVEL_NUMBER" checkAllRows="true"/>
    </expectations>
  </checks>

  <!-- Check 5: Alle Measures haben FormatString -->
  <checks xsi:type="xmlachecks:DiscoverCheck"
          id="measures-have-format"
          requestType="MDSCHEMA_MEASURES"
          severity="WARN">
    <restrictions>
      <restriction name="CUBE_NAME" value="Sales"/>
    </restrictions>
    <expectations>
      <columnNotNullCheck columnName="DEFAULT_FORMAT_STRING" checkAllRows="true"/>
    </expectations>
  </checks>

  <!-- Check 6: MDX Query liefert erwartetes Ergebnis -->
  <checks xsi:type="xmlachecks:ExecuteCheck"
          id="total-sales-query"
          statement="SELECT {[Measures].[Unit Sales]} ON COLUMNS FROM [Sales]">
    <expectations>
      <expectNonEmptyResult>true</expectNonEmptyResult>
      <axisChecks>
        <axisCheck axisOrdinal="0" expectedPositionCount="1"/>
      </axisChecks>
      <cellValueChecks>
        <cellValueCheck rowIndex="0" columnIndex="0"
                        expectedValue="266773"
                        comparisonMode="EXACT"/>
      </cellValueChecks>
      <performanceCheck maxExecutionTimeMs="5000"/>
    </expectations>
  </checks>

</xmlachecks:CheckSuite>
```

### Beispiel 2: Rollenbasierte Zugriffstests

```xml
<?xml version="1.0" encoding="UTF-8"?>
<xmlachecks:CheckSuite xmi:version="2.0"
    xmlns:xmi="http://www.omg.org/XMI"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:xmlachecks="http://www.eclipse.org/daanse/xmla/checks/1.0"
    name="Role-Based Access Tests">

  <connection url="http://localhost:8080/xmla">
    <defaultProperties>
      <property name="Catalog" value="FoodMart"/>
    </defaultProperties>
  </connection>

  <!-- California manager: Sieht nur Sales Cube -->
  <checks xsi:type="xmlachecks:DiscoverCheck"
          id="manager-cubes"
          requestType="MDSCHEMA_CUBES">
    <properties>
      <property name="Roles" value="California manager"/>
    </properties>
    <expectations>
      <rowCountCheck expectedCount="1" operator="EQUALS"/>
      <columnValueCheck columnName="CUBE_NAME" expectedValue="Sales" rowIndex="0"/>
    </expectations>
  </checks>

  <!-- Admin: Sieht alle Cubes -->
  <checks xsi:type="xmlachecks:DiscoverCheck"
          id="admin-cubes"
          requestType="MDSCHEMA_CUBES">
    <properties>
      <property name="Roles" value="Admin"/>
    </properties>
    <expectations>
      <rowCountCheck expectedCount="7" operator="EQUALS"/>
    </expectations>
  </checks>

  <!-- User: Sieht nur Sales Cube, aber nicht alle Dimensionen -->
  <checks xsi:type="xmlachecks:DiscoverCheck"
          id="user-dimensions"
          requestType="MDSCHEMA_DIMENSIONS">
    <restrictions>
      <restriction name="CUBE_NAME" value="Sales"/>
    </restrictions>
    <properties>
      <property name="Roles" value="User"/>
    </properties>
    <expectations>
      <rowCountCheck expectedCount="5" operator="LESS_OR_EQUAL"/>
    </expectations>
  </checks>

  <!-- Admin: Sieht alle Dimensionen -->
  <checks xsi:type="xmlachecks:DiscoverCheck"
          id="admin-dimensions"
          requestType="MDSCHEMA_DIMENSIONS">
    <restrictions>
      <restriction name="CUBE_NAME" value="Sales"/>
    </restrictions>
    <properties>
      <property name="Roles" value="Admin"/>
    </properties>
    <expectations>
      <rowCountCheck expectedCount="8" operator="EQUALS"/>
    </expectations>
  </checks>

  <!-- REG1 Rolle: Time.Time Hierarchy nicht sichtbar -->
  <checks xsi:type="xmlachecks:DiscoverCheck"
          id="reg1-time-hierarchy-blocked"
          requestType="MDSCHEMA_HIERARCHIES">
    <restrictions>
      <restriction name="CUBE_NAME" value="Sales"/>
      <restriction name="DIMENSION_UNIQUE_NAME" value="[Time]"/>
      <restriction name="HIERARCHY_UNIQUE_NAME" value="[Time].[Time]"/>
    </restrictions>
    <properties>
      <property name="Roles" value="REG1"/>
    </properties>
    <expectations>
      <rowCountCheck expectedCount="0" operator="EQUALS"/>
    </expectations>
  </checks>

  <!-- REG1 Rolle: Time.Weekly Hierarchy sichtbar -->
  <checks xsi:type="xmlachecks:DiscoverCheck"
          id="reg1-weekly-hierarchy-visible"
          requestType="MDSCHEMA_HIERARCHIES">
    <restrictions>
      <restriction name="CUBE_NAME" value="Sales"/>
      <restriction name="DIMENSION_UNIQUE_NAME" value="[Time]"/>
      <restriction name="HIERARCHY_UNIQUE_NAME" value="[Time].[Weekly]"/>
    </restrictions>
    <properties>
      <property name="Roles" value="REG1"/>
    </properties>
    <expectations>
      <rowCountCheck expectedCount="1" operator="EQUALS"/>
    </expectations>
  </checks>

</xmlachecks:CheckSuite>
```

### Beispiel 3: Datenqualitäts-Checks

```xml
<?xml version="1.0" encoding="UTF-8"?>
<xmlachecks:CheckSuite xmi:version="2.0"
    xmlns:xmi="http://www.omg.org/XMI"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:xmlachecks="http://www.eclipse.org/daanse/xmla/checks/1.0"
    name="Data Quality Checks"
    defaultSeverity="WARN">

  <connection url="http://localhost:8080/xmla">
    <defaultProperties>
      <property name="Catalog" value="FoodMart"/>
    </defaultProperties>
  </connection>

  <!-- Alle Cubes sollten Description haben -->
  <checks xsi:type="xmlachecks:DiscoverCheck"
          id="cubes-have-description"
          requestType="MDSCHEMA_CUBES">
    <expectations>
      <columnNotNullCheck columnName="DESCRIPTION" checkAllRows="true"/>
    </expectations>
  </checks>

  <!-- Alle Dimensions sollten Caption haben -->
  <checks xsi:type="xmlachecks:DiscoverCheck"
          id="dimensions-have-caption"
          requestType="MDSCHEMA_DIMENSIONS">
    <restrictions>
      <restriction name="CUBE_NAME" value="Sales"/>
    </restrictions>
    <expectations>
      <columnNotNullCheck columnName="DIMENSION_CAPTION" checkAllRows="true"/>
    </expectations>
  </checks>

  <!-- Alle Measures sollten FormatString haben -->
  <checks xsi:type="xmlachecks:DiscoverCheck"
          id="measures-have-format"
          requestType="MDSCHEMA_MEASURES">
    <restrictions>
      <restriction name="CUBE_NAME" value="Sales"/>
    </restrictions>
    <expectations>
      <columnNotNullCheck columnName="DEFAULT_FORMAT_STRING" checkAllRows="true"/>
    </expectations>
  </checks>

  <!-- Alle Levels sollten KeyColumn haben -->
  <checks xsi:type="xmlachecks:DiscoverCheck"
          id="levels-have-key-column"
          requestType="MDSCHEMA_LEVELS"
          severity="ERROR">
    <restrictions>
      <restriction name="CUBE_NAME" value="Sales"/>
    </restrictions>
    <expectations>
      <columnNotNullCheck columnName="LEVEL_KEY_SQL_COLUMN_NAME" checkAllRows="true"/>
    </expectations>
  </checks>

  <!-- Measure Namen sollten Pattern folgen -->
  <checks xsi:type="xmlachecks:DiscoverCheck"
          id="measure-naming-convention"
          requestType="MDSCHEMA_MEASURES"
          severity="INFO">
    <restrictions>
      <restriction name="CUBE_NAME" value="Sales"/>
    </restrictions>
    <expectations>
      <columnPatternCheck columnName="MEASURE_NAME"
                          pattern="^[A-Z][a-zA-Z ]+$"
                          checkAllRows="true"/>
    </expectations>
  </checks>

</xmlachecks:CheckSuite>
```

---

## 🚀 Implementierungs-Guide

### Phase 1: Setup & Model (Woche 1-2)

#### 1.1 Maven-Module erstellen
```xml
<!-- Parent POM -->
<modules>
  <module>checks-model</module>
  <module>checks-api</module>
  <module>checks-executor</module>
  <module>checks-assertj</module>
  <module>checks-junit5</module>
  <module>checks-examples</module>
</modules>
```

#### 1.2 EMF Code Generation
```bash
# checks-model/pom.xml
<plugin>
  <groupId>org.eclipse.emf</groupId>
  <artifactId>emf-maven-plugin</artifactId>
  <configuration>
    <models>
      <model>model/olapchecks-xmla.ecore</model>
    </models>
  </configuration>
</plugin>
```

#### 1.3 Dependencies
```xml
<!-- checks-executor -->
<dependencies>
  <dependency>
    <groupId>org.eclipse.daanse.xmla</groupId>
    <artifactId>client</artifactId>
  </dependency>
  <dependency>
    <groupId>org.eclipse.daanse.xmla</groupId>
    <artifactId>checks-model</artifactId>
  </dependency>
</dependencies>
```

### Phase 2: Core Implementation (Woche 3-4)

#### 2.1 CheckExecutor Interface
```java
package org.eclipse.daanse.xmla.checks.api;

public interface CheckExecutor {
    CheckSuiteResult execute(CheckSuite suite);
    CheckSuiteResult execute(CheckSuite suite, ExecutionContext context);
    CheckResult executeCheck(Check check);
}
```

#### 2.2 XmlaCheckExecutor Implementation
```java
package org.eclipse.daanse.xmla.checks.executor;

public class XmlaCheckExecutor implements CheckExecutor {
    private final XmlaClient client;

    public XmlaCheckExecutor(XmlaClient client) {
        this.client = client;
    }

    @Override
    public CheckSuiteResult execute(CheckSuite suite) {
        // Implementation
    }

    private CheckResult executeDiscoverCheck(DiscoverCheck check) {
        // Build XMLA DISCOVER request
        // Execute via XmlaClient
        // Validate expectations
        // Return CheckResult
    }

    private CheckResult executeExecuteCheck(ExecuteCheck check) {
        // Build XMLA EXECUTE request
        // Execute via XmlaClient
        // Validate expectations
        // Return CheckResult
    }
}
```

#### 2.3 DiscoverCheckExecutor
```java
package org.eclipse.daanse.xmla.checks.executor;

public class DiscoverCheckExecutor {
    public CheckResult execute(DiscoverCheck check, XmlaClient client) {
        // 1. Build DISCOVER request
        DiscoverRequest request = buildDiscoverRequest(check);

        // 2. Execute
        DiscoverResponse response = client.discover(request);

        // 3. Validate expectations
        List<ValidationError> errors = validateExpectations(
            check.getExpectations(),
            response
        );

        // 4. Create CheckResult
        return createCheckResult(check, response, errors);
    }

    private DiscoverRequest buildDiscoverRequest(DiscoverCheck check) {
        DiscoverRequest request = new DiscoverRequest();
        request.setRequestType(check.getRequestType().getLiteral());

        // Add restrictions
        for (Restriction restriction : check.getRestrictions()) {
            request.addRestriction(restriction.getName(), restriction.getValue());
        }

        // Add properties
        for (Property property : check.getProperties()) {
            request.addProperty(property.getName(), property.getValue());
        }

        return request;
    }
}
```

### Phase 3: AssertJ Integration (Woche 5)

#### 3.1 CheckResultAssert
```java
package org.eclipse.daanse.xmla.checks.assertj;

public class CheckResultAssert extends AbstractAssert<CheckResultAssert, CheckResult> {

    public static CheckResultAssert assertThat(CheckResult actual) {
        return new CheckResultAssert(actual);
    }

    public CheckResultAssert isSuccessful() {
        isNotNull();
        if (!actual.isSuccess()) {
            failWithMessage("Expected check to be successful but was: %s",
                            actual.getMessage());
        }
        return this;
    }

    public CheckResultAssert hasExpectedValue(String expected) {
        isNotNull();
        if (!Objects.equals(actual.getExpectedValue(), expected)) {
            failWithMessage("Expected value <%s> but was <%s>",
                            expected, actual.getExpectedValue());
        }
        return this;
    }
}
```

#### 3.2 SoftAssertions Support
```java
SoftAssertions softly = new SoftAssertions();
for (CheckResult result : suite.getResults()) {
    softly.assertThat(result)
          .as("Check %s", result.getCheckId())
          .isSuccessful();
}
softly.assertAll();
```

### Phase 4: JUnit 5 Integration (Woche 6)

#### 4.1 @XmlaCheck Annotation
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(XmlaCheckExtension.class)
public @interface XmlaCheck {
    String value(); // Path to check suite XML
    String url() default "http://localhost:8080/xmla";
}
```

#### 4.2 Usage
```java
@Test
@XmlaCheck("classpath:checks/foodmart-checks.xml")
void testFoodMartSchema(CheckSuiteResult result) {
    assertThat(result.getFailedChecks()).isZero();
}
```

---

## 📊 Performance Considerations

### Parallele DISCOVER Requests
```java
List<DiscoverCheck> checks = suite.getDiscoverChecks();
List<CompletableFuture<CheckResult>> futures = checks.stream()
    .map(check -> CompletableFuture.supplyAsync(() ->
        executor.executeCheck(check)))
    .collect(Collectors.toList());

List<CheckResult> results = futures.stream()
    .map(CompletableFuture::join)
    .collect(Collectors.toList());
```

### Connection Pooling
```java
public class PooledXmlaCheckExecutor implements CheckExecutor {
    private final XmlaConnectionPool pool;

    @Override
    public CheckSuiteResult execute(CheckSuite suite) {
        try (XmlaClient client = pool.borrowClient()) {
            return doExecute(suite, client);
        }
    }
}
```

---

## 📝 Übergabe-Checkliste

### Artefakte
- ✅ `olapchecks-xmla.ecore` - XMLA-Ecore-Modell
- ✅ `ERKENNTNISSE_ZUSAMMENFASSUNG.md` - Komplette Analyse
- ✅ `XMLA_REPOSITORY_HANDOVER.md` - Diese Datei
- ✅ `XMLA_REQUEST_MAPPING.md` - Request Mapping
- ⬜ `IMPLEMENTATION_GUIDE.md` - Detaillierte Implementierung
- ⬜ `example-discover-checks.xml` - Beispiele
- ⬜ `example-execute-checks.xml` - Beispiele
- ⬜ `example-role-checks.xml` - Beispiele
- ⬜ `example-integration.xml` - Vollständiges Beispiel

### Dokumentation
- ✅ Konzept dokumentiert
- ✅ XMLA Mapping dokumentiert
- ⬜ API JavaDoc
- ⬜ User Guide
- ⬜ Developer Guide
- ⬜ Best Practices

### Code (Optional für PoC)
- ⬜ Maven Module Setup
- ⬜ EMF Code Generation
- ⬜ Basic XmlaCheckExecutor
- ⬜ AssertJ Integration
- ⬜ Beispiel-Tests

---

## 🎯 Nächste Schritte

1. **Review** - Review dieser Übergabe-Doku
2. **Feedback** - Feedback zu Konzept und Design
3. **Planning** - Sprint-Planning für Implementierung
4. **Implementation** - Schrittweise Umsetzung
5. **Testing** - Integration-Tests mit FoodMart
6. **Release** - Alpha-Release für Community-Feedback

---

**Status:** Ready for Handover
**Ziel-Repository:** https://github.com/eclipse-daanse/org.eclipse.daanse.xmla
**Contact:** Eclipse Daanse Community

---

**Lizenz:** EPL-2.0
**Version:** 1.0
**Datum:** 2025-11-21
