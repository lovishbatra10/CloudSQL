package com.example.cloudsql;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@SuppressFBWarnings(
    value = {"SE_NO_SERIALVERSIONID", "WEM_WEAK_EXCEPTION_MESSAGING"},
    justification = "Not needed for IndexServlet, Exception adds context")
@WebServlet(name = "Index", value = "")
public class IndexServlet extends HttpServlet {

  private static final Logger LOGGER = Logger.getLogger(IndexServlet.class.getName());

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    DataSource pool = (DataSource) req.getServletContext().getAttribute("my-pool");

    int tabCount = 0;
    int spaceCount = 0;
    List<Vote> recentVotes = new ArrayList<>();
    try (Connection conn = pool.getConnection()) {
      String stmt2 = "SELECT COUNT(vote_id) FROM votes;";
      try (PreparedStatement voteCountStmt = conn.prepareStatement(stmt2); ) {
        ResultSet tabResult = voteCountStmt.executeQuery();
        if (tabResult.next()) {
          tabCount = tabResult.getInt(1);
        }
      }
    } catch (SQLException ex) {
      throw new ServletException(
          "Unable to successfully connect to the database. Please check the "
              + "steps in the README and try again.",
          ex);
    }

    // Add variables and render the page
    req.setAttribute("count", tabCount);
    req.getRequestDispatcher("/index.jsp").forward(req, resp);
  }

  @SuppressFBWarnings(
      value = {"SERVLET_PARAMETER", "XSS_SERVLET"},
      justification = "Input is validated and sanitized.")
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Timestamp now = new Timestamp(new Date().getTime());
    DataSource pool = (DataSource) req.getServletContext().getAttribute("my-pool");
    // ADD query whose time needs to be evaluated in Prepared Statement and check the Time take in the logs.
    try (Connection conn = pool.getConnection()) {
      String stmt = "INSERT INTO votes (time_cast, candidate) VALUES (?, ?);";
      try (PreparedStatement voteStmt = conn.prepareStatement(stmt); ) {
        int i=0;
        while(i<100){
          voteStmt.setTimestamp(1, now);
          voteStmt.setString(2, "TABS");
          voteStmt.addBatch();
          i+=1;
        }
        long start  = System.currentTimeMillis();
        voteStmt.executeBatch();
        long end = System.currentTimeMillis();
        LOGGER.log(Level.INFO, "query took " +
            (end - start) + "ms");
      }
    } catch (SQLException ex) {
      LOGGER.log(Level.WARNING, "Error while attempting to crete entry.", ex);
      resp.setStatus(500);
      resp.getWriter()
          .write(
              "Unable to successfully create entry! Please check the application "
                  + "logs for more details.");
    }
    resp.setStatus(200);
    resp.getWriter().printf("Entry successfully created at time %s!%n", now);
  }
}
