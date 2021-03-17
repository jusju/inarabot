package engine;

public class KelloRunner {

	public static void main(String[] args) {
		Kello kello = new SuomiKello(new SysteemiKelloLahde());
		System.out.println(kello.getPvm());
		System.out.println(kello.getAika());


	}

}
