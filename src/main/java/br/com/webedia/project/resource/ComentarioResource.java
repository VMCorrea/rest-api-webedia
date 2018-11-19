package br.com.webedia.project.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import br.com.webedia.project.dao.ComentarioDAO;
import br.com.webedia.project.model.Comentario;

/**
 * <h1>ComentarioResource</h1>
 * <p>
 * Classe que contém os métodos que são executados através de HTTP Request, para
 * manipular objetos do tipo Comentario.
 * </p>
 * 
 * @author Victor Corrêa
 *
 */
@Path("comentarios")
public class ComentarioResource {

	/**
	 * Método que executa um POST Request, para inserir um comentario na base de
	 * dados.
	 * <p>
	 * O cliente deve enviar um arquivo json com o padrão da classe Comentario, que
	 * será consumido pelo método.
	 * </p>
	 * <p>
	 * link da requisição: http://localhost:8080/comentarios/
	 * </p>
	 * 
	 * @param json String do tipo json, com os dados do comentario.
	 * @return Resposta HTTP.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response adiciona(String json) {

		Comentario comentario = new Gson().fromJson(json, Comentario.class);
		return new ComentarioDAO().createComentario(comentario);
	}

	/**
	 * Método que executa um PUT Request, para atualizar um comentario na base de
	 * dados.
	 * <p>
	 * O cliente deve enviar um arquivo json com o padrão da classe Comentario, que
	 * será consumido pelo método. Neste caso, o idComentario é obrigatório.
	 * </p>
	 * <p>
	 * link da requisição: http://localhost:8080/comentarios/
	 * </p>
	 * 
	 * @param json String do tipo json, com os dados do comentario.
	 * @return Resposta HTTP.
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response altera(String json) {

		Comentario comentario = new Gson().fromJson(json, Comentario.class);
		return new ComentarioDAO().updateComentario(comentario);
	}

	/**
	 * Método que executa um GET Request para um único comentário.
	 * <p>
	 * link da requisição: http://localhost:8080/comentarios/{id}
	 * 
	 * @param id ID utilizado para buscar um comentário.
	 * @return String json que representa um comentário.
	 */
	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String busca(@PathParam("id") Long id) {
		return new ComentarioDAO().readComentario(id).toJson();
	}

	/**
	 * Método que executa um GET Request para uma lista de comentarios.
	 * <p>
	 * A lista contém um sistema de páginação, com a possibilidade de definir o
	 * número de itens por página.
	 * </p>
	 * <p>
	 * link da requisição: http://localhost:8080/comentarios?page=1&size=5
	 * </p>
	 * 
	 * @param page Quantidade de elementos por página, 5 por padrão.
	 * @param size Página selecionada, primeira página por padrão.
	 * @return String Json que representa uma lista de comentários com paginação.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String lista(@QueryParam("page") int page, @QueryParam("size") int size) {

		return new ComentarioDAO().listComentario(size, page, null).toJson();
	}

	/**
	 * Método que executa um DELETE Request, para remover um comentário na base de
	 * dados.
	 * <p>
	 * O cliente deve selecionar o comentário através do id.
	 * </p>
	 * <p>
	 * link da requisição: http://localhost:8080/comentarios/{id}
	 * </p>
	 * 
	 * @param id ID que identifica o comentario que será deletado.
	 * @return Resposta HTTP.
	 */
	@Path("{id}")
	@DELETE
	public Response deleta(@PathParam("id") Long id) {
		return new ComentarioDAO().deleteComentario(id);
	}
}
