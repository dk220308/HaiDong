package DAO;

import Model.HoaDon;
import Model.TopSanPham; // Đảm bảo bạn có class TopSanPham trong Model
import Service.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Import QLTKE chỉ khi bạn cần sử dụng UI_DATE_FORMATTER trực tiếp từ QLTKE
// Nếu bạn chỉ dùng để format cho UI, tốt nhất là nên format ngay trong QLTKE
// import View.QLTKE; 

public class ThongKeDAO {

    public ThongKeDAO() {
        // Constructor
    }

    public List<HoaDon> getAllHoaDon() throws SQLException {
        List<HoaDon> danhSachHoaDon = new ArrayList<>();
        // SỬA CÂU SQL: JOIN với bảng KhachHang thông qua SDT
        // Chọn các cột cần thiết từ cả HoaDon (hd) và KhachHang (kh)
        String sql = "SELECT hd.MaHD, hd.MaNV, hd.SDT, kh.TenKH, hd.TrangThai, hd.NgayTao, hd.TongTien, hd.TienTra, hd.TienThua, hd.ThanhToan, hd.GhiChu, hd.GiaoHang " +
                     "FROM HoaDon hd " +
                     "JOIN KhachHang kh ON hd.SDT = kh.SDT " + // THAY ĐỔI JOIN TỪ MaKH SANG SDT
                     "ORDER BY NgayTao DESC";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMahd(rs.getString("MaHD"));
                hd.setManv(rs.getString("MaNV"));
                
                // Lấy SDT và TenKH từ bảng KhachHang (qua JOIN)
                hd.setSdt(rs.getString("SDT"));     // Set SDT
                hd.setTenkh(rs.getString("TenKH")); // Set TenKH
                
                hd.setTrangThai(rs.getString("TrangThai"));

                // Đọc NgayTao từ DB (kiểu DATE) và chuyển đổi sang LocalDate
                java.sql.Date sqlDate = rs.getDate("NgayTao");
                if (sqlDate != null) {
                    hd.setNgayTao(sqlDate.toLocalDate()); // Chuyển đổi sang LocalDate
                } else {
                    hd.setNgayTao(null); // Xử lý trường hợp ngày là NULL
                }
                
                hd.setTongTien(rs.getDouble("TongTien"));
                hd.setTienTra(rs.getDouble("TienTra"));
                hd.setTienThua(rs.getDouble("TienThua"));
                hd.setThanhToan(rs.getString("ThanhToan"));
                hd.setGiaoHang(rs.getString("GiaoHang"));
                hd.setGhiChu(rs.getString("GhiChu"));
                danhSachHoaDon.add(hd);
            }
        }
        return danhSachHoaDon;
    }

    public List<HoaDon> getDanhSachHoaDonByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<HoaDon> hoaDonList = new ArrayList<>();
        // SỬA CÂU SQL: JOIN với bảng KhachHang thông qua SDT
        String sql = "SELECT hd.MaHD, hd.MaNV, hd.SDT, kh.TenKH, hd.TrangThai, hd.NgayTao, hd.TongTien, hd.TienTra, hd.TienThua, hd.ThanhToan, hd.GiaoHang, hd.GhiChu " +
                     "FROM HoaDon hd " +
                     "JOIN KhachHang kh ON hd.SDT = kh.SDT " + // THAY ĐỔI JOIN TỪ MaKH SANG SDT
                     "WHERE hd.NgayTao BETWEEN ? AND ? " + // SỬ DỤNG TRỰC TIẾP KIỂU DATE
                     "ORDER BY NgayTao DESC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set ngày tháng dưới dạng java.sql.Date
            stmt.setDate(1, java.sql.Date.valueOf(startDate));
            stmt.setDate(2, java.sql.Date.valueOf(endDate));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    HoaDon hd = new HoaDon();
                    hd.setMahd(rs.getString("MaHD"));
                    hd.setManv(rs.getString("MaNV"));
                    
                    // Lấy SDT và TenKH từ bảng KhachHang (qua JOIN)
                    hd.setSdt(rs.getString("SDT"));
                    hd.setTenkh(rs.getString("TenKH"));
                    
                    hd.setTrangThai(rs.getString("TrangThai"));

                    // Đọc NgayTao từ DB (kiểu DATE) và chuyển đổi sang LocalDate
                    java.sql.Date sqlDate = rs.getDate("NgayTao");
                    if (sqlDate != null) {
                        hd.setNgayTao(sqlDate.toLocalDate());
                    } else {
                        hd.setNgayTao(null);
                    }
                    
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

    public double getTongDoanhThu() throws SQLException {
        double tongDoanhThu = 0;
        // Sử dụng CAST để đảm bảo tính toán chính xác và ISNULL để xử lý trường hợp không có doanh thu
        String sql = "SELECT ISNULL(SUM(CAST(TongTien AS DECIMAL(18, 2))), 0) FROM HoaDon WHERE TrangThai LIKE N'Ðã thanh toán%'";
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
        String sql = "SELECT ISNULL(SUM(SoLuong), 0) FROM SanPham"; // Sử dụng ISNULL
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                tongTonKho = rs.getInt(1);
            }
        }
        return tongTonKho;
    }

    public List<TopSanPham> getTop5SanPhamByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<TopSanPham> topSanPhamList = new ArrayList<>();
        String sql = "SELECT TOP 5 sp.TenSP, SUM(hdct.SoLuong) AS TongSoLuongBan, SUM(hdct.SoLuong * hdct.DonGia) AS TongDoanhThu " +
                     "FROM HoaDonChiTiet hdct " +
                     "JOIN SanPham sp ON hdct.MaSP = sp.MaSP " +
                     "JOIN HoaDon hd ON hdct.MaHD = hd.MaHD " +
                     "WHERE hd.NgayTao BETWEEN ? AND ? " + // SỬ DỤNG TRỰC TIẾP KIỂU DATE
                     "GROUP BY sp.TenSP " +
                     "ORDER BY TongDoanhThu DESC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Set ngày tháng dưới dạng java.sql.Date
            stmt.setDate(1, java.sql.Date.valueOf(startDate));
            stmt.setDate(2, java.sql.Date.valueOf(endDate));
            
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