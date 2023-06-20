package com.vunh.first_demo_hilt.utils

const val BASE_URL = "https://golang-heroku.herokuapp.com/api/"
const val HOME_URL = "https://golang-heroku.herokuapp.com/api/"
const val LIST_USER_URL = "https://gorest.co.in/public/v2/users"
const val REQRES_URL = "https://reqres.in/"
const val DATABASE_NAME = "first_demo_hilt.db"

const val VERSION_NAME = "Version 1.0"
const val TIMEOUT_CONNECT = 10L
const val TIMEOUT_READ = 20L

const val KEY_FIRST_BOOT = "first_boot"
const val DISTANCE_PER_DAY = 33
const val DAY_PER_PERIOD = 30
const val HOTLINE = "0334422586"

annotation class ConstantsIntentKey {
    companion object {
        const val REFRESH_DATA = "refresh_data"
        const val PROFILE_DATA = "profile_data"
        const val MAINTENANCE_DATA = "maintenance_data"
    }
}

annotation class ConstantsTimeDelay {
    companion object {
        const val TIME_SPLASH = 30
    }
}

annotation class ConstantsIcon {
    companion object {
//        var HOME = R.drawable.ic_home
//        var EXCHANGE = R.drawable.ic_exchange
//        var QRCODE = R.drawable.ic_qrcode
//        var QUERY = R.drawable.ic_query
//        var REQUEST = R.drawable.ic_request
//        var WALLET = R.drawable.ic_wallet
//        var BOOK = R.drawable.ic_book
//        var WARNING = R.drawable.ic_warning
//        var CONTACTS = R.drawable.ic_contacts
    }
}

annotation class ConstantsHttpCode {
    companion object {
        const val SUCCESS = "200"
        const val CREATE_SUCCESS = "201"
        const val AUTHENTICATION_ERROR = "401"
        const val NOT_FOUND_ERROR = "404"
        const val FORBIDDEN_ERROR = "403"
        const val SERVER_ERROR = "500"
    }
}

annotation class ConstantsLanguage {
    companion object {
        const val JAPANESE = "ja"
        const val VIETNAMESE = "vi"
        const val ENGLISH = "en"
    }
}

annotation class ConstantsSetting {
    companion object {
        const val SETTINGS_APP = "setting_app"
        const val SETTINGS_LANGUAGE = "setting_language"
        const val SETTINGS_MANUAL_INPUT = "setting_manual_input"
        const val SETTINGS_ENABLE_OFFLINE = "setting_enable_offline"
        const val SETTINGS_GET_LAST_VERSION = "setting_get_last_version"
        const val SETTINGS_SYNC_DATA_LOCAL = "setting_sync_data_local"
    }
}

annotation class ConstantsPopupType {
    companion object {
        const val SUCCESS = 1
        const val ERROR = 2
        const val WARNING = 3
    }
}

annotation class ConstantsDateType {
    companion object {
        const val DATE = "yyyy-MM-dd"
        const val TIME = "hh:mm:ss"
        const val DATETIME = "dd/MM/yyyy hh:mm:ss"
    }
}

