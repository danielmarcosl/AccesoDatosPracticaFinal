package practicasqlitehospital;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 *
 * @author Daniel Marcos Lorrio
 */
public class Controlador implements ActionListener {

    // Declaramos un objeto para cada clase
    Vista vista = null;
    Modelo modelo = null;

    /**
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

        // Hacemos visible los componentes de la ventana
        vista.setVisible(true);

        Modelo.mostrarHabitaciones("hosp");
        Modelo.mostrarPacientes("hosp");
    }

    /**
     * Metodo que recoge las pulsaciones de los botones y ejecuta una accion
     * dependiendo de cual sea pulsado
     *
     * @param e Boton pulsado
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Vista.bSalir) { // Boton salir
            System.out.println("Se ha cerrado el programa");
            System.exit(0);
        } else if (e.getSource() == Vista.bAlta) { // Boton alta
            Modelo.insertArrayListSQLite("ejemplo", Modelo.crearArrayPaciente(), "paciente(cod_pac, dni, nombre, edad, sexo, alergias, cod_hab)");
        } else if (e.getSource() == Vista.bBaja) { // Boton baja
            String codigo = null;
            Modelo.deleteSQLite("ejemplo", codigo);
        } else if (e.getSource() == Vista.bMod) { // Boton modificar

        } else if (e.getSource() == Vista.bList) { // Boton listado
            Vista.textarea.setText("Menu de listados"
                    + "\nEscribe PACIENTE para listar los pacientes"
                    + "\nEscribe HABITACION para listar las habitaciones");
            if ()
        }
    }
}
