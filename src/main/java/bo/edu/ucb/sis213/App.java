package bo.edu.ucb.sis213;

import javax.swing.JOptionPane;


public class App {

    //  VARIABLES DE LA APP
    private static int intentos;
    private static int usuarioId;
    private static double saldo;
    private static int pinActual;
    private static String usuarioNombre;
    private static DBFunctions dbFunctions;

    // CONSTRUCTOR
    
    public App() {
        intentos = 3;
        dbFunctions = new DBFunctions();
    }

    // GETTERS Y SETTERS
    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        App.intentos = intentos;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        App.usuarioId = usuarioId;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        App.saldo = saldo;
    }

    public int getPinActual() {
        return pinActual;
    }

    public void setPinActual(int pinActual) {
        App.pinActual = pinActual;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        App.usuarioNombre = usuarioNombre;
    }

    //  FUNCIONES DE LA APP

    public boolean loginAttempt(String username, int pinIngresado){
        //Log-In logic
        if (intentos > 0) {
            if (dbFunctions.validarPIN(username, pinIngresado)) {
                return true;
            } else {
                intentos--;
                if (intentos > 0) {
                    JOptionPane.showMessageDialog(null,"PIN incorrecto. Le quedan " + intentos + " intentos.");
                } else {
                    JOptionPane.showMessageDialog(null,"PIN incorrecto. Ha excedido el número de intentos.");
                    System.exit(0);
                }
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null,"PIN incorrecto. Ha excedido el número de intentos.");
            System.exit(0);
            return false;
        }
    }

    public void setApp(String username){
        //Setear la app con los datos del usuario
        usuarioNombre = dbFunctions.getUsuarioNombre(username);
        usuarioId = dbFunctions.getUsuarioId(username);
        saldo = dbFunctions.getUsuarioSaldo(username);
        pinActual = dbFunctions.getUsuarioPin(username);
    }

    public void showSaldo(){
        //Mostrar el saldo actual en pantalla
        JOptionPane.showMessageDialog(null,"Su saldo actual es: $" + saldo);
    }
    
    public void realizarDeposito() {
        //Depósito por ventana
        double cantidad = Double.parseDouble(JOptionPane.showInputDialog("Ingrese la cantidad a depositar: $"));

        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(null,"Cantidad no válida.");
        } else {
            updateSaldo(cantidad, "DEPOSITO");
            JOptionPane.showMessageDialog(null,"Depósito realizado con éxito. Su nuevo saldo es: $" + saldo);
        }
    }

    public void realizarRetiro() {
        //Retiro por ventana
        double cantidad = Double.parseDouble(JOptionPane.showInputDialog("Ingrese la cantidad a retirar: $"));

        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(null,"Cantidad no válida.");
        } else if (cantidad > saldo) {
            JOptionPane.showMessageDialog(null,"Saldo insuficiente.");
        } else {
            updateSaldo(cantidad, "RETIRO");
            JOptionPane.showMessageDialog(null,"Retiro realizado con éxito. Su nuevo saldo es: $" + saldo);
        }
    }

    private void updateSaldo(double cantidad, String operacion){
        //Actualizar el historial
        dbFunctions.actualizarHistorico(usuarioId, cantidad, operacion);
        //Actualizar el saldo en la BDD
        if (operacion.equals("DEPOSITO")) saldo += cantidad;
        else if(operacion.equals("RETIRO")) saldo -= cantidad;

        dbFunctions.actualizarSaldo(usuarioId, saldo);        
    }

    public void cambiarPIN() {
        //Cambiar PIN por ventana
        int pinIngresado = Integer.parseInt(JOptionPane.showInputDialog("Ingrese su PIN actual: "));

        if (pinIngresado == pinActual) {
            int nuevoPin = Integer.parseInt(JOptionPane.showInputDialog("Ingrese su nuevo PIN: "));
            
            int confirmacionPin = Integer.parseInt(JOptionPane.showInputDialog("Confirme su nuevo PIN: "));

            if (nuevoPin == confirmacionPin) {
                pinActual = nuevoPin;
                dbFunctions.updatePIN(usuarioId, nuevoPin);
                JOptionPane.showMessageDialog(null,"PIN actualizado con éxito.");
            } else {
                JOptionPane.showMessageDialog(null,"Los PINs no coinciden.");
            }
        } else {
            JOptionPane.showMessageDialog(null,"PIN incorrecto.");
        }
    }


    
}
