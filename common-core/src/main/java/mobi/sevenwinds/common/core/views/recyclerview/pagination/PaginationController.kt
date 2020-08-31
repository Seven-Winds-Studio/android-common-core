package mobi.sevenwinds.common.core.views.recyclerview.pagination

class PaginationController(
    private val onNearEndCheck: (lastElementIndex: Int) -> Unit
) {

    var isLoading: Boolean = false
    var totalCount: Int = 0

    var getVisibleItemCount: (() -> Int)? = null
    var getTotalItemCount: (() -> Int)? = null
    var getFirstVisibleItemPosition: (() -> Int)? = null

    fun checkAboutPagination() {
        if (isLoading) return

        val totalItemCount = getTotalItemCount?.invoke() ?: return

        if (totalItemCount == totalCount) return

        val visibleItemCount = getVisibleItemCount?.invoke() ?: return
        val firstVisibleItemPosition = getFirstVisibleItemPosition?.invoke() ?: return

        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
            onNearEndCheck.invoke(totalItemCount)
        }
    }
}