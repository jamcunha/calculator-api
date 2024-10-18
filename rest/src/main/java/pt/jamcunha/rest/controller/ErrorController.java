package pt.jamcunha.rest.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestController
@RestControllerAdvice
public class ErrorController {

	@RequestMapping("/**")
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Map<String, String> notFound() {
		return Map.of("error", "invalid endpoint");
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> invalidParam(final MethodArgumentTypeMismatchException e) {
		final String param = e.getName();
		final String requiredType = e.getRequiredType().getSimpleName();
		final String givenType = e.getValue().getClass().getSimpleName();

		final String errorMessage = String.format(
				"parameter '%s' must be of type '%s'. Got '%s' of type '%s'",
				param, requiredType, e.getValue().toString(), givenType);

		return Map.of(
				"error", "invalid parameter",
				"message", errorMessage);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, String> unexpectedError() {
		// TODO: Log error
		return Map.of("error", "unexpected error");
	}
}
