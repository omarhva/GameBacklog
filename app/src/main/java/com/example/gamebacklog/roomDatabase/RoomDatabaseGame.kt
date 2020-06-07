package com.example.gamebacklog.roomDatabase
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gamebacklog.dao.GameDao
import com.example.gamebacklog.model.Game

@Database(entities = [Game::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)

abstract class RoomDatabaseGame : RoomDatabase() {

    abstract fun gameDao(): GameDao

    companion object {
        private const val DATABASE_NAME = "GAMES_DATABASE"

        @Volatile
        private var gameRoomDatabaseInstance: RoomDatabaseGame? = null

        fun getDatabase(context: Context): RoomDatabaseGame? {
            if (gameRoomDatabaseInstance == null) {
                synchronized(RoomDatabaseGame::class.java) {
                    if (gameRoomDatabaseInstance == null) {
                        gameRoomDatabaseInstance = Room.databaseBuilder(
                                context.applicationContext,
                                RoomDatabaseGame::class.java, DATABASE_NAME
                            )
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return gameRoomDatabaseInstance
        }
    }

}