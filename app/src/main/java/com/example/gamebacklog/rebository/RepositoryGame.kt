package com.example.gamebacklog.rebository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gamebacklog.dao.GameDao
import com.example.gamebacklog.model.Game
import com.example.gamebacklog.roomDatabase.RoomDatabaseGame

class RepositoryGame(context: Context) {
    private var gameDao: GameDao?

    init {
        val gameRoomDatabase = RoomDatabaseGame.getDatabase(context)
        gameDao = gameRoomDatabase?.gameDao()
    }

    fun getAllGames(): LiveData<List<Game>> {
        return gameDao?.getAllGames() ?: MutableLiveData(emptyList())
    }

    fun insertGame(game: Game) {
        gameDao?.insertGame(game)
    }

    suspend fun deleteGame(game: Game) {
        gameDao?.deleteGame(game)
    }

    suspend fun deleteAllGames() {
        gameDao?.deleteAllGames()
    }

}