import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.io.*;


public class Cliente {

	private String operacao;
	
	public Cliente (String operacao) {
		this.operacao = operacao;		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args)  {	
		// TODO Auto-generated method stub
		
		
	}	
	
	public void realizarConexao () throws UnknownHostException, IOException {
		try {
			
			Socket socketCliente = new Socket("127.0.0.1",18800);			
			System.out.println("Realizada conexão com o Servidor: " + socketCliente.getInetAddress().getHostAddress());
			
			//BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
			DataOutputStream outtoServer = new DataOutputStream(socketCliente.getOutputStream());				
						
			if (this.operacao.contains("2")) {		
				outtoServer.writeBytes(this.operacao + '\n');
				outtoServer.flush();
				
				receberArquivoByte(socketCliente);				

				System.out.println("Bye");
			}
			else {	
				
				outtoServer.writeBytes(this.operacao + '\n');
				outtoServer.flush();
				
				enviarArquivoByte(socketCliente);			
										
				System.out.println("Bye");
				
			}									
							
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
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
			
			System.out.println("Aguardando recebimento do arquivo....");
			String arquivoNome = "/home/andre/redes/ClientFiles/Retorno_Servidor.txt";
			
			//recebendo o arquivo vindo do servidor
			
			byte [] mybytearray = new byte[tamanhoArquivo];
			InputStream is = pSocketCliente.getInputStream();
			fos = new FileOutputStream(arquivoNome);
			bos = new BufferedOutputStream(fos);
			bytesRead = is.read(mybytearray,0,mybytearray.length);
			//System.out.println("byte array length:" + mybytearray.length);
			current = bytesRead;
			//System.out.println("current:" + current);
			
			System.out.println("Salvando arquivo em disco no Cliente....");
			
			do {
				bytesRead = is.read(mybytearray, current, (mybytearray.length-current));
				if (bytesRead >= 0) current += bytesRead;				
			} while (bytesRead > -1);
			
			bos.write(mybytearray, 0, current);
			bos.flush();
			System.out.println("Recebimento do arquivo " + arquivoNome + " concluído (" + current + " bytes lidos)" );
						
							
			
			
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
			System.out.println("Enviando arquivo para o Servidor...");
			String nomeArquivo  = "/home/andre/redes/ClientFiles/ArquivoCliente.txt";
			System.out.println("Caminho do arquivo no Cliente: " + nomeArquivo);
			
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
