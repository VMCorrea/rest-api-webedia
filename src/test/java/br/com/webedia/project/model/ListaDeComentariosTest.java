package br.com.webedia.project.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

public class ListaDeComentariosTest {

	private static List<Comentario> comentarios = new ArrayList<>();

	@BeforeClass
	public static void setUp() {

		for (int i = 0; i < 10; i++) {
			Comentario comentario = new Comentario();
			comentario.setId(Long.valueOf(i));
			comentario.setIdArtigo(5l);
			comentario.setUsuario("Usuario");
			comentario.setTexto("Texto");

			comentarios.add(comentario);
		}
	}

	@Test
	public void tamanhoDaLista() {
		assertEquals(10, comentarios.size());
	}

	@Test
	public void testaPrevUrl() {

		// Teste baseado na lista com 10 comentarios.

		// Cenário 1: Teste padrão
		ListaDeComentarios lista = new ListaDeComentarios(comentarios, 1, 4, null);
		assertEquals("http://localhost:8080/comentarios?page=3&size=1", lista.getPrev());

		// Cenário 2: Teste última página com menos elementos.
		lista = new ListaDeComentarios(comentarios, 4, 3, null);
		assertEquals("http://localhost:8080/comentarios?page=2&size=4", lista.getPrev());

		// Cenário 3: Teste primeira página.
		lista = new ListaDeComentarios(comentarios, 4, 1, null);
		assertEquals("", lista.getPrev());

		// Cenário 4: Teste com números maiores do que o normal.
		lista = new ListaDeComentarios(comentarios, 75, 50, null);
		assertEquals("", lista.getPrev());

		// Cenário 5: Teste com números negativos.
		lista = new ListaDeComentarios(comentarios, -10, -10, null);
		assertEquals("", lista.getPrev());

	}

	@Test
	public void testaPrevUrlComPermalink() {

		// Teste baseado na lista com 10 comentarios.

		// Cenário 1: Teste padrão
		ListaDeComentarios lista = new ListaDeComentarios(comentarios, 1, 4, "titulo-do-artigo");
		assertEquals("http://localhost:8080/artigos/titulo-do-artigo/comentarios?page=3&size=1", lista.getPrev());

		// Cenário 2: Teste última página com menos elementos.
		lista = new ListaDeComentarios(comentarios, 4, 3, "titulo-do-artigo");
		assertEquals("http://localhost:8080/artigos/titulo-do-artigo/comentarios?page=2&size=4", lista.getPrev());

		// Cenário 3: Teste primeira página.
		lista = new ListaDeComentarios(comentarios, 4, 1, "titulo-do-artigo");
		assertEquals("", lista.getPrev());

		// Cenário 4: Teste com números maiores do que o normal.
		lista = new ListaDeComentarios(comentarios, 75, 50, "titulo-do-artigo");
		assertEquals("", lista.getPrev());

		// Cenário 5: Teste com números negativos.
		lista = new ListaDeComentarios(comentarios, -10, -10, "titulo-do-artigo");
		assertEquals("", lista.getPrev());

	}

	@Test
	public void testaNextUrl() {

		// Teste baseado na lista com 10 artigos.

		// Cenário 1: Teste padrão
		ListaDeComentarios lista = new ListaDeComentarios(comentarios, 1, 4, null);
		assertEquals("http://localhost:8080/comentarios?page=5&size=1", lista.getNext());

		// Cenário 2: Teste última página.
		lista = new ListaDeComentarios(comentarios, 4, 3, null);
		assertEquals("", lista.getNext());

		// Cenário 3: Teste primeira página.
		lista = new ListaDeComentarios(comentarios, 4, 1, null);
		assertEquals("http://localhost:8080/comentarios?page=2&size=4", lista.getNext());

		// Cenário 4: Teste com números maiores do que o normal.
		lista = new ListaDeComentarios(comentarios, 75, 50, null);
		assertEquals("", lista.getNext());

		// Cenário 5: Teste com números negativos.
		lista = new ListaDeComentarios(comentarios, -10, -10, null);
		assertEquals("http://localhost:8080/comentarios?page=2&size=5", lista.getNext());

	}

	@Test
	public void testaNextUrlComPermalink() {

		// Teste baseado na lista com 10 artigos.

		// Cenário 1: Teste padrão
		ListaDeComentarios lista = new ListaDeComentarios(comentarios, 1, 4, "titulo-do-artigo");
		assertEquals("http://localhost:8080/artigos/titulo-do-artigo/comentarios?page=5&size=1", lista.getNext());

		// Cenário 2: Teste última página.
		lista = new ListaDeComentarios(comentarios, 4, 3, "titulo-do-artigo");
		assertEquals("", lista.getNext());

		// Cenário 3: Teste primeira página.
		lista = new ListaDeComentarios(comentarios, 4, 1, "titulo-do-artigo");
		assertEquals("http://localhost:8080/artigos/titulo-do-artigo/comentarios?page=2&size=4", lista.getNext());

		// Cenário 4: Teste com números maiores do que o normal.
		lista = new ListaDeComentarios(comentarios, 75, 50, "titulo-do-artigo");
		assertEquals("", lista.getNext());

		// Cenário 5: Teste com números negativos.
		lista = new ListaDeComentarios(comentarios, -10, -10, "titulo-do-artigo");
		assertEquals("http://localhost:8080/artigos/titulo-do-artigo/comentarios?page=2&size=5", lista.getNext());

	}

	@Test
	public void testaTransformacaoEmJson() {

		ListaDeComentarios lista = new ListaDeComentarios(comentarios, 5, 1, null);

		List<Comentario> comentariosDaLista = lista.getComentarios();

		assertEquals(Long.valueOf(1), comentariosDaLista.get(1).getId());

		String json = lista.toJson();

		ListaDeComentarios listaJson = new Gson().fromJson(json, ListaDeComentarios.class);

		List<Comentario> comentariosDaListaJson = listaJson.getComentarios();

		assertEquals(Long.valueOf(1), comentariosDaListaJson.get(1).getId());
	}
}
