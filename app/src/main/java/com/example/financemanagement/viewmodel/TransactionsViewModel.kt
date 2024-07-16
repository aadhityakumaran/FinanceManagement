package com.example.financemanagement.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financemanagement.model.Transactions
import com.example.financemanagement.repository.TransactionRepository
import kotlinx.coroutines.launch
import java.time.Instant.ofEpochMilli
import java.time.LocalDate
import java.time.ZoneId

class TransactionsViewModel : ViewModel() {

    var transactionMap by mutableStateOf(mapOf<String, Transactions>())
        private set

    init {
        fetchTransactions()
    }


    private fun fetchTransactions() {
        viewModelScope.launch {
            TransactionRepository.getTransactions(
                onChange = { transactions ->
                    // Filter transactions by current month
                    val currentMonthTransactions = transactions.filter {
                        val localDate = ofEpochMilli(it.value.date)
                            .atZone(ZoneId.systemDefault()).toLocalDate()
                        localDate.year == LocalDate.now().year && localDate.month == LocalDate.now().month
                    }
                    transactionMap = currentMonthTransactions.toSortedMap { o1, o2 ->
                        transactions[o2]!!.date.compareTo(transactions[o1]!!.date)
                    }
                },
                onFailure = {}
            )
        }
    }
}