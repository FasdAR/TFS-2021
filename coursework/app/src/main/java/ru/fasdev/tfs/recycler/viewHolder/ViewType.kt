package ru.fasdev.tfs.recycler.viewHolder

open class ViewType {
    open val viewType: Int
        get() = error("Don't Provide ViewTyped: $this")

    open val uId: Int
        get() = error("Don't Provide uId: $this")

    override fun equals(other: Any?): Boolean {
        val otherViewType = (other as ViewType)
        return uId == otherViewType.uId && viewType == otherViewType.viewType
    }
}
