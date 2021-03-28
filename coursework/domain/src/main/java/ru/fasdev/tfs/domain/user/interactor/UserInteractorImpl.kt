package ru.fasdev.tfs.domain.user.interactor

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.fasdev.tfs.domain.TestError
import ru.fasdev.tfs.domain.model.User
import ru.fasdev.tfs.domain.model.UserStatus
import ru.fasdev.tfs.domain.user.repo.UserRepo

class UserInteractorImpl(private val usersRepo: UserRepo) : UserInteractor {
    override fun getAllUsers(): Single<List<User>> {
        return Single.just(usersRepo.getAllUsers())
            .doOnSuccess { TestError.testError("Get all users") }
            .subscribeOn(Schedulers.io())
    }

    override fun getUserById(id: Int): Single<User> {
        return Single.just(usersRepo.getUserById(id)!!)
            .doOnSuccess { TestError.testError("Get all user by id") }
            .subscribeOn(Schedulers.io())
    }

    override fun getStatusUser(id: Int): Single<UserStatus> {
        return Single.just(usersRepo.getStatusUser(id))
            .doOnSuccess { TestError.testError("Get status user") }
            .subscribeOn(Schedulers.io())
    }

    override fun getIsOnlineStatusUser(id: Int): Single<Boolean> {
        return Single.just(usersRepo.isOnlineUser(id))
            .doOnSuccess { TestError.testError("Get online status user") }
            .subscribeOn(Schedulers.io())
    }

    override fun searchUser(query: String): Single<List<User>> {
        return getAllUsers()
            .flatMapObservable { Observable.fromIterable(it) }
            .filter {
                val resQuery = query.toLowerCase().trim()
                return@filter it.fullName.toLowerCase().contains(resQuery) || it.email.toLowerCase().contains(resQuery)
            }
            .toList()
            .subscribeOn(Schedulers.io())
    }
}
