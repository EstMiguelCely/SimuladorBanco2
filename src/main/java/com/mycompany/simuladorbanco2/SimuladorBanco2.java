package com.mycompany.simuladorbanco2;

import com.murcia.utils.*;


public class SimuladorBanco2 {

    public static void main(String[] args) {

        Banco banco = new Banco();

        Menu menuPrincipal;
        menuPrincipal = new Menu(
                new String[]{
                    "\nCrear nuevo cliente\n",
                    "\nEliminar cliente de la fila\n",
                    "\nAtender siguiente cliente\n",
                    "\nVer fila de espera\n",
                    "\nVer historial de atendidos\n",
                    "\nVer todos los clientes registrados\n",
                    "\nSalir\n"
                },
                '1',
                "─────────────────────────",
                "=== BANCO NACIONAL ==="
        );

        boolean salir = false;
        while (!salir) {
            char op = menuPrincipal.select("\nSeleccione una opcion: \n");
            switch (op) {
                case '1': crearCliente(banco);              break;
                case '2': eliminarDeFila(banco);            break;
                case '3': atenderCliente(banco);            break;
                case '4':
                    System.out.println("\nFila de espera:\n" + banco.resumenFila());
                    break;
                case '5':
                    System.out.println("\nHistorial:\n" + banco.resumenHistorial());
                    break;
                case '6':
                    System.out.println("\nClientes registrados:\n" + banco.resumenRegistrados());
                    break;
                case '7': salir = true; break;
            }
        }

        System.out.println("Sistema cerrado. Hasta pronto.");
    }

    // =============================================================
    //  CREAR cliente
    //  Internamente: ColaEnlazada.encolar() + ListaEnlazada.add()
    // =============================================================
    private static void crearCliente(Banco banco) {
        Input input = new Input();

        // nextInt(msg, low, high) valida rango automáticamente
        int cuenta   = input.nextInt("Numero de cuenta (1-99999): ", 1, 99999);
        float saldo  = input.nextFloat("Saldo inicial ($): ");

        Cliente c = banco.registrarCliente(String.valueOf(cuenta), saldo);

        System.out.println("\nCliente creado y encolado exitosamente.");
        System.out.println("  Turno   : " + c.getTurno());
        System.out.println("  Cuenta  : " + c.getNombre());
        System.out.println("  Saldo   : $" + String.format("%.2f", c.getSaldo()));
    }

    // =============================================================
    //  ELIMINAR cliente de la fila por número de turno
    //  Internamente: reconstrucción de ColaEnlazada
    //                + ListaEnlazada.remove(i)
    // =============================================================
    private static void eliminarDeFila(Banco banco) {
        if (banco.filaVacia()) {
            System.out.println("La fila esta vacia, no hay nada que eliminar.");
            return;
        }

        System.out.println("\nFila actual:\n" + banco.resumenFila());

        Input input = new Input();
        int turno = input.nextInt("Ingrese el numero de turno a eliminar: ", 1, 99999);

        // Confirmar con nextChar (s/n)
        char conf = input.nextChar("Confirmar eliminacion del turno " + turno + "? (s/n): ");
        if (conf != 's' && conf != 'S') {
            System.out.println("Operacion cancelada.");
            return;
        }

        boolean ok = banco.eliminarClienteDeFila(turno);

        if (ok) {
            System.out.println("Turno " + turno + " eliminado de la fila correctamente.");
            System.out.println("\nFila actualizada:\n" + banco.resumenFila());
        } else {
            System.out.println("No se encontro el turno " + turno + " en la fila.");
        }
    }

    // =============================================================
    //  ATENDER cliente (desencolar + operaciones)
    // =============================================================
    private static void atenderCliente(Banco banco) {
        Cliente cliente = banco.atenderSiguiente();

        if (cliente == null) {
            System.out.println("No hay clientes en la fila de espera.");
            return;
        }

        System.out.println("\nAtendiendo → Turno " + cliente.getTurno() +
                           "  |  Cuenta: " + cliente.getNombre());

        Input input = new Input();

        Menu menuOp = new Menu(
            new String[]{"Consultar saldo", "Depositar", "Retirar",
                         "Eliminar del historial", "Finalizar atencion"},
            '1',
            "─────────────────────────",
            "--- Operaciones ---"
        );

        boolean continuar = true;
        while (continuar) {
            char op = menuOp.select("Opcion: ");
            switch (op) {
                case '1':
                    System.out.println("Saldo actual: $" +
                        String.format("%.2f", cliente.getSaldo()));
                    break;
                case '2':
                    float dep = input.nextFloat("Monto a depositar: ");
                    cliente.depositar(dep);
                    System.out.println("Deposito OK. Saldo: $" +
                        String.format("%.2f", cliente.getSaldo()));
                    break;
                case '3':
                    float ret = input.nextFloat("Monto a retirar: ");
                    if (!cliente.retirar(ret))
                        System.out.println("Fondos insuficientes. Saldo: $" +
                            String.format("%.2f", cliente.getSaldo()));
                    else
                        System.out.println("Retiro OK. Saldo: $" +
                            String.format("%.2f", cliente.getSaldo()));
                    break;
                case '4':
                    // Eliminar del historial: ListaEnlazada.remove(i)
                    char conf = input.nextChar("Eliminar este cliente del historial? (s/n): ");
                    if (conf == 's' || conf == 'S') {
                        banco.eliminarClienteDeHistorial(cliente.getTurno());
                        System.out.println("Cliente eliminado del historial.");
                        continuar = false;
                    }
                    break;
                case '5':
                    continuar = false;
                    break;
            }
        }

        System.out.println("Gracias por su visita. Hasta pronto.");
    }
}
