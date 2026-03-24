
package com.mycompany.simuladorbanco2;

import java.util.*;

public class Banco {
    private final Queue<Cliente> cola;
    private final List<Cliente> atendidos;
    private int contadorTurnos;

    public Banco() {
        cola = new LinkedList<>();
        atendidos = new ArrayList<>();
        contadorTurnos = 1;
    }

    public void pedirTurno(String nombre) {
        Cliente cliente = new Cliente(nombre, contadorTurnos++);
        cola.add(cliente);
        System.out.println("Turno asignado: " + cliente.getTurno());
    }

    public void atenderCliente(Scanner sc) {
        if (cola.isEmpty()) {
            System.out.println("No hay clientes en espera.");
            return;
        }

        Cliente cliente = cola.poll();
        System.out.println("\nAtendiendo a: " + cliente.getNombre() + " (Turno " + cliente.getTurno() + ")");

        boolean continuar = true;

        while (continuar) {
            System.out.println("\nSeleccione una opción:");
            System.out.println("1. Consultar saldo");
            System.out.println("2. Depositar");
            System.out.println("3. Retirar");
            System.out.println("4. Salir");

            int opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    System.out.println("Saldo: $" + cliente.getSaldo());
                    break;
                case 2:
                    System.out.print("Monto a depositar: ");
                    double dep = sc.nextDouble();
                    cliente.depositar(dep);
                    break;
                case 3:
                    System.out.print("Monto a retirar: ");
                    double ret = sc.nextDouble();
                    cliente.retirar(ret);
                    break;
                case 4:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción inválida");
            }

            if (continuar) {
                System.out.print("¿Desea otra operación? (s/n): ");
                char resp = sc.next().charAt(0);
                if (resp == 'n' || resp == 'N') {
                    continuar = false;
                }
            }
        }

        System.out.println("Gracias por su visita, " + cliente.getNombre());
        atendidos.add(cliente);
    }
}
