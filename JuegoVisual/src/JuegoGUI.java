import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * ============================================================
 *  HÉROES vs DEMONIO DE FUEGO — Versión Visual (Swing)
 * ============================================================
 *  Reimplementación completa del juego por turnos original
 *  con interfaz gráfica, barras de vida, animaciones y log
 *  de combate en tiempo real.
 *
 *  Autores originales: G. Ruiz, I. Salazar, U. Iguarán
 *  Interfaz visual generada por GitHub Copilot
 * ============================================================
 */
public class JuegoGUI extends JFrame {

    // ─── Colores del tema ───────────────────────────────────
    static final Color BG_DARK      = new Color(24, 24, 36);
    static final Color BG_PANEL     = new Color(35, 35, 55);
    static final Color BG_CARD      = new Color(45, 45, 70);
    static final Color GOLD         = new Color(255, 215, 0);
    static final Color RED_HP       = new Color(220, 50, 50);
    static final Color GREEN_HP     = new Color(50, 205, 50);
    static final Color BLUE_MP      = new Color(70, 130, 220);
    static final Color ORANGE_FIRE  = new Color(255, 100, 20);
    static final Color PURPLE       = new Color(160, 80, 220);
    static final Color TEXT_WHITE   = new Color(230, 230, 240);
    static final Color TEXT_DIM     = new Color(150, 150, 170);
    static final Color CURANDERA_PK = new Color(220, 100, 180);
    static final Color MAGO_CYAN    = new Color(80, 200, 255);
    static final Color ESPA_YELLOW  = new Color(255, 200, 60);
    static final Color DEMONIO_RED  = new Color(255, 60, 30);

    // ─── Personajes (datos del juego) ───────────────────────
    int esVida=500,esAtk=35,esDef=10,esEstado=0,esContador=0;
    int mgVida=500,mgAtk=30,mgDef=5,mgEstado=0,mgContador=0;
    int cuVida=500,cuAtk=25,cuDef=15,cuEstado=0,cuContador=0;
    int dfVida=1500,dfAtk=45,dfDef=15,dfEstado=0;
    int dfContHab1=0,dfContHab2=0;

    boolean esHabEspecial=false, mgHabEspecial=false;
    boolean esCubierto=false, mgCubierto=false, cuCubierto=false;

    // Stats base para restaurar
    final int ES_ATK_BASE=35,ES_DEF_BASE=10;
    final int MG_ATK_BASE=30,MG_DEF_BASE=5;
    final int CU_ATK_BASE=25,CU_DEF_BASE=15;

    // Objetos (uso único)
    boolean tieneFuerza=true, tieneCura=true, tieneCuraEstados=true;

    // Estado del juego
    String heroeActual = "espadachin"; // espadachin, mago, curandera
    boolean turnoJugador = true;
    boolean partidaTerminada = false;
    boolean esperandoObjeto = false;

    Random random = new Random();

    // ─── Componentes UI ─────────────────────────────────────
    JTextPane logArea;
    StyledDocument logDoc;
    JScrollPane logScroll;

    // Barras de vida
    BarraVida barraEs, barraMg, barraCu, barraDf;

    // Labels de estado
    JLabel lblEstadoEs, lblEstadoMg, lblEstadoCu, lblEstadoDf;
    JLabel lblContadorEs, lblContadorMg, lblContadorCu;
    JLabel lblHeroeActual;
    JLabel lblTurno;

    // Paneles de personajes
    JPanel panelEs, panelMg, panelCu, panelDf;

    // Botones de acción
    JButton btnAtacar, btnCubrirse, btnObjeto, btnCambiar, btnHabilidad;
    JButton btnObjFuerza, btnObjCura, btnObjCuraEstados, btnObjCancelar;
    JButton btnCambiarEs, btnCambiarMg, btnCambiarCu, btnCambiarCancelar;

    // Paneles de acciones
    JPanel panelAcciones, panelObjetos, panelCambio;
    CardLayout cardAcciones;
    JPanel contenedorAcciones;

    // Panel de juego (visual de personajes)
    PanelBatalla panelBatalla;

    // ─── Constructor ────────────────────────────────────────
    public JuegoGUI() {
        super("⚔️ Héroes vs Demonio de Fuego");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 800);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 0));

        construirUI();
        setVisible(true);

        // Mostrar instrucciones y empezar
        SwingUtilities.invokeLater(() -> {
            mostrarInstrucciones();
            iniciarTurnoJugador();
        });
    }

    // ═════════════════════════════════════════════════════════
    //  CONSTRUCCIÓN DE LA INTERFAZ
    // ═════════════════════════════════════════════════════════

    void construirUI() {
        // ── Panel superior: título ──
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BG_DARK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));

        JLabel titulo = new JLabel("⚔️  HÉROES  vs  DEMONIO DE FUEGO  ⚔️", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(GOLD);
        topPanel.add(titulo, BorderLayout.CENTER);

        lblTurno = new JLabel("🎮 Turno de los Héroes", SwingConstants.CENTER);
        lblTurno.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTurno.setForeground(TEXT_WHITE);
        lblTurno.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        topPanel.add(lblTurno, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // ── Panel central: dividido en batalla + log ──
        JSplitPane splitMain = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitMain.setBackground(BG_DARK);
        splitMain.setDividerLocation(420);
        splitMain.setResizeWeight(0.6);
        splitMain.setBorder(null);
        splitMain.setDividerSize(4);

        // Panel de batalla (personajes)
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setBackground(BG_DARK);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Héroes a la izquierda
        JPanel panelHeroes = new JPanel(new GridLayout(3, 1, 0, 8));
        panelHeroes.setBackground(BG_DARK);
        panelHeroes.setPreferredSize(new Dimension(280, 0));

        panelEs = crearPanelPersonaje("⚔️ ESPADACHÍN", esVida, 500, ESPA_YELLOW, "espadachin");
        panelMg = crearPanelPersonaje("🧙 MAGO", mgVida, 500, MAGO_CYAN, "mago");
        panelCu = crearPanelPersonaje("💚 CURANDERA", cuVida, 500, CURANDERA_PK, "curandera");

        panelHeroes.add(panelEs);
        panelHeroes.add(panelMg);
        panelHeroes.add(panelCu);

        // Batalla visual en el centro
        panelBatalla = new PanelBatalla();
        panelBatalla.setPreferredSize(new Dimension(400, 0));

        // Villano a la derecha
        JPanel panelVillano = new JPanel(new BorderLayout());
        panelVillano.setBackground(BG_DARK);
        panelVillano.setPreferredSize(new Dimension(280, 0));

        panelDf = crearPanelVillano("🔥 DEMONIO DE FUEGO", dfVida, 1500, DEMONIO_RED);
        panelVillano.add(panelDf, BorderLayout.CENTER);

        panelSuperior.add(panelHeroes, BorderLayout.WEST);
        panelSuperior.add(panelBatalla, BorderLayout.CENTER);
        panelSuperior.add(panelVillano, BorderLayout.EAST);

        // Panel inferior: log + acciones
        JPanel panelInferior = new JPanel(new BorderLayout(10, 0));
        panelInferior.setBackground(BG_DARK);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));

        // Log de combate
        logArea = new JTextPane();
        logArea.setEditable(false);
        logArea.setBackground(new Color(20, 20, 30));
        logArea.setForeground(TEXT_WHITE);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        logArea.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        logDoc = logArea.getStyledDocument();

        logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 90), 1),
            " 📜 Log de Combate ",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 12), GOLD));
        logScroll.setPreferredSize(new Dimension(0, 200));
        ((JComponent) logScroll.getViewport().getView()).setBackground(new Color(20, 20, 30));

        // Panel de acciones
        construirPanelAcciones();

        panelInferior.add(logScroll, BorderLayout.CENTER);
        panelInferior.add(contenedorAcciones, BorderLayout.EAST);

        splitMain.setTopComponent(panelSuperior);
        splitMain.setBottomComponent(panelInferior);

        add(splitMain, BorderLayout.CENTER);
    }

    // ── Panel de un héroe ──
    JPanel crearPanelPersonaje(String nombre, int vida, int vidaMax, Color color, String id) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblNombre.setForeground(color);
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        BarraVida barra = new BarraVida(vida, vidaMax, GREEN_HP);
        barra.setAlignmentX(Component.LEFT_ALIGNMENT);
        barra.setMaximumSize(new Dimension(260, 22));
        barra.setPreferredSize(new Dimension(260, 22));

        JLabel lblEstado = new JLabel("Estado: Normal");
        lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblEstado.setForeground(TEXT_DIM);
        lblEstado.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblContador = new JLabel("Hab. Especial: ⏳");
        lblContador.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblContador.setForeground(TEXT_DIM);
        lblContador.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblNombre);
        panel.add(Box.createVerticalStrut(4));
        panel.add(barra);
        panel.add(Box.createVerticalStrut(3));
        panel.add(lblEstado);
        panel.add(lblContador);

        // Guardar referencias
        if (id.equals("espadachin")) { barraEs = barra; lblEstadoEs = lblEstado; lblContadorEs = lblContador; }
        else if (id.equals("mago"))  { barraMg = barra; lblEstadoMg = lblEstado; lblContadorMg = lblContador; }
        else                         { barraCu = barra; lblEstadoCu = lblEstado; lblContadorCu = lblContador; }

        return panel;
    }

    // ── Panel del villano ──
    JPanel crearPanelVillano(String nombre, int vida, int vidaMax, Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DEMONIO_RED.darker(), 2),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblNombre.setForeground(color);
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        barraDf = new BarraVida(vida, vidaMax, RED_HP);
        barraDf.setAlignmentX(Component.LEFT_ALIGNMENT);
        barraDf.setMaximumSize(new Dimension(260, 24));
        barraDf.setPreferredSize(new Dimension(260, 24));

        lblEstadoDf = new JLabel("Estado: Normal");
        lblEstadoDf.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblEstadoDf.setForeground(TEXT_DIM);
        lblEstadoDf.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblInfo = new JLabel("<html>⚔️ ATK: 45 &nbsp; 🛡️ DEF: 15</html>");
        lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblInfo.setForeground(ORANGE_FIRE);
        lblInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblHab = new JLabel("<html>💥 Hab1: cada 3 turnos<br>💥 Hab2: cada 5 turnos</html>");
        lblHab.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblHab.setForeground(TEXT_DIM);
        lblHab.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblNombre);
        panel.add(Box.createVerticalStrut(8));
        panel.add(barraDf);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblEstadoDf);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblInfo);
        panel.add(Box.createVerticalStrut(3));
        panel.add(lblHab);

        // Rellenar espacio
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    // ── Panel de acciones con CardLayout ──
    void construirPanelAcciones() {
        contenedorAcciones = new JPanel();
        cardAcciones = new CardLayout();
        contenedorAcciones.setLayout(cardAcciones);
        contenedorAcciones.setBackground(BG_DARK);
        contenedorAcciones.setPreferredSize(new Dimension(320, 0));

        // === Panel de acciones principales ===
        panelAcciones = new JPanel(new GridLayout(6, 1, 0, 6));
        panelAcciones.setBackground(BG_PANEL);
        panelAcciones.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(GOLD, 1),
                " 🎯 Acciones ",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12), GOLD),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        lblHeroeActual = new JLabel("Controlando: Espadachín", SwingConstants.CENTER);
        lblHeroeActual.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblHeroeActual.setForeground(ESPA_YELLOW);

        btnAtacar   = crearBoton("⚔️  Atacar",            new Color(200, 60, 60));
        btnCubrirse = crearBoton("🛡️  Cubrirse",          new Color(60, 130, 200));
        btnObjeto   = crearBoton("🧪  Utilizar Objeto",    new Color(60, 180, 80));
        btnCambiar  = crearBoton("🔄  Cambiar Personaje",  new Color(180, 140, 50));
        btnHabilidad= crearBoton("⭐  Habilidad Especial", PURPLE);
        btnHabilidad.setVisible(false);

        btnAtacar.addActionListener(e -> accionAtacar());
        btnCubrirse.addActionListener(e -> accionCubrirse());
        btnObjeto.addActionListener(e -> mostrarPanelObjetos());
        btnCambiar.addActionListener(e -> mostrarPanelCambio());
        btnHabilidad.addActionListener(e -> accionHabilidadEspecial());

        panelAcciones.add(lblHeroeActual);
        panelAcciones.add(btnAtacar);
        panelAcciones.add(btnCubrirse);
        panelAcciones.add(btnObjeto);
        panelAcciones.add(btnCambiar);
        panelAcciones.add(btnHabilidad);

        // === Panel de objetos ===
        panelObjetos = new JPanel(new GridLayout(5, 1, 0, 6));
        panelObjetos.setBackground(BG_PANEL);
        panelObjetos.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(60, 180, 80), 1),
                " 🧪 Objetos ",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12), new Color(60, 180, 80)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        JLabel lblObjInfo = new JLabel("Elige un objeto:", SwingConstants.CENTER);
        lblObjInfo.setForeground(TEXT_WHITE);
        lblObjInfo.setFont(new Font("SansSerif", Font.PLAIN, 12));

        btnObjFuerza      = crearBoton("💪  Poción de Fuerza (+50 ATK)", new Color(200, 120, 40));
        btnObjCura         = crearBoton("❤️  Poción de Cura (+100 HP)",  new Color(200, 60, 60));
        btnObjCuraEstados  = crearBoton("✨  Poción Cura Estados",       new Color(100, 60, 200));
        btnObjCancelar     = crearBoton("❌  Cancelar",                   new Color(100, 100, 120));

        btnObjFuerza.addActionListener(e -> usarObjeto(1));
        btnObjCura.addActionListener(e -> usarObjeto(2));
        btnObjCuraEstados.addActionListener(e -> usarObjeto(3));
        btnObjCancelar.addActionListener(e -> { cardAcciones.show(contenedorAcciones, "acciones"); });

        panelObjetos.add(lblObjInfo);
        panelObjetos.add(btnObjFuerza);
        panelObjetos.add(btnObjCura);
        panelObjetos.add(btnObjCuraEstados);
        panelObjetos.add(btnObjCancelar);

        // === Panel de cambio de personaje ===
        panelCambio = new JPanel(new GridLayout(5, 1, 0, 6));
        panelCambio.setBackground(BG_PANEL);
        panelCambio.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 140, 50), 1),
                " 🔄 Cambiar Personaje ",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12), new Color(180, 140, 50)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        JLabel lblCambioInfo = new JLabel("Elige un héroe:", SwingConstants.CENTER);
        lblCambioInfo.setForeground(TEXT_WHITE);
        lblCambioInfo.setFont(new Font("SansSerif", Font.PLAIN, 12));

        btnCambiarEs = crearBoton("⚔️  Espadachín", ESPA_YELLOW.darker());
        btnCambiarMg = crearBoton("🧙  Mago",       MAGO_CYAN.darker());
        btnCambiarCu = crearBoton("💚  Curandera",   CURANDERA_PK.darker());
        btnCambiarCancelar = crearBoton("❌  Cancelar", new Color(100, 100, 120));

        btnCambiarEs.addActionListener(e -> cambiarA("espadachin"));
        btnCambiarMg.addActionListener(e -> cambiarA("mago"));
        btnCambiarCu.addActionListener(e -> cambiarA("curandera"));
        btnCambiarCancelar.addActionListener(e -> { cardAcciones.show(contenedorAcciones, "acciones"); });

        panelCambio.add(lblCambioInfo);
        panelCambio.add(btnCambiarEs);
        panelCambio.add(btnCambiarMg);
        panelCambio.add(btnCambiarCu);
        panelCambio.add(btnCambiarCancelar);

        contenedorAcciones.add(panelAcciones, "acciones");
        contenedorAcciones.add(panelObjetos, "objetos");
        contenedorAcciones.add(panelCambio, "cambio");
    }

    JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);

        Color hover = color.brighter();
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { if (btn.isEnabled()) btn.setBackground(hover); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(color); }
        });

        return btn;
    }

    // ═════════════════════════════════════════════════════════
    //  LOG DE COMBATE
    // ═════════════════════════════════════════════════════════

    void log(String msg, Color color) {
        SwingUtilities.invokeLater(() -> {
            try {
                Style style = logArea.addStyle("color", null);
                StyleConstants.setForeground(style, color);
                StyleConstants.setFontSize(style, 13);
                logDoc.insertString(logDoc.getLength(), msg + "\n", style);
                logArea.setCaretPosition(logDoc.getLength());
            } catch (Exception ex) { /* ignorar */ }
        });
    }

    void log(String msg) { log(msg, TEXT_WHITE); }
    void logInfo(String msg) { log("ℹ️  " + msg, MAGO_CYAN); }
    void logAtaque(String msg) { log("⚔️  " + msg, ORANGE_FIRE); }
    void logDano(String msg) { log("💥  " + msg, RED_HP); }
    void logCura(String msg) { log("💚  " + msg, GREEN_HP); }
    void logEstado(String msg) { log("🔥  " + msg, new Color(255, 150, 50)); }
    void logEspecial(String msg) { log("⭐  " + msg, PURPLE); }
    void logSistema(String msg) { log("━━━ " + msg + " ━━━", GOLD); }

    // ═════════════════════════════════════════════════════════
    //  DADO
    // ═════════════════════════════════════════════════════════

    int tirarDado() {
        return random.nextInt(6) + 1;
    }

    // ═════════════════════════════════════════════════════════
    //  LÓGICA DEL JUEGO
    // ═════════════════════════════════════════════════════════

    void mostrarInstrucciones() {
        String instrucciones =
            "🎮 HÉROES vs DEMONIO DE FUEGO 🎮\n\n" +
            "Controlas a 3 héroes: Espadachín, Mago y Curandera.\n" +
            "Debes derrotar al Demonio de Fuego (1500 HP).\n\n" +
            "ACCIONES:\n" +
            "  ⚔️ Atacar: daño = ATK × dado - DEF enemigo (dado=6 → crítico ×2)\n" +
            "  🛡️ Cubrirse: recibes solo 50 de daño fijo, anula turno del villano\n" +
            "  🧪 Objeto: usa una poción (no gasta turno)\n" +
            "  🔄 Cambiar: controla otro héroe\n" +
            "  ⭐ Hab. Especial: se desbloquea tras varios turnos\n\n" +
            "HABILIDADES ESPECIALES:\n" +
            "  • Espadachín (3 turnos): Contraataque — devuelve el daño ×2\n" +
            "  • Mago (5 turnos): Hechizo de Hielo — ataque + congela al enemigo\n" +
            "  • Curandera (2 turnos): Curación grupal — +100 HP a todos\n\n" +
            "ESTADOS: Quemadura (-50 HP), Aturdimiento (no atacar),\n" +
            "         Parálisis (dado ≥4 para moverse)\n\n" +
            "¡BUENA SUERTE!";

        JTextArea area = new JTextArea(instrucciones);
        area.setEditable(false);
        area.setFont(new Font("SansSerif", Font.PLAIN, 13));
        area.setBackground(BG_CARD);
        area.setForeground(TEXT_WHITE);
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane sp = new JScrollPane(area);
        sp.setPreferredSize(new Dimension(520, 420));

        JOptionPane.showMessageDialog(this, sp, "📖 Instrucciones del Juego",
            JOptionPane.PLAIN_MESSAGE);
    }

    // ── Iniciar turno del jugador ──
    void iniciarTurnoJugador() {
        if (partidaTerminada) return;

        turnoJugador = true;
        lblTurno.setText("🎮 Turno de los Héroes");
        lblTurno.setForeground(GREEN_HP);
        logSistema("TURNO DE LOS HÉROES");

        // Seleccionar primer héroe vivo
        if (esVida > 0)      heroeActual = "espadachin";
        else if (mgVida > 0) heroeActual = "mago";
        else if (cuVida > 0) heroeActual = "curandera";

        actualizarHeroeActual();
        habilitarAcciones(true);
        cardAcciones.show(contenedorAcciones, "acciones");
    }

    void actualizarHeroeActual() {
        String nombre = "";
        Color color = TEXT_WHITE;
        int contador = 0, limite = 0;

        switch (heroeActual) {
            case "espadachin": nombre = "⚔️ Espadachín"; color = ESPA_YELLOW; contador = esContador; limite = 3; break;
            case "mago":       nombre = "🧙 Mago";       color = MAGO_CYAN;   contador = mgContador; limite = 5; break;
            case "curandera":  nombre = "💚 Curandera";   color = CURANDERA_PK;contador = cuContador; limite = 2; break;
        }

        lblHeroeActual.setText("Controlando: " + nombre);
        lblHeroeActual.setForeground(color);
        logInfo("Controlando a " + nombre.substring(3));

        // Mostrar/ocultar habilidad especial
        btnHabilidad.setVisible(contador >= limite);

        // Actualizar tooltip de habilidad
        switch (heroeActual) {
            case "espadachin": btnHabilidad.setText("⭐  Contraataque"); break;
            case "mago":       btnHabilidad.setText("⭐  Hechizo de Hielo"); break;
            case "curandera":  btnHabilidad.setText("⭐  Curación Grupal"); break;
        }

        resaltarHeroeActivo();
    }

    void resaltarHeroeActivo() {
        Color normalBorder = BG_CARD;
        panelEs.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(heroeActual.equals("espadachin") ? GOLD : ESPA_YELLOW.darker(), heroeActual.equals("espadachin") ? 3 : 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        panelMg.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(heroeActual.equals("mago") ? GOLD : MAGO_CYAN.darker(), heroeActual.equals("mago") ? 3 : 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        panelCu.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(heroeActual.equals("curandera") ? GOLD : CURANDERA_PK.darker(), heroeActual.equals("curandera") ? 3 : 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
    }

    void habilitarAcciones(boolean val) {
        btnAtacar.setEnabled(val);
        btnCubrirse.setEnabled(val);
        btnObjeto.setEnabled(val);
        btnCambiar.setEnabled(val);
        btnHabilidad.setEnabled(val);
    }

    // ════════════════════════════════════════════════════
    //  ACCIONES DEL JUGADOR
    // ════════════════════════════════════════════════════

    void accionAtacar() {
        if (partidaTerminada) return;

        int estado = obtenerEstado(heroeActual);
        int atk = obtenerAtaque(heroeActual);
        String nombre = nombreHeroe(heroeActual);

        // Comprobar estados
        if (estado == 3) {
            logEstado(nombre + " está aturdido y no puede atacar.");
            finTurnoJugador(false);
            return;
        }

        if (estado == 4) {
            logEstado(nombre + " está paralizado...");
            int dado = tirarDado();
            if (dado >= 4) {
                logEstado("¡Pero puede moverse! (dado: " + dado + ")");
                cambiarEstado(heroeActual, 5);
            } else {
                logEstado("¡No puede moverse! (dado: " + dado + ")");
                finTurnoJugador(false);
                return;
            }
        }

        // Ejecutar ataque
        ejecutarAtaque(heroeActual, atk, "demonio");

        // Efecto pasivo del espadachín: aturdir
        if (heroeActual.equals("espadachin") && dfVida > 0 && obtenerEstado("espadachin") == 0) {
            int dado = tirarDado();
            if (dado >= 5) {
                logEstado("¡El golpe ha aturdido al Demonio! No podrá atacar en su siguiente turno (dado: " + dado + ")");
                dfEstado = 3;
            }
        }

        // Efecto pasivo del mago: paralizar
        if (heroeActual.equals("mago") && dfVida > 0 && obtenerEstado("mago") == 0) {
            int dado = tirarDado();
            if (dado >= 4) {
                logEstado("¡El Demonio ha quedado paralizado! (dado: " + dado + ")");
                dfEstado = 4;
            }
        }

        sumarContador(heroeActual);
        finTurnoJugador(false);
    }

    void accionCubrirse() {
        if (partidaTerminada) return;
        String nombre = nombreHeroe(heroeActual);

        logInfo(nombre + " se cubre y recibe daño reducido (50).");
        bajarVidaHeroe(heroeActual, 50);
        setCubierto(heroeActual, true);
        sumarContador(heroeActual);

        // Al cubrirse, se saltan los contadores del demonio
        dfContHab1++;
        dfContHab2++;

        finTurnoJugador(true);
    }

    void mostrarPanelObjetos() {
        // Actualizar disponibilidad
        btnObjFuerza.setEnabled(tieneFuerza);
        btnObjCura.setEnabled(tieneCura);
        btnObjCuraEstados.setEnabled(tieneCuraEstados);

        if (!tieneFuerza) btnObjFuerza.setText("💪  Poción de Fuerza (AGOTADA)");
        if (!tieneCura)   btnObjCura.setText("❤️  Poción de Cura (AGOTADA)");
        if (!tieneCuraEstados) btnObjCuraEstados.setText("✨  Cura Estados (AGOTADA)");

        cardAcciones.show(contenedorAcciones, "objetos");
    }

    void usarObjeto(int tipo) {
        String nombre = nombreHeroe(heroeActual);

        if (tipo == 1) {
            if (tieneFuerza) {
                subirAtaque(heroeActual, 50);
                tieneFuerza = false;
                logCura("¡" + nombre + " usa Poción de Fuerza! ATK +50");
                btnObjFuerza.setText("💪  Poción de Fuerza (AGOTADA)");
                btnObjFuerza.setEnabled(false);
            } else {
                logInfo("No tienes Poción de Fuerza.");
            }
        } else if (tipo == 2) {
            if (tieneCura) {
                curar(heroeActual);
                tieneCura = false;
                logCura("¡" + nombre + " usa Poción de Cura! +100 HP");
                btnObjCura.setText("❤️  Poción de Cura (AGOTADA)");
                btnObjCura.setEnabled(false);
            } else {
                logInfo("No tienes Poción de Cura.");
            }
        } else if (tipo == 3) {
            if (tieneCuraEstados) {
                cambiarEstado(heroeActual, 0);
                tieneCuraEstados = false;
                logCura("¡" + nombre + " usa Cura Estados! Estado → Normal");
                btnObjCuraEstados.setText("✨  Cura Estados (AGOTADA)");
                btnObjCuraEstados.setEnabled(false);
            } else {
                logInfo("No tienes Poción Cura Estados.");
            }
        }

        actualizarUI();
        // Usar objeto NO gasta turno, volver al menú de acciones
        cardAcciones.show(contenedorAcciones, "acciones");
    }

    void mostrarPanelCambio() {
        btnCambiarEs.setEnabled(esVida > 0 && !heroeActual.equals("espadachin"));
        btnCambiarMg.setEnabled(mgVida > 0 && !heroeActual.equals("mago"));
        btnCambiarCu.setEnabled(cuVida > 0 && !heroeActual.equals("curandera"));
        cardAcciones.show(contenedorAcciones, "cambio");
    }

    void cambiarA(String heroe) {
        int vida = heroe.equals("espadachin") ? esVida : heroe.equals("mago") ? mgVida : cuVida;
        if (vida <= 0) {
            logInfo("Ese personaje está muerto.");
            return;
        }
        if (heroe.equals(heroeActual)) {
            logInfo("Ya estás controlando a ese héroe.");
            return;
        }
        heroeActual = heroe;
        logInfo("Cambias de personaje a " + nombreHeroe(heroe) + ".");
        actualizarHeroeActual();
        cardAcciones.show(contenedorAcciones, "acciones");
    }

    void accionHabilidadEspecial() {
        if (partidaTerminada) return;
        String nombre = nombreHeroe(heroeActual);

        switch (heroeActual) {
            case "espadachin":
                // Contraataque: el enemigo ataca al espadachín y se devuelve ×2
                logEspecial("¡" + nombre + " toma postura de CONTRAATAQUE!");
                int dado = tirarDado();
                int danoRecibido = dfAtk * dado - esDef;
                if (danoRecibido < 0) danoRecibido = 0;
                logDano("El Demonio ataca al Espadachín causando " + danoRecibido + " de daño");
                bajarVidaHeroe("espadachin", danoRecibido);
                int danoDevuelto = danoRecibido * 2;
                logEspecial("¡Pero lo REPELE devolviendo " + danoDevuelto + " de daño al Demonio!");
                bajarVidaDemonio(danoDevuelto);
                esContador = -1;
                esHabEspecial = true;
                break;

            case "mago":
                // Hechizo de Hielo: ataque + congelar
                logEspecial("¡" + nombre + " lanza el HECHIZO DE HIELO! ❄️");
                ejecutarAtaque("mago", mgAtk, "demonio");
                logEspecial("¡El Demonio queda CONGELADO! No podrá atacar este turno.");
                mgContador = -1;
                mgHabEspecial = true;
                break;

            case "curandera":
                // Curación grupal
                logEspecial("¡" + nombre + " usa CURACIÓN GRUPAL! 💚");
                if (esVida > 0) { curar("espadachin"); logCura("Espadachín recupera HP → " + esVida); }
                if (mgVida > 0) { curar("mago");       logCura("Mago recupera HP → " + mgVida); }
                if (cuVida > 0) { curar("curandera");   logCura("Curandera recupera HP → " + cuVida); }
                cuContador = -1;
                break;
        }

        sumarContador(heroeActual);
        finTurnoJugador(heroeActual.equals("espadachin") || heroeActual.equals("mago"));
    }

    // ── Fin turno jugador → restauración → turno villano ──
    void finTurnoJugador(boolean saltarTurnoVillano) {
        habilitarAcciones(false);

        // Restauración: aplicar estados, restaurar stats
        restauracion();
        actualizarUI();

        // Comprobar fin de partida
        if (comprobarFinPartida()) return;

        boolean skipVillano = saltarTurnoVillano || esHabEspecial || mgHabEspecial || hayCubierto();

        if (skipVillano) {
            esHabEspecial = false;
            mgHabEspecial = false;
            quitarCubiertos();
            if (!saltarTurnoVillano) {
                logInfo("Se salta el turno del Demonio (habilidad especial / cubierto).");
            }
            // Siguiente turno del jugador
            Timer t = new Timer(800, e -> iniciarTurnoJugador());
            t.setRepeats(false);
            t.start();
        } else {
            // Turno del villano
            Timer t = new Timer(1000, e -> turnoVillano());
            t.setRepeats(false);
            t.start();
        }
    }

    // ════════════════════════════════════════════════════
    //  TURNO DEL VILLANO
    // ════════════════════════════════════════════════════

    void turnoVillano() {
        if (partidaTerminada) return;
        if (dfVida <= 0) { comprobarFinPartida(); return; }

        turnoJugador = false;
        lblTurno.setText("👹 Turno del Demonio de Fuego");
        lblTurno.setForeground(DEMONIO_RED);
        logSistema("TURNO DEL DEMONIO DE FUEGO");

        panelBatalla.setAnimacionDemonio(true);

        // Elegir objetivo aleatorio
        int dado = tirarDado();
        String objetivo;
        if ((dado == 1 || dado == 2) && esVida > 0)      objetivo = "espadachin";
        else if ((dado == 3 || dado == 4) && mgVida > 0)  objetivo = "mago";
        else if ((dado == 5 || dado == 6) && cuVida > 0)  objetivo = "curandera";
        else {
            // Buscar primer héroe vivo
            if (esVida > 0)      objetivo = "espadachin";
            else if (mgVida > 0) objetivo = "mago";
            else                 objetivo = "curandera";
        }

        logAtaque("¡El Demonio de Fuego ataca a " + nombreHeroe(objetivo) + "!");

        // Determinar tipo de acción
        if (dfContHab2 % 5 == 0 && dfContHab2 != 0) {
            accionVillanoHab2(objetivo);
        } else if (dfContHab1 % 3 == 0 && dfContHab2 % 5 != 0 && dfContHab1 != 0) {
            accionVillanoHab1(objetivo);
        } else {
            accionVillanoNormal(objetivo);
        }

        dfContHab1++;
        dfContHab2++;

        // Restaurar estado del demonio
        dfEstado = 0;
        actualizarUI();

        Timer t = new Timer(1500, e -> {
            panelBatalla.setAnimacionDemonio(false);
            if (!comprobarFinPartida()) {
                iniciarTurnoJugador();
            }
        });
        t.setRepeats(false);
        t.start();
    }

    void accionVillanoNormal(String objetivo) {
        if (dfEstado == 3) {
            logEstado("¡El Demonio está aturdido! No puede atacar.");
            return;
        }
        if (dfEstado == 4) {
            logEstado("El Demonio está paralizado...");
            int d = tirarDado();
            if (d >= 4) {
                logEstado("¡Pero puede moverse! (dado: " + d + ")");
                dfEstado = 5;
            } else {
                logEstado("¡No puede moverse! (dado: " + d + ")");
                return;
            }
        }

        ejecutarAtaque("demonio", dfAtk, objetivo);

        // Quema
        if (dfEstado == 0 && obtenerVida(objetivo) > 0) {
            int dq = tirarDado();
            if (dq >= 5) {
                cambiarEstado(objetivo, 1);
                logEstado("¡" + nombreHeroe(objetivo) + " ha sido QUEMADO! 🔥 (dado: " + dq + ")");
            }
        }
    }

    void accionVillanoHab1(String objetivo) {
        if (dfEstado == 3) {
            logEstado("¡El Demonio está aturdido!");
            return;
        }
        logEspecial("¡El Demonio usa ATAQUE ESPECIAL! 💥");
        ejecutarAtaque("demonio", dfAtk, objetivo);
        if (dfEstado != 3 && dfEstado != 4 && obtenerVida(objetivo) > 0) {
            cambiarEstado(objetivo, 3);
            logEstado("¡" + nombreHeroe(objetivo) + " ha sido ATURDIDO!");
        }
    }

    void accionVillanoHab2(String objetivo) {
        if (dfEstado == 3) {
            logEstado("¡El Demonio está aturdido!");
            return;
        }
        logEspecial("¡El Demonio usa DOBLE ATAQUE! 💥💥");
        ejecutarAtaque("demonio", dfAtk, objetivo);
        if (obtenerVida(objetivo) > 0) {
            ejecutarAtaque("demonio", dfAtk, objetivo);
        }
    }

    // ════════════════════════════════════════════════════
    //  MECÁNICAS DE COMBATE
    // ════════════════════════════════════════════════════

    void ejecutarAtaque(String atacante, int atk, String defensor) {
        int def = defensor.equals("demonio") ? dfDef : obtenerDefensa(defensor);
        int dado = tirarDado();
        int dano;

        if (dado < 6) {
            dano = atk * dado - def;
            if (dano < 0) dano = 0;
            logAtaque(nombreCompleto(atacante) + " ataca (dado: " + dado + ") → Daño: " + dano);
        } else {
            dano = (atk * dado - def) * 2;
            if (dano < 0) dano = 0;
            logAtaque("¡¡GOLPE CRÍTICO!! (dado: 6) → Daño: " + dano + " 💥");
        }

        if (defensor.equals("demonio")) {
            bajarVidaDemonio(dano);
        } else {
            bajarVidaHeroe(defensor, dano);
        }
    }

    void bajarVidaHeroe(String heroe, int dano) {
        switch (heroe) {
            case "espadachin": esVida = Math.max(0, esVida - dano); break;
            case "mago":       mgVida = Math.max(0, mgVida - dano); break;
            case "curandera":  cuVida = Math.max(0, cuVida - dano); break;
        }
        int vidaAct = obtenerVida(heroe);
        if (vidaAct <= 0) {
            logDano("¡" + nombreHeroe(heroe) + " ha caído! ☠️");
        } else {
            logDano(nombreHeroe(heroe) + " HP: " + vidaAct);
        }
        actualizarUI();
        panelBatalla.flashDano(heroe);
    }

    void bajarVidaDemonio(int dano) {
        dfVida = Math.max(0, dfVida - dano);
        if (dfVida <= 0) {
            logDano("¡El Demonio de Fuego ha caído! 🎉");
        } else {
            logDano("Demonio de Fuego HP: " + dfVida);
        }
        actualizarUI();
        panelBatalla.flashDano("demonio");
    }

    void curar(String heroe) {
        switch (heroe) {
            case "espadachin": esVida = Math.min(500, esVida + 100); break;
            case "mago":       mgVida = Math.min(500, mgVida + 100); break;
            case "curandera":  cuVida = Math.min(500, cuVida + 100); break;
        }
        actualizarUI();
    }

    void subirAtaque(String heroe, int cantidad) {
        switch (heroe) {
            case "espadachin": esAtk += cantidad; break;
            case "mago":       mgAtk += cantidad; break;
            case "curandera":  cuAtk += cantidad; break;
        }
    }

    void sumarContador(String heroe) {
        switch (heroe) {
            case "espadachin": esContador++; break;
            case "mago":       mgContador++; break;
            case "curandera":  cuContador++; break;
        }
    }

    // ── Restauración de fin de turno ──
    void restauracion() {
        // Aplicar quemaduras
        if (esEstado == 1 && esVida > 0) {
            logEstado("¡Espadachín recibe daño de quemadura! -50 HP 🔥");
            esVida = Math.max(0, esVida - 50);
            if (esVida <= 0) logDano("¡El Espadachín ha caído por la quemadura! ☠️");
        }
        if (mgEstado == 1 && mgVida > 0) {
            logEstado("¡Mago recibe daño de quemadura! -50 HP 🔥");
            mgVida = Math.max(0, mgVida - 50);
            if (mgVida <= 0) logDano("¡El Mago ha caído por la quemadura! ☠️");
        }
        if (cuEstado == 1 && cuVida > 0) {
            logEstado("¡Curandera recibe daño de quemadura! -50 HP 🔥");
            cuVida = Math.max(0, cuVida - 50);
            if (cuVida <= 0) logDano("¡La Curandera ha caído por la quemadura! ☠️");
        }

        // Restaurar estados
        esEstado = 0; mgEstado = 0; cuEstado = 0;

        // Restaurar stats base
        esAtk = ES_ATK_BASE; esDef = ES_DEF_BASE;
        mgAtk = MG_ATK_BASE; mgDef = MG_DEF_BASE;
        cuAtk = CU_ATK_BASE; cuDef = CU_DEF_BASE;
    }

    boolean hayCubierto() {
        return esCubierto || mgCubierto || cuCubierto;
    }

    void quitarCubiertos() {
        esCubierto = false; mgCubierto = false; cuCubierto = false;
    }

    void setCubierto(String heroe, boolean val) {
        switch (heroe) {
            case "espadachin": esCubierto = val; break;
            case "mago":       mgCubierto = val; break;
            case "curandera":  cuCubierto = val; break;
        }
    }

    // ── Fin de partida ──
    boolean comprobarFinPartida() {
        if (dfVida <= 0) {
            partidaTerminada = true;
            int puntuacion = Math.max(0, esVida) + Math.max(0, mgVida) + Math.max(0, cuVida);
            logSistema("¡¡VICTORIA!!");
            log("🏆  ¡Enhorabuena! Has derrotado al Demonio de Fuego!", GOLD);
            log("🏆  Tu puntuación: " + puntuacion + " puntos", GOLD);
            lblTurno.setText("🏆 ¡¡VICTORIA!! Puntuación: " + puntuacion);
            lblTurno.setForeground(GOLD);
            habilitarAcciones(false);
            mostrarDialogoFin(true, puntuacion);
            return true;
        }
        if (esVida <= 0 && mgVida <= 0 && cuVida <= 0) {
            partidaTerminada = true;
            logSistema("DERROTA");
            log("💀  Todos los héroes han caído... ¡Inténtalo de nuevo!", RED_HP);
            lblTurno.setText("💀 DERROTA — Todos los héroes han caído");
            lblTurno.setForeground(RED_HP);
            habilitarAcciones(false);
            mostrarDialogoFin(false, 0);
            return true;
        }
        return false;
    }

    void mostrarDialogoFin(boolean victoria, int puntuacion) {
        Timer t = new Timer(500, e -> {
            String msg = victoria
                ? "🏆 ¡Has derrotado al Demonio de Fuego!\n\nPuntuación: " + puntuacion + " puntos\n\n¿Quieres jugar de nuevo?"
                : "💀 Todos tus héroes han caído...\n\n¿Quieres intentarlo de nuevo?";
            int resp = JOptionPane.showConfirmDialog(this, msg,
                victoria ? "¡VICTORIA!" : "DERROTA",
                JOptionPane.YES_NO_OPTION,
                victoria ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
            if (resp == JOptionPane.YES_OPTION) {
                reiniciarPartida();
            }
        });
        t.setRepeats(false);
        t.start();
    }

    void reiniciarPartida() {
        esVida=500;esAtk=35;esDef=10;esEstado=0;esContador=0;
        mgVida=500;mgAtk=30;mgDef=5;mgEstado=0;mgContador=0;
        cuVida=500;cuAtk=25;cuDef=15;cuEstado=0;cuContador=0;
        dfVida=1500;dfAtk=45;dfDef=15;dfEstado=0;
        dfContHab1=0;dfContHab2=0;
        esHabEspecial=false;mgHabEspecial=false;
        esCubierto=false;mgCubierto=false;cuCubierto=false;
        tieneFuerza=true;tieneCura=true;tieneCuraEstados=true;
        partidaTerminada=false;
        heroeActual="espadachin";

        // Reset botones objetos
        btnObjFuerza.setText("💪  Poción de Fuerza (+50 ATK)");
        btnObjCura.setText("❤️  Poción de Cura (+100 HP)");
        btnObjCuraEstados.setText("✨  Poción Cura Estados");

        // Limpiar log
        logArea.setText("");
        actualizarUI();
        logSistema("NUEVA PARTIDA");
        iniciarTurnoJugador();
    }

    // ════════════════════════════════════════════════════
    //  HELPERS
    // ════════════════════════════════════════════════════

    int obtenerVida(String h) {
        switch (h) { case "espadachin": return esVida; case "mago": return mgVida; case "curandera": return cuVida; default: return dfVida; }
    }
    int obtenerAtaque(String h) {
        switch (h) { case "espadachin": return esAtk; case "mago": return mgAtk; case "curandera": return cuAtk; default: return dfAtk; }
    }
    int obtenerDefensa(String h) {
        switch (h) { case "espadachin": return esDef; case "mago": return mgDef; case "curandera": return cuDef; default: return dfDef; }
    }
    int obtenerEstado(String h) {
        switch (h) { case "espadachin": return esEstado; case "mago": return mgEstado; case "curandera": return cuEstado; default: return dfEstado; }
    }
    void cambiarEstado(String h, int e) {
        switch (h) { case "espadachin": esEstado=e; break; case "mago": mgEstado=e; break; case "curandera": cuEstado=e; break; default: dfEstado=e; }
        actualizarUI();
    }
    String nombreHeroe(String h) {
        switch (h) { case "espadachin": return "Espadachín"; case "mago": return "Mago"; case "curandera": return "Curandera"; default: return "Demonio"; }
    }
    String nombreCompleto(String h) {
        if (h.equals("demonio")) return "Demonio de Fuego";
        return nombreHeroe(h);
    }
    String estadoTexto(int e) {
        switch (e) { case 1: return "🔥 Quemado"; case 3: return "💫 Aturdido"; case 4: return "⚡ Paralizado"; case 5: return "🔄 Recuperado"; default: return "✅ Normal"; }
    }

    // ── Actualizar toda la UI ──
    void actualizarUI() {
        SwingUtilities.invokeLater(() -> {
            barraEs.setValor(esVida);
            barraMg.setValor(mgVida);
            barraCu.setValor(cuVida);
            barraDf.setValor(dfVida);

            lblEstadoEs.setText("Estado: " + estadoTexto(esEstado));
            lblEstadoMg.setText("Estado: " + estadoTexto(mgEstado));
            lblEstadoCu.setText("Estado: " + estadoTexto(cuEstado));
            lblEstadoDf.setText("Estado: " + estadoTexto(dfEstado));

            lblContadorEs.setText("Hab. Especial: " + (esContador >= 3 ? "✅ LISTA" : "⏳ " + esContador + "/3"));
            lblContadorMg.setText("Hab. Especial: " + (mgContador >= 5 ? "✅ LISTA" : "⏳ " + mgContador + "/5"));
            lblContadorCu.setText("Hab. Especial: " + (cuContador >= 2 ? "✅ LISTA" : "⏳ " + cuContador + "/2"));

            // Oscurecer héroes muertos
            panelEs.setBackground(esVida > 0 ? BG_CARD : new Color(40, 25, 25));
            panelMg.setBackground(mgVida > 0 ? BG_CARD : new Color(40, 25, 25));
            panelCu.setBackground(cuVida > 0 ? BG_CARD : new Color(40, 25, 25));

            panelBatalla.repaint();
        });
    }

    // ═════════════════════════════════════════════════════════
    //  BARRA DE VIDA PERSONALIZADA
    // ═════════════════════════════════════════════════════════

    static class BarraVida extends JPanel {
        int valor, maximo;
        int valorAnimado;
        Color color;
        Timer animTimer;

        BarraVida(int valor, int maximo, Color color) {
            this.valor = valor;
            this.maximo = maximo;
            this.valorAnimado = valor;
            this.color = color;
            setOpaque(false);
        }

        void setValor(int nuevoValor) {
            this.valor = Math.max(0, nuevoValor);
            // Animación suave
            if (animTimer != null) animTimer.stop();
            animTimer = new Timer(20, e -> {
                if (valorAnimado > valor) { valorAnimado = Math.max(valor, valorAnimado - 5); repaint(); }
                else if (valorAnimado < valor) { valorAnimado = Math.min(valor, valorAnimado + 5); repaint(); }
                else { ((Timer) e.getSource()).stop(); }
            });
            animTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();

            // Fondo
            g2.setColor(new Color(30, 30, 50));
            g2.fillRoundRect(0, 0, w, h, 10, 10);

            // Barra
            float pct = (float) valorAnimado / maximo;
            Color barColor = pct > 0.5 ? GREEN_HP : pct > 0.25 ? GOLD : RED_HP;
            int bw = (int) (pct * (w - 4));
            if (bw > 0) {
                GradientPaint gp = new GradientPaint(0, 0, barColor.brighter(), 0, h, barColor.darker());
                g2.setPaint(gp);
                g2.fillRoundRect(2, 2, bw, h - 4, 8, 8);
            }

            // Borde
            g2.setColor(new Color(80, 80, 110));
            g2.drawRoundRect(0, 0, w - 1, h - 1, 10, 10);

            // Texto
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            String text = valorAnimado + " / " + maximo;
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(text, (w - fm.stringWidth(text)) / 2, (h + fm.getAscent() - 2) / 2);

            g2.dispose();
        }
    }

    // ═════════════════════════════════════════════════════════
    //  PANEL DE BATALLA (VISUAL DE PERSONAJES)
    // ═════════════════════════════════════════════════════════

    class PanelBatalla extends JPanel {
        boolean animDemonio = false;
        java.util.Map<String, Float> flashMap = new java.util.HashMap<>();
        java.util.Map<String, Timer> flashTimers = new java.util.HashMap<>();

        PanelBatalla() {
            setBackground(BG_DARK);
            setOpaque(true);
        }

        void setAnimacionDemonio(boolean val) {
            animDemonio = val;
            repaint();
        }

        void flashDano(String quien) {
            flashMap.put(quien, 1.0f);
            Timer ft = flashTimers.get(quien);
            if (ft != null) ft.stop();
            Timer t = new Timer(40, e -> {
                float v = flashMap.getOrDefault(quien, 0f) - 0.05f;
                if (v <= 0) { flashMap.remove(quien); ((Timer)e.getSource()).stop(); }
                else flashMap.put(quien, v);
                repaint();
            });
            flashTimers.put(quien, t);
            t.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();

            // Suelo
            g2.setColor(new Color(35, 30, 45));
            g2.fillRect(0, h * 3 / 4, w, h / 4);
            g2.setColor(new Color(50, 45, 60));
            g2.drawLine(0, h * 3 / 4, w, h * 3 / 4);

            // Dibujar héroes a la izquierda
            int heroX = w / 5;
            int heroY = h / 2;
            dibujarHeroe(g2, "espadachin", heroX, heroY - 100, "⚔️", ESPA_YELLOW, esVida);
            dibujarHeroe(g2, "mago",       heroX, heroY + 10,  "🧙", MAGO_CYAN, mgVida);
            dibujarHeroe(g2, "curandera",  heroX, heroY + 120, "💚", CURANDERA_PK, cuVida);

            // VS
            g2.setFont(new Font("SansSerif", Font.BOLD, 28));
            g2.setColor(new Color(255, 255, 255, 80));
            String vs = "VS";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(vs, (w - fm.stringWidth(vs)) / 2, h / 2 + 10);

            // Dibujar demonio a la derecha
            int demonX = w * 4 / 5 - 30;
            int demonY = h / 2 - 30;
            dibujarDemonio(g2, demonX, demonY);

            g2.dispose();
        }

        void dibujarHeroe(Graphics2D g2, String id, int x, int y, String emoji, Color color, int vida) {
            if (vida <= 0) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            }

            // Flash de daño
            Float flash = flashMap.get(id);
            if (flash != null && flash > 0) {
                g2.setColor(new Color(255, 50, 50, (int)(flash * 150)));
                g2.fillOval(x - 30, y - 20, 60, 60);
            }

            // Indicador de héroe activo
            if (id.equals(heroeActual) && vida > 0) {
                g2.setColor(new Color(GOLD.getRed(), GOLD.getGreen(), GOLD.getBlue(), 60));
                g2.fillOval(x - 35, y - 25, 70, 70);
                g2.setColor(GOLD);
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(x - 35, y - 25, 70, 70);
                g2.setStroke(new BasicStroke(1));
            }

            // Cuerpo
            g2.setColor(color);
            g2.fillOval(x - 20, y - 10, 40, 40);
            g2.setColor(color.darker());
            g2.drawOval(x - 20, y - 10, 40, 40);

            // Nombre
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            g2.setColor(vida > 0 ? TEXT_WHITE : TEXT_DIM);
            String nombre = nombreHeroe(id);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(vida > 0 ? nombre : nombre + " ☠️", x - fm.stringWidth(nombre) / 2, y + 45);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }

        void dibujarDemonio(Graphics2D g2, int x, int y) {
            if (dfVida <= 0) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            }

            // Flash
            Float flash = flashMap.get("demonio");
            if (flash != null && flash > 0) {
                g2.setColor(new Color(255, 255, 50, (int)(flash * 150)));
                g2.fillOval(x - 50, y - 40, 100, 100);
            }

            // Aura de fuego
            if (animDemonio && dfVida > 0) {
                g2.setColor(new Color(255, 80, 0, 40));
                g2.fillOval(x - 55, y - 45, 110, 110);
            }

            // Cuerpo
            GradientPaint gp = new GradientPaint(x, y - 30, DEMONIO_RED, x, y + 50, new Color(180, 30, 0));
            g2.setPaint(gp);
            g2.fillOval(x - 40, y - 30, 80, 80);
            g2.setColor(new Color(100, 10, 0));
            g2.drawOval(x - 40, y - 30, 80, 80);

            // Ojos
            g2.setColor(GOLD);
            g2.fillOval(x - 18, y, 12, 12);
            g2.fillOval(x + 6, y, 12, 12);
            g2.setColor(Color.BLACK);
            g2.fillOval(x - 14, y + 3, 6, 6);
            g2.fillOval(x + 10, y + 3, 6, 6);

            // Cuernos
            g2.setColor(new Color(80, 0, 0));
            g2.setStroke(new BasicStroke(3));
            g2.drawLine(x - 25, y - 20, x - 35, y - 50);
            g2.drawLine(x + 25, y - 20, x + 35, y - 50);
            g2.setStroke(new BasicStroke(1));

            // Nombre
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            g2.setColor(dfVida > 0 ? DEMONIO_RED : TEXT_DIM);
            String n = dfVida > 0 ? "Demonio de Fuego" : "Demonio ☠️";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(n, x - fm.stringWidth(n) / 2, y + 65);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }

    // ═════════════════════════════════════════════════════════
    //  MAIN
    // ═════════════════════════════════════════════════════════

    public static void main(String[] args) {
        // Look and feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { /* usar default */ }

        // Colores del diálogo
        UIManager.put("OptionPane.background", BG_PANEL);
        UIManager.put("Panel.background", BG_PANEL);
        UIManager.put("OptionPane.messageForeground", TEXT_WHITE);

        SwingUtilities.invokeLater(JuegoGUI::new);
    }
}
