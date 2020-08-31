package mobi.sevenwinds.common.core.views.recyclerview.dataholder

open class ImmutableDataHolder<T>(val data: List<T>) : IDataHolder<T> {
    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemAt(position: Int): T? {
        return data.getOrNull(position)
    }
}