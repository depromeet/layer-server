package org.layer.global.exception;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.layer.common.exception.BaseCustomException;
import org.layer.common.exception.ExceptionResponse;
import org.layer.common.exception.ExceptionType;
import org.layer.discord.event.ErrorEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
	private static final String LOG_FORMAT = "%s";
	private final ApplicationEventPublisher eventPublisher;

	@ExceptionHandler(BaseCustomException.class)
	public ResponseEntity<ExceptionResponse> handleBaseException(BaseCustomException e) {
		ExceptionType exceptionType = e.getExceptionType();
		log.warn(String.format(LOG_FORMAT, e.getMessage()), e);
		return ResponseEntity.status(exceptionType.httpStatus())
			.body(ExceptionResponse.of(exceptionType.name(), exceptionType.message()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleException(Exception e) {
		log.error(String.format(LOG_FORMAT, e.getMessage()), e);
		eventPublisher.publishEvent(ErrorEvent.of(e.getMessage(), Arrays.toString(e.getStackTrace()), LocalDateTime.now()));
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ExceptionResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.name(), "서버 에러입니다."));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException e) {
		log.error(String.format(LOG_FORMAT, e.getMessage()), e);

		// TODO: ValidException 타입 정의 후 교체
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(ExceptionResponse.of(HttpStatus.BAD_REQUEST.name(), "유효하지 않은 값"));
	}

	//TODO: JSON deserialize 에러 시 해당 에러 반환 -> enum 배열 받는 방법이 있다면 적용 후 제거
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(Exception e) {
		log.error(String.format(LOG_FORMAT, e.getMessage()), e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(ExceptionResponse.of(HttpStatus.BAD_REQUEST.name(), "유효하지 않은 값."));
	}

}
