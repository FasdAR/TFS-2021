package ru.fasdev.tfs.domain.user.interactor

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.fasdev.tfs.domain.user.model.User
import ru.fasdev.tfs.domain.user.model.UserStatus
import ru.fasdev.tfs.domain.user.repo.UserRepo
import java.util.Locale

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
                val resQuery = query.toLowerCase(Locale.ROOT).trim()
                return@filter it.fullName.toLowerCase(Locale.ROOT).contains(resQuery) ||
                    it.email.toLowerCase(Locale.ROOT).contains(resQuery)
            }
            .toList()
            .subscribeOn(Schedulers.io())
    }

    fun getOwnUser(): Single<User> {
        return usersRepo.getOwnUser()
    }
}
