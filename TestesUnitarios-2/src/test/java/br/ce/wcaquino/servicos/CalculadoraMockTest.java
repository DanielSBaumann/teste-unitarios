package br.ce.wcaquino.servicos;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class CalculadoraMockTest {

    @Mock
    private Calculadora calcMock;

    @Spy
    private Calculadora spy;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void devoMostrarEntreMockESpy() {
        when(calcMock.somar(1, 2))
                .thenReturn(3);

        when(spy.somar(1, 2))
                .thenReturn(3);

//        doNothing()
//                .when(spy)
//                .imprime();

        System.out.println("Mock " + calcMock.somar(1, 5));

        System.out.println("Spy " + spy.somar(1, 5));

        System.out.println("Mock");
        calcMock.imprime();

        System.out.println("Spy");
        spy.imprime();
    }

    @Test
    public void test() {
        Calculadora calc = mock(Calculadora.class);

        ArgumentCaptor<Integer> argCapt = ArgumentCaptor
                .forClass(Integer.class);

        when(calc.somar(argCapt.capture(), argCapt.capture()))
                .thenReturn(5);

        assertEquals(5, calc.somar(1, 100));

        System.out.println(argCapt.getAllValues());
    }


}
