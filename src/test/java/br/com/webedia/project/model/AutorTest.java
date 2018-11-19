package br.com.webedia.project.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gson.Gson;

public class AutorTest {

	@Test
	public void testaValidateDeveSerNulo() {

		Autor autor = new Autor();

		// Id deve ser nulo
		autor.setId(null);

		// Deve ter Nome
		autor.setNome("Nome");

		// Deve ter Sobrenome
		autor.setSobrenome("Sobrenome");

		assertTrue(autor.validate() == null);

	}

	@Test
	public void testaValidateNaoDeveSerNulo() {

		Autor autor = new Autor();

		autor.setId(10l);
		autor.setNome("Nome");
		autor.setSobrenome("Sobrenome");

		// Deve dar falso por causa do ID
		assertFalse(autor.validate() == null);

		autor.setId(null);
		autor.setNome(null);

		// Deve dar false por causa do nome
		assertFalse(autor.validate() == null);

		autor.setNome("Nome");
		autor.setSobrenome(null);

		// Deve dar false por causa do sobrenome
		assertFalse(autor.validate() == null);

	}

	@Test
	public void testaMergeDeAutores() {

		Autor autor1 = new Autor();

		Autor autor2 = new Autor();
		autor2.setNome("Nome 2");
		autor2.setSobrenome("Sobrenome 2");
		autor2.setBio("Biografia 2");

		autor1.merge(autor2);

		assertEquals("Nome 2", autor1.getNome());
		assertEquals("Sobrenome 2", autor1.getSobrenome());
		assertEquals("Biografia 2", autor1.getBio());

		autor1.setNome("Nome 1");
		autor1.setSobrenome("Sobrenome 1");
		autor1.setBio("Biografia 1");

		autor1.merge(autor2);

		assertEquals("Nome 1", autor1.getNome());
		assertEquals("Sobrenome 1", autor1.getSobrenome());
		assertEquals("Biografia 1", autor1.getBio());

	}

	@Test
	public void testaTransformacaoEmJson() {

		Autor autor = new Autor();
		autor.setNome("Nome");
		autor.setSobrenome("Sobrenome");
		autor.setBio("Biografia");

		String json = autor.toJson();

		Autor autorDoJson = new Gson().fromJson(json, Autor.class);

		assertEquals("Nome", autorDoJson.getNome());
		assertEquals("Sobrenome", autorDoJson.getSobrenome());
		assertEquals("Biografia", autorDoJson.getBio());

	}
}
