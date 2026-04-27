package br.com.dequebraeconomy.dao;
import br.com.dequebraeconomy.factory.ConnectionFactory;
import br.com.dequebraeconomy.model.Expense;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ExpenseDao {

    public void updateExpense(Expense e) throws SQLException {

        String sql = """
            UPDATE T_EXPENSE
            SET PAYMENT_METHOD = ?,
                PAYMENT_STATUS = ?,
                RECURRING_PAYMENT = ?
            WHERE ID_TRANSACTION = ?
        """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {

            stm.setString(1, e.getPaymentMethod());

            // Oracle geralmente usa 'Y/N' ou 1/0, então cuidado:
            stm.setString(2, e.isPaymentStatus() ? "Y" : "N");
            stm.setString(3, e.isRecurringPayment() ? "Y" : "N");

            stm.setLong(4, e.getId());

            stm.executeUpdate();
        }
    }
}
