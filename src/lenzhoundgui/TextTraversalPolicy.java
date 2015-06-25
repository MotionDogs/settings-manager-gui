/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lenzhoundgui;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JTextField;

/**
 *
 * @author Thayer
 */
public class TextTraversalPolicy extends FocusTraversalPolicy{
    Container holder;
    boolean sorted = false;
    ArrayList<JTextField> fields = new ArrayList<>();
    Comparator<JTextField> sortLogic = (JTextField former, JTextField latter) -> {
        if(former.getY() > latter.getY())
            return 1;
        else if(former.getY() < latter.getY())
            return -1;
        else if(former.getX() > latter.getX())
            return 1;
        else if(former.getX() < latter.getX())
            return -1;
        else
            return 0;
    };
    
    public TextTraversalPolicy(Container target){
        holder = target;
        System.out.println(target.getComponents().length);
        for(Component c : target.getComponents()){
            if(c instanceof JTextField)
                fields.add((JTextField)c);
            else if(c instanceof Container)
                unwrapContainer((Container)c);
        }
    }
    
    private void unwrapContainer(Container target){
        for(Component c : target.getComponents()){
            if(c instanceof JTextField)
                fields.add((JTextField)c);
            else if(c instanceof Container)
                unwrapContainer((Container)c);
        }
    }
    
    @Override
    public Component getComponentAfter(Container aContainer, Component aComponent) {
        if(!sorted){                                //this should have a better space, but if you sort in the constructor the
            Collections.sort(fields,sortLogic);     //positions of the text fields will not have been assigned and the sort fails.
            sorted = true;                          //only sort once, sorts are expenxive
        }
        if(!(aComponent instanceof JTextField))
            return getFirstComponent(null);
        Component returnValue = null;
        for(int i = 0; i < fields.size(); i++){
            if(fields.get(i) == aComponent)//intentional reference compare
                if(fields.size() > i + 1)
                    returnValue = fields.get(i+1);
                else
                    returnValue = fields.get(0);
        }
        return returnValue;
    }

    @Override
    public Component getComponentBefore(Container aContainer, Component aComponent) {
        if(!sorted){
            Collections.sort(fields,sortLogic);
            sorted = true;
        }
        if(!(aComponent instanceof JTextField))
            return getFirstComponent(null);
        Component returnValue = null;
        for(int i = 0; i < fields.size(); i++){
            if(fields.get(i) == aComponent)//intentional reference compare
                if(i > 0)
                    returnValue = fields.get(i-1);
                else
                    returnValue = getLastComponent(null);
        }
        return returnValue;
    }

    @Override
    public Component getFirstComponent(Container aContainer) {
        return fields.get(0);
    }

    @Override
    public Component getLastComponent(Container aContainer) {
        return fields.get(fields.size() - 1);
    }

    @Override
    public Component getDefaultComponent(Container aContainer) {
        return holder;
    }
    
}
