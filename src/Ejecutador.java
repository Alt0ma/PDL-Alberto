import java.io.File;

public class Ejecutador {

	public static void main(String[] args) {
		File miDir = new File(".");
		String pathAbsoluto = "";
		try {
			pathAbsoluto = miDir.getCanonicalPath();
		} catch (Exception e) {
			e.printStackTrace();
		}

		@SuppressWarnings("unused")
		AnalizadorSintactico analizaS = new AnalizadorSintactico(pathAbsoluto, pathAbsoluto + "//src//token");
	}

}
