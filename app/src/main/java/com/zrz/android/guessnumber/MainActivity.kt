package com.zrz.android.guessnumber

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var array: Array<Int>
    private var currentRandomIndex: Int = 0
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fillGridLayout()

        textViewResult.isSelected = true

        buttonStart.setOnClickListener {
            buttonStart.isClickable = false
            buttonLess.isClickable = true
            buttonOk.isClickable = true
            buttonMore.isClickable = true
            count++
            makeMove()
        }

        buttonLess.setOnClickListener {
            count++
            changeRange(false)
            makeMove()
        }

        buttonOk.setOnClickListener {
            prepareForNewGame()
        }

        buttonMore.setOnClickListener {
            count++
            changeRange(true)
            makeMove()
        }

        prepareForNewGame()
    }

    private fun prepareForNewGame() {
        buttonLess.isClickable = false
        buttonOk.isClickable = false
        buttonMore.isClickable = false
        showText(getString(R.string.main_start_text))

        array = Array(100){ i -> i + 1 }

        colorGridLayout()
        currentRandomIndex = 0
        count = 0
        showCount(count)
        buttonStart.isClickable = true
    }

    private fun makeMove() {
        colorGridLayout()

        val rangeSize = getRangeSize()

        if (rangeSize == 0) {
            Toast.makeText(this, R.string.main_all_numbers_used, Toast.LENGTH_SHORT).show()
            prepareForNewGame()
        } else {
            val randomNumber = getRandomNumber(rangeSize)

            currentRandomIndex = setCurrentRandomIndex(randomNumber)

            colorCurrentRandomIndex()
            showText("${numberForShowing(currentRandomIndex)}" )
            showCount(count)
        }
    }

    private fun changeRange(searchRangeToUp: Boolean) {

        if (searchRangeToUp) {
            for ((index) in array.withIndex()) {
                if (index<=currentRandomIndex) {
                    array[index] = 0
                }
            }
        } else {
            for ((index) in array.withIndex()) {
                if (index >= currentRandomIndex) {
                    array[index] = 0
                }
            }
        }
    }

    private fun setCurrentRandomIndex(randomNumber: Int) : Int {
        var index = getFirstIndexWithoutZero()

        for (i in randomNumber downTo 1) {
            if (array[index] != 0) {
                index++}
        }

        return index
    }

    private fun getFirstIndexWithoutZero() : Int {
        for ((index, element) in array.withIndex()) {
            if (element != 0) {
                return index
            }
        }

        return 0
    }

    private fun getRandomNumber(range : Int) = Random.nextInt(range)

    private fun getRangeSize(): Int {
        var rangeSize = 0
        for (i in array) {
            if (i != 0) {
                rangeSize += 1
            }
        }

        return rangeSize
    }

    private fun showText(text: String) {
        textViewResult.text = text
    }

    private fun showCount(count: Int) {
        val fullText = "${getString(R.string.main_count)} $count"
        textViewCount.text = fullText
    }

    private val numberForShowing = { i: Int -> i + 1 }

    private fun fillGridLayout() {
        for (i in 0..99) {
            val view = TextView(this).apply {
                setTextColor(Color.WHITE)
                textSize = 12F
                gravity = Gravity.CENTER
                text = numberForShowing(i).toString()
            }
            gridLayout.addView(view, 50,50)
        }
    }

    private fun colorGridLayout() {
        for (i in 0..99) {
            if (array[i] == 0) {
                gridLayout.getChildAt(i).setBackgroundResource(R.color.orange)
            } else {
                gridLayout.getChildAt(i).setBackgroundResource(R.color.colorPrimaryDark)
            }
        }
    }

    private fun colorCurrentRandomIndex() {
        gridLayout.getChildAt(currentRandomIndex).setBackgroundResource(R.color.colorAccent)
    }
}
