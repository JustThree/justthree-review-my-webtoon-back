package com.java.JustThree.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.java.JustThree.domain.*;
import com.java.JustThree.dto.mypage.*;
import com.java.JustThree.exception.UserNotFoundException;
import com.java.JustThree.repository.*;
import com.java.JustThree.repository.board.BoardImageRepository;
import com.java.JustThree.repository.mypage.*;
import com.java.JustThree.repository.StarRepository;
import com.java.JustThree.service.board.BoardImageService;
import jakarta.transaction.Status;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class MyPageService {
    private final UsersRepository usersRepository;
    private final StarRepository starRepository;
    private final InterestRepository interestRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewReplyRepository reviewReplyRepository;
    private final ReviewHeartRepository reviewHeartRepository;
    private final FollowRepository followRepository;
    private final AmazonS3Client s3Client;
    private final BoardImageRepository boardImageRepository;
    private static String bucketName = "just-three";
    private final BoardImageService boardImageService;


    //////////////////////회원 정보 수정 Update////////////////////
//    @Transactional
//    public boolean updateUser(String nickname, Long usersId) {
//        boolean result = true;
//        try {
//            Users nuser = usersRepository.findById(usersId).get();
//            nuser.setUsersNickname(nickname);
//        } catch (Exception e) {
//            System.out.println("회원정보 업데이트 실패");
//            result = false;
//        }
//        return result;
//    }
    //////////////////////프로필 사진 변경 ////////////////////
    @Transactional
    public boolean updateUser(String nickname, Long usersId, MultipartFile profileUrl) {
        boolean result = true;
        try {
            // 프로필 이미지 업로드 및 저장
            String storedName = boardImageService.uploadFile(profileUrl);
            String accessUrl = boardImageService.getAccessUrl(storedName);
            // 사용자 정보 업데이트
            Users nuser = usersRepository.findById(usersId).get();
            if(nickname!=null){
                nuser.setUsersNickname(nickname);
            }
            nuser.setProfileUrl(accessUrl);
            usersRepository.save(nuser);
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
}

    //////////////////////////팔로워 팔로잉 리스트 ////////////////
    public List<FollowResponse> followlist(Long currentUser, Long usersId, int sortNum) {
        List<Follow> list = switch (sortNum) {
            case 2 -> followRepository.findAllByFollower_UsersId(usersId);//팔로잉 목록
            default -> followRepository.findAllByFollowing_UsersId(usersId);//팔로워 목록
        };
        List<FollowResponse> followList = new ArrayList<>();
        for (Follow follow : list) {
            boolean isFollowing = followRepository.existsByFollowerAndFollowing(usersRepository.findById(currentUser).get(), sortNum == 1 ? follow.getFollower() : follow.getFollowing());
            FollowResponse dto = new FollowResponse(follow, sortNum, isFollowing);
            followList.add(dto);
        }
        return followList;
    }

    ////////////////////평가 웹툰 리스트////////////////////
    public List<RatedWebtoonResponse> ratedWebtoonlist(Long usersId, int sortNum) {
        List<Star> list = switch (sortNum) {
            case 2 -> starRepository.findByUsers_UsersId_OrderByStarValDesc(usersId);
            case 3 -> starRepository.findByUsers_UsersId_OrderByStarVal(usersId);
            case 4 -> starRepository.findByUsers_UsersIdAndStarVal(usersId, 10);
            default -> starRepository.findByUsers_UsersId(usersId); // 기본순
        };
        List<RatedWebtoonResponse> ratedWebtoonList = new ArrayList<>();
        for (Star star : list) {
            RatedWebtoonResponse dto = new RatedWebtoonResponse(star.getWebtoon(), star.getStarVal());
            ratedWebtoonList.add(dto);
        }
        return ratedWebtoonList;
    }

    //////////////////////팔로우 기능  //////////////////////
    public void toggleFollow(Long followerId, Long followingId) {
        Users follower = usersRepository.findById(followerId)
                .orElseThrow(() -> new UserNotFoundException("Follower not found with id: " + followerId));
        Users following = usersRepository.findById(followingId)
                .orElseThrow(() -> new UserNotFoundException("Following user not found with id: " + followingId));
        // 팔로우 상태 확인 후 팔로우중이면 언팔로우 언팔로우중이면 팔로우
        boolean isFollowing = followRepository.existsByFollowerAndFollowing(follower, following);
        if (isFollowing) {
            unfollowUser(follower, following);
        } else {
            followUser(follower, following);
        }
    }
    private void followUser(Users follower, Users following) {
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        followRepository.save(follow);
    }
    private void unfollowUser(Users follower, Users following) {
        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new RuntimeException("Follow not found"));
        followRepository.delete(follow);
    }


    //////////////////////관심 웹툰 리스트////////////////////

    public List<InterestedWebtoonResponse> interestedWebtoonlist(Long usersId) {
        List<Interest> list = interestRepository.findByUsers_UsersId(usersId);
        List<InterestedWebtoonResponse> interestedWebtoonList = new ArrayList<>();

        for (Interest interest : list) {
            InterestedWebtoonResponse dto = new InterestedWebtoonResponse(interest.getWebtoon());
            interestedWebtoonList.add(dto);
        }
        return interestedWebtoonList;
    }

    //////////////////////웹툰 리뷰 리스트////////////////////
    public List<ReviewedWebtoonResponse> reviewedWebtoonlist(Long usersId) {
        List<Review> list = reviewRepository.findByUsers_UsersIdIs(usersId);
        List<ReviewedWebtoonResponse> reviewedWebtoonList = new ArrayList<>();

        for (Review review : list) {
            Long reviewReplyCount = reviewReplyRepository.countByReview_ReviewId(review.getReviewId());
            Long reviewHeartCount = reviewHeartRepository.countByReview_ReviewId(review.getReviewId());

            ReviewedWebtoonResponse dto = new ReviewedWebtoonResponse(review.getUsers(), review.getWebtoon(), review.getReviewId(), review.getContent(), reviewReplyCount, reviewHeartCount);
            reviewedWebtoonList.add(dto);
        }
        return reviewedWebtoonList;
    }

    //////////////////////유저 정보 페이지 /////////////////////////
    public UserInfoResponse userinfo(Long currentUser, Long usersId) {
        Long followerCount = followRepository.countByFollowing_UsersId(usersId);
        Long followingCount = followRepository.countByFollower_UsersId(usersId);
        Long reviewedCount = reviewRepository.countByUsers_UsersId(usersId);
        Long ratedCount = starRepository.countByUsers_UsersId(usersId);
        Long interestedCount = interestRepository.countByUsers_UsersId(usersId);
        boolean isFollowing = followRepository.existsByFollowerAndFollowing(usersRepository.findById(currentUser).get(), usersRepository.findById(usersId).get());

        return new UserInfoResponse(usersRepository.findById(usersId).get(), isFollowing, ratedCount, reviewedCount, interestedCount, followerCount, followingCount);
    }


    //////////////////////팔로잉 리스트//////////////////////
    public List<FollowResponse> getFollowingList(Long usersId) {
        List<Follow> list = followRepository.findAllByFollower_UsersId(usersId);
        List<FollowResponse> followingList = new ArrayList<>();
        for (Follow follow : list) {
            FollowResponse dto = new FollowResponse(follow.getFollowing(), follow.getFollowId());
            followingList.add(dto);
        }
        return followingList;
    }

    //////////////////////팔로워 리스트//////////////////////
    public List<FollowResponse> getFollowerList(Long usersId) {
        List<Follow> list = followRepository.findAllByFollowing_UsersId(usersId);
        List<FollowResponse> followerList = new ArrayList<>();
        for (Follow follow : list) {
            FollowResponse dto = new FollowResponse(follow.getFollower(), follow.getFollowId());
            followerList.add(dto);
        }
        return followerList;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////
//MultipartFile을 받아서 Amazon S3에 업로드
    public String uploadFile(MultipartFile multipartFile) {
        try {
            String fileName = generateFileName(multipartFile.getOriginalFilename());
            File convertedFile = convertMultipartFileToFile(multipartFile);
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, convertedFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            convertedFile.delete();
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("프로필 사진 변경중 오류가 발생했습니다.", e);
        }
    }

    public String getAccessUrl(String fileName) {
        return s3Client.getUrl(bucketName, fileName).toString();
    }

    //업로드할 파일의 고유한 파일 이름을 생성
    private String generateFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID().toString() + extension;
    }

    //MultipartFile을 File객체로 변환
    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        try (OutputStream os = new FileOutputStream(convertedFile)) {
            os.write(file.getBytes());
        }
        return convertedFile;
    }


    //BoardImage 에서 삭제 & S3에서 삭제
    public void deleteBoardImage(long boardImgId) {
        try {
            BoardImage boardImage = boardImageRepository.findById(boardImgId).get();
            String imgStoredName = boardImage.getStoredName();
            boardImageRepository.delete(boardImage);
            deleteFile(imgStoredName);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    //Amazon S3에서 삭제
    public void deleteFile(String storedName) {
        try {
            s3Client.deleteObject(bucketName, storedName);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("S3 파일 삭제 중 오류", e);
        }
    }
}
