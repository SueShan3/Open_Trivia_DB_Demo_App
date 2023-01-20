package com.example.opentriviadbdemoapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import com.example.opentriviadbdemoapp.R
import com.example.opentriviadbdemoapp.databinding.ActivityStartQuizBinding
import com.example.opentriviadbdemoapp.databinding.ViewChoicesBinding
import com.example.opentriviadbdemoapp.model.Question
import com.example.opentriviadbdemoapp.utils.Constants

private const val LIST = "list"
private const val SCORE = "score"
private const val TIME_LEFT = "timeLeft"
private const val TIMER_IN_SECOND = 60L

class StartQuizActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context, quizList: ArrayList<Question>): Intent {
            val intent = Intent(context, StartQuizActivity::class.java).apply {
                putParcelableArrayListExtra(LIST, quizList)
            }
            return (intent)
        }
    }

    private lateinit var dataBinding: ActivityStartQuizBinding

    private var mQuestion = listOf<Question>()

    private var totalScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_start_quiz)

        val intent: Intent = intent

        intent.getParcelableArrayListExtra<Question>(LIST).let {
            mQuestion = it!!
        }

        populateNextQuestion(0)
        quizTimer.start()

        dataBinding.closeBtn.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

    private var currentQuestionNo: Int = 0

    private fun populateNextQuestion(questionNo: Int) {
        val quizNo = questionNo + 1
        currentQuestionNo = questionNo
        val question = mQuestion[currentQuestionNo]

        dataBinding.quizNo.text = "${quizNo}/${mQuestion.count()}"
        dataBinding.question.text =
            HtmlCompat.fromHtml(question.question, HtmlCompat.FROM_HTML_MODE_LEGACY)
        dataBinding.difficulty.text = question.difficulty.capitalize()
        when (question.difficulty) {
            Constants.QuestionDifficulty.EASY -> {
                dataBinding.difficulty.background.setTint(
                    ContextCompat.getColor(
                        this,
                        R.color.green
                    )
                )
            }
            Constants.QuestionDifficulty.MEDIUM -> {
                dataBinding.difficulty.background.setTint(
                    ContextCompat.getColor(
                        this,
                        R.color.golden_yellow
                    )
                )
            }
            Constants.QuestionDifficulty.HARD -> {
                dataBinding.difficulty.background.setTint(ContextCompat.getColor(this, R.color.red))
            }
        }

        question.getAllAvailableAnswers().shuffled().forEach { answer ->
            val viewBinding = ViewChoicesBinding.inflate(
                LayoutInflater.from(dataBinding.choicesLayout.context),
                dataBinding.choicesLayout,
                true
            )

            viewBinding.choices.text = HtmlCompat.fromHtml(answer, HtmlCompat.FROM_HTML_MODE_LEGACY)
            viewBinding.cvSelectedAnswer.setOnClickListener {
                mQuestion[questionNo].selectedAnswer = answer

                totalScore = mQuestion.filter {
                    it.isAnswerCorrect(it.selectedAnswer)
                }.count()

                if (quizNo == mQuestion.size) {
                    intent.putExtra(SCORE, totalScore)
                    intent.putExtra(TIME_LEFT, timeLeft)
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    dataBinding.choicesLayout.removeAllViews()
                    populateNextQuestion(++currentQuestionNo)
                }
            }
        }
    }

    private var timeLeft: String? = null

    private var quizTimer = object : CountDownTimer(TIMER_IN_SECOND * 1000, 100) {
        override fun onTick(millisUntilFiished: Long) {
            val seconds = millisUntilFiished / 1000
            timeLeft = String.format("%02d:%02d", seconds % 3600 / 60, seconds % 60)
            dataBinding.timer.text = timeLeft
        }

        override fun onFinish() {
            finish()
        }
    }

    override fun onDestroy() {
        quizTimer.cancel()
        super.onDestroy()
    }
}