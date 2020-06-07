package com.example.gamebacklog.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.gamebacklog.model.Game
import com.example.gamebacklog.rebository.RepositoryGame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Author: Omar
 */
class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val gameRepo = RepositoryGame(application.applicationContext)
    val game: LiveData<List<Game>> = gameRepo.getAllGames()

    //  insert game method will be calld in the mainActivity
    fun insertGame(game: Game) {
        ioScope.launch {
            gameRepo.insertGame(game)
        }
    }

    //  Delete game method will be calld in the mainActivity
    fun deleteGame(game: Game) {
        ioScope.launch {
            gameRepo.deleteGame(game)
        }
        //  Delete all games methode will be calld in the mainActivity

    }

    fun deleteAllGames() {
        ioScope.launch {
            gameRepo.deleteAllGames()
        }
    }
}