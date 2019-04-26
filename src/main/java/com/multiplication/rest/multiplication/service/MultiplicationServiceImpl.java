package com.multiplication.rest.multiplication.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multiplication.rest.multiplication.domain.Multiplication;
import com.multiplication.rest.multiplication.domain.MultiplicationResultAttempt;

@Service
class MultiplicationServiceImpl implements MultiplicationService {

	private RandomGeneratorService randomGeneratorService;

    @Autowired
    public MultiplicationServiceImpl(final RandomGeneratorService randomGeneratorService) {
        this.randomGeneratorService = randomGeneratorService;
    }

    @Override
    public Multiplication createRandomMultiplication() {
        int factorA = randomGeneratorService.generateRandomFactor();
        int factorB = randomGeneratorService.generateRandomFactor();
        return new Multiplication(factorA, factorB);
    }

    @Override
    public boolean checkAttempt(final MultiplicationResultAttempt resultAttempt) {
        return resultAttempt.getResultAttempt() ==
                resultAttempt.getMultiplication().getFactorA() *
                resultAttempt.getMultiplication().getFactorB();
    }
}
