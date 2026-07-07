package com.valmiraguiar.listo.feature.common.flow

sealed interface FlowResult<Data, Error> {
    data class Success<Data, Error>(val data: Data) : FlowResult<Data, Error>
    data class Error<Data, Error>(val error: Error) : FlowResult<Data, Error>

    fun isSuccess() = this is FlowResult.Success
    fun isError() = this is FlowResult.Error
    fun asSuccess() = this is FlowResult.Success
    fun asError() = this is FlowResult.Error
}