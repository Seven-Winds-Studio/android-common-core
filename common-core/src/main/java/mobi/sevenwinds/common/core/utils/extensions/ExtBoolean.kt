package mobi.sevenwinds.common.core.utils.extensions

/**
 * Returns first value is this not null && TRUE, otherwise returns second value
 */
fun <T> Boolean?.ternaryMap(first: T, second: T): T {
    return when (this) {
        true -> first
        else -> second
    }
}