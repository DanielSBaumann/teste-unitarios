package br.ce.wcaquino.servicos;

import org.junit.Test;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CalculadoraMockTest {

    @Test
    public void test() {
        Calculadora calc = mock(Calculadora.class);
        when(calc.somar(eq(1), anyInt()))
                .thenReturn(5);
        System.out.println(calc.somar(1, 8));
    }
}
