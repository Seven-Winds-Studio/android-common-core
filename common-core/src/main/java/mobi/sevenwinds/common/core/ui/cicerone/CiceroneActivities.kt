package mobi.sevenwinds.common.core.ui.cicerone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import mobi.sevenwinds.common.core.ui.BaseActivity
import mobi.sevenwinds.common.core.ui.BaseFragment
import mobi.sevenwinds.common.core.utils.extensions.withExtra
import mobi.sevenwinds.common_core.R

class CiceroneActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty_cicerone)

        navigator = MyNavigator(this, R.id.v_container)

        if (savedInstanceState == null) {
            intent.getStringExtra(CLASS_NAME)
                ?.let { Class.forName(it).newInstance() as? BaseFragment }
                ?.let { fragment ->
                    val screen = Screens.of { fragment.withExtra(intent.extras) }
                    router().replaceScreen(screen)
                }
        }
    }

    override fun onBackPressed() {
        router().exit() // cause must be handled by router (fragment stacks, etc.)
    }

    companion object {
        const val CLASS_NAME = "CiceroneActivity_CLASS_NAME"

        private fun <T : CiceroneActivity> intentOf(
            screen: MyScreen,
            context: Context,
            clazz: Class<T>
        ): Intent {
            val fragmentName = screen.fragment::class.java.canonicalName
            val fragmentArguments = screen.fragment.arguments

            return Intent(context, clazz)
                .withExtra(CLASS_NAME, fragmentName)
                .withExtra(fragmentArguments)
        }

        fun of(screen: MyScreen, context: Context): Intent {
            return intentOf(screen, context, CiceroneActivity::class.java)
        }
    }
}