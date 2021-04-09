package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class LocacaoServiceTest {

    @Test
    public void test() {
        //  cenario
        LocacaoService service = new LocacaoService();

        Usuario usuario = new Usuario("Daniel Baumann");
        Filme filme = new Filme("It a coisa", 6, 7.5);

        //  ação
        Locacao locacao = null;
        try {

            locacao = service.alugarFilme(usuario, filme);

            //  verificação
            Assert.assertTrue(locacao.getFilme().getNome().toLowerCase().equals("it a coisa"));
            Assert.assertEquals(7.5, locacao.getValor(), 0.01);
            Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
            Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
