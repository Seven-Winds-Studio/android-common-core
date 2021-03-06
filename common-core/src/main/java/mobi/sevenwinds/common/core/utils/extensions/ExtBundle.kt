package mobi.sevenwinds.common.core.utils.extensions

import android.os.Bundle

fun Bundle.getIntOrNull(key: String): Int? {
    return when {
        this.containsKey(key) -> this.getInt(key)
        else -> null
    }
}