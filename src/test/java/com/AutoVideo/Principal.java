package com.AutoVideo;

public class Principal {
	
	public static void main(String args[]) throws InterruptedException {
		AzureReader azure = new AzureReader();
		String texto = "Um aplicativo é uma coleção de itens que funcionam em conjunto para servir a um propósito específico. No Lightning Experience, os aplicativos Lightning dão aos usuários acesso a conjuntos de objetos, guias e outros itens, tudo em um mesmo pacote prático na barra de navegação.";
		String nome = "teste";
		String path = azure.gravarVoz(texto, nome);
		System.out.println(path);
	}

}
