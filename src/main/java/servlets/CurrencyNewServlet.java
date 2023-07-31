package servlets;

import DAO.CurrencyDAO;
import DAO.CurrencyDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Currency;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

@WebServlet("/currencies/new")
public class CurrencyNewServlet extends HttpServlet {

    private final CurrencyDAO currencyDAO = CurrencyDAOImpl.INSTANCE;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");
        if (name == null || code == null || sign == null) {
            resp.sendError(400, "You missed one or more arguments");
            return;
        }
        try {
            if (currencyDAO.getCurrencyByCode(code) != null) {
                resp.sendError(409, "Currency with such code is already exists");
                return;
            }
            Currency curr = new Currency(0, code, name, sign);
            currencyDAO.createCurrency(curr);
            resp.sendRedirect("/currency/" + code);
        } catch (SQLException e) {
            resp.sendError(500, "Unable to connect to database");
        }
    }
}
