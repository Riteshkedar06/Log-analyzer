package com.loganalyzer.platform.common.exception;
public class AgentAuthenticationException
        extends RuntimeException {

    public AgentAuthenticationException(
            String message
    ) {
        super(message);
    }
}