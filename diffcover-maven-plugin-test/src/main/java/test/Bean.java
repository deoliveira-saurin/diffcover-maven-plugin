package test;

/**
 * @author U00I168
 * @version 1.0
 */
@Deprecated
public class Bean {

	private String attr;

	public String toto() {
		return "test";
	}

	public String get() {
		Boolean test = true;
		String result = "test";
		if (test) {
			attr = result;
		} else {
			System.out.println();
		}
		return attr;
	}

	public String titi() {
		return "test";
	}

	public String tutu() {
		return null;
	}
}