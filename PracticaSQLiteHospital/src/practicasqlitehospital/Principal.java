package practicasqlitehospital;

/**
 *
 * @author Daniel Marcos Lorrio
 */
public class Principal {

    /**
     * Metodo principal que llama al constructor y muestra la ventana creada
     *
     * @param args
     */
    public static void main(String args[]) {
        Vista vista = new Vista(); // Invocamos al constructor de Vista
        Modelo modelo = new Modelo(); // Invocamos al constructor de Modelo

        new Controlador(vista, modelo);
    }
}
