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

@WebFilter(urlPatterns = {"/principal/*"})/*Interceptas todas as requisiçoes que vierem do projeto ou mapeamento*/
public class FilterAutenticacao implements Filter {
	
	
	private static Connection connection; /*Declarando a conexão, e no método init desta classe vamos chamar esse connection*/

    public FilterAutenticacao() {
    }

    /*Encerra os processo quando o servidor é parado*/
    /*Mataria os processo de conexão com banco*/
	public void destroy() {
		
		try {
			connection.close();
		} catch (SQLException e) {/*Assim que demos o close exigiu que seja feito tratamento de exceções por isso o try/catch*/
			e.printStackTrace();
		}
	}

	/*Intercepta as requisicoes e a as respostas no sistema*/
	/*Tudo que fizer no sistema vai fazer por aqui*/
	/*Validação de autenticao*/
	/*Dar commit e rolback de transaçoes do banco*/
	/*Validar e fazer redirecionamento de paginas*/
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
	     
		/*E aqui dentro do do Filter precisamos envolver todo esse nosso bloco de código dentro de um try/catch
	      *  e dar um commit e dar um rollback caso aconteça alguma exceção*/
		
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession();
			
			String usuarioLogado = (String) session.getAttribute("usuario");
			
			String urlParaAutenticar = req.getServletPath();/*Url que está sendo acessada*/
			
			/*Validar se está logado senão redireciona para a tela de login*/
			if (usuarioLogado == null &&
					!urlParaAutenticar.equalsIgnoreCase("/principal/ServletLogin")) {/*Não está logado*/
				
				RequestDispatcher redireciona = request.getRequestDispatcher("/index.jsp?url=" + urlParaAutenticar);
				request.setAttribute("msg", "Por favor realize o login!");
				redireciona.forward(request, response);
				return; /*Para a execução e redireciona para o login*/
						
			}else {
				chain.doFilter(request, response);
			}
			
			connection.commit(); /*último passo se deu tudo certo então commita as alterações no banco de dados*/
			
		}catch (Exception e) {
			e.printStackTrace();/*qualquer exceção que acontecer aqui dentro*/
			
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", "Informe o login e senha corretamente!");
			redirecionar.forward(request, response);
			
			
			try {
				connection.rollback();/*e se der uma exceção pego a minha conexão e dou um rollback*/
			} catch (SQLException e1) {
				e1.printStackTrace();/*Aqui é a exceção do rollback*/
			}
		}
	}

	/*Inicia os processo ou recursos quando o servidor sobre o projeto*/
	// inicar a conexão com o banco quando inicia o projeto
	public void init(FilterConfig fConfig) throws ServletException {
		
		connection = SingleConnectionBanco.getConnection();/*Retornando a conexão da nossa classe de conexão*/
		
	}
}
