package com.example.test.data.repository

import com.example.test.utils.ResponseDataBase
import kotlinx.coroutines.flow.FlowCollector

abstract class BaseRepositoryDataBase(
) {
    protected suspend fun <T> doWorkList(value: List<T>, collector: FlowCollector<ResponseDataBase<T>>) {
        try {
            if (value.isEmpty()) {
                collector.emit(ResponseDataBase.Empty)
            } else {
                collector.emit(ResponseDataBase.SuccessList(value))
            }
        } catch (e: Exception) {
            collector.emit(ResponseDataBase.Failure(e))
        }
    }

    protected suspend fun <T> doWorkNotList(value: T,collector: FlowCollector<ResponseDataBase<T>>) {
        try {
            if (value == null ) {
                collector.emit(ResponseDataBase.Empty)
            } else {
                collector.emit(ResponseDataBase.SuccessNotList(value))
            }
        } catch (e: Exception) {
            collector.emit(ResponseDataBase.Failure(e))
        }
    }
}