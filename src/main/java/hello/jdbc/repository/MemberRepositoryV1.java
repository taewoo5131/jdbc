package hello.jdbc.repository;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;
import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;
/**
 * JDBC - DataSource 사용, JdbcUtils 사용
 */
@Slf4j
public class MemberRepositoryV1 {
    private final DataSource dataSource;
    public MemberRepositoryV1(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    //save()...
    public Member save(Member member) throws SQLException{
        String sql = "insert into member values(?,?)";
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
            log.error("db error", e);
            throw e;
        } finally {
            close(connection,pstmt,null);
        }
    }

    //findById()...
    public Member findById(String memberId) throws SQLException{
        String sql = "select * from member where member_id = ?";
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Member member = new Member(rs.getString("member_id"), rs.getInt("money"));
                return member;
            } else{
                throw new NoSuchElementException("member 없음");
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(connection, pstmt, rs);
        }
    }

    //update()....
    public void update(String memberId, int money) throws SQLException{
        String sql = "update member set money = ? where member_id = ?";
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
        } finally {
            close(connection,pstmt,null);
        }
    }

    //delete()....
    public void delete(String memberId) throws SQLException{
        String sql = "delete member where member_id = ?";
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = getConnection();
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
        } finally {
            close(connection,pstmt,null);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }
    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }
}