package chatter;

  import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.*;

public class Client extends JFrame implements ActionListener, KeyListener {

private static final long serialVersionUID = 1L;
private JTextArea texto;
private JTextField txtMsg;
private JButton btnSend;
private JButton btnSair;
private JLabel lblHistorico;
private JLabel lblMsg;
private JPanel pnlContent;
private Socket socketCliente;

private OutputStream output ;
private Writer outputw; 
private BufferedWriter buffer;
private JTextField txtIP;
private JTextField txtPorta;
private JTextField txtNome;
private KeyPairGenerator assimetrica; 
private PrivateKey chavePrivada;
private PublicKey chavePublica;
private PublicKey chaveDestinatario;
private SecretKey chaveSecreta;

  public Client() throws IOException{                  
    JLabel lblMessage = new JLabel("Verificar!");
    txtIP = new JTextField("127.0.0.1");
    txtPorta = new JTextField("12345");
    txtNome = new JTextField("Cliente");                
    Object[] texts = {lblMessage, txtIP, txtPorta, txtNome };  
    JOptionPane.showMessageDialog(null, texts);              
     pnlContent = new JPanel();
     texto              = new JTextArea(10,20);
     texto.setEditable(false);
     texto.setBackground(new Color(240,240,240));
     txtMsg                       = new JTextField(20);
     lblHistorico     = new JLabel("Histórico");
     lblMsg        = new JLabel("Mensagem");
     btnSend                     = new JButton("Enviar");
     btnSend.setToolTipText("Enviar Mensagem");
     btnSair           = new JButton("Sair");
     btnSair.setToolTipText("Sair do Chat");
     btnSend.addActionListener(this);
     btnSair.addActionListener(this);
     btnSend.addKeyListener(this);
     txtMsg.addKeyListener(this);
     JScrollPane scroll = new JScrollPane(texto);
     texto.setLineWrap(true);  
     pnlContent.add(lblHistorico);
     pnlContent.add(scroll);
     pnlContent.add(lblMsg);
     pnlContent.add(txtMsg);
     pnlContent.add(btnSair);
     pnlContent.add(btnSend);
     pnlContent.setBackground(Color.LIGHT_GRAY);                                 
     texto.setBorder(BorderFactory.createEtchedBorder(Color.BLUE,Color.BLUE));
     txtMsg.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));                    
     setTitle(txtNome.getText());
     setContentPane(pnlContent);
     setLocationRelativeTo(null);
     setResizable(false);
     setSize(250,300);
     setVisible(true);
     setDefaultCloseOperation(EXIT_ON_CLOSE);
}

  public void conectar() throws IOException{
                          
  socketCliente = new Socket(txtIP.getText(),Integer.parseInt(txtPorta.getText()));
  output = socketCliente.getOutputStream();
  outputw = new OutputStreamWriter(output);
  buffer = new BufferedWriter(outputw);
  buffer.write(txtNome.getText()+"\r\n");
  buffer.flush();
}

  public void enviarMensagem(String msg) throws IOException{
                          
    if(msg.equals("Sair")){
      buffer.write("Desconectado \r\n");
      texto.append("Desconectado \r\n");
    }else{
      buffer.write(msg+"\r\n");
      texto.append( txtNome.getText() + " diz -> " + txtMsg.getText()+"\r\n");
    }
     buffer.flush();
     txtMsg.setText("");        
}

public void escutar() throws IOException{
                          
   InputStream in = socketCliente.getInputStream();
   InputStreamReader inr = new InputStreamReader(in);
   BufferedReader bfr = new BufferedReader(inr);
   String msg = "";
                          
    while(!"Sair".equalsIgnoreCase(msg))
                                     
       if(bfr.ready()){
         msg = bfr.readLine();
       if(msg.equals("Sair"))
         texto.append("Servidor caiu! \r\n");
        else
         texto.append(msg+"\r\n");         
        }
}           

   public void sair() throws IOException{
                          
    enviarMensagem("Sair");
    buffer.close();
    outputw.close();
    output.close();
    socketCliente.close();
 }

public void actionPerformed(ActionEvent e) {
         
  try {
     if(e.getActionCommand().equals(btnSend.getActionCommand()))
        enviarMensagem(txtMsg.getText());
     else
        if(e.getActionCommand().equals(btnSair.getActionCommand()))
        sair();
     } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
     }                       
}
  
public void keyPressed(KeyEvent e) {
               
    if(e.getKeyCode() == KeyEvent.VK_ENTER){
       try {
          enviarMensagem(txtMsg.getText());
       } catch (IOException e1) {
           // TODO Auto-generated catch block
           e1.printStackTrace();
       }                                                          
   }                       
}

public void keyReleased(KeyEvent arg0) {
  // TODO Auto-generated method stub               
}
   
public void keyTyped(KeyEvent arg0) {
  // TODO Auto-generated method stub               
}           
public static void main(String []args) throws IOException{
   Client app = new Client();
   app.conectar();
   app.escutar();
  }
}           

