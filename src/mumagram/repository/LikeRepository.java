package mumagram.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mumagram.model.Like;
import mumagram.model.Post;
import mumagram.model.User;
import mumagram.util.DbUtil;

public class LikeRepository {
	private UserRepository userRepository;
	private PostRepository postRepository;

	public LikeRepository() {
		userRepository = new UserRepository();
		postRepository = new PostRepository();
	}

	/*
	 * Receives an int value as parameter and read like data from database. Then converting the data to the model object
	 */
	public Like findOneById(int id) {
		Like like = null;
		try(Connection connection = DbUtil.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(
				"SELECT id, user_id, post_id, created_date, updated_date FROM `like` WHERE id = ?"
			);
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();

			if(rs.next()) {
				User user = userRepository.findOneById(rs.getInt("user_id"));
				Post post = postRepository.findOneById(rs.getInt("post_id"));
				like = new Like();
				like.setId(rs.getInt("id"));
				like.setUser(user);
				like.setPost(post);
				like.setCreatedDate(rs.getDate("created_date").toLocalDate());
				if(rs.getDate("updated_date") != null) {
					like.setUpdatedDate(rs.getDate("updated_date").toLocalDate());
				}
			}

			rs.close();
			preparedStatement.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return like;
	}
	
	/*
	 * Receives Post and User object as parameter and read like data from database. Then converting the data to the model object
	 * If Like data found in database it will return Like object.
	 * If nothing found it will return null.
	 */
	public Like isLiked(Post post, User user) {
		Like result = null;
		try(Connection connection = DbUtil.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(
				"SELECT id, user_id, post_id, created_date, updated_date FROM `like` WHERE user_id = ? AND post_id = ?"
			);
			preparedStatement.setInt(1, user.getId());
			preparedStatement.setInt(2, post.getId());
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()) {
				User likedUser = userRepository.findOneById(rs.getInt("user_id"));
				Post likedPost = postRepository.findOneById(rs.getInt("post_id"));

				result = new Like();
				result.setId(rs.getInt("id"));
				result.setUser(likedUser);
				result.setPost(likedPost);
				result.setCreatedDate(rs.getDate("created_date").toLocalDate());
				if(rs.getDate("updated_date") != null) {
					result.setUpdatedDate(rs.getDate("updated_date").toLocalDate());
				}
			}

			rs.close();
			preparedStatement.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/*
	 * Receives Like object as parameter and insert Like data to the database table.
	 */
	public boolean save(Like like) {
		boolean result = false;
		try(Connection connection = DbUtil.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(
				"INSERT INTO `like`(user_id, post_id, created_date)"
				+ "VALUES"
				+ "(?, ?, ?)"
			);
			preparedStatement.setInt(1, like.getUser().getId());
			preparedStatement.setInt(2, like.getPost().getId());
			preparedStatement.setDate(3, Date.valueOf(like.getCreatedDate()));
			result = preparedStatement.execute();

			preparedStatement.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/*
	 * Receives Like object as parameter and update Like data in the database table.
	 */
	public boolean update(Like like) {
		boolean result = false;
		try(Connection connection = DbUtil.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(
				"UPDATE `like` SET user_id = ?, post_id = ?, updated_date = ?"
				+ "WHERE id = ?"
			);
			preparedStatement.setInt(1, like.getUser().getId());
			preparedStatement.setInt(2, like.getPost().getId());
			preparedStatement.setDate(3, Date.valueOf(like.getUpdatedDate()));
			preparedStatement.setInt(4, like.getId());
			result = preparedStatement.execute();

			preparedStatement.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/*
	 * Receives Like object as parameter and delet Like data from the database table.
	 */
	public boolean delete(Like like) {
		boolean result = false;
		try(Connection connection = DbUtil.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(
				"DELETE FROM `like` WHERE id = ?"
			);
			preparedStatement.setInt(1, like.getId());
			result = preparedStatement.execute();

			preparedStatement.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}