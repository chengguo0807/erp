package com.enterprisehub.common.core.exception;

import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int code;

    public BusinessException(String message) {
        this(422, message);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}
