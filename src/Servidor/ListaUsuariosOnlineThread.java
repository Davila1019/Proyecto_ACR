/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;




public class ListaUsuariosOnlineThread implements Runnable{

        
    ServidorGUI sg;
    
    public ListaUsuariosOnlineThread(ServidorGUI sg){
        this.sg = sg;
    }

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()){
                String msj = "";
                //lista de usuarios que se encuentran en linea para agregar
                for(int x=0; x < sg.clienteLista.size(); x++){
                    msj = msj+" "+ sg.clienteLista.elementAt(x);
                }
                
                for(int x=0; x < sg.socketLista.size(); x++){
                    //agregar elementos a la lista de socket
                    Socket tsoc = (Socket) sg.socketLista.elementAt(x);
                    DataOutputStream salida = new DataOutputStream(tsoc.getOutputStream());
                    //Mandar comando de estado activo junto con el mensaje para
                    //que el case lo reconozca y haga su operacion correspondiente
                    if(msj.length() > 0){
                        salida.writeUTF("CMD_ONLINE "+ msj);
                    }
                }
                
                Thread.sleep(1900);
            }
        } catch(InterruptedException e){
            sg.agregarMensaje("[InterruptedException]: "+ e.getMessage());
        } catch (IOException e) {
            sg.agregarMensaje("[IOException]: "+ e.getMessage());
        }
    }
    
}
