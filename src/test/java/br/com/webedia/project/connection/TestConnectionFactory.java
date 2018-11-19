package br.com.webedia.project.connection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

public class TestConnectionFactory {

	@Test
	public void TestaAbrirEFecharConexao() throws SQLException {

		Connection con = ConnectionFactory.getConnection();

		assertFalse(con.isClosed());

		ConnectionFactory.closeConnection(con);

		assertTrue(con.isClosed());

	}

	@Test
	public void TestaAbrirEFecharConexaoComStatement() throws SQLException {

		Connection con = ConnectionFactory.getConnection();

		PreparedStatement stmt = con.prepareStatement("SELECT * FROM artigos;");

		assertFalse(con.isClosed() && stmt.isClosed());

		ConnectionFactory.closeConnection(con, stmt);

		assertTrue(con.isClosed() && stmt.isClosed());

	}
	
	@Test
	public void TestaAbrirEFecharConexaoComStatementEResultSet() throws SQLException {

		Connection con = ConnectionFactory.getConnection();

		PreparedStatement stmt = con.prepareStatement("SELECT * FROM artigos;");
		
		ResultSet rs = stmt.executeQuery();

		assertFalse(con.isClosed() && stmt.isClosed() && rs.isClosed());

		ConnectionFactory.closeConnection(con, stmt, rs);

		assertTrue(con.isClosed() && stmt.isClosed() && rs.isClosed());

	}

}
