package com.multiplication.rest.multiplication.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.multiplication.rest.multiplication.domain.Multiplication;
import com.multiplication.rest.multiplication.domain.MultiplicationResultAttempt;
import com.multiplication.rest.multiplication.domain.User;
import com.multiplication.rest.multiplication.event.EventDispatcher;
import com.multiplication.rest.multiplication.event.MultiplicationSolvedEvent;
import com.multiplication.rest.multiplication.repository.MultiplicationResultAttemptRepository;
import com.multiplication.rest.multiplication.repository.UserRepository;

@Service
class MultiplicationServiceImpl implements MultiplicationService {

	private RandomGeneratorService randomGeneratorService;
	private UserRepository UserRepository;
	private MultiplicationResultAttemptRepository attemptRepository;
	private EventDispatcher eventDispatcher;

	@Autowired
	public MultiplicationServiceImpl(final RandomGeneratorService randomGeneratorService,
			final UserRepository userRepository, final MultiplicationResultAttemptRepository attemptRepository,
			final EventDispatcher eventDispatcher) {
		this.randomGeneratorService = randomGeneratorService;
		this.UserRepository = userRepository;
		this.attemptRepository = attemptRepository;
		this.eventDispatcher = eventDispatcher;
	}

	@Override
	public Multiplication createRandomMultiplication() {
		int factorA = randomGeneratorService.generateRandomFactor();
		int factorB = randomGeneratorService.generateRandomFactor();
		return new Multiplication(factorA, factorB);
	}

	@Override
	public boolean checkAttempt(final MultiplicationResultAttempt resultAttempt) {

		Optional<User> user = UserRepository.findByAlias(resultAttempt.getUser().getAlias());

		Assert.isTrue(!resultAttempt.isCorrect(), "You can't send an attempt marked as correct!!");

		boolean isCorrect = resultAttempt.getResultAttempt() == resultAttempt.getMultiplication().getFactorA()
				* resultAttempt.getMultiplication().getFactorB();

		MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(
				user.orElse(resultAttempt.getUser()), resultAttempt.getMultiplication(),
				resultAttempt.getResultAttempt(), isCorrect);

		attemptRepository.save(checkedAttempt);

		// Communicates the result via Event
		eventDispatcher.send(new MultiplicationSolvedEvent(checkedAttempt.getId(), checkedAttempt.getUser().getId(),
				checkedAttempt.isCorrect()));
		return isCorrect;
	}

	@Override
	public List<MultiplicationResultAttempt> getStatsForUser(String userAlias) {
		return attemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
	}

	@Override
	public MultiplicationResultAttempt getResultById(final Long resultId) {
		return attemptRepository.findById(resultId).orElseThrow(
				() -> new IllegalArgumentException("The requested resultId [" + resultId + "]" + "doesn't exits."));
	}
}
