package mobi.sevenwinds.common.core.utils.extensions

import android.content.res.Resources
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.util.TypedValue.applyDimension

fun Int.dp(): Int = toFloat().dp()

fun Float.dp(): Int {
    val displayMetrics = Resources.getSystem().displayMetrics
    val dp = applyDimension(COMPLEX_UNIT_DIP, this, displayMetrics)
    return dp.toInt()
}