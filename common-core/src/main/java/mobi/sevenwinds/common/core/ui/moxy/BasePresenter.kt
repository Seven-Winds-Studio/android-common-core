package mobi.sevenwinds.common.core.ui.moxy

import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import mobi.sevenwinds.common.core.app.BaseApp

abstract class BasePresenter<T : BaseView> : MvpPresenter<T>() {
    protected val compositeDisposable = CompositeDisposable()

    fun router() = BaseApp.instance.getRouter()

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}