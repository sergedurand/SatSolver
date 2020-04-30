package tools;

public class Tools {

	public Tools() {
		super();
	}
	
	/**
	 * print an interpretation
	 * @param interpretation
	 */
	public static void printInterpration(int[] interpretation) {
		String res = "";
		for(int i = 0;i<interpretation.length;i++) {
			res += "x"+i+" = " + interpretation[i];
			if(i<interpretation.length-1) {
				res += ", ";
			}
		}
		System.out.println(res);
	}

}

