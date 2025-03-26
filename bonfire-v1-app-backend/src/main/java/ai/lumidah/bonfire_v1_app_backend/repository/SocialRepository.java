package ai.lumidah.bonfire_v1_app_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ai.lumidah.bonfire_v1_app_backend.model.Follow;

@Repository
public interface SocialRepository extends JpaRepository<Follow, Long> {

    void deleteByUserIdAndFollowingId(Long id, Long followingId);
    boolean existsByUserIdAndFollowingId(Long id, Long followingId);
    int countByUserId(Long userId);
    int countByFollowingId(Long userId);
    List<Follow> findByUserId(Long userId);


}
