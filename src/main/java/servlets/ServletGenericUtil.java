package servlets;

import java.io.Serializable;

import dao.DAOUsuarioRepository;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class ServletGenericUtil extends HttpServlet implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();/*3º passo pegar a instancia da DAO para podermos utilizar o método consultaUsuario por login*/
	
	public Long getUserLogado(HttpServletRequest request) throws Exception {/*5º passo ao declarar retorno pediu para dar uma excecão, então colocamos aqui o throws Exception*/
		
		HttpSession session = request.getSession();/*pegando a sessão lá dentro da resquest*/
		
		String usuarioLogado = (String) session.getAttribute("usuario");/*pegando o usuário logado pelo atributo login da Model*/
		
		return daoUsuarioRepository.consultaUsuarioLogado(usuarioLogado).getId();/*4 passo Ao passar o retorno daoUsuarioRepository, chamando o método consultarUsuario e passando por parametro o usuariologado que foi atribuito acima*/
	}
}
