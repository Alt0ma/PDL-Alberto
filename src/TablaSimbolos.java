import java.util.ArrayList;
import java.util.Map;

public class TablaSimbolos {
    private boolean enDeclaracion=false, enDeclarado=false, enPrincipal=true;
    private boolean enDeclaradoFuncion=false,enFuncionTipoDevuelve=false;
    private boolean enTipoParamentros=false,enNombreParametros=false;
    private String variableAux,tipoAux;
    private String palabraActual;
    private int desplzPrincipal,desplzFuncion,contadorLlave;
    private TSColumna columnaActual;
    private ArrayList<TSColumna>  TSPrincipal=new ArrayList<TSColumna>();
    private ArrayList<TSColumna>  TSFuncion=new ArrayList<TSColumna>();
    private ArrayList<ArrayList<TSColumna>> TStotal=new ArrayList<ArrayList<TSColumna>>();

    public ArrayList<ArrayList<TSColumna>> generarTabla(ArrayList<Tokens> tokensGenerados) {
        // DECLARAR: let
        // TIPOS:    boolean, input, int, string
        // FUNCION: function-nombre-tipo-(argumentos)tipo-nombre..
        // Lexema Tipo Direc NúmParám TipoParam ModoPaso TipoDev Etiq
        Tokens tokenAux;

        for (Tokens tokenActual:tokensGenerados){
            palabraActual=tokenActual.getNombre();
            // Empezamos a comprobar
            if(enPrincipal){
                if(!tokenActual.getCodigo().equals("CaracterEspecial")||palabraActual.equals(")")||palabraActual.equals("(")){
                    // Aqui seria en la tabla principal
                    if(enDeclaracion){ // Si lo anterior ha sido LET, osea que no era funcion
                        //añadir 'tokenActual' a token
                        variableAux=palabraActual;
                        enDeclaracion=false;
                        enDeclarado=true;
                    }
                    else if(enDeclarado&&!palabraActual.equals(")")&&!palabraActual.equals("(")){ // Si lo anterior ha sido el nombre de la variable
                        tipoAux=palabraActual;
                        //Generar en TS con 'variableAux' con el tipo 'tipoAux'
                        TSPrincipal.add(new TSColumna(variableAux,tipoAux,desplzPrincipal));
                        enDeclarado=false;
                    }
                    else if (enDeclaradoFuncion&&!palabraActual.equals(")")&&!palabraActual.equals("(")) {
                        variableAux=palabraActual;
                        columnaActual=new TSColumna(variableAux,"function");
                        enDeclaradoFuncion=false;
                        enFuncionTipoDevuelve=true;
                    }
                    else if(enFuncionTipoDevuelve&&!palabraActual.equals(")")){
                        if(palabraActual.equals("(")){
                            columnaActual.addDev("NULL");
                        } else{
                            columnaActual.addDev(palabraActual);
                        }
                        enFuncionTipoDevuelve=false;
                        enTipoParamentros=true;
                    }
                    else if (enTipoParamentros&&!palabraActual.equals("(")){
                        if(palabraActual.equals(")")){
                            enPrincipal=false; //Estamos dentro de una funcion
                            enTipoParamentros=false;
                            TSPrincipal.add(columnaActual); // Anadimos paramentros a la TSPrincipal
                        }
                        else {
                        tipoAux=palabraActual;
                        columnaActual.addParam(tipoAux,"ModoParm");
                        enTipoParamentros=false;
                        enNombreParametros=true;
                        }
                    }
                    else if (enNombreParametros){
                        variableAux=palabraActual;
                        TSFuncion.add(new TSColumna(variableAux,tipoAux)); // Anadimos a TSFuncion
                        enNombreParametros=false;
                        enTipoParamentros=true;
                    }
                    if(palabraActual.equals("let")){ // Si lo actual es LET
                        enDeclaracion=true;
                    }
                    else if(palabraActual.equals("function")){ // Si lo actual es FUNCTION
                        enDeclaradoFuncion=true;
                        TSFuncion=new ArrayList<TSColumna>();
                        contadorLlave=0;
                    }
                }
            }
            else {
                //_______________________________________________________________
                // Gestion funcion
                if(palabraActual.equals("{")||palabraActual.equals("}")||!(tokenActual.getCodigo().equals("CaracterEspecial"))){ //Filtro para coger palabras relevantes
                    // Gestion de llaves y fin de funcion
                    if(palabraActual.equals("}")){
                        contadorLlave--;
                    }
                    else if(palabraActual.equals("{")){
                        contadorLlave++;
                    }
                    if(contadorLlave==0){
                        enPrincipal=true;
                        TStotal.add(TSFuncion);
                    }
                    else if(!tokenActual.getCodigo().equals("CaracterEspecial")){
                        if(enDeclaracion){ // Si lo anterior ha sido LET, osea que no era funcion
                            //añadir 'tokenActual' a token
                            variableAux=palabraActual;
                            enDeclaracion=false;
                            enDeclarado=true;
                        }
                        else if(enDeclarado){ // Si lo anterior ha sido el nombre de la variable
                            tipoAux=palabraActual;
                            enDeclarado=false;
                            //Generar en TS con 'variableAux' con el tipo 'tipoAux'
                            TSFuncion.add(new TSColumna(variableAux,tipoAux,desplzFuncion));
                            enDeclarado=false;
                        }
                        if(palabraActual.equals("let")){ // Si lo actual es LET
                            enDeclaracion=true;
                        }
                    }
                }
            }
        }
        TStotal.add(TSPrincipal);
        return TStotal;
    }

}

