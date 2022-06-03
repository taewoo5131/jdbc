package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

/**
 * jdbc driverManager 사용
 */
@Slf4j
public class MemberRepositoryV0{

    public Member save(Member member) throws SQLException{
        String sql = "insert into member(member_id , money) values(?,?)";
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("db error");
            throw e;
        } finally {
            this.close(connection, pstmt, null);
        }
    }

    public Member findById(String memberId) throws SQLException{
        String sql = "select * from member where member_id = ?";
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = DBConnectionUtil.getConnection();
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1,memberId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Member member = new Member(rs.getString("member_id"),rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found MemberId=" + memberId);
            }

        } catch (SQLException e) {
            log.error("db error");
            throw e;
        } finally {
            this.close(connection , pstmt , rs);
        }
    }

    public void update(String memberId, int money) throws SQLException{
        String sql = "update member set money = ? where member_id = ?";
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DBConnectionUtil.getConnection();
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int i = pstmt.executeUpdate();
            log.info("update result size = {} ", i);
        } catch (SQLException e) {
            log.error("db error");
            throw e;
        } finally {
            this.close(connection, pstmt, null);
        }
    }

    public void delete(String memberId) throws SQLException{
        String sql = "delete from member where member_id = ?";
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DBConnectionUtil.getConnection();
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error");
            throw e;
        } finally {
            this.close(connection, pstmt, null);
        }
    }


    private void close(Connection connection, Statement stmt, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                log.error("error");
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.error("error");
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("error");
            }
        }
    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
