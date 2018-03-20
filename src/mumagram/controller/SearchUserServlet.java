package mumagram.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import mumagram.model.User;
import mumagram.repository.UserRepository;
import mumagram.service.Service;

@WebServlet("/SearchUserServlet")
public class SearchUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Service service;
	private UserRepository userRepository;

    public SearchUserServlet() {
        super();
        service = new Service();
        userRepository = new UserRepository();
    }

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if(service.validateSession(session)) {
			String query = request.getParameter("query");
			List<User> users = userRepository.search(query);

			ObjectMapper mapper = new ObjectMapper();
			String resultJson = mapper.writeValueAsString(users);

			response.setContentType("application/json");

			PrintWriter out = response.getWriter();
			out.write(resultJson);
			out.flush();
		} else {
			response.sendRedirect("/mumagram/login?error=Please login your username and password");
		}		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}