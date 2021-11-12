enum class ParseError {
    Invalid,
    Expired
}

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

fun main() {
    run()
}
