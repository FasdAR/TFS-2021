package ru.fasdev.tfs.view

interface MviView<T> {
    fun render(state: T)
}