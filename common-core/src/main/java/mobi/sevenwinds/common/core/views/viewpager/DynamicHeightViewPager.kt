package mobi.sevenwinds.common.core.views.viewpager

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager

/**
 * Provide dynamic height for each nested fragment
 */
class DynamicHeightViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    private var mCurrentView: View? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mCurrentView?.let {
            it.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))

            // respecting minimum height of root view from xml layout
            val height = it.measuredHeight
                .coerceAtLeast(0)
                .coerceAtLeast(minimumHeight)

            val newHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)

            super.onMeasure(widthMeasureSpec, newHeightSpec)
        }
    }

    fun measureCurrentView(currentView: View?) {
        mCurrentView = currentView
        requestLayout()
    }
}