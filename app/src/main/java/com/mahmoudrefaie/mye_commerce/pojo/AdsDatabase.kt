package com.mahmoudrefaie.mye_commerce.pojo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Ads::class] , version = 2)
abstract class AdsDatabase : RoomDatabase() {

    public abstract fun adsDao(): AdsDao

    companion object{

        private var mInstance: AdsDatabase?=null

//        fun getInstance(context: Context) : AdsDatabase {
//            if (mInstance == null) {
//                AdsDatabase.mInstance = Room.databaseBuilder(context.applicationContext, AdsDatabase::class.java, "ads_database")
//                    .fallbackToDestructiveMigration()
//                    .build()
//            }
//            return mInstance!!
//        }

        fun getInstance(context: Context) : AdsDatabase{
            return mInstance?: synchronized(this){
                mInstance
                    ?: buildDatabase(context).also { mInstance = it }
            }
        }

        private fun buildDatabase(context: Context): AdsDatabase{
            return Room.databaseBuilder(context.applicationContext,AdsDatabase::class.java,"ads_database")
                .fallbackToDestructiveMigration()
                .build()
        }

    }
}


//if(mInstance == null){
//    AdsDatabase.mInstance = Room.databaseBuilder(context.applicationContext,AdsDatabase::class.java,"ads_database")
//        .fallbackToDestructiveMigration()
//        .build()
//}
//return mInstance!!