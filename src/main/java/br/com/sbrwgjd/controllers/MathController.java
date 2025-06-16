package br.com.sbrwgjd.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/math")
public class MathController {

    @GetMapping("subtract/{num1}/{num2}")
    public Double subtract(
            @PathVariable("num1") String num1,
            @PathVariable("num2") String num2) throws IllegalArgumentException {

        if (!isNumeric(num1) || !isNumeric(num2)) {
            throw new IllegalArgumentException();
        }

        return convertToDouble(num1) - convertToDouble(num2);
    }

    @GetMapping("multiply/{num1}/{num2}")
    public Double multiply(
            @PathVariable("num1") String num1,
            @PathVariable("num2") String num2) throws IllegalArgumentException {

        if (!isNumeric(num1) || !isNumeric(num2)) {
            throw new IllegalArgumentException();
        }

        return convertToDouble(num1) * convertToDouble(num2);
    }

    @GetMapping("divide/{num1}/{num2}")
    public Double divide(
            @PathVariable("num1") String num1,
            @PathVariable("num2") String num2) throws IllegalArgumentException {

        if (!isNumeric(num1) || !isNumeric(num2)) {
            throw new IllegalArgumentException();
        }

        return convertToDouble(num1) / convertToDouble(num2);
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
