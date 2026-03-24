
package com.mycompany.simuladorbanco2;

public class Cliente extends Persona {
    private final int turno;
    private double saldo;

    public Cliente(String nombre, int turno) {
        super(nombre);
        this.turno = turno;
        this.saldo = 1000; // saldo inicial
    }

    public int getTurno() {
        return turno;
    }

    public double getSaldo() {
        return saldo;
    }

    public void depositar(double monto) {
        saldo += monto;
    }

    public void retirar(double monto) {
        if (monto <= saldo) {
            saldo -= monto;
        } else {
            System.out.println("Fondos insuficientes.");
        }
    }
}

