package br.com.dequebraeconomy.dao;
import br.com.dequebraeconomy.factory.ConnectionFactory;
import br.com.dequebraeconomy.model.Income;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class IncomeDao {

    public void updateIncome(Income i) throws SQLException {

        String sql = """
            UPDATE T_INCOME
            SET sourceT = ?
            WHERE id_transaction = ?
        """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {

            stm.setString(1, i.getSource());
            stm.setLong(2, i.getId());

            int rows = stm.executeUpdate();

            if (rows == 0) {
                throw new SQLException("Income not found");
            }
        }
    }
}