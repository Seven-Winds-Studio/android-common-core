package mobi.sevenwinds.common.core.utils.extensions

import android.os.Bundle
import mobi.sevenwinds.common.core.ui.BaseFragment
import java.io.Serializable

fun BaseFragment.withExtra(key: String, value: Int?): BaseFragment {
    applyArgs(key, value) { bundle, k, v -> bundle.putInt(k, v) }
    return this
}

fun BaseFragment.withExtra(key: String, value: Boolean?): BaseFragment {
    applyArgs(key, value) { bundle, k, v -> bundle.putBoolean(k, v) }
    return this
}

fun BaseFragment.withExtra(key: String, value: String?): BaseFragment {
    applyArgs(key, value) { bundle, k, v -> bundle.putString(k, v) }
    return this
}

fun BaseFragment.withExtra(key: String, value: Serializable?): BaseFragment {
    applyArgs(key, value, Bundle::putSerializable)
    return this
}

fun BaseFragment.withExtra(bundle: Bundle?): BaseFragment {
    val args = arguments ?: Bundle()

    bundle?.let(args::putAll)

    arguments = args

    return this
}

private fun <T> BaseFragment.applyArgs(key: String, value: T?, applier: (Bundle, key: String, value: T) -> Unit) {
    if (value == null) return

    val args = arguments ?: Bundle()

    applier.invoke(args, key, value)

    arguments = args
}