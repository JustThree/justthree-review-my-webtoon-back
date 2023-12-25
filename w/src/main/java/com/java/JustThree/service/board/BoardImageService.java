package com.java.JustThree.service.board;

import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.java.JustThree.domain.Board;
import com.java.JustThree.domain.BoardImage;
import com.java.JustThree.repository.board.BoardImageRepository;
import com.java.JustThree.repository.board.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardImageService {

    //AWS S3 관련
    private final AmazonS3Client s3Client;
    private static String bucketName = "just-three";

    private final BoardImageRepository boardImageRepository;

    //DB(BoardImage) 에 등록  & S3에서 업로드
    @Transactional
    public void saveBoardImage(Board newBoard, MultipartFile[] newMfList){
        try{
            for(MultipartFile mf: newMfList){
                String storedName = uploadFile(mf);
                String accessUrl = getAccessUrl(storedName);

                BoardImage boardImage = BoardImage.builder()
                        .board(newBoard)
                        .accessUrl(accessUrl)
                        .originName(mf.getOriginalFilename())
                        .storedName(storedName)
                        .build();

                boardImageRepository.save(boardImage);
            }
            log.info( "success");
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    //MultipartFile을 받아서 Amazon S3에 업로드
    public String uploadFile(MultipartFile mf) {
        try {
            String fileName = generateFileName(mf.getOriginalFilename());
            File convertedFile = convertMultipartFileToFile(mf); //MultipartFile → File
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, convertedFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            convertedFile.delete();
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
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
    //DB(BoardImage) 에서 삭제 & S3에서 삭제
    @Transactional
    public void deleteBoardImage(long boardImgId){
        try{
            BoardImage boardImage = boardImageRepository.findById(boardImgId).get();
            String imgStoredName = boardImage.getStoredName();
            boardImageRepository.delete(boardImage);
            deleteFile(imgStoredName);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
    //Amazon S3에서 삭제
    public void deleteFile(String storedName){
        try{
            s3Client.deleteObject(bucketName, storedName);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException("S3 파일 삭제 중 오류", e);
        }
    }
}
