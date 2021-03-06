# Ludicrously fast json library for scala

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/7d84fbd2dcee449885b39c5b1a77c443)](https://www.codacy.com/app/jan-cajthaml/json?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jancajthaml-scala/json&amp;utm_campaign=Badge_Grade) [![Build Status](https://travis-ci.org/jancajthaml-scala/json.svg?branch=master)](https://travis-ci.org/jancajthaml-scala/json)

* casual json serialization ~80ys (~0.08ms)
* casual json deserialization ~60ys (~0.06ms)

## What is does

maps json string to Map and vice versa

## How it works

It decomposes json parsing/loading problems into mapping flat json to key-value pairs and
flat map into "k:v," string. Blocks of {} and instance of Map[String,Any] means recursion.

`some features and some data types are TBD` 

## Implementation tricks:

### jsonloads

(k, v) = chunk separated by `,` splitted by `:`

| leading char of v | resolved as              | type            |
| ----------------- |:------------------------:|:---------------:|
| t                 | true                     | Boolean         |
| f                 | false                    | Boolean         |
| 0                 | 0                        | Integer         |
| 1                 | positibe integral number | Integer \| Long |
| 2                 | positibe integral number | Integer \| Long |
| 3                 | positibe integral number | Integer \| Long |
| 4                 | positibe integral number | Integer \| Long |
| 5                 | positibe integral number | Integer \| Long |
| 6                 | positibe integral number | Integer \| Long |
| 7                 | positibe integral number | Integer \| Long |
| 8                 | positibe integral number | Integer \| Long |
| 9                 | positibe integral number | Integer \| Long |
| -                 | negative integral number | Integer \| Long |
| "                 | " + _ + "                | String          |
| n                 | null                     | null            |

####TBD

| leading char of v | resolved as              | type            |
| ----------------- |:------------------------:|:---------------:|
| u                 | ---                      | ---             |
| e                 | ---                      | ---             |
| {                 | ---                      | ---             |
| }                 | ---                      | ---             |
| [                 | ---                      | ---             |
| ]                 | ---                      | ---             |

### jsondumps

| v match             | resolved into              |
| ------------------- |:--------------------------:|
| v:String            | String(" + v + ")          |
| null                | String(null)               |
| v: Map[String, Any] | recursion                  |
| v:Any               | String(v.toString)         |
