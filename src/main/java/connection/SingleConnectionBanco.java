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
				Class.forName("org.postgresql.Driver");/*Carrega o driver de conex�o do banco*/
				connection = DriverManager.getConnection(banco, user, senha);
				connection.setAutoCommit(false);/*para n�o efetuar altera��es no banco sem nosso comando*/
				
				/*E quando que vai ser invocado esse conectar? 
				 *  eu quero que ele seja chamado sempre que eu invocar 
				 * a classe singleconnection*/
				
				/*
				 *Ent�o sempre que ela for invocada
				 *eu tenho que chamar esse conectar se nao tiver conexao vai criar e se tiver
				 *vai retornar a mesma, ent�o temos que criar um construtor e dentro dele 
				 *chamaremos o m�todo conectar na linha 23
				 *
				 *depois na linha 19 chamamos a classe de forma direta que tamb�m � bem comum
				 *A� criamos um static e chamamos o conectar
				 *Ent�o d equalquer clugar que chamar essa clase eu sempre vou obter uma conecx�o
				 *
				 *ent�o chamar a classe direto ou ent�o instanciar um objeto, s�o duas formas
				 * de voc� manter a garantia da conex�o
				 * 
				 * e da� preciso de um m�todo para retornar essa conex�o existente
				 * 
				 * ent�o criei na linha 23 o m�todo getConnection(). Pronto
				 * 
				 * Agora para fazermos o teste se ela vai conectar no banco l� no banco a gente 
				 * tem que chamar  l� no Filter e l� no filter ele � executado na hora que o projeto estiver subindo.
				 * 
				 * no m�todo init()
				*/
			}
			
		} catch (Exception e) {
			e.printStackTrace();/*Mostar qualquer erro no momento de conectar*/
		}
		
	}
	
	
}
