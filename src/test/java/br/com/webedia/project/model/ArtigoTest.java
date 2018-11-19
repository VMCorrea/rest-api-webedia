package br.com.webedia.project.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArtigoTest {

	@Test
	public void testaValidateDeveSerNulo() {
		Artigo artigo = new Artigo();

		Autor autor = new Autor();
		autor.setId(1l);

		// Artigo precisa de pelo menos um autor.
		artigo.addAutor(autor);

		// Artigo precisa ter título.
		artigo.setTitulo("Título Qualquer");

		// Artigo precisa ter subtítulo
		artigo.setSubtitulo("Subtítulo Qualquer");

		// Artigo precisa ter conteúdo
		artigo.setConteudo("Conteúdo do Artigo");

		// Permalink e ID devem ser nulos
		// Não pode ter comentário.

		assertTrue(artigo.validate() == null);

	}

	@Test
	public void testaGeneratePermalink() {
		Artigo artigo = new Artigo();
		artigo.setTitulo("Título Artigo - Parte 1");
		artigo.generatePermalink();

		assertEquals("titulo-artigo-parte-1", artigo.getPermalink());
	}

}
