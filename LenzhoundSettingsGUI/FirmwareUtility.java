/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lenzhoundgui;

import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 *
 * @author Thayer
 */
public final class FirmwareUtility extends javax.swing.JFrame
                             implements PropertyChangeListener{

    /**
     * Creates new form FirmwareUtility
     */
    public FirmwareUtility() {
        initComponents();
        setUpListeners();
        setLocationRelativeTo(null);
        FileNameExtensionFilter hexFilter = new FileNameExtensionFilter("Hex Files", "hex");
        fc.addChoosableFileFilter(hexFilter);
        fc.setFileFilter(hexFilter);
        updatePortComboBox();
    }
    String updatePath = MainGUI.lenzhoundDirectory + "avr" + File.separator;
    final JFileChooser fc = new JFileChooser();

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
        String cmd = null;
        Frame linkedFrame = null;
        
        public Task(Frame in){
            linkedFrame = in;
        }
        @Override
        public Void doInBackground() {
            try{
                SerialCommunicator sComms = new SerialCommunicator();
                setProgress(5);
                LenzLogger.log("Update Started...");
                
                if(sComms.serialErrorThrown()){
                    LenzLogger.log("Unable to initially open port. Ensure the"
                        + " correct port is being targeted. Process is being"
                        + "aborted.");
                    return null;
                }
                
                sComms.flushBuffer();
                setProgress(20);
                LenzLogger.log("Buffer flushed", 1);
                
                sComms.resetPort();
                setProgress(50);
                LenzLogger.log("Port Reset", 1);
                
                cmd = updatePath + "avrdude -v -v -v -v "
                    +"-C " + updatePath + "avrdude.conf -p atmega32u4 -cavr109 -P "
                    +SerialCommunicator.findBootLoaderPort()+" -b19200 -Uflash:w:"
                    + fileLocationField.getText() + ":i";
                LenzLogger.log(cmd, 1);
                setProgress(35);
                Process process = Runtime.getRuntime().exec(cmd);
                
                uploadProgressBar.setIndeterminate(true);
                
                InputStream inStream = process.getErrorStream();
                StringBuilder b = new StringBuilder();
                while(process.isAlive()){
                    while(inStream.available() > 0){
                        char holder = ((char)inStream.read());
                        LenzLogger.log(holder);
                        b.append(holder);
                    }
                }
                //perform loop once more to get last bits of output
                while(inStream.available() > 0){
                        char holder = ((char)inStream.read());
                        LenzLogger.log(holder);
                        b.append(holder);
                }
                System.out.println("Exit Value: " + process.exitValue());
                
                uploadProgressBar.setIndeterminate(false);
                if(process.exitValue() == 0){
                    setProgress(100);
                    JOptionPane.showMessageDialog(null,
                        "Device firmware has been updated.", "Success!",
                        JOptionPane.INFORMATION_MESSAGE);                    
                }
                else{
                    setProgress(0);
                    JOptionPane.showMessageDialog(null,
                        "Upload did not complete successfully", "Error Message",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(SerialPortException | IOException e){
                System.out.println(e);
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

        setTitle("Firmware Update Utility");
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(uploadProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(portComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fileLocationField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(locateButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(startButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(21, Short.MAX_VALUE))
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

        Task task = new Task(this);
        task.addPropertyChangeListener(this);
        task.execute();
    }//GEN-LAST:event_startButtonActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField fileLocationField;
    private javax.swing.JButton locateButton;
    private javax.swing.JComboBox portComboBox;
    private javax.swing.JButton startButton;
    private javax.swing.JProgressBar uploadProgressBar;
    // End of variables declaration//GEN-END:variables
}
