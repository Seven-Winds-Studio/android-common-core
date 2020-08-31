package mobi.sevenwinds.common.core.views.recyclerview.pagination

interface PaginatedResponse<DATA> {
    val total: Int

    fun getData(): List<DATA>
}