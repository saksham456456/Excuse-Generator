package com.example.savageexcuse.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import java.io.InputStreamReader

class ExcuseRepository(private val excuseDao: ExcuseDao, private val context: Context) {

    val allExcuses: Flow<List<Excuse>> = excuseDao.getAllExcuses()
    val favoriteExcuses: Flow<List<Excuse>> = excuseDao.getFavoriteExcuses()

    fun getExcusesByCategories(categories: List<String>): Flow<List<Excuse>> {
        return excuseDao.getExcusesByCategories(categories)
    }

    suspend fun toggleFavorite(excuse: Excuse) {
        excuseDao.updateExcuse(excuse.copy(isFavorite = !excuse.isFavorite))
    }

    suspend fun populateDatabaseIfEmpty() {
        if (excuseDao.getCount() == 0) {
            val inputStream = context.assets.open("excuses.json")
            val reader = InputStreamReader(inputStream)
            val listType = object : TypeToken<List<ExcuseDto>>() {}.type
            val excuseDtos: List<ExcuseDto> = Gson().fromJson(reader, listType)

            val excuses = excuseDtos.map {
                Excuse(category = it.category, text = it.text)
            }
            excuseDao.insertAll(excuses)
        }
    }
}

data class ExcuseDto(val id: Int, val category: String, val text: String)
