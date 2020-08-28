package mobi.sevenwinds.common.core.utils.extensions

import android.content.Intent
import android.os.Bundle
import java.io.Serializable

fun Intent.withExtra(key: String, value: Int?): Intent {
    value?.let { this.putExtra(key, it) }
    return this
}

fun Intent.withExtra(key: String, value: Long?): Intent {
    value?.let { this.putExtra(key, it) }
    return this
}

fun Intent.withExtra(key: String, value: String?): Intent {
    value?.let { this.putExtra(key, it) }
    return this
}

fun Intent.withExtra(key: String, value: Boolean?): Intent {
    value?.let { this.putExtra(key, it) }
    return this
}

fun Intent.withExtra(key: String, value: Serializable?): Intent {
    value?.let { this.putExtra(key, it) }
    return this
}

fun Intent.withExtra(bundle: Bundle?): Intent {
    bundle?.let { this.putExtras(it) }
    return this
}