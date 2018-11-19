package br.com.webedia.project.model;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;

/**
 * <h1>Autor</h1>
 * <p>
 * Classe com os métodos e atributos relacionados aos autores.
 * 
 * @author Victor Corrêa
 *
 */
public class Autor {

	/* Atributos */
	private Long idAutor;
	private String nome;
	private String sobrenome;
	private String bio;

	/* Getters e Setters do idAutor */
	public Long getId() {
		return idAutor;
	}

	public void setId(Long id) {
		this.idAutor = id;
	}

	/* Getters e Setters do nome */
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	/* Getters e Setters do sobrenome */
	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	/* Getters e Setters da bio */
	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	/**
	 * Método que transforma o objeto desta classe em uma string no formato Json.
	 * 
	 * @return String em formato Json.
	 */
	public String toJson() {
		return new Gson().toJson(this);
	}

	/**
	 * Método que valida o autor antes de ser cadastrado no banco de dados.
	 * <p>
	 * A validação utiliza as seguintes regras:
	 * </p>
	 * <ul>
	 * <li>ID do autor deve ser nulo.</li>
	 * <li>Nome não pode ser nulo.</li>
	 * <li>Sobrenome não pode ser nulo.</li>
	 * </ul>
	 * 
	 * @return Retorna uma resposta HTTP 500 caso alguma regra seja quebrada, nulo
	 *         se estiver tudo certo.
	 */
	public Response validate() {

		if (this.getId() != null)
			return Response.serverError().entity("Cliente nao pode passar o id!").build();
		else if (this.getNome() == null)
			return Response.serverError().entity("Nome não pode ser nulo").build();
		else if (this.getSobrenome() == null)
			return Response.serverError().entity("Sobrenome não pode ser nulo").build();

		return null;
	}

	/**
	 * Método que recebe um novo autor e mescla os atributos do autor atual que
	 * estiverem nulos.
	 * 
	 * @param autor Autor com os campos que serão copiados.
	 */
	public void merge(Autor autor) {

		if (this.nome == null)
			this.nome = autor.nome;
		if (this.sobrenome == null)
			this.sobrenome = autor.sobrenome;
		if (this.bio == null)
			this.bio = autor.bio;

	}

}
