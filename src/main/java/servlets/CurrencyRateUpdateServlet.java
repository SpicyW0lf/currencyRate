package servlets;

import exceptions.ExceptionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.CurrencyRate;
import services.CurrencyRateService;
import util.Validator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.SQLException;


@WebServlet("/exchangeRate/update/*")
public class CurrencyRateUpdateServlet extends HttpServlet {

    private final CurrencyRateService currencyRateService = CurrencyRateService.INSTANCE;

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
        if (!Validator.isCodePairValid(codes)) {
            ExceptionHandler.handleException(400, "Incorrect currency pair", resp);
            return;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String line = br.readLine();
        br.close();
        if (line == null) {
            ExceptionHandler.handleException(400, "Missing required parameter", resp);
            return;
        }
        String[] parameters = line.split("&");
        if (parameters.length > 1) {
            ExceptionHandler.handleException(400, "Remove unnecessary parameters", resp);
            return;
        }
        String[] rateParameter = parameters[0].split("=");
        if (!rateParameter[0].equals("rate")) {
            ExceptionHandler.handleException(400, "Missing required parameter", resp);
            return;
        }
        String rate = rateParameter[1];
        if (rate == null) {
            ExceptionHandler.handleException(400, "Missing required parameter", resp);
            return;
        }

        try {
            CurrencyRate cr = currencyRateService.getByCodePair(codes);
            if (cr == null) {
                ExceptionHandler.handleException(404, "Entered currency rate doesn't exists", resp);
                return;
            }
            currencyRateService.update(cr, BigDecimal.valueOf(Double.parseDouble(rate)));

            resp.sendRedirect("/exchangeRate/" + codes);
        } catch (SQLException e) {
            ExceptionHandler.handleException(500, "Unable to connect to database", resp);
        }
    }
}
