/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mittbant.cocochatcliente;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class FriendRequest {
    
    String from;
    String to;
    char state;

    public FriendRequest(String from, String to, char state) {
        this.from = from;
        this.to = to;
        this.state = state;
    }

    public FriendRequest(String selectedrequest) {
        Cliente.verificaCarpetas(from);
        File listasolicitudes= new File("CocoChat"+Cliente.nombre+"Amigos/Solicitudes.ccc");
        
        
        try {
            BufferedReader lectorlista = new BufferedReader(new FileReader(listasolicitudes));
            //System.out.println("Leyendo lista de solitudes amigos");
            String solicitudjson;
            Gson gson= new Gson();
            while( (solicitudjson=lectorlista.readLine())!=null ){
                
                FriendRequest solicitud= gson.fromJson(solicitudjson, FriendRequest.class);
                System.out.println("Soliciutd :" + solicitudjson);
                if(solicitud.getFrom().equals(selectedrequest)){
                    this.from=solicitud.getFrom();
                    this.to=solicitud.getTo();
                    this.state='A';
                    
                    //System.out.println("Solicitud encontrada y objeto creado");
                }
                
            }
            
            //System.out.println("Terminado de escanear solicutdes");
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FriendRequest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FriendRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public char getState() {
        return state;
    }

    public void setState(char state) {
        
        this.state = state;
    }

    void save() {
        
        Cliente.verificaCarpetas(to);
        
        try {
            Gson gson = new Gson();
            String lineamensaje= gson.toJson(this)+"\n";
            
            
            Files.write(Paths.get("CocoChat"+to+"Amigos/Solicitudes.ccc"), lineamensaje.getBytes() , StandardOpenOption.APPEND);
           
            //System.out.println("Solcitud guardada " + lineamensaje);
            
            //System.out.println(" solicitud agregada !");
           
            //System.out.println("escrito mio !!");
        }catch (IOException e) {
//            System.out.println(e.getCause());
//            System.out.println(e.getMessage());
//            System.out.println(e.getLocalizedMessage());
        }
        
        
    }

    public void aceptar() {
        Gson gson = new Gson();
        try {
            Cliente.cliente.getOutputStream().write(
                    (
                            "|FRA|" +
                                    gson.toJson(this)
                            ).getBytes(StandardCharsets.UTF_8)
                    
            );
            //System.out.println("Solicitud Aceptada !");
            //System.out.println("A punto de borrar de la lista !");
            
            borraDeListaSolicitudes();
            
            agergaAListaAmigos();
            
        } catch (IOException ex) {
            Logger.getLogger(FriendRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void agergaAListaAmigos(){
        
        Cliente.verificaCarpetas(Cliente.nombre);
        
            
            File listasolicitudes= new File("CocoChat"+Cliente.nombre+"Amigos/Amigos.ccc");
            
            Gson gson = new Gson();
            this.setState('A');
            String soliciutacutal=gson.toJson(this);
            
            //System.out.println("Guardando amigo !" + soliciutacutal);
            
            soliciutacutal+="\n";
            
            
            
        try {
            //System.out.println("Tratando de agregar amigo  con soliciutd aceptada !" + soliciutacutal);
            Files.write(Paths.get("CocoChat"+Cliente.nombre+"Amigos/Amigos.ccc"), soliciutacutal.getBytes() , StandardOpenOption.APPEND);
        } catch (IOException ex) {
            Logger.getLogger(FriendRequest.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("No se pudo agregar a " +soliciutacutal);
        }
                        
                        
                   
        
        
        
        
        
        
        
    }
    
    
    public void borraDeListaSolicitudes(){
        
        
        try {
            
            
            File listasolicitudes= new File("CocoChat"+Cliente.nombre+"Amigos/Solicitudes.ccc");
            
            File listatemporal= new File("CocoChat"+Cliente.nombre+"Amigos/Solicitudes.ccc.tmp");
            
            listatemporal.createNewFile();
            
            Gson gson = new Gson();
            this.setState('P');
            String soliciutacutal=gson.toJson(this);
            
            try {
                BufferedReader lectorlista = new BufferedReader(new FileReader(listasolicitudes));
                String linea;
                while((linea=lectorlista.readLine()) !=null ){
                    
                    
                    //System.out.println("Si la linea" + linea);
                    //System.out.println("Contiene " + soliciutacutal);
                    
                    if(!linea.contains(soliciutacutal)){
                        
                        Files.write(Paths.get("CocoChat"+to+"Amigos/Solicitudes.ccc.tmp"), linea.getBytes() , StandardOpenOption.APPEND);
                        
                      //  System.out.println("Copiando a nueva lsita" + linea);
                        
                    }
                    
                }
                
                listasolicitudes.delete();
                
                //System.out.println("Vieja lista elimanda");
                
                listatemporal.renameTo(new File("CocoChat"+Cliente.nombre+"Amigos/Solicitudes.ccc"));
            
                //System.out.println("Nueva lista creada !");
                
                new File("CocoChat"+Cliente.nombre+"Amigos/Solicitudes.ccc.tmp").delete();
                
                //System.out.println("Vieja lista borrada");
                
                
                
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FriendRequest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FriendRequest.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (IOException ex) {
            Logger.getLogger(FriendRequest.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("No se pudo crear el archivo deseado !");
        }
        
    }
    
    
    
}
