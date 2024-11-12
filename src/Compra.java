import Plugins.PluginPago;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Compra extends JFrame {
    private final String opcionSeleccionada;
    private final String zonaSeleccionada;
    private final String categoriaSeleccionada;
    public JPanel panelB;
    private PluginPago pluginPago;
    private String  pluginPagoSeleccionado;
    Map<String, Class> classList = new HashMap<>();

    static final String PLUGIN_FOLDER = "src/Plugins/impl";

    public Compra(String opcionSeleccionada, String zonaSeleccionada, String categoriaSeleccionada) {
        this.opcionSeleccionada = opcionSeleccionada;
        this.zonaSeleccionada = zonaSeleccionada;
        this.categoriaSeleccionada = categoriaSeleccionada;
        System.out.println(categoriaSeleccionada);
        inicializarInterfaz();
    }

    public void cargarPluginPago() {
        try {

            pluginPago = (PluginPago) classList.get(pluginPagoSeleccionado).getDeclaredConstructor().newInstance();
            realizarCompra();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Debe seleccionar un plugin de pago.");
        }
    }

    public void realizarCompra() {
        if (pluginPago == null) {
            JOptionPane.showMessageDialog(this, "Por favor, cargue un plugin de pago primero.");
            return;
        }
        boolean exito = pluginPago.procesarPago(opcionSeleccionada, zonaSeleccionada, categoriaSeleccionada);
        //pagarCompra();
        if (exito) {
            //JOptionPane.showMessageDialog(this, "Compra realizada con éxito.");
            pagarCompra();
        } else {
            JOptionPane.showMessageDialog(this, "Error al procesar el pago.");
        }
    }


    private void inicializarInterfaz() {
        cargarPlugins();
        panelB = new JPanel();
        panelB.setLayout(new BorderLayout());

        // Título
        JLabel labelTitulo = new JLabel("Detalles de la Compra", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        panelB.add(labelTitulo, BorderLayout.NORTH);

        // Detalles de la compra
        JPanel panelDetalles = new JPanel();
        panelDetalles.setLayout(new GridLayout(4, 1));
        panelDetalles.add(new JLabel("Opción: " + opcionSeleccionada, SwingConstants.CENTER));
        panelDetalles.add(new JLabel("Zona: " + zonaSeleccionada, SwingConstants.CENTER));
        panelDetalles.add(new JLabel("Categoría: " + categoriaSeleccionada, SwingConstants.CENTER));
        panelB.add(panelDetalles, BorderLayout.CENTER);

        // Panel de plugins
        if (!classList.isEmpty()) {
            Set<String> keys = classList.keySet();

            String[] plugins = new String[classList.size()+1];
            plugins[0] ="Seleccione un plugin de pago";

            System.arraycopy(keys.toArray(new String[0]),0,plugins,1,keys.toArray(new String[0]).length);

            JPanel panelPlugins = new JPanel();
            panelPlugins.setLayout(new GridLayout(2, 1));

            JLabel preguntaLabel = new JLabel("Escoge un plugin de pago", SwingConstants.CENTER);
            preguntaLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            panelPlugins.add(preguntaLabel);

            JComboBox<String> comboBox = new JComboBox<>(plugins);
            comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
            comboBox.addActionListener(e -> {
                pluginPagoSeleccionado = (String) comboBox.getSelectedItem();
            });

            panelPlugins.add(comboBox);
            panelB.add(panelPlugins, BorderLayout.BEFORE_LINE_BEGINS); // Asegúrate de colocar el panel en el lugar correcto
        }

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnPagar = new JButton("Pagar");
        btnPagar.addActionListener(e -> cargarPluginPago());
        panelBotones.add(btnPagar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> cancelarCompra());
        panelBotones.add(btnCancelar);

        panelB.add(panelBotones, BorderLayout.SOUTH);

        // Finalización
        setContentPane(panelB);
        revalidate();
        repaint();
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private String comprar(String row, String seat) {
        String request = "purchase" + "\t" + categoriaSeleccionada + "\t" + zonaSeleccionada + "\t" + row + "\t" + seat;
        System.out.println(request);
        String response = conectarServidor(request);
        return response;
    }

    private String cancelar(String row, String seat) {
        String request = "cancel" + "\t" + categoriaSeleccionada + "\t" + zonaSeleccionada + "\t" + row + "\t" + seat;
        System.out.println(request);
        String response = conectarServidor(request);
        return response;
    }

    private String conectarServidor(String request) {
        String host = "127.0.0.1";
        int port = 7878;
        String response = "";

        try (Socket socket = new Socket(host, port);
             OutputStream outputStream = socket.getOutputStream();
             BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            outputStream.write(request.getBytes("UTF-8"));
            outputStream.flush();

            char[] buffer = new char[4096];
            int bytesRead = inputReader.read(buffer);
            response = new String(buffer, 0, bytesRead);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error connecting to the server. Please try again.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return response;
    }

    public void cancelarCompra() {
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea cancelar la compra?", "Cancelar compra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) {
            String[] asientos = opcionSeleccionada.split("\n");

            for (String asiento : asientos) {
                asiento = asiento.trim();

                if (asiento.isEmpty()) {
                    continue;
                }

                String[] partes = asiento.split(", ");

                if (partes.length != 2) {
                    System.err.println("Formato incorrecto en la opción seleccionada: " + asiento);
                    continue;
                }

                try {
                    String row = partes[0].split(": ")[1];
                    String seat = partes[1].split(": ")[1];

                    String response = cancelar(row, seat);
                    System.out.println("Respuesta del servidor para Row: " + row + ", Seat: " + seat + " - " + response);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Error al procesar asiento: " + asiento + " - Formato inválido.");
                }
            }

            dispose();
        }


    }

    private void cargarPlugins() {
        File pluginFolder = new File(PLUGIN_FOLDER);

        if (!pluginFolder.exists()) {
            if (pluginFolder.mkdirs()) {
                System.out.println("Created plugin folder");
            }
        }

        File[] files = pluginFolder.listFiles((dir, name) -> name.endsWith(".java"));
        if (files != null) {
            for (File file : files) {
                try {
                    String className = file.getName().replace(".java", "");
                    String fullClassName = "Plugins.impl." + className;
                    Class<?> pluginClass = Class.forName(fullClassName);
                    classList.put(className, pluginClass);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void pagarCompra(){
        int respuesta = JOptionPane.showConfirmDialog(this, "Desea confirmar la compra?", " ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Compra realizada con éxito.");
            String[] asientos = opcionSeleccionada.split("\n");

            for (String asiento : asientos) {
                asiento = asiento.trim();

                if (asiento.isEmpty()) {
                    continue;
                }

                String[] partes = asiento.split(", ");

                if (partes.length != 2) {
                    System.err.println("Formato incorrecto en la opción seleccionada: " + asiento);
                    continue;
                }

                try {
                    String row = partes[0].split(": ")[1];
                    String seat = partes[1].split(": ")[1];

                    String response = comprar(row, seat);
                    System.out.println("Respuesta del servidor para Row: " + row + ", Seat: " + seat + " - " + response);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Error al procesar asiento: " + asiento + " - Formato inválido.");
                }
            }

            dispose();
        }
        else if(respuesta == JOptionPane.NO_OPTION ){
            JOptionPane.showMessageDialog(this, "Compra cancelada.");
            confirmarNo();
        }

    }

    public void confirmarNo(){
        String[] asientos = opcionSeleccionada.split("\n");

        for (String asiento : asientos) {
            asiento = asiento.trim();

            if (asiento.isEmpty()) {
                continue;
            }

            String[] partes = asiento.split(", ");

            if (partes.length != 2) {
                System.err.println("Formato incorrecto en la opción seleccionada: " + asiento);
                continue;
            }

            try {
                String row = partes[0].split(": ")[1];
                String seat = partes[1].split(": ")[1];

                String response = cancelar(row, seat);
                System.out.println("Respuesta del servidor para Row: " + row + ", Seat: " + seat + " - " + response);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Error al procesar asiento: " + asiento + " - Formato inválido.");
            }
        }

        dispose();
    }

}
