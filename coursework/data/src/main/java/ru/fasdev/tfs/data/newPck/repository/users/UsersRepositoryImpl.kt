package ru.fasdev.tfs.data.newPck.repository.users

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.fasdev.tfs.data.newPck.mapper.toUser
import ru.fasdev.tfs.data.newPck.mapper.toUserOnlineStatus
import ru.fasdev.tfs.data.newPck.source.network.users.api.UsersApi
import ru.fasdev.tfs.data.newPck.source.network.users.model.BaseUser
import ru.fasdev.tfs.domain.newPck.user.model.User
import ru.fasdev.tfs.domain.newPck.user.model.UserOnlineStatus
import java.lang.ref.SoftReference
import java.util.*
import java.util.concurrent.TimeUnit

class UsersRepositoryImpl(private val userApi: UsersApi) : UsersRepository {
    private companion object {
        const val DELAY_QUERY = 10L
    }

    //Временный кэш списка всех пользователей
    private var temporaryCacheAllUsers: SoftReference<List<User>> = SoftReference(listOf())

    private fun getOnlineStatus(user: User): Single<User> {
        return getStatusUser(user.email)
            .map { user.copy(onlineStatus = it) }
    }

    override fun getOwnUser(): Single<User> {
        return userApi.getOwnUser()
            .map {
                (it as BaseUser).toUser().copy(isBot = false)
            }
            .flatMap(::getOnlineStatus)
    }

    override fun getStatusUser(email: String): Single<UserOnlineStatus> {
        return userApi.getUserPresence(email)
            .map { it.presence.toUserOnlineStatus() }
    }

    override fun getAllUsers(): Single<List<User>> {
        return userApi.getAllUsers()
            .flatMap {
                Observable.fromIterable(it.members)
                    .filter { !it.isBot }
                    .map { user -> user.toUser().copy(isBot = user.isBot) }
                    .concatMap { user ->
                        Observable.just(user).delay(DELAY_QUERY, TimeUnit.MILLISECONDS)
                    }
                    .flatMapSingle { user ->
                        getOnlineStatus(user).subscribeOn(Schedulers.io())
                    }
                    .toSortedList { item1, item2 -> item1.fullName.compareTo(item2.fullName) }
            }
            .doOnSuccess {
                temporaryCacheAllUsers = SoftReference(it)
            }
    }

    override fun searchUsers(query: String): Single<List<User>> {
        return Single.just(temporaryCacheAllUsers.get())
            .flatMapObservable { Observable.fromIterable(it) }
            .filter {
                val readyQuery = query.trim().toLowerCase(Locale.ROOT)
                val userFullName = it.fullName.toLowerCase(Locale.ROOT)
                val userEmail = it.email.toLowerCase(Locale.ROOT)

                return@filter userFullName.contains(readyQuery) || userEmail.contains(readyQuery)
            }
            .toList()
    }

    override fun getUserById(id: Long): Single<User> {
        return userApi.getUserById(id)
            .map {
                it.user.toUser().copy(isBot = false)
            }
            .flatMap(::getOnlineStatus)
    }
}