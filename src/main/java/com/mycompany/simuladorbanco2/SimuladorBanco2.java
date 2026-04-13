package com.mycompany.simuladorbanco2;

import com.murcia.utils.*;


public class SimuladorBanco2 {


    private static final Input input = new Input();

    public static void main(String[] args) {

        Banco banco = new Banco();


        Menu menuPrincipal = new Menu(
                new String[]{
                    "1 Crear nuevo cliente",
                    "2 Eliminar cliente de la fila",
                    "3 Atender siguiente cliente",
                    "4 Ver fila de espera",
                    "5 Ver historial de atendidos",
                    "6 Ver todos los clientes registrados",
                    "7 Salir"
                },
                '1',
                "-------------------------",
                "=== BANCO NACIONAL ==="
        );

        boolean salir = false;
        while (!salir) {
            char op = menuPrincipal.select("Seleccione una opcion: ");
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
    
    // =============================================================
    private static void crearCliente(Banco banco) {
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
    // =============================================================
    private static void eliminarDeFila(Banco banco) {
        if (banco.filaVacia()) {
            System.out.println("La fila esta vacia, no hay nada que eliminar.");
            return;
        }

        System.out.println("\nFila actual:\n" + banco.resumenFila());

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
    //  ATENDER cliente desencolar 
    
    private static void atenderCliente(Banco banco) {
        Cliente cliente = banco.atenderSiguiente();

        if (cliente == null) {
            System.out.println("No hay clientes en la fila de espera.");
            return;
        }

        System.out.println("\nAtendiendo → Turno " + cliente.getTurno() +
                           "  |  Cuenta: " + cliente.getNombre());

        Menu menuOp = new Menu(
            new String[]{
                "1 Consultar saldo",
                "2 Depositar",
                "3 Retirar",
                "4 Eliminar del historial",
                "5 Finalizar atencion"
            },
            '1',
            "-------------------------",
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
                    // Eliminar del historial
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

