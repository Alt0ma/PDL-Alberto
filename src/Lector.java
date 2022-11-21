import java.io.IOException;

import java.io.File;
import java.io.FileInputStream;

public class Lector {
    private FileInputStream lector;
    private int linea_actual;
    // LECTOR GITHUB 2!
    // Iniciamos el Lector, pero primero hacemos comprobaciones.
    public Lector(String filePath) {
        File doc = new File(filePath);
        if (!doc.exists()) {
            System.out.println(String.format("El fichero %s no existe", filePath));
            return;
        }
        if (!doc.isFile() || !doc.canRead()) {
            System.out.println(String.format("El fichero %s no se puede leer. Revise bien la ubicacion", filePath));
            return;
        }
        try {
            lector = new FileInputStream(doc);
        } catch (IOException e) {
            // TODO: handle exception
        }
        linea_actual = 1;
    }

    // Método para que avance de caracter en caracter y si llega al final cambiar de
    // linea.
    public char read() {
        char caracter_actual = '\0';
        int c;
        try {
            if ((c = lector.read()) != -1) {
                caracter_actual = (char) c;
                //System.out.println(caracter_actual);
   //             caracter_actual = (char) lector.read();
                if (caracter_actual == '\n') {
                    ++this.linea_actual;
                }
            }
        } catch (IOException e) {
            // TODO: handle exception
        }
        return caracter_actual;
    }

    // ¿Esta cerrado el doc?
    public boolean close() {
        boolean estado = false;
        try {
            lector.close();
            estado = true;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return estado;
    }

    public FileInputStream getFile() {
        return lector;
    }

    public int getLineAc() {
        return linea_actual;
    }
}
