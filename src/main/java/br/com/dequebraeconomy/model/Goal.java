package br.com.dequebraeconomy.model;

// Importações

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public class Goal extends Transaction {

    // Atributos
    private String title;
    private BigDecimal stipulatedAmount;
    private LocalDate endDate;
    private BigDecimal currentValue;

    // Construtor Vazio
    public Goal() {
        super();
        this.currentValue = BigDecimal.ZERO;
    }

    // Construtor Cheio
    public Goal(long id,long userId, String description, LocalDate date, BigDecimal stipulatedAmount, LocalDate endDate, Category category) {
        super(id ,date, BigDecimal.ZERO , description, category, userId);
        this.stipulatedAmount = stipulatedAmount;
        this.endDate = endDate;
        this.currentValue = BigDecimal.ZERO;
    }

    // Cópia | Registro
    public Goal(Goal other) {
        super(other.getId(),other.getDate(), other.getAmount(), other.getDescription(), other.getCategory());
        this.stipulatedAmount = other.stipulatedAmount;
        this.endDate = other.endDate;
        this.currentValue = other.currentValue;
    }


    // Getters e Setters
    public String getTitle(){return title;}

    public void setTitle(String title){
        if (title == null || title.trim().isEmpty()){
            System.out.println("Adicione um válido título ao seu objetivo.");
        }
        else{ this.title = title; }
    }

    public BigDecimal getStipulatedAmount() {return stipulatedAmount;}

    public void setStipulatedAmount(BigDecimal stipulatedAmount) {
        if (stipulatedAmount == null) {
            System.out.println("Forneça um valor válido para seu Objetivo.");
            return;
        }

        if (stipulatedAmount.scale() <= 2 && stipulatedAmount.precision() <= 10 ){
            this.stipulatedAmount = stipulatedAmount;
        }
        else {
            System.out.println("Valor estipulado excede o limite permitido.");
        }

    }

    public LocalDate getEndDate() {return endDate;}

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getCurrentValue(){return currentValue;}

    public void setCurrentValue(BigDecimal currentValue){
        this.currentValue = currentValue;
    }


    @Override
    public String showTransaction(){
        return String.format("""
                %s: 
                Descrição: %s
                Meta: %s
                Valor: %s
                Data: %s
                Acumulado: %s
                Concluído: %b
                Data final: %s
                """,getCategory(),getDescription(),getStipulatedAmount(), getAmount().abs(), getDate(), getCurrentValue(), goalAchieved(),getEndDate());

    }

    public String showGoal() {
        return String.format("""
                Descrição: %s
                Meta: R$%s
                Concluído: %b
                Acumulado: R$%s
                Data Final: %s
                """, getDescription(), getStipulatedAmount(), goalAchieved(), getCurrentValue(), getEndDate());
    }

    public  Goal registerDeposit(BigDecimal amount, LocalDate date){
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            System.out.println("Forneça um valor válido");
            return  null;
        }
        this.currentValue = this.currentValue.add(amount);

        Goal transactionGoal = new Goal(this);

        transactionGoal.setAmount(amount);
        transactionGoal.setDate(date);
        transactionGoal.setCurrentValue(this.currentValue);

        return transactionGoal;
    }

    public boolean goalAchieved(){
        if (stipulatedAmount == null){
            return false;
        }
        return currentValue.compareTo(stipulatedAmount) >= 0;
    }

    public BigDecimal surplus(){
        return currentValue.subtract(stipulatedAmount);
    }

    public BigDecimal percentage(){
        if (stipulatedAmount == null || stipulatedAmount.compareTo(BigDecimal.ZERO) == 0){
            return BigDecimal.ZERO;
        }

        BigDecimal hundred = BigDecimal.valueOf(100);
        BigDecimal percentage = currentValue.multiply(hundred).divide(stipulatedAmount,2, RoundingMode.HALF_UP);
        return percentage;
    }

    public String getGoalStatus(){
        if (goalAchieved()) {
            BigDecimal surplus = surplus();

            if (surplus.compareTo(BigDecimal.ZERO) > 0) {
                return String.format("""
                        Meta atingida!
                        %s
                        Meta: R$%.2f | Progresso: 100%%
                        Você ultrapassou em R$%.2f
                        
                        """, getDescription(), stipulatedAmount, surplus);

            } else {
                return String.format("""
                        Meta atingida com sucesso!
                        %s
                        Meta: R$%.2f | Progresso: %.2f%%
                        
                        """, getDescription(), stipulatedAmount, percentage());
            }

        } else {
            return String.format("""
                    Valor adicionado com sucesso!
                    %s
                    Valor acumulado: R$%s | Progresso: %.2f%%
                    
                    """, getDescription(), currentValue.abs(), percentage());
        }
    }

    public static Goal findGoalByName(String name, List<Transaction> transactions) {

        for (Transaction t : transactions) {

            if (t instanceof Goal) {

                Goal g = (Goal) t;

                if (g.getDescription().equalsIgnoreCase(name)) {
                    return g;
                }
            }
        }

        return null;
    }

}
