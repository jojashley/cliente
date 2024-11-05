import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Mapa extends JFrame {
    private JComboBox categoria;
    private JButton Buscar;
    private JTextField Asientos;
    private JLabel tectAsientos;
    private JLabel textCategoria;
    private JLabel T;
    private JLabel TextGraderia;
    private JLabel textClub;
    private JLabel textVIP;
    private JLabel cancha;
    private JLabel textBienvenida;
    public JPanel panel;

    public String categoriaSeleccionada;
    public String cantidadSeleccionada;

    public Mapa() {
        Buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                asignarDatos();
                Asientos f = new Asientos(categoriaSeleccionada, cantidadSeleccionada);  // Pasamos ambos valores al constructor
                f.setContentPane(f.panelA);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setVisible(true);
                f.pack();
            }
        });
    }

    public void asignarDatos() {
        // Obtener la categoría seleccionada del JComboBox
        categoriaSeleccionada = (String) categoria.getSelectedItem();

        // Obtener el texto del JTextField
        cantidadSeleccionada = Asientos.getText();

        // Aquí podrías imprimir o utilizar las variables según sea necesario
        System.out.println("Categoría seleccionada: " + categoriaSeleccionada);
        System.out.println("Cantidad seleccionada: " + cantidadSeleccionada);
    }
}
