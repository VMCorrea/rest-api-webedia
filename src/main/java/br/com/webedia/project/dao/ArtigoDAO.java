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
import br.com.webedia.project.model.Artigo;
import br.com.webedia.project.model.Autor;
import br.com.webedia.project.model.Comentario;
import br.com.webedia.project.model.ListaDeArtigos;
import br.com.webedia.project.view.ConsoleView;

/**
 * <h1>ArtigoDAO</h1>
 * <p>
 * Essa classe tem a função de fazer todas as operações CRUD relacionadas aos
 * artigos.
 * </p>
 * 
 * @author Victor Correa
 *
 */
public class ArtigoDAO {

	/**
	 * Método que recebe um artigo, valida e insere no banco de dados.
	 * 
	 * @param artigo Objeto do tipo Artigo.
	 * @return Retorna uma resposta HTTP. 201 para sucesso e 500 para falhas.
	 */
	public Response createArtigo(Artigo artigo) {

		// Valida o artigo de acordo com as regras para inserção no banco.
		Response r = artigo.validate();
		if (r != null)
			return r;

		// Gera o permalink de acordo com o título do artigo.
		artigo.generatePermalink();

		try {

			Connection con = ConnectionFactory.getConnection();

			String sql = "INSERT INTO artigos (titulo, subtitulo, conteudo, permalink, dataPublicacao) VALUES (?, ?, ?, ?, datetime('now','localtime'));";

			// Cria o PreparedStatement, com a opção de retornar possíveis Primary Keys
			// geradas na execução.
			PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, artigo.getTitulo());
			stmt.setString(2, artigo.getSubtitulo());
			stmt.setString(3, artigo.getConteudo());
			stmt.setString(4, artigo.getPermalink());

			stmt.executeUpdate();

			// Retorna primary keys geradas no processo. Neste caso, retorna apenas uma.
			ResultSet rs = stmt.getGeneratedKeys();

			Long ultimoId = rs.getLong(1);

			// Esse código sql será utilizado para criar a relação de artigos e autores em
			// uma tabela 'Many to Many' no banco de dados.
			sql = "INSERT INTO artigoAutores (idArtigo, idAutor) VALUES (?, ?);";

			stmt = con.prepareStatement(sql);

			List<Autor> autores = artigo.getAutores();
			for (Autor autor : autores) {

				stmt.setLong(1, ultimoId);
				stmt.setLong(2, autor.getId());

				stmt.addBatch();
			}

			stmt.executeBatch();

			// Fecha a conexão.
			ConnectionFactory.closeConnection(con, stmt);

			// Cria a URL em que o artigo poderá ser acessado.
			URI uri = URI.create("/artigos/" + artigo.getPermalink());

			// Retorna uma resposta 201 para o servidor, com o link de acesso ao artigo
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
	 * Método que retorna apenas um artigo do banco de dados.
	 * <p>
	 * Este método permite que o artigo seja pesquisado no banco de dados pelo
	 * permalink ou pelo idArtigo.
	 * 
	 * @param permalink Permalink do artigo a ser buscado.
	 * @param idArtigo  ID do artigo a ser buscado.
	 * @return Retorna um objeto do tipo Artigo, caso exista no banco.
	 */
	public Artigo readArtigo(String permalink, Long idArtigo) {

		try {

			Connection con = ConnectionFactory.getConnection();

			String sql;

			// Por padrão, o idArtigo virá como 0 e será utilizado o permalink para fazer a
			// busca no banco. Se o idArtigo for maior que 0, então ele será usado para a
			// pesquisa.
			if (idArtigo <= 0) {

				sql = String.format("SELECT * FROM artigos WHERE permalink = '%s';", permalink);

			} else {

				sql = String.format("SELECT * FROM artigos WHERE idArtigo = %d;", idArtigo);

			}

			PreparedStatement stmt = con.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();

			Artigo artigo = new Artigo();
			artigo.setId(rs.getLong("idArtigo"));
			artigo.setTitulo(rs.getString("titulo"));
			artigo.setSubtitulo(rs.getString("subtitulo"));
			artigo.setConteudo(rs.getString("conteudo"));
			artigo.setDataPublicacao(rs.getString("dataPublicacao"));
			artigo.setDataAtualizacao(rs.getString("dataAtualizacao"));
			artigo.generatePermalink();

			List<Autor> autores = new AutorDAO().listAutorPorArtigo(artigo.getId(), con, stmt, rs);

			for (Autor autor : autores) {
				artigo.addAutor(autor);
			}

			List<Comentario> comentarios = new ComentarioDAO().listComentarioPorArtigo(artigo.getId(), con, stmt, rs);

			for (Comentario comentario : comentarios) {
				artigo.addComentario(comentario);
			}

			ConnectionFactory.closeConnection(con, stmt, rs);

			return artigo;

		} catch (SQLException e) {

			// Cria a mensagem de erro e envia para o console da interface da aplicação.
			String mensagem = "Erro ao executar requisição: " + e.getMessage();
			ConsoleView.addText(mensagem);

			return null;
		}
	}

	/**
	 * Método que recebe um objeto do tipo Artigo, e atualiza um cadastro no banco
	 * de dados de acordo com o id do artigo enviado.
	 * 
	 * @param artigo Artigo com os atributos que serão atualizados.
	 * @return Retorna uma resposta HTTP. 200 para sucesso e 500 em caso de falha.
	 */
	public Response updateArtigo(Artigo artigo) {

		// O id do artigo enviado não pode ser nulo.
		if (artigo.getId() == null)
			return Response.serverError().entity("ID nulo!").build();

		// Preenche os campos que estiverem nulos no artigo enviado, com os campos do
		// artigo dentro do banco.
		artigo.merge(new ArtigoDAO().readArtigo(null, artigo.getId()));

		// Gera o permalink atualizado.
		artigo.generatePermalink();

		try {

			// Conecta com o banco de dados.
			Connection con = ConnectionFactory.getConnection();

			// Cria o comando sql.
			String sql = "UPDATE artigos SET dataAtualizacao = datetime('now','localtime'), titulo = ?, permalink = ?, subtitulo = ?, conteudo = ? WHERE idArtigo = ?;";

			// Configura o PreparedStatement com o comando sql.
			PreparedStatement stmt = con.prepareStatement(sql);

			// Substitui as '?', na string sql, com os valores necessários.
			stmt.setString(1, artigo.getTitulo());
			stmt.setString(2, artigo.getPermalink());
			stmt.setString(3, artigo.getSubtitulo());
			stmt.setString(4, artigo.getConteudo());
			stmt.setLong(5, artigo.getId());

			// Executa o sql
			stmt.executeUpdate();

			// Atualiza a relação de artigos e autores, apenas se a lista de autores do
			// artigo enviado não estiver vazia.
			atualizaAutores(artigo, con, stmt);

			// Fecha a conexão
			ConnectionFactory.closeConnection(con, stmt);

			// Retorna a resposta 200.
			return Response.ok("Artigo atualizado").build();

		} catch (SQLException e) {

			// Cria a mensagem de erro e envia para o console da interface da aplicação.
			String mensagem = "Erro ao executar requisição: " + e.getMessage();
			ConsoleView.addText(mensagem);

			// Retorna a resposta 500.
			return Response.serverError().entity(mensagem).build();
		}

	}

	/**
	 * Método que remove um artigo do banco de dados, recebendo o permalink ou o id
	 * do artigo.
	 * 
	 * @param permalink Permalink do artigo que será deletado.
	 * @param idArtigo  ID do artigo que será deletado.
	 * @return Retorna uma resposta HTTP. 200 para sucesso e 500 para falha.
	 */
	public Response deleteArtigo(String permalink, Long idArtigo) {

		// Caso o id e o permalink estiverem inválidos, retorna erro.
		if (idArtigo <= 0 && permalink == null)
			return Response.serverError().entity("Requisição sem os parametros necessários!").build();

		try {

			Connection con = ConnectionFactory.getConnection();

			String sql;

			// Caso o idArtigo for maior que 0, que é o padrão, é o que será utilizado para
			// a busca. Se o idArtigo não for usado, então será o permalink.
			if (idArtigo > 0) {

				sql = String.format("DELETE FROM artigos WHERE idArtigo = %d;", idArtigo);

			} else {

				sql = String.format("DELETE FROM artigos WHERE permalink = '%s';", permalink);

			}

			PreparedStatement stmt = con.prepareStatement(sql);

			stmt.executeUpdate();

			ConnectionFactory.closeConnection(con, stmt);

			// Retorna a resposta 200.
			return Response.ok("Artigo deletado com sucesso!").build();

		} catch (SQLException e) {

			// Cria a mensagem de erro e envia para o console da interface da aplicação.
			String mensagem = "Erro ao executar requisição: " + e.getMessage();
			ConsoleView.addText(mensagem);

			// Retorna a resposta de erro 500.
			return Response.serverError().entity(mensagem).build();
		}
	}

	/**
	 * Método que retorna uma lista de artigos.
	 * <p>
	 * A lista contém paginação e a possibilidade de escolher a quantidade de
	 * artigos por página.
	 * 
	 * @param size Quantidade de artigos por página, o padrão é 5.
	 * @param page Página da lista, por padrão será a primeira.
	 * @return Retorna uma string no padrão Json, com os dados de um objeto do tipo
	 *         ListaDeArtigos.
	 */
	public ListaDeArtigos listArtigo(int size, int page) {

		List<Artigo> artigos = new ArrayList<>();

		try {

			Connection con = ConnectionFactory.getConnection();

			String sql = "SELECT * FROM artigos;";

			PreparedStatement stmt = con.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Artigo artigo = new Artigo();
				artigo.setId(rs.getLong("idArtigo"));
				artigo.setTitulo(rs.getString("titulo"));
				artigo.setSubtitulo(rs.getString("subtitulo"));
				artigo.setConteudo(rs.getString("conteudo"));
				artigo.setDataAtualizacao(rs.getString("dataAtualizacao"));
				artigo.setDataPublicacao(rs.getString("dataPublicacao"));
				artigo.generatePermalink();

				artigos.add(artigo);

			}

			// Para cada artigo, será executada o sql que irá buscar os autores e os
			// comentários desses artigos para adicionar em suas respectivas lista.
			for (Artigo artigo : artigos) {

				sql = "SELECT * FROM autores INNER JOIN artigoAutores ON artigoAutores.idAutor = autores.idAutor WHERE artigoAutores.idArtigo = ?;";

				stmt = con.prepareStatement(sql);

				stmt.setLong(1, artigo.getId());

				rs = stmt.executeQuery();

				while (rs.next()) {
					Autor autor = new Autor();
					autor.setId(rs.getLong("idAutor"));
					autor.setNome(rs.getString("nome"));
					autor.setSobrenome(rs.getString("sobrenome"));
					autor.setBio(rs.getString("bio"));

					artigo.addAutor(autor);
				}

				sql = "SELECT * FROM comentarios WHERE idArtigo = ?";

				stmt = con.prepareStatement(sql);

				stmt.setLong(1, artigo.getId());

				rs = stmt.executeQuery();

				while (rs.next()) {
					Comentario comentario = new Comentario();
					comentario.setData(rs.getString("data"));
					comentario.setId(rs.getLong("idComentario"));
					comentario.setTexto(rs.getString("texto"));
					comentario.setUsuario(rs.getString("usuario"));
					comentario.setIdArtigo(artigo.getId());

					artigo.addComentario(comentario);
				}

			}

			ConnectionFactory.closeConnection(con, stmt, rs);

			// Retorna, o objeto do tipo ListaDeArtigos, que além
			// de conter os artigos, contém as informações e paginação;
			return new ListaDeArtigos(artigos, size, page);

		} catch (SQLException e) {

			// Cria a mensagem de erro e envia para o console da interface da aplicação.
			String mensagem = "Erro ao executar requisição: " + e.getMessage();
			ConsoleView.addText(mensagem);

			return null;
		}

	}

	private void atualizaAutores(Artigo artigo, Connection con, PreparedStatement stmt) throws SQLException {

		String sql;

		if (!artigo.getAutores().isEmpty()) {

			// Cria o sql para deletar as antigas relações de artigos e autores.
			sql = String.format("DELETE FROM artigoAutores WHERE idArtigo = %d;", artigo.getId());

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();

			// Agora o sql que adiciona as relações com base na lista atualizada de autores.
			sql = "INSERT INTO artigoAutores (idArtigo, idAutor) VALUES (?, ?);";

			stmt = con.prepareStatement(sql);

			List<Autor> autores = artigo.getAutores();
			for (Autor autor : autores) {
				stmt.setLong(1, artigo.getId());
				stmt.setLong(2, autor.getId());
				stmt.addBatch();
			}

			// Executa os múltiplos comandos.
			stmt.executeBatch();

		}
	}

}
