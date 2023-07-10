package com.zerobase.stockdividend.exception.impl;

import com.zerobase.stockdividend.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyExistException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미 존재하는 사용자명입니다.";
    }
}
