package br.com.sbrwgjd.controllers;

import br.com.sbrwgjd.exception.UnsupportedMathOperationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/math")
public class MathController {

    @GetMapping("sum/{num1}/{num2}")
    public Double sum(
            @PathVariable("num1") String num1,
            @PathVariable("num2") String num2) throws IllegalArgumentException {

        if (!isNumeric(num1) || !isNumeric(num2)) {
            throw new UnsupportedMathOperationException("Please set a numeric value.");
        }

        return convertToDouble(num1) + convertToDouble(num2);
    }

    @GetMapping("subtract/{num1}/{num2}")
    public Double subtract(
            @PathVariable("num1") String num1,
            @PathVariable("num2") String num2) throws IllegalArgumentException {

        if (!isNumeric(num1) || !isNumeric(num2)) {
            throw new UnsupportedMathOperationException("Please set a numeric value.");
        }

        return convertToDouble(num1) - convertToDouble(num2);
    }

    @GetMapping("multiply/{num1}/{num2}")
    public Double multiply(
            @PathVariable("num1") String num1,
            @PathVariable("num2") String num2) throws IllegalArgumentException {

        if (!isNumeric(num1) || !isNumeric(num2)) {
            throw new UnsupportedMathOperationException("Please set a numeric value.");
        }

        return convertToDouble(num1) * convertToDouble(num2);
    }

    @GetMapping("divide/{num1}/{num2}")
    public Double divide(
            @PathVariable("num1") String num1,
            @PathVariable("num2") String num2) throws IllegalArgumentException {

        if (!isNumeric(num1) || !isNumeric(num2)) {
            throw new UnsupportedMathOperationException("Please set a numeric value.");
        }

        return convertToDouble(num1) / convertToDouble(num2);
    }

    @GetMapping("mean/{num1}/{num2}")
    public Double mean(
            @PathVariable("num1") String num1,
            @PathVariable("num2") String num2) throws IllegalArgumentException {

        if (!isNumeric(num1) || !isNumeric(num2)) {
            throw new UnsupportedMathOperationException("Please set a numeric value.");
        }

        return (convertToDouble(num1) + convertToDouble(num2)) / 2;
    }

    @GetMapping("squareRoot/{num}")
    public Double squareRoot(
            @PathVariable("num") String num) throws IllegalArgumentException {

        if (!isNumeric(num)) {
            throw new UnsupportedMathOperationException("Please set a numeric value.");
        }

        return Math.sqrt(convertToDouble(num));
    }

    private Double convertToDouble(String strNumber) {
        if(strNumber == null || strNumber.isEmpty()){
            throw new IllegalArgumentException();
        }

        String number = strNumber.replace(",", ".");
        return Double.parseDouble(number);

    }

    private boolean isNumeric(String strNumber) {

        if(strNumber == null || strNumber.isEmpty()){
            return false;
        }

        String number = strNumber.replace(",", ".");

        return number.matches("[-+]?[0-9]*\\.?[0-9+]");
    }
}
