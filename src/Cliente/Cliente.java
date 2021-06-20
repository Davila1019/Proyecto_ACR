/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;
import java.awt.Color;
import java.util.Vector;
import javax.swing.JOptionPane;



public class Cliente implements Runnable{
        
    Socket socket;
    DataInputStream entrada;
    DataOutputStream salida;
    ChatGUI cg;
    StringTokenizer st;
    
    public Cliente(Socket socket, ChatGUI cg){
        this.cg = cg;
        this.socket = socket;
        try {
            entrada = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            cg.agregarMensaje("[IOException]: "+ e.getMessage(), "Error", Color.RED, Color.RED);
        }
    }

    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()){
                String data = entrada.readUTF();
                st = new StringTokenizer(data);
                //Obtener el mensaje de los comandos para los case
                String CMD = st.nextToken();
                switch(CMD){
                    case "CMD_MESSAGE":
                        //Reproducir el audio
                       // NotificacionMensaje.MessageReceive.play();
                        String msj = "";
                        String frm = st.nextToken();
                        while(st.hasMoreTokens()){
                            msj = msj +" "+ st.nextToken();
                        }
                        cg.agregarMensaje(msj, frm, Color.MAGENTA, Color.BLUE);
                        break;
                        
                    case "CMD_ONLINE":
                        Vector online = new Vector();
                        while(st.hasMoreTokens()){
                            String list = st.nextToken();
                            if(!list.equalsIgnoreCase(cg.nombreUsuario)){
                                online.add(list);
                            }
                        }
                        cg.appendOnlineList(online);
                        break;
                    
                    //En este caso al cliente se le informara que hay un archivo para recibir,
                    //y se le preguntara si lo acepta o lo rechaza    
                    case "CMD_FILE_XD":  // Formato:  CMD_FILE_XD [sender] [receiver] [filename]
                        
                        String sender = st.nextToken();
                        String receiver = st.nextToken();
                        String fname = st.nextToken();
                        int confirm = JOptionPane.showConfirmDialog(cg, "Nombre de Archivo: "+fname+"\n¿Desea aceptar el archivo?");
 
                        if(confirm == 0){ //El cliente acepto la solicitud, luego informa al remitente
                                          //que envie el archivo ahora
                            //Seleccionar donde se va aguardar el archivo que se reciba.
                            cg.abrirArchivos();
                            try {
                                salida = new DataOutputStream(socket.getOutputStream());
                                String format = "CMD_SEND_FILE_ACCEPT "+sender+" accepted";
                                salida.writeUTF(format);
                                
                                //Esto creará un socket de intercambio de archivos para manejar el archivo entrante y este
                                //socket se cerrará automáticamente cuando esté listo.
                                Socket fSoc = new Socket(cg.getMyHost(), cg.getMyPort());
                                DataOutputStream fdos = new DataOutputStream(fSoc.getOutputStream());
                                fdos.writeUTF("CMD_SHARINGSOCKET "+ cg.getMyUsername());
                                //Correr hilo 
                                new Thread(new RecibirArchivo(fSoc, cg)).start();
                            }catch(IOException e){
                                System.out.println("[CMD_FILE_XD]: "+e.getMessage());
                            }
                        }else{ //El cliente rechazó la solicitud y despues se envia el resultado al remitente.
                            try {
                                salida = new DataOutputStream(socket.getOutputStream());
                                String format = "CMD_SEND_FILE_ERROR "+sender+" El cliente rechazó tu solicitud o se perdió la conexión.";
                                salida.writeUTF(format);
                            } catch (IOException e) {
                                System.out.println("[CMD_FILE_XD]: "+e.getMessage());
                            }
                        }                       
                        break;   
                        
                    default: 
                        cg.agregarMensaje("[CMDException]: Comando desconocido "+ CMD, "CMDException", Color.RED, Color.RED);
                    break;
                }
            }
        } catch(IOException e){
            cg.agregarMensaje("Se perdió la conexión con el servidor.", "Error", Color.RED, Color.RED);
        }
    }
}

