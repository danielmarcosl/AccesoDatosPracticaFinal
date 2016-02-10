package practicasqlitehospital;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        vista.bSalir.addActionListener(this);

        vista.setVisible(true);
    }

    /**
     * Metodo que recoge las pulsaciones de los botones y ejecuta una accion
     * dependiendo de cual sea pulsado
     *
     * @param e Boton pulsado
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Vista.bSalir) {
            System.out.println("Se ha cerrado el programa");
            System.exit(0);
        } else if (e.getSource() == Vista.bAlta) {
            Modelo.insertArrayListSQLite("ejemplo", Modelo.crearArrayPaciente(), "paciente(cod_pac, dni, nombre, edad, sexo, alergias, cod_hab)");
        } else if (e.getSource() == Vista.bBaja) {
            String codigo = null;
            Modelo.deleteSQLite("ejemplo", codigo);
        } else if (e.getSource() == Vista.bMod) {

        }
    }
}
