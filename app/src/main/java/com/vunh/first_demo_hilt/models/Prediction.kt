package com.vunh.first_demo_hilt.models

import android.content.Context

data class Prediction(
    var id: String,
    var name: String,
    var isChecked: Boolean
) {
    constructor() : this("", "None", true)

    companion object {
        fun getListDemo(): MutableList<Prediction> {
            val data: MutableList<Prediction> = arrayListOf()
            data.add(Prediction("1", "Demo", true))
            return data
        }
        fun getMoreListDemo(): MutableList<Prediction> {
            val data: MutableList<Prediction> = arrayListOf()
            data.add(Prediction("demo1", "Demo 1", true))
            data.add(Prediction("demo2", "Demo 2", true))
            data.add(Prediction("demo3", "Demo 3", true))
            data.add(Prediction("demo4", "Demo 4", true))
            data.add(Prediction("demo5", "Demo 5", true))
            data.add(Prediction("demo6", "Demo 6", true))
            data.add(Prediction("demo7", "Demo 7", true))
            data.add(Prediction("demo8", "Demo 8", true))
            data.add(Prediction("demo9", "Demo 9", true))
            data.add(Prediction("demo10", "Demo 10", true))
            data.add(Prediction("demo11", "Demo 11", true))
            data.add(Prediction("demo12", "Demo 12", true))
            data.add(Prediction("demo13", "Demo 13", true))
            data.add(Prediction("demo14", "Demo 14", true))
            data.add(Prediction("demo15", "Demo 15", true))
            data.add(Prediction("demo16", "Demo 16", true))
            data.add(Prediction("demo17", "Demo 17", true))
            data.add(Prediction("demo18", "Demo 18", true))
            data.add(Prediction("demo19", "Demo 19", true))
            data.add(Prediction("demo20", "Demo 20", false))
            data.add(Prediction("demo21", "Demo 21", false))
            data.add(Prediction("demo22", "Demo 22", false))
            data.add(Prediction("demo23", "Demo 23", false))
            data.add(Prediction("demo24", "Demo 24", false))
            data.add(Prediction("demo25", "Demo 25", false))
            data.add(Prediction("demo26", "Demo 26", false))
            data.add(Prediction("demo27", "Demo 27", false))
            data.add(Prediction("demo28", "Demo 28", false))
            data.add(Prediction("demo29", "Demo 29", false))
            data.add(Prediction("demo30", "Demo 30", false))
            data.add(Prediction("demo31", "Demo 31", false))
            data.add(Prediction("demo32", "Demo 32", false))
            data.add(Prediction("demo33", "Demo 33", false))
            data.add(Prediction("demo34", "Demo 34", false))
            data.add(Prediction("demo35", "Demo 35", false))
            data.add(Prediction("demo36", "Demo 36", false))
            data.add(Prediction("demo37", "Demo 37", false))
            data.add(Prediction("demo38", "Demo 38", false))
            data.add(Prediction("demo39", "Demo 39", false))
            return data
        }
    }
}
