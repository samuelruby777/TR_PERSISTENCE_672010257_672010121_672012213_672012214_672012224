/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Servlet_Booking;

import Dao.dao_booking;
import Dao.dao_kendaraan;
import Dao.dao_loggin;
import Dao.dao_transaksi;
import DaoImpl.bookingDaoImpl;
import DaoImpl.kendaraanDaoImpl;
import DaoImpl.logginDaoImpl;
import DaoImpl.transaksiDaoImpl;
import Entity.e_Kendaraan;
import Entity.e_booking;
import Entity.e_loggin;
import Entity.e_rental;
import Entity.e_transaksi;
import java.io.IOException;
import java.io.PrintWriter;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author RUBY
 */
public class deleteBooking extends HttpServlet {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Rental_KendaraanPU");

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String id_session = (String) session.getAttribute("userID");
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        EntityManager em = emf.createEntityManager();
        dao_booking bdao = new bookingDaoImpl(em);
        dao_loggin ldao = new logginDaoImpl(em);
        dao_kendaraan kdao = new kendaraanDaoImpl(em);
        long idbook = Integer.valueOf(request.getParameter("idBook"));

        e_loggin log = ldao.getById(id_session);
        e_rental ren = log.getIdRental();
        e_booking bok = bdao.getById(idbook);
        e_Kendaraan ken = kdao.getById(bok.getPlatNo().getPlatNo());
        String statusKn = "Ada";

        if (id_session == null) {
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Gagal Menghapus'); window.location.href = 'logginRental.jsp';");
            out.println("</script>");

        } else {
            try {
                if (ken.getStatus().equals("Ada")) {
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('GAGAL update Status Kendaraan');");
                    out.println("</script>");
                } else if (ken.getStatus().equals("Dipakai")) {

                    e_Kendaraan kn = new e_Kendaraan();
                    kn.setPlatNo(ken.getPlatNo());
                    kn.setStatus(statusKn);
                    kn.setGambar(ken.getGambar());
                    kn.setJenisKendaraan(ken.getJenisKendaraan());
                    kn.setIdRental(ren);
                    kn.setIdUser(log);
                    kdao.update(kn);

                    bdao.delete(idbook);
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('Data dihapus'); window.location.href = 'transaksiSer';");
                    out.println("</script>");
                }
            } catch (Exception e) {
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Gagal Menghapus'); window.location.href = 'hapusTransSer';");
                out.println("</script>");
            }
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
