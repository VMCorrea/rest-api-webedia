package br.com.webedia.project.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

public class ListaDeAutoresTest {

	private static List<Autor> autores = new ArrayList<>();

	@BeforeClass
	public static void setUp() {

		for (int i = 0; i < 10; i++) {
			Autor autor = new Autor();
			autor.setId(Long.valueOf(i));
			autor.setNome("Nome");
			autor.setSobrenome("Sobrenome");
			autor.setBio("Biografia");

			autores.add(autor);
		}
	}

	@Test
	public void tamanhoDaLista() {
		assertEquals(10, autores.size());
	}

	@Test
	public void testaPrevUrl() {
		// Teste baseado na lista com 10 autores.

		// Cenário 1: Teste padrão
		ListaDeAutores lista = new ListaDeAutores(autores, 1, 4);
		assertEquals("http://localhost:8080/autores?page=3&size=1", lista.getPrev());

		// Cenário 2: Teste última página com menos elementos.
		lista = new ListaDeAutores(autores, 4, 3);
		assertEquals("http://localhost:8080/autores?page=2&size=4", lista.getPrev());

		// Cenário 3: Teste primeira página.
		lista = new ListaDeAutores(autores, 4, 1);
		assertEquals("", lista.getPrev());

		// Cenário 4: Teste com números maiores do que o normal.
		lista = new ListaDeAutores(autores, 75, 50);
		assertEquals("", lista.getPrev());

		// Cenário 5: Teste com números negativos.
		lista = new ListaDeAutores(autores, -10, -10);
		assertEquals("", lista.getPrev());
	}

	@Test
	public void testaNextUrl() {

		// Teste baseado na lista com 10 autores.

		// Cenário 1: Teste padrão
		ListaDeAutores lista = new ListaDeAutores(autores, 1, 4);
		assertEquals("http://localhost:8080/autores?page=5&size=1", lista.getNext());

		// Cenário 2: Teste última página.
		lista = new ListaDeAutores(autores, 4, 3);
		assertEquals("", lista.getNext());

		// Cenário 3: Teste primeira página.
		lista = new ListaDeAutores(autores, 4, 1);
		assertEquals("http://localhost:8080/autores?page=2&size=4", lista.getNext());

		// Cenário 4: Teste com números maiores do que o normal.
		lista = new ListaDeAutores(autores, 75, 50);
		assertEquals("", lista.getNext());

		// Cenário 5: Teste com números negativos.
		lista = new ListaDeAutores(autores, -10, -10);
		assertEquals("http://localhost:8080/autores?page=2&size=5", lista.getNext());

	}
	
	@Test
	public void testaTransformacaoEmJson() {

		ListaDeAutores lista = new ListaDeAutores(autores, 5, 1);

		List<Autor> artigosDaLista = lista.getAutores();

		assertEquals(Long.valueOf(1), artigosDaLista.get(1).getId());

		String json = lista.toJson();

		ListaDeAutores listaJson = new Gson().fromJson(json, ListaDeAutores.class);

		List<Autor> artigosDaListaJson = listaJson.getAutores();

		assertEquals(Long.valueOf(1), artigosDaListaJson.get(1).getId());
	}

}
