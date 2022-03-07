package com.rezababaei.jettrivia.screens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.rezababaei.jettrivia.component.Question

@Composable
fun TriviaHome(viewModel: QuestionViewModel = hiltViewModel()) {
    Question(viewModel)
}
