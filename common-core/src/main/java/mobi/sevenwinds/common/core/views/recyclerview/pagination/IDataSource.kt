package mobi.sevenwinds.common.core.views.recyclerview.pagination

interface IDataSource<T, PAGINATED_RESPONSE : PaginatedResponse<T>> {
    fun requestData(
        offset: Int,
        result: (PAGINATED_RESPONSE) -> Unit,
        onStart: () -> Unit,
        onFinish: () -> Unit
    )
}