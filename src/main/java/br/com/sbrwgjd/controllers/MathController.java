package br.com.sbrwgjd.controllers;

import br.com.sbrwgjd.exception.UnsupportedMathOperationException;
import br.com.sbrwgjd.math.SimpleMath;
import br.com.sbrwgjd.request.converters.NumberConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/math")
public class MathController {

    private SimpleMath math = new SimpleMath();

    @GetMapping("sum/{num1}/{num2}")
    public Double sum(
            @PathVariable("num1") String num1,
            @PathVariable("num2") String num2) throws IllegalArgumentException {

        if (!NumberConverter.isNumeric(num1) || !NumberConverter.isNumeric(num2)) {
            throw new UnsupportedMathOperationException("Please set a numeric value.");
        }

        return math.sum(NumberConverter.convertToDouble(num1), NumberConverter.convertToDouble(num2));
    }

    @GetMapping("subtract/{num1}/{num2}")
    public Double subtract(
            @PathVariable("num1") String num1,
            @PathVariable("num2") String num2) throws IllegalArgumentException {

        if (!NumberConverter.isNumeric(num1) || !NumberConverter.isNumeric(num2)) {
            throw new UnsupportedMathOperationException("Please set a numeric value.");
        }

        return math.subtract(NumberConverter.convertToDouble(num1), NumberConverter.convertToDouble(num2));
    }

    @GetMapping("multiply/{num1}/{num2}")
    public Double multiply(
            @PathVariable("num1") String num1,
            @PathVariable("num2") String num2) throws IllegalArgumentException {

        if (!NumberConverter.isNumeric(num1) || !NumberConverter.isNumeric(num2)) {
            throw new UnsupportedMathOperationException("Please set a numeric value.");
        }

        return math.multiply(NumberConverter.convertToDouble(num1), NumberConverter.convertToDouble(num2));
    }

    @GetMapping("divide/{num1}/{num2}")
    public Double divide(
            @PathVariable("num1") String num1,
            @PathVariable("num2") String num2) throws IllegalArgumentException {

        if (!NumberConverter.isNumeric(num1) || !NumberConverter.isNumeric(num2)) {
            throw new UnsupportedMathOperationException("Please set a numeric value.");
        }

        return math.divide(NumberConverter.convertToDouble(num1), NumberConverter.convertToDouble(num2));
    }

    @GetMapping("mean/{num1}/{num2}")
    public Double mean(
            @PathVariable("num1") String num1,
            @PathVariable("num2") String num2) throws IllegalArgumentException {

        if (!NumberConverter.isNumeric(num1) || !NumberConverter.isNumeric(num2)) {
            throw new UnsupportedMathOperationException("Please set a numeric value.");
        }

        return math.mean(NumberConverter.convertToDouble(num1), NumberConverter.convertToDouble(num2));
    }

    @GetMapping("squareRoot/{num}")
    public Double squareRoot(
            @PathVariable("num") String num) throws IllegalArgumentException {

        if (!NumberConverter.isNumeric(num)) {
            throw new UnsupportedMathOperationException("Please set a numeric value.");
        }

        return math.squareRoot(NumberConverter.convertToDouble(num));
    }


}
