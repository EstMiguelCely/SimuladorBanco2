package com.mycompany.simuladorbanco2;

import com.murcia.utils.*;


public class Banco {

    // ── Fila de espera FIFO ───────────────────────────────────────
    private final ColaEnlazada<Cliente>  filaEspera;

    // ── Registro maestro de todos los clientes del banco ─────────
    //    (independiente de si están en fila o ya fueron atendidos)
    private final ListaEnlazada<Cliente> clientesRegistrados;

    // ── Historial de clientes ya atendidos en ventanilla ─────────
    private final ListaEnlazada<Cliente> historialAtendidos;

    private int contadorTurnos;

    public Banco() {
        filaEspera           = new ColaEnlazada<>();
        clientesRegistrados  = new ListaEnlazada<>() {};
        historialAtendidos   = new ListaEnlazada<>() {};
        contadorTurnos       = 1;
    }

    // =============================================================
    //  CREAR cliente
    //  ListaEnlazada.add()  +  ColaEnlazada.encolar()
    // =============================================================
    public Cliente registrarCliente(String nombre, double saldoInicial) {
        Cliente c = new Cliente(nombre, contadorTurnos++, saldoInicial);
        clientesRegistrados.add(c);   // ListaEnlazada.add() → registro maestro
        filaEspera.encolar(c);        // ColaEnlazada.encolar() → fila de espera
        return c;
    }

    // =============================================================
    //  ELIMINAR cliente de la fila por número de turno
    //
    //  ColaEnlazada no tiene remove(i), así que reconstruimos:
    //    1. Desencolar todo hacia una cola temporal
    //    2. Si el turno coincide → lo saltamos (no lo reencolar)
    //    3. Reencolar el resto en orden original
    //  También se elimina del registro maestro con ListaEnlazada.remove(i)
    // =============================================================
    public boolean eliminarClienteDeFila(int turno) {
        ColaEnlazada<Cliente> temporal = new ColaEnlazada<>();
        boolean encontrado = false;
        Cliente eliminado  = null;

        // Paso 1 — vaciar la fila hacia temporal, saltando el turno buscado
        while (true) {
            Cliente c = filaEspera.desencolar();   // ColaEnlazada.desencolar()
            if (c == null) break;
            if (c.getTurno() == turno && !encontrado) {
                encontrado = true;
                eliminado  = c;
                // no reencolar → queda fuera
            } else {
                temporal.encolar(c);               // ColaEnlazada.encolar()
            }
        }

        // Paso 2 — restaurar la fila sin el elemento eliminado
        while (true) {
            Cliente c = temporal.desencolar();
            if (c == null) break;
            filaEspera.encolar(c);
        }

        // Paso 3 — eliminar también del registro maestro
        if (encontrado) {
            int n = clientesRegistrados.size();    // ListaEnlazada.size()
            for (int i = 0; i < n; i++) {
                if (clientesRegistrados.get(i).getTurno() == turno) {
                    clientesRegistrados.remove(i); // ListaEnlazada.remove(i)
                    break;
                }
            }
        }

        return encontrado;
    }

    // =============================================================
    //  ELIMINAR cliente del registro maestro por número de turno
    //  (cliente que ya fue atendido y queremos borrar del historial)
    //  Usa ListaEnlazada.remove(i)
    // =============================================================
    public boolean eliminarClienteDeHistorial(int turno) {
        int n = historialAtendidos.size();
        for (int i = 0; i < n; i++) {
            if (historialAtendidos.get(i).getTurno() == turno) {
                historialAtendidos.remove(i);      // ListaEnlazada.remove(i)
                // También del registro maestro
                int m = clientesRegistrados.size();
                for (int j = 0; j < m; j++) {
                    if (clientesRegistrados.get(j).getTurno() == turno) {
                        clientesRegistrados.remove(j);
                        break;
                    }
                }
                return true;
            }
        }
        return false;
    }

    // =============================================================
    //  ATENDER siguiente en la fila
    //  ColaEnlazada.desencolar()  +  ListaEnlazada.add()
    // =============================================================
    public Cliente atenderSiguiente() {
        Cliente c = filaEspera.desencolar();       // ColaEnlazada.desencolar()
        if (c != null) historialAtendidos.add(c);  // ListaEnlazada.add()
        return c;
    }

    // =============================================================
    //  BUSCAR cliente en registro maestro por turno
    //  ListaEnlazada.get(i)  +  ListaEnlazada.size()
    // =============================================================
    public Cliente buscarPorTurno(int turno) {
        int n = clientesRegistrados.size();
        for (int i = 0; i < n; i++) {
            Cliente c = clientesRegistrados.get(i);
            if (c.getTurno() == turno) return c;
        }
        return null;
    }

    // =============================================================
    //  COPIA AUXILIAR de la fila (sin modificarla)
    // =============================================================
    private ColaEnlazada<Cliente> copiarFila() {
        ColaEnlazada<Cliente> copia    = new ColaEnlazada<>();
        ColaEnlazada<Cliente> temporal = new ColaEnlazada<>();
        while (true) {
            Cliente c = filaEspera.desencolar();
            if (c == null) break;
            temporal.encolar(c);
        }
        while (true) {
            Cliente c = temporal.desencolar();
            if (c == null) break;
            filaEspera.encolar(c);
            copia.encolar(c);
        }
        return copia;
    }

    // =============================================================
    //  RESÚMENES para mostrar en UI / consola
    // =============================================================
    public boolean filaVacia() {
        ColaEnlazada<Cliente> copia = copiarFila();
        return copia.desencolar() == null;
    }

    public int turnoActual() { return contadorTurnos; }

    public ListaEnlazada<Cliente> getHistorial()           { return historialAtendidos; }
    public ListaEnlazada<Cliente> getClientesRegistrados() { return clientesRegistrados; }

    public String resumenFila() {
        ColaEnlazada<Cliente> copia = copiarFila();
        Cliente c = copia.desencolar();
        if (c == null) return "(fila vacia)";
        StringBuilder sb = new StringBuilder();
        int pos = 1;
        while (c != null) {
            sb.append(pos++).append(". ").append(c).append("\n");
            c = copia.desencolar();
        }
        return sb.toString();
    }

    public String resumenHistorial() {
        int n = historialAtendidos.size();
        if (n == 0) return "(ninguno atendido aun)";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++)
            sb.append(historialAtendidos.get(i)).append("\n");
        return sb.toString();
    }

    public String resumenRegistrados() {
        int n = clientesRegistrados.size();
        if (n == 0) return "(sin clientes registrados)";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++)
            sb.append(clientesRegistrados.get(i)).append("\n");
        return sb.toString();
    }
}
