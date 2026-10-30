package com.mycompany.main;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
/**
 *
 * @author Ysha
 */
public class Guest_Records extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Guest_Records.class.getName());

    /**
     */
    public Guest_Records() {
    initComponents();
    setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

    String name = UserSession.getUsername();
    if (name != null) {
        jLabel10.setText(" - - - - ADMIN " + name.toUpperCase() + " USING - - - - ");
    }

    loadData();


    jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        int row = jTable1.rowAtPoint(e.getPoint());
        int col = jTable1.columnAtPoint(e.getPoint());

        if (row >= 0 && col == 9) {
            editBooking(row);
        }
    }
});
}
    private void loadData() {
    String[] cols = {
    "ID", "FullName", "Email", "ContactNum",
    "Check-In Date", "Check-Out Date",
    "Check-In Status", "Check-Out Status",
    "Booking Status", "Action"
};

    DefaultTableModel model = new DefaultTableModel(cols, 0) {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };

    String sql = "SELECT ID, full_name, email, contact, "
           + "check_in, check_out, time_in, time_out, booking_type "
           + "FROM tbl_guest ORDER BY check_in DESC";

    try (Connection conn = DBConnection.connect();
         PreparedStatement pst = conn.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        LocalDate today = LocalDate.now();

        while (rs.next()) {
            LocalDate in = rs.getDate("check_in").toLocalDate();
            LocalDate out = rs.getDate("check_out").toLocalDate();

            java.sql.Time timeIn = rs.getTime("time_in");
            java.sql.Time timeOut = rs.getTime("time_out");

            String checkInStatus;
            if (timeIn != null) {
                checkInStatus = "Checked In - " + timeIn.toString();
            } else {
                checkInStatus = "Not Checked In";
            }
            String bookingStatus;

if (timeOut != null) {
    bookingStatus = "Completed";
} else if (timeIn != null) {
    bookingStatus = "Booked";
} else {
    bookingStatus = rs.getString("booking_type");
}
            String checkOutStatus;
            if (timeOut != null) {
                checkOutStatus = "Checked Out - " + timeOut.toString();
            } else {
                checkOutStatus = "Not Checked Out";
            }



            model.addRow(new Object[]{
    rs.getInt("ID"),
    rs.getString("full_name"),
    rs.getString("email"),
    rs.getString("contact"),
    in,
    out,
    checkInStatus,
    checkOutStatus,
    bookingStatus,
    "Edit"
});
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error: " + e.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE);
    }

    jTable1.setModel(model);
    jTable1.setRowHeight(26);
    jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    // Set column widths
    jTable1.getColumnModel().getColumn(0).setPreferredWidth(50);
jTable1.getColumnModel().getColumn(1).setPreferredWidth(180);
jTable1.getColumnModel().getColumn(2).setPreferredWidth(200);
jTable1.getColumnModel().getColumn(3).setPreferredWidth(130);
jTable1.getColumnModel().getColumn(4).setPreferredWidth(120);
jTable1.getColumnModel().getColumn(5).setPreferredWidth(120);
jTable1.getColumnModel().getColumn(6).setPreferredWidth(160);
jTable1.getColumnModel().getColumn(7).setPreferredWidth(170);
jTable1.getColumnModel().getColumn(8).setPreferredWidth(130);
jTable1.getColumnModel().getColumn(9).setPreferredWidth(100);

    // Renderer for check-in and check-out colors
    DefaultTableCellRenderer statusRenderer =
            new DefaultTableCellRenderer() {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            Component cell = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                String text = value == null ? "" : value.toString();

                if (text.startsWith("Checked In")
                        || text.startsWith("Checked Out")) {
                    cell.setBackground(new Color(198, 239, 206));
                    cell.setForeground(new Color(0, 97, 0));
                } else {
                    cell.setBackground(new Color(255, 199, 206));
                    cell.setForeground(new Color(156, 0, 6));
                }
            }

            return cell;
        }
    };

    // Apply colors to Check-In Status and Check-Out Status
    jTable1.getColumnModel().getColumn(6)
            .setCellRenderer(statusRenderer);

    jTable1.getColumnModel().getColumn(7)
            .setCellRenderer(statusRenderer);

    // Enable scrolling
    jScrollPane1.setHorizontalScrollBarPolicy(
        javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    jScrollPane1.setVerticalScrollBarPolicy(
        javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
}


private void editBooking(int row) {
    int id = Integer.parseInt(
        jTable1.getValueAt(row, 0).toString()
    );

    String name = jTable1.getValueAt(row, 1).toString();
    String email = jTable1.getValueAt(row, 2).toString();
    String contact = jTable1.getValueAt(row, 3).toString();

    javax.swing.JTextField txtName = new javax.swing.JTextField(name);
    javax.swing.JTextField txtEmail = new javax.swing.JTextField(email);
    javax.swing.JTextField txtContact = new javax.swing.JTextField(contact);

    Object[] options = {
        "Save Changes",
        "Confirm Check-In",
        "Confirm Check-Out",
        "Cancel"
    };

    Object[] fields = {
        "Full Name:", txtName,
        "Email:", txtEmail,
        "Contact Number:", txtContact
    };

    int choice = JOptionPane.showOptionDialog(
        this,
        fields,
        "Edit Guest / Confirm Stay",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.PLAIN_MESSAGE,
        null,
        options,
        options[0]
    );

    if (choice == 3 || choice == JOptionPane.CLOSED_OPTION) {
        return;
    }

    if (choice == 0) {
        if (txtName.getText().trim().isEmpty()
                || txtEmail.getText().trim().isEmpty()
                || txtContact.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please complete all fields.");
            return;
        }

       String sql = "UPDATE tbl_guest SET full_name = ?, email = ?, contact = ? "
           + "WHERE ID = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, txtName.getText().trim());
            pst.setString(2, txtEmail.getText().trim());
            pst.setString(3, txtContact.getText().trim());
            pst.setInt(4, id);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Guest information updated successfully.");
            loadData();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error updating guest: " + e.getMessage());
        }

    } else if (choice == 1) {
        confirmGuestTime(id, true);

    } else if (choice == 2) {
        confirmGuestTime(id, false);
    }
}

private void confirmGuestTime(int id, boolean checkIn) {
    String sql;

    if (checkIn) {
        sql = "UPDATE tbl_guest SET time_in = CURRENT_TIME "
            + "WHERE ID = ? AND time_in IS NULL";
    } else {
        sql = "UPDATE tbl_guest SET time_out = CURRENT_TIME "
            + "WHERE ID = ? AND time_in IS NOT NULL "
            + "AND time_out IS NULL";
    }

    String action = checkIn ? "check in" : "check out";

    int answer = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to confirm this guest's "
        + action + "?",
        "Confirm Guest " + (checkIn ? "Check-In" : "Check-Out"),
        JOptionPane.YES_NO_OPTION
    );

    if (answer != JOptionPane.YES_OPTION) {
        return;
    }

    try (Connection conn = DBConnection.connect();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setInt(1, id);

        int updated = pst.executeUpdate();

        if (updated > 0) {
            JOptionPane.showMessageDialog(this,
                "Guest " + action + " confirmed successfully.");
        } else {
            JOptionPane.showMessageDialog(this,
                checkIn
                    ? "This guest has already checked in."
                    : "The guest must check in first, or has already checked out.");
        }

        loadData();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error confirming guest: " + e.getMessage());
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
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();

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
        jToggleButton8.setText("Payment Records");
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(20, 20, 20))
        );

        jPanel1.setBackground(new java.awt.Color(251, 239, 243));

        jLabel2.setFont(new java.awt.Font("Javanese Text", 1, 60)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(225, 139, 161));
        jLabel2.setText("GUEST RECORDS");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "FullName", "Email", "Contact Number", "Time-In", "Check-In", "Time-Out", "Check-Out"
            }
        ));
        jTable1.setShowGrid(true);
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Search");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(271, 271, 271))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 874, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(68, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
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
        new Payment_Records().setVisible(true);
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
    String searchValue = jTextField1.getText().trim();

    if (searchValue.isEmpty()) {
        loadData();
        return;
    }

    String[] cols = {
    "ID", "FullName", "Email", "ContactNum",
    "Check-In Date", "Check-Out Date",
    "Check-In Status", "Check-Out Status",
    "Action"
};

    DefaultTableModel model = new DefaultTableModel(cols, 0) {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };

    String sql = "SELECT ID, full_name, email, contact, "
            + "check_in, check_out, time_in, time_out "
            + "FROM tbl_guest "
            + "WHERE CAST(ID AS CHAR) LIKE ? "
            + "OR full_name LIKE ? "
            + "ORDER BY check_in DESC";

    try (Connection conn = DBConnection.connect();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setString(1, "%" + searchValue + "%");
        pst.setString(2, "%" + searchValue + "%");

        try (ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                java.sql.Date checkInDate = rs.getDate("check_in");
                java.sql.Date checkOutDate = rs.getDate("check_out");

                LocalDate in = checkInDate.toLocalDate();
                LocalDate out = checkOutDate.toLocalDate();

                java.sql.Time timeIn = rs.getTime("time_in");
                java.sql.Time timeOut = rs.getTime("time_out");

                String checkInStatus = timeIn != null
                        ? "Checked In - " + timeIn
                        : "Not Checked In";

                String checkOutStatus = timeOut != null
                        ? "Checked Out - " + timeOut
                        : "Not Checked Out";

                
                model.addRow(new Object[]{
    rs.getInt("ID"),
    rs.getString("full_name"),
    rs.getString("email"),
    rs.getString("contact"),
    in,
    out,
    checkInStatus,
    checkOutStatus,
    "Edit"
});
            }
        }

        jTable1.setModel(model);
        jTable1.setRowHeight(26);
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

       int[] widths = {
    50, 180, 200, 130, 120,
    120, 160, 170, 100
};

        for (int i = 0; i < widths.length; i++) {
            jTable1.getColumnModel()
                    .getColumn(i).setPreferredWidth(widths[i]);
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No guest found with that ID or name.",
                    "Search Results",
                    JOptionPane.INFORMATION_MESSAGE);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                "Error searching guest: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
    }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
         jButton1ActionPerformed(evt);
    }//GEN-LAST:event_jTextField1ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Guest_Records().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
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
