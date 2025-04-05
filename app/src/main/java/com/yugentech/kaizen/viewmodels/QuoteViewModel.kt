package com.yugentech.kaizen.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yugentech.kaizen.data.model.Quote
import com.yugentech.kaizen.repositories.quotesRepository.QuoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuoteViewModel(
    private val repository: QuoteRepository
) : ViewModel() {

    private val _quoteState = MutableStateFlow<QuoteState>(QuoteState.Loading)
    val quoteState: StateFlow<QuoteState> = _quoteState.asStateFlow()

    init {
        getQuote()
    }

    private fun getQuote() {
        viewModelScope.launch {
            _quoteState.value = QuoteState.Loading
            try {
                val quote = repository.getQuote()
                _quoteState.value = QuoteState.Success(quote)
            } catch (e: Exception) {
                _quoteState.value = QuoteState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}

sealed class QuoteState {
    data object Loading : QuoteState()
    data class Success(val quote: Quote) : QuoteState()
    data class Error(val message: String) : QuoteState()
}