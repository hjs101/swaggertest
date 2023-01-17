package com.ssafy.crudtest.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "게시글")
public class Content {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "pk" , example = "1")
	private int uid;
	@Schema(description = "파일 경로" , example = "file.png")
	private String path;
	@Schema(description = "글 제목" , example = "title")
	private String title;
	@Schema(description = "비밀번호" , example = "password")
	private String password;
	
	@Builder
	public Content(String path, String title, String password) {
		super();
		this.path = path;
		this.title = title;
		this.password = password;
	}
	
}
