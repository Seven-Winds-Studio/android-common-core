package mobi.sevenwinds.common.core.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.afollestad.inlineactivityresult.startActivityForResult
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import mobi.sevenwinds.common.core.app.BaseApp
import mobi.sevenwinds.common.core.ui.cicerone.CiceroneActivityManager
import mobi.sevenwinds.common.core.ui.cicerone.MyNavigator
import mobi.sevenwinds.common.core.ui.cicerone.MyScreen
import mobi.sevenwinds.common.core.ui.moxy.BaseView
import mobi.sevenwinds.common.core.ui.moxy.MoxyActivity
import mobi.sevenwinds.common.core.utils.extensions.plusAssign
import mobi.sevenwinds.common.core.utils.extensions.ternaryMap

abstract class BaseActivity : MoxyActivity(), BaseView {
    protected var compositeDisposable = CompositeDisposable()
    private val bsPermission = PublishSubject.create<Int>()


    fun addSubscription(disposable: Disposable) {
        compositeDisposable += disposable
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (compositeDisposable.isDisposed) {
            compositeDisposable = CompositeDisposable()
        }

        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    open var navigator = MyNavigator(this, MyNavigator.NO_CONTAINER)

    /**
     * Requests given permission.
     * If the permission has been denied previously, a Dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private fun requestPermission(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            bsPermission.onNext(requestCode)
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun getBsPermission(): Flowable<Int> {
        return bsPermission.toFlowable(BackpressureStrategy.LATEST)
    }

    fun havePermissionWithRequest(permission: String, requestCode: Int): Boolean {
        return if (havePermission(permission)) {
            requestPermission(permission, requestCode)
            false
        } else {
            true
        }
    }

    fun havePermission(permission: String): Boolean =
        ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    fun haveAllPermissions(vararg permissions: String): Boolean {
        return permissions.all { havePermission(it) }
    }

    fun haveAnyPermissions(vararg permissions: String): Boolean {
        return permissions.any { havePermission(it) }
    }

    override fun ifHaveAllPermissions(
        ifTrue: () -> Unit,
        ifFalse: () -> Unit,
        vararg permissions: String
    ) {
        haveAllPermissions(*permissions).ternaryMap(ifTrue, ifFalse).invoke()
    }

    override fun ifHaveAnyPermissions(
        ifTrue: () -> Unit,
        ifFalse: () -> Unit,
        vararg permissions: String
    ) {
        haveAnyPermissions(*permissions).ternaryMap(ifTrue, ifFalse).invoke()
    }

    override fun requestPermissions(requestCode: Int, vararg permissions: String) =
        ActivityCompat.requestPermissions(this, permissions, requestCode)

    open fun app(): BaseApp {
        return application as BaseApp
    }

    open fun router() = app().getRouter()

    fun startActivityWithoutBackStack(intent: Intent) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun <T : BaseActivity> startActivity(clazz: Class<T>) {
        startActivity(Intent(this, clazz))
    }

    private var toast: Toast? = null
    fun showToast(text: String) {
        toast?.cancel()

        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        toast?.show()
    }

    fun finishWithOkResult(intent: Intent? = null) {
        setResult(Activity.RESULT_OK, intent ?: Intent())
        finish()
    }

    override fun startScreenForResult(
        screen: MyScreen,
        requestCode: Int,
        onSuccess: (data: Intent?) -> Unit
    ) {
        CiceroneActivityManager.getAppropriateIntent(screen, this)?.let { intent ->
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