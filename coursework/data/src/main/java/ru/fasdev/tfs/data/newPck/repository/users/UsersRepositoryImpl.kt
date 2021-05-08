package ru.fasdev.tfs.data.newPck.repository.users

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.fasdev.tfs.data.newPck.mapper.toUser
import ru.fasdev.tfs.data.newPck.mapper.toUserOnlineStatus
import ru.fasdev.tfs.data.newPck.source.network.users.api.UserApi
import ru.fasdev.tfs.data.newPck.source.network.users.model.BaseUser
import ru.fasdev.tfs.domain.newPck.user.model.User
import ru.fasdev.tfs.domain.newPck.user.model.UserOnlineStatus
import java.util.concurrent.TimeUnit

class UsersRepositoryImpl(private val userApi: UserApi) : UsersRepository
{
    private companion object {
        const val DELAY_QUERY = 10L
    }

    private fun getOnlineStatus(user: User): Single<User> {
        return getStatusUser(user.email)
            .map { user.copy(onlineStatus = it) }
    }

    override fun getOwnUser(): Single<User> {
        return userApi.getOwnUser()
            .map {
                (it as BaseUser).toUser()
                    .copy(isBot = false)
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
                    .concatMap {
                        Observable.just(it).delay(DELAY_QUERY, TimeUnit.MILLISECONDS)
                    }
                    .flatMapSingle { user ->
                        getOnlineStatus(user).subscribeOn(Schedulers.io())
                    }
                    .toSortedList { item1, item2 -> item1.fullName.compareTo(item2.fullName)  }
            }
    }
}