package dao;

import model.Category;
import model.Expense;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    // A conexão com o banco vem de fora — quem abre é o ConnectionFactory
    private Connection connection;

    public ExpenseDAO(Connection connection) {
        this.connection = connection;
    }

    // ================================================================
    // INSERT
    // ================================================================
    public void insert(Expense expense) {

        // Primeiro INSERT: campos comuns vão para a tabela pai
        String sqlTransacao = "INSERT INTO T_TRANSACAO (id, dt_transacao, vl_amount, ds_descricao) " +
                "VALUES (SQ_TRANSACAO.NEXTVAL, ?, ?, ?)";

        // Segundo INSERT: campos específicos de Expense vão para a tabela filha
        String sqlGasto = "INSERT INTO T_GASTO (id, ds_payment_method, fl_payment_status, fl_recurring) " +
                "VALUES (SQ_TRANSACAO.CURRVAL, ?, ?, ?)";

        try {
            // --- Primeiro INSERT: T_TRANSACAO ---
            PreparedStatement stmtTransacao = connection.prepareStatement(sqlTransacao);

            stmtTransacao.setDate(1, Date.valueOf(expense.getDate()));
            stmtTransacao.setBigDecimal(2, expense.getAmount());
            stmtTransacao.setString(3, expense.getDescription());

            stmtTransacao.executeUpdate();
            stmtTransacao.close();

            // --- Segundo INSERT: T_GASTO ---
            PreparedStatement stmtGasto = connection.prepareStatement(sqlGasto);

            stmtGasto.setString(1, expense.getPaymentMethod());

            // Oracle não tem boolean — converte: true = 1, false = 0
            stmtGasto.setInt(2, expense.isPaymentStatus() ? 1 : 0);
            stmtGasto.setInt(3, expense.isRecurringPayment() ? 1 : 0);

            stmtGasto.executeUpdate();
            stmtGasto.close();

            System.out.println("Expense inserido com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao inserir Expense: " + e.getMessage());
        }
    }

    // ================================================================
    // GET ALL
    // ================================================================
    public List<Expense> getAll() {

        // JOIN entre as duas tabelas para montar o objeto completo
        String sql = "SELECT t.id, t.dt_transacao, t.vl_amount, t.ds_descricao, " +
                "g.ds_payment_method, g.fl_payment_status, g.fl_recurring " +
                "FROM T_TRANSACAO t " +
                "JOIN T_GASTO g ON t.id = g.id";

        List<Expense> expenses = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Construtor: (id, date, amount, description, paymentMethod, paymentStatus, recurringPayment, category)
                Expense expense = new Expense(
                        rs.getLong("id"),
                        rs.getDate("dt_transacao").toLocalDate(),
                        rs.getBigDecimal("vl_amount"),
                        rs.getString("ds_descricao"),
                        rs.getString("ds_payment_method"),
                        rs.getInt("fl_payment_status") == 1,   // 1 = true, 0 = false
                        rs.getInt("fl_recurring") == 1,        // 1 = true, 0 = false
                        Category.EXPENSE  // Expense sempre é categoria EXPENSE
                );

                expenses.add(expense);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Erro ao buscar Expenses: " + e.getMessage());
        }

        return expenses;
    }
}