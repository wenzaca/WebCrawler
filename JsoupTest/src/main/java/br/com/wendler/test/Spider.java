package br.com.wendler.test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Spider {
	private static final int MAX_PAGES_TO_SEARCH = 10;
	private Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVisit = new LinkedList<String>();

	/**
	 * Início da crawl pela palavra
	 * 
	 * @param url
	 *            - Primeiro URL que será iniciado a pesquisa
	 * @param searchWord
	 *            - Palavra que se deseja buscar
	 */
	public void search(String url, String searchWord) {
		while (this.pagesVisited.size() < MAX_PAGES_TO_SEARCH) {
			String currentUrl;
			SpiderLeg leg = new SpiderLeg();
			if (this.pagesToVisit.isEmpty()) {
				currentUrl = url;
				this.pagesVisited.add(url);
			} else {
				currentUrl = this.nextUrl();
			}
			leg.crawl(currentUrl); 
			boolean success = leg.searchForWord(searchWord);
			if (success) {
				System.out.println(String.format("**Successo** Palavra %s encontrada %s", searchWord, currentUrl));
				break;
			}
			this.pagesToVisit.addAll(leg.getLinks());
		}
		System.out.println("\n**Done** Visited " + this.pagesVisited.size() + " web page(s)");
	}

	/**
	 * Returna a proxima URL que será acessada e verifica se a mesma ja não foi visitada
	 * 
	 * @return
	 */
	private String nextUrl() {
		String nextUrl;
		do {
			nextUrl = this.pagesToVisit.remove(0);
		} while (this.pagesVisited.contains(nextUrl));
		this.pagesVisited.add(nextUrl);
		return nextUrl;
	}
}