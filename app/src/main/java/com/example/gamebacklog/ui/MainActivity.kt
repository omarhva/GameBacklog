package com.example.gamebacklog.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamebacklog.R
import com.example.gamebacklog.adapter.GameAdapter
import com.example.gamebacklog.model.Game
import com.example.gamebacklog.ui.AddGameActivity.Companion.NEW_GAME

import kotlinx.android.synthetic.main.activity_main.*

/**
 * Author: Omar
 */

const val ADD_GAME_REQUEST_CODE = 100
const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private var games = arrayListOf<Game>()
    private var gameAdapter = GameAdapter(games)
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.rvGames)

        games = arrayListOf()

        gameAdapter = GameAdapter(games)

        viewManager = LinearLayoutManager(this)

        createItemTouchHelper().attachToRecyclerView(recyclerView)

        observeViewModel()

        recyclerView.apply {
            //when changing the contents of the adapter does not change the recyclerView height or the width (understanding of setHasFixedSize(true) ).
            setHasFixedSize(true)
            layoutManager = viewManager

            adapter = gameAdapter
        }


        fab.setOnClickListener {
            startAddActivity()
        }
    }

    private fun observeViewModel() {
        viewModel.game.observe(this, Observer { games ->
            this@MainActivity.games.clear()
            this@MainActivity.games.addAll(games)
            gameAdapter.notifyDataSetChanged()
        })

    }

    // go the other activty
    private fun startAddActivity() {
        val resultIntent = Intent(
            this, AddGameActivity::class.java
        )
        startActivityForResult(resultIntent, ADD_GAME_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ADD_GAME_REQUEST_CODE -> {
                    data?.let { safeData ->
                        val game = safeData.getParcelableExtra<Game>(NEW_GAME)

                        game?.let { safeGame ->
                            viewModel.insertGame(safeGame)
                        } ?: run {
                            Log.e(TAG, "game is null")
                        }
                    } ?: run {
                        Log.e(TAG, "null intent data received")
                    }
                }
            }
        }
    }

    private fun deletAllGames() {
        val gamesToDelete = ArrayList<Game>()
        gamesToDelete.addAll(games)
        viewModel.deleteAllGames()
        Snackbar.make(
                findViewById(R.id.rvGames),
                "All games are succesfully delete",
                Snackbar.LENGTH_LONG
            )
            .setAction("UNDO") {
                gamesToDelete.forEach {
                    viewModel.insertGame(it)
                }
            }.show()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_delete_games_list -> {
                deletAllGames()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createItemTouchHelper(): ItemTouchHelper {

        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val gameToDelete = games[position]
                if (direction == ItemTouchHelper.LEFT) {
                    viewModel.deleteGame(gameToDelete)
                    Snackbar.make(
                            viewHolder.itemView,
                            "The game is successfully deleted",
                            Snackbar.LENGTH_LONG
                        )
                        .setAction("UNDO") {
                            viewModel.insertGame(gameToDelete)
                        }.show()
                }
                gameAdapter.notifyDataSetChanged()

            }
        }
        return ItemTouchHelper(callback)
    }

}
