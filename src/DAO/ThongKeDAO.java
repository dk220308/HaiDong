package DAO;

import Model.HoaDon;
import Model.TopSanPham;
import Service.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ThongKeDAO {

    // CHỈNH SỬA Ở ĐÂY: Thay đổi định dạng ngày tháng trong CSDL thành "d/M/yyyy"
    // CSDL của bạn lưu "D/M/YYYY" (ví dụ: 6/6/2025, 10/7/2025), không phải MM/DD/YYYY
    private static final DateTimeFormatter DB_DATE_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy");

    // Định dạng ngày tháng trên giao diện người dùng (UI)
    // UI của bạn đang hiển thị và mong muốn định dạng "yyyy-MM-dd"
    private static final DateTimeFormatter UI_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public ThongKeDAO() {
        // Constructor
    }

    public List<HoaDon> getAllHoaDon() throws SQLException {
        List<HoaDon> danhSachHoaDon = new ArrayList<>();
        // Giữ nguyên câu SQL này vì nó chỉ SELECT, không CONVERT
        String sql = "SELECT MaHD, MaNV, TenKH, Sdt, TrangThai, NgayTao, TongTien, TienTra, TienThua, ThanhToan, GhiChu, GiaoHang FROM HoaDon ORDER BY NgayTao DESC";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMahd(rs.getString("MaHD"));
                hd.setManv(rs.getString("MaNV"));
                hd.setTenkh(rs.getString("TenKH"));
                hd.setSdt(rs.getString("Sdt"));
                hd.setTrangThai(rs.getString("TrangThai"));
                
                // Lấy ngày từ DB dưới dạng String
                String ngayTaoDBStr = rs.getString("NgayTao");
                // Chuyển đổi từ định dạng DB sang LocalDate, sau đó sang String định dạng UI cho model
                try {
                    LocalDate ngayTaoLocalDate = LocalDate.parse(ngayTaoDBStr, DB_DATE_FORMATTER); // Dùng DB_DATE_FORMATTER mới
                    hd.setNgayTao(ngayTaoLocalDate.format(UI_DATE_FORMATTER)); // Định dạng để hiển thị trên UI
                } catch (java.time.format.DateTimeParseException e) {
                    System.err.println("Lỗi parse ngày tháng từ DB: " + ngayTaoDBStr + " - " + e.getMessage());
                    hd.setNgayTao(ngayTaoDBStr); // Giữ nguyên chuỗi nếu không parse được
                }
                
                hd.setTongTien(rs.getDouble("TongTien"));
                hd.setTienTra(rs.getDouble("TienTra"));
                hd.setTienThua(rs.getDouble("TienThua"));
                hd.setThanhToan(rs.getString("ThanhToan"));
                hd.setGiaoHang(rs.getString("GiaoHang")); // Đảm bảo cột GiaoHang được lấy
                hd.setGhiChu(rs.getString("GhiChu"));
                danhSachHoaDon.add(hd);
            }
        }
        return danhSachHoaDon;
    }

    // --- Các hàm lấy tổng quan ---
public double getTongDoanhThu() throws SQLException {
    double tongDoanhThu = 0;
    // SỬA CÂU SQL: Chắc chắn dùng LIKE N'Đã thanh toán%' để bỏ qua ký tự ẩn cuối cùng
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
        String sql = "SELECT SUM(Soluong) FROM SanPham";
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
        // SỬA CÂU CONVERT: Sử dụng style 103 để chuyển đổi từ DD/MM/YYYY (CSDL của bạn)
        // và style 101 để chuyển đổi từ MM/DD/YYYY (Java gửi vào nếu dùng DB_DATE_FORMATTER cũ)
        // NHƯNG vì bạn đã đổi DB_DATE_FORMATTER thành "d/M/yyyy", thì chuỗi startDate.format(DB_DATE_FORMATTER)
        // sẽ là "DD/MM/YYYY". Để SQL Server hiểu chuỗi "DD/MM/YYYY" là ngày tháng, ta dùng CONVERT(DATE, ?, 103).
        String sql = "SELECT MaHD, MaNV, TenKH, SDT, TrangThai, NgayTao, TongTien, TienTra, TienThua, ThanhToan, GiaoHang, GhiChu FROM HoaDon WHERE CONVERT(DATE, NgayTao, 103) BETWEEN CONVERT(DATE, ?, 103) AND CONVERT(DATE, ?, 103) ORDER BY NgayTao DESC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Chuyển đổi LocalDate sang String theo định dạng CSDL (DB_DATE_FORMATTER = d/M/yyyy)
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
                    
                    String ngayTaoDBStr = rs.getString("NgayTao");
                    try {
                        LocalDate ngayTaoLocalDate = LocalDate.parse(ngayTaoDBStr, DB_DATE_FORMATTER); // Dùng DB_DATE_FORMATTER mới
                        hd.setNgayTao(ngayTaoLocalDate.format(UI_DATE_FORMATTER));
                    } catch (java.time.format.DateTimeParseException e) {
                        System.err.println("Lỗi parse ngày tháng từ DB: " + ngayTaoDBStr + " - " + e.getMessage());
                        hd.setNgayTao(ngayTaoDBStr);
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

    // --- Hàm lấy Top 5 sản phẩm bán chạy theo thời gian ---
    public List<TopSanPham> getTop5SanPhamByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<TopSanPham> topSanPhamList = new ArrayList<>();
        String sql = "SELECT TOP 5 sp.TenSP, SUM(hdct.SoLuong) AS TongSoLuongBan, SUM(hdct.SoLuong * hdct.DonGia) AS TongDoanhThu " +
                     "FROM HoaDonChiTiet hdct " +
                     "JOIN SanPham sp ON hdct.MaSP = sp.MaSP " +
                     "JOIN HoaDon hd ON hdct.MaHD = hd.MaHD " +
                     // SỬA CÂU CONVERT: Sử dụng style 103 để chuyển đổi từ DD/MM/YYYY
                     "WHERE CONVERT(DATE, hd.NgayTao, 103) BETWEEN CONVERT(DATE, ?, 103) AND CONVERT(DATE, ?, 103) " +
                     "GROUP BY sp.TenSP " +
                     "ORDER BY TongDoanhThu DESC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Chuyển đổi LocalDate sang String theo định dạng CSDL (DB_DATE_FORMATTER = d/M/yyyy)
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