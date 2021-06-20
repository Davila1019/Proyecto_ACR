/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitorInputStream;




public class RecibirArchivo implements Runnable{
    
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private ChatGUI cg;
    private StringTokenizer st;
    private final int BUFFER_SIZE = 100;
    
    public RecibirArchivo(Socket s, ChatGUI cg){
        this.socket = s;
        this.cg = cg;
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("[ReceivingFileThread]: " +e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()){
                String data = dis.readUTF();
                st = new StringTokenizer(data);
                String CMD = st.nextToken();
                
                switch(CMD){                  
                    //Esto manejara la recepción de un archivo en proceso en segundo plano de otro usuario
                    case "CMD_SENDFILE":
                        String consignee = null;
                            try {
                                String filename = st.nextToken();
                                int filesize = Integer.parseInt(st.nextToken());
                                //Obtener el nombre de usuario que envia el archivo
                                consignee = st.nextToken();
                                cg.setMyTitle("Descargando archivo....");
                                System.out.println("Descargando archivo....");
                                System.out.println("De: "+ consignee);
                                String path = cg.getMyDownloadFolder() + filename;                                
                                //stream para el archivo
                                FileOutputStream fos = new FileOutputStream(path);
                                InputStream input = socket.getInputStream();                                
                                //Progreso del monitor, gui
                                ProgressMonitorInputStream pmis = new ProgressMonitorInputStream(cg, "Descargando archivo, por favor espere...", input);
                                //Crear buffer
                                BufferedInputStream bis = new BufferedInputStream(pmis);
                                //Crear un archivo temporal
                                byte[] buffer = new byte[BUFFER_SIZE];
                                int count, percent = 0;
                                while((count = bis.read(buffer)) != -1){
                                    percent = percent + count;
                                    int p = (percent / filesize);
                                    cg.setMyTitle("Descargando archivo: "+ p +"%");
                                    fos.write(buffer, 0, count);
                                }
                                fos.flush();
                                fos.close();
                                //Volver a poner el titulo del inicio una vex que se termine de 
                                //descargar el archivo recibido
                                cg.setMyTitle("Ha iniciado sesión como: " + cg.getMyUsername());
                                JOptionPane.showMessageDialog(null, "El archivo ha sido descargado en: \n'"+ path +"'");
                                System.out.println("El archivo se guardó con éxito en: "+ path);
                            }catch(IOException e){
                                //Enviar un mensaje de error al cliente que envia elarchivo
                                DataOutputStream eDos = new DataOutputStream(socket.getOutputStream());
                                eDos.writeUTF("CMD_SENDFILERESPONSE "+ consignee + "La conexión se ha perdido.");
                                System.out.println(e.getMessage());
                                cg.setMyTitle("Ha iniciado sesión como: " + cg.getMyUsername());
                                JOptionPane.showMessageDialog(cg, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
                                socket.close();
                            }
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("[ReceivingFileThread]: " +e.getMessage());
        }
    }
}   

