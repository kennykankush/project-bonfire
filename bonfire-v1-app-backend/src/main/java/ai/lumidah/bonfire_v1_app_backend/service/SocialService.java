package ai.lumidah.bonfire_v1_app_backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ai.lumidah.bonfire_v1_app_backend.dto.FollowRequest;
import ai.lumidah.bonfire_v1_app_backend.model.Follow;
import ai.lumidah.bonfire_v1_app_backend.repository.SocialRepository;

@Service
public class SocialService { //haha socialservice get it 

    @Autowired
    private SocialRepository socialRepository;

    public Follow captureFollow(Long userId, Long followingId){
        Follow follow = new Follow();
        follow.setUserId(userId);
        follow.setFollowingId(followingId);
        follow.setStatus("ACCEPTED");
        return socialRepository.save(follow);
    }

    @Transactional
    public void retractFollow(Long userId, Long followingId){
        socialRepository.deleteByUserIdAndFollowingId(userId, followingId);
    }

    public boolean isFollowing(Long userId, Long followingId){
        return socialRepository.existsByUserIdAndFollowingId(userId, followingId);
    }

    public int getFollowing(Long userId){
        return socialRepository.countByUserId(userId);
    }

    public int getFollowers(Long userId){
        return socialRepository.countByFollowingId(userId);
    }

    public List<Long> getFollowingList(Long userId) {
        return socialRepository.findByUserId(userId).stream().map(Follow::getFollowingId).collect(Collectors.toList());
    }

    

    

}
