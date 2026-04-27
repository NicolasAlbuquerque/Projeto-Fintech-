package br.com.dequebraeconomy.dao;

import br.com.dequebraeconomy.factory.ConnectionFactory;
import br.com.dequebraeconomy.model.Goal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GoalDao {

    public void updateGoal(Goal g) throws SQLException {

        String sql = """
            UPDATE T_GOAL
            SET STIPULATEDAMOUNT = ?,
                CURRENTVALUE = ?,
                ENDDATE = ?
            WHERE ID_TRANSACTION = ?
        """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {

            stm.setBigDecimal(1, g.getStipulatedAmount());
            stm.setBigDecimal(2, g.getCurrentValue());
            stm.setDate(3, java.sql.Date.valueOf(g.getEndDate()));
            stm.setLong(4, g.getId());

            stm.executeUpdate();
        }
    }
}