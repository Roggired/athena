package ru.yofik.athena.messenger.infrastructure.storage.sql.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yofik.athena.messenger.infrastructure.storage.sql.user.entity.UserLastOnlineRecord;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrudUserLastOnlineRecordRepository extends JpaRepository<UserLastOnlineRecord, Long> {
    Optional<UserLastOnlineRecord> findByUserId(long userId);

    @Query("select u from UserLastOnlineRecord u " +
            "where u.userId in (:userIds)")
    List<UserLastOnlineRecord> findAllByUserIds(@Param("userIds") List<Long> userIds);
}
