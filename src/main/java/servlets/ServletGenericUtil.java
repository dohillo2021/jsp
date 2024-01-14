package servlets;

import java.io.Serializable;

import dao.DAOUsuarioRepository;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class ServletGenericUtil extends HttpServlet implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();/*3� passo pegar a instancia da DAO para podermos utilizar o m�todo consultaUsuario por login*/
	
	public Long getUserLogado(HttpServletRequest request) throws Exception {/*5� passo ao declarar retorno pediu para dar uma excec�o, ent�o colocamos aqui o throws Exception*/
		
		HttpSession session = request.getSession();/*pegando a sess�o l� dentro da resquest*/
		
		String usuarioLogado = (String) session.getAttribute("usuario");/*pegando o usu�rio logado pelo atributo login da Model*/
		
		return daoUsuarioRepository.consultaUsuarioLogado(usuarioLogado).getId();/*4 passo Ao passar o retorno daoUsuarioRepository, chamando o m�todo consultarUsuario e passando por parametro o usuariologado que foi atribuito acima*/
	}
}
