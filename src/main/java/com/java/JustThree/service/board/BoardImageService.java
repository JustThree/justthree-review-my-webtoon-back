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

    private final AmazonS3Client s3Client;
    private static String bucketName = "just-three";
    private final BoardImageRepository boardImageRepository;
    private final BoardRepository boardRepository;

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

    //S3에서 수정
    public String updateFile(Long boardId, MultipartFile[] mfList) {
        try {
            /*Board oldBoard = boardRepository.findById(boardId)
                    .orElseThrow(NoSuchElementException::new);*/

            List<BoardImage> boardImageList = boardImageRepository.findByBoard_BoardIdIs(boardId);

            for(BoardImage boardImg: boardImageList) {
                BoardImage oldBoardImage = boardImageRepository.findById(boardImg.getImgId()).get();
                //boardId 7이였던 boardImageId가 1~3인 storedName(기존파일)을 삭제
                deleteFile(oldBoardImage.getStoredName());
            }
            for(BoardImage boardImg: boardImageList){
                BoardImage oldBoardImage = boardImageRepository.findById(boardImg.getImgId()).get();
                for(MultipartFile mf: mfList){
                    //새로운 파일 업로드
                    String fileName = generateFileName(mf.getOriginalFilename());
                    File convertedFile = convertMultipartFileToFile(mf); //MultipartFile → File
                    s3Client.putObject(new PutObjectRequest(bucketName, fileName, convertedFile)
                            .withCannedAcl(CannedAccessControlList.PublicRead));
                    convertedFile.delete();
                    //새로운 파일 저장
                    String storedName = fileName;
                    String accessUrl = getAccessUrl(storedName);
                    oldBoardImage.updateFile(accessUrl, mf.getOriginalFilename(), storedName);
                    boardImageRepository.save(oldBoardImage);

                }
            }
            return "업로드 수정";
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    /*public String updateFile(Long boardImageId, MultipartFile mf) {
        try {
            BoardImage boardImage = boardImageRepository.findById(boardImageId)
                    .orElseThrow(() -> new NotFoundException("이미지를 찾을 수 없음"));

            //기존 파일 삭제
            deleteFile(boardImage.getStoredName());

            //새로운 파일 업로드
            String fileName = generateFileName(mf.getOriginalFilename());
            File convertedFile = convertMultipartFileToFile(mf); //MultipartFile → File
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, convertedFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            convertedFile.delete();
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }*/

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
