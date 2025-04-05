 package com.yugentech.kaizen.di

import com.yugentech.kaizen.data.KtorClient
import com.yugentech.kaizen.repositories.quotesRepository.QuoteRepository
import com.yugentech.kaizen.repositories.quotesRepository.QuoteRepositoryImpl
import com.yugentech.kaizen.viewmodels.QuoteViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val quoteModule = module {

    single { KtorClient() }

    single<QuoteRepository> { QuoteRepositoryImpl(get()) }

    viewModel { QuoteViewModel(get()) }
}