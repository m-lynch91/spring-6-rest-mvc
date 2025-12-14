package guru.springframework.spring6restmvc.controllers;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomErrorController {

	@ExceptionHandler
	ResponseEntity handleJPAViolations(TransactionSystemException ex) {
		ResponseEntity.BodyBuilder responseEntity = ResponseEntity.badRequest();

		if (ex.getCause().getCause() instanceof ConstraintViolationException) {
			ConstraintViolationException cve = (ConstraintViolationException) ex.getCause().getCause();

			List errors = cve.getConstraintViolations().stream().map(constraintViolation -> {
				Map<String, String> errMap = new HashMap<>();
				errMap.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
				return errMap;
			}).toList();
			return responseEntity.body(errors);
		}

		return responseEntity.build();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity handleBindErrors(MethodArgumentNotValidException ex) {
		List<Map<String, String>> errorList = ex.getFieldErrors().stream().map(fieldError -> {
			Map<String, String> errorMap = new HashMap<>();
			errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
			return errorMap;
		}).toList();

		return ResponseEntity.badRequest().body(errorList);
	}

}
