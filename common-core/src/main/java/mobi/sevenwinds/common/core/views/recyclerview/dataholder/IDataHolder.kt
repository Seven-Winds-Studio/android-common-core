package mobi.sevenwinds.common.core.views.recyclerview.dataholder

interface IDataHolder<T> {
    fun getItemCount(): Int
    fun getItemAt(position: Int): T?
}

