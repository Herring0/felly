package com.herring.felly.config;

import com.herring.felly.document.log.ApiLogRequestDocument;
import com.herring.felly.document.log.ApiLogResponseDocument;
import com.herring.felly.service.log.ApiLogService;
import com.herring.felly.util.JsonToMapConverter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    private final ApiLogService apiLogService;

    private final JsonToMapConverter jsonToMapConverter;

    public LoggingFilter(ApiLogService apiLogService, JsonToMapConverter jsonToMapConverter) {
        this.apiLogService = apiLogService;
        this.jsonToMapConverter = jsonToMapConverter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        Date startDate = new Date();
        filterChain.doFilter(requestWrapper, responseWrapper);
        Date endDate = new Date();

        String uuid = UUID.randomUUID().toString();

        String requestBody = getStringValue(requestWrapper.getContentAsByteArray(),
                request.getCharacterEncoding());
        String responseBody = getStringValue(responseWrapper.getContentAsByteArray(),
                response.getCharacterEncoding());

        ApiLogRequestDocument apiLogRequestDocument = new ApiLogRequestDocument(
                uuid, request.getRemoteAddr(), request.getMethod(), jsonToMapConverter.convertJsonToMap(requestBody),
                request.getRequestURI(), startDate, request.getHeader("User-Agent"), request.getHeader("Authorization"));

        ApiLogResponseDocument apiLogResponseDocument = new ApiLogResponseDocument(
                uuid, response.getStatus(), jsonToMapConverter.convertJsonToMap(responseBody), endDate);

        LOGGER.info("REQUEST: {}", apiLogRequestDocument);
        LOGGER.info("RESPONSE: {}", apiLogResponseDocument);

        apiLogService.writeLog(apiLogRequestDocument);
        apiLogService.writeLog(apiLogResponseDocument);

        responseWrapper.copyBodyToResponse();
    }

    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, 0, contentAsByteArray.length, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

}
