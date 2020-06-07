package com.example.gamebacklog.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.gamebacklog.R
import com.example.gamebacklog.model.Game

import kotlinx.android.synthetic.main.activity_add_game.*
import kotlinx.android.synthetic.main.content_add_game.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Author: Omar
 */
private val dateFormat = SimpleDateFormat("dd MM yyyy", Locale.ENGLISH)

class AddGameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_game)
        setSupportActionBar(toolbar)


        initview()
    }

    private fun initview() {
        fab.setOnClickListener {
            onSaveClickGame()
        }
    }

    private fun onSaveClickGame() {
        // validation things
        if (etTitle.text.toString().isNotBlank() && etPlatform.text.toString()
                .isNotBlank() && etDay.text.toString().isNotBlank()
            && etMonth.text.toString().isNotBlank() && etYear.text.toString().isNotBlank()
        ) {
            val titleInput = etTitle.text.toString()
            val platformInput = etPlatform.text.toString()
            val dayInput = etDay.text.toString()
            val monthInput = etMonth.text.toString()
            val yearInput = etYear.text.toString()

            //making game object and giving the values
            val game = Game(titleInput, platformInput, dateStyle(dayInput, monthInput, yearInput))

            // Making intent for the data

            val resultIntent = Intent(this, MainActivity::class.java)
            resultIntent.putExtra(NEW_GAME, game)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        } else {
            Toast.makeText(
                this, "All fields must be filed"
                , Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun dateStyle(day: String, month: String, year: String): Date {
        return dateFormat.parse("$day $month $year")
    }

    companion object {
        const val NEW_GAME = "NEW_GAME"
    }


}
