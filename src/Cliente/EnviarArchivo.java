/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;




public class EnviarArchivo implements Runnable{
    protected Socket socket;
    private DataOutputStream salida;
    protected EnviarArchivosGUI form;
    protected String file;
    protected String receiver;
    protected String sender;
    private final int BUFFER_SIZE = 100;
    
    public EnviarArchivo(Socket soc, String file, String receiver, String sender, EnviarArchivosGUI frm){
        this.socket = soc;
        this.file = file;
        this.receiver = receiver;
        this.sender = sender;
        this.form = frm;
    }

    @Override
    public void run() {
        try {
            form.inhabilitarGUI(true);
            System.out.println("Enviando archivo...");
            salida = new DataOutputStream(socket.getOutputStream());
            //Escribir el nombre del archivo, destinatario y nombre de archivo.
            //  Format: CMD_SENDFILE [Filename] [Size] [Recipient] [Consignee]
            File filename = new File(file);
            int lon = (int) filename.length();
            //Obtener el nombre del archivo
            int filesize = (int)Math.ceil(lon/BUFFER_SIZE);
            String clean_filename = filename.getName();
            //Mandar nombre de archivo sin espacios y su tamanio
            salida.writeUTF("CMD_SENDFILE "+ clean_filename.replace(" ", "_") +" "+ filesize +" "+ receiver +" "+ sender);
            System.out.println("De: "+ sender);
            System.out.println("Para: "+ receiver);
            InputStream input = new FileInputStream(filename);
            OutputStream output = socket.getOutputStream();
            
            //Leer archivo
            BufferedInputStream bis = new BufferedInputStream(input);
            //Crear un almacenamiento temporal de archivos
            byte[] buffer = new byte[BUFFER_SIZE];
            int cont, porcentaje = 0;
            //Obtener el porcentaje que se esta enviando del archivo
            while((cont = bis.read(buffer)) > 0){
                porcentaje = porcentaje + cont;
                int p = (porcentaje / filesize);
                //Poner en gui el porcentaje del archivo que se esta enviando
                form.setMyTitle(p +"% Enviando archivo...");
                output.write(buffer, 0, cont);
            }
            //Actualizar datos adjuntos desde la GUI
            form.setMyTitle("El archivo fue enviado.");
            //Actualizar archivo adjunto
            form.actualizarArchivoAdjunto(false);  
            JOptionPane.showMessageDialog(form, "El archivo se envió con éxito.", "Operación realizada con éxito", JOptionPane.INFORMATION_MESSAGE);
            form.closeThis();
            //Cerrar los streams
            output.flush();
            output.close();
            System.out.println("El archivo fue enviado.");
        } catch (IOException e) {
            //Actualizar Archivo adjunto
            form.actualizarArchivoAdjunto(false); 
            System.out.println("Enviar archivo: "+ e.getMessage());
        }
    }
}
