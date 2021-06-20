/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;




//Enum es una clase "especial" (tanto en Java como en otros lenguajes) que limitan
//la creación de objetos a los especificados explícitamente en la implementación de la clase.

public enum NotificacionMensaje {
  
    //Audio de notificacion para mensajes nuevos que ser eciban
    MessageReceive("/cliente/sonido.mp3", false);
    private Clip clip;
    private boolean loop;
    
    NotificacionMensaje(String filename, boolean loop){
        try{
            this.loop = loop;
            URL url = this.getClass().getResource(filename);
            AudioInputStream audioIS = AudioSystem.getAudioInputStream(url);
            
            clip = AudioSystem.getClip();
            clip.open(audioIS);
            
        }catch(IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.out.println("[Notificacion de Mensaje]" +e.getMessage());
        }
    }
    
    public void play(){
        if(clip.isRunning()){
            //Detener audio
            clip.stop();
        }
        //Iniciar audio desde el principio
        clip.setFramePosition(0);
        clip.start();
       
        //Comprobar si el audio se sigue reproduciendo de forma continua
        if(loop){
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    
    public void stop(){
        if(clip.isRunning()){
            //Detener audio
            clip.stop();
        }
    }
}
