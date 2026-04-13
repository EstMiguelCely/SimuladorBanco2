package com.mycompany.simuladorbanco2;

public class Cliente extends Persona {

    private final int turno;
    private double saldo;

    public Cliente(String nombre, int turno, double saldoInicial) {
        super(nombre);
        this.turno  = turno;
        this.saldo  = saldoInicial;
    }

    public int    getTurno()  { return turno; }
    public double getSaldo()  { return saldo; }

    public void depositar(double monto) {
        saldo += monto;
    }

    /** @return true si el retiro fue exitoso, false si fondos insuficientes */
    public boolean retirar(double monto) {
        if (monto > saldo) return false;
        saldo -= monto;
        return true;
    }

    @Override
    public String toString() {
        return "Turno " + turno + " — Cuenta " + nombre +
               "  ($" + String.format("%.2f", saldo) + ")";
    }
}
