package mobi.sevenwinds.common.core.views.recyclerview.dataholder

open class MutableDataHolder<T> : IDataHolder<T> {
    val data: MutableList<T> = mutableListOf()

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemAt(position: Int): T? {
        return data.getOrNull(position)
    }

    fun getLast(): T? {
        return data.lastOrNull()
    }
}