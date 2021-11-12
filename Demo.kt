enum class ParseError {
    Invalid,
    Expired
}

fun parse(): KResult<Int, ParseError> = err(ParseError.Invalid)

fun handle(): KResult<String, ParseError> =
    when (val res = parse()) {
        is Ok -> res.map(Int::toString)
        is Err -> err(res)
    }

fun run(): KResult<String, String> {
    val res = handle().check { return it.mapErr(ParseError::toString) }
    return Ok("yeah!")
}

fun main() {
    run()
}
