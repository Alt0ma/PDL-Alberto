import java.util.HashMap;
import java.util.Map;

public class AnalizadorLexico {

    private Map<String, Integer> pal_Res;
    private Map<String, Integer> simbolos;
    private Map<String, Integer> car_Especial;
    private TS tabla_mnp;
    private Lector lector;
    private Escritor token_a_escr;
    private char char_actual;
    private String string_actual;
    private int num_actual;
    private RE_E controler;

    public String getString() {
        return string_actual;
    }

    public Lector getFileLeido() {
        return lector;
    }

    public void setFileLeido(Lector fileReader) {
        lector = fileReader;
    }

    public void write(String s) {
        token_a_escr.write(s);
    }

    public void leeChar() {
        char_actual = getFileLeido().read();
    }

    private void concatValue() {
        string_actual = String.valueOf(string_actual) + char_actual;
    }

    private void valorReal() {
        concatValue();
        num_actual = num_actual * 10 + (char_actual - '0');
    }

    // Es un delimitador?
    private boolean esDel(char c) {
        if (c == ' ' || c == '\n' || c == '\t' || c == '\r' || c == '\0') {
            return true;
        }
        return false;
    }

    // Es un numero?
    private boolean esNum(char c) {
        if (c >= '0' && c <= '9') {
            return true;
        }
        return false;
    }

    // Es una letra?
    private boolean esLet(char c) {
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            return true;
        }
        return false;
    }

    // Es un caracter Escpecial?
    private boolean charEspec(char c) {
        if (c == '(' || c == ')' || c == '{' || c == '}' || c == ',' || c == ';') {
            return true;
        }
        return false;
    }

    // Genera el token
    private Tokens genToken(String code, Object atrib) {
        Tokens token = new Tokens(code, atrib);
        if (code.equals("Cad")) {
            token.setValor(string_actual);
        }
        if (code.equals("Num")) {
            token.setValor(num_actual);
        }
        token_a_escr.write(token.toString());
        return token;
    }

    // Iniciamos las tablas
    private void inicializaTabla() {
        pal_Res = new HashMap<String, Integer>();
        pal_Res.put("boolean", 0);
        pal_Res.put("else", 1);
        pal_Res.put("function", 2);
        pal_Res.put("if", 3);
        pal_Res.put("input", 4);
        pal_Res.put("int", 5);
        pal_Res.put("let", 6);
        pal_Res.put("print", 7);
        pal_Res.put("return", 8);
        pal_Res.put("string", 9);
        pal_Res.put("false", 10);
        pal_Res.put("true", 11);

        simbolos = new HashMap<String, Integer>();
        simbolos.put("*=", 0);
        simbolos.put("=", 1);
        simbolos.put("+", 2);
        simbolos.put("*", 3);
        simbolos.put("&&", 4);
        simbolos.put("<", 5);
        simbolos.put(">", 6);
        simbolos.put("-", 7);

        car_Especial = new HashMap<String, Integer>();
        car_Especial.put(",", 0);
        car_Especial.put(";", 1);
        car_Especial.put("(", 2);
        car_Especial.put(")", 3);
        car_Especial.put("{", 4);
        car_Especial.put("}", 5);

    }

    public Tokens getTokens() {
        Tokens token = new Tokens("NULL");
        string_actual = "";
        num_actual = 0;
        if (!esDel(char_actual)) {
            // Vemos que tipo de comentario puede ser: primero si es del tipo /* ... */
            if (char_actual == '/') {
                concatValue();
                leeChar();
                if (char_actual == '*') {
                    while (char_actual != '/') {
                        while (char_actual != '*') {
                            leeChar();
                        }
                        leeChar();
                    }
                    leeChar();
                    return getTokens();
                }
                else if (char_actual == '/'){
					leeChar();
					while (char_actual != '\r'&&char_actual !='\u0000') {
						leeChar();
					}
					leeChar();
					return getTokens();
				}
                //Parte del Gestor de Errores
                while(char_actual != ' ' && char_actual != '\r'){
                    concatValue();
                    leeChar();
                }
                controler.write(String.format("Error en Analizador Lexico: Linea %d, Comentario de bloque no valido \"%s\".",
                                getFileLeido().getLineAc(), string_actual));
                string_actual = "";
            }

            // ahora vemos si es del tipo '...'
            if (char_actual == '\'') {
                leeChar();
                int cont_char = 0;                                                      //Se guarda un cont, porque las cadenas no pueden ser mayores que 64 caractere y de paso para el gestor de errores.
                while (char_actual != '\'' && cont_char < 65 && char_actual != '\r') {
                    concatValue();
                    leeChar();
                    cont_char++;
                }
                if(char_actual == '\''){
                    leeChar();
                    return genToken("Cad", string_actual);
                }
                controler.write(String.format("Error en Analizador Lexico: Linea %d, Cadena \'%s\' no permitida.",
                                getFileLeido().getLineAc(), string_actual));
                string_actual = "";
            }

            // Vemos si se trata de una Cadena
            else if (char_actual == '\"') {
                concatValue();
                leeChar();
                int cont = 0;
                while (char_actual != '\"' && char_actual != '\r' && cont < 65) {
                    concatValue();
                    leeChar();
                    cont++;
                }
                if (char_actual == '\"') {
                    leeChar();
                    return genToken("Cad", string_actual);
                }
                controler.write(String.format("Error en Analizador Lexico: Linea %d, Cadena \'%s\' no permitida.",
                                getFileLeido().getLineAc(), char_actual));
                string_actual = "";
            }

            // Vemos ahora si se trata de una variable
            else if (esLet(char_actual)) {
                concatValue();
                leeChar();
                while (char_actual == '_' || esLet(char_actual) || esNum(char_actual)) {
                    concatValue();
                    leeChar();
                }
                Integer codePalRes = pal_Res.get(string_actual);
                if (codePalRes != null) {
                    return genToken("PalRes", codePalRes);
                } 
                else {
                    IntroTS intro = tabla_mnp.searchIntrobyLex(string_actual);
                    if (intro == null) {
                        intro = tabla_mnp.insertIntro(string_actual);
                    }
                    return genToken("Id", intro.getID());
                }
            }

            // Vemos si se trata de un Numero
            else if (esNum(char_actual)) {
                valorReal();
                leeChar();
                while (esNum(char_actual) && num_actual <= 32767) {
                    valorReal();
                    leeChar();
                }
                if (num_actual <= 32767) {
                    return genToken("Num", num_actual);
                }
                controler.write(String.format("Error en Analizador Lexico: Linea %d, Numero no permitido \"%d\".",
                                getFileLeido().getLineAc(), num_actual));
            }

            // Vemos si se trata de Operadores
            else {
                if (char_actual == '=') {
                    concatValue();
                    leeChar();
                    if(char_actual == '='){
                        controler.write(String.format("Error en Analizador Lexico: Linea %d, El OPERADOR de IGUALDAD \"==\" no esta permitido",
                                    getFileLeido().getLineAc()));

                    }
                    return genToken("Operador", simbolos.get(string_actual));
                }
                if (char_actual == '+') {
                    concatValue();
                    leeChar();
                    if(char_actual == '+'){
                        controler.write(String.format("Error en Analizador Lexico: Linea %d, El OPERADOR de INCREMENTO \"++\" no esta permitido",
                                    getFileLeido().getLineAc()));

                    }
                    return genToken("Operador", simbolos.get(string_actual));
                }
                if (char_actual == '-') {
                    concatValue();
                    leeChar();
                    if(char_actual == '-'){
                        controler.write(String.format("Error en Analizador Lexico: Linea %d, El OPERADOR de DECREMENTOº \"--\" no esta permitido",
                                    getFileLeido().getLineAc()));

                    }
                    return genToken("Operador", simbolos.get(string_actual));
                }
                if (char_actual == '&') {
                    concatValue();
                    leeChar();
                    if (char_actual == '&') {
                        concatValue();
                        leeChar();
                        return genToken("Operador", simbolos.get(string_actual));
                    }
                    controler.write(String.format("Error en Analizador Lexico: Linea %d, Error en OPERADOR LOGICO expected another \'&\'.",
                                    getFileLeido().getLineAc()));
                }
                if (char_actual == '<') {
                    concatValue();
                    leeChar();
                    if(char_actual == '='){
                        controler.write(String.format("Error en Analizador Lexico: Linea %d, El OPERADOR RELACIONAL \"<=\" no esta permitido.",
                                    getFileLeido().getLineAc()));
                        return getTokens();
                    }
                    return genToken("Operador", simbolos.get(string_actual));
                }
                if (char_actual == '>') {
                    concatValue();
                    leeChar();
                    if(char_actual == '='){
                        controler.write(String.format("Error en Analizador Lexico: Linea %d, El OPERADOR RELACIONAL \">=\" no esta permitido.",
                                    getFileLeido().getLineAc()));
                        return getTokens();
                    }
                    return genToken("Operador", simbolos.get(string_actual));
                }
                if (char_actual == '*') {
                    concatValue();
                    leeChar();
                    if (char_actual == '=') {
                        concatValue();
                        leeChar();
                        return genToken("Operador", simbolos.get(string_actual));
                    } 
                    else {
                        return genToken("Operador", simbolos.get(string_actual));
                    }
                } 
                else {
                    if (charEspec(char_actual)) {
                        concatValue();
                        leeChar();
                        return genToken("CaracterEspecial", car_Especial.get(string_actual));
                    }
                }
                while(char_actual != ' ' && char_actual != '\r'){
                    concatValue();
                    leeChar();
                }
                controler.write(String.format("Error en Analizador Lexico: Linea %d, Caracter no esperado \"%s\".",
                getFileLeido().getLineAc(), string_actual));
                string_actual = "";
            }
            return token;
        }
        if (char_actual == '\t') {
            leeChar();
            return getTokens();
        }
        if (char_actual == '\0') {
            token = genToken("EOF", null);
            getFileLeido().close();
            return token;
        }
        leeChar();
        return getTokens();
    }

    public AnalizadorLexico(Lector file_reader, Escritor token_wr, TS tabla_gene, RE_E errores) {
        inicializaTabla();
        setFileLeido(file_reader);
        token_a_escr = token_wr;
        controler = errores;
        tabla_mnp = tabla_gene;

        leeChar();

    }

}
