package servlets;

import DAO.CurrencyRateDAO;
import DAO.CurrencyRateDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mappers.ExchangeMapper;
import models.CurrencyRate;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private final CurrencyRateDAO currencyRateDAO = CurrencyRateDAOImpl.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String amount = req.getParameter("amount");

        if (from == null || to == null || amount == null) {
            resp.sendError(400, "Missing one or more required parameters");
            return;
        }
        BigDecimal amountDecimal = BigDecimal.valueOf(Integer.parseInt(amount));
        try(PrintWriter out = resp.getWriter()) {
            CurrencyRate cr1 = currencyRateDAO.getCurrencyRateByCodes(from + to);
            CurrencyRate cr2 = currencyRateDAO.getCurrencyRateByCodes(to + from);
            if (cr1 != null) {
                resp.setStatus(200);
                out.print(ExchangeMapper.toJson(
                        cr1.baseCurrency(),
                        cr1.targetCurrency(),
                        cr1.rate(),
                        amountDecimal,
                        amountDecimal.multiply(cr1.rate())
                        ));
            } else if (cr2 != null) {
                resp.setStatus(200);
                out.print(ExchangeMapper.toJson(
                        cr2.targetCurrency(),
                        cr2.baseCurrency(),
                        amountDecimal.divide(amountDecimal.multiply(cr2.rate())),
                        amountDecimal,
                        amountDecimal.divide(cr2.rate())
                ));
            } else {
                cr1 = currencyRateDAO.getCurrencyRateByCodes("USD" + from);
                cr2 = currencyRateDAO.getCurrencyRateByCodes("USD" + to);

                if (cr1 == null || cr2 == null) {
                    resp.sendError(404, "We have no currency rates for your currencies");
                    return;
                }
                resp.setStatus(200);
                out.print(ExchangeMapper.toJson(
                        cr1.targetCurrency(),
                        cr2.targetCurrency(),
                        cr2.rate().divide(cr1.rate(), 2, RoundingMode.DOWN),
                        amountDecimal,
                        amountDecimal.multiply(cr2.rate().divide(cr1.rate(), 2, RoundingMode.DOWN))
                ));
            }
        } catch (SQLException e) {
            resp.sendError(500, "Unable to connect to database");
        }
    }
}
