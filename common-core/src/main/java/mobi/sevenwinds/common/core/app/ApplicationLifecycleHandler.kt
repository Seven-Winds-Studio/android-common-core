package mobi.sevenwinds.common.core.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import mobi.sevenwinds.common.core.ui.BaseActivity

class ApplicationLifecycleHandler(val app: BaseApp) : Application.ActivityLifecycleCallbacks {
    private val BACKGROUND_DELAY: Long = 500
    private val mBackgroundDelayHandler = Handler()

    private var mInBackground = true
    private var mBackgroundTransition: Runnable? = null

    override fun onActivityPaused(activity: Activity?) {
        BaseApp.currentActivity = null
        app.getNavigatorHolder().removeNavigator()

        if (!mInBackground && mBackgroundTransition == null) {
            mBackgroundTransition = Runnable {
                mInBackground = true
                mBackgroundTransition = null

                onBackgrounded()
            }
            mBackgroundDelayHandler.postDelayed(mBackgroundTransition, BACKGROUND_DELAY)
        }
    }

    override fun onActivityResumed(activity: Activity?) {
        val baseActivity = activity as? BaseActivity

        BaseApp.currentActivity = baseActivity
        app.getNavigatorHolder().setNavigator(baseActivity?.navigator)

        mBackgroundTransition?.let(mBackgroundDelayHandler::removeCallbacks)
        mBackgroundTransition = null

        if (mInBackground) {
            mInBackground = false

            onForegrounded()
        }
    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
    }

    private fun onBackgrounded() {
    }

    private fun onForegrounded() {
    }
}