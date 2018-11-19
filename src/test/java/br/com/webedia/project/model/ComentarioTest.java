package br.com.webedia.project.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gson.Gson;

public class ComentarioTest {

	@Test
	public void testaValidateDeveDarNulo() {
		Comentario comentario = new Comentario();

		comentario.setIdArtigo(1l);
		comentario.setUsuario("Usuario");
		comentario.setTexto("Texto");

		assertTrue(comentario.validate() == null);

	}

	@Test
	public void testaValidateNaoDeveDarNulo() {
		Comentario comentario = new Comentario();

		comentario.setIdArtigo(1l);
		comentario.setUsuario("Usuario");
		comentario.setTexto("Texto");

		// Não deve ter ID
		comentario.setId(1l);
		assertFalse(comentario.validate() == null);

		// Id do artigo não deve ser nulo.
		comentario.setIdArtigo(null);
		comentario.setId(null);
		assertFalse(comentario.validate() == null);

		// Usuario não deve ser nulo.
		comentario.setIdArtigo(1l);
		comentario.setUsuario(null);
		assertFalse(comentario.validate() == null);

		// Texto não deve ser nulo
		comentario.setTexto(null);
		comentario.setUsuario("Usuario");
		assertFalse(comentario.validate() == null);

	}

	@Test
	public void testaMergeDeComentarios() {
		Comentario comentario1 = new Comentario();

		Comentario comentario2 = new Comentario();
		comentario2.setTexto("Texto 2");
		comentario2.setUsuario("Usuario 2");

		comentario1.merge(comentario2);

		assertEquals("Texto 2", comentario1.getTexto());
		assertEquals("Usuario 2", comentario1.getUsuario());

		comentario1.setTexto("Texto 1");
		comentario2.setUsuario("Usuario 1");

	}

	@Test
	public void testaTransformacaoEmJson() {

		Comentario comentario = new Comentario();
		comentario.setId(1l);
		comentario.setIdArtigo(2l);
		comentario.setTexto("Texto 2");
		comentario.setUsuario("Usuario 2");

		String json = comentario.toJson();

		Comentario comentarioDoJson = new Gson().fromJson(json, Comentario.class);

		assertEquals("Texto 2", comentario.getTexto());

	}
}
