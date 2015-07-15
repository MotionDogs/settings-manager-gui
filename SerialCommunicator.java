/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lenzhoundgui;

import jssc.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
    
    SerialCommunicator(String portName){
        try{
            initialize(portName);
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
    
    private void initialize(String portName) throws SerialPortException{
        try{
            serialPort = new SerialPort(portName);
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
    
    public void resetPort() throws SerialPortException{
        if(serialPort.isOpened())//ensures the port is closed to start
            serialPort.closePort();
        System.out.println("Resetting port: " + serialPort.getPortName());
        serialPort.openPort();
        serialPort.setParams(SerialPort.BAUDRATE_1200, 
                             SerialPort.DATABITS_8,
                             SerialPort.STOPBITS_1,
                             SerialPort.PARITY_NONE);
        serialPort.setDTR(false);
        serialPort.closePort();
        //System.out.println(Arrays.toString(SerialPortList.getPortNames()));
    }
    
    public void flushBuffer() throws SerialPortException{
        while(serialPort.getInputBufferBytesCount() > 0) {
            serialPort.readBytes().toString();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {}
        }
    }
    
    //random enumeration of ports when hitting the reset switch requires this function
    public static String findBootLoaderPort(){
        //get list of strings that are available on; the Leonardo is being reset
        //so it will not be part of this list
        ArrayList<String> startingPorts = new ArrayList<>(Arrays.asList(SerialPortList.getPortNames()));
        LenzLogger.log("Starting Ports: " + startingPorts.toString());
        int timeoutCycles = 0;
        while(timeoutCycles < 100){
            String[] curPorts = SerialPortList.getPortNames();
            LenzLogger.log(Arrays.toString(curPorts),1);
            for(String s : curPorts){
                //we look for the Leonardo automatically reconnecting in bootloader mode
                if(!startingPorts.contains(s)){
                    LenzLogger.log("Found Port: " + s);
                    return s;
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                LenzLogger.log(ex.toString());
                return null;
            }
            timeoutCycles++;
        }
        return null;
    }
    
    public boolean serialErrorThrown(){
        return errorThrown;
    }
    
    public enum DeviceType{
        DOGBONE, TRANSMITTER
    }
}
