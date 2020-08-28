package mobi.sevenwinds.common.core.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.inputmethod.InputMethodManager


object Gears {
    fun hideKeyboard(activity: Activity?) {
        if (activity == null) return

        (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.hideSoftInputFromWindow(activity.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        return convertDpToPixel(dp, context.resources)
    }

    fun convertDpToPixel(dp: Float, resources: Resources): Float {
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
        return if (p1 != null && p2 != null) block(p1, p2) else null
    }

    fun <T1 : Any, T2 : Any, T3 : Any, R : Any> safeLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3) -> R?): R? {
        return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
    }

    fun <T> isOneOf(target: T?, vararg candidates: T): Boolean {
        return candidates.any { it == target }
    }
}
