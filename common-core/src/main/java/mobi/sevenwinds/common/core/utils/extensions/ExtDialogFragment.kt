package mobi.sevenwinds.common.core.utils.extensions

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.io.Serializable


fun DialogFragment.withExtra(key: String, value: Int?): DialogFragment {
    applyArgs(key, value) { bundle, k, v -> bundle.putInt(k, v) }
    return this
}

fun DialogFragment.withExtra(key: String, value: Boolean?): DialogFragment {
    applyArgs(key, value) { bundle, k, v -> bundle.putBoolean(k, v) }
    return this
}

fun DialogFragment.withExtra(key: String, value: String?): DialogFragment {
    applyArgs(key, value) { bundle, k, v -> bundle.putString(k, v) }
    return this
}

fun DialogFragment.withExtra(key: String, value: ByteArray?): DialogFragment {
    applyArgs(key, value, Bundle::putByteArray)
    return this
}

fun DialogFragment.withExtra(key: String, value: Serializable?): DialogFragment {
    applyArgs(key, value, Bundle::putSerializable)
    return this
}

private fun <T> DialogFragment.applyArgs(key: String, value: T?, applier: (Bundle, key: String, value: T) -> Unit) {
    if (value == null) return

    val args = arguments ?: Bundle()

    applier.invoke(args, key, value)

    arguments = args
}