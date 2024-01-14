package servlets;

import java.io.IOException;

import dao.DAOLoginRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ModelLogin;


/*O chamando Controller são as servlets ou ServletLoginController*/
@WebServlet(urlPatterns = {"/principal/ServletLogin", "/ServletLogin"}) /*Mapeamento de URL que vem da tela*/
public class ServletLogin extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	
	private DAOLoginRepository daoLoginRepository = new DAOLoginRepository();

  
    public ServletLogin() {
    }


    /*Recebe os dados pela url em parametros*/
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String acao = request.getParameter("acao");/*que tá vindo por parametro da requesição*/
		
		if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("logout")) {
			request.getSession().invalidate();/* invalida a sessão, apagando todos os atributos que foram colocados na sessão, 
			no filter irá validar se está logado e não encontrará a sessão com dados e então será redirecionado para o login novamente!
			*/
			RequestDispatcher redirecionar = request.getRequestDispatcher("index.jsp");
			redirecionar.forward(request, response);
		}else {
			doPost(request, response);
		}
	}

	
	/*recebe os dados enviados por um formulario*/
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		String url = request.getParameter("url");
		
		try {
		
				if (login != null && !login.isEmpty() && senha != null && !senha.isEmpty()) {/*Se vier com os campos preenchidos*/
					
					ModelLogin modelLogin = new ModelLogin();
					modelLogin.setLogin(login);
					modelLogin.setSenha(senha);
					
					if (daoLoginRepository.validarAutenticacao(modelLogin)) { /*Simulando login*/
						
						/*Coloca o user na sessao e mais pra frente iremos
		                buscar para validar uma autenticação de segunrança*/
						request.getSession().setAttribute("usuario", modelLogin.getLogin());
						
						if (url == null || url.equals("null")) {
							url = "principal/principal.jsp";
						}
						
						RequestDispatcher redirecionar = request.getRequestDispatcher(url);
						redirecionar.forward(request, response);
						
					}else {
						RequestDispatcher redirecionar = request.getRequestDispatcher("/index.jsp");
						request.setAttribute("msg", "Informe o login e senha válidos!");
						redirecionar.forward(request, response);
					}
					
				}else {
					RequestDispatcher redirecionar = request.getRequestDispatcher("index.jsp");
					request.setAttribute("msg", "Informe o login e senha válidos!");
					redirecionar.forward(request, response);
				}
				
		}catch (Exception e) {
			e.printStackTrace();/*para imprimir no console caso aconteça exceção*/
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}
	}
}
