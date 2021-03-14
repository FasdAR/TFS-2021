package ru.fasdev.tfs.view.feature.recycler.base

open class ViewTyped
{
    open val viewType: Int
        get() = error("Don't Provide ViewTyped: $this")

    open val uId: Int
        get() = error("Don't Provide uId: $this")
}