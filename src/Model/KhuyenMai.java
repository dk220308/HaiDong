package Model;

public class KhuyenMai {
    private String maKM;
    private String maSP;
    private String tenKM;
    private String ngayBdau; // Lưu dạng String "dd/MM/yyyy"
    private String ngayKthuc; // Lưu dạng String "dd/MM/yyyy"
    private String loaiSP;
    private double giamGia;
    private String trangThai;

    public KhuyenMai() {}

    public KhuyenMai(String maKM, String maSP, String tenKM, String ngayBdau, String ngayKthuc, String loaiSP, double giamGia, String trangThai) {
        this.maKM = maKM;
        this.maSP = maSP;
        this.tenKM = tenKM;
        this.ngayBdau = ngayBdau;
        this.ngayKthuc = ngayKthuc;
        this.loaiSP = loaiSP;
        this.giamGia = giamGia;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public String getMaKM() { return maKM; }
    public void setMaKM(String maKM) { this.maKM = maKM; }

    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }

    public String getTenKM() { return tenKM; }
    public void setTenKM(String tenKM) { this.tenKM = tenKM; }

    public String getNgayBdau() { return ngayBdau; }
    public void setNgayBdau(String ngayBdau) { this.ngayBdau = ngayBdau; }

    public String getNgayKthuc() { return ngayKthuc; }
    public void setNgayKthuc(String ngayKthuc) { this.ngayKthuc = ngayKthuc; }

    public String getLoaiSP() { return loaiSP; }
    public void setLoaiSP(String loaiSP) { this.loaiSP = loaiSP; }

    public double getGiamGia() { return giamGia; }
    public void setGiamGia(double giamGia) { this.giamGia = giamGia; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}