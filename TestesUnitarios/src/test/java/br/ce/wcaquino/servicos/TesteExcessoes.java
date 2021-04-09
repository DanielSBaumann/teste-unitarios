package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class TesteExcessoes {

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * Elegante
     */
    @Test(expected = FilmeSemEstoqueException.class)
    public void filme_sem_estoque() throws Exception {

        //  cenario
        LocacaoService service = new LocacaoService();

        Usuario usuario = new Usuario("Daniel Baumann");
        Filme filme = new Filme("It a coisa", 0, 7.5);

        //  ação
        Locacao locacao = service.alugarFilme(usuario, filme);
        System.out.println("Forma elegante");
    }

    /**
     * Robusta
     */
    @Test
    public void teste_usuario_vazio() throws FilmeSemEstoqueException {

        //  cenario
        LocacaoService service = new LocacaoService();

        //Usuario usuario = new Usuario("Daniel Baumann");
        Filme filme = new Filme("It a coisa", 1, 7.5);

        //  ação
        try {
            Locacao locacao = service.alugarFilme(null, filme);
            fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuario vazio"));
        }
        System.out.println("Forma robusta");
    }

    /**
     * Nova
     */
    @Test
    public void teste_filme_vazio() throws FilmeSemEstoqueException, LocadoraException {

        //  cenario
        LocacaoService service = new LocacaoService();

        Usuario usuario = new Usuario("Daniel Baumann");

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        //  ação
        service.alugarFilme(usuario, null);
        System.out.println("Forma nova");
    }

}
