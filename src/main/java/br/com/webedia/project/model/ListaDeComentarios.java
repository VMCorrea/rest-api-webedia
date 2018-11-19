package br.com.webedia.project.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import br.com.webedia.project.connection.Server;

/**
 * <h1>ListaDeComentarios</h1>
 * <p>
 * Classe que guarda uma lista de comentários com informações de páginação.
 * </p>
 * 
 * @author Victor Corrêa
 *
 */
public class ListaDeComentarios {

	/* Atributos */
	private int pagina;
	private int totalPaginas;
	private String next; // URL da página seguinte.
	private String prev; // URL da página anterior.
	private List<Comentario> comentarios;

	public ListaDeComentarios(List<Comentario> comentariosSemPaginacao, int size, int pagina, String permalink) {

		if (pagina < 1)
			pagina = 1;

		if (size <= 0)
			size = 5;
		
		this.comentarios = new ArrayList<>(size);

		int itensTotal = comentariosSemPaginacao.size();

		// Ajusta o tamanho da página, caso o tamanho informado seja maior que o número
		// de itens.
		if (size > itensTotal)
			size = itensTotal;

		setTotalPaginas(itensTotal, size);

		setPagina(pagina);

		setComentarios(comentariosSemPaginacao, size);

		setPrev(size, permalink);

		setNext(size, permalink);
	}

	// Configura a url da página seguinte.
	private void setNext(int size, String permalink) {

		if (this.pagina < totalPaginas) {
			if (permalink == null)
				this.next = Server.URL + "comentarios?page=" + (this.pagina + 1) + "&size=" + size;
			else
				this.next = Server.URL + "artigos/" + permalink + "/comentarios?page=" + (this.pagina + 1) + "&size="
						+ size;
		} else
			this.next = "";

	}

	// Configura a url da página anterior.
	private void setPrev(int size, String permalink) {

		if (this.pagina > 1) {
			if (permalink == null)
				this.prev = Server.URL + "comentarios?page=" + (this.pagina - 1) + "&size=" + size;
			else
				this.prev = Server.URL + "artigos/" + permalink + "/comentarios?page=" + (this.pagina - 1) + "&size="
						+ size;
		}

		else
			this.prev = "";

	}

	private void setComentarios(List<Comentario> comentariosSemPaginacao, int size) {

		// Configura uma váriavel que indica em qual elemento da lista começar.
		int start = 0;
		if (this.pagina > 1)
			start = (this.pagina - 1) * size;

		// Itera a lista de comentários, utilizando a fórmula (start + i), até chegar no
		// tamanho estipulado, e adiciona na lista filtrada.
		for (int i = 0; i < size; i++) {
			try {
				this.comentarios.add(comentariosSemPaginacao.get(i + start));
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

	public List<Comentario> getComentarios() {
		return this.comentarios;
	}

	public String getNext() {
		return next;
	}

	public String getPrev() {
		return prev;
	}
}