package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import connection.SingleConnectionBanco;
import model.ModelLogin;

public class DAOLoginRepository {

	private Connection connection;
	
	public DAOLoginRepository() {
		connection = SingleConnectionBanco.getConnection();/*ao instanciar pega conex�o*/
	}
	
	public boolean validarAutenticacao(ModelLogin modelLogin) throws Exception {/*Demos um trhows para nosso filter mostrar a exce��o*/
		/*
		 * 	M�todo para validar nosso login(boolean sim ou n�o),recebendo o Modellogin o objeto inteiro
		 * 
		 */
		
		
		/*vamos agora preparar o nosso SQL, ent�o montamos o sql e preparamos com prepareStatement*/
		/*Seleciona tudo na tabela modellogin onde o login � igual ao parametro que eu passar e a senha igual ao segundo parametro que eu vou passar*/
		/*preciso agora de um objeto para preparar isso pra mim. ent�o usamos preparedStatement que recebe o sql*/
		String sql = "select * from model_login where upper(login) = upper(?) and upper(senha) = upper(?) ";/*Consultando no banco*/
		
		PreparedStatement statement = connection.prepareStatement(sql); /*
		*
		*Preparando a declara�ao do sql - preciso agora de um objeto para preparar isso pra mim. 
		ent�o usamos preparedStatement que recebe o sql
		*
		*/
		
		/*Setando os parametros*/
		statement.setString(1, modelLogin.getLogin());/*Setando uma string no parametro 1, e vai ser a modelLogin getLogin*/
		statement.setString(2, modelLogin.getSenha());/*Setando uma string no parametro 2, e vai ser a modelLogin getSenha*/
		
		ResultSet resultSet = statement.executeQuery();/*Agora preciso de um objeto de resultado(obs: import do java.sql)*/
		
		if (resultSet.next()) {/*Se tiver usu�rio com login e senha a� sim retorna true*/
			
			return true;/*Autenticado*/
		}
		 return false; /*Se n�o tiver usu�rio com login e senha a� da um retorno false pois n�o est� autenticado*/
	}
}
