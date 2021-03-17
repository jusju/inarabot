package engine;

/**
 * Rajapintaluokka, joka määrittelee mitkä kaikki metodit sellaisen luokan
 * tulee toteuttaa, ken väittää olevansa aito kello.
 * 
 * Toteuttavat luokat toimivat tietyllä aikavyöhykkeellä
 * ja muotoilevat ajan paikallisen tavan mukaisesti.
 */
public interface Kello {

	/**
	 * Palautta kellonajan merkkijonona 
	 * @return
	 */
	public String getAika();
	public String getPvm(); 
}
