/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.bancologix;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase principal BancoLogix intenta representar la interfaz gráfica de una aplicación bancaria
 * donde los usuarios pueden registrarse, iniciar sesión, consultar saldo, hacer depósitos,
 * retiros, transferencias y ver el historial de sus transacciones.
 */
public class BancoLogix extends JFrame {

    private JTextField usuarioField;
    private JPasswordField contraseñaField;
    private static Map<String, Usuario> usuarios = new HashMap<>();
    private Usuario usuarioActual;

    /**
     * Constructor que inicializa los componentes gráficos y carga los usuarios desde el archivo.
     */
    public BancoLogix() {
        setTitle("Banco Logix");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        cargarUsuarios();

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(0, 0, 128));
        panelSuperior.setBounds(0, 0, 400, 60);
        panelSuperior.setLayout(null);

        JLabel etiquetaBienvenida = new JLabel("BIENVENIDO");
        etiquetaBienvenida.setForeground(Color.YELLOW);
        etiquetaBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetaBienvenida.setBounds(150, 5, 200, 20);
        panelSuperior.add(etiquetaBienvenida);

        JLabel etiquetaBanco = new JLabel("BANCO LOGIX");
        etiquetaBanco.setForeground(Color.YELLOW);
        etiquetaBanco.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetaBanco.setBounds(142, 30, 200, 20);
        panelSuperior.add(etiquetaBanco);

        add(panelSuperior);

        // Campos de texto y etiquetas
        JLabel usuarioLabel = new JLabel("Usuario:");
        usuarioLabel.setBounds(50, 80, 100, 25);
        add(usuarioLabel);

        usuarioField = new JTextField();
        usuarioField.setBounds(150, 80, 150, 25);
        add(usuarioField);

        JLabel contraseñaLabel = new JLabel("Contraseña:");
        contraseñaLabel.setBounds(50, 120, 100, 25);
        add(contraseñaLabel);

        contraseñaField = new JPasswordField();
        contraseñaField.setBounds(150, 120, 150, 25);
        add(contraseñaField);

        // Botones
        JButton ingresarButton = new JButton("Ingresar");
        ingresarButton.setBounds(50, 170, 100, 25);
        add(ingresarButton);

        JButton registrarButton = new JButton("Registrar");
        registrarButton.setBounds(160, 170, 100, 25);
        add(registrarButton);

        JButton salirButton = new JButton("Salir");
        salirButton.setBounds(270, 170, 100, 25);
        add(salirButton);

        // Funcionalidad de los botones
        ingresarButton.addActionListener(e -> ingresar());

        registrarButton.addActionListener(e -> registrar());

        salirButton.addActionListener(e -> System.exit(0));
    }

     /**
     * Método para cargar los usuarios desde un archivo.
     * Lee el archivo de usuarios registrados y los añade a la lista.
     * @throws IOException si ocurre un error al leer el archivo de usuarios.
     */
    private void cargarUsuarios() {
        try (BufferedReader reader = new BufferedReader(new FileReader("usuarios.txt"))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                String nombreUsuario = datos[0];
                String contraseña = datos[1];
                double saldo = Double.parseDouble(datos[2]);
                String numeroCuenta = datos[3];
                Usuario usuario = new Usuario(nombreUsuario, contraseña, saldo, numeroCuenta);
                usuarios.put(nombreUsuario, usuario);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios");
        }
    }
     /**
     * Método para registrar un nuevo usuario.
     * Crea un nuevo usuario y lo añade a la lista de usuarios registrados.
     * @throws IOException si ocurre un error al guardar el usuario en el archivo.
     */
    private void registrar() {
        String nuevoUsuario = JOptionPane.showInputDialog(this, "Ingrese nuevo nombre de usuario:");
        if (nuevoUsuario == null || nuevoUsuario.isEmpty()) {
            return;
        }
        if (usuarios.containsKey(nuevoUsuario)) {
            JOptionPane.showMessageDialog(this, "El usuario ya existe. Intente con otro nombre.");
            return;
        }
        String nuevaContraseña = JOptionPane.showInputDialog(this, "Ingrese una contraseÃ±a:");
        if (nuevaContraseña == null || nuevaContraseña.isEmpty()) {
            return;
        }

        String numeroCuenta = generarNumeroCuenta();
        double saldoInicial = 0.0;

        Usuario nuevoUsuarioObj = new Usuario(nuevoUsuario, nuevaContraseña, saldoInicial, numeroCuenta);
        usuarios.put(nuevoUsuario, nuevoUsuarioObj);

        guardarUsuarioEnArchivo(nuevoUsuarioObj);
        crearArchivoHistorial(nuevoUsuarioObj);

        JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente. Su número de cuenta es: " + numeroCuenta);
    }

     /**
     * Método para crear un archivo .txt de historial de transacciones del usuario.
     * @param usuario El usuario para el cual se creará el archivo de historial.
     * @throws IOException si ocurre un error al crear el archivo de historial.
     */
    private void crearArchivoHistorial(Usuario usuario) {
        try (FileWriter writer = new FileWriter("historial_" + usuario.getNombreUsuario() + ".txt", true)) {
            writer.write("Historial de transacciones de " + usuario.getNombreUsuario() + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al crear el historial del usuario");
        }
    }

    /**
     * Método para guardar un solo usuario en el archivo de usuarios.
     * @param usuario El usuario a guardar.
     * @throws IOException si ocurre un error al escribir en el archivo.
     */
    private void guardarUsuarioEnArchivo(Usuario usuario) {
        try (FileWriter writer = new FileWriter("usuarios.txt", true)) {
            writer.write(usuario.getNombreUsuario() + "," + usuario.getContraseña() + "," + usuario.getSaldo() + "," + usuario.getNumeroCuenta() + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el usuario");
        }
    }

     /**
     * Método para guardar todos los usuarios en el archivo.
     * Escribe todos los usuarios registrados en el archivo de usuarios. 
     * @throws IOException si ocurre un error al escribir en el archivo.
     */
    private void guardarTodosLosUsuarios() {
        try (FileWriter writer = new FileWriter("usuarios.txt")) {
            for (Usuario usuario : usuarios.values()) {
                writer.write(usuario.getNombreUsuario() + "," + usuario.getContraseña() + "," + usuario.getSaldo() + "," + usuario.getNumeroCuenta() + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar los usuarios");
        }
    }

    /**
     * Método para generar un número de cuenta único para el usuario.
     * @return El número de cuenta generado.
     */
    private String generarNumeroCuenta() {
        return String.valueOf((int) (Math.random() * 900000000) + 100000000);
    }

     /**
    * Método para iniciar sesión en el sistema.
    * Verifica que el nombre de usuario y la contraseña proporcionados sean correctos.
    * @param usuario El nombre de usuario ingresado.
    * @param contrasena La contraseña ingresada.
    * @return El usuario si las credenciales son correctas, null si las credenciales son incorrectas.
    * @throws IOException si ocurre un error al cargar los usuarios desde el archivo.
    */
    private void ingresar() {
        String usuario = usuarioField.getText();
        String contraseña = new String(contraseñaField.getPassword());

        if (usuarios.containsKey(usuario)) {
            Usuario usuarioObj = usuarios.get(usuario);
            if (usuarioObj.getContraseña().equals(contraseña)) {
                usuarioActual = usuarioObj;
                abrirMenuPrincipal();
            } else {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Usuario no encontrado");
        }
    }
    /**
    * Método que abre el menú principal después de iniciar sesión.
    * Permite al usuario realizar varias operaciones como ver el saldo, depositar, retirar, transferir dinero, entre otras. 
    * @param usuario El objeto Usuario que ha iniciado sesión y desea acceder al menú principal.
    * @throws IOException si ocurre un error al cargar o interactuar con los archivos del sistema.
    */
    private void abrirMenuPrincipal() {
        getContentPane().removeAll();
        repaint();
        setTitle("Banco Logix - Menú Principal");
        setSize(400, 420);

        JLabel usuarioInfo = new JLabel("Usuario: " + usuarioActual.getNombreUsuario());
        usuarioInfo.setBounds(50, 0, 150, 25);
        add(usuarioInfo);

        JLabel numeroDeCuenta = new JLabel("N de Cuenta: " + usuarioActual.getNumeroCuenta());
        numeroDeCuenta.setBounds(50, 20, 150, 25);
        add(numeroDeCuenta);

        JButton consultarSaldoButton = new JButton("Consultar saldo");
        consultarSaldoButton.setBounds(50, 80, 150, 30);
        add(consultarSaldoButton);

        JButton depositarButton = new JButton("Depositar dinero");
        depositarButton.setBounds(50, 120, 150, 30);
        add(depositarButton);

        JButton retirarButton = new JButton("Retirar dinero");
        retirarButton.setBounds(50, 160, 150, 30);
        add(retirarButton);


        JButton transferirButton = new JButton("Transferir dinero");
        transferirButton.setBounds(50, 200, 150, 30);
        add(transferirButton);


        JButton verHistorialButton = new JButton("Ver Historial");
        verHistorialButton.setBounds(50, 240, 150, 30);
        add(verHistorialButton);

        JButton salirButton = new JButton("Salir");
        salirButton.setBounds(150, 320, 100, 30);
        add(salirButton);

        salirButton.addActionListener(e -> System.exit(0));

        consultarSaldoButton.addActionListener(e ->abrirVentanaSaldo());

        depositarButton.addActionListener(e -> abrirVentanaDepositar());

        retirarButton.addActionListener(e -> abrirVentanaRetirar());

        transferirButton.addActionListener(e -> abrirVentanaTransferencia());

        verHistorialButton.addActionListener(e -> abrirVentanaHistorial());
    }

     /**
     * Método para abrir la ventana de saldo.
     * Muestra el saldo actual del usuario.
     */
    private void abrirVentanaSaldo() {
        JFrame saldoFrame = new JFrame("Consulta de Saldo");
        saldoFrame.setSize(400, 200);
        saldoFrame.setLocationRelativeTo(this);
        saldoFrame.setLayout(null);

        JLabel saldoLabel = new JLabel("Consulta de Saldo");
        saldoLabel.setForeground(new Color(255, 69, 0)); // Naranja
        saldoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        saldoLabel.setBounds(80, 5, 200, 30);
        
        JLabel saldoMontoLabel = new JLabel("Saldo disponible: " + usuarioActual.getSaldo() + " Bs.");
        saldoMontoLabel.setBounds(110, 60, 200, 25);
        saldoFrame.add(saldoMontoLabel);

        JButton atrasButton = new JButton("AtrÃ¡s");
        atrasButton.setBounds(150, 120, 100, 30);
        saldoFrame.add(atrasButton);

        atrasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saldoFrame.dispose(); // Cierra la ventana de saldo
            }
        });

        saldoFrame.setVisible(true);
    }

    /**
     * Método para abrir la ventana de depositar.
     * Permite al usuario depositar una cantidad de dinero en su cuenta.
     * @throws NumberFormatException si el monto ingresado no es válido.
     */
    private void abrirVentanaDepositar() {
        JFrame ventanaDeposito = new JFrame("Depositar Dinero");
        ventanaDeposito.setSize(400, 200);
        ventanaDeposito.setLocationRelativeTo(this);
        ventanaDeposito.setLayout(null);

        JLabel cantidadLabel = new JLabel("Cantidad a depositar:");
        cantidadLabel.setBounds(50, 30, 150, 25);
        ventanaDeposito.add(cantidadLabel);

        JTextField cantidadField = new JTextField();
        cantidadField.setBounds(140, 60, 150, 25);
        ventanaDeposito.add(cantidadField);

        JButton atrasButton = new JButton("AtrÃ¡s");
        atrasButton.setBounds(240, 100, 100, 25);
        ventanaDeposito.add(atrasButton);

        atrasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventanaDeposito.dispose(); // Cierra la ventana de saldo
            }
        });
        JButton confirmarButton = new JButton("Confirmar");
        confirmarButton.setBounds(80, 100, 100, 25);
        ventanaDeposito.add(confirmarButton);

        confirmarButton.addActionListener(e -> {
            double cantidad = Double.parseDouble(cantidadField.getText());
            usuarioActual.depositar(cantidad);
            guardarTodosLosUsuarios();
            registrarHistorial(usuarioActual, "Depósito", cantidad);
            ventanaDeposito.dispose();
        });

        ventanaDeposito.setVisible(true);
    }

    /**
     * Método para abrir la ventana de retirar.
     * Permite al usuario retirar una cantidad de dinero de su cuenta.
     * @throws NumberFormatException si el monto ingresado no es válido.
     */
    private void abrirVentanaRetirar() {
        JFrame ventanaRetiro = new JFrame("Retirar Dinero");
        ventanaRetiro.setSize(400, 200);
        ventanaRetiro.setLocationRelativeTo(this);
        ventanaRetiro.setLayout(null);

        JLabel cantidadLabel = new JLabel("Cantidad a retirar:");
        cantidadLabel.setBounds(50, 30, 150, 25);
        ventanaRetiro.add(cantidadLabel);

        JTextField cantidadField = new JTextField();
        cantidadField.setBounds(140, 60, 150, 25);
        ventanaRetiro.add(cantidadField);

        JButton atrasButton = new JButton("Atrás");
        atrasButton.setBounds(240, 100, 100, 25);
        ventanaRetiro.add(atrasButton);

        atrasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventanaRetiro.dispose(); // Cierra la ventana de saldo
            }
        });
        

        JButton confirmarButton = new JButton("Confirmar");
        confirmarButton.setBounds(80, 100, 100, 25);
        ventanaRetiro.add(confirmarButton);

        confirmarButton.addActionListener(e -> {
            double cantidad = Double.parseDouble(cantidadField.getText());
            if (cantidad <= usuarioActual.getSaldo()) {
                usuarioActual.retirar(cantidad);
                guardarTodosLosUsuarios();
                registrarHistorial(usuarioActual, "Retiro", cantidad);
                ventanaRetiro.dispose();
            } else {
                JOptionPane.showMessageDialog(ventanaRetiro, "Saldo insuficiente");
            }
        });

        ventanaRetiro.setVisible(true);
    }

    /**
     * Método para abrir la ventana de transferencia.
     * Permite al usuario transferir una cantidad de dinero a otro usuario. 
     * @throws NumberFormatException si el monto ingresado no es válido.
     * @throws NullPointerException si el destinatario no existe.
     */
    private void abrirVentanaTransferencia() {
        JFrame ventanaTransferencia = new JFrame("Transferir Dinero");
        ventanaTransferencia.setSize(400, 300);
        ventanaTransferencia.setLocationRelativeTo(this);
        ventanaTransferencia.setLayout(null);
    
        JLabel cuentaDestinoLabel = new JLabel("Número de cuenta destino:");
        cuentaDestinoLabel.setBounds(50, 30, 180, 25);
        ventanaTransferencia.add(cuentaDestinoLabel);
    
        JTextField cuentaDestinoField = new JTextField();
        cuentaDestinoField.setBounds(140, 60, 150, 25);
        ventanaTransferencia.add(cuentaDestinoField);
    
        JLabel cantidadLabel = new JLabel("Cantidad a transferir:");
        cantidadLabel.setBounds(50, 100, 150, 25);
        ventanaTransferencia.add(cantidadLabel);
    
        JTextField cantidadField = new JTextField();
        cantidadField.setBounds(140, 130, 150, 25);
        ventanaTransferencia.add(cantidadField);
    
        JButton atrasButton = new JButton("AtrÃ¡s");
        atrasButton.setBounds(240, 200, 100, 25);
        ventanaTransferencia.add(atrasButton);
    
        JButton confirmarButton = new JButton("Confirmar");
        confirmarButton.setBounds(80, 200, 100, 25);
        ventanaTransferencia.add(confirmarButton);
    
        atrasButton.addActionListener(e -> ventanaTransferencia.dispose());
    
        confirmarButton.addActionListener(e -> {
            String numeroCuentaDestino = cuentaDestinoField.getText();
    
            // Verificar si el nÃºmero de cuenta destino existe
            Usuario destinatario = buscarUsuarioPorNumeroCuenta(numeroCuentaDestino);
            if (destinatario == null) {
                JOptionPane.showMessageDialog(ventanaTransferencia, "El nÃºmero de cuenta destino no existe.");
                return;
            }
    
            double cantidad;
            try {
                cantidad = Double.parseDouble(cantidadField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(ventanaTransferencia, "Ingrese una cantidad vÃ¡lida.");
                return;
            }
    
            // Verificar si el saldo es suficiente
            if (cantidad <= usuarioActual.getSaldo()) {
                // Realizar la transferencia
                usuarioActual.retirar(cantidad);
                destinatario.depositar(cantidad);
    
                // Guardar los cambios de ambos usuarios
                guardarTodosLosUsuarios();
    
                // Registrar en el historial del usuario actual y del destinatario
                registrarHistorial(usuarioActual, "Transferencia a cuenta " + destinatario.getNumeroCuenta(), cantidad);
                registrarHistorial(destinatario, "Transferencia recibida de cuenta " + usuarioActual.getNumeroCuenta(), cantidad);
    
                JOptionPane.showMessageDialog(ventanaTransferencia, "Transferencia realizada con éxito.");
                ventanaTransferencia.dispose();
            } else {
                JOptionPane.showMessageDialog(ventanaTransferencia, "Saldo insuficiente.");
            }
        });
    
        ventanaTransferencia.setVisible(true);
    }
    
     /**
     * Método para buscar un usuario por su número de cuenta. 
     * @param numeroCuenta El número de cuenta del usuario a buscar.
     * @return El usuario encontrado o null si no existe.
     */
    private Usuario buscarUsuarioPorNumeroCuenta(String numeroCuenta) {
        for (Usuario usuario : usuarios.values()) {
            if (usuario.getNumeroCuenta().equals(numeroCuenta)) {
                return usuario;
            }
        }
        return null; // Si no se encuentra un usuario con ese número de cuenta
    }
    
    /**
     * Método para abrir la ventana de historial.
     * Muestra el historial de transacciones del usuario actual.
     */
    private void abrirVentanaHistorial() {
        JFrame ventanaHistorial = new JFrame("Historial de Transacciones");
        ventanaHistorial.setSize(500, 400);
        ventanaHistorial.setLocationRelativeTo(this);
        ventanaHistorial.setLayout(new BorderLayout());

        JTextArea historialArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(historialArea);
        ventanaHistorial.add(scrollPane, BorderLayout.CENTER);

    // BotÃ³n para cerrar la ventana de historial
    JButton cerrarButton = new JButton("Cerrar");
    cerrarButton.setBounds(200, 320, 100, 30); 
    ventanaHistorial.add(cerrarButton, BorderLayout.SOUTH); 
    cerrarButton.addActionListener(e -> ventanaHistorial.dispose()); 


        try (BufferedReader reader = new BufferedReader(new FileReader("historial_" + usuarioActual.getNombreUsuario() + ".txt"))) {
            String linea;
            StringBuilder historial = new StringBuilder();
            while ((linea = reader.readLine()) != null) {
                historial.append(linea).append("\n");
            }
            historialArea.setText(historial.toString());
        } catch (IOException e) {
            historialArea.setText("Error al leer el historial");
        }

        ventanaHistorial.setVisible(true);
    }

    /**
     * Método para registrar una transacción en el historial del usuario.
     * @param tipoTransaccion La descripción de la transacción.
     * @param cantidad El monto de la transacción.
     * @throws IOException si ocurre un error al registrar la transacción.
     */
    private void registrarHistorial(Usuario usuario, String tipoTransaccion, double cantidad) {
        try (FileWriter writer = new FileWriter("historial_" + usuario.getNombreUsuario() + ".txt", true)) {
            writer.write(tipoTransaccion + ": " + cantidad + " Bs. - Saldo actual: " + usuario.getSaldo() + " Bs.\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar historial");
        }
    }

    /**
    * Clase Usuario que representa a un usuario del sistema bancario.
    * Contiene información del usuario, como el nombre de usuario, contraseña, saldo y transacciones.
    */
    public static class Usuario {
        private String nombreUsuario;
        private String contraseña;
        private double saldo;
        private String numeroCuenta;

        /**
        * Constructor para crear un nuevo usuario. 
        * @param nombreUsuario El nombre de usuario.
        * @param contraseña La contraseña del usuario.
        * @param saldo El saldo inicial del usuario.
        * @param numeroCuenta El número de cuenta único del usuario.
        */
        public Usuario(String nombreUsuario, String contraseña, double saldo, String numeroCuenta) {
            this.nombreUsuario = nombreUsuario;
            this.contraseña = contraseña;
            this.saldo = saldo;
            this.numeroCuenta = numeroCuenta;
        }

        /**
        * Método para obtener el nombre de usuario.
        * @return El nombre de usuario.
        */
        public String getNombreUsuario() {
            return nombreUsuario;
        }

        /**
        * Método para obtener la contraseña del usuario. 
        * @return La contraseña del usuario.
        */
        public String getContraseña() {
            return contraseña;
        }

         /**
        * Método para obtener el saldo del usuario. 
        * @return El saldo del usuario.
        */
        public double getSaldo() {
            return saldo;
        }

        /**
         * Método para obtener el número de cuenta del usuario
         * @return  El número de cuenta del usuario
         */
        public String getNumeroCuenta() {
            return numeroCuenta;
        }

        /**
        * Método para depositar una cantidad de dinero en la cuenta del usuario.
        * @param cantidad La cantidad a depositar.
        */
        public void depositar(double cantidad) {
            saldo += cantidad;
        }

         /**
        * Método para retirar una cantidad de dinero de la cuenta del usuario.
        * @param cantidad La cantidad a retirar.
        */
        public void retirar(double cantidad) {
            saldo -= cantidad;
        }
    }

    public static void main(String[] args) {
        BancoLogix ventana = new BancoLogix();
        ventana.setVisible(true);
    }
}