import java.net.*;
import java.io.*;


public class Servidor implements Runnable{

	public Socket cliente;	
	
	public Servidor(Socket cliente){
		this.cliente = cliente;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		ServerSocket servidorSocket = new ServerSocket(18800);
		System.out.println("Porta 18800: aberta...");
		
		System.out.println("Aguardando nova conexão do cliente...");
		
		while (true) {
			
			Socket cliente = servidorSocket.accept();
			Servidor acao = new Servidor(cliente);
			Thread t = new Thread(acao);
			
			t.start();
		
		}		
		

	}
	
	public void run() {
		System.out.println("Nova conexão: " + this.cliente.getInetAddress().getHostAddress());
		
		try {
			String mensagemTela;			
			String opcao;
			mensagemTela = "Cliente: " + this.cliente.getInetAddress().getHostAddress() + " seja bem vindo!";		
			System.out.println(mensagemTela);
			
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(this.cliente.getInputStream()));
			//DataOutputStream outToClient = new DataOutputStream(this.cliente.getOutputStream());	
			
			opcao = inFromClient.readLine();		
										
			if (opcao.contains("2")) {		
				System.out.println("Operação selecionada no Cliente: Receber Arquivo");
				enviarArquivoByte(this.cliente);					
			}			
			else {							
				System.out.println("Operação selecionada pelo Cliente: Enviar arquivo");
				receberArquivoByte(this.cliente);							
			}
								
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}	
	 		
	public void receberArquivoByte (Socket pSocketCliente) throws IOException {
		int bytesRead;
		int current = 0;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		
		int tamanhoArquivo = 6022386;
		
		try {
			
			System.out.println("Aguardando envio do arquivo....");
			String arquivoNome = "/home/andre/redes/ServerFiles/ArquivoServidor.txt";
			
			
			//recebendo o arquivo vindo do cliente			
			byte [] mybytearray = new byte[tamanhoArquivo];
			InputStream is = pSocketCliente.getInputStream();
			fos = new FileOutputStream(arquivoNome);
			bos = new BufferedOutputStream(fos);
			bytesRead = is.read(mybytearray,0,mybytearray.length);			
			current = bytesRead;
			
			
			System.out.println("Salvando arquivo em disco no Servidor....");
			
			do {
				bytesRead = is.read(mybytearray, current, (mybytearray.length-current));
				if (bytesRead >= 0) current += bytesRead;				
			} while (bytesRead > -1);
			
			bos.write(mybytearray, 0, current);
			bos.flush();
			System.out.println("Envio do arquivo " + arquivoNome + " concluído (" + current + " bytes lidos)" );
						
			System.out.println("Retornando para o cliente...");					
			
			
		} finally {
			if (fos != null) fos.close();
			if (bos != null) bos.close();	
			
		}
		
	}
	
	public void enviarArquivoByte (Socket pSocketCliente) throws IOException {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;
		
		try {
			System.out.println("Enviando arquivo para o Cliente...");
			String nomeArquivo  = "/home/andre/redes/ServerFiles/ArquivoServidor.txt";
			System.out.println("Caminho do arquivo no Servidor: " + nomeArquivo);
			
			//enviar arquivo
			System.out.println("Lendo arquivo...");
			
			File myFile = new File(nomeArquivo);
			byte [] mybytearray = new byte [(int)myFile.length()];
			fis = new FileInputStream(myFile);
			bis = new BufferedInputStream(fis);
			bis.read(mybytearray,0,mybytearray.length);
			
			os = pSocketCliente.getOutputStream();
			System.out.println("Enviando " + nomeArquivo + "(" + mybytearray.length + " bytes)");
			os.write(mybytearray,0,mybytearray.length);
			os.flush();
			System.out.println("Envio concluído!");
						
			
		} finally {
			if (bis != null) bis.close();
			if (os != null) os.close();			
		}
		
	}	

}
