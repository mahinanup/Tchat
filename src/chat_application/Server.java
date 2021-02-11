package chat_application;
import java.net.*;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*; 
public class Server extends JFrame{
	
	ServerSocket server;
	Socket socket;
	BufferedReader br;
	PrintWriter out;
	private JLabel heading = new JLabel("Client Area");
	private JTextArea messageArea = new JTextArea();
	private JTextField messageInput = new JTextField();
	private Font font = new Font("Roboto",Font.PLAIN,20);
	
	
	public Server() {
		
		try {
			server = new ServerSocket(7777);
			System.out.println("Server is Ope at port 7777 ");
			System.out.println("Waiting for client sonnection... ");
			createGUI();
			socket = server.accept();
			System.out.println("Client "+socket.getInetAddress()+" has connected");
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			
			
			//createGUI();
			handleEvents();
			startReading();
			//startWriting(); 

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
private void createGUI() {
		
		this.setTitle("Server Side");
		this.setSize(500,700);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		// components 
		
		heading.setFont(font);
		messageArea.setFont(font);
		messageInput.setFont(font);
		
		heading.setIcon(new ImageIcon("images/logo.png"));
		heading.setHorizontalTextPosition(SwingConstants.CENTER);
		heading.setVerticalTextPosition(SwingConstants.BOTTOM);
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		messageArea.setEditable(false);
		
		this.setLayout(new BorderLayout() );
		
		//adding components
		this.add(heading, BorderLayout.NORTH);
		JScrollPane jScrollPane = new JScrollPane(messageArea);
		this.add(jScrollPane, BorderLayout.CENTER);
		this.add(messageInput, BorderLayout.SOUTH);
		
		
		
		this.setVisible(true);
		

		
		
	}
private void handleEvents() {
	
	messageInput.addKeyListener(new KeyListener() {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			System.out.println("key released "+e.getKeyCode());
			if(e.getKeyCode()==10)
			{
				String contentToSend = messageInput.getText();
				messageArea.append("Me : "+contentToSend+"\n");
				out.println(contentToSend);
				out.flush();
				messageInput.setText("");
				messageInput.requestFocus();
			
			}
		}
		
	});
	
}
	
	public void startReading() {
		
		// thread 1 for reading 
		Runnable r1 = ()->{
			
			System.out.println("Reader has started ...");
			
			try {
				
			
			while(true) {
				String msg = br.readLine();
				if(msg.equals("exit"))
				{
					System.out.println("Client terminated the chat ");
					JOptionPane.showMessageDialog(this, "Client terminated the chat ");
					messageInput.setEnabled(false);
					socket.close();
					break;
				}
				//System.out.println("Client : "+msg);
				messageArea.append("Client : "+msg+"\n");
			}
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		};
		
		new Thread(r1).start();
		
	}
	
	public void startWriting() {
		
		//thread 2 for writing 
		Runnable r2 = ()-> {
			
			System.out.println("writer has started ...");
			
			try {
				
			
			while(true && !socket.isClosed()) {
				BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
				String content = br1.readLine();
				out.println(content);
				out.flush();
				if(content.equals("exit"))
				{
					socket.close();
					break;
				}
			}
		}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		};
		
		new Thread(r2).start();
	}
	
	public static void main(String[] args) {
		
		System.out.println("This is the Server ... ");
		new Server();
	}

}
