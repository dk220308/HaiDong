package Model;

// import java.time.LocalDate; // Không cần import này nếu ngayTao là String

/**
 *
 * @author XPS
 */
public class HoaDon {
    private String mahd;
    private String Manv;
    private String tenkh;
    private String sdt;
    private String trangThai;
    private String ngayTao; // Giữ nguyên là String để khớp với DB nvarchar(20)
    private double tongTien;
    private double tienTra;
    private double tienThua;
    private String thanhToan;
    private String giaoHang;
    private String ghiChu;

    public HoaDon() {
    }

    // Constructor đã cập nhật để nhận String cho ngayTao
    public HoaDon(String mahd, String Manv, String tenkh, String sdt, String trangThai, String ngayTao, double tongTien, double tienTra, double tienThua, String thanhToan, String giaoHang, String ghiChu) {
        this.mahd = mahd;
        this.Manv = Manv;
        this.tenkh = tenkh;
        this.sdt = sdt;
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
        return Manv;
    }

    public void setManv(String Manv) {
        this.Manv = Manv;
    }

    public String getTenkh() {
        return tenkh;
    }

    public void setTenkh(String tenkh) {
        this.tenkh = tenkh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    // Getter cho String
    public String getNgayTao() {
        return ngayTao;
    }

    // Setter cho String
    public void setNgayTao(String ngayTao) {
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