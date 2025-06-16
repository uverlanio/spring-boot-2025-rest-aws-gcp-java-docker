package br.com.sbrwgjd.math;

public class SimpleMath {

    public Double sum(Double num1, Double num2) {

        return num1 + num2;
    }

    public Double subtract(Double num1, Double num2) {

        return num1 - num2;
    }

    public Double multiply(Double num1, Double num2) {
        return num1 * num2;
    }

    public Double divide(Double num1, Double num2) {
        return num1 / num2;
    }

    public Double mean(Double num1, Double num2) {
        return sum(num1, num2) / 2;
    }

    public Double squareRoot(Double num) {
        return Math.sqrt(num);
    }
}
