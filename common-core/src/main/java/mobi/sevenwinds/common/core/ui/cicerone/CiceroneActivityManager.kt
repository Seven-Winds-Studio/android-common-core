package mobi.sevenwinds.common.core.ui.cicerone

import android.content.Context
import android.content.Intent
import mobi.sevenwinds.common.core.ui.BaseActivity

object CiceroneActivityManager {
    fun getAppropriateIntent(screen: MyScreen, context: Context): Intent? {
        val activityIntent = screen.getActivityIntent(context)
        val fragment = screen.fragment

        return when {
            activityIntent != null -> {
                activityIntent
            }
            fragment != null -> {
                CiceroneActivity.of(screen, context)
            }
            else -> null
        }
    }

    fun hasDifferentActivities(activity: BaseActivity?, screen: MyScreen?): Boolean {
        if (activity == null || screen == null || screen.fragment == null) return true

        return false
    }
}