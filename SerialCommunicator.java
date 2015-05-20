/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lenzhoundgui;

import jssc.*;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
/**
 *
 * @author Thayer Birch
 */
public class SerialCommunicator{  
    private boolean errorThrown = false;
    static SerialPort serialPort;
    
    SerialCommunicator(){
        try{
            initialize();
        }
        catch(SerialPortException e){
            System.out.println(e);
            errorThrown = true;
        }
    }
    
    SerialCommunicator(DeviceType dType){
        try{
            initialize(dType);
        }
        catch(SerialPortException e){
            System.out.println(e);
            errorThrown = true;
        }
    }
    
    private void initialize(DeviceType dType) throws SerialPortException {
        try{
            searchPorts(dType);
            if(serialPort == null){
                LenzLogger.log("\nCould not find serial port.");
                errorThrown = true;
                return;
            }
            serialPort.openPort();//Open serial port
            serialPort.setParams(SerialPort.BAUDRATE_9600, 
                                 SerialPort.DATABITS_8,
                                 SerialPort.STOPBITS_1,
                                 SerialPort.PARITY_NONE);
        }
        catch(SerialPortException e){
            LenzLogger.log(e.toString());
            System.out.println("Inititialization exception.");
            errorThrown = true;
            if(serialPort!=null)
                serialPort.closePort();
        }
        finally{}
    }
    
    private void searchPorts(DeviceType dType) throws SerialPortException{
        for(int i = 0; i < 10; i++){                
            String[] availablePorts = SerialPortList.getPortNames();
            LenzLogger.log("Scan " + i + ": ");
            LenzLogger.log("\n\n\tPorts Found: " + Arrays.toString(availablePorts));
            for(String s : availablePorts){                
                LenzLogger.log("\n\t" + s);

                serialPort = checkPort(s, dType);
                if(serialPort!= null)
                    return;
            }
        }
    }
    
    private SerialPort checkPort(String portName, DeviceType dType) throws SerialPortException{
        SerialPort tempPort = null;
        try{
            System.out.println("Starting check on " + portName);
            tempPort = new SerialPort(portName);
            tempPort.openPort();
            tempPort.setParams(9600, 8, 1, 0);//Set params.
            serialWrite(tempPort, "0;");
            try{
                Thread.sleep(350);
            }catch(Exception e){
                LenzLogger.log(e.toString());
            }
            String returnedString = new String(tempPort.readBytes());
            tempPort.closePort();
            if(returnedString.contains("Available commands")){
                if(dType == DeviceType.DOGBONE){
                    if(returnedString.contains("Dogbone")){
                        LenzLogger.log("Check success!\n");
                        return tempPort;
                    } else {
                        LenzLogger.log("Wrong device type\n");
                        return null;
                    }
                } else if(dType == DeviceType.TRANSMITTER){
                    if(returnedString.contains("Txr")){
                        LenzLogger.log("Check success!\n");
                        return tempPort;
                    } else {
                        LenzLogger.log("Wrong device type\n");
                        return null;
                    }
                }
                return null;
            }
            else{
                LenzLogger.log("Not a lenzhound device--check failed.\n");
                return null;
            }
        }
        catch(SerialPortException e){            
            if(tempPort!=null)
                tempPort.closePort();
            LenzLogger.log(e.toString());
            LenzLogger.log(portName + " has failed.");
            return null;
        }
    }
    
    private void initialize() throws SerialPortException {
        try{
            searchPorts();
            if(serialPort == null){
                LenzLogger.log("\nCould not find serial port.");
                errorThrown = true;
                return;
            }
            serialPort.openPort();//Open serial port
            serialPort.setParams(SerialPort.BAUDRATE_9600, 
                                 SerialPort.DATABITS_8,
                                 SerialPort.STOPBITS_1,
                                 SerialPort.PARITY_NONE);
        }
        catch(SerialPortException e){
            LenzLogger.log(e.toString());
            System.out.println("Inititialization exception.");
            errorThrown = true;
            if(serialPort!=null)
                serialPort.closePort();
        }
        finally{}
    }
    
    private void searchPorts() throws SerialPortException{
        for(int i = 0; i < 10; i++){                
            String[] availablePorts = SerialPortList.getPortNames();
            LenzLogger.log("Scan " + i + ": ");
            LenzLogger.log("\n\n\tPorts Found: " + Arrays.toString(availablePorts));
            for(String s : availablePorts){                
                LenzLogger.log("\n\t" + s);

                serialPort = checkPort(s);
                if(serialPort!= null)
                    return;
            }
        }
    }
    
    private SerialPort checkPort(String portName) throws SerialPortException{
        SerialPort tempPort = null;
        try{
            System.out.println("Starting check on " + portName);
            tempPort = new SerialPort(portName);
            tempPort.openPort();
            tempPort.setParams(9600, 8, 1, 0);//Set params.
            serialWrite(tempPort, "0;");
            byte[] buffer = tempPort.readBytes(18, 250);
            String returnedString = new String(buffer, "UTF-8");
            tempPort.closePort();
            if(returnedString.equals("Available commands")){
                LenzLogger.log("Check success!\n");
                return tempPort;
            }
            else{
                LenzLogger.log("Incorrect responce--check failed.\n");
                return null;
            }
        }
        catch(SerialPortException | SerialPortTimeoutException | UnsupportedEncodingException e){            
            if(tempPort!=null)
                tempPort.closePort();
            LenzLogger.log(e.toString());
            LenzLogger.log(portName + " has failed.");
            return null;
        }
    }
    
    public synchronized void close() {           
            try {
                if (serialPort != null) { 
                    serialPort.closePort();
                }
            } catch (SerialPortException e) {
                LenzLogger.log(e.toString());
            }
    }
    
    private void serialWrite(SerialPort curPort, String settingOut) throws SerialPortException{
        curPort.writeBytes(settingOut.getBytes());
    }
    
    public void serialWrite(String settingOut) throws SerialPortException{
        serialPort.writeBytes(settingOut.getBytes());
    }
    
    public String serialRead() throws SerialPortException{
        /*byte[] temp = new byte[10];
        while(temp.length < 100){
            serialPort.writeBytes("0;".getBytes());//Ask the device to send data
            //serialPort.readBytes(1);//important for some reason        
            //return serialPort.readBytes();
            System.out.println("serial read data:");
            int x = serialPort.getInputBufferBytesCount();
            if(x > 0)
                System.out.println("\tBytes to read" + x);
            temp = serialPort.readBytes();
            System.out.println('\n' + new String(temp) + '\n');
        }
        System.out.println("Exited loop with " + temp.length);                
        return temp;*/
        StringBuilder returnData = new StringBuilder();
        if(serialPort!=null){
            
            serialPort.writeBytes("0;".getBytes());
            try {
                Thread.sleep(350);//Wait to ensure we get all the transferable data
            } catch (InterruptedException ex) {
                LenzLogger.log(ex.toString());
                LenzLogger.log("Thread stuck waiting for data.");
            }
            int x = serialPort.getInputBufferBytesCount();
            
            while(x>0){
                String temp = new String(serialPort.readBytes());
                returnData.append(temp);
                LenzLogger.log("Bytes read:" + x);
                LenzLogger.log("\t" + temp);
                try {
                    Thread.sleep(350);//Wait to ensure we get all the transferable data
                } catch (InterruptedException ex) {
                    LenzLogger.log(ex.toString());
                    LenzLogger.log("Thread stuck waiting for data.");
                }
                x = serialPort.getInputBufferBytesCount();
                if(x!=0)
                    System.out.println("\tNext set of bytes:" + x);
            } 
            return returnData.toString();
        }
        else{
            return null;
        }
    }
    
    public boolean serialErrorThrown(){
        return errorThrown;
    }
    
    public enum DeviceType{
        DOGBONE, TRANSMITTER
    }
}
