package ru.fasdev.tfs.mviCore

typealias Reducer<S, A> = (state: S, action: A) -> S
