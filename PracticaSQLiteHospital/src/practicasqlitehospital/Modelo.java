package practicasqlitehospital;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Daniel Marcos Lorrio
 */
public class Modelo {

    // ############
    // # Conectar #
    // ############
    /**
     * Metodo para conectar a la base de datos
     *
     * @param ruta Ruta de la base de datos
     * @return Conexion a la base de datos
     */
    private static Connection connectSQLite(String ruta) {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:C:\\sqlite3\\" + ruta + ".db");
            System.out.println("DB abierta con exito :D");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al iniciar la DB D:");
        }
        return c;
    }

    // ###########
    // # INSERTS #
    // ###########
    /**
     * Metodo para insertar un nuevo paciente a partir de un ArrayList
     *
     * @param db Nombre de la base de datos
     * @param al ArrayList con los datos del paciente
     */
    public static void insertarPaciente(String db, ArrayList al) {

        String consulta = "Insert into paciente values (?,?,?,?,?,?,?)";

        try {
            PreparedStatement ps = connectSQLite(db).prepareStatement(consulta);

            Iterator it = al.iterator();

            while (it.hasNext()) {
                System.out.println(al);
                ps.setString(1, null); // cod_pac
                ps.setString(2, (String) it.next()); // dni
                ps.setString(3, (String) it.next()); // nombre
                ps.setInt(4, Integer.parseInt((String) it.next())); // edad
                ps.setString(5, (String) it.next()); // sexo
                ps.setString(6, (String) it.next()); // alergias
                ps.setInt(7, Integer.parseInt((String) it.next())); // cod_hab
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // ##########
    // # DELETE #
    // ##########
    /**
     * Metodo para eliminar un paciente por cod_pac
     *
     * @param db Nombre de la base de datos
     * @param cod Codigo de paciente
     */
    public static void borrarPacienteCod_pac(String db, String cod) {
        String consulta = "Delete from paciente where cod_pac = " + cod;

        try {
            PreparedStatement ps = connectSQLite(db).prepareStatement(consulta);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo para eliminar un paciente por dni
     *
     * @param db Nombre de la base de datos
     * @param dni Codigo del dni del paciente
     */
    public static void borrarPacienteDni(String db, String dni) {
        String consulta = "Delete from paciente where dni = '" + dni + "'";

        try {
            PreparedStatement ps = connectSQLite(db).prepareStatement(consulta);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // #############
    // # MODIFICAR #
    // #############
    /**
     * Metodo para modificar los datos de un paciente por su dni
     *
     * @param db Nombre de la base de datos
     */
    public static void modificarPacienteDni(String db) {

        ArrayList<String> al = Controlador.datos;
        String dni = Controlador.paciente.get(1);

        String consulta = "Update paciente set"
                + " dni = '" + al.get(0)
                + "', nombre = '" + al.get(1)
                + "', edad = " + al.get(2)
                + ", sexo = '" + al.get(3)
                + "', alergias = '" + al.get(4)
                + "', cod_hab = " + al.get(5)
                + " where dni = '" + dni + "'";

        try {
            PreparedStatement upd = connectSQLite(db).prepareStatement(consulta);

            upd.executeUpdate();
            upd.close();
            
            // Actualizamos los estados de la cama antigua y la nueva
            actualizarCamaDni(db, "N", dni);
            actualizarCamaDni(db, "S", al.get(0));

            Controlador.vista.textarea.setText("Paciente actualizado");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo para modificar los datos de un paciente por su cod_pac
     *
     * @param db Nombre de la base de datos
     */
    public static void modificarPacienteCod_pac(String db) {

        ArrayList<String> al = Controlador.datos;
        String cod = Controlador.paciente.get(0);

        String consulta = "Update paciente set"
                + " dni = '" + al.get(0)
                + "', nombre = '" + al.get(1)
                + "', edad = " + al.get(2)
                + ", sexo = '" + al.get(3)
                + "', alergias = '" + al.get(4)
                + "', cod_hab = " + al.get(5)
                + " where cod_pac = " + cod;

        try {
            PreparedStatement upd = connectSQLite(db).prepareStatement(consulta);

            upd.executeUpdate();
            upd.close();
            
            // Actualizamos los estados de la cama antigua y la nueva
            actualizarCamaCod(db, "N", cod);
            actualizarCamaDni(db, "S", al.get(0));

            Controlador.vista.textarea.setText("Paciente actualizado");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo para actualizar el estado de la cama por dni
     *
     * @param db Nombre de la base de datos
     * @param e Estado ocupado de la cama (S/N)
     * @param dni Codigo del dni del paciente
     */
    public static void actualizarCamaDni(String db, String e, String dni) {
        String consulta = "Update habitacion"
                + " set ocupado = '" + e
                + "' where cod_hab = ("
                + "select cod_hab"
                + " from paciente"
                + " where dni = '" + dni + "')";

        try {
            PreparedStatement upd = connectSQLite(db).prepareStatement(consulta);

            upd.executeUpdate();
            upd.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo para actualizar el estado de la cama por cod_paciente
     *
     * @param db Nombre de la base de datos
     * @param e Estado ocupado de la cama (S/N)
     * @param cod Codigo del dni del paciente
     */
    public static void actualizarCamaCod(String db, String e, String cod) {
        String consulta = "Update habitacion "
                + "set ocupado = '" + e
                + "' where cod_hab = ("
                + "select cod_hab "
                + "from paciente "
                + "where cod_pac = " + cod + ")";

        try {
            PreparedStatement upd = connectSQLite(db).prepareStatement(consulta);

            upd.executeUpdate();
            upd.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // ###########
    // # SELECTS #
    // ###########
    /**
     * Metodo para hacer select de habitaciones
     *
     * @param ruta Ruta de la base de datos
     */
    public static void mostrarHabitaciones(String ruta) {
        String consulta = "select * from habitacion";

        try {
            PreparedStatement sel = connectSQLite(ruta).prepareStatement(consulta);
            ResultSet result = sel.executeQuery();

            Controlador.vista.textarea.setText("\n___Habitaciones___\n");

            while (result.next()) {
                int cod_hab = result.getInt(1);
                int planta = result.getInt(2);
                int num_cama = result.getInt(3);
                String doctorasignado = result.getString(4);

                Controlador.vista.textarea.setText(Controlador.vista.textarea.getText()
                        + "\nCodigo habitacion: " + cod_hab
                        + "\nPlanta: " + planta
                        + "\nNumero cama: " + num_cama
                        + "\nDoctor asignado: " + doctorasignado + "\n");
            }
            result.close();
            sel.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo para hacer select de habitaciones libres
     *
     * @param ruta Ruta de la base de datos
     */
    public static void mostrarHabitacionesLibres(String ruta) {
        String consulta = "select * from habitacion where ocupado = 'N'";

        try {
            PreparedStatement sel = connectSQLite(ruta).prepareStatement(consulta);
            ResultSet result = sel.executeQuery();

            Controlador.vista.textarea.setText("\n___Habitaciones___\n");

            while (result.next()) {
                int cod_hab = result.getInt(1);
                int planta = result.getInt(2);
                int num_cama = result.getInt(3);
                String doctorasignado = result.getString(4);

                Controlador.vista.textarea.setText(Controlador.vista.textarea.getText()
                        + "\nCodigo habitacion: " + cod_hab
                        + "\nPlanta: " + planta
                        + "\nNumero cama: " + num_cama
                        + "\nDoctor asignado: " + doctorasignado + "\n");
            }
            result.close();
            sel.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo para hacer select de habitaciones ocupadas
     *
     * @param ruta Ruta de la base de datos
     */
    public static void mostrarHabitacionesOcupadas(String ruta) {
        String consulta = "select * from habitacion where ocupado = 'S'";

        try {
            PreparedStatement sel = connectSQLite(ruta).prepareStatement(consulta);
            ResultSet result = sel.executeQuery();

            Controlador.vista.textarea.setText("\n___Habitaciones___\n");

            while (result.next()) {
                int cod_hab = result.getInt(1);
                int planta = result.getInt(2);
                int num_cama = result.getInt(3);
                String doctorasignado = result.getString(4);

                Controlador.vista.textarea.setText(Controlador.vista.textarea.getText()
                        + "\nCodigo habitacion: " + cod_hab
                        + "\nPlanta: " + planta
                        + "\nNumero cama: " + num_cama
                        + "\nDoctor asignado: " + doctorasignado + "\n");
            }
            result.close();
            sel.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo para hacer select de pacientes
     *
     * @param ruta Ruta de la base de datos
     */
    public static void mostrarPacientes(String ruta) {
        String consulta = "select * from paciente";

        try {
            PreparedStatement sel = connectSQLite(ruta).prepareStatement(consulta);
            ResultSet result = sel.executeQuery();

            Controlador.vista.textarea.setText("\n___Pacientes___\n");

            while (result.next()) {
                int cod_pac = result.getInt(1);
                String dni = result.getString(2);
                String nombre = result.getString(3);
                int edad = result.getInt(4);
                String sexo = result.getString(5);
                String alergias = result.getString(6);
                int cod_hab = result.getInt(7);

                Controlador.vista.textarea.setText(Controlador.vista.textarea.getText()
                        + "\nCodigo paciente: " + cod_hab
                        + "\nDNI: " + dni
                        + "\nNombre: " + nombre
                        + "\nEdad: " + edad
                        + "\nSexo: " + sexo
                        + "\nAlergias: " + alergias
                        + "\nCodigo Habitacion: " + cod_hab + "\n");
            }
            result.close();
            sel.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo para almacenar el paciente seleccionado en un ArrayList sabiendo
     * su dni
     *
     * @param ruta Ruta de la base de datos
     * @param dni Codigo del dni del paciente
     */
    public static void almacenarPacienteDni(String ruta, String dni) {
        String consulta = "select * from paciente where dni = '" + dni + "'";

        try {
            PreparedStatement sel = connectSQLite(ruta).prepareStatement(consulta);
            ResultSet result = sel.executeQuery();

            Controlador.paciente.add(Integer.toString(result.getInt(1))); // cod_pac
            Controlador.paciente.add(result.getString(2)); // dni
            Controlador.paciente.add(result.getString(3)); // nombre
            Controlador.paciente.add(Integer.toString(result.getInt(4))); // edad
            Controlador.paciente.add(result.getString(5)); // sexo
            Controlador.paciente.add(result.getString(6)); // alergias
            Controlador.paciente.add(Integer.toString(result.getInt(7))); // cod_hab

            System.out.println(Controlador.paciente);
            result.close();
            sel.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo para almacenar el paciente seleccionado en un ArrayList sabiendo
     * su cod_pac
     *
     * @param ruta Ruta de la base de datos
     * @param cod Codigo del paciente
     */
    public static void almacenarPacienteCod_pac(String ruta, String cod) {
        String consulta = "select * from paciente where cod_pac = " + cod;

        try {
            PreparedStatement sel = connectSQLite(ruta).prepareStatement(consulta);
            ResultSet result = sel.executeQuery();

            Controlador.paciente.add(Integer.toString(result.getInt(1))); // cod_pac
            Controlador.paciente.add(result.getString(2)); // dni
            Controlador.paciente.add(result.getString(3)); // nombre
            Controlador.paciente.add(Integer.toString(result.getInt(4))); // edad
            Controlador.paciente.add(result.getString(5)); // sexo
            Controlador.paciente.add(result.getString(6)); // alergias
            Controlador.paciente.add(Integer.toString(result.getInt(7))); // cod_hab

            result.close();
            sel.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // ###################
    // # MENU Y OPCIONES #
    // ###################
    /**
     * Metodo que ejecuta una accion u otra dependiendo del estado en el que se
     * encuentre el programa
     *
     * @param e Estado del programa
     * @param s String contenido en el JTextField campoTexto
     */
    public static void opcion(String s) {
        switch (Controlador.estado) {
            case "alta":
                altaNueva(s);

                // Si el ArrayList contiene todos los datos se procede a insertarlo
                if (Controlador.datos.size() == 6) {
                    // Insertamos el paciente
                    insertarPaciente(Controlador.db, Controlador.datos);
                    // Modificamos el estado de la cama a ocupado
                    actualizarCamaDni(Controlador.db, "S", Controlador.datos.get(1));
                    // Reiniciamos el estado
                    Controlador.estado = "";
                    // Vaciamos el ArrayList
                    vaciarArray();
                }
                break;
            case "baja":
                bajaPaciente(s);
                break;
            case "modificar":
                modificar(s);
                break;
            case "modificar_dni":
                altaNueva(s); // Reutilizamos el metodo de altaNueva para rellenar el ArrayList
                System.out.println(Controlador.datos);

                // Si el ArrayList contiene todos los datos se procede a modificarlos
                if (Controlador.datos.size() == 6) {
                    // Modificamos el paciente
                    modificarPacienteDni(Controlador.db);
                    // Reiniciamos el estado
                    Controlador.estado = "";
                    // Vaciamos el ArrayList
                    vaciarArray();
                }
                break;
            case "modificar_cod":
                altaNueva(s); // Reutilizamos el metodo de altaNueva para rellenar el ArrayList
                System.out.println(Controlador.datos);

                // Si el ArrayList contiene todos los datos se procede a modificarlos
                if (Controlador.datos.size() == 6) {
                    // Modificamos el paciente
                    modificarPacienteCod_pac(Controlador.db);
                    // Reiniciamos el estado
                    Controlador.estado = "";
                    // Vaciamos el ArrayList
                    vaciarArray();
                }
                break;
            case "listado":
                listado(s);
                break;
            default:
                System.out.println("Ninguna opcion marcada");
                break;
        } // end switch
    } // end opciones

    /**
     * Metodo para dar de alta un paciente
     *
     * @param s String contenido en el JtextField campoTexto
     */
    public static void altaNueva(String s) {
        boolean isNumber;

        // Se recorrera el switch y en cada caso se anadira al array el valor correspondiente
        switch (Controlador.datos.size()) {
            case 0: // dni
                // Comprobamos que el string no exceda el tamano maximo de la columna
                // Si lo excede, se borrara el jTextField y se podra volver a intentar
                if (s.length() <= 10) {
                    // Comprobamos si existe un paciente con el mismo dni
                    if (comprobarDNI(Controlador.db, s)) {
                        Controlador.datos.add(s);
                        Controlador.vista.textarea.setText(Controlador.vista.textarea.getText() + s
                                + "\nNombre: ");
                    } else {
                        System.out.println("El dni introducido ya existe, introduce otro");
                    }
                } else {
                    System.out.println("Se ha excedido el tamano maximo de la columna.\n"
                            + "Tamano maximo: 10, Tamano introducido: " + s.length());
                }
                break;
            case 1: // nombre
                // Comprobamos que el string no exceda el tamano maximo de la columna
                // Si lo excede, se borrara el jTextField y se podra volver a intentar
                if (s.length() <= 20) {
                    Controlador.datos.add(s);
                    Controlador.vista.textarea.setText(Controlador.vista.textarea.getText() + s
                            + "\nEdad: ");
                } else {
                    System.out.println("Se ha excedido el tamano maximo de la columna.\n"
                            + "Tamano maximo: 20, Tamano introducido: " + s.length());
                }
                break;
            case 2: // edad
                // Comprobamos que lo que se ha introducido es un numero
                // Si no es un numero, se borrara el jTextField y se podra volver a intentar
                try {
                    Integer.parseInt(s);
                    isNumber = true;
                } catch (NumberFormatException nfe) {
                    isNumber = false;
                    System.out.println("Error al introducir edad, valor no numerico");
                }

                if (isNumber) {
                    Controlador.datos.add(s);
                    Controlador.vista.textarea.setText(Controlador.vista.textarea.getText() + s
                            + "\nSexo(H/M): ");
                }
                break;
            case 3: // sexo
                // Comprobamos que el string coincide con los parametros de la columna
                if (s.toUpperCase().equals("M") || s.toUpperCase().equals("H")) {
                    Controlador.datos.add(s);
                    Controlador.vista.textarea.setText(Controlador.vista.textarea.getText() + s
                            + "\nAlergias: ");
                } else {
                    System.out.println("Se ha introducido un valor no valido, introduzca M o H");
                }
                break;
            case 4: // alergias
                // Comprobamos que el string no supera el tamano de la columna
                // Si lo supera se borra el contenido del jTextField y se podra volver a intentar
                if (s.length() <= 20) {
                    Controlador.datos.add(s);
                    Controlador.vista.textarea.setText(Controlador.vista.textarea.getText() + s
                            + "\nCodigo Habitacion: ");
                } else {
                    System.out.println("Se ha excedido el tamano maximo de la columna.\n"
                            + "Tamano maximo: 20, Tamano introducido: " + s.length());
                }
                break;
            case 5: // cod_hab
                // Comprobamos que lo que se ha introducido es un numero
                // Si no es un numero, se borrara el jTextField y se podra volver a intentar
                try {
                    Integer.parseInt(s);
                    isNumber = true;
                } catch (NumberFormatException nfe) {
                    isNumber = false;
                    System.out.println("Error al introducir edad, valor no numerico");
                }

                if (isNumber) {
                    // Comprobamos si el numero de habitacion existe y si esta libre
                    if (comprobarHabitacion(Controlador.db, s)) {
                        Controlador.datos.add(s);
                        Controlador.vista.textarea.setText(Controlador.vista.textarea.getText() + s
                                + "\n\nPaciente anadido con exito.");
                    } else {
                        System.out.println("La cama introducida esta ocupada, introduce otra");
                    }
                }
                break;
        } // end switch alta
    } // end altaNueva

    /**
     * Metodo para dar de baja a un paciente
     *
     * @param s Contenido del JTextField campoTexto
     */
    public static void bajaPaciente(String s) {
        boolean isNumber;
        // Comprobamos si lo introducido es un int (cod_pac) o String (dni)
        try {
            Integer.parseInt(s);
            isNumber = true;
        } catch (NumberFormatException nfe) {
            isNumber = false;
        }

        if (isNumber) { // Si es cod_pac
            // Comprobamos si existe
            if (comprobarCod_pac(Controlador.db, s)) {
                // Borramos el paciente
                borrarPacienteCod_pac(Controlador.db, s);
                // Cambiamos el estado de la cama a no ocupado
                actualizarCamaCod(Controlador.db, "N", s);
                Controlador.vista.textarea.setText("Paciente dado de baja con exito");
            } else {
                System.out.println("El cod_pac introducido no existe, vuelve a intentarlo");
            }
        } else// Si es dni
        // Comprobamos si existe
        {
            if (!comprobarDNI(Controlador.db, s)) {
                // Borramos el paciente
                borrarPacienteDni(Controlador.db, s);
                // Cambiamos el estado de la cama a no ocupado
                actualizarCamaDni(Controlador.db, "N", s);
                Controlador.vista.textarea.setText("Paciente dado de baja con exito");
            } else {
                System.out.println("El dni introducido no existe, vuelve a intentarlo");
            }
        }
    } // end bajaPaciente

    /**
     * Metodo para modificar los datos de un paciente
     *
     * @param s Contenido del JTextField campoTexto
     */
    public static void modificar(String s) {
        boolean isNumber;
        // Comprobamos si lo introducido es un int (cod_pac) o String (dni)
        try {
            Integer.parseInt(s);
            isNumber = true;
        } catch (NumberFormatException nfe) {
            isNumber = false;
        }

        if (isNumber) { // Si es cod_pac
            // Comprobamos si existe
            if (comprobarCod_pac(Controlador.db, s)) {
                // Cambiamos el estado
                Controlador.estado = "modificar_cod";
                // Anadimos los datos actuales del paciente a su correspondiente ArrayList
                almacenarPacienteCod_pac(Controlador.db, s);

                Controlador.vista.textarea.setText("DNI: ");
            } else {
                System.out.println("El cod_pac introducido no existe, vuelve a intentarlo");
            }
        } else// Si es dni
        // Comprobamos si existe
        {
            if (!comprobarDNI(Controlador.db, s)) {
                // Cambiamos el estado
                Controlador.estado = "modificar_dni";
                // Anadimos los datos actuales del paciente a su correspondiente ArrayList
                almacenarPacienteDni(Controlador.db, s);

                Controlador.vista.textarea.setText("DNI: ");
            } else {
                System.out.println("El dni introducido no existe, vuelve a intentarlo");
            }
        }
    } // end modificar

    /**
     * Metodo para listar el contenido de las tablas
     *
     * @param db Nombre de la base de datos
     * @param s Contenido del JTextField campoTexto
     */
    public static void listado(String s) {
        if (s.toUpperCase().equals("PACIENTES")) {
            mostrarPacientes(Controlador.db);
        } else if (s.toUpperCase().equals("HABITACIONES")) {
            mostrarHabitaciones(Controlador.db);
        } else if (s.toUpperCase().equals("HABITACIONESLIBRES")) {
            mostrarHabitacionesLibres(Controlador.db);
        } else if (s.toUpperCase().equals("HABITACIONESOCUPADAS")) {
            mostrarHabitacionesOcupadas(Controlador.db);
        } else {
            Controlador.vista.textarea.setText("Opcion no valida, volviendo al menu principal.\nPor favor seleccione una opcion del menu superior.");
        }
        // Reiniciamos el estado
        Controlador.estado = "";
    } // end listado

    // ##################
    // # Comprobaciones #
    // ##################
    /**
     * Metodo para comprobar si existe un paciente con el mismo dni
     *
     * @param db Nombre de la base de datos
     * @param dni Codigo del dni
     * @return True si existe, False si no existe
     */
    public static boolean comprobarDNI(String db, String dni) {
        boolean libre = false;

        String consulta = "select * from paciente where dni = '" + dni + "'";

        try {
            PreparedStatement com = connectSQLite(db).prepareStatement(consulta);
            ResultSet result = com.executeQuery();

            String n = result.getString(2);

            if (n != null) {
                libre = false;
            }

            result.close();
            com.close();

        } catch (SQLException ex) {
            libre = true;
        }

        return libre;
    } // end comprobarDNI

    /**
     * Metodo para comprobar si existe el cod_pac de un paciente
     *
     * @param db Nombre de la base de datos
     * @param dni Codigo del dni
     * @return True si existe, False si no existe
     */
    public static boolean comprobarCod_pac(String db, String cod) {
        boolean existe = false;

        String consulta = "select * from paciente where cod_pac = '" + cod + "'";

        try {
            PreparedStatement com = connectSQLite(db).prepareStatement(consulta);
            ResultSet result = com.executeQuery();

            String n = result.getString(2);

            if (n != null) {
                existe = true;
            }

            result.close();
            com.close();

        } catch (SQLException ex) {
            existe = false;
        }

        return existe;
    } // end comprobarCod_pac

    /**
     * Metodo para comprobar si existe una habitacion y si esta ocupada
     *
     * @param db Nombre de la base de datos
     * @param cod Codigo de la habitacion
     * @return True si existe, False si no existe
     */
    public static boolean comprobarHabitacion(String db, String cod) {
        boolean existe = false;

        String consulta = "select * from habitacion where cod_hab = " + cod;

        try {
            PreparedStatement com = connectSQLite(db).prepareStatement(consulta);
            ResultSet result = com.executeQuery();

            String cama = result.getString(5);

            if (cama.toUpperCase().equals("N")) {
                existe = true;
            } else {
                existe = false;
            }

            result.close();
            com.close();

        } catch (SQLException ex) {
            existe = false;
        }

        return existe;
    } // end comprobarHabitacion

    /**
     * Metodo para vaciar el ArrayList
     */
    public static void vaciarArray() {
        Controlador.datos.clear();
    } // end vaciarArray
} // end class
