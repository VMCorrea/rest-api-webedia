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
import br.com.webedia.project.model.Autor;
import br.com.webedia.project.model.ListaDeAutores;
import br.com.webedia.project.view.ConsoleView;

/**
 * <h1>AutorDAO</h1>
 * <p>
 * Essa classe tem a função de fazer todas as operações CRUD relacionadas aos
 * autores.
 * </p>
 * 
 * @author Victor Correa
 *
 */
public class AutorDAO {

	/**
	 * Método que recebe um autor, valida e insere no banco de dados.
	 * 
	 * @param autor Objeto do tipo Autor, que será inserido no banco.
	 * @return Retorna uma resposta HTTP. 201 para sucesso e 500 para falhas.
	 */
	public Response createAutor(Autor autor) {

		// Valida o autor de acordo com as regras para inserção no banco.
		Response r = autor.validate();
		if (r != null)
			return r;

		try {

			Connection con = ConnectionFactory.getConnection();

			String sql = "INSERT INTO autores (nome, sobrenome, bio) VALUES (?, ?, ?);";

			// Cria o PreparedStatement, com a opção de retornar possíveis Primary Keys
			// geradas na execução.
			PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, autor.getNome());
			stmt.setString(2, autor.getSobrenome());
			stmt.setString(3, autor.getBio());

			stmt.executeUpdate();

			// Retorna primary keys geradas no processo. Neste caso, retorna apenas uma.
			ResultSet rs = stmt.getGeneratedKeys();

			Long ultimoId = rs.getLong(1);

			ConnectionFactory.closeConnection(con, stmt, rs);

			// Cria a URL em que o autor poderá ser acessado.
			URI uri = URI.create("/autores/" + ultimoId);

			// Retorna uma resposta 201 para o servidor, com o link de acesso ao autor
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
	 * Método que verifica se todos os autores de uma lista estão no banco de dados.
	 * 
	 * <p>
	 * Esse método é utilizado na criação de um artigo no banco de dados, já que por
	 * regra todos os autores de um novo artigo devem estar registrados no banco.
	 * </p>
	 * 
	 * @param autores Lista de autores para ser verificada.
	 * @return Boolean. Verdadeiro caso todos os autores da lista esteja registrados
	 *         no banco de dados.
	 */
	public boolean verifyAutores(List<Autor> autores) {

		for (Autor autor : autores) {

			if (readAutor(autor.getId()) == null)
				return false;

		}

		return true;
	}

	/**
	 * Método que retorna apenas um autor do banco de dados.
	 * 
	 * @param idAutor Long utilizado para buscar o autor no banco.
	 * @return Retorna um objeto do tipo Autor, caso exista no banco.
	 */
	public Autor readAutor(long idAutor) {

		try {

			Connection con = ConnectionFactory.getConnection();

			String sql = "SELECT * FROM autores WHERE idAutor = ?;";

			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, idAutor);

			ResultSet rs = stmt.executeQuery();

			Autor autor = new Autor();
			autor.setId(rs.getLong("idAutor"));
			autor.setNome(rs.getString("nome"));
			autor.setSobrenome(rs.getString("sobrenome"));
			autor.setBio(rs.getString("bio"));

			ConnectionFactory.closeConnection(con, stmt, rs);

			return autor;

		} catch (SQLException e) {

			// Cria a mensagem de erro e envia para o console da interface da aplicação.
			String mensagem = "Erro ao executar requisição: " + e.getMessage();
			ConsoleView.addText(mensagem);

			return null;
		}

	}

	/**
	 * Método que recebe um objeto do tipo Autor, e atualiza um cadastro no banco de
	 * dados de acordo com o id do autor enviado.
	 * 
	 * @param autor Autor com os atributos que serão atualizados.
	 * @return Retorna uma resposta HTTP. 200 para sucesso e 500 em caso de falha.
	 */
	public Response updateAutor(Autor autor) {

		// O id do autor enviado não pode ser nulo.
		if (autor.getId() == null)
			return Response.serverError().entity("Id para busca está nulo").build();

		autor.merge(readAutor(autor.getId()));

		try {

			Connection con = ConnectionFactory.getConnection();

			String sql = "UPDATE autores SET nome = ?, sobrenome = ?, bio = ? WHERE idAutor = ?;";

			PreparedStatement stmt = con.prepareStatement(sql);

			stmt.setString(1, autor.getNome());
			stmt.setString(2, autor.getSobrenome());
			stmt.setString(3, autor.getBio());
			stmt.setLong(4, autor.getId());

			stmt.executeUpdate();

			ConnectionFactory.closeConnection(con, stmt);

			// Retorna a resposta 200.
			return Response.ok("Autor atualizado").build();

		} catch (SQLException e) {

			// Cria a mensagem de erro e envia para o console da interface da aplicação.
			String mensagem = "Erro ao executar requisição: " + e.getMessage();
			ConsoleView.addText(mensagem);

			// Retorna a resposta 500.
			return Response.serverError().entity(mensagem).build();
		}

	}

	/**
	 * Método que remove um autor do banco de dados.
	 * 
	 * @param idAutor Long utilizada para buscar o autor no banco de dados.
	 * @return Retorna uma resposta HTTP. 200 para sucesso e 500 em caso de falha.
	 */
	public Response deleteAutor(Long idAutor) {

		try {

			Connection con = ConnectionFactory.getConnection();

			String sql = "DELETE FROM autores WHERE idAutor = ?;";

			PreparedStatement stmt = con.prepareStatement(sql);

			stmt.setLong(1, idAutor);

			stmt.executeUpdate();

			ConnectionFactory.closeConnection(con, stmt);

			// Retorna a resposta 200.
			return Response.ok("Autor deletado com sucesso").build();

		} catch (SQLException e) {

			// Cria a mensagem de erro e envia para o console da interface da aplicação.
			String mensagem = "Erro ao executar requisição: " + e.getMessage();
			ConsoleView.addText(mensagem);

			// Retorna a resposta de erro 500.
			return Response.serverError().entity(mensagem).build();
		}

	}

	/**
	 * Método que retorna uma lista de autores.
	 * <p>
	 * A lista contém paginação e a possibilidade de escolher a quantidade de
	 * autores por página.
	 * 
	 * @param size Quantidade de autores por página, o padrão é 5.
	 * @param page Página da lista, por padrão será a primeira.
	 * @return Retorna uma string no padrão Json, com os dados de um objeto do tipo
	 *         ListaDeAutores.
	 */
	public ListaDeAutores listAutor(int size, int page) {

		List<Autor> autores = new ArrayList<>();

		try {

			Connection con = ConnectionFactory.getConnection();

			String sql = "SELECT * FROM autores;";

			PreparedStatement stmt = con.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Autor autor = new Autor();
				autor.setId(rs.getLong("idAutor"));
				autor.setNome(rs.getString("nome"));
				autor.setSobrenome(rs.getString("sobrenome"));
				autor.setBio(rs.getString("bio"));

				autores.add(autor);
			}

			ConnectionFactory.closeConnection(con, stmt, rs);

			// Retorna o objeto do tipo ListaDeAutores, que além de conter os autores,
			// contém as informações e paginação;
			return new ListaDeAutores(autores, size, page);

		} catch (SQLException e) {

			// Cria a mensagem de erro e envia para o console da interface da aplicação.
			String mensagem = "Erro ao executar requisição: " + e.getMessage();
			ConsoleView.addText(mensagem);

			return null;
		}

	}

	/**
	 * Método que retorna uma lista de autores relacionados à um artigo.
	 * <p>
	 * Esse método é utilizado internamente, apenas na execução do método
	 * {@link ArtigoDAO#readArtigo(String, Long) readArtigo} da classe ArtigoDAO,
	 * para montar um artigo. Ele não envia a lista via HTTP.
	 * </p>
	 * 
	 * @param idArtigo ID do artigo relacionado.
	 * @param rs       ResultSet que irá guardar os autores.
	 * @param stmt     PreparedStatement que irá executar o sql.
	 * @param con      Conexão já aberta com o banco de dados.
	 * @return Retorna um objeto do tipo List, contendo os autores.
	 * @throws SQLException
	 */
	public List<Autor> listAutorPorArtigo(Long idArtigo, Connection con, PreparedStatement stmt, ResultSet rs)
			throws SQLException {

		List<Autor> autores = new ArrayList<>();

		// Para buscar os autores relacionados a um artigo, faz-se uma query, utilizando
		// Inner Join com o idArtigo.
		String sql = "SELECT * FROM autores INNER JOIN artigoAutores ON artigoAutores.idAutor = autores.idAutor WHERE artigoAutores.idArtigo = ?;";

		stmt = con.prepareStatement(sql);
		stmt.setLong(1, idArtigo);

		rs = stmt.executeQuery();

		while (rs.next()) {
			Autor autor = new Autor();
			autor.setId(rs.getLong("idAutor"));
			autor.setNome(rs.getString("nome"));
			autor.setSobrenome(rs.getString("sobrenome"));
			autor.setBio(rs.getString("bio"));

			autores.add(autor);
		}

		return autores;
	}
}