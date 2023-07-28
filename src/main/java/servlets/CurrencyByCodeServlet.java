package servlets;

import DAO.CurrencyDAOImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Currency;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/currency/*")
public class CurrencyByCodeServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CurrencyDAOImpl currencyDAO = new CurrencyDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getPathInfo().substring(1);
        if (code.equals("")) {
            resp.sendError(400, "You haven't put currency code in path");
            return;
        }

        try {
            Currency curr = currencyDAO.getCurrencyByCode(code);
            if (curr == null) {
                resp.sendError(404, "Currency not found");
            } else {
                objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                String json = objectMapper.writeValueAsString(curr);
                PrintWriter pw = resp.getWriter();
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                pw.print(json);
                pw.close();
            }
        } catch (SQLException e) {
            resp.sendError(500, e.getMessage());
        }
    }
}
