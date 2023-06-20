package com.vunh.first_demo_hilt.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.vunh.first_demo_hilt.base.BaseViewModel
import com.vunh.first_demo_hilt.database.AppDatabase
import com.vunh.first_demo_hilt.models.Profile
import com.vunh.first_demo_hilt.models.User
import com.vunh.first_demo_hilt.repositories.UserRepository
import com.vunh.first_demo_hilt.repositories.profile.ProfileRepository
import com.vunh.first_demo_hilt.utils.states.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher,
    private val profileRepository: ProfileRepository,
    private val appDatabase: AppDatabase,
) : BaseViewModel() {

    private val _userProfileState = MutableStateFlow<UserProfileState>(UserProfileState.Init)
    val userProfileState: StateFlow<UserProfileState> = _userProfileState

    private val _resultState = MutableStateFlow<ResultState<Any>?>(null)
    val resultState: StateFlow<ResultState<Any>?> = _resultState

    var userProfile: User? = null

    init {
        getProfile(profileRepository)
//        getUserProfile(userId = 1)
    }

    fun getProfile() = getProfile(profileRepository)

    suspend fun deleteProfile(profileId: Int) {
        return deleteProfile(profileId, profileRepository)
    }

    suspend fun clearAllDatabase() {
        return withContext(Dispatchers.Main) {
            viewModelScope.launch(Dispatchers.Main) {
                appDatabase.clearAllTables()
//                appDatabase.query(SimpleSQLiteQuery("ALTER TABLE `tbMaintenance` AUTO_INCREMENT = 1"))
//                val query = SimpleSQLiteQuery("DELETE FROM 'tbMaintenance'")
//                appDatabase.beginTransaction()
//                appDatabase.query(SimpleSQLiteQuery("DELETE FROM tbMaintenance"))
//                appDatabase.compileStatement("DELETE FROM tbMaintenance")
//                appDatabase.setTransactionSuccessful()
            }
        }
    }

    private fun getUserProfile(userId: Int) {
        viewModelScope.launch(dispatcher) {
            userRepository.getUser(userId = userId)
                .onStart {

                }
                .catch {

                }
                .collect {
                    when {
                        it.isLoading -> {
                            _userProfileState.value = UserProfileState.IsLoading(true)
                        }
                        it.data == null -> {
                            _userProfileState.value = UserProfileState.Error(message = it.message.toString())
                        }
                        else -> {
                            _userProfileState.value = UserProfileState.Success(user = it.data.data)
                        }
                    }
                }
        }
    }
}