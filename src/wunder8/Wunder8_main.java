package wunder8;

import wunder8.BookRefactorer;
import wunder8.Reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Wunder8_main {
	
	/**
	 * Wundernut 8 ratkaisu
	 * 
	 * Ratkaisun idea:
	 * Sortata ensin kaikki sanat hashMap<Integer, ArrayList<String>> tietorakenteeseen, jossa key = sanan pituus, value = kaikki keyn pituiset sanat
	 * 
	 * Muodostaa 80 merkkisiä rivejä niin, että otetaan aina ensin pisin mahdollinen sana, jonka perään laitetaan pisin mahdollinen sana
	 * 
	 * Jos tulee tilanne, että jäljellä olevista sanoista ei saada 80 merkkisiä niin siirrytään rekursioon, jossa poistetaan aina edellinen
	 * sana ja yritetään etsiä sen perään mahdollinen yhdistelmä. Yritetyt yhdistelmät pidetään muistissa "ArrayList<String>" -tietorakenteessa.
	 * Sanoja poistetaan niin kauan kunnes toimiva yhdistelmä löytyy. Esimerkiksi jos on tilanne:
	 * asd asd as as as as as as as as as as as as as as as as as as as as as as as as = 81 merkkiä, aletaan poistamaan edellisiä sanoja:
	 * asd asd as as as as as as as as as as as as as as as as as as as as as as as
	 * asd asd as as as as as as as as as as as as as as as as as as as as as as
	 * ...
	 * asd asd as as as
	 * asd asd as as
	 * asd asd as
	 * asd asd
	 * asd
	 * 
	 * ja nyt yritetään etsiä rekursiolla koko 80 merkkiseen rakoon (gap) yhdistelmiä. Tämän olisi tietysti voinut ratkaista antamalla heti
	 * rekursiolle tuon 80 pituisen merkkijonon täytettäväksi, mutta ainakin nykyinen ratkaisu on paljon tehokkaampi kuin pelkän rekursion käyttö.
	 * 
	 * Testasin tätä myös toisella kirjalla, jossa oli paljon 1 merkin pituisia "sanoja". Löysin sen ansiosta bugin, jota ei tuolla 
	 * alaston_salissa.txt tullut vastaan.
	 */
	public static void main(String[] args) {
		Reader reader = new Reader();
		try {
			HashMap<Integer, ArrayList<String>> bookMap = reader.readFile();

			BookRefactorer bookRefactorer = new BookRefactorer(bookMap);
			bookRefactorer.refactorBook();

			ArrayList<String> book = bookRefactorer.getBook();
			reader.writeBook(book);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
