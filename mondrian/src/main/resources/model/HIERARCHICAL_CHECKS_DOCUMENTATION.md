# OLAP Checks - Hierarchisches, Verkettetes Check-System

## Übersicht

Das **hierarchische OLAP Checks** Ecore-Modell ermöglicht **verkettete, aufeinander aufbauende** Tests durch **Selektoren und Filter**. Checks können auf Ergebnissen vorheriger Selektionen arbeiten und beliebig verschachtelt werden.

---

## Kernkonzept: Selector-Chain-Pattern

### Prinzip

```
Catalog selektieren
  └─> Cubes filtern (nach Name/Pattern/Typ)
       └─> Checks auf gefilterte Cubes
       └─> Dimensions filtern (nach Name/Pattern/Typ)
            └─> Checks auf gefilterte Dimensions
            └─> Hierarchies filtern
                 └─> Checks auf Hierarchies
                 └─> Levels filtern
                      └─> Checks auf Levels
                      └─> Members filtern
                           └─> Checks auf Members
                           └─> Properties filtern
                                └─> Checks auf Properties
```

### Hierarchie-Ebenen

| Ebene | Selector | Was wird selektiert | Checks auf |
|-------|----------|---------------------|------------|
| 1 | `CatalogSelector` | Catalog(e) | Catalog-Eigenschaften |
| 2 | `CubeSelector` | Cube(s) aus Catalog | Cube-Anzahl, -Eigenschaften |
| 3 | `DimensionSelector` | Dimension(en) aus Cube(s) | Dimension-Anzahl, -Typ |
| 4 | `HierarchySelector` | Hierarchy(ies) aus Dimension(en) | Hierarchy-Eigenschaften |
| 5 | `LevelSelector` | Level(s) aus Hierarchy(ies) | Level-Anzahl, -Tiefe |
| 6 | `MemberSelector` | Member(s) aus Level(s) | Member-Anzahl, -Werte |
| 7 | `PropertySelector` | Property(ies) aus Member(s) | Property-Werte |
| - | `MeasureSelector` | Measure(s) aus Cube(s) | Measure-Format, Aggregator |

---

## Selector-Typen

### CatalogSelector (Root)

Startet die Selector-Chain. Wählt einen oder mehrere Catalogs aus.

```xml
<catalogSelector id="foodmart-catalog"
                 catalogName="FoodMart"
                 roleName="California manager">
  <!-- Checks auf Catalog-Ebene -->
  <!-- Weitere Selektoren: cubeSelectors -->
</catalogSelector>
```

**Attribute:**
- `catalogName`: Exakter Catalog-Name
- `namePattern`: Regex-Pattern für Catalog-Namen
- `roleName`: (Optional) Rolle für rollenbasierten Zugriff

**Unterselektoren:**
- `cubeSelectors`: Wählt Cubes aus dem Catalog

**Checks:**
- `CountCheck`: Anzahl Catalogs
- `ExistsCheck`: Mindestens ein Catalog existiert
- `AttributeCheck`: Catalog-Eigenschaften (Name, Description, etc.)

---

### CubeSelector

Selektiert Cubes aus dem aktuellen Catalog-Kontext.

```xml
<cubeSelector id="sales-cubes"
              namePattern="Sales.*"
              cubeType="PHYSICAL"
              includeVirtualCubes="false">

  <!-- Checks auf selektierte Cubes -->
  <checks xsi:type="CountCheck" expectedCount="2" operator="GREATER_OR_EQUAL"/>
  <checks xsi:type="ExistsCheck" shouldExist="true"/>
  <checks xsi:type="AttributeCheck" attributeName="caption" expectedValue="Sales"/>

  <!-- Weitere Selektoren -->
  <dimensionSelectors>...</dimensionSelectors>
  <measureSelectors>...</measureSelectors>
</cubeSelector>
```

**Filter-Attribute:**
- `cubeName`: Exakter Cube-Name
- `namePattern`: Regex-Pattern (z.B. `"Sales.*"`, `".*Cube"`)
- `cubeType`: `ALL`, `PHYSICAL`, `VIRTUAL`
- `includeVirtualCubes`: `true`/`false`

**Unter-Selektoren:**
- `dimensionSelectors`: Dimensionen in den selektierten Cubes
- `measureSelectors`: Measures in den selektierten Cubes

**Checks:**
- `CountCheck`: Anzahl selektierter Cubes
- `ExistsCheck`: Mindestens ein Cube existiert
- `AttributeCheck`: Caption, Description, DefaultMeasure, etc.
- `AccessCheck`: Rollenbasierte Sichtbarkeit
- `NotNullCheck`: Attribute nicht null

---

### DimensionSelector

Selektiert Dimensions aus dem aktuellen Cube-Kontext.

```xml
<dimensionSelector id="time-dimensions"
                   dimensionType="TIME"
                   includeTimeDimensions="true"
                   includeMeasureDimension="false">

  <!-- Checks auf selektierte Dimensions -->
  <checks xsi:type="CountCheck" expectedCount="1" operator="EQUALS"/>
  <checks xsi:type="AttributeCheck"
          attributeName="type"
          expectedValue="TIME"/>

  <!-- Weitere Selektoren -->
  <hierarchySelectors>...</hierarchySelectors>
</dimensionSelector>
```

**Filter-Attribute:**
- `dimensionName`: Exakter Name
- `namePattern`: Regex-Pattern
- `dimensionType`: `ALL`, `STANDARD`, `TIME`, `MEASURE`
- `includeTimeDimensions`: `true`/`false`
- `includeMeasureDimension`: `true`/`false`

**Unter-Selektoren:**
- `hierarchySelectors`: Hierarchien in den selektierten Dimensionen

**Checks:**
- `CountCheck`: Anzahl Dimensionen
- `ExistsCheck`: Dimension existiert
- `AttributeCheck`: Name, Caption, Type, Description
- `AccessCheck`: Rollenbasierte Sichtbarkeit
- `NotNullCheck`: Beschreibung vorhanden

---

### HierarchySelector

Selektiert Hierarchies aus dem aktuellen Dimension-Kontext.

```xml
<hierarchySelector id="all-hierarchies"
                   namePattern=".*"
                   hasAllMember="true">

  <checks xsi:type="CountCheck" expectedCount="1" operator="GREATER_OR_EQUAL"/>
  <checks xsi:type="AttributeCheck"
          attributeName="hasAll"
          expectedValue="true"/>

  <levelSelectors>...</levelSelectors>
</hierarchySelector>
```

**Filter-Attribute:**
- `hierarchyName`: Exakter Name
- `namePattern`: Regex-Pattern
- `hasAllMember`: Filter nach hasAll (true/false/null = alle)

**Unter-Selektoren:**
- `levelSelectors`: Levels in den selektierten Hierarchien

**Checks:**
- `CountCheck`: Anzahl Hierarchien
- `ExistsCheck`: Hierarchy existiert
- `AttributeCheck`: HasAll, AllMemberName, DefaultMember, etc.
- `AccessCheck`: Rollenbasierte Sichtbarkeit

---

### LevelSelector

Selektiert Levels aus dem aktuellen Hierarchy-Kontext.

```xml
<levelSelector id="year-level"
               levelName="Year"
               depth="1"
               levelType="TimeYears">

  <checks xsi:type="CountCheck" expectedCount="1"/>
  <checks xsi:type="AttributeCheck"
          attributeName="levelType"
          expectedValue="TimeYears"/>
  <checks xsi:type="NotNullCheck" attributeName="keyColumn"/>

  <memberSelectors>...</memberSelectors>
</levelSelector>
```

**Filter-Attribute:**
- `levelName`: Exakter Name
- `namePattern`: Regex-Pattern
- `depth`: Tiefe im Hierarchy (0 = All-Level)
- `levelType`: TimeYears, TimeQuarters, etc.

**Unter-Selektoren:**
- `memberSelectors`: Members auf den selektierten Levels

**Checks:**
- `CountCheck`: Anzahl Levels
- `ExistsCheck`: Level existiert
- `AttributeCheck`: Depth, UniqueMembers, LevelType, etc.
- `NotNullCheck`: KeyColumn, NameColumn definiert

---

### MemberSelector

Selektiert Members aus dem aktuellen Level-Kontext oder by UniqueName.

```xml
<memberSelector id="1997-members"
                memberName="1997"
                parentUniqueName="[Time].[Time].[All]"
                maxMembers="100"
                includeCalculatedMembers="false">

  <checks xsi:type="CountCheck" expectedCount="1"/>
  <checks xsi:type="ExistsCheck" shouldExist="true"/>
  <checks xsi:type="AttributeCheck"
          attributeName="caption"
          expectedValue="1997"
          checkAllElements="true"/>
  <checks xsi:type="NotNullCheck" attributeName="description"/>

  <propertySelectors>...</propertySelectors>
</memberSelector>
```

**Filter-Attribute:**
- `memberName`: Exakter Member-Name
- `namePattern`: Regex-Pattern für Member-Namen
- `uniqueName`: Exakter UniqueName (z.B. `[Time].[Time].[1997]`)
- `parentUniqueName`: Parent-Member (filtert Children)
- `maxMembers`: Max. Anzahl Members (Performance)
- `includeCalculatedMembers`: Calculated Members einschließen

**Unter-Selektoren:**
- `propertySelectors`: Properties der selektierten Members

**Checks:**
- `CountCheck`: Anzahl Members
- `ExistsCheck`: Member existiert
- `AttributeCheck`: Caption, UniqueName, Ordinal, etc.
- `AccessCheck`: Rollenbasierte Sichtbarkeit
- `NotNullCheck`: Description, Caption vorhanden

---

### PropertySelector

Selektiert Properties aus dem aktuellen Member-Kontext.

```xml
<propertySelector id="member-properties"
                  namePattern=".*">

  <checks xsi:type="CountCheck" expectedCount="3" operator="GREATER_OR_EQUAL"/>
  <checks xsi:type="AttributeCheck"
          attributeName="value"
          expectedValue=".*"
          comparisonMode="REGEX"/>
  <checks xsi:type="NotNullCheck" attributeName="value"/>
</propertySelector>
```

**Filter-Attribute:**
- `propertyName`: Exakter Property-Name
- `namePattern`: Regex-Pattern

**Checks:**
- `CountCheck`: Anzahl Properties
- `ExistsCheck`: Property existiert
- `AttributeCheck`: Value, Type, etc.
- `NotNullCheck`: Wert vorhanden

---

### MeasureSelector

Selektiert Measures aus dem aktuellen Cube-Kontext.

```xml
<measureSelector id="sum-measures"
                 namePattern=".*Sales"
                 aggregator="sum"
                 includeCalculatedMeasures="false">

  <checks xsi:type="CountCheck" expectedCount="2" operator="GREATER_OR_EQUAL"/>
  <checks xsi:type="AttributeCheck"
          attributeName="aggregator"
          expectedValue="sum"/>
  <checks xsi:type="NotNullCheck" attributeName="formatString"/>
</measureSelector>
```

**Filter-Attribute:**
- `measureName`: Exakter Name
- `namePattern`: Regex-Pattern
- `aggregator`: sum, avg, count, min, max, etc.
- `includeCalculatedMeasures`: Calculated Measures einschließen

**Checks:**
- `CountCheck`: Anzahl Measures
- `ExistsCheck`: Measure existiert
- `AttributeCheck`: Aggregator, FormatString, etc.
- `NotNullCheck`: FormatString vorhanden

---

## Check-Typen

### CountCheck
Prüft die Anzahl der selektierten Elemente.

```xml
<checks xsi:type="CountCheck"
        id="cube-count"
        expectedCount="7"
        operator="EQUALS"
        severity="ERROR"/>
```

**Operator:**
- `EQUALS`: Genau N
- `GREATER_THAN`: > N
- `LESS_THAN`: < N
- `GREATER_OR_EQUAL`: >= N
- `LESS_OR_EQUAL`: <= N
- `NOT_EQUALS`: != N

---

### ExistsCheck
Prüft, ob mindestens ein Element selektiert wurde.

```xml
<checks xsi:type="ExistsCheck"
        id="sales-exists"
        shouldExist="true"
        severity="ERROR"/>
```

---

### AttributeCheck
Prüft Attribute/Eigenschaften der selektierten Elemente.

```xml
<checks xsi:type="AttributeCheck"
        id="caption-check"
        attributeName="caption"
        expectedValue="Sales Cube"
        comparisonMode="EXACT"
        checkAllElements="true"
        severity="WARN"/>
```

**ComparisonMode:**
- `EXACT`: Exakter String-Vergleich
- `CONTAINS`: String enthält Wert
- `REGEX`: Regex-Match
- `NUMERIC_EQUALS`: Numerischer Vergleich
- `NUMERIC_TOLERANCE`: Numerisch mit Toleranz

**checkAllElements:**
- `true`: Alle selektierten Elemente müssen passen
- `false`: Mindestens ein Element muss passen

**Häufige Attribute:**

| Ebene | Attribute |
|-------|-----------|
| Catalog | name, description |
| Cube | name, caption, description, defaultMeasure, visible |
| Dimension | name, caption, type (STANDARD/TIME), description |
| Hierarchy | name, caption, hasAll, allMemberName, defaultMember |
| Level | name, caption, depth, uniqueMembers, levelType, keyColumn, nameColumn |
| Member | name, caption, uniqueName, ordinal, type, description |
| Measure | name, caption, aggregator, formatString, datatype |
| Property | name, value, type |

---

### NotNullCheck
Prüft, dass ein Attribut nicht null/leer ist.

```xml
<checks xsi:type="NotNullCheck"
        id="description-not-null"
        attributeName="description"
        severity="WARN"/>
```

---

### AccessCheck
Prüft rollenbasierte Sichtbarkeit.

```xml
<checks xsi:type="AccessCheck"
        id="sales-accessible"
        roleName="California manager"
        shouldBeAccessible="true"
        severity="ERROR"/>
```

---

## Rollenbasierter Zugriff

### Zwei Modi für Rollentests

#### 1. Connection-Level (global)
Rolle wird auf CheckSuite- oder ConnectionConfig-Ebene gesetzt.

```xml
<CheckSuite>
  <connectionConfig catalogName="FoodMart" roleName="California manager"/>

  <catalogSelector>
    <cubeSelector>
      <!-- Nur Cubes sichtbar für "California manager" -->
      <checks xsi:type="CountCheck" expectedCount="1"/>
    </cubeSelector>
  </catalogSelector>
</CheckSuite>
```

#### 2. Reader-Level (per Selector)
Rolle kann pro Selector überschrieben werden.

```xml
<catalogSelector roleName="California manager">
  <cubeSelector>
    <!-- Cubes für California manager -->
    <checks xsi:type="CountCheck" expectedCount="1"/>
  </cubeSelector>
</catalogSelector>

<catalogSelector roleName="Admin">
  <cubeSelector>
    <!-- Cubes für Admin -->
    <checks xsi:type="CountCheck" expectedCount="7"/>
  </cubeSelector>
</catalogSelector>
```

#### 3. AccessCheck (pro Element)
Prüft Sichtbarkeit für spezifische Rolle.

```xml
<cubeSelector cubeName="HR">
  <checks xsi:type="AccessCheck"
          roleName="California manager"
          shouldBeAccessible="false"/>
</cubeSelector>
```

---

## Vollständige Beispiele

### Beispiel 1: Hierarchische Validierung mit Pattern

```xml
<?xml version="1.0" encoding="UTF-8"?>
<olapchecks:CheckSuite xmi:version="2.0"
    xmlns:xmi="http://www.omg.org/XMI"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:olapchecks="http://www.eclipse.org/daanse/olap/checks/2.0"
    name="FoodMart Hierarchical Validation"
    executionMode="OLAP_API">

  <connectionConfig catalogName="FoodMart"/>

  <catalogSelectors id="foodmart">
    <!-- Selektiere alle Cubes mit "Sales" im Namen -->
    <cubeSelectors id="sales-cubes" namePattern="Sales.*">

      <!-- Check: Mindestens 2 Sales-Cubes -->
      <checks xsi:type="olapchecks:CountCheck"
              expectedCount="2"
              operator="GREATER_OR_EQUAL"
              severity="ERROR"/>

      <!-- Selektiere TIME Dimensionen -->
      <dimensionSelectors id="time-dims" dimensionType="TIME">

        <!-- Check: Genau 1 TIME Dimension pro Cube -->
        <checks xsi:type="olapchecks:CountCheck"
                expectedCount="1"
                operator="EQUALS"
                severity="ERROR"/>

        <!-- Check: Type Attribut = TIME -->
        <checks xsi:type="olapchecks:AttributeCheck"
                attributeName="type"
                expectedValue="TIME"
                checkAllElements="true"
                severity="ERROR"/>

        <!-- Selektiere alle Hierarchien -->
        <hierarchySelectors id="all-hierarchies" namePattern=".*">

          <!-- Check: Mindestens 1 Hierarchy -->
          <checks xsi:type="olapchecks:CountCheck"
                  expectedCount="1"
                  operator="GREATER_OR_EQUAL"
                  severity="ERROR"/>

          <!-- Selektiere alle Levels -->
          <levelSelectors id="all-levels">

            <!-- Check: Mindestens 3 Levels (ohne All) -->
            <checks xsi:type="olapchecks:CountCheck"
                    expectedCount="3"
                    operator="GREATER_OR_EQUAL"
                    severity="ERROR"/>

            <!-- Check: Alle Levels haben keyColumn -->
            <checks xsi:type="olapchecks:NotNullCheck"
                    attributeName="keyColumn"
                    severity="ERROR"/>

            <!-- Selektiere Year-Level -->
            <memberSelectors id="year-members"
                            parentUniqueName="[Time].[Time].[All]"
                            maxMembers="10">

              <!-- Check: Genau 1 Year-Member (1997) -->
              <checks xsi:type="olapchecks:CountCheck"
                      expectedCount="1"
                      operator="EQUALS"
                      severity="ERROR"/>

              <!-- Check: Member hat Description -->
              <checks xsi:type="olapchecks:NotNullCheck"
                      attributeName="description"
                      severity="WARN"/>

            </memberSelectors>
          </levelSelectors>
        </hierarchySelectors>
      </dimensionSelectors>

      <!-- Selektiere alle Measures -->
      <measureSelectors id="all-measures">

        <!-- Check: Alle Measures haben FormatString -->
        <checks xsi:type="olapchecks:NotNullCheck"
                attributeName="formatString"
                severity="WARN"/>

      </measureSelectors>

    </cubeSelectors>
  </catalogSelectors>

</olapchecks:CheckSuite>
```

---

### Beispiel 2: Rollenbasierte Zugriffstests

```xml
<CheckSuite name="Role-Based Access Tests" executionMode="OLAP_API">

  <connectionConfig catalogName="FoodMart"/>

  <!-- Test 1: California manager sieht nur Sales Cube -->
  <catalogSelectors id="catalog-manager" roleName="California manager">

    <cubeSelectors id="all-cubes">
      <!-- Sollte nur 1 Cube sehen -->
      <checks xsi:type="CountCheck" expectedCount="1" severity="ERROR"/>
    </cubeSelectors>

    <cubeSelectors id="sales-cube" cubeName="Sales">
      <!-- Sales sollte sichtbar sein -->
      <checks xsi:type="ExistsCheck" shouldExist="true" severity="ERROR"/>
      <checks xsi:type="AccessCheck"
              roleName="California manager"
              shouldBeAccessible="true"
              severity="ERROR"/>
    </cubeSelectors>

    <cubeSelectors id="hr-cube" cubeName="HR">
      <!-- HR sollte NICHT sichtbar sein -->
      <checks xsi:type="ExistsCheck" shouldExist="false" severity="ERROR"/>
      <checks xsi:type="AccessCheck"
              roleName="California manager"
              shouldBeAccessible="false"
              severity="ERROR"/>
    </cubeSelectors>

  </catalogSelectors>

  <!-- Test 2: Admin sieht alle Cubes -->
  <catalogSelectors id="catalog-admin" roleName="Admin">

    <cubeSelectors id="all-cubes-admin">
      <!-- Sollte 7 Cubes sehen -->
      <checks xsi:type="CountCheck" expectedCount="7" severity="ERROR"/>
    </cubeSelectors>

  </catalogSelectors>

  <!-- Test 3: REG1 Rolle - Dimension/Hierarchy Grants -->
  <catalogSelectors id="catalog-reg1" roleName="REG1">

    <cubeSelectors cubeName="Sales">

      <dimensionSelectors dimensionName="Store">
        <!-- Store Dimension sollte NICHT sichtbar sein -->
        <checks xsi:type="ExistsCheck" shouldExist="false" severity="ERROR"/>
        <checks xsi:type="AccessCheck"
                roleName="REG1"
                shouldBeAccessible="false"
                severity="ERROR"/>
      </dimensionSelectors>

      <dimensionSelectors dimensionName="Time">

        <hierarchySelectors hierarchyName="Time">
          <!-- Time.Time Hierarchy sollte NICHT sichtbar sein -->
          <checks xsi:type="AccessCheck"
                  roleName="REG1"
                  shouldBeAccessible="false"
                  severity="ERROR"/>
        </hierarchySelectors>

        <hierarchySelectors hierarchyName="Weekly">
          <!-- Time.Weekly Hierarchy sollte sichtbar sein -->
          <checks xsi:type="AccessCheck"
                  roleName="REG1"
                  shouldBeAccessible="true"
                  severity="ERROR"/>
        </hierarchySelectors>

      </dimensionSelectors>

    </cubeSelectors>

  </catalogSelectors>

</CheckSuite>
```

---

### Beispiel 3: Datenqualitäts-Checks

```xml
<CheckSuite name="Data Quality Checks" executionMode="OLAP_API">

  <connectionConfig catalogName="FoodMart"/>

  <catalogSelectors>

    <cubeSelectors cubeName="Sales">

      <!-- Alle Measures müssen FormatString haben -->
      <measureSelectors namePattern=".*">
        <checks xsi:type="NotNullCheck"
                attributeName="formatString"
                severity="WARN"
                description="All measures should have format strings"/>
      </measureSelectors>

      <dimensionSelectors namePattern=".*">

        <!-- Alle Dimensionen müssen Description haben -->
        <checks xsi:type="NotNullCheck"
                attributeName="description"
                severity="WARN"/>

        <hierarchySelectors namePattern=".*">

          <levelSelectors namePattern=".*">

            <!-- Alle Levels müssen keyColumn haben -->
            <checks xsi:type="NotNullCheck"
                    attributeName="keyColumn"
                    severity="ERROR"/>

            <memberSelectors maxMembers="1000">

              <!-- Alle Members sollten Caption haben -->
              <checks xsi:type="NotNullCheck"
                      attributeName="caption"
                      severity="WARN"/>

              <!-- Alle Members sollten Description haben -->
              <checks xsi:type="NotNullCheck"
                      attributeName="description"
                      severity="INFO"
                      description="Members should have descriptions for better UX"/>

            </memberSelectors>

          </levelSelectors>

        </hierarchySelectors>

      </dimensionSelectors>

    </cubeSelectors>

  </catalogSelectors>

</CheckSuite>
```

---

### Beispiel 4: Pattern-basierte Suche

```xml
<CheckSuite name="Pattern-Based Validation">

  <connectionConfig catalogName="FoodMart"/>

  <catalogSelectors>

    <!-- Alle Cubes die mit "Sales" beginnen -->
    <cubeSelectors namePattern="^Sales.*">
      <checks xsi:type="CountCheck" expectedCount="2" operator="GREATER_OR_EQUAL"/>

      <!-- In jedem Sales-Cube: Dimensions die "Time" enthalten -->
      <dimensionSelectors namePattern=".*Time.*">
        <checks xsi:type="CountCheck" expectedCount="1" operator="GREATER_OR_EQUAL"/>

        <!-- Hierarchien mit "Weekly" oder "Time" im Namen -->
        <hierarchySelectors namePattern=".*(Weekly|Time).*">
          <checks xsi:type="ExistsCheck" shouldExist="true"/>

          <!-- Levels mit "Year" oder "Quarter" im Namen -->
          <levelSelectors namePattern=".*(Year|Quarter).*">
            <checks xsi:type="ExistsCheck" shouldExist="true"/>

            <!-- Members deren Name eine Zahl ist -->
            <memberSelectors namePattern="[0-9]+">
              <checks xsi:type="CountCheck"
                      expectedCount="1"
                      operator="GREATER_OR_EQUAL"/>
            </memberSelectors>

          </levelSelectors>

        </hierarchySelectors>

      </dimensionSelectors>

    </cubeSelectors>

    <!-- Alle Measures die auf "Sales" enden -->
    <cubeSelectors>
      <measureSelectors namePattern=".*Sales$">
        <checks xsi:type="CountCheck" expectedCount="2" operator="GREATER_OR_EQUAL"/>
        <checks xsi:type="AttributeCheck"
                attributeName="aggregator"
                expectedValue="sum"
                checkAllElements="false"/>
      </measureSelectors>
    </cubeSelectors>

  </catalogSelectors>

</CheckSuite>
```

---

## Execution & Results

### Ausführung

```java
// 1. Check Suite laden
CheckSuite suite = loadCheckSuite("my-hierarchical-checks.xml");

// 2. Executor erstellen
CheckExecutor executor = new OlapApiCheckExecutor(connection);
// oder: new XmlaCheckExecutor(xmlaClient);

// 3. Ausführen
CheckSuiteResult result = executor.execute(suite);

// 4. Ergebnisse verarbeiten
printResults(result);
```

### Ergebnis-Struktur

```java
public class CheckResult {
    String checkId;              // "cube-count"
    String selectorPath;         // "catalog/cubes[Sales.*]/dimensions[TIME]"
    boolean success;             // true/false
    Severity severity;           // INFO/WARN/ERROR
    String message;              // "Expected 1, found 2"
    String actualValue;          // "2"
    String expectedValue;        // "1"
    List<String> affectedElements; // ["Sales", "Sales 2"]
    long executionTimeMs;        // 123
    Date timestamp;
}
```

**selectorPath** zeigt den Pfad durch die Selector-Hierarchie:
```
catalog[FoodMart]/cubes[Sales.*]/dimensions[TIME]/hierarchies[Time]/levels[Year]/members[1997]
```

---

## AssertJ Integration

### Soft-Assertions mit Pfad

```java
@Test
void testHierarchicalChecks() {
    CheckSuiteResult result = executor.execute(suite);

    SoftAssertions softly = new SoftAssertions();

    for (CheckResult check : result.getResults()) {
        softly.assertThat(check.isSuccess())
              .as("Check '%s' at path '%s': %s",
                  check.getCheckId(),
                  check.getSelectorPath(),
                  check.getMessage())
              .isTrue();
    }

    softly.assertAll();
}
```

### Konsolen-Ausgabe mit Pfad

```
=== FoodMart Hierarchical Validation ===
Total: 15 | Success: 13 | Failed: 2

✓ [ERROR] catalog/cubes[Sales.*]: Count check passed (2 cubes)
✓ [ERROR] catalog/cubes[Sales.*]/dimensions[TIME]: Count check passed (1 dimension)
✗ [ERROR] catalog/cubes[Sales.*]/dimensions[TIME]/hierarchies[.*]/levels: Expected >= 3, found 2
   Path: catalog[FoodMart]/cubes[Sales]/dimensions[Time]/hierarchies[Time]/levels
   Expected: >= 3
   Actual: 2
   Affected: [Time].[Time]
✓ [WARN] catalog/cubes[Sales.*]/measures: FormatString not null (all passed)
✗ [INFO] catalog/cubes[Sales.*]/dimensions/hierarchies/levels/members: 3 members missing description
   Path: catalog[FoodMart]/cubes[Sales]/dimensions[Product]/hierarchies[Product]/levels[Product Name]/members
   Expected: All members have description
   Actual: 3 members without description
   Affected: [Product A], [Product B], [Product C]
```

---

## Vorteile des hierarchischen Ansatzes

### ✅ Verkettung & Wiederverwendung
```xml
<!-- Einmal filtern, mehrfach prüfen -->
<cubeSelectors namePattern="Sales.*">
  <checks xsi:type="CountCheck" .../>
  <checks xsi:type="AttributeCheck" .../>
  <dimensionSelectors ...>
    <checks .../>
  </dimensionSelectors>
</cubeSelectors>
```

### ✅ Pattern-basierte Massenprüfungen
```xml
<!-- Alle TIME Dimensionen in allen Sales Cubes -->
<cubeSelectors namePattern="Sales.*">
  <dimensionSelectors dimensionType="TIME">
    <checks xsi:type="CountCheck" expectedCount="1"/>
  </dimensionSelectors>
</cubeSelectors>
```

### ✅ Kontextuelle Checks
```xml
<!-- Check nur auf gefilterten Elementen -->
<dimensionSelectors dimensionType="TIME">
  <hierarchySelectors>
    <levelSelectors depth="1">
      <!-- Check nur auf Levels mit depth=1 in TIME Dimensions -->
      <checks xsi:type="AttributeCheck" .../>
    </levelSelectors>
  </hierarchySelectors>
</dimensionSelectors>
```

### ✅ Flexible Rollen-Tests
```xml
<!-- Verschiedene Rollen, verschiedene Erwartungen -->
<catalogSelectors roleName="Manager">
  <cubeSelectors>
    <checks xsi:type="CountCheck" expectedCount="1"/>
  </cubeSelectors>
</catalogSelectors>

<catalogSelectors roleName="Admin">
  <cubeSelectors>
    <checks xsi:type="CountCheck" expectedCount="7"/>
  </cubeSelectors>
</catalogSelectors>
```

### ✅ Granulare Fehlerberichte
Jeder Check kennt seinen Selector-Pfad:
```
catalog[FoodMart]/cubes[Sales]/dimensions[Time]/hierarchies[Time]/levels[Year]
```

---

## Best Practices

### 1. Pattern optimal nutzen
```xml
<!-- Gut: Breite Selektion, spezifische Checks -->
<cubeSelectors namePattern=".*">
  <dimensionSelectors dimensionType="TIME">
    <checks xsi:type="CountCheck" expectedCount="1"/>
  </dimensionSelectors>
</cubeSelectors>

<!-- Schlecht: Zu spezifisch -->
<cubeSelectors cubeName="Sales">
  <dimensionSelectors dimensionName="Time">
    ...
  </dimensionSelectors>
</cubeSelectors>
<cubeSelectors cubeName="Sales 2">
  <dimensionSelectors dimensionName="Time">
    ...
  </dimensionSelectors>
</cubeSelectors>
```

### 2. MaxMembers bei großen Levels
```xml
<memberSelectors maxMembers="1000">
  <!-- Verhindert Performance-Probleme -->
  <checks xsi:type="NotNullCheck" attributeName="caption"/>
</memberSelectors>
```

### 3. Severity sinnvoll setzen
- `ERROR`: Strukturelle Probleme (Existenz, Anzahl)
- `WARN`: Qualitätsprobleme (fehlende Descriptions)
- `INFO`: Hinweise (Best Practices)

### 4. ClearCacheBefore bei Bedarf
```xml
<catalogSelectors clearCacheBefore="true">
  <!-- Stellt sicher, dass Cache frisch ist -->
</catalogSelectors>
```

---

## Nächste Schritte

### Implementierung
1. Java-Klassen aus Ecore generieren
2. CheckExecutor implementieren
3. Selector-Engine implementieren (Filter/Pattern-Matching)
4. Check-Evaluatoren implementieren

### Testing
1. Unit-Tests für jeden Selector-Typ
2. Integration-Tests mit FoodMart
3. Performance-Tests mit großen Modellen

### Erweiterungen
1. Custom Selektoren (z.B. `CalculatedMemberSelector`)
2. Custom Checks (z.B. `SqlQueryCheck`)
3. Check-Templates
4. Suite-Import/Komposition

---

**Lizenz:** EPL-2.0
**Dokumentation Version:** 2.0
