package br.com.webedia.project;

import java.util.ArrayList;
import java.util.List;

import br.com.webedia.project.model.Artigo;
import br.com.webedia.project.model.ListaDeArtigos;

public class Main {

	public static void main(String[] args) {

		List<Artigo> artigos = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			Artigo artigo = new Artigo();
			artigo.setId(Long.valueOf(i));
			artigo.setTitulo("Título do Artigo - Parte " + (i + 1));
			artigo.setSubtitulo("Subtítulo do Artigo");
			artigo.setConteudo("Conteúdo");
			artigo.generatePermalink();
			artigos.add(artigo);
		}
		
		ListaDeArtigos lista = new ListaDeArtigos(artigos, 75, 50);
		
		System.out.println(lista.toJson());

	}

}
