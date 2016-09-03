ludicrously fast json library for scala

----

* serialize Map to json
* deserialize json to Map

currently supporting flat json (no nesting)

TDB - decimals ( *.* , sci notation )


-----

jsonloads


(t|f) => boolean (true|false)
(0|1|2|3|4|5|6|7|8|9) => integral number (decimals unsuported)
(") => string
(n) => null
(u) => undefined or unicode ... skip
(e) => decimal number sci notation ... skip


jsondumps

(x:String) => String(" + x.toString + ")
(null) => String(null)
(x:Any) => String(x.toString)
