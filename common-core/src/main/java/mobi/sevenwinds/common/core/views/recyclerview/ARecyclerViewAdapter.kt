package mobi.sevenwinds.common.core.views.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import mobi.sevenwinds.common.core.views.recyclerview.dataholder.IDataHolder
import mobi.sevenwinds.common.core.views.recyclerview.dataholder.ImmutableDataHolder

typealias ARecyclerViewListener<T> = (T) -> Unit

abstract class ARecyclerViewAdapter<T, T_HOLDER : ARecyclerViewHolder<T>>(
    val data: List<T>,
    context: Context,
    val listener: ARecyclerViewListener<T>?
) :
    RecyclerView.Adapter<T_HOLDER>() {

    val dataHolder: IDataHolder<T> by lazy { createDataHolder() }

    protected val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T_HOLDER {
        return createHolder(createView(parent))
    }

    open fun createView(parent: ViewGroup): View = createView(parent, getItemLayoutID())

    open fun createView(parent: ViewGroup, layoutID: Int): View = layoutInflater.inflate(layoutID, parent, false)

    abstract fun createHolder(view: View): T_HOLDER

    @LayoutRes
    abstract fun getItemLayoutID(): Int

    override fun getItemCount(): Int {
        return dataHolder.getItemCount()
    }

    override fun onBindViewHolder(holder: T_HOLDER, position: Int) {
        val item = dataHolder.getItemAt(position) ?: return

        holder.itemView.setOnClickListener { listener?.invoke(item) }
        holder.bind(item)
    }

    protected open fun createDataHolder(): IDataHolder<T> = ImmutableDataHolder(data)
}