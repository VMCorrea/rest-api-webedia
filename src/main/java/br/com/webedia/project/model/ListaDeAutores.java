package br.com.webedia.project.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import br.com.webedia.project.connection.Server;

/**
 * <h1>ListaDeAutores</h1>
 * <p>
 * Classe que guarda uma lista de autores com informações de páginação.
 * </p>
 * 
 * 
 * @author Victor Corrêa
 *
 */
public class ListaDeAutores {

	/* Atributos */
	private int pagina;
	private int totalPaginas;
	private String next; // URL da página seguinte.
	private String prev; // URL da página anterior.
	private List<Autor> autores;

	/**
	 * Construtor da classe ListaDeAutores. Assim que é chamado, são feitas as
	 * configurações de páginação da lista de autores, utilizando os métodos
	 * privados.
	 * 
	 * @param autoresSemPaginacao Lista de autores sem páginação.
	 * @param size                Quantidade de itens por página.
	 * @param pagina              Página atual da lista.
	 */
	public ListaDeAutores(List<Autor> autoresSemPaginacao, int size, int pagina) {

		if (pagina < 1)
			pagina = 1;

		if (size <= 0)
			size = 5;

		this.autores = new ArrayList<>(size);

		int itensTotal = autoresSemPaginacao.size();

		// Ajusta o tamanho da página, caso o tamanho informado seja maior que o número
		// de itens.
		if (size > itensTotal)
			size = itensTotal;

		setTotalPaginas(itensTotal, size);

		setPagina(pagina);

		setAutores(autoresSemPaginacao, size);

		setPrev(size);

		setNext(size);
	}

	private void setNext(int size) {

		// Configura a url da página seguinte.
		if (this.pagina < totalPaginas)
			this.next = Server.URL + "autores?page=" + (this.pagina + 1) + "&size=" + size;
		else
			this.next = "";

	}

	private void setPrev(int size) {

		// Configura a url da página anterior.
		if (this.pagina > 1)
			this.prev = Server.URL + "autores?page=" + (this.pagina - 1) + "&size=" + size;
		else
			this.prev = "";

	}

	private void setAutores(List<Autor> autoresSemPaginacao, int size) {

		// Configura uma váriavel que indica em qual elemento da lista começar.
		int start = 0;
		if (this.pagina > 1)
			start = (this.pagina - 1) * size;

		// Itera a lista de autores, utilizando a fórmula (start + i), até chegar no
		// tamanho estipulado, e adiciona na lista filtrada.
		for (int i = 0; i < size; i++) {
			try {
				this.autores.add(autoresSemPaginacao.get(i + start));
			} catch (Exception e) {
				break;
			}
		}

	}

	private void setPagina(int pagina) {

		// Configura a página atual, e ajusta caso ela ultrapasse o total de páginas.
		this.pagina = pagina;
		if (this.pagina > this.totalPaginas)
			this.pagina = this.totalPaginas;
	}

	private void setTotalPaginas(int itensTotal, int size) {

		// Calcula o total de páginas, e faz um ajuste caso o total seja ímpar.
		if (itensTotal % size == 0)
			this.totalPaginas = itensTotal / size;
		else
			this.totalPaginas = (itensTotal / size) + 1;

	}

	/**
	 * Método que transforma o objeto desta classe em uma string no formato Json.
	 * 
	 * @return String em formato Json.
	 */
	public String toJson() {
		return new Gson().toJson(this);
	}

	public List<Autor> getAutores() {
		return autores;
	}

	public String getPrev() {
		return prev;
	}

	public String getNext() {
		return next;
	}

}
