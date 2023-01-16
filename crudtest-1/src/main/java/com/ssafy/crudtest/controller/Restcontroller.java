package com.ssafy.crudtest.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import com.ssafy.crudtest.entity.Content;
import com.ssafy.crudtest.repository.ContentRepository;

import lombok.AllArgsConstructor;

@Tag(name = "crud test", description = "Rest API 테스트")
@RestController
@AllArgsConstructor
@RequestMapping("/content")
public class Restcontroller {

	ContentRepository contentRepository;
	@Tag(name= "crud test")
	@Operation(summary = "test hello", description = "hello api example")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK !!"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
			@ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
			@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
	})
	@GetMapping
	public List<Map<String, Object>> list() {
		List<Map<String, Object>> result = new ArrayList<>();
		contentRepository.findTop1000ByOrderByUidDesc().forEach(contentList -> {
			Map<String, Object> obj = new HashMap<>();
			obj.put("uid", contentList.getUid());
			obj.put("path", contentList.getPath());
			obj.put("title", contentList.getTitle());
			result.add(obj);
		});

		return result;
	}

	@Tag(name= "crud test")
	@Operation(summary = "test hello", description = "hello api example")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK !!"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
			@ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
			@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
	})
	@PostMapping
	public Map<String, String> post(@RequestPart("picture") MultipartFile pic, @RequestParam("title") String title,
			@RequestParam("password") String password) throws IOException {
		String path = System.getProperty("user.dir");
		System.out.println(path);

		File file = new File(path + "/src/main/resources/static/" + pic.getOriginalFilename());
		try {
			if (file.createNewFile()) {
				System.out.println("File created");
			} else {
				System.out.println("File already exists");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		pic.transferTo(file);
		contentRepository.save(Content.builder().password(password).path(file.getName()).title(title).build()).getUid();
		return Map.of("path", file.getName());

	}

	@Tag(name= "crud test")
	@Operation(summary = "test hello", description = "hello api example")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK !!"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
			@ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
			@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
	})
	@PutMapping("/{uid}")
	public Map<String, String> update(@PathVariable int uid, @RequestPart("picture") MultipartFile pic,
			@RequestParam("title") String title, @RequestParam("password") String password) throws IOException {
		Content content = contentRepository.findById(uid).get();
		if (!password.equals(content.getPassword())) {
			return Map.of("ERROR", "패스워드가 다릅니다.");
		}
		if (!pic.isEmpty()) {
			String path = System.getProperty("user.dir");
			File file = new File(path + "/src/main/resources/static/" + pic.getOriginalFilename());
			try {
				if (file.createNewFile()) {
					System.out.println("File created");
				} else {
					System.out.println("File already exists");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			pic.transferTo(file);
			content.setPath(file.getName());
		}

		contentRepository.save(content);
		return Map.of("path", content.getPath());
	}

	@Tag(name= "crud test")
	@Operation(summary = "test hello", description = "hello api example")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK !!"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
			@ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
			@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
	})
	@DeleteMapping("/{uid}")
	public void delete(@PathVariable int uid, @RequestBody Map<String, Object> body) {
		System.out.println(contentRepository.findById(uid).get().getPassword());
		if (body.get("password").toString().equals(contentRepository.findById(uid).get().getPassword())) {
			contentRepository.deleteById(uid);
		}
	}

}
