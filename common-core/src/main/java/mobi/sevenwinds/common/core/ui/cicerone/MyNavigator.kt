package mobi.sevenwinds.common.core.ui.cicerone

import android.content.Intent
import android.util.Log
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import mobi.sevenwinds.common.core.ui.BaseActivity
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace

class MyNavigator(
    val activity: BaseActivity,
    val fragmentManager: FragmentManager,
    val containerId: Int
) : SupportAppNavigator(activity, fragmentManager, containerId) {

    constructor(
        activity: BaseActivity,
        containerId: Int
    ) : this(activity, activity.supportFragmentManager, containerId)

    override fun applyCommands(commands: Array<out Command>) {
        val rootIndex = commands.indexOfLast(rootPredicate)
        if (rootIndex >= 0) {
            val newCommands = commands.drop(rootIndex + 1)
            if (newCommands.isEmpty()) {
                Log.e("AA", "Null chain of commands!")
                return
            }
            val firstScreen = newCommands[0]
            when (firstScreen) {
                is Replace -> startWithoutBackStack(firstScreen.screen)
                is Forward -> startWithoutBackStack(firstScreen.screen)
                else -> Log.e("AA", "WTF! Wrong type!")
            }

            if (newCommands.size > 1) {
                super.applyCommands(newCommands.drop(1).toTypedArray())
            }
        } else {
            super.applyCommands(commands)
        }
    }

    private fun startWithoutBackStack(screen: Screen) {
        val activityIntent = (screen as? SupportAppScreen)?.getActivityIntent(activity)
        if (activityIntent == null) {
            Log.e("AA", "Wtf?! wrong activity intent")
            return
        }

        //TODO there is sort of 'blink' - which is not existing in case of simple "replacing" root activity. Maybe think about chain of finish & replace at last step?

        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        activity?.startActivity(activityIntent)
    }

    override fun backToUnexisting(screen: SupportAppScreen?) {
        val activityIntent = screen?.getActivityIntent(activity)
        if (activityIntent != null) {
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            activity?.startActivity(activityIntent)
        } else {
            Log.e("AA", "Not found screen with key " + screen?.screenKey)
            super.backToUnexisting(screen)
        }
    }

    override fun fragmentForward(command: Forward?) {
        val screen = command?.screen as? MyScreen

        when {
            screen != null && containerId == NO_CONTAINER -> openInNewActivity(screen)
            screen != null && screen.alwaysInNewActivity -> openInNewActivity(screen)
            screen != null && CiceroneActivityManager.hasDifferentActivities(
                activity,
                screen
            ) -> openInNewActivity(
                screen
            )
            else -> super.fragmentForward(command)
        }
    }

    override fun fragmentReplace(command: Replace?) {
        val screen = command?.screen as? MyScreen

        when {
            screen != null && containerId == NO_CONTAINER -> {
                openInNewActivity(screen)
                activity?.finish()
            }
            screen != null && CiceroneActivityManager.hasDifferentActivities(activity, screen) -> {
                openInNewActivity(screen)
                activity?.finish()
            }

            /* do nothing in case of 'alwaysInNewActivity' flag - cause Replace command */
            else -> super.fragmentReplace(command)
        }
    }

    private fun openInNewActivity(screen: MyScreen) {
        if (activity == null) return

        CiceroneActivityManager.getAppropriateIntent(screen, activity)?.let(activity::startActivity)
    }

    private var forwardAnimations: Array<Int>? = null
    private var replaceAnimations: Array<Int>? = null

    fun setForwardAnimation(
        @AnimatorRes @AnimRes enter: Int,
        @AnimatorRes @AnimRes exit: Int, @AnimatorRes @AnimRes popEnter: Int,
        @AnimatorRes @AnimRes popExit: Int
    ) {
        forwardAnimations = arrayOf(enter, exit, popEnter, popExit)
    }

    fun setForwardAnimation(
        @AnimatorRes @AnimRes enter: Int,
        @AnimatorRes @AnimRes exit: Int
    ) {
        forwardAnimations = arrayOf(enter, exit)
    }

    fun setReplaceAnimation(
        @AnimatorRes @AnimRes enter: Int,
        @AnimatorRes @AnimRes exit: Int, @AnimatorRes @AnimRes popEnter: Int,
        @AnimatorRes @AnimRes popExit: Int
    ) {
        replaceAnimations = arrayOf(enter, exit, popEnter, popExit)
    }

    fun setReplaceAnimation(
        @AnimatorRes @AnimRes enter: Int,
        @AnimatorRes @AnimRes exit: Int
    ) {
        replaceAnimations = arrayOf(enter, exit)
    }

    override fun setupFragmentTransaction(
        command: Command?,
        currentFragment: Fragment?,
        nextFragment: Fragment?,
        fragmentTransaction: FragmentTransaction?
    ) {
        super.setupFragmentTransaction(command, currentFragment, nextFragment, fragmentTransaction)

        when (command) {
            is Forward -> {
                forwardAnimations?.let {
                    setCustomAnimation(it, fragmentTransaction)
                }
            }
            is Replace -> {
                replaceAnimations?.let {
                    setCustomAnimation(it, fragmentTransaction)
                }
            }
        }
    }

    private fun setCustomAnimation(animArray: Array<Int>, ft: FragmentTransaction?){
        when(animArray.size){
            2 -> ft?.setCustomAnimations( animArray[0], animArray[1])
            4 -> ft?.setCustomAnimations( animArray[0], animArray[1], animArray[2], animArray[3])
            else -> {}
        }
    }

    companion object {
        private val rootPredicate: (Command) -> Boolean = { it is BackTo && it.screen == null }

        const val NO_CONTAINER = -1
    }
}