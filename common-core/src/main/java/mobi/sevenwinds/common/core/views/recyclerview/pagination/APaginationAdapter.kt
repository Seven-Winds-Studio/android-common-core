package mobi.sevenwinds.common.core.views.recyclerview.pagination

import android.content.Context
import mobi.sevenwinds.common.core.views.recyclerview.ARecyclerViewAdapter
import mobi.sevenwinds.common.core.views.recyclerview.ARecyclerViewHolder
import mobi.sevenwinds.common.core.views.recyclerview.ARecyclerViewListener
import mobi.sevenwinds.common.core.views.recyclerview.dataholder.MutableDataHolder

abstract class APaginationAdapter<T, T_HOLDER : ARecyclerViewHolder<T>, DATA, PAGINATED_RESPONSE : PaginatedResponse<DATA>>(
    context: Context,
    listener: ARecyclerViewListener<T>?,
    private var source: IDataSource<DATA, PAGINATED_RESPONSE>,
    private val converter: (element: DATA, holder: MutableDataHolder<T>) -> List<T>,
    private val loadingStateListener: (isLoading: Boolean) -> Unit
) : ARecyclerViewAdapter<T, T_HOLDER>(emptyList(), context, listener), IPaginationAdapter {

    override val paginationController: PaginationController = PaginationController {
        if (it > 0) loadData(it) // sort of fix - preventing double loading of start data
    }

    init {
        loadData(0)
    }

    private fun loadData(offset: Int) {
        source.requestData(offset, { paginatedResponse ->
            paginationController.totalCount = paginatedResponse.total

            paginatedResponse.getData()
                .asSequence()
                .forEach {
                    mutableDataHolder().data.addAll(converter.invoke(it, mutableDataHolder()))
                }

            notifyDataSetChanged()
        }, {
            paginationController.isLoading = true
            loadingStateListener.invoke(true)
        }, {
            paginationController.isLoading = false
            loadingStateListener.invoke(false)
        })
    }

    override fun createDataHolder() = MutableDataHolder<T>()

    fun mutableDataHolder() = dataHolder as MutableDataHolder<T>
}


