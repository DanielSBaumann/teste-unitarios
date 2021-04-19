package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.reflect.Whitebox;

import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.builder.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builder.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static br.ce.wcaquino.utils.DataUtils.*;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.whenNew;

public class LocacaoServiceTestPowerMock {

    @InjectMocks
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
        service = spy(service);
    }

    @Test
    public void deveAlugarFilme() throws Exception {

//        assumeFalse(verificarDiaSemana(new Date(), SATURDAY));

        //  cenario
        Usuario usuario = umUsuario()
                .agora();

        List<Filme> filmes = asList(umFilme()
                .comValor(5.0)
                .agora());

        whenNew(Date.class)
                .withNoArguments()
                .thenReturn(obterData(28, 4, 2017));

        //  ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //  verificação
        error
                .checkThat(
                        locacao.getValor(),
                        is(equalTo(5.0))
                );

        error
                .checkThat(isMesmaData(
                        locacao.getDataLocacao(),
                        new Date()),
                        is(true)
                );

        error
                .checkThat(isMesmaData(
                        locacao.getDataRetorno(),
                        obterDataComDiferencaDias(1)),
                        is(true)
                );
    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {

//        assumeTrue(verificarDiaSemana(new Date(), SATURDAY));

        //cenario
        Usuario usuario = umUsuario()
                .agora();
        List<Filme> filmes = asList(umFilme()
                .agora());

        whenNew(Date.class)
                .withNoArguments()
                .thenReturn(obterData(29, 4, 2017));

        //acao
        Locacao retorno = service.alugarFilme(usuario, filmes);
        assertThat(retorno.getDataRetorno(), caiNumaSegunda());
    }

    @Test
    public void deveCalcularValorLocacao() throws Exception {
        //cenario
        Usuario usuario = umUsuario()
                .agora();

        List<Filme> filmes = asList(umFilme()
                .agora());

        //acao
        Double valor = (Double) Whitebox
                .invokeMethod(service, "calcularValorLocacao", filmes);

        //verificacao
        assertThat(valor, is(4.0));

    }

//    @Test
//    public void deveAlugarFilmeSemCalcularValor() throws Exception {
//        //cenario
//        Usuario usuario = umUsuario()
//                .agora();
//
//        List<Filme> filmes = asList(umFilme()
//                .agora());
//
//        PowerMockito
//                .doReturn(1.0)
//                .when(service,
//                        "calcularValorLocacao",
//                        filmes);
//
//        //acao
//        Locacao locacao = service
//                .alugarFilme(usuario, filmes);
//
//        //verificacao
//        assertThat(
//                locacao.getValor(),
//                is(1.0)
//        );
//
//        PowerMockito
//                .verifyPrivate(service)
//                .invoke("calcularValorLocacao", filmes);
//
//    }

}
