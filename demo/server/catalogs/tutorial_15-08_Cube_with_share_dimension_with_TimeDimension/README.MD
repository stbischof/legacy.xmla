# Documentation
### CatalogName : tutorial_15-08_Cube_with_share_dimension_with_TimeDimension
### Schema Minimal_Time_Dimension : 

    Time dimensions based on year/month/week/day are coded differently in the Mondrian schema due to the MDX time related functions such as:

    ParallelPeriod([level[, index[, member]]])
    PeriodsToDate([level[, member]])
    WTD([member])
    MTD([member])
    QTD([member])
    YTD([member])
    LastPeriod(index[, member])

    Time dimensions have type="TimeDimension". The role of a level in a time dimension is indicated by the level's levelType attribute, whose allowable values are as follows:

    TimeYears 	Level is a year
    TimeQuarters 	Level is a quarter
    TimeMonths 	Level is a month
    TimeWeeks 	Level is a week
    TimeDays 	Level represents days
  
### Public Dimensions:

    Time

##### Dimension "Time":

Hierarchies:

    Hierarchy0

##### Hierarchy Hierarchy0:

Tables: "Fact"

Levels: "Years, Quarters, Months, Week, Day"

###### Level "Years" :

    column(s): YEAR_ID

###### Level "Quarters" :

    column(s): QTR_NAME

###### Level "Months" :

    column(s): MONTH_NAME

###### Level "Week" :

    column(s): WEEK_IN_MONTH

###### Level "Day" :

    column(s): DAY_IN_MONTH

---
### Cubes :

    CubeTimeDimension

---
#### Cube "CubeTimeDimension":

    

##### Table: "Fact"

##### Dimensions:
##### Dimension: "Time -> Time":

### Cube "CubeTimeDimension" diagram:

---

```mermaid
%%{init: {
"theme": "default",
"themeCSS": [
    ".er.entityBox {stroke: black;}",
    ".er.attributeBoxEven {stroke: black;}",
    ".er.attributeBoxOdd {stroke: black;}",
    "[id^=entity-c] .er.entityBox { fill: lightgreen;} ",
    "[id^=entity-d] .er.entityBox { fill: powderblue;} ",
    "[id^=entity-h] .er.entityBox { fill: pink;} "
]
}}%%
erDiagram
c0["CubeTimeDimension"]{
M Measure-Sum ""
D Time ""
}
d00["Time"] {
H _ ""
}
h000["_"] {
L Years ""
L Quarters ""
L Months ""
L Week ""
L Day ""
}
"c0" ||--|| "d00" : "DATE_KEY-DATE_KEY"
"d00" ||--|| "h000" : "DATE_KEY-YEAR_ID"
"d00" ||--|| "h000" : "DATE_KEY-QTR_NAME"
"d00" ||--|| "h000" : "DATE_KEY-MONTH_NAME"
"d00" ||--|| "h000" : "DATE_KEY-WEEK_IN_MONTH"
"d00" ||--|| "h000" : "DATE_KEY-DAY_IN_MONTH"
```
---
### Cube Matrix for Minimal_Time_Dimension:
```mermaid
quadrantChart
title Cube Matrix
x-axis small level number --> high level number
y-axis Low row count --> High row count
quadrant-1 Complex
quadrant-2 Deep
quadrant-3 Simple
quadrant-4 Wide

Cube CubeTimeDimension: [0.2500, 0.0000]
```
---
### Database :
---
```mermaid
---
title: Diagram;
---
erDiagram
"Fact✔"{
timestamp DATE_KEY "✔"
int VALUE "✔"
int YEAR_ID "✔"
string QTR_ID "✔"
string QTR_NAME "✔"
string MONTH_ID "✔"
string MONTH_NAME "✔"
int WEEK_IN_MONTH "✔"
int DAY_IN_MONTH "✔"
}

```
---
## Validation result for schema Minimal_Time_Dimension
## <span style='color: blue;'>WARNING</span> : 
|Type|   |
|----|---|
|DATABASE|Table: Schema must be set|
