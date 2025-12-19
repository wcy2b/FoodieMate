package com.example.foodiemate.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.foodiemate.data.dao.DiaryDao
import com.example.foodiemate.data.entity.Diary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Database(entities = [Diary::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun diaryDao(): DiaryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "foodiemate_database"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Pre-populate data on database creation
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                val diaryDao = database.diaryDao()
                                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                val currentDate = sdf.format(Date())
                                
                                diaryDao.insertDiary(Diary(
                                    foodName = "招牌红烧肉",
                                    content = "今天在巷子里发现一家宝藏餐馆，红烧肉软糯适口，肥而不腻，配上白米饭简直绝了！",
                                    date = currentDate,
                                    imageUri = "android.resource://${context.packageName}/drawable/img_hongshaorou"
                                ))
                                
                                diaryDao.insertDiary(Diary(
                                    foodName = "鲜虾小笼包",
                                    content = "早起去排队买的小笼包，皮薄馅大，每一口都有鲜美的汤汁，虾仁Q弹有力。",
                                    date = currentDate,
                                    imageUri = "android.resource://${context.packageName}/drawable/img_xiaolongbao"
                                ))
                            }
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

