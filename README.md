ludicrously fast json library for scala

----

* serialize Map to json
* deserialize json to Map

currently supporting flat json (no nesting)

TDB - decimals ( *.* , sci notation )


-----

jsonloads

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

jsondumps

| v match  | resolved into              |
| -------- |:--------------------------:|
| x:String | String(" + x.toString + ") |
| null     | String(null)               |
| x:Any    | String(x.toString)         |

