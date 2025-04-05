package com.yugentech.kaizen.di

import com.yugentech.kaizen.console.FirestoreService
import com.yugentech.kaizen.repositories.habitsRepository.HabitsRepository
import com.yugentech.kaizen.repositories.habitsRepository.HabitsRepositoryImpl
import com.yugentech.kaizen.viewmodels.HabitsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val firestoreModule = module {

    single { FirestoreService() }

    single<HabitsRepository> { HabitsRepositoryImpl(get()) }

    viewModel<HabitsViewModel> { HabitsViewModel(get(), androidContext()) }
}