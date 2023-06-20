package com.vunh.first_demo_hilt.models

data class NewsStickyHeader(
    var viewType: Int = 0,
    var headerNews: String?,
    var listNews: MutableList<News> = arrayListOf(),
)
