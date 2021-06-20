/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.StringTokenizer;
import java.net.Socket;




public class SocketHilo implements Runnable{
    
    Socket socket;
    ServidorGUI sg;
    DataInputStream entrada;
    StringTokenizer st;
    String cliente, filesharing_username;
    int BUFFER_SIZE = 100;
    
    public SocketHilo(java.net.Socket socket, ServidorGUI sg){
        this.sg = sg;
        this.socket = socket;
        
        try{
            entrada = new DataInputStream(socket.getInputStream());

        }catch(IOException e){
            sg.agregarMensaje("[SocketThreadIOException]: "+ e.getMessage());
        }
    }
    
    //Este método obtendrá el socket del cliente en la lista de socket del cliente y luego establecerá una conexión  
    private void createConnection(String receiver, String sender, String filename){
        try {
            sg.agregarMensaje("[createConnection]: Creando conexión para compartir archivos..");
            java.net.Socket s = sg.getListaCliente(receiver);
            //Verificar que el cliente exista
            if(s != null){
                sg.agregarMensaje("[createConnection]: Socket OK");
                DataOutputStream dosS = new DataOutputStream(s.getOutputStream());
                sg.agregarMensaje("[createConnection]: DataOutputStream OK");
               //Enviar comando de envio de archivo al case
                String format = "CMD_FILE_XD "+sender+" "+receiver +" "+filename;
                dosS.writeUTF(format);
                sg.agregarMensaje("[createConnection]: "+ format);
                //Si el cliente no existe entonces se le notifica al que envia el archivo
            }else{
                sg.agregarMensaje("[createConnection]: El cliente no se encontró.'"+receiver+"'");
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF("CMD_SENDFILEERROR "+ "Client '"+receiver+"' no se encontró en la lista. Verifique que se encuentre activo.");
            }
        } catch (IOException e) {
            sg.agregarMensaje("[createConnection]: "+ e.getLocalizedMessage());
        }
    }
    
    @Override
    public void run() {
        try {
            while(true){
                //Obtener los datos del cliente
                String data = entrada.readUTF();
                st = new StringTokenizer(data);
                String CMD = st.nextToken();
                //Casos para los comandos[cmd]
                switch(CMD){
                    case "CMD_JOIN":
                        //Un cliente se une al chat
                        String clientUsername = st.nextToken();
                        cliente = clientUsername;
                        sg.setListaCliente(clientUsername);
                        sg.setSocketList(socket);
                        sg.agregarMensaje("[Cliente]: "+ clientUsername +" ha ingresado al chat.");
                        break;
                     //Un cliente manda un mensaje   
                    case "CMD_CHAT":
                        String de = st.nextToken();
                        String enviarA = st.nextToken();
                        String msg = "";
                        while(st.hasMoreTokens()){
                            msg = msg +" "+ st.nextToken();
                        }
                        java.net.Socket tsoc = sg.getListaCliente(enviarA);
                        try {
                            DataOutputStream dos = new DataOutputStream(tsoc.getOutputStream());
                            //Mandar comando de mensaje al case
                            String content = de +": "+ msg;
                            dos.writeUTF("CMD_MESSAGE "+ content);
                            sg.agregarMensaje("[Message]: De "+ de +" Para "+ enviarA +" : "+ msg);
                        }catch(IOException e){ 
                            sg.agregarMensaje("[IOException]: No se ha podido mandar el mensaje a "+ enviarA); }
                        break;
                    
                    //Mensajes que se envien entre clientes
                    case "CMD_CHATALL":
                        String chatall_from = st.nextToken();
                        String chatall_msg = "";
                        while(st.hasMoreTokens()){
                            chatall_msg = chatall_msg +" "+st.nextToken();
                        }
                        String chatall_content = chatall_from +" "+ chatall_msg;
                        for(int x=0; x < sg.clienteLista.size(); x++){
                            if(!sg.clienteLista.elementAt(x).equals(chatall_from)){
                                try {
                                    java.net.Socket tsoc2 = (java.net.Socket) sg.socketLista.elementAt(x);
                                    DataOutputStream dos2 = new DataOutputStream(tsoc2.getOutputStream());
                                    //Mostar en el servidor los mensajes que se envien y quien los envia
                                    dos2.writeUTF("CMD_MESSAGE "+ chatall_content);
                                } catch (IOException e) {
                                    sg.agregarMensaje("[CMD_CHATALL]: "+ e.getMessage());
                                }
                            }
                        }
                        sg.agregarMensaje("[CMD_CHATALL]: "+ chatall_content);
                        break;
                    
                    case "CMD_SHARINGSOCKET":
                        sg.agregarMensaje("CMD_SHARINGSOCKET : El cliente ha establecido una conexión de socket para compartir archivos...");
                        String file_sharing_username = st.nextToken();
                        filesharing_username = file_sharing_username;
                        sg.setClientFileSharingUsername(file_sharing_username);
                        sg.setClientFileSharingSocket(socket);
                        sg.agregarMensaje("CMD_SHARINGSOCKET : Usuario: "+ file_sharing_username);
                        sg.agregarMensaje("CMD_SHARINGSOCKET : Ahora está disponible enviar archivos");
                        break;
                    
                    case "CMD_SENDFILE":
                        sg.agregarMensaje("CMD_SENDFILE : El cliente está enviando un archivo...");
                        String file_name = st.nextToken();
                        String filesize = st.nextToken();
                        String sendto = st.nextToken();
                        String consignee = st.nextToken();
                        sg.agregarMensaje("CMD_SENDFILE : De: "+ consignee);
                        sg.agregarMensaje("CMD_SENDFILE : Para: "+ sendto);
                        //Obtener el socket del cliente
                        sg.agregarMensaje("CMD_SENDFILE : Preparando conexiones..");
                        //socket del destinatario
                        java.net.Socket cSock = sg.getClientFileSharingSocket(sendto);
                        //Verificar que el socket del destinatario existe
                        if(cSock != null){ 
                            try {
                                sg.agregarMensaje("CMD_SENDFILE : Conectado..!");
                                //Escribir el nombre del archivo
                                sg.agregarMensaje("CMD_SENDFILE : Enviando archivo al cliente...");
                                DataOutputStream cDos = new DataOutputStream(cSock.getOutputStream());
                                cDos.writeUTF("CMD_SENDFILE "+ file_name +" "+ filesize +" "+ consignee);
                                //Escribir contenido
                                InputStream input = socket.getInputStream();
                                OutputStream sendFile = cSock.getOutputStream();
                                byte[] buffer = new byte[BUFFER_SIZE];
                                int cnt;
                                while((cnt = input.read(buffer)) > 0){
                                    sendFile.write(buffer, 0, cnt);
                                }
                                sendFile.flush();
                                sendFile.close();
                                //Eliminar el cliente de la lista
                                sg.removeClientFileSharing(sendto);
                                sg.removeClientFileSharing(consignee);
                                sg.agregarMensaje("CMD_SENDFILE : El archivo se envió al cliente...");
                            }catch(IOException e){
                                sg.agregarMensaje("[CMD_SENDFILE]: "+ e.getMessage());
                            }
                            //Si no existe se manda un errror
                        }else{
                            
                            sg.removeClientFileSharing(consignee);
                            sg.agregarMensaje("CMD_SENDFILE : Cliente '"+sendto+"' no ha sido encontrado.");
                            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                            dos.writeUTF("CMD_SENDFILEERROR "+ "Cliente '"+sendto+"' no se encontró.");
                        }                        
                        break;
                        
                    //Caso para cuando el cliente ha aceptado o no recibir el archivo    
                    case "CMD_SENDFILERESPONSE":
                        String receiver = st.nextToken(); // get the receiver username
                        String rMsg = ""; // get the error message
                        sg.agregarMensaje("[CMD_SENDFILERESPONSE]: usuario: "+ receiver);
                        while(st.hasMoreTokens()){
                            rMsg = rMsg+" "+st.nextToken();
                        }
                        try {
                            java.net.Socket rSock = (java.net.Socket) sg.getClientFileSharingSocket(receiver);
                            DataOutputStream rDos = new DataOutputStream(rSock.getOutputStream());
                            rDos.writeUTF("CMD_SENDFILERESPONSE" +" "+ receiver +" "+ rMsg);
                        } catch(IOException e){
                            sg.agregarMensaje("[CMD_SENDFILERESPONSE]: "+ e.getMessage());
                        }
                        break;
                        
                        
                    case "CMD_SEND_FILE_XD":                       
                        try {
                            String send_sender = st.nextToken();
                            String send_receiver = st.nextToken();
                            String send_filename = st.nextToken();
                            sg.agregarMensaje("[CMD_SEND_FILE_XD]: Host: "+ send_sender);
                            this.createConnection(send_receiver, send_sender, send_filename);
                        }catch(Exception e){
                            sg.agregarMensaje("[CMD_SEND_FILE_XD]: "+ e.getLocalizedMessage());
                        }
                        break;
                        
                        
                    case "CMD_SEND_FILE_ERROR":
                        String eReceiver = st.nextToken();
                        String eMsg = "";
                        while(st.hasMoreTokens()){
                            eMsg = eMsg+" "+st.nextToken();
                        }
                        try {
                            //Enviar error sl host que esta compartiendo el archivo
                            java.net.Socket eSock = sg.getClientFileSharingSocket(eReceiver);
                            //Obtener el socket del cliente que comparte el archivo
                            DataOutputStream eDos = new DataOutputStream(eSock.getOutputStream());
                            //comando error
                            eDos.writeUTF("CMD_RECEIVE_FILE_ERROR "+ eMsg);
                        } catch (IOException e) {
                            sg.agregarMensaje("[CMD_RECEIVE_FILE_ERROR]: "+ e.getMessage());
                        }
                        break;
                        
                    
                    case "CMD_SEND_FILE_ACCEPT":
                        String aReceiver = st.nextToken();
                        String aMsg = "";
                        while(st.hasMoreTokens()){
                            aMsg = aMsg+" "+st.nextToken();
                        }
                        try {
                            //Enviar error al cliente que envia el archivo
                            //obtener el socket del host para compartir archivos para la conexión
                            java.net.Socket aSock = sg.getClientFileSharingSocket(aReceiver); 
                            DataOutputStream aDos = new DataOutputStream(aSock.getOutputStream());
                            //Comando para aceptar el archivo que se verifica en los case
                            aDos.writeUTF("CMD_RECEIVE_FILE_ACCEPT "+ aMsg);
                        } catch (IOException e) {
                            sg.agregarMensaje("[CMD_RECEIVE_FILE_ERROR]: "+ e.getMessage());
                        }
                        break;
                        
                        
                    default: 
                        sg.agregarMensaje("[CMDException]: Comando desconocido "+ CMD);
                    break;
                }
            }
        } catch (IOException e) {
            //Para el cliente que está en el chat
            System.out.println(cliente);
            System.out.println("Archivo compartido: " +filesharing_username);
            sg.removeFromTheList(cliente);
            //Si existe entonces se elimina
            if(filesharing_username != null){
                sg.removeClientFileSharing(filesharing_username);
            }
            sg.agregarMensaje("[SocketThread]: La conexión con el cliente se ha cerrado.");
        }
    }
}

