package connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class SingleConnectionBanco {
	
	private static String banco = "jdbc:postgresql://localhost:5434/curso-jsp?autoReconnect=true";
	private static String user = "postgres";
	private static String senha = "admin";
	private static Connection connection = null;
	
	
	public static Connection getConnection() {
		return connection;
	}
	
	
	static { /*Chamando essa classe de forma direta*/
		conectar();
	}
	
	public SingleConnectionBanco()  { /*quando tiver uma instancia vai conectar*/
		conectar();
	}
	
	
	
	private static void conectar () {
		
		try {
			
			if (connection == null) {
				Class.forName("org.postgresql.Driver");/*Carrega o driver de conexão do banco*/
				connection = DriverManager.getConnection(banco, user, senha);
				connection.setAutoCommit(false);/*para não efetuar alterações no banco sem nosso comando*/
				
				/*E quando que vai ser invocado esse conectar? 
				 *  eu quero que ele seja chamado sempre que eu invocar 
				 * a classe singleconnection*/
				
				/*
				 *Então sempre que ela for invocada
				 *eu tenho que chamar esse conectar se nao tiver conexao vai criar e se tiver
				 *vai retornar a mesma, então temos que criar um construtor e dentro dele 
				 *chamaremos o método conectar na linha 23
				 *
				 *depois na linha 19 chamamos a classe de forma direta que também é bem comum
				 *Aí criamos um static e chamamos o conectar
				 *Então d equalquer clugar que chamar essa clase eu sempre vou obter uma conecxão
				 *
				 *então chamar a classe direto ou então instanciar um objeto, são duas formas
				 * de você manter a garantia da conexão
				 * 
				 * e daí preciso de um método para retornar essa conexão existente
				 * 
				 * então criei na linha 23 o método getConnection(). Pronto
				 * 
				 * Agora para fazermos o teste se ela vai conectar no banco lá no banco a gente 
				 * tem que chamar  lá no Filter e lá no filter ele é executado na hora que o projeto estiver subindo.
				 * 
				 * no método init()
				*/
			}
			
		} catch (Exception e) {
			e.printStackTrace();/*Mostar qualquer erro no momento de conectar*/
		}
		
	}
	
	
}
