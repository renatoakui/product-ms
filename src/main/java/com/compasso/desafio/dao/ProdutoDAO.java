package com.compasso.desafio.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.compasso.desafio.model.Produto;

@Service
public class ProdutoDAO {

	public List<Produto> searchProduto(String q, BigDecimal min_price, BigDecimal max_price) {

		Connection con = null;
		ArrayList<Produto> prod = new ArrayList<Produto>();

		try {
			con = conectar();
			String sql = montarQuery(q, min_price, max_price);
			PreparedStatement p = con.prepareStatement(sql);

			ResultSet rs = p.executeQuery();
			
			while (rs.next()) {
				Produto produto = new Produto();
				produto.setId(rs.getString("id"));
				produto.setName(rs.getString("name"));
				produto.setDescription(rs.getString("description"));
				produto.setPrice(rs.getBigDecimal("price"));
				prod.add(produto);
			}

			p.close();
			rs.close();
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return prod;

	}

	public String montarQuery(String q, BigDecimal min_price, BigDecimal max_price) {

		Boolean name = Boolean.FALSE;
		StringBuilder sql = new StringBuilder();

		sql.append("Select * from compasso.produto where ");
		if (q != null) {
			sql.append("(name LIKE '%" + q + "%' or description LIKE '%" + q + "%') ");
			name = true;
		}
		if (min_price != null || max_price != null) {
			if (name) {
				sql.append("and ");
			}
			if (min_price != null) {
				sql.append("price >= " + min_price);
				if (max_price != null) {
					sql.append(" and ");
				}
			}
			if (max_price != null) {
				sql.append("price <= " + max_price);
			}
		}
		return sql.toString();

	}

	private static final String USUARIO = "root";
	private static final String SENHA = "admin";
	private static final String URL = "jdbc:mysql://localhost:3306/compasso?useTimezone=true&serverTimezone=UTC";

	public static Connection conectar() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
		return conexao;
	}

}
