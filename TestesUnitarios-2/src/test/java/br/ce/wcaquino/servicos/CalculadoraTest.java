package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import br.ce.wcaquino.runners.ParallelRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(ParallelRunner.class)
public class CalculadoraTest {

    private Calculadora calc;

    @Before
    public void setup() {
        calc = new Calculadora();
    }

    @Test
    public void deveSomarDoisValores() {
        //  cenario
        int a = 5;
        int b = 3;

        //  ação
        int resultado = calc.somar(a, b);

        //  verificação
        assertEquals(8, resultado);
    }

    @Test
    public void deveSubtrairDoisValores() {
        //  cenario
        int a = 5;
        int b = 3;

        //  ação
        int resultado = calc.subtrair(a, b);

        //  verificação
        assertEquals(2, resultado);
    }

    @Test
    public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {

        //  cenario
        int a = 4;
        int b = 2;

        //  ação
        int resultado = calc.dividir(a, b);

        //  verificação
        assertEquals(2, resultado);

    }

    @Test(expected = NaoPodeDividirPorZeroException.class)
    public void deveLancarExcessaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {

        //  cenario
        int a = 10;
        int b = 0;

        //  ação
        calc.dividir(a, b);
    }

    @Test
    public void deveDividir() {

        String a = "4";
        String b = "2";

        int resultado = calc.divide(a, b);

        assertEquals(2, resultado);
    }
}
