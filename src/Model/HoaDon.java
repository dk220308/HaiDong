package Model;

import java.time.LocalDate; // Import LocalDate

public class HoaDon {
    private String mahd;
    private String manv;
    // tempTenKH và tempSdt sẽ được điền thông qua JOIN từ bảng KhachHang cho mục đích hiển thị trên UI
    private String tempTenKH; 
    private String tempSdt;   

    private String trangThai;
    private LocalDate ngayTao; // Đã thay đổi kiểu dữ liệu thành LocalDate
    private double tongTien;
    private double tienTra;
    private double tienThua;
    private String thanhToan;
    private String giaoHang;
    private String ghiChu;

    public HoaDon() {
    }

    // Constructor đã cập nhật để nhận LocalDate cho ngayTao
    public HoaDon(String mahd, String manv, String tempTenKH, String tempSdt, String trangThai, LocalDate ngayTao, double tongTien, double tienTra, double tienThua, String thanhToan, String giaoHang, String ghiChu) {
        this.mahd = mahd;
        this.manv = manv;
        this.tempTenKH = tempTenKH;
        this.tempSdt = tempSdt;
        this.trangThai = trangThai;
        this.ngayTao = ngayTao;
        this.tongTien = tongTien;
        this.tienTra = tienTra;
        this.tienThua = tienThua;
        this.thanhToan = thanhToan;
        this.giaoHang = giaoHang;
        this.ghiChu = ghiChu;
    }

    public String getMahd() {
        return mahd;
    }

    public void setMahd(String mahd) {
        this.mahd = mahd;
    }

    public String getManv() {
        return manv;
    }

    public void setManv(String manv) {
        this.manv = manv;
    }

    // Getter và Setter cho TenKH (tạm thời cho hiển thị từ JOIN)
    public String getTenkh() {
        return tempTenKH;
    }

    public void setTenkh(String tempTenKH) {
        this.tempTenKH = tempTenKH;
    }

    // Getter và Setter cho Sdt (tạm thời cho hiển thị từ JOIN)
    public String getSdt() {
        return tempSdt;
    }

    public void setSdt(String tempSdt) {
        this.tempSdt = tempSdt;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    // Getter và Setter cho ngayTao (kiểu LocalDate)
    public LocalDate getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDate ngayTao) {
        this.ngayTao = ngayTao;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public double getTienTra() {
        return tienTra;
    }

    public void setTienTra(double tienTra) {
        this.tienTra = tienTra;
    }

    public double getTienThua() {
        return tienThua;
    }

    public void setTienThua(double tienThua) {
        this.tienThua = tienThua;
    }

    public String getThanhToan() {
        return thanhToan;
    }

    public void setThanhToan(String thanhToan) {
        this.thanhToan = thanhToan;
    }

    public String getGiaoHang() {
        return giaoHang;
    }

    public void setGiaoHang(String giaoHang) {
        this.giaoHang = giaoHang;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}