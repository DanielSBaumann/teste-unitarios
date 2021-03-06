package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.runners.ParallelRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.lang.reflect.Method;
import java.util.List;

import static br.ce.wcaquino.builder.FilmeBuilder.filmeSemEstoque;
import static br.ce.wcaquino.builder.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builder.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.builder.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.*;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterData;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(ParallelRunner.class)
public class LocacaoServiceTest {

    @InjectMocks
    @Spy
    private LocacaoService service;

    @Mock
    private SPCService spc;

    @Mock
    private LocacaoDAO dao;

    @Mock
    private EmailService emailService;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void deveAlugarFilme() throws Exception {

        //  cenario
        Usuario usuario = umUsuario()
                .agora();

        List<Filme> filmes = asList(umFilme()
                .comValor(5.0)
                .agora());

        doReturn(obterData(28, 4, 2017))
                .when(service).obterData();

        //  a????o
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //  verifica????o
        error
                .checkThat(
                        locacao.getValor(),
                        is(equalTo(5.0))
                );

        error
                .checkThat(isMesmaData(
                        locacao.getDataLocacao(),
                        obterData(28, 4, 2017)),
                        is(true)
                );

        error
                .checkThat(isMesmaData(
                        locacao.getDataRetorno(),
                        obterData(29, 4, 2017)),
                        is(true)
                );
    }

    /**
     * Elegante
     */
    @Test(expected = FilmeSemEstoqueException.class)
    public void naoDeveAlugarFilmeSemEstoque() throws Exception {

        //  cenario

        Usuario usuario = umUsuario()
                .agora();
        List<Filme> filmes = asList(filmeSemEstoque()
                .agora());

        //  a????o
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //  verifica????o
        error
                .checkThat(
                        locacao
                                .getValor(),
                        is(equalTo(7.5)));

        error
                .checkThat(locacao.getDataLocacao(), hoje());

        error
                .checkThat(locacao.getDataLocacao(), hojeComDiferencaDias(1));
    }

    /**
     * Robusta
     */
    @Test
    public void naoDeveALugarFilmeSemUsuario() throws FilmeSemEstoqueException {

        //  cenario
        LocacaoService service = new LocacaoService();

        //Usuario usuario = new Usuario("Daniel Baumann");
        List<Filme> filmes = asList(umFilme()
                .agora());

        //  a????o
        try {
            Locacao locacao = service.alugarFilme(null, filmes);
            fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuario vazio"));
        }
    }

    /**
     * Nova
     */
    @Test
    public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {

        //  cenario
        LocacaoService service = new LocacaoService();

        Usuario usuario = umUsuario()
                .agora();

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        //  a????o
        service.alugarFilme(usuario, null);
        System.out.println("Forma nova");
    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {
        //cenario
        Usuario usuario = umUsuario()
                .agora();
        List<Filme> filmes = asList(umFilme()
                .agora());

        doReturn(obterData(29, 4, 2017))
                .when(service).obterData();

        //acao
        Locacao retorno = service.alugarFilme(usuario, filmes);
        assertThat(retorno.getDataRetorno(), caiNumaSegunda());
    }

    @Test
    public void naoDeveALugarFilmeNegativadoSPC() throws Exception {
        //cenario
        Usuario usuario = umUsuario()
                .agora();
        List<Filme> filmes = asList(umFilme()
                .agora());

        when(spc.possuiNegativacao(any(Usuario.class)))
                .thenReturn(true);

        //acao
        try {
            service.alugarFilme(usuario, filmes);
            fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuario negativado!"));
        }

        //verificacao
        verify(spc)
                .possuiNegativacao(usuario);
    }

    @Test
    public void deveEnviarEmailParaLocacoesAtrasadas() {

        //cenario
        Usuario usuario = umUsuario()
                .agora();

        Usuario usuario2 = umUsuario()
                .comNome("Usuario em dia")
                .agora();

        Usuario usuario3 = umUsuario()
                .comNome("Outro atrasado")
                .agora();

        List<Locacao> locacoes = asList(
                umLocacao()
                        .comUsuario(usuario)
                        .atrasado()
                        .agora(),
//                umLocacao()
//                        .comUsuario(usuario2)
//                        .agora(),
                umLocacao()
                        .comUsuario(usuario3)
                        .atrasado()
                        .agora(),
                umLocacao()
                        .comUsuario(usuario3)
                        .atrasado()
                        .agora()
        );

        when(dao.obterLocacoesPendentes())
                .thenReturn(locacoes);

        //acao
        service.notificarAtrasos();

        //verificacao
        verify(emailService, times(3))
                .notificarAtraso(any(Usuario.class));

//        verify(emailService)
//                .notificarAtraso(usuario);
//
//        verify(emailService, never())
//                .notificarAtraso(usuario2);
//
//        verify(emailService)
//                .notificarAtraso(usuario3);

        verifyNoMoreInteractions(emailService);

        verifyZeroInteractions(spc);
    }

    @Test
    public void deveTratarErroNoSPC() throws Exception {
        //cenario
        Usuario usuario = umUsuario()
                .agora();

        List<Filme> filmes = asList(umFilme()
                .agora());

        when(spc.possuiNegativacao(usuario))
                .thenThrow(new Exception("Falha catastr??fica"));

        exception
                .expect(LocadoraException.class);

        exception
                .expectMessage("Problemas com SPC, tente novamente");

        //acao
        service.alugarFilme(usuario, filmes);

        //verificacao

    }

    @Test
    public void deveProrrogarUmaLocacao() {
        //cenario
        Locacao locacao = umLocacao().agora();

        //acao
        service.prorrogarLocacao(locacao, 3);

        //verificacao
        ArgumentCaptor<Locacao> argCapt = ArgumentCaptor
                .forClass(Locacao.class);

        verify(dao)
                .salvar(argCapt.capture());

        Locacao locacaoRetornada = argCapt
                .getValue();

        error.checkThat(
                locacaoRetornada.getValor(),
                is(12.0)
        );

        error.checkThat(
                locacaoRetornada.getDataLocacao(),
                hoje()
        );

        error.checkThat(
                locacaoRetornada.getDataRetorno(),
                hojeComDiferencaDias(3)
        );
    }

    @Test
    public void deveCalcularValorLocacao() throws Exception {
        //cenario
        Usuario usuario = umUsuario()
                .agora();

        List<Filme> filmes = asList(umFilme()
                .agora());

        //acao

        Class<LocacaoService> clazz = LocacaoService.class;
        Method metodo = clazz.getDeclaredMethod("calcularValorLocacao", List.class);
        metodo.setAccessible(true);

        Double valor = (Double) metodo
                .invoke(service, filmes);

        //verificacao
        assertThat(valor, is(4.0));

    }

}
