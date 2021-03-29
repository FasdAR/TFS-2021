package ru.fasdev.tfs.domain

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.domain.TestEnv.Companion.ERR_RANDOM_TITLE
import ru.fasdev.tfs.domain.TestEnv.Companion.TIME_EMULATE_DELAY
import java.util.concurrent.TimeUnit

class TestEnv {
    companion object {
        const val TIME_EMULATE_DELAY = 1000L
        const val ERR_RANDOM_TITLE = "RANDOM_ERROR"
    }
}

fun Completable.testEnv(errorTitle: String = ERR_RANDOM_TITLE): Completable = delay(TIME_EMULATE_DELAY, TimeUnit.MILLISECONDS)
fun <T> Observable<T>.testEnv(errorTitle: String = ERR_RANDOM_TITLE): Observable<T> = delay(TIME_EMULATE_DELAY, TimeUnit.MILLISECONDS)
fun <T> Single<T>.testEnv(errorTitle: String = ERR_RANDOM_TITLE): Single<T> = delay(TIME_EMULATE_DELAY, TimeUnit.MILLISECONDS)