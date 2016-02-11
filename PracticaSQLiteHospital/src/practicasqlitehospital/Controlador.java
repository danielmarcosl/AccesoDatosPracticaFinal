package practicasqlitehospital;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 *
 * @author Daniel Marcos Lorrio
 */
public class Controlador implements ActionListener {

    // Declaramos un objeto para cada clase
    public static Vista vista = null;
    public static Modelo modelo = null;

    public static ArrayList<String> datos = new ArrayList<String>();
    public static ArrayList<String> paciente = new ArrayList<String>();

    public static String estado = "";
    public static String db = "hospital";

    /**
     * Constructor de la clase Controlador
     *
     * @param v Nuevo objeto Vista
     * @param m Nuevo objeto Modelo
     */
    public Controlador(Vista v, Modelo m) {
        this.vista = v;
        this.modelo = m;

        // Anadimos listeners a los botones
        vista.bAlta.addActionListener(this);
        vista.bBaja.addActionListener(this);
        vista.bMod.addActionListener(this);
        vista.bList.addActionListener(this);
        vista.bSalir.addActionListener(this);

        // Anadimos listener al campo de texto para que ejecute una accion al pulsar la tecla enter
        vista.campoTexto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ejecutamos una accion segun el estado
                modelo.opcion(vista.campoTexto.getText());
                // Borramos lo que este escrito
                vista.campoTexto.setText("");
            }
        }); // end actionListener

        // Hacemos visible los componentes de la ventana
        vista.setVisible(true);
    } // end Constructor

    /**
     * Metodo que recoge las pulsaciones de los botones y ejecuta una accion
     * dependiendo de cual sea pulsado
     *
     * @param e Boton pulsado
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.bSalir) { // Boton salir
            System.out.println("Se ha cerrado el programa");
            System.exit(0);
        } else if (e.getSource() == vista.bAlta) { // Boton alta
            vista.textarea.setText("Introduce los datos del alta nueva\n"
                    + "DNI: ");
            // Cambiamos el estado
            estado = "alta";
        } else if (e.getSource() == vista.bBaja) { // Boton baja
            vista.textarea.setText("Introduce el DNI o el Codigo del paciente que desea dar de baja.");
            // Cambiamos el estado
            estado = "baja";
        } else if (e.getSource() == vista.bMod) { // Boton modificar
            vista.textarea.setText("Introduce el DNI o el Codigo del paciente que desea modificar.");
            // Cambiamos el estado
            estado = "modificar";
        } else if (e.getSource() == vista.bList) { // Boton listado
            // Mostramos menu de listado
            vista.textarea.setText("Menu de listados"
                    + "\nEscribe PACIENTES para listar los pacientes"
                    + "\nEscribe HABITACIONES para listar las habitaciones"
                    + "\nEscribe HABITACIONESOCUPADAS para listar las habitaciones con un paciente"
                    + "\nEscribe HABITACIONESLIBRES para listar las habitaciones sin paciente");
            // Cambiamos el estado
            estado = "listado";
        } // end if

        // Vaciamos el ArrayList si se cambia de opcion
        modelo.vaciarArray();
    } // end actionPerformed
} // end main
