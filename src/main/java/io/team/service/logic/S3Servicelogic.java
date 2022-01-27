package io.team.service.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import io.team.jwt.JwtManager;
import io.team.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Servicelogic {
	
	private final AmazonS3 s3Client;
	
	@Autowired
	FileMapper fileMapper;
	
	@Autowired
	JwtManager jwtManager;
	
	@Value("${cloud.aws.s3.bucket}")
	public String bucket; // S3 버킷 이름

	public int upload(MultipartFile multipartFile, String originFileName, String token) throws IOException {
		
		File uploadFile = convert(multipartFile);
			
		return upload(uploadFile, originFileName, token);

	}

	// S3로 파일 업로드하기
	private int upload(File uploadFile, String originFileName, String token) {
		
		int mem_id = jwtManager.getIdFromToken(token);
		String fileName = "files/" + UUID.randomUUID() + uploadFile.getName(); // S3에 저장된 파일 이름
		String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
		fileMapper.uploadFile(mem_id, originFileName,fileName, uploadImageUrl);
		
		int img_id = fileMapper.findByFilename(fileName);
		
		removeNewFile(uploadFile);
		
		return img_id;
	}

	// S3로 업로드
	private String putS3(File uploadFile, String fileName) {

		s3Client.putObject(
				new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
		return s3Client.getUrl(bucket, fileName).toString();
	}

	// 로컬에 저장된 이미지 지우기
	private void removeNewFile(File targetFile) {

		if (targetFile.delete()) {
			log.info("File delete success");
			return;
		}
		log.info("File delete fail");
	}

	// 로컬에 파일 업로드 하기
	private File convert(MultipartFile file) throws IOException {

		File convFile = new File(file.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}
	
	public String findUrlById(int id) {
		String reusult= fileMapper.findUrlById(id);
		return reusult;
	}

	
	/*
	 * public String upload(MultipartFile multipartFile, String fileName, String
	 * dirName) throws IOException { File uploadFile =
	 * convert(multipartFile).orElseThrow(() -> new
	 * IllegalArgumentException("파일변환실패"));
	 * 
	 * fileName = dirName + fileName; String uploadImageUrl = putS3(uploadFile,
	 * fileName); removeNewFile(uploadFile); return uploadImageUrl; }
	 * 
	 * // S3로 파일 업로드하기 private String upload(File uploadFile, String dirName) {
	 * String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName();
	 * // S3에 저장된 파일 이름 String uploadImageUrl = putS3(uploadFile, fileName); // s3로
	 * 업로드 removeNewFile(uploadFile); return uploadImageUrl; }
	 * 
	 * // S3로 업로드 private String putS3(File uploadFile, String fileName) {
	 * s3Client.putObject( new PutObjectRequest(bucket, fileName, uploadFile)
	 * .withCannedAcl(CannedAccessControlList.PublicRead) ); return
	 * s3Client.getUrl(bucket, fileName).toString(); }
	 * 
	 * // 로컬에 저장된 이미지 지우기 private void removeNewFile(File targetFile) { if
	 * (targetFile.delete()) { log.info("File delete success"); return; }
	 * log.info("File delete fail"); }
	 * 
	 * // 로컬에 파일 업로드 하기 private Optional<File> convert(MultipartFile file) throws
	 * IOException { File convertFile = new File(file.getOriginalFilename());
	 * if(convertFile.createNewFile()) { try (FileOutputStream fos = new
	 * FileOutputStream(convertFile)) { fos.write(file.getBytes()); } return
	 * Optional.of(convertFile); } return Optional.empty(); }
	 */
}
