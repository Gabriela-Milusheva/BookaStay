package com.hotelmanager.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelmanager.dtos.request.RequestDto;
import com.hotelmanager.dtos.response.ResponseDto;
import com.hotelmanager.exception.requestAndResponse.RequestAndResponseException;
import com.hotelmanager.models.RequestAndResponse;
import com.hotelmanager.repositories.RequestAndResponseRepository;

import jakarta.validation.Validator;

@Service
public class RequestAndResponseService {

    private final RequestAndResponseRepository requestAndResponseRepository;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public RequestAndResponseService(RequestAndResponseRepository requestAndResponseRepository, ObjectMapper objectMapper, Validator validator) {
        this.requestAndResponseRepository = requestAndResponseRepository;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @Transactional
    public void createRequestAndResponse(RequestDto<?> request, ResponseDto<?> response, String controllerName, String methodName) {
        try {
            RequestAndResponse requestAndResponse = new RequestAndResponse();
            requestAndResponse.setDate(LocalDateTime.now());
            requestAndResponse.setControllerName(controllerName);
            requestAndResponse.setMethodName(methodName);
            requestAndResponse.setRequestId(request.getRequestId() != null ? request.getRequestId() : UUID.randomUUID().toString());
            requestAndResponse.setRequestData(objectMapper.writeValueAsString(request.getData()));
            requestAndResponse.setResponseId(response.getResponseId());
            requestAndResponse.setResponseData(objectMapper.writeValueAsString(response.getData()));
            requestAndResponse.setErrorMessage(response.getErrorDescription());

            requestAndResponseRepository.save(requestAndResponse);
        } catch (Exception e) {
            throw new RequestAndResponseException.SaveRequestAndResponseException(e);
        }
    }

    @Transactional
    public <T> void createRequestAndResponse(T data, ResponseDto<?> response, String controllerName, String methodName) {
        try {
            RequestAndResponse requestAndResponse = new RequestAndResponse();
            requestAndResponse.setDate(LocalDateTime.now());
            requestAndResponse.setControllerName(controllerName);
            requestAndResponse.setMethodName(methodName);
            requestAndResponse.setRequestId(UUID.randomUUID().toString());
            requestAndResponse.setRequestData("Route parameter: " + objectMapper.writeValueAsString(data));
            requestAndResponse.setResponseId(response.getResponseId());
            requestAndResponse.setResponseData(objectMapper.writeValueAsString(response.getData()));
            requestAndResponse.setErrorMessage(response.getErrorDescription());

            requestAndResponseRepository.save(requestAndResponse);
        } catch (Exception e) {
            throw new RequestAndResponseException.SaveRequestAndResponseException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<RequestAndResponse> getRequestsAndResponses() {
        return requestAndResponseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public RequestAndResponse findByRequestId(String requestId) {
        if (requestId == null || requestId.isEmpty()) {
            throw new RequestAndResponseException.RequestIdNullOrEmptyException();
        }
        return requestAndResponseRepository.findByRequestId(requestId).orElse(null);
    }

    @Transactional(readOnly = true)
    public RequestAndResponse findByResponseId(String responseId) {
        if (responseId == null || responseId.isEmpty()) {
            throw new RequestAndResponseException.ResponseIdNullOrEmptyException();
        }
        return requestAndResponseRepository.findByResponseId(responseId).orElse(null);
    }
}
