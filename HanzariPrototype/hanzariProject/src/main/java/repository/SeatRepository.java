/*
 * package repository;
 * 
 * import java.sql.ResultSet; import java.sql.SQLException;
 * 
 * import javax.sql.DataSource;
 * 
 * import org.apache.juli.logging.Log; import
 * org.apache.juli.logging.LogFactory; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.dao.DataAccessException; import
 * org.springframework.jdbc.core.RowMapper; import
 * org.springframework.jdbc.core.namedparam.MapSqlParameterSource; import
 * org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate; import
 * org.springframework.jdbc.support.GeneratedKeyHolder;
 * 
 * import model.Seat;
 * 
 * public class SeatRepository { private static final Log logger =
 * LogFactory.getLog(SeatRepository.class); private NamedParameterJdbcTemplate
 * jdbcTemplate;
 * 
 * @Autowired public void setDataSource(DataSource dataSource) {
 * this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource); }
 * 
 * public Seat getSeat(int seat_id) { MapSqlParameterSource params = new
 * MapSqlParameterSource(); params.addValue("seat_id", seat_id); String
 * selectSQL = "SELECT * FROM seats WHERE seat_id= :seat_id"; Seat result; try {
 * result = this.jdbcTemplate.queryForObject(selectSQL, params, new
 * SeatMapper()); } catch (DataAccessException e) {
 * logger.error("Faild to get Seat", e); return null; } return result; }
 * 
 * class SeatMapper implements RowMapper<Seat> { // interface method public Seat
 * mapRow(ResultSet rs, int rowNum) throws SQLException { Seat seat = new
 * Seat();
 * 
 * seat.setBuilding_id(rs.getString("building_id"));
 * seat.setFloor(rs.getString("floor"));
 * seat.set_group(rs.getBoolean("is_group"));
 * seat.setGroup_id(rs.getString("group_id")); seat.setX(rs.getDouble("x"));
 * seat.setY(rs.getDouble("y")); seat.setFigure_id(rs.getString("figure_id"));
 * return seat; } } }
 */