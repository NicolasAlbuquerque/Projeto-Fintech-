package br.com.dequebraeconomy.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Income extends Transaction {

    private String source;

    public Income() {
        super();
    }

    public Income(long id, long userId, LocalDate date, BigDecimal amount, String description, String source, Category category) {
        super(id, date, amount, description, category, userId);
        this.source = source;

    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }



    @Override
    public String showTransaction() {

        return String.format("""
                %s:
                Descrição: %s
                Valor:  %.2f
                Data: %s
                Fonte: %s
                """,getCategory(),
                getDescription(),
                getAmount(),
                getDate(),
                getSource());
    }
}