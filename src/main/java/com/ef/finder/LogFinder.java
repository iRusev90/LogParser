package com.ef.finder;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ef.db.GenericGateway;

@Repository
public class LogFinder {
	@Autowired
	private GenericGateway gateway;
	
	public List<String> findLogsForTimeframeWithEntriesOverThreshhold(LocalDateTime startDate, LocalDateTime endDate,
			Integer threshhold) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT GROUPED_LOGS.ip FROM ( SELECT COUNT(id) as logs_count, log.ip FROM public.logs log ")
					.append("WHERE log.timestamp BETWEEN :startDate AND :endDate GROUP BY log.ip) as GROUPED_LOGS ")
					.append("WHERE GROUPED_LOGS.logs_count >= :threshold");

		Query<String> query = gateway.createSqlQuery(queryBuilder.toString());
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("threshold", threshhold);

		return query.getResultList();
	}
}
