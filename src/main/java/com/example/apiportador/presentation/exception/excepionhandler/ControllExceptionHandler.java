package com.example.apiportador.presentation.exception.excepionhandler;

import com.example.apiportador.presentation.exception.ApiDownException;
import com.example.apiportador.presentation.exception.CardHolderAlreadyExistsException;
import com.example.apiportador.presentation.exception.CardHolderNotFoundException;
import com.example.apiportador.presentation.exception.ClientIdNotCompatibleException;
import com.example.apiportador.presentation.exception.CreditNotApprovedException;
import com.example.apiportador.presentation.exception.CreditNotFoundException;
import com.example.apiportador.presentation.exception.InsufficientLimitException;
import com.example.apiportador.presentation.exception.UuidOutOfFormatException;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class ControllExceptionHandler {


    private static final String TIMESTAMP = "timestamp";
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int NOT_FOUND = 404;
    private static final int CONFLICT = 409;

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CreditNotFoundException.class)
    public ResponseEntity<ProblemDetail> creditNotFoundExceptionHandle(final CreditNotFoundException exception) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setType(URI.create(""));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setDetail(exception.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(problemDetail);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CardHolderNotFoundException.class)
    public ResponseEntity<ProblemDetail> cardHolderNotFoundExceptionHandle(final CardHolderNotFoundException exception) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setType(URI.create(""));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setDetail(exception.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(problemDetail);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(CreditNotApprovedException.class)
    public ResponseEntity<ProblemDetail> creditNotApprovedExceptionHandle(final CreditNotApprovedException exception) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setType(URI.create(""));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setDetail(exception.getMessage());
        return ResponseEntity.unprocessableEntity().body(problemDetail);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ClientIdNotCompatibleException.class)
    public ResponseEntity<ProblemDetail> clientIdNotCompatibleExceptionHandle(final ClientIdNotCompatibleException exception) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setType(URI.create(""));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setDetail(exception.getMessage());
        return ResponseEntity.unprocessableEntity().body(problemDetail);
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ApiDownException.class)
    public ResponseEntity<ProblemDetail> apiDownExceptionHandle(final ApiDownException exception) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE);
        problemDetail.setType(URI.create(""));
        problemDetail.setProperty(TIMESTAMP, Instant.now());
        problemDetail.setTitle(exception.getMessage());
        return ResponseEntity.status(SERVICE_UNAVAILABLE).body(problemDetail);
    }


    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(CardHolderAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> cardHolderAlreadyExistsExceptionHandle(final CardHolderAlreadyExistsException exception) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setType(URI.create(""));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setDetail(exception.getMessage());
        return ResponseEntity.unprocessableEntity().body(problemDetail);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(UuidOutOfFormatException.class)
    public ResponseEntity<ProblemDetail> uuidOutOfFormatExceptionHandle(final UuidOutOfFormatException exception) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setType(URI.create(""));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setDetail(exception.getMessage());
        return ResponseEntity.unprocessableEntity().body(problemDetail);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InsufficientLimitException.class)
    public ResponseEntity<ProblemDetail> insufficientLimitExceptionHandle(final InsufficientLimitException exception) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setType(URI.create(""));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setDetail(exception.getMessage());
        return ResponseEntity.unprocessableEntity().body(problemDetail);
    }
}
