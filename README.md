# Ludicrously fast json library for scala

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/7d84fbd2dcee449885b39c5b1a77c443)](https://www.codacy.com/app/jan-cajthaml/json?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jancajthaml-scala/json&amp;utm_campaign=Badge_Grade) [![Dependency Status](https://www.versioneye.com/user/projects/57dc1a3f500a3100425c97b3/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/57dc1a3f500a3100425c97b3)

* casual json serialization ~80ys (~0.08ms)
* casual json deserialization ~60ys (~0.06ms)

----

## What is does

maps json string to Map and vice versa

## How it works

It decomposes json parsing/loading problems into mapping flat json to key-value pairs and
flat map into "k:v," string. Blocks of {} and instance of Map[String,Any] means recursion.

`some features and some data types are TBD` 

-----

### Performance in details (in ms)

#### jsondumps
```
[info] ::Benchmark com.github.jancajthaml.json.jsondumps::
[info] cores: 4
[info] name: Java HotSpot(TM) 64-Bit Server VM
[info] osArch: x86_64
[info] osName: Mac OS X
[info] vendor: Oracle Corporation
[info] version: 25.91-b14
[info] Parameters(numberOfKeys -> 0): 0.001531
[info] Parameters(numberOfKeys -> 3000): 3.819704
[info] Parameters(numberOfKeys -> 6000): 7.899319
[info] Parameters(numberOfKeys -> 9000): 12.000378
[info] Parameters(numberOfKeys -> 12000): 16.56781
[info] Parameters(numberOfKeys -> 15000): 21.440188
[info] Parameters(numberOfKeys -> 18000): 26.214439
[info] Parameters(numberOfKeys -> 21000): 30.950173
[info] Parameters(numberOfKeys -> 24000): 35.723854
[info] Parameters(numberOfKeys -> 27000): 40.383375
[info] Parameters(numberOfKeys -> 30000): 44.961602
[info] Parameters(numberOfKeys -> 33000): 49.492231
[info] Parameters(numberOfKeys -> 36000): 54.027376
[info] Parameters(numberOfKeys -> 39000): 58.546606
[info] Parameters(numberOfKeys -> 42000): 63.09093
[info] Parameters(numberOfKeys -> 45000): 67.88862
[info] Parameters(numberOfKeys -> 48000): 72.560683
[info] Parameters(numberOfKeys -> 51000): 77.327245
[info] Parameters(numberOfKeys -> 54000): 81.965045
[info] Parameters(numberOfKeys -> 57000): 87.551014
[info] Parameters(numberOfKeys -> 60000): 90.641135
[info] Parameters(numberOfKeys -> 63000): 96.539909
[info] Parameters(numberOfKeys -> 66000): 100.480757
[info] Parameters(numberOfKeys -> 69000): 106.730581
[info] Parameters(numberOfKeys -> 72000): 110.775642
[info] Parameters(numberOfKeys -> 75000): 115.850534
[info] Parameters(numberOfKeys -> 78000): 123.328884
[info] Parameters(numberOfKeys -> 81000): 133.086574
[info] Parameters(numberOfKeys -> 84000): 140.350652
[info] Parameters(numberOfKeys -> 87000): 149.891334
[info] Parameters(numberOfKeys -> 90000): 159.868311
```

#### jsonloads
```
[info] ::Benchmark com.github.jancajthaml.json.jsonloads::
[info] cores: 4
[info] name: Java HotSpot(TM) 64-Bit Server VM
[info] osArch: x86_64
[info] osName: Mac OS X
[info] vendor: Oracle Corporation
[info] version: 25.91-b14
[info] Parameters(numberOfKeys -> 0): 0.004812
[info] Parameters(numberOfKeys -> 3000): 4.865389
[info] Parameters(numberOfKeys -> 6000): 9.774624
[info] Parameters(numberOfKeys -> 9000): 14.714519
[info] Parameters(numberOfKeys -> 12000): 19.892579
[info] Parameters(numberOfKeys -> 15000): 25.22713
[info] Parameters(numberOfKeys -> 18000): 30.48835
[info] Parameters(numberOfKeys -> 21000): 35.676145
[info] Parameters(numberOfKeys -> 24000): 40.8962
[info] Parameters(numberOfKeys -> 27000): 46.111992
[info] Parameters(numberOfKeys -> 30000): 51.29934
[info] Parameters(numberOfKeys -> 33000): 56.581007
[info] Parameters(numberOfKeys -> 36000): 61.807249
[info] Parameters(numberOfKeys -> 39000): 66.969753
[info] Parameters(numberOfKeys -> 42000): 72.628665
[info] Parameters(numberOfKeys -> 45000): 77.724146
[info] Parameters(numberOfKeys -> 48000): 82.973819
[info] Parameters(numberOfKeys -> 51000): 88.05248
[info] Parameters(numberOfKeys -> 54000): 93.229682
[info] Parameters(numberOfKeys -> 57000): 98.441395
[info] Parameters(numberOfKeys -> 60000): 103.737445
[info] Parameters(numberOfKeys -> 63000): 109.140576
[info] Parameters(numberOfKeys -> 66000): 114.272794
[info] Parameters(numberOfKeys -> 69000): 119.978274
[info] Parameters(numberOfKeys -> 72000): 125.657045
[info] Parameters(numberOfKeys -> 75000): 131.959808
[info] Parameters(numberOfKeys -> 78000): 138.087714
[info] Parameters(numberOfKeys -> 81000): 144.614552
[info] Parameters(numberOfKeys -> 84000): 149.004824
[info] Parameters(numberOfKeys -> 87000): 157.020853
[info] Parameters(numberOfKeys -> 90000): 162.424383
```

-----

## Implementation tricks:

### jsonloads

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

### jsondumps

| v match             | resolved into              |
| ------------------- |:--------------------------:|
| v:String            | String(" + x.toString + ") |
| null                | String(null)               |
| v: Map[String, Any] | recursion                  |
| v:Any               | String(x.toString)         |
