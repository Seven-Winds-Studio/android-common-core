package mobi.sevenwinds.common.core.views.viewpager

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import mobi.sevenwinds.common.core.ui.BaseFragment

abstract class AutoheightFragmentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private var mCurrentPosition = -1

    override fun setPrimaryItem(container: View, position: Int, o: Any) {
        super.setPrimaryItem(container, position, o)

        checkAboutDynamicHeight(container, position, o)
    }

    private fun checkAboutDynamicHeight(container: Any, position: Int, o: Any) {
        if (position != mCurrentPosition && container is DynamicHeightViewPager) {
            (o as? BaseFragment)?.let {
                mCurrentPosition = position
                container.measureCurrentView(it.view)
            }
        }
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, o: Any) {
        super.setPrimaryItem(container, position, o)
        checkAboutDynamicHeight(container, position, o)
    }
}