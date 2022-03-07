package com.rezababaei.jettrivia.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezababaei.jettrivia.data.DataOrException
import com.rezababaei.jettrivia.model.QuestionsItem
import com.rezababaei.jettrivia.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val repository: QuestionRepository):ViewModel() {

    val data: MutableState<DataOrException<ArrayList<QuestionsItem>,
            Boolean, Exception>> = mutableStateOf(
        DataOrException(null, true, Exception("")))

    init {
        getAllQuestions()
    }

    private fun getAllQuestions(){
        viewModelScope.launch {
            data.value.loading=true
            data.value=repository.getAllQuestions()
            if (data.value.data.toString().isNotEmpty()){
                data.value.loading=false
            }
        }
    }
}