package br.com.dequebraeconomy.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Transaction {
    private long id;
    private LocalDate date;
    private BigDecimal amount;
    private String description;
    private Category category;
    private long userId;

    public Transaction() {

    }

    public Transaction(long id,LocalDate date, BigDecimal amount, String description, Category category, long userId) {
        this.id =id;
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.userId = userId;
        if(category == Category.EXPENSE){
            this.amount = amount.abs().negate();
        }else {
            this.amount =   amount.abs();
        }
    }

    public Transaction(long id, LocalDate date, BigDecimal amount, String description, Category category) {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }


    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null && description.length() <= 150) {
            this.description = description;
        } else {
            System.out.println("Descrição muito longa.");
        }
    }

    public Category getCategory() {
        return category;
    }

    public abstract String showTransaction();


    public long getId() {
        return id;
    }
}
