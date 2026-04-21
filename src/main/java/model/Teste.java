package model;

import dao.ExpenseDAO;
import dao.GoalDAO;
import dao.IncomeDAO;
import dao.InvestmentDAO;
import factory.ConnectionFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Teste {

    public static void main(String[] args) {


        try (Connection connection = ConnectionFactory.getConnection()) {

            System.out.println("Conexão estabelecida com sucesso!\n");


            IncomeDAO incomeDAO = new IncomeDAO(connection);

            System.out.println("=== INSERINDO INCOMES ===");
            incomeDAO.insert(new Income(0, LocalDate.of(2024, 1, 5),  new BigDecimal("3500.00"), "Salário Janeiro",   "Empresa ABC", Category.INCOME));
            incomeDAO.insert(new Income(0, LocalDate.of(2024, 2, 5),  new BigDecimal("3500.00"), "Salário Fevereiro", "Empresa ABC", Category.INCOME));
            incomeDAO.insert(new Income(0, LocalDate.of(2024, 3, 10), new BigDecimal("500.00"),  "Freela Design",     "Cliente X",   Category.INCOME));
            incomeDAO.insert(new Income(0, LocalDate.of(2024, 3, 15), new BigDecimal("200.00"),  "Venda de curso",    "Hotmart",     Category.INCOME));
            incomeDAO.insert(new Income(0, LocalDate.of(2024, 4, 1),  new BigDecimal("4000.00"), "Salário Março",     "Empresa ABC", Category.INCOME));

            System.out.println("\n=== BUSCANDO TODOS OS INCOMES ===");
            List<Income> incomes = incomeDAO.getAll();
            for (Income i : incomes) {
                System.out.println(i.showTransaction());
            }

            ExpenseDAO expenseDAO = new ExpenseDAO(connection);

            System.out.println("=== INSERINDO EXPENSES ===");
            expenseDAO.insert(new Expense(0, LocalDate.of(2024, 1, 10), new BigDecimal("1500.00"), "Aluguel",      "Transferência", true,  true,  Category.EXPENSE));
            expenseDAO.insert(new Expense(0, LocalDate.of(2024, 1, 15), new BigDecimal("350.00"),  "Mercado",      "Débito",        true,  false, Category.EXPENSE));
            expenseDAO.insert(new Expense(0, LocalDate.of(2024, 2, 5),  new BigDecimal("120.00"),  "Internet",     "Boleto",        true,  true,  Category.EXPENSE));
            expenseDAO.insert(new Expense(0, LocalDate.of(2024, 2, 20), new BigDecimal("80.00"),   "Farmácia",     "Crédito",       true,  false, Category.EXPENSE));
            expenseDAO.insert(new Expense(0, LocalDate.of(2024, 3, 1),  new BigDecimal("200.00"),  "Conta de luz", "Débito",        false, true,  Category.EXPENSE));

            System.out.println("\n=== BUSCANDO TODOS OS EXPENSES ===");
            List<Expense> expenses = expenseDAO.getAll();
            for (Expense e : expenses) {
                System.out.println(e.showTransaction());
            }

            InvestmentDAO investmentDAO = new InvestmentDAO(connection);

            System.out.println("=== INSERINDO INVESTMENTS ===");
            investmentDAO.insert(new Investment(0, LocalDate.of(2024, 1, 1),  new BigDecimal("1000.00"), "Tesouro Selic",  LocalDate.of(2025, 1, 1),  "Tesouro Selic 2025", "Banco do Brasil", false, new BigDecimal("12.5"), Category.INVESTMENT));
            investmentDAO.insert(new Investment(0, LocalDate.of(2024, 1, 15), new BigDecimal("2000.00"), "CDB Bradesco",   LocalDate.of(2024, 7, 15), "CDB 110% CDI",       "Bradesco",        true,  new BigDecimal("13.0"), Category.INVESTMENT));
            investmentDAO.insert(new Investment(0, LocalDate.of(2024, 2, 1),  new BigDecimal("5000.00"), "LCI Itaú",       LocalDate.of(2025, 2, 1),  "LCI 95% CDI",        "Itaú",            false, new BigDecimal("11.8"), Category.INVESTMENT));
            investmentDAO.insert(new Investment(0, LocalDate.of(2024, 2, 20), new BigDecimal("500.00"),  "Tesouro IPCA",   LocalDate.of(2026, 2, 20), "Tesouro IPCA+ 2026", "XP",              true,  new BigDecimal("6.2"),  Category.INVESTMENT));
            investmentDAO.insert(new Investment(0, LocalDate.of(2024, 3, 5),  new BigDecimal("3000.00"), "RDB Nubank",     LocalDate.of(2024, 9, 5),  "RDB 100% CDI",       "Nubank",          true,  new BigDecimal("12.0"), Category.INVESTMENT));

            System.out.println("\n=== BUSCANDO TODOS OS INVESTMENTS ===");
            List<Investment> investments = investmentDAO.getAll();
            for (Investment inv : investments) {
                System.out.println(inv.showTransaction());
            }
            GoalDAO goalDAO = new GoalDAO(connection);

            System.out.println("=== INSERINDO GOALS ===");
            goalDAO.insert(new Goal(0, "Viagem para Europa",    LocalDate.now(), new BigDecimal("15000.00"), LocalDate.of(2025, 6, 1),   Category.GOAL));
            goalDAO.insert(new Goal(0, "Reserva de emergência", LocalDate.now(), new BigDecimal("10000.00"), LocalDate.of(2024, 12, 31), Category.GOAL));
            goalDAO.insert(new Goal(0, "Notebook novo",         LocalDate.now(), new BigDecimal("4500.00"),  LocalDate.of(2024, 8, 1),   Category.GOAL));
            goalDAO.insert(new Goal(0, "Curso de inglês",       LocalDate.now(), new BigDecimal("2400.00"),  LocalDate.of(2024, 10, 1),  Category.GOAL));
            goalDAO.insert(new Goal(0, "Entrada do apartamento",LocalDate.now(), new BigDecimal("50000.00"), LocalDate.of(2027, 1, 1),   Category.GOAL));

            System.out.println("\n=== BUSCANDO TODOS OS GOALS ===");
            List<Goal> goals = goalDAO.getAll();
            for (Goal g : goals) {
                System.out.println(g.showTransaction());
            }

        } catch (SQLException e) {
            System.out.println("Erro de conexão: " + e.getMessage());
        }
    }
}