package br.com.sbrwgjd.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/math")
public class MathController {

    @GetMapping("subtract/{num1}/{num2}")
    public Double subtract(
            @PathVariable("num1") double num1,
            @PathVariable("num2") double num2) throws IllegalArgumentException {

        if (!isNumeric(num1) || !isNumeric(num2)) {
            throw new IllegalArgumentException();
        }

        return num1 - num2;
    }

    @GetMapping("multiply/{num1}/{num2}")
    public Double multiply(
            @PathVariable("num1") double num1,
            @PathVariable("num2") double num2) throws IllegalArgumentException {

        if (!isNumeric(num1) || !isNumeric(num2)) {
            throw new IllegalArgumentException();
        }

        return num1 * num2;
    }

    @GetMapping("divide/{num1}/{num2}")
    public Double divide(
            @PathVariable("num1") double num1,
            @PathVariable("num2") double num2) throws IllegalArgumentException {

        if (!isNumeric(num1) || !isNumeric(num2) || num2 == 0) {
            throw new IllegalArgumentException();
        }

        return num1 / num2;
    }

    private boolean isNumeric(double num) {
        return !Double.isNaN(num) && !Double.isInfinite(num);
    }
}
