package mobi.sevenwinds.common_core.utils.extensions

import android.content.res.Resources
import androidx.annotation.PluralsRes

fun Resources.getPlural(@PluralsRes pluralRes: Int, count: Int):String {
    return this.getQuantityString(pluralRes, count, count)
}