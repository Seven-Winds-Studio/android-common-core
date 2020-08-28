package mobi.sevenwinds.common.core.ui.cicerone

import android.content.Context
import android.content.Intent
import mobi.sevenwinds.common.core.ui.BaseActivity
import mobi.sevenwinds.common.core.ui.BaseFragment

@Suppress("FunctionName")
object Screens {

    fun <T : BaseActivity> of(clazz: Class<T>): MyScreen {
        return object : MyScreen(true) {
            override fun getActivityIntent(context: Context?): Intent {
                return Intent(context, clazz)
            }

            override fun getScreenKey() = clazz.canonicalName
        }
    }

    fun ofCiceroneNewActivity(screen: MyScreen): MyScreen {
        return object : MyScreen(true) {
            override fun getActivityIntent(context: Context?): Intent? {
                return when (context) {
                    null -> null
                    else -> CiceroneActivityManager.getAppropriateIntent(screen, context)
                }
            }
        }
    }

    fun ofActivityIntent(intentProvider: (context: Context?) -> Intent?): MyScreen {
        return object : MyScreen(true) {
            override fun getActivityIntent(context: Context?) = intentProvider.invoke(context)
        }
    }

    fun <T : BaseFragment> of(fragmentSupplier: () -> T) =
        of(fragmentSupplier, false)

    fun <T : BaseFragment> of(
        fragmentSupplier: () -> T,
        alwaysInNewActivity: Boolean = false
    ): MyScreen {
        return object : MyScreen(alwaysInNewActivity) {
            override fun getFragment() = fragmentSupplier.invoke()

            override fun getScreenKey() = fragment.javaClass.canonicalName
        }
    }

    fun <T : BaseActivity> ofParametrizied(
        clazz: Class<T>,
        provider: (Intent) -> Intent
    ): MyScreen {
        return object : MyScreen(true) {
            override fun getActivityIntent(context: Context?): Intent {
                val intent = Intent(context, clazz)
                return provider.invoke(intent)
            }

            override fun getScreenKey() = clazz.canonicalName
        }
    }
}