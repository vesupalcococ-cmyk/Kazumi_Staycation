package com.mycompany.main;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Ysha
 */
public class Availability extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Availability.class.getName());

    /**
     * Creates new form Availability
     */
    public Availability() {
        initComponents();
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        
        showMonth();

    jButton2.addActionListener(e -> {
        currentMonth = currentMonth.plusMonths(1);
        showMonth();
    });
    }
    private java.time.YearMonth currentMonth = java.time.YearMonth.now();


    private void showMonth() {
    String[] days = {"SUN", "MON", "TUE", "WED", "THURS", "FRI", "SAT"};

    javax.swing.table.DefaultTableModel model =
        new javax.swing.table.DefaultTableModel(days, 6) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

    int firstColumn = currentMonth.atDay(1).getDayOfWeek().getValue() % 7;
    int totalDays = currentMonth.lengthOfMonth();

    for (int day = 1; day <= totalDays; day++) {
        int cell = firstColumn + day - 1;
        model.setValueAt(String.valueOf(day), cell / 7, cell % 7);
    }

    jTable1.setModel(model);
    jTable1.setRowHeight(24);

    // day number -> "BOOKED", "RESERVED" or "COMPLETED" (not in map = no booking)
    final java.util.Map<Integer, String> dayStatus = loadDayStatuses();

    jTable1.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row, int col) {

            java.awt.Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, col);
            setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            setOpaque(true);

            String status = null;
            if (value != null) {
                status = dayStatus.get(Integer.parseInt(value.toString()));
            }

            if ("BOOKED".equals(status)) {
                c.setBackground(new java.awt.Color(225, 139, 161)); // pink
                c.setForeground(java.awt.Color.WHITE);
            } else if ("RESERVED".equals(status)) {
                c.setBackground(new java.awt.Color(155, 89, 182));  // purple
                c.setForeground(java.awt.Color.WHITE);
            } else if ("COMPLETED".equals(status)) {
                c.setBackground(new java.awt.Color(76, 175, 80));   // green
                c.setForeground(java.awt.Color.WHITE);
            } else {
                c.setBackground(java.awt.Color.WHITE);              // no bookings
                c.setForeground(java.awt.Color.BLACK);
            }
            return c;
        }
    });

    String monthName = currentMonth.getMonth()
        .getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.ENGLISH)
        .toUpperCase();
    jLabel3.setText(monthName + " " + currentMonth.getYear());
}

private java.util.Map<Integer, String> loadDayStatuses() {
    java.util.Map<Integer, String> dayStatus = new java.util.HashMap<>();

    String sql = "SELECT check_in, check_out, time_in, time_out, booking_type FROM tbl_guest "
               + "WHERE check_in < ? AND check_out > ?";

    try (java.sql.Connection conn = DBConnection.connect();
         java.sql.PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setDate(1, java.sql.Date.valueOf(currentMonth.plusMonths(1).atDay(1)));
        pst.setDate(2, java.sql.Date.valueOf(currentMonth.atDay(1)));

        try (java.sql.ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {

                java.sql.Date inDate = rs.getDate("check_in");
                java.sql.Date outDate = rs.getDate("check_out");
                if (inDate == null || outDate == null) {
                    continue;
                }

                // same rule as Guest Records and Admin_Availability
                String status;
                if (rs.getTime("time_out") != null) {
                    status = "COMPLETED";
                } else if (rs.getTime("time_in") != null
                        || "Booked".equalsIgnoreCase(rs.getString("booking_type"))) {
                    status = "BOOKED";
                } else {
                    status = "RESERVED";
                }

                java.time.LocalDate in = inDate.toLocalDate();
                java.time.LocalDate out = outDate.toLocalDate();

                for (java.time.LocalDate d = in; d.isBefore(out); d = d.plusDays(1)) {
                    if (java.time.YearMonth.from(d).equals(currentMonth)) {
                        int day = d.getDayOfMonth();
                        String existing = dayStatus.get(day);
                        if (existing == null || priority(status) > priority(existing)) {
                            dayStatus.put(day, status);
                        }
                    }
                }
            }
        }
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
            "Database Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    return dayStatus;
}

private int priority(String status) {
    switch (status) {
        case "BOOKED":   return 3;
        case "RESERVED": return 2;
        default:         return 1; // COMPLETED
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

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jToggleButton3 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jToggleButton4 = new javax.swing.JToggleButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jToggleButton5 = new javax.swing.JToggleButton();
        jToggleButton6 = new javax.swing.JToggleButton();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(251, 239, 243));

        jLabel2.setFont(new java.awt.Font("Javanese Text", 1, 60)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(225, 139, 161));
        jLabel2.setText("AVAILABILITY");

        jPanel2.setBackground(new java.awt.Color(225, 139, 161));

        jToggleButton3.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jToggleButton3.setForeground(new java.awt.Color(225, 139, 161));
        jToggleButton3.setText("VIEW STAYCATION ");
        jToggleButton3.addActionListener(this::jToggleButton3ActionPerformed);

        jToggleButton4.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jToggleButton4.setForeground(new java.awt.Color(225, 139, 161));
        jToggleButton4.setText("LOGIN HERE");
        jToggleButton4.addActionListener(this::jToggleButton4ActionPerformed);

        jLabel10.setFont(new java.awt.Font("Sylfaen", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText(" - - - - - - - - FOR PUBLIC - - - - - - -");

        jLabel11.setFont(new java.awt.Font("Sylfaen", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("- - - - - - - - FOR ADMIN - - - - - - - -");

        jToggleButton5.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jToggleButton5.setForeground(new java.awt.Color(225, 139, 161));
        jToggleButton5.setText("VIEW AMENITIES ");
        jToggleButton5.addActionListener(this::jToggleButton5ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jToggleButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(12, 12, 12)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jToggleButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addComponent(jLabel10)
                .addGap(31, 31, 31)
                .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jToggleButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(111, 111, 111))
        );

        jToggleButton6.setFont(new java.awt.Font("Sylfaen", 1, 12)); // NOI18N
        jToggleButton6.setForeground(new java.awt.Color(225, 139, 161));
        jToggleButton6.setText("BACK TO HOMPAGE");
        jToggleButton6.addActionListener(this::jToggleButton6ActionPerformed);

        jButton1.setText("<");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jLabel3.setFont(new java.awt.Font("Javanese Text", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(225, 139, 161));
        jLabel3.setText("SEPTEMBER");

        jButton2.setText(">");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, "1", "2", "3", "4", "5"},
                {"6", "7", "8", "9", "10", "11", "12"},
                {"13", "14", "15", "16", "17", "18", "19"},
                {"20", "21", "22", "23", "24", "25", "26"},
                {"27", "28", "29", "30", null, null, null}
            },
            new String [] {
                "SUN", "MON", "TUE", "WED", "THURS", "FRI", "SAT"
            }
        ));
        jTable1.setAlignmentX(1.0F);
        jTable1.setAlignmentY(1.0F);
        jTable1.setEditingColumn(5);
        jTable1.setEditingRow(6);
        jTable1.setRowMargin(6);
        jTable1.setShowGrid(true);
        jScrollPane1.setViewportView(jTable1);

        jLabel4.setFont(new java.awt.Font("Javanese Text", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(225, 139, 161));
        jLabel4.setText("RESERVED");

        jPanel3.setBackground(new java.awt.Color(155, 89, 182));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new java.awt.Color(225, 139, 161));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jLabel5.setFont(new java.awt.Font("Javanese Text", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(225, 139, 161));
        jLabel5.setText("BOOKED");

        jPanel5.setBackground(new java.awt.Color(76, 175, 80));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jLabel6.setFont(new java.awt.Font("Javanese Text", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(225, 139, 161));
        jLabel6.setText("COMPLETED");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(376, 376, 376)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(97, 97, 97)
                                .addComponent(jToggleButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 752, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(120, 120, 120))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(148, 148, 148)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6)))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jToggleButton6)))
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(111, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        new View_Staycation().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jToggleButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton5ActionPerformed
        new Amenities().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jToggleButton5ActionPerformed

    private void jToggleButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton6ActionPerformed
         new Home_Page().setVisible(true);
         this.dispose();// TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        currentMonth = currentMonth.minusMonths(1);
        showMonth();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jToggleButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton4ActionPerformed
        new Login().setVisible(true);
        this.dispose();   // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton4ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Availability().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    private javax.swing.JToggleButton jToggleButton5;
    private javax.swing.JToggleButton jToggleButton6;
    // End of variables declaration//GEN-END:variables
}
