package br.com.webedia.project.model;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;

public class Comentario {

	/* Atributos */
	private Long idComentario;
	private Long idArtigo;
	private String usuario;
	private String texto;
	private String data;

	/* Getters e Setters do idComentario */
	public Long getId() {
		return idComentario;
	}

	public void setId(Long id) {
		this.idComentario = id;
	}

	/* Getters e Setters do idArtigo */
	public Long getIdArtigo() {
		return idArtigo;
	}

	public void setIdArtigo(Long idArtigo) {
		this.idArtigo = idArtigo;
	}

	/* Getters e Setters do usuario */
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/* Getters e Setters do texto */
	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	/* Getters e Setters da data */
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
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
	 * Método que valida o comentário antes de ser cadastrado no banco de dados.
	 * <p>
	 * A validação utiliza as seguintes regras:
	 * </p>
	 * <ul>
	 * <li>ID do comentário deve ser nulo.</li>
	 * <li>idArtigo não pode ser nulo.</li>
	 * <li>Artigo precisa estar cadastrado no banco de dados.</li>
	 * <li>Usuário não pode ser nulo.</li>
	 * <li>Texto não pode ser nulo.</li>
	 * </ul>
	 * 
	 * @param dao Objeto do ArtigoDAO, usado para buscar o artigo relacionado.
	 * @return Retorna uma resposta HTTP 500 caso alguma regra seja quebrada, nulo
	 *         se estiver tudo certo.
	 */
	public Response validate() {

		if (this.idComentario != null)
			return Response.serverError().entity("ID do comentario é gerado automaticamente, e não deve ser enviado!")
					.build();
		else if (this.idArtigo == null)
			return Response.serverError().entity("O id do artigo não pode ser nulo!").build();
		else if (this.usuario == null)
			return Response.serverError().encoding("Comentário precisa de um usuário").build();
		else if (this.texto == null)
			return Response.serverError().encoding("Comentário sem conteúdo!").build();

		return null;
	}

	/**
	 * Método que recebe um novo comentario e mescla os atributos do comentario
	 * atual que estiverem nulos.
	 * 
	 * @param comentario Comentario com os campos que serão copiados.
	 */
	public void merge(Comentario comentario) {

		if (this.texto == null)
			this.texto = comentario.texto;
		if (this.usuario == null)
			this.usuario = comentario.usuario;

	}
}
