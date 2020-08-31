package mobi.sevenwinds.common.core.views.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import mobi.sevenwinds.common.core.ui.BaseFragment

open class SimplePageAdapter(
    fm: FragmentManager,
    vararg val fragments: SimplePageWrapper
) : FragmentPagerAdapter(fm) {

    init {
        if (fm.fragments.isNotEmpty()) {
            val transaction = fm.beginTransaction()

            fm.fragments
                .filter { it.tag?.startsWith("android:switcher:") ?: false }
                .forEach { transaction.remove(it) }

            transaction.commitNowAllowingStateLoss()
        }
    }

    override fun getItem(position: Int): Fragment = fragments[position].fragment

    override fun getCount() = fragments.size

    override fun getPageTitle(position: Int) = fragments[position].title
}

data class SimplePageWrapper(val fragment: BaseFragment, val title: String)