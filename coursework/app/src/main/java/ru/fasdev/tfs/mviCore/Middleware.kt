package ru.fasdev.tfs.mviCore

import io.reactivex.Observable

typealias Middleware<A, S> = (actions: Observable<A>, state: Observable<S>) -> Observable<A>