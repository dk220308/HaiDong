package DAO;

import Model.HoaDon;
import Model.HoaDonChiTiet;
import Service.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

    // Phương thức thêm hóa đơn mới (tạo cả hóa đơn và chi tiết hóa đơn)
    public boolean addHoaDon(HoaDon hoaDon, List<HoaDonChiTiet> danhSachChiTiet) throws SQLException {
        Connection con = null;
        PreparedStatement psHoaDon = null;
        PreparedStatement psChiTiet = null;
        boolean success = false;

        // SQL cho việc thêm HoaDon
        // Nếu bạn đã xóa TenKH khỏi DB, câu SQL này sẽ hoạt động.
        // Cần đảm bảo rằng các cột trong SQL khớp với các cột có trong bảng HoaDon
        String sqlHoaDon = "INSERT INTO HoaDon (MaHD, MaNV, SDT, TrangThai, NgayTao, TongTien, TienTra, TienThua, ThanhToan, GiaoHang, GhiChu) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlChiTiet = "INSERT INTO HoaDonChiTiet (MaHD, MaSP, TenSP, SoLuong, DonGia, GiamGia, ThanhTien) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            con = DBConnect.getConnection();
            con.setAutoCommit(false); // Bắt đầu transaction

            // 1. Thêm hóa đơn chính
            psHoaDon = con.prepareStatement(sqlHoaDon);
            psHoaDon.setString(1, hoaDon.getMahd());
            psHoaDon.setString(2, hoaDon.getManv());
            psHoaDon.setString(3, hoaDon.getSdt()); // LƯU SDT VÀO BẢNG HOADON (khóa ngoại)
            psHoaDon.setString(4, hoaDon.getTrangThai());
            
            // Chuyển đổi LocalDate sang java.sql.Date để lưu vào cột DATE
            psHoaDon.setDate(5, java.sql.Date.valueOf(hoaDon.getNgayTao()));
            
            psHoaDon.setDouble(6, hoaDon.getTongTien());
            psHoaDon.setDouble(7, hoaDon.getTienTra());
            psHoaDon.setDouble(8, hoaDon.getTienThua());
            psHoaDon.setString(9, hoaDon.getThanhToan());
            psHoaDon.setString(10, hoaDon.getGiaoHang());
            psHoaDon.setString(11, hoaDon.getGhiChu());
            psHoaDon.executeUpdate();

            // 2. Thêm các chi tiết hóa đơn
            psChiTiet = con.prepareStatement(sqlChiTiet);
            for (HoaDonChiTiet chiTiet : danhSachChiTiet) {
                psChiTiet.setString(1, chiTiet.getMahd());
                psChiTiet.setString(2, chiTiet.getMasp());
                psChiTiet.setString(3, chiTiet.getTensp());
                psChiTiet.setInt(4, chiTiet.getSluong());
                psChiTiet.setFloat(5, chiTiet.getDongia());
                psChiTiet.setFloat(6, chiTiet.getGiamGia());
                psChiTiet.setFloat(7, chiTiet.getThanhTien());
                psChiTiet.addBatch();
            }
            psChiTiet.executeBatch(); // Thực thi tất cả các lệnh thêm chi tiết

            con.commit(); // Hoàn thành transaction
            success = true;

        } catch (SQLException e) {
            if (con != null) {
                con.rollback(); // Rollback nếu có lỗi
            }
            e.printStackTrace();
            throw e; // Ném lỗi để xử lý ở tầng trên
        } finally {
            try {
                if (psHoaDon != null) psHoaDon.close();
                if (psChiTiet != null) psChiTiet.close();
                if (con != null) con.setAutoCommit(true); // Đặt lại auto-commit
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    // Phương thức lấy thông tin hóa đơn theo MaHD, kèm theo thông tin khách hàng
    public HoaDon getHoaDonById(String maHD) throws SQLException {
        HoaDon hd = null;
        // JOIN với KhachHang để lấy TenKH và Sdt cho model HoaDon
        String sql = "SELECT hd.MaHD, hd.MaNV, hd.SDT, kh.TenKH, hd.TrangThai, hd.NgayTao, hd.TongTien, hd.TienTra, hd.TienThua, hd.ThanhToan, hd.GiaoHang, hd.GhiChu " +
                     "FROM HoaDon hd " +
                     "JOIN KhachHang kh ON hd.SDT = kh.SDT " + // THAY ĐỔI JOIN TỪ MaKH SANG SDT
                     "WHERE hd.MaHD = ?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    hd = new HoaDon();
                    hd.setMahd(rs.getString("MaHD"));
                    hd.setManv(rs.getString("MaNV"));
                    hd.setSdt(rs.getString("SDT"));     // Set SDT
                    hd.setTenkh(rs.getString("TenKH")); // Set TenKH từ JOIN
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
                }
            }
        }
        return hd;
    }

    // Phương thức lấy chi tiết hóa đơn theo MaHD (giữ nguyên)
    public List<HoaDonChiTiet> getChiTietHoaDonByMaHD(String maHD) throws SQLException {
        List<HoaDonChiTiet> danhSachChiTiet = new ArrayList<>();
        String sql = "SELECT MaHD, MaSP, TenSP, SoLuong, DonGia, GiamGia, ThanhTien FROM HoaDonChiTiet WHERE MaHD = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HoaDonChiTiet hdct = new HoaDonChiTiet();
                    hdct.setMahd(rs.getString("MaHD"));
                    hdct.setMasp(rs.getString("MaSP"));
                    hdct.setTensp(rs.getString("TenSP"));
                    hdct.setSluong(rs.getInt("SoLuong"));
                    hdct.setDongia(rs.getFloat("DonGia"));
                    hdct.setGiamGia(rs.getFloat("GiamGia"));
                    hdct.setThanhTien(rs.getFloat("ThanhTien"));
                    danhSachChiTiet.add(hdct);
                }
            }
        }
        return danhSachChiTiet;
    }
    
    // Phương thức lấy hóa đơn theo SDT khách hàng (dùng cho "Lịch sử giao dịch" của QLKH)
    public List<HoaDon> getHoaDonsBySdt(String sdt) throws SQLException {
        List<HoaDon> danhSachHoaDon = new ArrayList<>();
        String sql = "SELECT hd.MaHD, hd.MaNV, hd.SDT, kh.TenKH, hd.TrangThai, hd.NgayTao, hd.TongTien, hd.TienTra, hd.TienThua, hd.ThanhToan, hd.GiaoHang, hd.GhiChu " +
                     "FROM HoaDon hd " +
                     "JOIN KhachHang kh ON hd.SDT = kh.SDT " + 
                     "WHERE hd.SDT = ? ORDER BY hd.NgayTao DESC"; 

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sdt);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HoaDon hd = new HoaDon();
                    hd.setMahd(rs.getString("MaHD"));
                    hd.setManv(rs.getString("MaNV"));
                    hd.setSdt(rs.getString("SDT"));
                    hd.setTenkh(rs.getString("TenKH"));
                    hd.setTrangThai(rs.getString("TrangThai"));

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
                    danhSachHoaDon.add(hd);
                }
            }
        }
        return danhSachHoaDon;
    }
}