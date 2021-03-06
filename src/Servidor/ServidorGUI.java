/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;


/**
 *
 * @author Ismael Cortes
 */

public class ServidorGUI extends javax.swing.JFrame {

    Servidor server;
    Thread hilo;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
    public Vector clienteLista = new Vector();
    public Vector socketLista = new Vector();
    
    public Vector clientFileSharingUsername = new Vector();
    public Vector clientFileSharingSocket = new Vector();
    
    /**
     * Creates new form ServidorGUI
     */
    
    public ServidorGUI() {
        initComponents();
        setLocationRelativeTo(null);//Esto sirve para que aparezca la ventana en el centro
        //Evitar que el boton detener este disponible antes de iniciar el servidor
        detener_btn.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ventana_txt = new javax.swing.JTextArea();
        iniciar_btn = new javax.swing.JButton();
        detener_btn = new javax.swing.JButton();
        puerto_txt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(153, 0, 0));

        ventana_txt.setEditable(false);
        ventana_txt.setColumns(20);
        ventana_txt.setRows(5);
        jScrollPane1.setViewportView(ventana_txt);

        iniciar_btn.setBackground(new java.awt.Color(153, 0, 0));
        iniciar_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/archivos/start.png"))); // NOI18N
        iniciar_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iniciar_btnActionPerformed(evt);
            }
        });

        detener_btn.setBackground(new java.awt.Color(255, 255, 255));
        detener_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/archivos/detener.png"))); // NOI18N
        detener_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detener_btnActionPerformed(evt);
            }
        });

        puerto_txt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                puerto_txtKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                puerto_txtKeyTyped(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Puerto:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 23, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(iniciar_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(detener_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(puerto_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(puerto_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(iniciar_btn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(detener_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(34, 34, 34))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void iniciar_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iniciar_btnActionPerformed
        // TODO add your handling code here:
        String aux = puerto_txt.getText().trim();
        
        //Verificar que ningun campo este vacio
        if(aux.length()==0){
            
            JOptionPane.showMessageDialog(this, "Se debe de agregar un puerto.", "Error", JOptionPane.ERROR_MESSAGE);
        
        }else{
                int puerto = Integer.parseInt(puerto_txt.getText());
                server = new Servidor(puerto, this);
                hilo = new Thread(server);
                hilo.start();
        
                new Thread(new ListaUsuariosOnlineThread(this)).start();
        
                iniciar_btn.setEnabled(false);
                detener_btn.setEnabled(true);
            }
               
    }//GEN-LAST:event_iniciar_btnActionPerformed

    private void detener_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detener_btnActionPerformed
        // TODO add your handling code here:
        //Verificar si el usuario desea detener el servidor
        int confirmacion = JOptionPane.showConfirmDialog(null, "??Desea cerrar el servidor?");
        if(confirmacion == 0){
            server.stop();
        }
    }//GEN-LAST:event_detener_btnActionPerformed

    private void puerto_txtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_puerto_txtKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_puerto_txtKeyPressed

    private void puerto_txtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_puerto_txtKeyTyped
        // TODO add your handling code here:
         String s1 = String.valueOf(evt.getKeyChar());
        //Validacion para que solo se pueda ingresar numeros
        if(!(s1.matches("[0-9]"))){
            evt.consume();
            getToolkit().beep();        
        }
    }//GEN-LAST:event_puerto_txtKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServidorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServidorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServidorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServidorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServidorGUI().setVisible(true);
                
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton detener_btn;
    private javax.swing.JButton iniciar_btn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField puerto_txt;
    private javax.swing.JTextArea ventana_txt;
    // End of variables declaration//GEN-END:variables

   public void setListaCliente(String clientUsername) {
       try{
       clienteLista.add(clientUsername);
       agregarMensaje("Agregar Cliente a lista: Agregado");
       }catch(Exception e){
           agregarMensaje("Agregar Cliente a lista: "+e.getMessage());       
       }
    }
   
   //Agregar socket a la lista 
    void setSocketList(Socket socket) {
        try {
            socketLista.add(socket);
            agregarMensaje("Agregar socket a lista: Agregado");
        }catch (Exception e){
            agregarMensaje("Agregar socket a lista: "+ e.getMessage()); }
    }
    
    //Agregar mensaje en la gui del servidor con hora en la que se ha mandado
    void agregarMensaje(String msj) {
        Date date = new Date();
        ventana_txt.append(sdf.format(date) +": "+ msj +"\n");
        ventana_txt.setCaretPosition(ventana_txt.getText().length() - 1);
    }
       
    //Obtener la lista de clientes conectados
    public Socket getListaCliente(String cliente) {
        Socket tsoc = null;
        
        for(int i=0;i<clienteLista.size();i++ ){
            if(clienteLista.get(i).equals(cliente)){
               tsoc = (Socket)socketLista.get(i);
               break;
            }
        }
        return tsoc;
    }

    public void setClientFileSharingUsername(String nombreUsuario) {
        try{
            //Agregar a la lsita de usuarios
            clientFileSharingUsername.add(nombreUsuario);
        }catch(Exception e){
            System.out.println("No se pudo completar el proceso");
        }
    }

    void setClientFileSharingSocket(Socket usuario) {
        try{
            //Agregar a la lsita
            clientFileSharingSocket.add(usuario);
        }catch(Exception e){
            System.out.println("No se pudo completar el proceso");
        }
    }

    public Socket getClientFileSharingSocket(String usuario) {
        Socket tsoc = null;
        for(int x=0; x < clientFileSharingUsername.size(); x++){
            if(clientFileSharingUsername.elementAt(x).equals(usuario)){
                tsoc = (Socket) clientFileSharingSocket.elementAt(x);
                break;
            }
        }
        return tsoc;
    }
    
    //Eliminar a cliente de la lista para compartir un archivo
    void removeClientFileSharing(String usuario) {
        for(int x=0; x < clientFileSharingUsername.size(); x++){
            if(clientFileSharingUsername.elementAt(x).equals(usuario)){
                try {
                    Socket rSock = getClientFileSharingSocket(usuario);
                    if(rSock != null){
                        //Cerrar socket
                        rSock.close();
                    }
                    //Eliminar a cliente
                    clientFileSharingUsername.removeElementAt(x);
                    clientFileSharingSocket.removeElementAt(x);
                    agregarMensaje("Compartir Archivo - Eliminado:  "+ usuario);
                } catch (IOException e) {
                    agregarMensaje("Compartir Archivo - "+ e.getMessage());
                    agregarMensaje("Compartir Archivo - No se puede eliminar: "+ usuario);
                }
                break;
            }
        }
    }

    //Eliminar a cliente de la lista, mostrando un mensaje en el servidor de que ha salido
    void removeFromTheList(String cliente) {
        try {
            for(int x=0; x < clienteLista.size(); x++){
                //Verificar cual cliente ha salido
                if(clienteLista.elementAt(x).equals(cliente)){
                    clienteLista.removeElementAt(x);
                    socketLista.removeElementAt(x);
                    //Mostrar mensaje en gui del cliente
                    agregarMensaje("Ha salido: "+ cliente);
                    break;
                }
            }
        } catch (Exception e) {
            agregarMensaje("[RemovedException]: "+ e.getMessage());
        }
    }

}
