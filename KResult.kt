sealed interface KResult<T, E>{
    fun bool(): Boolean = this is Ok

    fun option(): T? = when(this){
        is Err -> null
        is Ok -> value
    }

    infix fun and(other: KResult<T, E>): KResult<T, E> = when(this){
        is Err -> other
        is Ok -> this
    }

    infix fun or(other: KResult<T, E>): KResult<T, E> = when(this){
        is Err -> other
        is Ok -> this
    }
}

data class Err<T, E> internal constructor(val error: E): KResult<T, E>
data class Ok<T, E> internal constructor(val value: T): KResult<T, E>

inline fun <T, E> KResult<T, E>.check(block: (Err<T, E>) -> Nothing): KResult<T, E> {
    if (this is Err) block(this)
    return this
}

inline fun <T, E, U> KResult<T, E>.map(op: (T) -> U): KResult<U, E> = when(this){
    is Err -> err(this)
    is Ok -> ok(op(value))
}

inline fun <T, E, U> KResult<T, E>.andThen(op: (T) -> KResult<U, E>): KResult<U, E> = when(this){
    is Err -> err(this)
    is Ok -> op(value)
}

inline fun <T, E, U> KResult<T, E>.orElse(op: (E) -> KResult<T, U>): KResult<T, U> = when(this){
    is Err -> op(error)
    is Ok -> ok(this)
}

fun <T, E> ok(value: T): KResult<T, E> = Ok(value)
fun <E> ok(): KResult<Any?, E> = Ok(null)
fun <T, E> ok(old: Ok<T, *>): KResult<T, E> = @Suppress("UNCHECKED_CAST") (old as KResult<T, E>)

fun <T, E> err(error: E): KResult<T, E> = Err(error)
fun <T> err(): KResult<T, Any?> = Err(null)
fun <T, E> err(old: Err<*, E>): KResult<T, E> = @Suppress("UNCHECKED_CAST") (old as KResult<T, E>)

fun<T> T?.asRes(): KResult<T, Any?> = if (this == null) err() else ok(this)
fun<T, E> T?.asRes(error: E): KResult<T, E> = if (this == null) err(error) else ok(this)

fun<T> Optional<T>.res(): KResult<T, Any?> = if (isEmpty) err() else ok(get())
fun<T, E> Optional<T>.res(error: E): KResult<T, E> = if (isEmpty) err(error) else ok(get())

fun<T> Result<T>.res(): KResult<T, Any?> = if (isFailure) err() else ok(getOrThrow())
fun<T, E> Result<T>.res(error: E): KResult<T, E> = if (isFailure) err(error) else ok(getOrThrow())
