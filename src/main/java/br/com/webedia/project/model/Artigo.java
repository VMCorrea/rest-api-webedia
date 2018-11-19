package br.com.webedia.project.model;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import br.com.webedia.project.dao.AutorDAO;

/**
 * <h1>Artigo</h1>
 * <p>
 * Classe com os métodos e atributos relacionados aos artigos.
 * 
 * @author Victor Corrêa
 *
 */
public class Artigo {

	/* Atributos */
	private Long idArtigo;
	private String permalink;
	private String titulo;
	private String subtitulo;
	private String dataPublicacao;
	private String dataAtualizacao;
	private String conteudo;
	private List<Autor> autores = new ArrayList<>();
	private List<Comentario> comentarios = new ArrayList<>();

	public Artigo() {

	}

	/* Getters e Setters do idArtigo */
	public Long getId() {
		return idArtigo;
	}

	public void setId(Long id) {
		this.idArtigo = id;
	}

	/* Getters e Setters do titulo */
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/* Getters e Setters do subtitulo */
	public String getSubtitulo() {
		return subtitulo;
	}

	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}

	/* Getters e Setters da dataPublicacao */
	public String getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(String dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	/* Getters e Setters da dataAtualizacao */
	public String getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(String dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	/* Getters e Setters do conteudo */
	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	/**
	 * @return Retorna uma lista não modificável de autores.
	 */
	public List<Autor> getAutores() {
		return Collections.unmodifiableList(this.autores);
	}

	/**
	 * @return Retorna uma lista não modíficável de comentários.
	 */
	public List<Comentario> getComentarios() {
		return Collections.unmodifiableList(this.comentarios);
	}

	/* Getters e Setters do permalink */
	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	public String getPermalink() {
		return permalink;
	}

	/**
	 * Método que adiciona um comentário na lista do artigo.
	 * 
	 * @param comentario Comentário que será adicionado.
	 */
	public void addComentario(Comentario comentario) {
		this.comentarios.add(comentario);
	}

	/**
	 * Método que adiciona um autor na lista do artigo.
	 * 
	 * @param autor Autor que será adicionado.
	 */
	public void addAutor(Autor autor) {
		this.autores.add(autor);
	}

	/**
	 * Método que gera o permalink, baseado no título do artigo formatado.
	 */
	public void generatePermalink() {

		// Remove espaços e múltiplos '-' por apenas um '-', depois transforma tudo em
		// minúsculas.
		this.permalink = this.titulo.replaceAll("-", " ").replaceAll("\\s+", "-").toLowerCase();

		// Remove acentos e caracteres especiais, "normalizando-os".
		this.permalink = Normalizer.normalize(permalink, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
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
	 * Método que valida o artigo antes de ser cadastrado no banco de dados.
	 * <p>
	 * A validação utiliza as seguintes regras:
	 * </p>
	 * <ul>
	 * <li>ID do artigo deve ser nulo.</li>
	 * <li>Título não pode ser nulo.</li>
	 * <li>Subtítulo não pode ser nulo.</li>
	 * <li>O permalink deve ser nulo.</li>
	 * <li>Deve ter pelo menos um autor.</li>
	 * <li>Os autores da lista precisam ter id.</li>
	 * <li>Os autores da lista precisam estar cadastrados no banco de dados.</li>
	 * <li>O conteúdo do artigo não pode ser vazio.</li>
	 * <li>Artigos não podem ser criados com comentários.</li>
	 * </ul>
	 * 
	 * @return Retorna uma resposta HTTP 500 caso alguma regra seja quebrada, nulo
	 *         se estiver tudo certo.
	 */
	public Response validate() {

		if (this.getId() != null)
			return Response.serverError().entity("ID do artigo é gerado automaticamente, e não deve ser enviado!")
					.build();
		else if (this.getTitulo() == null)
			return Response.serverError().entity("Título não pode ser nulo!").build();
		else if (this.getSubtitulo() == null)
			return Response.serverError().entity("Subtítulo não pode ser nulo!").build();
		else if (this.permalink != null)
			return Response.serverError().entity("Permalink é gerado automaticamente, e não deve ser enviado!").build();
		else if (this.autores.isEmpty())
			return Response.serverError().entity("O artigo deve ter pelo menos um autor!").build();
		else if (this.autores.get(0).getId() == null)
			return Response.serverError().entity("Os autores devem ter ID!").build();
		else if (this.conteudo == null)
			return Response.serverError().entity("Conteúdo vazio!").build();
		else if (!new AutorDAO().verifyAutores(this.autores))
			return Response.serverError().entity("A lista de autores contém um autor não cadastrado!").build();
		else if (!this.comentarios.isEmpty())
			return Response.serverError().entity("Artigos não devem ser criados com comentários!").build();

		return null;

	}

	/**
	 * Método que recebe um novo artigo e mescla os atributos do artigo atual que
	 * estiverem nulos.
	 * 
	 * @param artigo Artigo com os campos que serão copiados.
	 */
	public void merge(Artigo artigo) {
		if (this.titulo == null)
			this.setTitulo(artigo.titulo);
		if (this.subtitulo == null)
			this.setSubtitulo(artigo.subtitulo);
		if (this.conteudo == null)
			this.setConteudo(artigo.conteudo);
	}
}
