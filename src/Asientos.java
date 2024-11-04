import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

public class Asientos extends JFrame {
    private JLabel AsientosText;
    public JPanel panelA;
    private JLabel Atext;
    private JLabel Btext;
    private JLabel Ctext;
    private JLabel Dtext;

    private String PrimeraRespuesta;
    private String SegundaRespuesta;
    private String TerceraRespuesta;

    public String PrimeraSolucion;
    public String SegundaSolucion;
    public String TerceraSolucion;
    public String CuartaSolucion;

    public Asientos() {
        PrimeraSolucion = definirPrimeraSolucion();
        SegundaSolucion = definirSegundaSolucion();
        TerceraSolucion = definirTerceraSolucion();
        CuartaSolucion = definirCuartaSolucion();
        crearInterfaz();
    }

    private String definirPrimeraSolucion() {
        return "F F F P P\n" +
                "F F F F R\n" +
                "R R R F F";
    }

    private String definirSegundaSolucion() {
        return "P P P F F\n" +
                "P F F F F\n" +
                "R R F F F";
    }

    private String definirTerceraSolucion() {
        return "P P P P P\n" +
                "R R R F F\n" +
                "F F F F F\n" +
                "R R F F F";
    }

    private String definirCuartaSolucion() {
        return "P P P P F\n" +
                "R R F F F\n" +
                "R R F F F\n" +
                "F F F F F";
    }

    private void crearInterfaz() {
        panelA = new JPanel();
        panelA.setLayout(new GridLayout(3, 4, 5, 5));

        AsientosText = new JLabel("Asientos", SwingConstants.CENTER);
        AsientosText.setFont(new Font("Arial", Font.BOLD, 24));
        panelA.add(AsientosText);
        panelA.add(new JLabel());
        panelA.add(new JLabel());
        panelA.add(new JLabel()); // Espacio vacío para que el título ocupe toda la fila

        panelA.add(createZoneLabel("Zona A"));
        panelA.add(createZoneLabel("Zona B"));
        panelA.add(createZoneLabel("Zona C"));
        panelA.add(createZoneLabel("Zona D"));

        JPanel panelAzone = crearPanelZona(PrimeraSolucion);
        JPanel panelBzone = crearPanelZona(SegundaSolucion);
        JPanel panelCzone = crearPanelZona(TerceraSolucion);
        JPanel panelDzone = crearPanelZona(CuartaSolucion);

        panelA.add(panelAzone);
        panelA.add(panelBzone);
        panelA.add(panelCzone);
        panelA.add(panelDzone);
    }

    private JLabel createZoneLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        return label;
    }

    private JPanel crearPanelZona(String solucion) {
        JPanel panelZona = new JPanel();
        panelZona.setLayout(new GridLayout(0, 5));

        String[] filas = solucion.split("\n");
        for (String fila : filas) {
            String[] asientos = fila.split(" ");
            for (int i = 0; i < asientos.length; i++) {
                String asiento = asientos[i];
                JLabel label = new JLabel(String.valueOf(i + 1));
                label.setOpaque(true);
                label.setPreferredSize(new Dimension(50, 30));
                label.setHorizontalAlignment(SwingConstants.CENTER);

                switch (asiento) {
                    case "F":
                        label.setBackground(Color.GREEN);
                        break;
                    case "P":
                        label.setBackground(Color.RED);
                        break;
                    case "R":
                        label.setBackground(Color.YELLOW);
                        break;
                    default:
                        label.setBackground(Color.WHITE); // Color por defecto
                }
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Borde más grueso para los cuadros
                panelZona.add(label);
            }
        }
        return panelZona;
    }

}