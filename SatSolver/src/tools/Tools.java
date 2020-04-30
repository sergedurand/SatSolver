package tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
			res += "x"+(i+1)+" = " + interpretation[i];
			if(i<interpretation.length-1) {
				res += ", ";
			}
		}
		System.out.println(res);
	}
	
	
	public static List<String> listFiles(String folder_path){
		List<String> res = new ArrayList<String>();
		try (Stream<Path> walk = Files.walk(Paths.get(folder_path))) {

			res = walk.map(x -> x.toString())
					.filter(f -> f.endsWith(".cnf")).collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

}

