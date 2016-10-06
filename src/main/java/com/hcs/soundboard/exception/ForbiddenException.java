package com.hcs.soundboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Returns a 403 unauthorized HTTP status code. This indicates the user is not
 * authorized to access the resource they tried to access or perform the action
 * they tried to perform/
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
}