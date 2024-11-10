import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Main {

    static final String PLUGIN_FOLDER = "src/Plugins";

    public static void main(String[] args) {
        // Initialize and display the Mapa window
        Mapa f = new Mapa();
        f.setContentPane(new Mapa().panel);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        f.pack();

        // Load plugins
//        Map<String, Class> plugins = cargarPlugins();

//        // Example usage: pass a plugin name to Compra
//        String pluginName = "Plugins.PluginPagoImpl";  // Replace with your plugin's full class name
//        Compra compra = new Compra("Entrada General", "Zona A", "VIP");
//        compra.cargarPluginPago(pluginName);  // Load the plugin
//        compra.setVisible(true);
    }


}
