package jetbrains.datalore.base.unsupported

/*

Use UNSUPPORTED() instead of TODO() from Kotlin standard library.
TODO() throws an `Error` and we are generally don't try to catch errors.

 */

@Suppress("FunctionName")
fun UNSUPPORTED(): Nothing = throw UnsupportedOperationException()

@Suppress("FunctionName")
fun UNSUPPORTED(what: String): Nothing = throw UnsupportedOperationException(what)
