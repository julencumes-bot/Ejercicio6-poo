package poo.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import poo.controlador.Accion;
import poo.controlador.ControladorIntegraciones;
import poo.modelo.CargaInicial;
import poo.modelo.MotorCheckout;
import poo.modelo.Resultado;

/**
 * CC2008 - Ejercicio 6, Temario C.
 * Driver y única vista: toda entrada/salida de consola y Swing vive en Principal.
 * El controlador realiza los casos de uso; aquí solo se presentan datos y eventos.
 */
public final class Principal {
    private static final Color AZUL = new Color(20, 42, 66);
    private static final Color VERDE = new Color(0, 116, 120);
    private final ControladorIntegraciones controlador;
    private final Scanner entrada;
    private JTable tabla;
    private JTextField campoId;
    private JTextField campoDato;
    private JComboBox<Accion> selectorAccion;
    private JTextArea salida;
    private JLabel ayudaDato;
    private AbstractTableModel modeloTabla;

    public Principal(ControladorIntegraciones controlador) {
        this.controlador = controlador;
        this.entrada = new Scanner(System.in, java.nio.charset.StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        Principal principal = new Principal(new ControladorIntegraciones(
                CargaInicial.crearCatalogo(), new MotorCheckout()));
        boolean consola = args.length > 0 && "--consola".equals(args[0]);
        if (args.length > 0 && "--ayuda".equals(args[0])) {
            System.out.println("Uso: java -cp build poo.vista.Principal [--consola | --ayuda]");
            System.out.println("Sin argumentos: interfaz gráfica. Requiere JDK 17 o posterior.");
        } else if (consola || GraphicsEnvironment.isHeadless()) {
            if (!consola) {
                System.out.println("No hay pantalla disponible. Se inicia el menú de consola.");
            }
            principal.ejecutarConsola();
        } else {
            SwingUtilities.invokeLater(principal::abrirVentana);
        }
    }

    private void abrirVentana() {
        JFrame ventana = new JFrame("Integraciones | Ejercicio 6 - POO");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setContentPane(crearPanel());
        ventana.setMinimumSize(new Dimension(1000, 720));
        ventana.setSize(1140, 850);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    /** Construye la vista también sin JFrame para verificar eventos en modo headless. */
    public JPanel crearPanel() {
        JPanel raiz = new JPanel(new BorderLayout(0, 14));
        raiz.setBackground(new Color(242, 246, 250));
        raiz.setBorder(BorderFactory.createEmptyBorder(22, 24, 18, 24));

        JPanel cabecera = new JPanel(new GridLayout(3, 1, 0, 5));
        cabecera.setOpaque(false);
        JLabel titulo = new JLabel("Centro de integraciones");
        titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        titulo.setForeground(AZUL);
        cabecera.add(titulo);
        cabecera.add(new JLabel("Pagos, logística y FEL | " + controlador.cantidad() + " proveedores registrados"));
        JLabel modo = new JLabel("DEMOSTRACIÓN LOCAL  ·  Sin cobros reales ni documentos con validez fiscal");
        modo.setForeground(VERDE);
        cabecera.add(modo);
        raiz.add(cabecera, BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(0, 12));
        cuerpo.setOpaque(false);
        JPanel controles = new JPanel(new GridLayout(3, 1, 0, 7));
        controles.setOpaque(false);
        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        busqueda.setOpaque(false);
        campoId = new JTextField(10);
        campoId.setName("id");
        campoId.setToolTipText("Ejemplo: TAR-01. También puede seleccionar una fila.");
        busqueda.add(new JLabel("Identificador"));
        busqueda.add(campoId);
        JButton buscar = boton("Ver configuración", "buscar");
        buscar.addActionListener(e -> mostrar(controlador.buscar(campoId.getText())));
        campoId.addActionListener(e -> mostrar(controlador.buscar(campoId.getText())));
        busqueda.add(buscar);
        JButton listar = boton("Listar todas", "listar");
        listar.addActionListener(e -> {
            refrescarTabla();
            mostrar(Resultado.exito("Se muestran las " + controlador.cantidad() + " integraciones registradas."));
        });
        busqueda.add(listar);
        JButton ordenar = boton("Ordenar por latencia ↑", "ordenar");
        ordenar.addActionListener(e -> {
            Resultado resultado = controlador.ordenar();
            refrescarTabla();
            mostrar(resultado);
        });
        busqueda.add(ordenar);
        controles.add(busqueda);

        JPanel operaciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        operaciones.setOpaque(false);
        selectorAccion = new JComboBox<>(Accion.values());
        selectorAccion.setName("accion");
        campoDato = new JTextField(19);
        campoDato.setName("dato");
        operaciones.add(new JLabel("Operación"));
        operaciones.add(selectorAccion);
        operaciones.add(new JLabel("Dato"));
        operaciones.add(campoDato);
        JButton ejecutar = boton("Ejecutar", "ejecutar");
        ejecutar.setBackground(VERDE);
        ejecutar.setForeground(Color.WHITE);
        ejecutar.addActionListener(e -> ejecutarSeleccion());
        campoDato.addActionListener(e -> ejecutarSeleccion());
        operaciones.add(ejecutar);
        controles.add(operaciones);
        ayudaDato = new JLabel();
        ayudaDato.setForeground(new Color(69, 85, 100));
        ayudaDato.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        controles.add(ayudaDato);
        selectorAccion.addActionListener(e -> actualizarAyuda());
        actualizarAyuda();
        cuerpo.add(controles, BorderLayout.NORTH);

        // Adaptador visual sin colección propia: cada celda consulta al controlador.
        modeloTabla = new AbstractTableModel() {
            private static final long serialVersionUID = 1L;
            @Override
            public int getRowCount() { return controlador.cantidad(); }
            @Override
            public int getColumnCount() { return 5; }
            @Override
            public String getColumnName(int columna) {
                return switch (columna) {
                    case 0 -> "ID";
                    case 1 -> "Proveedor";
                    case 2 -> "Tipo de servicio";
                    case 3 -> "Conexión";
                    default -> "Latencia (ms)";
                };
            }
            @Override
            public Object getValueAt(int fila, int columna) {
                return controlador.celda(fila, columna);
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setName("catalogo");
        tabla.setRowHeight(29);
        tabla.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setSelectionBackground(new Color(204, 233, 235));
        tabla.setSelectionForeground(AZUL);
        tabla.setFillsViewportHeight(true);
        tabla.setShowVerticalLines(false);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(215);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(210);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(155);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                campoId.setText(controlador.celda(tabla.getSelectedRow(), 0));
            }
        });
        JScrollPane scrollTabla = new JScrollPane(tabla);
        // Cabecera explícita: también queda disponible al verificar sin una ventana.
        scrollTabla.setColumnHeaderView(tabla.getTableHeader());
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Catálogo único de integraciones"));
        salida = new JTextArea();
        salida.setName("resultado");
        salida.setEditable(false);
        salida.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        salida.setLineWrap(true);
        salida.setWrapStyleWord(true);
        salida.setMargin(new java.awt.Insets(12, 12, 12, 12));
        JScrollPane scrollSalida = new JScrollPane(salida);
        scrollSalida.setBorder(BorderFactory.createTitledBorder("Resultado / Configuración / XML / Auditoría"));
        JSplitPane dividido = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollTabla, scrollSalida);
        dividido.setResizeWeight(0.62);
        dividido.setDividerLocation(350);
        dividido.setBorder(null);
        cuerpo.add(dividido, BorderLayout.CENTER);
        raiz.add(cuerpo, BorderLayout.CENTER);
        JLabel pie = new JLabel("Seleccione una fila o escriba su ID. Las operaciones no compatibles se ignoran de forma segura.");
        pie.setForeground(new Color(69, 85, 100));
        raiz.add(pie, BorderLayout.SOUTH);
        mostrar(Resultado.exito("Sistema listo.", "Prueba sugerida: TAR-01 → Cobrar → 100.00.\n"
                + "Después: LOG-01 → Reintegrar → 25.00 para observar una operación ignorada.\n"
                + "Para crear XML: GUI-01 o FEL-01 → Generar XML → PED-001."));
        return raiz;
    }

    private JButton boton(String texto, String nombre) {
        JButton boton = new JButton(texto);
        boton.setName(nombre);
        boton.setFocusPainted(false);
        return boton;
    }

    private void actualizarAyuda() {
        Accion accion = (Accion) selectorAccion.getSelectedItem();
        campoDato.setEnabled(accion != Accion.AUDITAR);
        String ayuda = switch (accion) {
            case COBRAR, REINTEGRAR -> "Monto en quetzales, sin separador de miles. Ejemplo: 100.00 o 100,00.";
            case COTIZAR -> "Peso de 0.01 a 1000 kg. Ejemplo: 2.50. La cotización no realiza cobros.";
            case DOCUMENTAR, RASTREAR -> "Referencia de 1 a 80 caracteres. Ejemplo: PED-001.";
            case AUDITAR -> "No requiere dato: muestra el historial de esta integración durante la sesión.";
        };
        ayudaDato.setText(ayuda);
    }

    private void ejecutarSeleccion() {
        mostrar(controlador.ejecutar(campoId.getText(),
                (Accion) selectorAccion.getSelectedItem(), campoDato.getText()));
    }

    private void refrescarTabla() {
        String idSeleccionado = campoId.getText();
        modeloTabla.fireTableDataChanged();
        for (int fila = 0; fila < controlador.cantidad(); fila++) {
            if (controlador.celda(fila, 0).equalsIgnoreCase(idSeleccionado.trim())) {
                tabla.setRowSelectionInterval(fila, fila);
            }
        }
    }

    private void mostrar(Resultado resultado) {
        salida.setText(resultado.toString());
        salida.setCaretPosition(0);
    }

    public void ejecutarConsola() {
        System.out.println("CENTRO DE INTEGRACIONES - DEMOSTRACIÓN LOCAL");
        System.out.println("Sin conexiones externas, cobros reales ni validez fiscal.");
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n1. Listar integraciones      2. Buscar configuración");
            System.out.println("3. Ordenar por latencia      4. Cobrar");
            System.out.println("5. Reintegrar                6. Generar XML");
            System.out.println("7. Rastrear paquete          8. Cotizar envío");
            System.out.println("9. Ver auditoría             0. Salir");
            String opcion = leerTexto("Opción: ");
            if (opcion == null || "0".equals(opcion)) {
                continuar = false;
            } else if ("1".equals(opcion)) {
                listarConsola();
            } else if ("2".equals(opcion)) {
                System.out.println(controlador.buscar(leerTexto("ID (ejemplo TAR-01): ")));
            } else if ("3".equals(opcion)) {
                System.out.println(controlador.ordenar());
                listarConsola();
            } else {
                Accion accion = switch (opcion) {
                    case "4" -> Accion.COBRAR;
                    case "5" -> Accion.REINTEGRAR;
                    case "6" -> Accion.DOCUMENTAR;
                    case "7" -> Accion.RASTREAR;
                    case "8" -> Accion.COTIZAR;
                    case "9" -> Accion.AUDITAR;
                    default -> null;
                };
                if (accion == null) {
                    System.out.println("Opción inválida. Elija un número del menú.");
                } else {
                    String id = leerTexto("ID de la integración: ");
                    String indicacion = switch (accion) {
                        case COBRAR, REINTEGRAR -> "Monto Q (hasta 2 decimales): ";
                        case COTIZAR -> "Peso kg (hasta 2 decimales): ";
                        default -> "Referencia (ejemplo PED-001): ";
                    };
                    String dato = accion == Accion.AUDITAR ? "" : leerTexto(indicacion);
                    System.out.println(controlador.ejecutar(id, accion, dato));
                }
            }
        }
        System.out.println("Sesión finalizada.");
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return entrada.hasNextLine() ? entrada.nextLine().trim() : null;
    }

    private void listarConsola() {
        System.out.printf("%-8s %-23s %-25s %-13s %s%n", "ID", "PROVEEDOR", "TIPO", "CONEXIÓN", "MS");
        for (int fila = 0; fila < controlador.cantidad(); fila++) {
            System.out.printf("%-8s %-23s %-25s %-13s %s%n", controlador.celda(fila, 0),
                    controlador.celda(fila, 1), controlador.celda(fila, 2),
                    controlador.celda(fila, 3), controlador.celda(fila, 4));
        }
    }
}
