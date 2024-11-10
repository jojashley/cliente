import Plugins.PluginPago;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Compra extends JFrame {
    private final String opcionSeleccionada;
    private final String zonaSeleccionada;
    private final String categoriaSeleccionada;
    public JPanel panelB;
    private PluginPago pluginPago;
    Map<String, Class> classList = new HashMap<>();

    static final String PLUGIN_FOLDER = "src/Plugins/impl";

    public Compra(String opcionSeleccionada, String zonaSeleccionada, String categoriaSeleccionada) {
        this.opcionSeleccionada = opcionSeleccionada;
        this.zonaSeleccionada = zonaSeleccionada;
        this.categoriaSeleccionada = categoriaSeleccionada;
        inicializarInterfaz();
    }

    // Load the payment plugin dynamically
    public void cargarPluginPago(String pluginClassName) {
        try {

            pluginPago = (PluginPago) classList.get(pluginClassName).getDeclaredConstructor().newInstance();
            realizarCompra();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar el plugin de pago.");
        }
    }

    // Finalize the purchase
    public void realizarCompra() {
        if (pluginPago == null) {
            JOptionPane.showMessageDialog(this, "Por favor, cargue un plugin de pago primero.");
            return;
        }
        boolean exito = pluginPago.procesarPago(opcionSeleccionada, zonaSeleccionada, categoriaSeleccionada);
        if (exito) {
            JOptionPane.showMessageDialog(this, "Compra realizada con éxito.");
        } else {
            JOptionPane.showMessageDialog(this, "Error al procesar el pago.");
        }
    }

    private void inicializarInterfaz() {
        panelB = new JPanel();
        panelB.setLayout(new BorderLayout());

        JLabel labelTitulo = new JLabel("Detalles de la Compra", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        panelB.add(labelTitulo, BorderLayout.NORTH);

        JPanel panelDetalles = new JPanel();
        panelDetalles.setLayout(new GridLayout(4, 1));
        panelDetalles.add(new JLabel("Opción: " + opcionSeleccionada, SwingConstants.CENTER));
        panelDetalles.add(new JLabel("Zona: " + zonaSeleccionada, SwingConstants.CENTER));
        panelDetalles.add(new JLabel("Categoría: " + categoriaSeleccionada, SwingConstants.CENTER));
        panelB.add(panelDetalles, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnPagar = new JButton("Pagar");
        btnPagar.addActionListener(e -> cargarPlugins());
        panelBotones.add(btnPagar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> cancelarCompra());
        panelBotones.add(btnCancelar);

        panelB.add(panelBotones, BorderLayout.SOUTH);
        setContentPane(panelB);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }

    public void cancelarCompra() {
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea cancelar la compra?", "Cancelar compra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) {
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
        cargarComboBoxPlugis();
    }

    public void cargarComboBoxPlugis() {
        Set<String> keys = classList.keySet();
        String [] plugins = keys.toArray(new String[0]);
        JComboBox<String> comboBox = new JComboBox<>(plugins);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));

    }

}
