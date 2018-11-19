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

import br.com.webedia.project.dao.ArtigoDAO;
import br.com.webedia.project.dao.ComentarioDAO;
import br.com.webedia.project.model.Artigo;

/**
 * <h1>ArtigoResource</h1>
 * <p>
 * Classe que contém os métodos que são executados através de HTTP Request, para
 * manipular objetos do tipo Artigo.
 * </p>
 * 
 * @author Victor Corrêa
 *
 */
@Path("artigos")
public class ArtigoResource {

	/**
	 * Método que executa um GET Request para um único artigo.
	 * <p>
	 * link da requisição: http://localhost:8080/artigos/{permalink}
	 * 
	 * @param permalink Permalink utilizado para buscar um artigo.
	 * @return String Json que representa um artigo.
	 */
	@Path("{permalink}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String busca(@PathParam("permalink") String permalink) {
		return new ArtigoDAO().readArtigo(permalink, 0l).toJson();
	}

	/**
	 * Método que executa um GET Request para uma lista de artigos.
	 * <p>
	 * A lista contém um sistema de páginação, com a possibilidade de definir o
	 * número de itens por página.
	 * </p>
	 * <p>
	 * link da requisição: http://localhost:8080/artigos?page=1&size=5
	 * </p>
	 * 
	 * @param size Quantidade de elementos por página, 5 por padrão.
	 * @param page Página selecionada, primeira página por padrão.
	 * @return String Json que representa uma lista de artigos com paginação.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String lista(@QueryParam("size") int size, @QueryParam("page") int page) {

		return new ArtigoDAO().listArtigo(size, page).toJson();
	}

	/**
	 * Método que executa um POST Request, para inserir um artigo na base de dados.
	 * <p>
	 * O cliente deve enviar um arquivo json com o padrão da classe Artigo, que será
	 * consumido pelo método.
	 * </p>
	 * <p>
	 * link da requisição: http://localhost:8080/artigos/
	 * </p>
	 * 
	 * @param json String do tipo json, com os dados do artigo.
	 * @return Resposta HTTP.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response adiciona(String json) {

		// Transformação do json em um objeto do tipo Artigo.
		Artigo artigo = new Gson().fromJson(json, Artigo.class);

		return new ArtigoDAO().createArtigo(artigo);

	}

	/**
	 * Método que executa um PUT Request, para atualizar um artigo na base de dados.
	 * <p>
	 * O cliente deve enviar um arquivo json com o padrão da classe Artigo, que será
	 * consumido pelo método. Neste caso, o idArtigo é obrigatório.
	 * </p>
	 * <p>
	 * link da requisição: http://localhost:8080/artigos/
	 * </p>
	 * 
	 * @param json String do tipo json, com os dados do artigo.
	 * @return Resposta HTTP.
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response altera(String json) {

		// Transformação do json em um objeto do tipo Artigo.
		Artigo artigo = new Gson().fromJson(json, Artigo.class);

		return new ArtigoDAO().updateArtigo(artigo);

	}

	/**
	 * Método que executa um DELETE Request, para remover um artigo na base de
	 * dados.
	 * <p>
	 * Nesta opção de método, o cliente seleciona o artigo através do permalink.
	 * </p>
	 * <p>
	 * link da requisição: http://localhost:8080/artigos/{permalink}
	 * </p>
	 * 
	 * @param permalink Permalink do artigo que irá ser deletado.
	 * @return Resposta HTTP.
	 */
	@Path("{permalink}")
	@DELETE
	public Response deleta(@PathParam("permalink") String permalink) {

		return new ArtigoDAO().deleteArtigo(permalink, 0l);
	}

	/**
	 * Método que executa um DELETE Request, para remover um artigo na base de
	 * dados.
	 * <p>
	 * Nesta opção de método, o cliente seleciona o artigo através do id.
	 * </p>
	 * <p>
	 * link da requisição: http://localhost:8080/artigos?id=1
	 * </p>
	 * 
	 * @param id ID que identifica o artigo que será deletado.
	 * @return Resposta HTTP.
	 */
	@DELETE
	public Response deleta(@QueryParam("id") Long id) {

		return new ArtigoDAO().deleteArtigo(null, id);
	}

	/**
	 * Método que executa um GET Request, para listar comentários de um artigo
	 * específico, utilizando o permalink do artigo. A listagem contém paginação.
	 * <p>
	 * link da requisição:
	 * http://localhost:8080/artigos/{permalink}/comentarios?page=1&size=5
	 * </p>
	 * 
	 * 
	 * @param permalink Permalink do artigo que serão listados od comentários.
	 * @param size      Quantidade de itens por página.
	 * @param page      Página atual da lista.
	 * @return String Json que representa uma lista de comentários com paginação.
	 */
	@Path("{permalink}/comentarios")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String listaComentarios(@PathParam("permalink") String permalink, @QueryParam("size") int size,
			@QueryParam("page") int page) {

		// Caso o valor do parâmetro da página seja menor que 1, por padrão será 0,
		// então a página será a primeira.
		if (page < 1)
			page = 1;

		// Caso o tamanho por página seja 0, que é o padrão, ele mudará para 5.
		if (size == 0)
			size = 5;

		return new ComentarioDAO().listComentario(size, page, permalink).toJson();
	}
}
