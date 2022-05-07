package com.mousom.edvora.utils

import com.mousom.edvora.data.model.OperationErrorType


class EdvoraOperationError private constructor(
    val errorType: OperationErrorType,
    val errorId: Int? = 1,
    val messageTitle: String? = null,
    val message: String? = null,
    val fieldErrors: String? = null) {

    data class Builder(
        var errorType: OperationErrorType,
        var errorId: Int? = 1,
        var messageTitle: String? = null,
        var message: String? = null,
        var fieldErrors: String? = null) {

        fun errorId(errorId: Int) = apply { this.errorId = errorId }
        fun messageTitle(messageTitle: String) = apply { this.messageTitle = messageTitle }
        fun message(message: String) = apply { this.message = message }
        fun fieldError(fieldErrors: String) = apply { this.fieldErrors = fieldErrors }

        fun build() = EdvoraOperationError(
            errorType,
            errorId,
            messageTitle = messageTitle,
            message = message,
            fieldErrors = fieldErrors
        )
    }
}