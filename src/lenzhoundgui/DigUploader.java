/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lenzhoundgui;
import java.io.*;
import javax.swing.JOptionPane;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.beans.PropertyChangeEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.ColorUIResource;
import lenzhoundgui.SerialCommunicator.DeviceType;

/**
 *
 * @author Thayer
 */
public final class DigUploader extends javax.swing.JFrame{
    
    public static void main(String args[]) {
        System.setProperty("apple.laf.useScreenMenuBar", "false");
            System.setProperty(
                "com.apple.mrj.application.apple.menu.about.name", "Dig-Uploader");
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
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DigUploader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        
        //</editor-fold>
        /* Create and display the form */
        
        showSplashScreen();
        new DigUploader().setVisible(true);
        /*java.awt.EventQueue.invokeLater(() -> {
            new DigUploader().setVisible(true);            
        }); */       
    }
    /**
     * Creates new form DigUploader
     */
    public DigUploader() {
        lenzhoundDirectory = filePath() + File.separator;
        UIManager.put("ToolTip.background", new ColorUIResource(255, 247, 200));//ensures none of the tooltips default to gray background
        DATA = new File(lenzhoundDirectory + ".data.txt");
        LenzLogger.initialize(new File(lenzhoundDirectory), true);
        LenzLogger.log("GUI opened");
        //LenzLogger.log("GUI opened at " + new SimpleDateFormat("hh:mm").format(new Date()));
        LenzLogger.log("GUI stored at " + lenzhoundDirectory);
        LenzLogger.log("Begining component set up.");
        initComponents();
        LenzLogger.log("Components finished, starting listeners.");
        setUpListeners();
        LenzLogger.log("Listeners finished, staring keyboard manager.");
        setUpKFM();//keyboardfocusmanager
        LenzLogger.log("Keyboard manager finished, starting dialog frame.");
        setUpDialogFrame();
        LenzLogger.log("Dialog frame finished, starting load of saved settings.");
        loadFromFile();        
        LenzLogger.log("File loading finished, storing settings list");
        storeSettingsList();
        LenzLogger.log("Setting gui values.");
        updateGUIValues();
        updateComboBox();
        setFocusTraversalPolicy(new TextTraversalPolicy(this));
        timeLapseWindow = new TimeLapse(this);
        firmwareUtility = new FirmwareUtility();
        
        lastRChannel = cameraList.get(currentCamera).getChannel();
        lastTChannel = cameraList.get(currentCamera).getTChannel();
        if(lastRChannel!=lastTChannel){
            setLabelToWarning(rStatusLabel);
            setLabelToWarning(tStatusLabel);
        } else{
            setLabelToSuccess(rStatusLabel);
            setLabelToSuccess(tStatusLabel);
        }
        LenzLogger.log(exposeCurrentValues());              
        
        //Sets the icon for the upper left corner of the application
        java.net.URL url = ClassLoader.getSystemResource("lenzhoundgui/res/motion-dogs.png");
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.createImage(url);
        this.setIconImage(img);
        
        FirmwareUtility.findNecessaryFiles();
        
        if(!UIManager.getLookAndFeel().getName().equals("Nimbus")){
        //if(!OSUtils.isWindows()&&!OSUtils.isMac()){
            this.setSize(this.size().width, this.size().height - 30);//Correcting for weird sizing on unix machines
        }
        
        
        LenzLogger.log("Setup process finished.");
    }
    
    private String filePath(){
        System.out.println("Finding application path:");
        String applicationDir = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        applicationDir = applicationDir.replaceAll("%20", " ");
        applicationDir = new File(applicationDir).getParent();
        System.out.println("\t1: checking " + applicationDir);
        if(new File(applicationDir + File.separator + "avr").exists())
            return applicationDir;
        //checks parent directory if avr not found, common if you run outside of jar
        applicationDir = new File(applicationDir).getParent();
        System.out.println("\t2: checking " + applicationDir);
        if(new File(applicationDir + File.separator + "avr").exists())
            return applicationDir;
        System.out.println("Defaulting to user.dir, checking:");
        if(new File(System.getProperty("user.dir") + File.separator + "avr").exists())
            return System.getProperty("user.dir");
        return applicationDir;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        exitButton = new javax.swing.JButton();
        defaultButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        savedSettingsComboBox = new javax.swing.JComboBox();
        deleteButton = new javax.swing.JButton();
        newButton = new javax.swing.JButton();
        versionLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tStatusLabel = new javax.swing.JLabel();
        tChannelTextField = new javax.swing.JTextField();
        channelLabel = new javax.swing.JLabel();
        powerAmpComboBox = new javax.swing.JComboBox();
        powerAmpLabel = new javax.swing.JLabel();
        tUploadButton = new javax.swing.JToggleButton();
        tPollButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        normalModeLabel = new javax.swing.JLabel();
        velocityLabel = new javax.swing.JLabel();
        velocitySlider = new javax.swing.JSlider();
        velocityTextField = new javax.swing.JTextField();
        accelerationLabel = new javax.swing.JLabel();
        accelerationSlider = new javax.swing.JSlider();
        accelerationTextField = new javax.swing.JTextField();
        zModeLabel = new javax.swing.JLabel();
        zVelocityLabel = new javax.swing.JLabel();
        zVelocitySlider = new javax.swing.JSlider();
        zVelocityTextField = new javax.swing.JTextField();
        zAccelerationLabel = new javax.swing.JLabel();
        zAccelerationSlider = new javax.swing.JSlider();
        zAccelerationTextField = new javax.swing.JTextField();
        rChannelTextField = new javax.swing.JTextField();
        channelLabel2 = new javax.swing.JLabel();
        rStatusLabel = new javax.swing.JLabel();
        rUploadButton = new javax.swing.JToggleButton();
        timeLapseButton = new javax.swing.JToggleButton();
        rPollButton = new javax.swing.JButton();
        saveAsButton = new javax.swing.JButton();
        saveAllButton = new javax.swing.JButton();
        firmwareButton = new javax.swing.JButton();
        logoLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Lenzhound D.I.G. Uploader");
        setResizable(false);

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        defaultButton.setText("Default");
        defaultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defaultButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        savedSettingsComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Default Settings" }));
        savedSettingsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savedSettingsComboBoxActionPerformed(evt);
            }
        });

        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        newButton.setText("Create New");
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });

        versionLabel.setText("Vesion 1.2.3");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("TXR-1");

        tStatusLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tStatusLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tStatusLabel.setText("statusLabel");

        tChannelTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tChannelTextField.setText("1");
        tChannelTextField.setName(""); // NOI18N

        channelLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        channelLabel.setText("Channel");

        powerAmpComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-18 dBm", "-12 dBm", "-06 dBm", "   0 dBm" }));
        powerAmpComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                powerAmpComboBoxActionPerformed(evt);
            }
        });

        powerAmpLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        powerAmpLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        powerAmpLabel.setText("Power Amp");

        tUploadButton.setText("Upload TXR-1");
        tUploadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tUploadButtonActionPerformed(evt);
            }
        });

        tPollButton.setText("Poll TXR-1");
        tPollButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tPollButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tUploadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tPollButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(powerAmpLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(powerAmpComboBox, 0, 324, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(channelLabel)
                        .addGap(18, 18, 18)
                        .addComponent(tChannelTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tStatusLabel)
                    .addComponent(tUploadButton)
                    .addComponent(tPollButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tChannelTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(channelLabel)
                    .addComponent(powerAmpComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(powerAmpLabel))
                .addGap(19, 19, 19))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("DB-1");

        normalModeLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        normalModeLabel.setText("Free Run");

        velocityLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        velocityLabel.setText("Max Velocity");

        velocitySlider.setMaximum(1500);

        velocityTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        velocityTextField.setText("32768");

        accelerationLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        accelerationLabel.setText("Acceleration");

        accelerationSlider.setMaximum(1000);
        accelerationSlider.setMinimum(1);
        accelerationSlider.setToolTipText("");
        accelerationSlider.setValue(128);

        accelerationTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        accelerationTextField.setText("256");

        zModeLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        zModeLabel.setText("Z-Mode");

        zVelocityLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        zVelocityLabel.setText("Max Velocity");

        zVelocitySlider.setMaximum(1500);
        zVelocitySlider.setMinimum(10);

        zVelocityTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        zVelocityTextField.setText("32768");

        zAccelerationLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        zAccelerationLabel.setText("Acceleration");

        zAccelerationSlider.setMaximum(1024);
        zAccelerationSlider.setMinimum(1);
        zAccelerationSlider.setValue(128);

        zAccelerationTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        zAccelerationTextField.setText("256");

        rChannelTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        rChannelTextField.setText("1");
        rChannelTextField.setName(""); // NOI18N

        channelLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        channelLabel2.setText("Channel");

        rStatusLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        rStatusLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        rStatusLabel.setText("statusLabel");

        rUploadButton.setText("Upload DB-1");
        rUploadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rUploadButtonActionPerformed(evt);
            }
        });

        timeLapseButton.setText("Time Lapse Settings");
        timeLapseButton.setToolTipText("A calculator utility for setting up timelapse shots.");
        timeLapseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeLapseButtonActionPerformed(evt);
            }
        });

        rPollButton.setText("Poll DB-1");
        rPollButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rPollButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(rStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rUploadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rPollButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(77, 77, 77)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(accelerationLabel)
                                    .addComponent(velocityLabel)
                                    .addComponent(zVelocityLabel)
                                    .addComponent(zAccelerationLabel)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(zModeLabel)
                                    .addComponent(normalModeLabel))))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(velocitySlider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(accelerationSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(zVelocitySlider, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(zAccelerationSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addComponent(velocityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(timeLapseButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(channelLabel2)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(rChannelTextField)
                            .addComponent(accelerationTextField)
                            .addComponent(zVelocityTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                            .addComponent(zAccelerationTextField))))
                .addGap(20, 20, 20))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(rStatusLabel)
                    .addComponent(rUploadButton)
                    .addComponent(rPollButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(normalModeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(velocityLabel)
                            .addComponent(velocitySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(accelerationLabel)
                            .addComponent(accelerationSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(accelerationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(zModeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(zVelocityLabel)
                            .addComponent(zVelocityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(zVelocitySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(velocityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(95, 95, 95)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(zAccelerationLabel)
                    .addComponent(zAccelerationSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(zAccelerationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rChannelTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(channelLabel2)
                    .addComponent(timeLapseButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        saveAsButton.setText("Save As");
        saveAsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsButtonActionPerformed(evt);
            }
        });

        saveAllButton.setText("Save All");
        saveAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAllButtonActionPerformed(evt);
            }
        });

        firmwareButton.setText("Firmware");
        firmwareButton.setToolTipText("Update device programming");
        firmwareButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firmwareButtonActionPerformed(evt);
            }
        });

        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lenzhoundgui/res/MotionDogsBanner.png"))); // NOI18N
        logoLabel.setMinimumSize(new java.awt.Dimension(28, 17));
        logoLabel.setPreferredSize(new java.awt.Dimension(56, 23));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(savedSettingsComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(newButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveAsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(logoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(versionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(firmwareButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveAllButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(defaultButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(savedSettingsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteButton)
                    .addComponent(newButton)
                    .addComponent(saveAsButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(saveButton)
                            .addComponent(saveAllButton)
                            .addComponent(exitButton)
                            .addComponent(defaultButton)
                            .addComponent(firmwareButton)
                            .addComponent(versionLabel))
                        .addGap(45, 45, 45))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(logoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        setSize(new java.awt.Dimension(673, 504));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="collapsed" desc="Buttons">
    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        String newName = JOptionPane.showInputDialog("Name the new group of motor settings:");
        if(newName!=null){
            while(newName.contains(";")){
                newName = JOptionPane.showInputDialog("Names cannot contain semicolons, please try another name:");
            }
            cameraList.add(new CameraSettings(newName));
            updateComboBox();
            savedSettingsComboBox.setSelectedIndex(savedSettingsComboBox.getItemCount() - 1);
            currentCamera = savedSettingsComboBox.getSelectedIndex();
            updateGUIValues();
            storeSettingsList();
            LenzLogger.log("Created a new setting set named: " + newName);
        }        
    }//GEN-LAST:event_newButtonActionPerformed

    private void savedSettingsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savedSettingsComboBoxActionPerformed
        currentCamera = savedSettingsComboBox.getSelectedIndex();
        LenzLogger.log("Saved Settings Combo Box selected index: " + currentCamera);
        updateGUIValues();
    }//GEN-LAST:event_savedSettingsComboBoxActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        if(JOptionPane.showConfirmDialog(null,
                "Would you like to delete the current setting set? Deleted settings cannot be restored.",
                "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            cameraList.remove(currentCamera);
            if(cameraList.isEmpty())
                cameraList.add(new CameraSettings("Base Setup"));
            
            updateComboBox();
            LenzLogger.log("Deleted camera settings at index: " + currentCamera);
            if(currentCamera >= savedSettingsComboBox.getItemCount())
                currentCamera = savedSettingsComboBox.getItemCount() - 1;
            savedSettingsComboBox.setSelectedIndex(currentCamera);
            
            updateGUIValues();
            //saving
            cameraList.get(currentCamera).setChannel(Integer.parseInt(rChannelTextField.getText()));
            cameraList.get(currentCamera).setTChannel(Integer.parseInt(tChannelTextField.getText()));
            writeToFile();
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void defaultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defaultButtonActionPerformed
        if(JOptionPane.showConfirmDialog(null,
                "Would you like to return the current setting set to default values? Deleted settings cannot be restored.",
                "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            cameraList.get(currentCamera).setToDefault();
            updateGUIValues();
            //saving
            cameraList.get(currentCamera).setChannel(Integer.parseInt(rChannelTextField.getText()));
            cameraList.get(currentCamera).setTChannel(Integer.parseInt(tChannelTextField.getText()));
            writeToFile();
            LenzLogger.log("Reset camera at index " + currentCamera + "to default values");
        }
    }//GEN-LAST:event_defaultButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        cameraList.get(currentCamera).setChannel(Integer.parseInt(rChannelTextField.getText()));
        cameraList.get(currentCamera).setTChannel(Integer.parseInt(tChannelTextField.getText()));
        lastCameraListSaved.set(currentCamera, cameraList.get(currentCamera));
        writeToFile();
        if(!errorThrown)
            JOptionPane.showMessageDialog(null,
                    "Settings saved.", "Success",JOptionPane.INFORMATION_MESSAGE);
        errorThrown = false;
        LenzLogger.log("Camera at index " + currentCamera + " values saved.");
    }//GEN-LAST:event_saveButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        if(JOptionPane.showConfirmDialog(null,
                "Would you like to save before exiting?",
                "Exiting", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            cameraList.get(currentCamera).setChannel(Integer.parseInt(rChannelTextField.getText()));
            cameraList.get(currentCamera).setTChannel(Integer.parseInt(tChannelTextField.getText()));
            writeToFile();
        }
        LenzLogger.log("GUI closed");
        //LenzLogger.log("GUI closed at " + new SimpleDateFormat("hh:mm").format(new Date()));
        LenzLogger.close();
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    private void powerAmpComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_powerAmpComboBoxActionPerformed
        cameraList.get(currentCamera).setPowerLevel(powerAmpComboBox.getSelectedIndex());
    }//GEN-LAST:event_powerAmpComboBoxActionPerformed

    private void rUploadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rUploadButtonActionPerformed
        Thread t = new Thread(() -> {
            rUploadButton.setSelected(true);
            SerialCommunicator serialComm = new SerialCommunicator(DeviceType.DOGBONE);
            int[] contents = cameraList.get(currentCamera).getContents();
            errorThrown = serialComm.serialErrorThrown();

            if(!errorThrown){
                for(int i = 0; i < contents.length-1; i++){
                    try{
                        int index = i+1;
                        String outputString = (index + "," + contents[i] + ";");
                        LenzLogger.log(outputString);
                        serialComm.serialWrite(outputString);
                    }
                    catch(Exception e){
                        errorThrown = true;
                        break;
                    }
                }            
                lastRChannel = cameraList.get(currentCamera).getChannel();
                setLabelToSuccess(rStatusLabel);
                LenzLogger.log("last-r: " + Integer.toString(lastRChannel) +
                        ",last-t: " + Integer.toString(lastTChannel));
                if(lastRChannel!=lastTChannel){
                    setLabelToWarning(tStatusLabel);
                }
                else{
                    setLabelToSuccess(tStatusLabel);
                }
            }
            else{
                JOptionPane.showMessageDialog(null,
                    "Error Uploading. Serial communications port in use or not found.",
                    "Alert", JOptionPane.ERROR_MESSAGE);
                dialogFrame.setVisible(false);
                rUploadButton.setSelected(false);
                return;
            }

            if(!errorThrown){
                JOptionPane.showMessageDialog(null,
                        "Settings have finished uploading.",
                        "Success", JOptionPane.OK_OPTION);
            }
            else{            
                JOptionPane.showMessageDialog(null,
                    "Error Uploading. One or more of your settings may not have uploaded.",
                    "Alert", JOptionPane.ERROR_MESSAGE);
                rUploadButton.setSelected(false);
            }

            errorThrown = false;
            serialComm.close();
            writeToFile();
            lastRChannel = cameraList.get(currentCamera).getChannel();
            setLabelToSuccess(rStatusLabel);
            LenzLogger.log("last-r" + Integer.toString(lastRChannel) +
                        ",last-t:" + Integer.toString(lastTChannel));
        });
        t.start();
    }//GEN-LAST:event_rUploadButtonActionPerformed

    private void tUploadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tUploadButtonActionPerformed
        Thread t = new Thread(() -> {
            tUploadButton.setSelected(true);
            SerialCommunicator serialComm = new SerialCommunicator(DeviceType.TRANSMITTER);
            int[] contents = cameraList.get(currentCamera).getContents();
            errorThrown = serialComm.serialErrorThrown();

            if(!errorThrown){
                try{
                    String outputString = (5 + "," + contents[4] + ";");//Powerlevel
                    System.out.println(outputString);
                    serialComm.serialWrite(outputString);

                    outputString = (4 + "," + contents[8] + ";");//Channel
                    System.out.println(outputString);
                    serialComm.serialWrite(outputString);

                    lastTChannel = cameraList.get(currentCamera).getTChannel();
                    setLabelToSuccess(tStatusLabel);
                    LenzLogger.log("last-r" + Integer.toString(lastRChannel) +
                        ",last-t:" + Integer.toString(lastTChannel));
                    if(lastRChannel!=lastTChannel){
                        setLabelToWarning(rStatusLabel);
                    }
                    else{
                        setLabelToSuccess(rStatusLabel);
                    }
                }
                catch(Exception e){
                    System.out.println(e);
                    errorThrown = true;
                }
            }
            else{
                JOptionPane.showMessageDialog(null,
                    "Error Uploading. Serial communications port not found.",
                    "Alert", JOptionPane.ERROR_MESSAGE);
                dialogFrame.setVisible(false);
                tUploadButton.setSelected(false);
                return;
            }

            if(!errorThrown){
                JOptionPane.showMessageDialog(null,
                    "Settings have finished uploading.",
                    "Success", JOptionPane.PLAIN_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null,
                    "Error Uploading. One or more of your settings may not have uploaded.",
                    "Alert", JOptionPane.ERROR_MESSAGE);            
                tUploadButton.setSelected(false);
            }

            errorThrown = false;
            serialComm.close();
            writeToFile();
            lastTChannel = cameraList.get(currentCamera).getTChannel();
            setLabelToSuccess(tStatusLabel);
        });
        t.start();
    }//GEN-LAST:event_tUploadButtonActionPerformed

    private void saveAsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsButtonActionPerformed
        String newName = JOptionPane.showInputDialog("Name the new group of motor settings:");
        if(newName!=null){
            while(newName.contains(";")){
                newName = JOptionPane.showInputDialog("Names cannot contain semicolons, please try another name:");
            }
            LenzLogger.log("Creating new setting set named " + newName
                + " with the settings from camera at index " + currentCamera);
            cameraList.add(new CameraSettings(cameraList.get(currentCamera), newName));
            updateComboBox();
            savedSettingsComboBox.setSelectedIndex(savedSettingsComboBox.getItemCount() - 1);
            currentCamera = savedSettingsComboBox.getSelectedIndex();
            updateGUIValues();
            storeSettingsList();
        } 
    }//GEN-LAST:event_saveAsButtonActionPerformed

    private void saveAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAllButtonActionPerformed
        storeSettingsList();
        writeToFile();
        if(!errorThrown){
            JOptionPane.showMessageDialog(null,
                    "All settings saved.", "Success",JOptionPane.INFORMATION_MESSAGE);
            LenzLogger.log("All settings saved.");
        }
        errorThrown = false;
    }//GEN-LAST:event_saveAllButtonActionPerformed

    private void timeLapseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeLapseButtonActionPerformed
        LenzLogger.log("Starting timelapse utility.");
        if(cameraList.get(currentCamera).getSavedPositions()[0]
                == CameraSettings.UNSET_SAVED_POSITION){//Can't set them individually
            LenzLogger.log("No positions set, aborting timelapse utility...");
            JOptionPane.showMessageDialog(null,"No saved positions to calculate from."
                    + "Please poll device before usinng the timelapse utility.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            timeLapseButton.setSelected(false);
            return;
        }
        timeLapseWindow.setCamera(cameraList.get(currentCamera));
        timeLapseButton.setSelected(true);
        timeLapseWindow.show();
    }//GEN-LAST:event_timeLapseButtonActionPerformed

    private void tPollButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tPollButtonActionPerformed
        startImportPoll(DeviceType.TRANSMITTER);
    }//GEN-LAST:event_tPollButtonActionPerformed

    private void rPollButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rPollButtonActionPerformed
        startImportPoll(DeviceType.DOGBONE);
    }//GEN-LAST:event_rPollButtonActionPerformed

    private void firmwareButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firmwareButtonActionPerformed
        LenzLogger.log("Opening firmware update utility.");
        firmwareUtility = new FirmwareUtility();
        firmwareUtility.show();
    }//GEN-LAST:event_firmwareButtonActionPerformed
    // </editor-fold> 
    
    private void startImportPoll(DeviceType dType){
        Thread t = new Thread(() -> {
            //Custom button text
            SerialCommunicator sComms = null;
            boolean valueImportDidSucceed = false;
            Object[] options = {"Create New",
                                "Overwrite",
                                "Cancel"};
            LenzLogger.log("Begining Import from Device");
            int n = JOptionPane.showOptionDialog(this,
                "Would you like to create a new settings profile "
                + "or overwrite current settings?",
                "Poll settings from a connected device.",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

            if(n == 0)
                newButtonActionPerformed(null);
            if(n == 2)
                return;

            try{
                sComms = new SerialCommunicator(dType);
                if(!sComms.serialErrorThrown())
                    valueImportDidSucceed = CameraSettings.readImport(sComms,cameraList.get(currentCamera), this);
                else
                    throw new Exception("Port not found");
                if(!valueImportDidSucceed)
                    JOptionPane.showMessageDialog(null,
                        "Error Importing. Please try again.",
                        "Alert", JOptionPane.ERROR_MESSAGE);
            }
            catch(Exception ex){
                System.out.println(ex);
                LenzLogger.log(ex.toString());
                System.out.println("Error Importing. Serial communications port not found." +
                                    " Please insure device is connected and try again.");
                JOptionPane.showMessageDialog(null,
                        "Error Importing. Serial communications port not found."+
                        " Please insure device is connected and try again.",
                        "Alert", JOptionPane.ERROR_MESSAGE);
            }
            finally{
                if(sComms != null)
                    sComms.close();
                errorThrown = false;
            }
            updateGUIValues();
            //saving
            writeToFile();
        });
        t.start();
    }
    
    private void setUpListeners(){
        // <editor-fold defaultstate="collapsed" desc="Slider Listeners">
        velocitySlider.addChangeListener((ChangeEvent e) -> {
            if(textEdit || editLock)
                textEdit = false;
            else{
                int rawValue = velocitySlider.getValue();
                double roundedValue = roundNumber(rawValue);
                int newValue = (int) Math.pow(2, roundedValue);
                velocitySlider.setValue((int)(roundedValue * 100));
                cameraList.get(currentCamera).setVelocity(newValue);
                velocityTextField.setText(Integer.toString(newValue));
                LenzLogger.log("Velocity set to " + newValue);
                rUploadButton.setSelected(false);
                timeLapseButton.setSelected(false);
            }
        });
        accelerationSlider.addChangeListener((ChangeEvent e) -> {
            if(textEdit || editLock)
                textEdit = false;
            else{
                int rawValue = accelerationSlider.getValue();
                double roundedValue = roundNumber(rawValue);
                int newValue = (int) Math.pow(2, roundedValue);
                accelerationSlider.setValue((int)(roundedValue * 100));
                cameraList.get(currentCamera).setAcceleration(newValue);
                accelerationTextField.setText(Integer.toString(newValue));
                LenzLogger.log("Acceleration set to " + newValue);
                rUploadButton.setSelected(false);
                timeLapseButton.setSelected(false);
            } 
        });
        zVelocitySlider.addChangeListener((ChangeEvent e) -> {
            if(textEdit || editLock)
                textEdit = false;
            else{
                int rawValue = zVelocitySlider.getValue();
                double roundedValue = roundNumber(rawValue);
                int newValue = (int) Math.pow(2, roundedValue);
                zVelocitySlider.setValue((int)(roundedValue * 100));
                cameraList.get(currentCamera).setZVelocity(newValue);
                zVelocityTextField.setText(Integer.toString(newValue));
                LenzLogger.log("zVelocity set to " + newValue);
                rUploadButton.setSelected(false);
            }
        });
        zAccelerationSlider.addChangeListener((ChangeEvent e) -> {
            if(textEdit || editLock)
                textEdit = false;
            else{
                int rawValue = zAccelerationSlider.getValue();
                double roundedValue = roundNumber(rawValue);
                int newValue = (int) Math.pow(2, roundedValue);
                zAccelerationSlider.setValue((int)(roundedValue * 100));
                cameraList.get(currentCamera).setZAcceleration(newValue);
                zAccelerationTextField.setText(Integer.toString(newValue));
                LenzLogger.log("zAcceleration set to " + newValue);
                rUploadButton.setSelected(false);
            }
        });
        // </editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="TextField Listeners">
        rChannelTextField.addActionListener((ActionEvent e) -> {
            rChannelTextChanged();
        });        
        this.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent fe) {
            
            }

            @Override
            public void focusLost(FocusEvent fe) {
                rChannelTextChanged();
            }
        });        
        rChannelTextField.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent fe) {
            
            }

            @Override
            public void focusLost(FocusEvent fe) {
                rChannelTextChanged();
            }
        });
        
        tChannelTextField.addActionListener((ActionEvent e) -> {
            tChannelTextChanged();
        });
        tChannelTextField.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent fe) {
            
            }

            @Override
            public void focusLost(FocusEvent fe) {
                tChannelTextChanged();
            }
        });
        
        velocityTextField.addActionListener((ActionEvent e) -> {
            velocityTextChanged();
        });
        velocityTextField.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent fe) {
            
            }

            @Override
            public void focusLost(FocusEvent fe) {
                velocityTextChanged();
            }
        });
        
        accelerationTextField.addActionListener((ActionEvent e) -> {
            accelerationTextChanged();
        });
        accelerationTextField.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent fe) {
            
            }

            @Override
            public void focusLost(FocusEvent fe) {
                accelerationTextChanged();
            }
        });
        
        zVelocityTextField.addActionListener((ActionEvent e) -> {
            zVelocityTextChanged();
        });
        zVelocityTextField.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent fe) {
            
            }

            @Override
            public void focusLost(FocusEvent fe) {
                zVelocityTextChanged();
            }
        });
        
        zAccelerationTextField.addActionListener((ActionEvent e) -> {
            zAccelerationTextChanged();
        });
        zAccelerationTextField.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent fe) {
            
            }

            @Override
            public void focusLost(FocusEvent fe) {
                zAccelerationTextChanged();
            }
        });
        // </editor-fold> 
        
        //exit behaivior when exiting using window controls
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(JOptionPane.showConfirmDialog(null,
                        "Would you like to save before exiting?",
                        "Exiting", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    writeToFile();
                }
                LenzLogger.log("GUI closed at "
                    + new SimpleDateFormat("hh:mm").format(new Date()));
                LenzLogger.close();
            }

        });
        addWindowFocusListener(new WindowFocusListener()
        {
            @Override
            public void windowGainedFocus(WindowEvent we)
            {
                if(timeLapseWindow.isVisible())
                    timeLapseWindow.requestFocus();
                else if(firmwareUtility.isVisible())
                    firmwareUtility.requestFocus();
               updateGUIValues();
            }

            @Override
            public void windowLostFocus(WindowEvent we)
            {               
            }
        });
        
        logoLabel.addMouseListener(new MouseAdapter(){  
            public void mouseClicked(MouseEvent e)  
            {
                try {
                    openWebpage(new URL("https://www.motiondogs.com/").toURI());
                } catch (Exception ex) {
                    LenzLogger.log("Unable to open website.");
                    ex.printStackTrace();
                }
            }  
        }); 
    }
    
    // <editor-fold defaultstate="collapsed" desc="Text Change Callbacks">
    private void rChannelTextChanged(){
        int textValue;
        try{
            textValue = Integer.parseInt((rChannelTextField.getText()));
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,
                    "Invalid reciever channel value.", "Error Message",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(textValue < 0 || textValue > MAX_CHANNEL){
            rChannelTextField.setText(Integer.toString(cameraList.get(currentCamera).getChannel()));
            JOptionPane.showMessageDialog(null,
                    "Invalid channel value.", "Error Message",
                    JOptionPane.ERROR_MESSAGE);
        }
        else{
            cameraList.get(currentCamera).setChannel(textValue);
            LenzLogger.log("Channel changed to " + textValue);
        }
        rUploadButton.setSelected(false);
    }    
    private void tChannelTextChanged(){
        int textValue;
            try{
                textValue = Integer.parseInt(tChannelTextField.getText());
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null,
                        "Invalid transmitter channel value.", "Error Message",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if(textValue < 0 || textValue > MAX_CHANNEL){
                tChannelTextField.setText(Integer.toString(cameraList.get(currentCamera).getChannel()));
                JOptionPane.showMessageDialog(null,
                        "Invalid channel value.", "Error Message",
                        JOptionPane.ERROR_MESSAGE);
            }
            else{
                cameraList.get(currentCamera).setTChannel(textValue);
                LenzLogger.log("Set tChannel to " + textValue);
            }
            tUploadButton.setSelected(false);
    }
    private void velocityTextChanged(){
            int textValue = Integer.parseInt((velocityTextField.getText()));
            if(textValue < 0 || textValue > MAX_VELOCITY){
                velocityTextField.setText(Integer.toString(velocitySlider.getValue()));
                JOptionPane.showMessageDialog(null,
                        "Invalid velocity value.", "Error Message",
                        JOptionPane.ERROR_MESSAGE);
            }
            else{
                textEdit = true;
                velocitySlider.setValue(findPower(textValue));
                cameraList.get(currentCamera).setVelocity(textValue);
                LenzLogger.log("Velocity set to " + textValue);
            }
            rUploadButton.setSelected(false);
            timeLapseButton.setSelected(false);
        }
    private void accelerationTextChanged(){
        int textValue = Integer.parseInt((accelerationTextField.getText()));
            if(textValue < 0 || textValue > MAX_ACCELERATION){
                accelerationTextField.setText(Integer.toString(accelerationSlider.getValue()));
                JOptionPane.showMessageDialog(null,
                        "Invalid acceleration value.", "Error Message",
                        JOptionPane.ERROR_MESSAGE);
            }
            else{
                textEdit = true;
                accelerationSlider.setValue(findPower(textValue));
                cameraList.get(currentCamera).setAcceleration(textValue);
                LenzLogger.log("Acceleration set to " + textValue);
            }
            rUploadButton.setSelected(false);
            timeLapseButton.setSelected(false);
    }
    private void zVelocityTextChanged(){
        int textValue = Integer.parseInt((zVelocityTextField.getText()));
            if(textValue < 0 || textValue > MAX_VELOCITY){
                zVelocityTextField.setText(Integer.toString(zVelocitySlider.getValue()));
                JOptionPane.showMessageDialog(null,
                        "Invalid Z-mode velocity value.", "Error Message",
                        JOptionPane.ERROR_MESSAGE);
            }
            else{
                textEdit = true;
                zVelocitySlider.setValue(findPower(textValue));
                cameraList.get(currentCamera).setZVelocity(textValue);
                LenzLogger.log("zVelocity set to " + textValue);
            }
            rUploadButton.setSelected(false);
    }
    private void zAccelerationTextChanged(){
        int textValue = Integer.parseInt((zAccelerationTextField.getText()));
            if(textValue < 0 || textValue > MAX_ACCELERATION){
                zAccelerationTextField.setText(Integer.toString(zAccelerationSlider.getValue()));
                JOptionPane.showMessageDialog(null,
                        "Invalid Z-mode acceleration value.", "Error Message",
                        JOptionPane.ERROR_MESSAGE);
            }
            else{
                textEdit = true;
                zAccelerationSlider.setValue(findPower(textValue));
                cameraList.get(currentCamera).setZAcceleration(textValue);
                LenzLogger.log("zAcceleration set to " + textValue);
            }
            rUploadButton.setSelected(false);
    }
    // </editor-fold>
    
    
    public static void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void updateComboBox(){
        String[] nameArray = new String[cameraList.size()];
        for(int i = 0; i<nameArray.length; i++){
            nameArray[i] = cameraList.get(i).getName();
        }
        savedSettingsComboBox.setModel(new javax.swing.DefaultComboBoxModel(nameArray));
    }
    
    private void writeToFile(){
        Thread t = new Thread(() -> {
            PrintWriter writer = null;
            errorThrown = false;
            try{
                LenzLogger.log("Using file: " + DATA.getPath());
                try{
                    if(OSUtils.isWindows())
                        OSUtils.setShow(DATA.toPath());
                }
                catch(Exception e){
                    System.out.println(e.toString());
                    LenzLogger.log(e.toString());
                }
                if(!DATA.exists()){
                    DATA.createNewFile();
                }
                writer = new PrintWriter(DATA);
                LenzLogger.log("Beginning file write...");
                for(CameraSettings i:lastCameraListSaved){
                    writer.println(i.saveString());
                    LenzLogger.log("Writing " + i.getName());
                }
                LenzLogger.log("Ending file write.");
            }
            catch(IOException e){
                JOptionPane.showMessageDialog(null,
                        "Error saving. One or more of your settings may not have been saved.",
                        "Alert", JOptionPane.ERROR_MESSAGE);
                System.out.println(e.toString());
                LenzLogger.log(e.toString());
                errorThrown = true;
            }
            finally{
                if(writer!=null)
                    writer.close();
            }

            try{
                if(OSUtils.isWindows())
                    OSUtils.setHide(DATA.toPath());
            }
            catch(Exception e){
                LenzLogger.log("Not a pc, skipping file attribute set.");
            }
        });
        t.start();
    }

    private void loadFromFile(){
        BufferedReader lineReader;
        try{
            try{
                if(OSUtils.isWindows())
                    OSUtils.setShow(DATA.toPath());
            }
            catch(Exception e){
                System.out.println("Not a pc, skipping file attribute set.");
                LenzLogger.log("Not a pc, skipping file attribute set.");
            }
            
            String line;
            if(!DATA.exists())
                throw new IOException("Saved data not found. A new file will be created.");
            lineReader = new BufferedReader(new FileReader(DATA));
            while((line=lineReader.readLine())!=null){
                CameraSettings newCamera = new CameraSettings(line.split(";"));
                if(!newCamera.errorThrown())
                    cameraList.add(newCamera);
                else
                    throw new IOException("Error Loading. One or more of your settings may not have loaded.");
            }
            lineReader.close();
        }
        catch(IOException ex){
            JOptionPane.showMessageDialog(null,
                    //the following line cuts off the Java.IOException prefix to the error string
                    ex.toString().split(":")[1],
                    "Alert", JOptionPane.ERROR_MESSAGE);
            System.out.println(ex.toString());
            LenzLogger.log(ex.toString());
        }
        finally{
            if(cameraList.isEmpty()){
                cameraList.add(new CameraSettings(DEFAULT_SETTINGS_NAME));
            }
        }
    }
    
    private static void setLabelToWarning(JLabel statusLabel){
        statusLabel.setForeground(Color.red);
        statusLabel.setText("Communication out of sync.");
        statusLabel.setToolTipText("The channel last uploaded to this device has not been updated to "
                                      + "the most recently uploaded channel.");
    }
    
    private static void setLabelToSuccess(JLabel statusLabel){
        statusLabel.setForeground(Color.black);
        statusLabel.setText("");//("Communications Synced");
        statusLabel.setToolTipText("The most recently uploaded channels match across devices.");
    }
    
    /**
     *
     * @param selected
     */
    public static void setTimeLapseButtonSelected(boolean selected){
        timeLapseButton.setSelected(selected);
    }
    
    /**
     *This function toggles the enabled status of the button
     * that brings users to the firmware update utility form.
     * @param newStatus
     */
    public static void setFirmwareUpdateEnabled(boolean newStatus){
        if(newStatus == true)
            firmwareButton.setEnabled(true);
        if(newStatus == false)
            firmwareButton.setEnabled(false);
        firmwareButton.setToolTipText("Unable to upload new programming to device. "
                                     +"Some necessary files are missing.");
    }
    private static void setUpDialogFrame(){
        dialogFrame.setSize(400, 100);
        dialogFrame.setLayout(new BorderLayout());
        dialogFrame.getContentPane().add(new JLabel("Uploading Settings. This should take no longer than thirty seconds",
            JLabel.CENTER),BorderLayout.CENTER);
        //shows the frame at the center of the screen
        dialogFrame.setLocationRelativeTo(null);
    }
    
    private void setUpKFM(){
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
        .addPropertyChangeListener("permanentFocusOwner", (final PropertyChangeEvent e) -> {
            if (e.getOldValue() instanceof JTextField){
                SwingUtilities.invokeLater(() -> {
                    JTextField oldTextField = (JTextField)e.getOldValue();
                    oldTextField.setText(oldTextField.getText());
                    oldTextField.dispatchEvent(new ActionEvent(oldTextField,
                        ActionEvent.ACTION_PERFORMED, "SET_NEW_VALUE"));
                });
            }
            
            if (e.getNewValue() instanceof JTextField){
                SwingUtilities.invokeLater(() -> {
                    JTextField textField = (JTextField)e.getNewValue();
                    textField.selectAll();
                });                
            }
        });
    }                
    
    private static void showSplashScreen(){
        splash = new SplashForm();
    }
    
    private static void hideSplashScreen(){
        splash.hide();
    }
    
    public void checkLabel(boolean rLabel){
        if(rLabel){
            if(lastRChannel!=lastTChannel){
                setLabelToWarning(tStatusLabel);
            }
            else{
                setLabelToSuccess(tStatusLabel);
            }
        }
        else{
           if(lastRChannel!=lastTChannel){
                setLabelToWarning(rStatusLabel);
            }
            else{
                setLabelToSuccess(rStatusLabel);
            } 
        }
    }
    
    void updateGUIValues(){
        editLock = true;
        LenzLogger.log("Beginning update of all GUI Values");
        powerAmpComboBox.setSelectedIndex(cameraList.get(currentCamera).getPowerLevel());
        tChannelTextField.setText(Integer.toString(cameraList.get(currentCamera).getTChannel()));
        rChannelTextField.setText(Integer.toString(cameraList.get(currentCamera).getChannel()));
        velocityTextField.setText(Integer.toString(cameraList.get(currentCamera).getVelocity()));
        accelerationTextField.setText(Integer.toString(cameraList.get(currentCamera).getAcceleration()));
        zVelocityTextField.setText(Integer.toString(cameraList.get(currentCamera).getZVelocity()));
        zAccelerationTextField.setText(Integer.toString(cameraList.get(currentCamera).getZAcceleration()));
        velocitySlider.setValue(findPower(cameraList.get(currentCamera).getVelocity()));
        accelerationSlider.setValue(findPower(cameraList.get(currentCamera).getAcceleration()));
        zVelocitySlider.setValue(findPower(cameraList.get(currentCamera).getZVelocity()));
        zAccelerationSlider.setValue(findPower(cameraList.get(currentCamera).getZAcceleration()));
        
        int numPositionsSet = 0;
        for(int i = 0; i < 4; i++){
            if(cameraList.get(currentCamera).getSavedPositions()[i] != CameraSettings.UNSET_SAVED_POSITION){
                numPositionsSet++;
            }
        }
        if(numPositionsSet>=2){
            timeLapseButton.setEnabled(true);
            timeLapseButton.setToolTipText(TIMELAPSE_ACTIVE_TOOLTIP);
        } else{
            timeLapseButton.setEnabled(false);
            timeLapseButton.setToolTipText(TIMELAPSE_INACTIVE_TOOLTIP);
        }
        
        editLock = false;
    }
    
    private static int roundNumber(int rawValue){
        return (int) (rawValue * .01 + .5);//rounds to  nearst int
    }
    
    private int findPower(int textValue){
        double pow = Math.log(textValue)/Math.log(2);
        return (int)(pow*100);
    }
    
    public String exposeCurrentValues(){
        StringBuilder returnString = new StringBuilder();
        returnString.append("Current Values:");
        returnString.append("\n\ttextEdit = ").append(textEdit);
        returnString.append("\n\teditLock = ").append(editLock);
        returnString.append("\n\terrorThrown = ").append(errorThrown);
        returnString.append("\n\tlast T-Channel: ").append(lastTChannel);
        returnString.append("\n\tlast R-Channel: ").append(lastRChannel);
        return returnString.toString();
    }
    
    private void storeSettingsList(){
        lastCameraListSaved = new ArrayList<>();
        for(CameraSettings i:cameraList){
            lastCameraListSaved.add(new CameraSettings(i));
        }
        LenzLogger.log("Successfully stored. Length: " + lastCameraListSaved.size());
    }
    
    public static void newCameraSettings(CameraSettings cameraIn){
        LenzLogger.log("New settings recieved.");
        cameraList.get(currentCamera).copySettings(cameraIn);
        rUploadButton.setSelected(false);
    }
    
    private static int currentCamera = 0;
    public int lastTChannel = 0;
    public int lastRChannel = 0;
    private static final ArrayList<CameraSettings> cameraList = new ArrayList<>();
    private ArrayList<CameraSettings> lastCameraListSaved = new ArrayList<>();
    private static boolean errorThrown = false;
    private boolean textEdit = false;
    private boolean editLock = false;
    private static final JFrame dialogFrame = new JFrame();    
    private static TimeLapse timeLapseWindow = null;
    protected static FirmwareUtility firmwareUtility = null;
    private static final int MAX_VELOCITY = 32768;
    private static final int MAX_ACCELERATION = 1024;
    private static final int MAX_CHANNEL = 81;
    private static final String DEFAULT_SETTINGS_NAME = "Base Setup";
    private static final String TIMELAPSE_ACTIVE_TOOLTIP = "A calculator utility for setting up timelapse shots.";
    private static final String TIMELAPSE_INACTIVE_TOOLTIP = "Calclutor cannot be used without polling saved " + 
                                                             "positions from your TXR";
    private static SplashForm splash;
    public static String lenzhoundDirectory = null;
    private static File DATA = null;//
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel accelerationLabel;
    private javax.swing.JSlider accelerationSlider;
    private javax.swing.JTextField accelerationTextField;
    private javax.swing.JLabel channelLabel;
    private javax.swing.JLabel channelLabel2;
    private javax.swing.JButton defaultButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton exitButton;
    protected static javax.swing.JButton firmwareButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JButton newButton;
    private javax.swing.JLabel normalModeLabel;
    private javax.swing.JComboBox powerAmpComboBox;
    private javax.swing.JLabel powerAmpLabel;
    private javax.swing.JTextField rChannelTextField;
    private javax.swing.JButton rPollButton;
    private javax.swing.JLabel rStatusLabel;
    private static javax.swing.JToggleButton rUploadButton;
    private javax.swing.JButton saveAllButton;
    private javax.swing.JButton saveAsButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JComboBox savedSettingsComboBox;
    private javax.swing.JTextField tChannelTextField;
    private javax.swing.JButton tPollButton;
    private javax.swing.JLabel tStatusLabel;
    private javax.swing.JToggleButton tUploadButton;
    private static javax.swing.JToggleButton timeLapseButton;
    private javax.swing.JLabel velocityLabel;
    private javax.swing.JSlider velocitySlider;
    private javax.swing.JTextField velocityTextField;
    private javax.swing.JLabel versionLabel;
    private javax.swing.JLabel zAccelerationLabel;
    private javax.swing.JSlider zAccelerationSlider;
    private javax.swing.JTextField zAccelerationTextField;
    private javax.swing.JLabel zModeLabel;
    private javax.swing.JLabel zVelocityLabel;
    private javax.swing.JSlider zVelocitySlider;
    private javax.swing.JTextField zVelocityTextField;
    // End of variables declaration//GEN-END:variables
}

