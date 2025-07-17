/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package View;

import DAO.KhuyenMaiDAO;
import DAO.SanPhamDAO;
import Model.KhuyenMai;
import Model.SanPham;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author XPS
 */
public class QLKM extends javax.swing.JPanel {

    DefaultTableModel tableModelKM;  // cho jTable2 (Khuyến mãi)
    DefaultTableModel tableModelSP;  // cho jTable1 (Sản phẩm)

    KhuyenMaiDAO kmdao = new KhuyenMaiDAO();
    SanPhamDAO spdao = new SanPhamDAO();

    /**
     * Creates new form QLKM
     */
    public QLKM() {
        initComponents();
//        initTableKhuyenMai();
//        initTableSanPham();
//        fillTableKhuyenMai();
//        fillTableSanPham();
//    }
//
//    public void initTableKhuyenMai() {
//        String[] cols = new String[]{
//            "Mã KM", "Mã SP", "Tên KM", "Ngày Bắt đầu", "Ngày Kết thúc", "Loại SP", "Giảm giá", "Trạng thái"
//        };
//        tableModelKM = new DefaultTableModel();
//        tableModelKM.setColumnIdentifiers(cols);
//        jTable2.setModel(tableModelKM);
//    }

//    public void initTableSanPham() {
//        String[] cols = new String[]{"Mã SP", "Tên SP", "Loại SP", "Giá", "Số Lượng", "Màu Sắc", "Kích Thước", "Chất Liệu", "Trạng thái"};
//        tableModelSP = new DefaultTableModel();
//        tableModelSP.setColumnIdentifiers(cols);
//        jTable1.setModel(tableModelSP);
//    }

//    public void fillTableKhuyenMai() {
//        tableModelKM.setRowCount(0);
//        for (KhuyenMai km : kmdao.getAllKM()) {
//            tableModelKM.addRow(kmdao.getRowKM(km));
//        }
//    }
//
//    public void fillTableSanPham() {
//        tableModelSP.setRowCount(0);
//        for (SanPham sp : spdao.getAll()) {
//            tableModelSP.addRow(spdao.getRow(sp));
//        }
//    }
//
////    private boolean validateFormKhuyenMai() {
//        String maKM = TF_MaKM.getText();
//        if (maKM == null || !maKM.trim().startsWith("KM")) {
//            JOptionPane.showMessageDialog(this, "Mã khuyến mãi phải bắt đầu bằng 'KM'.");
//            TF_MaKM.requestFocus();
//            return false;
//        }
//
//        if (TF_TenKM.getText().trim().isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khuyến mãi.");
//            return false;
//        }
//
//        if (TF_NgayBD.getText().trim().isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Vui lòng nhập ngày bắt đầu.");
//            return false;
//        }
//
//        if (TF_NgayKT.getText().trim().isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Vui lòng nhập ngày kết thúc.");
//            return false;
//        }
//
//        if (TF_GiamGia.getText().trim().isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Vui lòng nhập giảm giá.");
//            return false;
//        }
//
//        try {
//            float gg = Float.parseFloat(TF_GiamGia.getText().trim());
//            if (gg < 0 || gg > 1) {
//                JOptionPane.showMessageDialog(this, "Giảm giá phải từ 0 đến 1.");
//                return false;
//            }
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(this, "Giảm giá phải là số hợp lệ.");
//            return false;
//        }
//
//        if (jComboBox2.getSelectedItem() == null || jComboBox2.getSelectedItem().toString().trim().isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Vui lòng chọn trạng thái.");
//            return false;
//        }
//
//        return true;
//    }

//    public void showDetailKhuyenMai() {
//        int i = jTable2.getSelectedRow();
//        if (i >= 0) {
//            KhuyenMai km = kmdao.getAllKM().get(i);
//
//            TF_MaKM.setText(km.getMaKM());
//            TF_TenKM.setText(km.getTenKm());
//            TF_NgayBD.setText(km.getNgayBdau());
//            TF_NgayKT.setText(km.getNgayKthuc());
//            TF_GiamGia.setText(String.valueOf(km.getGiamgia())); // ✅ đã sửa
//            jComboBox2.setSelectedItem(km.getTrangThai());
//        }
//    }

//    public void capNhatKhuyenMai() {
//        String maKM = TF_MaKM.getText().trim();
//        String tenKm = TF_TenKM.getText().trim();
//        String ngayBd = TF_NgayBD.getText().trim();
//        String ngayKt = TF_NgayKT.getText().trim();
//        String loaiSP = TF_LoaiSP.getText().trim();
//
//        String trangThai = jComboBox2.getSelectedItem().toString().trim();
//
//        float giamGia = 0;
//        try {
//            giamGia = Float.parseFloat(TF_GiamGia.getText().trim());
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(this, "Giảm giá phải là số hợp lệ.");
//            return;
//        }
//
//        if (maKM.isEmpty() || tenKm.isEmpty() || ngayBd.isEmpty() || ngayKt.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.");
//            return;
//        }
//
//        KhuyenMai km = new KhuyenMai(maKM, "", tenKm, ngayBd, ngayKt, loaiSP, giamGia, trangThai);
//
//        int result = kmdao.capNhatKhuyenMai(km);
//        if (result == 1) {
//            fillTableKhuyenMai();
//            JOptionPane.showMessageDialog(this, "Cập nhật khuyến mãi thành công!");
//        } else {
//            JOptionPane.showMessageDialog(this, "Cập nhật khuyến mãi thất bại!");
//        }
//    }

//    public void xoaKhuyenMai() {
//        int i = jTable2.getSelectedRow();
//        if (i >= 0) {
//            int chon = JOptionPane.showConfirmDialog(this, "Bạn có thực sự muốn xoá khuyến mãi này?",
//                    "Xác nhận", JOptionPane.YES_NO_OPTION);
//
//            if (chon == JOptionPane.YES_OPTION) {
//                String maKM = jTable2.getValueAt(i, 0).toString();
//
//                int result = kmdao.xoaKhuyenMai(maKM);
//                if (result == 1) {
//                    fillTableKhuyenMai();
//                    JOptionPane.showMessageDialog(this, "Xoá khuyến mãi thành công!");
//                } else {
//                    JOptionPane.showMessageDialog(this, "Xoá khuyến mãi thất bại!");
//                }
//            }
//        } else {
//            JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi để xoá.");
//        }
//    }
//
//    public void luuKhuyenMai() {
//        int selectedRow = jTable1.getSelectedRow();
//        if (selectedRow < 0) {
//            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm áp dụng khuyến mãi.");
//            return;
//        }
//
//
//        String maSP = jTable1.getValueAt(selectedRow, 0).toString();
//
//        String maKM = TF_MaKM.getText().trim();
//        String tenKm = TF_TenKM.getText().trim();
//        String ngayBd = TF_NgayBD.getText().trim();
//        String ngayKt = TF_NgayKT.getText().trim();
//        String loaiSP = TF_LoaiSP.getText().trim();
//        String trangThai = jComboBox2.getSelectedItem().toString().trim();
//
//        float giamGia = 0;
//        try {
//            giamGia = Float.parseFloat(TF_GiamGia.getText().trim());
//        } catch (NumberFormatException ex) {
//            JOptionPane.showMessageDialog(this, "Giảm giá phải là số hợp lệ.");
//            return;
//        }
//
//  
//        KhuyenMai km = new KhuyenMai(maKM, maSP, tenKm, ngayBd, ngayKt, loaiSP, giamGia, trangThai);
//
//        int result = kmdao.luuKhuyenMai(km);
//        if (result == 1) {
//            fillTableKhuyenMai(); // Load lại bảng
//            JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thành công");
//        } else {
//            JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thất bại");
//        }
//    }

//    public void lamMoiKhuyenMai() {
        TF_MaKM.setText("");
        TF_TenKM.setText("");
        TF_NgayBD.setText("");
        TF_NgayKT.setText("");
        TF_GiamGia.setText("");
        jComboBox2.setSelectedIndex(0); // hoặc set lại về "Đang hoạt động" nếu cần cụ thể

        // Xóa lựa chọn trong bảng (nếu muốn)
        jTable2.clearSelection();

        // Nếu có lọc/tìm kiếm, reset lại filter ở đây (nếu dùng)
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        TF_LoaiSP = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        TF_MaKM = new javax.swing.JTextField();
        TF_TenKM = new javax.swing.JTextField();
        TF_NgayBD = new javax.swing.JTextField();
        TF_NgayKT = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        TF_GiamGia = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("Khuyến mãi");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Danh sách sản phẩm"));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Áp dụng cho :");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Hình thức :");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả sản phẩm", "Theo khoảng giá" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 815, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TF_LoaiSP, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(TF_LoaiSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 26, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Khuyến mãi"));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel6.setText("Tên khuyến mãi :");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel7.setText("Mã khuyến mãi :");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel8.setText("Ngày bắt đầu :");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel9.setText("Ngày kết thúc :");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel10.setText("Trạng thái  :");

        TF_MaKM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_MaKMActionPerformed(evt);
            }
        });

        TF_TenKM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_TenKMActionPerformed(evt);
            }
        });

        TF_NgayBD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_NgayBDActionPerformed(evt);
            }
        });

        TF_NgayKT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_NgayKTActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đang hoạt động", "Ngừng hoạt động", "Sắp hoạt động" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jButton1.setText("Lưu");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jButton2.setText("Cập nhật");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jButton3.setText("Xoá");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jButton4.setText("Làm mới");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel11.setText("Giảm giá :");

        TF_GiamGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_GiamGiaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(TF_MaKM, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 1, Short.MAX_VALUE))
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(TF_GiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TF_TenKM, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TF_NgayBD, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TF_NgayKT, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(49, 49, 49)
                    .addComponent(jLabel7)
                    .addContainerGap(318, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(TF_MaKM, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(TF_TenKM, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TF_NgayBD, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TF_NgayKT, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TF_GiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(57, 57, 57)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(665, Short.MAX_VALUE)))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Danh sách khuyến mãi"));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 815, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(51, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(625, 625, 625)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void TF_MaKMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_MaKMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TF_MaKMActionPerformed

    private void TF_TenKMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_TenKMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TF_TenKMActionPerformed

    private void TF_NgayBDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_NgayBDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TF_NgayBDActionPerformed

    private void TF_NgayKTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_NgayKTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TF_NgayKTActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
//        lamMoiKhuyenMai();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void TF_GiamGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_GiamGiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TF_GiamGiaActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
//        showDetailKhuyenMai();

    }//GEN-LAST:event_jTable2MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
//        if (validateFormKhuyenMai()) {
//            capNhatKhuyenMai();
//        }
        return;
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
//        if (validateFormKhuyenMai()) {
////            xoaKhuyenMai();
//        }
//        return;
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
//        if (validateFormKhuyenMai()) {
////            luuKhuyenMai();
//        }
//        return;
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField TF_GiamGia;
    private javax.swing.JTextField TF_LoaiSP;
    private javax.swing.JTextField TF_MaKM;
    private javax.swing.JTextField TF_NgayBD;
    private javax.swing.JTextField TF_NgayKT;
    private javax.swing.JTextField TF_TenKM;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
