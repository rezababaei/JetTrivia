package com.rezababaei.jettrivia.component

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rezababaei.jettrivia.model.QuestionsItem
import com.rezababaei.jettrivia.screens.QuestionViewModel
import com.rezababaei.jettrivia.util.AppColors
import java.lang.Exception

@Composable
fun Question(viewModel: QuestionViewModel) {
    var questions = viewModel.data.value.data?.toMutableList()
    val questionIndex = remember {
        mutableStateOf(0)
    }
    if (viewModel.data.value.loading == true) {
        CircularProgressIndicator()

        Log.d("loading", "Loading.....")

    } else {
        val currentQuestion = try {
            questions?.get(questionIndex.value)
        } catch (e: Exception) {
            null
        }

        if (currentQuestion != null) {
            QuestionDisplay(question = currentQuestion, questionIndex, viewModel) {
                questionIndex.value += 1
            }
        }
    }
}

//@Preview
@Composable
fun QuestionDisplay(
    question: QuestionsItem,
    questionIndex: MutableState<Int>,
    viewModel: QuestionViewModel,
    onNextClicked: (Int) -> Unit = {},
) {
    val choicesState = remember(question) {
        question.choices.toMutableList()
    }
    val answerState = remember(question) {
        mutableStateOf<Int?>(null)
    }
    val correctAnswerState = remember {
        mutableStateOf<Boolean?>(null)
    }

    val updateAnswer: (Int) -> Unit = remember(question) {
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == question.answer
        }
    }

    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

    Surface(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        color = AppColors.mDarkPurple) {
        Column(Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start) {
            
            if (questionIndex.value>3) ShowProgress(questionIndex.value)

            QuestionTracker(counter = questionIndex.value)
            DrawDottedLine(pathEffect)

            Column {
                Text(text = question.question,
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth()
                        .align(Alignment.Start)
                        .fillMaxHeight(0.3f), // 30% of Screen
                    fontSize = 17.sp,
                    color = AppColors.mOffWhite,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp)
                //choices
                choicesState.forEachIndexed { index, answerText ->
                    Row(modifier = Modifier
                        .padding(3.dp)
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 4.dp, brush = Brush.linearGradient(
                            colors = listOf(AppColors.mOffDarkPurple, AppColors.mOffDarkPurple)
                        ), shape = RoundedCornerShape(15.dp))
                        .clip(RoundedCornerShape(topStart = 50.dp,
                            topEnd = 50.dp,
                            bottomStart = 50.dp,
                            bottomEnd = 50.dp))
                        .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Row(modifier =
                        Modifier
                            .clickable {
                                updateAnswer(index)
                            }
                            .fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {


                            RadioButton(selected = (answerState.value == index),
                                onClick = {
                                    // updateAn swer(index)
                                }, modifier = Modifier.padding(start = 16.dp),
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = if (correctAnswerState.value == true
                                        && index == answerState.value
                                    ) {
                                        Color.Green.copy(0.2f)
                                    } else {
                                        Color.Red.copy(0.2f)
                                    }
                                ))//end RadioButton

                            val annotatedString = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = if (correctAnswerState.value == true
                                    && index == answerState.value
                                ) {
                                    Color.Green
                                } else if (correctAnswerState.value == false
                                    && index == answerState.value
                                ) {
                                    Color.Red
                                } else {
                                    AppColors.mOffWhite
                                }, fontSize = 17.sp)) {
                                    append(answerText)
                                }
                            }
                            Text(text = annotatedString, modifier = Modifier.padding(6.dp))
                        }
                    }
                }
                Button(onClick = {
                    onNextClicked(questionIndex.value)
                }, modifier = Modifier
                    .padding(3.dp)
                    .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(35.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = AppColors.mLightBlue
                    )) {
                    Text(text = "Next",
                        modifier = Modifier.padding(4.dp),
                        color = AppColors.mOffWhite,
                        fontSize = 18.sp)
                }
            }


        }
    }
}

@Composable
fun DrawDottedLine(pathEffect: PathEffect) {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)) {
        drawLine(color = AppColors.mLightGray,
            start = Offset(0f, 0f),
            end = Offset(size.width, y = 0f),
            pathEffect = pathEffect)

    }
}


//@Preview
@Composable
fun QuestionTracker(
    counter: Int = 10,
    outOf: Int = 100,
) {
    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
            withStyle(style = SpanStyle(color = AppColors.mLightGray,
                fontWeight = FontWeight.Bold,
                fontSize = 27.sp)) {
                append("Question $counter/")
            }
            withStyle(style = SpanStyle(color = AppColors.mLightGray,
                fontWeight = FontWeight.Light,
                fontSize = 14.sp)) {
                append("$outOf")
            }
        }
    }, modifier = Modifier.padding(20.dp))
}


@Preview
@Composable
fun ShowProgress(score: Int = 22) {
    val gradient = Brush.linearGradient(listOf(Color(0xFFF95075),
        Color(0XFFBE6BE5)))

    val progressFactor = remember(score) {
        mutableStateOf(score * 0.005f)
    }
    Row(Modifier
        .padding(3.dp)
        .fillMaxWidth()
        .height(45.dp)
        .border(width = 4.dp,
            brush = Brush.linearGradient(colors = listOf(AppColors.mLightPurple, AppColors.mBlue)),
            shape = RoundedCornerShape(50.dp))
        .clip(RoundedCornerShape(topStart = 50.dp,
            topEnd = 50.dp,
            bottomStart = 50.dp,
            bottomEnd = 50.dp))
        .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically) {
        Button(onClick = {}, contentPadding = PaddingValues(1.dp),
            modifier = Modifier
                .fillMaxWidth(progressFactor.value)
                .background(brush = gradient),
            enabled = false,
            elevation = null,
            colors = buttonColors(
                backgroundColor = Color.Transparent,
                disabledBackgroundColor = Color.Transparent
            )) {
Text(text = (score * 10).toString(), modifier =
Modifier.clip(shape = RoundedCornerShape(23.dp))
    .fillMaxWidth()
    .fillMaxHeight(0.87f)
    .padding(6.dp),
color = AppColors.mOffWhite,
textAlign = TextAlign.Center)
        }

    }
}


