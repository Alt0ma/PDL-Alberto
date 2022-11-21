public class AnalizadorSintactico {

    private Tokens actualToken;
    private Lector lectura;
    private Escritor escritura;
    private AnalizadorLexico aLexico;
    private String rutaEntrada_text;
    private TS tabla_gene;
    private String lex;
    private String rutaAbsoluta;
    private RE_E reg_E;
    private Integer llaves_ab;


    public AnalizadorSintactico(String rutaEntrada, String rutaSalida) {

        lex = "";
        rutaEntrada_text =  rutaEntrada + "\\src\\nose.js";
        reg_E = new RE_E(rutaEntrada + "\\errores");
        lectura = new Lector(rutaEntrada_text);
        tabla_gene = new TS(rutaEntrada);
        rutaAbsoluta = rutaEntrada;
        escritura = new Escritor(rutaSalida);
        tabla_gene.crearTabla(rutaEntrada, lex);
        aLexico = new AnalizadorLexico(lectura, escritura, tabla_gene, reg_E);
        actualToken = aLexico.getTokens();

        compToken(actualToken);

        while (actualToken.getCodigo() != "EOF") {
            compToken(actualToken);
            nextToken();
        }
        System.out.println("Tokens generados. \n");
        tabla_gene.borrarTabla();
        System.out.println("Archivos creados en ruta: " + rutaSalida);
        System.out.println("Finalizado satisfactoriamente");
    }

    private void nextToken() {
        actualToken = aLexico.getTokens();
        if (actualToken == null) {
            nextToken();
        }
    }

    private void compToken(Tokens token){
        if(token.equals(Tokens.FUNCTION) ){
            nextToken();
            tabla_gene.crearTabla(rutaAbsoluta, aLexico.getString());
            llaves_ab = 1;
        }
   //     if()
        if(token.getCodigo() == "CaracterEspecial" && token.getAtrib() == (Integer) 5){
            tabla_gene.borrarTabla();
        }
    }
}
