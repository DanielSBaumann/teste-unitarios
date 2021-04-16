package br.ce.wcaquino.servicos;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CalculadoraMockTest {

    @Test
    public void test() {
        Calculadora calc = mock(Calculadora.class);

        ArgumentCaptor<Integer> argCapt = ArgumentCaptor
                .forClass(Integer.class);

        when(calc.somar(argCapt.capture(), argCapt.capture()))
                .thenReturn(5);

        assertEquals(5,calc.somar(1,100));

        System.out.println(argCapt.getAllValues());
    }
}
