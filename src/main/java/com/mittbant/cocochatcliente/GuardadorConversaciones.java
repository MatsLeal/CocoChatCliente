/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mittbant.cocochatcliente;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class GuardadorConversaciones {
    

    
    
    
    public static void GuardaMensaje(Mensaje mensaje) {
        
        if(!origenTieneArchivo(mensaje)){
            return ;
        }
        
        //System.out.println("Anadiendo el mensaje " + mensaje.getMensaje() + " a la conversacion con " +mensaje.getDestinatario());
        
        
        
        
        
        try {
            String lineamensaje= mensaje.getJson()+"\n";
           Files.write(Paths.get("CocoChatMensajes"+ mensaje.getOrigen() + "/"+mensaje.getDestinatario() +".ccc"), lineamensaje.getBytes() , StandardOpenOption.APPEND);
           //if(this.h.getJlbl_nombreconversacion().getText().equals(mensaje.getOrigen())){
             //  System.out.println("La conversacion con " + mensaje.getOrigen() + " es la cual esta abierta ");
            //Cliente.abreConversacion(mensaje.getOrigen(), this.h.getMessage_view());
            
          //  }
           
            //System.out.println("escrito mio !!");
        }catch (IOException e) {
//            System.out.println(e.getCause());
//            System.out.println(e.getMessage());
//            System.out.println(e.getLocalizedMessage());
        }
        
        
        
    }
    
    
    private static void verificaCarpetaMensajes(String origen){
        
        
    //System.out.println("Verificando la carpeta CocoChatMensajes");
    
    File directory = new File("CocoChatMensajes"+origen);
    if (directory.exists() && directory.isFile())
    {
      //  System.out.println("The dir with name could not be" +
      //  " created as it is a normal file");
    }
    else
    {
        
            if (!directory.exists())
            {
                directory.mkdir();
        //        System.out.println("Carpeta creada !");
            }
            
    }

        
     
        
    }
    
    
    private static Boolean origenTieneArchivo(Mensaje mensaje){
        
        verificaCarpetaMensajes(mensaje.getOrigen());
        
        File conversacion= new File("CocoChatMensajes" +mensaje.getOrigen() +    "/"+mensaje.getDestinatario()+".ccc");
        
        if(!conversacion.exists()){
            try {
                //System.out.println("Creando converasion para " + mensaje.getDestinatario());
                conversacion.createNewFile();
                
                return true;
                
            } catch (IOException ex) {
                //System.out.println("No se pudo crear la conversacion con  " + mensaje.getDestinatario());
                Logger.getLogger(HiloReceptorMensaje.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        
        //System.out.println("La conversacion con "+ mensaje.getDestinatario()+ " ya existe");
        
       return true;
    }
    
    
    
}
