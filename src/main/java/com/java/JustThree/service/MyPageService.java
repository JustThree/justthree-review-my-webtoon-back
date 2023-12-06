package com.java.JustThree.service;

import com.java.JustThree.domain.Interest;
import com.java.JustThree.domain.Star;
import com.java.JustThree.domain.Users;
import com.java.JustThree.dto.InterestedWebtoonResponse;
import com.java.JustThree.dto.RatedWebtoonResponse;
import com.java.JustThree.repository.InterestRepository;
import com.java.JustThree.repository.StarRepository;
import com.java.JustThree.repository.UsersRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.tool.schema.spi.ScriptTargetOutput;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private final UsersRepository usersRepository;
    private final StarRepository starRepository;
    private final InterestRepository interestRepository;
    //회원 정보 수정 Update
    @Transactional
    public boolean updateUser(Users user,int usersId){
        boolean result = true;
        try{
            Users nuser = usersRepository.findById((long) usersId).get();
            nuser.setUsersEmail(user.getUsersEmail());
            nuser.setUsersPw(user.getUsersPw());
            nuser.setProfileUrl(user.getProfileUrl());
        } catch (Exception e){
            System.out.println("회원정보 업데이트 실패");
            result=false;
        }
        return result;
    }
    //평가 웹툰 리스트
    public List<RatedWebtoonResponse>ratedWebtoonlist(Long usersId){
        List<Star> list = starRepository.findByUsers_UsersId(usersId);
        List<RatedWebtoonResponse> ratedWebtoonList= new ArrayList<>();
        for (Star star : list) {
            RatedWebtoonResponse dto = new RatedWebtoonResponse(star.getWebtoon(), star.getStarVal());
            ratedWebtoonList.add(dto);
        }
        return ratedWebtoonList;
    }
    //관심 웹툰 리스트
    public List<InterestedWebtoonResponse>interestedWebtoonlist(Long usersId){
        List<Interest> list = interestRepository.findByUsers_UsersId(usersId);
        List<InterestedWebtoonResponse> interestedWebtoonList = new ArrayList<>();
        for(Interest interest : list){
            InterestedWebtoonResponse dto = new InterestedWebtoonResponse(interest.getWebtoon());
            interestedWebtoonList.add(dto);
        }
        return interestedWebtoonList;
    }
    //리뷰 웹툰 리스트

    //팔로워 리스트

    //팔로잉 리스트




}
