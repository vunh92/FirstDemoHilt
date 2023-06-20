package com.vunh.first_demo_hilt.ui.news

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vunh.first_demo_hilt.base.BaseViewModel
import com.vunh.first_demo_hilt.models.News
import com.vunh.first_demo_hilt.models.NewsStickyHeader
import com.vunh.first_demo_hilt.repositories.mainenance.MaintenanceRepository
import com.vunh.first_demo_hilt.repositories.news.NewsRepository
import com.vunh.first_demo_hilt.ui.login.LoginActivityState
import com.vunh.first_demo_hilt.utils.extension.toStringDate
import com.vunh.first_demo_hilt.utils.states.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val application: Application,
    private val newsRepository: NewsRepository,
) : BaseViewModel() {
    val newsStickyHeaderList = MutableLiveData<MutableList<NewsStickyHeader>>()

    fun getNewsList(context: Context) {
        viewModelScope.launch {
            isLoading.value = true
            delay(1000L)
            newsRepository.getAllNews(context = context)
                .onStart {
//                    isLoading.value = true
                }
                .catch {
                    isLoading.value = false
                    showError.value = it.message.toString()
                }
                .collect {
                    isLoading.value = false
                    when(it) {
                        is ResultState.Error -> {
                            showError.value = it.message
                        }
                        is ResultState.Success -> {
                            if (it.data != null) {
                                filterStickyHeader(it.data)
                            }
                        }
                    }
                }
        }
    }

    private fun filterStickyHeader(list: MutableList<News>) {
        if (list.isNotEmpty()) {
            val stickyHeaderList = arrayListOf<NewsStickyHeader>()
            val listHeader = arrayListOf<String>()
            list.forEach { news ->
                if (listHeader.find { it == news.time.toStringDate() } == null) {
                    listHeader.add(news.time.toStringDate() ?: "")
                }
            }
            listHeader.forEach { header ->
                stickyHeaderList.add(
                    NewsStickyHeader(
                        viewType = 1,
                        headerNews = header,
                        listNews = arrayListOf(),
                    )
                )
                stickyHeaderList.add(
                    NewsStickyHeader(
                        viewType = 0,
                        headerNews = header,
                        listNews = list.filter { it.time.toStringDate() == header}.toMutableList(),
                    )
                )
            }
            newsStickyHeaderList.value = stickyHeaderList
        }
    }
}