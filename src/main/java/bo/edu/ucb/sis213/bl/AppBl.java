package bo.edu.ucb.sis213.bl;

import bo.edu.ucb.sis213.dao.UsuarioDao;
import bo.edu.ucb.sis213.dao.HistoricoDao;


public class AppBl {

    //  VARIABLES DE LA APP
    private int intentos;
    private int usuarioId;
    private double saldo;
    private int pinActual;
    private String usuarioNombre;
    private String username;
    private UsuarioDao usuarioDao;
    private HistoricoDao historicoDao;

    // CONSTRUCTOR
    
    public AppBl() {
        intentos = 3;
        usuarioDao = new UsuarioDao();
        historicoDao = new HistoricoDao();
    }

    // GETTERS Y SETTERS
    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getPinActual() {
        return pinActual;
    }

    public void setPinActual(int pinActual) {
        this.pinActual = pinActual;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //  FUNCIONES DE LA APP

    public void validarPINbl(String username, int pin) throws ATMException{
        if (usuarioDao.getUsuarioPin(username) == pin) return;
        throw new ATMException("Los PINs no coinciden");
    }

    public void loginAttempt(String username, int pinIngresado) throws ATMException{
        
        if(intentos > 0){
            try{
                validarPINbl(username, pinIngresado);
                return;
            } catch (ATMException ex) {
                intentos--;
                if (intentos > 0) {
                    throw new ATMException("PIN incorrecto. Le quedan " + intentos + " intentos.");
                } else {
                    throw new ATMException("PIN incorrecto. Ha excedido el número de intentos.", 0);
                }
            }
        } else {
            throw new ATMException("PIN incorrecto. Ha excedido el número de intentos.", 0);
        }
    }

    public void setApp(String username){
        //Setear la app con los datos del usuario
        usuarioNombre = usuarioDao.getUsuarioNombre(username);
        usuarioId = usuarioDao.getUsuarioId(username);
        saldo = usuarioDao.getUsuarioSaldo(username);
        pinActual = usuarioDao.getUsuarioPin(username);
        this.username = username;
    }
    
    public void realizarDeposito(double cantidad) throws ATMException {
        //Depósito por ventana
        

        if (cantidad <= 0) {
            throw new ATMException("Cantidad no válida.");
        } else {
            updateSaldo(cantidad, "DEPOSITO");
            
        }
    }

    public void realizarRetiro(double cantidad) throws ATMException {
        //Retiro por ventana
        
        if (cantidad <= 0) {
            throw new ATMException("Cantidad no válida.");
        } else if (cantidad > saldo) {
            throw new ATMException("Saldo insuficiente.");
        } else {
            updateSaldo(cantidad, "RETIRO");
            
        }
    }

    private void updateSaldo(double cantidad, String operacion){
        //Actualizar el historial
        historicoDao.actualizarHistorico(usuarioId, cantidad, operacion);
        //Actualizar el saldo en la BDD
        if (operacion.equals("DEPOSITO")) saldo += cantidad;
        else if(operacion.equals("RETIRO")) saldo -= cantidad;

        usuarioDao.actualizarSaldo(usuarioId, saldo);        
    }

    public void cambiarPIN(int nuevoPin, int confirmacionPin) throws ATMException {
        //Cambiar PIN por ventana
        if (nuevoPin == confirmacionPin) {
            pinActual = nuevoPin;
            usuarioDao.updatePIN(usuarioId, nuevoPin);
                
        } else {
            throw new ATMException("Los PINs no coinciden.");   
        }
    }


    
}
