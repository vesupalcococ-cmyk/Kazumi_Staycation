/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Ysha
 */
public class Payment_Records extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Payment_Records.class.getName());


    public Payment_Records() {
    initComponents();
    
    setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

    String name = UserSession.getUsername();
    if (name != null) {
        jLabel10.setText(" - - - - ADMIN " + name.toUpperCase() + " USING - - - - ");
    }
    jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            int row = jTable1.rowAtPoint(evt.getPoint());
            int column = jTable1.columnAtPoint(evt.getPoint());
            if (row >= 0 && column == 9) {
                editPayment(row);
            }
        }
    });

    loadPaymentRecords();
}
    
    

private void loadPaymentRecords() {
    String[] columns = {
        "ID",
        "Full Name",
        "Grand Total",
        "Downpayment",
        "Balance",
        "Payment Method",
        "Reference Number",
        "Payment Sent (TIME)",
        "Payment Sent (DATE)",
        "Action"
    };

    DefaultTableModel model = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    String sql = "SELECT b.ID AS guest_id, b.full_name, "
            + "p.grand_total, p.downpayment, p.balance, "
            + "p.payment_method, p.reference_number, "
            + "p.payment_time, p.payment_date "
            + "FROM tbl_payment p "
            + "INNER JOIN tbl_guest b ON p.guest_id = b.ID "
            + "ORDER BY p.payment_date DESC, p.payment_time DESC";

    try (Connection conn = DBConnection.connect();
         PreparedStatement pst = conn.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("guest_id"),
                rs.getString("full_name"),
                rs.getBigDecimal("grand_total"),
                rs.getBigDecimal("downpayment"),
                rs.getBigDecimal("balance"),
                rs.getString("payment_method"),
                rs.getString("reference_number"),
                rs.getTime("payment_time"),
                rs.getDate("payment_date"),
                "Edit"
            });
        }
        
        jTable1.setModel(model);
        applyTableFormat();

        jTable1.setModel(model);

        jTable1.getColumnModel()
                .getColumn(9)
                .setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
                    @Override
                    public java.awt.Component getTableCellRendererComponent(
                            javax.swing.JTable table, Object value,
                            boolean isSelected, boolean hasFocus,
                            int row, int column) {

                        javax.swing.JButton button =
                                new javax.swing.JButton("Edit");

                        button.setFont(new java.awt.Font("Sylfaen", 1, 14));
                        button.setForeground(new java.awt.Color(225, 139, 161));

                        return button;
                    }
                });

        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = jTable1.rowAtPoint(evt.getPoint());
                int column = jTable1.columnAtPoint(evt.getPoint());

                if (row >= 0 && column == 9) {
                    editPayment(row);
                }
            }
        });

    } catch (Exception e) {
        JOptionPane.showMessageDialog(
                this,
                "Unable to load payment records:\n" + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}


private void editPayment(int row) {
    int guestId = Integer.parseInt(
        jTable1.getValueAt(row, 0).toString()
    );

    String total = jTable1.getValueAt(row, 2).toString();
    String downpayment = jTable1.getValueAt(row, 3).toString();
    String balance = jTable1.getValueAt(row, 4).toString();
    String method = jTable1.getValueAt(row, 5).toString();
    String reference = jTable1.getValueAt(row, 6).toString();
    String time = jTable1.getValueAt(row, 7).toString();
    String date = jTable1.getValueAt(row, 8).toString();

    javax.swing.JTextField txtTotal =
            new javax.swing.JTextField(total);
    javax.swing.JTextField txtDownpayment =
            new javax.swing.JTextField(downpayment);
    javax.swing.JTextField txtBalance =
            new javax.swing.JTextField(balance);
    javax.swing.JTextField txtMethod =
            new javax.swing.JTextField(method);
    javax.swing.JTextField txtReference =
            new javax.swing.JTextField(reference);
    javax.swing.JTextField txtTime =
            new javax.swing.JTextField(time);
    javax.swing.JTextField txtDate =
            new javax.swing.JTextField(date);

    Object[] options = {
        "Save Changes",
        "Cancel"
    };

    Object[] fields = {
        "Grand Total:", txtTotal,
        "Downpayment:", txtDownpayment,
        "Balance:", txtBalance,
        "Payment Method:", txtMethod,
        "Reference Number:", txtReference,
        "Payment Time (HH:MM:SS):", txtTime,
        "Payment Date (YYYY-MM-DD):", txtDate
    };

    int choice = JOptionPane.showOptionDialog(
        this,
        fields,
        "Edit Payment Record",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.PLAIN_MESSAGE,
        null,
        options,
        options[0]
    );

    if (choice != 0) {
        return;
    }

    if (txtTotal.getText().trim().isEmpty()
            || txtDownpayment.getText().trim().isEmpty()
            || txtBalance.getText().trim().isEmpty()
            || txtMethod.getText().trim().isEmpty()
            || txtReference.getText().trim().isEmpty()
            || txtTime.getText().trim().isEmpty()
            || txtDate.getText().trim().isEmpty()) {

        JOptionPane.showMessageDialog(
            this,
            "Please complete all fields."
        );
        return;
    }

    String sql = "UPDATE tbl_payment SET "
            + "grand_total = ?, downpayment = ?, balance = ?, "
            + "payment_method = ?, reference_number = ?, "
            + "payment_time = ?, payment_date = ? "
            + "WHERE guest_id = ?";

    try (Connection conn = DBConnection.connect();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setBigDecimal(
            1,
            new java.math.BigDecimal(txtTotal.getText().trim())
        );
        pst.setBigDecimal(
            2,
            new java.math.BigDecimal(txtDownpayment.getText().trim())
        );
        pst.setBigDecimal(
            3,
            new java.math.BigDecimal(txtBalance.getText().trim())
        );
        pst.setString(4, txtMethod.getText().trim());
        pst.setString(5, txtReference.getText().trim());
        pst.setTime(
            6,
            java.sql.Time.valueOf(txtTime.getText().trim())
        );
        pst.setDate(
            7,
            java.sql.Date.valueOf(txtDate.getText().trim())
        );
        pst.setInt(8, guestId);

        int updated = pst.executeUpdate();

        if (updated > 0) {
            JOptionPane.showMessageDialog(
                this,
                "Payment information updated successfully."
            );
            loadPaymentRecords();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "No payment record was updated."
            );
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(
            this,
            "Error updating payment record:\n"
            + "Check the amount, time, and date formats.\n\n"
            + e.getMessage(),
            "Update Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}

private void applyTableFormat() {
    jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    jTable1.setRowHeight(26);

    int[] widths = {70, 160, 120, 120, 100, 140, 160, 170, 170, 90};
    for (int i = 0; i < widths.length; i++) {
        jTable1.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
    }

    jTable1.getColumnModel().getColumn(9)
        .setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {
                javax.swing.JButton button = new javax.swing.JButton("Edit");
                button.setFont(new java.awt.Font("Sylfaen", 1, 14));
                button.setForeground(new java.awt.Color(225, 139, 161));
                return button;
            }
        });
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton7 = new javax.swing.JToggleButton();
        jToggleButton8 = new javax.swing.JToggleButton();
        jToggleButton6 = new javax.swing.JToggleButton();
        jToggleButton9 = new javax.swing.JToggleButton();
        jToggleButton10 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(225, 139, 161));

        jLabel1.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ysha\\Downloads\\kaz-removebg-preview (1).png")); // NOI18N

        jLabel10.setFont(new java.awt.Font("Sylfaen", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText(" - - - - ADMIN \"NAME\" USING - - - -  ");

        jToggleButton1.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jToggleButton1.setForeground(new java.awt.Color(225, 139, 161));
        jToggleButton1.setText("Admin Page");
        jToggleButton1.addActionListener(this::jToggleButton1ActionPerformed);

        jToggleButton2.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jToggleButton2.setForeground(new java.awt.Color(225, 139, 161));
        jToggleButton2.setText("Add Guest");
        jToggleButton2.addActionListener(this::jToggleButton2ActionPerformed);

        jToggleButton7.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jToggleButton7.setForeground(new java.awt.Color(225, 139, 161));
        jToggleButton7.setText("Add Payment");
        jToggleButton7.addActionListener(this::jToggleButton7ActionPerformed);

        jToggleButton8.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jToggleButton8.setForeground(new java.awt.Color(225, 139, 161));
        jToggleButton8.setText("Guest Records");
        jToggleButton8.addActionListener(this::jToggleButton8ActionPerformed);

        jToggleButton6.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jToggleButton6.setForeground(new java.awt.Color(225, 139, 161));
        jToggleButton6.setText("View Availability");
        jToggleButton6.addActionListener(this::jToggleButton6ActionPerformed);

        jToggleButton9.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jToggleButton9.setForeground(new java.awt.Color(225, 139, 161));
        jToggleButton9.setText("View Staycation");
        jToggleButton9.addActionListener(this::jToggleButton9ActionPerformed);

        jToggleButton10.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jToggleButton10.setForeground(new java.awt.Color(225, 139, 161));
        jToggleButton10.setText("View Amenities");
        jToggleButton10.addActionListener(this::jToggleButton10ActionPerformed);

        jToggleButton3.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jToggleButton3.setForeground(new java.awt.Color(225, 139, 161));
        jToggleButton3.setText("Logout");
        jToggleButton3.addActionListener(this::jToggleButton3ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel10))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jToggleButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jToggleButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jToggleButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jToggleButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jToggleButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jToggleButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jToggleButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel1))))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(20, 20, 20))
        );

        jPanel1.setBackground(new java.awt.Color(251, 239, 243));

        jLabel2.setFont(new java.awt.Font("Javanese Text", 1, 60)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(225, 139, 161));
        jLabel2.setText("PAYMENT RECORDS");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Full Name", "Grand Total", "Downpayment", "Balance", "Payment Method", "Reference Number", "Payment Sent (TIME)", "Payment Sent (DATE)"
            }
        ));
        jTable1.setShowGrid(true);
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(182, 182, 182))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 889, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        new Admin_Page().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        new Add_Guest().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void jToggleButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton7ActionPerformed
        new Add_Payment().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jToggleButton7ActionPerformed

    private void jToggleButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton8ActionPerformed
        new Guest_Records().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jToggleButton8ActionPerformed

    private void jToggleButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton6ActionPerformed
        new Admin_Availability().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jToggleButton6ActionPerformed

    private void jToggleButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton9ActionPerformed
        new Admin_Staycation().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jToggleButton9ActionPerformed

    private void jToggleButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton10ActionPerformed
        new Admin_Amenities().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jToggleButton10ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        new Logout().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Payment_Records().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton10;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton6;
    private javax.swing.JToggleButton jToggleButton7;
    private javax.swing.JToggleButton jToggleButton8;
    private javax.swing.JToggleButton jToggleButton9;
    // End of variables declaration//GEN-END:variables
}
