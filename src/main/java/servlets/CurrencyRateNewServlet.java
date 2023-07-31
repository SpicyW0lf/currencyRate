package servlets;

import DAO.CurrencyDAO;
import DAO.CurrencyDAOImpl;
import DAO.CurrencyRateDAO;
import DAO.CurrencyRateDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.CurrencyRate;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/exchangeRates/new")
public class CurrencyRateNewServlet extends HttpServlet {

    private final CurrencyRateDAO currencyRateDAO = CurrencyRateDAOImpl.INSTANCE;
    private final CurrencyDAO currencyDAO = CurrencyDAOImpl.INSTANCE;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");

        if (baseCurrencyCode == null || targetCurrencyCode == null || rate == null) {
            resp.sendError(400, "One of parameters is missed");
            return;
        }
        try {
            CurrencyRate currRate = currencyRateDAO.getCurrencyRateByCodes(baseCurrencyCode + targetCurrencyCode);
            if (currRate != null) {
                resp.sendError(409, "CurrencyRate with such codes is already exists");
                return;
            }
            currRate = new CurrencyRate(
                    0,
                    currencyDAO.getCurrencyByCode(baseCurrencyCode),
                    currencyDAO.getCurrencyByCode(targetCurrencyCode),
                    BigDecimal.valueOf(Double.parseDouble(rate))
            );
            currencyRateDAO.createCurrencyRate(currRate);

            resp.sendRedirect("/exchangeRate/" + baseCurrencyCode + targetCurrencyCode);
        } catch (SQLException e) {
            resp.sendError(500, "Unable to connect to database");
        }
    }
}
