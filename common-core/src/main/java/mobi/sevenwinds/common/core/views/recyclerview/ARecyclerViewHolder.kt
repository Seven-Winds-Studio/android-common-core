package mobi.sevenwinds.common.core.views.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ARecyclerViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T)
}