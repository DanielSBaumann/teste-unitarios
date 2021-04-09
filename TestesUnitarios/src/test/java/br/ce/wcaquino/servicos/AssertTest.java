package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Usuario;
import org.junit.Assert;
import org.junit.Test;

public class AssertTest {

    @Test
    public void test() {

        Assert.assertTrue(true);
        Assert.assertFalse(false);

        Assert.assertEquals(1, 1);
        Assert.assertEquals(0.51234, 0.512, 0.001);
        Assert.assertEquals(Math.PI, 3.14, 0.01);

        Assert.assertEquals("bola", "bola");
        //Assert.assertEquals("bola","Bola");
        Assert.assertTrue("bola".equalsIgnoreCase("Bola"));

        Usuario u1 = new Usuario("Usuario 1");
        Usuario u2 = new Usuario("Usuario 1");
        Usuario u3 = null;

        //Assert.assertEquals("Erro de comparação", u1, u3);
        Assert.assertEquals(u1, u2);
        Assert.assertEquals(u1, u1);
        //Assert.assertSame(u2,u3);

        Assert.assertTrue(u3 == null);
        Assert.assertNull(u3);
    }
}
