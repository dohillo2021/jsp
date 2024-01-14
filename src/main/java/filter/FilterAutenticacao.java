package filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import connection.SingleConnectionBanco;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = {"/principal/*"})/*Interceptas todas as requisi�oes que vierem do projeto ou mapeamento*/
public class FilterAutenticacao implements Filter {
	
	
	private static Connection connection; /*Declarando a conex�o, e no m�todo init desta classe vamos chamar esse connection*/

    public FilterAutenticacao() {
    }

    /*Encerra os processo quando o servidor � parado*/
    /*Mataria os processo de conex�o com banco*/
	public void destroy() {
		
		try {
			connection.close();
		} catch (SQLException e) {/*Assim que demos o close exigiu que seja feito tratamento de exce��es por isso o try/catch*/
			e.printStackTrace();
		}
	}

	/*Intercepta as requisicoes e a as respostas no sistema*/
	/*Tudo que fizer no sistema vai fazer por aqui*/
	/*Valida��o de autenticao*/
	/*Dar commit e rolback de transa�oes do banco*/
	/*Validar e fazer redirecionamento de paginas*/
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
	     
		/*E aqui dentro do do Filter precisamos envolver todo esse nosso bloco de c�digo dentro de um try/catch
	      *  e dar um commit e dar um rollback caso aconte�a alguma exce��o*/
		
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession();
			
			String usuarioLogado = (String) session.getAttribute("usuario");
			
			String urlParaAutenticar = req.getServletPath();/*Url que est� sendo acessada*/
			
			/*Validar se est� logado sen�o redireciona para a tela de login*/
			if (usuarioLogado == null &&
					!urlParaAutenticar.equalsIgnoreCase("/principal/ServletLogin")) {/*N�o est� logado*/
				
				RequestDispatcher redireciona = request.getRequestDispatcher("/index.jsp?url=" + urlParaAutenticar);
				request.setAttribute("msg", "Por favor realize o login!");
				redireciona.forward(request, response);
				return; /*Para a execu��o e redireciona para o login*/
						
			}else {
				chain.doFilter(request, response);
			}
			
			connection.commit(); /*�ltimo passo se deu tudo certo ent�o commita as altera��es no banco de dados*/
			
		}catch (Exception e) {
			e.printStackTrace();/*qualquer exce��o que acontecer aqui dentro*/
			
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", "Informe o login e senha corretamente!");
			redirecionar.forward(request, response);
			
			
			try {
				connection.rollback();/*e se der uma exce��o pego a minha conex�o e dou um rollback*/
			} catch (SQLException e1) {
				e1.printStackTrace();/*Aqui � a exce��o do rollback*/
			}
		}
	}

	/*Inicia os processo ou recursos quando o servidor sobre o projeto*/
	// inicar a conex�o com o banco quando inicia o projeto
	public void init(FilterConfig fConfig) throws ServletException {
		
		connection = SingleConnectionBanco.getConnection();/*Retornando a conex�o da nossa classe de conex�o*/
		
	}
}
