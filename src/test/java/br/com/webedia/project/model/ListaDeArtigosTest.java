package br.com.webedia.project.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

public class ListaDeArtigosTest {

	private static List<Artigo> artigos = new ArrayList<>();

	@BeforeClass
	public static void setUp() {

		for (int i = 0; i < 10; i++) {
			Artigo artigo = new Artigo();
			artigo.setId(Long.valueOf(i));
			artigo.setTitulo("Título do Artigo - Parte " + (i + 1));
			artigo.setSubtitulo("Subtítulo do Artigo");
			artigo.setConteudo("Conteúdo");
			artigo.generatePermalink();
			artigos.add(artigo);
		}

	}

	@Test
	public void tamanhoDaLista() {
		assertEquals(10, artigos.size());
	}

	@Test
	public void testaPrevUrl() {

		// Teste baseado na lista com 10 artigos.

		// Cenário 1: Teste padrão
		ListaDeArtigos lista = new ListaDeArtigos(artigos, 1, 4);
		assertEquals("http://localhost:8080/artigos?page=3&size=1", lista.getPrev());

		// Cenário 2: Teste última página com menos elementos.
		lista = new ListaDeArtigos(artigos, 4, 3);
		assertEquals("http://localhost:8080/artigos?page=2&size=4", lista.getPrev());

		// Cenário 3: Teste primeira página.
		lista = new ListaDeArtigos(artigos, 4, 1);
		assertEquals("", lista.getPrev());

		// Cenário 4: Teste com números maiores do que o normal.
		lista = new ListaDeArtigos(artigos, 75, 50);
		assertEquals("", lista.getPrev());

		// Cenário 5: Teste com números negativos.
		lista = new ListaDeArtigos(artigos, -10, -10);
		assertEquals("", lista.getPrev());

	}

	@Test
	public void testaNextUrl() {

		// Teste baseado na lista com 10 artigos.

		// Cenário 1: Teste padrão
		ListaDeArtigos lista = new ListaDeArtigos(artigos, 1, 4);
		assertEquals("http://localhost:8080/artigos?page=5&size=1", lista.getNext());

		// Cenário 2: Teste última página.
		lista = new ListaDeArtigos(artigos, 4, 3);
		assertEquals("", lista.getNext());

		// Cenário 3: Teste primeira página.
		lista = new ListaDeArtigos(artigos, 4, 1);
		assertEquals("http://localhost:8080/artigos?page=2&size=4", lista.getNext());

		// Cenário 4: Teste com números maiores do que o normal.
		lista = new ListaDeArtigos(artigos, 75, 50);
		assertEquals("", lista.getNext());

		// Cenário 5: Teste com números negativos.
		lista = new ListaDeArtigos(artigos, -10, -10);
		assertEquals("http://localhost:8080/artigos?page=2&size=5", lista.getNext());

	}

	@Test
	public void testaTransformacaoEmJson() {

		ListaDeArtigos lista = new ListaDeArtigos(artigos, 5, 1);

		List<Artigo> artigosDaLista = lista.getArtigos();

		assertEquals(Long.valueOf(1), artigosDaLista.get(1).getId());

		String json = lista.toJson();

		ListaDeArtigos listaJson = new Gson().fromJson(json, ListaDeArtigos.class);

		List<Artigo> artigosDaListaJson = listaJson.getArtigos();

		assertEquals(Long.valueOf(1), artigosDaListaJson.get(1).getId());
	}

}
