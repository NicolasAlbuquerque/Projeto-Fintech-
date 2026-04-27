package br.com.dequebraeconomy.dao;


import br.com.dequebraeconomy.factory.ConnectionFactory;
import br.com.dequebraeconomy.model.*;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {


    // PARSE PRINCIPAL

    private Transaction parseTransaction(ResultSet rs, long idUser) throws SQLException {

        long id = rs.getLong("id_transaction");
        int idCategory = rs.getInt("id_category");
        LocalDate date = rs.getDate("dt_transaction").toLocalDate();
        BigDecimal amount = rs.getBigDecimal("amount");
        String description = rs.getString("ds_transaction");

        Category category = Category.fromId(idCategory);

        return switch (category) {

            case INCOME -> parseIncome(rs, id, idUser, date, amount, description);

            case EXPENSE -> parseExpense(rs, id, idUser, date, amount, description);

            case INVESTMENT -> parseInvestment(rs, id, idUser, date, amount, description);

            case GOAL -> parseGoal(rs, id, idUser, date, amount, description);
        };
    }


    // INCOME

    private Income parseIncome(ResultSet rs, long id, long idUser,
                               LocalDate date, BigDecimal amount, String desc) throws SQLException {

        String sql = "SELECT * FROM T_INCOME WHERE ID_TRANSACTION = ?";

        try (PreparedStatement stm = rs.getStatement().getConnection().prepareStatement(sql)) {

            stm.setLong(1, id);

            ResultSet rsi = stm.executeQuery();

            if (!rsi.next()) {
                throw new SQLException("Income not found");
            }

            String source = rsi.getString("sourcet");

            return new Income(id, idUser, date, amount, desc, source, Category.INCOME);
        }
    }

    // EXPENSE
    private Expense parseExpense(ResultSet rs, long id, long idUser,
                                 LocalDate date, BigDecimal amount, String desc) throws SQLException {

        String sql = "SELECT * FROM T_EXPENSE WHERE ID_TRANSACTION = ?";

        try (PreparedStatement stm = rs.getStatement().getConnection().prepareStatement(sql)) {

            stm.setLong(1, id);

            ResultSet rse = stm.executeQuery();

            if (!rse.next()) {
                throw new SQLException("Expense not found");
            }

            return new Expense(
                    id,
                    idUser,
                    date,
                    amount,
                    desc,
                    rse.getString("payment_method"),
                    rse.getBoolean("payment_status"),
                    rse.getBoolean("recurring_payment"),
                    Category.EXPENSE
            );
        }
    }

    // INVESTMENT
    private Investment parseInvestment(ResultSet rs, long id, long idUser,
                                       LocalDate date, BigDecimal amount, String desc) throws SQLException {

        String sql = "SELECT * FROM T_INVESTMENT WHERE ID_TRANSACTION = ?";

        try (PreparedStatement stm = rs.getStatement().getConnection().prepareStatement(sql)) {

            stm.setLong(1, id);

            ResultSet rsi = stm.executeQuery();

            if (!rsi.next()) {
                throw new SQLException("Investment not found");
            }

            return new Investment(
                    id,
                    idUser,
                    date,
                    amount,
                    desc,
                    rsi.getDate("payout_date") != null
                            ? rsi.getDate("payout_date").toLocalDate()
                            : null,
                    rsi.getString("investment_name"),
                    rsi.getString("issuing_bank"),
                    rsi.getBoolean("taxable"),
                    rsi.getBigDecimal("interest_rate"),
                    Category.INVESTMENT
            );
        }
    }

    // GOAL (CORRIGIDO)

    private Goal parseGoal(ResultSet rs, long id, long idUser,
                           LocalDate date, BigDecimal amount, String desc) throws SQLException {

        String sql = "SELECT * FROM T_GOAL WHERE ID_TRANSACTION = ?";

        try (PreparedStatement stm = rs.getStatement().getConnection().prepareStatement(sql)) {

            stm.setLong(1, id);

            ResultSet rsg = stm.executeQuery();

            if (!rsg.next()) {
                throw new SQLException("Goal not found");
            }

            return new Goal(
                    id,
                    idUser,
                    desc,
                    date,
                    rsg.getBigDecimal("stipulatedamount"),
                    rsg.getDate("enddate").toLocalDate(),
                    Category.GOAL
            );
        }
    }


    // REGISTER

    public int registerTransaction(Transaction t, long idUser) throws SQLException {

        String sql = """
            INSERT INTO T_TRANSACTION
            (ID_USER, ID_CATEGORY, DT_TRANSACTION, AMOUNT, DS_TRANSACTION)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql, new String[]{"ID_TRANSACTION"})) {

            stm.setLong(1, idUser);
            stm.setInt(2, t.getCategory().getId());
            stm.setDate(3, Date.valueOf(t.getDate()));
            stm.setBigDecimal(4, t.getAmount());
            stm.setString(5, t.getDescription());

            stm.executeUpdate();

            ResultSet rs = stm.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

            throw new RuntimeException("Erro ao registrar transação");
        }
    }


    // GET BY ID

    public Transaction getById(Long id, long idUser) throws SQLException {

        String sql = """
            SELECT * 
            FROM T_TRANSACTION 
            WHERE ID_TRANSACTION = ? 
            AND ID_USER = ?
        """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {

            stm.setLong(1, id);
            stm.setLong(2, idUser);

            ResultSet rs = stm.executeQuery();

            if (!rs.next()) {
                throw new SQLException("Transaction not found");
            }

            return parseTransaction(rs, idUser);
        }
    }


    // GET ALL
    public List<Transaction> getAll(long idUser) throws SQLException {

        String sql = """
            SELECT *
            FROM T_TRANSACTION
            WHERE ID_USER = ?
            ORDER BY DT_TRANSACTION DESC
        """;

        List<Transaction> list = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {

            stm.setLong(1, idUser);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                list.add(parseTransaction(rs, idUser));
            }
        }

        return list;
    }




    public void updateTransaction(Transaction t, long idUser) throws SQLException {

        String sql = """
        UPDATE T_TRANSACTION
        SET ID_CATEGORY = ?,
            DT_TRANSACTION = ?,
            AMOUNT = ?,
            DS_TRANSACTION = ?
        WHERE ID_TRANSACTION = ?
        AND ID_USER = ?
    """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {

            stm.setInt(1, t.getCategory().getId());
            stm.setDate(2, Date.valueOf(t.getDate()));
            stm.setBigDecimal(3, t.getAmount());
            stm.setString(4, t.getDescription());
            stm.setLong(5, t.getId());
            stm.setLong(6, idUser);

            stm.executeUpdate();
        }


        if (t instanceof Income income) {
            new IncomeDao().updateIncome(income);
        }

        else if (t instanceof Expense expense) {
            new ExpenseDao().updateExpense(expense);
        }

        else if (t instanceof Investment investment) {
            new InvestmentDao().updateInvestment(investment);
        }

        else if (t instanceof Goal goal) {
            new GoalDao().updateGoal(goal);
        }
    }

}