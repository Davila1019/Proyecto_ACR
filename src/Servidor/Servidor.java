/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;




public class Servidor implements Runnable{
    ServerSocket server;
    ServidorGUI sg;
    boolean seguir = true;
    
    public Servidor(int port, ServidorGUI sg){
        //Agregar mensaje a la gui del servidor
        sg.agregarMensaje("[Servidor]: Iniciando servidor en el puerto: "+ port);
        try {
            this.sg = sg;
            server = new ServerSocket(port);
            //Agregar mensaje a la gui del servidor
            sg.agregarMensaje("[Servidor]: Servidor a la escucha...");
        }catch(IOException e){
            sg.agregarMensaje("[IOException]: "+ e.getMessage()); }
    }

    @Override
    public void run() {
        try {
            while(seguir){
                Socket socket = server.accept();
               //Empezar el hilo de socket
                new Thread(new SocketHilo(socket, sg)).start();
            }
        }catch(IOException e){
            sg.agregarMensaje("[ServerThreadIOException]: "+ e.getMessage());
        }
    }
    
    public void stop(){
        try {
            //Detener servidor
            server.close();
            //El estado para que siga activo el proceso del servidor se cambia a falso
            seguir = false;
            //Imprimir mensaje en consola
            System.out.println("El servidor se ha cerrado...");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
