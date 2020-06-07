package com.example.gamebacklog.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
@Entity(tableName = "gameTable")
data class Game(
    @ColumnInfo(name = "title")
    var titleText: String,
    @ColumnInfo(name = "platform")
    var platformText: String,
    @ColumnInfo(name = "releasDate")
    var releasDateText: Date,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
) : Parcelable