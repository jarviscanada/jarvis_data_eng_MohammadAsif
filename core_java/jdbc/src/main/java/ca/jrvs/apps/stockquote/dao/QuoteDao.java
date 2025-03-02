package ca.jrvs.apps.stockquote.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuoteDao implements CrudDao<Quote, String> {

    private final Connection connection;
    private final Logger logger = LoggerFactory.getLogger(QuoteDao.class);

    public QuoteDao(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.connection = connection;
    }

    private final String SELECT_ALL = "SELECT * FROM quote";
    private final String SELECT_BY_ID = "SELECT * FROM quote WHERE symbol = ?";
    private final String INSERT = "INSERT INTO quote (symbol, open, high, low, " +
            "price, volume, latest_trading_day, previous_close, change, change_percent, " +
            "timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE = "UPDATE quote SET open = ?, high = ?, " +
            "low = ?, price = ?, volume = ?, latest_trading_day = ?, previous_close = ?, " +
            "change = ?, change_percent = ?, timestamp = ? WHERE symbol = ?";
    private final String DELETE = "DELETE FROM quote WHERE symbol = ?";
    private final String DELETE_ALL = "DELETE FROM quote";

    @Override
    public Quote save(Quote entity) throws IllegalArgumentException {
        if (this.findById(entity.getTicker()).isEmpty()) {
            try (PreparedStatement ps = this.connection.prepareStatement(INSERT);) {
                ps.setString(1, entity.getTicker());
                ps.setDouble(2, entity.getOpen());
                ps.setDouble(3, entity.getHigh());
                ps.setDouble(4, entity.getLow());
                ps.setDouble(5, entity.getPrice());
                ps.setDouble(6, entity.getVolume());
                ps.setDate(7, entity.getLatestTradingDay());
                ps.setDouble(8, entity.getPreviousClose());
                ps.setDouble(9, entity.getChange());
                ps.setString(10, entity.getChangePercent());
                ps.setTimestamp(11, entity.getTimestamp());
                ps.execute();
                logger.info("Executed INSERT statement on ticker: {}", entity.getTicker());
                return this.findById(entity.getTicker()).get();
            } catch (SQLException e) {
                logger.error("UPDATE Statement failure for provided entity", e);
            }
        } else {
            try (PreparedStatement ps = this.connection.prepareStatement(UPDATE);) {
                ps.setDouble(1, entity.getOpen());
                ps.setDouble(2, entity.getHigh());
                ps.setDouble(3, entity.getLow());
                ps.setDouble(4, entity.getPrice());
                ps.setDouble(5, entity.getVolume());
                ps.setDate(6, entity.getLatestTradingDay());
                ps.setDouble(7, entity.getPreviousClose());
                ps.setDouble(8, entity.getChange());
                ps.setString(9, entity.getChangePercent());
                ps.setTimestamp(10, entity.getTimestamp());
                ps.setString(11, entity.getTicker());
                ps.execute();
                logger.info("Executed UPDATE statement on ticker: {}", entity.getTicker());
                return this.findById(entity.getTicker()).get();
            } catch (SQLException e) {
                logger.error("UPDATE Statement failure for provided entity", e);
            }
        }
        return null;
    }

    @Override
    public Optional<Quote> findById(String s) throws IllegalArgumentException {
        Quote quote = new Quote();
        try (PreparedStatement ps = this.connection.prepareStatement(SELECT_BY_ID);) {
            ps.setString(1, s);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                quote.setTicker(rs.getString("symbol"));
                quote.setOpen(rs.getDouble("open"));
                quote.setHigh(rs.getDouble("high"));
                quote.setLow(rs.getDouble("low"));
                quote.setPrice(rs.getDouble("price"));
                quote.setVolume(rs.getInt("volume"));
                quote.setLatestTradingDay(rs.getDate("latest_trading_day"));
                quote.setPreviousClose(rs.getDouble("previous_close"));
                quote.setChange(rs.getDouble("change"));
                quote.setChangePercent(rs.getString("change_percent"));
                quote.setTimestamp(rs.getTimestamp("timestamp"));
            }
            if (quote.getTicker() == null) {
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Could not retrieve quote from ticker {}", s, e);
        } catch (IllegalArgumentException e) {
            logger.error("Please provide a valid ticker symbol.", e);
        }
        return Optional.of(quote);
    }

    @Override
    public Iterable<Quote> findAll() {
        List<Quote> quotes = new ArrayList<>();
        try (Statement s = connection.createStatement();
             ResultSet rs = s.executeQuery(SELECT_ALL)) {
            while(rs.next()) {
                Quote q = new Quote();
                q.setTicker(rs.getString("symbol"));
                q.setOpen(rs.getDouble("open"));
                q.setHigh(rs.getDouble("high"));
                q.setLow(rs.getDouble("low"));
                q.setPrice(rs.getDouble("price"));
                q.setVolume(rs.getInt("volume"));
                q.setLatestTradingDay(rs.getDate("latest_trading_day"));
                q.setPreviousClose(rs.getDouble("previous_close"));
                q.setChange(rs.getDouble("change"));
                q.setChangePercent(rs.getString("change_percent"));
                q.setTimestamp(rs.getTimestamp("timestamp"));
                quotes.add(q);
            }
        } catch (SQLException e) {
            logger.error("Could not complete request: Find All Entities");
        }
        return quotes;
    }

    @Override
    public void deleteById(String s) throws IllegalArgumentException {
        if (this.findById(s).isEmpty()) {
            throw new IllegalArgumentException("Could not find entity with id: " + s);
        }
        try (PreparedStatement ps = this.connection.prepareStatement(DELETE)) {
            ps.setString(1, s);
            ps.execute();
        } catch (SQLException e) {
            logger.error("Could not delete position with id: {}", s, e);
        }
    }

    @Override
    public void deleteAll() {
        try (PreparedStatement ps = this.connection.prepareStatement(DELETE_ALL)) {
            ps.execute();
            logger.info("DELETE ALL statement executed.");
        } catch (SQLException e) {
            logger.error("Could not delete all entities", e);
        }
    }

    //implement all inherited methods
    //you are not limited to methods defined in CrudDao
}
