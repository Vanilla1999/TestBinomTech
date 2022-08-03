package com.example.test.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.test.domain.GetCoordinateUseCase
import com.example.test.domain.GetPointsUseCase
import com.example.test.data.model.UserLocationModel
import com.example.test.utils.ErrorApp
import com.example.test.utils.ResponseDataBase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel(
    private val getCoordinateUseCase: GetCoordinateUseCase,
    private val getPointsUseCase: GetPointsUseCase,
) : ViewModel() {
    private val _sharedFlowError = MutableSharedFlow<ErrorApp<Any?>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val sharedFlowError = _sharedFlowError.asSharedFlow()

    private val coroutineException = CoroutineExceptionHandler { coroutineContext, throwable ->
        viewModelScope.launch(Dispatchers.Main) {
            _sharedFlowError.emit(ErrorApp.FailureUnknown(throwable.toString()))
            Log.d("FilterViewModel", throwable.toString())
        }
    } + CoroutineName("FilterViewModel")

    private val _sharedFlowUserPoint = MutableSharedFlow<ResponseDataBase<Any?>>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val sharedFlowUserPoint = _sharedFlowUserPoint.asSharedFlow()

    private val _stateFlowCoordinate =
        MutableStateFlow<ResponseDataBase<UserLocationModel?>>(ResponseDataBase.Loading)
    val stateFlowCoordinate = _stateFlowCoordinate.asStateFlow()


    init {
        getAllMarkers()
        getCoordinate()
    }

    private fun getAllMarkers() {
        viewModelScope.launch(Dispatchers.IO + coroutineException) {
            getPointsUseCase.getUserPoints().collect { markerList ->
                when (markerList) {
                    ResponseDataBase.Empty -> {
                        _sharedFlowUserPoint.emit(ResponseDataBase.Empty)
                    }
                    is ResponseDataBase.SuccessList -> {
                        var markerListThis = markerList.value
                        _sharedFlowUserPoint.emit(ResponseDataBase.SuccessList(markerListThis))
                    }
                    is ResponseDataBase.Failure -> {
                        _sharedFlowError.emit(ErrorApp.FailureDataBase(markerList.errorBody))
                    }
                    else -> {}
                }
            }
        }
    }

    private fun getCoordinate() {
        viewModelScope.launch(Dispatchers.IO + coroutineException) {
            getCoordinateUseCase.getLastUserCoordinate().collect { markerList ->
                when (markerList) {
                    ResponseDataBase.Empty -> {
                        _stateFlowCoordinate.emit(ResponseDataBase.Empty)
                    }
                    is ResponseDataBase.SuccessNotList -> {
                        var markerListThis = markerList.value
                        _stateFlowCoordinate.emit(ResponseDataBase.SuccessNotList(markerListThis))
                    }
                    is ResponseDataBase.Failure -> {
                        _sharedFlowError.emit(ErrorApp.FailureDataBase(markerList.errorBody))
                    }
                    else -> {}
                }
            }
        }
    }

    fun errorLocation(error: Throwable) {
        viewModelScope.launch(Dispatchers.IO + coroutineException) {
            _sharedFlowError.emit(ErrorApp.FailureLocation(error))
        }
    }

    override fun onCleared() {
        Log.d("FilterViewModel", "onCleard")
        super.onCleared()
    }
}

class FactoryMainView @Inject constructor(
    private val getCoordinateUseCase: GetCoordinateUseCase,
    private val getPointsUseCase: GetPointsUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(getCoordinateUseCase, getPointsUseCase) as T
    }
}