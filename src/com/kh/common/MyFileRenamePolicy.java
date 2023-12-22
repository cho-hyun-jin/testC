package com.kh.common;

import java.io.File;

import com.oreilly.servlet.multipart.FileRenamePolicy;


	public class MyFileRenamePolicy implements FileRenamePolicy {

	@Override
	public File rename(File originFile) {
		// 기존의 파일을 매개변수로 전달받아서 (originFile) 파일명 수정 작업 후에 수정된 파일을 반환해주는 메소드

		// 원본파일명("aaa.jpg")
		String originName = originFile.getName();

		// 수정파일명 : 파일업로드 된 시간(년월일시분초)+5자리랜덤값 => 최대한 안겹치게함
		// 확장자 : 원본파일의 확장자를 그대로 사용

		// aaa.jpg -----> 2023121114374512345.jpg 요런식으로 만들겠다.
		// 1. 파일업로드 된 시간(년월일시분초)
		String currentTime = new java.text.SimpleDateFormat("yyyMMddHHmmss").format(new java.util.Date());
		// 파일에 이름기준으로 언제 업로드된건지 확인가능

		// 2. 5자리 랜덤값 => int ranNum;
		int ranNum = (int) ((Math.random() * 90000) + 10000); // 10000~99999 까지의 랜덤값 반환

		// 3. 원본파일 확장자 (String ext) 라는 변수에다가 확장자 저장
		String ext = originName.substring(originName.lastIndexOf(".")); // .jpg 문자가 짤라져 나온다.

		String changeName = currentTime + ranNum + ext;

		// 원본 파일을 수정된 파일명으로 적용시켜서 파일객체로 반환
		return new File(originFile.getParent(), changeName);
	}

}