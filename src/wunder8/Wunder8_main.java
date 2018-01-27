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
	 * Muodostaa 80 merkkisi� rivej� niin, ett� otetaan aina ensin pisin mahdollinen sana, jonka per��n laitetaan pisin mahdollinen sana
	 * 
	 * Jos tulee tilanne, ett� j�ljell� olevista sanoista ei saada 80 merkkisi� niin siirryt��n rekursioon, jossa poistetaan aina edellinen
	 * sana ja yritet��n etsi� sen per��n mahdollinen yhdistelm�. Yritetyt yhdistelm�t pidet��n muistissa "ArrayList<String>" -tietorakenteessa.
	 * Sanoja poistetaan niin kauan kunnes toimiva yhdistelm� l�ytyy. Esimerkiksi jos on tilanne:
	 * asd asd as as as as as as as as as as as as as as as as as as as as as as as as = 81 merkki�, aletaan poistamaan edellisi� sanoja:
	 * asd asd as as as as as as as as as as as as as as as as as as as as as as as
	 * asd asd as as as as as as as as as as as as as as as as as as as as as as
	 * ...
	 * asd asd as as as
	 * asd asd as as
	 * asd asd as
	 * asd asd
	 * asd
	 * 
	 * ja nyt yritet��n etsi� rekursiolla koko 80 merkkiseen rakoon (gap) yhdistelmi�. T�m�n olisi tietysti voinut ratkaista antamalla heti
	 * rekursiolle tuon 80 pituisen merkkijonon t�ytett�v�ksi, mutta ainakin nykyinen ratkaisu on paljon tehokkaampi kuin pelk�n rekursion k�ytt�.
	 * 
	 * Testasin t�t� my�s toisella kirjalla, jossa oli paljon 1 merkin pituisia "sanoja". L�ysin sen ansiosta bugin, jota ei tuolla 
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
