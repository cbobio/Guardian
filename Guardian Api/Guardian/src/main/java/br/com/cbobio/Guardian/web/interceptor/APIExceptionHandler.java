
package br.com.cbobio.Guardian.web.interceptor;


import br.com.cbobio.Guardian.service.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class APIExceptionHandler {

	private final MessageSource messageSource;

	@ExceptionHandler(Erro400Exception.class)
	public ResponseEntity<Object> handleBadRequest(
			Erro400Exception ex,
			HttpServletRequest request) {

		DefaultApiResponse<Object> errorResponse =
				DefaultApiResponse.error(ex.getMessage());

		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(Erro401Exception.class)
	public ResponseEntity<Object> handleUnauthorized(
			Erro401Exception ex,
			HttpServletRequest request) {

		DefaultApiResponse<Object> errorResponse =
				DefaultApiResponse.error(ex.getMessage());

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(errorResponse);
	}

	@ExceptionHandler(Erro403Exception.class)
	public ResponseEntity<Object> handleForbidden(
			Erro403Exception ex,
			HttpServletRequest request) {

		DefaultApiResponse<Object> errorResponse =
				DefaultApiResponse.error(ex.getMessage());

		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(errorResponse);
	}

	@ExceptionHandler(Erro404Exception.class)
	public ResponseEntity<Object> handleNotFound(Erro404Exception ex) {

		DefaultApiResponse<Object> errorResponse =
				DefaultApiResponse.error(ex.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(errorResponse);
	}

	@ExceptionHandler(ConflitoException.class)
	public ResponseEntity<Object> handleConflict(ConflitoException ex) {

		DefaultApiResponse<Object> errorResponse =
				DefaultApiResponse.error(ex.getMessage());

		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(errorResponse);
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<Object> handleUserAlreadyExists(
			UserAlreadyExistsException ex) {

		DefaultApiResponse<Object> errorResponse =
				DefaultApiResponse.error(ex.getMessage());

		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(errorResponse);
	}

	@ExceptionHandler(Erro500Exception.class)
	public ResponseEntity<Object> handleInternalError(Erro500Exception ex) {

		DefaultApiResponse<Object> errorResponse =
				DefaultApiResponse.error(ex.getMessage());

		return ResponseEntity.internalServerError().body(errorResponse);
	}
	@ExceptionHandler(QuantidadeSolicitadaException.class)
	public ResponseEntity<Object> handleQuantidadeSolicitada(
			QuantidadeSolicitadaException ex) {

		DefaultApiResponse<Object> errorResponse =
				DefaultApiResponse.error(ex.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(errorResponse);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			WebRequest request) {

		String mensagemGeral =
				"Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

		List<ValidationError> erros = new ArrayList<>();

		for (ObjectError erro : ex.getBindingResult().getAllErrors()) {

			if (erro instanceof FieldError fieldError) {

				String campo = fieldError.getField();

				String mensagem = messageSource.getMessage(
						erro,
						LocaleContextHolder.getLocale()
				);

				erros.add(new ValidationError(campo, mensagem));

			} else {

				String nome = erro.getObjectName();
				String mensagem = erro.getDefaultMessage();

				erros.add(new ValidationError(nome, mensagem));
			}
		}

		DefaultApiResponse<Object> errorResponse =
				DefaultApiResponse.error(
						mensagemGeral,
						new ApiResponseData<>(erros)
				);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(errorResponse);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolation(
			ConstraintViolationException ex,
			WebRequest request) {

		StringBuilder errorMsg = new StringBuilder(
				"Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente."
		);

		for (ConstraintViolation<?> violacao : ex.getConstraintViolations()) {

			String[] fieldPath =
					violacao.getPropertyPath().toString().split("\\.");

			String field = fieldPath[fieldPath.length - 1];

			String fieldFormatted = field.substring(0, 1).toUpperCase()
					+ field.substring(1);

			errorMsg.append(" ")
					.append(fieldFormatted)
					.append(": ")
					.append(violacao.getMessage());
		}

		DefaultApiResponse<Object> errorResponse =
				DefaultApiResponse.error(errorMsg.toString());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUnexpectedError(
			Exception ex,
			WebRequest request) {

		DefaultApiResponse<Object> errorResponse =
				DefaultApiResponse.error(
						"Ocorreu um erro interno no servidor."
				);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(errorResponse);
	}
}