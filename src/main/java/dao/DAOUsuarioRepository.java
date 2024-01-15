package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.SingleConnectionBanco;
import model.ModelLogin;
	

public class DAOUsuarioRepository {
	
	private Connection connection;

	public DAOUsuarioRepository() {
		connection = SingleConnectionBanco.getConnection();
	}
	
	public ModelLogin gravarUsuario(ModelLogin objeto, Long userLogado) throws Exception {
		
		if(objeto.isNovo()) {/*Grava um novo usuário*/
			
		String sql = "INSERT INTO model_login(login, senha, nome, email, usuario_id) VALUES (?, ?, ?, ?, ?);";
		PreparedStatement preparedSql = connection.prepareStatement(sql);
		
		preparedSql.setString(1, objeto.getLogin());
		preparedSql.setString(2, objeto.getSenha());
		preparedSql.setString(3, objeto.getNome());
		preparedSql.setString(4, objeto.getEmail());
		preparedSql.setLong(5, userLogado);
		
		preparedSql.execute(); /*Para executar a instrução sql*/
		
		connection.commit(); /*Para persistir gravar no banco*/
		
		}else { /*Atualiza*/
			String sql = "UPDATE model_login SET login=?, senha=?, nome=?, email=? WHERE id = "+objeto.getId()+";";
			
			PreparedStatement prepareSql = connection.prepareStatement(sql);
			
			prepareSql.setString(1, objeto.getLogin());
			prepareSql.setString(2, objeto.getSenha());
			prepareSql.setString(3, objeto.getNome());
			prepareSql.setString(4, objeto.getEmail());
			
			prepareSql.executeUpdate();
			
			connection.commit();
			
		}
		
		return this.consultaUsuario(objeto.getLogin(), userLogado);/*retorna o método consultar, retorna a consulta*/
		
		
	}
	
	
	public List<ModelLogin> consultaUsuarioList(Long userLogado) throws Exception {/*retornar uma lista de ModelLogin e recebendo parametro nome*/
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();/*retorno será uma lista de ModelLogin e aqui instanciamos essa lista, para não ter um nullpointer Excpetion*/
		
		String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado;
		
		PreparedStatement statement = connection.prepareStatement(sql); /*Preparei o sql*/
		
		ResultSet resultado = statement.executeQuery();
		
		while (resultado.next()) {/*Enquanto tiver resultado ele vai percorrer as linhas de rsultado do SQL*/
			ModelLogin modelLogin = new ModelLogin();/*E para cada linha precisamos iniciar um novo objeto para podermos setar os dados e depois colocar na lista*/
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
//			modelLogin.setSenha(resultado.getString("senha"));Por questão de segurança não precisa carregar a senha
			
			retorno.add(modelLogin);/*Adicionando na lista*/
		}
		return retorno;
	}
	
	public List<ModelLogin> consultaUsuarioList(String nome, Long userLogado) throws Exception {/*retornar uma lista de ModelLogin e recebendo parametro nome*/
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();/*retorno será uma lista de ModelLogin e aqui instanciamos essa lista, para não ter um nullpointer Excpetion*/
		
		String sql = "select * from model_login where upper(nome) like upper (?) and useradmin is false and usuario_id = ? ";
		
		PreparedStatement statement = connection.prepareStatement(sql); /*Preparei o sql*/
		
		statement.setString(1, "%" + nome + "%");
		statement.setLong(2, userLogado);
		
		ResultSet resultado = statement.executeQuery();
		
		while (resultado.next()) {/*Enquanto tiver resultado ele vai percorrer as linhas de rsultado do SQL*/
			ModelLogin modelLogin = new ModelLogin();/*E para cada linha precisamos iniciar um novo objeto para podermos setar os dados e depois colocar na lista*/
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
//			modelLogin.setSenha(resultado.getString("senha"));Por questão de segurança não precisa carregar a senha
			
			retorno.add(modelLogin);/*Adicionando na lista*/
		}
		return retorno;
	}
	
	/*Quero consultar também pelo login e se for admin is true antes de logar */
	public ModelLogin consultaUsuarioLogado(String login) throws Exception {
		
		ModelLogin modelLogin = new ModelLogin(); /*Intanciando objeto aqui para eu sempre ter ele para retornar*/
		
		String sql = "select * from model_login where upper(login) = upper('"+login+"')";
		
		PreparedStatement statement = connection.prepareStatement(sql); /*Preparei o sql*/
		
		ResultSet resultado = statement.executeQuery(); /*Executei o sql*/
		
		while(resultado.next()) { /*Se tem resultado ele vai entrar logo abaixo e preencher esse objeto e vai retornar.*/
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setUseradmin(resultado.getBoolean("useradmin"));
		}
		
		return modelLogin;
	} 
	
	/*Quero consultar também pelo login e se for admin is false */
	public ModelLogin consultaUsuario(String login) throws Exception {
		
		ModelLogin modelLogin = new ModelLogin(); /*Intanciando objeto aqui para eu sempre ter ele para retornar*/
		
		String sql = "select * from model_login where upper(login) = upper('"+login+"') and useradmin is false";
		
		PreparedStatement statement = connection.prepareStatement(sql); /*Preparei o sql*/
		
		ResultSet resultado = statement.executeQuery(); /*Executei o sql*/
		
		while(resultado.next()) { /*Se tem resultado ele vai entrar logo abaixo e preencher esse objeto e vai retornar.*/
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setUseradmin(resultado.getBoolean("useradmin"));
			
		}
		
		return modelLogin;
	} 
	
	
	/*Quero consultar também se vou consultar quero retornar o que quero retornar? queor retornar os dados do usuário que está no ModelLogin*/
	public ModelLogin consultaUsuario(String login, Long userLogado) throws Exception {
		
		ModelLogin modelLogin = new ModelLogin(); /*Intanciando objeto aqui para eu sempre ter ele para retornar*/
		
		String sql = "select * from model_login where upper(login) = upper('"+login+"') and useradmin is false and usuario_id = " + userLogado;
		
		PreparedStatement statement = connection.prepareStatement(sql); /*Preparei o sql*/
		
		ResultSet resultado = statement.executeQuery(); /*Executei o sql*/
		
		while(resultado.next()) { /*Se tem resultado ele vai entrar logo abaixo e preencher esse objeto e vai retornar.*/
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			
		}
		
		return modelLogin;
	} 
	
	
public ModelLogin consultaUsuarioID(String id, Long userLogado) throws Exception {
		
		ModelLogin modelLogin = new ModelLogin(); /*Intanciando objeto aqui para eu sempre ter ele para retornar*/
		
		String sql = "select * from model_login where id = ? and useradmin is false and usuario_id = ?";
		
		PreparedStatement statement = connection.prepareStatement(sql); /*Preparei o sql*/
		
		statement.setLong(1, Long.parseLong(id));
		statement.setLong(2, userLogado);
		
		ResultSet resultado = statement.executeQuery(); /*Executei o sql*/
		
		while(resultado.next()) { /*Se tem resultado ele vai entrar logo abaixo e preencher esse objeto e vai retornar.*/
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setSenha(resultado.getString("senha"));
			
		}
		
		return modelLogin;
	} 
	
	public boolean validarLogin(String login) throws Exception {
		String sql = "select count(1) > 0 as existe from model_login  where  upper(login) = upper('"+login+"');";/*Temos que concatenar o +login+*/
		
		PreparedStatement statement = connection.prepareStatement(sql); /*Preparei o sql*/
		
		ResultSet resultado = statement.executeQuery(); /*Executei o sql*/
		
		resultado.next();/*Pra ele entrar nos resultados do sql. Tem que dar um next porque ele tem que avançar como se fosse um ponteiro Para ele entrar nos resultados*/
		return resultado.getBoolean("existe");
	}
	
	public void deletarUser(String idUser) throws Exception {
		String sql = "DELETE FROM model_login  WHERE id = ? and useradmin is false;";
		
		PreparedStatement prepareSql = connection.prepareStatement(sql); /*preparando sql*/
		
		prepareSql.setLong(1, Long.parseLong(idUser)); /*Setando o primeiro parametro que é o id e no banco ele é do tipo Long e aí temos que converter*/
		
		prepareSql.executeUpdate();/*Usado para Insert, atualização ou delete*/
		
		connection.commit();/*o commit fica na conexão e não no preparedStatment*/
		
	}
	
}
