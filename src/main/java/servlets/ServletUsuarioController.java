package servlets;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.DAOUsuarioRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ModelLogin;

@WebServlet(urlPatterns = {"/ServletUsuarioController"})
public class ServletUsuarioController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	
	private DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();

    public ServletUsuarioController() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	try {	
		
		 String acao  = request.getParameter("acao");
		 
		 if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("deletar")) {/*delete com jsp sem ajax*/
			 
			 String idUser = request.getParameter("id");
			 
			 daoUsuarioRepository.deletarUser(idUser);
			 
			 List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList();
			 request.setAttribute("modelLogins", modelLogins);
			 
			 request.setAttribute("msg", "Excluido com sucesso!");
			 
			 request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);/*Se for Delete normal sem ajax continua fluxo e redireciona*/
		 }
		 else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("deletarajax")) {/*Se for delete por ajax vai pegar id, vai deletar e vamos setar o parametro de outra forma(atributo de resposta) e não podemos dar o redirecionamento*/
				 
				 String idUser = request.getParameter("id");/*Faz mesma coisa aqui pega usuário*/
				 
				 daoUsuarioRepository.deletarUser(idUser);/*Deleta*/
				 
				 response.getWriter().write("Excluido com sucesso!");/*Quando trabalha com ajax tem que ser dessa forma. Setando o atributo de resposta(response.EscrevernaResposta().escreva("Excluído com sucesso"))*/
				 /*request.setAttribute("msg", "Excluído com sucesso!");Não pode ser dessa forma desse jeito é para ter redirecionamento com Servlet*/
		 }
		 
		 else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarUserAjax")) {/*Se for buscar por usuário faz essa daqui*/
			 
			 String nomeBusca = request.getParameter("nomeBusca");/**/
			 
			List<ModelLogin> dadosJsonUser =  daoUsuarioRepository.consultaUsuarioList(nomeBusca);
			 
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(dadosJsonUser);
			 
			response.getWriter().write(json);
			
			 //daoUsuarioRepository.deletarUser(idUser);/*Deleta*/
			 
			// response.getWriter().write("Excluido com sucesso!");/*Quando trabalha com ajax tem que ser dessa forma. Setando o atributo de resposta(response.EscrevernaResposta().escreva("Excluído com sucesso"))*/
			 /*request.setAttribute("msg", "Excluído com sucesso!");Não pode ser dessa forma desse jeito é para ter redirecionamento com Servlet*/
		 
		 }
		 
		 else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarEditar")) {
			 
			 	String id = request.getParameter("id");
			 
			 	ModelLogin modelLogin = daoUsuarioRepository.consultaUsuarioID(id);
			 
			 	List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList();
			 	request.setAttribute("modelLogins", modelLogins);
			 	
				request.setAttribute("msg", "Usuário em edição");
				request.setAttribute("modolLogin", modelLogin);
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
		 }
		 
		 else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("listarUser")) {
			 
			 List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList();
			 
			 request.setAttribute("msg", "Usuários carregados");
			 request.setAttribute("modelLogins", modelLogins);
			 request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
		 }
		 
		 else {
			 List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList();
			 request.setAttribute("modelLogins", modelLogins);
			 request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);/*Se não for nenhum dos 2 deletes então Fica em outro senão porque se deixar redirecionamento no ajax ele perde a informação da variavel e é volátil*/
		 }
		 
		
		 
		 
		}catch (Exception e) {
			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
		String msg = "Operação realizada com sucesso!";	
		
		String id = request.getParameter("id");
		String nome = request.getParameter("nome");
		String email = request.getParameter("email");
		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		
		ModelLogin modelLogin = new ModelLogin();
		
		modelLogin.setId(id != null && !id.isEmpty() ? Long.parseLong(id) : null);
		modelLogin.setNome(nome);
		modelLogin.setEmail(email);
		modelLogin.setLogin(login);
		modelLogin.setSenha(senha);
		
		
		if (daoUsuarioRepository.validarLogin(modelLogin.getLogin()) && modelLogin.getId() == null) {
			msg = "Já existe usuário com o mesmo login, informe outro login;";
		}else {
			if (modelLogin.isNovo()) {
				msg = "Gravado com sucesso!";
			}else {
				msg= "Atualizado com sucesso!";
			}
			
		    modelLogin = daoUsuarioRepository.gravarUsuario(modelLogin);
		}
		
		List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList();
	 	request.setAttribute("modelLogins", modelLogins);
		
		request.setAttribute("msg", msg);
		request.setAttribute("modolLogin", modelLogin);
		request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
		
		} catch (Exception e) {
			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}
		
	}

}
