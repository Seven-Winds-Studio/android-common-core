package mobi.sevenwinds.common.core.ui.cicerone

import android.annotation.SuppressLint
import io.reactivex.subjects.BehaviorSubject
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import java.util.concurrent.TimeUnit

class MyRouter : Router() {
    val bsNavigator = BehaviorSubject.create<Screen>()

    init {
        initClickThrottler()
    }

    @SuppressLint("CheckResult") //cause this is app-global router
    private fun initClickThrottler() {
        bsNavigator
            .throttleFirst(400, TimeUnit.MILLISECONDS)
            .subscribe {
                super.navigateTo(it)
            }
    }

    override fun navigateTo(screen: Screen?) {
        screen?.also(bsNavigator::onNext)
    }
}