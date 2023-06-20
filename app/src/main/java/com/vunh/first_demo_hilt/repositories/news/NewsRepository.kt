package com.vunh.first_demo_hilt.repositories.news

import android.content.Context
import com.vunh.first_demo_hilt.models.News
import com.vunh.first_demo_hilt.utils.states.ResultState
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getAllNews(context: Context) : Flow<ResultState<MutableList<News>>>
}