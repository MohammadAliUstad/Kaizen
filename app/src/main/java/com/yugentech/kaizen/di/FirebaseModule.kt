package com.yugentech.kaizen.di

import com.yugentech.kaizen.console.FirebaseService
import com.yugentech.kaizen.repositories.firebaseRepository.FirebaseRepository
import com.yugentech.kaizen.repositories.firebaseRepository.FirebaseRepositoryImpl
import com.yugentech.kaizen.viewmodels.FirebaseViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val firebaseModule = module {

    single { FirebaseService(androidContext()) }

    single<FirebaseRepository> { FirebaseRepositoryImpl(get()) }

    viewModel { FirebaseViewModel(get()) }
}