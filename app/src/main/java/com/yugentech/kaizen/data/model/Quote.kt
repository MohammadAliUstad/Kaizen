package com.yugentech.kaizen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class QuoteResponse(
    val qotd_date: String,
    val quote: Quote
)

@Serializable
data class Quote(
    val author: String,
    val body: String
)