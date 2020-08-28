package mobi.sevenwinds.common.core.ui.moxy

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpDelegate

abstract class MoxyFragment : Fragment() {

    private val mMvpDelegate: MvpDelegate<out MoxyFragment> by lazy {
        MvpDelegate<MoxyFragment>(this)
    }
    
    private var mIsStateSaved: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mMvpDelegate.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        mIsStateSaved = false

        mMvpDelegate.onAttach()
    }

    override fun onResume() {
        super.onResume()

        mIsStateSaved = false

        mMvpDelegate.onAttach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        mIsStateSaved = true

        mMvpDelegate.onSaveInstanceState(outState)
        mMvpDelegate.onDetach()
    }

    override fun onStop() {
        super.onStop()

        mMvpDelegate.onDetach()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mMvpDelegate.onDetach()
        mMvpDelegate.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()

        //We leave the screen and respectively all fragments will be destroyed
        if (activity?.isFinishing == true) {
            mMvpDelegate.onDestroy()
            return
        }

        // When we rotate device isRemoving() return true for fragment placed in backstack
        // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
        if (mIsStateSaved) {
            mIsStateSaved = false
            return
        }

        // See https://github.com/Arello-Mobile/Moxy/issues/24
        var anyParentIsRemoving = false
        var parent = parentFragment
        while (!anyParentIsRemoving && parent != null) {
            anyParentIsRemoving = parent.isRemoving
            parent = parent.parentFragment
        }

        if (isRemoving || anyParentIsRemoving) {
            mMvpDelegate.onDestroy()
        }
    }
}