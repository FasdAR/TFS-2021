package ru.fasdev.tfs.di.module

import ru.fasdev.tfs.data.repo.StreamRepoImpl
import ru.fasdev.tfs.data.repo.UserRepoImpl
import ru.fasdev.tfs.data.source.network.stream.api.StreamApi
import ru.fasdev.tfs.data.source.network.users.api.UserApi
import ru.fasdev.tfs.domain.stream.interactor.StreamInteractor
import ru.fasdev.tfs.domain.stream.interactor.StreamInteractorImpl
import ru.fasdev.tfs.domain.stream.repo.StreamRepo
import ru.fasdev.tfs.domain.user.interactor.UserInteractor
import ru.fasdev.tfs.domain.user.interactor.UserInteractorImpl
import ru.fasdev.tfs.domain.user.repo.UserRepo

class StreamDomainModule
{
    companion object {
        fun getStreamRepo(streamApi: StreamApi): StreamRepo = StreamRepoImpl(streamApi)
        fun getStreamInteractor(streamRepo: StreamRepo): StreamInteractor = StreamInteractorImpl(streamRepo)
    }
}