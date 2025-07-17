package DAO;

import Model.KhachHang;
import Service.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {
    public List<KhachHang> getAllKhachHang() throws SQLException {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT MaKH, TenKH, GioiTinh, SDT, TrangThai, DiaChi FROM KhachHang";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                KhachHang kh = new KhachHang(
                    rs.getString("MaKH"),
                    rs.getString("TenKH"),
                    rs.getString("GioiTinh"),
                    rs.getString("SDT"),
                    rs.getString("TrangThai"),
                    rs.getString("DiaChi")
                );
                list.add(kh);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e; 
        }
        return list;
    }

    public boolean insertKhachHang(KhachHang kh) throws SQLException {
        String sql = "INSERT INTO KhachHang (MaKH, TenKH, GioiTinh, SDT, TrangThai, DiaChi) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getGioiTinh());
            ps.setString(4, kh.getSdt()); // SDT là khóa chính
            ps.setString(5, kh.getTrangThai());
            ps.setString(6, kh.getDiaChi());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // Xử lý lỗi trùng khóa chính (duplicate primary key) nếu cần thiết
            if (e.getMessage().contains("Violation of PRIMARY KEY constraint")) {
                throw new SQLException("Số điện thoại " + kh.getSdt() + " đã tồn tại.", e);
            }
            e.printStackTrace();
            throw e;
        }
    }

    public boolean updateKhachHang(KhachHang kh) throws SQLException {
        // Cập nhật dựa trên SDT vì SDT là khóa chính
        String sql = "UPDATE KhachHang SET MaKH = ?, TenKH = ?, GioiTinh = ?, TrangThai = ?, DiaChi = ? WHERE SDT = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, kh.getMaKH()); 
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getGioiTinh());
            ps.setString(4, kh.getTrangThai());
            ps.setString(5, kh.getDiaChi());
            ps.setString(6, kh.getSdt()); // Điều kiện WHERE là SDT

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    // Phương thức cập nhật trạng thái khách hàng riêng biệt (dùng cho Khóa/Mở khóa)
    public boolean updateTrangThaiKhachHang(String sdt, String newStatus) throws SQLException {
        String sql = "UPDATE KhachHang SET TrangThai = ? WHERE SDT = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setString(2, sdt);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    public boolean deleteKhachHang(String sdt) throws SQLException {
        String sql = "DELETE FROM KhachHang WHERE SDT = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, sdt);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // Xử lý lỗi ràng buộc khóa ngoại nếu có hóa đơn liên quan
            if (e.getMessage().contains("The DELETE statement conflicted with the REFERENCE constraint")) {
                throw new SQLException("Không thể xóa khách hàng này vì có hóa đơn liên quan. Vui lòng xóa hóa đơn trước hoặc cập nhật khách hàng của hóa đơn.", e);
            }
            e.printStackTrace();
            throw e;
        }
    }

    public List<KhachHang> searchBySDT(String sdtKeyword) throws SQLException {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT MaKH, TenKH, GioiTinh, SDT, TrangThai, DiaChi FROM KhachHang WHERE SDT LIKE ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + sdtKeyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KhachHang kh = new KhachHang(
                        rs.getString("MaKH"),
                        rs.getString("TenKH"),
                        rs.getString("GioiTinh"),
                        rs.getString("SDT"),
                        rs.getString("TrangThai"),
                        rs.getString("DiaChi")
                    );
                    list.add(kh);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return list;
    }
    
    // Phương thức tìm khách hàng theo SDT (để lấy chi tiết một khách hàng)
    public KhachHang getKhachHangBySdt(String sdt) throws SQLException {
        String sql = "SELECT MaKH, TenKH, GioiTinh, SDT, TrangThai, DiaChi FROM KhachHang WHERE SDT = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sdt);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new KhachHang(
                        rs.getString("MaKH"),
                        rs.getString("TenKH"),
                        rs.getString("GioiTinh"),
                        rs.getString("SDT"),
                        rs.getString("TrangThai"),
                        rs.getString("DiaChi")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }
    
    public int getSoLuongHoaDonBySdt(String sdt) throws SQLException {
    int count = 0;
    String sql = "SELECT COUNT(mahd) FROM hoadon WHERE sdt = ?";
    try (Connection conn = DBConnect.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, sdt);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        }
    }
    return count;
}
    
}