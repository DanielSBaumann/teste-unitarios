package br.ce.wcaquino.servicos;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrdemDeExecucao {

    public static int count = 0;

    @Test
    public void inicia() {
        count = 1;
    }

    @Test
    public void verifica() {
        assertEquals(1, count);
    }

//    @Test
//    public void geral() {
//        inicia();
//        verifica();
//    }
}
