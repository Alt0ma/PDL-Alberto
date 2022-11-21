public class RE_E{
	@SuppressWarnings("unused")
	private String mensaje;
	@SuppressWarnings("unused")
	private int linea;
	private Escritor error;
	public RE_E(String msg){
		error = new Escritor(msg);
	}
	public void write(String msg){
		error.write(msg);
	}
}