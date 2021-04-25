package ru.fasdev.tfs.view.ui.global.recycler.base

open class ViewType {
    open val viewType: Int
        get() = error("Don't Provide ViewTyped: $this")

    open val uId: Int
        get() = error("Don't Provide uId: $this")

    override fun equals(other: Any?): Boolean {
        val type = (other as ViewType)
        return uId == type.uId && viewType == type.viewType
    }

    override fun hashCode(): Int {
        return viewType + uId
    }
}
