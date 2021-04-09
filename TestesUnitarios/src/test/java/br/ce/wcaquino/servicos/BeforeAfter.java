package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class BeforeAfter {

    private LocacaoService service;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

//    @BeforeClass
//    public static void setupClass() {
//        System.out.println("Inicializando classe");
//    }

    @Before
    public void setup() {
        service = new LocacaoService();
    }

//    @After
//    public void tearDown() {
//        System.out.println("After");
//    }

//    @AfterClass
//    public static void tearDownClass() {
//        System.out.println("Finalizando classe");
//    }

    /**
     * Elegante
     */
    @Test(expected = FilmeSemEstoqueException.class)
    public void filme_sem_estoque() throws Exception {

        //  cenario

        Usuario usuario = new Usuario("Daniel Baumann");
        Filme filme = new Filme("It a coisa", 0, 7.5);

        //  ação
        Locacao locacao = service.alugarFilme(usuario, filme);
    }

    /**
     * Robusta
     */
    @Test
    public void teste_usuario_vazio() throws FilmeSemEstoqueException {

        //  cenario

        //Usuario usuario = new Usuario("Daniel Baumann");
        Filme filme = new Filme("It a coisa", 1, 7.5);

        //  ação
        try {
            service.alugarFilme(null, filme);
            fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuario vazio"));
        }
    }

    /**
     * Nova
     */
    @Test
    public void teste_filme_vazio() throws FilmeSemEstoqueException, LocadoraException {

        //  cenario
        Usuario usuario = new Usuario("Daniel Baumann");

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        //  ação
        service.alugarFilme(usuario, null);
    }
}
