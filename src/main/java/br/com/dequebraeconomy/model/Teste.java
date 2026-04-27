package br.com.dequebraeconomy.model;

import br.com.dequebraeconomy.factory.ConnectionFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class Teste {
   public  static void main(String[] args) {

       //conectando com o DB
       try {
           Connection conexao = ConnectionFactory.getConnection();
           System.out.println("Conexão realizada!");
       } catch (SQLException e) {
           System.err.println(e.getMessage());
       }





        LocalDate bday = LocalDate.of(1994,9,27);
        User usuario = new User(1l,"nico","445855","nicolasAlbuquerque@gmail.com", "827941",bday);
        LocalDate vence = LocalDate.of(2026,12,12);
        LocalDate hoje = LocalDate.of(2025,12,12);


//        //UTILIZAÇÃO DE POLIMORFISMO
//        Transaction investimento2 = new Investment(1l,hoje, new BigDecimal("1000"),"Cdb",vence,"tesouro","itau",true, new BigDecimal("20"), Category.INVESTMENT);
//
//        Transaction gasto2 = new Expense(2l,hoje,new BigDecimal("3211"),"Gastei", "Crédito", true,false, Category.EXPENSE);
//        Transaction goal2 = new Goal(3l,"Tete",hoje,new BigDecimal("200"),hoje, Category.GOAL);
//
//        //DEFININDO TIPOS PARA TESTE DE METODOS.
//        Investment investimento = new Investment(3l,hoje, new BigDecimal("1000"),"Cdb",vence,"tesouro","itau",true, new BigDecimal("20"), Category.INVESTMENT);
//
//        Expense gasto = new Expense(4l, hoje,new BigDecimal("3211"),"Gastei", "Crédito", true,false, Category.EXPENSE);
//        Goal goal = new Goal(4l,"Tete",hoje,new BigDecimal("200"),hoje, Category.GOAL);

//Accounts.addAccont(usuario);
//        usuario.addTransaction(investimento);
//        usuario.addTransaction(gasto);
//        usuario.addTransaction(goal);
//        usuario.addTransaction(investimento2);
//        usuario.addTransaction(gasto2);
//        usuario.addTransaction(goal2);
//
//        usuario.showTransactions();
//
//
//        System.out.println(gasto.showTransaction());


        System.out.println(Accounts.findById(10));
        System.out.println(Accounts.findById(1));
        System.out.println(Accounts.findByEmail("nicolasAlbuquerque@gmail.com"));



    }
}
