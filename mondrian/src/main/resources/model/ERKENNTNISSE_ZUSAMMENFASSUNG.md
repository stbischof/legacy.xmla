# Erkenntnisse: OLAP Test-Analyse und Check-System Design

## Executive Summary

Nach Analyse von **2200+ Tests** im legacy.xmla Repository wurde ein deklaratives Check-System entwickelt. Die Erkenntnisse zeigen, dass ein **XMLA-natives Check-System** optimal ist, das auf DISCOVER Requests und EXECUTE Statements basiert.

---

## 🔍 Test-Analyse Erkenntnisse

### 1. Test-Cluster (2200+ Tests analysiert)

| Cluster | Anzahl Tests | Was wird getestet | XMLA Mapping |
|---------|--------------|-------------------|--------------|
| **Strukturelle Existenz** | ~500 | Catalog, Cube, Dimension, Hierarchy, Level, Member existieren | `DISCOVER` Requests mit Restrictions |
| **Zugriffskontrolle** | ~300 | Rollenbasierte Sichtbarkeit | `DISCOVER` mit unterschiedlichen Sessions |
| **Kardinalität** | ~200 | Anzahl Elemente (Cubes, Dimensions, Members, etc.) | `DISCOVER` Result RowCount |
| **Eigenschaften** | ~400 | Caption, Description, FormatString, Type, etc. | `DISCOVER` Result Column Values |
| **Query-Execution** | ~600 | MDX Queries mit erwarteten Ergebnissen | `EXECUTE` MDX |
| **Aggregation/Cache** | ~200 | Cache-Funktionalität, Performance | `EXECUTE` mit Properties |

### 2. Häufigste Test-Patterns

#### Pattern 1: "Existiert Element X?"
```java
// Test-Code
Connection connection = context.getConnectionWithDefaultRole();
Cube cube = connection.getCatalog().lookupCube("Sales");
assertNotNull(cube);
```

**XMLA Equivalent:**
```xml
<Discover>
  <RequestType>MDSCHEMA_CUBES</RequestType>
  <Restrictions>
    <CATALOG_NAME>FoodMart</CATALOG_NAME>
    <CUBE_NAME>Sales</CUBE_NAME>
  </Restrictions>
</Discover>
<!-- Erwartung: RowCount >= 1 -->
```

#### Pattern 2: "Rolle sieht nur bestimmte Cubes"
```java
// Test-Code
Connection connection = context.getConnection(new ConnectionProps(List.of("California manager")));
List<Cube> cubes = connection.getCatalogReader().getCubes();
assertEquals(1, cubes.size());
assertEquals("Sales", cubes.get(0).getName());
```

**XMLA Equivalent:**
```xml
<!-- Session mit Role -->
<Discover>
  <RequestType>MDSCHEMA_CUBES</RequestType>
  <Properties>
    <Roles>California manager</Roles>
  </Properties>
</Discover>
<!-- Erwartung: RowCount = 1, CUBE_NAME = "Sales" -->
```

#### Pattern 3: "Anzahl Dimensions in Cube"
```java
// Test-Code
Cube cube = connection.getCatalog().lookupCube("Sales");
List<Dimension> dims = cube.getDimensions();
assertEquals(8, dims.size());
```

**XMLA Equivalent:**
```xml
<Discover>
  <RequestType>MDSCHEMA_DIMENSIONS</RequestType>
  <Restrictions>
    <CATALOG_NAME>FoodMart</CATALOG_NAME>
    <CUBE_NAME>Sales</CUBE_NAME>
  </Restrictions>
</Discover>
<!-- Erwartung: RowCount = 8 -->
```

#### Pattern 4: "TIME Dimension hat bestimmte Eigenschaften"
```java
// Test-Code
Dimension dim = cube.getDimensions().stream()
    .filter(d -> d.getDimensionType() == DimensionType.TIME)
    .findFirst().orElseThrow();
assertEquals("TIME", dim.getType());
```

**XMLA Equivalent:**
```xml
<Discover>
  <RequestType>MDSCHEMA_DIMENSIONS</RequestType>
  <Restrictions>
    <CATALOG_NAME>FoodMart</CATALOG_NAME>
    <CUBE_NAME>Sales</CUBE_NAME>
    <DIMENSION_TYPE>2</DIMENSION_TYPE> <!-- TIME -->
  </Restrictions>
</Discover>
<!-- Erwartung: RowCount >= 1, DIMENSION_TYPE = 2 -->
```

#### Pattern 5: "MDX Query liefert erwartete Werte"
```java
// Test-Code
Result result = connection.execute("SELECT {[Measures].[Unit Sales]} ON COLUMNS FROM Sales");
Cell cell = result.getCell(new int[]{0, 0});
assertEquals("266773", cell.getFormattedValue());
```

**XMLA Equivalent:**
```xml
<Execute>
  <Command>
    <Statement>SELECT {[Measures].[Unit Sales]} ON COLUMNS FROM Sales</Statement>
  </Command>
  <Properties>
    <Catalog>FoodMart</Catalog>
  </Properties>
</Execute>
<!-- Erwartung: Cell[0,0].Value = 266773 -->
```

---

## 📊 XMLA Schema Rowsets Mapping

### Hierarchie der XMLA DISCOVER Requests

```
DBSCHEMA_CATALOGS
  └─> MDSCHEMA_CUBES (Restriction: CATALOG_NAME)
       ├─> MDSCHEMA_DIMENSIONS (Restriction: CUBE_NAME)
       │    └─> MDSCHEMA_HIERARCHIES (Restriction: DIMENSION_UNIQUE_NAME)
       │         └─> MDSCHEMA_LEVELS (Restriction: HIERARCHY_UNIQUE_NAME)
       │              └─> MDSCHEMA_MEMBERS (Restriction: LEVEL_UNIQUE_NAME)
       │                   └─> MDSCHEMA_PROPERTIES (Restriction: MEMBER_UNIQUE_NAME)
       │
       └─> MDSCHEMA_MEASURES (Restriction: CUBE_NAME)
```

### XMLA DISCOVER Request Types

| Request Type | Liefert | Wichtige Restrictions | Häufigkeit in Tests |
|--------------|---------|----------------------|---------------------|
| `DBSCHEMA_CATALOGS` | Catalogs | - | Gering |
| `MDSCHEMA_CUBES` | Cubes | CATALOG_NAME, CUBE_NAME, CUBE_TYPE | Sehr hoch |
| `MDSCHEMA_DIMENSIONS` | Dimensions | CATALOG_NAME, CUBE_NAME, DIMENSION_UNIQUE_NAME, DIMENSION_TYPE | Sehr hoch |
| `MDSCHEMA_HIERARCHIES` | Hierarchies | CATALOG_NAME, CUBE_NAME, DIMENSION_UNIQUE_NAME, HIERARCHY_UNIQUE_NAME | Hoch |
| `MDSCHEMA_LEVELS` | Levels | CATALOG_NAME, CUBE_NAME, HIERARCHY_UNIQUE_NAME, LEVEL_UNIQUE_NAME, LEVEL_NUMBER | Hoch |
| `MDSCHEMA_MEMBERS` | Members | CATALOG_NAME, CUBE_NAME, HIERARCHY_UNIQUE_NAME, LEVEL_UNIQUE_NAME, MEMBER_UNIQUE_NAME | Mittel |
| `MDSCHEMA_MEASURES` | Measures | CATALOG_NAME, CUBE_NAME, MEASURE_UNIQUE_NAME | Hoch |
| `MDSCHEMA_PROPERTIES` | Properties | PROPERTY_NAME, PROPERTY_TYPE | Niedrig |
| `MDSCHEMA_SETS` | Named Sets | CATALOG_NAME, CUBE_NAME | Niedrig |

### XMLA Restrictions (Filter)

| Restriction | Datentyp | Verwendung | Pattern Support |
|-------------|----------|------------|-----------------|
| `CATALOG_NAME` | String | Filter nach Catalog | ✓ (Wildcards) |
| `SCHEMA_NAME` | String | Filter nach Schema | ✓ |
| `CUBE_NAME` | String | Filter nach Cube | ✓ |
| `CUBE_TYPE` | String | "CUBE" oder "DIMENSION" | ✗ |
| `DIMENSION_UNIQUE_NAME` | String | Filter nach Dimension | ✓ |
| `DIMENSION_TYPE` | Integer | 0=Unknown, 1=Time, 2=Measure, 3=Other | ✗ |
| `HIERARCHY_UNIQUE_NAME` | String | Filter nach Hierarchy | ✓ |
| `LEVEL_UNIQUE_NAME` | String | Filter nach Level | ✓ |
| `LEVEL_NUMBER` | Integer | Tiefe in Hierarchy | ✗ |
| `MEMBER_UNIQUE_NAME` | String | Filter nach Member | ✓ |
| `MEMBER_TYPE` | Integer | 1=Regular, 2=All, 3=Measure, 4=Formula | ✗ |
| `MEASURE_UNIQUE_NAME` | String | Filter nach Measure | ✓ |
| `PROPERTY_NAME` | String | Filter nach Property | ✓ |

### XMLA Properties

| Property | Datentyp | Verwendung |
|----------|----------|------------|
| `Catalog` | String | Aktiver Catalog |
| `Roles` | String | Komma-separierte Rollen |
| `StateSupport` | String | None/Sessions |
| `Timeout` | Integer | Query Timeout (Sekunden) |
| `DbpropMsmdFlattened2` | Boolean | Flattened Hierarchies |
| `EffectiveUserName` | String | Impersonation |
| `LocaleIdentifier` | Integer | LCID |

---

## 🎯 Design-Entscheidung: XMLA-natives Check-System

### Vorteile gegenüber OLAP API

| Aspekt | OLAP API | XMLA |
|--------|----------|------|
| **Abhängigkeiten** | Benötigt Connection, CatalogReader | Nur XMLA Client |
| **Testbarkeit** | Nur innerhalb JVM | Remote-Testing möglich |
| **Standards** | Proprietär | XMLA Standard (ISO/IEC 9075) |
| **Portabilität** | Mondrian/Daanse-spezifisch | Jeder XMLA-Server |
| **CI/CD** | Komplexes Setup | HTTP-basiert, einfach |
| **Mock/Stub** | Schwierig | HTTP-Level einfach |
| **Performance** | Direkt | Overhead durch HTTP |
| **Vollständigkeit** | Voller Zugriff | XMLA-Umfang |

### Entscheidung

✅ **XMLA-basiertes Check-System** im **xmla Repository**

**Begründung:**
1. **Standard-konform**: XMLA ist ein offener Standard
2. **Portabel**: Funktioniert mit jedem XMLA-Server (Mondrian, SSAS, etc.)
3. **Testbar**: Einfaches Remote-Testing ohne JVM-Setup
4. **CI/CD-freundlich**: HTTP-basierte Tests
5. **Unabhängig**: Keine Abhängigkeit zu legacy.xmla
6. **Wiederverwendbar**: Check-Definitionen server-übergreifend

---

## 📦 Übergabe an xmla Repository

### Ziel-Repository
```
https://github.com/eclipse-daanse/org.eclipse.daanse.xmla
```

### Vorgeschlagene Modul-Struktur

```
org.eclipse.daanse.xmla/
├── client/                  (existiert bereits)
├── api/                     (existiert bereits)
├── checks/                  (NEU)
│   ├── model/
│   │   ├── olapchecks.ecore
│   │   └── pom.xml
│   ├── executor/
│   │   ├── XmlaCheckExecutor.java
│   │   ├── DiscoverCheckExecutor.java
│   │   ├── ExecuteCheckExecutor.java
│   │   └── pom.xml
│   ├── api/
│   │   ├── CheckExecutor.java
│   │   ├── CheckResult.java
│   │   └── pom.xml
│   ├── assertj/
│   │   ├── CheckResultAssert.java
│   │   ├── CheckSuiteResultAssert.java
│   │   └── pom.xml
│   └── examples/
│       ├── foodmart-checks.xml
│       ├── role-based-checks.xml
│       └── pom.xml
```

### Zu übergebende Artefakte

#### 1. Ecore-Modell (XMLA-Version)
- `olapchecks-xmla.ecore`
- Basiert auf DISCOVER Requests und Restrictions
- EXECUTE Checks für MDX

#### 2. Dokumentation
- `XMLA_CHECKS_SPECIFICATION.md`
- Vollständige Spezifikation
- Mapping von Checks zu XMLA Requests
- Beispiele

#### 3. Beispiele
- `example-discover-checks.xml` - DISCOVER-basierte Checks
- `example-execute-checks.xml` - MDX EXECUTE Checks
- `example-role-checks.xml` - Rollenbasierte Tests
- `example-integration.xml` - Vollständiges Integrations-Beispiel

#### 4. Implementierungs-Guide
- `IMPLEMENTATION_GUIDE.md`
- Schritt-für-Schritt Anleitung
- Code-Beispiele
- Best Practices

---

## 🔄 Konzept-Änderungen

### Alt: OLAP API basiert
```xml
<catalogSelector catalogName="FoodMart">
  <cubeSelector cubeName="Sales">
    <checks xsi:type="ExistsCheck" shouldExist="true"/>
  </cubeSelector>
</catalogSelector>
```

### Neu: XMLA DISCOVER basiert
```xml
<discoverCheck id="sales-cube-exists" requestType="MDSCHEMA_CUBES">
  <restrictions>
    <restriction key="CATALOG_NAME" value="FoodMart"/>
    <restriction key="CUBE_NAME" value="Sales"/>
  </restrictions>
  <expectations>
    <rowCountCheck operator="GREATER_OR_EQUAL" expectedCount="1"/>
  </expectations>
</discoverCheck>
```

### Alt: Hierarchische Selektoren
```xml
<cubeSelector>
  <dimensionSelector>
    <hierarchySelector>
      <levelSelector>
        <memberSelector>
          <checks/>
        </memberSelector>
      </levelSelector>
    </hierarchySelector>
  </dimensionSelector>
</cubeSelector>
```

### Neu: Flache DISCOVER Requests mit Restrictions
```xml
<!-- 1. Check: Cube existiert -->
<discoverCheck requestType="MDSCHEMA_CUBES">
  <restrictions>
    <restriction key="CUBE_NAME" value="Sales"/>
  </restrictions>
  <expectations>
    <rowCountCheck expectedCount="1"/>
  </expectations>
</discoverCheck>

<!-- 2. Check: Dimensions im Cube -->
<discoverCheck requestType="MDSCHEMA_DIMENSIONS">
  <restrictions>
    <restriction key="CUBE_NAME" value="Sales"/>
    <restriction key="DIMENSION_TYPE" value="2"/> <!-- TIME -->
  </restrictions>
  <expectations>
    <rowCountCheck expectedCount="1"/>
    <columnValueCheck columnName="DIMENSION_TYPE" expectedValue="2"/>
  </expectations>
</discoverCheck>

<!-- 3. Check: Levels in TIME Hierarchy -->
<discoverCheck requestType="MDSCHEMA_LEVELS">
  <restrictions>
    <restriction key="CUBE_NAME" value="Sales"/>
    <restriction key="HIERARCHY_UNIQUE_NAME" value="[Time].[Time]"/>
  </restrictions>
  <expectations>
    <rowCountCheck operator="GREATER_OR_EQUAL" expectedCount="3"/>
  </expectations>
</discoverCheck>
```

---

## 💡 Vorteile des XMLA-Ansatzes

### 1. Standard-konform
```xml
<!-- Verwendet offizielle XMLA Request Types -->
<discoverCheck requestType="MDSCHEMA_CUBES">
  <!-- Standard XMLA Restrictions -->
  <restrictions>
    <restriction key="CATALOG_NAME" value="FoodMart"/>
  </restrictions>
</discoverCheck>
```

### 2. Direkt testbar mit jedem XMLA Tool
```bash
# curl Test
curl -X POST http://localhost:8080/xmla \
  -H "Content-Type: text/xml" \
  -d @discover-cubes.xml
```

### 3. Mock/Stub einfach
```java
// HTTP-basiertes Mocking
MockWebServer server = new MockWebServer();
server.enqueue(new MockResponse().setBody(xmlResponse));
```

### 4. Dokumentation ist XMLA-Spezifikation
- Keine custom API-Dokumentation nötig
- XMLA Spec ist öffentlich verfügbar
- Restrictions sind standardisiert

### 5. Kompatibel mit allen XMLA-Servern
- Microsoft SSAS
- Mondrian
- Daanse XMLA
- icCube
- AtScale

---

## 📋 Anforderungen an Implementierung

### Muss-Kriterien

1. **DISCOVER Request Support**
   - Alle MDSCHEMA_* Requests
   - Alle DBSCHEMA_* Requests
   - Restrictions als Key-Value Pairs
   - Properties Support (Roles, Catalog, etc.)

2. **EXECUTE Request Support**
   - MDX Statements
   - Properties Support
   - Cell Value Validation
   - Performance Metrics (Execution Time)

3. **Check-Typen**
   - RowCount Checks
   - ColumnValue Checks
   - Cell Value Checks (für EXECUTE)
   - Existence Checks
   - Pattern Matching (Regex auf Column Values)

4. **Result Handling**
   - Strukturierte Ergebnisse
   - AssertJ Integration
   - JUnit Jupiter Integration
   - Fehlerberichte mit ✓ und ✗

5. **Session Management**
   - Role-basierte Tests
   - Multiple Sessions
   - Connection Pooling

### Kann-Kriterien

1. **Performance**
   - Parallele DISCOVER Requests
   - Connection Reuse
   - Result Caching

2. **Erweiterte Checks**
   - MDX Query Performance Benchmarks
   - Comparison Checks (compare results between servers)
   - Temporal Checks (data changes over time)

3. **Reporting**
   - HTML Report Generation
   - JUnit XML Output
   - JSON Result Export

4. **CI/CD Integration**
   - Maven Plugin
   - Gradle Plugin
   - GitHub Actions Example

---

## 🎯 Use Cases

### Use Case 1: Schema Validation nach Build
```xml
<checkSuite name="Post-Build Schema Validation">
  <!-- Prüfe: Alle erwarteten Cubes existieren -->
  <discoverCheck requestType="MDSCHEMA_CUBES">
    <restrictions>
      <restriction key="CATALOG_NAME" value="MyCatalog"/>
    </restrictions>
    <expectations>
      <rowCountCheck expectedCount="5"/>
    </expectations>
  </discoverCheck>

  <!-- Prüfe: Jeder Cube hat Measures -->
  <discoverCheck requestType="MDSCHEMA_MEASURES">
    <restrictions>
      <restriction key="CATALOG_NAME" value="MyCatalog"/>
      <restriction key="CUBE_NAME" value="Sales"/>
    </restrictions>
    <expectations>
      <rowCountCheck operator="GREATER_THAN" expectedCount="0"/>
    </expectations>
  </discoverCheck>
</checkSuite>
```

### Use Case 2: Rollenbasierte Zugriffstests
```xml
<checkSuite name="Role Access Tests">
  <!-- Session mit Manager-Rolle -->
  <discoverCheck requestType="MDSCHEMA_CUBES">
    <properties>
      <property key="Roles" value="Manager"/>
    </properties>
    <expectations>
      <rowCountCheck expectedCount="3"/>
      <columnValueCheck columnName="CUBE_NAME"
                        expectedValues="Sales,Warehouse,Inventory"/>
    </expectations>
  </discoverCheck>

  <!-- Session mit User-Rolle -->
  <discoverCheck requestType="MDSCHEMA_CUBES">
    <properties>
      <property key="Roles" value="User"/>
    </properties>
    <expectations>
      <rowCountCheck expectedCount="1"/>
      <columnValueCheck columnName="CUBE_NAME" expectedValue="Sales"/>
    </expectations>
  </discoverCheck>
</checkSuite>
```

### Use Case 3: Datenqualitäts-Checks
```xml
<checkSuite name="Data Quality Checks">
  <!-- Prüfe: Alle Measures haben FormatString -->
  <discoverCheck requestType="MDSCHEMA_MEASURES">
    <restrictions>
      <restriction key="CUBE_NAME" value="Sales"/>
    </restrictions>
    <expectations>
      <columnNotNullCheck columnName="DEFAULT_FORMAT_STRING"/>
    </expectations>
  </discoverCheck>

  <!-- Prüfe: Alle Dimensions haben Description -->
  <discoverCheck requestType="MDSCHEMA_DIMENSIONS">
    <restrictions>
      <restriction key="CUBE_NAME" value="Sales"/>
    </restrictions>
    <expectations>
      <columnNotNullCheck columnName="DESCRIPTION"/>
    </expectations>
  </discoverCheck>
</checkSuite>
```

### Use Case 4: MDX Query Validation
```xml
<checkSuite name="MDX Query Tests">
  <!-- Prüfe: Query liefert erwartetes Ergebnis -->
  <executeCheck id="total-sales">
    <statement>
      SELECT {[Measures].[Unit Sales]} ON COLUMNS
      FROM [Sales]
    </statement>
    <properties>
      <property key="Catalog" value="FoodMart"/>
    </properties>
    <expectations>
      <cellValueCheck row="0" column="0" expectedValue="266773"/>
      <executionTimeCheck maxMilliseconds="5000"/>
    </expectations>
  </executeCheck>
</checkSuite>
```

---

## 📚 Nächste Schritte

### Phase 1: Modell & Spezifikation (Aktuell)
- ✅ Test-Analyse abgeschlossen
- ✅ Konzept definiert
- 🔄 XMLA-Ecore-Modell erstellen
- 🔄 Spezifikation schreiben
- 🔄 Beispiele erstellen

### Phase 2: Implementation (im xmla Repository)
- [ ] Java-Klassen aus Ecore generieren
- [ ] XmlaCheckExecutor implementieren
- [ ] AssertJ Integration
- [ ] JUnit Jupiter Integration

### Phase 3: Testing & Dokumentation
- [ ] Unit-Tests
- [ ] Integration-Tests mit FoodMart
- [ ] Benutzer-Dokumentation
- [ ] API-Dokumentation

### Phase 4: Release & Adoption
- [ ] Maven Central Release
- [ ] Beispiel-Projekt
- [ ] Blog-Post
- [ ] Community-Feedback

---

## 📄 Übergabe-Checkliste

- [ ] `olapchecks-xmla.ecore` - XMLA-basiertes Ecore-Modell
- [ ] `XMLA_CHECKS_SPECIFICATION.md` - Vollständige Spezifikation
- [ ] `XMLA_REQUEST_MAPPING.md` - Mapping von Checks zu XMLA Requests
- [ ] `IMPLEMENTATION_GUIDE.md` - Implementierungs-Anleitung
- [ ] `example-discover-checks.xml` - DISCOVER Beispiele
- [ ] `example-execute-checks.xml` - EXECUTE Beispiele
- [ ] `example-role-checks.xml` - Rollen-Tests
- [ ] `example-integration.xml` - Vollständiges Beispiel
- [ ] `README.md` - Übersicht für xmla Repository
- [ ] Proof-of-Concept Code (optional)

---

**Status:** Bereit für Übergabe an xmla Repository
**Version:** 1.0
**Datum:** 2025-11-21
