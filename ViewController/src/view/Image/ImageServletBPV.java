package view.Image;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.sql.Blob;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;

import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import javax.sql.DataSource;

@WebServlet(name = "ImageServletBPV", urlPatterns = { "/imageservletbpv" })
public class ImageServletBPV extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        response.setContentType(CONTENT_TYPE);
               String imageId = request.getParameter("Id");
               OutputStream os = response.getOutputStream();
               Connection conn = null;
               try {
                   Context ctx = new InitialContext();  
                   DataSource ds= (DataSource)ctx.lookup("java:comp/env/jdbc/Connection1DS");
                   conn = ds.getConnection();
                   PreparedStatement statement =
                       conn.prepareStatement("SELECT Id,Image_Name " +
                                             "FROM Tbl_BPV_M " +
                                             "WHERE Id = ?");
                   statement.setInt(1, new Integer(imageId));
                   ResultSet rs = statement.executeQuery();

                   if (rs.next()) {
                       Blob blob = rs.getBlob("IMAGE_NAME");               
                       BufferedInputStream in = new BufferedInputStream(blob.getBinaryStream());
                       int b;
                       byte[] buffer = new byte[10240];
                       while ((b = in.read(buffer, 0, 10240)) != -1) {
                           os.write(buffer, 0, b);
                       }
                       os.close();
                   }
               } catch (Exception e) {
                   System.out.println(e);
               } finally {
                   try {
                       if (conn != null) {
                           conn.close();
                       }
                   } catch (SQLException sqle) {
                       System.out.println("SQLException error");
                   }
               }
    }
}
