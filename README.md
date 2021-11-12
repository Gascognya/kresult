# kresult
a rusty Result for kotlin

```
fun parse(): KResult<Int, ParseError> = err(ParseError.Invalid)

fun handle(): KResult<String, ParseError> =
    when (val res = parse()) {
        is Ok -> res.map(Int::toString) //KResult<Int,ParseError> -(map)-> <String,ParseError>
        is Err -> err(res) //KResult<Int,ParseError> -(as)-> <String,ParseError>
    }

fun run(): KResult<String, String> {
    val res = handle().check { return it.mapErr(ParseError::toString) } // rust `?` op
    return Ok("yeah!")
}
```

you can download **Custom Postfix Templates** for IDEA
```
.ok: the ok result
    ANY -> ok($expr$)

.err: the err result
    ANY -> err($expr$)

.match: match result
    ANY -> when($expr$){\
        is Ok -> TODO()\
        is Err -> TODO()\
    }

.check: check err
    ANY -> $expr$.check { return it }
```

something.ok => ok(something)
result.check => result.check{ return it }
check = rust's `?` operator

and def some live template (res0, res1, res2) just like kotlin's (fun0, fun1, fun2)
```
fun $NAME$($PARAM1$ : $PARAM1TYPE$, $PARAM2$ : $PARAM2TYPE$) : KResult<$T1$, $T2$> {
$END$
}
```
