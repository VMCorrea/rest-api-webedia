package br.com.webedia.project.dao;

import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import br.com.webedia.project.connection.ConnectionFactory;
import br.com.webedia.project.model.Comentario;
import br.com.webedia.project.model.ListaDeComentarios;
import br.com.webedia.project.view.ConsoleView;

/**
 * <h1>ComentarioDAO</h1>
 * <p>
 * Essa classe tem a função de fazer todas as operações CRUD relacionadas aos
 * comentários.
 * </p>
 * 
 * @author Victor Correa
 *
 */
public class ComentarioDAO {

	private ArtigoDAO artigoDao = new ArtigoDAO();

	/**
	 * Método que recebe um comentario, valida e insere no banco de dados.
	 * 
	 * @param comentario Objeto do tipo Comentario, que será inserido no banco.
	 * @return Retorna uma resposta HTTP. 201 para sucesso e 500 para falhas.
	 */
	public Response createComentario(Comentario comentario) {

		// Valida o comentario de acordo com as regras para inserção no banco.
		Response r = comentario.validate();
		if (r != null)
			return r;
		else if (artigoDao.readArtigo(null, comentario.getIdArtigo()) == null)
			return Response.serverError().entity("Artigo relacionado não existe!").build();

		try {

			Connection con = ConnectionFactory.getConnection();

			String sql = "INSERT INTO comentarios (usuario, texto, data, idArtigo) VALUES (?, ?, datetime('now','localtime'), ?);";

			// Cria o PreparedStatement, com a opção de retornar possíveis Primary Keys
			// geradas na execução.
			PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, comentario.getUsuario());
			stmt.setString(2, comentario.getTexto());
			stmt.setLong(3, comentario.getIdArtigo());

			stmt.executeUpdate();

			// Retorna primary keys geradas no processo.
			ResultSet rs = stmt.getGeneratedKeys();

			Long ultimoId = rs.getLong(1);

			ConnectionFactory.closeConnection(con, stmt, rs);

			// Cria a URL em que o comentario poderá ser acessado.
			URI uri = URI.create("/comentarios/" + ultimoId);

			// Retorna uma resposta 201 para o servidor, com o link de acesso ao comentario
			// criado.
			return Response.created(uri).build();

		} catch (SQLException e) {

			// Cria a mensagem de erro e envia para o console da interface da aplicação.
			String mensagem = "Erro ao executar requisição: " + e.getMessage();
			ConsoleView.addText(mensagem);

			// Retorna uma resposta 500, com a mensagem criada.
			return Response.serverError().entity(mensagem).build();
		}
	}

	/**
	 * Método que retorna apenas um comentário do banco de dados.
	 * 
	 * @param idComentario Long utilizado para buscar o comentário no banco.
	 * @return Retorna um objeto do tipo Comentario, caso exista no banco.
	 */
	public Comentario readComentario(Long idComentario) {
		try {

			Connection con = ConnectionFactory.getConnection();

			String sql = "SELECT * FROM comentarios WHERE idComentario = ?;";

			PreparedStatement stmt = con.prepareStatement(sql);

			stmt.setLong(1, idComentario);

			ResultSet rs = stmt.executeQuery();

			Comentario comentario = new Comentario();
			comentario.setId(rs.getLong("idComentario"));
			comentario.setIdArtigo(rs.getLong("idArtigo"));
			comentario.setData(rs.getString("data"));
			comentario.setTexto(rs.getString("texto"));
			comentario.setUsuario(rs.getString("usuario"));

			ConnectionFactory.closeConnection(con, stmt, rs);

			return comentario;

		} catch (SQLException e) {

			// Cria a mensagem de erro e envia para o console da interface da aplicação.
			String mensagem = "Erro ao executar requisição: " + e.getMessage();
			ConsoleView.addText(mensagem);

			return null;
		}
	}

	/**
	 * Método que recebe um objeto do tipo Comnetario, e atualiza um cadastro no
	 * banco de dados de acordo com o id do comentario enviado.
	 * 
	 * @param comentario Comentario com os atributos que serão atualizados.
	 * @return Retorna uma resposta HTTP. 200 para sucesso e 500 em caso de falha.
	 */
	public Response updateComentario(Comentario comentario) {

		// O id do comentario enviado não pode ser nulo.
		if (comentario.getId() == null)
			return Response.serverError().entity("ID nulo!").build();

		// Preenche os campos que estiverem nulos no comentario enviado, com os campos
		// do comentario dentro do banco.
		comentario.merge(readComentario(comentario.getId()));

		try {

			Connection con = ConnectionFactory.getConnection();

			String sql = "UPDATE comentarios SET texto = ?, usuario = ? WHERE idComentario = ?;";

			PreparedStatement stmt = con.prepareStatement(sql);

			stmt.setString(1, comentario.getTexto());
			stmt.setString(2, comentario.getUsuario());
			stmt.setLong(3, comentario.getId());

			stmt.executeUpdate();

			ConnectionFactory.closeConnection(con, stmt);

			// Retorna a resposta 200.
			return Response.ok("Comentario atualizado!").build();

		} catch (SQLException e) {

			// Cria a mensagem de erro e envia para o console da interface da aplicação.
			String mensagem = "Erro ao executar requisição: " + e.getMessage();
			ConsoleView.addText(mensagem);

			// Retorna a resposta 500.
			return Response.serverError().entity(mensagem).build();
		}

	}

	/**
	 * Método que remove um comentário do banco de dados.
	 * 
	 * @param idComentario Long utilizada para buscar o comentário no banco de
	 *                     dados.
	 * @return Retorna uma resposta HTTP. 200 para sucesso e 500 em caso de falha.
	 */
	public Response deleteComentario(Long idComentario) {
		try {

			Connection con = ConnectionFactory.getConnection();

			String sql = "DELETE FROM comentarios WHERE idComentario = ?;";

			PreparedStatement stmt = con.prepareStatement(sql);

			stmt.setLong(1, idComentario);

			stmt.executeUpdate();

			ConnectionFactory.closeConnection(con, stmt);

			// Retorna a resposta 200.
			return Response.ok("Comentario deletado com sucesso!").build();

		} catch (SQLException e) {

			// Cria a mensagem de erro e envia para o console da interface da aplicação.
			String mensagem = "Erro ao executar requisição: " + e.getMessage();
			ConsoleView.addText(mensagem);

			// Retorna a resposta de erro 500.
			return Response.serverError().entity(mensagem).build();
		}
	}

	/**
	 * Método que retorna uma lista de comentários.
	 * <p>
	 * A lista contém paginação e a possibilidade de escolher a quantidade de
	 * comentários por página.
	 * 
	 * @param size      Quantidade de comentários por página, o padrão é 5.
	 * @param page      Página da lista, por padrão será a primeira.
	 * @param permalink Permalink para buscar comentários de um artigo em
	 *                  específico.
	 * @return Retorna uma string no padrão Json, com os dados de um objeto do tipo
	 *         ListaDeComentarios.
	 */
	public ListaDeComentarios listComentario(int size, int page, String permalink) {

		List<Comentario> comentarios = new ArrayList<>();

		try {

			Connection con = ConnectionFactory.getConnection();

			// SQL para selecionar todos os comentários. Se tiver permalink, busca os
			// comentários de um artigo específico
			String sql;
			if (permalink == null)
				sql = "SELECT * FROM comentarios;";
			else {
				sql = String.format(
						"SELECT comentarios.* FROM comentarios INNER JOIN artigos ON comentarios.idArtigo = artigos.idArtigo WHERE artigos.permalink = '%s';",
						permalink);
			}

			PreparedStatement stmt = con.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Comentario comentario = new Comentario();
				comentario.setId(rs.getLong("idComentario"));
				comentario.setIdArtigo(rs.getLong("idArtigo"));
				comentario.setData(rs.getString("data"));
				comentario.setTexto(rs.getString("texto"));
				comentario.setUsuario(rs.getString("usuario"));

				comentarios.add(comentario);
			}

			ConnectionFactory.closeConnection(con, stmt, rs);

			// Retorna o objeto do tipo ListaDeComentarios, que além
			// de conter os comentários, contém as informações e paginação;
			return new ListaDeComentarios(comentarios, size, page, permalink);

		} catch (SQLException e) {

			// Cria a mensagem de erro e envia para o console da interface da aplicação.
			String mensagem = "Erro ao executar requisição: " + e.getMessage();
			ConsoleView.addText(mensagem);

			return null;

		}

	}

	/**
	 * 
	 * Método que retorna uma lista de comentários relacionados à um artigo.
	 * <p>
	 * Esse método é utilizado de forma interna, e serve apenas para montar o artigo
	 * no método {@link ArtigoDAO#readArtigo(String, Long) readArtigo}. Para criar
	 * uma listagem de comentários para ser enviada ao cliente via HTTP, é utilizado
	 * o método {@link #listComentario(int, int, String) listComentario}, passando o
	 * atributo permalink.
	 * </p>
	 * 
	 * 
	 * @param idArtigo ID do artigo relacionado.
	 * @param rs       ResultSet que irá guardar os comentários.
	 * @param stmt     PreparedStatement que irá executar o sql.
	 * @param con      Conexão já aberta com o banco de dados.
	 * @return Retorna um objeto do tipo List, contendo os autores.
	 * @throws SQLException
	 */
	public List<Comentario> listComentarioPorArtigo(Long idArtigo, Connection con, PreparedStatement stmt, ResultSet rs)
			throws SQLException {

		List<Comentario> comentarios = new ArrayList<>();

		String sql = "SELECT * FROM comentarios WHERE idArtigo = ?";

		stmt = con.prepareStatement(sql);
		stmt.setLong(1, idArtigo);

		rs = stmt.executeQuery();

		while (rs.next()) {
			Comentario comentario = new Comentario();
			comentario.setData(rs.getString("data"));
			comentario.setId(rs.getLong("idComentario"));
			comentario.setTexto(rs.getString("texto"));
			comentario.setUsuario(rs.getString("usuario"));
			comentario.setIdArtigo(idArtigo);

			comentarios.add(comentario);
		}

		return comentarios;
	}
}