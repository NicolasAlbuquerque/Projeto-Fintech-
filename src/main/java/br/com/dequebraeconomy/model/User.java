package br.com.dequebraeconomy.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {
    private long id;
    private String name;
    private String cpf;
    private String email;
    private String password;
    private LocalDate birthDate;
    //Criar a lista de Transactions aqui
    private List<Transaction> transactions;

    public User() {}

    public User(long id, String name, String cpf, String email, String password, LocalDate birthDate) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.id = id;
        this.transactions = new ArrayList<>();
    }

    public User(String name, String cpf, String email, String password, LocalDate birthDate) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.transactions = transactions;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public long getId() {
        return id;
    }

    /**
     * Método privado: não use fora da classe.
     * Use addTransaction() para adicionar itens à lista.
     * setTransactions, inicia uma nova lista e sobrescreve a lista antiga.
     */

    @Deprecated(forRemoval = true)
    private void setTransactions(List<Transaction> transaction) {
        this.transactions = transaction;
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public void showTransactions(){
        for(Transaction t : transactions){
            System.out.println(t.showTransaction());
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User ID: ").append(this.id).append("\n");
        sb.append("Name: ").append(this.name).append("\n");
        sb.append("CPF: ").append(this.cpf).append("\n");
        sb.append("Brith Date: ").append(this.birthDate).append("\n");



        return sb.toString();
    }



}
