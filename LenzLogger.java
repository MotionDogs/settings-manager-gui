/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lenzhoundgui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    
    //Call this method with the directory you wish your logs to be written to
    public static boolean initialize(File path){
        if(myWriter==null){
            try{                
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
    
    public static void log(String output, int indents){
        StringBuilder indentedString = new StringBuilder();
        for(int i = 0; i < indents; i++)
            indentedString.append("\t");
        log(indentedString.append(output).toString());
    }
    
    public static void log(String output, boolean makeNewLine){
        try{
            if(makeNewLine)
                myWriter.write(output + newLine);
            else
                myWriter.write(output);
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
        System.out.println(output);
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
