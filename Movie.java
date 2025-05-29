package project;
/**
 * @author Simão Alexandre 61874
 */


public class Movie {
	
	private final String title;
	private final int year;
	private int quantity;
	private int[][] rentalsM;
	private double price;
	private double tax;
	private String code;
	
	/**
	 * Controi um novo filme com os parâmetros dados.
	 *
	 * @param title     titulo do filme
	 * @param year      ano de lançamento do filme
	 * @param quantity  quantidade deste filme no videoclub 
	 * @param rentals   número de alugueres
	 * @param price     preço do aluguer
	 * @param tax       impostos de aluguer e atraso
	 */
	public Movie(String title, int year, int quantity, String rentals, double price, double tax){
		
		this.title = title;
		this.year = year;
		this.quantity = quantity;
		this.rentalsM = setRentals(rentals);
		this.price = price;
		this.tax = tax;
	}
	/**
	 * @return título do filme 
	 */
	public  String getTitle() {
		return this.title;
	}
	/**
	 * @return número de vezes  que o filme foi alugado
	 */
	public int getYear() {
		return this.year;
	}
	/**
	 * @return quantidade deste filme no videoclub
	 */
	public int getQuantity() {
		return this.quantity;
	}
	/**
	 * @return preço do aluguer
	 */
	public double getPrice() {
		return this.price;
	}
	/**
	 * @return impostos de aluguer e de atraso
	 */
	public double getTax() {
		return this.tax / 100;
	}
	/**
	 * @return número de vezes  que o filme foi alugado
	 */
	public int [][] getRentals() {
		return this.rentalsM;
	}
	/**
	 * @param String s do tipo "(4;1) (27;5) (15;6)"
	 * 
	 * @return matriz com os dados dos alugueres(nº de utilizador e dias de aluguer)
	 */
	public int[][] setRentals(String s) { 
		this.rentalsM = new int [quantity][2];
		s = s.replace("(", "").replace(")", "");
		String [] v = s.split(" ");
		int k = 0; 
		for(int l = 0; l < v.length / 2; l++) {
			this.rentalsM[l][0] = Integer.parseInt(v[k].split(";")[0]);				
			this.rentalsM[l][1] = Integer.parseInt(v[k].split(";")[1]);
		}
		return this.rentalsM;
	}
	/**
	 * Este método retorna o número de vezes que o filme foi alugado
	 * 
	 * @return o número de vezes que o filme foi alugado
	 */
	public int numberOfRentals() {
		int rentalCounter = 0;
		for(int i = 0; i < this.rentalsM.length; i++) {  
			if(this.rentalsM[i][0] != 0) {				
				rentalCounter++;
			}
		}
		return rentalCounter;
	}
	/**
	 * Este método verifica se o filme está disponivel para ser alugado
	 * 
	 * @return true se estiver disponivel, false caso contrário
	 */
	public boolean isAvailable(Movie movie) {
		return (movie.getQuantity() - movie.numberOfRentals() > 0);
	}
	/**
	 * Este método atualiza a matriz com a informação dos rentals quando um filme é alugado
	 */
	public void rentMovie(int user) {
		for(int i = 0; i < this.rentalsM.length; i++) {
			if(this.rentalsM[i][0] == 0) {
				this.rentalsM[i][0] = user;
				this.rentalsM[i][0] = 7;
				break;
			}
		}
	}
	/**
	 * @return número de vezes  que o filme foi alugado
	 */
	public void returnMovie(int user) {
		for(int i = 0; i < this.rentalsM.length; i++) {
			if(this.rentalsM[i][0] == user ) {
				this.rentalsM[i][0] = 0;
				this.rentalsM[i][1] = 0;
			}
		}
	}
	/**
	 * Este método passa a matriz com a informação dos alugueres para uma String do tipo "(4;1) (27;5) (15;6)"
	 * 
	 * @return uma String do tipo "(4;1) (27;5) (15;6)"
	 */
	public String rentalsToString() {
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < this.rentalsM.length; i++) {
			if(this.rentalsM[i][0] != 0) {
				sb.append("("+ this.rentalsM[i][0] + ";" + this.rentalsM[i][1] + ") ");
			}
			
		}
		
		return sb.toString();// 
	}
	/**
	 * Este método cria o código correspondente ao titulo do filme
	 * 
	 * @return o código correspondente ao titulo do filme
	 */
	public String getCode(){
		String s = this.title;
		s = s.replaceAll("[^a-zA-Z]", "").toUpperCase();
		char [][] m = new char [3][s.length()];
		int i = 0;
		boolean direction = true;
		for (int j = 0; j < s.length(); j++) {
			if (direction) {
				m[i][j] = s.charAt(j);
				i++;
			}else {
		        m[i][j] = s.charAt(j);
		        i--;
			}
		    if(i == 3 || i == -1) {
		    	direction = !direction; 
		        i = 1;
		    }
		}
		StringBuilder cota = new StringBuilder();
		for(int l = 0; l < m.length; l++) {
			for(int c = 0; c < m[0].length; c++) {
				if((m[l][c])  >= 'A' && (m[l][c]) <= 'Z' )
				cota.append(m[l][c]);
			}
		}
		this.code = cota.substring(0, 3) + cota.substring(cota.length() - 3);
		return this.code;
	}
}
