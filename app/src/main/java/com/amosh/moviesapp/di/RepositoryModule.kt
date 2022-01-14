package com.amosh.moviesapp.di

import com.amosh.data.repository.LocalDataSource
import com.amosh.data.repository.RemoteDataSource
import com.amosh.data.repository.RepositoryImp
import com.amosh.domain.repository.Repository
import com.amosh.local.source.LocalDataSourceImp
import com.amosh.remote.source.RemoteDataSourceImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Module that holds Repository classes
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideLocalDataSource(localDataSourceImpl: LocalDataSourceImp): LocalDataSource

    @Binds
    abstract fun provideRemoteDataSource(remoteDataSourceImp: RemoteDataSourceImp): RemoteDataSource

    @Binds
    @ViewModelScoped
    abstract fun provideRepository(repository : RepositoryImp) : Repository

}