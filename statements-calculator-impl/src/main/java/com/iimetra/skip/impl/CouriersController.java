package com.iimetra.skip.impl;

import com.iimetra.skip.api.CouriersApi;
import com.iimetra.skip.impl.service.StatementCalculatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CouriersController implements CouriersApi {
    private final StatementCalculatorService calculatorService;

    @Override
    public ResponseEntity<Object> getStatements(String id) {
        return null;
    }
}
