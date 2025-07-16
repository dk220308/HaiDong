// File: DAO/KhuyenMaiDAO.java
package DAO;

import Model.KhuyenMai;
import Service.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMaiDAO {

    public List<KhuyenMai> getAllKhuyenMai() throws SQLException {
        List<KhuyenMai> list = new ArrayList<>();
        String sql = "SELECT MaKM, MaSP, TenKM, NgayBdau, NgayKthuc, LoaiSP, GiamGia, TrangThai FROM KhuyenMai";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                KhuyenMai km = new KhuyenMai();
                km.setMaKM(rs.getString("MaKM"));
                km.setMaSP(rs.getString("MaSP"));
                km.setTenKM(rs.getString("TenKM"));
                km.setNgayBdau(rs.getString("NgayBdau"));
                km.setNgayKthuc(rs.getString("NgayKthuc"));
                km.setLoaiSP(rs.getString("LoaiSP"));
                km.setGiamGia(rs.getDouble("GiamGia"));
                km.setTrangThai(rs.getString("TrangThai"));
                list.add(km);
            }
        }
        return list;
    }

    public KhuyenMai getKhuyenMaiByMaKM(String maKM) throws SQLException {
        KhuyenMai km = null;
        String sql = "SELECT MaKM, MaSP, TenKM, NgayBdau, NgayKthuc, LoaiSP, GiamGia, TrangThai FROM KhuyenMai WHERE MaKM = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, maKM);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    km = new KhuyenMai();
                    km.setMaKM(rs.getString("MaKM"));
                    km.setMaSP(rs.getString("MaSP"));
                    km.setTenKM(rs.getString("TenKM"));
                    km.setNgayBdau(rs.getString("NgayBdau"));
                    km.setNgayKthuc(rs.getString("NgayKthuc"));
                    km.setLoaiSP(rs.getString("LoaiSP"));
                    km.setGiamGia(rs.getDouble("GiamGia"));
                    km.setTrangThai(rs.getString("TrangThai"));
                }
            }
        }
        return km;
    }

    public boolean addKhuyenMai(KhuyenMai km) throws SQLException {
        String sql = "INSERT INTO KhuyenMai (MaKM, MaSP, TenKM, NgayBdau, NgayKthuc, LoaiSP, GiamGia, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, km.getMaKM());
            pst.setString(2, km.getMaSP());
            pst.setString(3, km.getTenKM());
            pst.setString(4, km.getNgayBdau());
            pst.setString(5, km.getNgayKthuc());
            pst.setString(6, km.getLoaiSP());
            pst.setDouble(7, km.getGiamGia());
            pst.setString(8, km.getTrangThai());
            return pst.executeUpdate() > 0;
        }
    }

    public boolean updateKhuyenMai(KhuyenMai km) throws SQLException {
        String sql = "UPDATE KhuyenMai SET MaSP = ?, TenKM = ?, NgayBdau = ?, NgayKthuc = ?, LoaiSP = ?, GiamGia = ?, TrangThai = ? WHERE MaKM = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, km.getMaSP());
            pst.setString(2, km.getTenKM());
            pst.setString(3, km.getNgayBdau());
            pst.setString(4, km.getNgayKthuc());
            pst.setString(5, km.getLoaiSP());
            pst.setDouble(6, km.getGiamGia());
            pst.setString(7, km.getTrangThai());
            pst.setString(8, km.getMaKM());
            return pst.executeUpdate() > 0;
        }
    }

    public boolean deleteKhuyenMai(String maKM) throws SQLException {
        String sql = "DELETE FROM KhuyenMai WHERE MaKM = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, maKM);
            return pst.executeUpdate() > 0;
        }
    }
}