package com.beddoed.offers.web;

import java.util.List;

class ApiError {

    private List<FieldError> fieldErrors;

    public ApiError(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }
}
