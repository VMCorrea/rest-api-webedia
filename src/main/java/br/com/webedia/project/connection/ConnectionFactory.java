package br.com.webedia.project.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.sqlite.SQLiteConfig;

import br.com.webedia.project.view.ConsoleView;

/**
 * <h1>ConnectionFactory</h1>
 * <p>
 * Classe que gerencia a conexão com o banco de dados.
 * 
 * @author Victor Corrêa
 *
 */
public class ConnectionFactory {

	// Constante com o endereço do banco de dados.
	private static final String URL = "jdbc:sqlite:banco-de-dados/banco.db";

	// Constante com o driver do banco de dados.
	private static final String DRIVER = "org.sqlite.JDBC";

	/**
	 * Método que cria a conexão com o banco de dados.
	 * 
	 * @return Objeto do tipo Connection
	 */
	public static Connection getConnection() {

		try {
			// Valida o DRIVER
			Class.forName(DRIVER); 

			// Configuração do SQLite, para utilizar as funções de foreign key.
			SQLiteConfig config = new SQLiteConfig();
			config.enforceForeignKeys(true);

			return DriverManager.getConnection(URL, config.toProperties());
		} catch (Exception e) {
			String mensagem = "Erro ao conectar banco de dados: " + e.getMessage();
			ConsoleView.addText(mensagem);
			throw new RuntimeException(mensagem);
		}

	}

	/**
	 * Método que finaliza a conexão com o banco de dados.
	 * 
	 * @param con Connection a ser finalizada.
	 */
	public static void closeConnection(Connection con) {

		try {
			// Verifica se a Connection está fechada. Se não estiver, fecha.
			if (con != null)
				con.close();
		} catch (SQLException e) {
			String mensagem = "Erro ao desconectar do banco de dados: " + e.getMessage();
			ConsoleView.addText(mensagem);
			throw new RuntimeException(mensagem);
		}

	}

	/**
	 * Método que finaliza a conexão com o banco de dados.
	 * 
	 * @param con  Connection a ser finalizada
	 * @param stmt PreparedStatement a ser finalizado
	 */
	public static void closeConnection(Connection con, PreparedStatement stmt) {
		// Utiliza sobrecarga para fechar a Connection
		closeConnection(con);
		try {
			// Verifica se o PreparedStatement está fechado. Se não estiver, fecha.
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			String mensagem = "Erro ao desconectar do banco de dados: " + e.getMessage();
			ConsoleView.addText(mensagem);
			throw new RuntimeException(mensagem);
		}
	}

	/**
	 * Método que finaliza a conexão com o banco de dados.
	 * 
	 * @param con  Connection a ser finalizada.
	 * @param stmt PreparedStatement a ser finalizado.
	 * @param rs   ResultSet a ser finalizado.
	 */
	public static void closeConnection(Connection con, PreparedStatement stmt, ResultSet rs) {
		// Utiliza sobrecarga para fechar a Connection e o PreparedStatement
		closeConnection(con, stmt);
		try {
			// Verifica se o ResultSet está fechado. Se não estiver, fecha.
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			String mensagem = "Erro ao desconectar do banco de dados: " + e.getMessage();
			ConsoleView.addText(mensagem);
			throw new RuntimeException(mensagem);
		}
	}
}
