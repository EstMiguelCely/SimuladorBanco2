package com.mycompany.simuladorbanco2;

import com.murcia.utils.*;


import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;



public class BancoSimulacion extends JFrame {

    // ── Modelo ────────────────────────────────────────────────────
    private final Banco banco = new Banco();
    private Cliente clienteActual = null;

    // ── CardLayout ────────────────────────────────────────────────
    private JPanel     panelContenido;
    private CardLayout cardLayout;

    // ── Panel MENU ────────────────────────────────────────────────
    private JPanel  pnlMenu;
    private JLabel  lblBancoTitulo, lblBancoSub;
    private JButton btnMenuCrear, btnMenuEliminar, btnMenuAtender,
                    btnMenuFila,  btnMenuHistorial, btnMenuRegistrados,
                    btnMenuSalir;

    // ── Panel CREAR CLIENTE ───────────────────────────────────────
    private JPanel     pnlCrear;
    private JLabel     lblCrearTitulo, lblCrearCuenta, lblCrearSaldo, lblCrearHint;
    private JTextField txtCrearCuenta, txtCrearSaldo;
    private JButton    btnCrearConfirmar, btnCrearVolver;

    // ── Panel ELIMINAR ────────────────────────────────────────────
    private JPanel     pnlEliminar;
    private JLabel     lblElimTitulo, lblElimTurno, lblElimHint;
    private JTextField txtElimTurno;
    private JButton    btnElimFila, btnElimHistorial, btnElimVolver;

    // ── Panel OPERACIONES ─────────────────────────────────────────
    private JPanel      pnlOperaciones;
    private JLabel      lblOpCliente, lblOpMonto;
    private JTextField  txtOpMonto;
    private JButton     btnOpConsultar, btnOpDepositar, btnOpRetirar,
                        btnOpElimHistorial, btnOpCerrar;
    private JTextArea   txtOpLog;
    private JScrollPane scrollLog;

    // ── Panel INFO (fila / historial / registrados) ───────────────
    private JPanel      pnlInfo;
    private JLabel      lblInfoTitulo;
    private JTextArea   txtInfo;
    private JScrollPane scrollInfo;
    private JButton     btnInfoVolver;

    // ── Colores ───────────────────────────────────────────────────
    private static final Color C_FONDO    = new Color(245, 247, 250);
    private static final Color C_PRIMARIO = new Color(25,  90, 160);
    private static final Color C_VERDE    = new Color(30, 140,  60);
    private static final Color C_ROJO     = new Color(190, 30,  30);
    private static final Color C_NARANJA  = new Color(180, 100,  10);
    private static final Color C_GRIS     = new Color(100, 100, 110);
    private static final Color C_MORADO   = new Color(110,  40, 150);

    // =============================================================
    //  CONSTRUCTOR
    // =============================================================
    public BancoSimulacion() {
        initComponents();
        initListeners();
    }

    // =============================================================
    //  initComponents()
    // =============================================================
    private void initComponents() {
        setTitle("Banco Nacional — Simulacion");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(540, 540);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout     = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        panelContenido.setBackground(C_FONDO);

        initPanelMenu();
        initPanelCrear();
        initPanelEliminar();
        initPanelOperaciones();
        initPanelInfo();

        panelContenido.add(pnlMenu,        "MENU");
        panelContenido.add(pnlCrear,       "CREAR");
        panelContenido.add(pnlEliminar,    "ELIMINAR");
        panelContenido.add(pnlOperaciones, "OPERACIONES");
        panelContenido.add(pnlInfo,        "INFO");

        getContentPane().add(panelContenido);
        cardLayout.show(panelContenido, "MENU");
    }

    // ── Panel MENU ────────────────────────────────────────────────
    private void initPanelMenu() {
        pnlMenu = new JPanel(new BorderLayout());
        pnlMenu.setBackground(C_FONDO);

        JPanel hdr = new JPanel(new GridLayout(2, 1, 0, 2));
        hdr.setBackground(C_PRIMARIO);
        hdr.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        lblBancoTitulo = new JLabel("Banco Nacional", SwingConstants.CENTER);
        lblBancoTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblBancoTitulo.setForeground(Color.WHITE);

        lblBancoSub = new JLabel("Sistema de Atencion al Cliente", SwingConstants.CENTER);
        lblBancoSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblBancoSub.setForeground(new Color(190, 215, 255));

        hdr.add(lblBancoTitulo);
        hdr.add(lblBancoSub);

        JPanel cuerpo = new JPanel();
        cuerpo.setLayout(new BoxLayout(cuerpo, BoxLayout.Y_AXIS));
        cuerpo.setOpaque(false);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(14, 70, 14, 70));

        btnMenuCrear       = crearBoton("Crear nuevo cliente",           C_VERDE);
        btnMenuEliminar    = crearBoton("Eliminar cliente",              C_ROJO);
        btnMenuAtender     = crearBoton("Atender siguiente cliente",     C_PRIMARIO);
        btnMenuFila        = crearBoton("Ver fila de espera",            C_NARANJA);
        btnMenuHistorial   = crearBoton("Ver historial de atendidos",    C_GRIS);
        btnMenuRegistrados = crearBoton("Ver todos los registrados",     C_MORADO);
        btnMenuSalir       = crearBoton("Salir del sistema",             C_ROJO);

        Dimension dimBtn = new Dimension(360, 40);
        for (JButton b : new JButton[]{btnMenuCrear, btnMenuEliminar, btnMenuAtender,
                btnMenuFila, btnMenuHistorial, btnMenuRegistrados, btnMenuSalir}) {
            b.setPreferredSize(dimBtn);
            b.setMaximumSize(dimBtn);
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        cuerpo.add(Box.createVerticalStrut(6));
        cuerpo.add(btnMenuCrear);       cuerpo.add(Box.createVerticalStrut(5));
        cuerpo.add(btnMenuEliminar);    cuerpo.add(Box.createVerticalStrut(5));
        cuerpo.add(btnMenuAtender);     cuerpo.add(Box.createVerticalStrut(5));
        cuerpo.add(btnMenuFila);        cuerpo.add(Box.createVerticalStrut(5));
        cuerpo.add(btnMenuHistorial);   cuerpo.add(Box.createVerticalStrut(5));
        cuerpo.add(btnMenuRegistrados); cuerpo.add(Box.createVerticalStrut(12));
        cuerpo.add(btnMenuSalir);

        pnlMenu.add(hdr,    BorderLayout.NORTH);
        pnlMenu.add(cuerpo, BorderLayout.CENTER);
    }

    // ── Panel CREAR CLIENTE ───────────────────────────────────────
    private void initPanelCrear() {
        pnlCrear = new JPanel(new BorderLayout());
        pnlCrear.setBackground(C_FONDO);
        pnlCrear.setBorder(BorderFactory.createEmptyBorder(28, 70, 20, 70));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        lblCrearTitulo = new JLabel("Crear Nuevo Cliente", SwingConstants.CENTER);
        lblCrearTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblCrearTitulo.setForeground(C_PRIMARIO);
        lblCrearTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblCrearHint = new JLabel(
            "<html><center>El cliente sera registrado y agregado<br>al final de la fila de espera.</center></html>",
            SwingConstants.CENTER);
        lblCrearHint.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblCrearHint.setForeground(Color.GRAY);
        lblCrearHint.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblCrearCuenta = crearLabel("Numero de cuenta:");
        txtCrearCuenta = crearTextField("", 300);

        lblCrearSaldo  = crearLabel("Saldo inicial ($):");
        txtCrearSaldo  = crearTextField("0.00", 300);

        JPanel bots = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        bots.setOpaque(false);
        btnCrearConfirmar = crearBoton("Crear y encolar", C_VERDE);
        btnCrearVolver    = crearBoton("Volver",           C_GRIS);
        btnCrearConfirmar.setPreferredSize(new Dimension(150, 38));
        btnCrearVolver.setPreferredSize(new Dimension(110, 38));
        bots.add(btnCrearConfirmar);
        bots.add(btnCrearVolver);

        form.add(lblCrearTitulo);
        form.add(Box.createVerticalStrut(6));
        form.add(lblCrearHint);
        form.add(Box.createVerticalStrut(18));
        form.add(lblCrearCuenta);
        form.add(Box.createVerticalStrut(4));
        form.add(txtCrearCuenta);
        form.add(Box.createVerticalStrut(12));
        form.add(lblCrearSaldo);
        form.add(Box.createVerticalStrut(4));
        form.add(txtCrearSaldo);
        form.add(Box.createVerticalStrut(22));
        form.add(bots);

        pnlCrear.add(form, BorderLayout.CENTER);
    }

    // ── Panel ELIMINAR ────────────────────────────────────────────
    private void initPanelEliminar() {
        pnlEliminar = new JPanel(new BorderLayout());
        pnlEliminar.setBackground(C_FONDO);
        pnlEliminar.setBorder(BorderFactory.createEmptyBorder(28, 70, 20, 70));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        lblElimTitulo = new JLabel("Eliminar Cliente", SwingConstants.CENTER);
        lblElimTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblElimTitulo.setForeground(C_ROJO);
        lblElimTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblElimHint = new JLabel(
            "<html><center>Ingrese el numero de turno del cliente.<br>" +
            "Puede eliminarlo de la fila o del historial.</center></html>",
            SwingConstants.CENTER);
        lblElimHint.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblElimHint.setForeground(Color.GRAY);
        lblElimHint.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblElimTurno = crearLabel("Numero de turno:");
        txtElimTurno = crearTextField("", 260);

        // Indicador de fila actual
        JLabel lblFilaActual = crearLabel("Fila actual:");
        lblFilaActual.setFont(new Font("SansSerif", Font.BOLD, 11));

        JPanel bots = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        bots.setOpaque(false);
        btnElimFila      = crearBoton("Eliminar de la fila",      C_ROJO);
        btnElimHistorial = crearBoton("Eliminar del historial",    C_NARANJA);
        btnElimVolver    = crearBoton("Volver",                    C_GRIS);
        btnElimFila.setPreferredSize(new Dimension(180, 38));
        btnElimHistorial.setPreferredSize(new Dimension(190, 38));
        btnElimVolver.setPreferredSize(new Dimension(100, 38));
        bots.add(btnElimFila);
        bots.add(btnElimHistorial);
        bots.add(btnElimVolver);

        form.add(lblElimTitulo);
        form.add(Box.createVerticalStrut(6));
        form.add(lblElimHint);
        form.add(Box.createVerticalStrut(20));
        form.add(lblElimTurno);
        form.add(Box.createVerticalStrut(4));
        form.add(txtElimTurno);
        form.add(Box.createVerticalStrut(24));
        form.add(bots);

        pnlEliminar.add(form, BorderLayout.CENTER);
    }

    // ── Panel OPERACIONES ─────────────────────────────────────────
    private void initPanelOperaciones() {
        pnlOperaciones = new JPanel(new BorderLayout());
        pnlOperaciones.setBackground(C_FONDO);

        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(C_PRIMARIO);
        hdr.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        lblOpCliente = new JLabel("Ventanilla", SwingConstants.CENTER);
        lblOpCliente.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblOpCliente.setForeground(Color.WHITE);
        hdr.add(lblOpCliente, BorderLayout.CENTER);

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setOpaque(false);
        centro.setBorder(BorderFactory.createEmptyBorder(12, 30, 10, 30));

        lblOpMonto = crearLabel("Monto ($):");
        txtOpMonto = crearTextField("0.00", 200);

        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        fila1.setOpaque(false);
        btnOpConsultar    = crearBoton("Consultar saldo",     C_PRIMARIO);
        btnOpCerrar       = crearBoton("Finalizar atencion",  C_GRIS);
        btnOpConsultar.setPreferredSize(new Dimension(168, 38));
        btnOpCerrar.setPreferredSize(new Dimension(168, 38));
        fila1.add(btnOpConsultar);
        fila1.add(btnOpCerrar);

        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        fila2.setOpaque(false);
        btnOpDepositar     = crearBoton("Depositar",               C_VERDE);
        btnOpRetirar       = crearBoton("Retirar",                 C_ROJO);
        btnOpElimHistorial = crearBoton("Eliminar del historial",  C_NARANJA);
        btnOpDepositar.setPreferredSize(new Dimension(130, 38));
        btnOpRetirar.setPreferredSize(new Dimension(110, 38));
        btnOpElimHistorial.setPreferredSize(new Dimension(190, 38));
        fila2.add(btnOpDepositar);
        fila2.add(btnOpRetirar);
        fila2.add(btnOpElimHistorial);

        txtOpLog = new JTextArea(6, 34);
        txtOpLog.setEditable(false);
        txtOpLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtOpLog.setBackground(new Color(240, 245, 255));
        txtOpLog.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        scrollLog = new JScrollPane(txtOpLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 230)),
            " Historial de operaciones ",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("SansSerif", Font.PLAIN, 11), Color.GRAY));

        centro.add(lblOpMonto);
        centro.add(Box.createVerticalStrut(4));
        centro.add(txtOpMonto);
        centro.add(Box.createVerticalStrut(6));
        centro.add(fila1);
        centro.add(fila2);
        centro.add(Box.createVerticalStrut(8));
        centro.add(scrollLog);

        pnlOperaciones.add(hdr,    BorderLayout.NORTH);
        pnlOperaciones.add(centro, BorderLayout.CENTER);
    }

    // ── Panel INFO ────────────────────────────────────────────────
    private void initPanelInfo() {
        pnlInfo = new JPanel(new BorderLayout(0, 8));
        pnlInfo.setBackground(C_FONDO);
        pnlInfo.setBorder(BorderFactory.createEmptyBorder(16, 28, 12, 28));

        lblInfoTitulo = new JLabel("", SwingConstants.CENTER);
        lblInfoTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblInfoTitulo.setForeground(C_PRIMARIO);

        txtInfo = new JTextArea(14, 36);
        txtInfo.setEditable(false);
        txtInfo.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtInfo.setBackground(new Color(240, 245, 255));
        txtInfo.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        scrollInfo = new JScrollPane(txtInfo);
        scrollInfo.setBorder(BorderFactory.createLineBorder(new Color(180, 200, 230)));

        btnInfoVolver = crearBoton("Volver al menu", C_PRIMARIO);
        btnInfoVolver.setPreferredSize(new Dimension(180, 38));
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pie.setOpaque(false);
        pie.add(btnInfoVolver);

        pnlInfo.add(lblInfoTitulo, BorderLayout.NORTH);
        pnlInfo.add(scrollInfo,    BorderLayout.CENTER);
        pnlInfo.add(pie,           BorderLayout.SOUTH);
    }

    // =============================================================
    //  initListeners()
    // =============================================================
    private void initListeners() {
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { confirmarSalida(); }
        });

        // Menu
        btnMenuCrear      .addActionListener(e -> cardLayout.show(panelContenido, "CREAR"));
        btnMenuEliminar   .addActionListener(e -> abrirEliminar());
        btnMenuAtender    .addActionListener(e -> accionAtender());
        btnMenuFila       .addActionListener(e -> mostrarInfo("Fila de espera",           banco.resumenFila()));
        btnMenuHistorial  .addActionListener(e -> mostrarInfo("Historial de atendidos",   banco.resumenHistorial()));
        btnMenuRegistrados.addActionListener(e -> mostrarInfo("Clientes registrados",     banco.resumenRegistrados()));
        btnMenuSalir      .addActionListener(e -> confirmarSalida());

        // Crear
        btnCrearConfirmar.addActionListener(e -> accionCrearCliente());
        btnCrearVolver   .addActionListener(e -> {
            txtCrearCuenta.setText(""); txtCrearSaldo.setText("0.00");
            cardLayout.show(panelContenido, "MENU");
        });

        // Eliminar
        btnElimFila     .addActionListener(e -> accionEliminarDeFila());
        btnElimHistorial.addActionListener(e -> accionEliminarDeHistorial());
        btnElimVolver   .addActionListener(e -> {
            txtElimTurno.setText("");
            cardLayout.show(panelContenido, "MENU");
        });

        // Operaciones
        btnOpConsultar    .addActionListener(e -> accionConsultar());
        btnOpDepositar    .addActionListener(e -> accionDepositar());
        btnOpRetirar      .addActionListener(e -> accionRetirar());
        btnOpElimHistorial.addActionListener(e -> accionEliminarClienteActualDeHistorial());
        btnOpCerrar       .addActionListener(e -> accionFinalizarAtencion());

        // Info
        btnInfoVolver.addActionListener(e -> cardLayout.show(panelContenido, "MENU"));
    }

    // =============================================================
    //  ACCIONES
    // =============================================================

    // ── CREAR cliente ─────────────────────────────────────────────
    // ColaEnlazada.encolar()  +  ListaEnlazada.add()
    private void accionCrearCliente() {
        String cuentaStr = txtCrearCuenta.getText().trim();
        String saldoStr  = txtCrearSaldo.getText().trim();

        if (cuentaStr.isEmpty()) { aviso("Ingrese el numero de cuenta."); return; }

        double saldo;
        try {
            saldo = Double.parseDouble(saldoStr);
            if (saldo < 0) { aviso("El saldo no puede ser negativo."); return; }
        } catch (NumberFormatException ex) {
            aviso("Saldo invalido. Use solo numeros."); return;
        }

        // Internamente llama ColaEnlazada.encolar() + ListaEnlazada.add()
        Cliente c = banco.registrarCliente(cuentaStr, saldo);

        txtCrearCuenta.setText("");
        txtCrearSaldo.setText("0.00");

        JOptionPane.showMessageDialog(this,
            "Cliente creado y agregado a la fila.\n\n" +
            "Turno  : " + c.getTurno()  + "\n" +
            "Cuenta : " + c.getNombre() + "\n" +
            "Saldo  : $" + fmt(c.getSaldo()),
            "Cliente creado", JOptionPane.INFORMATION_MESSAGE);

        cardLayout.show(panelContenido, "MENU");
    }

    // ── Abrir panel eliminar mostrando la fila actual ─────────────
    private void abrirEliminar() {
        txtElimTurno.setText("");
        cardLayout.show(panelContenido, "ELIMINAR");
    }

    // ── ELIMINAR de la fila ───────────────────────────────────────
    // Reconstrucción de ColaEnlazada  +  ListaEnlazada.remove(i)
    private void accionEliminarDeFila() {
        int turno = leerTurno(); if (turno < 0) return;

        int conf = JOptionPane.showConfirmDialog(this,
            "Eliminar turno " + turno + " de la fila de espera?",
            "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;

        // banco.eliminarClienteDeFila() reconstruye la ColaEnlazada
        // saltando el elemento y usa ListaEnlazada.remove(i) en el registro
        boolean ok = banco.eliminarClienteDeFila(turno);

        if (ok) {
            JOptionPane.showMessageDialog(this,
                "Turno " + turno + " eliminado de la fila correctamente.",
                "Eliminado", JOptionPane.INFORMATION_MESSAGE);
            txtElimTurno.setText("");
        } else {
            aviso("No se encontro el turno " + turno + " en la fila.");
        }
    }

    // ── ELIMINAR del historial ────────────────────────────────────
    // ListaEnlazada.remove(i)
    private void accionEliminarDeHistorial() {
        int turno = leerTurno(); if (turno < 0) return;

        int conf = JOptionPane.showConfirmDialog(this,
            "Eliminar turno " + turno + " del historial de atendidos?",
            "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;

        // Internamente usa ListaEnlazada.remove(i)
        boolean ok = banco.eliminarClienteDeHistorial(turno);

        if (ok) {
            JOptionPane.showMessageDialog(this,
                "Turno " + turno + " eliminado del historial.",
                "Eliminado", JOptionPane.INFORMATION_MESSAGE);
            txtElimTurno.setText("");
        } else {
            aviso("No se encontro el turno " + turno + " en el historial.");
        }
    }

    // ── ATENDER siguiente ─────────────────────────────────────────
    private void accionAtender() {
        clienteActual = banco.atenderSiguiente();
        if (clienteActual == null) { aviso("No hay clientes en la fila."); return; }

        lblOpCliente.setText("Turno " + clienteActual.getTurno() +
                             "  |  " + clienteActual.getNombre());
        txtOpLog.setText("");
        log("Inicio de atencion — Turno " + clienteActual.getTurno());
        log("Cuenta : " + clienteActual.getNombre());
        log("Saldo  : $" + fmt(clienteActual.getSaldo()));
        cardLayout.show(panelContenido, "OPERACIONES");
    }

    private void accionConsultar() {
        log(">> Saldo actual : $" + fmt(clienteActual.getSaldo()));
    }

    private void accionDepositar() {
        double m = leerMonto(); if (m < 0) return;
        clienteActual.depositar(m);
        log("+  Deposito  : +$" + fmt(m) + "   => $" + fmt(clienteActual.getSaldo()));
        txtOpMonto.setText("0.00");
    }

    private void accionRetirar() {
        double m = leerMonto(); if (m < 0) return;
        if (!clienteActual.retirar(m)) {
            aviso("Fondos insuficientes. Saldo: $" + fmt(clienteActual.getSaldo())); return;
        }
        log("-  Retiro    : -$" + fmt(m) + "   => $" + fmt(clienteActual.getSaldo()));
        txtOpMonto.setText("0.00");
    }

    // ── ELIMINAR cliente actual del historial desde ventanilla ────
    // ListaEnlazada.remove(i)
    private void accionEliminarClienteActualDeHistorial() {
        int conf = JOptionPane.showConfirmDialog(this,
            "Eliminar a " + clienteActual.getNombre() + " (Turno " +
            clienteActual.getTurno() + ") del historial?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;

        banco.eliminarClienteDeHistorial(clienteActual.getTurno());
        log(">> Cliente eliminado del historial.");
        log("Fin de atencion.");
        clienteActual = null;
        cardLayout.show(panelContenido, "MENU");
    }

    private void accionFinalizarAtencion() {
        log("Fin de atencion — Gracias, " + clienteActual.getNombre() + ".");
        clienteActual = null;
        cardLayout.show(panelContenido, "MENU");
    }

    // =============================================================
    //  AUXILIARES
    // =============================================================
    private void mostrarInfo(String titulo, String contenido) {
        lblInfoTitulo.setText(titulo);
        txtInfo.setText(contenido);
        txtInfo.setCaretPosition(0);
        cardLayout.show(panelContenido, "INFO");
    }

    private double leerMonto() {
        try {
            double m = Double.parseDouble(txtOpMonto.getText().trim());
            if (m <= 0) { aviso("El monto debe ser mayor a cero."); return -1; }
            return m;
        } catch (NumberFormatException ex) { aviso("Monto invalido."); return -1; }
    }

    private int leerTurno() {
        try {
            int t = Integer.parseInt(txtElimTurno.getText().trim());
            if (t <= 0) { aviso("Ingrese un numero de turno valido."); return -1; }
            return t;
        } catch (NumberFormatException ex) { aviso("Turno invalido."); return -1; }
    }

    private void log(String msg) {
        txtOpLog.append(msg + "\n");
        txtOpLog.setCaretPosition(txtOpLog.getDocument().getLength());
    }

    private void aviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private void confirmarSalida() {
        int r = JOptionPane.showConfirmDialog(this,
            "Desea cerrar el sistema?", "Confirmar salida",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (r == JOptionPane.YES_OPTION) System.exit(0);
    }

    private static String fmt(double v) { return String.format("%.2f", v); }

    // ── Fábricas ──────────────────────────────────────────────────
    private static JButton crearBoton(String txt, Color fondo) {
        JButton b = new JButton(txt);
        b.setBackground(fondo); b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private static JTextField crearTextField(String val, int w) {
        JTextField tf = new JTextField(val);
        tf.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tf.setHorizontalAlignment(JTextField.CENTER);
        tf.setMaximumSize(new Dimension(w, 34));
        tf.setAlignmentX(Component.CENTER_ALIGNMENT);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 230), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return tf;
    }

    private static JLabel crearLabel(String txt) {
        JLabel l = new JLabel(txt, SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        l.setForeground(Color.DARK_GRAY);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    // =============================================================
    //  MAIN
    // =============================================================
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new BancoSimulacion().setVisible(true));
    }
}
