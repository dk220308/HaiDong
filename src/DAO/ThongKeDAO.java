package DAO;

import Model.HoaDon;
import Model.TopSanPham;
import Service.DBConnect; // Import lớp DBConnect của bạn
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter; // Để format LocalDate sang String
import java.util.ArrayList;
import java.util.List;

public class ThongKeDAO {

    // Định dạng ngày tháng mà bạn lưu trong DB (nvarchar).
    // Đảm bảo định dạng này khớp với cách bạn lưu ngày tháng trong cột NgayTao của bảng HoaDon.
    // Ví dụ: "yyyy-MM-dd"
    private static final DateTimeFormatter DB_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ThongKeDAO() {
        // Constructor
    }

    // --- Các hàm lấy tổng quan ---
    public double getTongDoanhThu() throws SQLException {
        double tongDoanhThu = 0;
        String sql = "SELECT SUM(TongTien) FROM HoaDon WHERE TrangThai = N'Hoàn thành'";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                tongDoanhThu = rs.getDouble(1);
            }
        }
        return tongDoanhThu;
    }

    public int getTongDonHang() throws SQLException {
        int tongDonHang = 0;
        String sql = "SELECT COUNT(*) FROM HoaDon";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                tongDonHang = rs.getInt(1);
            }
        }
        return tongDonHang;
    }

    public int getTongSoLuongTonKho() throws SQLException {
        int tongTonKho = 0;
        // Đã sửa tên cột từ SoLuongTon thành Soluong để khớp với schema DB của bạn
        String sql = "SELECT SUM(Soluong) FROM SanPham"; // <-- Đã sửa ở đây
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                tongTonKho = rs.getInt(1);
            }
        }
        return tongTonKho;
    }

    // --- Hàm tìm kiếm hóa đơn theo thời gian ---
    public List<HoaDon> getDanhSachHoaDonByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<HoaDon> hoaDonList = new ArrayList<>();
        // Vì NgayTao trong DB là nvarchar, chúng ta sẽ so sánh chuỗi ngày
       String sql = "SELECT MaHD, MaNV, TenKH, SDT, TrangThai, NgayTao, TongTien, TienTra, TienThua, ThanhToan, GiaoHang, GhiChu FROM HoaDon WHERE CONVERT(DATE, NgayTao) BETWEEN ? AND ?";
        
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Chuyển đổi LocalDate sang String theo định dạng DB để so sánh
            stmt.setString(1, startDate.format(DB_DATE_FORMATTER));
            stmt.setString(2, endDate.format(DB_DATE_FORMATTER));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    HoaDon hd = new HoaDon();
                    hd.setMahd(rs.getString("MaHD"));
                    hd.setManv(rs.getString("MaNV"));
                    hd.setTenkh(rs.getString("TenKH"));
                    hd.setSdt(rs.getString("SDT"));
                    hd.setTrangThai(rs.getString("TrangThai"));
                    // Lấy ngày từ DB dưới dạng String
                    hd.setNgayTao(rs.getString("NgayTao")); 
                    hd.setTongTien(rs.getDouble("TongTien"));
                    hd.setTienTra(rs.getDouble("TienTra"));
                    hd.setTienThua(rs.getDouble("TienThua"));
                    hd.setThanhToan(rs.getString("ThanhToan"));
                    hd.setGiaoHang(rs.getString("GiaoHang"));
                    hd.setGhiChu(rs.getString("GhiChu"));
                    hoaDonList.add(hd);
                }
            }
        }
        return hoaDonList;
    }

    // --- Hàm lấy Top 5 sản phẩm bán chạy theo thời gian ---
    public List<TopSanPham> getTop5SanPhamByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<TopSanPham> topSanPhamList = new ArrayList<>();
        // Giả định bạn có bảng HoaDonChiTiet và SanPham
        // Sử dụng TOP 5 cho SQL Server. Nếu dùng MySQL/PostgreSQL, thay bằng LIMIT 5
        String sql = "SELECT TOP 5 sp.TenSP, SUM(hdct.SoLuong) AS TongSoLuongBan, SUM(hdct.SoLuong * hdct.DonGia) AS TongDoanhThu " +
             "FROM HoaDonChiTiet hdct " +
             "JOIN SanPham sp ON hdct.MaSP = sp.MaSP " +
             "JOIN HoaDon hd ON hdct.MaHD = hd.MaHD " +
             "WHERE CONVERT(DATE, hd.NgayTao) BETWEEN ? AND ? " + // <-- Sửa ở đây
             "GROUP BY sp.TenSP " +
             "ORDER BY TongDoanhThu DESC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Chuyển đổi LocalDate sang String theo định dạng DB để so sánh
            stmt.setString(1, startDate.format(DB_DATE_FORMATTER));
            stmt.setString(2, endDate.format(DB_DATE_FORMATTER));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TopSanPham tsp = new TopSanPham();
                    tsp.setTenSanPham(rs.getString("TenSP"));
                    tsp.setTongSoLuongBan(rs.getInt("TongSoLuongBan"));
                    tsp.setTongDoanhThu(rs.getDouble("TongDoanhThu"));
                    topSanPhamList.add(tsp);
                }
            }
        }
        return topSanPhamList;
    }
}