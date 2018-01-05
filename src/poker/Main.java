package poker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {
	private static long inicio = 0;
	private static int qtdConflitos = 0;
	/**
	 * Abre o arquivo e lê linha por linha gravando em um arquivo de texto o 
	 * arquivo recuperado.
	 * @throws IOException
	 */
	private static void auditoria() throws IOException {
		inicio = System.currentTimeMillis();
			
		qtdConflitos = lerArquivo("pokerK.txt");
		gravarArquivo("saidaK.txt");
		
		qtdConflitos = lerArquivo("pokerM.txt");
		gravarArquivo("saidaM.txt");
		
		qtdConflitos = lerArquivo("poker100M.txt");
		gravarArquivo("saida100M.txt");
	}
	
	/**
	 * Abre o arquivo tipo texto percorre toda sua extensão
	 * verifica as jogadas contando os conflitos.
	 * @param nomeArquivo
	 * @return int
	 * @throws IOException
	 */
	private static int lerArquivo(String nomeArquivo) throws IOException{
		String jogada = null;
		int conflito = 0;
		FileReader reader = new FileReader(nomeArquivo);
		BufferedReader buffer = new BufferedReader(reader);

		while((jogada = buffer.readLine()) != null) {
			String p1 = jogada.substring(0, 14);
			String p2 = jogada.substring(15, 29);
			String ganhador = jogada.substring(30, 32);
			String resultado = null;
			
			if(jogos(p1) > jogos(p2)) {
				resultado = "P1";
			}
			if(jogos(p2) > jogos(p1)) {
				resultado = "P2";
			}
			if(jogos(p2) == jogos(p1)) {
				resultado = desempate(p1,p2);				
			}
			if(!ganhador.equals(resultado)) {
				conflito++;
			}
		}
		reader.close();
		buffer.close();
		return conflito;
	}
	
	/**
	 * Grava o arquivo em txt
	 * @param nomeArquivo
	 * @throws IOException
	 */
	private static void gravarArquivo(String nomeArquivo) throws IOException {
		FileWriter writer = new FileWriter(nomeArquivo);
		PrintWriter printer = new PrintWriter(writer);
		float tempoExecucao = 0;	
		printer.println(qtdConflitos);
		
		tempoExecucao = System.currentTimeMillis()-inicio;
		printer.printf("%.4f",tempoExecucao/1000);
		writer.close();
	}
		
	/**
	 * 	Testa cada jogada linha por linha
	 * @param jogada
	 * @return int
	 */
	private static int jogos(String jogada){
		char num[] = new char[5];
		char naipe[] = new char[5];
		int table[] = new int[31];
		String sequencia = "";
		int j = 1;
		int l = 0;
		
		//array com os naipes
		for (int i = 0; i<5; i++) {
			naipe[i] = jogada.charAt(j);
			j += 3;
		}
		
		//cria um array com os números das cartas
		for(int k = 0; k<5; k++) {
			num[k] = jogada.charAt(l);
			l += 3;
		}
		num = sort(num);
		
		//Tranforma o array com os números em uma String
		for(int m = 0; m<num.length; m++) {
			sequencia += num[m];
		}
		
		table = hash(sequencia);
		
		if(royalFlush(naipe,sequencia)) {
			return  23;
		}
		if(straightFlush(naipe,sequencia)) {
			return 22;
		}
		if(quadra(table)) {
			return 21;
		}
		if(fullHouse(table)) {
			return 20;
		}
		if(flush(naipe)) {
			return 19;
		}
		if(straight(sequencia)) {
			return 18;
		}
		if(trinca(table)) {
			return 17;
		}
		if(doisPares(table)) {
			return 16;
		}
		if(umPar(table)) {
			return 15;
		}
		return cartaAlta(sequencia);
		
	}
	
	/**
	 * Verifica se uma jogada tem o royal flush
	 * @param jogada
	 * @return boolean
	 */
	private static boolean royalFlush(char[] naipe, String sequencia) {
		int temDez = sequencia.indexOf("D");
		int temRainha = sequencia.indexOf("Q");
		int temValete = sequencia.indexOf("J");
		int temRei = sequencia.indexOf("K");
		int temAs = sequencia.indexOf("A");
		
		if(temDez >0 && temRainha >0 && temValete >0
				&& temRei >0 && temAs >0) {
			if(naipe[0] == naipe[1] && naipe[0] == naipe[2] &&
					naipe[0] == naipe[3] && naipe[0] == naipe[4]) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Verifica se em uma jogada existe o Straight Flush
	 * @param jogada
	 * @return boolean
	 */
	
	private static boolean straightFlush(char[] naipe, String sequencia) {				
		if(naipe[0] == naipe[1] && naipe[0] == naipe[2] &&
				naipe[0] == naipe[3] && naipe[0] == naipe[4]) {
			String naipes = "23456789DQJKA";
			if(naipes.contains(sequencia)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean quadra(int[] array) {
		for(int i=0; i < array.length; i++) {
			if(array[i] == 4) {
				return true;
			}
		}
		
		return false;
		
	}
	/**
	 * Verifica se a jogada tem Full House 
	 * @param sequencia
	 * @return boolean
	 */
	private static boolean fullHouse(int[] array) {
		boolean trinca = false;
		boolean par = false;
		
		for(int i=0; i < array.length; i++) {
			if(array[i] == 3) {
				trinca = true;
			}
			if(array[i] == 2) {
				par = true;
			}
		}
		if(trinca && par) {
			return true;
		}
		return false;
	}
	/**
	 * Verifica se tem flush entre os naipes
	 * @param naipe
	 * @return boolean
	 */
	private static boolean flush(char[] naipe) {
		if(naipe[0] == naipe[1] && naipe[0] == naipe[2] &&
				naipe[0] == naipe[3] && naipe[0] == naipe[4]) {
			return true;
		}
		return false;
	}
	
	/**
	 * Verifica se tem straight na sequencia
	 * @param sequencia
	 * @return boolean
	 */
	private static boolean straight(String sequencia) {
		String naipes = "23456789DQJKA";
		if(naipes.contains(sequencia)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 	Verifica se existe uma trinca na sequencia
	 * @param sequencia
	 * @return boolean
	 */
	private static boolean trinca(int[] array) {
		for(int i=0; i < array.length; i++) {
			if(array[i] == 3) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Verifica se a sequencia possui dois pares
	 * @param sequencia
	 * @return boolean
	 */
	private static boolean doisPares(int[] array) {
		int contador = 0;
		for(int i=0; i < array.length; i++) {
			if(array[i] == 2) {
				contador++;
			}
		}
		if(contador == 2) {
			return true;
		}
		return false;
	}
	
	/**
	 * Verifica se a sequencia possui um par
	 * @param sequencia
	 * @return boolean
	 */
	private static boolean umPar(int[] array) {
		for(int i=0; i < array.length; i++) {
			if(array[i] == 2) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna o valor da maior carta
	 * @param sequencia
	 * @return int
	 */
	private static int cartaAlta(String sequencia) {
		int carta = sequencia.charAt(4)-50;
		return valorCarta(carta);
	}
	
	/**
	 * Testa o valor de cada carta pegando a de maior valor.
	 * @param carta
	 * @return int
	 */
	private static int valorCarta(int carta) {
		int valor = 0;
		switch(carta) {
		case 0 : valor = 2; break;
		case 1 : valor = 3; break;
		case 2 : valor = 4; break;
		case 3 : valor = 5; break;
		case 4 : valor = 6; break;
		case 5 : valor = 7; break;
		case 6 : valor = 8; break;
		case 7 : valor = 9; break;
		case 18 : valor = 10; break;
		case 31 : valor = 11; break;
		case 24 : valor = 12; break;
		case 25 : valor = 13; break;
		case 15 : valor = 14; break;
		}
		return valor;
	}
	
	private static String desempate(String p1, String p2) {
		int ponteiro = 12;
		int cartaAltaP1 = 0;
		int cartaAltaP2 = 0;
		String ganhou = null;
		do {
			cartaAltaP1 = valorCarta(p1.charAt(ponteiro)-50);
			cartaAltaP2 = valorCarta(p2.charAt(ponteiro)-50);
			ponteiro = ponteiro - 2;
		}while(cartaAltaP1 == cartaAltaP2);
		if(cartaAltaP1 > cartaAltaP2) {
			ganhou = "P1";
		}
		if(cartaAltaP2 > cartaAltaP1) {
			ganhou =  "P2";
		}
		return ganhou;
		
	}
	
	/**
	 * Componente do quick sort que organiza as cartas do baralho.
	 * @param matriz
	 * @return Array
	 */
	private static char[] sort(char[] matriz) {
		int tamanho = matriz.length;
		matriz = quicksort(matriz,0,tamanho-1);
		return matriz;
	}
	
	/**
	 * Função Quick sort para organização das cartas
	 * @param matriz
	 * @param inicio
	 * @param fim
	 * @return Array
	 */
	private static char[] quicksort(char[] matriz, int inicio, int fim) {
		char pivo = matriz[inicio + (fim-inicio)/2];
		int i = inicio;
		int j = fim;
		
		while(i <= j) {
			while(valorCarta(matriz[i]) < valorCarta(pivo)) {
				i++;
			}
			while(valorCarta(matriz[j]) > valorCarta(pivo)) {
				j--;
			}
			
			if(inicio <= fim) {
				matriz = troca(matriz, i,j);
				i++;
				j--;
			}
		}
		
		if(j > inicio) {
			quicksort(matriz,inicio,j);
		}
		
		if(i < fim) {
			quicksort(matriz,i,fim);
		}
		return matriz;
	}
	
	/**
	 * Componente do quick sort que realiza a troca entre as
	 * posições da matriz.
	 * @param matriz
	 * @param i
	 * @param j
	 * @return Array
	 */
	private static char[] troca(char[] matriz, int i, int j) {
		char temp = matriz[i];
		 matriz[i] = matriz[j];
		 matriz[j] = temp;
		 return matriz;
	}
	/**
	 * Cria tabela com a frequencia de acontecimentos
	 * @param sequencia
	 * @return array
	 */
	private static int[] hash(String sequencia) {
		int[] tabela = new int[32];
		for(int i = 0; i < sequencia.length(); i++) {
			tabela[sequencia.charAt(i)-50]++;
		}
		return tabela;
	}
	
	public static void main(String[] args) throws IOException {
		auditoria();
	}
}
