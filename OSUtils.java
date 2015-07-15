/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lenzhoundgui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

/**
 *
 * @author edit_omega
 */
public class OSUtils {
   private static String OS = null;
    
   public static String getOsName(){
      if(OS == null) { OS = System.getProperty("os.name"); }
      return OS;
   }
   
   public static boolean isWindows(){
      return getOsName().startsWith("Windows");
   }
   
   public static boolean isMac(){
       return getOsName().startsWith("Mac");
   }
   
   public static void setHide(Path path) throws IOException{
        Boolean hidden = (Boolean) Files.getAttribute(path,"dos:hidden", LinkOption.NOFOLLOW_LINKS);
        if (hidden != null && !hidden) {
            Files.setAttribute(path,"dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        }
   }
   
   public static void setShow(Path path) throws IOException{
        Boolean hidden = (Boolean) Files.getAttribute(path,"dos:hidden", LinkOption.NOFOLLOW_LINKS);
        if (hidden != null && hidden) {
            Files.setAttribute(path,"dos:hidden", Boolean.FALSE, LinkOption.NOFOLLOW_LINKS);
        }
   }
}
