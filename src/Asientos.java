import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;          // Para usar List
import java.util.Map;           // Para usar Map
import java.util.ArrayList;     // Para usar ArrayList
import java.util.LinkedHashMap; // Para usar LinkedHashMap


public class Asientos extends JFrame {
    private JLabel AsientosText;
    public JPanel panelA;

    private JLabel Atext;
    private JLabel Btext;
    private JLabel Ctext;
    private JLabel Dtext;

    private String Op1;
    private String Op2;
    private String Op3;

    private String Respuesta1;
    private String Respuesta2;
    private String Respuesta3;

    private String Categoria;
    private String Cantidad;

    public String PrimeraSolucion;
    public String SegundaSolucion;
    public String TerceraSolucion;
    public String CuartaSolucion;

    public String opcionSeleccionada;
    public String zonaSeleccionada;

    public Asientos(String categoriaSeleccionada, String cantidadSeleccionada)  {
        this.Categoria = categoriaSeleccionada;
        this.Cantidad = cantidadSeleccionada;
        Op1 = obtenerPrimeraLetra();
        Op2 = obtenerSegundaLetra();
        Op3 = obtenerTerceraLetra();
        Respuesta1 = definirRespuesta1();
        Respuesta2 = definirRespuesta2();
        Respuesta3 = definirRespuesta3();
        PrimeraSolucion = definirPrimeraSolucion();
        SegundaSolucion = definirSegundaSolucion();
        TerceraSolucion = definirTerceraSolucion();
        CuartaSolucion = definirCuartaSolucion();
        crearInterfaz();
    }

    private String definirRespuesta() {
        String request = "display_top_zones " + Categoria;
        String response = conectarServidor(request);
        System.out.println("La respuesta que recibí fue: " + response);
        return response;
    }

    public String[] obtenerLetrasSeparadas() {
        String respuesta = definirRespuesta();
        return respuesta.split("\n");
    }

    public String obtenerPrimeraLetra() {
        String[] letras = obtenerLetrasSeparadas();
        return letras[0];
    }

    public String obtenerSegundaLetra() {
        String[] letras = obtenerLetrasSeparadas();
        return letras[1];
    }

    public String obtenerTerceraLetra() {
        String[] letras = obtenerLetrasSeparadas();
        return letras[2];
    }

    private String definirRespuesta1() {
        String request = Categoria + "\t" +  Op1 + "\t" + Cantidad;
        String response = conectarServidor(request);
        System.out.println("La respuesta que recibí fue: " + response);
        return response;
    }

    private String definirRespuesta2() {
        String request = Categoria + "\t" +  Op2 + "\t" + Cantidad;
        String response = conectarServidor(request);
        System.out.println("La respuesta que recibí fue: " + response);
        return response;
    }

    private String definirRespuesta3() {
        String request = Categoria + "\t" +  Op3 + "\t" + Cantidad;
        String response = conectarServidor(request);
        System.out.println("La respuesta que recibí fue: " + response);
        return response;
    }

    private String definirPrimeraSolucion() {
        String request = "display " + Categoria + " A";
        String response = conectarServidor(request);
        System.out.println("La respuesta que recibí fue: " + response);
        return response;
    }

    private String definirSegundaSolucion() {
        String request = "display " + Categoria + " B";
        String response = conectarServidor(request);
        return response;
    }

    private String definirTerceraSolucion() {
        String request = "display " + Categoria + " C";
        String response = conectarServidor(request);
        return response;
    }

    private String definirCuartaSolucion() {
        String request = "display " + Categoria + " D";
        String response = conectarServidor(request);
        return response;
    }

    private String Reservar(String row, String seat) {
        String request = Categoria + "\t" + zonaSeleccionada + "\t" + row + "\t" + seat;
        String response = conectarServidor(request);
        return response;
    }

    private String conectarServidor(String request) {
        String host = "127.0.0.1";
        int port = 8080;
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
            e.printStackTrace();
        }

        return response;
    }

    private void crearInterfaz() {
        panelA = new JPanel();
        panelA.setLayout(new GridLayout(5, 4, 5, 5)); // Ajustado para 5 filas y 4 columnas

        AsientosText = new JLabel("Asientos", SwingConstants.CENTER);
        AsientosText.setFont(new Font("Arial", Font.BOLD, 24));
        panelA.add(AsientosText);
        panelA.add(new JLabel());
        panelA.add(new JLabel());
        panelA.add(new JLabel());

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

        panelA.add(createOptionPanel("Zona: " + Op1, formatRespuesta(Respuesta1)));
        panelA.add(createOptionPanel("Zona: " + Op2, formatRespuesta(Respuesta2)));
        panelA.add(createOptionPanel("Zona: " + Op3, formatRespuesta(Respuesta3)));

        panelA.add(createComboBoxPanel());

        JButton btnTerminarCompra = new JButton("Reservar y terminar la compra");
        btnTerminarCompra.addActionListener(e -> {
            String[] asientosSeleccionados = opcionSeleccionada.split("\n");

            for (String asientoInfo : asientosSeleccionados) {
                if (asientoInfo.contains("Row:") && asientoInfo.contains("Seat:")) {
                    String[] partes = asientoInfo.split(", ");
                    if (partes.length == 2) {
                        String row = partes[0].split(": ")[1];
                        String seat = partes[1].split(": ")[1];

                        String respuestaReserva = Reservar(row, seat);

                        System.out.println("Reserva realizada para: Row " + row + ", Seat " + seat);
                        System.out.println("Respuesta del servidor: " + respuestaReserva);
                    } else {
                        System.out.println("Formato incorrecto en la línea: " + asientoInfo);
                    }
                } else {
                    System.out.println("Línea inválida: " + asientoInfo);
                }
            }

            Compra f = new Compra(opcionSeleccionada, zonaSeleccionada, Categoria);
            f.setContentPane(f.panelB);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
            f.pack();
        });
        btnTerminarCompra.setFont(new Font("Arial", Font.BOLD, 16));
        panelA.add(new JLabel());
        panelA.add(new JLabel());
        panelA.add(new JLabel());
        panelA.add(btnTerminarCompra);
    }

    private String formatRespuesta(String respuesta) {
        String[] filas = respuesta.split("\n");
        Map<String, List<String>> agrupadas = new LinkedHashMap<>();

        for (String fila : filas) {
            String[] partes = fila.split(", ");

            if (partes.length == 2) {
                String row = partes[0].split(": ")[1];
                String seat = partes[1].split(": ")[1];

                if (!agrupadas.containsKey(row)) {
                    agrupadas.put(row, new ArrayList<>());
                }
                agrupadas.get(row).add(seat);
            } else {
            }
        }

        StringBuilder resultado = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : agrupadas.entrySet()) {
            String row = entry.getKey();
            String asientos = String.join(",", entry.getValue());
            resultado.append("Fila: ").append(row)
                    .append("\nAsientos: ").append(asientos).append("\n");
        }

        return resultado.toString().trim();
    }


    private JPanel createOptionPanel(String op, String respuesta) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        JLabel opcionLabel = new JLabel(op, SwingConstants.CENTER);
        opcionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(opcionLabel);

        JLabel respuestaLabel = new JLabel("<html>" + respuesta.replace("\n", "<br>") + "</html>", SwingConstants.CENTER);
        respuestaLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(respuestaLabel);

        return panel;
    }

    private JPanel createComboBoxPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        JLabel preguntaLabel = new JLabel("Escoge una opción", SwingConstants.CENTER);
        preguntaLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(preguntaLabel);

        String[] opciones = {Op1, Op2, Op3};
        JComboBox<String> comboBox = new JComboBox<>(opciones);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));

        comboBox.addActionListener(e -> {
            String seleccionada = (String) comboBox.getSelectedItem();

            if (seleccionada.equals(Op1)) {
                opcionSeleccionada = Respuesta1;
                zonaSeleccionada = Op1;
            } else if (seleccionada.equals(Op2)) {
                opcionSeleccionada = Respuesta2;
                zonaSeleccionada = Op2;
            } else if (seleccionada.equals(Op3)) {
                opcionSeleccionada = Respuesta3;
                zonaSeleccionada = Op3;
            }
        });

        panel.add(comboBox);

        return panel;
    }

    private JLabel createZoneLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        return label;
    }

    private JPanel crearPanelZona(String solucion) {
        JPanel panelZona = new JPanel();

        String[] filas = solucion.split("\n");

        int maxColumnas = 0;
        for (String fila : filas) {
            String[] asientos = fila.split(" ");
            maxColumnas = Math.max(maxColumnas, asientos.length);
        }

        panelZona.setLayout(new GridLayout(filas.length, maxColumnas));

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
                        label.setBackground(Color.WHITE);
                }
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                panelZona.add(label);
            }

            for (int i = asientos.length; i < maxColumnas; i++) {
                JLabel labelVacio = new JLabel("");
                labelVacio.setOpaque(true);
                labelVacio.setPreferredSize(new Dimension(50, 30));
                labelVacio.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                panelZona.add(labelVacio);
            }
        }
        return panelZona;
    }

}