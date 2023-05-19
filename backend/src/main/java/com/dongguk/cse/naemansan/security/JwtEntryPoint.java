package com.dongguk.cse.naemansan.security;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.common.ExceptionDto;
import com.dongguk.cse.naemansan.common.ResponseDto;
import com.dongguk.cse.naemansan.common.RestApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ErrorCode errorCode = (ErrorCode) request.getAttribute("exception");

        if (errorCode == null) {
            setErrorResponse(response, ErrorCode.TOKEN_INVALID_ERROR);
        } else {
            switch (errorCode) {
                case ACCESS_DENIED_ERROR -> { setErrorResponse(response, ErrorCode.ACCESS_DENIED_ERROR); }
                case TOKEN_MALFORMED_ERROR -> { setErrorResponse(response, ErrorCode.TOKEN_MALFORMED_ERROR); }
                case TOKEN_EXPIRED_ERROR -> { setErrorResponse(response, ErrorCode.TOKEN_EXPIRED_ERROR); }
                case TOKEN_TYPE_ERROR -> { setErrorResponse(response, ErrorCode.TOKEN_TYPE_ERROR); }
                case TOKEN_UNSUPPORTED_ERROR -> { setErrorResponse(response, ErrorCode.TOKEN_UNSUPPORTED_ERROR); }
                case TOKEN_UNKNOWN_ERROR -> { setErrorResponse(response, ErrorCode.TOKEN_UNKNOWN_ERROR); }
            }
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put("success", false);
        responseJson.put("data", null);
        responseJson.put("error", new ExceptionDto(errorCode));

        response.getWriter().print(responseJson);
    }
}
