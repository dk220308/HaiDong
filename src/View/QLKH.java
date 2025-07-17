/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package View;

import DAO.KhachHangDAO;
import DAO.HoaDonDAO; // Cần import HoaDonDAO để lấy lịch sử giao dịch
import Model.KhachHang;
import Model.HoaDon; // Cần import HoaDon model để hiển thị lịch sử giao dịch
import java.sql.SQLException;
import java.time.format.DateTimeFormatter; // Để format ngày tháng cho lịch sử giao dịch
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JFrame; // Có thể không cần nếu QLKH là JPanel
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class QLKH extends javax.swing.JPanel {

    DefaultTableModel modelKhachHang;
    DefaultTableModel modelLichSuGiaoDich; // Model cho bảng lịch sử giao dịch

    private KhachHangDAO khDAO = new KhachHangDAO();
    private HoaDonDAO hdDAO = new HoaDonDAO(); // Khởi tạo HoaDonDAO

    private List<KhachHang> currentKhachHangList; // List lưu trữ khách hàng hiện tại (sau lọc/tìm kiếm)

    // Định dạng ngày tháng UI cho lịch sử giao dịch (nếu cần)
    private static final DateTimeFormatter UI_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public QLKH() {
        initComponents(); // Khởi tạo các components của NetBeans
        setupTables();    // Cài đặt cấu trúc cột cho các bảng
        loadKhachHangData(); // Tải dữ liệu khách hàng ban đầu

        // Thêm Listener cho ComboBox lọc
        jComboBox3.addActionListener(e -> filter()); // Giới tính
        jComboBox4.addActionListener(e -> filter()); // Trạng thái

        // Thêm listener cho TabbedPane để tải dữ liệu lịch sử giao dịch
        jTabbedPane1.addChangeListener(e -> {
            if (jTabbedPane1.getSelectedIndex() == 1) { // Tab "Lịch sử giao dịch"
                displayLichSuGiaoDich();
            }
        });
    }

    private void setupTables() {
        // --- SỬA Ở ĐÂY CHO modelKhachHang ---
        modelKhachHang = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Đặt tất cả các ô không thể chỉnh sửa trực tiếp trên bảng
            }
        };
        modelKhachHang.addColumn("Mã KH");
        modelKhachHang.addColumn("Tên KH");
        modelKhachHang.addColumn("Giới tính");
        modelKhachHang.addColumn("SĐT");
        modelKhachHang.addColumn("Trạng thái");
        modelKhachHang.addColumn("Địa chỉ");
        jTable2.setModel(modelKhachHang); // Gán model mới cho jTable2

        // --- Phần này của modelLichSuGiaoDich đã đúng (hoặc bạn có thể thêm lại nếu bị lỗi) ---
        modelLichSuGiaoDich = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelLichSuGiaoDich.addColumn("Mã HĐ");
        modelLichSuGiaoDich.addColumn("Ngày Tạo");
        modelLichSuGiaoDich.addColumn("Tổng Tiền");
        modelLichSuGiaoDich.addColumn("Trạng Thái HĐ"); // Lưu ý: trong code trước của tôi là modelLichSuGiaoDuyich, hãy sửa lại thành modelLichSuGiaoDich nếu bạn đã copy đúng.
        jTable3.setModel(modelLichSuGiaoDich);
    }

    public void fillToTable(List<KhachHang> list) {
        modelKhachHang.setRowCount(0); // Xóa dữ liệu cũ

        if (list == null || list.isEmpty()) {
            return;
        }

        for (KhachHang kh : list) {
            modelKhachHang.addRow(new Object[]{
                kh.getMaKH(),
                kh.getTenKH(),
                kh.getGioiTinh(),
                kh.getSdt(),
                kh.getTrangThai(),
                kh.getDiaChi()
            });
        }
    }

    // Phương thức lấy dữ liệu từ Form để tạo đối tượng KhachHang
    private KhachHang getForm() {
        String ma = TF_MaKH.getText().trim();
        String ten = TF_TenKH.getText().trim();
        String sdt = TF_SDT.getText().trim();
        String diachi = TA_diachi.getText().trim();
        String gt = rdonam.isSelected() ? "Nam" : "Nữ";
        String trangthai = rdokhachquen.isSelected() ? "Khách quen" : (rdokhachmoi.isSelected() ? "Khách mới" : "");
        return new KhachHang(ma, ten, gt, sdt, trangthai, diachi);
    }

    // Phương thức đặt dữ liệu từ đối tượng KhachHang lên Form
    private void setForm(KhachHang kh) {
        TF_MaKH.setText(kh.getMaKH());
        TF_TenKH.setText(kh.getTenKH());
        TF_SDT.setText(kh.getSdt());
        TA_diachi.setText(kh.getDiaChi());
        if (kh.getGioiTinh() != null) {
            if (kh.getGioiTinh().equalsIgnoreCase("Nam")) {
                rdonam.setSelected(true);
            } else if (kh.getGioiTinh().equalsIgnoreCase("Nữ")) {
                rdonu.setSelected(true);
            } else {
                buttonGroup1.clearSelection(); // Clear if gender is not Nam/Nữ
            }
        } else {
            buttonGroup1.clearSelection();
        }

        if (kh.getTrangThai() != null) {
            if (kh.getTrangThai().equalsIgnoreCase("Khách quen")) {
                rdokhachquen.setSelected(true);
            } else if (kh.getTrangThai().equalsIgnoreCase("Khách mới")) {
                rdokhachmoi.setSelected(true);
            } else {
                buttonGroup2.clearSelection(); // Clear if status is not Khách quen/Khách mới
            }
        } else {
            buttonGroup2.clearSelection();
        }

        if (kh.getTrangThai() != null && kh.getTrangThai().equalsIgnoreCase("Khóa")) {
            toggleInputFields(false); // Vô hiệu hóa tất cả các trường và nút chỉnh sửa/thêm/xóa
            jButton17.setEnabled(false); // Nút "Khóa" bị vô hiệu hóa vì đã khóa rồi
            jButton18.setEnabled(true);  // Nút "Mở khóa" được kích hoạt để cho phép mở khóa

            // Các nút tìm kiếm, làm mới, lọc vẫn hoạt động
            BT_timkh.setEnabled(true);
            TF_MaKH1.setEnabled(true);
            jButton14.setEnabled(true);
            jComboBox3.setEnabled(true);
            jComboBox4.setEnabled(true);

        } else { // Nếu khách hàng KHÔNG bị Khóa
            toggleInputFields(true);  // Kích hoạt tất cả các trường và nút chỉnh sửa/thêm/xóa
            jButton17.setEnabled(true);  // Nút "Khóa" được kích hoạt
            jButton18.setEnabled(false); // Nút "Mở khóa" bị vô hiệu hóa

            // Các nút tìm kiếm, làm mới, lọc vẫn hoạt động
            BT_timkh.setEnabled(true);
            TF_MaKH1.setEnabled(true);
            jButton14.setEnabled(true);
            jComboBox3.setEnabled(true);
            jComboBox4.setEnabled(true);
        }
    }

    // Phương thức xóa trắng Form và bỏ chọn hàng trong bảng
    private void clearForm() {
        TF_MaKH.setText("");
        TF_TenKH.setText("");
        TF_SDT.setText("");
        TA_diachi.setText("");
        buttonGroup1.clearSelection();
        buttonGroup2.clearSelection();
        jTable2.clearSelection(); // Bỏ chọn hàng trong bảng khách hàng
        modelLichSuGiaoDich.setRowCount(0); // Xóa dữ liệu lịch sử giao dịch khi clear form

        toggleInputFields(true); // Kích hoạt lại tất cả các trường và nút chỉnh sửa/thêm/xóa
        jButton17.setEnabled(true);  // Nút "Khóa" được kích hoạt
        jButton18.setEnabled(false); // Nút "Mở khóa" bị vô hiệu hóa (vì không có khách hàng nào được chọn/khóa)
        // --- Kết thúc phần thêm mới/chỉnh sửa ---
    }

    private void toggleInputFields(boolean enable) {
        TF_MaKH.setEnabled(enable);
        TF_TenKH.setEnabled(enable);
        TF_SDT.setEnabled(enable);
        TA_diachi.setEnabled(enable); // Đảm bảo TA_diachi là tên biến của JTextArea Địa chỉ
        rdonam.setEnabled(enable); // Đảm bảo rdonam là tên biến của JRadioButton Nam
        rdonu.setEnabled(enable);   // Đảm bảo rdonu là tên biến của JRadioButton Nữ
        rdokhachquen.setEnabled(enable); // Đảm bảo rdokhachquen là tên biến của JRadioButton Khách quen
        rdokhachmoi.setEnabled(enable);  // Đảm bảo rdokhachmoi là tên biến của JRadioButton Khách mới

        jButton13.setEnabled(enable); // Nút "Sửa"
        jButton15.setEnabled(enable); // Nút "Thêm"
        jButton16.setEnabled(enable); // Nút "Xóa"
        // Nút "Khóa" (jButton17) và "Mở khóa" (jButton18) sẽ được xử lý riêng trong setForm
        // Các nút tìm kiếm, làm mới, lọc sẽ luôn hoạt động
        BT_timkh.setEnabled(true); // Nút Tìm kiếm (theo SĐT ở trên)
        TF_MaKH1.setEnabled(true); // Trường SĐT cho tìm kiếm
        jButton14.setEnabled(true); // Nút "Làm mới" (hoặc tên tương tự)
        jComboBox3.setEnabled(true); // Lọc Giới tính
        jComboBox4.setEnabled(true); // Lọc Trạng thái
    }

    // Phương thức tải dữ liệu khách hàng từ DAO và đổ vào bảng
    private void loadKhachHangData() {
        try {
            currentKhachHangList = khDAO.getAllKhachHang();
            fillToTable(currentKhachHangList);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu khách hàng: " + e.getMessage(), "Lỗi DB", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Phương thức lọc khách hàng theo giới tính và trạng thái
    private void filter() {
        String gt = jComboBox3.getSelectedItem().toString().trim();
        String tt = jComboBox4.getSelectedItem().toString().trim();

        try {
            List<KhachHang> allCustomers = khDAO.getAllKhachHang(); // Lấy lại danh sách đầy đủ
            List<KhachHang> filteredList = allCustomers.stream()
                    .filter(kh -> (gt.equals("ALL") || (kh.getGioiTinh() != null && kh.getGioiTinh().equalsIgnoreCase(gt))))
                    .filter(kh -> (tt.equals("ALL") || (kh.getTrangThai() != null && kh.getTrangThai().equalsIgnoreCase(tt))))
                    .collect(Collectors.toList());
            fillToTable(filteredList);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lọc dữ liệu khách hàng: " + e.getMessage(), "Lỗi DB", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Phương thức hiển thị lịch sử giao dịch của khách hàng được chọn
    private void displayLichSuGiaoDich() {
        modelLichSuGiaoDich.setRowCount(0); // Xóa dữ liệu cũ
        int selectedRow = jTable2.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng để xem lịch sử giao dịch.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String sdtKhachHang = (String) modelKhachHang.getValueAt(selectedRow, 3); // Lấy SDT từ bảng khách hàng
        if (sdtKhachHang == null || sdtKhachHang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy số điện thoại của khách hàng được chọn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            List<HoaDon> hoaDonList = hdDAO.getHoaDonsBySdt(sdtKhachHang);
            if (hoaDonList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Khách hàng này chưa có giao dịch nào.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (HoaDon hd : hoaDonList) {
                    modelLichSuGiaoDich.addRow(new Object[]{
                        hd.getMahd(),
                        hd.getNgayTao() != null ? hd.getNgayTao().format(UI_DATE_FORMATTER) : "", // Format LocalDate
                        String.format("%,.0f", hd.getTongTien()), // Format tiền tệ
                        hd.getTrangThai()
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải lịch sử giao dịch: " + e.getMessage(), "Lỗi DB", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private int findRowBySdt(String sdt) {
        for (int i = 0; i < modelKhachHang.getRowCount(); i++) {
            if (sdt.equals(modelKhachHang.getValueAt(i, 3))) { // Cột 3 là SĐT, nếu cột SĐT của bạn ở vị trí khác, hãy đổi số 3
                return i;
            }
        }
        return -1;
    }

    private void performStatusCheckForSelectedCustomer() {
        int selectedRow = jTable2.getSelectedRow();
        if (selectedRow < 0) { // Nếu không có hàng nào được chọn
            return;
        }

        String sdt = (String) modelKhachHang.getValueAt(selectedRow, 3); // Lấy SĐT từ bảng
        if (sdt == null || sdt.isEmpty()) {
            return;
        }

        try {
            KhachHang kh = khDAO.getKhachHangBySdt(sdt); // Lấy thông tin khách hàng từ DB
            if (kh != null && kh.getTrangThai().equalsIgnoreCase("Khách mới")) {
                int soLuongHoaDon = khDAO.getSoLuongHoaDonBySdt(sdt); // Đếm số hóa đơn của KH
                if (soLuongHoaDon >= 5) {
                    boolean updated = khDAO.updateTrangThaiKhachHang(sdt, "Khách quen");
                    if (updated) {
                        JOptionPane.showMessageDialog(this, "Khách hàng " + kh.getTenKH() + " (" + sdt + ") đã trở thành Khách quen!", "Cập nhật trạng thái", JOptionPane.INFORMATION_MESSAGE);
                        loadKhachHangData(); // Tải lại bảng để hiển thị trạng thái mới
                        // Chọn lại hàng và cập nhật form để hiển thị trạng thái mới ngay lập tức
                        int newSelectedRow = findRowBySdt(sdt);
                        if (newSelectedRow != -1) {
                            jTable2.setRowSelectionInterval(newSelectedRow, newSelectedRow);
                            KhachHang updatedKh = khDAO.getKhachHangBySdt(sdt);
                            if (updatedKh != null) {
                                setForm(updatedKh); // Cập nhật form
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra và cập nhật trạng thái khách hàng: " + e.getMessage(), "Lỗi DB", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
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
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        TF_TenKH = new javax.swing.JTextField();
        TF_MaKH = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        TF_SDT = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        rdonam = new javax.swing.JRadioButton();
        rdonu = new javax.swing.JRadioButton();
        rdokhachquen = new javax.swing.JRadioButton();
        rdokhachmoi = new javax.swing.JRadioButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        TA_diachi = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        TF_MaKH1 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        BT_timkh = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jButton18 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("Quản lý khách hàng");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Quản lý khách hàng"));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel13.setText("Mã khách hàng :");

        TF_TenKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_TenKHActionPerformed(evt);
            }
        });

        TF_MaKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_MaKHActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel14.setText("Số điện thoại :");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel21.setText("Tên khách hàng :");

        TF_SDT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_SDTActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel22.setText("Địa chỉ :");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel23.setText("Giới tính :");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel24.setText("Trạng thái :");

        buttonGroup1.add(rdonam);
        rdonam.setText("Nam");
        rdonam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdonamActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdonu);
        rdonu.setText("Nữ");

        buttonGroup2.add(rdokhachquen);
        rdokhachquen.setText("Khách quen");

        buttonGroup2.add(rdokhachmoi);
        rdokhachmoi.setText("Khách mới");

        TA_diachi.setColumns(20);
        TA_diachi.setRows(5);
        jScrollPane2.setViewportView(TA_diachi);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Tìm kiếm"));

        TF_MaKH1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_MaKH1ActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel25.setText("Số điện thoại :");

        BT_timkh.setText("Tìm");
        BT_timkh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_timkhActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TF_MaKH1, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BT_timkh, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(140, 140, 140))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TF_MaKH1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addGap(18, 18, 18)
                .addComponent(BT_timkh, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jButton13.setText("Sửa");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setText("Làm mới");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setText("Thêm");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setText("Xoá");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton17.setText("Khoá");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Lọc"));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel26.setText("Lọc theo giới tính :");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel27.setText("Lọc theo giới tính :");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ALL", "Nam ", "Nữ" }));

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ALL", "Khách quen", "Khách mới", " " }));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47))
        );

        jButton18.setText("Mở khoá");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel14)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel13)
                                        .addComponent(jLabel21)))
                                .addGap(48, 48, 48)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(TF_MaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(TF_TenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(TF_SDT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(57, 57, 57)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel22)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(65, 65, 65)
                                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(59, 59, 59)
                                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)
                                .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(64, 64, 64)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rdonam)
                                    .addComponent(rdokhachquen))
                                .addGap(36, 36, 36)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rdokhachmoi)
                                    .addComponent(rdonu)))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(132, 132, 132)
                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57)
                        .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(213, 213, 213)
                        .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(TF_MaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23)
                            .addComponent(rdonam)
                            .addComponent(rdonu))
                        .addGap(46, 46, 46)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(TF_TenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24)
                            .addComponent(rdokhachquen)
                            .addComponent(rdokhachmoi)))
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel14)
                                    .addComponent(TF_SDT, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel22))
                                .addGap(42, 42, 42)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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
        jScrollPane3.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Xem thông tin", jPanel8);

        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTable3);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Lịch sử giao dịch", jPanel9);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(567, 567, 567))
            .addGroup(layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTabbedPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(79, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void TF_TenKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_TenKHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TF_TenKHActionPerformed

    private void TF_MaKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_MaKHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TF_MaKHActionPerformed

    private void TF_SDTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_SDTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TF_SDTActionPerformed

    private void rdonamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdonamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdonamActionPerformed

    private void TF_MaKH1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_MaKH1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TF_MaKH1ActionPerformed

    private void BT_timkhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_timkhActionPerformed
        // TODO add your handling code here:
        String sdt = TF_MaKH1.getText().trim();
        if (sdt.isEmpty()) {
            loadKhachHangData(); // Nếu ô tìm kiếm trống, hiển thị tất cả
            return;
        }
        try {
            List<KhachHang> list = khDAO.searchBySDT(sdt);
            fillToTable(list);
            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng nào với SĐT này.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm khách hàng: " + e.getMessage(), "Lỗi DB", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_BT_timkhActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        KhachHang kh = getForm();
        if (kh.getSdt().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập SĐT khách hàng để cập nhật.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            // Kiểm tra xem khách hàng có tồn tại trước khi cập nhật
            if (khDAO.getKhachHangBySdt(kh.getSdt()) == null) {
                JOptionPane.showMessageDialog(this, "Khách hàng với SĐT này không tồn tại. Vui lòng thêm mới hoặc kiểm tra lại SĐT.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (khDAO.updateKhachHang(kh)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadKhachHangData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại! Vui lòng kiểm tra dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật khách hàng: " + e.getMessage(), "Lỗi DB", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable2.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần mở khóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sdtToUnlock = (String) modelKhachHang.getValueAt(selectedRow, 3); // Lấy SDT từ cột thứ 4 (index 3)
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn mở khóa khách hàng có SĐT " + sdtToUnlock + " không?", "Xác nhận mở khóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Mặc định về "Khách quen" khi mở khóa, hoặc "Khách mới" tùy theo logic nghiệp vụ của bạn
                if (khDAO.updateTrangThaiKhachHang(sdtToUnlock, "Khách quen")) {
                    JOptionPane.showMessageDialog(this, "Mở khóa khách hàng thành công!");
                    loadKhachHangData();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Mở khóa khách hàng thất bại! Khách hàng không tồn tại hoặc trạng thái không thể thay đổi.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi mở khóa khách hàng: " + e.getMessage(), "Lỗi DB", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Đặt lại trạng thái mong muốn sau khi mở khóa, ví dụ "Khách quen"
                if (khDAO.updateTrangThaiKhachHang(sdtToUnlock, "Khách quen")) {
                    JOptionPane.showMessageDialog(this, "Mở khóa khách hàng thành công!");
                    loadKhachHangData(); // Tải lại dữ liệu để cập nhật bảng

                    // Cập nhật lại trạng thái của form nếu khách hàng đó vẫn đang được chọn
                    int newSelectedRow = findRowBySdt(sdtToUnlock);
                    if (newSelectedRow != -1) {
                        jTable2.setRowSelectionInterval(newSelectedRow, newSelectedRow);
                        KhachHang khUnlocked = khDAO.getKhachHangBySdt(sdtToUnlock);
                        if (khUnlocked != null) {
                            setForm(khUnlocked); // Cập nhật UI theo trạng thái "Khách quen"
                        }
                    } else {
                        clearForm();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Mở khóa khách hàng thất bại! Khách hàng không tồn tại hoặc trạng thái không thể thay đổi.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi mở khóa khách hàng: " + e.getMessage(), "Lỗi DB", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
        int row = jTable2.getSelectedRow();
        if (row >= 0) {
            String sdt = (String) modelKhachHang.getValueAt(row, 3);
            try {
                KhachHang kh = khDAO.getKhachHangBySdt(sdt);
                if (kh != null) {
                    setForm(kh);
                    // --- Gọi phương thức kiểm tra trạng thái ở đây ---
                    performStatusCheckForSelectedCustomer(); // Kiểm tra và cập nhật trạng thái
                    // --- Kết thúc ---

                    if (jTabbedPane1.getSelectedIndex() == 1) {
                        displayLichSuGiaoDich();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin khách hàng chi tiết.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải thông tin khách hàng: " + e.getMessage(), "Lỗi DB", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
        KhachHang kh = getForm();
        if (kh.getMaKH().isEmpty() || kh.getTenKH().isEmpty() || kh.getSdt().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã KH, Tên KH và SĐT không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            // Kiểm tra SĐT đã tồn tại chưa vì SĐT là PK
            if (khDAO.getKhachHangBySdt(kh.getSdt()) != null) {
                JOptionPane.showMessageDialog(this, "Số điện thoại này đã tồn tại trong hệ thống.", "Lỗi Trùng Lặp", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (khDAO.insertKhachHang(kh)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                loadKhachHangData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại! Vui lòng kiểm tra dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm khách hàng: " + e.getMessage(), "Lỗi DB", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
        String sdt = TF_SDT.getText().trim(); // Lấy SDT từ form
        if (sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xoá hoặc nhập SĐT.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xoá khách hàng có SĐT " + sdt + " không?", "Xác nhận xoá", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (khDAO.deleteKhachHang(sdt)) { // Xóa theo SDT
                    JOptionPane.showMessageDialog(this, "Xoá thành công!");
                    loadKhachHangData();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Xoá thất bại! Khách hàng không tồn tại hoặc có ràng buộc dữ liệu (ví dụ: hóa đơn liên quan).", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi xoá khách hàng: " + e.getMessage(), "Lỗi DB", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable2.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần khóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sdtToLock = (String) modelKhachHang.getValueAt(selectedRow, 3); // Lấy SDT từ cột thứ 4 (index 3)
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn khóa khách hàng có SĐT " + sdtToLock + " không?", "Xác nhận khóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Sử dụng phương thức updateTrangThaiKhachHang mới
                if (khDAO.updateTrangThaiKhachHang(sdtToLock, "Khóa")) {
                    JOptionPane.showMessageDialog(this, "Khóa khách hàng thành công!");
                    loadKhachHangData();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Khóa khách hàng thất bại! Khách hàng không tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khóa khách hàng: " + e.getMessage(), "Lỗi DB", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (khDAO.updateTrangThaiKhachHang(sdtToLock, "Khóa")) {
                    JOptionPane.showMessageDialog(this, "Khóa khách hàng thành công!");
                    loadKhachHangData(); // Tải lại dữ liệu để cập nhật bảng

                    // Cập nhật lại trạng thái của form nếu khách hàng đó vẫn đang được chọn
                    int newSelectedRow = findRowBySdt(sdtToLock); // Tìm hàng mới của khách hàng sau khi load lại
                    if (newSelectedRow != -1) {
                        jTable2.setRowSelectionInterval(newSelectedRow, newSelectedRow); // Chọn lại hàng
                        // Tự động gọi jTable2MouseClicked hoặc gọi setForm trực tiếp
                        KhachHang khLocked = khDAO.getKhachHangBySdt(sdtToLock);
                        if (khLocked != null) {
                            setForm(khLocked); // Cập nhật UI theo trạng thái "Khóa"
                        }
                    } else {
                        clearForm(); // Nếu không tìm thấy, làm sạch form
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Khóa khách hàng thất bại! Khách hàng không tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khóa khách hàng: " + e.getMessage(), "Lỗi DB", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
        clearForm();
        loadKhachHangData();
        // Sau khi làm mới, nếu đang ở tab lịch sử giao dịch thì cũng cần xóa dữ liệu cũ đi
        modelLichSuGiaoDich.setRowCount(0);
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1MouseClicked

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable3MouseClicked

//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//     * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(QLKH.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(QLKH.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(QLKH.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(QLKH.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                // Tạo một JFrame để chứa QLKH JPanel
//                JFrame frame = new JFrame("Quản lý Khách hàng - Test");
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                frame.add(new QLKH()); // Thêm JPanel QLKH vào frame
//                frame.pack();
//                frame.setLocationRelativeTo(null);
//                frame.setVisible(true);
//            }
//        });
//    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BT_timkh;
    private javax.swing.JTextArea TA_diachi;
    private javax.swing.JTextField TF_MaKH;
    private javax.swing.JTextField TF_MaKH1;
    private javax.swing.JTextField TF_SDT;
    private javax.swing.JTextField TF_TenKH;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JRadioButton rdokhachmoi;
    private javax.swing.JRadioButton rdokhachquen;
    private javax.swing.JRadioButton rdonam;
    private javax.swing.JRadioButton rdonu;
    // End of variables declaration//GEN-END:variables
}
