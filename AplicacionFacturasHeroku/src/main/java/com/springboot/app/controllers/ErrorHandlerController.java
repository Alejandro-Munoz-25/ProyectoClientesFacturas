package com.springboot.app.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandlerController {

	@ExceptionHandler({ ArithmeticException.class })
	public String aritmeticaError(ArithmeticException ex, Model model) {
		return "error/urlError";
	}

	@ExceptionHandler({ NumberFormatException.class })
	public String numberFormatError(NumberFormatException ex, Model model) {
		return "error/urlError";
	}

}
