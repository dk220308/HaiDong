package Model;

public class KhachHang {
    private String maKH;
    private String tenKH;
    private String gioiTinh; // "Nam" hoặc "Nữ"
    private String sdt;
    private String trangThai; // "Khách quen" hoặc "Khách mới"
    private String diaChi;

    public KhachHang() {
    }

    public KhachHang(String maKH, String tenKH, String gioiTinh, String sdt, String trangThai, String diaChi) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.gioiTinh = gioiTinh;
        this.sdt = sdt;
        this.trangThai = trangThai;
        this.diaChi = diaChi;
    }

    // Getters & Setters
    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
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

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
}
