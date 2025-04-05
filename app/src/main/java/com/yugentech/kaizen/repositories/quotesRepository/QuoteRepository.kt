package com.yugentech.kaizen.repositories.quotesRepository

import com.yugentech.kaizen.data.model.Quote

interface QuoteRepository {
    suspend fun getQuote(): Quote
}