import java.io.*;



public class MenuOperacoes {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		System.out.println("Informe a operação:");
		System.out.println("1. Enviar Arquivo");
		System.out.println("2. Receber Arquivo");

		BufferedReader brEntrada = new BufferedReader(new InputStreamReader(System.in));		
		String strOperacao = brEntrada.readLine();
		
		System.out.println("Operação: " + strOperacao);
		
		Cliente SocketCliente = new Cliente(strOperacao);
		SocketCliente.realizarConexao();
	
		
	}

}
