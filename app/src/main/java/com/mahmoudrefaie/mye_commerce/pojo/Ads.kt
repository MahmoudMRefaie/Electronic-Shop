package com.mahmoudrefaie.mye_commerce.pojo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ads_table")
data class Ads(
    @ColumnInfo(name = "col_type")
    val type : String?= null,

    @ColumnInfo(name = "col_image_url")
    var imageUrl : String?= null
){
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
}