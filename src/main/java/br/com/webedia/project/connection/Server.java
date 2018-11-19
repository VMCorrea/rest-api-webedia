package br.com.webedia.project.connection;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.ErrorPageGenerator;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import br.com.webedia.project.view.ConsoleView;

/**
 * <h1>Server</h1>
 * <p>
 * Classe que utiliza a biblioteca Grizzly para criar um servidor local.
 * 
 * @author Victor Correa
 *
 */
public class Server {

	/**
	 * Constante pública, com o endereço do servidor.
	 */
	public static final String URL = "http://localhost:8080/";
	
	private static HttpServer server;

	/**
	 * Método que inicializa o servidor.
	 * 
	 * @throws IOException
	 */
	public static void startServer() throws IOException {

		// Cria a configuração da aplicação web.
		ResourceConfig config = new ResourceConfig().packages("br.com.webedia.project");

		// URL em que o server devera ser acessado.
		URI uri = URI.create(Server.URL);

		// Cria o servidor.
		server = GrizzlyHttpServerFactory.createHttpServer(uri, config, false);

		// Modifica a resposta de erro padrão do Grizzly.
		server.getServerConfiguration().setDefaultErrorPageGenerator(new ErrorPageGenerator() {

			@Override
			public String generate(Request request, int status, String reasonPhrase, String description,
					Throwable exception) {
				return reasonPhrase;
			}
		});

		// Inicia o servidor.
		server.start();

		// Adiciona mensagem no console da interface.
		ConsoleView.addText("Server iniciado em " + URL);

	}

	/**
	 * Método que fecha o servidor.
	 * 
	 */
	public static void closeServer() {

		// Fecha o servidor.
		server.shutdown();

		// Adiciona mensagem no console da interface.
		ConsoleView.addText("Server fechado");
	}

}
