package com.mahmoudrefaie.mye_commerce.pojo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AdsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAds(ads: Ads)

    @Query("select * from ads_table")
    suspend fun getAds(): List<Ads>

    @Query("DELETE FROM ads_table")
    suspend fun nukeTable()
}