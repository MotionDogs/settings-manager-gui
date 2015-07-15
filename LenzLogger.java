/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lenzhoundgui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Thayer
 */
public final class LenzLogger {
    private static File logPath = null;
    private static BufferedWriter myWriter = null;
    private static boolean showErrorDialogs = false;
    private static final String newLine = System.getProperty("line.separator");
    
    private LenzLogger(){
        
    }
    
    public static void logOnly(String output){
        try {
            myWriter.write(output);
        } catch (IOException e) {
            if(showErrorDialogs)
                    JOptionPane.showMessageDialog(null,
                        //the following line cuts off the Java.IOException
                        //prefix to the error string
                        e.toString().split(":")[1],
                        "Alert", JOptionPane.ERROR_MESSAGE);
                System.out.println(e.toString());
        }
    }
    
    //Call this method with the directory you wish your logs to be written to
    public static boolean initialize(File path){
        if(myWriter==null){
            try{
                path = new File(path + File.separator + "logs");
                if(!path.exists())
                    path.mkdirs();
                logPath = new File(path + File.separator + "log_"
                        + new SimpleDateFormat("MM-dd-yyyy").format(new Date())
                        +".txt");
                if(!logPath.exists())
                    logPath.createNewFile();                
                myWriter = new BufferedWriter(new FileWriter(logPath, true));
            }
            catch(Exception e){
                if(showErrorDialogs)
                    JOptionPane.showMessageDialog(null,
                        //the following line cuts off the Java.IOException
                        //prefix to the error string
                        e.toString().split(":")[1],
                        "Alert", JOptionPane.ERROR_MESSAGE);
                System.out.println(e.toString());
            }
        }
        else
            System.out.println("Logger is already logging to a different file.");
        return false;
    }
    
    //adds a newline character unless specifiec otherwise
    public static void log(String output){
        log(output,true);
    }
    
    public static void log(char output){
        log(String.valueOf(output),false);
    }
    
    public static void log(String output, int indents){
        StringBuilder indentedString = new StringBuilder();
        for(int i = 0; i < indents; i++)
            indentedString.append("\t");
        log(indentedString.append(output).toString());
    }
    
    public static void log(String output, boolean makeNewLine){
        try{
            if(makeNewLine){
                myWriter.write(output + newLine);                
                System.out.println(output);
            }
            else{
                myWriter.write(output);
                System.out.print(output);
            }
        }
        catch(Exception e){
            if(showErrorDialogs)
                JOptionPane.showMessageDialog(null,
                    //the following line cuts off the Java.IOException
                    //prefix to the error string
                    e.toString().split(":")[1],
                    "Alert", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.toString());
        }
    }
    
    public static void log(String[] arrOut, boolean makeNewLine, int indents){
        for(String s : arrOut){
            StringBuilder b = new StringBuilder(s);
            for(int i = 0; i < indents; i++)
                b.insert(0, '\t');
            if(makeNewLine)
                log(s,true);
            else
                log(s);
        }
    }
    
    public static void log(String[] arrOut, boolean makeNewLine){
        if(makeNewLine)
            for(String s : arrOut){
                s = s + '\n';
            }
        log(arrOut);
    }
    
    public static void log(String[] arrOut, int indents){
        StringBuilder b = new StringBuilder(arrOut[0]);
        for(int i = 0; i < indents; i++)
            b.insert(0, '\t');
        arrOut[0] = b.toString();
        for(String s : arrOut){
            s = s + ' ';
        }
        log(arrOut);
    }
    
    public static void log(String[] arrOut){
        for(String s : arrOut)
            log(s);
    }
    
    public static boolean close(){
        try{
            myWriter.close();
            myWriter = null;
            return true;
        }
        catch(Exception e){
            if(showErrorDialogs)
                JOptionPane.showMessageDialog(null,
                    //the following line cuts off the Java.IOException
                    //prefix to the error string
                    e.toString().split(":")[1],
                    "Alert", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.toString());
        }
        return false;
    }
    
    public static void setShowErrorDialogs(boolean newValue){
        showErrorDialogs = newValue;
    }
}
