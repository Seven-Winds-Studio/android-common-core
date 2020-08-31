package mobi.sevenwinds.common.core.views.recyclerview.pagination

interface IPaginationAdapter {
    /** 'Static' or 'Fake' items, not counted in pagination counter */
    fun getStaticItemCount(): Int

    /** Items, counted in pagination counter */
    fun getPaginationItemCount(): Int

    val paginationController: PaginationController
}