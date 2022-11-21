public class Tokens {

    private String codigo;
    private Object atributo;
    private Object valor;
    private String nombre;

    // Tokens sin atributo
    public Tokens(String code) {
        codigo = code;
        atributo = null;
        valor = code;
    }

    // Tokens con atributo especial, (,),{,},...
    public Tokens(String code, Object atrib) {
        codigo = code;
        atributo = atrib;
        valor = code;
    }

    public Tokens(String code, Object atrib,String nombre) {
        codigo = code;
        atributo = atrib;
        valor = code;
        this.nombre=nombre;
    }

    // Tokens palabras reservadas y operadores
    public Tokens(String code, int atrib) {
        codigo = code;
        atributo = atrib;
    }
    public String getNombre(){
        return nombre;
    }
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String code) {
        codigo = code;
    }

    public Object getAtrib() {
        return atributo;
    }

    public void setAtrib(String atrib) {
        atributo = atrib;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object value) {
        valor = value;
    }

    // Definimos los Tokens
    public static Tokens Id = new Tokens("Id");
    public static Tokens Cad = new Tokens("Cad");
    public static Tokens Num = new Tokens("Num");

    public static Tokens BOOL = new Tokens("PalRes", 0);
    public static Tokens ELSE = new Tokens("PalRes", 1);
    public static Tokens FUNCTION = new Tokens("PalRes", 2);
    public static Tokens IF = new Tokens("PalRes", 3);
    public static Tokens INPUT = new Tokens("PalRes", 4);
    public static Tokens INT = new Tokens("PalRes", 5);
    public static Tokens LET = new Tokens("PalRes", 6);
    public static Tokens PRINT = new Tokens("PalRes", 7);
    public static Tokens RETURN = new Tokens("PalRes", 8);
    public static Tokens STRING = new Tokens("PalRes", 9);
    public static Tokens FALSE = new Tokens("PalRes", 10);
    public static Tokens TRUE = new Tokens("PalRes", 11);

    public static Tokens Asig_Con_Mul = new Tokens("Operador", 0);
    public static Tokens Asignacion = new Tokens("Operador", 1);
    public static Tokens Suma = new Tokens("Operador", 2);
    public static Tokens Mul = new Tokens("Operador", 3);
    public static Tokens And = new Tokens("Operador", 4);
    public static Tokens Menor = new Tokens("Operador", 5);
    public static Tokens Mayor = new Tokens("Operador", 6);
    public static Tokens Resta = new Tokens("Operador", 7);

    public static Tokens Coma = new Tokens("CaracterEspecial", 0);
    public static Tokens Punto_Y_Coma = new Tokens("CaracterEspecial", 1);
    public static Tokens Parentesis_I = new Tokens("CaracterEspecial", 2);
    public static Tokens Parentesis_D = new Tokens("CaracterEspecial", 3);
    public static Tokens Llave_I = new Tokens("CaracterEspecial", 4);
    public static Tokens Llave_D = new Tokens("CaracterEspecial", 5);

    public static Tokens EOF = new Tokens("EOF");

    // Comprobador de Token
    public boolean equals(Object object) {
        boolean es_igual = false;
        if (object != null) {
            if (object instanceof Tokens) {
                Tokens token = (Tokens) object;
                switch (token.codigo) {
                    case "Id":
                    case "Cad":
                    case "Num":
                    case "EOF":
                        es_igual = codigo == token.codigo;
                        break;
                    default:
                        if (codigo == token.codigo) {
                            es_igual = atributo.equals(token.atributo);
                        }
                        break;
                }
            }
        }
        return es_igual;
    }

    // Formato pedido
    public String toString() {
        if (atributo == null) {
            return "<" + codigo + ", >";
        }
        return "<" + codigo + ", " + atributo + ">";
    }
}
