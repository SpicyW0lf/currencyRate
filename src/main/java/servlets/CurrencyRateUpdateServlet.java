package servlets;

import DAO.CurrencyRateDAO;
import DAO.CurrencyRateDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.CurrencyRate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;


@WebServlet("/exchangeRate/update/*")
public class CurrencyRateUpdateServlet extends HttpServlet {

    private final CurrencyRateDAO currencyRateDAO = CurrencyRateDAOImpl.INSTANCE;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();

        if (!method.equals("PATCH")) {
            super.service(req, resp);
            return;
        }

        this.doPatch(req, resp);
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String codes = req.getPathInfo().substring(1);
        if (codes.length() != 6) {
            resp.sendError(400, "Incorrect currency pair");
            return;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String line = br.readLine();
        br.close();
        if (line == null) {
            resp.sendError(400, "Missing required parameter");
            return;
        }
        String[] parameters = line.split("&");
        if (parameters.length > 1) {
            resp.sendError(400, "Remove unnecessary parameters");
            return;
        }
        String[] rateParameter = parameters[0].split("=");
        if (!rateParameter[0].equals("rate")) {
            resp.sendError(400, "Missing required parameter");
            return;
        }
        String rate = rateParameter[1];
        if (rate == null) {
            resp.sendError(400, "Missing required parameter");
            return;
        }

        try {
            CurrencyRate cr = currencyRateDAO.getCurrencyRateByCodes(codes);
            if (cr == null) {
                resp.sendError(404, "Entered currency rate doesn't exists");
                return;
            }
            currencyRateDAO.updateCurrencyRate(cr, BigDecimal.valueOf(Double.parseDouble(rate)));

            resp.sendRedirect("/exchangeRate/" + codes);
        } catch (SQLException e) {
            resp.sendError(500, "Unable to connect to database");
        }
    }
}
