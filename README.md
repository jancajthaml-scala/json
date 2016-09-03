ludicrously fast json library for scala

----

* serialize Map to json
* deserialize json to Map

currently supporting flat json (no nesting)

-----

## jsonloads

(k, v) = chunk separated by `,` splitted by `:`

| leading char of v | resolved as        | type    | TBD?                                  |
| ----------------- |:------------------:|:-------:|:-------------------------------------:|
| t                 | true               | Boolean |                                       |
| f                 | false              | Boolean |                                       |
| 0                 | 0                  | Integer |                                       |
| 1                 | _.parseInt         | Integer |                                       |
| 2                 | _.parseInt         | Integer |                                       |
| 3                 | _.parseInt         | Integer |                                       |
| 4                 | _.parseInt         | Integer |                                       |
| 5                 | _.parseInt         | Integer |                                       |
| 6                 | _.parseInt         | Integer |                                       |
| 7                 | _.parseInt         | Integer |                                       |
| 8                 | _.parseInt         | Integer |                                       |
| 9                 | _.parseInt         | Integer |                                       |
| "                 | " + _.toString + " | String  |                                       |
| n                 | null               | null    |                                       |
| u                 | ---                | ---     | YES (undefined or unicode)            |
| e                 | ---                | ---     | YES (decimal number sci notation)     |
| {                 | ---                | ---     | YES (recursion to nested block enter) |
| }                 | ---                | ---     | YES (recursion to nested block leave) |

## jsondumps

| v match  | resolved into              |
| -------- |:--------------------------:|
| x:String | String(" + x.toString + ") |
| null     | String(null)               |
| x:Any    | String(x.toString)         |


## Performance (in ms)

### jsondumps
```
[info] ::Benchmark com.github.jancajthaml.json.jsondumps::
[info] cores: 4
[info] name: Java HotSpot(TM) 64-Bit Server VM
[info] osArch: x86_64
[info] osName: Mac OS X
[info] vendor: Oracle Corporation
[info] version: 25.91-b14
[info] Parameters(numberOfKeys -> 0): 0.001741
[info] Parameters(numberOfKeys -> 3000): 3.861912
[info] Parameters(numberOfKeys -> 6000): 7.882357
[info] Parameters(numberOfKeys -> 9000): 11.966896
[info] Parameters(numberOfKeys -> 12000): 16.542081
[info] Parameters(numberOfKeys -> 15000): 21.472868
[info] Parameters(numberOfKeys -> 18000): 26.346799
[info] Parameters(numberOfKeys -> 21000): 31.107558
[info] Parameters(numberOfKeys -> 24000): 35.943811
[info] Parameters(numberOfKeys -> 27000): 40.540713
[info] Parameters(numberOfKeys -> 30000): 45.142919
```

### jsonloads
```
[info] ::Benchmark com.github.jancajthaml.json.jsonloads::
[info] cores: 4
[info] name: Java HotSpot(TM) 64-Bit Server VM
[info] osArch: x86_64
[info] osName: Mac OS X
[info] vendor: Oracle Corporation
[info] version: 25.91-b14
[info] Parameters(numberOfKeys -> 0): 0.003103
[info] Parameters(numberOfKeys -> 3000): 6.052836
[info] Parameters(numberOfKeys -> 6000): 12.164405
[info] Parameters(numberOfKeys -> 9000): 18.296846
[info] Parameters(numberOfKeys -> 12000): 24.610448
[info] Parameters(numberOfKeys -> 15000): 31.111202
[info] Parameters(numberOfKeys -> 18000): 37.556477
[info] Parameters(numberOfKeys -> 21000): 43.961495
[info] Parameters(numberOfKeys -> 24000): 50.332477
[info] Parameters(numberOfKeys -> 27000): 56.705562
[info] Parameters(numberOfKeys -> 30000): 63.123621
```
