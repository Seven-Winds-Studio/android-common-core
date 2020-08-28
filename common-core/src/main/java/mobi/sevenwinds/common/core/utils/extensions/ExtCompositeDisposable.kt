package mobi.sevenwinds.common.core.utils.extensions

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

operator fun CompositeDisposable.plusAssign(disposable: Disposable?) {
    when {
        disposable == null -> return
        this.isDisposed -> return
        else -> this.add(disposable)
    }
}