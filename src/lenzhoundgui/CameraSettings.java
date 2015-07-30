/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lenzhoundgui;
import java.util.ArrayList;
import jssc.SerialPortException;
/**
 *
 * @author Thayer
 */
public class CameraSettings {
    public CameraSettings(String[] fileContents){
        LenzLogger.log("Length of Entry: " + fileContents.length);
        if(fileContents.length != 11)
            error = true;
        try{
            name = fileContents[0];
            LenzLogger.log(fileContents[0]);
            for(int i=0;i<contents.length;i++){
                contents[i] = Integer.parseInt(fileContents[i+1]);
                LenzLogger.log("\t" + i + " " + fileContents[i+1]);
            }            
            String[] posArr = fileContents[10].split(",");
            for(int i=0;i<savedPositions.length;i++){
                savedPositions[i] = Integer.parseInt(posArr[i]);
                //LenzLogger.log("\tPosition " + i + ": " + savedPositions[i]);
            }
        }
        catch(Exception ex){
            error = true;
        }
        finally{}        
    }
    
    public CameraSettings(String newName){
        name = newName;
        contents[VELOCITY]       = DEFAULT_VELOCITY;
        contents[ACCELERATION]   = DEFAULT_ACCELERATION;
        contents[ANTENNA]        = DEFAULT_ANTENNA;
        contents[CHANNEL]        = DEFAULT_CHANNEL;
        contents[POWER_LEVEL]    = DEFAULT_POWER_LEVEL;
        contents[DATA_RATE]      = DEFAULT_DATA_RATE;
        contents[Z_VELOCITY]     = DEFAULT_Z_VELOCITY;
        contents[Z_ACCELERATION] = DEFAULT_Z_ACCELERATION;        
        contents[T_CHANNEL]      = DEFAULT_CHANNEL;
        savedPositions = new int[]{UNSET_SAVED_POSITION,UNSET_SAVED_POSITION,UNSET_SAVED_POSITION,UNSET_SAVED_POSITION};
    }
    
    public CameraSettings(CameraSettings oldSet){
        name                     = oldSet.getName();
        contents[VELOCITY]       = oldSet.getVelocity();
        contents[ACCELERATION]   = oldSet.getAcceleration();
        contents[ANTENNA]        = oldSet.getAntenna();
        contents[CHANNEL]        = oldSet.getChannel();
        contents[POWER_LEVEL]    = oldSet.getPowerLevel();
        contents[DATA_RATE]      = DEFAULT_DATA_RATE;
        contents[Z_VELOCITY]     = oldSet.getZVelocity();
        contents[Z_ACCELERATION] = oldSet.getZAcceleration();        
        contents[T_CHANNEL]      = oldSet.getTChannel();
    }
    
    public CameraSettings(CameraSettings oldSet, String newName){
        name                     = newName;
        contents[VELOCITY]       = oldSet.getVelocity();
        contents[ACCELERATION]   = oldSet.getAcceleration();
        contents[ANTENNA]        = oldSet.getAntenna();
        contents[CHANNEL]        = oldSet.getChannel();
        contents[POWER_LEVEL]    = oldSet.getPowerLevel();
        contents[DATA_RATE]      = DEFAULT_DATA_RATE;
        contents[Z_VELOCITY]     = oldSet.getZVelocity();
        contents[Z_ACCELERATION] = oldSet.getZAcceleration();        
        contents[T_CHANNEL]      = oldSet.getTChannel();
    }
    
    public String saveString(){
        StringBuilder builtString = new StringBuilder(name);
        for(int i = 0; i < 9; i++){
            builtString.append(";");
            builtString.append(Integer.toString(contents[i]));
        }
        builtString.append(";");
        for(int i = 0; i < 4; i++){
            builtString.append(Integer.toString(savedPositions[i]));
            builtString.append(",");
        }
        builtString.append(";");
        return builtString.toString();
    }
    
    public void setToDefault(){
        contents[VELOCITY]       = DEFAULT_VELOCITY;
        contents[ACCELERATION]   = DEFAULT_ACCELERATION;
        contents[CHANNEL]        = DEFAULT_CHANNEL;
        contents[POWER_LEVEL]    = DEFAULT_POWER_LEVEL;
        contents[Z_VELOCITY]     = DEFAULT_Z_VELOCITY;
        contents[Z_ACCELERATION] = DEFAULT_Z_ACCELERATION;
        contents[T_CHANNEL]      = DEFAULT_CHANNEL;
    }
    
    public void copySettings(CameraSettings oldSet){
        name                     = oldSet.getName();
        contents[VELOCITY]       = oldSet.getVelocity();
        contents[ACCELERATION]   = oldSet.getAcceleration();
        contents[ANTENNA]        = oldSet.getAntenna();
        contents[CHANNEL]        = oldSet.getChannel();
        contents[POWER_LEVEL]    = oldSet.getPowerLevel();
        contents[DATA_RATE]      = DEFAULT_DATA_RATE;
        contents[Z_VELOCITY]     = oldSet.getZVelocity();
        contents[Z_ACCELERATION] = oldSet.getZAcceleration();        
        contents[T_CHANNEL]      = oldSet.getTChannel();
    }
    
    public boolean errorThrown(){
        return error;
    }
    public String getName(){
        return name;
    }
    
    public int getVelocity(){
        return contents[VELOCITY];
    }    
    public void setVelocity(int velocity){
        contents[VELOCITY] = velocity;
    }    
    
    public int getAcceleration(){
        return contents[ACCELERATION];
    }    
    public void setAcceleration(int acceleration){
        contents[ACCELERATION] = acceleration;
    }   
    
    public int getAntenna(){
        return contents[ANTENNA];
    }    
    public void setAntenna(int antenna){
        contents[ANTENNA] = antenna;
    }
    
    public int getChannel(){
        return contents[CHANNEL];
    }    
    public void setChannel(int channel){
        contents[CHANNEL] = channel;
    }    
    
    public int getPowerLevel(){
        return contents[POWER_LEVEL];
    }    
    public void setPowerLevel(int powLevel){
        contents[POWER_LEVEL] = powLevel;
    }    
    
    public int getDataRate(){
        return contents[DATA_RATE];
    }    
    public void setDataRate(int dataRate){
        contents[DATA_RATE] = dataRate;
    }    
    
    public int getZVelocity(){
        return contents[Z_VELOCITY];
    }    
    public void setZVelocity(int zVelocity){
        contents[Z_VELOCITY] = zVelocity;
    }
    
    public int getZAcceleration(){
        return contents[Z_ACCELERATION];
    }
    public void setZAcceleration(int zAcceleration){
        contents[Z_ACCELERATION] = zAcceleration;
    }   
    
    public int getTChannel(){
        return contents[T_CHANNEL];
    }    
    public void setTChannel(int channel){
        contents[T_CHANNEL] = channel;
    }        
    
    public int[] getContents(){
        return contents;
    }
    
    public void setSavedPositions(int[] savedPos){
        if(savedPos.length == 4){
            System.arraycopy(savedPos, 0, savedPositions, 0, savedPos.length);
        } else {
            LenzLogger.log("Invalid array sent to saved positions.");
        }
    }
    
    public int[] getSavedPositions(){
        return savedPositions;
    }
    
    public static boolean readImport(SerialCommunicator comms, CameraSettings target, DigUploader gui) throws SerialPortException{
        String[] temp;
        boolean startPointFound = false;
        int startPoint = 0;
        try{
            LenzLogger.log("Begining Import");
            for(int i = 0; startPointFound == false && i < 10; i++){//timeout
                int type = 0; //1 motor, 2 transmitter, 0 not finding correct device
                LenzLogger.log("Attempt:" + (i+ 1));               
                String hold = comms.serialRead();
                if(hold == null)
                    continue;
                temp = hold.split("\n");
                deviceContents = new ArrayList<>();
                LenzLogger.log("Lines read:" + temp.length);
                for(int j = 0; j < temp.length; j++){                    
                    if(startPointFound == false){
                        if(temp[j].contains("Available commands")){
                            if(temp[j].contains("Dogbone")){
                                type = 1;
                            } else if (temp[j].contains("Txr")){
                                type = 2;
                            }
                        }
                        if(temp[j]!= null && temp[j].contains("Current values")){
                            LenzLogger.log("Starting point found in: " + temp[j]);
                            startPoint = j;
                            startPointFound = true;
                        }
                        else
                            LenzLogger.log("\tSkipping junk data..." + temp[j]);
                    }else{
                        if(temp[j] == null){
                            LenzLogger.log("item " + j + " is null");
                        }
                        else if(j-startPoint < 10){
                            LenzLogger.log("adding value: "  + temp[j]);
                            deviceContents.add(temp[j]);
                        }
                    }
                }

                if(!deviceContents.isEmpty()){
                    if(type == 1){//receiver
                        int counter = 0;
                        //velocity
                        while(!(deviceContents.get(counter).contains("Vel")))
                            counter++;
                        String holder = deviceContents.get(counter).split(": ")[1];
                        holder = holder.replaceAll("\\s","");//removes space at the end
                        LenzLogger.log("Setting velocity: " + holder);
                        target.setVelocity(Integer.parseInt(holder));
                        //acceleration
                        while(!(deviceContents.get(counter).contains("Accel")))
                            counter++;
                        holder = deviceContents.get(counter).split(": ")[1];
                        holder = holder.replaceAll("\\s","");
                        LenzLogger.log("Setting acceleration: " + holder);
                        target.setAcceleration(Integer.parseInt(holder));
                        //channel
                        while(!(deviceContents.get(counter).contains("Channel")))
                            counter++;
                        holder = deviceContents.get(counter).split(": ")[1];
                        holder = holder.replaceAll("\\s","");
                        LenzLogger.log("Setting channel: " + holder);
                        target.setChannel(Integer.parseInt(holder));
                        gui.lastRChannel = target.getChannel();
                        gui.checkLabel(true);
                        //zVelocity
                        while(!(deviceContents.get(counter).contains("ZMode Max Vel")))
                            counter++;
                        holder = deviceContents.get(counter).split(": ")[1];
                        holder = holder.replaceAll("\\s","");
                        LenzLogger.log("Setting z-velocity: " + holder);
                        target.setZVelocity(Integer.parseInt(holder));
                        //zAccel
                        while(!(deviceContents.get(counter).contains("ZMode Accel")))
                            counter++;
                        holder = deviceContents.get(counter).split(": ")[1];
                        holder = holder.replaceAll("\\s","");
                        LenzLogger.log("Setting z-acceleration: " + holder);
                        target.setZAcceleration(Integer.parseInt(holder));
                        if(!(   target.getVelocity()        <= MAX_VELOCITY     &&
                                target.getVelocity()        >= 0                &&
                                target.getAcceleration()    <= MAX_ACCELERATION &&
                                target.getAcceleration()    >= 0                &&
                                target.getChannel()         <= MAX_CHANNEL      &&
                                target.getChannel()         >= 0                &&
                                target.getZVelocity()       <= MAX_VELOCITY     &&
                                target.getZVelocity()       >= 0                &&
                                target.getZAcceleration()   <= MAX_ACCELERATION &&
                                target.getZAcceleration()   >= 0))
                                    throw new Exception("Error parsing data: one or more values" +
                                            " out of bounds.");
                    }
                    else if(type == 2){
                    LenzLogger.log("transmitter contents");
                        int counter = 0;
                        while(!(deviceContents.get(counter).contains("Channel")))
                            counter++;
                        //channel
                        String holder = deviceContents.get(counter++).split(": ")[1];
                        holder = holder.replaceAll("\\s","");
                        LenzLogger.log("Setting t-channel: " + holder);
                        target.setTChannel(Integer.parseInt(holder));
                        gui.lastTChannel = target.getTChannel();
                        gui.checkLabel(false);
                        //Power Level
                        holder = deviceContents.get(counter++).split(": ")[1];
                        holder = holder.replaceAll("\\s","");
                        LenzLogger.log("Setting power-level: " + holder);
                        target.setPowerLevel(Integer.parseInt(holder));
                        if(!(   target.getTChannel()        <= MAX_CHANNEL      &&
                                target.getTChannel()        >= 0                &&
                                target.getPowerLevel()      <= MAX_POWER_LEVEL  &&
                                target.getPowerLevel()      >= 0))
                                    throw new Exception("Error parsing data: one or more values" +
                                            " out of bounds.");
                        int y = 3;//next value
                        while(!(holder = deviceContents.get(counter++)).contains("Saved Positions:"))
                            LenzLogger.log("Skipping junk data " + holder);
                        int[] arrHolder = new int[4];
                        try{
                            for(int k = 0; k < 4; k++){
                                holder = deviceContents.get(counter++).trim();
                                LenzLogger.log("Saved postion " + k + " " + holder);
                                arrHolder[k] = Integer.parseInt(holder);
                            }
                        } catch(Exception e) {
                            LenzLogger.log(e.toString());
                            LenzLogger.log("Likely old firmware on device.");
                            arrHolder = new int[]{UNSET_SAVED_POSITION,UNSET_SAVED_POSITION,
                                UNSET_SAVED_POSITION,UNSET_SAVED_POSITION};
                        }
                        target.setSavedPositions(arrHolder);
                    }
                    else{
                        LenzLogger.log("Contents array is " + deviceContents.size() + "; retrying...");
                        startPointFound = false;
                    }
                }
                else
                    startPointFound = false;
            }
            if(!startPointFound)
                throw new SerialPortException("Lenzhound Device", "Main Port","No data found.");
        }
        catch(Exception ex){
            if(target!=null)
                LenzLogger.log("Error importing to: " + target.getName());
            LenzLogger.log(ex.toString());
            LenzLogger.log("Dumping Contents:");
            for(int i = 0;i < deviceContents.size(); i++)
                    LenzLogger.log("\t" + Integer.toString(i) + " " + deviceContents.get(i));
            LenzLogger.log(ex.toString());
            return false;
        }
        LenzLogger.log("Import Successs!");
        return true;
    }
    
    private int[] contents = new int[9];
    private int[] savedPositions = new int[4];
    private static ArrayList<String> deviceContents = new ArrayList<>();
    private boolean error = false;
    private String name = null;
    static final int VELOCITY = 0;
    static final int ACCELERATION = 1;
    static final int ANTENNA = 2;
    static final int CHANNEL = 3;
    static final int POWER_LEVEL = 4;
    static final int DATA_RATE = 5;
    static final int Z_VELOCITY = 6;
    static final int Z_ACCELERATION = 7;
    static final int T_CHANNEL = 8;    
    static final int MAX_VELOCITY = 32768;
    static final int MAX_ACCELERATION = 1024;
    static final int MAX_CHANNEL = 81;
    static final int MAX_POWER_LEVEL = 3;
    static final int DEFAULT_VELOCITY = 32768;
    static final int DEFAULT_ACCELERATION = 256;
    static final int DEFAULT_ANTENNA = 0;
    static final int DEFAULT_CHANNEL = 1;
    static final int DEFAULT_POWER_LEVEL = 3;
    static final int DEFAULT_DATA_RATE = 0;
    static final int DEFAULT_Z_VELOCITY = 32768;
    static final int DEFAULT_Z_ACCELERATION = 256;
    static final int UNSET_SAVED_POSITION = -Integer.MAX_VALUE;
}