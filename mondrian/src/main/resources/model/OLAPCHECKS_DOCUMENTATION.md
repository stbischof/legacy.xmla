# OLAP Checks - Deklaratives Test- und Validierungssystem

## Übersicht

Das **OLAP Checks** Ecore-Modell ermöglicht die **deklarative Definition** von Tests und Validierungen für OLAP-Modelle **ohne Code zu schreiben**. Es basiert auf der Analyse von über 2200 existierenden Tests im Repository und extrahiert wiederverwendbare Test-Patterns.

## Konzept

### Ziel
Regelmäßige Prüfung von OLAP-Cubes, insbesondere:
- **Bei Updates** der Datenbank oder des Schemas
- **Nach dem Build** von Mapping-Modellen
- **In Integration-Tests** für CI/CD-Pipelines
- **Manuelle Validierung** von Cube-Definitionen

### Kernidee
Anstatt für jeden Test neuen Java-Code zu schreiben, wird ein **Check-Modell** als EMF-Instanz erstellt, das:
1. Deklarativ definiert, **was** getestet werden soll
2. Automatisch ausgeführt werden kann
3. Ergebnisse als strukturierte Daten liefert
4. Mit AssertJ Soft-Assertions integriert werden kann

### Zwei Ausführungsmodi

#### 1. OLAP_API (Direct Access)
Direkter Zugriff über:
- `org.eclipse.daanse.olap.api.connection.Connection`
- `org.eclipse.daanse.olap.api.CatalogReader`
- Schnell, für lokale Tests und während der Entwicklung

#### 2. XMLA (Remote Access)
Zugriff über XMLA-Client:
- `org.eclipse.daanse.xmla.client` (aus https://github.com/eclipse-daanse/org.eclipse.daanse.xmla)
- Simuliert echte Client-Zugriffe
- Ideal für Integrationstests und Produktionsvalidierung

---

## Modell-Struktur

### CheckSuite (Root)
Container für alle Checks einer Test-Suite.

```xml
<CheckSuite name="MyOLAPValidation"
            catalogName="FoodMart"
            executionMode="OLAP_API"
            roleName="California manager">
  <checks>...</checks>
</CheckSuite>
```

**Attribute:**
- `name`: Name der Test-Suite
- `description`: Beschreibung
- `executionMode`: `OLAP_API` oder `XMLA`
- `catalogName`: Name des Catalogs
- `roleName`: (Optional) Rolle für rollenbasierten Zugriff
- `connectionConfig`: (Optional) XMLA-Verbindungskonfiguration

---

## Check-Kategorien

### 1. Existenz-Checks
Prüfen, ob Elemente existieren (oder nicht existieren).

#### CatalogExistsCheck
```xml
<checks xsi:type="CatalogExistsCheck"
        id="catalog-exists"
        description="Catalog FoodMart muss existieren"
        elementName="FoodMart"
        shouldExist="true"
        severity="ERROR"/>
```

#### CubeExistsCheck
```xml
<checks xsi:type="CubeExistsCheck"
        id="sales-cube-exists"
        description="Sales Cube muss existieren"
        cubeName="Sales"
        shouldExist="true"
        severity="ERROR"/>
```

#### DimensionExistsCheck
```xml
<checks xsi:type="DimensionExistsCheck"
        id="store-dimension-exists"
        cubeName="Sales"
        dimensionName="Store"
        shouldExist="true"
        severity="ERROR"/>
```

#### HierarchyExistsCheck
```xml
<checks xsi:type="HierarchyExistsCheck"
        id="time-hierarchy-exists"
        cubeName="Sales"
        dimensionName="Time"
        hierarchyName="Time"
        shouldExist="true"
        severity="ERROR"/>
```

#### LevelExistsCheck
```xml
<checks xsi:type="LevelExistsCheck"
        id="year-level-exists"
        cubeName="Sales"
        dimensionName="Time"
        hierarchyName="Time"
        levelName="Year"
        shouldExist="true"
        severity="ERROR"/>
```

#### MemberExistsCheck
```xml
<checks xsi:type="MemberExistsCheck"
        id="member-1997-exists"
        memberUniqueName="[Time].[Time].[1997]"
        shouldExist="true"
        severity="ERROR"/>
```

#### MeasureExistsCheck
```xml
<checks xsi:type="MeasureExistsCheck"
        id="unit-sales-exists"
        cubeName="Sales"
        measureName="Unit Sales"
        shouldExist="true"
        severity="ERROR"/>
```

#### CalculatedMemberExistsCheck
```xml
<checks xsi:type="CalculatedMemberExistsCheck"
        id="profit-calc-exists"
        cubeName="Sales"
        calculatedMemberUniqueName="[Measures].[Profit]"
        shouldExist="true"
        severity="WARN"/>
```

---

### 2. Kardinalitäts-Checks
Prüfen die Anzahl von Elementen.

#### CubeCountCheck
```xml
<checks xsi:type="CubeCountCheck"
        id="cube-count-for-role"
        description="California manager sollte nur 1 Cube sehen"
        expectedCount="1"
        comparisonOperator="EQUALS"
        severity="ERROR"/>
```

**ComparisonOperator:**
- `EQUALS`: Genau N
- `GREATER_THAN`: Mehr als N
- `LESS_THAN`: Weniger als N
- `GREATER_OR_EQUAL`: Mindestens N
- `LESS_OR_EQUAL`: Höchstens N

#### DimensionCountCheck
```xml
<checks xsi:type="DimensionCountCheck"
        id="sales-dimension-count"
        cubeName="Sales"
        expectedCount="8"
        comparisonOperator="EQUALS"
        severity="ERROR"/>
```

#### LevelCountCheck
```xml
<checks xsi:type="LevelCountCheck"
        id="time-level-count"
        cubeName="Sales"
        dimensionName="Time"
        hierarchyName="Time"
        expectedCount="4"
        comparisonOperator="EQUALS"
        severity="ERROR"/>
```

#### MemberCountCheck
```xml
<checks xsi:type="MemberCountCheck"
        id="year-member-count"
        cubeName="Sales"
        dimensionName="Time"
        hierarchyName="Time"
        levelName="Year"
        expectedCount="1"
        comparisonOperator="EQUALS"
        severity="ERROR"/>

<!-- Oder: Anzahl Children eines Members -->
<checks xsi:type="MemberCountCheck"
        id="1997-children-count"
        cubeName="Sales"
        dimensionName="Time"
        hierarchyName="Time"
        parentMemberUniqueName="[Time].[Time].[1997]"
        expectedCount="4"
        comparisonOperator="EQUALS"
        severity="ERROR"/>
```

---

### 3. Eigenschaften-Checks

#### ElementPropertyCheck
Prüft beliebige Eigenschaften von Elementen.

```xml
<checks xsi:type="ElementPropertyCheck"
        id="cube-caption"
        elementType="CUBE"
        elementUniqueName="Sales"
        propertyName="CAPTION"
        expectedValue="Sales"
        severity="WARN"/>
```

**ElementType:**
- `CATALOG`, `CUBE`, `DIMENSION`, `HIERARCHY`, `LEVEL`, `MEMBER`, `MEASURE`

#### DescriptionCheck
Prüft, ob Elemente Beschreibungen haben.

```xml
<checks xsi:type="DescriptionCheck"
        id="cube-has-description"
        elementType="CUBE"
        elementUniqueName="Sales"
        shouldHaveDescription="true"
        severity="WARN"/>
```

#### CaptionCheck
Prüft Captions von Elementen.

```xml
<checks xsi:type="CaptionCheck"
        id="dimension-caption"
        elementType="DIMENSION"
        elementUniqueName="[Store]"
        expectedCaption="Store"
        severity="INFO"/>
```

#### FormatStringCheck
Prüft Format Strings von Measures.

```xml
<checks xsi:type="FormatStringCheck"
        id="unit-sales-format"
        cubeName="Sales"
        measureName="Unit Sales"
        expectedFormatString="Standard"
        shouldHaveFormatString="true"
        severity="WARN"/>
```

---

### 4. Zugriffskontroll-Checks
Prüfen rollenbasierte Sichtbarkeit.

#### CubeAccessCheck
```xml
<checks xsi:type="CubeAccessCheck"
        id="sales-accessible-for-manager"
        cubeName="Sales"
        roleName="California manager"
        shouldBeAccessible="true"
        severity="ERROR"/>

<checks xsi:type="CubeAccessCheck"
        id="hr-not-accessible"
        cubeName="HR"
        roleName="California manager"
        shouldBeAccessible="false"
        severity="ERROR"/>
```

#### DimensionAccessCheck
```xml
<checks xsi:type="DimensionAccessCheck"
        id="store-dim-accessible"
        cubeName="Sales"
        dimensionName="Store"
        roleName="California manager"
        shouldBeAccessible="true"
        severity="ERROR"/>
```

#### HierarchyAccessCheck
```xml
<checks xsi:type="HierarchyAccessCheck"
        id="time-hierarchy-blocked"
        cubeName="Sales"
        dimensionName="Time"
        hierarchyName="Time"
        roleName="REG1"
        shouldBeAccessible="false"
        severity="ERROR"/>
```

#### MemberAccessCheck
```xml
<checks xsi:type="MemberAccessCheck"
        id="member-1997-accessible"
        memberUniqueName="[Time].[Time].[1997]"
        roleName="Role1"
        shouldBeAccessible="true"
        severity="ERROR"/>
```

---

### 5. MDX Query-Checks
Ausführung und Validierung von MDX-Queries.

```xml
<checks xsi:type="MdxQueryCheck"
        id="simple-query"
        cubeName="Sales"
        mdxQuery="SELECT {[Measures].[Unit Sales]} ON COLUMNS FROM Sales"
        expectNonEmptyResult="true"
        expectedRowCount="1"
        expectedColumnCount="1"
        maxExecutionTimeMs="5000"
        severity="ERROR">

  <!-- Cell-Wert-Checks -->
  <cellValueChecks rowIndex="0"
                   columnIndex="0"
                   expectedValue="266773"
                   valueComparisonMode="EXACT"/>
</checks>
```

**ValueComparisonMode:**
- `EXACT`: Exakter String-Vergleich
- `NUMERIC_TOLERANCE`: Numerischer Vergleich mit Toleranz
- `CONTAINS`: String enthält Wert
- `REGEX`: Regex-Match

---

### 6. Datenqualitäts-Checks

#### AllMembersHaveDescriptionCheck
Prüft, ob **alle** Members auf einem Level Descriptions haben.

```xml
<checks xsi:type="AllMembersHaveDescriptionCheck"
        id="all-products-have-desc"
        cubeName="Sales"
        dimensionName="Product"
        hierarchyName="Product"
        levelName="Product Name"
        severity="WARN"/>
```

#### AllMeasuresHaveFormatStringCheck
Prüft, ob **alle** Measures in einem Cube Format Strings haben.

```xml
<checks xsi:type="AllMeasuresHaveFormatStringCheck"
        id="all-measures-formatted"
        cubeName="Sales"
        severity="WARN"/>
```

---

### 7. Database Schema-Checks
Für rollenbasierte Zugriffstests auf Datenbankebene.

```xml
<checks xsi:type="DatabaseSchemaCheck"
        id="db-schema-tables"
        schemaName="foodmart"
        expectedTableCount="23"
        severity="INFO"/>

<checks xsi:type="DatabaseSchemaCheck"
        id="sales-fact-columns"
        schemaName="foodmart"
        tableName="sales_fact_1997"
        expectedColumnCount="8"
        severity="ERROR"/>
```

---

## Gemeinsame Attribute

Alle Checks haben folgende Basisattribute:

- **`id`**: Eindeutige ID des Checks
- **`description`**: Beschreibung (optional)
- **`severity`**: `INFO`, `WARN`, oder `ERROR`
- **`enabled`**: `true`/`false` - Check aktivieren/deaktivieren
- **`clearCacheBefore`**: `true`/`false` - Cache vor Check leeren

---

## Verwendung in Tests

### Beispiel: AssertJ Integration

```java
@Test
void testOlapModel() {
    // 1. Check Suite laden
    CheckSuite suite = loadCheckSuite("my-checks.xml");

    // 2. Executor erstellen
    CheckExecutor executor = new OlapApiCheckExecutor(connection);
    // oder: new XmlaCheckExecutor(xmlaClient);

    // 3. Checks ausführen
    CheckSuiteResult result = executor.execute(suite);

    // 4. Mit AssertJ Soft-Assertions auswerten
    SoftAssertions softly = new SoftAssertions();

    for (CheckResult checkResult : result.getResults()) {
        softly.assertThat(checkResult.isSuccess())
              .as("Check %s: %s", checkResult.getCheckId(), checkResult.getMessage())
              .isTrue();
    }

    // 5. Alle Fehler auf einmal anzeigen
    softly.assertAll();
}
```

### Beispiel: Konsolen-Ausgabe mit ✓ und ✗

```java
void printResults(CheckSuiteResult result) {
    System.out.println("=== " + result.getSuiteName() + " ===");
    System.out.println("Total: " + result.getTotalChecks());
    System.out.println("Success: " + result.getSuccessfulChecks());
    System.out.println("Failed: " + result.getFailedChecks());
    System.out.println();

    for (CheckResult check : result.getResults()) {
        String icon = check.isSuccess() ? "✓" : "✗";
        String severity = check.getSeverity().toString();

        System.out.printf("%s [%s] %s: %s%n",
            icon, severity, check.getCheckId(), check.getMessage());

        if (!check.isSuccess()) {
            System.out.printf("   Expected: %s%n", check.getExpectedValue());
            System.out.printf("   Actual:   %s%n", check.getActualValue());
        }
    }
}
```

**Ausgabe:**
```
=== FoodMart Validation ===
Total: 10
Success: 8
Failed: 2

✓ [ERROR] catalog-exists: Catalog 'FoodMart' exists
✓ [ERROR] sales-cube-exists: Cube 'Sales' exists
✗ [ERROR] hr-cube-count: Expected 7 cubes, found 6
   Expected: 7
   Actual:   6
✓ [WARN] unit-sales-format: Measure has format string
✗ [WARN] all-products-have-desc: 3 members missing descriptions
   Expected: All members have descriptions
   Actual:   Found 3 members without description
✓ [ERROR] member-1997-exists: Member exists
...
```

---

## Vollständiges Beispiel

```xml
<?xml version="1.0" encoding="UTF-8"?>
<olapchecks:CheckSuite xmi:version="2.0"
    xmlns:xmi="http://www.omg.org/XMI"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:olapchecks="http://www.eclipse.org/daanse/olap/checks/1.0"
    name="FoodMart Sales Cube Validation"
    catalogName="FoodMart"
    executionMode="OLAP_API">

  <!-- Existenz-Checks -->
  <checks xsi:type="olapchecks:CubeExistsCheck"
          id="sales-exists"
          cubeName="Sales"
          shouldExist="true"
          severity="ERROR"/>

  <checks xsi:type="olapchecks:DimensionExistsCheck"
          id="time-dim-exists"
          cubeName="Sales"
          dimensionName="Time"
          shouldExist="true"
          severity="ERROR"/>

  <!-- Kardinalitäts-Checks -->
  <checks xsi:type="olapchecks:DimensionCountCheck"
          id="sales-dim-count"
          cubeName="Sales"
          expectedCount="8"
          comparisonOperator="EQUALS"
          severity="ERROR"/>

  <checks xsi:type="olapchecks:MemberCountCheck"
          id="year-count"
          cubeName="Sales"
          dimensionName="Time"
          hierarchyName="Time"
          levelName="Year"
          expectedCount="1"
          comparisonOperator="EQUALS"
          severity="ERROR"/>

  <!-- Eigenschaften-Checks -->
  <checks xsi:type="olapchecks:FormatStringCheck"
          id="unit-sales-format"
          cubeName="Sales"
          measureName="Unit Sales"
          expectedFormatString="Standard"
          severity="WARN"/>

  <!-- MDX Query-Check -->
  <checks xsi:type="olapchecks:MdxQueryCheck"
          id="total-sales-query"
          cubeName="Sales"
          mdxQuery="SELECT {[Measures].[Unit Sales]} ON COLUMNS FROM Sales"
          expectNonEmptyResult="true"
          maxExecutionTimeMs="3000"
          severity="ERROR">
    <cellValueChecks rowIndex="0"
                     columnIndex="0"
                     expectedValue="266773"
                     valueComparisonMode="EXACT"/>
  </checks>

  <!-- Datenqualitäts-Checks -->
  <checks xsi:type="olapchecks:AllMeasuresHaveFormatStringCheck"
          id="all-measures-formatted"
          cubeName="Sales"
          severity="WARN"/>

</olapchecks:CheckSuite>
```

---

## Test-Pattern aus dem Repository

Basierend auf der Analyse wurden folgende häufige Test-Patterns identifiziert:

### Pattern 1: Role-based Visibility
**Aus:** `RolapCatalogReaderTest.testGetCubesForCaliforniaManager()`

```xml
<CheckSuite roleName="California manager">
  <checks xsi:type="CubeCountCheck" expectedCount="1"/>
  <checks xsi:type="CubeExistsCheck" cubeName="Sales" shouldExist="true"/>
  <checks xsi:type="CubeAccessCheck" cubeName="Sales" shouldBeAccessible="true"/>
  <checks xsi:type="CubeAccessCheck" cubeName="HR" shouldBeAccessible="false"/>
</CheckSuite>
```

### Pattern 2: Schema Structure Validation
**Aus:** `RoleTest.testDatabaseSchemaWithRole()`

```xml
<CheckSuite roleName="Test">
  <checks xsi:type="DatabaseSchemaCheck" expectedTableCount="3"/>
  <checks xsi:type="DatabaseSchemaCheck"
          tableName="sales_fact_1997"
          expectedColumnCount="8"/>
  <checks xsi:type="DatabaseSchemaCheck"
          tableName="product"
          expectedColumnCount="7"/>
</CheckSuite>
```

### Pattern 3: Calculated Members Visibility
**Aus:** `RolapCubeTest.testGetCalculatedMembersForCaliforniaManager()`

```xml
<CheckSuite cubeName="Sales" roleName="California manager">
  <checks xsi:type="CalculatedMemberExistsCheck"
          calculatedMemberUniqueName="[Measures].[Profit]"
          shouldExist="true"/>
  <checks xsi:type="CalculatedMemberExistsCheck"
          calculatedMemberUniqueName="[Measures].[Profit last Period]"
          shouldExist="true"/>
  <checks xsi:type="CalculatedMemberExistsCheck"
          calculatedMemberUniqueName="[Measures].[Profit Growth]"
          shouldExist="true"/>
</CheckSuite>
```

### Pattern 4: Hierarchy Access Control
**Aus:** `RolapCatalogReaderTest.testGetCubeDimensions()`

```xml
<CheckSuite cubeName="Sales" roleName="REG1">
  <checks xsi:type="DimensionAccessCheck"
          dimensionName="Store"
          shouldBeAccessible="false"/>
  <checks xsi:type="HierarchyAccessCheck"
          dimensionName="Time"
          hierarchyName="Time"
          shouldBeAccessible="false"/>
  <checks xsi:type="HierarchyAccessCheck"
          dimensionName="Time"
          hierarchyName="Weekly"
          shouldBeAccessible="true"/>
</CheckSuite>
```

---

## Workflow: Von Tests zu kleinen ROLAP-Mappings

### Zielworkflow

1. **ROLAP Mapping erstellen** (z.B. `my-cube-mapping.xml`)
   - Kleines, fokussiertes Mapping
   - Mit Testdaten

2. **Check Suite definieren** (`my-cube-checks.xml`)
   - Deklarative Checks für das Mapping
   - Basierend auf diesem Ecore-Modell

3. **Test ausführen**
   ```java
   @Test
   void testMyCube() {
       CheckSuite suite = loadCheckSuite("my-cube-checks.xml");
       CheckSuiteResult result = executor.execute(suite);
       assertAllChecksPass(result);
   }
   ```

4. **Bei Fehlern:**
   - Klare Fehlerausgabe mit ✓ und ✗
   - Vergleich Expected vs. Actual
   - Severity-basierte Fehlerbehandlung

---

## Vorteile dieses Ansatzes

### ✓ Wiederverwendbarkeit
- Check Suites können zwischen Projekten geteilt werden
- Patterns einmal definieren, überall verwenden

### ✓ Wartbarkeit
- Änderungen an Checks ohne Code-Änderungen
- Versionierung der Check-Definitionen

### ✓ Lesbarkeit
- Nicht-Entwickler können Checks verstehen und schreiben
- Dokumentation durch Struktur

### ✓ Testabdeckung
- Systematische Abdeckung aller OLAP-Aspekte
- Keine vergessenen Validierungen

### ✓ CI/CD Integration
- Automatische Validierung nach Build
- Fail-fast bei Schema-Problemen

### ✓ Datenqualität
- Kontinuierliche Qualitäts-Checks
- Frühzeitige Erkennung von Problemen

---

## Nächste Schritte

### 1. Implementierung
- [ ] Java-Klassen aus Ecore generieren
- [ ] `CheckExecutor` Interfaces definieren
- [ ] `OlapApiCheckExecutor` implementieren
- [ ] `XmlaCheckExecutor` implementieren

### 2. Beispiele
- [ ] Check Suites für FoodMart erstellen
- [ ] Integrationstests implementieren
- [ ] AssertJ Conditions entwickeln

### 3. Erweiterungen
- [ ] Custom Check-Typen
- [ ] Check Gruppen/Suites
- [ ] Parallele Ausführung
- [ ] Result-Export (JUnit XML, HTML)

---

## Kontakt

Bei Fragen oder Erweiterungswünschen: Eclipse Daanse Community

**Lizenz:** EPL-2.0
