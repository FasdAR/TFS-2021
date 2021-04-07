package ru.fasdev.tfs.domain.user.interactor

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.fasdev.tfs.domain.user.model.User
import ru.fasdev.tfs.domain.user.model.UserStatus
import ru.fasdev.tfs.domain.user.repo.UserRepo

class UserInteractorImpl(private val usersRepo: UserRepo) : UserInteractor {
    override fun getAllUsers(): Single<List<User>> {
        return usersRepo.getAllUsers()
    }

    override fun getUserById(id: Long): Single<User> {
        return usersRepo.getUserById(id)
    }

    override fun getStatusUser(email: String): Single<UserStatus> {
        return usersRepo.getStatusUser(email)
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
