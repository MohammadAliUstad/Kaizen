package com.yugentech.kaizen.repositories.quotesRepository

import com.yugentech.kaizen.data.KtorClient
import com.yugentech.kaizen.data.model.Quote

class QuoteRepositoryImpl(
    private val ktorClient: KtorClient
) : QuoteRepository {

    override suspend fun getQuote(): Quote {
        return ktorClient.getQuote().quote
    }
}