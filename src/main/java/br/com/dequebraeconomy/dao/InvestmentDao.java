package br.com.dequebraeconomy.dao;

import br.com.dequebraeconomy.factory.ConnectionFactory;
import br.com.dequebraeconomy.model.Investment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InvestmentDao {

        public void updateInvestment(Investment i) throws SQLException {

            String sql = """
            UPDATE T_INVESTMENT
            SET PAYOUT_DATE = ?,
                INVESTMENT_NAME = ?,
                ISSUING_BANK = ?,
                TAXABLE = ?,
                INTEREST_RATE = ?
            WHERE ID_TRANSACTION = ?
        """;

            try (Connection connection = ConnectionFactory.getConnection();
                 PreparedStatement stm = connection.prepareStatement(sql)) {

                stm.setDate(1, java.sql.Date.valueOf(i.getPayoutDate()));
                stm.setString(2, i.getInvestmentName());
                stm.setString(3, i.getIssuingBank());
                stm.setBoolean(4, i.isTaxable());
                stm.setBigDecimal(5, i.getInterestRate());
                stm.setLong(6, i.getId());

                int rows = stm.executeUpdate();

                if (rows == 0) {
                    throw new SQLException("Income not found");
                }
            }
        }
    }

