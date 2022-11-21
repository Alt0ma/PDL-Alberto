import java.util.HashMap;
import java.util.Map;

public class TSColumna {

    private String lexema, tipo, tipoParam, tipoDev, etiq,modoParam;
    private int direc=-1, numParm=0;
    private Map<String, String> parametros = new HashMap<String, String>();

    public TSColumna (String variable,String tipo,int desplz){
        lexema=variable;
        this.tipo=tipo;
        direc=desplz;
    }
    public TSColumna (String variable,String tipo){
        lexema=variable;
        this.tipo=tipo;
    }
    public void addParam(String tipo,String modoParam){
        parametros.put(tipo,modoParam);
        numParm++;
    }
    public void addDev(String tipo){
        tipoDev=tipo;
    }
    public String toString(){
        String res="* LEXEMA : '"+lexema+"'"+"\n"+"  + tipo : '"+"'";
        return res;
    }
    public String toStringSintactico(){
        String res=lexema+", "+tipo +", -";//", "+numParm;
        if(tipo.equals("function")){
            res+=", devuelve: "+tipoDev+",num_param: "+numParm+"\n";
            for (Map.Entry<String, String> me :parametros.entrySet()) {
                res+="    "+me.getKey()+", "+me.getValue()+"\n";
            }
        }
        return res;
    }
}
