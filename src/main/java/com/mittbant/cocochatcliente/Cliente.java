package com.mittbant.cocochatcliente;

import com.google.gson.Gson;
import interfaces.LogIn;
import interfaces.home;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;
import javax.swing.DefaultListModel;
import javax.swing.JList;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
/**
 *
* @author Departamento
 */
public class Cliente {
    
 
    
    static Gson gson;
    
    public static String nombre;
    
    public static Socket cliente;
    
    public static home h;

    public static void mandaMensaje(String contenido, String destinatario){
                    byte[] manda = new byte[100];
        
                    Mensaje m = new Mensaje(nombre,contenido,destinatario);
                    
                    
                    manda = m.getJson().getBytes();
                    
                    
        try {
            cliente.getOutputStream().write(manda);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    public static void inicializa(String nombre){
                h= new home();
                
                h.setVisible(true);
        
                h.getJlbl_minombre().setText(nombre);
                setNombre(nombre);
                verificaCarpetas(nombre);
                HiloReceptorMensaje entra=new HiloReceptorMensaje(cliente,h);
                
                
                setCliente(cliente);
                
    }

    public static void main(String[] args) {
        
        gson = new Gson();
        
        //home h = new home();
        
        //h.setVisible(true);
        
        LogIn log = new LogIn();
        
        log.setVisible(true);
        
        
//        Scanner teclado = new Scanner (System.in);
//        
//        String contenidomensaje =null;
//        
//        System.out.println("Nombre de usuario:\n");
//        
//        contenidomensaje = teclado.nextLine();
//        
//        String nombre = contenidomensaje;
//        
//        byte[] recibe=new byte[666];
//        
//        byte[] manda=new byte[666];
//        
//        byte[] bytescontenidomensaje = null;
//        
//        bytescontenidomensaje=contenidomensaje.getBytes(StandardCharsets.UTF_8);
//        
        //try {
            
            
//            Socket cliente = null;
//            
//            cliente = new Socket ("192.168.0.2", 5001);
//            
//            cliente.getOutputStream().write(bytescontenidomensaje);
//            
//            cliente.getInputStream().read(recibe);
//            
//            String respuestalogin=new String(recibe,"UTF-8");
//            
//            System.out.println(respuestalogin);
//            
            
                
                
                
                
//                h.getJlbl_minombre().setText(nombre);
//                setNombre(nombre);
//                verificaCarpetas(nombre);
//                HiloReceptorMensaje entra=new HiloReceptorMensaje(cliente,h);
//                
//                
//                setCliente(cliente);
//                
                while(true)
                {
                  
                    
                                        
                }
            
            
//            System.in.read();
//            cliente.close();
//        } catch (IOException ex) {
//            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }



   
    public static void abreConversacion(String origen, JTextPane vistaconversacion) {
        
        //System.out.println("\t\tAttemptin to open conversation file ");
        
        vistaconversacion.setText("");
        
        if(!conversacionExiste(origen))
            return ;
        
        
        
        
        
        StyledDocument doc = vistaconversacion.getStyledDocument();

        SimpleAttributeSet left = new SimpleAttributeSet();
        StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(left, Color.BLUE);

        SimpleAttributeSet right = new SimpleAttributeSet();
        StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setForeground(right, Color.BLUE);
        
        
        //System.out.println("\t\tConversation exists !");
        
        File conversacion= new File("CocoChatMensajes"+nombre+"/"+origen+".ccc");
        
        try {
            
            BufferedReader lectordeconversacion= new BufferedReader(new FileReader(conversacion));
            String linea;
            
            Mensaje m  ;
            
            Gson json= new Gson();
            
            while((linea=lectordeconversacion.readLine()) != null){
                
                
                m=json.fromJson(linea, Mensaje.class );
                
                //System.out.println("\t\tExtraido del archivo ! :"+linea);
                
                if(m.getOrigen().equals(nombre)){
                     doc.insertString(doc.getLength(),"\n"+ m.getMensaje()  , right );
                     doc.setParagraphAttributes(doc.getLength(), 1, right, false);
                }
                else{
                    doc.insertString(doc.getLength(),"\n" + m.getMensaje() , left );
                    doc.setParagraphAttributes(doc.getLength(), 1, left, false);
                }
                
                
                
                
                
                
                
                
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadLocationException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    
    
    private static Boolean conversacionExiste(String origen){
             
        //System.out.println("\t\tOpening : ");
        
        
        //System.out.println("\t\tCocoChatMensajes"+nombre+"/"+origen+".ccc");
        
        File conversacion= new File("CocoChatMensajes"+nombre+"/"+origen+".ccc");
        
        if(!conversacion.exists()) {
            
            try {
          //      System.out.println("Conversacoin no exisitia");
          //      System.out.println("Creando nueva covercscion");
                return conversacion.createNewFile();
        } catch (IOException ex) {
            //    System.out.println("NO se pudo abrir el acrhivo");
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        
            return true;
            
        }
    
    
    
    
    
    
    public static void verificaCarpetas(String nombre){
    
        
        verificaCarpetaAmigos(nombre);
        verificaListaAmigos(nombre);
        verificaListaSolicitudesAmigos(nombre);
        
    
            File directory = new File("CocoChatMensajes"+nombre);
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

    public static void sendRequest(String nombre) {
        
        
        
        try {
            if(isFriend(nombre))
                return;
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        saveFriend(nombre);
        
        sendFriendRequest(nombre);
        
    }
    
    
    public static Boolean isFriend(String personname) throws FileNotFoundException, IOException{
        
        verificaCarpetas(nombre);
        
        File friendlist = new File("CocoChat"+nombre+"Amigos/Amigos.ccc");
        
        BufferedReader lectordeconversacion= new BufferedReader(new FileReader(friendlist));
        String linea;
        while((linea=lectordeconversacion.readLine())!=null){
            
          
            
            FriendRequest request= gson.fromJson(linea, FriendRequest.class);
            
            //System.out.println("Datos de soliciutd acual en verifaicion de isFriend");
            
            //System.out.println(request.from);
            
            //System.out.println(request.to);
            
            System.out.println(request.state);
            
            if(request.getFrom().equals(personname) || request.getTo().equals(personname) && request.getState()=='A' ){
                
              //  System.out.println(linea + "Ya estra agregado como amigo");
                return true;
                
                
            }
                
        }
        
        return false;
    }

    private static void saveFriend(String nombre) {
        
        try {
            String file="CocoChat"+Cliente.nombre+"Amigos/Amigos.ccc";
            //System.out.println("Opnenig friends list" + file);
            
            String request= gson.toJson(new FriendRequest(nombre,Cliente.nombre,'P'));
            
            request+=" \n";
            Files.write(Paths.get(file), request.getBytes() , StandardOpenOption.APPEND);
            //System.out.println("Amigo agregado ! ");
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("Exepciones \n\n");
//            System.out.println(ex.getCause());
//            System.out.println(ex.getLocalizedMessage());
//            System.out.println(ex.getMessage());
//            System.out.println("No se pudo agregar amigo :c ");
            
        }
    }

    private static void sendFriendRequest(String nombre) {
        
        
        
        
    
        String friendrequest="|FR|" + gson.toJson(new FriendRequest(Cliente.nombre,nombre,'P')) ;
        
        
        
        try {
            
            cliente.getOutputStream().write(friendrequest.getBytes(StandardCharsets.UTF_8));
            
        } catch (IOException ex) {
            
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
        
        
    }

    private static void verificaCarpetaAmigos(String nombre) {
    File    directory = new File("CocoChat"+nombre+"Amigos");
    if (directory.exists() && directory.isFile() || directory.exists())
    {
        return;
    }
    
    directory.mkdir();
        //System.out.println("Carpeta creada Amigos creada!");
    }

    private static void verificaListaAmigos(String nombre) {
        
       File  directory = new File("CocoChat"+nombre+"Amigos/Amigos.ccc");
    if (directory.exists() && directory.isFile() || directory.exists())
    {
        return;
    }
    
        try {
            directory.createNewFile();
          //  System.out.println("Carpeta creada Amigos creada!");
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("No se pudo crear la carpeta de amigos para " + nombre);
        }
        
    
    }
    
    
    private static void verificaListaSolicitudesAmigos(String nombre) {
        
       File  directory = new File("CocoChat"+nombre+"Amigos/Solicitudes.ccc");
    if (directory.exists() && directory.isFile() || directory.exists())
    {
        return;
    }
    
        try {
            directory.createNewFile();
            //System.out.println("Carpeta creada solicitudes creada!");
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("No se pudo crear la carpeta de solicitudes para " + nombre);
        }
        
    
    }

    public static void llenaListaSolicitudes(JList<String> listasolicitudesamigos) {
        verificaCarpetas(nombre);
        File  directory = new File("CocoChat"+nombre+"Amigos/Solicitudes.ccc");
        
        try {
            //System.out.println("Intentando leer la lista de soliciutes");
            BufferedReader lectordesolicitudes= new BufferedReader(new FileReader(directory));
            String solicitudjson="";
            listasolicitudesamigos.removeAll();
            DefaultListModel modelolista= new DefaultListModel();
            while((solicitudjson=lectordesolicitudes.readLine())!=null){
                
                FriendRequest solicitud = gson.fromJson(solicitudjson, FriendRequest.class);
            
                
                modelolista.addElement(solicitud.getFrom());
                
               // System.out.println("Se agrego la solicitud de "+solicitud.getFrom());
                
            }
            
            listasolicitudesamigos.setModel(modelolista);
            
         //   System.out.println("Se lleno la lista de soliciutes de amigos !");
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        //    System.out.println("No se pudo abrir la lista de solicitudes");
        } catch (IOException ex) {
            System.out.println("No se pudo leer la lista de soliciudes ");
        //    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    public static void llenaListaAmigos(JList<String> listaamigos) {
        BufferedReader lectordeconversacion = null;
        listaamigos.removeAll();
        
        try {
            verificaCarpetas(nombre);
            File friendlist = new File("CocoChat"+nombre+"Amigos/Amigos.ccc");
            lectordeconversacion = new BufferedReader(new FileReader(friendlist));
            String linea;
            
            DefaultListModel amigos = new DefaultListModel();
            while((linea=lectordeconversacion.readLine())!=null){
                
                
                
                FriendRequest request= gson.fromJson(linea, FriendRequest.class);
                
             //   System.out.println("Datos de soliciutd acual en verifaicion de isFriend");
                
               // System.out.println(request.from);
                
               // System.out.println(request.to);
                
              //  System.out.println(request.state);
                
                if(request.getState()=='A' ){
                    
                    
                   if(request.from.contains(Cliente.nombre)){
                       if(!request.to.contains(Cliente.nombre))
                           amigos.addElement(request.to);
                   }
                   else
                   {
                       amigos.addElement(request.from);
                   }
                    
                    
                }
                
            }
            
            listaamigos.setModel(amigos);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                lectordeconversacion.close();
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
        
        
        
       
   


    public String getNombre() {
        return nombre;
    }

    public static void setNombre(String nombreent) {
        nombre = nombreent;
    }

    public static Socket getCliente() {
        return cliente;
    }

    public static void setCliente(Socket cliente) {
        Cliente.cliente = cliente;
    }
    
    
        
}
