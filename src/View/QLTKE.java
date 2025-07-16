/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package View;

import DAO.ThongKeDAO;
import Model.HoaDon;
import Model.TopSanPham; // Đảm bảo bạn có class TopSanPham trong Model
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
// import javax.swing.JFrame; // Có thể không cần nếu QLTKE là JPanel

public class QLTKE extends javax.swing.JPanel {

    private ThongKeDAO thongKeDAO;
    private DefaultTableModel hoaDonTableModel;
    private DefaultTableModel topSanPhamTableModel;

    // Định dạng ngày tháng mà bạn mong muốn nhập/hiển thị trên giao diện (ví dụ: "yyyy-MM-dd")
    private static final DateTimeFormatter UI_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Constructor của QLTKE
    public QLTKE() {
        initComponents(); // Khởi tạo các thành phần giao diện (do NetBeans tạo)
        thongKeDAO = new ThongKeDAO();
        setupTables(); // Cài đặt cấu trúc cột cho các bảng
        initData();    // Khởi tạo dữ liệu ban đầu cho các trường và thống kê
        
        // Load dữ liệu cho tab được chọn ban đầu (thường là tab đầu tiên)
        loadDataForSelectedTab(); 

        // Thêm listener để tải lại dữ liệu khi chuyển đổi tab
        jTabbedPane2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                loadDataForSelectedTab();
            }
        });
    }

    private void updateGeneralStatistics() {
        try {
            double totalRevenue = thongKeDAO.getTongDoanhThu();
            jLabel_DoanhThu.setText(String.format("%,.0f VNĐ", totalRevenue));

            int totalOrders = thongKeDAO.getTongDonHang();
            jLabel_DonHang.setText(String.valueOf(totalOrders));

            int totalStock = thongKeDAO.getTongSoLuongTonKho();
            jLabel_TonKho.setText(String.valueOf(totalStock));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật số liệu thống kê chung: " + e.getMessage(), "Lỗi DB", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void initData() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        // Đặt giá trị mặc định cho ô tìm kiếm theo khoảng ngày
        TF_MaKH1.setText(firstDayOfMonth.format(UI_DATE_FORMATTER)); // Giả định TF_MaKH1 là trường "Ngày bắt đầu"
        TF_MaKH3.setText(lastDayOfMonth.format(UI_DATE_FORMATTER));  // Giả định TF_MaKH3 là trường "Ngày kết thúc"

        // Đặt giá trị mặc định cho ComboBox tháng/năm (cho Top 5 sản phẩm)
        // Đảm bảo jComboBox1 là tháng và jComboBox2 là năm
        jComboBox1.setSelectedIndex(today.getMonthValue() - 1); // Tháng hiện tại (index 0-11)
        jComboBox2.setSelectedItem("Năm " + today.getYear()); // Năm hiện tại
    }

    private void setupTables() {
        // Thiết lập bảng "Danh sách hóa đơn"
        hoaDonTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa trực tiếp trên bảng
            }
        };
        hoaDonTableModel.addColumn("Mã HĐ");
        hoaDonTableModel.addColumn("Mã NV");
        hoaDonTableModel.addColumn("Tên KH");
        hoaDonTableModel.addColumn("SĐT");
        hoaDonTableModel.addColumn("Trạng thái");
        hoaDonTableModel.addColumn("Ngày tạo");
        hoaDonTableModel.addColumn("Tổng tiền");
        hoaDonTableModel.addColumn("Tiền trả");
        hoaDonTableModel.addColumn("Tiền thừa");
        hoaDonTableModel.addColumn("Thanh toán");
        hoaDonTableModel.addColumn("Giao hàng");
        hoaDonTableModel.addColumn("Ghi chú");
        jTable2.setModel(hoaDonTableModel); // Giả định jTable2 là bảng hiển thị hóa đơn

        // Thiết lập bảng "Top 5 sản phẩm"
        topSanPhamTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa trực tiếp trên bảng
            }
        };
        topSanPhamTableModel.addColumn("Tên SP");
        topSanPhamTableModel.addColumn("Số lượng bán");
        topSanPhamTableModel.addColumn("Tổng doanh thu");
        jTable1.setModel(topSanPhamTableModel); // Giả định jTable1 là bảng hiển thị Top 5 sản phẩm
    }

    private void fillHoaDonTable(List<HoaDon> list) {
        hoaDonTableModel.setRowCount(0); // Xóa tất cả các hàng hiện có

        if (list == null || list.isEmpty()) {
            // Optional: Hiển thị thông báo nếu không có dữ liệu
            // JOptionPane.showMessageDialog(this, "Không có hóa đơn để hiển thị.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (HoaDon hd : list) {
            hoaDonTableModel.addRow(new Object[]{
                hd.getMahd(),
                hd.getManv(),
                hd.getTenkh(), // Lấy từ thuộc tính tempTenKH đã được set trong DAO
                hd.getSdt(),   // Lấy từ thuộc tính tempSdt đã được set trong DAO
                hd.getTrangThai(),
                // Format LocalDate sang String để hiển thị trên UI
                hd.getNgayTao() != null ? hd.getNgayTao().format(UI_DATE_FORMATTER) : "",
                hd.getTongTien(),
                hd.getTienTra(),
                hd.getTienThua(),
                hd.getThanhToan(),
                hd.getGiaoHang(),
                hd.getGhiChu()
            });
        }
    }

    private void fillTopSanPhamTable(List<TopSanPham> list) {
        topSanPhamTableModel.setRowCount(0); // Xóa tất cả các hàng hiện có

        if (list == null || list.isEmpty()) {
            // Optional: Hiển thị thông báo nếu không có dữ liệu
            // JOptionPane.showMessageDialog(this, "Không có sản phẩm top để hiển thị.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (TopSanPham tsp : list) {
            topSanPhamTableModel.addRow(new Object[]{
                tsp.getTenSanPham(),
                tsp.getTongSoLuongBan(),
                String.format("%,.0f VNĐ", tsp.getTongDoanhThu()) // Định dạng tiền tệ
            });
        }
    }

    private void loadDataForSelectedTab() {
        int selectedIndex = jTabbedPane2.getSelectedIndex(); // Lấy index của tab đang được chọn
        try {
            if (selectedIndex == 0) { // Tab "Danh sách hóa đơn"
                fillHoaDonTable(thongKeDAO.getAllHoaDon());
            } else if (selectedIndex == 1) { // Tab "Top 5 sản phẩm"
                int month = jComboBox1.getSelectedIndex() + 1; // Lấy tháng từ ComboBox (index 0-11 -> tháng 1-12)
                String yearItem = (String) jComboBox2.getSelectedItem(); // Lấy mục năm từ ComboBox
                int year = 0;
                try {
                    year = Integer.parseInt(yearItem.replace("Năm ", "")); // Chuyển đổi "Năm XXXX" thành XXXX
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Lỗi định dạng năm trong ComboBox. Vui lòng kiểm tra lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    year = LocalDate.now().getYear(); // Mặc định về năm hiện tại nếu lỗi
                }

                LocalDate startDate = LocalDate.of(year, month, 1); // Ngày đầu tiên của tháng
                LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth()); // Ngày cuối cùng của tháng
                fillTopSanPhamTable(thongKeDAO.getTop5SanPhamByDateRange(startDate, endDate));
            }
            updateGeneralStatistics(); // Cập nhật thống kê chung mỗi khi tải dữ liệu
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu khi tải dữ liệu cho tab: " + e.getMessage(), "Lỗi DB", JOptionPane.ERROR_MESSAGE);
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

        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel_DoanhThu = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel_DonHang = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel_TonKho = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        TF_MaKH1 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        TF_MaKH3 = new javax.swing.JTextField();
        BT_timkh = new javax.swing.JButton();
        TF_MaKH2 = new javax.swing.JTextField();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        BT_timkh1 = new javax.swing.JButton();

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel8.setText("Thống kê");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setText("00");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(jLabel8))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(jLabel9)))
                .addContainerGap(66, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel8)
                .addGap(63, 63, 63)
                .addComponent(jLabel9)
                .addContainerGap(58, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel2.setText("Thống kê");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setText("Doanh thu");

        jLabel_DoanhThu.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel_DoanhThu.setText("00");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(jLabel1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(jLabel_DoanhThu)))
                .addContainerGap(66, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addGap(63, 63, 63)
                .addComponent(jLabel_DoanhThu)
                .addContainerGap(58, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel4.setText("Đơn hàng");

        jLabel_DonHang.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel_DonHang.setText("00");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(jLabel4))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(jLabel_DonHang)))
                .addContainerGap(80, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addComponent(jLabel_DonHang)
                .addGap(59, 59, 59))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel_TonKho.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel_TonKho.setText("00");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel10.setText("Tồn kho");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(108, 108, 108)
                .addComponent(jLabel_TonKho)
                .addContainerGap(105, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                    .addContainerGap(70, Short.MAX_VALUE)
                    .addComponent(jLabel10)
                    .addGap(56, 56, 56)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addComponent(jLabel_TonKho)
                .addContainerGap(58, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(31, 31, 31)
                    .addComponent(jLabel10)
                    .addContainerGap(143, Short.MAX_VALUE)))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel25.setText("Ngày bắt đầu :");

        TF_MaKH1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_MaKH1ActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel26.setText("Ngày kết thúc :");

        TF_MaKH3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_MaKH3ActionPerformed(evt);
            }
        });

        BT_timkh.setText("Tìm");
        BT_timkh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_timkhActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(jLabel25)
                .addGap(18, 18, 18)
                .addComponent(TF_MaKH1, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(jLabel26)
                .addGap(32, 32, 32)
                .addComponent(TF_MaKH3, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67)
                .addComponent(BT_timkh, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(82, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TF_MaKH1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26)
                    .addComponent(TF_MaKH3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BT_timkh))
                .addGap(15, 15, 15))
        );

        jTabbedPane1.addTab("Tìm kiếm", jPanel10);

        TF_MaKH2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_MaKH2ActionPerformed(evt);
            }
        });

        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Danh sách hoá đơn", jPanel7);

        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel27.setText("Tháng 5 :");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel28.setText("Năm :");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Năm 2022", "Năm 2023", "Năm 2024", "Năm 2025" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        BT_timkh1.setText("Tìm");
        BT_timkh1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_timkh1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1291, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jLabel27)
                .addGap(29, 29, 29)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(111, 111, 111)
                .addComponent(jLabel28)
                .addGap(29, 29, 29)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(BT_timkh1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BT_timkh1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        jTabbedPane2.addTab("Top 5 sản phẩm", jPanel8);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(326, 326, 326))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(120, 120, 120)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane2)
                    .addComponent(jTabbedPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(197, 197, 197)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(81, 81, 81)))
                .addGap(45, 45, 45))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(669, 669, 669)
                    .addComponent(jLabel2)
                    .addContainerGap(673, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(582, 582, 582)
                    .addComponent(TF_MaKH2, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(580, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(jLabel2)
                    .addContainerGap(836, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(431, 431, 431)
                    .addComponent(TF_MaKH2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(432, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="collapsed" desc="Generated Code">

    private void TF_MaKH2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_MaKH2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TF_MaKH2ActionPerformed

    private void BT_timkhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_timkhActionPerformed
        // TODO add your handling code here:
        try {
            LocalDate startDate = LocalDate.parse(TF_MaKH1.getText().trim(), UI_DATE_FORMATTER);
            LocalDate endDate = LocalDate.parse(TF_MaKH3.getText().trim(), UI_DATE_FORMATTER);

            if (startDate.isAfter(endDate)) {
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu không được sau ngày kết thúc.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            fillHoaDonTable(thongKeDAO.getDanhSachHoaDonByDateRange(startDate, endDate));
            updateGeneralStatistics(); // Cập nhật thống kê chung sau khi lọc
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Ngày không hợp lệ. Vui lòng nhập theo định dạng yyyy-MM-dd.", "Lỗi Định Dạng", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm hóa đơn theo ngày: " + e.getMessage(), "Lỗi DB", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_BT_timkhActionPerformed

    private void TF_MaKH3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_MaKH3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TF_MaKH3ActionPerformed

    private void TF_MaKH1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_MaKH1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TF_MaKH1ActionPerformed

    private void BT_timkh1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_timkh1ActionPerformed
        // TODO add your handling code here:
       loadDataForSelectedTab(); // Tải lại dữ liệu khi tháng thay đổi
    }//GEN-LAST:event_BT_timkh1ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jTable2MouseClicked

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        loadDataForSelectedTab(); // Tải lại dữ liệu khi tháng thay đổi
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
        loadDataForSelectedTab(); // Tải lại dữ liệu khi tháng thay đổi
    }//GEN-LAST:event_jComboBox2ActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QLTKE.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QLTKE.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QLTKE.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QLTKE.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Tạo một JFrame mới
                JFrame frame = new JFrame("Quản Lý Thống Kê"); // Tiêu đề cửa sổ

                // Tạo đối tượng QLTKE panel
                QLTKE qltkePanel = new QLTKE();

                // Thêm QLTKE panel vào JFrame
                frame.add(qltkePanel);

                // Đặt kích thước cho JFrame (hoặc pack() để tự động điều chỉnh theo nội dung)
                frame.setSize(1400, 800); // Kích thước ví dụ, bạn có thể điều chỉnh

                // Đặt thao tác mặc định khi đóng cửa sổ
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // Đặt vị trí cửa sổ ở giữa màn hình
                frame.setLocationRelativeTo(null);

                // Hiển thị JFrame
                frame.setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BT_timkh;
    private javax.swing.JButton BT_timkh1;
    private javax.swing.JTextField TF_MaKH1;
    private javax.swing.JTextField TF_MaKH2;
    private javax.swing.JTextField TF_MaKH3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_DoanhThu;
    private javax.swing.JLabel jLabel_DonHang;
    private javax.swing.JLabel jLabel_TonKho;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
