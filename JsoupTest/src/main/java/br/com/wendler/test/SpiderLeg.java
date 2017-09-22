package br.com.wendler.test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderLeg {
	// USER AGENT é utilizado para informar ao JSOUP que a busca realizada será
	// em páginas web para diferentes aplicações, neste caso está sendo
	// informado o tipo Desktop, assim o mesmo trará o tamanha de páginas para
	// este tipo de aplicação
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	private List<String> links = new LinkedList<String>();
	private Document htmlDocument;

	/**
	 * Realiza a etapa de crawl, buscando links da URL passada e retornando caso
	 * a recuperação de novas URLs no href da página foi executada com sucesso.
	 *
	 * @param url
	 *            - Url onde será iniciado o crawl por busca de novas urls no
	 *            HREF
	 * @return true -> Achou novas URLs para buscar false -> não encontrou URL
	 *         para o crawl
	 */
	public boolean crawl(String url) {
		try {
			//Cria a conexão com o site informado
			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
			Document htmlDocument = connection.get();
			this.htmlDocument = htmlDocument;
			if (connection.response().statusCode() == 200) {
				System.out.println("\n**Sucesso** Página acessada pela url " + url);
			}
			if (!connection.response().contentType().contains("text/html")) {
				System.out.println("**Falha** Não foi identificada como uma página HTML");
				return false;
			}
			Elements linksOnPage = htmlDocument.select("a[href]");
			System.out.println("Encontrados (" + linksOnPage.size() + ") links");
			for (Element link : linksOnPage) {
				this.links.add(link.absUrl("href"));
			}
			return true;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}
	}

	/**
	 * Realiza a busca da palavra inputada na classe main na URL atual
	 * 
	 * @param searchWord
	 *            - A palavra sendo buscada
	 * @return true -> palavra encontrada na página false - > palavra não
	 *         encontrada
	 */
	public boolean searchForWord(String searchWord) {
		if (this.htmlDocument == null) {
			System.out.println("ERROR! Ainda não foi executado o crawl completo das páginas");
			return false;
		}
		System.out.println("Procurando pela palavra " + searchWord + "...");
		String bodyText = this.htmlDocument.body().text();
		return bodyText.toLowerCase().contains(searchWord.toLowerCase());
	}

	public List<String> getLinks() {
		return this.links;
	}

}