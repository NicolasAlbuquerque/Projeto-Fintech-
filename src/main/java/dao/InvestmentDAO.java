package dao;

import model.Category;
import model.Investment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvestmentDAO {

    // A conexão com o banco vem de fora — quem abre é o ConnectionFactory
    private Connection connection;

    public InvestmentDAO(Connection connection) {
        this.connection = connection;
    }

    // ================================================================
    // INSERT
    // ================================================================
    public void insert(Investment investment) {

        // Primeiro INSERT: campos comuns vão para a tabela pai
        String sqlTransacao = "INSERT INTO T_TRANSACAO (id, dt_transacao, vl_amount, ds_descricao) " +
                "VALUES (SQ_TRANSACAO.NEXTVAL, ?, ?, ?)";

        // Segundo INSERT: campos específicos de Investment vão para a tabela filha
        String sqlInvestimento = "INSERT INTO T_INVESTIMENTO (id, dt_payout, nm_investimento, nm_banco, fl_taxable, vl_taxa_juros) " +
                "VALUES (SQ_TRANSACAO.CURRVAL, ?, ?, ?, ?, ?)";

        try {
            // --- Primeiro INSERT: T_TRANSACAO ---
            PreparedStatement stmtTransacao = connection.prepareStatement(sqlTransacao);

            stmtTransacao.setDate(1, Date.valueOf(investment.getDate()));
            stmtTransacao.setBigDecimal(2, investment.getAmount());
            stmtTransacao.setString(3, investment.getDescription());

            stmtTransacao.executeUpdate();
            stmtTransacao.close();

            // --- Segundo INSERT: T_INVESTIMENTO ---
            PreparedStatement stmtInvestimento = connection.prepareStatement(sqlInvestimento);

            stmtInvestimento.setDate(1, Date.valueOf(investment.getPayoutDate()));
            stmtInvestimento.setString(2, investment.getInvestmentName());
            stmtInvestimento.setString(3, investment.getIssuingBank());

            // Oracle não tem boolean — converte: true = 1, false = 0
            stmtInvestimento.setInt(4, investment.isTaxable() ? 1 : 0);
            stmtInvestimento.setBigDecimal(5, investment.getInterestRate());

            stmtInvestimento.executeUpdate();
            stmtInvestimento.close();

            System.out.println("Investment inserido com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao inserir Investment: " + e.getMessage());
        }
    }

    // ================================================================
    // GET ALL
    // ================================================================
    public List<Investment> getAll() {

        // JOIN entre as duas tabelas para montar o objeto completo
        String sql = "SELECT t.id, t.dt_transacao, t.vl_amount, t.ds_descricao, " +
                "i.dt_payout, i.nm_investimento, i.nm_banco, i.fl_taxable, i.vl_taxa_juros " +
                "FROM T_TRANSACAO t " +
                "JOIN T_INVESTIMENTO i ON t.id = i.id";

        List<Investment> investments = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Construtor: (id, date, amount, description, payoutDate, investmentName, issuingBank, taxable, interestRate, category)
                Investment investment = new Investment(
                        rs.getLong("id"),
                        rs.getDate("dt_transacao").toLocalDate(),
                        rs.getBigDecimal("vl_amount"),
                        rs.getString("ds_descricao"),
                        rs.getDate("dt_payout").toLocalDate(),
                        rs.getString("nm_investimento"),
                        rs.getString("nm_banco"),
                        rs.getInt("fl_taxable") == 1,
                        rs.getBigDecimal("vl_taxa_juros"),
                        Category.INVESTMENT  // Investment sempre é categoria INVESTMENT
                );

                investments.add(investment);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Erro ao buscar Investments: " + e.getMessage());
        }

        return investments;
    }
}