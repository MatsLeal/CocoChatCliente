/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mittbant.cocochatcliente;

import interfaces.home;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 *
 * @author Omar Silva
 */
public class HiloReceptorMensaje extends Thread{
    
    public int id;
    
    private Socket clientereceptor;
    
    private home h;
    
    private String nombre;
    
    public HiloReceptorMensaje(Socket s, home h){
        
        clientereceptor = s;
        
        this.h=h;
        
        start();
        
    }
    
    
    
    

    public void run(){
        setNombre(this.h.getJlbl_minombre().getText());
        byte[] binPack=new byte[100];
        try {
            //System.out.println("\t \t Recepcion de mensajes comenzado");
        
            while(true)
            {
                
                binPack=new byte[100];
                clientereceptor.getInputStream().read(binPack);
                String pack=new String(binPack,"UTF-8").trim();
            
                //Mensaje
                if(!pack.contains("|"))
                {
                    
                GuardaMensaje(pack);
                    
              //  System.out.println(" Mensaje recibido " +pack);    
                
                }
                //Solicitud de amistad
                else if(pack.contains("|FR|"))
                {
                    
                    
                //    System.out.println("Recived Request !!! ");
                //    System.out.println(pack);
                    
                    Gson gson= new Gson();
                    
                    pack=pack.replace("|FR|", "");
                    
                    FriendRequest request= gson.fromJson(pack, FriendRequest.class);
                    
                    request.save();
                    
                    
                    
                }
                //Lista de conectados/desconectados
                else if(pack.contains("|FRA|"))
                {
                    pack=pack.replace("|FRA|", "");
                    
                    Gson gson = new Gson();
                    
                    FriendRequest acceptedrequest= gson.fromJson(pack, FriendRequest.class);
                    
                    
                    acceptedrequest.state='A';
                    
                    acceptedrequest.agergaAListaAmigos();
                    
                  //  System.out.println("Got request acceptance ");
                    
                }
                else{
                    this.llenaListaConectados(pack);
                    binPack=new byte[100];
                }
                
                
                
                
                
                
            }
        } catch (IOException ex) {
            Logger.getLogger(HiloReceptorMensaje.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public void llenaListaConectados(String listaclientes){
        
        this.h.getTodos_list_conectados().removeAll();
        
        this.h.getTodos_List_Desconectados().removeAll();
        
        //System.out.println("Lista de clientes recibida del server" + listaclientes);
        
        
        String[] clientes= listaclientes.split(Pattern.quote("|"));
        
        
        
        
        
        
        DefaultListModel listModelconectados = new DefaultListModel();
        
        DefaultListModel listModeldesconectados= new DefaultListModel();
        
        for (int i = 0; i < clientes.length; i++) {
            
            
            
            if(clientes[i].contains(":C")){
                
                try{
                listModelconectados.addElement(clientes[i].substring(0, clientes[i].indexOf(":")));
                }catch(StringIndexOutOfBoundsException ex){
                }
            }
            else
            {
                clientes[i].replace(":D", "");
                try {
                listModeldesconectados.addElement(clientes[i].substring(0, clientes[i].indexOf(":")));
                }catch(StringIndexOutOfBoundsException ex){
                }
            }
            
            
        }
        
        
        this.h.getTodos_list_conectados().setModel(listModelconectados);
        this.h.getTodos_List_Desconectados().setModel(listModeldesconectados);
        
    }

    private void GuardaMensaje(String mensajejson) {
        
        Gson gson = new Gson();
        
        //System.out.println(mensajejson);
        
        Mensaje mensaje = gson.fromJson(mensajejson, Mensaje.class);
        
        setNombre(mensaje.getDestinatario());
        
        if(!origenTieneArchivo(mensaje.getOrigen())){
            return ;
        }
        
        //System.out.println("Anadiendo el mensaje " + mensaje.getMensaje() + " a la conversacion con " +mensaje.getOrigen());
        
        
        
        
        
        try {
            
            String lineamensaje= mensaje.getJson()+"\n";
           Files.write(Paths.get("CocoChatMensajes"+ getNombre() + "/"+mensaje.getOrigen() +".ccc"), lineamensaje.getBytes() , StandardOpenOption.APPEND);
           if(this.h.getJlbl_nombreconversacion().getText().equals(mensaje.getOrigen())){
          //     System.out.println("La conversacion con " + mensaje.getOrigen() + " es la cual esta abierta ");
            Cliente.abreConversacion(mensaje.getOrigen(), this.h.getMessage_view());
            
            }
           
//            System.out.println("escrito !");
        }catch (IOException e) {
//            System.out.println(e.getCause());
//            System.out.println(e.getMessage());
//            System.out.println(e.getLocalizedMessage());
        }
        
        
        
    }
    
    
    private void verificaCarpetaMensajes(){
        
        
        //System.out.println("Verificando la carpeta CocoChatMensajes");
    File directory = new File("CocoChatMensajes"+getNombre());
    if (directory.exists() && directory.isFile())
    {
        //System.out.println("The dir with name could not be" +
        //" created as it is a normal file");
    }
    else
    {
        
            if (!directory.exists())
            {
                directory.mkdir();
          //      System.out.println("Carpeta creada !");
            }
            
    }

        
     
        
    }
    
    
    private Boolean origenTieneArchivo(String origen){
        
        verificaCarpetaMensajes();
        
        File conversacion= new File("CocoChatMensajes" +getNombre() +    "/"+origen+".ccc");
        
        if(!conversacion.exists()){
            try {
                //System.out.println("Creando converasion para " + origen);
                conversacion.createNewFile();
                
                return true;
                
            } catch (IOException ex) {
                //System.out.println("No se pudo crear la conversacion con  " + origen);
                Logger.getLogger(HiloReceptorMensaje.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        
        //System.out.println("La conversacion con "+ origen + " ya existe");
        
       return true;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
    
    
    
}
