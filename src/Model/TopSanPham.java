package Model;

/**
 *
 * @author XPS
 */
public class TopSanPham {
    private String tenSanPham;
    private int tongSoLuongBan;
    private double tongDoanhThu; // Giữ là double để chính xác hơn

    public TopSanPham() {
    }

    public TopSanPham(String tenSanPham, int tongSoLuongBan, double tongDoanhThu) {
        this.tenSanPham = tenSanPham;
        this.tongSoLuongBan = tongSoLuongBan;
        this.tongDoanhThu = tongDoanhThu;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public int getTongSoLuongBan() {
        return tongSoLuongBan;
    }

    public void setTongSoLuongBan(int tongSoLuongBan) {
        this.tongSoLuongBan = tongSoLuongBan;
    }

    public double getTongDoanhThu() {
        return tongDoanhThu;
    }

    public void setTongDoanhThu(double tongDoanhThu) {
        this.tongDoanhThu = tongDoanhThu;
    }
}