package mobi.sevenwinds.common.core.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.CallSuper
import com.afollestad.inlineactivityresult.startActivityForResult
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import mobi.sevenwinds.common.core.app.BaseApp
import mobi.sevenwinds.common.core.ui.cicerone.CiceroneActivityManager
import mobi.sevenwinds.common.core.ui.cicerone.MyScreen
import mobi.sevenwinds.common.core.ui.moxy.BaseView
import mobi.sevenwinds.common.core.ui.moxy.MoxyFragment
import mobi.sevenwinds.common.core.utils.extensions.ternaryMap

abstract class BaseFragment : MoxyFragment(), BaseView {
    protected var compositeDisposable = CompositeDisposable()

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (compositeDisposable.isDisposed) {
            compositeDisposable = CompositeDisposable()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    open fun baseActivity(): BaseActivity? {
        return activity as? BaseActivity
    }

    open fun addSubscription(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    @CallSuper
    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }

    fun <T : BaseActivity> startActivity(clazz: Class<T>) {
        baseActivity()?.let { startActivity(Intent(it, clazz)) }
    }

    open fun router() = BaseApp.instance.getRouter()


    override fun requestPermissions(requestCode: Int, vararg permissions: String) =
        requestPermissions(permissions, requestCode)

    override fun ifHaveAllPermissions(
        ifTrue: () -> Unit,
        ifFalse: () -> Unit,
        vararg permissions: String
    ) {
        baseActivity()?.haveAllPermissions(*permissions).ternaryMap(ifTrue, ifFalse).invoke()
    }

    override fun ifHaveAnyPermissions(
        ifTrue: () -> Unit,
        ifFalse: () -> Unit,
        vararg permissions: String
    ) {
        baseActivity()?.haveAnyPermissions(*permissions).ternaryMap(ifTrue, ifFalse).invoke()
    }

    override fun startScreenForResult(
        screen: MyScreen,
        requestCode: Int,
        onSuccess: (data: Intent?) -> Unit
    ) {
        CiceroneActivityManager.getAppropriateIntent(screen, context ?: return)?.let { intent ->
            startIntentForResult(intent, requestCode, onSuccess)
        }
    }

    override fun startIntentForResult(
        intent: Intent,
        requestCode: Int,
        onSuccess: (data: Intent?) -> Unit
    ) {
        startIntentForResult(intent, requestCode, onSuccess, null)
    }

    override fun startIntentForResult(
        intent: Intent,
        requestCode: Int,
        onSuccess: (data: Intent?) -> Unit,
        onFail: ((data: Intent?) -> Unit)?
    ) {
        try {
            startActivityForResult(intent, requestCode) { success, data ->
                when (success) {
                    true -> onSuccess.invoke(data)
                    false -> onFail?.invoke(data)
                }
            }
        } catch (e: Exception) {
            Log.e("AA", "Error", e)
        }
    }
}