/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lenzhoundgui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;
import jssc.SerialPortList;

/**
 *
 * @author Thayer
 */
public final class FirmwareUtility extends javax.swing.JFrame
                             implements PropertyChangeListener{
    
    static String updatePath = MainGUI.lenzhoundDirectory + "avr" + File.separator;
    int TOGGLE_HEIGHT_CONSTANT;
    final JFileChooser fc = new JFileChooser();
    boolean outputHidden = false;
    StringBuilder b = null;
    InputStream inStream = null;
    InputStream outStream = null;
    Process process = null;
    static final String TEXT_HIDDEN = "Show Advanced Output   ▼";
    static final String TEXT_SHOWN = "Hide Advanced Output     ";
    Thread a;

    /**
     * Creates new form FirmwareUtility
     */
    public FirmwareUtility() {
        initComponents();
        TOGGLE_HEIGHT_CONSTANT = outputField.getHeight() + 25;
        setUpListeners();
        setLocationRelativeTo(null);
        FileNameExtensionFilter hexFilter = new FileNameExtensionFilter("Hex Files", "hex");
        fc.addChoosableFileFilter(hexFilter);
        fc.setFileFilter(hexFilter);
        updatePortComboBox();
        outputToggleButtonActionPerformed(null);
        
        //auto-scrolls the textarea to the bottom
        DefaultCaret caret = (DefaultCaret)outputField.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    public void updatePortComboBox(){
        String[] availablePorts = SerialPortList.getPortNames();
        if(availablePorts.length > 0){
            portComboBox.setModel(new javax.swing.DefaultComboBoxModel(availablePorts));
            if(fileLocationField.getText().endsWith(".hex"))
                startButton.setEnabled(true);
        }
        else{
            availablePorts = new String[]{"No ports found"};
            portComboBox.setModel(new javax.swing.DefaultComboBoxModel(availablePorts));
            startButton.setEnabled(false);
        }
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())) {
            int progress = (Integer) evt.getNewValue();
            uploadProgressBar.setValue(progress);
        } 
    }
    
    public static void findNecessaryFiles(){
        File avrdude = new File(updatePath + "bin/avrdude");
        File bin = new File(updatePath + "bin/avrdude_bin");
        if(!avrdude.exists()||!bin.exists()){
            LenzLogger.log("Missing files required for flashing device. Firmware"
                            +" updates will be disabled");
            MainGUI.setFirmwareUpdateEnabled(false);
            return;
        }
        checkFilePermissions(avrdude,bin);
    }
    
    private static void checkFilePermissions(File avrdude, File bin){
        String[] cmd = new String[]{avrdude.getAbsolutePath()};
        StringBuilder b1 = new StringBuilder();
        StringBuilder b2 = new StringBuilder();
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            InputStream stream1 = process.getInputStream();
            InputStream stream2 = process.getErrorStream();
            int in;
            while(process.isAlive()){
                try {
                    Thread.sleep(300);
                    if((in = stream1.read()) != -1)
                        b1.append((char)in);
                    if((in = stream2.read()) != -1)
                        b2.append((char)in);
                } catch (InterruptedException e) {System.out.println(e.toString());}
            }
            while(stream1.available() > 0){
                b2.append((char)stream1.read());
            }
            while(stream2.available() > 0){
                b2.append((char)stream2.read());
            }
        } catch (IOException ex) {
            LenzLogger.log("Avrdude test failed, likely a permissions issue.");
        }
        System.out.print(b1.toString());
        System.out.println(b2.toString());
        
        if(process!=null){
            System.out.println("avrdude exit value: " + process.exitValue());
            if(process.exitValue()==0)
                return;
        }
        
        if(!OSUtils.isWindows()){
            String[] avrPerm = new String[]{"chmod", "755", avrdude.getAbsolutePath()};
            String[] binPerm = new String[]{"chmod", "755", bin.getAbsolutePath()};
            try {
                Runtime.getRuntime().exec(avrPerm);
                Runtime.getRuntime().exec(binPerm);

            } catch (IOException ex) {
                LenzLogger.log("Setting Permissions Failed");            
            }
        }    
    }
    
    public void setUpListeners(){
        portComboBox.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent pme) {
                updatePortComboBox();
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent pme) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent pme) {
            }
         });
    }
    
    class Task extends SwingWorker<Void, Void> {
        String[] cmd = null;
        Frame linkedFrame = null;
        
        public Task(Frame in){
            linkedFrame = in;
        }
        @Override
        public Void doInBackground() {
            portComboBox.setEnabled(false);
            locateButton.setEnabled(false);
            try{
                SerialCommunicator sComms = new SerialCommunicator();
                setProgress(5);
                LenzLogger.log("Update Started...");
                
                if(sComms.serialErrorThrown()){
                    LenzLogger.log("Unable to initially open port. Ensure the"
                        + " correct port is being targeted. Process is being "
                        + "aborted.");
                    throw new Exception("Unable to initially open port. Ensure the"
                        + " correct port is being targeted. Process is being "
                        + "aborted.");
                }
                
                sComms.flushBuffer();
                setProgress(20);
                LenzLogger.log("Buffer flushed", 1);
                
                sComms.resetPort();
                setProgress(25);
                LenzLogger.log("Port Reset", 1);
                
                if(OSUtils.isWindows()){
                    String targetPort = SerialCommunicator.findBootLoaderPort();
                    if(targetPort != null)
                        cmd = new String[]{updatePath + "bin/avrdude.exe", "-v",
                            "-C" + updatePath + "etc/avrdude.conf", "-p", "atmega32u4",
                            "-cavr109", "-P" + targetPort, "-b57600", "-D",
                            "-Uflash:w:" + fileLocationField.getText() + ":i"};
                    else //handles the case of no code on device, todo:differentiate from actual broken cases
                        cmd = new String[]{updatePath + "bin/avrdude.exe", "-v",
                            "-C" + updatePath + "etc/avrdude.conf", "-p", "atmega32u4",
                            "-cavr109", "-P" + portComboBox.getSelectedItem().toString(),
                            "-b57600", "-D", "-Uflash:w:" + fileLocationField.getText() + ":i"};
                }
                else{
                    String portName = fileLocationField.getText();
                    if(portName.startsWith("/dev/"))
                        portName = portName.substring(5);
                    cmd = new String[]{updatePath + "bin/avrdude", "-F", "-v", "-v",
                        "-C" + updatePath + "etc/avrdude.conf", "-patmega32u4",
                        "-cavr109", "-P" + portComboBox.getSelectedItem().toString(),
                        "-b57600", "-D", "-Uflash:w:" + portName + ":i"};
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {}
                    sComms.resetPort();//This delay and reset doesn't make a lot of sense
                                       //but restructuring this block to something neater
                                       //causes avrdude to not connect to device 
                    //throw new Exception("Port search timed out. Port could not be found");
                }
                
                LenzLogger.log(cmd,true,1);
                outputField.append(Arrays.toString(cmd) + '\n');
                setProgress(35);
                process = Runtime.getRuntime().exec(cmd);
                Thread t = new Thread(() -> {
                    JOptionPane.showMessageDialog(null,
                            "Device Firmware is being installed, please allow a few "
                            +"moments for the process\n to complete. You may"
                            +" use your computer for other tasks during this time, "
                            +"but do\n not disconnect the Lenzhound device or attempt"
                            +" to send any other information.",
                            "Begining Process",
                            JOptionPane.INFORMATION_MESSAGE);
                });
                t.start();
                uploadProgressBar.setIndeterminate(true);
                
                inStream = process.getErrorStream();
                outStream = process.getInputStream();
                b = new StringBuilder();
                while(process.isAlive()){
                    while(inStream.available() > 0){
                        char holder = ((char)inStream.read());
                        System.out.print(holder);
                        b.append(holder);
                        outputField.append(String.valueOf(holder));
                    }
                }
                //perform loop once more to get last bits of output
                while(inStream.available() > 0){
                        char holder = ((char)inStream.read());
                        System.out.print(holder);
                        b.append(holder);
                        outputField.append(String.valueOf(holder));
                }
                
                uploadProgressBar.setIndeterminate(false);
                if(process.exitValue() == 0){
                    setProgress(99);
                    JOptionPane.showMessageDialog(null,
                        "Device firmware has been updated.", "Success!",
                        JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    setProgress(0);
                    locateButton.setEnabled(true);
                    portComboBox.setEnabled(true);
                    startButton.setEnabled(true);
                    JOptionPane.showMessageDialog(null,
                        "Upload did not complete successfully", "Error Message",
                        JOptionPane.ERROR_MESSAGE);
                    LenzLogger.log("Device update failed.");
                }
            }
            catch(Exception e){
                setProgress(0);
                locateButton.setEnabled(true);
                portComboBox.setEnabled(true);
                System.out.println(e);
                JOptionPane.showMessageDialog(null,
                        "Upload did not complete successfully", "Error Message",
                        JOptionPane.ERROR_MESSAGE);
            } finally{
                if(b!=null){
                    try {
                        while(outStream.available() > 0){
                            LenzLogger.log((char)outStream.read());
                        }
                        while(inStream.available() > 0){
                            char holder = ((char)inStream.read());
                            b.append(holder);
                            System.out.print(holder);
                            outputField.append(String.valueOf(holder));
                        }
                    } catch (Exception ex) {
                        LenzLogger.log(ex.toString());
                    }
                }
                LenzLogger.logOnly(b.toString());
                System.out.println("Exit Value: " + process.exitValue());
                setProgress(100);
            }
            return null;
        }
    
 
        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            setCursor(null);
            linkedFrame.hide();
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

        uploadProgressBar = new javax.swing.JProgressBar();
        fileLocationField = new javax.swing.JTextField();
        portComboBox = new javax.swing.JComboBox();
        locateButton = new javax.swing.JButton();
        startButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        outputField = new javax.swing.JTextArea();
        outputToggleButton = new javax.swing.JButton();

        setTitle("Firmware Update Utility");
        setLocation(new java.awt.Point(21, 23));
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);

        fileLocationField.setText("Please select a hex file...");
        fileLocationField.setEnabled(false);

        portComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        locateButton.setText("Locate File");
        locateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locateButtonActionPerformed(evt);
            }
        });

        startButton.setText("Start");
        startButton.setEnabled(false);
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        outputField.setColumns(20);
        outputField.setRows(5);
        jScrollPane1.setViewportView(outputField);

        outputToggleButton.setFont(new java.awt.Font("Times New Roman", 0, 10)); // NOI18N
        outputToggleButton.setText("Show Advanced Output   ▼");
        outputToggleButton.setBorderPainted(false);
        outputToggleButton.setContentAreaFilled(false);
        outputToggleButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        outputToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outputToggleButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(uploadProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(portComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(fileLocationField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(locateButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(startButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(outputToggleButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(214, 214, 214)))
                .addGap(21, 21, 21))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(uploadProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(locateButton)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(fileLocationField)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(portComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(outputToggleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void locateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locateButtonActionPerformed
        if(fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
            String selectedFile = fc.getSelectedFile().toString();
            if(selectedFile.endsWith(".hex"))//ensure's the correct file type
                fileLocationField.setText(selectedFile);
            else
                JOptionPane.showMessageDialog(null,
                    "Invalid file type.", "Error Message",
                    JOptionPane.ERROR_MESSAGE);
            
            //ensures that avrdude will target a port
            if(!(portComboBox.getSelectedItem().toString().equals("No ports found")))
                startButton.setEnabled(true);
        }
    }//GEN-LAST:event_locateButtonActionPerformed

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        startButton.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        outputField.setText("");

        Task task = new Task(this);
        task.addPropertyChangeListener(this);
        task.execute();
    }//GEN-LAST:event_startButtonActionPerformed

    private void outputToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outputToggleButtonActionPerformed
        if(outputHidden){
            outputToggleButton.setText(TEXT_SHOWN);
            this.setSize(new Dimension(this.getWidth(),
                    this.getHeight() + TOGGLE_HEIGHT_CONSTANT));
            outputField.show();
            outputHidden = false;
        }
        else{
            outputToggleButton.setText(TEXT_HIDDEN);
            this.setSize(new Dimension(this.getWidth(),
                    this.getHeight() - TOGGLE_HEIGHT_CONSTANT));
            outputField.hide();
            outputHidden = true;
        }
    }//GEN-LAST:event_outputToggleButtonActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField fileLocationField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton locateButton;
    private javax.swing.JTextArea outputField;
    private javax.swing.JButton outputToggleButton;
    private javax.swing.JComboBox portComboBox;
    private javax.swing.JButton startButton;
    private javax.swing.JProgressBar uploadProgressBar;
    // End of variables declaration//GEN-END:variables
}
