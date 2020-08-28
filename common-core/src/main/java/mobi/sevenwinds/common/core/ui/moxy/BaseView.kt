package mobi.sevenwinds.common.core.ui.moxy

import android.content.Intent
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import mobi.sevenwinds.common.core.ui.cicerone.MyScreen

@StateStrategyType(AddToEndSingleStrategy::class)
interface BaseView : MvpView {
    fun requestPermissions(requestCode: Int, vararg permissions: String)

    fun ifHaveAllPermissions(ifTrue: () -> Unit, ifFalse: () -> Unit, vararg permissions: String)
    fun ifHaveAnyPermissions(ifTrue: () -> Unit, ifFalse: () -> Unit, vararg permissions: String)

    fun startScreenForResult(screen: MyScreen, requestCode: Int, onSuccess: (data: Intent?) -> Unit)

    fun startIntentForResult(intent: Intent, requestCode: Int, onSuccess: (data: Intent?) -> Unit)

    fun startIntentForResult(
        intent: Intent,
        requestCode: Int,
        onSuccess: (data: Intent?) -> Unit,
        onFail: ((data: Intent?) -> Unit)?
    )
}