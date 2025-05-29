package project;

/**
 * @author Simão Alexandre 61874
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Locale;

public class VideoClub {

	private double totalRevenue;
	private double totalProfit;
	Movie[] movieList;

	/**
	 * Controi um videoclube com todos filmes, e as suas informações.
	 *
	 * @param fileName       ficheiro CSV que contem as informações dos filmes
	 * @param numberOfMovies número de linhas que vão ser lidar do ficheiro CSV
	 */
	public VideoClub(String fileName, int numberOfMovies) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(fileName));
		int totalMovies = 0;
		sc.nextLine();
		while (sc.hasNextLine() && numberOfMovies > totalMovies) {
			sc.nextLine();
			totalMovies++;
		}
		this.movieList = new Movie[totalMovies];
		sc.close();

		Scanner s = new Scanner(new File(fileName));
		s.nextLine();
		for (int i = 0; i < this.movieList.length && s.hasNextLine(); i++) {
			String[] movieData = s.nextLine().split(",");
			String m = movieData[5].replace("%", "");
			Movie movie = new Movie(movieData[0], Integer.parseInt(movieData[1]), Integer.parseInt(movieData[2]),
					(movieData[3]), Double.valueOf(movieData[4]), Double.valueOf(m));
			this.movieList[i] = movie;
		}
		s.close();
	}

	/**
	 * @return quantidade de títulos de filmes distintos presentes no videoclube
	 */
	public int getNumberOfMovies() {

		return this.movieList.length;
	}

	/**
	 * @return quantidade de títulos de filmes disponiveis presentes no videoclube
	 */
	public int numberAvailableMovies() {
		int moviesAvailable = 0;
		for (int i = 0; i < this.movieList.length; i++) {
			if (this.movieList[i].getQuantity() - this.movieList[i].numberOfRentals() > 0) {
				moviesAvailable++;
			}
		}
		return moviesAvailable;
	}

	/**
	 * @return o total de dinheiro feito dos alugueres
	 */
	public double getTotalRevenue() {
		return this.totalRevenue;
	}

	/**
	 * @return o total de dinheiro feito dos alugueres depois de pagar impostos
	 */
	public double getTotalProfit() {
		return this.totalProfit;
	}

	/**
	 * Este método retorna uma String com todos os filmes que estrearam num
	 * determinado ano
	 * 
	 * @param year
	 * @return String com todos os filmes que estrearam num determinado ano
	 */
	public String filterByYear(int year) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.movieList.length; i++) {
			if (this.movieList[i].getYear() == year) {
				sb.append("Title:" + movieList[i].getTitle() + ",Price:$" + movieList[i].getPrice()
						+ System.lineSeparator());
			}
		}
		return sb.toString();
	}

	/**
	 * Este método retorna uma String, dividida em linhas, onde cada linha contém o
	 * título de filmes que têm preço menor que price
	 * 
	 * @param price preço máximos desta list de filmes
	 * @return String, dividida em linhas, onde cada linha contém o título de filmes
	 *         que têm preço menor que price
	 */
	public String filterByPrice(double price) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.movieList.length; i++) {
			if (this.movieList[i].getPrice() <= price) {
				sb.append("Title:" + movieList[i].getTitle() + ",Price:$" + movieList[i].getPrice()
						+ System.lineSeparator());
			}
		}
		return sb.toString();
	}

	/**
	 * Este método retorna uma String, dividida em linhas, onde cada linha contém o
	 * título de filmes que está disponivel
	 * 
	 * @return tring, dividida em linhas, onde cada linha contém o título de filmes
	 *         que está disponivel
	 */
	public String filterAvailableMovies() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.movieList.length; i++) {
			if (this.movieList[i].getQuantity() - this.movieList[i].numberOfRentals() > 0) {
				sb.append("Title:" + movieList[i].getTitle() + ",Price:$" + movieList[i].getPrice()
						+ System.lineSeparator());
			}
		}
		return sb.toString();
	}

	/**
	 * Este método realiza as transações que estão descritas no ficheiro dado como
	 * parametro
	 * 
	 * @param rentalsFileName
	 * 
	 * @return String que reflete o estado dos alugueres ativos no início do dia
	 *         seguinte
	 */
	public String activityLog(String rentalsFileName) throws FileNotFoundException {
		StringBuilder sb = new StringBuilder();
		Scanner sc = new Scanner(new File(rentalsFileName));
		sc.nextLine();	//Salta a linha do cabeçalho para começar a ler a partir da segunda linha
		while (sc.hasNextLine()) {
			
			String[] rentalData = sc.nextLine().split(",");
			String TransactionType = rentalData[0];
			int user = Integer.parseInt(rentalData[1]);
			String movieID = rentalData[2];
			
			if (isMovie(movieID)) {  //verifica se esta String corresponde a um filme do videoclub
				
				if (TransactionType.equals("rent")) { //verifica se a ação é "alugar"
					
					if (stringToMovie(movieID).isAvailable(stringToMovie(movieID))) { //Verifica se o filme está disponivel
						stringToMovie(movieID).rentMovie(user);
						this.totalRevenue += stringToMovie(movieID).getPrice();
						this.totalProfit += (stringToMovie(movieID).getPrice()
								- (stringToMovie(movieID).getPrice() * stringToMovie(movieID).getTax()));
						sb.append("Rental successful: client " + user + " rented " + stringToMovie(movieID).getTitle()
								+ " for $" + stringToMovie(movieID).getPrice() + System.lineSeparator());
						sb.append("Total: $" + String.format(Locale.US, "%.2f", stringToMovie(movieID).getPrice())
								+ " [$"
								+ String.format(Locale.US, "%.2f", (stringToMovie(movieID).getPrice()
										- (stringToMovie(movieID).getPrice() * stringToMovie(movieID).getTax())))
								+ "]" + System.lineSeparator());
						
					} else {	
						sb.append("Movie currently not available: client " + user + " asked for "
								+ stringToMovie(movieID).getTitle() + System.lineSeparator());
					}
					
				} else {	//Se nao for "alugar" é "devolver"

					if (returnDays(user, stringToMovie(movieID)) < 0) { //verifica se a devolução está atrasada
						
						this.totalRevenue += 2 * returnDays(user, stringToMovie(movieID));
						this.totalProfit += 2 * returnDays(user, stringToMovie(movieID))
								- (2 * (stringToMovie(movieID).getTax()));
						stringToMovie(movieID).returnMovie(user);
						sb.append("Movie returned with - " + returnDays(user, stringToMovie(movieID))
								+ " days of delay: client " + user + " returned " + movieID + System.lineSeparator());
						sb.append("Total: $"
								+ String.format(Locale.US, "%.2f", 2.0 * returnDays(user, stringToMovie(movieID)))
								+ " [$"
								+ String.format(Locale.US, "%.2f",
										2.0 * returnDays(user, stringToMovie(movieID))
												- (2.0 * (stringToMovie(movieID).getTax())))
								+ "]" + System.lineSeparator());
						
					} else {
						stringToMovie(movieID).returnMovie(user);
						sb.append("Movie returned: client " + user + " returned " + stringToMovie(movieID).getTitle()
								+ System.lineSeparator());
						sb.append("Total: $0.00 [$0.00]" + System.lineSeparator());
					}
				}
				
			} else { //Diz que não existe este filme
				sb.append("Movie not found: client " + user + " asked for " + movieID + System.lineSeparator());
			}
		}
		return sb.toString();
	}

	/**
	 * Este método retorna o valor de verdade 'true' caso o filme esteja disponivel,
	 * e o valor de verdade 'false' caso contrário
	 * 
	 * @param s titulo do filme
	 * 
	 * @return true se estiver disponivel, false caso contrário
	 */
	private boolean isMovie(String s) {
		for (int i = 0; i < this.movieList.length; i++) {
			if (s.equals(this.movieList[i].getTitle()) || s.equals(this.movieList[i].getCode())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Este método retorna o filme que corresponde a String dada
	 * 
	 * @param movie titulo do filme
	 * 
	 * @return true se estiver disponivel, false caso contrário
	 */
	private Movie stringToMovie(String movie) {
		for (int i = 0; i < this.movieList.length; i++) {
			if (this.movieList[i].getTitle().equals(movie) || this.movieList[i].getCode().equals(movie)) {
				return this.movieList[i];
			}
		}
		return null;
	}

	/**
	 * Este método retorna os dias de atraso do retorno do filme
	 * 
	 * @param movie titulo do filme
	 * @param user  número de cliente da pessoa que alugou
	 * 
	 * @return dias de atraso do retorno do filme
	 */
	public int returnDays(int user, Movie movie) {
		for (int i = 0; i < movie.numberOfRentals(); i++) {
			if (movie.getRentals()[i][0] == user) {
				return movie.getRentals()[i][1] * -1;
			}
		}
		return 0;
	}

	/**
	 * Este método Atualiza o stock para refletir a atividade do dia. Grava num
	 * ficheiro CSV identificado por fileName o estado do stock.
	 * 
	 * @param fileName String identificador do ficheiro
	 * 
	 */
	public void updateStock(String fileName) throws FileNotFoundException { // passei no testAvailiable movies mas da
																			// erro nos expected
		PrintWriter writer = new PrintWriter(new File(fileName));
		writer.write("Title,Year,Quantity,Rentals,Price,Tax" + System.lineSeparator());
		for (int i = 0; i < movieList.length; i++) {
			writer.write(movieList[i].getTitle() + "," + movieList[i].getYear() + "," + movieList[i].getQuantity() + ","
					+ movieList[i].rentalsToString() + "," + movieList[i].getPrice() + "," + movieList[i].getTax()
					+ System.lineSeparator());
		}
		writer.close();
	}

}
