import java.util.HashMap;
import java.util.Map;

public class TS {

    private Integer tablaActId;
    private Tabla_Sim lastTS;
    private Map<Integer, Integer> bloqTS;
    private Map<Integer, Tabla_Sim> listTS;
    private Escritor TS_wr;

    public TS(String ruta_completa) {
        Tabla_Sim.N_tablas = 0;
        IntroTS.id_Intro = 1;

        bloqTS = new HashMap<Integer, Integer>();
        listTS = new HashMap<Integer, Tabla_Sim>();

        tablaActId = -1;
        TS_wr = new Escritor(ruta_completa + "\\tabla");
    }

    public void crearTabla(String ruta_compl, String lexema) {
        Tabla_Sim ts = new Tabla_Sim(lexema);
        listTS.put(ts.getID(), ts);
        bloqTS.put(ts.getID(), tablaActId);
        tablaActId = ts.getID();
    }

    public Tabla_Sim getTabAct() {
        return listTS.get(tablaActId);
    }

    public void borrarTabla() {
        TS_wr.write(getTabAct().toString());
        listTS.remove(tablaActId);
        tablaActId = bloqTS.get(tablaActId);
    }

    public IntroTS searchIntro_ID(Integer intro_id) {
        IntroTS in = null;

        Integer id_tabla = tablaActId;
        lastTS = listTS.get(id_tabla);
        in = lastTS.searchbyID(id_tabla);
        while (in == null) {
            id_tabla = bloqTS.get(id_tabla);
            if (id_tabla == null) {
                break;
            }
            lastTS = listTS.get(id_tabla);
            if (lastTS == null) {
                break;
            }
            in = lastTS.searchbyID(id_tabla);
        }
        return in;
    }

    public IntroTS searchIntrobyLex(String lex) {
        IntroTS in = null;

        Integer id_tabla = tablaActId;
        Integer tabla_sub_0 = tablaActId;
        lastTS = listTS.get(id_tabla);
        in = lastTS.searchbyLex(lex);
        while (in == null) {
            id_tabla = bloqTS.get(id_tabla);
            if (id_tabla == null) {
                break;
            }
            lastTS = listTS.get(id_tabla);
            if (lastTS == null) {
                break;
            }
            in = lastTS.searchbyLex(lex);
        }
        if (in != null && in.getTable() == 0 &&
                in.getTable() != tabla_sub_0.intValue() &&
                in.getDesp().intValue() > -1) {
            in = null;
        }
        return in;
    }

    public IntroTS insertIntro(String lex) {
        Tabla_Sim table = listTS.get(tablaActId);
        return table.insert(lex, table.getID());
    }

    public void insertTypeTS(Integer id_intro, Types type, Integer tam) {
        IntroTS in = searchIntro_ID(id_intro);
        if (in != null) {
            in.setType(type);
            if (tam != null) {
                in.setDesp(lastTS.getLastMov());
                lastTS.setLastMov(lastTS.getLastMov() + tam);
            }
        }
    }

    public Object searchType(Integer id_intro) {
        Object type = "";
        IntroTS in = searchIntro_ID(id_intro);
        if (in != null) {
            type = in.getType();
        }
        return type;
    }

}

// Class de IntroTS
class IntroTS {
    protected static Integer id_Intro = 1;

    private String lex;
    private Types type;
    private Integer desp;
    private Integer table;
    private Integer id;

    public IntroTS(String lexema) {
        id = IntroTS.id_Intro;
        lex = lexema;
        type = new Types();
        desp = -1;
        table = -1;

        IntroTS.id_Intro += 1;
    }

    public Integer getTable() {
        return table;
    }

    public void setTable(Integer tabla) {
        table = tabla;
    }

    public IntroTS(Integer id, String lex) {
        this.id = id;
        this.lex = lex;
        type = new Types();
        desp = -1;
    }

    public Integer getID() {
        return id;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public String getLex() {
        return lex;
    }

    public void setLex(String lexema) {
        lex = lexema;
    }

    public Object getType() {
        return type;
    }

    public void setType(Types tipo) {
        type = tipo;
    }

    public Integer getDesp() {
        Integer aux;
        if (desp == null) {
            aux = null;
        } else {
            aux = desp;
        }

        return aux;
    }

    public void setDesp(Integer desp) {
        this.desp = desp;
    }
    @Override
    public String toString(){
        String resp = "* LEXEMA: '" + lex + "'" + '\n' + "  +tipo : " + "''"; 
        return resp; 
    }
/*
    @Override
    public String toString() {
        if (desp >= 0) {
            return String.format( "* LEXEMA: '%s'%n   ATRIBUTOS:%n   + tipo: '%s'%n   + desplazamiento: %d",
                    lex, ' ', desp);
        } else {
            return String.format( "* LEXEMA: '%s'%n   ATRIBUTOS:%n   + tipo: '%s'", lex, ' ');
        }
    }
*/
}

// Class de Tabla_Sim
class Tabla_Sim {
    protected static Integer N_tablas = 0;

    private Map<String, Integer> index;
    private Map<Integer, IntroTS> into;
    private Integer id;
    private Integer last_desp;
    private String lex;

    public Tabla_Sim(String lexema) {
        lex = lexema;
        setID(Tabla_Sim.N_tablas);
        index = new HashMap<String, Integer>();
        into = new HashMap<Integer, IntroTS>();
        last_desp = 0;

        Tabla_Sim.N_tablas++;

    }

    public Integer getID() {
        return id;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public Integer getLastMov() {
        return last_desp;
    }

    public void setLastMov(Integer last_mov) {
        last_desp = last_mov;
    }

    public IntroTS insert(String lex, int tabla) {
        IntroTS intro = new IntroTS(lex);
        intro.setTable(tabla);
        index.put(lex, intro.getID());
        into.put(intro.getID(), intro);

        return intro;
    }

    public IntroTS searchbyID(Integer id) {
        return into.get(id);
    }

    public IntroTS searchbyLex(String lex) {
        Integer id_intro = index.get(lex);
        if (id_intro != null) {
            return into.get(id_intro);
        }

        return null;
    }

    @Override
    public String toString() {
        if (id > 0) {
            String string_tb = "#"+id + ':' + '\n';
 //           String string_tb = String.format(/*"Tabla de la FUNCION %s # %d:%n", lex, id*/ "#%d:/n", id);
            for (IntroTS intro : into.values()) {
                string_tb += String.format("%s" + '\n', intro);
            }
            return string_tb;
        } else {
            String string_tb = "#"+id + ':' + '\n';
//            String string_tb = String.format(/*"Tabla Principal # %d:%n", id*/ "#%d:/n", id);
            for (IntroTS intro : into.values()) {
                string_tb += String.format("%s" + '\n', intro);
            }
            return string_tb;
        }
    }
}
