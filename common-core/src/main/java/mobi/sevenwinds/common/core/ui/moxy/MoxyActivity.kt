package mobi.sevenwinds.common.core.ui.moxy

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.arellomobile.mvp.MvpDelegate

abstract class MoxyActivity : AppCompatActivity() {
    private val mMvpDelegate: MvpDelegate<out MoxyActivity> by lazy {
        MvpDelegate<MoxyActivity>(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mMvpDelegate.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        mMvpDelegate.onAttach()
    }

    override fun onResume() {
        super.onResume()

        mMvpDelegate.onAttach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        mMvpDelegate.onSaveInstanceState(outState)
        mMvpDelegate.onDetach()
    }

    override fun onStop() {
        super.onStop()

        mMvpDelegate.onDetach()
    }

    override fun onDestroy() {
        try {
            super.onDestroy()
        } catch (e: Exception) {
            Log.e("MoxyActivity", "error on destroy", e)
        }

        mMvpDelegate.onDestroyView()

        if (isFinishing) {
            mMvpDelegate.onDestroy()
        }
    }
}