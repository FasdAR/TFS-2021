package ru.fasdev.tfs.di.module

import ru.fasdev.tfs.data.old.repo.UserRepoImpl
import ru.fasdev.tfs.data.old.source.network.users.api.UserApi
import ru.fasdev.tfs.domain.old.user.interactor.UserInteractor
import ru.fasdev.tfs.domain.old.user.interactor.UserInteractorImpl
import ru.fasdev.tfs.domain.old.user.repo.UserRepo

class UserDomainModule {
    companion object {
        fun getUserRepo(userApi: UserApi): UserRepo = UserRepoImpl(userApi)
        fun getUserInteractor(userRepo: UserRepo): UserInteractor = UserInteractorImpl(userRepo)
    }
}
