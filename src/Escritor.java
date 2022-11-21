import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import javax.swing.text.Document;

public class Escritor {

    private String nombreFile;

    private Writer escritor_File;
    private Writer escritor_Cons;
    private Document escritor_win;

    public Escritor(String nombre) {
        nombreFile = nombre;

        try {
            escritor_File = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(nombreFile + ".txt"), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO: handle exception

        } catch (FileNotFoundException e) {
            // TODO: handle exception
        }
    }

    private void writeConsole(Object mensaje) throws IOException {
        if (escritor_Cons != null) {
            escritor_Cons.write(mensaje + System.lineSeparator());
            escritor_Cons.flush();
        } else if (this.escritor_win != null) {
            try {
                escritor_win.insertString(escritor_win.getLength(), mensaje + System.lineSeparator(), null);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    public boolean write(Object mensaje) {
        boolean estado = false;
        try {

             escritor_File.write(mensaje + "\n");
             escritor_File.flush();
             writeConsole(mensaje);
            estado = true;
        } catch (IOException e) {
            // CHECK:
            // e.printStackTrace();
        }
        return estado;
    }

}
