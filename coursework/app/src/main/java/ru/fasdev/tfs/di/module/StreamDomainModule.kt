package ru.fasdev.tfs.di.module

import ru.fasdev.tfs.data.old.repo.StreamRepoImpl
import ru.fasdev.tfs.data.old.source.db.dao.StreamDao
import ru.fasdev.tfs.data.old.source.db.dao.TopicDao
import ru.fasdev.tfs.data.old.source.network.stream.api.StreamApi
import ru.fasdev.tfs.domain.old.stream.interactor.StreamInteractor
import ru.fasdev.tfs.domain.old.stream.interactor.StreamInteractorImpl
import ru.fasdev.tfs.domain.old.stream.repo.StreamRepo

class StreamDomainModule {
    companion object {
        fun getStreamRepo(
            streamApi: StreamApi,
            streamDao: StreamDao,
            topicDao: TopicDao
        ): StreamRepo = StreamRepoImpl(streamApi, streamDao, topicDao)

        fun getStreamInteractor(streamRepo: StreamRepo): StreamInteractor =
            StreamInteractorImpl(streamRepo)
    }
}
