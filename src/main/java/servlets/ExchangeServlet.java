package servlets;

import DTO.Exchange;
import exceptions.ExceptionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mappers.JsonMapper;
import services.ExchangeService;
import util.Validator;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private final ExchangeService exchangeService = ExchangeService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String amount = req.getParameter("amount");
        if (!Validator.isNotNull(from, to, amount)) {
            ExceptionHandler.handleException(400, "Missing one or more required parameters", resp);
            return;
        }
        BigDecimal amountDecimal = BigDecimal.valueOf(Integer.parseInt(amount));

        try {
            Exchange result = exchangeService.exchangeCurrency(from, to, amountDecimal);

            if (result == null) {
                ExceptionHandler.handleException(404, "We have no currency rates for your currencies", resp);
                return;
            }
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            String json = JsonMapper.toJson(result);
            PrintWriter out = resp.getWriter();
            resp.setStatus(200);
            out.print(json);
            out.flush();
        } catch (SQLException e) {
            ExceptionHandler.handleException(500, "Unable to connect to database", resp);
        }
    }
}
