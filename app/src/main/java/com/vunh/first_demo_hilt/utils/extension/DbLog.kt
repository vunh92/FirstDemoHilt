package com.vunh.first_demo_hilt.utils.extension

import android.os.Environment
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*

fun eLog(string: Any) = DbLog.writeLog("e", string)
fun dLog(string: Any) = DbLog.writeLog("d", string)
fun wLog(string: Any) = DbLog.writeLog("w", string)
fun iLog(string: Any) = DbLog.writeLog("i", string)
fun wtfLog(string: Any) = DbLog.writeLog("wtf", string)

object DbLog {

    /* Regular Colors */
    val RED = "\u001b[0;31m"     // RED
    val GREEN = "\u001b[0;32m"   // GREEN
    val YELLOW = "\u001b[0;33m"  // YELLOW
    val BLUE = "\u001b[0;34m"    // BLUE
    val PURPLE = "\u001b[0;35m"  // PURPLE
    val CYAN = "\u001b[0;36m"    // CYAN
    val ORGANE = "\u001b[38;5;172m"

    private var lstBuffer: MutableList<String> = mutableListOf()

    private var tagName: String = "TAG"
    private var isDebug: Boolean = true
    private var writeLogFile: Boolean = false
    private val LOG_WITH_COLOR = false   // Make color to easy determine log when using pidcat & terminal. Set [false] if using Android Studio Logcat

    private val MAX_LOG_SIZE = 1000

    fun initLog(tagName: String, isDebug: Boolean, writeLogFile: Boolean = false) {
        DbLog.tagName = tagName
        DbLog.isDebug = isDebug
        DbLog.writeLogFile = writeLogFile
    }

    fun writeLog(type: String, obj: Any) {

        if (!isDebug) return

        // Multi check type
        val text: String = when (obj) {
            is Objects -> Gson().toJson(obj)
            else -> obj.toString()
        }

        // -- Run at background
        saveToBuffer(type, text)

        // -- Print log
        // Make color to easy determine log when using Pidcat & Terminal. Set [false] if using Android Studio Logcat
        if (LOG_WITH_COLOR) {

            val stackTrade = Throwable().stackTrace
            val className = stackTrade[2].fileName + "-" + stackTrade[2].methodName

            val checkJson = isJSONValid(text)
            val convertJsonString = when (checkJson) {
                is JSONObject -> checkJson.toString(4)
                is JSONArray -> checkJson.toString(4)
                else -> text
            }

            when (type) {
                "e" -> handleLongLogText(convertJsonString) {
                    Log.e(className, makeColorLog(RED, it))
                }
                "i" -> handleLongLogText(convertJsonString) {
                    Log.i(className, makeColorLog(BLUE, it))
                }
                "d" -> handleLongLogText(convertJsonString) {
                    Log.d(className, makeColorLog(CYAN, it))
                }
                "w" -> handleLongLogText(convertJsonString) {
                    Log.w(className, makeColorLog(YELLOW, it))
                }
                "wtf" -> handleLongLogText(convertJsonString) {
                    Log.wtf(className, makeColorLog(PURPLE, it))
                }
            }
        } else {

            val checkJson = isJSONValid(text)
            val convertJsonString = when (checkJson) {
                is JSONObject -> checkJson.toString(4)
                is JSONArray -> checkJson.toString(4)
                else -> text
            }

            when (type) {
                "e" -> handleLongLogText(convertJsonString) {
                    Log.e(tagName, makeColorLog(RED, it))
                }
                "i" -> handleLongLogText(convertJsonString) {
                    Log.i(tagName, makeColorLog(BLUE, it))
                }
                "d" -> handleLongLogText(convertJsonString) {
                    Log.d(tagName, makeColorLog(CYAN, it))
                }
                "w" -> handleLongLogText(convertJsonString) {
                    Log.w(tagName, makeColorLog(YELLOW, it))
                }
                "wtf" -> handleLongLogText(convertJsonString) {
                    Log.wtf(tagName, makeColorLog(PURPLE, it))
                }
            }
        }
    }

    private fun handleLongLogText(message: String, logFunction: (textLog: String) -> Unit) {
        for (i in 0..(message.length / MAX_LOG_SIZE)) {
            val start = i * MAX_LOG_SIZE
            var end = (i + 1) * MAX_LOG_SIZE
            end = if (end > message.length) message.length else end
            logFunction.invoke(message.substring(start, end))
        }
    }

    private fun saveToBuffer(type: String, text: String) = GlobalScope.launch(Dispatchers.IO) {
        if (writeLogFile) {
            if (lstBuffer.size >= 400) { // Gioi gian luu 400 dong
                lstBuffer = mutableListOf()
            }

            val stackTrade = Throwable().stackTrace
            var className = stackTrade[1].fileName
            var methodName = stackTrade[1].methodName
            var lineNumber = stackTrade[1].lineNumber

            if (stackTrade.size > 3) {
                className = stackTrade[2].fileName
                methodName = stackTrade[2].methodName
                lineNumber = stackTrade[2].lineNumber
            }

            val log = "[$tagName:$type] $className [$methodName : $lineNumber] $text"

            lstBuffer.add(log)
        }
    }


    fun startWriteLogFile() {
        if (lstBuffer.size <= 0) return

        // Write to file
        val logFile = File(Environment.getExternalStorageDirectory().absolutePath + "/logfile.log")
        if (!logFile.exists()) {
            try {
                logFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        try {
            val buf = BufferedWriter(FileWriter(logFile, true))
            for (log: String in lstBuffer) {
                buf.append(log)
                buf.newLine()
            }
            buf.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    //region Private Support Methods
    @Suppress("ConstantConditionIf")
    fun makeColorLog(colorLog: String, string: String): String {
        if (!LOG_WITH_COLOR) return string
        val stringBuilder = StringBuilder(string)
        val stringBuilderTemp = StringBuilder(colorLog)
        stringBuilder.forEach {
            stringBuilderTemp.append(it)
            if (it == '\n') stringBuilderTemp.append(colorLog)
        }
        return stringBuilderTemp.toString()
    }

    fun isJSONValid(test: String?): Any? {
        return try {
            JSONObject(test)
        } catch (ex: JSONException) {
            try {
                JSONArray(test)
            } catch (ex1: JSONException) {
                null
            }
        }
    }
    //endregion


}