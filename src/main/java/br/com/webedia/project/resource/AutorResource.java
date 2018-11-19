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

import br.com.webedia.project.dao.AutorDAO;
import br.com.webedia.project.model.Autor;

@Path("autores")
public class AutorResource {

	/**
	 * Método que executa um GET Request para um único autor.
	 * <p>
	 * link da requisição: http://localhost:8080/autores/{id}
	 * 
	 * @param id ID utilizado para buscar um autor.
	 * @return String json que representa um autor.
	 */
	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String busca(@PathParam("id") Long id) {
		return new AutorDAO().readAutor(id).toJson();
	}

	/**
	 * Método que executa um GET Request para uma lista de autores.
	 * <p>
	 * A lista contém um sistema de páginação, com a possibilidade de definir o
	 * número de itens por página.
	 * </p>
	 * <p>
	 * link da requisição: http://localhost:8080/autores?page=1&size=5
	 * </p>
	 * 
	 * @param page Quantidade de elementos por página, 5 por padrão.
	 * @param size Página selecionada, primeira página por padrão.
	 * @return String Json que representa uma lista de autores com paginação.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String lista(@QueryParam("page") int page, @QueryParam("size") int size) {

		return new AutorDAO().listAutor(size, page).toJson();
	}

	/**
	 * Método que executa um POST Request, para inserir um autor na base de dados.
	 * <p>
	 * O cliente deve enviar um arquivo json com o padrão da classe Autor, que será
	 * consumido pelo método.
	 * </p>
	 * <p>
	 * link da requisição: http://localhost:8080/autores/
	 * </p>
	 * 
	 * @param json String do tipo json, com os dados do autor.
	 * @return Resposta HTTP.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response adiciona(String json) {

		Autor autor = new Gson().fromJson(json, Autor.class);

		return new AutorDAO().createAutor(autor);

	}

	/**
	 * Método que executa um PUT Request, para atualizar um autor na base de dados.
	 * <p>
	 * O cliente deve enviar um arquivo json com o padrão da classe Autor, que será
	 * consumido pelo método. Neste caso, o idAutor é obrigatório.
	 * </p>
	 * <p>
	 * link da requisição: http://localhost:8080/autores/
	 * </p>
	 * 
	 * @param json String do tipo json, com os dados do autor.
	 * @return Resposta HTTP.
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response altera(String json) {

		Autor autor = new Gson().fromJson(json, Autor.class);
		return new AutorDAO().updateAutor(autor);

	}

	/**
	 * Método que executa um DELETE Request, para remover um autor na base de dados.
	 * <p>
	 * O cliente deve selecionar o autor através do id.
	 * </p>
	 * <p>
	 * link da requisição: http://localhost:8080/autores/{id}
	 * </p>
	 * 
	 * @param id ID que identifica o autor que será deletado.
	 * @return Resposta HTTP.
	 */
	@Path("{id}")
	@DELETE
	public Response deleta(@PathParam("id") Long id) {
		return new AutorDAO().deleteAutor(id);
	}
}
