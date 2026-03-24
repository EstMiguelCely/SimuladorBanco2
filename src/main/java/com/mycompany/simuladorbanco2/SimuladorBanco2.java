
package com.mycompany.simuladorbanco2;

import java.util.Scanner;

public class SimuladorBanco2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Banco banco = new Banco();

        boolean salir = false;

        while (!salir) {
            System.out.println("\n=== BANCO ===");
            System.out.println("1. Pedir turno");
            System.out.println("2. Atender cliente");
            System.out.println("3. Salir");

            int opcion = sc.nextInt();
            sc.nextLine(); 

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese su nombre: ");
                    String nombre = sc.nextLine();
                    banco.pedirTurno(nombre);
                    break;
                case 2:
                    banco.atenderCliente(sc);
                    break;
                case 3:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

        sc.close();
        System.out.println("Sistema cerrado.");
    }
}
